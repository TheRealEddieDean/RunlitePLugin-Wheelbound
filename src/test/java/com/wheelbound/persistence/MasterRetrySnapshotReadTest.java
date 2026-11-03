package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

/** Reader-first schema migration fixtures; the schema-19 writer remains gated. */
public class MasterRetrySnapshotReadTest
{
    private final RunCodec codec = new RunCodec(new Gson());

    @Test public void candidateWriterRetainsMultipleHistoricalResponses() throws Exception
    {
        for (int count : new int[] {0, 1, 128, 513})
        {
            var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, count, 7);
            var before = source.pendingMasterSelection().orElseThrow();
            var creation = source.masterCreations().orElseThrow().receipts().get(before.transactionId());
            var after = before.append(before.transactionId(), count, before.currentStreamState(),
                new MasterSelectionProvenance.Step(new DrawCommitment(before.pool(), 0), "repeated"));
            var first = MasterRetryOperationProposal.restoreResponse(creation, creation.identity(),
                new UUID(99, 1), 5, count, after, MasterRetryContinuation.Status.PROPOSED);
            var next = MasterRetryOperation.prepare(source, first);
            var second = MasterRetryOperationProposal.restoreResponse(creation, creation.identity(),
                new UUID(99, 2), 6, after.drawCount(), after, MasterRetryContinuation.Status.SUSPENDED);
            var suspended = MasterRetryOperation.prepare(next, second);
            var terminal = MasterSelectionResolutionReceipt.prepare(new UUID(99, 3), 7, creation, after,
                new MasterSelectionProvenance.Step(new DrawCommitment(after.pool(),
                    after.pool().totalWeight() - 1), "terminal"));
            var resolved = MasterTaintTerminalOperation.prepare(suspended, terminal);
            var paused = LifecycleAdmission.prepare(resolved, first.identity(), new UUID(99, 4), 8,
                RunReceipt.Operation.PAUSE);
            var resumed = LifecycleAdmission.prepare(paused, first.identity(), new UUID(99, 5), 9,
                RunReceipt.Operation.RESUME);
            for (var head : new RunDocument[] {next, suspended, resolved, paused, resumed})
            {
                // Independent handcrafted fixture verifies fields as well as decode compatibility.
                var expected = bytes(payload(source, head), 20);
                if (expected.length > RunCodec.MAX_BYTES)
                {
                    assertEquals(513, count);
                    assertThrows(IllegalArgumentException.class, () -> codec.encodeRetryCandidate(head));
                    continue;
                }
                var encoded = codec.encodeRetryCandidate(head);
                assertEquals(new JsonParser().parse(new String(expected, StandardCharsets.UTF_8)),
                    new JsonParser().parse(new String(encoded, StandardCharsets.UTF_8)));
                var restored = codec.decode(encoded, first.identity());
                assertEquals(head.receipts(), restored.receipts());
                assertArrayEquals(encoded, codec.encodeRetryCandidate(restored));
                assertSame(restored, MasterRetryOperation.prepare(restored, first));
                if (head != next)
                { assertSame(restored, MasterRetryOperation.prepare(restored, second)); }
                assertEquals(head.masterSpins().orElseThrow().available(),
                    restored.masterSpins().orElseThrow().available());
                assertEquals(head.lifecycle().state(), restored.lifecycle().state());
                assertEquals(head.lifecycle().obligation(), restored.lifecycle().obligation());
                assertThrows(IllegalArgumentException.class, () -> codec.encode(restored));
            }
            assertEquals(5, source.lifecycle().revision());
            assertSame(before, source.pendingMasterSelection().orElseThrow());
        }
    }

    @Test public void candidateWriterMigratesSchema19WithoutInventingHistory() throws Exception
    {
        var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, 1, 7);
        var oldBytes = codec.encode(source);
        var oldHead = codec.decode(oldBytes, source.lifecycle().identity());
        var candidate = codec.encodeRetryCandidate(oldHead);
        assertEquals(20, new JsonParser().parse(new String(candidate, StandardCharsets.UTF_8))
            .getAsJsonObject().get("schemaVersion").getAsInt());
        var restored = codec.decode(candidate, source.lifecycle().identity());
        assertEquals(source.receipts(), restored.receipts());
        assertArrayEquals(oldBytes, codec.encode(restored));
        assertArrayEquals(candidate, codec.encodeRetryCandidate(restored));
    }

    @Test public void restoresOriginalResponsesAcrossTerminalAndPause() throws Exception
    {
        for (int count : new int[] {0, 1, 128, 513})
        for (boolean suspended : new boolean[] {false, true})
        {
            var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, count, 7);
            var before = source.pendingMasterSelection().orElseThrow();
            var creation = source.masterCreations().orElseThrow().receipts().get(before.transactionId());
            var after = suspended ? before : before.append(before.transactionId(), count,
                before.currentStreamState(), new MasterSelectionProvenance.Step(
                    new DrawCommitment(before.pool(), 0), "repeated"));
            var request = MasterRetryOperationProposal.restoreResponse(creation, creation.identity(),
                new UUID(98, 1), 5, count, after, suspended
                    ? MasterRetryContinuation.Status.SUSPENDED : MasterRetryContinuation.Status.PROPOSED);
            var next = MasterRetryOperation.prepare(source, request);
            var terminal = MasterSelectionResolutionReceipt.prepare(new UUID(98, 2), 6, creation, after,
                new MasterSelectionProvenance.Step(new DrawCommitment(after.pool(),
                    after.pool().totalWeight() - 1), "terminal"));
            var resolved = MasterTaintTerminalOperation.prepare(next, terminal);
            var paused = LifecycleAdmission.prepare(resolved, request.identity(), new UUID(98, 3), 7,
                RunReceipt.Operation.PAUSE);
            var resumed = LifecycleAdmission.prepare(paused, request.identity(), new UUID(98, 4), 8,
                RunReceipt.Operation.RESUME);
            for (var head : new RunDocument[] {next, resolved, paused, resumed})
            {
                var payload = payload(source, head);
                var snapshot = bytes(payload, 20);
                // The complete response and terminal journals repeat the transcript.
                // Long histories can exceed the aggregate snapshot budget even when
                // each individual transcript is valid. Preserve the reader's limit.
                if (snapshot.length > RunCodec.MAX_BYTES)
                {
                    assertEquals(513, count);
                    assertThrows(IOException.class, () -> codec.decode(snapshot, request.identity()));
                    continue;
                }
                var restored = codec.decode(snapshot, request.identity());
                assertEquals(head.receipts(), restored.receipts());
                assertEquals(head.lifecycle().revision(), restored.lifecycle().revision());
                assertEquals(head.lifecycle().state(), restored.lifecycle().state());
                assertEquals(head.lifecycle().obligation(), restored.lifecycle().obligation());
                assertEquals(head.masterSpins().orElseThrow().available(),
                    restored.masterSpins().orElseThrow().available());
                assertSame(request, request.validateRetained(restored));
                assertSame(restored, MasterRetryOperation.prepare(restored, request));
                assertThrows(IllegalArgumentException.class, () -> codec.encode(restored));
                assertThrows(IOException.class, () -> codec.decode(bytes(payload, 19), request.identity()));
            }
            assertEquals(5, source.lifecycle().revision());
            assertSame(before, source.pendingMasterSelection().orElseThrow());
        }
    }

    @Test public void rejectsCorruptEnvelopeAndMissingHistory() throws Exception
    {
        var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, 1, 7);
        var before = source.pendingMasterSelection().orElseThrow();
        var creation = source.masterCreations().orElseThrow().receipts().get(before.transactionId());
        var after = before.append(before.transactionId(), before.drawCount(), before.currentStreamState(),
            new MasterSelectionProvenance.Step(new DrawCommitment(before.pool(), 0), "next"));
        var request = MasterRetryOperationProposal.restoreResponse(creation, creation.identity(),
            new UUID(98, 5), 5, before.drawCount(), after, MasterRetryContinuation.Status.PROPOSED);
        var head = MasterRetryOperation.prepare(source, request);
        var valid = payload(source, head);
        for (String field : new String[] {"commandId", "beforeRevision", "afterRevision", "state", "obligation"})
        {
            var changed = valid.deepCopy();
            var receipt = changed.getAsJsonArray("receipts").get(0).getAsJsonObject();
            if (field.equals("commandId")) { receipt.addProperty(field, new UUID(98, 99).toString()); }
            else if (field.equals("state")) { receipt.addProperty(field, "PAUSED"); }
            else if (field.equals("obligation")) { receipt.addProperty(field, "PUNISHMENT_PENDING"); }
            else { receipt.addProperty(field, 0); }
            assertThrows(IOException.class, () -> codec.decode(bytes(changed, 20), request.identity()));
        }
        for (String field : new String[] {"masterCreations", "pendingMasterSelection"})
        {
            var changed = valid.deepCopy();
            changed.add(field, JsonNull.INSTANCE);
            assertThrows(IOException.class, () -> codec.decode(bytes(changed, 20), request.identity()));
        }
        var shortened = valid.deepCopy();
        shortened.add("pendingMasterSelection", MasterSelectionProvenanceJson.encode(request.before()));
        assertThrows(IOException.class, () -> codec.decode(bytes(shortened, 20), request.identity()));
        var unknown = valid.deepCopy();
        unknown.getAsJsonArray("receipts").get(0).getAsJsonObject().addProperty("extra", true);
        assertThrows(IOException.class, () -> codec.decode(bytes(unknown, 20), request.identity()));
        assertThrows(IOException.class, () -> codec.decode(bytes(valid, 21), request.identity()));
    }

    @Test public void schema19StillReadsWithoutInventingRetryHistory() throws Exception
    {
        var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, 1, 7);
        var encoded = codec.encode(source);
        assertEquals(19, new JsonParser().parse(new String(encoded, StandardCharsets.UTF_8))
            .getAsJsonObject().get("schemaVersion").getAsInt());
        var restored = codec.decode(encoded, source.lifecycle().identity());
        assertEquals(source.receipts(), restored.receipts());
        assertTrue(restored.receipts().stream().noneMatch(r -> r.masterRetry() != null));
    }

    private JsonObject payload(RunDocument source, RunDocument head)
    {
        var envelope = new JsonParser().parse(new String(codec.encode(source), StandardCharsets.UTF_8)).getAsJsonObject();
        var payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        var run = head.lifecycle();
        payload.addProperty("revision", run.revision());
        payload.addProperty("state", run.state().name());
        payload.addProperty("obligation", run.obligation().name());
        payload.add("pendingMasterSelection", head.pendingMasterSelection().isPresent()
            ? MasterSelectionProvenanceJson.encode(head.pendingMasterSelection().get()) : JsonNull.INSTANCE);
        payload.add("masterResolutions", head.masterResolutions().isPresent()
            ? MasterSelectionResolutionsJson.encode(head.masterResolutions().get(), run.revision()) : JsonNull.INSTANCE);
        payload.add("masterSpins", MasterSpinStateJson.encode(head.masterSpins().orElseThrow(), run.revision()));
        payload.add("currentMasterSpecial", head.currentMasterSpecial().isPresent()
            ? MasterCurrentSpecialObligationJson.encode(head.currentMasterSpecial().get(),
                head.masterResolutions().orElseThrow(), head.masterSpins().orElseThrow(), run) : JsonNull.INSTANCE);
        var receipts = new JsonArray();
        for (var receipt : head.receipts())
        {
            var item = new JsonObject();
            item.addProperty("commandId", receipt.commandId().toString());
            item.addProperty("operation", receipt.operation().name());
            item.addProperty("beforeRevision", receipt.beforeRevision());
            item.addProperty("afterRevision", receipt.afterRevision());
            item.addProperty("state", receipt.state().name());
            item.addProperty("obligation", receipt.obligation().name());
            if (receipt.masterRetry() != null)
            { item.add("masterRetry", MasterRetryOperationProposalJson.encode(receipt.masterRetry())); }
            if (receipt.taintTerminal() != null)
            { item.add("taintTerminal", MasterTaintTerminalResponseJson.encode(receipt.taintTerminal())); }
            receipts.add(item);
        }
        payload.add("receipts", receipts);
        return payload;
    }

    private byte[] bytes(JsonObject payload, int schema)
    {
        String text = payload.toString();
        var envelope = new JsonObject();
        envelope.addProperty("schemaVersion", schema);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return envelope.toString().getBytes(StandardCharsets.UTF_8);
    }
}

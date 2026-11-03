package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterSpecialCodecTest
{
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    private final RunCodec codec = new RunCodec(gson);

    private RunDocument terminal(int empties, long balance)
    {
        var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, empties, balance);
        var pending = source.pendingMasterSelection().orElseThrow();
        var creation = source.masterCreations().orElseThrow().receipts().get(pending.transactionId());
        var request = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5, creation, pending,
            new MasterSelectionProvenance.Step(new DrawCommitment(pending.pool(), pending.pool().totalWeight() - 1), "terminal"));
        return MasterTaintTerminalOperation.prepare(source, request);
    }

    private byte[] edit(RunDocument source, int schema, Consumer<JsonObject> change)
    {
        var envelope = new JsonParser().parse(new String(codec.encode(source), StandardCharsets.UTF_8)).getAsJsonObject();
        var payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        change.accept(payload);
        String text = gson.toJson(payload);
        envelope.addProperty("schemaVersion", schema);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return gson.toJson(envelope).getBytes(StandardCharsets.UTF_8);
    }

    @Test public void activePausedAndResumedRestoreExactOriginalResponse() throws Exception
    {
        for (int empties : new int[] {0, 1, 513})
        for (long balance : new long[] {1, 7, Long.MAX_VALUE})
        {
            var active = terminal(empties, balance);
            var identity = active.lifecycle().identity();
            var paused = LifecycleAdmission.prepare(active, identity, UUID.randomUUID(), 6, RunReceipt.Operation.PAUSE);
            var resumed = LifecycleAdmission.prepare(paused, identity, UUID.randomUUID(), 7, RunReceipt.Operation.RESUME);
            var request = active.receipts().get(0).taintTerminal().resolution();
            for (var head : List.of(active, paused, resumed))
            {
                byte[] bytes = codec.encode(head);
                var restored = codec.decode(bytes, identity);
                assertArrayEquals(bytes, codec.encode(restored));
                assertEquals(head.receipts(), restored.receipts());
                assertEquals(head.lifecycle().state(), restored.lifecycle().state());
                assertEquals(head.lifecycle().revision(), restored.lifecycle().revision());
                assertEquals(RunLifecycle.Obligation.PUNISHMENT_PENDING, restored.lifecycle().obligation());
                assertEquals(head.currentMasterSpecial().orElseThrow().obligation(),
                    restored.currentMasterSpecial().orElseThrow().obligation());
                assertEquals(balance, restored.masterSpins().orElseThrow().reservation().orElseThrow().spinsBefore());
                assertEquals(balance - 1, restored.masterSpins().orElseThrow().available());
                assertTrue(restored.pendingMasterSelection().isEmpty());
                assertSame(restored, MasterTaintTerminalOperation.prepare(restored, request));
                var changed = new MasterSelectionResolutionReceipt(UUID.randomUUID(), request.beforeRevision(),
                    request.creation(), request.resolved());
                assertThrows(IllegalArgumentException.class, () -> MasterTaintTerminalOperation.prepare(restored, changed));
            }
        }
    }

    @Test public void schema18PreservesAbsenceWithoutInferringCurrentSpecial() throws Exception
    {
        var head = MasterResolutionAggregateChecks.resolved(1);
        byte[] old = edit(head, 18, p -> p.remove("currentMasterSpecial"));
        byte[] original = old.clone();
        var restored = codec.decode(old, head.lifecycle().identity());
        assertTrue(restored.currentMasterSpecial().isEmpty());
        assertTrue(restored.masterSpins().isEmpty());
        assertEquals(head.masterResolutions().orElseThrow().receipts(), restored.masterResolutions().orElseThrow().receipts());
        assertTrue(codec.decode(codec.encode(restored), head.lifecycle().identity()).currentMasterSpecial().isEmpty());
        assertArrayEquals(original, old);
        assertThrows(IOException.class, () -> codec.decode(edit(head, 18, p -> {}), head.lifecycle().identity()));
        var taint = terminal(0, 7);
        assertThrows(IOException.class, () -> codec.decode(edit(taint, 18,
            p -> p.remove("currentMasterSpecial")), taint.lifecycle().identity()));
        var resolution = head.masterResolutions().orElseThrow().receipts().values().iterator().next();
        var reserved = MasterSpinAggregateChecks.attach(head, new MasterSpinState(1, head.lifecycle().identity(),
            resolution.afterRevision(), 7, Optional.of(MasterSpinAllocation.restore(resolution, 7))));
        var migrated = codec.decode(edit(reserved, 18, p -> p.remove("currentMasterSpecial")), head.lifecycle().identity());
        assertTrue(migrated.currentMasterSpecial().isEmpty());
        assertEquals(6, migrated.masterSpins().orElseThrow().available());
        assertEquals(resolution, migrated.masterSpins().orElseThrow().reservation().orElseThrow().resolution());
    }

    @Test public void explicitSacrificeContextRetainsStructureWithoutInventingRoute() throws Exception
    {
        var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.SACRIFICE, 1, 7);
        var pending = source.pendingMasterSelection().orElseThrow();
        var creation = source.masterCreations().orElseThrow().receipts().get(pending.transactionId());
        var request = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5, creation, pending,
            new MasterSelectionProvenance.Step(new DrawCommitment(pending.pool(), pending.pool().totalWeight() - 1), "terminal"));
        var prepared = MasterSpecialTerminalPreparation.prepare(source, request);
        var head = new RunDocument(new RunLifecycle(source.lifecycle().identity(), 6, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.SACRIFICE_ACQUISITION), source.catalogVersion(), List.of(),
            source.fatePoints(), source.inventory(), source.grandFate(), source.grandProgress(),
            source.grandJournal(), source.grandActivations(), source.grandCurrentAttempt(), Optional.empty(),
            source.masterCreations(), Optional.of(prepared.resolutions()), Optional.of(prepared.spins()),
            Optional.of(prepared.obligation()));
        byte[] bytes = codec.encode(head);
        var restored = codec.decode(bytes, head.lifecycle().identity());
        assertArrayEquals(bytes, codec.encode(restored));
        assertEquals(prepared.obligation(), restored.currentMasterSpecial().orElseThrow().obligation());
        assertEquals(6, restored.masterSpins().orElseThrow().available());
        assertTrue(restored.receipts().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> MasterTaintTerminalOperation.prepare(restored, request));
    }

    @Test public void malformedOrDisagreeingContextFailsWithoutChangingInput()
    {
        var head = terminal(1, 7);
        List<Consumer<JsonObject>> changes = List.of(
            p -> p.remove("currentMasterSpecial"),
            p -> p.add("currentMasterSpecial", JsonNull.INSTANCE),
            p -> p.add("currentMasterSpecial", new JsonArray()),
            p -> p.getAsJsonObject("currentMasterSpecial").addProperty("version", 2),
            p -> p.getAsJsonObject("currentMasterSpecial").addProperty("extra", true),
            p -> p.getAsJsonObject("currentMasterSpecial").getAsJsonObject("resolution")
                .addProperty("commandId", UUID.randomUUID().toString()),
            p -> p.add("masterResolutions", JsonNull.INSTANCE),
            p -> p.add("masterSpins", JsonNull.INSTANCE),
            p -> p.getAsJsonObject("masterSpins").add("reservation", JsonNull.INSTANCE),
            p -> p.addProperty("obligation", "NONE"),
            p -> p.getAsJsonArray("receipts").get(0).getAsJsonObject().remove("taintTerminal"),
            p -> p.getAsJsonArray("receipts").get(0).getAsJsonObject().addProperty("commandId", UUID.randomUUID().toString()),
            p -> p.getAsJsonArray("receipts").get(0).getAsJsonObject().addProperty("beforeRevision", 4),
            p -> p.getAsJsonArray("receipts").get(0).getAsJsonObject().addProperty("afterRevision", 7),
            p -> p.getAsJsonArray("receipts").get(0).getAsJsonObject().addProperty("state", "PAUSED"),
            p -> p.getAsJsonArray("receipts").get(0).getAsJsonObject().addProperty("obligation", "NONE"),
            p -> p.getAsJsonArray("receipts").get(0).getAsJsonObject().addProperty("operation", "PAUSE"),
            p -> p.getAsJsonArray("receipts").get(0).getAsJsonObject().getAsJsonObject("taintTerminal")
                .addProperty("spinsBefore", 8));
        for (var change : changes)
        {
            byte[] bytes = edit(head, RunCodec.SCHEMA, change);
            byte[] original = bytes.clone();
            assertThrows(IOException.class, () -> codec.decode(bytes, head.lifecycle().identity()));
            assertArrayEquals(original, bytes);
        }
    }
}

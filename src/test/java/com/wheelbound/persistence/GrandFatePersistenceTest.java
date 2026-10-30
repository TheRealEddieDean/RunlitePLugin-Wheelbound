package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class GrandFatePersistenceTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final Gson gson = new Gson().newBuilder().serializeNulls().create();
    private final RunCodec codec = new RunCodec(gson);
    private final RunIdentity id = new RunIdentity("grand-test", UUID.randomUUID());
    private final UUID command = UUID.randomUUID();

    private SealedGrandFate sealed(int ticket)
    { return new SealedGrandFate(id, command, GrandFatePool.atTicket(ticket)); }

    private RunDocument document(long revision, Optional<SealedGrandFate> evidence)
    {
        return new RunDocument(new RunLifecycle(id, revision, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), "v1", List.of(), Optional.empty(), Optional.empty(), evidence);
    }

    private RunDocument staged(String catalog, int ticket)
    {
        return new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.NOT_STARTED,
            RunLifecycle.Obligation.NONE), catalog, List.of(), Optional.empty(), Optional.empty(),
            Optional.of(sealed(ticket)));
    }

    @Test public void stagesOnceAndReplaysOnlyTheWholeInitialRequest() throws Exception
    {
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        assertEquals(sealed(0), store.stageGrandFate(staged("v1", 0)));
        byte[] original = Files.readAllBytes(store.snapshotPath());
        AtomicRunStore reopened = new AtomicRunStore(folder.getRoot().toPath(), codec, id,
            boundary -> { throw new AssertionError("Replay must not write"); });
        assertEquals(sealed(0), reopened.stageGrandFate(staged("v1", 0)));
        assertThrows(IOException.class, () -> reopened.stageGrandFate(staged("changed", 0)));
        assertThrows(IOException.class, () -> reopened.stageGrandFate(staged("v1", 1)));
        assertThrows(IllegalArgumentException.class,
            () -> reopened.stageGrandFate(document(0, Optional.of(sealed(0)))));
        RunDocument missingSeal = new RunDocument(staged("v1", 0).lifecycle(), "v1");
        assertThrows(IllegalArgumentException.class, () -> reopened.stageGrandFate(missingSeal));
        assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
        assertEquals(RunLifecycle.State.NOT_STARTED, reopened.load().get().lifecycle().state());
        assertFalse(Files.exists(store.pendingPath()));
    }

    @Test public void stagingCannotResealLegacyState() throws Exception
    {
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        store.write(document(0, Optional.empty()), -1);
        byte[] original = Files.readAllBytes(store.snapshotPath());
        assertThrows(IOException.class, () -> store.stageGrandFate(staged("v1", 0)));
        assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
    }

    @Test public void stagingCannotOverwriteLaterStateButSealRecoveryStillWorks() throws Exception
    {
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        store.stageGrandFate(staged("v1", 0));
        // Low-level snapshot fixture only: this is not an authorized activation workflow.
        store.write(document(1, Optional.of(sealed(0))), 0);
        byte[] original = Files.readAllBytes(store.snapshotPath());
        assertThrows(IOException.class, () -> store.stageGrandFate(staged("v1", 0)));
        assertEquals(sealed(0), store.committedGrandFate(sealed(0)).get());
        assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
    }

    @Test public void failedStagingRetainsPendingAndBlocksRetryAcrossRestart() throws Exception
    {
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id,
            boundary -> { if (boundary == AtomicRunStore.Boundary.TEMP_FORCED) { throw new IOException("failure"); } });
        assertThrows(IOException.class, () -> store.stageGrandFate(staged("v1", 0)));
        byte[] pending = Files.readAllBytes(store.pendingPath());
        assertFalse(Files.exists(store.snapshotPath()));
        assertThrows(IOException.class, () -> store.stageGrandFate(staged("v1", 0)));
        AtomicRunStore reopened = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        assertThrows(IOException.class, () -> reopened.stageGrandFate(staged("v1", 1)));
        assertArrayEquals(pending, Files.readAllBytes(store.pendingPath()));
    }

    @Test public void lostStagingAcknowledgementRecoversOriginalAfterRestart() throws Exception
    {
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id,
            boundary -> { if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("lost ack"); } });
        assertThrows(IOException.class, () -> store.stageGrandFate(staged("v1", 0)));
        assertThrows(IOException.class, () -> store.stageGrandFate(staged("v1", 0)));
        byte[] original = Files.readAllBytes(store.snapshotPath());
        AtomicRunStore reopened = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        assertEquals(sealed(0), reopened.stageGrandFate(staged("v1", 0)));
        assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
    }

    private JsonObject json(byte[] value)
    { return new JsonParser().parse(new String(value, StandardCharsets.UTF_8)).getAsJsonObject(); }

    private byte[] envelope(JsonObject payload, int schema)
    {
        if (schema < 10) { payload.remove("grandJournal"); }
        if (schema < 11) { payload.remove("grandActivations"); payload.remove("grandCurrentAttempt"); }
        String text = gson.toJson(payload);
        JsonObject result = new JsonObject();
        result.addProperty("schemaVersion", schema);
        result.addProperty("payload", text);
        result.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return gson.toJson(result).getBytes(StandardCharsets.UTF_8);
    }

    private JsonObject payload()
    { return json(json(codec.encode(document(0, Optional.of(sealed(0))))).get("payload")
        .getAsString().getBytes(StandardCharsets.UTF_8)); }

    @Test public void allOutcomesRoundTripWithoutSampling() throws Exception
    {
        for (int ticket = 0; ticket < 5; ticket++)
        {
            byte[] bytes = codec.encode(document(0, Optional.of(sealed(ticket))));
            RunDocument restored = codec.decode(bytes, id);
            assertEquals(sealed(ticket), restored.grandFate().get());
            assertArrayEquals(bytes, codec.encode(restored));
        }
    }

    @Test public void exactSealRecoveryRejectsChangedRequestsWithoutWriting() throws Exception
    {
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        assertFalse(store.committedGrandFate(sealed(0)).isPresent());
        store.write(document(0, Optional.of(sealed(0))), -1);
        byte[] original = Files.readAllBytes(store.snapshotPath());
        AtomicRunStore reopened = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        assertEquals(sealed(0), reopened.committedGrandFate(sealed(0)).get());
        assertThrows(IOException.class, () -> reopened.committedGrandFate(sealed(1)));
        assertThrows(IOException.class, () -> reopened.committedGrandFate(
            new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(0))));
        assertThrows(IllegalArgumentException.class, () -> reopened.committedGrandFate(
            new SealedGrandFate(new RunIdentity("other", id.runId()), command, GrandFatePool.atTicket(0))));
        assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
    }

    @Test public void sealRecoveryRetainsLegacyAbsenceAndRejectsOperationCollision() throws Exception
    {
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        store.write(document(0, Optional.empty()), -1);
        assertFalse(store.committedGrandFate(sealed(0)).isPresent());
        new LifecycleCommands(store).execute(id, command, 0, RunReceipt.Operation.PAUSE);
        assertThrows(IOException.class, () -> store.committedGrandFate(sealed(0)));
        assertFalse(store.load().get().grandFate().isPresent());
    }

    @Test public void sealRecoveryFailsClosedOnPendingAndCorruptSnapshots() throws Exception
    {
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        store.write(document(0, Optional.of(sealed(0))), -1);
        Files.write(store.pendingPath(), new byte[] {1});
        assertThrows(IOException.class, () -> store.committedGrandFate(sealed(0)));
        Files.delete(store.pendingPath());
        byte[] corrupt = new byte[] {1, 2, 3};
        Files.write(store.snapshotPath(), corrupt);
        assertThrows(IOException.class, () -> store.committedGrandFate(sealed(0)));
        assertArrayEquals(corrupt, Files.readAllBytes(store.snapshotPath()));
    }

    @Test public void schemasOneThroughSevenNeverInferDestiny() throws Exception
    {
        for (int schema = 1; schema <= 7; schema++)
        {
            JsonObject value = payload(); value.remove("grandFate"); value.remove("grandProgress");
            if (schema < 6) { value.remove("inventory"); }
            if (schema < 4) { value.remove("fatePoints"); }
            if (schema == 1) { value.remove("receipts"); }
            RunDocument restored = codec.decode(envelope(value, schema), id);
            assertFalse(restored.grandFate().isPresent());
            assertFalse(codec.decode(codec.encode(restored), id).grandFate().isPresent());
        }
    }

    @Test public void checksumValidInvalidEvidenceFailsClosed()
    {
        List<java.util.function.Consumer<JsonObject>> changes = List.of(
            p -> p.remove("grandFate"),
            p -> p.getAsJsonObject("grandFate").remove("commandId"),
            p -> p.getAsJsonObject("grandFate").addProperty("commandId", "bad"),
            p -> p.getAsJsonObject("grandFate").addProperty("goal", "invented"),
            p -> p.getAsJsonObject("grandFate").getAsJsonObject("draw").addProperty("ticket", 5),
            p -> p.getAsJsonObject("grandFate").getAsJsonObject("draw").addProperty("version", "future"),
            p -> p.getAsJsonObject("grandFate").getAsJsonObject("draw").getAsJsonArray("entries").remove(0));
        for (java.util.function.Consumer<JsonObject> change : changes)
        {
            JsonObject value = payload(); change.accept(value);
            byte[] bytes = envelope(value, 8); byte[] original = bytes.clone();
            assertThrows(IOException.class, () -> codec.decode(bytes, id));
            assertArrayEquals(original, bytes);
        }
        RunIdentity foreign = new RunIdentity("foreign", id.runId());
        assertThrows(IllegalArgumentException.class, () -> document(0, Optional.of(
            new SealedGrandFate(foreign, command, GrandFatePool.atTicket(0)))));
    }

    @Test public void storePreservesSealAcrossRestartAndLifecycleWrites() throws Exception
    {
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        store.write(document(0, Optional.of(sealed(0))), -1);
        byte[] original = Files.readAllBytes(store.snapshotPath());
        for (Optional<SealedGrandFate> changed : List.of(Optional.<SealedGrandFate>empty(),
            Optional.of(sealed(1)), Optional.of(new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(0)))))
        {
            assertThrows(IOException.class, () -> store.write(document(1, changed), 0));
            assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
            assertFalse(Files.exists(store.pendingPath()));
        }
        LifecycleCommands commands = new LifecycleCommands(store);
        assertThrows(IllegalArgumentException.class, () -> commands.execute(id, command, 0, RunReceipt.Operation.PAUSE));
        commands.execute(id, UUID.randomUUID(), 0, RunReceipt.Operation.PAUSE);
        commands.execute(id, UUID.randomUUID(), 1, RunReceipt.Operation.RESUME);
        AtomicRunStore reopened = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        assertEquals(sealed(0), reopened.load().get().grandFate().get());
    }

    @Test public void legacyExistingRunCannotBeRetrofittedWithDestiny() throws Exception
    {
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        store.write(document(0, Optional.empty()), -1);
        assertThrows(IOException.class, () -> store.write(document(1, Optional.of(sealed(0))), 0));
        store.write(document(1, Optional.empty()), 0);
        assertFalse(store.load().get().grandFate().isPresent());
    }

    @Test public void debitAndOrdinaryDrawRetainOriginalGrandCommitment() throws Exception
    {
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        RunDocument initial = document(0, Optional.of(sealed(4)));
        store.write(new RunDocument(initial.lifecycle(), "v1", List.of(),
            Optional.of(new FatePointLedger(100, List.of())), Optional.empty(), initial.grandFate()), -1);
        new DurableFatePointDebits(store).debit(id, UUID.randomUUID(), 0, "synthetic", "v1", 7);
        assertEquals(sealed(4), store.load().get().grandFate().get());
        WeightedPool pool = new WeightedPool("v1", List.of(new WeightedPool.Entry("ordinary", 1)));
        new DurableWheelDraws(store, bound -> 0).commit(id, UUID.randomUUID(), 1, pool);
        RunDocument restored = new AtomicRunStore(folder.getRoot().toPath(), codec, id).load().get();
        assertEquals(sealed(4), restored.grandFate().get());
        assertEquals(93, restored.fatePoints().get().balance());
        assertEquals(RunLifecycle.Obligation.OFFERS_COMMITTED, restored.lifecycle().obligation());
    }
}

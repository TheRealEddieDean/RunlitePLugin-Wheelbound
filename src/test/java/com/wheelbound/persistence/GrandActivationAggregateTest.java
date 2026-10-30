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

public class GrandActivationAggregateTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final Gson gson = new Gson();
    private final RunCodec codec = new RunCodec(gson);

    @Test public void structuralContracts() { GrandActivationAggregateChecks.main(new String[0]); }

    @Test public void fullPayloadRestartAndSchemaTenAbsence() throws Exception
    {
        for (int ticket = 0; ticket < 5; ticket++)
        {
            RunIdentity id = new RunIdentity("activation-codec", UUID.randomUUID());
            SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
            GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
            GrandActivationReceipt receipt = new GrandActivationReceipt(UUID.randomUUID(), id, 0,
                checklist, checklist.unfinished().iterator().next());
            GrandActivationHistory history = new GrandActivationHistory(seal, 1, List.of(receipt));
            RunDocument original = GrandActivationAggregateChecks.document(seal, 40, history, List.of());
            RunDocument restored = codec.decode(codec.encode(original), id);
            assertEquals(receipt, restored.grandActivations().get().replay(receipt, id, 40));
            JsonObject envelope = new JsonParser().parse(new String(codec.encode(original), StandardCharsets.UTF_8)).getAsJsonObject();
            JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
            payload.remove("grandActivations"); payload.remove("grandCurrentAttempt");
            String text = gson.newBuilder().serializeNulls().create().toJson(payload);
            envelope.addProperty("schemaVersion", 10);
            envelope.addProperty("payload", text);
            envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
            RunDocument legacy = codec.decode(gson.toJson(envelope).getBytes(StandardCharsets.UTF_8), id);
            assertFalse(legacy.grandActivations().isPresent());
            assertFalse(codec.decode(codec.encode(legacy), id).grandActivations().isPresent());
        }
    }

    @Test public void storePreservesEmptyHistoryAndRejectsAddDropAndReplacement() throws Exception
    {
        RunIdentity id = new RunIdentity("activation-store", UUID.randomUUID());
        SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(0));
        GrandActivationHistory empty = new GrandActivationHistory(seal, 0, List.of());
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        store.write(GrandActivationAggregateChecks.document(seal, 0, empty, List.of()), -1);
        LifecycleCommands commands = new LifecycleCommands(store);
        // Initial fixture is paused; ordinary resume/pause copies must retain explicit empty history.
        commands.execute(id, UUID.randomUUID(), 0, RunReceipt.Operation.RESUME);
        assertTrue(store.load().get().grandActivations().isPresent());
        byte[] original = Files.readAllBytes(store.snapshotPath());
        assertThrows(IOException.class, () -> store.write(
            GrandActivationAggregateChecks.document(seal, 2, null, List.of()), 1));
        GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
        GrandActivationReceipt receipt = new GrandActivationReceipt(UUID.randomUUID(), id, 0,
            checklist, checklist.unfinished().iterator().next());
        GrandActivationHistory replaced = new GrandActivationHistory(seal, 2, List.of(receipt));
        assertThrows(IOException.class, () -> store.write(
            GrandActivationAggregateChecks.document(seal, 2, replaced, List.of()), 1));
        assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
        RunIdentity legacyId = new RunIdentity("legacy-store", UUID.randomUUID());
        SealedGrandFate legacySeal = new SealedGrandFate(legacyId, UUID.randomUUID(), seal.commitment());
        AtomicRunStore legacy = new AtomicRunStore(folder.newFolder().toPath(), codec, legacyId);
        legacy.write(GrandActivationAggregateChecks.document(legacySeal, 0, null, List.of()), -1);
        assertThrows(IOException.class, () -> legacy.write(GrandActivationAggregateChecks.document(legacySeal, 1,
            new GrandActivationHistory(legacySeal, 1, List.of()), List.of()), 0));
        assertFalse(Files.exists(legacy.pendingPath()));
    }

    @Test public void populatedHistorySurvivesCommandCopiesAndRejectsPayloadReplacement() throws Exception
    {
        RunIdentity id = new RunIdentity("populated-history", UUID.randomUUID());
        SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(3));
        GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
        GrandFateChecklist.Objective objective = checklist.unfinished().iterator().next();
        GrandActivationReceipt activation = new GrandActivationReceipt(UUID.randomUUID(), id, 0, checklist, objective);
        GrandActivationHistory history = new GrandActivationHistory(seal, 2, List.of(activation));
        RunDocument fixture = new RunDocument(new RunLifecycle(id, 2, RunLifecycle.State.PAUSED,
            RunLifecycle.Obligation.NONE), "test", List.of(), Optional.of(new FatePointLedger(100, List.of())),
            Optional.empty(), Optional.of(seal), Optional.empty(), Optional.empty(), Optional.of(history));
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        // Restore fixture only; writing bytes grants no activation or evidence admission authority.
        Files.createDirectories(store.snapshotPath().getParent());
        Files.write(store.snapshotPath(), codec.encode(fixture));
        new LifecycleCommands(store).execute(id, UUID.randomUUID(), 2, RunReceipt.Operation.RESUME);
        new DurableFatePointDebits(store).debit(id, UUID.randomUUID(), 3, "fixture", "v1", 1);
        assertEquals(history.receipts(), store.load().get().grandActivations().get().receipts());
        byte[] original = Files.readAllBytes(store.snapshotPath());
        GrandFateChecklist.Objective other = checklist.unfinished().stream().filter(o -> o != objective).findFirst().get();
        GrandActivationReceipt changed = new GrandActivationReceipt(activation.commandId(), id, 0, checklist, other);
        RunDocument current = store.load().get();
        RunDocument replaced = new RunDocument(new RunLifecycle(id, 5, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), "test", List.of(), current.fatePoints(), current.inventory(),
            current.grandFate(), current.grandProgress(), current.grandJournal(),
            Optional.of(new GrandActivationHistory(seal, 5, List.of(changed))));
        assertThrows(IOException.class, () -> store.write(replaced, 4));
        assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
        new DurableWheelDraws(store, bound -> 0).commit(id, UUID.randomUUID(), 4,
            new WeightedPool("test", List.of(new WeightedPool.Entry("fixture", 1))));
        assertEquals(history.receipts(), new AtomicRunStore(folder.getRoot().toPath(), codec, id)
            .load().get().grandActivations().get().receipts());
    }

    @Test public void checksumValidActivationCorruptionPreservesOriginalBytes() throws Exception
    {
        RunIdentity id = new RunIdentity("activation-corruption", UUID.randomUUID());
        SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(0));
        GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
        GrandActivationReceipt receipt = new GrandActivationReceipt(UUID.randomUUID(), id, 0,
            checklist, checklist.unfinished().iterator().next());
        RunDocument fixture = GrandActivationAggregateChecks.document(seal, 2,
            new GrandActivationHistory(seal, 2, List.of(receipt)), List.of());
        List<java.util.function.Consumer<JsonObject>> corruptions = List.of(
            p -> p.remove("grandActivations"),
            p -> p.getAsJsonObject("grandActivations").addProperty("version", 2),
            p -> p.getAsJsonObject("grandActivations").addProperty("sealCommandId", UUID.randomUUID().toString()),
            p -> p.getAsJsonObject("grandActivations").getAsJsonArray("receipts").add(
                p.getAsJsonObject("grandActivations").getAsJsonArray("receipts").get(0)));
        for (java.util.function.Consumer<JsonObject> corrupt : corruptions)
        {
            JsonObject envelope = new JsonParser().parse(new String(codec.encode(fixture), StandardCharsets.UTF_8)).getAsJsonObject();
            JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
            corrupt.accept(payload);
            String text = gson.newBuilder().serializeNulls().create().toJson(payload);
            envelope.addProperty("payload", text);
            envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
            byte[] bytes = gson.toJson(envelope).getBytes(StandardCharsets.UTF_8);
            AtomicRunStore store = new AtomicRunStore(folder.newFolder().toPath(), codec, id);
            Files.createDirectories(store.snapshotPath().getParent());
            Files.write(store.snapshotPath(), bytes);
            assertThrows(IOException.class, store::load);
            assertArrayEquals(bytes, Files.readAllBytes(store.snapshotPath()));
            assertFalse(Files.exists(store.pendingPath()));
        }
    }
}

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

public class GrandCurrentAttemptAggregateTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    private final RunCodec codec = new RunCodec(gson);

    @Test public void structuralContracts() { GrandCurrentAttemptAggregateChecks.main(new String[0]); }

    private RunDocument fixture(int ticket, long head, RunLifecycle.State state)
    {
        RunIdentity id = new RunIdentity("current-codec", UUID.randomUUID());
        SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
        GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
        GrandActivationReceipt activation = new GrandActivationReceipt(UUID.randomUUID(), id, 0,
            checklist, checklist.unfinished().iterator().next());
        GrandActivationHistory history = new GrandActivationHistory(seal, head, List.of(activation));
        RunLifecycle lifecycle = new RunLifecycle(id, head, state, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        GrandCurrentAttempt attempt = GrandCurrentAttempt.restore(activation.commandId(), history, lifecycle, checklist);
        return new RunDocument(lifecycle, "test", List.of(), Optional.of(new FatePointLedger(100, List.of())),
            Optional.empty(), Optional.of(seal), Optional.empty(), Optional.empty(), Optional.of(history), Optional.of(attempt));
    }

    private byte[] edited(byte[] bytes, int schema, java.util.function.Consumer<JsonObject> edit)
    {
        JsonObject envelope = new JsonParser().parse(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        if (schema < 14) { payload.remove("pendingMasterSelection"); }
        if (schema < 15) { payload.remove("masterCreations"); }
        if (schema < 17) { payload.remove("masterResolutions"); }
        if (schema < 18) { payload.remove("masterSpins"); }
        edit.accept(payload);
        String text = gson.toJson(payload);
        envelope.addProperty("schemaVersion", schema);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return gson.toJson(envelope).getBytes(StandardCharsets.UTF_8);
    }

    @Test public void allGoalsRestartAtActivePausedAndMaximumHeadsAndLegacyIsAbsent() throws Exception
    {
        for (int ticket = 0; ticket < 5; ticket++)
        for (RunLifecycle.State state : List.of(RunLifecycle.State.ACTIVE, RunLifecycle.State.PAUSED))
        for (long head : new long[] {1, 40, Long.MAX_VALUE})
        {
            RunDocument before = fixture(ticket, head, state);
            RunIdentity id = before.lifecycle().identity();
            byte[] bytes = codec.encode(before);
            RunDocument restored = codec.decode(bytes, id);
            assertEquals(before.grandCurrentAttempt().get().activation(), restored.grandCurrentAttempt().get().activation());
            assertEquals(state, restored.grandCurrentAttempt().get().lifecycle().state());
            assertEquals(head, restored.grandCurrentAttempt().get().lifecycle().revision());
            assertArrayEquals(bytes, codec.encode(restored));
            RunDocument legacy = codec.decode(edited(bytes, 11, p -> p.remove("grandCurrentAttempt")), id);
            assertTrue(legacy.grandActivations().isPresent());
            assertFalse(legacy.grandCurrentAttempt().isPresent());
            assertFalse(codec.decode(codec.encode(legacy), id).grandCurrentAttempt().isPresent());
        }
    }

    @Test public void everyObjectiveAndRetainedPrefixSurvivesTextRestart() throws Exception
    {
        for (int ticket = 0; ticket < 5; ticket++)
        {
            RunDocument base = fixture(ticket, 1, RunLifecycle.State.ACTIVE);
            SealedGrandFate seal = base.grandFate().get();
            GrandFateChecklist initial = GrandFateChecklist.initial(seal);
            for (GrandFateChecklist.Objective objective : initial.unfinished())
            {
                List<GrandFateProgress.Evidence> evidence = new ArrayList<>();
                initial.unfinished().stream().filter(o -> o != objective).findFirst().ifPresent(other ->
                    evidence.add(new GrandFateProgress.Evidence(seal, other, UUID.randomUUID(), 1)));
                GrandFateProgress progress = new GrandFateProgress(seal, 3, evidence);
                GrandActivationReceipt activation = new GrandActivationReceipt(UUID.randomUUID(), seal.identity(), 2,
                    progress.checklist(), objective);
                GrandActivationHistory history = new GrandActivationHistory(seal, 3, List.of(activation));
                GrandCurrentAttempt attempt = GrandCurrentAttempt.restore(activation.commandId(), history,
                    activation.attempt().lifecycle(), progress.checklist());
                RunDocument document = GrandCurrentAttemptAggregateChecks.document(attempt.lifecycle(), seal, history, attempt, progress);
                RunDocument restored = codec.decode(codec.encode(document), seal.identity());
                assertEquals(activation, restored.grandCurrentAttempt().get().activation());
                assertEquals(progress.checklist().completed(), restored.grandProgress().get().checklist().completed());
            }
        }
    }

    @Test public void lifecycleAndDebitCopiesRetainAttemptAndDrawRemainsBlocked() throws Exception
    {
        RunDocument fixture = fixture(3, 1, RunLifecycle.State.ACTIVE);
        RunIdentity id = fixture.lifecycle().identity();
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        Files.createDirectories(store.snapshotPath().getParent());
        Files.write(store.snapshotPath(), codec.encode(fixture)); // Structural restore fixture, not activation admission.
        LifecycleCommands commands = new LifecycleCommands(store);
        commands.execute(id, UUID.randomUUID(), 1, RunReceipt.Operation.PAUSE);
        assertEquals(RunLifecycle.State.PAUSED, store.load().get().grandCurrentAttempt().get().lifecycle().state());
        commands.execute(id, UUID.randomUUID(), 2, RunReceipt.Operation.RESUME);
        new DurableFatePointDebits(store).debit(id, UUID.randomUUID(), 3, "fixture", "v1", 1);
        RunDocument restored = new AtomicRunStore(folder.getRoot().toPath(), codec, id).load().get();
        assertEquals(fixture.grandCurrentAttempt().get().activation(), restored.grandCurrentAttempt().get().activation());
        assertEquals(4, restored.grandCurrentAttempt().get().lifecycle().revision());
        byte[] original = Files.readAllBytes(store.snapshotPath());
        assertThrows(IllegalStateException.class, () -> new DurableWheelDraws(store, bound -> {
            throw new AssertionError("Must not draw during Grand attempt");
        }).commit(id, UUID.randomUUID(), 4, new WeightedPool("test", List.of(new WeightedPool.Entry("x", 1)))));
        RunLifecycle next = new RunLifecycle(id, 5, RunLifecycle.State.PAUSED, restored.lifecycle().obligation());
        List<RunReceipt> nextReceipts = new ArrayList<>(restored.receipts());
        nextReceipts.add(new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.PAUSE, 4, 5,
            next.state(), next.obligation()));
        RunDocument dropped = new RunDocument(next, "test", nextReceipts, restored.fatePoints(), restored.inventory(),
            restored.grandFate(), restored.grandProgress(), restored.grandJournal(), restored.grandActivations());
        assertThrows(IOException.class, () -> store.write(dropped, 4));
        assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
        GrandFateChecklist checklist = GrandFateChecklist.initial(restored.grandFate().get());
        GrandActivationReceipt prior = restored.grandCurrentAttempt().get().activation();
        GrandFateChecklist.Objective other = checklist.unfinished().stream()
            .filter(o -> o != prior.attempt().objective()).findFirst().get();
        GrandActivationReceipt replacement = new GrandActivationReceipt(prior.commandId(), id, 0, checklist, other);
        GrandActivationHistory replacedHistory = new GrandActivationHistory(restored.grandFate().get(), 5, List.of(replacement));
        GrandCurrentAttempt replacedAttempt = GrandCurrentAttempt.restore(replacement.commandId(), replacedHistory, next, checklist);
        RunDocument replaced = new RunDocument(next, "test", nextReceipts, restored.fatePoints(), restored.inventory(),
            restored.grandFate(), restored.grandProgress(), restored.grandJournal(), Optional.of(replacedHistory),
            Optional.of(replacedAttempt));
        assertThrows(IOException.class, () -> store.write(replaced, 4));
        assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
        // Same history and obligation, explicit legacy absence: an ordinary write cannot add activity.
        Files.write(store.snapshotPath(), codec.encode(new RunDocument(restored.lifecycle(), "test", restored.receipts(),
            restored.fatePoints(), restored.inventory(), restored.grandFate(), restored.grandProgress(),
            restored.grandJournal(), restored.grandActivations())));
        byte[] absent = Files.readAllBytes(store.snapshotPath());
        RunDocument added = new RunDocument(next, "test", nextReceipts, restored.fatePoints(), restored.inventory(),
            restored.grandFate(), restored.grandProgress(), restored.grandJournal(), restored.grandActivations(),
            restored.retainedGrandAttempt(next));
        assertThrows(IOException.class, () -> store.write(added, 4));
        assertArrayEquals(absent, Files.readAllBytes(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
    }

    @Test public void checksumValidCorruptionFailsWithoutChangingOriginal() throws Exception
    {
        RunDocument fixture = fixture(0, 1, RunLifecycle.State.ACTIVE);
        List<java.util.function.Consumer<JsonObject>> edits = List.of(
            p -> p.remove("grandCurrentAttempt"),
            p -> p.add("grandActivations", JsonNull.INSTANCE),
            p -> p.addProperty("state", "COMPLETED"),
            p -> p.getAsJsonObject("grandCurrentAttempt").addProperty("version", 2),
            p -> p.getAsJsonObject("grandCurrentAttempt").addProperty("unknown", true),
            p -> p.getAsJsonObject("grandCurrentAttempt").getAsJsonObject("activation")
                .addProperty("commandId", UUID.randomUUID().toString()));
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, fixture.lifecycle().identity());
        Files.createDirectories(store.snapshotPath().getParent());
        for (java.util.function.Consumer<JsonObject> edit : edits)
        {
            byte[] corrupt = edited(codec.encode(fixture), 12, edit);
            Files.write(store.snapshotPath(), corrupt);
            assertThrows(IOException.class, store::load);
            assertArrayEquals(corrupt, Files.readAllBytes(store.snapshotPath()));
            assertFalse(Files.exists(store.pendingPath()));
        }
    }
}

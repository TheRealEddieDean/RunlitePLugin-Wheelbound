package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Reader-first migration fixtures, never a production publication path. */
public class QuestFateRunCodecTest
{
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    private final RunCodec codec = new RunCodec(gson);
    private final QuestFateAssignment assignment = QuestFateAssignmentChecks.fixture();

    @Test public void restoresOriginalAssignmentAcrossActiveAndPausedHeads() throws Exception
    {
        for (boolean paused : new boolean[] {false, true})
        {
            var payload = payload(paused);
            var restored = codec.decode(envelope(payload, 22), assignment.identity());
            assertEquals(assignment, restored.currentQuestAssignment().get().assignment());
            assertEquals(assignment.originalWindow(), restored.currentQuestAssignment().get().assignment().originalWindow());
            assertEquals(paused ? RunLifecycle.State.PAUSED : RunLifecycle.State.ACTIVE, restored.lifecycle().state());
            assertEquals(assignment.allocation().spinsBefore(), restored.masterSpins().get().allotted());
            assertThrows(IllegalArgumentException.class, () -> codec.encode(restored));
            assertThrows(IllegalArgumentException.class, () -> codec.encodeRetryCandidate(restored));
        }
    }

    @Test public void preservesExplicitAbsenceAndRejectsSchemaConfusion() throws Exception
    {
        var payload = payload(false);
        payload.add("currentQuestAssignment", JsonNull.INSTANCE);
        assertFalse(codec.decode(envelope(payload, 22), assignment.identity()).currentQuestAssignment().isPresent());
        assertThrows(IOException.class, () -> codec.decode(envelope(payload, 21), assignment.identity()));
        payload.remove("currentQuestAssignment");
        assertFalse(codec.decode(envelope(payload, 21), assignment.identity()).currentQuestAssignment().isPresent());
        assertThrows(IOException.class, () -> codec.decode(envelope(payload, 22), assignment.identity()));
        assertThrows(IOException.class, () -> codec.decode(envelope(payload, 24), assignment.identity()));
    }

    @Test public void rejectsInconsistentCommitmentsAndPreservesInput()
    {
        rejects(p -> p.addProperty("currentQuestAssignment", true));
        rejects(p -> p.add("currentQuestAssignment", new JsonArray()));
        rejects(p -> p.add("masterSpins", JsonNull.INSTANCE));
        rejects(p -> p.getAsJsonObject("masterSpins").add("reservation", JsonNull.INSTANCE));
        rejects(p -> p.add("masterCreations", JsonNull.INSTANCE));
        rejects(p -> p.add("masterResolutions", JsonNull.INSTANCE));
        rejects(p -> p.getAsJsonObject("masterCreations").getAsJsonArray("receipts").remove(0));
        rejects(p -> p.getAsJsonObject("masterResolutions").getAsJsonArray("receipts").remove(0));
        rejects(p -> p.addProperty("catalogVersion", "foreign"));
        rejects(p -> p.addProperty("obligation", "NONE"));
        rejects(p -> p.getAsJsonObject("currentQuestAssignment").getAsJsonObject("provenance")
            .addProperty("spinsBefore", assignment.allocation().spinsBefore() + 1));
        rejects(p -> p.getAsJsonObject("currentQuestAssignment").getAsJsonObject("originalWindow")
            .addProperty("baseline", "COMPLETE"));
    }

    @Test public void legacyHeadsNeverInferAssignmentFromRetainedHistory() throws Exception
    {
        for (int schema : new int[] {19, 20, 21})
        {
            var payload = payload(false);
            payload.remove("currentQuestAssignment");
            if (schema < 21)
            {
                payload.remove("fateCards");
                payload.remove("cardPurchases");
            }
            var restored = codec.decode(envelope(payload, schema), assignment.identity());
            assertFalse(restored.currentQuestAssignment().isPresent());
            assertFalse(restored.questCompletionHistory().isPresent());
            assertTrue(restored.masterSpins().get().reservation().isPresent());
            // Even explicit null is a foreign field before schema 22.
            payload.add("currentQuestAssignment", JsonNull.INSTANCE);
            byte[] original = envelope(payload, schema);
            byte[] copy = original.clone();
            assertThrows(IOException.class, () -> codec.decode(original, assignment.identity()));
            assertArrayEquals(copy, original);
        }
    }

    @Test public void schema23DistinguishesUnavailableEmptyAndHistoricalCompletion() throws Exception
    {
        var payload = payload(false);
        assertFalse(codec.decode(envelope(payload, 22), assignment.identity()).questCompletionHistory().isPresent());
        payload.add("questCompletionHistory", JsonNull.INSTANCE);
        assertFalse(codec.decode(envelope(payload, 23), assignment.identity()).questCompletionHistory().isPresent());
        assertThrows(IOException.class, () -> codec.decode(envelope(payload, 22), assignment.identity()));
        var history = new JsonObject();
        history.addProperty("version", 1);
        history.add("history", QuestFateCompletionHistoryCodec.encode(new QuestFateCompletionHistory(
            new QuestFateCompletionJournal(assignment.identity(), java.util.List.of()), java.util.List.of())));
        history.add("accounting", new JsonArray());
        payload.add("questCompletionHistory", history);
        var empty = codec.decode(envelope(payload, 23), assignment.identity());
        assertTrue(empty.questCompletionHistory().isPresent());
        assertTrue(empty.questCompletionHistory().get().journal().receipts().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> codec.encode(empty));
        assertThrows(IllegalArgumentException.class, () -> codec.encodeRetryCandidate(empty));

        var context = empty.currentQuestAssignment().get();
        var receipt = new QuestFateCompletionReceipt(context, assignment.originalWindow(),
            context.lifecycle().revision(), 1, java.util.UUID.randomUUID(), "quests", "completion");
        history.add("history", QuestFateCompletionHistoryCodec.encode(new QuestFateCompletionHistory(
            new QuestFateCompletionJournal(assignment.identity(), java.util.List.of(receipt)), java.util.List.of(context))));
        var accounting = new JsonObject();
        accounting.add("resolutions", payload.get("masterResolutions").deepCopy());
        accounting.add("spins", payload.get("masterSpins").deepCopy());
        history.getAsJsonArray("accounting").add(accounting);
        // A completed historical assignment does not require today's reservation.
        payload.add("currentQuestAssignment", JsonNull.INSTANCE);
        payload.add("masterSpins", JsonNull.INSTANCE);
        var restored = codec.decode(envelope(payload, 23), assignment.identity());
        assertEquals(java.util.List.of(receipt), restored.questCompletionHistory().get().journal().receipts());
        assertFalse(restored.masterSpins().isPresent());
        for (Consumer<JsonObject> mutation : java.util.List.<Consumer<JsonObject>>of(
            p -> p.remove("questCompletionHistory"),
            p -> p.getAsJsonObject("questCompletionHistory").addProperty("version", 2),
            p -> p.addProperty("questCompletionHistory", false),
            p -> p.getAsJsonObject("questCompletionHistory").addProperty("extra", true),
            p -> p.getAsJsonObject("questCompletionHistory").add("accounting", new JsonArray()),
            p -> p.getAsJsonObject("questCompletionHistory").getAsJsonArray("accounting")
                .get(0).getAsJsonObject().add("spins", JsonNull.INSTANCE),
            p -> p.add("masterResolutions", JsonNull.INSTANCE),
            p -> p.addProperty("revision", assignment.creationRevision() - 1)))
        {
            var changed = payload.deepCopy();
            mutation.accept(changed);
            byte[] bytes = envelope(changed, 23);
            byte[] original = bytes.clone();
            assertThrows(IOException.class, () -> codec.decode(bytes, assignment.identity()));
            assertArrayEquals(original, bytes);
        }
    }

    private JsonObject payload(boolean paused)
    {
        var lifecycle = new RunLifecycle(assignment.identity(), assignment.creationRevision() + (paused ? 1 : 0),
            paused ? RunLifecycle.State.PAUSED : RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        var resolution = assignment.provenance().resolution();
        var base = new RunDocument(lifecycle, resolution.resolved().catalogVersion());
        var envelope = new JsonParser().parse(new String(codec.encode(base), StandardCharsets.UTF_8)).getAsJsonObject();
        var payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        payload.add("fateCards", JsonNull.INSTANCE);
        payload.add("cardPurchases", JsonNull.INSTANCE);
        payload.add("masterCreations", MasterSelectionCreationJournalJson.encode(new MasterSelectionCreationJournal(
            assignment.identity(), lifecycle.revision(), java.util.List.of(resolution.creation())), lifecycle.revision()));
        payload.add("masterResolutions", MasterSelectionResolutionsJson.encode(new MasterSelectionResolutions(
            assignment.identity(), lifecycle.revision(), java.util.List.of(resolution)), lifecycle.revision()));
        payload.add("masterSpins", MasterSpinStateJson.encode(new MasterSpinState(1, assignment.identity(),
            lifecycle.revision(), assignment.allocation().spinsBefore(), java.util.Optional.of(assignment.allocation())), lifecycle.revision()));
        payload.add("currentQuestAssignment", QuestFateAssignmentCodec.encode(assignment));
        return payload;
    }

    private byte[] envelope(JsonObject payload, int schema)
    {
        String text = gson.toJson(payload);
        var value = new JsonObject();
        value.addProperty("schemaVersion", schema);
        value.addProperty("payload", text);
        value.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return gson.toJson(value).getBytes(StandardCharsets.UTF_8);
    }

    private void rejects(Consumer<JsonObject> mutation)
    {
        var payload = payload(false);
        mutation.accept(payload);
        byte[] original = envelope(payload, 22);
        byte[] copy = original.clone();
        assertThrows(IOException.class, () -> codec.decode(original, assignment.identity()));
        assertArrayEquals(copy, original);
    }
}

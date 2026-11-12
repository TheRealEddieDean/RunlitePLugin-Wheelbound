package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuestFateCompletionHistoryCodecTest
{
    @Test public void roundTripsExplicitEmptyAndRetainedHistoricalHistory()
    {
        var assignment = QuestFateAssignmentChecks.fixture();
        long revision = assignment.creationRevision() + 2;
        var resolutions = new MasterSelectionResolutions(assignment.identity(), revision,
            List.of(assignment.provenance().resolution()));
        var spins = new MasterSpinState(MasterSpinState.VERSION, assignment.identity(), revision,
            assignment.allocation().spinsBefore(), Optional.of(assignment.allocation()));
        var context = QuestFateCurrentAssignment.restore(assignment, resolutions, spins,
            new RunLifecycle(assignment.identity(), revision, RunLifecycle.State.ACTIVE,
                RunLifecycle.Obligation.ACTIVE_OBJECTIVE));
        var receipt = new QuestFateCompletionReceipt(context, assignment.originalWindow(), revision,
            1, UUID.randomUUID(), "quests", "completion");
        var history = new QuestFateCompletionHistory(new QuestFateCompletionJournal(
            assignment.identity(), List.of(receipt)), List.of(context));
        var encoded = QuestFateCompletionHistoryCodec.encode(history);
        var parsed = new JsonParser().parse(new Gson().toJson(encoded)).getAsJsonObject();
        var restored = QuestFateCompletionHistoryCodec.decode(parsed, assignment.identity(),
            List.of(resolutions), List.of(spins));
        assertEquals(List.of(receipt), restored.journal().receipts());
        assertEquals(encoded, QuestFateCompletionHistoryCodec.encode(restored));
        Consumer<Consumer<JsonObject>> mutate = edit -> {
            var copy = parsed.deepCopy();
            edit.accept(copy);
            rejects(() -> QuestFateCompletionHistoryCodec.decode(copy, assignment.identity(),
                List.of(resolutions), List.of(spins)));
        };
        for (String field : encoded.keySet())
        {
            mutate.accept(v -> v.remove(field));
            mutate.accept(v -> v.add(field, JsonNull.INSTANCE));
        }
        mutate.accept(v -> v.addProperty("version", 2));
        mutate.accept(v -> v.addProperty("version", "1"));
        mutate.accept(v -> v.addProperty("extra", true));
        mutate.accept(v -> v.add("journal", new JsonArray()));
        mutate.accept(v -> v.add("contexts", new JsonObject()));
        mutate.accept(v -> v.getAsJsonArray("contexts").set(0, JsonNull.INSTANCE));
        mutate.accept(v -> v.getAsJsonObject("journal").add("receipts", new JsonArray()));
        mutate.accept(v -> v.getAsJsonArray("contexts").get(0).getAsJsonObject()
            .addProperty("revision", revision - 1));
        rejects(() -> QuestFateCompletionHistoryCodec.decode(parsed, assignment.identity(),
            List.of(), List.of(spins)));
        rejects(() -> QuestFateCompletionHistoryCodec.decode(parsed, assignment.identity(),
            List.of(resolutions), List.of()));
        rejects(() -> QuestFateCompletionHistoryCodec.decode(parsed, assignment.identity(),
            List.of(resolutions), List.of(new MasterSpinState(MasterSpinState.VERSION,
                assignment.identity(), revision, spins.allotted(), Optional.empty()))));
        var empty = new QuestFateCompletionHistory(new QuestFateCompletionJournal(
            assignment.identity(), List.of()), List.of());
        var emptyJson = QuestFateCompletionHistoryCodec.encode(empty);
        assertTrue(QuestFateCompletionHistoryCodec.decode(emptyJson, assignment.identity(),
            List.of(), List.of()).contexts().isEmpty());
        rejects(() -> QuestFateCompletionHistoryCodec.decode(emptyJson, assignment.identity(),
            List.of(resolutions), List.of(spins)));
        assertEquals(encoded, parsed);
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); fail("Invalid completion history accepted"); }
        catch (IllegalArgumentException | NullPointerException expected) { }
    }
}

package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuestFateCompletionJournalCodecTest
{
    @Test public void roundTripsOrderedHistoryWithSeparateHistoricalContexts()
    {
        var first = context(QuestFateAssignmentChecks.fixture());
        var second = otherContext(first.assignment());
        var receipts = List.of(receipt(first, "one"), receipt(second, "two"));
        var journal = new QuestFateCompletionJournal(first.assignment().identity(), receipts);
        var json = QuestFateCompletionJournalCodec.encode(journal);
        var parsed = new JsonParser().parse(new Gson().toJson(json)).getAsJsonObject();
        var restored = QuestFateCompletionJournalCodec.decode(parsed, journal.identity(), List.of(first, second));
        assertEquals(receipts, restored.receipts());
        assertEquals(json, QuestFateCompletionJournalCodec.encode(restored));
        rejects(() -> QuestFateCompletionJournalCodec.decode(parsed, journal.identity(), List.of(second, first)));
        rejects(() -> QuestFateCompletionJournalCodec.decode(parsed, journal.identity(), List.of(first)));
        var empty = new QuestFateCompletionJournal(journal.identity(), List.of());
        assertTrue(QuestFateCompletionJournalCodec.decode(QuestFateCompletionJournalCodec.encode(empty),
            journal.identity(), List.of()).receipts().isEmpty());
        rejects(() -> QuestFateCompletionJournalCodec.decode(QuestFateCompletionJournalCodec.encode(empty),
            journal.identity(), List.of(first)));
    }

    @Test public void rejectsMalformedIdentityContextsAndHistoryCollisions()
    {
        var current = context(QuestFateAssignmentChecks.fixture());
        var receipt = receipt(current, "one");
        var identity = current.assignment().identity();
        var valid = QuestFateCompletionJournalCodec.encode(new QuestFateCompletionJournal(identity, List.of(receipt)));
        Consumer<Consumer<JsonObject>> mutate = edit -> {
            var copy = valid.deepCopy();
            edit.accept(copy);
            rejects(() -> QuestFateCompletionJournalCodec.decode(copy, identity, List.of(current)));
        };
        for (String field : valid.keySet())
        {
            mutate.accept(v -> v.remove(field));
            mutate.accept(v -> v.add(field, JsonNull.INSTANCE));
        }
        mutate.accept(v -> v.addProperty("extra", true));
        mutate.accept(v -> v.addProperty("version", 2));
        mutate.accept(v -> v.addProperty("version", "1"));
        mutate.accept(v -> v.addProperty("accountKey", "foreign"));
        mutate.accept(v -> v.addProperty("runId", UUID.randomUUID().toString()));
        mutate.accept(v -> v.add("receipts", new JsonObject()));
        mutate.accept(v -> v.getAsJsonArray("receipts").set(0, JsonNull.INSTANCE));
        var duplicated = valid.deepCopy();
        duplicated.getAsJsonArray("receipts").add(QuestFateCompletionReceiptCodec.encode(receipt));
        rejects(() -> QuestFateCompletionJournalCodec.decode(duplicated, identity, List.of(current, current)));
        // Individually valid receipts may still collide at journal scope.
        var second = otherContext(current.assignment());
        for (var collision : List.of(
            receipt(current, "different-event"),
            new QuestFateCompletionReceipt(second, second.assignment().originalWindow(),
                second.lifecycle().revision(), 1, receipt.commandId(), "quests", "different-event"),
            receipt(second, "one")))
        {
            var copy = valid.deepCopy();
            copy.getAsJsonArray("receipts").add(QuestFateCompletionReceiptCodec.encode(collision));
            var retained = collision.assignment().equals(current.assignment()) ? current : second;
            rejects(() -> QuestFateCompletionJournalCodec.decode(copy, identity, List.of(current, retained)));
        }
        assertEquals(valid, QuestFateCompletionJournalCodec.encode(new QuestFateCompletionJournal(identity, List.of(receipt))));
    }

    private static QuestFateCompletionReceipt receipt(QuestFateCurrentAssignment current, String event)
    {
        return new QuestFateCompletionReceipt(current, current.assignment().originalWindow(),
            current.lifecycle().revision(), 1, UUID.randomUUID(), "quests", event);
    }

    private static QuestFateCurrentAssignment otherContext(QuestFateAssignment assignment)
    {
        var original = assignment.originalWindow();
        var window = new QuestFateObservationWindow(new RunLifecycle(assignment.identity(),
            assignment.creationRevision(), RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE),
            UUID.randomUUID(), original.sessionId(), original.windowId(), original.baselinePosition(),
            original.selection(), original.baseline());
        return context(new QuestFateAssignment(assignment.provenance(), assignment.allocation(), window));
    }

    private static QuestFateCurrentAssignment context(QuestFateAssignment assignment)
    {
        long revision = assignment.creationRevision() + 1;
        return QuestFateCurrentAssignment.restore(assignment,
            new MasterSelectionResolutions(assignment.identity(), revision, List.of(assignment.provenance().resolution())),
            new MasterSpinState(MasterSpinState.VERSION, assignment.identity(), revision,
                assignment.allocation().spinsBefore(), Optional.of(assignment.allocation())),
            new RunLifecycle(assignment.identity(), revision, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE));
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); fail("Invalid journal accepted"); }
        catch (IllegalArgumentException | NullPointerException expected) { }
    }
}

package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuestFateCompletionReceiptCodecTest
{
    @Test public void roundTripsOriginalAndRefreshedWindowsAtLongBoundary()
    {
        var assignment = QuestFateAssignmentChecks.fixture();
        long revision = assignment.creationRevision() + 1;
        var current = context(assignment, revision, RunLifecycle.State.ACTIVE);
        var refreshed = new QuestFateObservationWindow(current.lifecycle(), assignment.assignmentId(),
            UUID.randomUUID(), UUID.randomUUID(), 10,
            assignment.provenance().reward().selection(), QuestFateCompletion.QuestState.IN_PROGRESS);
        for (var window : List.of(assignment.originalWindow(), refreshed))
        {
            var original = new QuestFateCompletionReceipt(current, window, revision,
                Long.MAX_VALUE, UUID.randomUUID(), "quests", "event-1");
            var json = QuestFateCompletionReceiptCodec.encode(original);
            var parsed = new JsonParser().parse(new Gson().toJson(json)).getAsJsonObject();
            var restored = QuestFateCompletionReceiptCodec.decode(parsed, current);
            assertEquals(original, restored);
            assertEquals(original.hashCode(), restored.hashCode());
            assertEquals(json, QuestFateCompletionReceiptCodec.encode(restored));
            assertSame(assignment, restored.assignment());
            assertEquals(window, restored.window());
            rejects(() -> QuestFateCompletionReceiptCodec.decode(parsed,
                context(assignment, revision + 1, RunLifecycle.State.ACTIVE)));
            rejects(() -> QuestFateCompletionReceiptCodec.decode(parsed,
                context(assignment, revision, RunLifecycle.State.PAUSED)));
        }
    }

    @Test public void rejectsMalformedPayloadsAndChangedRetainedCommitment()
    {
        var assignment = QuestFateAssignmentChecks.fixture();
        long revision = assignment.creationRevision() + 1;
        var current = context(assignment, revision, RunLifecycle.State.ACTIVE);
        var receipt = new QuestFateCompletionReceipt(current, assignment.originalWindow(), revision,
            1, UUID.randomUUID(), "quests", "event-1");
        var valid = QuestFateCompletionReceiptCodec.encode(receipt);
        Consumer<Consumer<JsonObject>> mutate = edit -> {
            var copy = valid.deepCopy();
            edit.accept(copy);
            rejects(() -> QuestFateCompletionReceiptCodec.decode(copy, current));
        };
        for (String field : valid.keySet())
        {
            mutate.accept(v -> v.remove(field));
            mutate.accept(v -> v.add(field, JsonNull.INSTANCE));
            mutate.accept(v -> v.add(field, new JsonArray()));
        }
        mutate.accept(v -> v.addProperty("extra", 1));
        mutate.accept(v -> v.addProperty("version", 2));
        mutate.accept(v -> v.addProperty("version", "1"));
        mutate.accept(v -> v.addProperty("observedPosition", 0));
        mutate.accept(v -> v.addProperty("observedPosition", "1"));
        mutate.accept(v -> v.addProperty("expectedRevision", revision + 1));
        mutate.accept(v -> v.addProperty("commandId", "1-1-1-1-1"));
        mutate.accept(v -> v.addProperty("eventKey", " event-1"));
        mutate.accept(v -> v.getAsJsonObject("window").addProperty("baseline", "COMPLETE"));
        mutate.accept(v -> v.getAsJsonObject("window").addProperty("assignmentId", UUID.randomUUID().toString()));
        mutate.accept(v -> v.getAsJsonObject("assignment").getAsJsonObject("provenance")
            .addProperty("finalStreamState", "changed-state"));
        rejects(() -> QuestFateCompletionReceiptCodec.decode(valid, null));
        rejects(() -> QuestFateCompletionReceiptCodec.encode(null));
        assertEquals(valid, QuestFateCompletionReceiptCodec.encode(receipt));
    }

    private static QuestFateCurrentAssignment context(QuestFateAssignment assignment,
        long revision, RunLifecycle.State state)
    {
        return QuestFateCurrentAssignment.restore(assignment,
            new MasterSelectionResolutions(assignment.identity(), revision,
                List.of(assignment.provenance().resolution())),
            new MasterSpinState(MasterSpinState.VERSION, assignment.identity(), revision,
                assignment.allocation().spinsBefore(), Optional.of(assignment.allocation())),
            new RunLifecycle(assignment.identity(), revision, state, RunLifecycle.Obligation.ACTIVE_OBJECTIVE));
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); fail("Invalid receipt accepted"); }
        catch (IllegalArgumentException | NullPointerException expected) { }
    }
}

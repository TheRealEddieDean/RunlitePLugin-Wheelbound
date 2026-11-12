package com.wheelbound.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Supplied-value receipt checks, not client authentication or durable replay tests. */
public final class QuestFateCompletionReceiptChecks
{
    public static void main(String[] args)
    {
        var assignment = QuestFateAssignmentChecks.fixture();
        long revision = assignment.creationRevision() + 1;
        var current = current(assignment, revision, RunLifecycle.State.ACTIVE);
        var window = assignment.originalWindow();
        var command = UUID.randomUUID();
        var receipt = receipt(current, window, revision, 1, command, "quests", "event-1");
        var replay = receipt(current, window, revision, 1, command, "quests", "event-1");
        check(receipt.equals(replay) && replay.equals(receipt) && receipt.hashCode() == replay.hashCode());
        check(receipt.assignment() == assignment && receipt.window() == window
            && receipt.expectedRevision() == revision && receipt.observedPosition() == 1
            && receipt.commandId().equals(command) && receipt.sourceNamespace().equals("quests")
            && receipt.eventKey().equals("event-1"));
        receipt.requireCurrent(current, revision, window.sessionId(), window.windowId());
        receipt.requireCurrent(current, revision, window.sessionId(), window.windowId());
        check(assignment.originalWindow() == window && assignment.allocation().spinsBefore() == 5);

        var changedPosition = receipt(current, window, revision, Long.MAX_VALUE, command, "quests", "event-1");
        var changedCommand = receipt(current, window, revision, 1, UUID.randomUUID(), "quests", "event-1");
        check(!receipt.equals(changedPosition) && receipt.sameSourceEvent(changedPosition));
        check(!receipt.equals(changedCommand) && receipt.sameSourceEvent(changedCommand));
        check(!receipt.sameSourceEvent(receipt(current, window, revision, 1, command, "other", "event-1")));
        check(!receipt.sameSourceEvent(receipt(current, window, revision, 1, command, "quests", "event-2")));

        var refreshed = window(assignment, revision, window.sessionId(), UUID.randomUUID(),
            QuestFateCompletion.QuestState.IN_PROGRESS);
        var refreshedReceipt = receipt(current, refreshed, revision, 11, command, "quests", "event-1");
        check(!receipt.equals(refreshedReceipt) && receipt.sameSourceEvent(refreshedReceipt));
        var newSession = window(assignment, revision, UUID.randomUUID(), UUID.randomUUID(),
            QuestFateCompletion.QuestState.NOT_STARTED);
        check(!receipt.sameSourceEvent(receipt(current, newSession, revision, 11, command, "quests", "event-1")));
        rejects(() -> receipt.requireCurrent(current, revision, UUID.randomUUID(), window.windowId()));
        rejects(() -> receipt.requireCurrent(current, revision, window.sessionId(), refreshed.windowId()));
        rejects(() -> receipt.requireCurrent(current, revision - 1, window.sessionId(), window.windowId()));
        rejects(() -> receipt.requireCurrent(current(assignment, revision + 1, RunLifecycle.State.ACTIVE),
            revision + 1, window.sessionId(), window.windowId()));
        rejects(() -> receipt.requireCurrent(current(assignment, revision + 1, RunLifecycle.State.ACTIVE),
            revision, window.sessionId(), window.windowId()));
        rejects(() -> receipt.requireCurrent(current(assignment, revision, RunLifecycle.State.PAUSED),
            revision, window.sessionId(), window.windowId()));

        // Same assignment ID and original window cannot conceal changed generation provenance.
        var provenance = assignment.provenance();
        var altered = new QuestFateAssignment(new QuestFateProvenance(provenance.resolution(),
            provenance.sliceId(), "questing", "changed-stream", "0", "3", provenance.reward()),
            assignment.allocation(), window);
        rejects(() -> receipt.requireCurrent(current(altered, revision, RunLifecycle.State.ACTIVE),
            revision, window.sessionId(), window.windowId()));
        rejects(() -> receipt(current, window, revision, 0, command, "quests", "event-1"));
        rejects(() -> receipt(current, window, revision + 1, 1, command, "quests", "event-1"));
        rejects(() -> receipt(current, window(assignment, revision, window.sessionId(), UUID.randomUUID(),
            QuestFateCompletion.QuestState.COMPLETE), revision, 11, command, "quests", "event-1"));
        for (String key : new String[] {"", " ", " quests", "quests ", "\t"})
        {
            rejects(() -> receipt(current, window, revision, 1, command, key, "event-1"));
            rejects(() -> receipt(current, window, revision, 1, command, "quests", key));
        }
        rejects(() -> receipt(current, window, revision, 1, null, "quests", "event-1"));
        rejects(() -> receipt(current, window, revision, 1, command, null, "event-1"));
        rejects(() -> receipt(current, window, revision, 1, command, "quests", null));
        rejects(() -> receipt(current, null, revision, 1, command, "quests", "event-1"));
        rejects(() -> receipt(null, window, revision, 1, command, "quests", "event-1"));
        check(!receipt.equals(null) && !receipt.equals(command));
        System.out.println("QuestFateCompletionReceiptChecks PASSED");
    }

    private static QuestFateCompletionReceipt receipt(QuestFateCurrentAssignment current,
        QuestFateObservationWindow window, long revision, long position, UUID command,
        String namespace, String event)
    { return new QuestFateCompletionReceipt(current, window, revision, position, command, namespace, event); }

    private static QuestFateCurrentAssignment current(QuestFateAssignment assignment,
        long revision, RunLifecycle.State state)
    {
        var resolution = assignment.provenance().resolution();
        return QuestFateCurrentAssignment.restore(assignment,
            new MasterSelectionResolutions(assignment.identity(), revision, List.of(resolution)),
            new MasterSpinState(MasterSpinState.VERSION, assignment.identity(), revision,
                assignment.allocation().spinsBefore(), Optional.of(assignment.allocation())),
            new RunLifecycle(assignment.identity(), revision, state, RunLifecycle.Obligation.ACTIVE_OBJECTIVE));
    }

    private static QuestFateObservationWindow window(QuestFateAssignment assignment, long revision,
        UUID session, UUID window, QuestFateCompletion.QuestState baseline)
    {
        return new QuestFateObservationWindow(new RunLifecycle(assignment.identity(), revision,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE),
            assignment.assignmentId(), session, window, 10, assignment.provenance().reward().selection(), baseline);
    }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError(); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException | NullPointerException expected) { return; }
        throw new AssertionError("Expected receipt refusal");
    }
}

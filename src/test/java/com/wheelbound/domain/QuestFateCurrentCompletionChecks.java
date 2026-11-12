package com.wheelbound.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Offline composition checks; supplied windows do not prove live coverage. */
public final class QuestFateCurrentCompletionChecks
{
    public static void main(String[] args)
    {
        var assignment = QuestFateAssignmentChecks.fixture();
        var resolution = assignment.provenance().resolution();
        long revision = assignment.creationRevision() + 1;
        var history = new MasterSelectionResolutions(assignment.identity(), revision, List.of(resolution));
        var spins = new MasterSpinState(MasterSpinState.VERSION, assignment.identity(), revision,
            assignment.allocation().spinsBefore(), Optional.of(assignment.allocation()));
        var head = new RunLifecycle(assignment.identity(), revision, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        var current = QuestFateCurrentAssignment.restore(assignment, history, spins, head);
        var original = assignment.originalWindow();
        var sessionId = UUID.randomUUID();
        var windowId = UUID.randomUUID();
        var prepared = current.prepareSuppliedObservationWindow(assignment.identity(), revision,
            sessionId, windowId, 20, "q1", QuestFateCompletion.QuestState.IN_PROGRESS);
        check(prepared.identity().equals(assignment.identity())
            && prepared.assignmentId().equals(assignment.assignmentId())
            && prepared.baselineRevision() == revision && prepared.baselinePosition() == 20
            && prepared.sessionId().equals(sessionId) && prepared.windowId().equals(windowId)
            && prepared.selection().equals(assignment.provenance().reward().selection()));
        complete(current, prepared, revision, 21);
        rejects(() -> complete(current, prepared, revision, 20));
        rejects(() -> current.prepareSuppliedObservationWindow(assignment.identity(), revision - 1,
            sessionId, windowId, 20, "q1", QuestFateCompletion.QuestState.NOT_STARTED));
        rejects(() -> current.prepareSuppliedObservationWindow(assignment.identity(), revision + 1,
            sessionId, windowId, 20, "q1", QuestFateCompletion.QuestState.NOT_STARTED));
        for (String questId : new String[] { "q2", "", null })
        {
            rejects(() -> current.prepareSuppliedObservationWindow(assignment.identity(), revision,
                sessionId, windowId, 20, questId, QuestFateCompletion.QuestState.NOT_STARTED));
        }
        rejects(() -> current.prepareSuppliedObservationWindow(null, revision,
            sessionId, windowId, 20, "q1", QuestFateCompletion.QuestState.NOT_STARTED));
        for (var foreignIdentity : List.of(
            new RunIdentity(assignment.identity().accountKey(), UUID.randomUUID()),
            new RunIdentity(assignment.identity().accountKey() + "-other", assignment.identity().runId())))
        {
            rejects(() -> current.prepareSuppliedObservationWindow(foreignIdentity, revision,
                sessionId, windowId, 20, "q1", QuestFateCompletion.QuestState.NOT_STARTED));
        }
        rejects(() -> current.prepareSuppliedObservationWindow(assignment.identity(), revision,
            null, windowId, 20, "q1", QuestFateCompletion.QuestState.NOT_STARTED));
        rejects(() -> current.prepareSuppliedObservationWindow(assignment.identity(), revision,
            sessionId, null, 20, "q1", QuestFateCompletion.QuestState.NOT_STARTED));
        rejects(() -> current.prepareSuppliedObservationWindow(assignment.identity(), revision,
            sessionId, windowId, -1, "q1", QuestFateCompletion.QuestState.NOT_STARTED));
        rejects(() -> current.prepareSuppliedObservationWindow(assignment.identity(), revision,
            sessionId, windowId, 20, "q1", null));
        var completeBaseline = current.prepareSuppliedObservationWindow(assignment.identity(), revision,
            sessionId, UUID.randomUUID(), 20, "q1", QuestFateCompletion.QuestState.COMPLETE);
        rejects(() -> complete(current, completeBaseline, revision, 21));
        complete(current, original, revision, 1);
        var refreshed = window(assignment, head, assignment.assignmentId(),
            QuestFateCompletion.QuestState.IN_PROGRESS);
        complete(current, refreshed, revision, 11);
        complete(current, refreshed, revision, Long.MAX_VALUE);
        // Pure validation cannot consume a transition or debit another spin.
        complete(current, refreshed, revision, 11);
        check(current.assignment() == assignment && assignment.originalWindow() == original);
        check(spins.available() == assignment.allocation().spinsAfter());

        var foreign = window(assignment, head, UUID.randomUUID(), QuestFateCompletion.QuestState.NOT_STARTED);
        // A window can validate against its own IDs; the retained assignment must reject it.
        foreign.requireFreshCompletion(head, revision, foreign.assignmentId(), foreign.sessionId(),
            foreign.windowId(), foreign.selection(), 11, "q1", QuestFateCompletion.QuestState.COMPLETE);
        rejects(() -> complete(current, foreign, revision, 11));
        rejects(() -> complete(current, refreshed, revision - 1, 11));
        rejects(() -> complete(current, refreshed, revision + 1, 11));
        rejects(() -> complete(current, refreshed, revision, 10));
        rejects(() -> current.requireFreshCompletion(refreshed, revision, original.sessionId(),
            refreshed.windowId(), 11, "q1", QuestFateCompletion.QuestState.COMPLETE));
        rejects(() -> current.requireFreshCompletion(refreshed, revision, refreshed.sessionId(),
            original.windowId(), 11, "q1", QuestFateCompletion.QuestState.COMPLETE));
        rejects(() -> current.requireFreshCompletion(refreshed, revision, refreshed.sessionId(),
            refreshed.windowId(), 11, "q2", QuestFateCompletion.QuestState.COMPLETE));
        rejects(() -> current.requireFreshCompletion(refreshed, revision, refreshed.sessionId(),
            refreshed.windowId(), 11, "q1", QuestFateCompletion.QuestState.IN_PROGRESS));
        rejects(() -> complete(current, window(assignment, head, assignment.assignmentId(),
            QuestFateCompletion.QuestState.COMPLETE), revision, 11));
        var future = new RunLifecycle(assignment.identity(), revision + 1, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        rejects(() -> complete(current, window(assignment, future, assignment.assignmentId(),
            QuestFateCompletion.QuestState.IN_PROGRESS), revision, 11));
        var paused = QuestFateCurrentAssignment.restore(assignment, history, spins,
            new RunLifecycle(assignment.identity(), revision, RunLifecycle.State.PAUSED,
                RunLifecycle.Obligation.ACTIVE_OBJECTIVE));
        rejects(() -> complete(paused, refreshed, revision, 11));
        rejects(() -> paused.prepareSuppliedObservationWindow(assignment.identity(), revision,
            sessionId, windowId, 20, "q1", QuestFateCompletion.QuestState.IN_PROGRESS));
        rejects(() -> current.requireFreshCompletion(null, revision, refreshed.sessionId(),
            refreshed.windowId(), 11, "q1", QuestFateCompletion.QuestState.COMPLETE));
        System.out.println("QuestFateCurrentCompletionChecks PASSED");
    }

    private static QuestFateObservationWindow window(QuestFateAssignment assignment,
        RunLifecycle context, UUID assignmentId, QuestFateCompletion.QuestState baseline)
    {
        return new QuestFateObservationWindow(context, assignmentId, UUID.randomUUID(), UUID.randomUUID(),
            10, assignment.provenance().reward().selection(), baseline);
    }

    private static void complete(QuestFateCurrentAssignment current, QuestFateObservationWindow window,
        long revision, long position)
    {
        current.requireFreshCompletion(window, revision, window.sessionId(), window.windowId(),
            position, "q1", QuestFateCompletion.QuestState.COMPLETE);
    }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError(); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException | NullPointerException expected) { return; }
        throw new AssertionError("Expected completion refusal");
    }
}

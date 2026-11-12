package com.wheelbound.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Dependency-free coverage lifecycle checks; no client authenticity is claimed. */
public final class QuestFateObservationCoverageChecks
{
    public static void main(String[] args)
    {
        var assignment = QuestFateAssignmentChecks.fixture();
        long revision = assignment.creationRevision();
        var current = current(assignment, revision, RunLifecycle.State.ACTIVE);
        var guard = guard(current, QuestFateCompletion.QuestState.IN_PROGRESS);
        check(!observe(guard, current, 11, QuestFateCompletion.QuestState.IN_PROGRESS));
        check(observe(guard, current, 14, QuestFateCompletion.QuestState.COMPLETE));
        check(!guard.isOpen() && guard.lastPosition() == 14);
        rejects(() -> observe(guard, current, 15, QuestFateCompletion.QuestState.COMPLETE));

        for (long position : new long[] { -1, 10, 11 })
        {
            var unordered = guard(current, QuestFateCompletion.QuestState.NOT_STARTED);
            observe(unordered, current, 11, QuestFateCompletion.QuestState.IN_PROGRESS);
            rejects(() -> observe(unordered, current, position, QuestFateCompletion.QuestState.COMPLETE));
            check(!unordered.isOpen() && unordered.lastPosition() == 11);
            rejects(() -> observe(unordered, current, 12, QuestFateCompletion.QuestState.COMPLETE));
        }
        var interrupted = guard(current, QuestFateCompletion.QuestState.IN_PROGRESS);
        interrupted.interrupt();
        interrupted.interrupt();
        rejects(() -> observe(interrupted, current, 12, QuestFateCompletion.QuestState.COMPLETE));
        // A new COMPLETE baseline after an interruption cannot earn retrospective credit.
        var resumed = guard(current, QuestFateCompletion.QuestState.COMPLETE);
        rejects(() -> observe(resumed, current, 12, QuestFateCompletion.QuestState.COMPLETE));
        check(!resumed.isOpen());

        var later = current(assignment, revision + 2, RunLifecycle.State.ACTIVE);
        var advancing = guard(current, QuestFateCompletion.QuestState.NOT_STARTED);
        observe(advancing, later, 11, QuestFateCompletion.QuestState.IN_PROGRESS);
        rejects(() -> observe(advancing, current, 12, QuestFateCompletion.QuestState.COMPLETE));
        var validLater = guard(current, QuestFateCompletion.QuestState.NOT_STARTED);
        check(observe(validLater, later, Long.MAX_VALUE, QuestFateCompletion.QuestState.COMPLETE));
        var foreign = guard(current, QuestFateCompletion.QuestState.NOT_STARTED);
        var otherAssignment = QuestFateAssignmentChecks.fixture();
        rejects(() -> observe(foreign,
            current(otherAssignment, otherAssignment.creationRevision(), RunLifecycle.State.ACTIVE),
            12, QuestFateCompletion.QuestState.COMPLETE));
        check(!foreign.isOpen());

        for (int fault = 0; fault < 7; fault++)
        {
            var invalid = guard(current, QuestFateCompletion.QuestState.NOT_STARTED);
            final int kind = fault;
            rejects(() -> invalid.observe(kind == 0 ? null : kind == 1
                    ? current(assignment, revision + 1, RunLifecycle.State.PAUSED) : current,
                kind == 2 ? revision + 1 : revision,
                kind == 3 ? UUID.randomUUID() : invalid.window().sessionId(),
                kind == 4 ? UUID.randomUUID() : invalid.window().windowId(), 11,
                kind == 5 ? "q2" : "q1",
                kind == 6 ? null : QuestFateCompletion.QuestState.COMPLETE));
            check(!invalid.isOpen() && invalid.lastPosition() == 10);
        }
        check(assignment.originalWindow().baselinePosition() == 0);
        System.out.println("QuestFateObservationCoverageChecks PASSED");
    }

    private static QuestFateCurrentAssignment current(QuestFateAssignment assignment,
        long revision, RunLifecycle.State state)
    {
        var history = new MasterSelectionResolutions(assignment.identity(), revision,
            List.of(assignment.provenance().resolution()));
        var spins = new MasterSpinState(MasterSpinState.VERSION, assignment.identity(), revision,
            assignment.allocation().spinsBefore(), Optional.of(assignment.allocation()));
        return QuestFateCurrentAssignment.restore(assignment, history, spins,
            new RunLifecycle(assignment.identity(), revision, state, RunLifecycle.Obligation.ACTIVE_OBJECTIVE));
    }

    private static QuestFateObservationCoverage guard(QuestFateCurrentAssignment current,
        QuestFateCompletion.QuestState baseline)
    {
        return new QuestFateObservationCoverage(current, current.lifecycle().identity(),
            current.lifecycle().revision(), UUID.randomUUID(), UUID.randomUUID(), 10, "q1", baseline);
    }

    private static boolean observe(QuestFateObservationCoverage guard, QuestFateCurrentAssignment current,
        long position, QuestFateCompletion.QuestState state)
    {
        return guard.observe(current, current.lifecycle().revision(), guard.window().sessionId(),
            guard.window().windowId(), position, "q1", state);
    }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError(); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException | NullPointerException expected) { return; }
        throw new AssertionError("Expected coverage refusal");
    }
}

package com.wheelbound.domain;

import java.util.Objects;

/**
 * Explicit supplied current Questing binding. Does not infer an assignment from
 * history, authenticate catalogs/evidence or authorize persistence or settlement.
 * RunDocument retains this context in memory; codec and durable admission remain
 * required before gameplay use.
 */
public final class QuestFateCurrentAssignment
{
    private final QuestFateAssignment assignment;
    private final RunLifecycle lifecycle;

    private QuestFateCurrentAssignment(QuestFateAssignment assignment, RunLifecycle lifecycle)
    {
        this.assignment = assignment;
        this.lifecycle = lifecycle;
    }

    /** Validate an explicit commitment against retained terminal/accounting inputs. */
    public static QuestFateCurrentAssignment restore(QuestFateAssignment assignment,
        MasterSelectionResolutions history, MasterSpinState spins, RunLifecycle current)
    {
        Objects.requireNonNull(assignment, "assignment");
        Objects.requireNonNull(history, "history");
        Objects.requireNonNull(spins, "spins");
        Objects.requireNonNull(current, "current");
        var resolution = assignment.provenance().resolution();
        history.replay(resolution, current.identity(), current.revision());
        var retained = spins.replay(current.identity(), current.revision(), resolution);
        if (!assignment.identity().equals(current.identity())
            || !spins.identity().equals(current.identity())
            || current.revision() < assignment.creationRevision()
            || retained.spinsBefore() != assignment.allocation().spinsBefore()
            || (current.state() != RunLifecycle.State.ACTIVE && current.state() != RunLifecycle.State.PAUSED)
            || current.obligation() != RunLifecycle.Obligation.ACTIVE_OBJECTIVE)
        { throw new IllegalArgumentException("Inconsistent current Questing assignment"); }
        if (history.receipts().values().stream().anyMatch(r -> r.afterRevision() > resolution.afterRevision()))
        { throw new IllegalArgumentException("Questing assignment superseded by later resolution"); }
        return new QuestFateCurrentAssignment(assignment, current);
    }

    public QuestFateAssignment assignment() { return assignment; }
    public RunLifecycle lifecycle() { return lifecycle; }

    /**
     * Bind a supplied uninterrupted observation window to this retained commitment
     * before checking its completion transition. The caller must authenticate the
     * current head, session, window and source ordering; this pure check neither
     * consumes evidence nor authorizes a reward or durable settlement.
     */
    public void requireFreshCompletion(QuestFateObservationWindow window, long expectedRevision,
        java.util.UUID currentSessionId, java.util.UUID currentWindowId,
        long observedPosition, String observedQuestId, QuestFateCompletion.QuestState observedState)
    {
        assignment.requireWindow(window);
        window.requireFreshCompletion(lifecycle, expectedRevision, assignment.assignmentId(),
            currentSessionId, currentWindowId, assignment.provenance().reward().selection(),
            observedPosition, observedQuestId, observedState);
    }
}

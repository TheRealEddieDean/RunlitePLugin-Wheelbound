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
     * Prepare a supplied baseline from this retained head and selection. The caller
     * must establish live source identity, ordering and coverage, and supply a new
     * window ID after any interruption. This does not replace the original window
     * or register a live observation session. A complete baseline cannot later earn
     * fresh completion credit.
     */
    public QuestFateObservationWindow prepareSuppliedObservationWindow(
        RunIdentity expectedIdentity, long expectedRevision, java.util.UUID sessionId,
        java.util.UUID windowId, long baselinePosition, String observedQuestId,
        QuestFateCompletion.QuestState baseline)
    {
        if (!lifecycle.identity().equals(Objects.requireNonNull(expectedIdentity, "expectedIdentity"))
            || lifecycle.revision() != expectedRevision)
        { throw new IllegalArgumentException("Foreign or stale Questing baseline head"); }
        var selection = assignment.provenance().reward().selection();
        if (!selection.selection().questId().equals(observedQuestId))
        { throw new IllegalArgumentException("Baseline observation is for another quest"); }
        var window = new QuestFateObservationWindow(lifecycle, assignment.assignmentId(),
            sessionId, windowId, baselinePosition, selection, baseline);
        assignment.requireWindow(window);
        return window;
    }

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

package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Supplied baseline binding for one uninterrupted active quest observation window.
 * The observation boundary must authenticate source ordering and quest mappings,
 * rotate the window ID on pause/resume, login, hop or lost coverage, and resolve
 * the actual durable assignment. Matching public values does not prove freshness.
 * No evidence admission, replay prevention, persistence or settlement occurs here.
 */
public final class QuestFateObservationWindow
{
    private final RunIdentity identity;
    private final long baselineRevision;
    private final UUID assignmentId;
    private final UUID sessionId;
    private final UUID windowId;
    private final long baselinePosition;
    private final QuestFateSelection selection;
    private final QuestFateCompletion completion;

    public QuestFateObservationWindow(RunLifecycle baselineContext, UUID assignmentId,
        UUID sessionId, UUID windowId, long baselinePosition,
        QuestFateSelection selection, QuestFateCompletion.QuestState baseline)
    {
        requireActiveObjective(Objects.requireNonNull(baselineContext, "baselineContext"));
        this.identity = baselineContext.identity();
        this.baselineRevision = baselineContext.revision();
        this.assignmentId = Objects.requireNonNull(assignmentId, "assignmentId");
        this.sessionId = Objects.requireNonNull(sessionId, "sessionId");
        this.windowId = Objects.requireNonNull(windowId, "windowId");
        if (baselinePosition < 0) { throw new IllegalArgumentException("Negative baseline position"); }
        this.baselinePosition = baselinePosition;
        this.selection = Objects.requireNonNull(selection, "selection");
        this.completion = new QuestFateCompletion(selection.selection(), baseline);
    }

    public RunIdentity identity() { return identity; }
    public long baselineRevision() { return baselineRevision; }
    public UUID assignmentId() { return assignmentId; }
    public UUID sessionId() { return sessionId; }
    public UUID windowId() { return windowId; }
    public long baselinePosition() { return baselinePosition; }
    public QuestFateSelection selection() { return selection; }
    public QuestFateCompletion.QuestState baseline() { return completion.baseline(); }

    /**
     * Check supplied current context and a later observation. Positions belong to
     * the named session/window; timestamps and accumulated offline counters cannot
     * substitute. Later aggregate revisions are legal if the caller establishes
     * uninterrupted coverage. Repeated checks cannot award or consume evidence.
     */
    public void requireFreshCompletion(RunLifecycle current, long expectedRevision,
        UUID currentAssignmentId, UUID currentSessionId, UUID currentWindowId,
        QuestFateSelection currentSelection, long observedPosition,
        String observedQuestId, QuestFateCompletion.QuestState observedState)
    {
        Objects.requireNonNull(current, "current");
        if (!identity.equals(current.identity()) || current.revision() != expectedRevision
            || expectedRevision < baselineRevision
            || !assignmentId.equals(Objects.requireNonNull(currentAssignmentId, "currentAssignmentId"))
            || !sessionId.equals(Objects.requireNonNull(currentSessionId, "currentSessionId"))
            || !windowId.equals(Objects.requireNonNull(currentWindowId, "currentWindowId")))
        { throw new IllegalArgumentException("Foreign or stale quest observation context"); }
        requireActiveObjective(current);
        selection.requireSameSelection(currentSelection);
        if (observedPosition <= baselinePosition)
        { throw new IllegalArgumentException("Observation must follow the quest baseline"); }
        completion.requireFreshCompletion(currentSelection.selection(), current.state(),
            observedQuestId, observedState);
    }

    private static void requireActiveObjective(RunLifecycle context)
    {
        if (context.state() != RunLifecycle.State.ACTIVE
            || context.obligation() != RunLifecycle.Obligation.ACTIVE_OBJECTIVE)
        { throw new IllegalStateException("Active quest objective context required"); }
    }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof QuestFateObservationWindow)) { return false; }
        QuestFateObservationWindow other = (QuestFateObservationWindow) value;
        return identity.equals(other.identity) && baselineRevision == other.baselineRevision
            && assignmentId.equals(other.assignmentId) && sessionId.equals(other.sessionId)
            && windowId.equals(other.windowId) && baselinePosition == other.baselinePosition
            && selection.equals(other.selection) && completion.equals(other.completion);
    }

    @Override public int hashCode()
    { return Objects.hash(identity, baselineRevision, assignmentId, sessionId,
        windowId, baselinePosition, selection, completion); }
}

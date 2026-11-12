package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Ephemeral, single-thread-confined coverage guard for supplied quest observations.
 * The client boundary must authenticate the source, current durable head and quest
 * mapping, and interrupt this guard on pause, logout, hop or lost coverage. Public
 * values cannot prove those facts. Never persist or reuse a guard after restart.
 * A completion closes this guard but does not consume durable evidence or pay FP.
 */
public final class QuestFateObservationCoverage
{
    private final QuestFateObservationWindow window;
    private final QuestFateAssignment assignment;
    private long lastPosition;
    private long lastRevision;
    private boolean open = true;

    public QuestFateObservationCoverage(QuestFateCurrentAssignment current,
        RunIdentity expectedIdentity, long expectedRevision, UUID sessionId,
        UUID windowId, long baselinePosition, String questId,
        QuestFateCompletion.QuestState baseline)
    {
        window = Objects.requireNonNull(current, "current").prepareSuppliedObservationWindow(
            expectedIdentity, expectedRevision, sessionId, windowId, baselinePosition, questId, baseline);
        assignment = current.assignment();
        lastPosition = baselinePosition;
        lastRevision = expectedRevision;
    }

    public QuestFateObservationWindow window() { return window; }
    public boolean isOpen() { return open; }
    public long lastPosition() { return lastPosition; }

    /** Idempotent; resuming requires a new guard and a freshly observed baseline. */
    public void interrupt() { open = false; }

    /**
     * Prepare a supplied completion receipt while this coverage is still open.
     * Retain the returned receipt for retries: this guard closes on success or
     * refusal and cannot prepare another receipt. Source identities remain caller
     * supplied; this neither authenticates evidence nor admits a durable write.
     */
    public QuestFateCompletionReceipt prepareSuppliedCompletionReceipt(
        QuestFateCurrentAssignment current, long expectedRevision,
        UUID sessionId, UUID windowId, long position, String questId,
        QuestFateCompletion.QuestState state, UUID commandId,
        String sourceNamespace, String eventKey)
    {
        if (!open) { throw new IllegalStateException("Quest observation coverage is closed"); }
        try
        {
            // Validate metadata before advancing coverage; failed preparation must
            // not leave a reusable baseline or an apparently accepted position.
            var receipt = QuestFateCompletionReceipt.fromObservation(current, window,
                expectedRevision, sessionId, windowId, position, questId, state,
                commandId, sourceNamespace, eventKey);
            observe(current, expectedRevision, sessionId, windowId, position, questId, state);
            return receipt;
        }
        catch (RuntimeException failure)
        {
            open = false;
            throw failure;
        }
    }

    /**
     * Observe the assigned quest only. Positions must strictly increase, but need
     * not be contiguous: unrelated client events may occupy intervening positions.
     * Returns true once for a fresh completion. Invalid input closes coverage;
     * catching a refusal cannot resurrect the preceding baseline.
     */
    public boolean observe(QuestFateCurrentAssignment current, long expectedRevision,
        UUID sessionId, UUID windowId, long position, String questId,
        QuestFateCompletion.QuestState state)
    {
        if (!open) { throw new IllegalStateException("Quest observation coverage is closed"); }
        try
        {
            Objects.requireNonNull(current, "current");
            Objects.requireNonNull(state, "state");
            var lifecycle = current.lifecycle();
            current.assignment().requireWindow(window);
            if (!window.identity().equals(lifecycle.identity())
                || !assignment.equals(current.assignment())
                || lifecycle.revision() != expectedRevision || expectedRevision < lastRevision
                || lifecycle.state() != RunLifecycle.State.ACTIVE
                || lifecycle.obligation() != RunLifecycle.Obligation.ACTIVE_OBJECTIVE
                || !window.sessionId().equals(sessionId) || !window.windowId().equals(windowId)
                || !window.selection().selection().questId().equals(questId)
                || position <= lastPosition)
            { throw new IllegalArgumentException("Interrupted or unordered quest observation coverage"); }
            boolean complete = state == QuestFateCompletion.QuestState.COMPLETE;
            if (complete)
            {
                current.requireFreshCompletion(window, expectedRevision, sessionId, windowId,
                    position, questId, state);
            }
            lastPosition = position;
            lastRevision = expectedRevision;
            if (complete) { open = false; }
            return complete;
        }
        catch (RuntimeException failure)
        {
            open = false;
            throw failure;
        }
    }
}

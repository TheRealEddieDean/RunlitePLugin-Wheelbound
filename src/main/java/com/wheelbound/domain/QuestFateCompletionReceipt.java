package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Immutable supplied completion payload for a future trusted Questing admission
 * boundary. Public construction checks structural binding only: it authenticates
 * no source, consumes no event and authorizes no persistence or reward.
 */
public final class QuestFateCompletionReceipt
{
    private final QuestFateAssignment assignment;
    private final QuestFateObservationWindow window;
    private final long expectedRevision;
    private final long observedPosition;
    private final UUID commandId;
    private final String sourceNamespace;
    private final String eventKey;

    /** Retain exact source identities and original commitment for full replay comparison. */
    public QuestFateCompletionReceipt(QuestFateCurrentAssignment current,
        QuestFateObservationWindow window, long expectedRevision, long observedPosition,
        UUID commandId, String sourceNamespace, String eventKey)
    {
        this.assignment = Objects.requireNonNull(current, "current").assignment();
        this.window = Objects.requireNonNull(window, "window");
        this.expectedRevision = expectedRevision;
        this.observedPosition = observedPosition;
        this.commandId = Objects.requireNonNull(commandId, "commandId");
        this.sourceNamespace = requireKey(sourceNamespace);
        this.eventKey = requireKey(eventKey);
        requireCurrent(current, expectedRevision, window.sessionId(), window.windowId());
    }

    private static String requireKey(String value)
    {
        Objects.requireNonNull(value, "source key");
        if (value.isBlank() || !value.equals(value.strip()))
        { throw new IllegalArgumentException("Source identity must be nonblank and canonical"); }
        return value;
    }

    public QuestFateAssignment assignment() { return assignment; }
    public QuestFateObservationWindow window() { return window; }
    public long expectedRevision() { return expectedRevision; }
    public long observedPosition() { return observedPosition; }
    public UUID commandId() { return commandId; }
    public String sourceNamespace() { return sourceNamespace; }
    public String eventKey() { return eventKey; }

    /**
     * Recheck against a supplied current head and independently resolved live IDs.
     * The caller must authenticate that head, source mapping and uninterrupted
     * coverage. A durable journal must resolve exact replays before this check;
     * a later revision cannot turn an old receipt into a new completion.
     */
    public void requireCurrent(QuestFateCurrentAssignment current, long revision,
        UUID currentSessionId, UUID currentWindowId)
    {
        Objects.requireNonNull(current, "current");
        if (expectedRevision != revision || !assignment.equals(current.assignment()))
        { throw new IllegalArgumentException("Foreign or stale Questing receipt"); }
        current.requireFreshCompletion(window, revision, currentSessionId, currentWindowId,
            observedPosition, window.selection().selection().questId(),
            QuestFateCompletion.QuestState.COMPLETE);
    }

    /** Collision identity only; window/command changes cannot disguise the same source event. */
    public boolean sameSourceEvent(QuestFateCompletionReceipt other)
    {
        Objects.requireNonNull(other, "other");
        return assignment.identity().equals(other.assignment.identity())
            && window.sessionId().equals(other.window.sessionId())
            && sourceNamespace.equals(other.sourceNamespace) && eventKey.equals(other.eventKey);
    }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof QuestFateCompletionReceipt)) { return false; }
        QuestFateCompletionReceipt other = (QuestFateCompletionReceipt) value;
        return assignment.equals(other.assignment) && window.equals(other.window)
            && expectedRevision == other.expectedRevision && observedPosition == other.observedPosition
            && commandId.equals(other.commandId) && sourceNamespace.equals(other.sourceNamespace)
            && eventKey.equals(other.eventKey);
    }

    @Override public int hashCode()
    { return Objects.hash(assignment, window, expectedRevision, observedPosition,
        commandId, sourceNamespace, eventKey); }
}

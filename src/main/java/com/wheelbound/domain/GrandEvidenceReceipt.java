package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Structural receipt payload for the future trusted Grand acceptance boundary.
 * Source keys are opaque identities supplied by a reviewed verifier, not proof.
 * Construction and proposals authenticate nothing and do not persist or award.
 * No client path may treat this publicly constructible value as verified evidence.
 */
public final class GrandEvidenceReceipt
{
    private final GrandFateProgress.Evidence reference;
    private final long attemptRevision;
    private final UUID sourceSessionId;
    private final String sourceNamespace;
    private final String eventKey;

    /** Restore the exact payload; never regenerate source identity on replay. */
    public GrandEvidenceReceipt(SealedGrandFate seal, GrandFateChecklist.Objective objective,
        UUID commandId, long attemptRevision, UUID sourceSessionId,
        String sourceNamespace, String eventKey)
    {
        if (attemptRevision <= 0) { throw new IllegalArgumentException("Missing activated attempt revision"); }
        this.reference = new GrandFateProgress.Evidence(seal, objective, commandId,
            Math.addExact(attemptRevision, 1));
        this.attemptRevision = attemptRevision;
        this.sourceSessionId = Objects.requireNonNull(sourceSessionId, "sourceSessionId");
        this.sourceNamespace = requireKey(sourceNamespace);
        this.eventKey = requireKey(eventKey);
    }

    private static String requireKey(String value)
    {
        Objects.requireNonNull(value, "source key");
        if (value.isBlank() || !value.equals(value.strip()))
        { throw new IllegalArgumentException("Source identity must be nonblank and canonical"); }
        return value;
    }

    public GrandFateProgress.Evidence reference() { return reference; }
    public long attemptRevision() { return attemptRevision; }
    public UUID sourceSessionId() { return sourceSessionId; }
    public String sourceNamespace() { return sourceNamespace; }
    public String eventKey() { return eventKey; }

    /**
     * Checks structural binding before proposing success. The trusted caller must
     * obtain the session from current observation admission, resolve the durable
     * active attempt and progress under lock, authenticate the source, and settle
     * off-task audit before acceptance. A caller-supplied matching UUID alone is
     * not session validation. Pause/resume requires separate durable reconciliation.
     * Exact replay must resolve this full payload before checking a newer head;
     * this method deliberately cannot reapply an already committed receipt.
     */
    public GrandFateAttempt.Success proposeSuccess(GrandFateAttempt attempt, RunLifecycle current,
        GrandFateProgress progress, UUID currentSessionId, RunIdentity expectedIdentity,
        long expectedRevision)
    {
        Objects.requireNonNull(attempt, "attempt");
        if (!sourceSessionId.equals(Objects.requireNonNull(currentSessionId, "currentSessionId"))
            || attempt.lifecycle().revision() != attemptRevision
            || !attempt.checklist().seal().equals(reference.seal())
            || attempt.objective() != reference.objective())
        { throw new IllegalArgumentException("Foreign session or Grand attempt evidence"); }
        return attempt.proposeSuccess(current, progress, reference, expectedIdentity, expectedRevision);
    }

    /** Collision key only; a durable journal must enforce uniqueness across receipts. */
    public boolean sameSourceEvent(GrandEvidenceReceipt other)
    {
        Objects.requireNonNull(other, "other");
        return reference.seal().identity().equals(other.reference.seal().identity())
            && sourceSessionId.equals(other.sourceSessionId)
            && sourceNamespace.equals(other.sourceNamespace) && eventKey.equals(other.eventKey);
    }

    /** Full request equality for replay; command ID equality alone is insufficient. */
    @Override public boolean equals(Object value)
    {
        if (!(value instanceof GrandEvidenceReceipt)) { return false; }
        GrandEvidenceReceipt other = (GrandEvidenceReceipt) value;
        return reference.equals(other.reference) && attemptRevision == other.attemptRevision
            && sourceSessionId.equals(other.sourceSessionId)
            && sourceNamespace.equals(other.sourceNamespace) && eventKey.equals(other.eventKey);
    }

    @Override public int hashCode()
    { return Objects.hash(reference, attemptRevision, sourceSessionId, sourceNamespace, eventKey); }
}

package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Immutable original activation payload for future durable commands (WB-D089/104).
 * Structural data only: construction neither admits a session nor persists an attempt.
 * A future store must enforce global command uniqueness and commit the full aggregate.
 */
public final class GrandActivationReceipt
{
    private final UUID commandId;
    private final long beforeRevision;
    private final GrandFateAttempt attempt;

    /** Reconstructs the original transition, never from a later aggregate head. */
    public GrandActivationReceipt(UUID commandId, RunIdentity identity, long beforeRevision,
        GrandFateChecklist checklist, GrandFateChecklist.Objective objective)
    {
        this.commandId = Objects.requireNonNull(commandId, "commandId");
        this.beforeRevision = beforeRevision;
        RunLifecycle original = new RunLifecycle(identity, beforeRevision,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        this.attempt = GrandFateAttempt.activate(original, checklist, objective, identity, beforeRevision);
    }

    /** Proposes a receipt only after checking the supplied current activation gate. */
    public static GrandActivationReceipt propose(UUID commandId, RunLifecycle current,
        GrandFateChecklist checklist, GrandFateChecklist.Objective objective,
        RunIdentity expectedIdentity, long expectedRevision)
    {
        GrandFateAttempt.activate(current, checklist, objective, expectedIdentity, expectedRevision);
        return new GrandActivationReceipt(commandId, expectedIdentity, expectedRevision, checklist, objective);
    }

    public UUID commandId() { return commandId; }
    public long beforeRevision() { return beforeRevision; }
    public long afterRevision() { return attempt.lifecycle().revision(); }
    public GrandFateAttempt attempt() { return attempt; }

    /** Full payload equality; matching command UUIDs alone never establish replay. */
    @Override public boolean equals(Object value)
    {
        if (!(value instanceof GrandActivationReceipt)) { return false; }
        GrandActivationReceipt other = (GrandActivationReceipt) value;
        return commandId.equals(other.commandId) && beforeRevision == other.beforeRevision
            && attempt.checklist().seal().equals(other.attempt.checklist().seal())
            && attempt.checklist().completed().equals(other.attempt.checklist().completed())
            && attempt.objective() == other.attempt.objective();
    }

    @Override public int hashCode()
    {
        return Objects.hash(commandId, beforeRevision, attempt.checklist().seal(),
            attempt.checklist().completed(), attempt.objective());
    }
}

package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Frozen run-start evidence for WB-D003. Construction/restore does not acknowledge
 * persistence, verify onboarding eligibility, or authorize a Grand attempt.
 * The durable run-start boundary must retain this value with the new run atomically.
 */
public final class SealedGrandFate
{
    private final RunIdentity identity;
    private final UUID commandId;
    private final DrawCommitment commitment;

    /** Restore the original full commitment; never sample or infer a legacy destiny. */
    public SealedGrandFate(RunIdentity identity, UUID commandId, DrawCommitment commitment)
    {
        this.identity = Objects.requireNonNull(identity, "identity");
        this.commandId = Objects.requireNonNull(commandId, "commandId");
        this.commitment = Objects.requireNonNull(commitment, "commitment");
        GrandFatePool.goal(commitment);
    }

    public RunIdentity identity() { return identity; }
    public UUID commandId() { return commandId; }
    public DrawCommitment commitment() { return commitment; }
    public GrandFatePool.Goal goal() { return GrandFatePool.goal(commitment); }

    /**
     * Exact request matching for future durable replay. A command ID alone cannot
     * authorize returning evidence from a different account, run, or sampled draw.
     */
    public boolean matches(RunIdentity requestedIdentity, UUID requestedCommandId,
        DrawCommitment requestedCommitment)
    {
        return identity.equals(Objects.requireNonNull(requestedIdentity, "identity"))
            && commandId.equals(Objects.requireNonNull(requestedCommandId, "commandId"))
            && commitment.equals(Objects.requireNonNull(requestedCommitment, "commitment"));
    }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof SealedGrandFate)) { return false; }
        SealedGrandFate other = (SealedGrandFate) value;
        return identity.equals(other.identity) && commandId.equals(other.commandId)
            && commitment.equals(other.commitment);
    }

    @Override public int hashCode() { return Objects.hash(identity, commandId, commitment); }
}

package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Assignment-origin binding for the frozen starting XP pair (STATE_CONTRACT).
 * Order in the retained pair identifies each offer within this assignment.
 * This is a structural value, not evidence of eligibility or a durable commitment.
 * Callers must authenticate the run and creation revision, validate supported
 * restrictions/eligibility, and persist the complete assignment before display.
 * No selection, current-assignment authority or evidence admission is supplied.
 */
public final class FateXpOfferSet
{
    private final RunIdentity identity;
    private final UUID assignmentId;
    private final long creationRevision;
    private final FateXpRestrictedOffers offers;

    public FateXpOfferSet(RunIdentity identity, UUID assignmentId, long creationRevision,
        FateXpRestrictedOffers offers)
    {
        this.identity = Objects.requireNonNull(identity, "identity");
        this.assignmentId = Objects.requireNonNull(assignmentId, "assignmentId");
        if (creationRevision < 0) { throw new IllegalArgumentException("Negative creation revision"); }
        this.creationRevision = creationRevision;
        this.offers = Objects.requireNonNull(offers, "offers");
    }

    public RunIdentity identity() { return identity; }
    public UUID assignmentId() { return assignmentId; }
    public long creationRevision() { return creationRevision; }
    public FateXpRestrictedOffers offers() { return offers; }

    /** Compare an explicit trusted origin, not the aggregate's later current revision. */
    public void requireOrigin(RunIdentity expectedIdentity, UUID expectedAssignmentId,
        long expectedCreationRevision)
    {
        Objects.requireNonNull(expectedIdentity, "expectedIdentity");
        Objects.requireNonNull(expectedAssignmentId, "expectedAssignmentId");
        if (!identity.equals(expectedIdentity) || !assignmentId.equals(expectedAssignmentId)
            || creationRevision != expectedCreationRevision)
        { throw new IllegalArgumentException("Foreign or stale offer origin"); }
    }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof FateXpOfferSet)) { return false; }
        FateXpOfferSet other = (FateXpOfferSet) value;
        return identity.equals(other.identity) && assignmentId.equals(other.assignmentId)
            && creationRevision == other.creationRevision && offers.equals(other.offers);
    }

    @Override public int hashCode()
    { return Objects.hash(identity, assignmentId, creationRevision, offers); }
}

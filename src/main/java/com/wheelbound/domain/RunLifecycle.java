package com.wheelbound.domain;

import java.util.Objects;

/**
 * Immutable lifecycle portion of the future run aggregate (STATE_CONTRACT).
 * A returned transition is a proposal: callers must persist the complete aggregate before publishing it.
 * This type neither starts a run nor establishes that a stored payload is valid or durable.
 */
public final class RunLifecycle
{
    public enum State { NOT_STARTED, ACTIVE, PAUSED, ABANDONED, COMPLETED }

    public enum Obligation
    {
        NONE, OFFERS_COMMITTED, CARD_SELECTED, BOSS_COMMITTED, ACTIVE_OBJECTIVE,
        PUNISHMENT_PENDING, SACRIFICE_ACQUISITION, SACRIFICE_CANDIDATES_COMMITTED,
        SACRIFICE_DEMAND_COMMITTED, SACRIFICE_RECEIPT_PENDING, SETTLEMENT_PENDING
    }

    private final RunIdentity identity;
    private final long revision;
    private final State state;
    private final Obligation obligation;

    public RunLifecycle(RunIdentity identity, long revision, State state, Obligation obligation)
    {
        this.identity = Objects.requireNonNull(identity, "identity");
        if (revision < 0) { throw new IllegalArgumentException("Negative revision"); }
        this.revision = revision;
        this.state = Objects.requireNonNull(state, "state");
        this.obligation = Objects.requireNonNull(obligation, "obligation");
    }

    public RunIdentity identity() { return identity; }
    public long revision() { return revision; }
    public State state() { return state; }
    public Obligation obligation() { return obligation; }

    /** WB-D002/WB-D114: pausing retains the obligation and never clears a commitment. */
    public RunLifecycle pause(RunIdentity expectedIdentity, long expectedRevision)
    {
        checkContext(expectedIdentity, expectedRevision);
        if (state == State.PAUSED) { return this; }
        if (state != State.ACTIVE) { throw new IllegalStateException("Run is not active"); }
        return transition(State.PAUSED);
    }

    public RunLifecycle resume(RunIdentity expectedIdentity, long expectedRevision)
    {
        checkContext(expectedIdentity, expectedRevision);
        if (state == State.ACTIVE) { return this; }
        if (state != State.PAUSED) { throw new IllegalStateException("Run is not paused"); }
        return transition(State.ACTIVE);
    }

    /** Guard for immediate observations only; durable evidence needs its own session/receipt protocol. */
    public boolean acceptsObservation(RunIdentity observedIdentity, long observedRevision)
    {
        return state == State.ACTIVE && identity.equals(observedIdentity) && revision == observedRevision;
    }

    private void checkContext(RunIdentity expectedIdentity, long expectedRevision)
    {
        if (!identity.equals(expectedIdentity) || revision != expectedRevision)
        {
            throw new IllegalArgumentException("Stale or foreign run context");
        }
    }

    private RunLifecycle transition(State target)
    {
        return new RunLifecycle(identity, Math.addExact(revision, 1), target, obligation);
    }
}

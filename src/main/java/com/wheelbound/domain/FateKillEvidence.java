package com.wheelbound.domain;

import java.util.Objects;

/**
 * Pure kill accounting for one explicitly trusted assigned Fate/Penance (WB-D087).
 * Callers must authenticate supported personal kill attribution, bind observations
 * to the current run/session/assignment, and admit each event exactly once.
 * Counts do not establish coverage, objective completion, a clean audit, or penalties.
 * Legitimate combat XP belongs in separate XP evidence (WB-D087/098).
 */
public final class FateKillEvidence
{
    public enum Attribution { ASSIGNED, UNAUTHORIZED, UNKNOWN }

    private final long assigned;
    private final long unauthorized;
    private final long unknown;

    /** Restore explicit trusted totals; missing evidence cannot prove a clean audit. */
    public FateKillEvidence(long assigned, long unauthorized, long unknown)
    {
        if (assigned < 0 || unauthorized < 0 || unknown < 0)
        { throw new IllegalArgumentException("Negative kill evidence"); }
        Math.addExact(Math.addExact(assigned, unauthorized), unknown);
        this.assigned = assigned;
        this.unauthorized = unauthorized;
        this.unknown = unknown;
    }

    public long assigned() { return assigned; }
    public long unauthorized() { return unauthorized; }
    public long unknown() { return unknown; }
    public long total() { return assigned + unauthorized + unknown; }

    /**
     * Propose an active-only, nonnegative kill delta from explicitly trusted context.
     * Unknown ownership/target attribution remains unknown, never automatic credit
     * or a violation. This method does not classify targets or incidental kills.
     * Paused observations cannot be retained here for retrospective credit.
     * Repeated calls accumulate; replay prevention requires an enclosing journal.
     */
    public FateKillEvidence observe(RunLifecycle.State state, Attribution attribution, long delta)
    {
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(attribution, "attribution");
        if (state != RunLifecycle.State.ACTIVE)
        { throw new IllegalStateException("Active observation required"); }
        if (delta < 0) { throw new IllegalArgumentException("Negative kill delta"); }
        switch (attribution)
        {
            case ASSIGNED:
                return new FateKillEvidence(Math.addExact(assigned, delta), unauthorized, unknown);
            case UNAUTHORIZED:
                return new FateKillEvidence(assigned, Math.addExact(unauthorized, delta), unknown);
            case UNKNOWN:
                return new FateKillEvidence(assigned, unauthorized, Math.addExact(unknown, delta));
            default: throw new IllegalArgumentException("Unsupported kill attribution");
        }
    }
}

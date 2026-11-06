package com.wheelbound.domain;

import java.util.Objects;

/**
 * Pure XP accounting for one assigned Fate/Penance (WB-D087/098).
 * The caller must supply supported, assignment-specific attribution and uniquely
 * admitted active observations. This value does not authenticate events, establish
 * complete coverage, apply tolerances, or authorize a clean/violating completion.
 * Kill objectives additionally require kill evidence; XP alone cannot audit them.
 */
public final class FateXpEvidence
{
    public enum Attribution { ASSIGNED, VERIFIED_BY_PRODUCT, UNAUTHORIZED, UNKNOWN }

    private final long assigned;
    private final long byProduct;
    private final long unauthorized;
    private final long unknown;

    /** Restore explicit trusted totals; absence of evidence is not a clean audit. */
    public FateXpEvidence(long assigned, long byProduct, long unauthorized, long unknown)
    {
        if (assigned < 0 || byProduct < 0 || unauthorized < 0 || unknown < 0)
        { throw new IllegalArgumentException("Negative XP evidence"); }
        Math.addExact(Math.addExact(assigned, byProduct), Math.addExact(unauthorized, unknown));
        this.assigned = assigned;
        this.byProduct = byProduct;
        this.unauthorized = unauthorized;
        this.unknown = unknown;
    }

    public long assigned() { return assigned; }
    public long byProduct() { return byProduct; }
    public long unauthorized() { return unauthorized; }
    public long unknown() { return unknown; }
    public long total() { return assigned + byProduct + unauthorized + unknown; }

    /**
     * Propose accounting for a nonnegative observed XP delta. Paused and other
     * inactive observations are refused, never retained for retrospective credit.
     * Unknown attribution stays separate: it is neither exempt nor unauthorized.
     * Repeated calls are not idempotent; the enclosing evidence journal must admit
     * each event exactly once and bind it to the current account/session/assignment.
     */
    public FateXpEvidence observe(RunLifecycle.State state, Attribution attribution, long delta)
    {
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(attribution, "attribution");
        if (state != RunLifecycle.State.ACTIVE)
        { throw new IllegalStateException("Active observation required"); }
        if (delta < 0) { throw new IllegalArgumentException("Negative XP delta"); }
        switch (attribution)
        {
            case ASSIGNED:
                return new FateXpEvidence(Math.addExact(assigned, delta), byProduct, unauthorized, unknown);
            case VERIFIED_BY_PRODUCT:
                return new FateXpEvidence(assigned, Math.addExact(byProduct, delta), unauthorized, unknown);
            case UNAUTHORIZED:
                return new FateXpEvidence(assigned, byProduct, Math.addExact(unauthorized, delta), unknown);
            case UNKNOWN:
                return new FateXpEvidence(assigned, byProduct, unauthorized, Math.addExact(unknown, delta));
            default: throw new IllegalArgumentException("Unsupported XP attribution");
        }
    }
}

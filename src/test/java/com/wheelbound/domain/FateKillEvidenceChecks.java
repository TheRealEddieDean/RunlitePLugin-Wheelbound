package com.wheelbound.domain;

import static com.wheelbound.domain.FateKillEvidence.Attribution.*;
import static com.wheelbound.domain.RunLifecycle.State.*;

/** Dependency-free behavioral checks shared with JUnit. */
public final class FateKillEvidenceChecks
{
    public static void main(String[] args)
    {
        var empty = new FateKillEvidence(0, 0, 0);
        var target = empty.observe(ACTIVE, ASSIGNED, 5);
        var unrelated = target.observe(ACTIVE, UNAUTHORIZED, 2);
        var ambiguous = unrelated.observe(ACTIVE, UNKNOWN, 3);
        check(ambiguous.assigned() == 5 && ambiguous.unauthorized() == 2);
        check(ambiguous.unknown() == 3 && ambiguous.total() == 10);
        check(empty.total() == 0 && target.total() == 5 && unrelated.unknown() == 0);
        // Combat XP is tracked independently, not converted into unauthorized kills.
        var xp = new FateXpEvidence(0, 0, 0, 0)
            .observe(ACTIVE, FateXpEvidence.Attribution.VERIFIED_BY_PRODUCT, 1500);
        check(xp.unauthorized() == 0 && ambiguous.unauthorized() == 2);

        for (var attribution : FateKillEvidence.Attribution.values())
        {
            check(ambiguous.observe(ACTIVE, attribution, 0).total() == 10);
            rejects(IllegalArgumentException.class, () -> empty.observe(ACTIVE, attribution, -1));
            for (var state : RunLifecycle.State.values())
            {
                if (state != ACTIVE)
                { rejects(IllegalStateException.class, () -> empty.observe(state, attribution, 1)); }
            }
            for (int bucket = 0; bucket < 3; bucket++)
            {
                long[] totals = {0, 0, 0};
                totals[bucket] = Long.MAX_VALUE;
                var full = new FateKillEvidence(totals[0], totals[1], totals[2]);
                rejects(ArithmeticException.class, () -> full.observe(ACTIVE, attribution, 1));
                check(full.total() == Long.MAX_VALUE);
            }
        }
        rejects(NullPointerException.class, () -> empty.observe(null, ASSIGNED, 1));
        rejects(NullPointerException.class, () -> empty.observe(ACTIVE, null, 1));
        rejects(IllegalArgumentException.class, () -> new FateKillEvidence(-1, 0, 0));
        rejects(IllegalArgumentException.class, () -> new FateKillEvidence(0, -1, 0));
        rejects(IllegalArgumentException.class, () -> new FateKillEvidence(0, 0, -1));
        rejects(ArithmeticException.class, () -> new FateKillEvidence(Long.MAX_VALUE, 1, 0));
        rejects(ArithmeticException.class, () -> new FateKillEvidence(Long.MAX_VALUE, 0, 1));
        rejects(ArithmeticException.class, () -> new FateKillEvidence(0, Long.MAX_VALUE, 1));
        check(ambiguous.total() == 10);
    }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Kill evidence invariant failed"); } }

    private static void rejects(Class<? extends Throwable> expected, Runnable action)
    {
        try { action.run(); }
        catch (Throwable failure)
        {
            if (expected.isInstance(failure)) { return; }
            throw new AssertionError("Unexpected failure", failure);
        }
        throw new AssertionError("Expected " + expected.getSimpleName());
    }
}

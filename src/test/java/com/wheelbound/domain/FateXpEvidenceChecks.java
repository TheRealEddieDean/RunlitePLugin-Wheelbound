package com.wheelbound.domain;

import static com.wheelbound.domain.FateXpEvidence.Attribution.*;
import static com.wheelbound.domain.RunLifecycle.State.*;

/** Dependency-free behavioral checks shared with JUnit. */
public final class FateXpEvidenceChecks
{
    public static void main(String[] args)
    {
        var empty = new FateXpEvidence(0, 0, 0, 0);
        var primary = empty.observe(ACTIVE, ASSIGNED, 2000);
        // Verified inherent combat/HP XP does not consume unauthorized XP.
        var collateral = primary.observe(ACTIVE, VERIFIED_BY_PRODUCT, 700);
        var unrelated = collateral.observe(ACTIVE, UNAUTHORIZED, 1200);
        var ambiguous = unrelated.observe(ACTIVE, UNKNOWN, 50);
        check(ambiguous.assigned() == 2000 && ambiguous.byProduct() == 700);
        check(ambiguous.unauthorized() == 1200 && ambiguous.unknown() == 50);
        check(ambiguous.total() == 3950 && empty.total() == 0);
        check(primary.total() == 2000 && collateral.unauthorized() == 0);
        check(unrelated.unknown() == 0);

        for (var attribution : FateXpEvidence.Attribution.values())
        {
            check(ambiguous.observe(ACTIVE, attribution, 0).total() == ambiguous.total());
            rejects(IllegalArgumentException.class, () -> empty.observe(ACTIVE, attribution, -1));
            for (var state : RunLifecycle.State.values())
            {
                if (state != ACTIVE)
                { rejects(IllegalStateException.class, () -> ambiguous.observe(state, attribution, 10)); }
            }
            // Even cross-bucket addition must refuse total overflow atomically.
            var full = new FateXpEvidence(Long.MAX_VALUE, 0, 0, 0);
            rejects(ArithmeticException.class, () -> full.observe(ACTIVE, attribution, 1));
            check(full.total() == Long.MAX_VALUE);
        }
        rejects(NullPointerException.class, () -> empty.observe(null, ASSIGNED, 1));
        rejects(NullPointerException.class, () -> empty.observe(ACTIVE, null, 1));
        for (int bucket = 0; bucket < 4; bucket++)
        {
            long[] totals = {0, 0, 0, 0};
            totals[bucket] = -1;
            rejects(IllegalArgumentException.class,
                () -> new FateXpEvidence(totals[0], totals[1], totals[2], totals[3]));
            totals[bucket] = Long.MAX_VALUE;
            var restored = new FateXpEvidence(totals[0], totals[1], totals[2], totals[3]);
            check(restored.total() == Long.MAX_VALUE);
        }
        rejects(ArithmeticException.class, () -> new FateXpEvidence(0, 0, Long.MAX_VALUE, 1));
        rejects(ArithmeticException.class, () -> new FateXpEvidence(Long.MAX_VALUE, 1, 0, 0));
        rejects(ArithmeticException.class, () -> new FateXpEvidence(Long.MAX_VALUE, 0, 1, 0));
        check(ambiguous.total() == 3950);
    }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("XP evidence invariant failed"); } }

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

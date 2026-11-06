package com.wheelbound.domain;

import static com.wheelbound.domain.ConsecutivePenance.Audit.*;

/** Dependency-free behavioral checks shared with JUnit. */
public final class ConsecutivePenanceChecks
{
    public static void main(String[] args)
    {
        var initial = new ConsecutivePenance(0);
        var first = initial.afterCompletion(VIOLATION);
        var second = first.afterCompletion(VIOLATION);
        check(initial.violations() == 0 && first.violations() == 1 && second.violations() == 2);
        // The same rules apply when the assigned work being audited is Penance:
        // violating it continues escalation; a clean completion resets completely.
        var third = second.afterCompletion(VIOLATION);
        check(third.violations() == 3);
        var clean = third.afterCompletion(CLEAN);
        check(clean.violations() == 0 && third.violations() == 3);
        check(clean.afterCompletion(CLEAN).violations() == 0);
        check(clean.afterCompletion(VIOLATION).violations() == 1);

        for (long streak : new long[] {0, 1, 3, 1_000_000, Long.MAX_VALUE - 1, Long.MAX_VALUE})
        {
            var restored = new ConsecutivePenance(streak);
            check(restored.violations() == streak);
            rejects(IllegalArgumentException.class, () -> restored.afterCompletion(INCOMPLETE));
            rejects(NullPointerException.class, () -> restored.afterCompletion(null));
            check(restored.violations() == streak);
            check(restored.afterCompletion(CLEAN).violations() == 0);
            if (streak < Long.MAX_VALUE)
            { check(restored.afterCompletion(VIOLATION).violations() == streak + 1); }
            else
            { rejects(ArithmeticException.class, () -> restored.afterCompletion(VIOLATION)); }
            check(restored.violations() == streak);
        }
        rejects(IllegalArgumentException.class, () -> new ConsecutivePenance(-1));
        rejects(IllegalArgumentException.class, () -> new ConsecutivePenance(Long.MIN_VALUE));
    }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Consecutive Penance invariant failed"); } }

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

package com.wheelbound.domain;

import java.util.Objects;

/**
 * End-of-Fate consecutive violation state (WB-D054/087). The enclosing completion
 * command must supply a complete supported audit for the assigned Fate or Penance
 * and atomically retain the result with any resulting obligation. This arithmetic
 * does not judge evidence, choose punishment severity, charge FP or authorize a
 * completion. Never call it on a live tolerance crossing or paused observations.
 */
public final class ConsecutivePenance
{
    public enum Audit { CLEAN, VIOLATION, INCOMPLETE }

    private final long violations;

    /** Restore an explicit trusted streak; missing history must not default to zero. */
    public ConsecutivePenance(long violations)
    {
        if (violations < 0) { throw new IllegalArgumentException("Negative consecutive violations"); }
        this.violations = violations;
    }

    public long violations() { return violations; }

    /**
     * Pure proposal for a completed ordinary Fate or Penance. Unknown/unsupported
     * evidence must remain INCOMPLETE, never silently treated as a clean audit.
     * Replay protection and active-assignment validation belong to the enclosing
     * durable command; applying a violating completion twice is not idempotent.
     */
    public ConsecutivePenance afterCompletion(Audit audit)
    {
        switch (Objects.requireNonNull(audit, "audit"))
        {
            case CLEAN: return new ConsecutivePenance(0);
            case VIOLATION: return new ConsecutivePenance(Math.addExact(violations, 1));
            case INCOMPLETE: throw new IllegalArgumentException("Complete supported audit required");
            default: throw new IllegalArgumentException("Unsupported audit");
        }
    }
}

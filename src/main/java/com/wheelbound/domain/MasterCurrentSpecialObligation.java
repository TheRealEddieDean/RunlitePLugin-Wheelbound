package com.wheelbound.domain;

import java.util.Objects;

/**
 * Explicit structural current-special context, never inferred from terminal history.
 * This validates binding and the lifecycle stage family only. Stage-specific task,
 * candidate, acquisition and settlement payloads still need separate validation.
 * No aggregate, store or client admits this model yet.
 */
public final class MasterCurrentSpecialObligation
{
    private final MasterSpecialObligation obligation;
    private final MasterSpinState spins;
    private final RunLifecycle lifecycle;

    private MasterCurrentSpecialObligation(MasterSpecialObligation obligation,
        MasterSpinState spins, RunLifecycle lifecycle)
    {
        this.obligation = obligation;
        this.spins = spins;
        this.lifecycle = lifecycle;
    }

    /** Restore an explicit payload with its exact retained terminal and original reservation. */
    public static MasterCurrentSpecialObligation restore(MasterSpecialObligation obligation,
        MasterSelectionResolutions history, MasterSpinState spins, RunLifecycle current)
    {
        Objects.requireNonNull(obligation, "obligation");
        Objects.requireNonNull(history, "history");
        Objects.requireNonNull(spins, "spins");
        Objects.requireNonNull(current, "current");
        history.replay(obligation.resolution(), current.identity(), current.revision());
        spins.replay(current.identity(), current.revision(), obligation.resolution());
        if (!spins.identity().equals(current.identity())
            || (current.state() != RunLifecycle.State.ACTIVE && current.state() != RunLifecycle.State.PAUSED)
            || !matchesStage(obligation.requirement(), current.obligation()))
        { throw new IllegalArgumentException("Inconsistent current special lifecycle"); }
        for (var receipt : history.receipts().values())
        {
            if (receipt.afterRevision() > obligation.resolution().afterRevision())
            { throw new IllegalArgumentException("Current special superseded by later resolution"); }
        }
        return new MasterCurrentSpecialObligation(obligation, spins, current);
    }

    private static boolean matchesStage(MasterSpecialObligation.Requirement requirement,
        RunLifecycle.Obligation stage)
    {
        if (requirement == MasterSpecialObligation.Requirement.MANDATORY_PUNISHMENT)
        { return stage == RunLifecycle.Obligation.PUNISHMENT_PENDING; }
        switch (stage)
        {
            case SACRIFICE_ACQUISITION:
            case SACRIFICE_CANDIDATES_COMMITTED:
            case SACRIFICE_DEMAND_COMMITTED:
            case SACRIFICE_RECEIPT_PENDING: return true;
            default: return false;
        }
    }

    /** Pause preserves the exact terminal, reservation and mandatory requirement. */
    public MasterCurrentSpecialObligation pause(RunIdentity identity, long revision)
    { return new MasterCurrentSpecialObligation(obligation, spins, lifecycle.pause(identity, revision)); }

    /** Fresh session/evidence admission remains required before gameplay can resume. */
    public MasterCurrentSpecialObligation resume(RunIdentity identity, long revision)
    { return new MasterCurrentSpecialObligation(obligation, spins, lifecycle.resume(identity, revision)); }

    public MasterSpecialObligation obligation() { return obligation; }
    public MasterSpinState spins() { return spins; }
    public RunLifecycle lifecycle() { return lifecycle; }
}

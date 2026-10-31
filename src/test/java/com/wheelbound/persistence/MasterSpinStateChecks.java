package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Dependency-free contract checks, also executed by the JUnit wrapper. */
public final class MasterSpinStateChecks
{
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid spin reservation admitted");
    }

    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            var head = MasterCreationAggregateChecks.fixture(count);
            var lifecycle = head.lifecycle();
            var identity = lifecycle.identity();
            var pending = head.pendingMasterSelection().get();
            var creation = head.masterCreations().get().receipts().get(pending.transactionId());
            var terminal = new MasterSelectionProvenance.Step(
                new DrawCommitment(pending.pool(), pending.pool().totalWeight() - 1), "terminal");
            var receipt = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5,
                creation, pending, terminal);
            for (long balance : new long[] {1, 7, Long.MAX_VALUE})
            {
                var initial = new MasterSpinState(1, identity, 5, balance, Optional.empty());
                var allocation = MasterTerminalPreparation.prepare(head, balance, receipt).allocation();
                var decodedAllocation = MasterSpinAllocation.restore(receipt, balance);
                if (decodedAllocation.spinsBefore() != balance || decodedAllocation.spinsAfter() != balance - 1
                    || decodedAllocation.replay(identity, 6, receipt) != decodedAllocation
                    || decodedAllocation.fpDelta() != 0 || decodedAllocation.spinDelta() != -1)
                { throw new AssertionError("Restoration changed original accounting"); }
                rejects(() -> MasterSpinAllocation.restore(receipt, 0));
                rejects(() -> MasterSpinAllocation.restore(receipt, -1));
                var reserved = initial.reserve(lifecycle, allocation);
                var restored = new MasterSpinState(reserved.version(), identity, 6,
                    reserved.allotted(), reserved.reservation());
                if (reserved.allotted() != balance || reserved.available() != balance - 1
                    || initial.available() != balance || initial.reservation().isPresent()
                    || reserved.revision() != 6 || reserved.identity() != identity
                    || restored.replay(identity, 6, receipt) != allocation
                    || restored.replay(identity, Long.MAX_VALUE, receipt) != allocation)
                { throw new AssertionError("Reservation consumed balance or changed original accounting"); }
                rejects(() -> reserved.reserve(lifecycle, allocation));
                rejects(() -> initial.replay(identity, 5, receipt));
                rejects(() -> restored.replay(identity, 5, receipt));
                rejects(() -> restored.replay(new RunIdentity("foreign", identity.runId()), 6, receipt));
                rejects(() -> restored.replay(new RunIdentity(identity.accountKey(), UUID.randomUUID()), 6, receipt));
                var changed = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5,
                    creation, pending, terminal);
                rejects(() -> restored.replay(identity, 6, changed));
                rejects(() -> new MasterSpinState(1, identity, 5, balance, Optional.of(allocation)));
                rejects(() -> new MasterSpinState(1, identity, 6, balance - 1, Optional.of(allocation)));
                rejects(() -> new MasterSpinState(1, new RunIdentity("foreign", identity.runId()),
                    6, balance, Optional.of(allocation)));
                rejects(() -> new MasterSpinState(1, identity, 4, balance, Optional.empty())
                    .reserve(lifecycle, allocation));
                rejects(() -> initial.reserve(new RunLifecycle(identity, 6,
                    RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), allocation));
                rejects(() -> initial.reserve(new RunLifecycle(new RunIdentity("foreign", identity.runId()), 5,
                    RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), allocation));
                for (var state : RunLifecycle.State.values())
                {
                    if (state != RunLifecycle.State.ACTIVE)
                    { rejects(() -> initial.reserve(new RunLifecycle(identity, 5, state,
                        RunLifecycle.Obligation.NONE), allocation)); }
                }
                for (var obligation : RunLifecycle.Obligation.values())
                {
                    if (obligation != RunLifecycle.Obligation.NONE)
                    { rejects(() -> initial.reserve(new RunLifecycle(identity, 5,
                        RunLifecycle.State.ACTIVE, obligation), allocation)); }
                }
                if (balance > 1)
                { rejects(() -> new MasterSpinState(1, identity, 5, balance - 1, Optional.empty())
                    .reserve(lifecycle, allocation)); }
            }
            var zero = new MasterSpinState(1, identity, 5, 0, Optional.empty());
            if (zero.available() != 0) { throw new AssertionError("Zero balance changed"); }
            rejects(() -> zero.reserve(lifecycle, MasterSpinAllocation.prepare(lifecycle, 1, receipt)));
            rejects(() -> new MasterSpinState(0, identity, 5, 1, Optional.empty()));
            rejects(() -> new MasterSpinState(2, identity, 5, 1, Optional.empty()));
            rejects(() -> new MasterSpinState(1, identity, -1, 1, Optional.empty()));
            rejects(() -> new MasterSpinState(1, identity, 5, -1, Optional.empty()));
        }
        System.out.println("MasterSpinStateChecks PASSED");
    }
}

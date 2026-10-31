package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Dependency-free aggregate reservation binding and copy-path checks. */
public final class MasterSpinAggregateChecks
{
    static RunDocument attach(RunDocument head, MasterSpinState spins)
    {
        return new RunDocument(head.lifecycle(), head.catalogVersion(), head.receipts(), head.fatePoints(),
            head.inventory(), head.grandFate(), head.grandProgress(), head.grandJournal(),
            head.grandActivations(), head.grandCurrentAttempt(), head.pendingMasterSelection(),
            head.masterCreations(), head.masterResolutions(), Optional.of(spins));
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid spin aggregate admitted");
    }

    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            RunDocument resolved = MasterResolutionAggregateChecks.resolved(count);
            var receipt = resolved.masterResolutions().get().receipts().values().iterator().next();
            var id = resolved.lifecycle().identity();
            var before = new RunLifecycle(id, receipt.beforeRevision(), RunLifecycle.State.ACTIVE,
                RunLifecycle.Obligation.NONE);
            for (long balance : new long[] {1, 7, Long.MAX_VALUE})
            {
                var allocation = MasterSpinAllocation.prepare(before, balance, receipt);
                var spins = new MasterSpinState(1, id, before.revision(), balance, Optional.empty())
                    .reserve(before, allocation);
                RunDocument head = attach(resolved, spins);
                var paused = LifecycleAdmission.prepare(head, id, UUID.randomUUID(), 6, RunReceipt.Operation.PAUSE);
                var resumed = LifecycleAdmission.prepare(paused, id, UUID.randomUUID(), 7, RunReceipt.Operation.RESUME);
                if (resumed.masterSpins().orElseThrow() != spins
                    || spins.available() != balance - 1 || spins.allotted() != balance
                    || spins.replay(id, 8, receipt) != allocation || resolved.masterSpins().isPresent())
                { throw new AssertionError("Reservation lost, consumed twice or source changed"); }
                rejects(() -> attach(MasterCreationAggregateChecks.fixture(count), spins));
                rejects(() -> new RunDocument(resolved.lifecycle(), resolved.catalogVersion(), resolved.receipts(),
                    resolved.fatePoints(), resolved.inventory(), resolved.grandFate(), resolved.grandProgress(),
                    resolved.grandJournal(), resolved.grandActivations(), resolved.grandCurrentAttempt(),
                    Optional.empty(), resolved.masterCreations(), Optional.empty(), Optional.of(spins)));
                var other = MasterResolutionAggregateChecks.resolved(count);
                rejects(() -> attach(other, spins));
                rejects(() -> MasterCreationAdmission.prepare(head,
                    new MasterSelectionCreationReceipt(id, 6,
                        new MasterSelectionProvenance(UUID.randomUUID(), receipt.creation().initial().snapshotVersion(),
                            receipt.creation().initial().catalogVersion(), receipt.creation().initial().streamDescriptor(),
                            receipt.creation().initial().initialStreamState(), receipt.creation().initial().readiness(), List.of()))));
            }
            rejects(() -> attach(resolved, new MasterSpinState(1,
                new RunIdentity("foreign", id.runId()), 6, 1, Optional.empty())));
            rejects(() -> attach(resolved, new MasterSpinState(1, id, 7, 1, Optional.empty())));
            var empty = new MasterSpinState(1, id, 6, 0, Optional.empty());
            var original = receipt.creation().initial();
            var fresh = new MasterSelectionProvenance(UUID.randomUUID(), original.snapshotVersion(),
                original.catalogVersion(), original.streamDescriptor(), original.initialStreamState(),
                original.readiness(), List.of());
            var created = MasterCreationAdmission.prepare(attach(resolved, empty),
                new MasterSelectionCreationReceipt(id, 6, fresh));
            if (created.masterSpins().get() != empty)
            { throw new AssertionError("Creation copy dropped explicit state"); }
            if (attach(resolved, empty).masterSpins().get().available() != 0)
            { throw new AssertionError("Explicit zero lost"); }
        }
        var legacy = MasterCreationAggregateChecks.fixture(0);
        if (LifecycleAdmission.prepare(legacy, legacy.lifecycle().identity(), UUID.randomUUID(), 5,
            RunReceipt.Operation.PAUSE).masterSpins().isPresent())
        { throw new AssertionError("Legacy balance inferred"); }
        System.out.println("MasterSpinAggregateChecks PASSED");
    }
}

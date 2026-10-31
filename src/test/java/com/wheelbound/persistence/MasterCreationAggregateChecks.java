package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Dependency-free aggregate and lifecycle contracts, without disk/codec claims. */
public final class MasterCreationAggregateChecks
{
    static RunDocument fixture(int count)
    {
        RunDocument source = PendingLifecycleChecks.fixture(count);
        MasterSelectionProvenance pending = source.pendingMasterSelection().get();
        MasterSelectionProvenance zero = new MasterSelectionProvenance(pending.transactionId(),
            pending.snapshotVersion(), pending.catalogVersion(), pending.streamDescriptor(),
            pending.initialStreamState(), pending.readiness(), List.of());
        MasterSelectionCreationReceipt original = new MasterSelectionCreationReceipt(
            source.lifecycle().identity(), 3, zero);
        return copy(source, Optional.of(pending), Optional.of(new MasterSelectionCreationJournal(
            source.lifecycle().identity(), 5, List.of(original))));
    }

    static RunDocument copy(RunDocument source, Optional<MasterSelectionProvenance> pending,
        Optional<MasterSelectionCreationJournal> history)
    {
        return new RunDocument(source.lifecycle(), source.catalogVersion(), source.receipts(),
            source.fatePoints(), source.inventory(), source.grandFate(), source.grandProgress(),
            source.grandJournal(), source.grandActivations(), source.grandCurrentAttempt(), pending, history);
    }

    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            RunDocument source = fixture(count);
            RunIdentity id = source.lifecycle().identity();
            MasterSelectionCreationJournal history = source.masterCreations().get();
            RunDocument paused = LifecycleAdmission.prepare(source, id, new UUID(20, 1), 5, RunReceipt.Operation.PAUSE);
            RunDocument active = LifecycleAdmission.prepare(paused, id, new UUID(20, 2), 6, RunReceipt.Operation.RESUME);
            if (!active.masterCreations().get().receipts().equals(history.receipts())
                || active.pendingMasterSelection().get() != source.pendingMasterSelection().get())
            { throw new AssertionError("Lifecycle discarded original history"); }
            rejects(() -> LifecycleAdmission.prepare(source, id, PendingLifecycleChecks.TRANSACTION, 5, RunReceipt.Operation.PAUSE));
            RunDocument historical = copy(source, Optional.empty(), Optional.of(history));
            rejects(() -> LifecycleAdmission.prepare(historical, id, PendingLifecycleChecks.TRANSACTION, 5, RunReceipt.Operation.PAUSE));
            SealedGrandFate collision = new SealedGrandFate(id, PendingLifecycleChecks.TRANSACTION, GrandFatePool.atTicket(0));
            rejects(() -> new RunDocument(source.lifecycle(), "catalog", List.of(), Optional.empty(),
                Optional.empty(), Optional.of(collision), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.of(history)));
            rejects(() -> copy(source, source.pendingMasterSelection(), Optional.of(
                new MasterSelectionCreationJournal(id, 5, List.of()))));
            rejects(() -> copy(source, source.pendingMasterSelection(), Optional.of(
                new MasterSelectionCreationJournal(new RunIdentity("foreign", id.runId()), 5, List.of()))));
            MasterSelectionProvenance p = source.pendingMasterSelection().get();
            MasterSelectionProvenance changed = new MasterSelectionProvenance(p.transactionId(), "changed",
                p.catalogVersion(), p.streamDescriptor(), p.initialStreamState(), p.readiness(), List.of());
            rejects(() -> copy(source, Optional.of(changed), Optional.of(history)));
            RunDocument older = new RunDocument(new RunLifecycle(id, 3, RunLifecycle.State.ACTIVE,
                RunLifecycle.Obligation.NONE), "catalog");
            rejects(() -> copy(older, Optional.empty(), Optional.of(history)));
            // Completed historical selections need not remain pending.
            if (copy(source, Optional.empty(), Optional.of(history)).masterCreations().get().receipts().size() != 1)
            { throw new AssertionError("Lost historical selection"); }
        }
        if (PendingLifecycleChecks.fixture(0).masterCreations().isPresent())
        { throw new AssertionError("Legacy absence inferred history"); }
        System.out.println("MasterCreationAggregateChecks PASSED");
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid aggregate admitted");
    }
}

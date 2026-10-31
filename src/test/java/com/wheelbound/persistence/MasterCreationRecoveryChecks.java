package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Executable recovery contracts without codec or filesystem dependencies. */
public final class MasterCreationRecoveryChecks
{
    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            RunDocument source = MasterCreationAggregateChecks.fixture(count);
            MasterSelectionCreationReceipt original = source.masterCreations().get().receipts()
                .values().iterator().next();
            MasterSelectionCreationReceipt equal = new MasterSelectionCreationReceipt(original.identity(),
                original.beforeRevision(), original.initial());
            RunDocument paused = LifecycleAdmission.prepare(source, original.identity(),
                new UUID(30, 1), 5, RunReceipt.Operation.PAUSE);
            for (RunDocument head : List.of(source, paused,
                MasterCreationAggregateChecks.copy(paused, Optional.empty(), paused.masterCreations())))
            {
                if (MasterCreationRecovery.find(head, equal).get() != original)
                { throw new AssertionError("Recovery did not return retained original"); }
            }
            if (source.pendingMasterSelection().get().drawCount() != count)
            { throw new AssertionError("Recovery changed transcript"); }
            rejects(() -> MasterCreationRecovery.find(source, new MasterSelectionCreationReceipt(
                original.identity(), original.beforeRevision() + 1, original.initial())));
            rejects(() -> MasterCreationRecovery.find(source, new MasterSelectionCreationReceipt(
                new RunIdentity("foreign", original.identity().runId()), 3, original.initial())));
            MasterSelectionProvenance p = original.initial();
            rejects(() -> MasterCreationRecovery.find(source, new MasterSelectionCreationReceipt(
                original.identity(), 3, new MasterSelectionProvenance(p.transactionId(), "changed",
                    p.catalogVersion(), p.streamDescriptor(), p.initialStreamState(), p.readiness(), List.of()))));
            // A legacy pending selection cannot establish its original creation receipt.
            rejects(() -> MasterCreationRecovery.find(PendingLifecycleChecks.fixture(count), equal));
            MasterSelectionCreationReceipt absent = request(original, new UUID(30, 2));
            if (MasterCreationRecovery.find(source, absent).isPresent())
            { throw new AssertionError("Invented absent creation"); }
            rejects(() -> MasterCreationRecovery.find(paused, request(original, new UUID(30, 1))));
            RunDocument sealed = new RunDocument(source.lifecycle(), "catalog", List.of(),
                Optional.empty(), Optional.empty(), Optional.of(new SealedGrandFate(original.identity(),
                    absent.transactionId(), GrandFatePool.atTicket(0))));
            rejects(() -> MasterCreationRecovery.find(sealed, absent));
        }
        System.out.println("MasterCreationRecoveryChecks PASSED");
    }

    static MasterSelectionCreationReceipt request(MasterSelectionCreationReceipt source, UUID id)
    {
        MasterSelectionProvenance p = source.initial();
        return new MasterSelectionCreationReceipt(source.identity(), source.beforeRevision(),
            new MasterSelectionProvenance(id, p.snapshotVersion(), p.catalogVersion(),
                p.streamDescriptor(), p.initialStreamState(), p.readiness(), List.of()));
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid recovery accepted");
    }
}

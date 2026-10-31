package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Typed creation participation in the lifecycle chain; no creation authorization. */
public final class MasterCreationOperationChecks
{
    static RunDocument fixture(int count)
    {
        RunDocument source = MasterCreationAggregateChecks.fixture(count);
        MasterSelectionCreationReceipt original = source.masterCreations().get().receipts().values().iterator().next();
        RunReceipt creation = RunReceipt.masterSelectionCreation(original);
        RunReceipt pause = new RunReceipt(new UUID(31, 1), RunReceipt.Operation.PAUSE, 4, 5,
            RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
        RunDocument paused = document(source, new RunLifecycle(source.lifecycle().identity(), 5,
            RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE), List.of(creation, pause), source.masterCreations());
        return LifecycleAdmission.prepare(paused, source.lifecycle().identity(), new UUID(31, 2), 5, RunReceipt.Operation.RESUME);
    }

    static RunDocument document(RunDocument source, RunLifecycle head, List<RunReceipt> operations,
        Optional<MasterSelectionCreationJournal> history)
    {
        return new RunDocument(head, source.catalogVersion(), operations, source.fatePoints(), source.inventory(),
            source.grandFate(), source.grandProgress(), source.grandJournal(), source.grandActivations(),
            source.grandCurrentAttempt(), source.pendingMasterSelection(), history);
    }

    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            RunDocument source = fixture(count);
            MasterSelectionCreationReceipt original = source.receipts().get(0).masterCreation();
            check(source.lifecycle().revision() == 6 && source.pendingMasterSelection().get().drawCount() == count);
            check(source.fatePoints().get().balance() == -3);
            check(MasterCreationRecovery.find(source, original).get().equals(original));
            check(LifecycleAdmission.prepare(source, source.lifecycle().identity(), new UUID(31, 1), 4,
                RunReceipt.Operation.PAUSE) == source);
            rejects(() -> document(source, source.lifecycle(), source.receipts(), Optional.empty()));
            MasterSelectionProvenance p = original.initial();
            MasterSelectionCreationReceipt changed = new MasterSelectionCreationReceipt(original.identity(), 3,
                new MasterSelectionProvenance(p.transactionId(), "changed", p.catalogVersion(), p.streamDescriptor(),
                    p.initialStreamState(), p.readiness(), List.of()));
            rejects(() -> document(source, source.lifecycle(), source.receipts(), Optional.of(
                new MasterSelectionCreationJournal(original.identity(), 6, List.of(changed)))));
            // A retained chain cannot silently omit a creation in its revision interval.
            // Use a lifecycle operation to test omission without requiring a payment payload.
            List<RunReceipt> missing = List.of(new RunReceipt(new UUID(32, 1), RunReceipt.Operation.RESUME,
                3, 4, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE));
            final List<RunReceipt> omitted = missing;
            rejects(() -> document(source, new RunLifecycle(original.identity(), 4, RunLifecycle.State.ACTIVE,
                RunLifecycle.Obligation.NONE), omitted, source.masterCreations()));
            RunReceipt preceding = new RunReceipt(new UUID(33, 1), RunReceipt.Operation.PAUSE, 2, 3,
                RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
            rejects(() -> document(source, new RunLifecycle(original.identity(), 4, RunLifecycle.State.ACTIVE,
                RunLifecycle.Obligation.NONE), List.of(preceding, source.receipts().get(0)), source.masterCreations()));
            RunReceipt obligated = new RunReceipt(new UUID(33, 2), RunReceipt.Operation.RESUME, 2, 3,
                RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
            rejects(() -> document(source, new RunLifecycle(original.identity(), 4, RunLifecycle.State.ACTIVE,
                RunLifecycle.Obligation.NONE), List.of(obligated, source.receipts().get(0)), source.masterCreations()));
        }
        System.out.println("MasterCreationOperationChecks PASSED");
    }
    private static void check(boolean value) { if (!value) { throw new AssertionError("Creation chain mismatch"); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid creation chain admitted");
    }
}

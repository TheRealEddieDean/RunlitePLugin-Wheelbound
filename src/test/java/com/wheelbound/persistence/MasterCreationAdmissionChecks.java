package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Dependency-free preparation checks; no durability or trusted evidence claim. */
public final class MasterCreationAdmissionChecks
{
    static RunDocument base(RunDocument source, RunLifecycle head,
        Optional<MasterSelectionCreationJournal> history)
    {
        return new RunDocument(head, source.catalogVersion(), List.of(), source.fatePoints(),
            source.inventory(), source.grandFate(), source.grandProgress(), source.grandJournal(),
            source.grandActivations(), Optional.empty(), Optional.empty(), history);
    }

    public static void main(String[] args)
    {
        RunDocument source = MasterCreationOperationChecks.fixture(0);
        MasterSelectionCreationReceipt request = source.receipts().get(0).masterCreation();
        RunIdentity id = request.identity();
        RunLifecycle head = new RunLifecycle(id, request.beforeRevision(), RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE);
        Optional<MasterSelectionCreationJournal> empty = Optional.of(
            new MasterSelectionCreationJournal(id, head.revision(), List.of()));
        RunDocument before = base(source, head, empty);
        RunDocument after = MasterCreationAdmission.prepare(before, request);
        check(after.lifecycle().revision() == request.afterRevision());
        check(after.receipts().size() == 1 && after.receipts().get(0).masterCreation().equals(request));
        check(after.pendingMasterSelection().get() == request.initial());
        check(after.masterCreations().get().receipts().get(request.transactionId()).equals(request));
        check(after.fatePoints().get() == before.fatePoints().get());
        check(before.receipts().isEmpty() && before.pendingMasterSelection().isEmpty());
        check(MasterCreationAdmission.prepare(after, request) == after);
        RunDocument historical = MasterCreationAggregateChecks.copy(after, Optional.empty(), after.masterCreations());
        check(MasterCreationAdmission.prepare(historical, request) == historical);
        MasterSelectionProvenance initial = request.initial();
        rejects(() -> MasterCreationAdmission.prepare(before, new MasterSelectionCreationReceipt(
            new RunIdentity("foreign", id.runId()), 3, initial)));
        rejects(() -> MasterCreationAdmission.prepare(before, new MasterSelectionCreationReceipt(id, 3,
            new MasterSelectionProvenance(initial.transactionId(), initial.snapshotVersion(), "foreign-catalog",
                initial.streamDescriptor(), initial.initialStreamState(), initial.readiness(), List.of()))));
        rejects(() -> MasterCreationAdmission.prepare(base(source, head, Optional.empty()), request));
        rejects(() -> MasterCreationAdmission.prepare(base(source, new RunLifecycle(id, 2,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), empty), request));
        for (RunLifecycle.State state : RunLifecycle.State.values())
        {
            if (state == RunLifecycle.State.ACTIVE) { continue; }
            rejects(() -> MasterCreationAdmission.prepare(base(source, new RunLifecycle(id, 3, state,
                RunLifecycle.Obligation.NONE), empty), request));
        }
        rejects(() -> MasterCreationAdmission.prepare(base(source, new RunLifecycle(id, 3,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE), empty), request));
        for (int count : new int[] {0, 1, 513})
        {
            RunDocument advanced = MasterCreationOperationChecks.fixture(count);
            check(MasterCreationAdmission.prepare(advanced, request) == advanced);
            RunDocument paused = LifecycleAdmission.prepare(advanced, id, new UUID(80, count), 6,
                RunReceipt.Operation.PAUSE);
            check(MasterCreationAdmission.prepare(paused, request) == paused);
            MasterSelectionProvenance p = request.initial();
            MasterSelectionCreationReceipt changed = new MasterSelectionCreationReceipt(id, 3,
                new MasterSelectionProvenance(p.transactionId(), "changed", p.catalogVersion(),
                    p.streamDescriptor(), p.initialStreamState(), p.readiness(), List.of()));
            rejects(() -> MasterCreationAdmission.prepare(advanced, changed));
            MasterSelectionCreationReceipt another = new MasterSelectionCreationReceipt(id, 6,
                new MasterSelectionProvenance(new UUID(81, count), p.snapshotVersion(), p.catalogVersion(),
                    p.streamDescriptor(), p.initialStreamState(), p.readiness(), List.of()));
            rejects(() -> MasterCreationAdmission.prepare(advanced, another));
            MasterSelectionCreationReceipt collision = new MasterSelectionCreationReceipt(id, 6,
                new MasterSelectionProvenance(new UUID(31, 1), p.snapshotVersion(), p.catalogVersion(),
                    p.streamDescriptor(), p.initialStreamState(), p.readiness(), List.of()));
            rejects(() -> MasterCreationAdmission.prepare(advanced, collision));
        }
        System.out.println("MasterCreationAdmissionChecks PASSED");
    }
    private static void check(boolean value) { if (!value) { throw new AssertionError("Creation admission mismatch"); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid creation admitted");
    }
}

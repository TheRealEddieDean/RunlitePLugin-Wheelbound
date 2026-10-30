package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Dependency-free admission checks; persistence is covered separately. */
public final class PendingLifecycleChecks
{
    static final RunIdentity ID = new RunIdentity("pending-lifecycle", new UUID(9, 1));
    static final UUID TRANSACTION = new UUID(9, 2);

    static RunDocument fixture(int count)
    {
        List<ActiveWheel.Slice> slices = new ArrayList<>();
        Map<UUID, MasterSelectionReadiness.Classification> classifications = new HashMap<>();
        for (int i = 0; i < 5; i++)
        {
            UUID id = new UUID(0, i + 1);
            slices.add(ActiveWheel.Slice.ordinary(id, "activity-" + i % 3, ActiveWheel.Enhancement.BASE));
            classifications.put(id, new MasterSelectionReadiness.Classification(i == 4
                ? MasterSelectionReadiness.Availability.ASSIGNABLE
                : MasterSelectionReadiness.Availability.VERIFIED_EMPTY, "reason-" + i));
        }
        MasterSelectionReadiness readiness = new MasterSelectionReadiness(new ActiveWheel(slices), classifications);
        MasterSelectionProvenance initial = new MasterSelectionProvenance(TRANSACTION, "snapshot", "catalog",
            "opaque-stream", "cursor-0", readiness, List.of());
        List<MasterSelectionProvenance.Step> steps = new ArrayList<>();
        for (int i = 0; i < count; i++)
        { steps.add(new MasterSelectionProvenance.Step(new DrawCommitment(initial.pool(), 0), "cursor-" + (i + 1))); }
        MasterSelectionProvenance pending = new MasterSelectionProvenance(TRANSACTION, "snapshot", "catalog",
            "opaque-stream", "cursor-0", readiness, steps);
        return new RunDocument(new RunLifecycle(ID, 5, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
            "catalog", List.of(), Optional.of(new FatePointLedger(-3, List.of())), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(pending));
    }

    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            RunDocument source = fixture(count);
            UUID pause = new UUID(10, 1), resume = new UUID(10, 2);
            RunDocument paused = LifecycleAdmission.prepare(source, ID, pause, 5, RunReceipt.Operation.PAUSE);
            RunDocument active = LifecycleAdmission.prepare(paused, ID, resume, 6, RunReceipt.Operation.RESUME);
            if (paused.lifecycle().state() != RunLifecycle.State.PAUSED || active.lifecycle().revision() != 7
                || active.pendingMasterSelection().get() != source.pendingMasterSelection().get()
                || active.fatePoints().get() != source.fatePoints().get()
                || active.receipts().size() != 2 || source.lifecycle().revision() != 5)
            { throw new AssertionError("Lifecycle changed frozen selection or unrelated state"); }
            if (LifecycleAdmission.prepare(active, ID, pause, 5, RunReceipt.Operation.PAUSE) != active)
            { throw new AssertionError("Historical replay replaced current head"); }
            rejects(() -> LifecycleAdmission.prepare(source, ID, TRANSACTION, 5, RunReceipt.Operation.PAUSE));
            rejects(() -> LifecycleAdmission.prepare(source, ID, pause, 4, RunReceipt.Operation.PAUSE));
            rejects(() -> LifecycleAdmission.prepare(active, ID, pause, 5, RunReceipt.Operation.RESUME));
            rejects(() -> LifecycleAdmission.prepare(active, ID, pause, 4, RunReceipt.Operation.PAUSE));
            rejects(() -> LifecycleAdmission.prepare(active, new RunIdentity("foreign", ID.runId()),
                pause, 5, RunReceipt.Operation.PAUSE));
            rejects(() -> LifecycleAdmission.prepare(source, ID, pause, 5, RunReceipt.Operation.DRAW));
        }
        System.out.println("PendingLifecycleChecks PASSED");
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid lifecycle request admitted");
    }
}

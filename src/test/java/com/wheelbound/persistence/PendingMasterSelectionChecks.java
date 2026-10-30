package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Dependency-free structural aggregate checks; does not exercise Gson or disk writes. */
public final class PendingMasterSelectionChecks
{
    private static final RunIdentity ID = new RunIdentity("pending-check", new UUID(1, 1));

    public static void main(String[] args)
    {
        List<ActiveWheel.Slice> slices = new ArrayList<>();
        Map<UUID, MasterSelectionReadiness.Classification> evidence = new HashMap<>();
        for (int i = 0; i < 5; i++)
        {
            UUID slice = new UUID(0, i + 1);
            slices.add(ActiveWheel.Slice.ordinary(slice, "activity-" + i % 3, ActiveWheel.Enhancement.BASE));
            evidence.put(slice, new MasterSelectionReadiness.Classification(i == 4
                ? MasterSelectionReadiness.Availability.ASSIGNABLE
                : MasterSelectionReadiness.Availability.VERIFIED_EMPTY, "reason-" + i));
        }
        MasterSelectionReadiness readiness = new MasterSelectionReadiness(new ActiveWheel(slices), evidence);
        MasterSelectionProvenance initial = transcript(readiness, List.of());
        MasterSelectionProvenance empty = transcript(readiness, List.of(
            new MasterSelectionProvenance.Step(new DrawCommitment(initial.pool(), 0), "cursor-1")));
        MasterSelectionProvenance valid = transcript(readiness, List.of(
            new MasterSelectionProvenance.Step(new DrawCommitment(initial.pool(), 19), "cursor-valid")));
        for (RunLifecycle.State state : RunLifecycle.State.values())
        for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
        {
            RunLifecycle lifecycle = new RunLifecycle(ID, 5, state, obligation);
            for (MasterSelectionProvenance pending : List.of(initial, empty))
            {
                if ((state == RunLifecycle.State.ACTIVE || state == RunLifecycle.State.PAUSED)
                    && obligation == RunLifecycle.Obligation.NONE)
                {
                    RunDocument aggregate = document(lifecycle, "catalog", pending, List.of());
                    if (aggregate.pendingMasterSelection().get() != pending
                        || aggregate.lifecycle() != lifecycle || !aggregate.receipts().isEmpty())
                    { throw new AssertionError("Structural payload changed"); }
                }
                else { rejects(() -> document(lifecycle, "catalog", pending, List.of())); }
            }
        }
        RunLifecycle active = new RunLifecycle(ID, 5, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        rejects(() -> document(active, "changed", empty, List.of()));
        rejects(() -> document(active, "catalog", valid, List.of()));
        RunLifecycle paused = new RunLifecycle(ID, 5, RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
        RunReceipt collision = new RunReceipt(empty.transactionId(), RunReceipt.Operation.PAUSE, 4, 5,
            RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
        rejects(() -> document(paused, "catalog", empty, List.of(collision)));
        SealedGrandFate seal = new SealedGrandFate(ID, new UUID(3, 3), GrandFatePool.atTicket(0));
        GrandActivationHistory history = new GrandActivationHistory(seal, 5, List.of());
        RunDocument pendingWithGrand = new RunDocument(active, "catalog", List.of(), Optional.empty(),
            Optional.empty(), Optional.of(seal), Optional.empty(), Optional.empty(), Optional.of(history),
            Optional.empty(), Optional.of(empty));
        GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
        GrandActivationReceipt request = new GrandActivationReceipt(new UUID(4, 4), ID, 5, checklist,
            checklist.unfinished().iterator().next());
        rejects(() -> GrandActivationAdmission.prepare(pendingWithGrand, request, ID, 5));
        if (new RunDocument(active, "catalog").pendingMasterSelection().isPresent())
        { throw new AssertionError("Legacy absence invented selection"); }
        System.out.println("PendingMasterSelectionChecks PASSED");
    }

    private static MasterSelectionProvenance transcript(MasterSelectionReadiness readiness,
        List<MasterSelectionProvenance.Step> steps)
    {
        return new MasterSelectionProvenance(new UUID(2, 2), "snapshot", "catalog",
            "opaque-stream", "cursor-0", readiness, steps);
    }

    private static RunDocument document(RunLifecycle lifecycle, String catalog,
        MasterSelectionProvenance pending, List<RunReceipt> receipts)
    {
        return new RunDocument(lifecycle, catalog, receipts, Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(pending));
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid pending context accepted");
    }
}

package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;
import static com.wheelbound.domain.MasterSelectionReadiness.*;

/** Offline aggregate binding checks; no trusted detector or session claim. */
public final class MasterAggregateEvidenceChecks
{
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected aggregate context refusal");
    }

    public static void main(String[] args)
    {
        RunDocument pending = MasterCreationOperationChecks.fixture(0);
        RunIdentity id = pending.lifecycle().identity();
        List<ActiveWheel.Slice> slices = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        {
            slices.add(ActiveWheel.Slice.ordinary(UUID.randomUUID(), "activity" + (i % 3),
                ActiveWheel.Enhancement.BASE));
        }
        SliceInventory inventory = new SliceInventory(new ActiveWheel(slices), List.of(),
            SliceInventory.SatchelTier.BASE, Set.of("activity0", "activity1", "activity2"),
            Map.of("activity0", new SliceInventory.AcquisitionHistory(0),
                "activity1", new SliceInventory.AcquisitionHistory(0),
                "activity2", new SliceInventory.AcquisitionHistory(0)));
        RunDocument current = new RunDocument(new RunLifecycle(id, 6, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), pending.catalogVersion(), List.of(), pending.fatePoints(), Optional.of(inventory));
        Map<String, MasterOrdinaryPoolEvidence> pools = new HashMap<>();
        for (ActiveWheel.Slice slice : current.inventory().get().activeWheel().slices())
        {
            pools.put(slice.activity(), new MasterOrdinaryPoolEvidence(id, 6, "snapshot",
                current.catalogVersion(), slice.activity(), Set.of("normal"), Map.of("normal",
                    new Classification(Availability.ASSIGNABLE, "Offline complete fixture"))));
        }
        MasterSelectionReadiness ready = MasterAggregateEvidence.assemble(current, id, 6, "snapshot", pools);
        if (ready.status() != Status.READY || ready.wheel() != current.inventory().get().activeWheel()
            || current.lifecycle().revision() != 6 || current.pendingMasterSelection().isPresent()
            || current.fatePoints().get() != pending.fatePoints().get())
        { throw new AssertionError("Aggregate binding or immutability failed"); }
        rejects(() -> MasterAggregateEvidence.assemble(current, id, 5, "snapshot", pools));
        rejects(() -> MasterAggregateEvidence.assemble(current,
            new RunIdentity("foreign", id.runId()), 6, "snapshot", pools));
        rejects(() -> MasterAggregateEvidence.assemble(current,
            new RunIdentity(id.accountKey(), UUID.randomUUID()), 6, "snapshot", pools));
        rejects(() -> MasterAggregateEvidence.assemble(current, id, 6, "changed", pools));
        rejects(() -> MasterAggregateEvidence.assemble(pending, id, 6, "snapshot", pools));
        rejects(() -> MasterAggregateEvidence.assemble(new RunDocument(current.lifecycle(),
            current.catalogVersion()), id, 6, "snapshot", pools));
        rejects(() -> MasterAggregateEvidence.assemble(new RunDocument(current.lifecycle(),
            "changed", List.of(), current.fatePoints(), current.inventory()), id, 6, "snapshot", pools));
        for (RunLifecycle.State state : RunLifecycle.State.values())
        {
            if (state == RunLifecycle.State.ACTIVE) { continue; }
            RunDocument unavailable = new RunDocument(new RunLifecycle(id, 6, state,
                RunLifecycle.Obligation.NONE), current.catalogVersion(), List.of(), current.fatePoints(), current.inventory());
            rejects(() -> MasterAggregateEvidence.assemble(unavailable, id, 6, "snapshot", pools));
        }
        for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
        {
            if (obligation == RunLifecycle.Obligation.NONE) { continue; }
            RunDocument occupied = new RunDocument(new RunLifecycle(id, 6, RunLifecycle.State.ACTIVE,
                obligation), current.catalogVersion(), List.of(), current.fatePoints(), current.inventory());
            rejects(() -> MasterAggregateEvidence.assemble(occupied, id, 6, "snapshot", pools));
        }
        System.out.println("MasterAggregateEvidenceChecks PASSED");
    }
}

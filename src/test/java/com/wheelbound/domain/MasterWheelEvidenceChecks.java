package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import static com.wheelbound.domain.MasterSelectionReadiness.*;

/** Offline assembly fixtures; no live evidence claim. */
public final class MasterWheelEvidenceChecks
{
    private static final RunIdentity ID = new RunIdentity("offline", UUID.randomUUID());
    private static MasterOrdinaryPoolEvidence pool(String activity, Availability availability)
    {
        return new MasterOrdinaryPoolEvidence(ID, 7, "snapshot", "catalog", activity,
            Set.of("normal"), Map.of("normal", new Classification(availability, "Offline fixture")));
    }
    private static SliceInventory inventory(List<ActiveWheel.Slice> slices)
    {
        return new SliceInventory(new ActiveWheel(slices),
            List.of(ActiveWheel.Slice.ordinary(UUID.randomUUID(), "stored", ActiveWheel.Enhancement.BASE)),
            SliceInventory.SatchelTier.BASE, Set.of("a", "b", "c", "stored"), Map.of("a", new SliceInventory.AcquisitionHistory(0),
                "b", new SliceInventory.AcquisitionHistory(0), "c", new SliceInventory.AcquisitionHistory(0),
                "stored", new SliceInventory.AcquisitionHistory(0)));
    }
    private static MasterSelectionReadiness assemble(SliceInventory inventory, Map<String, MasterOrdinaryPoolEvidence> pools)
    { return MasterWheelEvidence.assemble(ID, 7, "snapshot", "catalog", inventory, pools); }
    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Wheel evidence assembly violated"); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected context/coverage rejection");
    }
    public static void main(String[] args)
    {
        ArrayList<ActiveWheel.Slice> slices = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        {
            slices.add(ActiveWheel.Slice.ordinary(UUID.randomUUID(), new String[] {"a", "b", "c"}[i % 3],
                i == 3 ? ActiveWheel.Enhancement.FOURTH : ActiveWheel.Enhancement.BASE));
        }
        SliceInventory inventory = inventory(slices);
        Map<String, MasterOrdinaryPoolEvidence> pools = new HashMap<>();
        pools.put("a", pool("a", Availability.ASSIGNABLE));
        pools.put("b", pool("b", Availability.VERIFIED_EMPTY));
        pools.put("c", pool("c", Availability.VERIFIED_EMPTY));
        MasterSelectionReadiness ready = assemble(inventory, pools);
        check(ready.status() == Status.READY && ready.wheel() == inventory.activeWheel());
        check(ready.wheel().totalWeightUnits() == 28 && ready.classifications().size() == 5);
        check(ready.classifyLanding(slices.get(3).id()) == Landing.ORDINARY_ASSIGNMENT);
        check(ready.classifyLanding(slices.get(1).id()) == Landing.EMPTY_ORDINARY_RETRY);
        pools.put("a", pool("a", Availability.VERIFIED_EMPTY));
        check(assemble(inventory, pools).status() == Status.NO_VALID_FATE);
        check(ready.status() == Status.READY); // Previous frozen assembly is independent.
        for (Availability unavailable : List.of(Availability.UNKNOWN, Availability.UNSUPPORTED))
        {
            pools.put("a", pool("a", unavailable));
            check(assemble(inventory, pools).status() == Status.WAITING_FOR_EVIDENCE);
        }
        pools.remove("a");
        rejects(() -> assemble(inventory, pools));
        pools.put("a", pool("b", Availability.ASSIGNABLE));
        rejects(() -> assemble(inventory, pools));
        pools.put("a", pool("a", Availability.ASSIGNABLE));
        pools.put("stored", pool("stored", Availability.ASSIGNABLE));
        rejects(() -> assemble(inventory, pools));
        pools.remove("stored");
        rejects(() -> MasterWheelEvidence.assemble(ID, 8, "snapshot", "catalog", inventory, pools));
        rejects(() -> MasterWheelEvidence.assemble(ID, 7, "changed", "catalog", inventory, pools));
        rejects(() -> MasterWheelEvidence.assemble(ID, 7, "snapshot", "changed", inventory, pools));
        rejects(() -> MasterWheelEvidence.assemble(new RunIdentity("foreign", ID.runId()), 7,
            "snapshot", "catalog", inventory, pools));
        rejects(() -> MasterWheelEvidence.assemble(new RunIdentity(ID.accountKey(), UUID.randomUUID()), 7,
            "snapshot", "catalog", inventory, pools));
        rejects(() -> MasterWheelEvidence.assemble(ID, -1, "snapshot", "catalog", inventory, pools));
        for (ActiveWheel.Kind kind : List.of(ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE))
        {
            ArrayList<ActiveWheel.Slice> specialSlices = new ArrayList<>(slices);
            ActiveWheel.Slice special = ActiveWheel.Slice.special(UUID.randomUUID(), kind, 9);
            specialSlices.add(special);
            MasterSelectionReadiness blocked = assemble(inventory(specialSlices), pools);
            check(blocked.status() == Status.WAITING_FOR_EVIDENCE);
            check(blocked.wheel().totalWeightUnits() == 37);
            check(blocked.classifications().get(special.id()).availability() == Availability.UNSUPPORTED);
        }
        pools.clear();
        check(ready.classifications().size() == 5);
        System.out.println("MasterWheelEvidenceChecks PASSED");
    }
}

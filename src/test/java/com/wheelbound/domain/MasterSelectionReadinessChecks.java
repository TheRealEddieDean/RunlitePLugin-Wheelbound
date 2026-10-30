package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static com.wheelbound.domain.MasterSelectionReadiness.*;

/** Offline policy checks only; no reviewed eligibility, RNG, allocation or durability proof. */
public final class MasterSelectionReadinessChecks
{
    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Readiness policy violated"); } }

    private static void rejects(Class<? extends Throwable> type, Runnable action)
    {
        try { action.run(); }
        catch (Throwable failure)
        {
            if (type.isInstance(failure)) { return; }
            throw new AssertionError(failure);
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }

    public static void main(String[] args)
    {
        ArrayList<ActiveWheel.Slice> slices = new ArrayList<>();
        Map<UUID, Classification> inputs = new HashMap<>();
        for (int i = 0; i < 5; i++)
        {
            ActiveWheel.Slice slice = ActiveWheel.Slice.ordinary(UUID.randomUUID(),
                "activity-" + (i % 3), ActiveWheel.Enhancement.values()[i]);
            slices.add(slice);
            inputs.put(slice.id(), new Classification(Availability.VERIFIED_EMPTY, "Complete empty pool"));
        }
        ActiveWheel ordinary = new ActiveWheel(slices);
        MasterSelectionReadiness empty = new MasterSelectionReadiness(ordinary, inputs);
        check(empty.status() == Status.NO_VALID_FATE);
        rejects(IllegalStateException.class, () -> empty.classifyLanding(slices.get(0).id()));
        UUID valid = slices.get(4).id();
        inputs.put(valid, new Classification(Availability.ASSIGNABLE, "Reviewed supported assignment"));
        MasterSelectionReadiness ready = new MasterSelectionReadiness(ordinary, inputs);
        check(ready.status() == Status.READY);
        check(ready.classifyLanding(valid) == Landing.ORDINARY_ASSIGNMENT);
        check(ready.classifyLanding(slices.get(0).id()) == Landing.EMPTY_ORDINARY_RETRY);
        check(ready.wheel() == ordinary && ordinary.totalWeightUnits() == 35);
        check(empty.classifications().get(valid).availability() == Availability.VERIFIED_EMPTY);
        rejects(UnsupportedOperationException.class, () -> ready.classifications().clear());
        for (Availability blocked : new Availability[] {Availability.UNKNOWN, Availability.UNSUPPORTED})
        {
            inputs.put(slices.get(0).id(), new Classification(blocked, "Incomplete support"));
            MasterSelectionReadiness waiting = new MasterSelectionReadiness(ordinary, inputs);
            check(waiting.status() == Status.WAITING_FOR_EVIDENCE);
            rejects(IllegalStateException.class, () -> waiting.classifyLanding(valid));
        }
        inputs.remove(slices.get(0).id());
        rejects(IllegalArgumentException.class, () -> new MasterSelectionReadiness(ordinary, inputs));
        inputs.put(slices.get(0).id(), new Classification(Availability.VERIFIED_EMPTY, "Empty"));
        inputs.put(UUID.randomUUID(), new Classification(Availability.ASSIGNABLE, "Foreign"));
        rejects(IllegalArgumentException.class, () -> new MasterSelectionReadiness(ordinary, inputs));
        rejects(IllegalArgumentException.class, () -> ready.classifyLanding(UUID.randomUUID()));
        rejects(IllegalArgumentException.class, () -> new Classification(Availability.UNKNOWN, " "));
        rejects(IllegalArgumentException.class, () -> new Classification(Availability.UNKNOWN, " padded"));
        rejects(NullPointerException.class, () -> new Classification(null, "Reason"));
        for (ActiveWheel.Kind kind : new ActiveWheel.Kind[] {ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE})
        {
            ArrayList<ActiveWheel.Slice> specialSlices = new ArrayList<>(slices);
            ActiveWheel.Slice special = ActiveWheel.Slice.special(UUID.randomUUID(), kind, 7);
            specialSlices.add(special);
            ActiveWheel wheel = new ActiveWheel(specialSlices);
            Map<UUID, Classification> statuses = new HashMap<>(empty.classifications());
            statuses.put(special.id(), new Classification(Availability.ASSIGNABLE, "Supported mandatory recovery"));
            MasterSelectionReadiness supported = new MasterSelectionReadiness(wheel, statuses);
            check(supported.status() == Status.READY);
            check(supported.classifyLanding(special.id()) == Landing.SPECIAL_OBLIGATION);
            check(supported.classifyLanding(slices.get(0).id()) == Landing.EMPTY_ORDINARY_RETRY);
            check(supported.wheel().totalWeightUnits() == 42);
            statuses.put(special.id(), new Classification(Availability.UNSUPPORTED, "No adapter"));
            check(new MasterSelectionReadiness(wheel, statuses).status() == Status.WAITING_FOR_EVIDENCE);
            statuses.put(special.id(), new Classification(Availability.VERIFIED_EMPTY, "No owned candidates"));
            rejects(IllegalArgumentException.class, () -> new MasterSelectionReadiness(wheel, statuses));
        }
        System.out.println("Master selection readiness checks passed");
    }
}

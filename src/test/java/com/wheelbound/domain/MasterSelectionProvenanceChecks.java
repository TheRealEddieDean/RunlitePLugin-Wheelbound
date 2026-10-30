package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static com.wheelbound.domain.MasterSelectionReadiness.*;

/** Offline transcript validation; no stream authenticity, durability or live admission proof. */
public final class MasterSelectionProvenanceChecks
{
    private static final UUID TRANSACTION = UUID.fromString("00000000-0000-0000-0000-000000000099");

    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Selection provenance violated"); } }

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

    private static MasterSelectionProvenance restore(MasterSelectionReadiness readiness,
        List<MasterSelectionProvenance.Step> steps)
    {
        return new MasterSelectionProvenance(TRANSACTION, "snapshot-7", "catalog-3",
            "test-stream-descriptor", "seed-and-cursor-0", readiness, steps);
    }

    private static MasterSelectionProvenance.Step step(WeightedPool pool, int ticket, String state)
    { return new MasterSelectionProvenance.Step(new DrawCommitment(pool, ticket), state); }

    public static void main(String[] args)
    {
        List<ActiveWheel.Slice> slices = new ArrayList<>();
        Map<UUID, Classification> classifications = new HashMap<>();
        for (int i = 0; i < 5; i++)
        {
            UUID id = new UUID(0, i + 1);
            slices.add(ActiveWheel.Slice.ordinary(id, "activity-" + (i % 3), ActiveWheel.Enhancement.values()[i]));
            classifications.put(id, new Classification(i == 4 ? Availability.ASSIGNABLE : Availability.VERIFIED_EMPTY,
                i == 4 ? "Reviewed assignment" : "Complete empty pool " + i));
        }
        MasterSelectionReadiness readiness = new MasterSelectionReadiness(new ActiveWheel(slices), classifications);
        MasterSelectionProvenance start = restore(readiness, List.of());
        check(start.transactionId().equals(TRANSACTION) && start.snapshotVersion().equals("snapshot-7"));
        check(start.catalogVersion().equals("catalog-3") && start.streamDescriptor().equals("test-stream-descriptor"));
        check(start.drawCount() == 0 && !start.hasValidLanding());
        check(start.currentStreamState().equals(start.initialStreamState()));
        check(start.readiness() == readiness && start.pool().totalWeight() == 35);
        // Every raw ticket still covers the original ordered slice and enhancement weight.
        int ticket = 0;
        for (ActiveWheel.Slice slice : slices)
        {
            for (int i = 0; i < slice.weightUnits(); i++)
            { check(start.pool().atTicket(ticket++).id().equals(slice.id().toString())); }
        }
        List<MasterSelectionProvenance.Step> steps = new ArrayList<>();
        steps.add(step(start.pool(), 0, "cursor-1"));
        steps.add(step(start.pool(), 4, "cursor-2"));
        MasterSelectionProvenance emptyPrefix = restore(readiness, steps);
        check(!emptyPrefix.hasValidLanding() && emptyPrefix.drawCount() == 2);
        check(emptyPrefix.currentStreamState().equals("cursor-2"));
        check(emptyPrefix.landings().get(0).reason().equals("Complete empty pool 0"));
        check(emptyPrefix.landings().get(1).reason().equals("Complete empty pool 1"));
        steps.add(step(start.pool(), 34, "cursor-3"));
        MasterSelectionProvenance valid = restore(readiness, steps);
        check(valid.hasValidLanding() && valid.drawCount() == 3);
        check(valid.landings().get(2).kind() == Landing.ORDINARY_ASSIGNMENT);
        check(valid.landings().get(2).reason().equals("Reviewed assignment"));
        check(valid.currentStreamState().equals("cursor-3"));
        // Restoring supplied commitments never samples, replaces or erases prior tickets.
        MasterSelectionProvenance again = restore(readiness, steps);
        for (int i = 0; i < steps.size(); i++)
        { check(again.landings().get(i).step().draw().equals(valid.landings().get(i).step().draw())); }
        steps.add(step(start.pool(), 0, "cursor-4"));
        rejects(IllegalArgumentException.class, () -> restore(readiness, steps));
        steps.clear();
        check(valid.drawCount() == 3 && emptyPrefix.drawCount() == 2 && start.drawCount() == 0);
        rejects(UnsupportedOperationException.class, () -> valid.landings().clear());
        // No arbitrary retry cap or automatic pity result.
        for (int i = 0; i < 1000; i++) { steps.add(step(start.pool(), 0, "cursor-" + i)); }
        check(!restore(readiness, steps).hasValidLanding());
        List<WeightedPool.Entry> changed = new ArrayList<>(start.pool().entries());
        Collections.reverse(changed);
        rejects(IllegalArgumentException.class, () -> restore(readiness,
            List.of(step(new WeightedPool("snapshot-7", changed), 0, "cursor-1"))));
        rejects(IllegalArgumentException.class, () -> restore(readiness,
            List.of(step(new WeightedPool("snapshot-8", start.pool().entries()), 0, "cursor-1"))));
        changed.clear();
        changed.add(new WeightedPool.Entry(slices.get(0).id().toString(), 1));
        rejects(IllegalArgumentException.class, () -> restore(readiness,
            List.of(step(new WeightedPool("snapshot-7", changed), 0, "cursor-1"))));
        rejects(IllegalArgumentException.class, () -> new MasterSelectionProvenance(TRANSACTION,
            " snapshot-7", "catalog-3", "stream", "state", readiness, List.of()));
        rejects(IllegalArgumentException.class, () -> new MasterSelectionProvenance(TRANSACTION,
            "snapshot-7", " ", "stream", "state", readiness, List.of()));
        rejects(IllegalArgumentException.class, () -> step(start.pool(), 0, ""));
        rejects(NullPointerException.class, () -> new MasterSelectionProvenance.Step(null, "state"));
        for (Availability blocked : new Availability[] {Availability.UNKNOWN, Availability.UNSUPPORTED, Availability.VERIFIED_EMPTY})
        {
            classifications.put(slices.get(4).id(), new Classification(blocked, "Not ready"));
            MasterSelectionReadiness notReady = new MasterSelectionReadiness(new ActiveWheel(slices), classifications);
            rejects(IllegalArgumentException.class, () -> restore(notReady, List.of()));
        }
        for (ActiveWheel.Kind kind : new ActiveWheel.Kind[] {ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE})
        {
            List<ActiveWheel.Slice> withSpecial = new ArrayList<>(slices);
            UUID special = new UUID(0, 10);
            withSpecial.add(ActiveWheel.Slice.special(special, kind, 7));
            classifications.put(special, new Classification(Availability.ASSIGNABLE, "Mandatory recovery"));
            MasterSelectionReadiness supported = new MasterSelectionReadiness(new ActiveWheel(withSpecial), classifications);
            WeightedPool specialPool = restore(supported, List.of()).pool();
            List<MasterSelectionProvenance.Step> specialSteps = new ArrayList<>();
            specialSteps.add(step(specialPool, 0, "cursor-1"));
            specialSteps.add(step(specialPool, 41, "cursor-2"));
            MasterSelectionProvenance result = restore(supported, specialSteps);
            check(result.hasValidLanding() && result.landings().get(1).kind() == Landing.SPECIAL_OBLIGATION);
            check(result.landings().get(1).reason().equals("Mandatory recovery"));
            specialSteps.add(step(specialPool, 0, "cursor-3"));
            rejects(IllegalArgumentException.class, () -> restore(supported, specialSteps));
        }
        System.out.println("Master selection provenance checks passed");
    }
}

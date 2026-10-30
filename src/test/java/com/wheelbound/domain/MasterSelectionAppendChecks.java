package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static com.wheelbound.domain.MasterSelectionReadiness.*;

/** Supplied-draw transitions only: no RNG authenticity or durable admission claim. */
public final class MasterSelectionAppendChecks
{
    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Append invariant violated"); } }

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
        for (ActiveWheel.Kind special : new ActiveWheel.Kind[]
            {null, ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE})
        {
            List<ActiveWheel.Slice> slices = new ArrayList<>();
            Map<UUID, Classification> classifications = new HashMap<>();
            for (int i = 0; i < 5; i++)
            {
                UUID id = new UUID(0, i + 1);
                slices.add(ActiveWheel.Slice.ordinary(id, "activity-" + i, ActiveWheel.Enhancement.values()[0]));
                classifications.put(id, new Classification(i == 4 ? Availability.ASSIGNABLE
                    : Availability.VERIFIED_EMPTY, "reason-" + i));
            }
            if (special != null)
            {
                UUID id = new UUID(0, 6);
                slices.add(ActiveWheel.Slice.special(id, special, 7));
                classifications.put(id, new Classification(Availability.ASSIGNABLE, "mandatory"));
            }
            MasterSelectionProvenance start = new MasterSelectionProvenance(UUID.randomUUID(),
                "snapshot", "catalog", "opaque-stream", "same-state",
                new MasterSelectionReadiness(new ActiveWheel(slices), classifications), List.of());
            MasterSelectionProvenance.Step empty = new MasterSelectionProvenance.Step(
                new DrawCommitment(start.pool(), 0), "same-state");
            MasterSelectionProvenance prefix = start;
            for (int i = 0; i < 513; i++)
            { prefix = prefix.append(start.transactionId(), i, "same-state", empty); }
            final MasterSelectionProvenance retained = prefix;
            check(start.drawCount() == 0 && retained.drawCount() == 513 && !retained.hasValidLanding());
            check(retained.landings().get(0).step() == empty);
            check(retained.landings().get(512).reason().equals("reason-0"));
            check(retained.readiness() == start.readiness() && retained.pool().equals(start.pool()));
            rejects(IllegalArgumentException.class, () -> retained.append(start.transactionId(), 0, "same-state", empty));
            rejects(IllegalArgumentException.class, () -> retained.append(UUID.randomUUID(), 513, "same-state", empty));
            rejects(IllegalArgumentException.class, () -> retained.append(start.transactionId(), 513, "other-state", empty));
            rejects(NullPointerException.class, () -> retained.append(start.transactionId(), 513, "same-state", null));
            WeightedPool changed = new WeightedPool("changed-snapshot", start.pool().entries());
            rejects(IllegalArgumentException.class, () -> retained.append(start.transactionId(), 513,
                "same-state", new MasterSelectionProvenance.Step(new DrawCommitment(changed, 0), "next")));
            MasterSelectionProvenance resolved = retained.append(start.transactionId(), 513, "same-state",
                new MasterSelectionProvenance.Step(new DrawCommitment(start.pool(), start.pool().totalWeight() - 1), "final"));
            check(resolved.hasValidLanding() && resolved.drawCount() == 514);
            check(resolved.currentStreamState().equals("final") && retained.currentStreamState().equals("same-state"));
            check(resolved.landings().get(513).kind() == (special == null
                ? Landing.ORDINARY_ASSIGNMENT : Landing.SPECIAL_OBLIGATION));
            rejects(IllegalStateException.class, () -> resolved.append(start.transactionId(), 514, "final", empty));
        }
        System.out.println("Master selection append checks passed");
    }
}

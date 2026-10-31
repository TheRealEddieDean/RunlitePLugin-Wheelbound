package com.wheelbound.domain;

import java.util.*;

/** Offline structural checks; classifications are fixture declarations only. */
public final class MasterSpecialObligationChecks
{
    public static void main(String[] args)
    {
        for (var kind : new ActiveWheel.Kind[] {ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE})
        for (int retries : new int[] {0, 1, 513})
        {
            List<ActiveWheel.Slice> slices = new ArrayList<>();
            Map<UUID, MasterSelectionReadiness.Classification> evidence = new HashMap<>();
            for (int i = 1; i <= 6; i++)
            {
                UUID id = new UUID(0, i);
                slices.add(i == 6 ? ActiveWheel.Slice.special(id, kind, 7)
                    : ActiveWheel.Slice.ordinary(id, "activity" + i, ActiveWheel.Enhancement.BASE));
                evidence.put(id, new MasterSelectionReadiness.Classification(i < 5
                    ? MasterSelectionReadiness.Availability.VERIFIED_EMPTY
                    : MasterSelectionReadiness.Availability.ASSIGNABLE, "fixture"));
            }
            var zero = new MasterSelectionProvenance(UUID.randomUUID(), "snapshot", "catalog",
                "stream", "initial", new MasterSelectionReadiness(new ActiveWheel(slices), evidence), List.of());
            var creation = new MasterSelectionCreationReceipt(new RunIdentity("offline", UUID.randomUUID()), 2, zero);
            var pending = zero;
            for (int i = 0; i < retries; i++) { pending = append(pending, 0, "retry" + i); }
            var terminal = append(pending, pending.pool().totalWeight() - 1, "terminal");
            var receipt = new MasterSelectionResolutionReceipt(UUID.randomUUID(), 9, creation, terminal);
            var payload = new MasterSpecialObligation(receipt);
            var restored = new MasterSelectionResolutionReceipt(receipt.commandId(), 9, creation, terminal);
            var same = new MasterSpecialObligation(restored);
            check(payload.equals(same) && same.equals(payload) && payload.hashCode() == same.hashCode());
            check(!payload.equals(null) && !payload.equals(receipt));
            check(payload.resolution() == receipt && payload.selectedSliceId().equals(new UUID(0, 6)));
            check(payload.requirement() == (kind == ActiveWheel.Kind.TAINT
                ? MasterSpecialObligation.Requirement.MANDATORY_PUNISHMENT
                : MasterSpecialObligation.Requirement.MANDATORY_SACRIFICE));
            check(payload.validateResolution(restored) == payload);
            var changed = new MasterSelectionResolutionReceipt(receipt.commandId(), 9, creation,
                append(pending, pending.pool().totalWeight() - 1, "changed"));
            rejects(() -> payload.validateResolution(changed));
            rejects(() -> payload.validateResolution(new MasterSelectionResolutionReceipt(
                UUID.randomUUID(), 9, creation, terminal)));
            // The fifth ordinary slice is assignable; it cannot stand in for a special obligation.
            var ordinary = new MasterSelectionResolutionReceipt(UUID.randomUUID(), 9, creation,
                append(pending, 16, "ordinary"));
            rejects(() -> new MasterSpecialObligation(ordinary));
            check(pending.drawCount() == retries && !pending.hasValidLanding());
        }
        System.out.println("MasterSpecialObligationChecks PASSED");
    }

    private static MasterSelectionProvenance append(MasterSelectionProvenance prefix, int ticket, String state)
    { return prefix.append(prefix.transactionId(), prefix.drawCount(), prefix.currentStreamState(),
        new MasterSelectionProvenance.Step(new DrawCommitment(prefix.pool(), ticket), state)); }
    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Special obligation changed"); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid special obligation accepted");
    }
}

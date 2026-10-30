package com.wheelbound.domain;

import java.util.*;

/** Dependency-free original-request replay checks; no store or evidence claim. */
public final class MasterSelectionCreationChecks
{
    private static final RunIdentity ID = new RunIdentity("creation-check", new UUID(1, 1));

    public static void main(String[] args)
    {
        MasterSelectionCreationReceipt original = receipt(0);
        MasterSelectionCreationReceipt restored = receipt(0);
        if (!original.equals(restored) || !restored.equals(original)
            || original.hashCode() != restored.hashCode() || original.equals(null)
            || original.equals(original.initial()) || original.beforeRevision() != 7
            || original.afterRevision() != 8 || original.initial().drawCount() != 0
            || !original.transactionId().equals(original.initial().transactionId()))
        { throw new AssertionError("Original request replay mismatch"); }
        for (int variant = 1; variant <= 16; variant++)
        {
            if (original.equals(receipt(variant)))
            { throw new AssertionError("Changed request accepted: " + variant); }
        }
        MasterSelectionProvenance initial = original.initial();
        MasterSelectionProvenance advanced = initial.append(initial.transactionId(), 0,
            initial.currentStreamState(), new MasterSelectionProvenance.Step(
                new DrawCommitment(initial.pool(), 0), "cursor-1"));
        rejects(() -> new MasterSelectionCreationReceipt(ID, 7, advanced));
        rejects(() -> new MasterSelectionCreationReceipt(ID, -1, initial));
        rejects(() -> new MasterSelectionCreationReceipt(ID, Long.MAX_VALUE, initial));
        rejects(() -> new MasterSelectionCreationReceipt(null, 7, initial));
        rejects(() -> new MasterSelectionCreationReceipt(ID, 7, null));
        MasterSelectionCreationReceipt boundary = new MasterSelectionCreationReceipt(ID,
            Long.MAX_VALUE - 1, initial);
        if (boundary.afterRevision() != Long.MAX_VALUE || !original.equals(restored)
            || initial.drawCount() != 0 || advanced.drawCount() != 1)
        { throw new AssertionError("Creation changed or overflowed"); }
        System.out.println("MasterSelectionCreationChecks PASSED");
    }

    private static MasterSelectionCreationReceipt receipt(int variant)
    {
        List<ActiveWheel.Slice> slices = new ArrayList<>();
        Map<UUID, MasterSelectionReadiness.Classification> classifications = new HashMap<>();
        for (int i = 0; i < 5; i++)
        {
            UUID id = new UUID(0, i + 1 + (variant == 8 ? 20 : 0));
            slices.add(ActiveWheel.Slice.ordinary(id,
                variant == 9 && i == 0 ? "changed" : "activity-" + i % 3,
                variant == 10 && i == 0 ? ActiveWheel.Enhancement.FIRST : ActiveWheel.Enhancement.BASE));
            classifications.put(id, new MasterSelectionReadiness.Classification(
                i == 4 || (variant == 11 && i == 0) ? MasterSelectionReadiness.Availability.ASSIGNABLE
                    : MasterSelectionReadiness.Availability.VERIFIED_EMPTY,
                variant == 12 && i == 0 ? "changed-reason" : "reason-" + i));
        }
        UUID special = new UUID(0, 100);
        slices.add(ActiveWheel.Slice.special(special, variant == 13
            ? ActiveWheel.Kind.SACRIFICE : ActiveWheel.Kind.TAINT, variant == 15 ? 8 : 4));
        classifications.put(special, new MasterSelectionReadiness.Classification(
            MasterSelectionReadiness.Availability.ASSIGNABLE, "supported"));
        if (variant == 7) { Collections.reverse(slices); }
        MasterSelectionProvenance initial = new MasterSelectionProvenance(
            new UUID(2, variant == 3 ? 3 : 2), variant == 4 ? "other-snapshot" : "snapshot",
            variant == 5 ? "other-catalog" : "catalog", variant == 6 ? "other-stream" : "stream",
            variant == 1 ? "other-state" : "cursor-0",
            new MasterSelectionReadiness(new ActiveWheel(slices), classifications), List.of());
        return new MasterSelectionCreationReceipt(variant == 2
            ? new RunIdentity("other-account", new UUID(1, 1)) : variant == 16
                ? new RunIdentity("creation-check", new UUID(1, 2)) : ID,
            variant == 14 ? 8 : 7, initial);
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | ArithmeticException | NullPointerException expected) { return; }
        throw new AssertionError("Invalid creation payload accepted");
    }
}

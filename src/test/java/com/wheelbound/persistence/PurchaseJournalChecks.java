package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.UUID;

/** Dependency-free checks, also executed by JUnit. Quotes are synthetic test inputs. */
public final class PurchaseJournalChecks
{
    private static final UUID COMMAND = UUID.randomUUID();
    private static final UUID INSTANCE = UUID.randomUUID();
    private static final RunIdentity IDENTITY = new RunIdentity("test-account", UUID.randomUUID());

    private static SlicePurchaseReceipt purchase(RunIdentity identity, UUID instance, String activity,
        String version, long amount, long ordinal, long balance)
    {
        return new SlicePurchaseReceipt(identity, instance,
            new SlicePurchaseProposal.Quote(activity, "synthetic", version, amount,
                SliceInventory.PurchaseKind.PAID_DUPLICATE, ordinal),
            new FatePointDebits.Receipt(COMMAND, 12, 13, "synthetic", version, amount, balance));
    }

    private static RunReceipt envelope(SlicePurchaseReceipt purchase)
    {
        return new RunReceipt(COMMAND, RunReceipt.Operation.SLICE_PURCHASE, 12, 13,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, null, null, purchase);
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected rejected envelope");
    }

    public static void main(String[] args)
    {
        SlicePurchaseReceipt original = purchase(IDENTITY, INSTANCE, "mining", "v1", 7, 1, 93);
        RunReceipt first = envelope(original);
        RunReceipt restored = envelope(purchase(IDENTITY, INSTANCE, "mining", "v1", 7, 1, 93));
        if (!first.equals(restored) || first.hashCode() != restored.hashCode()
            || first.purchase() != original || first.debit() != null)
        { throw new AssertionError("Original request/response not retained by value"); }
        for (SlicePurchaseReceipt changed : new SlicePurchaseReceipt[]{
            purchase(new RunIdentity("other", IDENTITY.runId()), INSTANCE, "mining", "v1", 7, 1, 93),
            purchase(new RunIdentity(IDENTITY.accountKey(), UUID.randomUUID()), INSTANCE, "mining", "v1", 7, 1, 93),
            purchase(IDENTITY, UUID.randomUUID(), "mining", "v1", 7, 1, 93),
            purchase(IDENTITY, INSTANCE, "fishing", "v1", 7, 1, 93),
            purchase(IDENTITY, INSTANCE, "mining", "v2", 7, 1, 93),
            purchase(IDENTITY, INSTANCE, "mining", "v1", 8, 1, 93),
            purchase(IDENTITY, INSTANCE, "mining", "v1", 7, 2, 93),
            purchase(IDENTITY, INSTANCE, "mining", "v1", 7, 1, 92)})
        {
            if (first.equals(envelope(changed))) { throw new AssertionError("Purchase history rewrite hidden"); }
        }
        rejects(() -> envelope(null));
        rejects(() -> new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.SLICE_PURCHASE, 12, 13,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, null, null, original));
        rejects(() -> new RunReceipt(COMMAND, RunReceipt.Operation.SLICE_PURCHASE, 11, 12,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, null, null, original));
        rejects(() -> new RunReceipt(COMMAND, RunReceipt.Operation.SLICE_PURCHASE, 12, 13,
            RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE, null, null, original));
        rejects(() -> new RunReceipt(COMMAND, RunReceipt.Operation.SLICE_PURCHASE, 12, 13,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.OFFERS_COMMITTED, null, null, original));
        rejects(() -> new RunReceipt(COMMAND, RunReceipt.Operation.FP_DEBIT, 12, 13,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, null, original.debit(), original));
        rejects(() -> new RunReceipt(COMMAND, RunReceipt.Operation.SLICE_PURCHASE, 12, 13,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, null, original.debit(), original));
        System.out.println("Purchase journal checks passed");
    }
}

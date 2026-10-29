package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Dependency-free aggregate tampering checks; all prices are synthetic. */
public final class PurchaseAggregateChecks
{
    private static final RunIdentity ID = new RunIdentity("test-account", UUID.randomUUID());

    static SliceInventory inventory()
    {
        List<ActiveWheel.Slice> slices = new ArrayList<>();
        for (String activity : List.of("mining", "mining", "fishing", "fishing", "woodcutting"))
        { slices.add(ActiveWheel.Slice.ordinary(UUID.randomUUID(), activity, ActiveWheel.Enhancement.BASE)); }
        return new SliceInventory(new ActiveWheel(slices), List.of(), SliceInventory.SatchelTier.BASE,
            Set.of("mining", "fishing", "woodcutting", "smithing"), Map.of(
                "mining", new SliceInventory.AcquisitionHistory(0),
                "fishing", new SliceInventory.AcquisitionHistory(0),
                "woodcutting", new SliceInventory.AcquisitionHistory(0)));
    }

    private static RunReceipt envelope(SlicePurchaseReceipt purchase)
    {
        FatePointDebits.Receipt debit = purchase.debit();
        return new RunReceipt(debit.commandId(), RunReceipt.Operation.SLICE_PURCHASE,
            debit.beforeRevision(), debit.afterRevision(), RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE, null, null, purchase);
    }

    private static RunDocument document(SlicePurchaseReceipt receipt, FatePointLedger ledger, SliceInventory inventory)
    {
        return new RunDocument(new RunLifecycle(ID, 1, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
            "synthetic", List.of(envelope(receipt)), Optional.ofNullable(ledger), Optional.ofNullable(inventory));
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected invalid purchase aggregate rejection");
    }

    public static void main(String[] args)
    {
        for (String activity : List.of("smithing", "mining"))
        {
            SliceInventory before = inventory();
            boolean first = activity.equals("smithing");
            SlicePurchaseProposal proposal = SlicePurchaseProposal.propose(
                new RunLifecycle(ID, 0, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
                new FatePointLedger(100, List.of()), before, ID, UUID.randomUUID(), 0, UUID.randomUUID(),
                new SlicePurchaseProposal.Quote(activity, "synthetic", "v1", 7,
                    first ? SliceInventory.PurchaseKind.FIRST_INSTANCE : SliceInventory.PurchaseKind.PAID_DUPLICATE,
                    first ? 0 : 1));
            SlicePurchaseReceipt receipt = proposal.receipt();
            document(receipt, proposal.ledger(), proposal.inventory());
            rejects(() -> document(receipt, null, proposal.inventory()));
            rejects(() -> document(receipt, proposal.ledger(), null));
            rejects(() -> document(receipt, new FatePointLedger(100, List.of()), proposal.inventory()));
            rejects(() -> document(receipt, new FatePointLedger(101, proposal.ledger().entries()), proposal.inventory()));
            rejects(() -> document(receipt, new FatePointLedger(100, List.of(
                new FatePointLedger.Entry(FatePointLedger.Kind.DISCRETIONARY_COST, 8))), proposal.inventory()));
            rejects(() -> document(receipt, proposal.ledger(), before));
            rejects(() -> document(new SlicePurchaseReceipt(new RunIdentity("foreign", ID.runId()),
                receipt.instanceId(), receipt.quote(), receipt.debit()), proposal.ledger(), proposal.inventory()));
            rejects(() -> document(new SlicePurchaseReceipt(new RunIdentity(ID.accountKey(), UUID.randomUUID()),
                receipt.instanceId(), receipt.quote(), receipt.debit()), proposal.ledger(), proposal.inventory()));
            SliceInventory stored = proposal.inventory().proposeStorage(receipt.instanceId());
            rejects(() -> document(receipt, proposal.ledger(), stored));
            List<ActiveWheel.Slice> wrongSlices = new ArrayList<>(proposal.inventory().activeWheel().slices());
            wrongSlices.set(wrongSlices.size() - 1, ActiveWheel.Slice.ordinary(receipt.instanceId(),
                "woodcutting", ActiveWheel.Enhancement.BASE));
            SliceInventory wrongActivity = new SliceInventory(new ActiveWheel(wrongSlices), List.of(),
                before.satchelTier(), before.unlockedActivities(), proposal.inventory().acquisitionHistory());
            rejects(() -> document(receipt, proposal.ledger(), wrongActivity));
            Map<String, SliceInventory.AcquisitionHistory> changed = new HashMap<>(proposal.inventory().acquisitionHistory());
            changed.put(activity, new SliceInventory.AcquisitionHistory(2));
            SliceInventory wrongHistory = new SliceInventory(proposal.inventory().activeWheel(), List.of(),
                before.satchelTier(), before.unlockedActivities(), changed);
            rejects(() -> document(receipt, proposal.ledger(), wrongHistory));
            SliceInventory enhanced = proposal.inventory().proposeEnhancement(receipt.instanceId(), ActiveWheel.Enhancement.FIRST);
            rejects(() -> document(receipt, proposal.ledger(), enhanced));

            // A later standalone debit must validate in the same ledger suffix and retain
            // the purchase's original response, rather than comparing it to today's balance.
            FatePointDebits.Result later = new FatePointDebits(proposal.lifecycle(), proposal.ledger()).debit(
                ID, UUID.randomUUID(), 1, "later", "v1", 3);
            FatePointDebits.Receipt debit = later.receipt();
            RunReceipt debitEnvelope = new RunReceipt(debit.commandId(), RunReceipt.Operation.FP_DEBIT,
                1, 2, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, null, debit);
            RunDocument current = new RunDocument(later.proposed().lifecycle(), "synthetic",
                List.of(envelope(receipt), debitEnvelope), Optional.of(later.proposed().ledger()), Optional.of(proposal.inventory()));
            if (current.receipts().get(0).purchase().debit().balance() != 93
                || current.fatePoints().get().balance() != 90)
            { throw new AssertionError("Original purchase response changed after later debit"); }
        }
        System.out.println("Purchase aggregate checks passed");
    }
}

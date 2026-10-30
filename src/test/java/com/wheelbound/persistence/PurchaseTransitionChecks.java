package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Dependency-free before/after checks using synthetic quotes only. */
public final class PurchaseTransitionChecks
{
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected transition rejection");
    }

    static RunDocument before;
    static RunDocument after;

    public static void main(String[] args)
    {
        RunIdentity id = new RunIdentity("synthetic", UUID.randomUUID());
        before = new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
            "synthetic", List.of(), Optional.of(new FatePointLedger(100, List.of())),
            Optional.of(PurchaseAggregateChecks.inventory()));
        SlicePurchaseProposal proposal = SlicePurchaseProposal.propose(before.lifecycle(), before.fatePoints().get(),
            before.inventory().get(), id, UUID.randomUUID(), 0, UUID.randomUUID(),
            new SlicePurchaseProposal.Quote("smithing", "synthetic", "v1", 7, SliceInventory.PurchaseKind.FIRST_INSTANCE, 0));
        RunReceipt receipt = new RunReceipt(proposal.debit().commandId(), RunReceipt.Operation.SLICE_PURCHASE,
            0, 1, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, null, null, proposal.receipt());
        after = new RunDocument(proposal.lifecycle(), "synthetic", List.of(receipt),
            Optional.of(proposal.ledger()), Optional.of(proposal.inventory()));
        PurchaseTransition.validate(before, after);
        SlicePurchaseProposal duplicate = SlicePurchaseProposal.propose(after.lifecycle(), proposal.ledger(),
            proposal.inventory(), id, UUID.randomUUID(), 1, UUID.randomUUID(), new SlicePurchaseProposal.Quote(
                "smithing", "synthetic", "v1", 9, SliceInventory.PurchaseKind.PAID_DUPLICATE, 1));
        RunReceipt second = new RunReceipt(duplicate.debit().commandId(), RunReceipt.Operation.SLICE_PURCHASE,
            1, 2, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, null, null, duplicate.receipt());
        RunDocument later = new RunDocument(duplicate.lifecycle(), "synthetic", List.of(receipt, second),
            Optional.of(duplicate.ledger()), Optional.of(duplicate.inventory()));
        PurchaseTransition.validate(after, later);
        if (later.receipts().get(0).purchase().debit().balance() != 93)
        { throw new AssertionError("Original response rewritten"); }
        rejects(() -> PurchaseTransition.validate(before, later));
        rejects(() -> PurchaseTransition.validate(null, after));
        rejects(() -> PurchaseTransition.validate(new RunDocument(before.lifecycle(), "changed", List.of(),
            before.fatePoints(), before.inventory()), after));
        rejects(() -> PurchaseTransition.validate(new RunDocument(before.lifecycle(), "synthetic"), after));
        rejects(() -> PurchaseTransition.validate(new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.PAUSED,
            RunLifecycle.Obligation.NONE), "synthetic", List.of(), before.fatePoints(), before.inventory()), after));
        SliceInventory good = proposal.inventory();
        UUID old = good.activeWheel().slices().get(0).id();
        rejectsInventory(good.proposeEnhancement(old, ActiveWheel.Enhancement.FIRST));
        rejectsInventory(good.proposeStorage(old));
        List<UUID> order = new ArrayList<>();
        for (ActiveWheel.Slice slice : good.activeWheel().slices()) { order.add(slice.id()); }
        Collections.reverse(order);
        rejectsInventory(new SliceInventory(good.activeWheel().reordered(order), good.stored(), good.satchelTier(),
            good.unlockedActivities(), good.acquisitionHistory()));
        Map<String, SliceInventory.AcquisitionHistory> history = new HashMap<>(good.acquisitionHistory());
        history.put("mining", new SliceInventory.AcquisitionHistory(8));
        rejectsInventory(new SliceInventory(good.activeWheel(), good.stored(), good.satchelTier(),
            good.unlockedActivities(), history));
        // A checksum-valid ledger can disguise a rewritten prefix while preserving the response balance.
        FatePointLedger forged = new FatePointLedger(90, List.of(
            new FatePointLedger.Entry(FatePointLedger.Kind.EARNING, 10),
            new FatePointLedger.Entry(FatePointLedger.Kind.DISCRETIONARY_COST, 7)));
        rejects(() -> PurchaseTransition.validate(before, new RunDocument(after.lifecycle(), "synthetic",
            after.receipts(), Optional.of(forged), after.inventory())));
        // A UUID owned before this write cannot be repurposed as the newly bought instance.
        List<ActiveWheel.Slice> priorSlices = new ArrayList<>(before.inventory().get().activeWheel().slices());
        priorSlices.set(0, ActiveWheel.Slice.ordinary(proposal.instanceId(), "mining", ActiveWheel.Enhancement.BASE));
        SliceInventory collision = new SliceInventory(new ActiveWheel(priorSlices), List.of(), good.satchelTier(),
            good.unlockedActivities(), before.inventory().get().acquisitionHistory());
        rejects(() -> PurchaseTransition.validate(new RunDocument(before.lifecycle(), "synthetic", List.of(),
            before.fatePoints(), Optional.of(collision)), after));
        if (before.fatePoints().get().balance() != 100 || before.inventory().get().activeWheel().slices().size() != 5)
        { throw new AssertionError("Prior snapshot mutated"); }
        System.out.println("Purchase transition checks passed");
    }

    private static void rejectsInventory(SliceInventory inventory)
    {
        rejects(() -> PurchaseTransition.validate(before, new RunDocument(after.lifecycle(), after.catalogVersion(),
            after.receipts(), after.fatePoints(), Optional.of(inventory))));
    }
}

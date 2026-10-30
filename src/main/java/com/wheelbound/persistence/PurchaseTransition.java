package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.List;
import java.util.Objects;

/** Store-side evidence check only; does not authorize a catalog, quote or live edit. */
final class PurchaseTransition
{
    private PurchaseTransition() {}

    static void validate(RunDocument before, RunDocument after)
    {
        int prefix = before == null ? 0 : before.receipts().size();
        List<RunReceipt> added = after.receipts().subList(prefix, after.receipts().size());
        if (added.stream().noneMatch(r -> r.purchase() != null)) { return; }
        if (before == null || added.size() != 1
            || !before.catalogVersion().equals(after.catalogVersion()))
        { throw new IllegalArgumentException("Purchase requires one transition from a saved catalog"); }
        SlicePurchaseReceipt purchase = added.get(0).purchase();
        SliceInventory inventory = before.inventory().orElseThrow(
            () -> new IllegalArgumentException("Unavailable prior inventory"));
        // Check retained journal identities as well as currently owned identities (the
        // proposal checks active and stored ownership). Future destruction receipts must
        // extend this check before those operations can be admitted by the store.
        if (before.receipts().stream().anyMatch(r -> r.purchase() != null
            && r.purchase().instanceId().equals(purchase.instanceId())))
        { throw new IllegalArgumentException("Historical purchase instance reused"); }
        SlicePurchaseProposal expected = SlicePurchaseProposal.propose(before.lifecycle(),
            before.fatePoints().orElseThrow(() -> new IllegalArgumentException("Unavailable prior ledger")),
            inventory, purchase.identity(), purchase.debit().commandId(), purchase.debit().beforeRevision(),
            purchase.instanceId(), purchase.quote());
        SliceInventory actual = after.inventory().orElseThrow(IllegalArgumentException::new);
        if (!sameInventory(expected.inventory(), actual)
            || !sameLedger(expected.ledger(), after.fatePoints().orElseThrow(IllegalArgumentException::new)))
        { throw new IllegalArgumentException("Purchase changed unrelated inventory or payment history"); }
    }

    private static boolean sameLedger(FatePointLedger a, FatePointLedger b)
    {
        if (a.startingBalance() != b.startingBalance() || a.entries().size() != b.entries().size()) { return false; }
        for (int i = 0; i < a.entries().size(); i++)
        {
            FatePointLedger.Entry x = a.entries().get(i), y = b.entries().get(i);
            if (x.kind() != y.kind() || x.amount() != y.amount()) { return false; }
        }
        return true;
    }

    private static boolean sameInventory(SliceInventory a, SliceInventory b)
    {
        if (a.satchelTier() != b.satchelTier() || !a.unlockedActivities().equals(b.unlockedActivities())
            || !a.acquisitionHistory().keySet().equals(b.acquisitionHistory().keySet())
            || !sameSlices(a.activeWheel().slices(), b.activeWheel().slices())
            || !sameSlices(a.stored(), b.stored())) { return false; }
        return a.acquisitionHistory().entrySet().stream().allMatch(e ->
            e.getValue().paidDuplicates() == b.acquisitionHistory().get(e.getKey()).paidDuplicates());
    }

    private static boolean sameSlices(List<ActiveWheel.Slice> a, List<ActiveWheel.Slice> b)
    {
        if (a.size() != b.size()) { return false; }
        for (int i = 0; i < a.size(); i++)
        {
            ActiveWheel.Slice x = a.get(i), y = b.get(i);
            if (!x.id().equals(y.id()) || x.kind() != y.kind() || !Objects.equals(x.activity(), y.activity())
                || x.enhancement() != y.enhancement() || x.weightUnits() != y.weightUnits()) { return false; }
        }
        return true;
    }
}

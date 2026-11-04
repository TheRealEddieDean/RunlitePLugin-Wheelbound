package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Dependency-free history checks; prices are fixtures only. */
public final class FateCardPurchaseHistoryChecks
{
    public static void main(String[] args)
    {
        var id = new RunIdentity("card-history", new UUID(183, 1));
        var lifecycle = new RunLifecycle(id, 9, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        var ledger = new FatePointLedger(100, List.of());
        var ownership = FateCardOwnership.starting(id);
        var seed = new FateCardPurchaseHistory(lifecycle, ledger, ownership);
        var current = seed;
        var retained = new ArrayList<FateCardPurchaseReceipt>();
        for (var pool : FateCardOwnership.Pool.values())
        for (var type : List.of(FateCardOwnership.Type.LESSER, FateCardOwnership.Type.GREATER))
        {
            var quote = new FateCardPurchaseProposal.Quote(pool, type, "fixture", "v1", 10);
            var before = current;
            var result = current.purchase(id, new UUID(183, 2 + retained.size()),
                current.lifecycle().revision(), quote);
            current = result.proposed();
            retained.add(result.receipt());
            check(before.receipts().size() + 1 == current.receipts().size());
            check(current.ownership().owns(pool, type));
            var head = current;
            rejects(() -> head.purchase(id, new UUID(183, 99), head.lifecycle().revision(), quote));
            var restored = FateCardPurchaseHistory.restore(lifecycle, ledger, ownership, retained);
            check(restored.ownership().equals(current.ownership()));
            check(restored.ledger().balance() == current.ledger().balance());
            check(restored.lifecycle().revision() == current.lifecycle().revision());
            for (var receipt : retained)
            {
                var retry = restored.purchase(id, receipt.debit().commandId(),
                    receipt.debit().beforeRevision(), receipt.quote());
                check(retry.proposed() == restored && retry.receipt() == receipt);
                rejects(() -> restored.purchase(id, receipt.debit().commandId(),
                    restored.lifecycle().revision(), receipt.quote()));
                var changed = new FateCardPurchaseProposal.Quote(receipt.quote().pool(),
                    receipt.quote().type(), "changed", "v1", 10);
                rejects(() -> restored.purchase(id, receipt.debit().commandId(),
                    receipt.debit().beforeRevision(), changed));
            }
        }
        check(seed.ledger().balance() == 100 && seed.receipts().isEmpty());
        check(current.ledger().balance() == 60 && current.ledger().entries().size() == 4);
        var head = current;
        rejects(() -> head.receipts().clear());
        rejects(() -> FateCardPurchaseHistory.restore(lifecycle, ledger, ownership,
            List.of(retained.get(1), retained.get(0))));
        rejects(() -> FateCardPurchaseHistory.restore(lifecycle, ledger, ownership,
            List.of(retained.get(0), retained.get(0))));
        rejects(() -> FateCardPurchaseHistory.restore(head.lifecycle(), head.ledger(),
            head.ownership(), retained));
        var first = retained.get(0);
        var debit = first.debit();
        var corrupt = new FateCardPurchaseReceipt(id, first.quote(), new FatePointDebits.Receipt(
            debit.commandId(), 9, 10, debit.quoteKey(), debit.quoteVersion(), 10, 89));
        rejects(() -> FateCardPurchaseHistory.restore(lifecycle, ledger, ownership, List.of(corrupt)));
        var reused = new FateCardPurchaseReceipt(id, retained.get(1).quote(),
            new FatePointDebits.Receipt(debit.commandId(), 10, 11, "fixture", "v1", 10, 80));
        rejects(() -> FateCardPurchaseHistory.restore(lifecycle, ledger, ownership, List.of(first, reused)));
        var foreign = new RunIdentity("foreign", id.runId());
        rejects(() -> new FateCardPurchaseHistory(lifecycle, ledger, FateCardOwnership.starting(foreign)));
        rejects(() -> head.purchase(foreign, debit.commandId(), 9, first.quote()));
        check(FateCardPurchaseHistory.restore(lifecycle, ledger, ownership, List.of()).receipts().isEmpty());
    }

    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException | UnsupportedOperationException expected) { return; }
        throw new AssertionError("Expected refusal");
    }
}

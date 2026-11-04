package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Dependency-free structural journal checks; amounts are fixtures only. */
public final class FateCardPurchaseJournalChecks
{
    public static void main(String[] args)
    {
        var id = new RunIdentity("card-journal", new UUID(184, 1));
        var foreign = new RunIdentity("foreign", id.runId());
        var retained = new ArrayList<FateCardPurchaseReceipt>();
        long revision = 3;
        for (var pool : FateCardOwnership.Pool.values())
        for (var type : List.of(FateCardOwnership.Type.LESSER, FateCardOwnership.Type.GREATER))
        {
            var quote = new FateCardPurchaseProposal.Quote(pool, type, "fixture", "v1", 10);
            retained.add(new FateCardPurchaseReceipt(id, quote, new FatePointDebits.Receipt(
                new UUID(184, revision), revision, revision + 1, "fixture", "v1", 10, 90)));
            revision += 3; // Deliberate gaps are other operations, not inferred purchases.
        }
        var journal = new FateCardPurchaseJournal(id, Long.MAX_VALUE, retained);
        check(new ArrayList<>(journal.receipts().values()).equals(retained));
        for (var receipt : retained)
        {
            var debit = receipt.debit();
            check(journal.replay(id, Long.MAX_VALUE, debit.commandId(), debit.beforeRevision(),
                receipt.quote()) == receipt);
            rejects(() -> journal.replay(id, Long.MAX_VALUE, debit.commandId(),
                debit.afterRevision(), receipt.quote()));
            rejects(() -> journal.replay(foreign, Long.MAX_VALUE, debit.commandId(),
                debit.beforeRevision(), receipt.quote()));
            rejects(() -> journal.replay(id, Long.MAX_VALUE, debit.commandId(),
                debit.beforeRevision(), new FateCardPurchaseProposal.Quote(receipt.quote().pool(),
                    receipt.quote().type(), "changed", "v1", 10)));
        }
        var first = retained.get(0);
        rejects(() -> new FateCardPurchaseJournal(id, 3, retained));
        rejects(() -> new FateCardPurchaseJournal(foreign, Long.MAX_VALUE, retained));
        rejects(() -> new FateCardPurchaseJournal(id, -1, List.of()));
        rejects(() -> new FateCardPurchaseJournal(id, 100, List.of(first, first)));
        rejects(() -> new FateCardPurchaseJournal(id, 100, List.of(retained.get(1), first)));
        var duplicateId = new FateCardPurchaseReceipt(id, retained.get(1).quote(),
            new FatePointDebits.Receipt(first.debit().commandId(), 20, 21, "fixture", "v1", 10, 80));
        rejects(() -> new FateCardPurchaseJournal(id, 100, List.of(first, duplicateId)));
        rejects(() -> journal.replay(id, 3, first.debit().commandId(), 3, first.quote()));
        rejects(() -> journal.replay(id, 100, new UUID(184, 99), 3, first.quote()));
        rejects(() -> journal.receipts().clear());
        retained.clear();
        check(journal.receipts().size() == 4);
        check(new FateCardPurchaseJournal(id, 0, List.of()).receipts().isEmpty());
        var adjacent = new FateCardPurchaseReceipt(id, first.quote(), new FatePointDebits.Receipt(
            new UUID(184, 100), 4, 5, "fixture", "v1", 10, 80));
        check(new FateCardPurchaseJournal(id, 5, List.of(first, adjacent)).receipts().size() == 2);
    }

    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | UnsupportedOperationException expected) { return; }
        throw new AssertionError("Expected refusal");
    }
}

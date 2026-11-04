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
        validatesTrustedMixedPrefixes(id, foreign);
    }

    private static void validatesTrustedMixedPrefixes(RunIdentity id, RunIdentity foreign)
    {
        var seed = FateCardOwnership.starting(id);
        var ledger = new FatePointLedger(100, List.of());
        var ownership = seed;
        var receipts = new ArrayList<FateCardPurchaseReceipt>();
        var prefixes = new ArrayList<FateCardPurchaseJournal.TrustedPrefix>();
        long revision = 3;
        for (var pool : FateCardOwnership.Pool.values())
        for (var type : List.of(FateCardOwnership.Type.LESSER, FateCardOwnership.Type.GREATER))
        {
            var lifecycle = active(id, revision);
            prefixes.add(new FateCardPurchaseJournal.TrustedPrefix(lifecycle, ledger));
            var result = FateCardPurchaseProposal.propose(lifecycle, ledger, ownership, id,
                new UUID(185, revision), revision,
                new FateCardPurchaseProposal.Quote(pool, type, "fixture", "v1", 10));
            receipts.add(result.receipt());
            ownership = result.ownership();
            ledger = result.ledger().append(new FatePointLedger.Entry(FatePointLedger.Kind.EARNING, 7));
            revision += 3;
        }
        var journal = new FateCardPurchaseJournal(id, revision, receipts);
        var head = active(id, revision);
        var finalLedger = ledger;
        var finalOwnership = ownership;
        journal.validateTransitions(seed, prefixes, head, ledger, ownership);
        check(seed.owned().values().stream().allMatch(types -> types.size() == 1));
        check(ledger.balance() == 88 && ledger.entries().size() == 8);
        rejects(() -> journal.validateTransitions(seed, prefixes, head, finalLedger, seed));
        rejects(() -> journal.validateTransitions(finalOwnership, prefixes, head, finalLedger, finalOwnership));
        rejects(() -> journal.validateTransitions(seed, prefixes.subList(1, 4), head, finalLedger, finalOwnership));
        rejects(() -> journal.validateTransitions(FateCardOwnership.starting(foreign), prefixes,
            head, finalLedger, finalOwnership));
        rejects(() -> journal.validateTransitions(seed, prefixes, active(foreign, 15), finalLedger, finalOwnership));
        rejects(() -> journal.validateTransitions(seed, prefixes, active(id, 3), finalLedger, finalOwnership));
        // Equal balance is insufficient: all retained payment entries must survive.
        rejects(() -> journal.validateTransitions(seed, prefixes, head,
            new FatePointLedger(88, List.of()), finalOwnership));
        var changed = new ArrayList<>(prefixes);
        changed.set(1, new FateCardPurchaseJournal.TrustedPrefix(active(id, 6),
            new FatePointLedger(100, List.of(new FatePointLedger.Entry(FatePointLedger.Kind.PENALTY, 3)))));
        rejects(() -> journal.validateTransitions(seed, changed, head, finalLedger, finalOwnership));
        changed.set(1, new FateCardPurchaseJournal.TrustedPrefix(active(id, 7), finalLedger));
        rejects(() -> journal.validateTransitions(seed, changed, head, finalLedger, finalOwnership));
        changed.set(0, new FateCardPurchaseJournal.TrustedPrefix(new RunLifecycle(id, 3,
            RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE), new FatePointLedger(100, List.of())));
        rejects(() -> journal.validateTransitions(seed, changed, head, finalLedger, finalOwnership));
        var corrupt = new ArrayList<>(receipts);
        var first = receipts.get(0);
        corrupt.set(0, new FateCardPurchaseReceipt(id, first.quote(), new FatePointDebits.Receipt(
            first.debit().commandId(), 3, 4, "fixture", "v1", 10, 89)));
        rejects(() -> new FateCardPurchaseJournal(id, 15, corrupt).validateTransitions(seed, prefixes,
            head, finalLedger, finalOwnership));
        new FateCardPurchaseJournal(id, 0, List.of()).validateTransitions(seed, List.of(),
            active(id, 0), new FatePointLedger(0, List.of()), seed);
    }

    private static RunLifecycle active(RunIdentity identity, long revision)
    { return new RunLifecycle(identity, revision, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE); }

    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException | UnsupportedOperationException expected) { return; }
        throw new AssertionError("Expected refusal");
    }
}

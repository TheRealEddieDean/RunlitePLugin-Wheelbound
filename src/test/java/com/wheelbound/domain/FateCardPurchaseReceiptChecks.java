package com.wheelbound.domain;

import java.util.List;
import java.util.UUID;

/** Dependency-free receipt checks; amounts are fixtures, not adopted prices. */
public final class FateCardPurchaseReceiptChecks
{
    public static void main(String[] args)
    {
        var id = new RunIdentity("card-receipt", new UUID(182, 1));
        var command = new UUID(182, 2);
        var lifecycle = new RunLifecycle(id, 7, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        var ownership = FateCardOwnership.starting(id);
        for (var pool : FateCardOwnership.Pool.values())
        for (var type : List.of(FateCardOwnership.Type.LESSER, FateCardOwnership.Type.GREATER))
        for (long amount : new long[] {1, 11, Long.MAX_VALUE})
        {
            var ledger = new FatePointLedger(amount, List.of());
            var quote = quote(pool, type, "fixture-card", "fixture-v1", amount);
            var proposal = FateCardPurchaseProposal.propose(lifecycle, ledger, ownership,
                id, command, 7, quote);
            var receipt = proposal.receipt();
            check(receipt.identity().equals(id) && receipt.quote() == quote
                && receipt.debit() == proposal.debit());
            check(receipt.matches(id, command, 7, quote(pool, type, "fixture-card", "fixture-v1", amount)));
            check(!receipt.matches(new RunIdentity("foreign", id.runId()), command, 7, quote));
            check(!receipt.matches(new RunIdentity(id.accountKey(), new UUID(182, 3)), command, 7, quote));
            check(!receipt.matches(id, new UUID(182, 4), 7, quote));
            check(!receipt.matches(id, command, 8, quote));
            check(!receipt.matches(id, command, 7, null));
            check(!receipt.matches(null, null, 7, quote));
            for (var otherPool : FateCardOwnership.Pool.values())
            for (var otherType : List.of(FateCardOwnership.Type.LESSER, FateCardOwnership.Type.GREATER))
            { check(receipt.matches(id, command, 7, quote(otherPool, otherType,
                "fixture-card", "fixture-v1", amount)) == (pool == otherPool && type == otherType)); }
            check(!receipt.matches(id, command, 7, quote(pool, type, "changed", "fixture-v1", amount)));
            check(!receipt.matches(id, command, 7, quote(pool, type, "fixture-card", "changed", amount)));
            check(!receipt.matches(id, command, 7, quote(pool, type, "fixture-card", "fixture-v1",
                amount == 1 ? 2 : amount - 1)));

            // Simulate decoded values, with no shared quote/debit object identities.
            var decoded = new FateCardPurchaseReceipt(id,
                quote(pool, type, quote.key(), quote.version(), amount),
                new FatePointDebits.Receipt(command, 7, 8, quote.key(), quote.version(), amount, 0));
            for (int retry = 0; retry < 2; retry++)
            {
                var restored = decoded.reconstruct(lifecycle, ledger, ownership);
                check(restored.ownership().equals(proposal.ownership()));
                check(restored.lifecycle().revision() == 8 && restored.ledger().balance() == 0);
                check(restored.ledger().entries().size() == 1);
                check(decoded.matches(id, command, 7, restored.quote()));
            }
            rejects(() -> decoded.reconstruct(proposal.lifecycle(), proposal.ledger(), proposal.ownership()));
            rejects(() -> decoded.reconstruct(lifecycle, ledger, proposal.ownership()));
            rejects(() -> decoded.reconstruct(new RunLifecycle(id, 6, RunLifecycle.State.ACTIVE,
                RunLifecycle.Obligation.NONE), ledger, ownership));
            var foreign = new RunIdentity("foreign", id.runId());
            rejects(() -> new FateCardPurchaseReceipt(foreign, quote, decoded.debit())
                .reconstruct(lifecycle, ledger, ownership));
            rejects(() -> decoded.reconstruct(lifecycle, ledger, FateCardOwnership.starting(foreign)));
            for (var state : RunLifecycle.State.values())
            for (var obligation : RunLifecycle.Obligation.values())
            {
                if (state == RunLifecycle.State.ACTIVE && obligation == RunLifecycle.Obligation.NONE) { continue; }
                rejects(() -> decoded.reconstruct(new RunLifecycle(id, 7, state, obligation), ledger, ownership));
            }
            var wrongBalance = new FatePointDebits.Receipt(command, 7, 8,
                quote.key(), quote.version(), amount, 1);
            rejects(() -> new FateCardPurchaseReceipt(id, quote, wrongBalance)
                .reconstruct(lifecycle, ledger, ownership));
            rejects(() -> new FateCardPurchaseReceipt(id, quote(pool, type, "changed", quote.version(), amount),
                decoded.debit()));
            rejects(() -> new FateCardPurchaseReceipt(id, quote(pool, type, quote.key(), "changed", amount),
                decoded.debit()));
            rejects(() -> new FateCardPurchaseReceipt(id, quote(pool, type, quote.key(), quote.version(),
                amount == 1 ? 2 : amount - 1), decoded.debit()));
            rejects(() -> new FateCardPurchaseReceipt(null, quote, decoded.debit()));
            rejects(() -> new FateCardPurchaseReceipt(id, null, decoded.debit()));
            rejects(() -> new FateCardPurchaseReceipt(id, quote, null));
            check(ledger.balance() == amount && ledger.entries().isEmpty() && lifecycle.revision() == 7);
            check(ownership.equals(FateCardOwnership.starting(id)));
        }
        // Sequential distinct purchases retain original responses after a later purchase.
        var ledger = new FatePointLedger(3, List.of());
        var lesser = FateCardPurchaseProposal.propose(lifecycle, ledger, ownership, id, command, 7,
            quote(FateCardOwnership.Pool.BOSSING, FateCardOwnership.Type.LESSER, "lesser", "v1", 1));
        var greater = FateCardPurchaseProposal.propose(lesser.lifecycle(), lesser.ledger(), lesser.ownership(),
            id, new UUID(182, 5), 8,
            quote(FateCardOwnership.Pool.BOSSING, FateCardOwnership.Type.GREATER, "greater", "v1", 2));
        check(greater.ledger().balance() == 0 && lesser.receipt().debit().balance() == 2);
        check(lesser.receipt().reconstruct(lifecycle, ledger, ownership).ledger().balance() == 2);
        check(greater.receipt().reconstruct(lesser.lifecycle(), lesser.ledger(), lesser.ownership())
            .ownership().equals(greater.ownership()));
    }

    private static FateCardPurchaseProposal.Quote quote(FateCardOwnership.Pool pool,
        FateCardOwnership.Type type, String key, String version, long amount)
    { return new FateCardPurchaseProposal.Quote(pool, type, key, version, amount); }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Card receipt check failed"); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException | ArithmeticException | NullPointerException expected)
        { return; }
        throw new AssertionError("Expected refusal");
    }
}

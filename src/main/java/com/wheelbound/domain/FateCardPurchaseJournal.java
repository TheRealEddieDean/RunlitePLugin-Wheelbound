package com.wheelbound.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Original ordinary-card purchase responses in commit order, allowing intervening
 * operations. Structural retention only: unlike FateCardPurchaseHistory this does
 * not reconstruct balances or ownership. Aggregate admission must validate those
 * transitions, global command collisions and atomic persistence separately.
 */
public final class FateCardPurchaseJournal
{
    private final RunIdentity identity;
    private final Map<UUID, FateCardPurchaseReceipt> receipts;

    public FateCardPurchaseJournal(RunIdentity identity, long currentRevision,
        Collection<FateCardPurchaseReceipt> payloads)
    {
        this.identity = Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(payloads, "payloads");
        if (currentRevision < 0) { throw new IllegalArgumentException("Negative journal head"); }
        Map<UUID, FateCardPurchaseReceipt> retained = new LinkedHashMap<>();
        long previousAfter = -1;
        for (var receipt : payloads)
        {
            Objects.requireNonNull(receipt, "receipt");
            var debit = receipt.debit();
            if (!identity.equals(receipt.identity()) || debit.afterRevision() > currentRevision
                || debit.beforeRevision() < previousAfter || retained.containsKey(debit.commandId()))
            { throw new IllegalArgumentException("Foreign, future, overlapping or duplicate card purchase"); }
            retained.put(debit.commandId(), receipt);
            previousAfter = debit.afterRevision();
        }
        receipts = Collections.unmodifiableMap(retained);
    }

    public RunIdentity identity() { return identity; }
    public Map<UUID, FateCardPurchaseReceipt> receipts() { return receipts; }

    /** Trusted original lifecycle/accounting immediately before one retained purchase. */
    public static final class TrustedPrefix
    {
        private final RunLifecycle lifecycle;
        private final FatePointLedger ledger;

        public TrustedPrefix(RunLifecycle lifecycle, FatePointLedger ledger)
        {
            this.lifecycle = Objects.requireNonNull(lifecycle, "lifecycle");
            this.ledger = Objects.requireNonNull(ledger, "ledger");
        }
    }

    /**
     * Validates card transitions across revision gaps from explicitly trusted original
     * prefixes, in journal order. Other operations must preserve ordinary ownership
     * and append to the same FP ledger. No gap, prefix authority, quote authenticity,
     * global command admission or durable publication is established by this method.
     * The seed must precede the first retained purchase, never be inferred from a head.
     */
    public void validateTransitions(FateCardOwnership trustedSeed, List<TrustedPrefix> prefixes,
        RunLifecycle head, FatePointLedger headLedger, FateCardOwnership headOwnership)
    {
        Objects.requireNonNull(trustedSeed, "trustedSeed");
        Objects.requireNonNull(head, "head");
        Objects.requireNonNull(headLedger, "headLedger");
        Objects.requireNonNull(headOwnership, "headOwnership");
        var originals = List.copyOf(prefixes);
        if (!identity.equals(trustedSeed.identity()) || !identity.equals(head.identity())
            || !identity.equals(headOwnership.identity()) || originals.size() != receipts.size())
        { throw new IllegalArgumentException("Foreign or incomplete card transition context"); }
        new FateCardPurchaseJournal(identity, head.revision(), receipts.values());
        var ownership = trustedSeed;
        FatePointLedger previous = null;
        int index = 0;
        for (var receipt : receipts.values())
        {
            var prefix = originals.get(index++);
            if (previous != null) { requireLedgerExtension(previous, prefix.ledger); }
            var result = receipt.reconstruct(prefix.lifecycle, prefix.ledger, ownership);
            ownership = result.ownership();
            previous = result.ledger();
        }
        if (previous != null) { requireLedgerExtension(previous, headLedger); }
        if (!ownership.equals(headOwnership))
        { throw new IllegalArgumentException("Card ownership disagrees with reconstructed purchases"); }
    }

    private static void requireLedgerExtension(FatePointLedger before, FatePointLedger after)
    {
        if (before.startingBalance() != after.startingBalance()
            || before.entries().size() > after.entries().size())
        { throw new IllegalArgumentException("Card accounting prefix was replaced or truncated"); }
        for (int i = 0; i < before.entries().size(); i++)
        {
            var expected = before.entries().get(i);
            var actual = after.entries().get(i);
            if (expected.kind() != actual.kind() || expected.amount() != actual.amount())
            { throw new IllegalArgumentException("Card accounting prefix was rewritten"); }
        }
    }

    /** Exact retained response lookup; does not authorize replay against saved state. */
    public FateCardPurchaseReceipt replay(RunIdentity expectedIdentity, long currentRevision,
        UUID commandId, long expectedRevision, FateCardPurchaseProposal.Quote quote)
    {
        if (!identity.equals(Objects.requireNonNull(expectedIdentity, "expectedIdentity")))
        { throw new IllegalArgumentException("Foreign card replay context"); }
        new FateCardPurchaseJournal(identity, currentRevision, receipts.values());
        var original = receipts.get(Objects.requireNonNull(commandId, "commandId"));
        if (original == null || !original.matches(expectedIdentity, commandId, expectedRevision, quote))
        { throw new IllegalArgumentException("Unknown or conflicting card purchase request"); }
        return original;
    }
}

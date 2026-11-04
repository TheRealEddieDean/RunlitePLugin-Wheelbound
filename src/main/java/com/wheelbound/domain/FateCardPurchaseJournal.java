package com.wheelbound.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
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

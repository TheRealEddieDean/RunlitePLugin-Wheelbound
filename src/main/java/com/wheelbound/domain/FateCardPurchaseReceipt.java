package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Frozen ordinary card purchase request and payment response. Construction checks
 * structural agreement only; it proves neither reviewed pricing nor durability.
 * Aggregate admission must reject command collisions across all operations and
 * persist ownership, lifecycle, ledger and this receipt together.
 */
public final class FateCardPurchaseReceipt
{
    private final RunIdentity identity;
    private final FateCardPurchaseProposal.Quote quote;
    private final FatePointDebits.Receipt debit;

    /** Decoded data must still be reconstructed against its trusted original state. */
    public FateCardPurchaseReceipt(RunIdentity identity, FateCardPurchaseProposal.Quote quote,
        FatePointDebits.Receipt debit)
    {
        this.identity = Objects.requireNonNull(identity, "identity");
        this.quote = Objects.requireNonNull(quote, "quote");
        this.debit = Objects.requireNonNull(debit, "debit");
        if (!quote.key().equals(debit.quoteKey()) || !quote.version().equals(debit.quoteVersion())
            || quote.amount() != debit.amount())
        { throw new IllegalArgumentException("Card quote disagrees with debit"); }
    }

    /** Exact replay matching retains the original revision and separate pool/type. */
    public boolean matches(RunIdentity identity, UUID commandId, long expectedRevision,
        FateCardPurchaseProposal.Quote quote)
    {
        return quote != null && this.identity.equals(identity) && debit.commandId().equals(commandId)
            && debit.beforeRevision() == expectedRevision && this.quote.pool() == quote.pool()
            && this.quote.type() == quote.type() && this.quote.key().equals(quote.key())
            && this.quote.version().equals(quote.version()) && this.quote.amount() == quote.amount();
    }

    /**
     * Reconstructs this one purchase from its ORIGINAL trusted state, not a later
     * saved head. Reuses ownership, lifecycle and debit rules and validates the
     * recorded result. This is not a mixed-history replay or persistence API.
     */
    public FateCardPurchaseProposal reconstruct(RunLifecycle originalLifecycle,
        FatePointLedger originalLedger, FateCardOwnership originalOwnership)
    {
        FateCardPurchaseProposal result = FateCardPurchaseProposal.propose(originalLifecycle,
            originalLedger, originalOwnership, identity, debit.commandId(), debit.beforeRevision(), quote);
        if (result.debit().afterRevision() != debit.afterRevision()
            || result.debit().balance() != debit.balance())
        { throw new IllegalArgumentException("Card receipt disagrees with original state"); }
        return result;
    }

    public RunIdentity identity() { return identity; }
    public FateCardPurchaseProposal.Quote quote() { return quote; }
    public FatePointDebits.Receipt debit() { return debit; }
}

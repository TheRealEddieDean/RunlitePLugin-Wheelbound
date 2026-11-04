package com.wheelbound.domain;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;

/**
 * Coupled ordinary card unlock and FP debit proposal (WB-D050/090).
 * The trusted caller must authenticate the quote and legal purchase boundary, reject
 * retained command IDs across operations, and persist ownership, payment, lifecycle
 * and a typed receipt together before acknowledging success. No durable replay,
 * catalog authorization, offers, Challenge grants or gameplay wiring is provided.
 */
public final class FateCardPurchaseProposal
{
    /** Frozen supplied price; construction does not establish review authority. */
    public static final class Quote
    {
        private final FateCardOwnership.Pool pool;
        private final FateCardOwnership.Type type;
        private final String key;
        private final String version;
        private final long amount;

        public Quote(FateCardOwnership.Pool pool, FateCardOwnership.Type type,
            String key, String version, long amount)
        {
            this.pool = Objects.requireNonNull(pool, "pool");
            this.type = Objects.requireNonNull(type, "type");
            this.key = text(key);
            this.version = text(version);
            if (type == FateCardOwnership.Type.STANDARD || amount <= 0)
            { throw new IllegalArgumentException("Only paid Lesser/Greater unlocks are supported"); }
            this.amount = amount;
        }

        public FateCardOwnership.Pool pool() { return pool; }
        public FateCardOwnership.Type type() { return type; }
        public String key() { return key; }
        public String version() { return version; }
        public long amount() { return amount; }
    }

    private final FateCardOwnership ownership;
    private final FatePointDebits.Result payment;
    private final Quote quote;

    private FateCardPurchaseProposal(FateCardOwnership ownership,
        FatePointDebits.Result payment, Quote quote)
    { this.ownership = ownership; this.payment = payment; this.quote = quote; }

    public static FateCardPurchaseProposal propose(RunLifecycle lifecycle, FatePointLedger ledger,
        FateCardOwnership ownership, RunIdentity identity, UUID commandId,
        long expectedRevision, Quote quote)
    {
        Objects.requireNonNull(lifecycle, "lifecycle");
        Objects.requireNonNull(ownership, "ownership");
        Objects.requireNonNull(quote, "quote");
        // Pending operations outside this partial lifecycle require aggregate admission.
        if (!ownership.identity().equals(identity)
            || !lifecycle.acceptsObservation(identity, expectedRevision)
            || lifecycle.obligation() != RunLifecycle.Obligation.NONE)
        { throw new IllegalArgumentException("Purchase requires current active unencumbered run"); }
        if (ownership.owns(quote.pool, quote.type))
        { throw new IllegalArgumentException("Card already owned"); }
        var next = new EnumMap<FateCardOwnership.Pool, java.util.Set<FateCardOwnership.Type>>(
            ownership.owned());
        var types = EnumSet.copyOf(next.get(quote.pool));
        types.add(quote.type);
        next.put(quote.pool, types);
        var payment = new FatePointDebits(lifecycle, ledger).debit(identity, commandId,
            expectedRevision, quote.key, quote.version, quote.amount);
        return new FateCardPurchaseProposal(new FateCardOwnership(FateCardOwnership.VERSION,
            identity, next), payment, quote);
    }

    public FateCardOwnership ownership() { return ownership; }
    public RunLifecycle lifecycle() { return payment.proposed().lifecycle(); }
    public FatePointLedger ledger() { return payment.proposed().ledger(); }
    /** Proposed payment evidence only, never an acknowledgement of durability. */
    public FatePointDebits.Receipt debit() { return payment.receipt(); }
    public Quote quote() { return quote; }

    private static String text(String value)
    {
        if (Objects.requireNonNull(value, "quote reference").isEmpty()
            || !value.equals(value.trim()))
        { throw new IllegalArgumentException("Invalid quote reference"); }
        return value;
    }
}

package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Immutable FP arithmetic/history (WB-D105). Amounts must come from reviewed quotes.
 * This model does not authorize rewards, settle obligations, deduplicate commands or
 * persist transactions. Those checks belong to the enclosing durable run command.
 */
public final class FatePointLedger
{
    public enum Kind { EARNING, DISCRETIONARY_COST, PENALTY }

    public static final class Entry
    {
        private final Kind kind;
        private final long amount;

        public Entry(Kind kind, long amount)
        {
            this.kind = Objects.requireNonNull(kind, "kind");
            if (amount <= 0) { throw new IllegalArgumentException("Amount must be positive"); }
            this.amount = amount;
        }

        public Kind kind() { return kind; }
        public long amount() { return amount; }
    }

    private final long startingBalance;
    private final List<Entry> entries;
    private final long balance;

    /** Restores trusted history, validating each intermediate balance and cost. */
    public FatePointLedger(long startingBalance, List<Entry> entries)
    {
        this.startingBalance = startingBalance;
        this.entries = List.copyOf(entries);
        long current = startingBalance;
        for (Entry entry : this.entries)
        {
            current = apply(current, entry);
        }
        balance = current;
    }

    public long startingBalance() { return startingBalance; }
    public long balance() { return balance; }
    public List<Entry> entries() { return entries; }

    /** No lifecycle or evidence authorization is implied by this arithmetic operation. */
    public FatePointLedger append(Entry entry)
    {
        Objects.requireNonNull(entry, "entry");
        // Validate before allocating a proposed history; failure leaves this ledger intact.
        apply(balance, entry);
        List<Entry> next = new ArrayList<>(entries);
        next.add(entry);
        return new FatePointLedger(startingBalance, next);
    }

    private static long apply(long current, Entry entry)
    {
        if (entry.kind == Kind.EARNING) { return Math.addExact(current, entry.amount); }
        if (entry.kind == Kind.DISCRETIONARY_COST && current < entry.amount)
        { throw new IllegalArgumentException("Insufficient Fate Points"); }
        return Math.subtractExact(current, entry.amount);
    }
}

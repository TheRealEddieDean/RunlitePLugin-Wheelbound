package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable debit proposals, never persistence acknowledgements or purchase authorization.
 * A trusted caller must validate the reviewed quote, product and legal action boundary,
 * then persist lifecycle, ledger, product changes and receipts together before publishing.
 * This component is deliberately not wired to the playable plugin or foundation store.
 */
public final class FatePointDebits
{
    public static final class Receipt
    {
        private final UUID commandId;
        private final long beforeRevision;
        private final long afterRevision;
        private final String quoteKey;
        private final String quoteVersion;
        private final long amount;
        private final long balance;

        /** Decoded data still requires stream validation through restore(). */
        public Receipt(UUID commandId, long beforeRevision, long afterRevision,
            String quoteKey, String quoteVersion, long amount, long balance)
        {
            this.commandId = Objects.requireNonNull(commandId, "commandId");
            requireText(quoteKey);
            requireText(quoteVersion);
            if (beforeRevision < 0 || beforeRevision == Long.MAX_VALUE
                || afterRevision != beforeRevision + 1 || amount <= 0 || balance < 0)
            { throw new IllegalArgumentException("Invalid debit receipt"); }
            this.beforeRevision = beforeRevision;
            this.afterRevision = afterRevision;
            this.quoteKey = quoteKey;
            this.quoteVersion = quoteVersion;
            this.amount = amount;
            this.balance = balance;
        }

        public UUID commandId() { return commandId; }
        public long beforeRevision() { return beforeRevision; }
        public long afterRevision() { return afterRevision; }
        public String quoteKey() { return quoteKey; }
        public String quoteVersion() { return quoteVersion; }
        public long amount() { return amount; }
        public long balance() { return balance; }
    }

    public static final class Result
    {
        private final FatePointDebits proposed;
        private final Receipt receipt;
        private Result(FatePointDebits proposed, Receipt receipt)
        { this.proposed = proposed; this.receipt = receipt; }
        public FatePointDebits proposed() { return proposed; }
        public Receipt receipt() { return receipt; }
    }

    private final RunLifecycle lifecycle;
    private final FatePointLedger ledger;
    private final List<Receipt> receipts;

    /** Seeds a proposal stream from trusted state; not a persisted-history restoration API. */
    public FatePointDebits(RunLifecycle lifecycle, FatePointLedger ledger)
    { this(lifecycle, ledger, List.of()); }

    private FatePointDebits(RunLifecycle lifecycle, FatePointLedger ledger, List<Receipt> receipts)
    {
        this.lifecycle = Objects.requireNonNull(lifecycle, "lifecycle");
        this.ledger = Objects.requireNonNull(ledger, "ledger");
        this.receipts = List.copyOf(receipts);
    }

    public RunLifecycle lifecycle() { return lifecycle; }
    public FatePointLedger ledger() { return ledger; }
    public List<Receipt> receipts() { return receipts; }

    /**
     * Rebuilds a contiguous debit-only stream from its trusted original seed, validating
     * every recorded result against arithmetic and lifecycle rules. The seed must precede
     * these debits, not contain their resulting ledger. This is not a save migration or
     * an acknowledgement of durability; mixed-operation journals need aggregate validation.
     */
    public static FatePointDebits restore(RunLifecycle seedLifecycle, FatePointLedger seedLedger,
        List<Receipt> history)
    {
        FatePointDebits current = new FatePointDebits(seedLifecycle, seedLedger);
        for (Receipt recorded : List.copyOf(history))
        {
            if (recorded.beforeRevision() != current.lifecycle.revision())
            { throw new IllegalArgumentException("Noncontiguous debit history"); }
            Result result = current.debit(seedLifecycle.identity(), recorded.commandId(),
                recorded.beforeRevision(), recorded.quoteKey(), recorded.quoteVersion(), recorded.amount());
            if (result.proposed() == current || result.receipt().balance() != recorded.balance()
                || result.receipt().afterRevision() != recorded.afterRevision())
            { throw new IllegalArgumentException("Debit receipt disagrees with reconstructed state"); }
            current = result.proposed();
        }
        return current;
    }

    public Result debit(RunIdentity identity, UUID commandId, long expectedRevision,
        String quoteKey, String quoteVersion, long amount)
    {
        Objects.requireNonNull(commandId, "commandId");
        requireText(quoteKey);
        requireText(quoteVersion);
        if (!lifecycle.identity().equals(identity))
        { throw new IllegalArgumentException("Foreign run context"); }
        for (Receipt receipt : receipts)
        {
            if (!receipt.commandId.equals(commandId)) { continue; }
            if (receipt.beforeRevision != expectedRevision || receipt.amount != amount
                || !receipt.quoteKey.equals(quoteKey) || !receipt.quoteVersion.equals(quoteVersion))
            { throw new IllegalArgumentException("Command ID reused for different request"); }
            return new Result(this, receipt);
        }
        if (!lifecycle.acceptsObservation(identity, expectedRevision))
        { throw new IllegalArgumentException("Stale or inactive run context"); }
        long nextRevision = Math.addExact(expectedRevision, 1);
        FatePointLedger nextLedger = ledger.append(
            new FatePointLedger.Entry(FatePointLedger.Kind.DISCRETIONARY_COST, amount));
        RunLifecycle next = new RunLifecycle(identity, nextRevision, lifecycle.state(), lifecycle.obligation());
        Receipt receipt = new Receipt(commandId, expectedRevision, nextRevision,
            quoteKey, quoteVersion, amount, nextLedger.balance());
        List<Receipt> history = new ArrayList<>(receipts);
        history.add(receipt);
        return new Result(new FatePointDebits(next, nextLedger, history), receipt);
    }

    private static void requireText(String value)
    {
        if (Objects.requireNonNull(value, "quote reference").trim().isEmpty())
        { throw new IllegalArgumentException("Empty quote reference"); }
    }
}

package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Combined new-instance/debit proposal for an available active slot (WB-D105/106/118).
 * This is neither authorization nor a durable receipt. The trusted aggregate caller must
 * validate catalog/quote authority, the complete legal edit boundary and historical UUID
 * freshness, reject command-ID reuse across operations, and persist all returned effects
 * with a typed purchase receipt in one write before publishing success. Do not persist
 * the debit separately. No live plugin wiring, replacement or replay service is supplied.
 */
public final class SlicePurchaseProposal
{
    /** Frozen price input; construction does not establish that a price was reviewed. */
    public static final class Quote
    {
        private final String activity;
        private final String key;
        private final String version;
        private final long amount;
        private final SliceInventory.PurchaseKind kind;
        private final long duplicateOrdinal;

        /** First-instance quotes use ordinal zero; duplicate quotes use a positive ordinal. */
        public Quote(String activity, String key, String version, long amount,
            SliceInventory.PurchaseKind kind, long duplicateOrdinal)
        {
            this.activity = text(activity);
            this.key = text(key);
            this.version = text(version);
            this.kind = Objects.requireNonNull(kind, "kind");
            if (amount <= 0 || (kind == SliceInventory.PurchaseKind.FIRST_INSTANCE
                ? duplicateOrdinal != 0 : duplicateOrdinal <= 0))
            { throw new IllegalArgumentException("Invalid purchase quote"); }
            this.amount = amount;
            this.duplicateOrdinal = duplicateOrdinal;
        }

        public String activity() { return activity; }
        public String key() { return key; }
        public String version() { return version; }
        public long amount() { return amount; }
        public SliceInventory.PurchaseKind kind() { return kind; }
        public long duplicateOrdinal() { return duplicateOrdinal; }
    }

    private final RunLifecycle lifecycle;
    private final FatePointLedger ledger;
    private final SliceInventory inventory;
    private final FatePointDebits.Receipt debit;
    private final UUID instanceId;
    private final Quote quote;

    private SlicePurchaseProposal(FatePointDebits.Result payment, SliceInventory inventory,
        UUID instanceId, Quote quote)
    {
        lifecycle = payment.proposed().lifecycle();
        ledger = payment.proposed().ledger();
        debit = payment.receipt();
        this.inventory = inventory;
        this.instanceId = instanceId;
        this.quote = quote;
    }

    public static SlicePurchaseProposal propose(RunLifecycle lifecycle, FatePointLedger ledger,
        SliceInventory inventory, RunIdentity identity, UUID commandId, long expectedRevision,
        UUID instanceId, Quote quote)
    {
        Objects.requireNonNull(lifecycle, "lifecycle");
        Objects.requireNonNull(inventory, "inventory");
        Objects.requireNonNull(quote, "quote");
        // These local guards cannot detect pending draws/offers absent from this partial model.
        if (!lifecycle.acceptsObservation(identity, expectedRevision)
            || lifecycle.obligation() != RunLifecycle.Obligation.NONE)
        { throw new IllegalArgumentException("Purchase requires current active context without obligation"); }
        SliceInventory.PurchaseClassification classification = inventory.classifyNewPurchase(quote.activity);
        if (classification.kind() != quote.kind || (quote.kind == SliceInventory.PurchaseKind.PAID_DUPLICATE
            && classification.duplicateOrdinal() != quote.duplicateOrdinal))
        { throw new IllegalArgumentException("Quote does not match lifetime purchase tier"); }
        SliceInventory next = inventory.proposeNewPurchase(instanceId, quote.activity);
        FatePointDebits.Result payment = new FatePointDebits(lifecycle, ledger).debit(identity,
            commandId, expectedRevision, quote.key, quote.version, quote.amount);
        return new SlicePurchaseProposal(payment, next, instanceId, quote);
    }

    public RunLifecycle lifecycle() { return lifecycle; }
    public FatePointLedger ledger() { return ledger; }
    public SliceInventory inventory() { return inventory; }
    public FatePointDebits.Receipt debit() { return debit; }
    public UUID instanceId() { return instanceId; }
    public Quote quote() { return quote; }

    /** Proposed receipt only; publish it after atomic aggregate persistence acknowledgement. */
    public SlicePurchaseReceipt receipt()
    { return new SlicePurchaseReceipt(lifecycle.identity(), instanceId, quote, debit); }

    private static String text(String value)
    {
        if (Objects.requireNonNull(value, "quote text").isEmpty() || !value.equals(value.trim()))
        { throw new IllegalArgumentException("Invalid quote text"); }
        return value;
    }
}

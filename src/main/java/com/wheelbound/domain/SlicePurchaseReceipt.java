package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Frozen purchase request and original response (WB-D105/118). This value alone proves
 * neither authorization nor durability. A future aggregate journal must validate it
 * against the ledger, inventory and history and persist all effects in one write.
 */
public final class SlicePurchaseReceipt
{
    private final RunIdentity identity;
    private final UUID instanceId;
    private final SlicePurchaseProposal.Quote quote;
    private final FatePointDebits.Receipt debit;

    /** For validated proposal output or decoded data awaiting aggregate validation. */
    public SlicePurchaseReceipt(RunIdentity identity, UUID instanceId,
        SlicePurchaseProposal.Quote quote, FatePointDebits.Receipt debit)
    {
        this.identity = Objects.requireNonNull(identity, "identity");
        this.instanceId = Objects.requireNonNull(instanceId, "instanceId");
        this.quote = Objects.requireNonNull(quote, "quote");
        this.debit = Objects.requireNonNull(debit, "debit");
        if (!quote.key().equals(debit.quoteKey()) || !quote.version().equals(debit.quoteVersion())
            || quote.amount() != debit.amount())
        { throw new IllegalArgumentException("Purchase quote disagrees with debit"); }
    }

    /** Exact request matching includes the original revision, even after later commands. */
    public boolean matches(RunIdentity identity, UUID commandId, long expectedRevision,
        UUID instanceId, SlicePurchaseProposal.Quote quote)
    {
        return quote != null && this.identity.equals(identity) && debit.commandId().equals(commandId)
            && debit.beforeRevision() == expectedRevision && this.instanceId.equals(instanceId)
            && this.quote.activity().equals(quote.activity()) && this.quote.key().equals(quote.key())
            && this.quote.version().equals(quote.version()) && this.quote.amount() == quote.amount()
            && this.quote.kind() == quote.kind() && this.quote.duplicateOrdinal() == quote.duplicateOrdinal();
    }

    public RunIdentity identity() { return identity; }
    public UUID instanceId() { return instanceId; }
    public SlicePurchaseProposal.Quote quote() { return quote; }
    public FatePointDebits.Receipt debit() { return debit; }
}

package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable ordinary-card-only purchase history from a trusted original seed.
 * This is not mixed aggregate admission, price authentication or durable replay.
 * Callers must validate other pending operations and command journals separately.
 */
public final class FateCardPurchaseHistory
{
    public static final class Result
    {
        private final FateCardPurchaseHistory proposed;
        private final FateCardPurchaseReceipt receipt;
        private Result(FateCardPurchaseHistory proposed, FateCardPurchaseReceipt receipt)
        { this.proposed = proposed; this.receipt = receipt; }
        public FateCardPurchaseHistory proposed() { return proposed; }
        public FateCardPurchaseReceipt receipt() { return receipt; }
    }

    private final RunLifecycle lifecycle;
    private final FatePointLedger ledger;
    private final FateCardOwnership ownership;
    private final List<FateCardPurchaseReceipt> receipts;

    public FateCardPurchaseHistory(RunLifecycle lifecycle, FatePointLedger ledger,
        FateCardOwnership ownership)
    { this(lifecycle, ledger, ownership, List.of()); }

    private FateCardPurchaseHistory(RunLifecycle lifecycle, FatePointLedger ledger,
        FateCardOwnership ownership, List<FateCardPurchaseReceipt> receipts)
    {
        this.lifecycle = Objects.requireNonNull(lifecycle, "lifecycle");
        this.ledger = Objects.requireNonNull(ledger, "ledger");
        this.ownership = Objects.requireNonNull(ownership, "ownership");
        if (!lifecycle.identity().equals(ownership.identity()))
        { throw new IllegalArgumentException("Foreign card ownership seed"); }
        this.receipts = List.copyOf(receipts);
    }

    /** Seed must precede every receipt; gaps and mixed-operation streams are refused. */
    public static FateCardPurchaseHistory restore(RunLifecycle lifecycle, FatePointLedger ledger,
        FateCardOwnership ownership, List<FateCardPurchaseReceipt> history)
    {
        var current = new FateCardPurchaseHistory(lifecycle, ledger, ownership);
        for (var receipt : List.copyOf(history))
        {
            if (receipt.debit().beforeRevision() != current.lifecycle.revision())
            { throw new IllegalArgumentException("Noncontiguous card purchase history"); }
            for (var retained : current.receipts)
            {
                if (retained.debit().commandId().equals(receipt.debit().commandId()))
                { throw new IllegalArgumentException("Repeated card purchase command"); }
            }
            var proposal = receipt.reconstruct(current.lifecycle, current.ledger, current.ownership);
            current = current.append(proposal, receipt);
        }
        return current;
    }

    /** Exact retries return the retained original response without a second debit. */
    public Result purchase(RunIdentity identity, UUID commandId, long expectedRevision,
        FateCardPurchaseProposal.Quote quote)
    {
        Objects.requireNonNull(commandId, "commandId");
        Objects.requireNonNull(quote, "quote");
        if (!lifecycle.identity().equals(identity))
        { throw new IllegalArgumentException("Foreign run context"); }
        for (var receipt : receipts)
        {
            if (!receipt.debit().commandId().equals(commandId)) { continue; }
            if (!receipt.matches(identity, commandId, expectedRevision, quote))
            { throw new IllegalArgumentException("Command reused for different card purchase"); }
            return new Result(this, receipt);
        }
        var proposal = FateCardPurchaseProposal.propose(lifecycle, ledger, ownership,
            identity, commandId, expectedRevision, quote);
        var receipt = proposal.receipt();
        return new Result(append(proposal, receipt), receipt);
    }

    private FateCardPurchaseHistory append(FateCardPurchaseProposal proposal,
        FateCardPurchaseReceipt receipt)
    {
        var history = new ArrayList<>(receipts);
        history.add(receipt);
        return new FateCardPurchaseHistory(proposal.lifecycle(), proposal.ledger(),
            proposal.ownership(), history);
    }

    public RunLifecycle lifecycle() { return lifecycle; }
    public FatePointLedger ledger() { return ledger; }
    public FateCardOwnership ownership() { return ownership; }
    public List<FateCardPurchaseReceipt> receipts() { return receipts; }
}

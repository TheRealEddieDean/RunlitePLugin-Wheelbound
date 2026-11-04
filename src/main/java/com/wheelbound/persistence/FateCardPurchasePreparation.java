package com.wheelbound.persistence;

import com.wheelbound.domain.FateCardOwnership;
import com.wheelbound.domain.FateCardPurchaseProposal;
import com.wheelbound.domain.RunIdentity;
import java.util.Objects;
import java.util.UUID;

/** Pure fresh-purchase preparation against one aggregate head; no publication or replay. */
public final class FateCardPurchasePreparation
{
    private FateCardPurchasePreparation() { }

    /**
     * Ownership must be explicit trusted current state. The caller must additionally
     * authenticate the quote and check the separate card journal for command reuse.
     * Persisting this proposal requires future atomic ownership/journal integration;
     * neither a returned proposal nor its receipt acknowledges a successful purchase.
     */
    public static FateCardPurchaseProposal prepareFresh(RunDocument head,
        FateCardOwnership ownership, RunIdentity identity, UUID commandId,
        long expectedRevision, FateCardPurchaseProposal.Quote quote)
    {
        Objects.requireNonNull(head, "head");
        Objects.requireNonNull(ownership, "ownership");
        if (head.pendingMasterSelection().isPresent()
            || head.currentMasterSpecial().isPresent()
            || head.masterSpins().filter(s -> s.reservation().isPresent()).isPresent())
        { throw new IllegalArgumentException("Card purchase cannot cross pending Master work"); }
        // Includes command IDs retained only in domain journals, without an envelope.
        MasterRetryRecovery.requireUnused(head, commandId);
        var ledger = head.fatePoints().orElseThrow(() ->
            new IllegalArgumentException("Explicit Fate Point accounting required"));
        return FateCardPurchaseProposal.propose(head.lifecycle(), ledger, ownership,
            identity, commandId, expectedRevision, quote);
    }
}

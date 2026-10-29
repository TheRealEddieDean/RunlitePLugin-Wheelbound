package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class PurchaseJournalTest
{
    @Test public void validatesEnvelopeAndHistoryEquality()
    { PurchaseJournalChecks.main(new String[0]); }

    @Test public void validatesPurchaseAggregateEvidence()
    { PurchaseAggregateChecks.main(new String[0]); }

    @Test public void schemaSixCannotSilentlyDiscardPurchasePayload()
    {
        RunIdentity identity = new RunIdentity("test-account", UUID.randomUUID());
        SlicePurchaseProposal proposal = SlicePurchaseProposal.propose(
            new RunLifecycle(identity, 0, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
            new FatePointLedger(100, List.of()), PurchaseAggregateChecks.inventory(), identity,
            UUID.randomUUID(), 0, UUID.randomUUID(), new SlicePurchaseProposal.Quote(
                "smithing", "synthetic", "v1", 7, SliceInventory.PurchaseKind.FIRST_INSTANCE, 0));
        FatePointDebits.Receipt debit = proposal.debit();
        SlicePurchaseReceipt purchase = proposal.receipt();
        RunReceipt receipt = new RunReceipt(debit.commandId(), RunReceipt.Operation.SLICE_PURCHASE,
            0, 1, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, null, null, purchase);
        RunDocument document = new RunDocument(proposal.lifecycle(), "synthetic", List.of(receipt),
            Optional.of(proposal.ledger()), Optional.of(proposal.inventory()));
        assertThrows(IllegalArgumentException.class, () -> new RunCodec(new Gson()).encode(document));
    }
}

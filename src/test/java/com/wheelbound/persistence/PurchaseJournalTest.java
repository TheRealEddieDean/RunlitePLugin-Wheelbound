package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class PurchaseJournalTest
{
    @Test public void validatesEnvelopeAndHistoryEquality()
    { PurchaseJournalChecks.main(new String[0]); }

    @Test public void schemaSixCannotSilentlyDiscardPurchasePayload()
    {
        RunIdentity identity = new RunIdentity("test-account", UUID.randomUUID());
        FatePointDebits.Receipt debit = new FatePointDebits.Receipt(UUID.randomUUID(), 0, 1,
            "synthetic", "v1", 7, 93);
        SlicePurchaseReceipt purchase = new SlicePurchaseReceipt(identity, UUID.randomUUID(),
            new SlicePurchaseProposal.Quote("mining", "synthetic", "v1", 7,
                SliceInventory.PurchaseKind.FIRST_INSTANCE, 0), debit);
        RunReceipt receipt = new RunReceipt(debit.commandId(), RunReceipt.Operation.SLICE_PURCHASE,
            0, 1, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, null, null, purchase);
        RunDocument document = new RunDocument(new RunLifecycle(identity, 1,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), "synthetic", List.of(receipt));
        assertThrows(IllegalArgumentException.class, () -> new RunCodec(new Gson()).encode(document));
    }
}

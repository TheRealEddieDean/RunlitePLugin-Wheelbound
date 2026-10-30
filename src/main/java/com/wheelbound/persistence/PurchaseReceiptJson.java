package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.wheelbound.domain.FatePointDebits;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.SliceInventory;
import com.wheelbound.domain.SlicePurchaseProposal;
import com.wheelbound.domain.SlicePurchaseReceipt;
import java.util.Set;
import java.util.UUID;

/** Strict purchase receipt representation for schema-7 aggregate journals.
 * Parsing preserves the original response; it establishes neither authority nor durability. */
final class PurchaseReceiptJson
{
    private PurchaseReceiptJson() { }

    static JsonObject encode(SlicePurchaseReceipt receipt)
    {
        JsonObject value = new JsonObject();
        value.addProperty("accountKey", receipt.identity().accountKey());
        value.addProperty("runId", receipt.identity().runId().toString());
        value.addProperty("instanceId", receipt.instanceId().toString());
        value.addProperty("commandId", receipt.debit().commandId().toString());
        value.addProperty("beforeRevision", receipt.debit().beforeRevision());
        value.addProperty("afterRevision", receipt.debit().afterRevision());
        value.addProperty("activity", receipt.quote().activity());
        value.addProperty("quoteKey", receipt.quote().key());
        value.addProperty("quoteVersion", receipt.quote().version());
        value.addProperty("amount", receipt.quote().amount());
        value.addProperty("kind", receipt.quote().kind().name());
        value.addProperty("duplicateOrdinal", receipt.quote().duplicateOrdinal());
        value.addProperty("balance", receipt.debit().balance());
        return value;
    }

    static SlicePurchaseReceipt decode(JsonObject value, RunIdentity expected)
    {
        RunCodec.keys(value, Set.of("accountKey", "runId", "instanceId", "commandId",
            "beforeRevision", "afterRevision", "activity", "quoteKey", "quoteVersion",
            "amount", "kind", "duplicateOrdinal", "balance"));
        RunIdentity identity = new RunIdentity(RunCodec.string(value, "accountKey"),
            UUID.fromString(RunCodec.string(value, "runId")));
        if (!identity.equals(expected)) { throw new IllegalArgumentException("Foreign purchase receipt"); }
        SlicePurchaseProposal.Quote quote = new SlicePurchaseProposal.Quote(
            RunCodec.string(value, "activity"), RunCodec.string(value, "quoteKey"),
            RunCodec.string(value, "quoteVersion"), RunCodec.integer(value, "amount"),
            SliceInventory.PurchaseKind.valueOf(RunCodec.string(value, "kind")),
            RunCodec.integer(value, "duplicateOrdinal"));
        FatePointDebits.Receipt debit = new FatePointDebits.Receipt(
            UUID.fromString(RunCodec.string(value, "commandId")), RunCodec.integer(value, "beforeRevision"),
            RunCodec.integer(value, "afterRevision"), quote.key(), quote.version(), quote.amount(),
            RunCodec.integer(value, "balance"));
        return new SlicePurchaseReceipt(identity,
            UUID.fromString(RunCodec.string(value, "instanceId")), quote, debit);
    }
}

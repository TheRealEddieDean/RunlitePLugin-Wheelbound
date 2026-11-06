package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wheelbound.domain.FateCardOwnership;
import com.wheelbound.domain.FateCardPurchaseJournal;
import com.wheelbound.domain.FateCardPurchaseProposal;
import com.wheelbound.domain.FateCardPurchaseReceipt;
import com.wheelbound.domain.FatePointDebits;
import com.wheelbound.domain.RunIdentity;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** Candidate lossless structural payload only. Does not authenticate prices/history,
 * migrate aggregate saves, publish purchases or enable card-bearing writes. */
final class FateCardPurchaseJournalCodec
{
    private FateCardPurchaseJournalCodec() { }

    static JsonObject encode(FateCardPurchaseJournal journal)
    {
        Objects.requireNonNull(journal, "journal");
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("accountKey", journal.identity().accountKey());
        value.addProperty("runId", journal.identity().runId().toString());
        JsonArray receipts = new JsonArray();
        for (var receipt : journal.receipts().values())
        {
            var quote = receipt.quote();
            var debit = receipt.debit();
            JsonObject item = new JsonObject();
            item.addProperty("commandId", debit.commandId().toString());
            item.addProperty("beforeRevision", debit.beforeRevision());
            item.addProperty("afterRevision", debit.afterRevision());
            item.addProperty("pool", quote.pool().name());
            item.addProperty("type", quote.type().name());
            item.addProperty("quoteKey", quote.key());
            item.addProperty("quoteVersion", quote.version());
            item.addProperty("amount", quote.amount());
            item.addProperty("balance", debit.balance());
            receipts.add(item);
        }
        value.add("receipts", receipts);
        return value;
    }

    static FateCardPurchaseJournal decode(JsonObject value, RunIdentity expectedIdentity,
        long currentRevision)
    {
        Objects.requireNonNull(expectedIdentity, "expectedIdentity");
        RunCodec.keys(value, Set.of("version", "accountKey", "runId", "receipts"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported card journal version"); }
        RunIdentity identity = new RunIdentity(RunCodec.string(value, "accountKey"),
            uuid(RunCodec.string(value, "runId")));
        if (!identity.equals(expectedIdentity))
        { throw new IllegalArgumentException("Foreign card journal"); }
        JsonElement raw = value.get("receipts");
        if (!raw.isJsonArray()) { throw new IllegalArgumentException("Expected card receipts"); }
        var receipts = new ArrayList<FateCardPurchaseReceipt>();
        for (JsonElement element : raw.getAsJsonArray())
        {
            if (!element.isJsonObject()) { throw new IllegalArgumentException("Expected card receipt"); }
            JsonObject item = element.getAsJsonObject();
            RunCodec.keys(item, Set.of("commandId", "beforeRevision", "afterRevision", "pool",
                "type", "quoteKey", "quoteVersion", "amount", "balance"));
            var quote = new FateCardPurchaseProposal.Quote(
                FateCardOwnership.Pool.valueOf(RunCodec.string(item, "pool")),
                FateCardOwnership.Type.valueOf(RunCodec.string(item, "type")),
                RunCodec.string(item, "quoteKey"), RunCodec.string(item, "quoteVersion"),
                RunCodec.integer(item, "amount"));
            var debit = new FatePointDebits.Receipt(uuid(RunCodec.string(item, "commandId")),
                RunCodec.integer(item, "beforeRevision"), RunCodec.integer(item, "afterRevision"),
                quote.key(), quote.version(), quote.amount(), RunCodec.integer(item, "balance"));
            receipts.add(new FateCardPurchaseReceipt(identity, quote, debit));
        }
        // Preserve commit order and revision gaps; domain validation rejects collisions,
        // overlaps and future receipts. Trusted-seed reconstruction remains separate.
        return new FateCardPurchaseJournal(identity, currentRevision, receipts);
    }

    private static UUID uuid(String text)
    {
        UUID value = UUID.fromString(text);
        if (!value.toString().equals(text))
        { throw new IllegalArgumentException("Noncanonical card journal UUID"); }
        return value;
    }
}

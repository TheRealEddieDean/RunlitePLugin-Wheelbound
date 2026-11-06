package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.util.*;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class FateCardPurchaseJournalCodecTest
{
    private final RunIdentity identity = new RunIdentity("card-journal", new UUID(182, 1));

    private FateCardPurchaseReceipt receipt(int index)
    {
        var quote = new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.values()[index / 2],
            index % 2 == 0 ? FateCardOwnership.Type.LESSER : FateCardOwnership.Type.GREATER,
            "reviewed-key-" + index, "frozen-version-" + index, 7 + index);
        return new FateCardPurchaseReceipt(identity, quote, new FatePointDebits.Receipt(
            new UUID(182, index + 2), index * 3, index * 3 + 1,
            quote.key(), quote.version(), quote.amount(), Long.MAX_VALUE - index));
    }

    private JsonObject payload()
    {
        return FateCardPurchaseJournalCodec.encode(new FateCardPurchaseJournal(identity, 12,
            List.of(receipt(0), receipt(1), receipt(2), receipt(3))));
    }

    @Test public void preservesEveryRequestResponseAndOrderAcrossRevisionGaps()
    {
        Gson gson = new Gson();
        String json = gson.toJson(payload());
        var restored = FateCardPurchaseJournalCodec.decode(new JsonParser().parse(json).getAsJsonObject(), identity, 12);
        assertEquals(json, gson.toJson(FateCardPurchaseJournalCodec.encode(restored)));
        assertEquals(4, restored.receipts().size());
        for (int i = 0; i < 4; i++)
        {
            var original = receipt(i);
            var replay = restored.replay(identity, 12, original.debit().commandId(),
                original.debit().beforeRevision(), original.quote());
            assertEquals(original.debit().balance(), replay.debit().balance());
            assertEquals(original.debit().afterRevision(), replay.debit().afterRevision());
        }
        var empty = new FateCardPurchaseJournal(identity, 0, List.of());
        assertTrue(FateCardPurchaseJournalCodec.decode(FateCardPurchaseJournalCodec.encode(empty),
            identity, 0).receipts().isEmpty());
    }

    @Test public void refusesUnsupportedMalformedForeignAndConflictingPayloads()
    {
        rejects(v -> v.remove("version"));
        rejects(v -> v.addProperty("version", 2));
        rejects(v -> v.addProperty("version", "1"));
        rejects(v -> v.addProperty("version", 1.5));
        rejects(v -> v.addProperty("extra", true));
        rejects(v -> v.addProperty("accountKey", "foreign"));
        rejects(v -> v.addProperty("runId", "0-0-0-0-1"));
        rejects(v -> v.addProperty("runId", new UUID(182, 99).toString()));
        rejects(v -> v.add("receipts", JsonNull.INSTANCE));
        rejects(v -> v.getAsJsonArray("receipts").add(JsonNull.INSTANCE));
        rejects(v -> v.getAsJsonArray("receipts").add(first(v).deepCopy()));
        rejects(v -> first(v).remove("balance"));
        rejects(v -> first(v).addProperty("extra", true));
        rejects(v -> first(v).addProperty("commandId", "0-0-0-0-2"));
        rejects(v -> first(v).addProperty("pool", "CHALLENGE"));
        rejects(v -> first(v).addProperty("type", "STANDARD"));
        rejects(v -> first(v).addProperty("type", "CHALLENGE"));
        rejects(v -> first(v).addProperty("quoteKey", ""));
        rejects(v -> first(v).addProperty("quoteVersion", ""));
        rejects(v -> first(v).addProperty("amount", 0));
        rejects(v -> first(v).addProperty("amount", "7"));
        rejects(v -> first(v).addProperty("amount", 7.5));
        rejects(v -> first(v).addProperty("balance", -1));
        rejects(v -> first(v).addProperty("balance", "9223372036854775807"));
        rejects(v -> first(v).addProperty("beforeRevision", Long.MAX_VALUE));
        rejects(v -> first(v).addProperty("afterRevision", 2));
        rejects(v -> {
            first(v).addProperty("beforeRevision", 12);
            first(v).addProperty("afterRevision", 13);
        });
        rejects(v -> v.getAsJsonArray("receipts").get(1).getAsJsonObject()
            .addProperty("commandId", first(v).get("commandId").getAsString()));
        rejects(v -> {
            var second = v.getAsJsonArray("receipts").get(1).getAsJsonObject();
            second.addProperty("beforeRevision", 0);
            second.addProperty("afterRevision", 1);
        });
    }

    private JsonObject first(JsonObject value)
    { return value.getAsJsonArray("receipts").get(0).getAsJsonObject(); }

    private void rejects(Consumer<JsonObject> mutation)
    {
        var value = payload();
        mutation.accept(value);
        try { FateCardPurchaseJournalCodec.decode(value, identity, 12); fail("Accepted invalid journal: " + value); }
        catch (IllegalArgumentException | ArithmeticException expected) { }
    }
}

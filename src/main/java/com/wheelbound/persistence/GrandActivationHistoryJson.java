package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wheelbound.domain.GrandActivationHistory;
import com.wheelbound.domain.GrandActivationReceipt;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.SealedGrandFate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Structural history payload only; neither current attempt state nor trusted admission. */
final class GrandActivationHistoryJson
{
    private GrandActivationHistoryJson() { }

    static JsonObject encode(GrandActivationHistory history, long currentRevision)
    {
        new GrandActivationHistory(history.seal(), currentRevision, history.receipts().values());
        SealedGrandFate seal = history.seal();
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("accountKey", seal.identity().accountKey());
        value.addProperty("runId", seal.identity().runId().toString());
        value.addProperty("sealCommandId", seal.commandId().toString());
        value.add("sealDraw", RunCodec.encodeDraw(seal.commitment()));
        JsonArray entries = new JsonArray();
        for (GrandActivationReceipt receipt : history.receipts().values())
        { entries.add(GrandActivationReceiptJson.encode(receipt, currentRevision)); }
        value.add("receipts", entries);
        return value;
    }

    static GrandActivationHistory decode(JsonObject value, SealedGrandFate expectedSeal, long currentRevision)
    {
        RunCodec.keys(value, Set.of("version", "accountKey", "runId", "sealCommandId", "sealDraw", "receipts"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported Grand activation history version"); }
        SealedGrandFate seal = new SealedGrandFate(new RunIdentity(RunCodec.string(value, "accountKey"),
            UUID.fromString(RunCodec.string(value, "runId"))),
            UUID.fromString(RunCodec.string(value, "sealCommandId")), RunCodec.decodeDraw(value.get("sealDraw")));
        if (!seal.equals(expectedSeal))
        { throw new IllegalArgumentException("Activation history requires its exact original seal"); }
        List<GrandActivationReceipt> receipts = new ArrayList<>();
        for (JsonElement entry : value.getAsJsonArray("receipts"))
        { receipts.add(GrandActivationReceiptJson.decode(entry.getAsJsonObject(), seal, currentRevision)); }
        return new GrandActivationHistory(seal, currentRevision, receipts);
    }
}

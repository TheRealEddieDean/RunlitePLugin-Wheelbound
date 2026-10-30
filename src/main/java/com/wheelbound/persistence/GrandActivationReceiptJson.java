package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wheelbound.domain.GrandActivationReceipt;
import com.wheelbound.domain.GrandFateChecklist;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.SealedGrandFate;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

/**
 * Versioned original activation payload. This standalone representation does not
 * admit a session, establish a currently active attempt, or implement a store command.
 * Legacy absence must remain absent at the future aggregate integration boundary.
 */
final class GrandActivationReceiptJson
{
    private GrandActivationReceiptJson() { }

    static JsonObject encode(GrandActivationReceipt receipt, long currentRevision)
    {
        checkHead(receipt, currentRevision);
        SealedGrandFate seal = receipt.attempt().checklist().seal();
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("accountKey", seal.identity().accountKey());
        value.addProperty("runId", seal.identity().runId().toString());
        value.addProperty("sealCommandId", seal.commandId().toString());
        value.add("sealDraw", RunCodec.encodeDraw(seal.commitment()));
        value.addProperty("commandId", receipt.commandId().toString());
        value.addProperty("beforeRevision", receipt.beforeRevision());
        value.addProperty("afterRevision", receipt.afterRevision());
        value.addProperty("objective", receipt.attempt().objective().name());
        JsonArray completed = new JsonArray();
        for (GrandFateChecklist.Objective objective : receipt.attempt().checklist().completed())
        { completed.add(objective.name()); }
        value.add("completed", completed);
        return value;
    }

    static GrandActivationReceipt decode(JsonObject value, SealedGrandFate expectedSeal, long currentRevision)
    {
        RunCodec.keys(value, Set.of("version", "accountKey", "runId", "sealCommandId", "sealDraw",
            "commandId", "beforeRevision", "afterRevision", "objective", "completed"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported Grand activation version"); }
        SealedGrandFate seal = new SealedGrandFate(new RunIdentity(RunCodec.string(value, "accountKey"),
            UUID.fromString(RunCodec.string(value, "runId"))),
            UUID.fromString(RunCodec.string(value, "sealCommandId")), RunCodec.decodeDraw(value.get("sealDraw")));
        if (!seal.equals(expectedSeal))
        { throw new IllegalArgumentException("Activation requires its exact original seal"); }
        EnumSet<GrandFateChecklist.Objective> completed = EnumSet.noneOf(GrandFateChecklist.Objective.class);
        JsonElement entries = value.get("completed");
        if (!entries.isJsonArray() || entries.getAsJsonArray().size() > GrandFateChecklist.initial(seal).required().size())
        { throw new IllegalArgumentException("Invalid completed activation prefix"); }
        for (JsonElement entry : entries.getAsJsonArray())
        {
            if (!entry.isJsonPrimitive() || !entry.getAsJsonPrimitive().isString()
                || !completed.add(GrandFateChecklist.Objective.valueOf(entry.getAsString())))
            { throw new IllegalArgumentException("Invalid or duplicate completed objective"); }
        }
        GrandActivationReceipt receipt = new GrandActivationReceipt(
            UUID.fromString(RunCodec.string(value, "commandId")), seal.identity(),
            RunCodec.integer(value, "beforeRevision"), GrandFateChecklist.restore(seal, completed),
            GrandFateChecklist.Objective.valueOf(RunCodec.string(value, "objective")));
        if (receipt.afterRevision() != RunCodec.integer(value, "afterRevision"))
        { throw new IllegalArgumentException("Activation revision pair disagrees"); }
        checkHead(receipt, currentRevision);
        return receipt;
    }

    private static void checkHead(GrandActivationReceipt receipt, long currentRevision)
    {
        if (currentRevision < receipt.afterRevision())
        { throw new IllegalArgumentException("Activation receipt is ahead of aggregate"); }
    }
}

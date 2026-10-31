package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.wheelbound.domain.MasterSelectionResolutionReceipt;
import com.wheelbound.domain.RunIdentity;
import java.util.Set;
import java.util.UUID;

/**
 * Versioned terminal transcript payload. Restoration proves structural consistency
 * only, not durable-prefix freshness, retained creation membership or spin allocation.
 */
final class MasterSelectionResolutionReceiptJson
{
    private MasterSelectionResolutionReceiptJson() { }

    static JsonObject encode(MasterSelectionResolutionReceipt receipt)
    {
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("commandId", receipt.commandId().toString());
        value.addProperty("beforeRevision", receipt.beforeRevision());
        value.addProperty("afterRevision", receipt.afterRevision());
        value.add("creation", MasterSelectionCreationReceiptJson.encode(receipt.creation()));
        value.add("resolved", MasterSelectionProvenanceJson.encode(receipt.resolved()));
        return value;
    }

    static MasterSelectionResolutionReceipt decode(JsonObject value, RunIdentity expected)
    {
        RunCodec.keys(value, Set.of("version", "commandId", "beforeRevision", "afterRevision",
            "creation", "resolved"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported selection resolution receipt version"); }
        if (!value.get("creation").isJsonObject() || !value.get("resolved").isJsonObject())
        { throw new IllegalArgumentException("Expected selection creation and terminal objects"); }
        MasterSelectionResolutionReceipt receipt = new MasterSelectionResolutionReceipt(
            UUID.fromString(RunCodec.string(value, "commandId")), RunCodec.integer(value, "beforeRevision"),
            MasterSelectionCreationReceiptJson.decode(value.getAsJsonObject("creation"), expected),
            MasterSelectionProvenanceJson.decode(value.getAsJsonObject("resolved")));
        if (receipt.afterRevision() != RunCodec.integer(value, "afterRevision"))
        { throw new IllegalArgumentException("Inconsistent selection resolution revision"); }
        return receipt;
    }
}

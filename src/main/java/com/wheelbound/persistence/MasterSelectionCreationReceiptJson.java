package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.wheelbound.domain.MasterSelectionCreationReceipt;
import com.wheelbound.domain.RunIdentity;
import java.util.Set;
import java.util.UUID;

/** Versioned original request payload; decoding establishes neither admission nor durability. */
final class MasterSelectionCreationReceiptJson
{
    private MasterSelectionCreationReceiptJson() { }

    static JsonObject encode(MasterSelectionCreationReceipt receipt)
    {
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("accountKey", receipt.identity().accountKey());
        value.addProperty("runId", receipt.identity().runId().toString());
        value.addProperty("beforeRevision", receipt.beforeRevision());
        value.addProperty("afterRevision", receipt.afterRevision());
        value.add("initial", MasterSelectionProvenanceJson.encode(receipt.initial()));
        return value;
    }

    static MasterSelectionCreationReceipt decode(JsonObject value, RunIdentity expected)
    {
        RunCodec.keys(value, Set.of("version", "accountKey", "runId", "beforeRevision",
            "afterRevision", "initial"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported selection creation receipt version"); }
        RunIdentity identity = new RunIdentity(RunCodec.string(value, "accountKey"),
            UUID.fromString(RunCodec.string(value, "runId")));
        if (!identity.equals(expected))
        { throw new IllegalArgumentException("Foreign selection creation receipt"); }
        if (!value.get("initial").isJsonObject())
        { throw new IllegalArgumentException("Expected original selection object"); }
        MasterSelectionCreationReceipt receipt = new MasterSelectionCreationReceipt(identity,
            RunCodec.integer(value, "beforeRevision"),
            MasterSelectionProvenanceJson.decode(value.getAsJsonObject("initial")));
        if (receipt.afterRevision() != RunCodec.integer(value, "afterRevision"))
        { throw new IllegalArgumentException("Inconsistent selection creation revision"); }
        return receipt;
    }
}

package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wheelbound.domain.MasterSelectionResolutions;
import com.wheelbound.domain.MasterSelectionResolutionReceipt;
import com.wheelbound.domain.RunIdentity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Versioned ordered terminal resolution receipts. Absence in a legacy aggregate must
 * remain separate from an explicitly empty journal. This codec does not authorize
 * resolution, retained creation membership or global aggregate command collisions.
 */
final class MasterSelectionResolutionsJson
{
    private MasterSelectionResolutionsJson() { }

    static JsonObject encode(MasterSelectionResolutions journal, long currentRevision)
    {
        new MasterSelectionResolutions(journal.identity(), currentRevision, journal.receipts().values());
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("accountKey", journal.identity().accountKey());
        value.addProperty("runId", journal.identity().runId().toString());
        JsonArray entries = new JsonArray();
        for (MasterSelectionResolutionReceipt receipt : journal.receipts().values())
        { entries.add(MasterSelectionResolutionReceiptJson.encode(receipt)); }
        value.add("receipts", entries);
        return value;
    }

    static MasterSelectionResolutions decode(JsonObject value, RunIdentity expected, long currentRevision)
    {
        RunCodec.keys(value, Set.of("version", "accountKey", "runId", "receipts"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported selection resolution journal version"); }
        RunIdentity identity = new RunIdentity(RunCodec.string(value, "accountKey"),
            UUID.fromString(RunCodec.string(value, "runId")));
        if (!identity.equals(expected))
        { throw new IllegalArgumentException("Foreign selection resolution journal"); }
        if (!value.get("receipts").isJsonArray())
        { throw new IllegalArgumentException("Expected selection resolution receipt array"); }
        List<MasterSelectionResolutionReceipt> receipts = new ArrayList<>();
        for (JsonElement entry : value.getAsJsonArray("receipts"))
        {
            if (!entry.isJsonObject())
            { throw new IllegalArgumentException("Expected selection resolution receipt object"); }
            receipts.add(MasterSelectionResolutionReceiptJson.decode(entry.getAsJsonObject(), identity));
        }
        return new MasterSelectionResolutions(identity, currentRevision, receipts);
    }
}

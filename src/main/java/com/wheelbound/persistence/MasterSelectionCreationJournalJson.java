package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wheelbound.domain.MasterSelectionCreationJournal;
import com.wheelbound.domain.MasterSelectionCreationReceipt;
import com.wheelbound.domain.RunIdentity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Versioned retained original creation requests. Absence in a legacy aggregate must
 * remain separate from an explicitly empty journal. This codec does not authorize
 * creation or validate global aggregate command collisions.
 */
final class MasterSelectionCreationJournalJson
{
    private MasterSelectionCreationJournalJson() { }

    static JsonObject encode(MasterSelectionCreationJournal journal, long currentRevision)
    {
        new MasterSelectionCreationJournal(journal.identity(), currentRevision, journal.receipts().values());
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("accountKey", journal.identity().accountKey());
        value.addProperty("runId", journal.identity().runId().toString());
        JsonArray entries = new JsonArray();
        for (MasterSelectionCreationReceipt receipt : journal.receipts().values())
        { entries.add(MasterSelectionCreationReceiptJson.encode(receipt)); }
        value.add("receipts", entries);
        return value;
    }

    static MasterSelectionCreationJournal decode(JsonObject value, RunIdentity expected, long currentRevision)
    {
        RunCodec.keys(value, Set.of("version", "accountKey", "runId", "receipts"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported selection creation journal version"); }
        RunIdentity identity = new RunIdentity(RunCodec.string(value, "accountKey"),
            UUID.fromString(RunCodec.string(value, "runId")));
        if (!identity.equals(expected))
        { throw new IllegalArgumentException("Foreign selection creation journal"); }
        if (!value.get("receipts").isJsonArray())
        { throw new IllegalArgumentException("Expected selection creation receipt array"); }
        List<MasterSelectionCreationReceipt> receipts = new ArrayList<>();
        for (JsonElement entry : value.getAsJsonArray("receipts"))
        {
            if (!entry.isJsonObject())
            { throw new IllegalArgumentException("Expected selection creation receipt object"); }
            receipts.add(MasterSelectionCreationReceiptJson.decode(entry.getAsJsonObject(), identity));
        }
        return new MasterSelectionCreationJournal(identity, currentRevision, receipts);
    }
}

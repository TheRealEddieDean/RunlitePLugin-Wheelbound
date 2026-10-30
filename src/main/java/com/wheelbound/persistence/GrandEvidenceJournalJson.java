package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wheelbound.domain.GrandEvidenceJournal;
import com.wheelbound.domain.GrandEvidenceReceipt;
import com.wheelbound.domain.GrandFateChecklist;
import com.wheelbound.domain.GrandFateProgress;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.SealedGrandFate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Versioned full-payload representation for the optional aggregate journal field.
 * Decoding requires separately restored progress and its original seal; it never
 * upgrades legacy references into receipts. Save retention supplies neither
 * authentication nor atomic evidence acceptance.
 */
final class GrandEvidenceJournalJson
{
    private GrandEvidenceJournalJson() { }

    static JsonObject encode(GrandEvidenceJournal journal, long currentRevision)
    {
        // Revalidate even an empty journal against the supplied aggregate head.
        new GrandEvidenceJournal(journal.progress(), currentRevision, journal.receipts().values());
        SealedGrandFate seal = journal.progress().checklist().seal();
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("accountKey", seal.identity().accountKey());
        value.addProperty("runId", seal.identity().runId().toString());
        value.addProperty("sealCommandId", seal.commandId().toString());
        value.add("sealDraw", RunCodec.encodeDraw(seal.commitment()));
        JsonArray entries = new JsonArray();
        for (GrandEvidenceReceipt receipt : journal.receipts().values())
        {
            JsonObject item = new JsonObject();
            item.addProperty("objective", receipt.reference().objective().name());
            item.addProperty("commandId", receipt.reference().receiptId().toString());
            item.addProperty("attemptRevision", receipt.attemptRevision());
            item.addProperty("sourceSessionId", receipt.sourceSessionId().toString());
            item.addProperty("sourceNamespace", receipt.sourceNamespace());
            item.addProperty("eventKey", receipt.eventKey());
            entries.add(item);
        }
        value.add("receipts", entries);
        return value;
    }

    static GrandEvidenceJournal decode(JsonObject value, GrandFateProgress progress, long currentRevision)
    {
        RunCodec.keys(value, Set.of("version", "accountKey", "runId", "sealCommandId", "sealDraw", "receipts"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported Grand journal version"); }
        SealedGrandFate seal = new SealedGrandFate(new RunIdentity(RunCodec.string(value, "accountKey"),
            UUID.fromString(RunCodec.string(value, "runId"))),
            UUID.fromString(RunCodec.string(value, "sealCommandId")), RunCodec.decodeDraw(value.get("sealDraw")));
        if (!seal.equals(progress.checklist().seal()))
        { throw new IllegalArgumentException("Grand journal requires its exact original seal"); }
        List<GrandEvidenceReceipt> receipts = new ArrayList<>();
        JsonArray entries = value.getAsJsonArray("receipts");
        if (entries.size() > progress.checklist().required().size())
        { throw new IllegalArgumentException("Too many Grand receipts"); }
        for (JsonElement element : entries)
        {
            JsonObject item = element.getAsJsonObject();
            RunCodec.keys(item, Set.of("objective", "commandId", "attemptRevision", "sourceSessionId",
                "sourceNamespace", "eventKey"));
            receipts.add(new GrandEvidenceReceipt(seal,
                GrandFateChecklist.Objective.valueOf(RunCodec.string(item, "objective")),
                UUID.fromString(RunCodec.string(item, "commandId")), RunCodec.integer(item, "attemptRevision"),
                UUID.fromString(RunCodec.string(item, "sourceSessionId")),
                RunCodec.string(item, "sourceNamespace"), RunCodec.string(item, "eventKey")));
        }
        return new GrandEvidenceJournal(progress, currentRevision, receipts);
    }
}

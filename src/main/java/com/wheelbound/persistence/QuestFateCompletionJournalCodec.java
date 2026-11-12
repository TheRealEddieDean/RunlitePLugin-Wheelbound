package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wheelbound.domain.QuestFateCompletionJournal;
import com.wheelbound.domain.QuestFateCompletionReceipt;
import com.wheelbound.domain.QuestFateCurrentAssignment;
import com.wheelbound.domain.RunIdentity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Nested supplied history only; no run-schema admission or evidence authentication. */
final class QuestFateCompletionJournalCodec
{
    private QuestFateCompletionJournalCodec() { }

    static JsonObject encode(QuestFateCompletionJournal journal)
    {
        Objects.requireNonNull(journal, "journal");
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("accountKey", journal.identity().accountKey());
        value.addProperty("runId", journal.identity().runId().toString());
        JsonArray receipts = new JsonArray();
        journal.receipts().forEach(receipt -> receipts.add(QuestFateCompletionReceiptCodec.encode(receipt)));
        value.add("receipts", receipts);
        return value;
    }

    /** Each ordered receipt needs its own retained historical active context, not today's head. */
    static QuestFateCompletionJournal decode(JsonObject value, RunIdentity identity,
        List<QuestFateCurrentAssignment> historicalContexts)
    {
        Objects.requireNonNull(identity, "identity");
        List<QuestFateCurrentAssignment> contexts = List.copyOf(historicalContexts);
        RunCodec.keys(value, Set.of("version", "accountKey", "runId", "receipts"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported Questing journal version"); }
        if (!identity.accountKey().equals(RunCodec.string(value, "accountKey"))
            || !identity.runId().toString().equals(RunCodec.string(value, "runId")))
        { throw new IllegalArgumentException("Foreign Questing journal identity"); }
        if (!value.get("receipts").isJsonArray())
        { throw new IllegalArgumentException("Expected Questing receipt array"); }
        JsonArray payloads = value.getAsJsonArray("receipts");
        if (payloads.size() != contexts.size())
        { throw new IllegalArgumentException("Missing or surplus historical Questing contexts"); }
        List<QuestFateCompletionReceipt> receipts = new ArrayList<>();
        for (int index = 0; index < payloads.size(); index++)
        {
            if (!payloads.get(index).isJsonObject())
            { throw new IllegalArgumentException("Expected Questing receipt object"); }
            receipts.add(QuestFateCompletionReceiptCodec.decode(
                payloads.get(index).getAsJsonObject(), contexts.get(index)));
        }
        // Domain restoration rejects duplicate commands, sources and assignments, even exact duplicates.
        return new QuestFateCompletionJournal(identity, receipts);
    }
}

package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wheelbound.domain.MasterSelectionResolutions;
import com.wheelbound.domain.MasterSpinState;
import com.wheelbound.domain.QuestFateCompletionHistory;
import com.wheelbound.domain.QuestFateCurrentAssignment;
import com.wheelbound.domain.RunIdentity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Nested supplied history only; does not authenticate evidence or admit run writes. */
final class QuestFateCompletionHistoryCodec
{
    private QuestFateCompletionHistoryCodec() { }

    static JsonObject encode(QuestFateCompletionHistory history)
    {
        Objects.requireNonNull(history, "history");
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.add("journal", QuestFateCompletionJournalCodec.encode(history.journal()));
        JsonArray contexts = new JsonArray();
        history.contexts().forEach(context -> contexts.add(QuestFateHistoricalContextCodec.encode(context)));
        value.add("contexts", contexts);
        return value;
    }

    /** Ordered accounting must be retained historical inputs, never today's reservation. */
    static QuestFateCompletionHistory decode(JsonObject value, RunIdentity identity,
        List<MasterSelectionResolutions> historicalResolutions, List<MasterSpinState> historicalSpins)
    {
        Objects.requireNonNull(identity, "identity");
        var resolutions = List.copyOf(historicalResolutions);
        var spins = List.copyOf(historicalSpins);
        RunCodec.keys(value, Set.of("version", "journal", "contexts"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported Questing completion history version"); }
        if (!value.get("journal").isJsonObject() || !value.get("contexts").isJsonArray())
        { throw new IllegalArgumentException("Expected Questing journal and historical context array"); }
        JsonArray payloads = value.getAsJsonArray("contexts");
        if (payloads.size() != resolutions.size() || payloads.size() != spins.size())
        { throw new IllegalArgumentException("Missing or surplus historical Questing accounting"); }
        List<QuestFateCurrentAssignment> contexts = new ArrayList<>();
        for (int index = 0; index < payloads.size(); index++)
        {
            if (!payloads.get(index).isJsonObject())
            { throw new IllegalArgumentException("Expected historical Questing context object"); }
            contexts.add(QuestFateHistoricalContextCodec.decode(payloads.get(index).getAsJsonObject(),
                resolutions.get(index), spins.get(index)));
        }
        return new QuestFateCompletionHistory(QuestFateCompletionJournalCodec.decode(
            value.getAsJsonObject("journal"), identity, contexts), contexts);
    }
}

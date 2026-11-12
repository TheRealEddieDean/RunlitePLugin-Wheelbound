package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.wheelbound.domain.MasterSelectionResolutions;
import com.wheelbound.domain.MasterSpinState;
import com.wheelbound.domain.QuestFateCompletionHistory;
import com.wheelbound.domain.RunLifecycle;
import java.util.ArrayList;
import java.util.Set;

/** Reader-only historical declarations; neither current accounting nor evidence authority. */
final class QuestFateRunHistoryCodec
{
    private QuestFateRunHistoryCodec() { }

    static QuestFateCompletionHistory decode(JsonObject value, RunLifecycle head)
    {
        RunCodec.keys(value, Set.of("version", "history", "accounting"));
        if (RunCodec.integer(value, "version") != 1
            || !value.get("history").isJsonObject() || !value.get("accounting").isJsonArray())
        { throw new IllegalArgumentException("Invalid run Questing history envelope"); }
        var history = value.getAsJsonObject("history");
        if (!history.has("contexts") || !history.get("contexts").isJsonArray())
        { throw new IllegalArgumentException("Missing historical Questing contexts"); }
        var contexts = history.getAsJsonArray("contexts");
        var accounting = value.getAsJsonArray("accounting");
        if (contexts.size() != accounting.size())
        { throw new IllegalArgumentException("Missing or surplus historical accounting"); }
        var resolutions = new ArrayList<MasterSelectionResolutions>();
        var spins = new ArrayList<MasterSpinState>();
        for (int index = 0; index < contexts.size(); index++)
        {
            if (!contexts.get(index).isJsonObject() || !accounting.get(index).isJsonObject())
            { throw new IllegalArgumentException("Expected historical context and accounting objects"); }
            long revision = RunCodec.integer(contexts.get(index).getAsJsonObject(), "revision");
            if (revision < 0 || revision > head.revision())
            { throw new IllegalArgumentException("Historical context is newer than run head"); }
            var entry = accounting.get(index).getAsJsonObject();
            RunCodec.keys(entry, Set.of("resolutions", "spins"));
            if (!entry.get("resolutions").isJsonObject() || !entry.get("spins").isJsonObject())
            { throw new IllegalArgumentException("Missing historical terminal or spin state"); }
            resolutions.add(MasterSelectionResolutionsJson.decode(
                entry.getAsJsonObject("resolutions"), head.identity(), revision));
            spins.add(MasterSpinStateJson.decode(entry.getAsJsonObject("spins"), head.identity(), revision));
        }
        return QuestFateCompletionHistoryCodec.decode(history, head.identity(), resolutions, spins);
    }
}

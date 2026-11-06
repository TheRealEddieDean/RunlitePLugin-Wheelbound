package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wheelbound.domain.QuestFateEligibility;
import com.wheelbound.domain.QuestFateOffers;
import com.wheelbound.domain.QuestFateReward;
import com.wheelbound.domain.QuestFateSelection;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

/** Versioned nested Questing payload prerequisite. Does not migrate run saves,
 * authenticate eligibility/quotes or enable assignment publication. Legacy absence
 * remains absence; the enclosing assignment must bind provenance and accounting.
 */
final class QuestFateRewardCodec
{
    private QuestFateRewardCodec() { }

    static JsonObject encode(QuestFateReward reward)
    {
        Objects.requireNonNull(reward, "reward");
        var selection = reward.selection();
        var offers = selection.selection().offers();
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("capacity", offers.capacity().name());
        value.addProperty("catalogVersion", offers.catalogVersion());
        JsonArray observations = new JsonArray();
        for (var observation : selection.eligibility().observations())
        {
            JsonObject item = new JsonObject();
            item.addProperty("questId", observation.questId());
            item.addProperty("routeKey", observation.routeKey());
            item.addProperty("routeVersion", observation.routeVersion());
            item.addProperty("questState", observation.questState().name());
            item.addProperty("prerequisites", observation.prerequisites().name());
            item.addProperty("accountRoute", observation.accountRoute().name());
            observations.add(item);
        }
        // Observation order is exactly the frozen offering order; store it once.
        value.add("observations", observations);
        value.addProperty("selectedQuestId", selection.selection().questId());
        value.addProperty("quoteKey", reward.quoteKey());
        value.addProperty("quoteVersion", reward.quoteVersion());
        value.addProperty("fatePoints", reward.fatePoints());
        return value;
    }

    static QuestFateReward decode(JsonObject value)
    {
        RunCodec.keys(value, Set.of("version", "capacity", "catalogVersion", "observations",
            "selectedQuestId", "quoteKey", "quoteVersion", "fatePoints"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported Questing reward version"); }
        var capacity = QuestFateOffers.Capacity.valueOf(RunCodec.string(value, "capacity"));
        JsonElement raw = value.get("observations");
        if (!raw.isJsonArray())
        { throw new IllegalArgumentException("Expected Questing observations"); }
        var observations = new ArrayList<QuestFateEligibility.Observation>();
        var questIds = new ArrayList<String>();
        for (JsonElement element : raw.getAsJsonArray())
        {
            if (!element.isJsonObject())
            { throw new IllegalArgumentException("Expected Questing observation"); }
            JsonObject item = element.getAsJsonObject();
            RunCodec.keys(item, Set.of("questId", "routeKey", "routeVersion", "questState",
                "prerequisites", "accountRoute"));
            String questId = RunCodec.string(item, "questId");
            questIds.add(questId);
            observations.add(new QuestFateEligibility.Observation(questId,
                RunCodec.string(item, "routeKey"), RunCodec.string(item, "routeVersion"),
                QuestFateEligibility.Status.valueOf(RunCodec.string(item, "questState")),
                QuestFateEligibility.Status.valueOf(RunCodec.string(item, "prerequisites")),
                QuestFateEligibility.Status.valueOf(RunCodec.string(item, "accountRoute"))));
        }
        var offers = new QuestFateOffers(capacity, RunCodec.string(value, "catalogVersion"), questIds);
        var selection = new QuestFateSelection(new QuestFateEligibility(offers, observations),
            offers.select(RunCodec.string(value, "selectedQuestId")));
        return new QuestFateReward(selection, RunCodec.string(value, "quoteKey"),
            RunCodec.string(value, "quoteVersion"), RunCodec.integer(value, "fatePoints"));
    }
}

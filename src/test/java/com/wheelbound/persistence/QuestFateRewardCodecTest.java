package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuestFateRewardCodecTest
{
    private QuestFateReward reward(QuestFateOffers.Capacity capacity, int selected, long amount)
    {
        var ids = new ArrayList<String>();
        var observations = new ArrayList<QuestFateEligibility.Observation>();
        for (int i = 0; i < capacity.count(); i++)
        {
            String id = "quest-" + i;
            ids.add(id);
            observations.add(new QuestFateEligibility.Observation(id, "route-" + i, "v" + i,
                QuestFateEligibility.Status.QUALIFIED, QuestFateEligibility.Status.QUALIFIED,
                QuestFateEligibility.Status.QUALIFIED));
        }
        var offers = new QuestFateOffers(capacity, "catalog-1", ids);
        return new QuestFateReward(new QuestFateSelection(new QuestFateEligibility(offers, observations),
            offers.select(ids.get(selected))), "quote-1", "prices-2", amount);
    }

    @Test public void roundTripsEveryCapacitySelectionAndLongBoundaryThroughGson()
    {
        Gson gson = new Gson();
        for (var capacity : QuestFateOffers.Capacity.values())
        {
            for (int selected = 0; selected < capacity.count(); selected++)
            {
                for (long amount : new long[] {0, 1, Long.MAX_VALUE})
                {
                    var original = reward(capacity, selected, amount);
                    String json = gson.toJson(QuestFateRewardCodec.encode(original));
                    var restored = QuestFateRewardCodec.decode(new JsonParser().parse(json).getAsJsonObject());
                    assertEquals(original, restored);
                    assertEquals(json, gson.toJson(QuestFateRewardCodec.encode(restored)));
                    assertEquals(original.selection().eligibility().observations(),
                        restored.selection().eligibility().observations());
                }
            }
        }
    }

    @Test public void refusesMalformedVersionStructureChoicesRoutesAndQuotes()
    {
        rejects(v -> v.remove("version"));
        rejects(v -> v.addProperty("version", 2));
        rejects(v -> v.addProperty("version", "1"));
        rejects(v -> v.addProperty("version", 1.5));
        rejects(v -> v.addProperty("extra", true));
        rejects(v -> v.addProperty("capacity", "TWO"));
        rejects(v -> v.addProperty("capacity", "FOUR"));
        rejects(v -> v.addProperty("catalogVersion", " catalog"));
        rejects(v -> v.add("observations", JsonNull.INSTANCE));
        rejects(v -> v.addProperty("observations", "quests"));
        rejects(v -> v.getAsJsonArray("observations").remove(0));
        rejects(v -> v.getAsJsonArray("observations").add(JsonNull.INSTANCE));
        rejects(v -> observation(v).addProperty("questId", "quest-1"));
        rejects(v -> observation(v).remove("routeVersion"));
        rejects(v -> observation(v).addProperty("extra", 1));
        rejects(v -> observation(v).addProperty("routeKey", ""));
        rejects(v -> observation(v).addProperty("routeVersion", " v1"));
        rejects(v -> observation(v).addProperty("questState", "COMPLETE"));
        for (String field : new String[] {"questState", "prerequisites", "accountRoute"})
        {
            for (var status : QuestFateEligibility.Status.values())
            {
                if (status != QuestFateEligibility.Status.QUALIFIED)
                { rejects(v -> observation(v).addProperty(field, status.name())); }
            }
            rejects(v -> observation(v).add(field, JsonNull.INSTANCE));
        }
        rejects(v -> v.addProperty("selectedQuestId", "foreign"));
        rejects(v -> v.addProperty("selectedQuestId", 1));
        rejects(v -> v.addProperty("quoteKey", ""));
        rejects(v -> v.remove("quoteVersion"));
        rejects(v -> v.addProperty("fatePoints", -1));
        rejects(v -> v.addProperty("fatePoints", "1"));
        rejects(v -> v.addProperty("fatePoints", 0.5));
        rejects(v -> v.add("fatePoints", new JsonParser().parse("9223372036854775808")));
    }

    @Test public void preservesChangedValidContextWithoutAuthenticatingIt()
    {
        var original = reward(QuestFateOffers.Capacity.THREE, 0, 10);
        JsonObject value = QuestFateRewardCodec.encode(original);
        observation(value).addProperty("routeVersion", "different-route-version");
        var changed = QuestFateRewardCodec.decode(value);
        assertNotEquals(original, changed);
        assertEquals("different-route-version",
            changed.selection().eligibility().observations().get(0).routeVersion());
        try { original.requireSelection(changed.selection()); fail("Changed route accepted"); }
        catch (IllegalArgumentException expected) { }
    }

    private JsonObject observation(JsonObject value)
    { return value.getAsJsonArray("observations").get(0).getAsJsonObject(); }

    private void rejects(Consumer<JsonObject> mutation)
    {
        JsonObject value = QuestFateRewardCodec.encode(reward(QuestFateOffers.Capacity.THREE, 0, 10));
        mutation.accept(value);
        try { QuestFateRewardCodec.decode(value); fail("Accepted malformed reward: " + value); }
        catch (IllegalArgumentException | IllegalStateException | ArithmeticException expected) { }
    }
}

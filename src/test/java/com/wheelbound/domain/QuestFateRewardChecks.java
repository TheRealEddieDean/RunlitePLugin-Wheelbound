package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.wheelbound.domain.QuestFateEligibility.Status.QUALIFIED;

/** Synthetic supplied quotes only; no approved amounts or live route evidence. */
public final class QuestFateRewardChecks
{
    public static void main(String[] args)
    {
        for (QuestFateOffers.Capacity capacity : QuestFateOffers.Capacity.values())
        {
            List<String> ids = new ArrayList<>();
            for (int i = 0; i < capacity.count(); i++) { ids.add("quest-" + i); }
            for (String id : ids)
            {
                QuestFateSelection selected = selection(capacity, ids, id, "catalog-v1", "route-v1");
                QuestFateReward reward = new QuestFateReward(selected, "quote", "v1", 23);
                QuestFateReward restored = new QuestFateReward(
                    selection(capacity, ids, id, "catalog-v1", "route-v1"), "quote", "v1", 23);
                check(reward.equals(restored) && reward.hashCode() == restored.hashCode());
                check(reward.selection() == selected && reward.fatePoints() == 23);
                check(reward.quoteKey().equals("quote") && reward.quoteVersion().equals("v1"));
                reward.requireSelection(restored.selection());
                for (String other : ids)
                {
                    if (!other.equals(id))
                    { fails(() -> reward.requireSelection(selection(capacity, ids, other, "catalog-v1", "route-v1"))); }
                }
                List<String> reversed = new ArrayList<>(ids);
                Collections.reverse(reversed);
                fails(() -> reward.requireSelection(selection(capacity, reversed, id, "catalog-v1", "route-v1")));
                fails(() -> reward.requireSelection(selection(capacity, ids, id, "catalog-v2", "route-v1")));
                fails(() -> reward.requireSelection(selection(capacity, ids, id, "catalog-v1", "route-v2")));
                check(!reward.equals(new QuestFateReward(selected, "other", "v1", 23)));
                check(!reward.equals(new QuestFateReward(selected, "quote", "v2", 23)));
                check(!reward.equals(new QuestFateReward(selected, "quote", "v1", 24)));
                check(new QuestFateReward(selected, "quote", "v1", 0).fatePoints() == 0);
                check(new QuestFateReward(selected, "quote", "v1", Long.MAX_VALUE).fatePoints() == Long.MAX_VALUE);
                fails(() -> new QuestFateReward(selected, "quote", "v1", -1));
                for (String bad : new String[] {null, "", " ", " quote", "quote ", "\n"})
                {
                    fails(() -> new QuestFateReward(selected, bad, "v1", 23));
                    fails(() -> new QuestFateReward(selected, "quote", bad, 23));
                }
                fails(() -> reward.requireSelection(null));
                check(!reward.equals(null) && !reward.equals(selected));
            }
        }
        fails(() -> new QuestFateReward(null, "quote", "v1", 23));
    }

    private static QuestFateSelection selection(QuestFateOffers.Capacity capacity,
        List<String> ids, String selected, String catalog, String routeVersion)
    {
        QuestFateOffers offers = new QuestFateOffers(capacity, catalog, ids);
        List<QuestFateEligibility.Observation> observations = new ArrayList<>();
        for (String id : ids)
        {
            observations.add(new QuestFateEligibility.Observation(id, "route-" + id,
                routeVersion, QUALIFIED, QUALIFIED, QUALIFIED));
        }
        return new QuestFateSelection(new QuestFateEligibility(offers, observations), offers.select(selected));
    }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError(); } }

    private static void fails(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Expected invalid quote/context rejection");
    }
}

package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import static com.wheelbound.domain.QuestFateEligibility.Status.*;

/** Synthetic bindings only; no runtime catalog or route authority. */
public final class QuestFateSelectionChecks
{
    public static void main(String[] args)
    {
        for (QuestFateOffers.Capacity capacity : QuestFateOffers.Capacity.values())
        {
            List<String> ids = List.of("a", "b", "c", "d", "e").subList(0, capacity.count());
            QuestFateOffers offers = new QuestFateOffers(capacity, "v1", ids);
            QuestFateEligibility eligibility = snapshot(offers, "route", "v1");
            for (String id : ids)
            {
                QuestFateSelection selected = new QuestFateSelection(eligibility, offers.select(id));
                QuestFateSelection restored = new QuestFateSelection(snapshot(offers, "route", "v1"), offers.select(id));
                selected.requireSameSelection(restored);
                check(selected.equals(restored) && selected.hashCode() == restored.hashCode());
                check(selected.eligibility() == eligibility && selected.selection().questId().equals(id));
                check(!selected.equals(null) && !selected.equals(offers));
                for (String other : ids)
                {
                    if (!id.equals(other))
                    { invalid(() -> selected.requireSameSelection(new QuestFateSelection(eligibility, offers.select(other)))); }
                }
                invalid(() -> selected.requireSameSelection(new QuestFateSelection(snapshot(offers, "other", "v1"), offers.select(id))));
                invalid(() -> selected.requireSameSelection(new QuestFateSelection(snapshot(offers, "route", "v2"), offers.select(id))));
                invalid(() -> selected.requireSameSelection(null));
            }
            ArrayList<String> reversed = new ArrayList<>(ids);
            java.util.Collections.reverse(reversed);
            invalid(() -> new QuestFateSelection(eligibility,
                new QuestFateOffers(capacity, "v1", reversed).select("a")));
            invalid(() -> new QuestFateSelection(eligibility,
                new QuestFateOffers(capacity, "v2", ids).select("a")));
            // Every displayed choice must qualify, including choices not selected.
            for (int position = 0; position < ids.size(); position++)
            {
                for (QuestFateEligibility.Status status : QuestFateEligibility.Status.values())
                {
                    if (status == QUALIFIED) { continue; }
                    for (int axis = 0; axis < 3; axis++)
                    {
                        ArrayList<QuestFateEligibility.Observation> changed = new ArrayList<>(eligibility.observations());
                        changed.set(position, new QuestFateEligibility.Observation(ids.get(position), "route", "v1",
                            axis == 0 ? status : QUALIFIED, axis == 1 ? status : QUALIFIED,
                            axis == 2 ? status : QUALIFIED));
                        QuestFateEligibility failed = new QuestFateEligibility(offers, changed);
                        invalid(() -> new QuestFateSelection(failed, offers.select("a")));
                    }
                }
            }
            invalid(() -> new QuestFateSelection(null, offers.select("a")));
            invalid(() -> new QuestFateSelection(eligibility, null));
        }
        System.out.println("QuestFateSelectionChecks PASSED");
    }

    private static QuestFateEligibility snapshot(QuestFateOffers offers, String route, String version)
    {
        ArrayList<QuestFateEligibility.Observation> observations = new ArrayList<>();
        for (String id : offers.questIds())
        { observations.add(new QuestFateEligibility.Observation(id, route, version, QUALIFIED, QUALIFIED, QUALIFIED)); }
        return new QuestFateEligibility(offers, observations);
    }

    private static void check(boolean condition) { if (!condition) { throw new AssertionError(); } }
    private static void invalid(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException | NullPointerException expected) { return; }
        throw new AssertionError("Expected refusal");
    }
}

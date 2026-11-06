package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import static com.wheelbound.domain.QuestFateEligibility.Status.*;

/** Synthetic supplied observations; no authored runtime route catalog is implied. */
public final class QuestFateEligibilityChecks
{
    public static void main(String[] args)
    {
        for (QuestFateOffers.Capacity capacity : QuestFateOffers.Capacity.values())
        {
            List<String> ids = List.of("a", "b", "c", "d", "e").subList(0, capacity.count());
            QuestFateOffers offers = new QuestFateOffers(capacity, "v1", ids);
            ArrayList<QuestFateEligibility.Observation> source = new ArrayList<>();
            for (String id : ids) { source.add(observation(id, QUALIFIED, QUALIFIED, QUALIFIED)); }
            QuestFateEligibility snapshot = new QuestFateEligibility(offers, source);
            QuestFateEligibility restored = new QuestFateEligibility(offers, List.copyOf(source));
            snapshot.requireQualified(new QuestFateOffers(capacity, "v1", ids));
            check(snapshot.equals(restored) && snapshot.hashCode() == restored.hashCode());
            check(snapshot.offers() == offers && !snapshot.equals(null));
            invalid(() -> snapshot.requireQualified(new QuestFateOffers(capacity, "v2", ids)));
            ArrayList<String> reversed = new ArrayList<>(ids);
            java.util.Collections.reverse(reversed);
            invalid(() -> snapshot.requireQualified(new QuestFateOffers(capacity, "v1", reversed)));
            invalid(() -> new QuestFateEligibility(offers, source.subList(1, source.size())));
            ArrayList<QuestFateEligibility.Observation> duplicate = new ArrayList<>(source);
            duplicate.set(1, duplicate.get(0));
            invalid(() -> new QuestFateEligibility(offers, duplicate));
            for (QuestFateEligibility.Status status : QuestFateEligibility.Status.values())
            {
                if (status == QUALIFIED) { continue; }
                for (int axis = 0; axis < 3; axis++)
                {
                    ArrayList<QuestFateEligibility.Observation> changed = new ArrayList<>(source);
                    changed.set(0, observation("a", axis == 0 ? status : QUALIFIED,
                        axis == 1 ? status : QUALIFIED, axis == 2 ? status : QUALIFIED));
                    QuestFateEligibility failed = new QuestFateEligibility(offers, changed);
                    invalid(() -> failed.requireQualified(offers));
                    check(!snapshot.equals(failed));
                    check(failed.observations().get(0).equals(changed.get(0)));
                }
            }
            ArrayList<QuestFateEligibility.Observation> routeChanged = new ArrayList<>(source);
            routeChanged.set(0, new QuestFateEligibility.Observation("a", "route.other", "v1",
                QUALIFIED, QUALIFIED, QUALIFIED));
            check(!snapshot.equals(new QuestFateEligibility(offers, routeChanged)));
            routeChanged.set(0, new QuestFateEligibility.Observation("a", "route", "v2",
                QUALIFIED, QUALIFIED, QUALIFIED));
            check(!snapshot.equals(new QuestFateEligibility(offers, routeChanged)));
            try { snapshot.observations().clear(); throw new AssertionError(); }
            catch (UnsupportedOperationException expected) { }
            source.clear();
            check(snapshot.equals(restored));
        }
        for (String bad : new String[] {null, "", " ", " x", "x "})
        {
            invalid(() -> new QuestFateEligibility.Observation(bad, "r", "v", QUALIFIED, QUALIFIED, QUALIFIED));
            invalid(() -> new QuestFateEligibility.Observation("a", bad, "v", QUALIFIED, QUALIFIED, QUALIFIED));
            invalid(() -> new QuestFateEligibility.Observation("a", "r", bad, QUALIFIED, QUALIFIED, QUALIFIED));
        }
        invalid(() -> observation("a", null, QUALIFIED, QUALIFIED));
        invalid(() -> observation("a", QUALIFIED, null, QUALIFIED));
        invalid(() -> observation("a", QUALIFIED, QUALIFIED, null));
        invalid(() -> new QuestFateEligibility(null, List.of()));
        QuestFateOffers offers = new QuestFateOffers(QuestFateOffers.Capacity.THREE, "v1", List.of("a", "b", "c"));
        invalid(() -> new QuestFateEligibility(offers, null));
        System.out.println("QuestFateEligibilityChecks PASSED");
    }

    private static QuestFateEligibility.Observation observation(String id,
        QuestFateEligibility.Status state, QuestFateEligibility.Status prerequisites,
        QuestFateEligibility.Status route)
    { return new QuestFateEligibility.Observation(id, "route", "v1", state, prerequisites, route); }

    private static void check(boolean condition) { if (!condition) { throw new AssertionError(); } }
    private static void invalid(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException | NullPointerException expected) { return; }
        throw new AssertionError("Expected refusal");
    }
}

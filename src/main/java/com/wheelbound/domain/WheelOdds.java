package com.wheelbound.domain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Raw Master Wheel draw probabilities, including every active special and ordinary slice.
 * These are not accepted-assignment odds after verified-empty retries. Comparing two
 * validated snapshots is a preview only and grants no edit, purchase or spin authority.
 */
public final class WheelOdds
{
    /** Exact fraction in quarter-weight units; rounding belongs to the presentation layer. */
    public static final class Probability
    {
        private final int weightUnits;
        private final int totalWeightUnits;

        private Probability(int weightUnits, int totalWeightUnits)
        {
            this.weightUnits = weightUnits;
            this.totalWeightUnits = totalWeightUnits;
        }

        public int weightUnits() { return weightUnits; }
        public int totalWeightUnits() { return totalWeightUnits; }
        public double fraction() { return (double) weightUnits / totalWeightUnits; }
    }

    private final Map<UUID, Probability> slices;
    private final Map<String, Probability> activities;

    public WheelOdds(ActiveWheel wheel)
    {
        Objects.requireNonNull(wheel, "wheel");
        Map<UUID, Probability> bySlice = new LinkedHashMap<>();
        Map<String, Integer> activityWeights = new LinkedHashMap<>();
        for (ActiveWheel.Slice slice : wheel.slices())
        {
            bySlice.put(slice.id(), new Probability(slice.weightUnits(), wheel.totalWeightUnits()));
            if (slice.kind() == ActiveWheel.Kind.ORDINARY)
            {
                activityWeights.merge(slice.activity(), slice.weightUnits(), Math::addExact);
            }
        }
        Map<String, Probability> byActivity = new LinkedHashMap<>();
        activityWeights.forEach((key, weight) ->
            byActivity.put(key, new Probability(weight, wheel.totalWeightUnits())));
        slices = Collections.unmodifiableMap(bySlice);
        activities = Collections.unmodifiableMap(byActivity);
    }

    /** Snapshot order is retained for presentation; each UUID remains a separate outcome. */
    public Map<UUID, Probability> slices() { return slices; }

    /** Ordinary activity copies are summed; special weights remain in every denominator. */
    public Map<String, Probability> activities() { return activities; }
}

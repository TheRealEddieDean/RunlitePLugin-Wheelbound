package com.wheelbound.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** Validated mode snapshot only: no ownership, purchasing, readiness or spin authorization. */
public final class ActiveWheel
{
    public static final int CAPACITY = 24;

    /** Integer quarter units avoid rounding the approved ordinary weight ladder. */
    public enum Enhancement
    {
        BASE(4), FIRST(5), SECOND(6), THIRD(8), FOURTH(12);
        private final int units;
        Enhancement(int units) { this.units = units; }
        public int weightUnits() { return units; }
    }

    public enum Kind { ORDINARY, TAINT, SACRIFICE }

    public static final class Slice
    {
        private final UUID id;
        private final Kind kind;
        private final String activity;
        private final Enhancement enhancement;
        private final int weightUnits;

        private Slice(UUID id, Kind kind, String activity, Enhancement enhancement, int weightUnits)
        {
            this.id = Objects.requireNonNull(id, "id");
            this.kind = Objects.requireNonNull(kind, "kind");
            this.activity = activity;
            this.enhancement = enhancement;
            if (weightUnits <= 0) { throw new IllegalArgumentException("Nonpositive weight"); }
            this.weightUnits = weightUnits;
        }

        /** Activity must be a canonical catalog key; Combat is a single activity key. */
        public static Slice ordinary(UUID id, String activity, Enhancement enhancement)
        {
            Objects.requireNonNull(activity, "activity");
            if (activity.isEmpty() || !activity.equals(activity.trim()))
            { throw new IllegalArgumentException("Invalid activity key"); }
            return new Slice(id, Kind.ORDINARY, activity,
                Objects.requireNonNull(enhancement, "enhancement"), enhancement.weightUnits());
        }

        /** Explicit snapshot weight, never a default or a Defy progression policy. */
        public static Slice special(UUID id, Kind kind, int weightUnits)
        {
            Objects.requireNonNull(kind, "kind");
            if (kind == Kind.ORDINARY) { throw new IllegalArgumentException("Ordinary requires activity/tier"); }
            return new Slice(id, kind, null, null, weightUnits);
        }

        public UUID id() { return id; }
        public Kind kind() { return kind; }
        public String activity() { return activity; }
        public Enhancement enhancement() { return enhancement; }
        public int weightUnits() { return weightUnits; }
    }

    private final List<Slice> slices;
    private final int totalWeightUnits;

    public ActiveWheel(List<Slice> slices)
    {
        this.slices = List.copyOf(slices);
        if (this.slices.size() > CAPACITY) { throw new IllegalArgumentException("Wheel exceeds capacity"); }
        Set<UUID> ids = new HashSet<>();
        Set<String> activities = new HashSet<>();
        int ordinary = 0;
        int total = 0;
        for (Slice slice : this.slices)
        {
            if (!ids.add(slice.id())) { throw new IllegalArgumentException("Duplicate slice ID"); }
            if (slice.kind() == Kind.ORDINARY)
            {
                ordinary++;
                activities.add(slice.activity());
            }
            total = Math.addExact(total, slice.weightUnits());
        }
        if (ordinary < 5 || activities.size() < 3)
        { throw new IllegalArgumentException("Wheel requires five ordinary slices across three activities"); }
        totalWeightUnits = total;
    }

    public List<Slice> slices() { return slices; }
    public int totalWeightUnits() { return totalWeightUnits; }

    /** Visual reorder must contain exactly the same instances; all weights/modifiers survive. */
    public ActiveWheel reordered(List<UUID> order)
    {
        List<UUID> requested = List.copyOf(order);
        if (requested.size() != slices.size() || new HashSet<>(requested).size() != slices.size())
        { throw new IllegalArgumentException("Reorder must be a permutation"); }
        java.util.Map<UUID, Slice> byId = new java.util.HashMap<>();
        for (Slice slice : slices) { byId.put(slice.id(), slice); }
        java.util.ArrayList<Slice> result = new java.util.ArrayList<>();
        for (UUID id : requested)
        {
            Slice slice = byId.get(id);
            if (slice == null) { throw new IllegalArgumentException("Unknown slice ID"); }
            result.add(slice);
        }
        return new ActiveWheel(result);
    }
}

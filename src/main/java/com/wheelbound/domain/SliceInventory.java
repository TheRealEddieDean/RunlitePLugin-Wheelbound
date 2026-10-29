package com.wheelbound.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Immutable ownership snapshot. No purchases, paid movement, destruction or grants.
 * Catalog admission and acquisition evidence are responsibilities of the restoring caller.
 */
public final class SliceInventory
{
    public enum SatchelTier
    {
        BASE(3), FIRST(6), SECOND(10), THIRD(15);
        private final int capacity;
        SatchelTier(int capacity) { this.capacity = capacity; }
        public int capacity() { return capacity; }
    }

    /** Presence records the first-ever instance; starting grants have zero paid duplicates. */
    public static final class AcquisitionHistory
    {
        private final long paidDuplicates;

        public AcquisitionHistory(long paidDuplicates)
        {
            if (paidDuplicates < 0) { throw new IllegalArgumentException("Negative duplicate count"); }
            this.paidDuplicates = paidDuplicates;
        }

        public long paidDuplicates() { return paidDuplicates; }
    }

    private final ActiveWheel activeWheel;
    private final List<ActiveWheel.Slice> stored;
    private final SatchelTier satchelTier;
    private final Set<String> unlockedActivities;
    private final Map<String, AcquisitionHistory> acquisitionHistory;

    public SliceInventory(ActiveWheel activeWheel, List<ActiveWheel.Slice> stored,
        SatchelTier satchelTier, Set<String> unlockedActivities,
        Map<String, AcquisitionHistory> acquisitionHistory)
    {
        this.activeWheel = Objects.requireNonNull(activeWheel, "activeWheel");
        this.stored = List.copyOf(stored);
        this.satchelTier = Objects.requireNonNull(satchelTier, "satchelTier");
        this.unlockedActivities = Set.copyOf(unlockedActivities);
        this.acquisitionHistory = Map.copyOf(acquisitionHistory);
        if (this.stored.size() > satchelTier.capacity())
        { throw new IllegalArgumentException("Satchel exceeds capacity"); }
        for (String activity : this.unlockedActivities) { validateKey(activity); }
        for (String activity : this.acquisitionHistory.keySet())
        {
            validateKey(activity);
            if (!this.unlockedActivities.contains(activity))
            { throw new IllegalArgumentException("Acquisition requires retained activity unlock"); }
        }
        Set<UUID> ids = new HashSet<>();
        for (ActiveWheel.Slice slice : activeWheel.slices()) { validateOwned(slice, ids); }
        for (ActiveWheel.Slice slice : this.stored)
        {
            if (slice.kind() != ActiveWheel.Kind.ORDINARY)
            { throw new IllegalArgumentException("Special slices cannot enter Satchel"); }
            validateOwned(slice, ids);
        }
    }

    private static void validateKey(String activity)
    {
        if (activity.isEmpty() || !activity.equals(activity.trim()))
        { throw new IllegalArgumentException("Invalid activity key"); }
    }

    private void validateOwned(ActiveWheel.Slice slice, Set<UUID> ids)
    {
        if (!ids.add(slice.id())) { throw new IllegalArgumentException("Duplicate owned slice ID"); }
        if (slice.kind() == ActiveWheel.Kind.ORDINARY
            && !acquisitionHistory.containsKey(slice.activity()))
        { throw new IllegalArgumentException("Owned ordinary slice requires acquisition history"); }
    }

    public ActiveWheel activeWheel() { return activeWheel; }
    public List<ActiveWheel.Slice> stored() { return stored; }
    public SatchelTier satchelTier() { return satchelTier; }
    public Set<String> unlockedActivities() { return unlockedActivities; }
    public Map<String, AcquisitionHistory> acquisitionHistory() { return acquisitionHistory; }
}

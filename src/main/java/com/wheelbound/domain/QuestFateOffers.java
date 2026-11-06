package com.wheelbound.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Frozen Questing choices (WB-D028/046), separate from same-type XP cards.
 * This value accepts an already resolved offering set; it does not generate quests
 * or establish eligibility. The caller must validate current quest prerequisites
 * and an authored account-compatible access route (WB-D070), then atomically bind
 * offers, randomness, rewards, restrictions and selection to an assignment.
 */
public final class QuestFateOffers
{
    /** Capacity only: no unlock threshold, price or purchase is implied. */
    public enum Capacity
    {
        THREE(3), FOUR(4), FIVE(5);

        private final int count;
        Capacity(int count) { this.count = count; }
        public int count() { return count; }
    }

    private final Capacity capacity;
    private final String catalogVersion;
    private final List<String> questIds;

    /**
     * Restore the exact original order without RNG. Requires a complete set for
     * the supplied capacity. A smaller/empty set is not proof of an empty eligible
     * pool and does not authorize a retry or define reduced-choice gameplay.
     */
    public QuestFateOffers(Capacity capacity, String catalogVersion, List<String> questIds)
    {
        this.capacity = Objects.requireNonNull(capacity, "capacity");
        this.catalogVersion = key(catalogVersion);
        this.questIds = List.copyOf(questIds);
        if (this.questIds.size() != capacity.count())
        { throw new IllegalArgumentException("Incomplete Questing offering set"); }
        HashSet<String> distinct = new HashSet<>();
        for (String id : this.questIds)
        {
            if (!distinct.add(key(id)))
            { throw new IllegalArgumentException("Repeated quest choice"); }
        }
    }

    public Capacity capacity() { return capacity; }
    public String catalogVersion() { return catalogVersion; }
    public List<String> questIds() { return questIds; }

    /** Pure selection payload; does not enforce a durable one-time choice. */
    public Selection select(String questId)
    { return new Selection(this, questId); }

    public static final class Selection
    {
        private final QuestFateOffers offers;
        private final String questId;

        public Selection(QuestFateOffers offers, String questId)
        {
            this.offers = Objects.requireNonNull(offers, "offers");
            this.questId = key(questId);
            if (!offers.questIds.contains(this.questId))
            { throw new IllegalArgumentException("Quest not in frozen offering set"); }
        }

        public QuestFateOffers offers() { return offers; }
        public String questId() { return questId; }

        @Override public boolean equals(Object value)
        {
            if (!(value instanceof Selection)) { return false; }
            Selection other = (Selection) value;
            return offers.equals(other.offers) && questId.equals(other.questId);
        }

        @Override public int hashCode() { return Objects.hash(offers, questId); }
    }

    private static String key(String value)
    {
        Objects.requireNonNull(value, "quest identity/catalog version");
        if (value.isBlank() || !value.equals(value.strip()))
        { throw new IllegalArgumentException("Noncanonical quest identity/catalog version"); }
        return value;
    }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof QuestFateOffers)) { return false; }
        QuestFateOffers other = (QuestFateOffers) value;
        return capacity == other.capacity && catalogVersion.equals(other.catalogVersion)
            && questIds.equals(other.questIds);
    }

    @Override public int hashCode() { return Objects.hash(capacity, catalogVersion, questIds); }
}

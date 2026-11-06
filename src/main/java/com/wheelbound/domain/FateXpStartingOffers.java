package com.wheelbound.domain;

import java.util.List;
import java.util.Objects;

/**
 * Frozen XP portion of the two starting Standard offers (WB-D050/091).
 * Supplied seeds identify the separate generation streams, not proof of fair
 * generation. The caller must authenticate the snapshot, generator and targets,
 * including legal-domain size and collision handling. No generator is run here.
 * This is not a complete offering/assignment: reward, restrictions, run identity
 * and durable publication must be bound before display. Upgraded and purchased
 * card pools are deliberately outside this starting-only component.
 */
public final class FateXpStartingOffers
{
    private final List<Offer> offers;

    public FateXpStartingOffers(Offer first, Offer second)
    {
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(second, "second");
        FateXpObjective a = first.objective();
        FateXpObjective b = second.objective();
        if (!a.skillKey().equals(b.skillKey()) || a.combatSkill() != b.combatSkill()
            || a.realLevel() != b.realLevel() || a.baselineXp() != b.baselineXp()
            || !a.catalogVersion().equals(b.catalogVersion())
            || !a.generationVersion().equals(b.generationVersion()))
        { throw new IllegalArgumentException("Starting offers require one frozen generation context"); }
        if (a.requiredXp() == b.requiredXp())
        { throw new IllegalArgumentException("Starting Standard XP targets must differ"); }
        offers = List.of(first, second);
    }

    /** Stable display order; callers cannot replace or regenerate an entry. */
    public List<Offer> offers() { return offers; }

    @Override public boolean equals(Object value)
    {
        return value instanceof FateXpStartingOffers
            && offers.equals(((FateXpStartingOffers) value).offers);
    }

    @Override public int hashCode() { return offers.hashCode(); }

    /** Each seed is the root of that offer's caller-supplied generation stream. */
    public static final class Offer
    {
        private final FateXpObjective objective;
        private final long generationSeed;

        public Offer(FateXpObjective objective, long generationSeed)
        {
            this.objective = Objects.requireNonNull(objective, "objective");
            this.generationSeed = generationSeed;
        }

        public FateXpObjective objective() { return objective; }
        public long generationSeed() { return generationSeed; }

        @Override public boolean equals(Object value)
        {
            if (!(value instanceof Offer)) { return false; }
            Offer other = (Offer) value;
            return objective.equals(other.objective) && generationSeed == other.generationSeed;
        }

        @Override public int hashCode() { return Objects.hash(objective, generationSeed); }
    }
}

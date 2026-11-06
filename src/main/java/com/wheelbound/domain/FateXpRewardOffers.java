package com.wheelbound.domain;

import java.util.List;
import java.util.Objects;

/**
 * Frozen reward portion of starting Standard XP offers (WB-D050; STATE_CONTRACT).
 * Each supplied quote is bound to its complete objective, not merely a target
 * number or display slot. The caller authenticates reviewed quote provenance and
 * calculation; strings and amounts alone do not establish that authority.
 * No reward formula, settlement ordering or payment is implemented here.
 * Restrictions, eligibility/identity and durable publication are still required
 * before this component can become displayable offers or an active assignment.
 */
public final class FateXpRewardOffers
{
    private final FateXpStartingOffers targets;
    private final List<Reward> rewards;

    public FateXpRewardOffers(FateXpStartingOffers targets, Reward first, Reward second)
    {
        this.targets = Objects.requireNonNull(targets, "targets");
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(second, "second");
        if (!targets.offers().get(0).objective().equals(first.objective())
            || !targets.offers().get(1).objective().equals(second.objective()))
        { throw new IllegalArgumentException("Reward quote must match its frozen offer objective"); }
        rewards = List.of(first, second);
    }

    public FateXpStartingOffers targets() { return targets; }
    /** Same immutable order as targets().offers(). */
    public List<Reward> rewards() { return rewards; }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof FateXpRewardOffers)) { return false; }
        FateXpRewardOffers other = (FateXpRewardOffers) value;
        return targets.equals(other.targets) && rewards.equals(other.rewards);
    }

    @Override public int hashCode() { return Objects.hash(targets, rewards); }

    /** A supplied earning quote, distinct from a ledger credit or audit result. */
    public static final class Reward
    {
        private final FateXpObjective objective;
        private final String quoteKey;
        private final String quoteVersion;
        private final long fatePoints;

        public Reward(FateXpObjective objective, String quoteKey, String quoteVersion, long fatePoints)
        {
            this.objective = Objects.requireNonNull(objective, "objective");
            this.quoteKey = key(quoteKey);
            this.quoteVersion = key(quoteVersion);
            if (fatePoints < 0)
            { throw new IllegalArgumentException("Reward quote cannot be a deduction"); }
            this.fatePoints = fatePoints;
        }

        private static String key(String value)
        {
            Objects.requireNonNull(value, "quote key/version");
            if (value.isBlank() || !value.equals(value.strip()))
            { throw new IllegalArgumentException("Noncanonical quote key/version"); }
            return value;
        }

        public FateXpObjective objective() { return objective; }
        public String quoteKey() { return quoteKey; }
        public String quoteVersion() { return quoteVersion; }
        public long fatePoints() { return fatePoints; }

        @Override public boolean equals(Object value)
        {
            if (!(value instanceof Reward)) { return false; }
            Reward other = (Reward) value;
            return objective.equals(other.objective) && quoteKey.equals(other.quoteKey)
                && quoteVersion.equals(other.quoteVersion) && fatePoints == other.fatePoints;
        }

        @Override public int hashCode()
        { return Objects.hash(objective, quoteKey, quoteVersion, fatePoints); }
    }
}

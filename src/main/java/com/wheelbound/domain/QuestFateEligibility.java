package com.wheelbound.domain;

import java.util.List;
import java.util.Objects;

/**
 * Supplied quest eligibility observations, frozen in offering order (WB-D070/102).
 * Does not resolve or authenticate authored routes, prerequisite completeness,
 * account access, or client evidence. No display or assignment authority is granted.
 */
public final class QuestFateEligibility
{
    public enum Status { QUALIFIED, INELIGIBLE, UNKNOWN, UNSUPPORTED }

    public static final class Observation
    {
        private final String questId;
        private final String routeKey;
        private final String routeVersion;
        private final Status questState;
        private final Status prerequisites;
        private final Status accountRoute;

        /** Route status must include required vendor access; quest need waives nothing. */
        public Observation(String questId, String routeKey, String routeVersion,
            Status questState, Status prerequisites, Status accountRoute)
        {
            this.questId = key(questId);
            this.routeKey = key(routeKey);
            this.routeVersion = key(routeVersion);
            this.questState = Objects.requireNonNull(questState, "questState");
            this.prerequisites = Objects.requireNonNull(prerequisites, "prerequisites");
            this.accountRoute = Objects.requireNonNull(accountRoute, "accountRoute");
        }

        public String questId() { return questId; }
        public String routeKey() { return routeKey; }
        public String routeVersion() { return routeVersion; }
        public Status questState() { return questState; }
        public Status prerequisites() { return prerequisites; }
        public Status accountRoute() { return accountRoute; }

        @Override public boolean equals(Object value)
        {
            if (!(value instanceof Observation)) { return false; }
            Observation other = (Observation) value;
            return questId.equals(other.questId) && routeKey.equals(other.routeKey)
                && routeVersion.equals(other.routeVersion) && questState == other.questState
                && prerequisites == other.prerequisites && accountRoute == other.accountRoute;
        }

        @Override public int hashCode()
        { return Objects.hash(questId, routeKey, routeVersion, questState, prerequisites, accountRoute); }
    }

    private final QuestFateOffers offers;
    private final List<Observation> observations;

    public QuestFateEligibility(QuestFateOffers offers, List<Observation> observations)
    {
        this.offers = Objects.requireNonNull(offers, "offers");
        this.observations = List.copyOf(observations);
        if (this.observations.size() != offers.questIds().size())
        { throw new IllegalArgumentException("Incomplete quest eligibility snapshot"); }
        for (int i = 0; i < this.observations.size(); i++)
        {
            if (!this.observations.get(i).questId().equals(offers.questIds().get(i)))
            { throw new IllegalArgumentException("Quest eligibility order/identity mismatch"); }
        }
    }

    public QuestFateOffers offers() { return offers; }
    public List<Observation> observations() { return observations; }

    /** Checks supplied observations only; cannot prove freshness or catalog provenance. */
    public void requireQualified(QuestFateOffers expectedOffers)
    {
        if (!offers.equals(Objects.requireNonNull(expectedOffers, "expectedOffers")))
        { throw new IllegalArgumentException("Foreign Questing offer set"); }
        for (Observation observation : observations)
        {
            if (observation.questState != Status.QUALIFIED
                || observation.prerequisites != Status.QUALIFIED
                || observation.accountRoute != Status.QUALIFIED)
            { throw new IllegalStateException("Unqualified quest: " + observation.questId); }
        }
    }

    private static String key(String value)
    {
        Objects.requireNonNull(value, "quest/route key/version");
        if (value.isBlank() || !value.equals(value.strip()))
        { throw new IllegalArgumentException("Noncanonical quest/route key/version"); }
        return value;
    }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof QuestFateEligibility)) { return false; }
        QuestFateEligibility other = (QuestFateEligibility) value;
        return offers.equals(other.offers) && observations.equals(other.observations);
    }

    @Override public int hashCode() { return Objects.hash(offers, observations); }
}

package com.wheelbound.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Supplied complete Questing pool for precommit Master readiness (WB-D111/120/139).
 * Reviewed catalog membership, capacity ownership and live observations remain caller
 * responsibilities. Never use an offering subset as a complete catalog or apply
 * this classification to an already committed assignment. No retry is authorized.
 */
public final class QuestFatePoolEvidence
{
    private final List<QuestFateEligibility.Observation> observations;
    private final MasterSelectionReadiness.Classification classification;
    private final MasterOrdinaryPoolEvidence masterEvidence;
    private final QuestFateOffers.Capacity capacity;
    private final String catalogVersion;

    public QuestFatePoolEvidence(RunIdentity identity, long revision, String snapshotVersion,
        String catalogVersion, String questingActivity, QuestFateOffers.Capacity capacity,
        Set<String> requiredQuestIds, List<QuestFateEligibility.Observation> observations)
    {
        Objects.requireNonNull(capacity, "capacity");
        Set<String> required = Set.copyOf(requiredQuestIds);
        if (required.isEmpty())
        { throw new IllegalArgumentException("Missing complete quest catalog"); }
        this.observations = List.copyOf(observations);
        Set<String> observed = new HashSet<>();
        boolean unknown = false;
        boolean unsupported = false;
        int qualified = 0;
        for (var observation : this.observations)
        {
            if (!observed.add(observation.questId()))
            { throw new IllegalArgumentException("Repeated quest pool observation"); }
            boolean eligible = true;
            for (var status : List.of(observation.questState(), observation.prerequisites(),
                observation.accountRoute()))
            {
                unknown |= status == QuestFateEligibility.Status.UNKNOWN;
                unsupported |= status == QuestFateEligibility.Status.UNSUPPORTED;
                eligible &= status == QuestFateEligibility.Status.QUALIFIED;
            }
            if (eligible) { qualified++; }
        }
        if (!observed.equals(required))
        { throw new IllegalArgumentException("Missing or foreign quest pool observations"); }
        var availability = unsupported ? MasterSelectionReadiness.Availability.UNSUPPORTED
            : unknown ? MasterSelectionReadiness.Availability.UNKNOWN
            : qualified == 0 ? MasterSelectionReadiness.Availability.VERIFIED_EMPTY
            : qualified < capacity.count() ? MasterSelectionReadiness.Availability.UNKNOWN
            : MasterSelectionReadiness.Availability.ASSIGNABLE;
        classification = new MasterSelectionReadiness.Classification(availability,
            "Complete supplied quest pool: " + qualified + " qualified; capacity " + capacity.count()
                + "; " + availability.name());
        // A nonempty pool below capacity is unresolved reduced-choice gameplay,
        // never proof of exhaustion and never permission to manufacture offers.
        masterEvidence = new MasterOrdinaryPoolEvidence(identity, revision, snapshotVersion,
            catalogVersion, questingActivity, Set.of(questingActivity),
            Map.of(questingActivity, classification));
        this.capacity = capacity;
        this.catalogVersion = catalogVersion;
    }

    /**
     * Bind already resolved choices to this supplied precommit pool. Preserves the
     * original observations in offer order; never draws, refreshes or authenticates
     * an offer. Must not be used to revalidate an already committed assignment.
     */
    public QuestFateEligibility bindSuppliedOffers(RunIdentity expectedIdentity,
        long expectedRevision, String expectedSnapshotVersion, String expectedCatalogVersion,
        ActiveWheel.Slice slice, QuestFateOffers offers)
    {
        masterEvidence.classifyFor(expectedIdentity, expectedRevision,
            expectedSnapshotVersion, expectedCatalogVersion, slice);
        Objects.requireNonNull(offers, "offers");
        if (offers.capacity() != capacity || !offers.catalogVersion().equals(catalogVersion))
        { throw new IllegalArgumentException("Foreign Questing offer capacity/catalog"); }
        if (classification.availability() != MasterSelectionReadiness.Availability.ASSIGNABLE)
        { throw new IllegalStateException("Questing pool is not assignable"); }
        Map<String, QuestFateEligibility.Observation> indexed = new java.util.HashMap<>();
        for (var observation : observations) { indexed.put(observation.questId(), observation); }
        var ordered = new java.util.ArrayList<QuestFateEligibility.Observation>();
        for (String quest : offers.questIds())
        {
            var observation = indexed.get(quest);
            if (observation == null)
            { throw new IllegalArgumentException("Offered quest outside complete pool"); }
            ordered.add(observation);
        }
        var eligibility = new QuestFateEligibility(offers, ordered);
        eligibility.requireQualified(offers);
        return eligibility;
    }

    /**
     * Check every active copy of the supplied Questing activity against a frozen
     * precommit readiness declaration. This checks structural agreement only;
     * neither matching labels nor classifications authenticate live evidence.
     * Does not reclassify a pending selection or authorize its advancement.
     */
    public void requireSuppliedReadiness(RunIdentity expectedIdentity, long expectedRevision,
        String expectedSnapshotVersion, String expectedCatalogVersion,
        String questingActivity, MasterSelectionReadiness readiness)
    {
        Objects.requireNonNull(readiness, "readiness");
        Objects.requireNonNull(questingActivity, "questingActivity");
        boolean found = false;
        for (var slice : readiness.wheel().slices())
        {
            if (slice.kind() != ActiveWheel.Kind.ORDINARY
                || !questingActivity.equals(slice.activity())) { continue; }
            found = true;
            var expected = masterEvidence.classifyFor(expectedIdentity, expectedRevision,
                expectedSnapshotVersion, expectedCatalogVersion, slice);
            var frozen = readiness.classifications().get(slice.id());
            if (frozen.availability() != expected.availability()
                || !frozen.reason().equals(expected.reason()))
            { throw new IllegalArgumentException("Changed supplied Questing readiness"); }
        }
        if (!found) { throw new IllegalArgumentException("Missing active Questing slice"); }
    }

    public List<QuestFateEligibility.Observation> observations() { return observations; }
    public MasterSelectionReadiness.Classification classification() { return classification; }
    public MasterOrdinaryPoolEvidence masterEvidence() { return masterEvidence; }
}

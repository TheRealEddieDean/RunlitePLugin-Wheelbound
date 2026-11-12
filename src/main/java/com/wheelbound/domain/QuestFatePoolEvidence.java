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
    }

    public List<QuestFateEligibility.Observation> observations() { return observations; }
    public MasterSelectionReadiness.Classification classification() { return classification; }
    public MasterOrdinaryPoolEvidence masterEvidence() { return masterEvidence; }
}

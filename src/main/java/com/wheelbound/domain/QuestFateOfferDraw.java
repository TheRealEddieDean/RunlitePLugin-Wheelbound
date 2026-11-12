package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Replay of supplied draws without replacement, not an approved weighting policy.
 * Tickets and initial ordered weights remain caller declarations. No RNG, trusted
 * source admission, persistence or permission to redraw an existing offer is implied.
 */
public final class QuestFateOfferDraw
{
    private final List<DrawCommitment> draws;
    private final QuestFateEligibility eligibility;

    public QuestFateOfferDraw(QuestFatePoolEvidence evidence, RunIdentity identity,
        long revision, String snapshotVersion, String catalogVersion, ActiveWheel.Slice slice,
        List<DrawCommitment> draws)
    {
        Objects.requireNonNull(evidence, "evidence");
        evidence.masterEvidence().classifyFor(identity, revision, snapshotVersion, catalogVersion, slice);
        if (evidence.classification().availability() != MasterSelectionReadiness.Availability.ASSIGNABLE)
        { throw new IllegalStateException("Questing pool is not assignable"); }
        this.draws = List.copyOf(draws);
        if (this.draws.size() != evidence.capacity().count())
        { throw new IllegalArgumentException("Incomplete Questing draw transcript"); }
        var qualified = new HashSet<String>();
        for (var observation : evidence.observations())
        {
            if (observation.questState() == QuestFateEligibility.Status.QUALIFIED
                && observation.prerequisites() == QuestFateEligibility.Status.QUALIFIED
                && observation.accountRoute() == QuestFateEligibility.Status.QUALIFIED)
            { qualified.add(observation.questId()); }
        }
        WeightedPool remaining = this.draws.get(0).pool();
        var supplied = new HashSet<String>();
        for (var entry : remaining.entries()) { supplied.add(entry.id()); }
        if (!remaining.version().equals(catalogVersion) || !supplied.equals(qualified))
        { throw new IllegalArgumentException("Draw pool differs from complete qualified Questing pool"); }
        var selected = new ArrayList<String>();
        for (int i = 0; i < this.draws.size(); i++)
        {
            var draw = this.draws.get(i);
            if (!remaining.equals(draw.pool()))
            { throw new IllegalArgumentException("Changed Questing draw pool/order/weights"); }
            selected.add(draw.selectedId());
            if (i + 1 < this.draws.size())
            {
                var entries = new ArrayList<>(remaining.entries());
                entries.removeIf(entry -> entry.id().equals(draw.selectedId()));
                remaining = new WeightedPool(catalogVersion, entries);
            }
        }
        var offers = new QuestFateOffers(evidence.capacity(), catalogVersion, selected);
        eligibility = evidence.bindSuppliedOffers(identity, revision, snapshotVersion, catalogVersion, slice, offers);
    }

    public List<DrawCommitment> draws() { return draws; }
    public QuestFateEligibility eligibility() { return eligibility; }
}

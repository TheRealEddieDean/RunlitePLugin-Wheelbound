package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import static com.wheelbound.domain.QuestFateEligibility.Status.*;

/** Executable replay checks; synthetic weights do not establish catalog policy. */
public final class QuestFateOfferDrawChecks
{
    private static final RunIdentity ID = new RunIdentity("fixture:quest-draw", UUID.randomUUID());
    private static final ActiveWheel.Slice SLICE = ActiveWheel.Slice.ordinary(
        UUID.randomUUID(), "questing", ActiveWheel.Enhancement.BASE);

    public static void main(String[] args)
    {
        for (var capacity : QuestFateOffers.Capacity.values())
        {
            var pool = evidence(capacity, QUALIFIED);
            var transcript = transcript(capacity.count());
            var result = replay(pool, transcript);
            check(result.eligibility().offers().questIds().equals(List.of("e", "d", "c", "b", "a")
                .subList(0, capacity.count())));
            check(result.eligibility().equals(replay(pool, transcript).eligibility()));
            result.eligibility().requireQualified(result.eligibility().offers());
            check(result.eligibility().observations().get(0).routeKey().equals("route-e"));
            transcript.clear();
            check(result.draws().size() == capacity.count());
            try { result.draws().clear(); throw new AssertionError("Mutable draws"); }
            catch (UnsupportedOperationException expected) { }
            rejects(() -> replay(pool, transcript(capacity.count() - 1)));
            rejects(() -> replay(pool, transcript(capacity.count() + 1)));
            rejects(() -> new QuestFateOfferDraw(pool, ID, 8, "snapshot", "catalog", SLICE,
                transcript(capacity.count())));
            rejects(() -> new QuestFateOfferDraw(pool, new RunIdentity("fixture:foreign", UUID.randomUUID()),
                7, "snapshot", "catalog", SLICE, transcript(capacity.count())));
            rejects(() -> new QuestFateOfferDraw(pool, ID, 7, "foreign", "catalog", SLICE,
                transcript(capacity.count())));
            rejects(() -> new QuestFateOfferDraw(pool, ID, 7, "snapshot", "foreign", SLICE,
                transcript(capacity.count())));
            rejects(() -> new QuestFateOfferDraw(pool, ID, 7, "snapshot", "catalog",
                ActiveWheel.Slice.ordinary(UUID.randomUUID(), "bossing", ActiveWheel.Enhancement.BASE),
                transcript(capacity.count())));
        }
        var pool = evidence(QuestFateOffers.Capacity.THREE, QUALIFIED);
        var zeroTickets = new ArrayList<DrawCommitment>();
        var remaining = new ArrayList<>(transcript(3).get(0).pool().entries());
        for (int i = 0; i < 3; i++)
        {
            zeroTickets.add(new DrawCommitment(new WeightedPool("catalog", remaining), 0));
            remaining.remove(0);
        }
        check(replay(pool, zeroTickets).eligibility().offers().questIds().equals(List.of("a", "b", "c")));
        var excludingE = new ArrayList<DrawCommitment>();
        remaining = new ArrayList<>(transcript(3).get(0).pool().entries().subList(0, 4));
        for (int i = 0; i < 3; i++)
        {
            excludingE.add(new DrawCommitment(new WeightedPool("catalog", remaining), 0));
            remaining.remove(0);
        }
        check(replay(evidence(QuestFateOffers.Capacity.THREE, INELIGIBLE), excludingE)
            .eligibility().offers().questIds().equals(List.of("a", "b", "c")));
        // Valid tickets at both boundaries replay differently without modifying the source.
        var first = transcript(3);
        var entries = first.get(0).pool().entries();
        first.set(0, new DrawCommitment(first.get(0).pool(), 0));
        rejects(() -> replay(pool, first)); // Later pools still removed e, not a.
        for (int mutation = 0; mutation < 5; mutation++)
        {
            var changed = transcript(3);
            var next = new ArrayList<>(changed.get(1).pool().entries());
            String version = "catalog";
            if (mutation == 0) { next.remove(0); }
            if (mutation == 1) { next.add(new WeightedPool.Entry("e", 5)); }
            if (mutation == 2) { next.set(0, new WeightedPool.Entry("a", 99)); }
            if (mutation == 3) { java.util.Collections.reverse(next); }
            if (mutation == 4) { version = "foreign"; }
            changed.set(1, new DrawCommitment(new WeightedPool(version, next), 0));
            rejects(() -> replay(pool, changed));
        }
        for (var replacement : List.of(
            new WeightedPool("catalog", entries.subList(0, 4)),
            new WeightedPool("foreign", entries),
            new WeightedPool("catalog", List.of(new WeightedPool.Entry("foreign", 1)))))
        {
            var changed = transcript(3);
            changed.set(0, new DrawCommitment(replacement, 0));
            rejects(() -> replay(pool, changed));
        }
        // Excluded quests must not be included even if enough other quests qualify.
        rejects(() -> replay(evidence(QuestFateOffers.Capacity.THREE, INELIGIBLE), transcript(3)));
        for (var status : List.of(UNKNOWN, UNSUPPORTED))
        { rejects(() -> replay(evidence(QuestFateOffers.Capacity.THREE, status), transcript(3))); }
    }

    private static QuestFatePoolEvidence evidence(QuestFateOffers.Capacity capacity,
        QuestFateEligibility.Status lastStatus)
    {
        var observations = new ArrayList<QuestFateEligibility.Observation>();
        for (String id : List.of("a", "b", "c", "d", "e"))
        { observations.add(new QuestFateEligibility.Observation(id, "route-" + id, "v1",
            id.equals("e") ? lastStatus : QUALIFIED, QUALIFIED, QUALIFIED)); }
        return new QuestFatePoolEvidence(ID, 7, "snapshot", "catalog", "questing", capacity,
            Set.of("a", "b", "c", "d", "e"), observations);
    }

    private static ArrayList<DrawCommitment> transcript(int count)
    {
        var entries = new ArrayList<WeightedPool.Entry>();
        int weight = 1;
        for (String id : List.of("a", "b", "c", "d", "e", "extra"))
        { if (!id.equals("extra") || count > 5) { entries.add(new WeightedPool.Entry(id, weight++)); } }
        var draws = new ArrayList<DrawCommitment>();
        for (int i = 0; i < count; i++)
        {
            var pool = new WeightedPool("catalog", entries);
            draws.add(new DrawCommitment(pool, pool.totalWeight() - 1));
            entries.remove(entries.size() - 1);
        }
        return draws;
    }

    private static QuestFateOfferDraw replay(QuestFatePoolEvidence evidence, List<DrawCommitment> draws)
    { return new QuestFateOfferDraw(evidence, ID, 7, "snapshot", "catalog", SLICE, draws); }
    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); throw new AssertionError("Expected refusal"); }
        catch (IllegalArgumentException | IllegalStateException | NullPointerException expected) { }
    }
}

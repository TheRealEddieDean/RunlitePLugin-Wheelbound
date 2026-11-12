package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import static com.wheelbound.domain.QuestFateEligibility.Status.*;
import static com.wheelbound.domain.MasterSelectionReadiness.Availability.ASSIGNABLE;
import static com.wheelbound.domain.MasterSelectionReadiness.Availability.VERIFIED_EMPTY;

/** Supplied complete-pool checks; no reviewed catalog or client authority claimed. */
public final class QuestFatePoolEvidenceChecks
{
    private static final RunIdentity ID = new RunIdentity("fixture:quest-pool", UUID.randomUUID());
    private static final Set<String> QUESTS = Set.of("a", "b", "c", "d", "e");

    public static void main(String[] args)
    {
        suppliedOfferBinding();
        suppliedReadinessBinding();
        for (var capacity : QuestFateOffers.Capacity.values())
        {
            for (int count = 0; count <= 5; count++)
            {
                var observations = new ArrayList<QuestFateEligibility.Observation>();
                int index = 0;
                for (String quest : List.of("a", "b", "c", "d", "e"))
                { observations.add(observation(quest, index++ < count ? QUALIFIED : INELIGIBLE, QUALIFIED, QUALIFIED)); }
                var pool = pool(capacity, observations);
                var expected = count == 0 ? VERIFIED_EMPTY : count < capacity.count() ? MasterSelectionReadiness.Availability.UNKNOWN : ASSIGNABLE;
                check(pool.classification().availability() == expected);
                var slice = ActiveWheel.Slice.ordinary(UUID.randomUUID(), "questing", ActiveWheel.Enhancement.BASE);
                check(pool.masterEvidence().classifyFor(ID, 4, "snapshot", "catalog", slice).availability() == expected);
                rejects(() -> pool.masterEvidence().classifyFor(ID, 5, "snapshot", "catalog", slice));
                rejects(() -> pool.masterEvidence().classifyFor(ID, 4, "foreign", "catalog", slice));
                rejects(() -> pool.masterEvidence().classifyFor(ID, 4, "snapshot", "foreign", slice));
                rejects(() -> pool.masterEvidence().classifyFor(
                    new RunIdentity("fixture:foreign", UUID.randomUUID()), 4, "snapshot", "catalog", slice));
                rejects(() -> pool.masterEvidence().classifyFor(ID, 4, "snapshot", "catalog",
                    ActiveWheel.Slice.ordinary(UUID.randomUUID(), "bossing", ActiveWheel.Enhancement.BASE)));
                rejects(() -> pool.masterEvidence().classifyFor(ID, 4, "snapshot", "catalog",
                    ActiveWheel.Slice.special(UUID.randomUUID(), ActiveWheel.Kind.TAINT, 4)));
                observations.clear();
                check(pool.observations().size() == 5);
                try { pool.observations().clear(); throw new AssertionError("Mutable observations"); }
                catch (UnsupportedOperationException expectedFailure) { }
            }
        }
        for (var status : QuestFateEligibility.Status.values())
        {
            for (int field = 0; field < 3; field++)
            {
                var observations = allQualified();
                observations.set(0, observation("a", field == 0 ? status : QUALIFIED,
                    field == 1 ? status : QUALIFIED, field == 2 ? status : QUALIFIED));
                var expected = status == UNSUPPORTED ? MasterSelectionReadiness.Availability.UNSUPPORTED
                    : status == UNKNOWN ? MasterSelectionReadiness.Availability.UNKNOWN
                    : status == INELIGIBLE ? MasterSelectionReadiness.Availability.UNKNOWN : ASSIGNABLE;
                check(pool(QuestFateOffers.Capacity.FIVE, observations).classification().availability() == expected);
            }
        }
        var mixed = allQualified();
        mixed.set(0, observation("a", INELIGIBLE, UNKNOWN, UNSUPPORTED));
        check(pool(QuestFateOffers.Capacity.THREE, mixed).classification().availability()
            == MasterSelectionReadiness.Availability.UNSUPPORTED);
        // Known access/prerequisite exclusions can prove empty; missing evidence cannot.
        for (int field = 0; field < 3; field++)
        {
            var excluded = new ArrayList<QuestFateEligibility.Observation>();
            for (String quest : QUESTS)
            { excluded.add(observation(quest, field == 0 ? INELIGIBLE : QUALIFIED,
                field == 1 ? INELIGIBLE : QUALIFIED, field == 2 ? INELIGIBLE : QUALIFIED)); }
            check(pool(QuestFateOffers.Capacity.THREE, excluded).classification().availability() == VERIFIED_EMPTY);
        }
        rejects(() -> pool(QuestFateOffers.Capacity.THREE, allQualified().subList(0, 4)));
        var duplicate = allQualified(); duplicate.set(4, duplicate.get(0));
        rejects(() -> pool(QuestFateOffers.Capacity.THREE, duplicate));
        var foreign = allQualified(); foreign.set(4, observation("foreign", QUALIFIED, QUALIFIED, QUALIFIED));
        rejects(() -> pool(QuestFateOffers.Capacity.THREE, foreign));
        rejects(() -> new QuestFatePoolEvidence(ID, 4, "snapshot", "catalog", "questing",
            QuestFateOffers.Capacity.THREE, Set.of(), List.of()));
    }

    private static void suppliedReadinessBinding()
    {
        var slices = new ArrayList<ActiveWheel.Slice>();
        for (int i = 0; i < 5; i++)
        { slices.add(ActiveWheel.Slice.ordinary(UUID.randomUUID(), i < 2 ? "questing" : "other-" + i,
            ActiveWheel.Enhancement.values()[i])); }
        var wheel = new ActiveWheel(slices);
        for (var status : QuestFateEligibility.Status.values())
        {
            var observations = new ArrayList<QuestFateEligibility.Observation>();
            for (String quest : QUESTS) { observations.add(observation(quest, status, QUALIFIED, QUALIFIED)); }
            var evidence = pool(QuestFateOffers.Capacity.THREE, observations);
            var classifications = new java.util.HashMap<UUID, MasterSelectionReadiness.Classification>();
            for (var slice : slices)
            {
                classifications.put(slice.id(), slice.activity().equals("questing")
                    ? evidence.masterEvidence().classifyFor(ID, 4, "snapshot", "catalog", slice)
                    : new MasterSelectionReadiness.Classification(ASSIGNABLE, "Other supplied pool"));
            }
            var readiness = new MasterSelectionReadiness(wheel, classifications);
            evidence.requireSuppliedReadiness(ID, 4, "snapshot", "catalog", "questing", readiness);
            check(readiness.wheel() == wheel);
            rejects(() -> evidence.requireSuppliedReadiness(ID, 5, "snapshot", "catalog", "questing", readiness));
            rejects(() -> evidence.requireSuppliedReadiness(ID, 4, "stale", "catalog", "questing", readiness));
            rejects(() -> evidence.requireSuppliedReadiness(ID, 4, "snapshot", "foreign", "questing", readiness));
            rejects(() -> evidence.requireSuppliedReadiness(new RunIdentity("fixture:foreign", UUID.randomUUID()),
                4, "snapshot", "catalog", "questing", readiness));
            rejects(() -> evidence.requireSuppliedReadiness(ID, 4, "snapshot", "catalog", "absent", readiness));
            rejects(() -> evidence.requireSuppliedReadiness(ID, 4, "snapshot", "catalog", "other-2", readiness));
            // A changed duplicate must be checked even when the first copy agrees.
            for (var availability : MasterSelectionReadiness.Availability.values())
            {
                classifications.put(slices.get(1).id(),
                    new MasterSelectionReadiness.Classification(availability, "Changed reason"));
                var changed = new MasterSelectionReadiness(wheel, classifications);
                rejects(() -> evidence.requireSuppliedReadiness(ID, 4, "snapshot", "catalog", "questing", changed));
            }
            var original = readiness.classifications().get(slices.get(1).id());
            var different = original.availability() == ASSIGNABLE ? VERIFIED_EMPTY : ASSIGNABLE;
            classifications.put(slices.get(1).id(),
                new MasterSelectionReadiness.Classification(different, original.reason()));
            var changed = new MasterSelectionReadiness(wheel, classifications);
            rejects(() -> evidence.requireSuppliedReadiness(ID, 4, "snapshot", "catalog", "questing", changed));
        }
    }

    private static void suppliedOfferBinding()
    {
        var slice = ActiveWheel.Slice.ordinary(UUID.randomUUID(), "questing", ActiveWheel.Enhancement.BASE);
        for (var capacity : QuestFateOffers.Capacity.values())
        {
            var evidence = pool(capacity, allQualified());
            var order = List.of("e", "c", "a", "d", "b").subList(0, capacity.count());
            var offers = new QuestFateOffers(capacity, "catalog", order);
            var bound = evidence.bindSuppliedOffers(ID, 4, "snapshot", "catalog", slice, offers);
            bound.requireQualified(offers);
            for (int i = 0; i < order.size(); i++)
            {
                String quest = order.get(i);
                check(bound.observations().get(i) == evidence.observations().stream()
                    .filter(o -> o.questId().equals(quest)).findFirst().orElseThrow());
            }
            rejects(() -> evidence.bindSuppliedOffers(ID, 5, "snapshot", "catalog", slice, offers));
            rejects(() -> evidence.bindSuppliedOffers(ID, 4, "other", "catalog", slice, offers));
            rejects(() -> evidence.bindSuppliedOffers(ID, 4, "snapshot", "other", slice, offers));
            rejects(() -> evidence.bindSuppliedOffers(new RunIdentity("other", UUID.randomUUID()),
                4, "snapshot", "catalog", slice, offers));
            rejects(() -> evidence.bindSuppliedOffers(ID, 4, "snapshot", "catalog",
                ActiveWheel.Slice.special(UUID.randomUUID(), ActiveWheel.Kind.TAINT, 4), offers));
            rejects(() -> evidence.bindSuppliedOffers(ID, 4, "snapshot", "catalog", slice,
                new QuestFateOffers(capacity, "foreign", order)));
        }
        var offers = new QuestFateOffers(QuestFateOffers.Capacity.THREE, "catalog", List.of("a", "b", "c"));
        var evidence = pool(QuestFateOffers.Capacity.THREE, allQualified());
        rejects(() -> evidence.bindSuppliedOffers(ID, 4, "snapshot", "catalog", slice,
            new QuestFateOffers(QuestFateOffers.Capacity.FOUR, "catalog", List.of("a", "b", "c", "d"))));
        rejects(() -> evidence.bindSuppliedOffers(ID, 4, "snapshot", "catalog", slice,
            new QuestFateOffers(QuestFateOffers.Capacity.THREE, "catalog", List.of("a", "b", "foreign"))));
        for (var status : QuestFateEligibility.Status.values())
        {
            if (status == QUALIFIED) { continue; }
            var observations = allQualified();
            observations.set(0, observation("a", status, QUALIFIED, QUALIFIED));
            var blocked = pool(QuestFateOffers.Capacity.THREE, observations);
            refusesBinding(() -> blocked.bindSuppliedOffers(ID, 4, "snapshot", "catalog", slice, offers));
            // A different qualified subset is valid only when the whole pool is known.
            var subset = new QuestFateOffers(QuestFateOffers.Capacity.THREE, "catalog", List.of("e", "d", "c"));
            if (status == INELIGIBLE)
            { blocked.bindSuppliedOffers(ID, 4, "snapshot", "catalog", slice, subset).requireQualified(subset); }
            else
            { refusesBinding(() -> blocked.bindSuppliedOffers(ID, 4, "snapshot", "catalog", slice, subset)); }
        }
    }

    private static void refusesBinding(Runnable action)
    {
        try { action.run(); throw new AssertionError("Expected binding refusal"); }
        catch (IllegalStateException expected) { }
    }

    private static ArrayList<QuestFateEligibility.Observation> allQualified()
    {
        var result = new ArrayList<QuestFateEligibility.Observation>();
        for (String quest : List.of("a", "b", "c", "d", "e"))
        { result.add(observation(quest, QUALIFIED, QUALIFIED, QUALIFIED)); }
        return result;
    }

    private static QuestFateEligibility.Observation observation(String quest,
        QuestFateEligibility.Status state, QuestFateEligibility.Status prerequisites,
        QuestFateEligibility.Status route)
    { return new QuestFateEligibility.Observation(quest, "route", "v1", state, prerequisites, route); }

    private static QuestFatePoolEvidence pool(QuestFateOffers.Capacity capacity,
        List<QuestFateEligibility.Observation> observations)
    { return new QuestFatePoolEvidence(ID, 4, "snapshot", "catalog", "questing", capacity, QUESTS, observations); }

    private static void check(boolean condition) { if (!condition) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); throw new AssertionError("Expected refusal"); }
        catch (IllegalArgumentException | NullPointerException expected) { }
    }
}

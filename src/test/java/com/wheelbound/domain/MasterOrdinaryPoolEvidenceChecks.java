package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import static com.wheelbound.domain.MasterSelectionReadiness.*;

/** Offline completeness/context checks, never proof of live detector support. */
public final class MasterOrdinaryPoolEvidenceChecks
{
    private static final RunIdentity ID = new RunIdentity("offline-account", UUID.randomUUID());
    private static final Set<String> REQUIRED = Set.of("normal", "challenge");

    private static Classification result(Availability availability)
    { return new Classification(availability, "Offline complete-family fixture"); }

    private static MasterOrdinaryPoolEvidence evidence(String activity, Map<String, Classification> families)
    { return new MasterOrdinaryPoolEvidence(ID, 7, "snapshot", "catalog", activity, REQUIRED, families); }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Complete-pool policy violated"); } }

    private static void rejects(Class<? extends Throwable> type, Runnable action)
    {
        try { action.run(); }
        catch (Throwable failure)
        {
            if (type.isInstance(failure)) { return; }
            throw new AssertionError(failure);
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }

    public static void main(String[] args)
    {
        ActiveWheel.Slice boss = ActiveWheel.Slice.ordinary(UUID.randomUUID(), "bossing", ActiveWheel.Enhancement.BASE);
        // Exhaust all availability pairs: incomplete evidence cannot establish emptiness/readiness.
        for (Availability normal : Availability.values())
        {
            for (Availability challenge : Availability.values())
            {
                MasterOrdinaryPoolEvidence pool = evidence("bossing", Map.of(
                    "normal", result(normal), "challenge", result(challenge)));
                Availability expected = normal == Availability.UNSUPPORTED || challenge == Availability.UNSUPPORTED
                    ? Availability.UNSUPPORTED : normal == Availability.UNKNOWN || challenge == Availability.UNKNOWN
                    ? Availability.UNKNOWN : normal == Availability.ASSIGNABLE || challenge == Availability.ASSIGNABLE
                    ? Availability.ASSIGNABLE : Availability.VERIFIED_EMPTY;
                check(pool.classifyFor(ID, 7, "snapshot", "catalog", boss).availability() == expected);
            }
        }
        Map<String, Classification> source = new HashMap<>(Map.of(
            "normal", result(Availability.ASSIGNABLE), "challenge", result(Availability.VERIFIED_EMPTY)));
        MasterOrdinaryPoolEvidence pool = evidence("bossing", source);
        source.clear();
        check(pool.families().size() == 2);
        rejects(UnsupportedOperationException.class, () -> pool.families().clear());
        rejects(IllegalArgumentException.class, () -> evidence("bossing", Map.of("normal", result(Availability.VERIFIED_EMPTY))));
        rejects(IllegalArgumentException.class, () -> evidence("bossing", Map.of(
            "normal", result(Availability.VERIFIED_EMPTY), "foreign", result(Availability.VERIFIED_EMPTY))));
        rejects(IllegalArgumentException.class, () -> new MasterOrdinaryPoolEvidence(ID, 7, "s", "c", "a", Set.of(), Map.of()));
        rejects(IllegalArgumentException.class, () -> new MasterOrdinaryPoolEvidence(ID, -1, "s", "c", "a", REQUIRED, pool.families()));
        rejects(IllegalArgumentException.class, () -> evidence(" bossing", pool.families()));
        rejects(IllegalArgumentException.class, () -> new MasterOrdinaryPoolEvidence(ID, 7, "s", "c", "a",
            Set.of(" normal"), Map.of(" normal", result(Availability.VERIFIED_EMPTY))));
        rejects(NullPointerException.class, () -> evidence("bossing", null));
        rejects(IllegalArgumentException.class, () -> pool.classifyFor(new RunIdentity("other", ID.runId()), 7, "snapshot", "catalog", boss));
        rejects(IllegalArgumentException.class, () -> pool.classifyFor(new RunIdentity(ID.accountKey(), UUID.randomUUID()), 7, "snapshot", "catalog", boss));
        rejects(IllegalArgumentException.class, () -> pool.classifyFor(ID, 8, "snapshot", "catalog", boss));
        rejects(IllegalArgumentException.class, () -> pool.classifyFor(ID, 7, "changed", "catalog", boss));
        rejects(IllegalArgumentException.class, () -> pool.classifyFor(ID, 7, "snapshot", "changed", boss));
        rejects(IllegalArgumentException.class, () -> pool.classifyFor(ID, 7, "snapshot", "catalog",
            ActiveWheel.Slice.ordinary(UUID.randomUUID(), "questing", ActiveWheel.Enhancement.BASE)));
        for (ActiveWheel.Kind special : new ActiveWheel.Kind[] {ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE})
        {
            rejects(IllegalArgumentException.class, () -> pool.classifyFor(ID, 7, "snapshot", "catalog",
                ActiveWheel.Slice.special(UUID.randomUUID(), special, 4)));
        }

        // Use the reduction in the existing full-wheel policy without dropping empty weights.
        ArrayList<ActiveWheel.Slice> slices = new ArrayList<>();
        Map<UUID, Classification> classifications = new HashMap<>();
        for (int i = 0; i < 5; i++)
        {
            String activity = "activity-" + i % 3;
            ActiveWheel.Slice slice = ActiveWheel.Slice.ordinary(UUID.randomUUID(), activity, ActiveWheel.Enhancement.BASE);
            slices.add(slice);
            classifications.put(slice.id(), evidence(activity, Map.of(
                "normal", result(i == 0 ? Availability.ASSIGNABLE : Availability.VERIFIED_EMPTY),
                "challenge", result(Availability.VERIFIED_EMPTY)))
                .classifyFor(ID, 7, "snapshot", "catalog", slice));
        }
        MasterSelectionReadiness readiness = new MasterSelectionReadiness(new ActiveWheel(slices), classifications);
        check(readiness.status() == Status.READY && readiness.wheel().totalWeightUnits() == 20);
        check(readiness.classifyLanding(slices.get(1).id()) == Landing.EMPTY_ORDINARY_RETRY);
        check(readiness.classifyLanding(slices.get(0).id()) == Landing.ORDINARY_ASSIGNMENT);
        ActiveWheel.Slice first = slices.get(0);
        classifications.put(first.id(), evidence(first.activity(), Map.of(
            "normal", result(Availability.VERIFIED_EMPTY), "challenge", result(Availability.VERIFIED_EMPTY)))
            .classifyFor(ID, 7, "snapshot", "catalog", first));
        MasterSelectionReadiness empty = new MasterSelectionReadiness(new ActiveWheel(slices), classifications);
        check(empty.status() == Status.NO_VALID_FATE);
        rejects(IllegalStateException.class, () -> empty.classifyLanding(first.id()));
        classifications.put(first.id(), evidence(first.activity(), Map.of(
            "normal", result(Availability.VERIFIED_EMPTY), "challenge", result(Availability.UNKNOWN)))
            .classifyFor(ID, 7, "snapshot", "catalog", first));
        MasterSelectionReadiness waiting = new MasterSelectionReadiness(new ActiveWheel(slices), classifications);
        check(waiting.status() == Status.WAITING_FOR_EVIDENCE);
        rejects(IllegalStateException.class, () -> waiting.classifyLanding(first.id()));
        System.out.println("MasterOrdinaryPoolEvidenceChecks PASSED");
    }
}

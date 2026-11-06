package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static com.wheelbound.domain.FateKillEvidence.Attribution.*;
import static com.wheelbound.domain.RunLifecycle.State.*;

/** Dependency-free behavioral checks shared with JUnit. */
public final class FateKillEvidenceJournalChecks
{
    private static final RunIdentity RUN = new RunIdentity("account", UUID.randomUUID());
    private static final UUID ASSIGNMENT = UUID.randomUUID();
    private static final UUID SESSION = UUID.randomUUID();

    public static void main(String[] args)
    {
        var active = new RunLifecycle(RUN, 7, ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        var empty = new FateKillEvidenceJournal(RUN, ASSIGNMENT, Collections.emptyList());
        var assigned = event("1", ASSIGNED, 200);
        var first = empty.observe(active, 7, SESSION, assigned);
        var head = first
            .observe(active, 7, SESSION, event("3", UNAUTHORIZED, 30))
            .observe(active, 7, SESSION, event("4", UNKNOWN, 10));
        check(empty.events().isEmpty() && empty.totals().total() == 0);
        check(first.totals().total() == 200 && head.events().size() == 3);
        check(head.totals().assigned() == 200);
        check(head.totals().unauthorized() == 30 && head.totals().unknown() == 10);
        check(head.observe(active, 7, SESSION, event("1", ASSIGNED, 200)) == head);
        check(assigned.equals(event("1", ASSIGNED, 200))
            && assigned.hashCode() == event("1", ASSIGNED, 200).hashCode());
        rejects(IllegalArgumentException.class,
            () -> head.observe(active, 7, SESSION, event("1", UNAUTHORIZED, 200)));
        rejects(IllegalArgumentException.class,
            () -> head.observe(active, 7, SESSION, event("1", ASSIGNED, 201)));

        // Source identity includes session and namespace, never string concatenation.
        UUID nextSession = UUID.randomUUID();
        var next = new FateKillEvidenceJournal.Event(RUN, ASSIGNMENT, nextSession, "kill", "1", ASSIGNED, 5);
        var resumed = head.observe(active, 7, nextSession, next);
        check(resumed.totals().total() == 245 && head.totals().total() == 240);
        var namespaced = new FateKillEvidenceJournal.Event(RUN, ASSIGNMENT, SESSION, "other", "1", UNKNOWN, 2);
        check(head.observe(active, 7, SESSION, namespaced).events().size() == 4);
        rejects(IllegalArgumentException.class, () -> head.observe(active, 7, nextSession, assigned));
        rejects(IllegalArgumentException.class, () -> head.observe(active, 6, SESSION, assigned));
        for (var state : RunLifecycle.State.values())
        {
            if (state == ACTIVE) { continue; }
            var inactive = new RunLifecycle(RUN, 7, state, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
            rejects(IllegalStateException.class, () -> head.observe(inactive, 7, SESSION, assigned));
            rejects(IllegalStateException.class, () -> head.observe(inactive, 7, SESSION, event("new", ASSIGNED, 1)));
        }
        var foreignRun = new RunIdentity("another", RUN.runId());
        var foreignLifecycle = new RunLifecycle(foreignRun, 7, ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        rejects(IllegalArgumentException.class, () -> head.observe(foreignLifecycle, 7, SESSION, assigned));
        var foreign = new FateKillEvidenceJournal.Event(foreignRun, ASSIGNMENT, SESSION, "kill", "1", ASSIGNED, 200);
        var otherAssignment = new FateKillEvidenceJournal.Event(RUN, UUID.randomUUID(), SESSION, "kill", "1", ASSIGNED, 200);
        for (var event : Arrays.asList(foreign, otherAssignment))
        {
            rejects(IllegalArgumentException.class, () -> head.observe(active, 7, SESSION, event));
            rejects(IllegalArgumentException.class,
                () -> new FateKillEvidenceJournal(RUN, ASSIGNMENT, Collections.singletonList(event)));
        }

        var restoredInput = new ArrayList<>(resumed.events());
        var restored = new FateKillEvidenceJournal(RUN, ASSIGNMENT, restoredInput);
        restoredInput.clear();
        check(restored.events().size() == 4 && restored.totals().total() == 245);
        check(restored.observe(active, 7, SESSION, assigned) == restored);
        rejects(UnsupportedOperationException.class, () -> restored.events().clear());
        rejects(IllegalArgumentException.class,
            () -> new FateKillEvidenceJournal(RUN, ASSIGNMENT, Arrays.asList(assigned, assigned)));
        rejects(IllegalArgumentException.class,
            () -> new FateKillEvidenceJournal(RUN, ASSIGNMENT,
                Arrays.asList(assigned, event("1", UNKNOWN, 200))));
        // Different field boundaries must not collide even when joined text matches.
        var boundaryA = new FateKillEvidenceJournal.Event(RUN, ASSIGNMENT, SESSION,
            "a:b", "c", UNKNOWN, 1);
        var boundaryB = new FateKillEvidenceJournal.Event(RUN, ASSIGNMENT, SESSION,
            "a", "b:c", ASSIGNED, 2);
        var boundaries = empty.observe(active, 7, SESSION, boundaryA)
            .observe(active, 7, SESSION, boundaryB);
        check(boundaries.events().size() == 2 && boundaries.totals().unknown() == 1
            && boundaries.totals().assigned() == 2);
        check(new ArrayList<>(restored.events()).equals(new ArrayList<>(resumed.events())));
        var full = empty.observe(active, 7, SESSION, event("max", ASSIGNED, Long.MAX_VALUE));
        check(full.observe(active, 7, SESSION, event("max", ASSIGNED, Long.MAX_VALUE)) == full);
        rejects(ArithmeticException.class, () -> full.observe(active, 7, SESSION, event("overflow", UNKNOWN, 1)));
        check(full.events().size() == 1 && full.totals().total() == Long.MAX_VALUE);
        var zero = empty.observe(active, 7, SESSION, event("zero", UNKNOWN, 0));
        check(zero.events().size() == 1 && zero.totals().total() == 0);
        rejects(IllegalArgumentException.class, () -> zero.observe(active, 7, SESSION, event("zero", UNKNOWN, 1)));
        for (String key : Arrays.asList("", " ", " padded", "padded "))
        {
            rejects(IllegalArgumentException.class,
                () -> new FateKillEvidenceJournal.Event(RUN, ASSIGNMENT, SESSION, key, "1", ASSIGNED, 1));
            rejects(IllegalArgumentException.class, () -> event(key, ASSIGNED, 1));
        }
        rejects(IllegalArgumentException.class, () -> event("bad", ASSIGNED, -1));
        rejects(NullPointerException.class, () -> event("bad", null, 1));
        rejects(NullPointerException.class, () -> event(null, ASSIGNED, 1));
        rejects(NullPointerException.class,
            () -> new FateKillEvidenceJournal.Event(RUN, ASSIGNMENT, SESSION, null, "1", ASSIGNED, 1));
        rejects(NullPointerException.class,
            () -> new FateKillEvidenceJournal.Event(null, ASSIGNMENT, SESSION, "kill", "1", ASSIGNED, 1));
        rejects(NullPointerException.class,
            () -> new FateKillEvidenceJournal.Event(RUN, null, SESSION, "kill", "1", ASSIGNED, 1));
        rejects(NullPointerException.class,
            () -> new FateKillEvidenceJournal.Event(RUN, ASSIGNMENT, null, "kill", "1", ASSIGNED, 1));
        rejects(NullPointerException.class, () -> head.observe(null, 7, SESSION, assigned));
        rejects(NullPointerException.class, () -> head.observe(active, 7, null, assigned));
        rejects(NullPointerException.class, () -> head.observe(active, 7, SESSION, null));
        rejects(NullPointerException.class, () -> new FateKillEvidenceJournal(RUN, ASSIGNMENT, null));
        rejects(NullPointerException.class,
            () -> new FateKillEvidenceJournal(RUN, ASSIGNMENT, Collections.singletonList(null)));
    }

    private static FateKillEvidenceJournal.Event event(String key, FateKillEvidence.Attribution attribution, long delta)
    { return new FateKillEvidenceJournal.Event(RUN, ASSIGNMENT, SESSION, "kill", key, attribution, delta); }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("kill journal invariant failed"); } }

    private static void rejects(Class<? extends Throwable> expected, Runnable action)
    {
        try { action.run(); }
        catch (Throwable failure)
        {
            if (expected.isInstance(failure)) { return; }
            throw new AssertionError("Unexpected failure", failure);
        }
        throw new AssertionError("Expected " + expected.getSimpleName());
    }
}

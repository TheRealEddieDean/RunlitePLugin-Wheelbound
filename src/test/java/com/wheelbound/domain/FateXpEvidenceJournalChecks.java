package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static com.wheelbound.domain.FateXpEvidence.Attribution.*;
import static com.wheelbound.domain.RunLifecycle.State.*;

/** Dependency-free behavioral checks shared with JUnit. */
public final class FateXpEvidenceJournalChecks
{
    private static final RunIdentity RUN = new RunIdentity("account", UUID.randomUUID());
    private static final UUID ASSIGNMENT = UUID.randomUUID();
    private static final UUID SESSION = UUID.randomUUID();

    public static void main(String[] args)
    {
        var active = new RunLifecycle(RUN, 7, ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        var empty = new FateXpEvidenceJournal(RUN, ASSIGNMENT, Collections.emptyList());
        var assigned = event("1", ASSIGNED, 200);
        var first = empty.observe(active, 7, SESSION, assigned);
        var head = first.observe(active, 7, SESSION, event("2", VERIFIED_BY_PRODUCT, 50))
            .observe(active, 7, SESSION, event("3", UNAUTHORIZED, 30))
            .observe(active, 7, SESSION, event("4", UNKNOWN, 10));
        check(empty.events().isEmpty() && empty.totals().total() == 0);
        check(first.totals().total() == 200 && head.events().size() == 4);
        check(head.totals().assigned() == 200 && head.totals().byProduct() == 50);
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
        var next = new FateXpEvidenceJournal.Event(RUN, ASSIGNMENT, nextSession, "xp", "1", ASSIGNED, 5);
        var resumed = head.observe(active, 7, nextSession, next);
        check(resumed.totals().total() == 295 && head.totals().total() == 290);
        var namespaced = new FateXpEvidenceJournal.Event(RUN, ASSIGNMENT, SESSION, "other", "1", UNKNOWN, 2);
        check(head.observe(active, 7, SESSION, namespaced).events().size() == 5);
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
        var foreign = new FateXpEvidenceJournal.Event(foreignRun, ASSIGNMENT, SESSION, "xp", "1", ASSIGNED, 200);
        var otherAssignment = new FateXpEvidenceJournal.Event(RUN, UUID.randomUUID(), SESSION, "xp", "1", ASSIGNED, 200);
        for (var event : Arrays.asList(foreign, otherAssignment))
        {
            rejects(IllegalArgumentException.class, () -> head.observe(active, 7, SESSION, event));
            rejects(IllegalArgumentException.class,
                () -> new FateXpEvidenceJournal(RUN, ASSIGNMENT, Collections.singletonList(event)));
        }

        var restoredInput = new ArrayList<>(resumed.events());
        var restored = new FateXpEvidenceJournal(RUN, ASSIGNMENT, restoredInput);
        restoredInput.clear();
        check(restored.events().size() == 5 && restored.totals().total() == 295);
        check(restored.observe(active, 7, SESSION, assigned) == restored);
        rejects(UnsupportedOperationException.class, () -> restored.events().clear());
        rejects(IllegalArgumentException.class,
            () -> new FateXpEvidenceJournal(RUN, ASSIGNMENT, Arrays.asList(assigned, assigned)));
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
                () -> new FateXpEvidenceJournal.Event(RUN, ASSIGNMENT, SESSION, key, "1", ASSIGNED, 1));
            rejects(IllegalArgumentException.class, () -> event(key, ASSIGNED, 1));
        }
        rejects(IllegalArgumentException.class, () -> event("bad", ASSIGNED, -1));
        rejects(NullPointerException.class, () -> event("bad", null, 1));
        rejects(NullPointerException.class, () -> head.observe(null, 7, SESSION, assigned));
        rejects(NullPointerException.class, () -> head.observe(active, 7, null, assigned));
        rejects(NullPointerException.class, () -> head.observe(active, 7, SESSION, null));
        rejects(NullPointerException.class, () -> new FateXpEvidenceJournal(RUN, ASSIGNMENT, null));
        rejects(NullPointerException.class,
            () -> new FateXpEvidenceJournal(RUN, ASSIGNMENT, Collections.singletonList(null)));
    }

    private static FateXpEvidenceJournal.Event event(String key, FateXpEvidence.Attribution attribution, long delta)
    { return new FateXpEvidenceJournal.Event(RUN, ASSIGNMENT, SESSION, "xp", key, attribution, delta); }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("XP journal invariant failed"); } }

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

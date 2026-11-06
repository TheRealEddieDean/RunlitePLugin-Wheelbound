package com.wheelbound.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/** Dependency-free behavioral checks shared with JUnit. */
public final class FateEvidenceSourceHistoryChecks
{
    private static final RunIdentity RUN = new RunIdentity("account", UUID.randomUUID());
    private static final UUID A = UUID.randomUUID();
    private static final UUID B = UUID.randomUUID();
    private static final UUID SESSION = UUID.randomUUID();

    public static void main(String[] args)
    {
        var xp = xp(RUN, A, SESSION, "client", "1", 10);
        var kill = kill(RUN, A, SESSION, "client", "1", 1);
        FateEvidenceSourceHistory.validate(RUN, List.of(), List.of());
        FateEvidenceSourceHistory.validate(RUN, List.of(xp), List.of(kill));
        // One source can describe independent metrics, even for different assignments.
        FateEvidenceSourceHistory.validate(RUN, List.of(xp), List.of(kill(RUN, B, SESSION, "client", "1", 1)));
        for (long delta : new long[] { 0, 10, 11 })
        {
            rejects(IllegalArgumentException.class, () -> FateEvidenceSourceHistory.validate(RUN,
                List.of(xp, xp(RUN, B, SESSION, "client", "1", delta)), List.of()));
            rejects(IllegalArgumentException.class, () -> FateEvidenceSourceHistory.validate(RUN,
                List.of(), List.of(kill, kill(RUN, B, SESSION, "client", "1", delta))));
        }
        // Session, namespace and key are separate fields, not concatenated strings.
        FateEvidenceSourceHistory.validate(RUN, List.of(xp,
            xp(RUN, B, UUID.randomUUID(), "client", "1", 2)), List.of(kill,
            kill(RUN, B, UUID.randomUUID(), "client", "1", 2)));
        FateEvidenceSourceHistory.validate(RUN, List.of(xp,
            xp(RUN, B, SESSION, "other", "1", 2)), List.of(kill,
            kill(RUN, B, SESSION, "client", "2", 2)));
        FateEvidenceSourceHistory.validate(RUN, List.of(
            xp(RUN, A, SESSION, "a:b", "c", 1), xp(RUN, B, SESSION, "a", "b:c", 1)), List.of(
            kill(RUN, A, SESSION, "a:b", "c", 1), kill(RUN, B, SESSION, "a", "b:c", 1)));
        // Multiple heads are ambiguous even when identical or empty.
        rejects(IllegalArgumentException.class, () -> FateEvidenceSourceHistory.validate(RUN, List.of(xp, xp), List.of()));
        rejects(IllegalArgumentException.class, () -> FateEvidenceSourceHistory.validate(RUN, List.of(), List.of(kill, kill)));
        var emptyXp = new FateXpEvidenceJournal(RUN, A, List.of());
        var emptyKill = new FateKillEvidenceJournal(RUN, A, List.of());
        rejects(IllegalArgumentException.class, () -> FateEvidenceSourceHistory.validate(RUN, List.of(emptyXp, xp), List.of()));
        rejects(IllegalArgumentException.class, () -> FateEvidenceSourceHistory.validate(RUN, List.of(), List.of(emptyKill, kill)));
        for (RunIdentity foreign : List.of(new RunIdentity("other", RUN.runId()),
            new RunIdentity(RUN.accountKey(), UUID.randomUUID())))
        {
            rejects(IllegalArgumentException.class, () -> FateEvidenceSourceHistory.validate(RUN,
                List.of(new FateXpEvidenceJournal(foreign, B, List.of())), List.of()));
            rejects(IllegalArgumentException.class, () -> FateEvidenceSourceHistory.validate(RUN,
                List.of(), List.of(new FateKillEvidenceJournal(foreign, B, List.of()))));
        }
        rejects(NullPointerException.class, () -> FateEvidenceSourceHistory.validate(null, List.of(), List.of()));
        rejects(NullPointerException.class, () -> FateEvidenceSourceHistory.validate(RUN, null, List.of()));
        rejects(NullPointerException.class, () -> FateEvidenceSourceHistory.validate(RUN, List.of(), null));
        rejects(NullPointerException.class, () -> FateEvidenceSourceHistory.validate(RUN, Arrays.asList(xp, null), List.of()));
        rejects(NullPointerException.class, () -> FateEvidenceSourceHistory.validate(RUN, List.of(), Arrays.asList(kill, null)));
        // Validation does not alter retained events, totals, or exact replay behavior.
        FateEvidenceSourceHistory.validate(RUN, Collections.unmodifiableList(List.of(xp)), List.of(kill));
        var active = new RunLifecycle(RUN, 3, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        if (xp.events().size() != 1 || xp.totals().total() != 10 || kill.totals().total() != 1
            || xp.observe(active, 3, SESSION, xp.events().iterator().next()) != xp
            || kill.observe(active, 3, SESSION, kill.events().iterator().next()) != kill)
        { throw new AssertionError("History validation changed journal heads"); }
    }

    private static FateXpEvidenceJournal xp(RunIdentity run, UUID assignment, UUID session,
        String namespace, String key, long delta)
    {
        return new FateXpEvidenceJournal(run, assignment, List.of(new FateXpEvidenceJournal.Event(
            run, assignment, session, namespace, key, FateXpEvidence.Attribution.UNKNOWN, delta)));
    }

    private static FateKillEvidenceJournal kill(RunIdentity run, UUID assignment, UUID session,
        String namespace, String key, long delta)
    {
        return new FateKillEvidenceJournal(run, assignment, List.of(new FateKillEvidenceJournal.Event(
            run, assignment, session, namespace, key, FateKillEvidence.Attribution.UNKNOWN, delta)));
    }

    private static void rejects(Class<? extends Throwable> expected, Runnable action)
    {
        try { action.run(); }
        catch (Throwable failure)
        {
            if (expected.isInstance(failure)) { return; }
            throw new AssertionError("Wrong rejection", failure);
        }
        throw new AssertionError("Expected " + expected.getSimpleName());
    }
}

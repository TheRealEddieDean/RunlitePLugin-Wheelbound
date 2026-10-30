package com.wheelbound.domain;

import java.util.Collections;
import java.util.UUID;

/** Runs without client or storage dependencies. */
public final class GrandAttemptSuccessChecks
{
    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Grand success proposal contract violated"); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException | ArithmeticException expected) { return; }
        throw new AssertionError("Invalid success proposal accepted");
    }

    public static void main(String[] args)
    {
        RunIdentity identity = new RunIdentity("success-account", UUID.randomUUID());
        for (int ticket = 0; ticket < 5; ticket++)
        {
            SealedGrandFate seal = new SealedGrandFate(identity, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
            GrandFateProgress progress = new GrandFateProgress(seal, 0, Collections.emptyList());
            RunLifecycle ready = new RunLifecycle(identity, 0, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
            for (GrandFateChecklist.Objective objective : progress.checklist().required())
            {
                GrandFateProgress prefix = progress;
                GrandFateAttempt attempt = GrandFateAttempt.activate(ready, prefix.checklist(), objective,
                    identity, ready.revision());
                long revision = attempt.lifecycle().revision();
                GrandFateProgress.Evidence reference = new GrandFateProgress.Evidence(seal, objective,
                    UUID.randomUUID(), revision + 1);
                for (RunLifecycle.State state : RunLifecycle.State.values())
                {
                    for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
                    {
                        if (state == RunLifecycle.State.ACTIVE && obligation == RunLifecycle.Obligation.ACTIVE_OBJECTIVE)
                        { continue; }
                        rejects(() -> attempt.proposeSuccess(new RunLifecycle(identity, revision, state, obligation),
                            prefix, reference, identity, revision));
                    }
                }
                rejects(() -> attempt.proposeSuccess(attempt.lifecycle(), prefix, reference, identity, revision - 1));
                rejects(() -> attempt.proposeSuccess(attempt.lifecycle(), prefix, reference,
                    new RunIdentity("foreign", identity.runId()), revision));
                rejects(() -> attempt.proposeSuccess(attempt.lifecycle().pause(identity, revision)
                    .resume(identity, revision + 1), prefix, reference, identity, revision));
                rejects(() -> attempt.proposeSuccess(attempt.lifecycle(), prefix,
                    new GrandFateProgress.Evidence(seal, objective, UUID.randomUUID(), revision), identity, revision));
                SealedGrandFate other = new SealedGrandFate(identity, UUID.randomUUID(), seal.commitment());
                rejects(() -> attempt.proposeSuccess(attempt.lifecycle(), prefix,
                    new GrandFateProgress.Evidence(other, objective, UUID.randomUUID(), revision + 1), identity, revision));
                for (GrandFateChecklist.Objective unselected : prefix.checklist().unfinished())
                {
                    if (unselected == objective) { continue; }
                    rejects(() -> attempt.proposeSuccess(attempt.lifecycle(), prefix,
                        new GrandFateProgress.Evidence(seal, unselected, UUID.randomUUID(), revision + 1), identity, revision));
                }
                if (!prefix.evidence().isEmpty())
                {
                    rejects(() -> attempt.proposeSuccess(attempt.lifecycle(),
                        new GrandFateProgress(seal, revision, Collections.emptyList()), reference, identity, revision));
                    UUID reused = prefix.evidence().values().iterator().next().receiptId();
                    rejects(() -> attempt.proposeSuccess(attempt.lifecycle(), prefix,
                        new GrandFateProgress.Evidence(seal, objective, reused, revision + 1), identity, revision));
                }
                GrandFateAttempt.Success result = attempt.proposeSuccess(attempt.lifecycle(), prefix, reference, identity, revision);
                check(result.lifecycle().state() == RunLifecycle.State.ACTIVE);
                check(result.lifecycle().obligation() == RunLifecycle.Obligation.NONE);
                check(result.lifecycle().revision() == revision + 1);
                check(result.progress().evidence().get(objective).equals(reference));
                check(prefix.checklist().isUnfinished(objective));
                for (GrandFateProgress.Evidence retained : prefix.evidence().values())
                { check(result.progress().evidence().get(retained.objective()).equals(retained)); }
                rejects(() -> attempt.proposeSuccess(result.lifecycle(), prefix, reference, identity, revision));
                progress = result.progress();
                ready = result.lifecycle();
            }
            check(progress.checklist().unfinished().isEmpty());
            GrandFateProgress empty = new GrandFateProgress(seal, 0, Collections.emptyList());
            GrandFateChecklist.Objective objective = empty.checklist().required().iterator().next();
            GrandFateAttempt last = GrandFateAttempt.activate(new RunLifecycle(identity, Long.MAX_VALUE - 1,
                RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), empty.checklist(), objective,
                identity, Long.MAX_VALUE - 1);
            rejects(() -> last.proposeSuccess(last.lifecycle(), empty, new GrandFateProgress.Evidence(
                seal, objective, UUID.randomUUID(), Long.MAX_VALUE), identity, Long.MAX_VALUE));
        }
        System.out.println("Grand attempt success checks passed");
    }
}

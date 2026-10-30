package com.wheelbound.domain;

import java.util.EnumSet;
import java.util.UUID;

public final class GrandFateAttemptChecks
{
    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Grand attempt contract violated"); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException | ArithmeticException expected) { return; }
        throw new AssertionError("Invalid attempt accepted");
    }

    public static void main(String[] args)
    {
        RunIdentity identity = new RunIdentity("attempt-account", UUID.randomUUID());
        RunIdentity foreign = new RunIdentity("other-account", identity.runId());
        RunIdentity foreignRun = new RunIdentity(identity.accountKey(), UUID.randomUUID());
        RunLifecycle ready = new RunLifecycle(identity, 4, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE);
        for (int ticket = 0; ticket < 5; ticket++)
        {
            GrandFateChecklist initial = GrandFateChecklist.initial(new SealedGrandFate(identity,
                UUID.randomUUID(), GrandFatePool.atTicket(ticket)));
            EnumSet<GrandFateChecklist.Objective> completed = EnumSet.noneOf(GrandFateChecklist.Objective.class);
            for (GrandFateChecklist.Objective objective : initial.required())
            {
                GrandFateChecklist progress = GrandFateChecklist.restore(initial.seal(), completed);
                GrandFateAttempt attempt = GrandFateAttempt.activate(ready, progress, objective, identity, 4);
                check(attempt.objective() == objective && attempt.checklist() == progress);
                check(attempt.lifecycle().revision() == 5);
                check(attempt.lifecycle().obligation() == RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
                check(ready.revision() == 4 && ready.obligation() == RunLifecycle.Obligation.NONE);
                rejects(() -> GrandFateAttempt.activate(attempt.lifecycle(), progress, objective, identity, 5));
                rejects(() -> attempt.endUnsuccessful(identity, 4));
                rejects(() -> attempt.endUnsuccessful(foreign, 5));
                rejects(() -> attempt.endUnsuccessful(foreignRun, 5));
                RunLifecycle ended = attempt.endUnsuccessful(identity, 5);
                check(ended.revision() == 6 && ended.obligation() == RunLifecycle.Obligation.NONE);
                check(progress.completed().equals(completed) && progress.isUnfinished(objective));
                check(GrandFateAttempt.activate(ended, progress, objective, identity, 6).lifecycle().revision() == 7);
                rejects(() -> GrandFateAttempt.activate(ready, progress, objective, foreign, 4));
                rejects(() -> GrandFateAttempt.activate(ready, progress, objective, identity, 3));
                rejects(() -> GrandFateAttempt.activate(ready, progress, null, identity, 4));
                GrandFateChecklist otherSeal = GrandFateChecklist.initial(new SealedGrandFate(foreign,
                    UUID.randomUUID(), GrandFatePool.atTicket(ticket)));
                rejects(() -> GrandFateAttempt.activate(ready, otherSeal, objective, identity, 4));
                for (RunLifecycle.State state : RunLifecycle.State.values())
                {
                    for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
                    {
                        if (state == RunLifecycle.State.ACTIVE && obligation == RunLifecycle.Obligation.NONE) { continue; }
                        RunLifecycle blocked = new RunLifecycle(identity, 4, state, obligation);
                        rejects(() -> GrandFateAttempt.activate(blocked, progress, objective, identity, 4));
                    }
                }
                completed.add(objective);
                GrandFateChecklist retained = GrandFateChecklist.restore(initial.seal(), completed);
                rejects(() -> GrandFateAttempt.activate(ready, retained, objective, identity, 4));
            }
            GrandFateChecklist.Objective selected = initial.required().iterator().next();
            for (GrandFateChecklist.Objective objective : GrandFateChecklist.Objective.values())
            {
                if (!initial.required().contains(objective))
                { rejects(() -> GrandFateAttempt.activate(ready, initial, objective, identity, 4)); }
            }
            RunLifecycle maximum = new RunLifecycle(identity, Long.MAX_VALUE, RunLifecycle.State.ACTIVE,
                RunLifecycle.Obligation.NONE);
            rejects(() -> GrandFateAttempt.activate(maximum, initial, selected, identity, Long.MAX_VALUE));
            RunLifecycle penultimate = new RunLifecycle(identity, Long.MAX_VALUE - 1, RunLifecycle.State.ACTIVE,
                RunLifecycle.Obligation.NONE);
            GrandFateAttempt last = GrandFateAttempt.activate(penultimate, initial, selected, identity, Long.MAX_VALUE - 1);
            rejects(() -> last.endUnsuccessful(identity, Long.MAX_VALUE));
        }
        System.out.println("Grand Fate attempt checks passed");
    }
}

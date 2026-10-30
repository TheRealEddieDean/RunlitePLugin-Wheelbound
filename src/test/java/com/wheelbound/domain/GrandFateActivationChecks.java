package com.wheelbound.domain;

import java.util.UUID;

/** Exhaustive lifecycle gate checks; no attempt, client evidence or durability claim. */
public final class GrandFateActivationChecks
{
    private static void check(boolean condition)
    {
        if (!condition) { throw new AssertionError("Grand activation lifecycle gate violated"); }
    }

    public static void main(String[] args)
    {
        RunIdentity identity = new RunIdentity("account-a", UUID.randomUUID());
        RunIdentity otherAccount = new RunIdentity("account-b", identity.runId());
        RunIdentity otherRun = new RunIdentity("account-a", UUID.randomUUID());
        for (RunLifecycle.State state : RunLifecycle.State.values())
        {
            for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
            {
                RunLifecycle run = new RunLifecycle(identity, 7, state, obligation);
                boolean allowed = state == RunLifecycle.State.ACTIVE
                    && obligation == RunLifecycle.Obligation.NONE;
                check(run.permitsGrandFateActivation(identity, 7) == allowed);
                check(!run.permitsGrandFateActivation(identity, 6));
                check(!run.permitsGrandFateActivation(identity, 8));
                check(!run.permitsGrandFateActivation(identity, -1));
                check(!run.permitsGrandFateActivation(otherAccount, 7));
                check(!run.permitsGrandFateActivation(otherRun, 7));
                check(!run.permitsGrandFateActivation(null, 7));
                check(run.state() == state && run.obligation() == obligation && run.revision() == 7);
                if (state == RunLifecycle.State.ACTIVE)
                {
                    RunLifecycle paused = run.pause(identity, 7);
                    check(!paused.permitsGrandFateActivation(identity, 8));
                    RunLifecycle resumed = paused.resume(identity, 8);
                    check(resumed.permitsGrandFateActivation(identity, 9) == allowed);
                    check(!resumed.permitsGrandFateActivation(identity, 7));
                    check(resumed.obligation() == obligation);
                }
            }
        }
        RunLifecycle boundary = new RunLifecycle(identity, Long.MAX_VALUE,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        check(boundary.permitsGrandFateActivation(identity, Long.MAX_VALUE));
        System.out.println("Grand Fate activation checks passed");
    }
}

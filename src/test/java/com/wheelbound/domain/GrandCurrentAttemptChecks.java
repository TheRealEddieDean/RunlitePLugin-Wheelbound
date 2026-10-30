package com.wheelbound.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public final class GrandCurrentAttemptChecks
{
    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Current attempt contract violated"); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Invalid current attempt accepted");
    }

    public static void main(String[] args)
    {
        RunIdentity identity = new RunIdentity("current-attempt", UUID.randomUUID());
        for (int ticket = 0; ticket < 5; ticket++)
        {
            SealedGrandFate seal = new SealedGrandFate(identity, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
            GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
            GrandFateChecklist.Objective objective = checklist.unfinished().iterator().next();
            GrandActivationReceipt receipt = new GrandActivationReceipt(UUID.randomUUID(), identity, 0, checklist, objective);
            GrandActivationHistory history = new GrandActivationHistory(seal, 1, Collections.singletonList(receipt));
            GrandCurrentAttempt active = GrandCurrentAttempt.restore(receipt.commandId(), history, receipt.attempt().lifecycle(), checklist);
            GrandCurrentAttempt paused = active.pause(identity, 1);
            check(paused.lifecycle().state() == RunLifecycle.State.PAUSED);
            check(!paused.lifecycle().acceptsObservation(identity, 2));
            GrandCurrentAttempt resumed = paused.resume(identity, 2);
            check(resumed.lifecycle().revision() == 3 && resumed.activation() == receipt);
            check(active.lifecycle().revision() == 1 && receipt.afterRevision() == 1);
            check(GrandCurrentAttempt.restore(receipt.commandId(), history, paused.lifecycle(), checklist).activation() == receipt);
            check(GrandCurrentAttempt.restore(receipt.commandId(), history, resumed.lifecycle(), checklist).activation() == receipt);
            rejects(() -> resumed.pause(identity, 1));
            rejects(() -> resumed.resume(new RunIdentity("foreign", identity.runId()), 3));
            rejects(() -> GrandCurrentAttempt.restore(receipt.commandId(), history,
                new RunLifecycle(new RunIdentity("foreign", identity.runId()), 3,
                    RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE), checklist));
            rejects(() -> GrandCurrentAttempt.restore(UUID.randomUUID(), history, resumed.lifecycle(), checklist));
            rejects(() -> GrandCurrentAttempt.restore(receipt.commandId(), new GrandActivationHistory(seal, 3, Collections.emptyList()), resumed.lifecycle(), checklist));
            rejects(() -> GrandCurrentAttempt.restore(receipt.commandId(), history, resumed.lifecycle(), GrandFateChecklist.restore(seal, Collections.singleton(objective))));
            SealedGrandFate foreign = new SealedGrandFate(identity, UUID.randomUUID(), seal.commitment());
            rejects(() -> GrandCurrentAttempt.restore(receipt.commandId(), history, resumed.lifecycle(), GrandFateChecklist.initial(foreign)));
            for (RunLifecycle.State state : RunLifecycle.State.values())
            {
                if (state == RunLifecycle.State.ACTIVE || state == RunLifecycle.State.PAUSED) { continue; }
                rejects(() -> GrandCurrentAttempt.restore(receipt.commandId(), history,
                    new RunLifecycle(identity, 3, state, RunLifecycle.Obligation.ACTIVE_OBJECTIVE), checklist));
            }
            for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
            {
                if (obligation == RunLifecycle.Obligation.ACTIVE_OBJECTIVE) { continue; }
                rejects(() -> GrandCurrentAttempt.restore(receipt.commandId(), history,
                    new RunLifecycle(identity, 3, RunLifecycle.State.ACTIVE, obligation), checklist));
            }
            rejects(() -> GrandCurrentAttempt.restore(receipt.commandId(), history,
                new RunLifecycle(identity, 0, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE), checklist));
            GrandActivationReceipt retry = new GrandActivationReceipt(UUID.randomUUID(), identity, 2, checklist, objective);
            GrandActivationHistory retried = new GrandActivationHistory(seal, 3, Arrays.asList(receipt, retry));
            rejects(() -> GrandCurrentAttempt.restore(receipt.commandId(), retried, resumed.lifecycle(), checklist));
            check(GrandCurrentAttempt.restore(retry.commandId(), retried, resumed.lifecycle(), checklist).activation() == retry);
            for (GrandFateChecklist.Objective remaining : checklist.unfinished())
            {
                if (remaining == objective) { continue; }
                GrandFateChecklist prefix = GrandFateChecklist.restore(seal, Collections.singleton(objective));
                GrandActivationReceipt next = new GrandActivationReceipt(UUID.randomUUID(), identity, 4, prefix, remaining);
                GrandActivationHistory advanced = new GrandActivationHistory(seal, 5, Arrays.asList(receipt, retry, next));
                GrandCurrentAttempt retained = GrandCurrentAttempt.restore(next.commandId(), advanced, next.attempt().lifecycle(), prefix);
                check(retained.pause(identity, 5).resume(identity, 6).activation().attempt().checklist().completed().contains(objective));
                rejects(() -> GrandCurrentAttempt.restore(next.commandId(), advanced, next.attempt().lifecycle(), checklist));
            }
            check(GrandCurrentAttempt.restore(receipt.commandId(), history,
                new RunLifecycle(identity, Long.MAX_VALUE, RunLifecycle.State.PAUSED, RunLifecycle.Obligation.ACTIVE_OBJECTIVE), checklist).activation() == receipt);
        }
        System.out.println("Grand current attempt checks passed");
    }
}

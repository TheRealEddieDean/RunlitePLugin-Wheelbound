package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Dependency-free aggregate validation; does not exercise durable persistence. */
public final class GrandCurrentAttemptAggregateChecks
{
    static RunDocument document(RunLifecycle lifecycle, SealedGrandFate seal,
        GrandActivationHistory history, GrandCurrentAttempt attempt, GrandFateProgress progress)
    {
        return new RunDocument(lifecycle, "test", List.of(), Optional.empty(), Optional.empty(),
            Optional.of(seal), Optional.ofNullable(progress), Optional.empty(), Optional.ofNullable(history),
            Optional.ofNullable(attempt));
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Inconsistent current attempt accepted");
    }

    public static void main(String[] args)
    {
        for (int ticket = 0; ticket < 5; ticket++)
        {
            RunIdentity id = new RunIdentity("current-aggregate", UUID.randomUUID());
            SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
            GrandFateChecklist initial = GrandFateChecklist.initial(seal);
            for (GrandFateChecklist.Objective objective : initial.unfinished())
            {
                GrandActivationReceipt activation = new GrandActivationReceipt(UUID.randomUUID(), id, 0, initial, objective);
                GrandActivationHistory history = new GrandActivationHistory(seal, 1, List.of(activation));
                GrandCurrentAttempt active = GrandCurrentAttempt.restore(activation.commandId(), history,
                    activation.attempt().lifecycle(), initial);
                for (GrandCurrentAttempt attempt : List.of(active, active.pause(id, 1),
                    active.pause(id, 1).resume(id, 2), GrandCurrentAttempt.restore(activation.commandId(), history,
                        new RunLifecycle(id, Long.MAX_VALUE, RunLifecycle.State.PAUSED,
                            RunLifecycle.Obligation.ACTIVE_OBJECTIVE), initial)))
                {
                    RunDocument aggregate = document(attempt.lifecycle(), seal, history, attempt, null);
                    if (!aggregate.grandCurrentAttempt().get().activation().equals(activation))
                    { throw new AssertionError("Original activation lost"); }
                    if (document(attempt.lifecycle(), seal, history, null, null).grandCurrentAttempt().isPresent())
                    { throw new AssertionError("Attempt inferred from history"); }
                }
                rejects(() -> document(active.lifecycle(), seal, null, active, null));
                rejects(() -> document(active.pause(id, 1).lifecycle(), seal, history, active, null));
                rejects(() -> document(new RunLifecycle(id, 1, RunLifecycle.State.PAUSED,
                    RunLifecycle.Obligation.ACTIVE_OBJECTIVE), seal, history, active, null));
                rejects(() -> document(new RunLifecycle(id, 1, RunLifecycle.State.ACTIVE,
                    RunLifecycle.Obligation.NONE), seal, history, active, null));
                GrandFateProgress completed = new GrandFateProgress(seal, 1, List.of(
                    new GrandFateProgress.Evidence(seal, objective, UUID.randomUUID(), 1)));
                rejects(() -> document(active.lifecycle(), seal, history, active, completed));
                if (initial.unfinished().size() > 1)
                {
                    GrandFateChecklist.Objective other = initial.unfinished().stream().filter(o -> o != objective).findFirst().get();
                    GrandFateProgress prefix = new GrandFateProgress(seal, 3, List.of(
                        new GrandFateProgress.Evidence(seal, other, UUID.randomUUID(), 1)));
                    GrandActivationReceipt retained = new GrandActivationReceipt(UUID.randomUUID(), id, 2,
                        prefix.checklist(), objective);
                    GrandActivationHistory retainedHistory = new GrandActivationHistory(seal, 3, List.of(retained));
                    GrandCurrentAttempt retainedAttempt = GrandCurrentAttempt.restore(retained.commandId(), retainedHistory,
                        retained.attempt().lifecycle(), prefix.checklist());
                    if (!document(retainedAttempt.lifecycle(), seal, retainedHistory, retainedAttempt, prefix)
                        .grandCurrentAttempt().get().activation().equals(retained))
                    { throw new AssertionError("Completed prefix lost"); }
                    rejects(() -> document(retainedAttempt.lifecycle(), seal, retainedHistory, retainedAttempt, null));
                    GrandActivationReceipt changed = new GrandActivationReceipt(activation.commandId(), id, 0, initial, other);
                    rejects(() -> document(active.lifecycle(), seal,
                        new GrandActivationHistory(seal, 1, List.of(changed)), active, null));
                    GrandActivationReceipt later = new GrandActivationReceipt(UUID.randomUUID(), id, 2, initial, other);
                    GrandActivationHistory superseded = new GrandActivationHistory(seal, 3, List.of(activation, later));
                    GrandCurrentAttempt head = GrandCurrentAttempt.restore(activation.commandId(), history,
                        new RunLifecycle(id, 3, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE), initial);
                    rejects(() -> document(head.lifecycle(), seal, superseded, head, null));
                }
            }
        }
    }
}

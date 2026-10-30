package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Dependency-free structural aggregate checks; history does not prove active state. */
public final class GrandActivationAggregateChecks
{
    static RunDocument document(SealedGrandFate seal, long head, GrandActivationHistory history,
        List<RunReceipt> operations)
    {
        return new RunDocument(new RunLifecycle(seal.identity(), head, RunLifecycle.State.PAUSED,
            RunLifecycle.Obligation.NONE), "test", operations, Optional.empty(), Optional.empty(),
            Optional.of(seal), Optional.empty(), Optional.empty(), Optional.ofNullable(history));
    }
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid aggregate accepted");
    }
    public static void main(String[] args)
    {
        for (int ticket = 0; ticket < 5; ticket++)
        {
            RunIdentity id = new RunIdentity("activation-aggregate", UUID.randomUUID());
            SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
            GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
            GrandActivationReceipt receipt = new GrandActivationReceipt(UUID.randomUUID(), id, 0,
                checklist, checklist.unfinished().iterator().next());
            GrandActivationHistory history = new GrandActivationHistory(seal, 1, List.of(receipt));
            for (long head : new long[] {1, 40, Long.MAX_VALUE})
            {
                RunDocument restored = document(seal, head, history, List.of());
                if (!restored.grandActivations().get().replay(receipt, id, head).equals(receipt))
                { throw new AssertionError("Original activation lost"); }
            }
            if (document(seal, 0, null, List.of()).grandActivations().isPresent())
            { throw new AssertionError("Legacy absence synthesized"); }
            rejects(() -> document(seal, 0, history, List.of()));
            rejects(() -> document(new SealedGrandFate(id, UUID.randomUUID(), seal.commitment()), 1, history, List.of()));
            RunReceipt collision = new RunReceipt(receipt.commandId(), RunReceipt.Operation.PAUSE,
                1, 2, RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
            rejects(() -> document(seal, 2, history, List.of(collision)));
            if (checklist.unfinished().size() > 1)
            {
                GrandFateChecklist advanced = GrandFateChecklist.restore(seal, Set.of(receipt.attempt().objective()));
                GrandActivationReceipt later = new GrandActivationReceipt(UUID.randomUUID(), id, 2,
                    advanced, advanced.unfinished().iterator().next());
                GrandActivationHistory withPrefix = new GrandActivationHistory(seal, 3, List.of(receipt, later));
                rejects(() -> document(seal, 3, withPrefix, List.of()));
                GrandFateProgress future = new GrandFateProgress(seal, 4, List.of(new GrandFateProgress.Evidence(
                    seal, receipt.attempt().objective(), UUID.randomUUID(), 4)));
                rejects(() -> new RunDocument(new RunLifecycle(id, 4, RunLifecycle.State.ACTIVE,
                    RunLifecycle.Obligation.NONE), "test", List.of(), Optional.empty(), Optional.empty(),
                    Optional.of(seal), Optional.of(future), Optional.empty(), Optional.of(withPrefix)));
                GrandFateProgress prior = new GrandFateProgress(seal, 3, List.of(new GrandFateProgress.Evidence(
                    seal, receipt.attempt().objective(), UUID.randomUUID(), 2)));
                RunDocument valid = new RunDocument(new RunLifecycle(id, 3, RunLifecycle.State.ACTIVE,
                    RunLifecycle.Obligation.NONE), "test", List.of(), Optional.empty(), Optional.empty(),
                    Optional.of(seal), Optional.of(prior), Optional.empty(), Optional.of(withPrefix));
                if (valid.grandActivations().get().receipts().size() != 2) { throw new AssertionError(); }
            }
        }
    }
}

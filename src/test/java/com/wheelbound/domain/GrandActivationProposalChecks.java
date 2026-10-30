package com.wheelbound.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class GrandActivationProposalChecks
{
    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Activation proposal contract"); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | ArithmeticException expected) { return; }
        throw new AssertionError("Invalid activation accepted");
    }

    public static void main(String[] args)
    {
        RunIdentity id = new RunIdentity("activation-proposal", UUID.randomUUID());
        for (int ticket = 0; ticket < 5; ticket++)
        {
            SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
            GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
            GrandActivationHistory empty = new GrandActivationHistory(seal, 0, List.of());
            RunLifecycle before = new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
            for (GrandFateChecklist.Objective objective : checklist.unfinished())
            {
                GrandActivationReceipt request = new GrandActivationReceipt(UUID.randomUUID(), id, 0, checklist, objective);
                GrandActivationProposal result = GrandActivationProposal.propose(before, checklist, empty,
                    Optional.empty(), request, id, 0);
                check(!result.isReplay() && result.lifecycle().revision() == 1
                    && result.lifecycle().obligation() == RunLifecycle.Obligation.ACTIVE_OBJECTIVE
                    && result.currentAttempt().get().activation().equals(request)
                    && result.history().receipts().size() == 1);
                check(empty.receipts().isEmpty() && before.revision() == 0);
                GrandCurrentAttempt paused = result.currentAttempt().get().pause(id, 1);
                GrandActivationProposal replay = GrandActivationProposal.propose(paused.lifecycle(), checklist,
                    result.history(), Optional.of(paused), request, id, 2);
                check(replay.isReplay() && replay.lifecycle() == paused.lifecycle()
                    && replay.receipt() == result.receipt() && replay.currentAttempt().get() == paused);
                RunLifecycle ended = new RunLifecycle(id, Long.MAX_VALUE, RunLifecycle.State.ACTIVE,
                    RunLifecycle.Obligation.NONE);
                GrandActivationProposal old = GrandActivationProposal.propose(ended, checklist,
                    result.history(), Optional.empty(), request, id, Long.MAX_VALUE);
                check(old.isReplay() && old.currentAttempt().isEmpty() && old.lifecycle() == ended);
                rejects(() -> GrandActivationProposal.propose(before, checklist, empty, Optional.empty(), request, id, 1));
                rejects(() -> GrandActivationProposal.propose(before, checklist, empty, Optional.empty(), request,
                    new RunIdentity("foreign", id.runId()), 0));
                GrandActivationReceipt collision = new GrandActivationReceipt(request.commandId(), id, 2, checklist, objective);
                rejects(() -> GrandActivationProposal.propose(paused.lifecycle(), checklist, result.history(),
                    Optional.of(paused), collision, id, 2));
                GrandActivationReceipt fresh = new GrandActivationReceipt(UUID.randomUUID(), id, 2, checklist, objective);
                rejects(() -> GrandActivationProposal.propose(paused.lifecycle(), checklist, result.history(),
                    Optional.of(paused), fresh, id, 2));
                for (RunLifecycle.State state : RunLifecycle.State.values())
                {
                    if (state == RunLifecycle.State.ACTIVE) { continue; }
                    rejects(() -> GrandActivationProposal.propose(new RunLifecycle(id, 0, state,
                        RunLifecycle.Obligation.NONE), checklist, empty, Optional.empty(), request, id, 0));
                }
                for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
                {
                    if (obligation == RunLifecycle.Obligation.NONE) { continue; }
                    rejects(() -> GrandActivationProposal.propose(new RunLifecycle(id, 0,
                        RunLifecycle.State.ACTIVE, obligation), checklist, empty, Optional.empty(), request, id, 0));
                }
                GrandActivationReceipt sealCollision = new GrandActivationReceipt(seal.commandId(), id, 0, checklist, objective);
                rejects(() -> GrandActivationProposal.propose(before, checklist, empty, Optional.empty(), sealCollision, id, 0));
            }
        }
        System.out.println("Grand activation proposal checks passed");
    }
}

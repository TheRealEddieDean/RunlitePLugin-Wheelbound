package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Executable aggregate admission checks; no persistence or client admission claim. */
public final class GrandActivationAdmissionChecks
{
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid admission accepted");
    }

    public static void main(String[] args)
    {
        for (int ticket = 0; ticket < 5; ticket++)
        {
            RunIdentity id = new RunIdentity("admission", UUID.randomUUID());
            SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
            GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
            GrandActivationHistory empty = new GrandActivationHistory(seal, 0, List.of());
            for (GrandFateChecklist.Objective objective : checklist.unfinished())
            {
                GrandActivationReceipt request = new GrandActivationReceipt(UUID.randomUUID(), id, 0, checklist, objective);
                RunLifecycle head = new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
                RunDocument current = document(head, seal, empty, List.of());
                GrandActivationProposal proposal = GrandActivationAdmission.propose(current, request, id, 0);
                if (proposal.isReplay() || proposal.lifecycle().revision() != 1
                    || current.lifecycle().revision() != 0 || !current.grandActivations().get().receipts().isEmpty())
                { throw new AssertionError("Proposal mutated input or failed activation"); }
                rejects(() -> GrandActivationAdmission.propose(document(head, seal, null, List.of()), request, id, 0));
                rejects(() -> GrandActivationAdmission.propose(current, request, id, 1));
                rejects(() -> GrandActivationAdmission.propose(current, request,
                    new RunIdentity("foreign", UUID.randomUUID()), 0));
                for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
                {
                    if (obligation == RunLifecycle.Obligation.NONE) { continue; }
                    RunLifecycle pending = new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE, obligation);
                    rejects(() -> GrandActivationAdmission.propose(document(pending, seal, empty, List.of()), request, id, 0));
                    // Original replay never reactivates or clears a newer obligation.
                    RunLifecycle later = new RunLifecycle(id, Long.MAX_VALUE, RunLifecycle.State.ACTIVE, obligation);
                    GrandActivationProposal replay = GrandActivationAdmission.propose(
                        document(later, seal, proposal.history(), List.of()), request, id, Long.MAX_VALUE);
                    if (!replay.isReplay() || replay.receipt() != proposal.receipt()
                        || replay.lifecycle() != later || replay.currentAttempt().isPresent())
                    { throw new AssertionError("Replay changed later state"); }
                }
                RunReceipt collision = new RunReceipt(request.commandId(), RunReceipt.Operation.RESUME,
                    0, 1, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
                RunLifecycle resumed = new RunLifecycle(id, 1, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
                GrandActivationReceipt reused = new GrandActivationReceipt(request.commandId(), id, 1, checklist, objective);
                rejects(() -> GrandActivationAdmission.propose(document(resumed, seal, empty, List.of(collision)), reused, id, 1));
                GrandActivationReceipt sealCollision = new GrandActivationReceipt(seal.commandId(), id, 0, checklist, objective);
                rejects(() -> GrandActivationAdmission.propose(current, sealCollision, id, 0));
                GrandFateProgress progress = new GrandFateProgress(seal, 2, List.of(
                    new GrandFateProgress.Evidence(seal, objective, request.commandId(), 2)));
                GrandEvidenceReceipt evidence = new GrandEvidenceReceipt(seal, objective,
                    request.commandId(), 1, UUID.randomUUID(), "fixture", "event");
                GrandEvidenceJournal journal = new GrandEvidenceJournal(progress, 2, List.of(evidence));
                RunDocument evidenced = new RunDocument(new RunLifecycle(id, 2,
                    RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), "test", List.of(),
                    Optional.empty(), Optional.empty(), Optional.of(seal), Optional.of(progress),
                    Optional.of(journal), Optional.of(empty));
                rejects(() -> GrandActivationAdmission.propose(evidenced, request, id, 2));
            }
        }
    }

    private static RunDocument document(RunLifecycle head, SealedGrandFate seal,
        GrandActivationHistory history, List<RunReceipt> receipts)
    {
        return new RunDocument(head, "test", receipts, Optional.empty(), Optional.empty(),
            Optional.of(seal), Optional.empty(), Optional.empty(), Optional.ofNullable(history));
    }
}

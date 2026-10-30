package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class GrandEvidenceJournalChecks
{
    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Grand journal invariant"); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Invalid journal accepted");
    }
    public static void main(String[] args)
    {
        RunIdentity identity = new RunIdentity("journal-account", UUID.randomUUID());
        UUID session = UUID.randomUUID();
        for (int ticket = 0; ticket < 5; ticket++)
        {
            SealedGrandFate seal = new SealedGrandFate(identity, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
            GrandEvidenceJournal journal = new GrandEvidenceJournal(
                new GrandFateProgress(seal, 0, Collections.emptyList()), 0, Collections.emptyList());
            RunLifecycle ready = new RunLifecycle(identity, 0, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
            for (GrandFateChecklist.Objective objective : journal.progress().checklist().required())
            {
                GrandEvidenceJournal prefix = journal;
                GrandFateAttempt attempt = GrandFateAttempt.activate(ready, prefix.progress().checklist(), objective,
                    identity, ready.revision());
                RunLifecycle active = attempt.lifecycle();
                GrandEvidenceReceipt receipt = new GrandEvidenceReceipt(seal, objective, UUID.randomUUID(),
                    active.revision(), session, "fixture", objective.name());
                journal = prefix.proposeAppend(receipt, attempt, active, session, identity, active.revision());
                long revision = active.revision() + 1;
                ready = new RunLifecycle(identity, revision, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
                GrandEvidenceJournal accepted = journal;
                RunLifecycle committed = ready;
                check(prefix.receipts().size() + 1 == accepted.receipts().size());
                check(prefix.progress().checklist().isUnfinished(objective));
                check(accepted.progress().checklist().completed().contains(objective));
                // Replay needs neither the old attempt nor its expired session.
                check(accepted.proposeAppend(receipt, null, committed, null, identity, revision) == accepted);
                check(accepted.proposeAppend(receipt, null,
                    new RunLifecycle(identity, Long.MAX_VALUE, RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE),
                    null, identity, Long.MAX_VALUE) == accepted);
                rejects(() -> accepted.proposeAppend(receipt, null, committed, null, identity, revision - 1));
                GrandEvidenceReceipt collision = new GrandEvidenceReceipt(seal, objective,
                    receipt.reference().receiptId(), active.revision(), session, "fixture", "changed");
                rejects(() -> accepted.proposeAppend(collision, null, committed, null, identity, revision));
                GrandEvidenceReceipt duplicateSource = new GrandEvidenceReceipt(seal, objective, UUID.randomUUID(),
                    active.revision(), session, "fixture", objective.name());
                rejects(() -> accepted.proposeAppend(duplicateSource, null, committed, null, identity, revision));
                rejects(() -> prefix.proposeAppend(receipt, attempt, active, UUID.randomUUID(), identity, active.revision()));
                rejects(() -> new GrandEvidenceJournal(accepted.progress(), revision, Collections.emptyList()));
                rejects(() -> new GrandEvidenceJournal(accepted.progress(), revision - 1, accepted.receipts().values()));
                List<GrandEvidenceReceipt> payloads = new ArrayList<>(accepted.receipts().values());
                GrandEvidenceJournal restored = new GrandEvidenceJournal(accepted.progress(), revision, payloads);
                payloads.clear();
                check(restored.receipts().equals(accepted.receipts()));
                check(restored.proposeAppend(receipt, null, committed, null, identity, revision) == restored);
                try { restored.receipts().clear(); throw new AssertionError("Mutable receipts"); }
                catch (UnsupportedOperationException expected) { }
                payloads.addAll(accepted.receipts().values());
                payloads.add(receipt);
                rejects(() -> new GrandEvidenceJournal(accepted.progress(), revision, payloads));
                if (accepted.receipts().size() > 1)
                {
                    List<GrandEvidenceReceipt> reversed = new ArrayList<>(accepted.receipts().values());
                    Collections.reverse(reversed);
                    rejects(() -> new GrandEvidenceJournal(accepted.progress(), revision, reversed));
                    // Even different objectives cannot reuse a source event.
                    List<GrandEvidenceReceipt> colliding = new ArrayList<>(accepted.receipts().values());
                    GrandEvidenceReceipt first = colliding.get(0);
                    colliding.set(colliding.size() - 1, new GrandEvidenceReceipt(seal, objective,
                        receipt.reference().receiptId(), active.revision(), first.sourceSessionId(),
                        first.sourceNamespace(), first.eventKey()));
                    rejects(() -> new GrandEvidenceJournal(accepted.progress(), revision, colliding));
                }
            }
        }
    }
}

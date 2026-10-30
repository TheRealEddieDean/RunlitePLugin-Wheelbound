package com.wheelbound.domain;

import java.util.Collections;
import java.util.UUID;

/** Executable without client or persistence dependencies; no live verification claim. */
public final class GrandEvidenceReceiptChecks
{
    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Grand receipt binding violated"); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException | ArithmeticException expected) { return; }
        throw new AssertionError("Invalid receipt accepted");
    }

    public static void main(String[] args)
    {
        RunIdentity identity = new RunIdentity("receipt-account", UUID.randomUUID());
        UUID session = UUID.randomUUID();
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
                UUID command = UUID.randomUUID();
                GrandEvidenceReceipt receipt = new GrandEvidenceReceipt(seal, objective, command, revision,
                    session, "fixture.verifier.v1", objective.name());
                GrandEvidenceReceipt restored = new GrandEvidenceReceipt(
                    new SealedGrandFate(identity, seal.commandId(), seal.commitment()), objective, command,
                    revision, session, "fixture.verifier.v1", objective.name());
                check(receipt.equals(restored) && restored.equals(receipt));
                check(receipt.hashCode() == restored.hashCode() && receipt.sameSourceEvent(restored));
                check(receipt.reference().verifiedRevision() == revision + 1);
                check(receipt.sourceSessionId().equals(session) && receipt.attemptRevision() == revision);
                check(receipt.sourceNamespace().equals("fixture.verifier.v1") && receipt.eventKey().equals(objective.name()));
                GrandEvidenceReceipt changedCommand = new GrandEvidenceReceipt(seal, objective, UUID.randomUUID(),
                    revision, session, "fixture.verifier.v1", objective.name());
                check(!receipt.equals(changedCommand) && receipt.sameSourceEvent(changedCommand));
                GrandEvidenceReceipt changedSession = new GrandEvidenceReceipt(seal, objective, command,
                    revision, UUID.randomUUID(), "fixture.verifier.v1", objective.name());
                GrandEvidenceReceipt changedNamespace = new GrandEvidenceReceipt(seal, objective, command,
                    revision, session, "other.verifier", objective.name());
                GrandEvidenceReceipt changedEvent = new GrandEvidenceReceipt(seal, objective, command,
                    revision, session, "fixture.verifier.v1", "other-event");
                for (GrandEvidenceReceipt changed : new GrandEvidenceReceipt[] {changedSession, changedNamespace, changedEvent})
                { check(!receipt.equals(changed) && !receipt.sameSourceEvent(changed)); }
                check(!receipt.equals(new GrandEvidenceReceipt(seal, objective, command, revision + 1,
                    session, "fixture.verifier.v1", objective.name())));
                rejects(() -> changedSession.proposeSuccess(attempt, attempt.lifecycle(), prefix, session, identity, revision));
                rejects(() -> receipt.proposeSuccess(attempt, attempt.lifecycle(), prefix, null, identity, revision));
                rejects(() -> receipt.proposeSuccess(attempt, attempt.lifecycle(), prefix, session, identity, revision - 1));
                rejects(() -> receipt.proposeSuccess(attempt, attempt.lifecycle(), prefix, session,
                    new RunIdentity("foreign", identity.runId()), revision));
                for (RunLifecycle.State state : RunLifecycle.State.values())
                {
                    for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
                    {
                        if (state == RunLifecycle.State.ACTIVE && obligation == RunLifecycle.Obligation.ACTIVE_OBJECTIVE)
                        { continue; }
                        rejects(() -> receipt.proposeSuccess(attempt, new RunLifecycle(identity, revision, state, obligation),
                            prefix, session, identity, revision));
                    }
                }
                SealedGrandFate foreignSeal = new SealedGrandFate(identity, UUID.randomUUID(), seal.commitment());
                GrandEvidenceReceipt wrongSeal = new GrandEvidenceReceipt(foreignSeal, objective, command,
                    revision, session, "fixture.verifier.v1", objective.name());
                check(!receipt.equals(wrongSeal));
                // The same source event remains a collision even across different seal payloads.
                check(receipt.sameSourceEvent(wrongSeal));
                rejects(() -> wrongSeal.proposeSuccess(attempt, attempt.lifecycle(), prefix, session, identity, revision));
                for (GrandFateChecklist.Objective other : prefix.checklist().unfinished())
                {
                    if (other == objective) { continue; }
                    GrandEvidenceReceipt wrongObjective = new GrandEvidenceReceipt(seal, other, command,
                        revision, session, "fixture.verifier.v1", objective.name());
                    check(!receipt.equals(wrongObjective) && receipt.sameSourceEvent(wrongObjective));
                    rejects(() -> wrongObjective.proposeSuccess(attempt, attempt.lifecycle(), prefix, session, identity, revision));
                }
                for (String invalid : new String[] {null, "", " ", " key", "key\n"})
                {
                    rejects(() -> new GrandEvidenceReceipt(seal, objective, command, revision, session, invalid, "event"));
                    rejects(() -> new GrandEvidenceReceipt(seal, objective, command, revision, session, "source", invalid));
                }
                rejects(() -> new GrandEvidenceReceipt(seal, objective, command, 0, session, "source", "event"));
                rejects(() -> new GrandEvidenceReceipt(seal, objective, command, Long.MAX_VALUE, session, "source", "event"));
                rejects(() -> new GrandEvidenceReceipt(seal, objective, seal.commandId(), revision, session, "source", "event"));
                rejects(() -> new GrandEvidenceReceipt(seal, objective, command, revision, null, "source", "event"));
                RunLifecycle paused = attempt.lifecycle().pause(identity, revision);
                rejects(() -> receipt.proposeSuccess(attempt, paused.resume(identity, revision + 1), prefix,
                    session, identity, revision));
                GrandFateAttempt retry = GrandFateAttempt.activate(attempt.endUnsuccessful(attempt.lifecycle(), identity,
                    revision), prefix.checklist(), objective, identity, revision + 1);
                rejects(() -> receipt.proposeSuccess(retry, retry.lifecycle(), prefix, session, identity, revision + 2));
                GrandFateAttempt.Success result = restored.proposeSuccess(attempt, attempt.lifecycle(), prefix,
                    session, identity, revision);
                check(result.progress().evidence().get(objective).equals(receipt.reference()));
                check(prefix.checklist().isUnfinished(objective));
                check(result.lifecycle().state() == RunLifecycle.State.ACTIVE);
                check(result.lifecycle().obligation() == RunLifecycle.Obligation.NONE);
                for (GrandFateProgress.Evidence prior : prefix.evidence().values())
                { check(result.progress().evidence().get(prior.objective()).equals(prior)); }
                rejects(() -> receipt.proposeSuccess(attempt, result.lifecycle(), result.progress(), session,
                    identity, revision + 1));
                progress = result.progress();
                ready = result.lifecycle();
            }
            check(progress.checklist().unfinished().isEmpty());
        }
        System.out.println("Grand evidence receipt checks passed");
    }
}

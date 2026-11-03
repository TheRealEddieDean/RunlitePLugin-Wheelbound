package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

public final class MasterRetryOperationProposalChecks
{
    static MasterRetryContinuation.Result result(RunDocument head,
        MasterSelectionReadiness.Availability availability, int ticket, String state)
    {
        var pending = head.pendingMasterSelection().get();
        Map<String, MasterOrdinaryPoolEvidence> pools = new HashMap<>();
        for (int i = 0; i < 5; i++)
        {
            String activity = "activity-" + i;
            pools.put(activity, new MasterOrdinaryPoolEvidence(head.lifecycle().identity(), head.lifecycle().revision(),
                "snapshot", "catalog", activity, Set.of("normal"), Map.of("normal",
                new MasterSelectionReadiness.Classification(i == 4 ? availability
                    : MasterSelectionReadiness.Availability.VERIFIED_EMPTY, "complete offline fixture"))));
        }
        return MasterRetryContinuation.prepare(head, head.lifecycle().identity(), head.lifecycle().revision(),
            pending.transactionId(), pending.drawCount(), pending.currentStreamState(),
            "snapshot", pools, 2, p -> new MasterSelectionProvenance.Step(
                new DrawCommitment(p.pool(), ticket), state));
    }

    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            var head = MasterRetryContinuationChecks.fixture(count);
            var command = new UUID(70, count);
            var continued = result(head, MasterSelectionReadiness.Availability.ASSIGNABLE, 0, "repeated");
            var proposal = MasterRetryOperationProposal.prepare(head, command, continued);
            check(proposal.identity().equals(head.lifecycle().identity())
                && proposal.commandId().equals(command) && proposal.beforeRevision() == 1
                && proposal.afterRevision() == 2 && proposal.before().drawCount() == count
                && proposal.after().drawCount() == count + 2
                && proposal.status() == MasterRetryContinuation.Status.PROPOSED);
            proposal.validateAgainst(MasterRetryContinuationChecks.fixture(count));
            var restored = MasterRetryOperationProposal.prepare(MasterRetryContinuationChecks.fixture(count),
                command, result(MasterRetryContinuationChecks.fixture(count),
                    MasterSelectionReadiness.Availability.ASSIGNABLE, 0, "repeated"));
            check(proposal.equals(restored) && proposal.hashCode() == restored.hashCode());
            var decoded = MasterRetryOperationProposal.restore(head, proposal.identity(), command,
                proposal.beforeRevision(), count, proposal.after(), proposal.status());
            check(decoded.equals(proposal) && decoded.hashCode() == proposal.hashCode());
            rejects(() -> MasterRetryOperationProposal.restore(head, proposal.identity(), command,
                0, count, proposal.after(), proposal.status()));
            rejects(() -> MasterRetryOperationProposal.restore(head, proposal.identity(), command,
                1, count + 1, proposal.after(), proposal.status()));
            rejects(() -> MasterRetryOperationProposal.restore(head,
                new RunIdentity("foreign", proposal.identity().runId()), command,
                1, count, proposal.after(), proposal.status()));
            rejects(() -> MasterRetryOperationProposal.restore(head, proposal.identity(), command,
                1, count, proposal.after(), MasterRetryContinuation.Status.SUSPENDED));
            var envelope = RunReceipt.masterRetry(proposal);
            var restoredEnvelope = RunReceipt.masterRetry(restored);
            check(envelope.equals(restoredEnvelope) && envelope.hashCode() == restoredEnvelope.hashCode()
                && envelope.masterRetry() == proposal && envelope.operation() == RunReceipt.Operation.MASTER_RETRY
                && envelope.commandId().equals(command) && envelope.beforeRevision() == 1
                && envelope.afterRevision() == 2 && envelope.state() == RunLifecycle.State.ACTIVE
                && envelope.obligation() == RunLifecycle.Obligation.NONE
                && envelope.draw() == null && envelope.debit() == null && envelope.purchase() == null
                && envelope.taintTerminal() == null);
            rejects(() -> new RunReceipt(command, RunReceipt.Operation.MASTER_RETRY, 1, 2,
                RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE));
            var premature = new ArrayList<>(head.receipts());
            premature.add(envelope);
            rejects(() -> new RunDocument(head.lifecycle(), head.catalogVersion(), premature,
                head.fatePoints(), head.inventory(), head.grandFate(), head.grandProgress(),
                head.grandJournal(), head.grandActivations(), head.grandCurrentAttempt(),
                head.pendingMasterSelection(), head.masterCreations(), head.masterResolutions(),
                head.masterSpins(), Optional.empty()));
            check(!proposal.equals(MasterRetryOperationProposal.prepare(head, new UUID(71, count), continued)));
            check(!proposal.equals(MasterRetryOperationProposal.prepare(head, command,
                result(head, MasterSelectionReadiness.Availability.ASSIGNABLE, 1, "repeated"))));
            check(!proposal.equals(MasterRetryOperationProposal.prepare(head, command,
                result(head, MasterSelectionReadiness.Availability.ASSIGNABLE, 0, "changed"))));
            for (var availability : MasterSelectionReadiness.Availability.values())
            {
                if (availability == MasterSelectionReadiness.Availability.ASSIGNABLE) { continue; }
                var suspended = MasterRetryOperationProposal.prepare(head, command,
                    result(head, availability, 19, "must-not-draw"));
                check(suspended.status() == MasterRetryContinuation.Status.SUSPENDED
                    && suspended.after() == head.pendingMasterSelection().get()
                    && suspended.before() == suspended.after() && !suspended.equals(proposal));
                suspended.validateAgainst(head);
                check(suspended.equals(MasterRetryOperationProposal.restore(head, suspended.identity(),
                    command, 1, count, suspended.after(), suspended.status())));
                rejects(() -> MasterRetryOperationProposal.restore(head, suspended.identity(), command,
                    1, count, suspended.after(), MasterRetryContinuation.Status.PROPOSED));
                check(!RunReceipt.masterRetry(suspended).equals(envelope)
                    && RunReceipt.masterRetry(suspended).masterRetry().after() == suspended.before());
            }
            rejects(() -> MasterRetryOperationProposal.prepare(head, command,
                result(head, MasterSelectionReadiness.Availability.ASSIGNABLE, 19, "valid")));
            rejects(() -> MasterRetryOperationProposal.prepare(head,
                head.receipts().get(0).commandId(), continued));
            rejects(() -> proposal.validateAgainst(MasterRetryContinuationChecks.fixture(count + 1)));
            if (count > 0)
            { rejects(() -> proposal.validateAgainst(MasterRetryContinuationChecks.fixture(count - 1))); }
            var paused = LifecycleAdmission.prepare(head, head.lifecycle().identity(), new UUID(72, count),
                1, RunReceipt.Operation.PAUSE);
            rejects(() -> proposal.validateAgainst(paused));
            check(head.pendingMasterSelection().get().drawCount() == count
                && head.lifecycle().revision() == 1 && head.receipts().size() == 1
                && head.masterSpins().isEmpty() && head.currentMasterSpecial().isEmpty());
        }
        System.out.println("MasterRetryOperationProposalChecks PASSED");
    }

    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Retry operation payload invariant"); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected retry operation refusal");
    }
}

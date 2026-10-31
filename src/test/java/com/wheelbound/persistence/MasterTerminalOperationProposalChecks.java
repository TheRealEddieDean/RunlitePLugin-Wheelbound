package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Offline response identity/accounting checks; no storage or gameplay admission. */
public final class MasterTerminalOperationProposalChecks
{
    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            var source = MasterCreationAggregateChecks.fixture(count);
            var pending = source.pendingMasterSelection().orElseThrow();
            var creation = source.masterCreations().orElseThrow().receipts().get(pending.transactionId());
            var step = new MasterSelectionProvenance.Step(
                new DrawCommitment(pending.pool(), pending.pool().totalWeight() - 1), "terminal");
            var request = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5, creation, pending, step);
            for (long balance : new long[] {1, 7, Long.MAX_VALUE})
            {
                var head = MasterSpinAggregateChecks.attach(source,
                    new MasterSpinState(1, source.lifecycle().identity(), 5, balance, Optional.empty()));
                var response = MasterTerminalOperationProposal.prepare(head, request);
                var same = MasterTerminalOperationProposal.restore(request, balance);
                check(response.equals(same) && same.equals(response) && response.hashCode() == same.hashCode());
                check(!response.equals(null) && !response.equals(request));
                check(response.commandId().equals(request.commandId()) && response.beforeRevision() == 5
                    && response.afterRevision() == 6 && response.resolution() == request
                    && response.allocation().spinsAfter() == balance - 1);
                // A proposal cannot validate itself as a historical replay on the unresolved head.
                rejects(() -> response.validateRetained(head));
                var prepared = MasterTerminalReservationPreparation.prepare(head, request);
                var historyOnly = MasterResolutionAggregateChecks.copy(head,
                    new RunLifecycle(head.lifecycle().identity(), 6, RunLifecycle.State.ACTIVE,
                        RunLifecycle.Obligation.NONE), Optional.empty(), Optional.of(prepared.resolutions()));
                rejects(() -> response.validateRetained(historyOnly));
                var retained = MasterSpinAggregateChecks.attach(historyOnly, prepared.spins());
                check(response.validateRetained(retained) == response);
                check(MasterTerminalOperationProposal.prepare(retained, request).equals(response));
                var paused = LifecycleAdmission.prepare(retained, retained.lifecycle().identity(),
                    UUID.randomUUID(), 6, RunReceipt.Operation.PAUSE);
                var resumed = LifecycleAdmission.prepare(paused, paused.lifecycle().identity(),
                    UUID.randomUUID(), 7, RunReceipt.Operation.RESUME);
                check(same.validateRetained(paused) == same && same.validateRetained(resumed) == same);
                long changedBalance = balance == Long.MAX_VALUE ? balance - 1 : balance + 1;
                var changed = MasterTerminalOperationProposal.restore(request, changedBalance);
                check(!changed.equals(response));
                rejects(() -> changed.validateRetained(retained));
                var changedRequest = MasterSelectionResolutionReceipt.prepare(request.commandId(), 5,
                    creation, pending, new MasterSelectionProvenance.Step(step.draw(), "changed"));
                rejects(() -> MasterTerminalOperationProposal.restore(changedRequest, balance).validateRetained(retained));
                var unknownRequest = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5,
                    creation, pending, step);
                rejects(() -> MasterTerminalOperationProposal.restore(unknownRequest, balance).validateRetained(retained));
                check(head.masterSpins().orElseThrow().reservation().isEmpty()
                    && head.pendingMasterSelection().orElseThrow() == pending
                    && retained.masterSpins().orElseThrow() == prepared.spins());
            }
            rejects(() -> MasterTerminalOperationProposal.restore(request, 0));
            rejects(() -> MasterTerminalOperationProposal.restore(request, -1));
            rejects(() -> MasterTerminalOperationProposal.prepare(source, request));
        }
        System.out.println("MasterTerminalOperationProposalChecks PASSED");
    }

    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Terminal response changed"); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid terminal response replay admitted");
    }
}

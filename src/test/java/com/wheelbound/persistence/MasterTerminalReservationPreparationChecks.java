package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Dependency-free checks of aggregate balance binding and retained replay. */
public final class MasterTerminalReservationPreparationChecks
{
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid terminal reservation admitted");
    }

    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            var unresolved = MasterCreationAggregateChecks.fixture(count);
            var pending = unresolved.pendingMasterSelection().orElseThrow();
            var creation = unresolved.masterCreations().orElseThrow().receipts().get(pending.transactionId());
            var step = new MasterSelectionProvenance.Step(
                new DrawCommitment(pending.pool(), pending.pool().totalWeight() - 1), "terminal");
            var request = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5, creation, pending, step);
            rejects(() -> MasterTerminalReservationPreparation.prepare(unresolved, request));
            for (long balance : new long[] {1, 7, Long.MAX_VALUE})
            {
                var initial = new MasterSpinState(1, unresolved.lifecycle().identity(), 5, balance, Optional.empty());
                var head = MasterSpinAggregateChecks.attach(unresolved, initial);
                var proposal = MasterTerminalReservationPreparation.prepare(head, request);
                var repeated = MasterTerminalReservationPreparation.prepare(head, request);
                if (proposal.spins().allotted() != balance || proposal.spins().available() != balance - 1
                    || proposal.spins().revision() != 6 || proposal.allocation().fpDelta() != 0
                    || !proposal.resolutions().receipts().equals(Map.of(request.commandId(), request))
                    || repeated.spins().available() != proposal.spins().available()
                    || head.masterSpins().orElseThrow() != initial || initial.reservation().isPresent()
                    || head.pendingMasterSelection().orElseThrow() != pending)
                { throw new AssertionError("Preparation changed balance, prefix or original state"); }
                // Structural fixture only: publication of a real obligation remains unimplemented.
                var resolved = MasterResolutionAggregateChecks.copy(head,
                    new RunLifecycle(head.lifecycle().identity(), 6, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
                    Optional.empty(), Optional.of(proposal.resolutions()));
                rejects(() -> MasterTerminalReservationPreparation.prepare(resolved, request));
                var retained = MasterSpinAggregateChecks.attach(resolved, proposal.spins());
                var replay = MasterTerminalReservationPreparation.prepare(retained, request);
                var paused = LifecycleAdmission.prepare(retained, retained.lifecycle().identity(), UUID.randomUUID(),
                    6, RunReceipt.Operation.PAUSE);
                var pausedReplay = MasterTerminalReservationPreparation.prepare(paused, request);
                if (replay.spins() != proposal.spins() || replay.allocation() != proposal.allocation()
                    || pausedReplay.spins() != proposal.spins())
                { throw new AssertionError("Replay rebuilt original reservation"); }
                var changed = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5, creation, pending, step);
                rejects(() -> MasterTerminalReservationPreparation.prepare(retained, changed));
                var changedPayload = MasterSelectionResolutionReceipt.prepare(request.commandId(), 5,
                    creation, pending, new MasterSelectionProvenance.Step(step.draw(), "changed-cursor"));
                rejects(() -> MasterTerminalReservationPreparation.prepare(retained, changedPayload));
                var missingPending = MasterCreationAggregateChecks.copy(head, Optional.empty(), head.masterCreations());
                rejects(() -> MasterTerminalReservationPreparation.prepare(missingPending, request));
                var stale = MasterSpinAggregateChecks.attach(unresolved,
                    new MasterSpinState(1, initial.identity(), 4, balance, Optional.empty()));
                rejects(() -> MasterTerminalReservationPreparation.prepare(stale, request));
                var pausedPending = LifecycleAdmission.prepare(head, initial.identity(), UUID.randomUUID(),
                    5, RunReceipt.Operation.PAUSE);
                rejects(() -> MasterTerminalReservationPreparation.prepare(pausedPending, request));
                if (count > 0)
                {
                    var shortened = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5,
                        creation, creation.initial(), step);
                    rejects(() -> MasterTerminalReservationPreparation.prepare(head, shortened));
                }
            }
            var zero = MasterSpinAggregateChecks.attach(unresolved,
                new MasterSpinState(1, unresolved.lifecycle().identity(), 5, 0, Optional.empty()));
            rejects(() -> MasterTerminalReservationPreparation.prepare(zero, request));
        }
        System.out.println("MasterTerminalReservationPreparationChecks PASSED");
    }
}

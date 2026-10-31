package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

public final class MasterTerminalPreparationChecks
{
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid terminal accounting proposal admitted");
    }

    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            var head = MasterCreationAggregateChecks.fixture(count);
            var pending = head.pendingMasterSelection().get();
            var creation = head.masterCreations().get().receipts().get(pending.transactionId());
            var terminal = new MasterSelectionProvenance.Step(
                new DrawCommitment(pending.pool(), pending.pool().totalWeight() - 1), "terminal");
            var receipt = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5, creation, pending, terminal);
            for (long spins : new long[] {1, 7, Long.MAX_VALUE})
            {
                var result = MasterTerminalPreparation.prepare(head, spins, receipt);
                var repeated = MasterTerminalPreparation.prepare(head, spins, receipt);
                if (result.allocation().spinsBefore() != spins || result.allocation().spinsAfter() != spins - 1
                    || result.allocation().fpDelta() != 0 || result.allocation().resolution() != receipt
                    || !result.resolutions().receipts().equals(Map.of(receipt.commandId(), receipt))
                    || !repeated.resolutions().receipts().equals(result.resolutions().receipts()))
                { throw new AssertionError("Terminal proposal lost original accounting/history"); }
            }
            rejects(() -> MasterTerminalPreparation.prepare(head, 0, receipt));
            rejects(() -> MasterTerminalPreparation.prepare(head, -1, receipt));
            var empty = MasterResolutionAggregateChecks.copy(head, head.lifecycle(), Optional.of(pending),
                Optional.of(new MasterSelectionResolutions(head.lifecycle().identity(), 5, List.of())));
            MasterTerminalPreparation.prepare(empty, 1, receipt);
            var paused = LifecycleAdmission.prepare(head, head.lifecycle().identity(), UUID.randomUUID(),
                5, RunReceipt.Operation.PAUSE);
            rejects(() -> MasterTerminalPreparation.prepare(paused, 1, receipt));
            var missing = MasterCreationAggregateChecks.copy(head, Optional.empty(), head.masterCreations());
            rejects(() -> MasterTerminalPreparation.prepare(missing, 1, receipt));
            rejects(() -> MasterTerminalPreparation.prepare(head, 1,
                MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 6, creation, pending, terminal)));
            if (count > 0)
            {
                var shortened = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5,
                    creation, creation.initial(), terminal);
                rejects(() -> MasterTerminalPreparation.prepare(head, 1, shortened));
            }
            var retained = MasterResolutionAggregateChecks.copy(head,
                new RunLifecycle(head.lifecycle().identity(), 6, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
                Optional.empty(), Optional.of(new MasterSelectionResolutions(head.lifecycle().identity(), 6, List.of(receipt))));
            // A retained transcript is not a retained allocation: never reconstruct a debit on replay.
            rejects(() -> MasterTerminalPreparation.prepare(retained, 7, receipt));
            if (head.lifecycle().revision() != 5 || head.pendingMasterSelection().get() != pending
                || head.masterResolutions().isPresent() || !empty.masterResolutions().get().receipts().isEmpty())
            { throw new AssertionError("Preparation mutated source aggregate"); }
        }
        System.out.println("MasterTerminalPreparationChecks PASSED");
    }
}

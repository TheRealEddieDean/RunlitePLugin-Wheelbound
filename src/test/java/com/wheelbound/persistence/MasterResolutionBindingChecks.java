package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Offline structural checks, without storage or trusted-evidence claims. */
public final class MasterResolutionBindingChecks
{
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException expected) { return; }
        throw new AssertionError("Invalid terminal binding admitted");
    }

    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            RunDocument head = MasterCreationAggregateChecks.fixture(count);
            var id = head.lifecycle().identity();
            var pending = head.pendingMasterSelection().get();
            var creation = head.masterCreations().get().receipts().get(pending.transactionId());
            var history = new MasterSelectionResolutions(id, 5, List.of());
            var terminal = new MasterSelectionProvenance.Step(
                new DrawCommitment(pending.pool(), pending.pool().totalWeight() - 1), "terminal");
            var receipt = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5, creation, pending, terminal);
            MasterResolutionBinding.validateAppend(head, history, receipt);
            var sealed = new RunDocument(head.lifecycle(), head.catalogVersion(), List.of(), head.fatePoints(),
                head.inventory(), Optional.of(new SealedGrandFate(id, receipt.commandId(), GrandFatePool.atTicket(0))),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                head.pendingMasterSelection(), head.masterCreations());
            rejects(() -> MasterResolutionBinding.validateAppend(sealed, history, receipt));
            if (head.lifecycle().revision() != 5 || head.pendingMasterSelection().get() != pending
                || pending.drawCount() != count || !history.receipts().isEmpty())
            { throw new AssertionError("Binding mutated input"); }
            rejects(() -> MasterResolutionBinding.validateHistory(head,
                new MasterSelectionResolutions(id, 6, List.of(receipt))));
            rejects(() -> MasterResolutionBinding.validateAppend(head, history,
                MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 6, creation, pending, terminal)));
            rejects(() -> MasterResolutionBinding.validateAppend(head,
                new MasterSelectionResolutions(new RunIdentity("foreign", id.runId()), 5, List.of()), receipt));
            rejects(() -> MasterResolutionBinding.validateHistory(PendingLifecycleChecks.fixture(count), history));
            var paused = LifecycleAdmission.prepare(head, id, UUID.randomUUID(), 5, RunReceipt.Operation.PAUSE);
            rejects(() -> MasterResolutionBinding.validateAppend(paused, history,
                MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 6, creation, pending, terminal)));
            var resumed = LifecycleAdmission.prepare(paused, id, UUID.randomUUID(), 6, RunReceipt.Operation.RESUME);
            var colliding = MasterSelectionResolutionReceipt.prepare(paused.receipts().get(0).commandId(),
                7, creation, pending, terminal);
            rejects(() -> MasterResolutionBinding.validateAppend(resumed, history, colliding));
            var withoutPending = MasterCreationAggregateChecks.copy(head, Optional.empty(), head.masterCreations());
            rejects(() -> MasterResolutionBinding.validateAppend(withoutPending, history, receipt));
            if (count > 0)
            {
                var zero = creation.initial();
                var shorter = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5, creation, zero, terminal);
                rejects(() -> MasterResolutionBinding.validateAppend(head, history, shorter));
                var steps = new ArrayList<MasterSelectionProvenance.Step>();
                pending.landings().forEach(l -> steps.add(l.step()));
                steps.set(0, new MasterSelectionProvenance.Step(steps.get(0).draw(), "changed"));
                var changed = new MasterSelectionProvenance(pending.transactionId(), pending.snapshotVersion(),
                    pending.catalogVersion(), pending.streamDescriptor(), pending.initialStreamState(), pending.readiness(), steps);
                var rewritten = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5, creation, changed, terminal);
                rejects(() -> MasterResolutionBinding.validateAppend(head, history, rewritten));
            }
            var historicalHead = new RunDocument(new RunLifecycle(id, 6, RunLifecycle.State.ACTIVE,
                RunLifecycle.Obligation.NONE), head.catalogVersion(), List.of(), head.fatePoints(), head.inventory(),
                head.grandFate(), head.grandProgress(), head.grandJournal(), head.grandActivations(),
                Optional.empty(), Optional.empty(), head.masterCreations());
            var retained = new MasterSelectionResolutions(id, 6, List.of(receipt));
            MasterResolutionBinding.validateHistory(historicalHead, retained);
            var conflictingOperation = LifecycleAdmission.prepare(withoutPending, id, UUID.randomUUID(),
                5, RunReceipt.Operation.PAUSE);
            rejects(() -> MasterResolutionBinding.validateHistory(conflictingOperation, retained));
            rejects(() -> MasterResolutionBinding.validateHistory(withoutPending, retained));
        }
        System.out.println("MasterResolutionBindingChecks PASSED");
    }
}

package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Structural retention only; does not publish an assignment or debit. */
public final class MasterResolutionAggregateChecks
{
    static RunDocument copy(RunDocument source, RunLifecycle lifecycle,
        Optional<MasterSelectionProvenance> pending, Optional<MasterSelectionResolutions> history)
    {
        return new RunDocument(lifecycle, source.catalogVersion(), source.receipts(), source.fatePoints(),
            source.inventory(), source.grandFate(), source.grandProgress(), source.grandJournal(),
            source.grandActivations(), source.grandCurrentAttempt(), pending, source.masterCreations(), history);
    }

    static RunDocument resolved(int count)
    {
        RunDocument source = MasterCreationAggregateChecks.fixture(count);
        var pending = source.pendingMasterSelection().get();
        var original = source.masterCreations().get().receipts().get(pending.transactionId());
        var terminal = new MasterSelectionProvenance.Step(
            new DrawCommitment(pending.pool(), pending.pool().totalWeight() - 1), "terminal");
        var receipt = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5, original, pending, terminal);
        var next = new RunLifecycle(source.lifecycle().identity(), 6, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE);
        return copy(source, next, Optional.empty(), Optional.of(new MasterSelectionResolutions(
            next.identity(), 6, List.of(receipt))));
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid retained resolution admitted");
    }

    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            RunDocument head = resolved(count);
            var history = head.masterResolutions().get();
            var receipt = history.receipts().values().iterator().next();
            var id = head.lifecycle().identity();
            var paused = LifecycleAdmission.prepare(head, id, UUID.randomUUID(), 6, RunReceipt.Operation.PAUSE);
            var resumed = LifecycleAdmission.prepare(paused, id, UUID.randomUUID(), 7, RunReceipt.Operation.RESUME);
            if (!resumed.masterResolutions().get().receipts().equals(history.receipts())
                || head.lifecycle().revision() != 6 || receipt.resolved().drawCount() != count + 1
                || head.pendingMasterSelection().isPresent())
            { throw new AssertionError("History lost or source mutated"); }
            if (MasterCreationAdmission.prepare(resumed, receipt.creation()) != resumed)
            { throw new AssertionError("Creation replay replaced resolved head"); }
            rejects(() -> LifecycleAdmission.prepare(head, id, receipt.commandId(), 6, RunReceipt.Operation.PAUSE));
            var source = MasterCreationAggregateChecks.fixture(count);
            rejects(() -> copy(source, source.lifecycle(), Optional.empty(), Optional.of(history)));
            rejects(() -> copy(source, head.lifecycle(), source.pendingMasterSelection(), Optional.of(history)));
            rejects(() -> copy(head, head.lifecycle(), Optional.empty(), Optional.of(
                new MasterSelectionResolutions(new RunIdentity("foreign", id.runId()), 6, List.of()))));
            rejects(() -> new RunDocument(head.lifecycle(), head.catalogVersion(), List.of(),
                head.fatePoints(), head.inventory(), head.grandFate(), head.grandProgress(), head.grandJournal(),
                head.grandActivations(), head.grandCurrentAttempt(), Optional.empty(), Optional.empty(), Optional.of(history)));
            rejects(() -> new RunDocument(head.lifecycle(), head.catalogVersion(), List.of(), head.fatePoints(),
                head.inventory(), Optional.of(new SealedGrandFate(id, receipt.commandId(), GrandFatePool.atTicket(0))),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                head.masterCreations(), Optional.of(history)));
            var pending = receipt.creation().initial();
            var replacement = new MasterSelectionProvenance(receipt.commandId(), pending.snapshotVersion(),
                pending.catalogVersion(), pending.streamDescriptor(), pending.initialStreamState(), pending.readiness(), List.of());
            var reused = new MasterSelectionCreationReceipt(id, 6, replacement);
            rejects(() -> MasterCreationRecovery.find(head, reused));
            rejects(() -> MasterCreationAdmission.prepare(head, reused));
            var fresh = new MasterSelectionProvenance(UUID.randomUUID(), pending.snapshotVersion(),
                pending.catalogVersion(), pending.streamDescriptor(), pending.initialStreamState(), pending.readiness(), List.of());
            var created = MasterCreationAdmission.prepare(head, new MasterSelectionCreationReceipt(id, 6, fresh));
            if (!created.masterResolutions().get().receipts().equals(history.receipts()))
            { throw new AssertionError("New creation discarded terminal history"); }
        }
        var legacy = MasterCreationAggregateChecks.fixture(0);
        if (legacy.masterResolutions().isPresent()) { throw new AssertionError("Inferred legacy history"); }
        var empty = copy(legacy, legacy.lifecycle(), legacy.pendingMasterSelection(), Optional.of(
            new MasterSelectionResolutions(legacy.lifecycle().identity(), 5, List.of())));
        if (!LifecycleAdmission.prepare(empty, empty.lifecycle().identity(), UUID.randomUUID(), 5,
            RunReceipt.Operation.PAUSE).masterResolutions().get().receipts().isEmpty())
        { throw new AssertionError("Explicit empty history changed"); }
        System.out.println("MasterResolutionAggregateChecks PASSED");
    }
}

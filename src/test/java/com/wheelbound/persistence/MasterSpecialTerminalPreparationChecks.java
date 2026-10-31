package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Coupled proposal checks; no obligation publication or storage claims. */
public final class MasterSpecialTerminalPreparationChecks
{
    public static void main(String[] args)
    {
        for (var kind : new ActiveWheel.Kind[] {ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE})
        for (int count : new int[] {0, 1, 513})
        for (long balance : new long[] {1, 7, Long.MAX_VALUE})
        {
            var source = fixture(kind, count, balance);
            var pending = source.pendingMasterSelection().orElseThrow();
            var creation = source.masterCreations().orElseThrow().receipts().get(pending.transactionId());
            var request = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5, creation, pending,
                new MasterSelectionProvenance.Step(new DrawCommitment(pending.pool(), pending.pool().totalWeight() - 1), "terminal"));
            var prepared = MasterSpecialTerminalPreparation.prepare(source, request);
            check(prepared.obligation().resolution().equals(prepared.response().resolution()));
            check(prepared.obligation().requirement() == (kind == ActiveWheel.Kind.TAINT
                ? MasterSpecialObligation.Requirement.MANDATORY_PUNISHMENT
                : MasterSpecialObligation.Requirement.MANDATORY_SACRIFICE));
            check(prepared.resolutions().receipts().get(request.commandId()).equals(request));
            check(prepared.spins().reservation().orElseThrow().resolution().equals(request));
            check(prepared.response().allocation().spinsBefore() == balance
                && prepared.spins().available() == balance - 1);
            var history = MasterResolutionAggregateChecks.copy(source,
                new RunLifecycle(source.lifecycle().identity(), 6, RunLifecycle.State.ACTIVE,
                    RunLifecycle.Obligation.NONE), Optional.empty(), Optional.of(prepared.resolutions()));
            var retained = MasterSpinAggregateChecks.attach(history, prepared.spins());
            rejects(() -> MasterTaintLifecyclePreparation.prepare(retained, request));
            if (kind == ActiveWheel.Kind.TAINT)
            {
                var lifecycle = MasterTaintLifecyclePreparation.prepare(source, request);
                check(lifecycle.current().lifecycle().revision() == request.afterRevision()
                    && lifecycle.current().lifecycle().state() == RunLifecycle.State.ACTIVE
                    && lifecycle.current().lifecycle().obligation() == RunLifecycle.Obligation.PUNISHMENT_PENDING
                    && lifecycle.current().spins().available() == balance - 1
                    && lifecycle.terminal().response().equals(prepared.response()));
                var published = new RunDocument(lifecycle.current().lifecycle(), source.catalogVersion(),
                    List.of(), source.fatePoints(), source.inventory(), source.grandFate(), source.grandProgress(),
                    source.grandJournal(), source.grandActivations(), source.grandCurrentAttempt(), Optional.empty(),
                    source.masterCreations(), Optional.of(lifecycle.terminal().resolutions()),
                    Optional.of(lifecycle.terminal().spins()), Optional.of(lifecycle.current().obligation()));
                var specialPaused = LifecycleAdmission.prepare(published, published.lifecycle().identity(),
                    UUID.randomUUID(), 6, RunReceipt.Operation.PAUSE);
                check(MasterTaintLifecyclePreparation.prepare(published, request).current()
                    == published.currentMasterSpecial().orElseThrow());
                var exactReplay = MasterTaintLifecyclePreparation.prepare(specialPaused, request);
                check(exactReplay.current() == specialPaused.currentMasterSpecial().orElseThrow()
                    && exactReplay.current().lifecycle().state() == RunLifecycle.State.PAUSED
                    && exactReplay.current().lifecycle().revision() == 7
                    && exactReplay.terminal().response().equals(prepared.response()));
                rejects(() -> MasterTaintLifecyclePreparation.prepare(source,
                    MasterSelectionResolutionReceipt.prepare(request.commandId(), 4, creation, pending,
                        new MasterSelectionProvenance.Step(new DrawCommitment(pending.pool(),
                            pending.pool().totalWeight() - 1), "terminal"))));
            }
            else { rejects(() -> MasterTaintLifecyclePreparation.prepare(source, request)); }
            var paused = LifecycleAdmission.prepare(retained, retained.lifecycle().identity(),
                UUID.randomUUID(), 6, RunReceipt.Operation.PAUSE);
            var replay = MasterSpecialTerminalPreparation.prepare(paused, request);
            check(replay.response().equals(prepared.response()) && replay.obligation().equals(prepared.obligation())
                && replay.spins() == prepared.spins());
            var changed = MasterSelectionResolutionReceipt.prepare(request.commandId(), 5, creation, pending,
                new MasterSelectionProvenance.Step(new DrawCommitment(pending.pool(), pending.pool().totalWeight() - 1), "changed"));
            rejects(() -> MasterSpecialTerminalPreparation.prepare(retained, changed));
            rejects(() -> MasterTaintLifecyclePreparation.prepare(retained, changed));
            var ordinary = MasterSelectionResolutionReceipt.prepare(UUID.randomUUID(), 5, creation, pending,
                new MasterSelectionProvenance.Step(new DrawCommitment(pending.pool(), 16), "ordinary"));
            rejects(() -> MasterSpecialTerminalPreparation.prepare(source, ordinary));
            rejects(() -> MasterTaintLifecyclePreparation.prepare(source, ordinary));
            rejects(() -> MasterSpecialTerminalPreparation.prepare(fixture(kind, count, 0), request));
            rejects(() -> MasterTaintLifecyclePreparation.prepare(fixture(kind, count, 0), request));
            var stopped = LifecycleAdmission.prepare(source, source.lifecycle().identity(), UUID.randomUUID(),
                5, RunReceipt.Operation.PAUSE);
            rejects(() -> MasterSpecialTerminalPreparation.prepare(stopped, request));
            rejects(() -> MasterTaintLifecyclePreparation.prepare(stopped, request));
            check(source.pendingMasterSelection().orElseThrow() == pending
                && source.masterSpins().orElseThrow().reservation().isEmpty()
                && source.masterResolutions().isEmpty() && source.lifecycle().revision() == 5);
        }
        System.out.println("MasterSpecialTerminalPreparationChecks PASSED");
    }

    private static RunDocument fixture(ActiveWheel.Kind kind, int count, long balance)
    {
        var slices = new ArrayList<ActiveWheel.Slice>();
        var evidence = new HashMap<UUID, MasterSelectionReadiness.Classification>();
        for (int i = 1; i <= 6; i++)
        {
            var id = new UUID(0, i);
            slices.add(i == 6 ? ActiveWheel.Slice.special(id, kind, 7)
                : ActiveWheel.Slice.ordinary(id, "activity" + i, ActiveWheel.Enhancement.BASE));
            evidence.put(id, new MasterSelectionReadiness.Classification(i < 5
                ? MasterSelectionReadiness.Availability.VERIFIED_EMPTY
                : MasterSelectionReadiness.Availability.ASSIGNABLE, "fixture"));
        }
        var id = new RunIdentity("offline", new UUID(0, 99));
        var zero = new MasterSelectionProvenance(new UUID(0, 100), "snapshot", "catalog", "stream", "initial",
            new MasterSelectionReadiness(new ActiveWheel(slices), evidence), List.of());
        var pending = zero;
        for (int i = 0; i < count; i++)
        { pending = pending.append(pending.transactionId(), pending.drawCount(), pending.currentStreamState(),
            new MasterSelectionProvenance.Step(new DrawCommitment(pending.pool(), 0), "retry" + i)); }
        var creation = new MasterSelectionCreationReceipt(id, 3, zero);
        return new RunDocument(new RunLifecycle(id, 5, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
            "catalog", List.of(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(pending),
            Optional.of(new MasterSelectionCreationJournal(id, 5, List.of(creation))), Optional.empty(),
            Optional.of(new MasterSpinState(1, id, 5, balance, Optional.empty())));
    }

    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Coupled special proposal changed"); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid special proposal accepted");
    }
}

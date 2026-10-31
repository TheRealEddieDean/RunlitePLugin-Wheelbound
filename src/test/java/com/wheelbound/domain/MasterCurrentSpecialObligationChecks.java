package com.wheelbound.domain;

import java.util.*;

/** Offline binding checks, not evidence of aggregate publication or supported special routes. */
public final class MasterCurrentSpecialObligationChecks
{
    public static void main(String[] args)
    {
        for (var kind : new ActiveWheel.Kind[] {ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE})
        for (int retries : new int[] {0, 1, 513})
        for (long balance : new long[] {1, 7, Long.MAX_VALUE})
        {
            var identity = new RunIdentity("offline", UUID.randomUUID());
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
            var zero = new MasterSelectionProvenance(UUID.randomUUID(), "snapshot", "catalog", "stream",
                "initial", new MasterSelectionReadiness(new ActiveWheel(slices), evidence), List.of());
            var creation = new MasterSelectionCreationReceipt(identity, 2, zero);
            var pending = zero;
            for (int i = 0; i < retries; i++) { pending = append(pending, 0, "retry" + i); }
            var receipt = new MasterSelectionResolutionReceipt(UUID.randomUUID(), 9, creation,
                append(pending, pending.pool().totalWeight() - 1, "terminal"));
            var obligation = new MasterSpecialObligation(receipt);
            var history = new MasterSelectionResolutions(identity, 10, List.of(receipt));
            var allocation = MasterSpinAllocation.restore(receipt, balance);
            var spins = new MasterSpinState(1, identity, 10, balance, Optional.of(allocation));
            var changedReceipt = new MasterSelectionResolutionReceipt(receipt.commandId(), 9, creation,
                append(pending, pending.pool().totalWeight() - 1, "changed"));
            var changedSpins = new MasterSpinState(1, identity, 10, balance,
                Optional.of(MasterSpinAllocation.restore(changedReceipt, balance)));
            var laterZero = new MasterSelectionProvenance(UUID.randomUUID(), "snapshot", "catalog", "stream",
                "initial", zero.readiness(), List.of());
            var laterCreation = new MasterSelectionCreationReceipt(identity, 11, laterZero);
            var laterReceipt = new MasterSelectionResolutionReceipt(UUID.randomUUID(), 13, laterCreation,
                append(laterZero, laterZero.pool().totalWeight() - 1, "later"));
            var laterHistory = new MasterSelectionResolutions(identity, 14, List.of(receipt, laterReceipt));
            for (var stage : RunLifecycle.Obligation.values())
            {
                boolean allowed = kind == ActiveWheel.Kind.TAINT
                    ? stage == RunLifecycle.Obligation.PUNISHMENT_PENDING
                    : stage.name().startsWith("SACRIFICE_");
                var head = new RunLifecycle(identity, 10, RunLifecycle.State.ACTIVE, stage);
                if (!allowed)
                {
                    rejects(() -> MasterCurrentSpecialObligation.restore(obligation, history, spins, head));
                    continue;
                }
                var current = MasterCurrentSpecialObligation.restore(obligation, history, spins, head);
                com.wheelbound.persistence.MasterCurrentSpecialAggregateChecks.verify(current, history);
                var paused = current.pause(identity, 10);
                var restored = MasterCurrentSpecialObligation.restore(obligation, history, spins, paused.lifecycle());
                var resumed = restored.resume(identity, 11);
                check(resumed.lifecycle().revision() == 12 && resumed.lifecycle().obligation() == stage);
                check(resumed.obligation() == obligation && resumed.spins() == spins
                    && spins.allotted() == balance && spins.available() == balance - 1);
                check(!paused.lifecycle().acceptsObservation(identity, 11));
                rejects(() -> current.pause(identity, 9));
                rejects(() -> current.pause(new RunIdentity("foreign", UUID.randomUUID()), 10));
                rejects(() -> MasterCurrentSpecialObligation.restore(obligation,
                    new MasterSelectionResolutions(identity, 10, List.of()), spins, head));
                rejects(() -> MasterCurrentSpecialObligation.restore(obligation, history,
                    new MasterSpinState(1, identity, 10, balance, Optional.empty()), head));
                rejects(() -> MasterCurrentSpecialObligation.restore(obligation, history,
                    new MasterSpinState(1, identity, 11, balance, Optional.of(allocation)), head));
                rejects(() -> MasterCurrentSpecialObligation.restore(obligation, history, changedSpins, head));
                rejects(() -> MasterCurrentSpecialObligation.restore(new MasterSpecialObligation(changedReceipt),
                    history, spins, head));
                rejects(() -> MasterCurrentSpecialObligation.restore(obligation, laterHistory, spins,
                    new RunLifecycle(identity, 14, RunLifecycle.State.ACTIVE, stage)));
                var changed = new MasterSpecialObligation(new MasterSelectionResolutionReceipt(
                    UUID.randomUUID(), 9, creation, receipt.resolved()));
                rejects(() -> MasterCurrentSpecialObligation.restore(changed, history, spins, head));
                for (var state : new RunLifecycle.State[] {RunLifecycle.State.NOT_STARTED,
                    RunLifecycle.State.ABANDONED, RunLifecycle.State.COMPLETED})
                { rejects(() -> MasterCurrentSpecialObligation.restore(obligation, history, spins,
                    new RunLifecycle(identity, 10, state, stage))); }
                rejects(() -> MasterCurrentSpecialObligation.restore(obligation, history, spins,
                    new RunLifecycle(identity, 9, RunLifecycle.State.ACTIVE, stage)));
                check(current.lifecycle() == head && head.revision() == 10);
            }
        }
        System.out.println("MasterCurrentSpecialObligationChecks PASSED");
    }

    private static MasterSelectionProvenance append(MasterSelectionProvenance prefix, int ticket, String state)
    { return prefix.append(prefix.transactionId(), prefix.drawCount(), prefix.currentStreamState(),
        new MasterSelectionProvenance.Step(new DrawCommitment(prefix.pool(), ticket), state)); }
    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Current special binding changed"); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid current special accepted");
    }
}

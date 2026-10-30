package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

/** Dependency-free fixture also invoked by JUnit. */
public final class GrandActivationEnvelopeChecks
{
    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Invalid activation envelope"); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Unsupported envelope accepted");
    }

    public static void main(String[] args)
    {
        RunIdentity identity = new RunIdentity("activation-envelope", UUID.randomUUID());
        for (int ticket = 0; ticket < 5; ticket++)
        {
            GrandFateChecklist checklist = GrandFateChecklist.initial(new SealedGrandFate(
                identity, UUID.randomUUID(), GrandFatePool.atTicket(ticket)));
            for (GrandFateChecklist.Objective objective : checklist.required())
            {
                UUID command = UUID.randomUUID();
                GrandActivationReceipt original = new GrandActivationReceipt(command, identity,
                    Long.MAX_VALUE - 1, checklist, objective);
                RunReceipt envelope = RunReceipt.grandActivation(original);
                RunReceipt copy = RunReceipt.grandActivation(new GrandActivationReceipt(command,
                    identity, Long.MAX_VALUE - 1, checklist, objective));
                check(envelope.equals(copy) && copy.equals(envelope));
                check(envelope.hashCode() == copy.hashCode());
                check(envelope.activation() == original && envelope.commandId().equals(command));
                check(envelope.operation() == RunReceipt.Operation.GRAND_ACTIVATION);
                check(envelope.beforeRevision() == Long.MAX_VALUE - 1
                    && envelope.afterRevision() == Long.MAX_VALUE);
                check(envelope.state() == RunLifecycle.State.ACTIVE
                    && envelope.obligation() == RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
                check(envelope.debit() == null && envelope.purchase() == null && envelope.draw() == null);
                check(!envelope.equals(RunReceipt.grandActivation(new GrandActivationReceipt(
                    UUID.randomUUID(), identity, Long.MAX_VALUE - 1, checklist, objective))));
                for (GrandFateChecklist.Objective other : checklist.required())
                {
                    if (other == objective) { continue; }
                    check(!envelope.equals(RunReceipt.grandActivation(new GrandActivationReceipt(
                        command, identity, Long.MAX_VALUE - 1, checklist, other))));
                }
                rejects(() -> new RunReceipt(command, RunReceipt.Operation.GRAND_ACTIVATION,
                    0, 1, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE));
                rejects(() -> new RunDocument(original.attempt().lifecycle(), "test", List.of(envelope)));
                GrandActivationHistory history = new GrandActivationHistory(checklist.seal(),
                    Long.MAX_VALUE, List.of(original));
                RunDocument aggregate = aggregate(original.attempt().lifecycle(), history, List.of(envelope));
                check(GrandActivationAdmission.propose(aggregate, original, identity, Long.MAX_VALUE)
                    .isReplay());
                check(aggregate.grandCurrentAttempt().isEmpty());
                RunLifecycle pausedHead = new RunLifecycle(identity, Long.MAX_VALUE,
                    RunLifecycle.State.PAUSED, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
                RunReceipt missing = new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.PAUSE,
                    Long.MAX_VALUE - 1, Long.MAX_VALUE, RunLifecycle.State.PAUSED,
                    RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
                rejects(() -> aggregate(pausedHead, history, List.of(missing)));
                RunReceipt resume = new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.RESUME,
                    Long.MAX_VALUE - 2, Long.MAX_VALUE - 1, RunLifecycle.State.ACTIVE,
                    RunLifecycle.Obligation.NONE);
                aggregate(original.attempt().lifecycle(), history, List.of(resume, envelope));
                RunReceipt blocked = new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.RESUME,
                    Long.MAX_VALUE - 2, Long.MAX_VALUE - 1, RunLifecycle.State.ACTIVE,
                    RunLifecycle.Obligation.PUNISHMENT_PENDING);
                rejects(() -> aggregate(original.attempt().lifecycle(), history, List.of(blocked, envelope)));
                GrandActivationReceipt changed = new GrandActivationReceipt(UUID.randomUUID(), identity,
                    Long.MAX_VALUE - 1, checklist, objective);
                rejects(() -> aggregate(original.attempt().lifecycle(), history,
                    List.of(RunReceipt.grandActivation(changed))));
                rejects(() -> aggregate(original.attempt().lifecycle(),
                    new GrandActivationHistory(checklist.seal(), Long.MAX_VALUE, List.of(changed)),
                    List.of(envelope)));
            }
        }
        rejects(() -> RunReceipt.grandActivation(null));
        RunReceipt pause = new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.PAUSE,
            0, 1, RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
        check(pause.activation() == null && pause.equals(pause));
        System.out.println("Grand activation envelope checks passed");
    }

    private static RunDocument aggregate(RunLifecycle lifecycle, GrandActivationHistory history,
        List<RunReceipt> operations)
    {
        return new RunDocument(lifecycle, "test", operations, Optional.empty(), Optional.empty(),
            Optional.of(history.seal()), Optional.empty(), Optional.empty(), Optional.of(history));
    }
}

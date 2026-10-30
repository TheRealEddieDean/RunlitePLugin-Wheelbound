package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.List;
import java.util.UUID;

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
            }
        }
        rejects(() -> RunReceipt.grandActivation(null));
        RunReceipt pause = new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.PAUSE,
            0, 1, RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
        check(pause.activation() == null && pause.equals(pause));
        System.out.println("Grand activation envelope checks passed");
    }
}

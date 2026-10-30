package com.wheelbound.domain;

import java.util.Collections;
import java.util.UUID;

public final class GrandActivationReceiptChecks
{
    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Activation receipt contract violated"); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException | ArithmeticException expected) { return; }
        throw new AssertionError("Invalid activation receipt accepted");
    }

    public static void main(String[] args)
    {
        RunIdentity identity = new RunIdentity("activation-receipt", UUID.randomUUID());
        UUID command = UUID.randomUUID();
        for (int ticket = 0; ticket < 5; ticket++)
        {
            GrandFateChecklist checklist = GrandFateChecklist.initial(new SealedGrandFate(identity,
                UUID.randomUUID(), GrandFatePool.atTicket(ticket)));
            for (GrandFateChecklist.Objective objective : checklist.required())
            {
                RunLifecycle ready = new RunLifecycle(identity, 12, RunLifecycle.State.ACTIVE,
                    RunLifecycle.Obligation.NONE);
                GrandActivationReceipt receipt = GrandActivationReceipt.propose(command, ready,
                    checklist, objective, identity, 12);
                GrandActivationReceipt restored = new GrandActivationReceipt(command, identity, 12,
                    GrandFateChecklist.restore(checklist.seal(), checklist.completed()), objective);
                check(receipt.equals(restored) && restored.equals(receipt));
                check(receipt.hashCode() == restored.hashCode());
                check(receipt.beforeRevision() == 12 && receipt.afterRevision() == 13);
                check(restored.attempt().objective() == objective);
                check(restored.attempt().endUnsuccessful(restored.attempt().lifecycle(), identity, 13)
                    .obligation() == RunLifecycle.Obligation.NONE);
                check(!receipt.equals(new GrandActivationReceipt(UUID.randomUUID(), identity, 12, checklist, objective)));
                check(!receipt.equals(new GrandActivationReceipt(command, identity, 13, checklist, objective)));
                GrandFateChecklist completed = GrandFateChecklist.restore(checklist.seal(), Collections.singleton(objective));
                rejects(() -> new GrandActivationReceipt(command, identity, 12, completed, objective));
                rejects(() -> new GrandActivationReceipt(command, identity, -1, checklist, objective));
                rejects(() -> new GrandActivationReceipt(command, identity, Long.MAX_VALUE, checklist, objective));
                rejects(() -> new GrandActivationReceipt(null, identity, 12, checklist, objective));
                rejects(() -> new GrandActivationReceipt(command,
                    new RunIdentity("foreign", identity.runId()), 12, checklist, objective));
                rejects(() -> GrandActivationReceipt.propose(command, ready, checklist, objective, identity, 11));
                for (RunLifecycle.State state : RunLifecycle.State.values())
                {
                    for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
                    {
                        if (state == RunLifecycle.State.ACTIVE && obligation == RunLifecycle.Obligation.NONE) { continue; }
                        rejects(() -> GrandActivationReceipt.propose(command,
                            new RunLifecycle(identity, 12, state, obligation), checklist, objective, identity, 12));
                    }
                }
                for (GrandFateChecklist.Objective different : checklist.unfinished())
                {
                    if (different == objective) { continue; }
                    check(!receipt.equals(new GrandActivationReceipt(command, identity, 12, checklist, different)));
                    check(!receipt.equals(new GrandActivationReceipt(command, identity, 12,
                        GrandFateChecklist.restore(checklist.seal(), Collections.singleton(different)), objective)));
                }
                GrandActivationReceipt last = new GrandActivationReceipt(command, identity,
                    Long.MAX_VALUE - 1, checklist, objective);
                check(last.afterRevision() == Long.MAX_VALUE);
            }
        }
        System.out.println("Grand activation receipt checks passed");
    }
}

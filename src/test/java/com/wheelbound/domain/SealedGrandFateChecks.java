package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Focused checks shared with JUnit; no file durability or onboarding claim. */
public final class SealedGrandFateChecks
{
    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Sealed evidence changed or mismatched"); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Expected invalid evidence rejection");
    }

    public static void main(String[] args)
    {
        UUID run = UUID.randomUUID();
        UUID command = UUID.randomUUID();
        RunIdentity identity = new RunIdentity("account-a", run);
        for (int ticket = 0; ticket < 5; ticket++)
        {
            DrawCommitment draw = GrandFatePool.atTicket(ticket);
            SealedGrandFate original = new SealedGrandFate(identity, command, draw);
            DrawCommitment restoredDraw = new DrawCommitment(new WeightedPool(draw.pool().version(),
                new ArrayList<>(draw.pool().entries())), draw.ticket());
            SealedGrandFate restored = new SealedGrandFate(new RunIdentity("account-a", run),
                command, restoredDraw);
            check(original.equals(restored) && restored.equals(original));
            check(original.hashCode() == restored.hashCode());
            check(original.goal() == GrandFatePool.goal(draw));
            check(restored.matches(identity, command, draw));
            check(!restored.matches(new RunIdentity("account-b", run), command, draw));
            check(!restored.matches(new RunIdentity("account-a", UUID.randomUUID()), command, draw));
            check(!restored.matches(identity, UUID.randomUUID(), draw));
            check(!restored.matches(identity, command, GrandFatePool.atTicket((ticket + 1) % 5)));
            check(!original.equals(new SealedGrandFate(identity, command,
                GrandFatePool.atTicket((ticket + 1) % 5))));
        }
        rejects(() -> new SealedGrandFate(null, command, GrandFatePool.atTicket(0)));
        rejects(() -> new SealedGrandFate(identity, null, GrandFatePool.atTicket(0)));
        rejects(() -> new SealedGrandFate(identity, command, null));
        rejects(() -> new SealedGrandFate(identity, command, new DrawCommitment(
            new WeightedPool("foreign", GrandFatePool.snapshot().entries()), 0)));
        rejects(() -> new SealedGrandFate(identity, command, new DrawCommitment(
            new WeightedPool(GrandFatePool.VERSION, List.of(
                new WeightedPool.Entry(GrandFatePool.Goal.INFERNO.id(), 1))), 0)));
        System.out.println("Sealed Grand Fate checks passed");
    }
}

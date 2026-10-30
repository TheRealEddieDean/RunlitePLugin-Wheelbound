package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Exhaustive ticket checks runnable without the JUnit/build dependencies. */
public final class GrandFatePoolChecks
{
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected rejection");
    }

    public static void main(String[] args)
    {
        WeightedPool pool = GrandFatePool.snapshot();
        if (pool.totalWeight() != 5 || pool.entries().size() != 5)
        { throw new AssertionError("Five equal outcomes required"); }
        Set<GrandFatePool.Goal> seen = new HashSet<>();
        for (int ticket = 0; ticket < 5; ticket++)
        {
            if (pool.entries().get(ticket).weight() != 1) { throw new AssertionError("Unequal odds"); }
            DrawCommitment draw = GrandFatePool.atTicket(ticket);
            GrandFatePool.Goal goal = GrandFatePool.goal(draw);
            if (!seen.add(goal) || !goal.id().equals(draw.selectedId()))
            { throw new AssertionError("Repeated or mismatched goal"); }
            WeightedPool restoredPool = new WeightedPool(pool.version(), new ArrayList<>(pool.entries()));
            DrawCommitment restored = new DrawCommitment(restoredPool, ticket);
            if (!restored.equals(draw) || GrandFatePool.goal(restored) != goal)
            { throw new AssertionError("Restore changed outcome"); }
        }
        if (!seen.equals(Set.of(GrandFatePool.Goal.INFERNO, GrandFatePool.Goal.FORTIS_COLOSSEUM,
            GrandFatePool.Goal.RADIANT_OATHPLATE, GrandFatePool.Goal.AWAKENED_DT2,
            GrandFatePool.Goal.MASTER_HILT_5))) { throw new AssertionError("Wrong goals"); }
        rejects(() -> GrandFatePool.atTicket(-1));
        rejects(() -> GrandFatePool.atTicket(5));
        rejects(() -> GrandFatePool.goal(new DrawCommitment(
            new WeightedPool("foreign", pool.entries()), 0)));
        List<WeightedPool.Entry> changed = new ArrayList<>(pool.entries());
        changed.set(4, new WeightedPool.Entry("grand.master_hilt_4", 1));
        rejects(() -> GrandFatePool.goal(new DrawCommitment(new WeightedPool(pool.version(), changed), 0)));
        changed.set(4, new WeightedPool.Entry(GrandFatePool.Goal.MASTER_HILT_5.id(), 2));
        rejects(() -> GrandFatePool.goal(new DrawCommitment(new WeightedPool(pool.version(), changed), 0)));
        rejects(() -> GrandFatePool.goal(new DrawCommitment(
            new WeightedPool(pool.version(), pool.entries().subList(0, 4)), 0)));
        try { pool.entries().clear(); throw new AssertionError("Mutable pool"); }
        catch (UnsupportedOperationException expected) { }
        System.out.println("Grand Fate pool checks passed");
    }
}

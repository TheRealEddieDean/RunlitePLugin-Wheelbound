package com.wheelbound.domain;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class GrandFateChecklistChecks
{
    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Grand checklist contract violated"); } }

    private static void rejects(Runnable action, Class<? extends RuntimeException> type)
    {
        try { action.run(); }
        catch (RuntimeException error)
        { if (type.isInstance(error)) { return; } throw error; }
        throw new AssertionError("Expected " + type.getSimpleName());
    }

    public static void main(String[] args)
    {
        RunIdentity identity = new RunIdentity("checklist-account", UUID.randomUUID());
        Set<GrandFateChecklist.Objective> all = EnumSet.noneOf(GrandFateChecklist.Objective.class);
        for (int ticket = 0; ticket < 5; ticket++)
        {
            SealedGrandFate seal = new SealedGrandFate(identity, UUID.randomUUID(),
                GrandFatePool.atTicket(ticket));
            GrandFateChecklist initial = GrandFateChecklist.initial(seal);
            check(initial.seal().equals(seal));
            check(initial.completed().isEmpty() && !initial.isComplete());
            check(initial.required().equals(initial.unfinished()));
            check(initial.required().size() == (seal.goal() == GrandFatePool.Goal.AWAKENED_DT2 ? 4 : 1));
            check(!initial.isUnfinished(null));
            Set<GrandFateChecklist.Objective> progress = new HashSet<>();
            for (GrandFateChecklist.Objective objective : initial.required())
            {
                check(all.add(objective));
                check(initial.isUnfinished(objective));
                progress.add(objective);
                GrandFateChecklist restored = GrandFateChecklist.restore(seal, progress);
                check(restored.completed().equals(progress));
                check(!restored.isUnfinished(objective));
                check(restored.unfinished().size() == initial.required().size() - progress.size());
                check(restored.isComplete() == (progress.size() == initial.required().size()));
                check(initial.completed().isEmpty());
                rejects(() -> restored.completed().clear(), UnsupportedOperationException.class);
                rejects(() -> restored.required().clear(), UnsupportedOperationException.class);
                rejects(() -> restored.unfinished().clear(), UnsupportedOperationException.class);
            }
            GrandFateChecklist full = GrandFateChecklist.restore(seal, progress);
            progress.clear();
            check(full.isComplete() && !full.completed().isEmpty());
            for (GrandFateChecklist.Objective foreign : GrandFateChecklist.Objective.values())
            {
                if (!initial.required().contains(foreign))
                {
                    check(!initial.isUnfinished(foreign));
                    rejects(() -> GrandFateChecklist.restore(seal, EnumSet.of(foreign)),
                        IllegalArgumentException.class);
                }
            }
            Set<GrandFateChecklist.Objective> malformed = new HashSet<>();
            malformed.add(null);
            rejects(() -> GrandFateChecklist.restore(seal, malformed), NullPointerException.class);
            rejects(() -> GrandFateChecklist.restore(seal, null), NullPointerException.class);
            if (seal.goal() == GrandFatePool.Goal.MASTER_HILT_5)
            { check(initial.required().equals(EnumSet.of(GrandFateChecklist.Objective.GHOMMALS_HILT_5))); }
            if (seal.goal() == GrandFatePool.Goal.RADIANT_OATHPLATE)
            { check(initial.required().equals(EnumSet.of(GrandFateChecklist.Objective.RADIANT_OATHPLATE_FULL_SET))); }
        }
        check(all.size() == GrandFateChecklist.Objective.values().length);
        rejects(() -> GrandFateChecklist.initial(null), NullPointerException.class);
        System.out.println("Grand Fate checklist checks passed");
    }
}

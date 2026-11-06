package com.wheelbound.domain;

import java.util.List;

/** Synthetic transition inputs only; does not prove live quest mapping or freshness. */
public final class QuestFateCompletionChecks
{
    public static void main(String[] args)
    {
        QuestFateOffers offers = new QuestFateOffers(QuestFateOffers.Capacity.THREE,
            "v1", List.of("parent", "subquest", "other"));
        QuestFateOffers.Selection selected = offers.select("parent");
        for (QuestFateCompletion.QuestState before : QuestFateCompletion.QuestState.values())
        {
            QuestFateCompletion evidence = new QuestFateCompletion(selected, before);
            QuestFateCompletion restored = new QuestFateCompletion(offers.select("parent"), before);
            check(evidence.equals(restored) && evidence.hashCode() == restored.hashCode());
            check(evidence.selection().equals(selected) && evidence.baseline() == before);
            for (RunLifecycle.State run : RunLifecycle.State.values())
            {
                for (QuestFateCompletion.QuestState after : QuestFateCompletion.QuestState.values())
                {
                    boolean qualifies = run == RunLifecycle.State.ACTIVE
                        && before != QuestFateCompletion.QuestState.COMPLETE
                        && after == QuestFateCompletion.QuestState.COMPLETE;
                    Runnable validate = () -> evidence.requireFreshCompletion(selected, run, "parent", after);
                    if (qualifies) { validate.run(); }
                    else { invalid(validate); }
                    check(evidence.equals(restored));
                }
            }
        }
        QuestFateCompletion evidence = new QuestFateCompletion(selected,
            QuestFateCompletion.QuestState.IN_PROGRESS);
        QuestFateCompletion.QuestState complete = QuestFateCompletion.QuestState.COMPLETE;
        invalid(() -> evidence.requireFreshCompletion(offers.select("subquest"), RunLifecycle.State.ACTIVE, "parent", complete));
        invalid(() -> evidence.requireFreshCompletion(selected, RunLifecycle.State.ACTIVE, "subquest", complete));
        for (QuestFateOffers foreign : List.of(
            new QuestFateOffers(QuestFateOffers.Capacity.THREE, "v2", offers.questIds()),
            new QuestFateOffers(QuestFateOffers.Capacity.THREE, "v1", List.of("parent", "other", "subquest"))))
        {
            invalid(() -> evidence.requireFreshCompletion(foreign.select("parent"), RunLifecycle.State.ACTIVE, "parent", complete));
        }
        for (String bad : new String[] { null, "", " parent", "parent " })
        { invalid(() -> evidence.requireFreshCompletion(selected, RunLifecycle.State.ACTIVE, bad, complete)); }
        invalid(() -> new QuestFateCompletion(null, complete));
        invalid(() -> new QuestFateCompletion(selected, null));
        invalid(() -> evidence.requireFreshCompletion(null, RunLifecycle.State.ACTIVE, "parent", complete));
        invalid(() -> evidence.requireFreshCompletion(selected, null, "parent", complete));
        invalid(() -> evidence.requireFreshCompletion(selected, RunLifecycle.State.ACTIVE, "parent", null));
        // Resume/login with an already complete baseline cannot retrospectively qualify.
        invalid(() -> new QuestFateCompletion(selected, complete)
            .requireFreshCompletion(selected, RunLifecycle.State.ACTIVE, "parent", complete));
        System.out.println("QuestFateCompletionChecks PASSED");
    }

    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void invalid(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException | NullPointerException expected) { return; }
        throw new AssertionError("Expected refusal");
    }
}

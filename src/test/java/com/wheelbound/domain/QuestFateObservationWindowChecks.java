package com.wheelbound.domain;

import java.util.List;
import java.util.UUID;

/** Offline context fixtures; IDs do not authenticate live observation coverage. */
public final class QuestFateObservationWindowChecks
{
    private static final RunIdentity RUN = new RunIdentity("account", new UUID(0, 1));
    private static final UUID ASSIGNMENT = new UUID(0, 2);
    private static final UUID SESSION = new UUID(0, 3);
    private static final UUID WINDOW = new UUID(0, 4);
    private static final UUID OTHER = new UUID(0, 5);
    private static final QuestFateCompletion.QuestState COMPLETE = QuestFateCompletion.QuestState.COMPLETE;

    public static void main(String[] args)
    {
        QuestFateSelection selected = selection("v1", "route", "parent");
        RunLifecycle active = lifecycle(RUN, 7, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        QuestFateObservationWindow value = window(active, selected, 10, QuestFateCompletion.QuestState.IN_PROGRESS);
        QuestFateObservationWindow restored = window(active, selection("v1", "route", "parent"), 10,
            QuestFateCompletion.QuestState.IN_PROGRESS);
        check(value.equals(restored) && restored.equals(value) && value.hashCode() == restored.hashCode());
        check(value.identity().equals(RUN) && value.baselineRevision() == 7
            && value.assignmentId().equals(ASSIGNMENT) && value.sessionId().equals(SESSION)
            && value.windowId().equals(WINDOW) && value.baselinePosition() == 10
            && value.selection().equals(selected) && value.baseline() == QuestFateCompletion.QuestState.IN_PROGRESS);
        for (long revision : new long[] {7, 8, Long.MAX_VALUE})
        {
            RunLifecycle current = lifecycle(RUN, revision, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
            for (long position : new long[] {11, Long.MAX_VALUE})
            {
                validate(value, current, revision, ASSIGNMENT, SESSION, WINDOW, selected, position);
                validate(value, current, revision, ASSIGNMENT, SESSION, WINDOW, selected, position);
            }
        }
        for (RunLifecycle.State state : RunLifecycle.State.values())
        {
            for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
            {
                if (state == RunLifecycle.State.ACTIVE && obligation == RunLifecycle.Obligation.ACTIVE_OBJECTIVE) { continue; }
                RunLifecycle inactive = lifecycle(RUN, 7, state, obligation);
                rejects(() -> window(inactive, selected, 10, COMPLETE));
                rejects(() -> validate(value, inactive, 7, ASSIGNMENT, SESSION, WINDOW, selected, 11));
            }
        }
        for (RunIdentity foreign : List.of(new RunIdentity("other", RUN.runId()), new RunIdentity("account", OTHER)))
        { rejects(() -> validate(value, lifecycle(foreign, 7, active.state(), active.obligation()), 7,
            ASSIGNMENT, SESSION, WINDOW, selected, 11)); }
        rejects(() -> validate(value, active, 8, ASSIGNMENT, SESSION, WINDOW, selected, 11));
        rejects(() -> validate(value, lifecycle(RUN, 6, active.state(), active.obligation()), 6,
            ASSIGNMENT, SESSION, WINDOW, selected, 11));
        // Same login session is insufficient after a pause or lost observation coverage.
        rejects(() -> validate(value, active, 7, ASSIGNMENT, SESSION, OTHER, selected, 11));
        rejects(() -> validate(value, active, 7, ASSIGNMENT, OTHER, WINDOW, selected, 11));
        rejects(() -> validate(value, active, 7, OTHER, SESSION, WINDOW, selected, 11));
        for (long position : new long[] {-1, 0, 9, 10})
        { rejects(() -> validate(value, active, 7, ASSIGNMENT, SESSION, WINDOW, selected, position)); }
        for (QuestFateSelection foreign : List.of(selection("v2", "route", "parent"),
            selection("v1", "changed-route", "parent"), selection("v1", "route", "subquest")))
        { rejects(() -> validate(value, active, 7, ASSIGNMENT, SESSION, WINDOW, foreign, 11)); }
        rejects(() -> value.requireFreshCompletion(active, 7, ASSIGNMENT, SESSION, WINDOW, selected,
            11, "subquest", COMPLETE));
        rejects(() -> value.requireFreshCompletion(active, 7, ASSIGNMENT, SESSION, WINDOW, selected,
            11, "parent", QuestFateCompletion.QuestState.IN_PROGRESS));
        QuestFateObservationWindow alreadyComplete = window(active, selected, 10, COMPLETE);
        rejects(() -> validate(alreadyComplete, active, 7, ASSIGNMENT, SESSION, WINDOW, selected, 11));
        QuestFateObservationWindow notStarted = window(active, selected, 0, QuestFateCompletion.QuestState.NOT_STARTED);
        validate(notStarted, active, 7, ASSIGNMENT, SESSION, WINDOW, selected, 1);
        QuestFateObservationWindow exhausted = window(active, selected, Long.MAX_VALUE, COMPLETE);
        rejects(() -> validate(exhausted, active, 7, ASSIGNMENT, SESSION, WINDOW, selected, Long.MAX_VALUE));
        rejects(() -> window(active, selected, -1, COMPLETE));
        rejects(() -> window(null, selected, 0, COMPLETE));
        rejects(() -> window(active, null, 0, COMPLETE));
        rejects(() -> window(active, selected, 0, null));
        rejects(() -> validate(value, null, 7, ASSIGNMENT, SESSION, WINDOW, selected, 11));
        rejects(() -> validate(value, active, 7, null, SESSION, WINDOW, selected, 11));
        rejects(() -> validate(value, active, 7, ASSIGNMENT, null, WINDOW, selected, 11));
        rejects(() -> validate(value, active, 7, ASSIGNMENT, SESSION, null, selected, 11));
        rejects(() -> validate(value, active, 7, ASSIGNMENT, SESSION, WINDOW, null, 11));
        rejects(() -> new QuestFateObservationWindow(active, null, SESSION, WINDOW, 0, selected, COMPLETE));
        rejects(() -> new QuestFateObservationWindow(active, ASSIGNMENT, null, WINDOW, 0, selected, COMPLETE));
        rejects(() -> new QuestFateObservationWindow(active, ASSIGNMENT, SESSION, null, 0, selected, COMPLETE));
        check(!value.equals(null) && !value.equals(selected) && !value.equals(alreadyComplete)
            && !value.equals(notStarted) && value.equals(restored));
        System.out.println("QuestFateObservationWindowChecks PASSED");
    }

    private static QuestFateSelection selection(String version, String route, String id)
    {
        QuestFateOffers offers = new QuestFateOffers(QuestFateOffers.Capacity.THREE, version,
            List.of("parent", "subquest", "other"));
        List<QuestFateEligibility.Observation> observations = new java.util.ArrayList<>();
        for (String quest : offers.questIds())
        { observations.add(new QuestFateEligibility.Observation(quest, route, "v1",
            QuestFateEligibility.Status.QUALIFIED, QuestFateEligibility.Status.QUALIFIED,
            QuestFateEligibility.Status.QUALIFIED)); }
        return new QuestFateSelection(new QuestFateEligibility(offers, observations), offers.select(id));
    }

    private static RunLifecycle lifecycle(RunIdentity identity, long revision, RunLifecycle.State state,
        RunLifecycle.Obligation obligation) { return new RunLifecycle(identity, revision, state, obligation); }
    private static QuestFateObservationWindow window(RunLifecycle context, QuestFateSelection selected,
        long position, QuestFateCompletion.QuestState baseline)
    { return new QuestFateObservationWindow(context, ASSIGNMENT, SESSION, WINDOW, position, selected, baseline); }
    private static void validate(QuestFateObservationWindow value, RunLifecycle context, long revision,
        UUID assignment, UUID session, UUID window, QuestFateSelection selected, long position)
    { value.requireFreshCompletion(context, revision, assignment, session, window, selected, position, "parent", COMPLETE); }
    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException | NullPointerException expected) { return; }
        throw new AssertionError("Expected refusal");
    }
}

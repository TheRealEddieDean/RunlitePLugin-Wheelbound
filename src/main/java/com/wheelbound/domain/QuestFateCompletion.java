package com.wheelbound.domain;

import java.util.Objects;

/**
 * Pure fresh-transition prerequisite for supplied assigned quest state (WB-D070).
 * The caller must authenticate the current assignment, supported quest mapping and
 * adjacent observations in one uninterrupted active observation window. Capture a
 * new baseline after login/resume; never compare against a pre-pause baseline.
 * Matching supplied selections does not establish assignment authority. This value
 * neither admits evidence nor settles an assignment or exempts quest-reward XP.
 */
public final class QuestFateCompletion
{
    public enum QuestState { NOT_STARTED, IN_PROGRESS, COMPLETE }

    private final QuestFateOffers.Selection selection;
    private final QuestState baseline;

    /** Retains even COMPLETE baselines, which can never prove a fresh completion. */
    public QuestFateCompletion(QuestFateOffers.Selection selection, QuestState baseline)
    {
        this.selection = Objects.requireNonNull(selection, "selection");
        this.baseline = Objects.requireNonNull(baseline, "baseline");
    }

    public QuestFateOffers.Selection selection() { return selection; }
    public QuestState baseline() { return baseline; }

    /**
     * Validate only a supplied transition. Repeated validation is not replay
     * prevention: durable once-only settlement remains an enclosing transaction.
     * Unknown/unsupported client state must not be converted into this enum.
     */
    public void requireFreshCompletion(QuestFateOffers.Selection expectedSelection,
        RunLifecycle.State state, String observedQuestId, QuestState observedState)
    {
        Objects.requireNonNull(expectedSelection, "expectedSelection");
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(observedQuestId, "observedQuestId");
        Objects.requireNonNull(observedState, "observedState");
        if (!selection.equals(expectedSelection)
            || !selection.questId().equals(observedQuestId))
        { throw new IllegalArgumentException("Foreign quest completion context"); }
        if (state != RunLifecycle.State.ACTIVE)
        { throw new IllegalStateException("Active quest observation required"); }
        if (baseline == QuestState.COMPLETE || observedState != QuestState.COMPLETE)
        { throw new IllegalStateException("Fresh quest completion transition required"); }
    }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof QuestFateCompletion)) { return false; }
        QuestFateCompletion other = (QuestFateCompletion) value;
        return selection.equals(other.selection) && baseline == other.baseline;
    }

    @Override public int hashCode() { return Objects.hash(selection, baseline); }
}

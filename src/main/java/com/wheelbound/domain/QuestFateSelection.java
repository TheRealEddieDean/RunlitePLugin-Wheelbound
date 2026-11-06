package com.wheelbound.domain;

import java.util.Objects;

/**
 * Selected quest bound to the complete supplied eligibility snapshot (WB-D070/102).
 * This is a structural prerequisite, not a durable assignment or evidence authority.
 * Callers still authenticate routes, account/session freshness, capacity ownership,
 * randomness, quotes and restrictions, and atomically commit the full assignment.
 */
public final class QuestFateSelection
{
    private final QuestFateEligibility eligibility;
    private final QuestFateOffers.Selection selection;

    public QuestFateSelection(QuestFateEligibility eligibility, QuestFateOffers.Selection selection)
    {
        this.eligibility = Objects.requireNonNull(eligibility, "eligibility");
        this.selection = Objects.requireNonNull(selection, "selection");
        eligibility.requireQualified(selection.offers());
    }

    public QuestFateEligibility eligibility() { return eligibility; }
    public QuestFateOffers.Selection selection() { return selection; }

    /** Compare the retained snapshot as well as the selected quest and full offer order. */
    public void requireSameSelection(QuestFateSelection expected)
    {
        if (!equals(Objects.requireNonNull(expected, "expected")))
        { throw new IllegalArgumentException("Changed frozen Questing selection"); }
    }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof QuestFateSelection)) { return false; }
        QuestFateSelection other = (QuestFateSelection) value;
        return eligibility.equals(other.eligibility) && selection.equals(other.selection);
    }

    @Override public int hashCode() { return Objects.hash(eligibility, selection); }
}

package com.wheelbound.domain;

import java.util.Objects;

/**
 * Supplied reward quote bound to the complete qualified Questing selection.
 * STATE_CONTRACT requires frozen reward provenance; this value does not authenticate
 * that provenance, calculate rewards, settle an audit or credit a ledger. A complete
 * durable assignment and supported active completion evidence remain required.
 */
public final class QuestFateReward
{
    private final QuestFateSelection selection;
    private final String quoteKey;
    private final String quoteVersion;
    private final long fatePoints;

    public QuestFateReward(QuestFateSelection selection, String quoteKey,
        String quoteVersion, long fatePoints)
    {
        this.selection = Objects.requireNonNull(selection, "selection");
        this.quoteKey = key(quoteKey);
        this.quoteVersion = key(quoteVersion);
        if (fatePoints < 0)
        { throw new IllegalArgumentException("Reward quote cannot be a deduction"); }
        this.fatePoints = fatePoints;
    }

    private static String key(String value)
    {
        Objects.requireNonNull(value, "quote key/version");
        if (value.isBlank() || !value.equals(value.strip()))
        { throw new IllegalArgumentException("Noncanonical quote key/version"); }
        return value;
    }

    public QuestFateSelection selection() { return selection; }
    public String quoteKey() { return quoteKey; }
    public String quoteVersion() { return quoteVersion; }
    public long fatePoints() { return fatePoints; }

    /** Full snapshot comparison prevents reuse for a changed quest or access route. */
    public void requireSelection(QuestFateSelection expected)
    { selection.requireSameSelection(expected); }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof QuestFateReward)) { return false; }
        QuestFateReward other = (QuestFateReward) value;
        return selection.equals(other.selection) && quoteKey.equals(other.quoteKey)
            && quoteVersion.equals(other.quoteVersion) && fatePoints == other.fatePoints;
    }

    @Override public int hashCode()
    { return Objects.hash(selection, quoteKey, quoteVersion, fatePoints); }
}

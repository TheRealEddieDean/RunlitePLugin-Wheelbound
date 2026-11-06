package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Immutable supplied Questing commitment. Retains original accounting, generation
 * and baseline independently of later observation windows. This is not durable
 * current-assignment authority: the aggregate must authenticate catalogs, quotes,
 * allocation retention and source coverage, then publish atomically. No settlement,
 * spin debit, evidence consumption or gameplay activation occurs here.
 */
public final class QuestFateAssignment
{
    private final QuestFateProvenance provenance;
    private final MasterSpinAllocation allocation;
    private final QuestFateObservationWindow originalWindow;

    public QuestFateAssignment(QuestFateProvenance provenance,
        MasterSpinAllocation allocation, QuestFateObservationWindow originalWindow)
    {
        this.provenance = Objects.requireNonNull(provenance, "provenance");
        this.allocation = Objects.requireNonNull(allocation, "allocation");
        this.originalWindow = Objects.requireNonNull(originalWindow, "originalWindow");
        provenance.requireAllocation(allocation);
        requireWindow(originalWindow);
        if (originalWindow.baseline() == QuestFateCompletion.QuestState.COMPLETE)
        { throw new IllegalArgumentException("New quest assignment requires an unfinished baseline"); }
    }

    public UUID assignmentId() { return originalWindow.assignmentId(); }
    public RunIdentity identity() { return originalWindow.identity(); }
    public long creationRevision() { return originalWindow.baselineRevision(); }
    public QuestFateProvenance provenance() { return provenance; }
    public MasterSpinAllocation allocation() { return allocation; }
    public QuestFateObservationWindow originalWindow() { return originalWindow; }

    /**
     * Bind a separately supplied refreshed window without replacing the original
     * commitment. A COMPLETE resume baseline may be retained, but cannot establish
     * fresh completion. Matching IDs do not authenticate a resume or coverage.
     */
    public void requireWindow(QuestFateObservationWindow window)
    {
        Objects.requireNonNull(window, "window");
        if (!provenance.resolution().identity().equals(window.identity())
            || !originalWindow.assignmentId().equals(window.assignmentId())
            || window.baselineRevision() < provenance.resolution().afterRevision()
            || window.baselineRevision() < originalWindow.baselineRevision())
        { throw new IllegalArgumentException("Foreign or regressing quest assignment window"); }
        provenance.reward().requireSelection(window.selection());
    }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof QuestFateAssignment)) { return false; }
        QuestFateAssignment other = (QuestFateAssignment) value;
        return provenance.equals(other.provenance)
            && allocation.spinsBefore() == other.allocation.spinsBefore()
            && originalWindow.equals(other.originalWindow);
    }

    @Override public int hashCode()
    { return Objects.hash(provenance, allocation.spinsBefore(), originalWindow); }
}

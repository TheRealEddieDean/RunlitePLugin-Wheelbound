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
    private final java.util.Optional<QuestFateOfferDraw> offerDraw;

    public QuestFateAssignment(QuestFateProvenance provenance,
        MasterSpinAllocation allocation, QuestFateObservationWindow originalWindow)
    {
        this(provenance, allocation, originalWindow, java.util.Optional.empty());
    }

    /** Supplied original draw retention; absence means unavailable legacy evidence. */
    public QuestFateAssignment(QuestFateProvenance provenance,
        MasterSpinAllocation allocation, QuestFateObservationWindow originalWindow,
        java.util.Optional<QuestFateOfferDraw> offerDraw)
    {
        this.offerDraw = Objects.requireNonNull(offerDraw, "offerDraw");
        this.provenance = Objects.requireNonNull(provenance, "provenance");
        this.allocation = Objects.requireNonNull(allocation, "allocation");
        this.originalWindow = Objects.requireNonNull(originalWindow, "originalWindow");
        provenance.requireAllocation(allocation);
        offerDraw.ifPresent(draw -> draw.requireProvenance(provenance));
        requireWindow(originalWindow);
        if (originalWindow.baseline() == QuestFateCompletion.QuestState.COMPLETE)
        { throw new IllegalArgumentException("New quest assignment requires an unfinished baseline"); }
    }

    public java.util.Optional<QuestFateOfferDraw> offerDraw() { return offerDraw; }
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
            && originalWindow.equals(other.originalWindow)
            && offerDraw.map(QuestFateOfferDraw::draws).equals(other.offerDraw.map(QuestFateOfferDraw::draws))
            && offerDraw.map(draw -> draw.evidence().observations())
                .equals(other.offerDraw.map(draw -> draw.evidence().observations()));
    }

    @Override public int hashCode()
    { return Objects.hash(provenance, allocation.spinsBefore(), originalWindow,
        offerDraw.map(QuestFateOfferDraw::draws), offerDraw.map(draw -> draw.evidence().observations())); }
}

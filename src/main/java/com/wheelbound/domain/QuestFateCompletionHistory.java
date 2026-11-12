package com.wheelbound.domain;

import java.util.List;
import java.util.Objects;

/** Supplied historical contexts and receipts, retained together without settlement authority. */
public final class QuestFateCompletionHistory
{
    private final QuestFateCompletionJournal journal;
    private final List<QuestFateCurrentAssignment> contexts;

    public QuestFateCompletionHistory(QuestFateCompletionJournal journal,
        List<QuestFateCurrentAssignment> contexts)
    {
        this.journal = Objects.requireNonNull(journal, "journal");
        this.contexts = List.copyOf(contexts);
        journal.requireHistoricalContexts(this.contexts);
    }

    public QuestFateCompletionJournal journal() { return journal; }
    public List<QuestFateCurrentAssignment> contexts() { return contexts; }

    /**
     * Retain a supplied receipt and its validated active context together. Exact
     * replay preserves the original context even when the live head has changed.
     * This does not authenticate evidence, publish a write or settle a reward.
     */
    public QuestFateCompletionHistory append(QuestFateCompletionReceipt request,
        QuestFateCurrentAssignment current, long revision,
        java.util.UUID sessionId, java.util.UUID windowId)
    {
        var nextJournal = journal.append(request, current, revision, sessionId, windowId);
        if (nextJournal == journal) { return this; }
        var nextContexts = new java.util.ArrayList<>(contexts);
        nextContexts.add(current);
        return new QuestFateCompletionHistory(nextJournal, nextContexts);
    }

    /** Historical contexts bind to retained originals, never to today's reservation. */
    public void requireRetained(RunLifecycle head, String catalogVersion,
        MasterSelectionCreationJournal creations, MasterSelectionResolutions resolutions)
    {
        if (!journal.identity().equals(head.identity()))
        { throw new IllegalArgumentException("Foreign Questing history"); }
        for (var receipt : journal.receipts())
        {
            var resolution = receipt.assignment().provenance().resolution();
            if (receipt.expectedRevision() > head.revision()
                || !resolution.resolved().catalogVersion().equals(catalogVersion)
                || creations == null || resolutions == null
                || !resolution.creation().equals(creations.receipts().get(resolution.creation().transactionId()))
                || !resolution.equals(resolutions.receipts().get(resolution.commandId())))
            { throw new IllegalArgumentException("Missing or changed historical Questing context"); }
        }
    }

    /** Structural prefix guard only; publication must also authenticate evidence and settle atomically. */
    public void requireExtensionOf(QuestFateCompletionHistory previous)
    { journal.requireExtensionOf(Objects.requireNonNull(previous, "previous").journal); }
}

package com.wheelbound.persistence;

import com.wheelbound.domain.MasterSelectionProvenance;
import com.wheelbound.domain.MasterSelectionCreationJournal;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Retained retries must remain backed by creation and the complete later transcript. */
final class MasterRetryHistoryBinding
{
    private MasterRetryHistoryBinding() { }

    static void validate(RunDocument head)
    {
        Map<UUID, MasterSelectionProvenance> previous = new HashMap<>();
        for (var receipt : head.receipts())
        {
            var retry = receipt.masterRetry();
            if (retry == null) { continue; }
            var creations = head.masterCreations().orElseThrow(
                () -> new IllegalArgumentException("Retry requires retained creation"));
            if (!head.lifecycle().identity().equals(retry.identity()))
            { throw new IllegalArgumentException("Retry creation mismatch"); }
            validateCreation(retry, creations);
            var prior = previous.put(retry.before().transactionId(), retry.after());
            if (prior != null) { requirePrefix(prior, retry.before()); }
            MasterSelectionProvenance retained = head.pendingMasterSelection()
                .filter(p -> p.transactionId().equals(retry.after().transactionId())).orElse(null);
            if (retained == null && head.masterResolutions().isPresent())
            {
                for (var resolution : head.masterResolutions().get().receipts().values())
                {
                    if (resolution.transactionId().equals(retry.after().transactionId()))
                    {
                        if (resolution.beforeRevision() < retry.afterRevision())
                        { throw new IllegalArgumentException("Retry follows terminal resolution"); }
                        retained = resolution.resolved();
                    }
                }
            }
            if (retained == null)
            { throw new IllegalArgumentException("Retry requires retained pending or terminal transcript"); }
            requirePrefix(retry.after(), retained);
        }
    }

    static void validateCreation(MasterRetryOperationProposal retry, MasterSelectionCreationJournal creations)
    {
        var creation = creations.receipts().get(retry.before().transactionId());
        if (creation == null || !creations.identity().equals(retry.identity()))
        { throw new IllegalArgumentException("Missing or foreign retry creation"); }
        // matchPending deliberately requires the latest creation. Historical retry
        // responses instead belong to their own original selection.
        var original = new MasterSelectionCreationJournal(retry.identity(), retry.beforeRevision(), List.of(creation));
        if (!original.matchPending(retry.before(), retry.identity(), retry.beforeRevision()).equals(
            original.matchPending(retry.after(), retry.identity(), retry.beforeRevision())))
        { throw new IllegalArgumentException("Retry creation mismatch"); }
    }

    private static void requirePrefix(MasterSelectionProvenance prefix, MasterSelectionProvenance full)
    {
        if (prefix.drawCount() > full.drawCount())
        { throw new IllegalArgumentException("Shortened retry history"); }
        for (int i = 0; i < prefix.drawCount(); i++)
        {
            var expected = prefix.landings().get(i).step();
            var actual = full.landings().get(i).step();
            if (!expected.draw().equals(actual.draw())
                || !expected.streamStateAfter().equals(actual.streamStateAfter()))
            { throw new IllegalArgumentException("Changed retry history"); }
        }
    }
}

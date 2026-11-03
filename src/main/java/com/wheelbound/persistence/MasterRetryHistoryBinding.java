package com.wheelbound.persistence;

import com.wheelbound.domain.MasterSelectionProvenance;
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
            if (!head.lifecycle().identity().equals(retry.identity())
                || !creations.matchPending(retry.before(), retry.identity(), retry.beforeRevision()).equals(
                    creations.matchPending(retry.after(), retry.identity(), retry.beforeRevision())))
            { throw new IllegalArgumentException("Retry creation mismatch"); }
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

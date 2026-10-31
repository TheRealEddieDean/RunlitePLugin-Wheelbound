package com.wheelbound.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Retained original Master creation requests (WB-D138/139).
 * Structural validation only; does not authorize creation, authenticate evidence,
 * allocate spins or persist anything. The aggregate must separately check global
 * command collisions and retain this journal atomically with selection state.
 */
public final class MasterSelectionCreationJournal
{
    private final RunIdentity identity;
    private final Map<UUID, MasterSelectionCreationReceipt> receipts;

    /** Restore in commit order; intervening non-creation revisions are permitted. */
    public MasterSelectionCreationJournal(RunIdentity identity, long currentRevision,
        Collection<MasterSelectionCreationReceipt> payloads)
    {
        this.identity = Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(payloads, "payloads");
        if (currentRevision < 0) { throw new IllegalArgumentException("Negative journal head"); }
        Map<UUID, MasterSelectionCreationReceipt> retained = new LinkedHashMap<>();
        long previousAfter = -1;
        for (MasterSelectionCreationReceipt receipt : payloads)
        {
            Objects.requireNonNull(receipt, "receipt");
            if (!identity.equals(receipt.identity()) || receipt.afterRevision() > currentRevision
                || receipt.beforeRevision() < previousAfter
                || retained.containsKey(receipt.transactionId()))
            { throw new IllegalArgumentException("Foreign, future, overlapping or duplicate creation"); }
            retained.put(receipt.transactionId(), receipt);
            previousAfter = receipt.afterRevision();
        }
        receipts = Collections.unmodifiableMap(retained);
    }

    public RunIdentity identity() { return identity; }
    /** Original commit order, including selections no longer pending. */
    public Map<UUID, MasterSelectionCreationReceipt> receipts() { return receipts; }

    /** Return the retained original, never the newer pending transcript. */
    public MasterSelectionCreationReceipt replay(MasterSelectionCreationReceipt request,
        RunIdentity expectedIdentity, long currentRevision)
    {
        Objects.requireNonNull(request, "request");
        if (!identity.equals(Objects.requireNonNull(expectedIdentity, "expectedIdentity")))
        { throw new IllegalArgumentException("Foreign creation replay context"); }
        new MasterSelectionCreationJournal(identity, currentRevision, receipts.values());
        MasterSelectionCreationReceipt original = receipts.get(request.transactionId());
        if (original == null || !original.equals(request))
        { throw new IllegalArgumentException("Unknown or conflicting creation request"); }
        return original;
    }
}

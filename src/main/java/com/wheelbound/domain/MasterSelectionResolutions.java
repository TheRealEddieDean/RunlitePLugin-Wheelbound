package com.wheelbound.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Retained terminal transcripts, at most one per Master selection. No allocation,
 * debit, evidence authentication or persistence. A future aggregate must also match
 * its retained creation journal, preserve the committed prefix, enforce global command
 * uniqueness and atomically publish the terminal assignment and accounting state.
 */
public final class MasterSelectionResolutions
{
    private final RunIdentity identity;
    private final Map<UUID, MasterSelectionResolutionReceipt> receipts;

    public MasterSelectionResolutions(RunIdentity identity, long currentRevision,
        Collection<MasterSelectionResolutionReceipt> payloads)
    {
        this.identity = Objects.requireNonNull(identity, "identity");
        if (currentRevision < 0) { throw new IllegalArgumentException("Negative head"); }
        Map<UUID, MasterSelectionResolutionReceipt> retained = new LinkedHashMap<>();
        Set<UUID> selections = new HashSet<>();
        Set<UUID> commands = new HashSet<>();
        long previousAfter = -1;
        for (MasterSelectionResolutionReceipt receipt : Objects.requireNonNull(payloads, "payloads"))
        {
            Objects.requireNonNull(receipt, "receipt");
            if (!identity.equals(receipt.identity()) || receipt.afterRevision() > currentRevision
                || receipt.beforeRevision() < previousAfter
                || receipt.creation().beforeRevision() < previousAfter
                || !selections.add(receipt.transactionId()) || !commands.add(receipt.commandId()))
            { throw new IllegalArgumentException("Foreign, future, overlapping or repeated resolution"); }
            retained.put(receipt.commandId(), receipt);
            previousAfter = receipt.afterRevision();
        }
        for (UUID command : commands)
        {
            if (selections.contains(command))
            { throw new IllegalArgumentException("Resolution command collides with selection creation"); }
        }
        receipts = Collections.unmodifiableMap(retained);
    }

    public RunIdentity identity() { return identity; }
    public Map<UUID, MasterSelectionResolutionReceipt> receipts() { return receipts; }

    /** Full original equality; replay never replaces the retained transcript. */
    public MasterSelectionResolutionReceipt replay(MasterSelectionResolutionReceipt request,
        RunIdentity expectedIdentity, long currentRevision)
    {
        Objects.requireNonNull(request, "request");
        if (!identity.equals(Objects.requireNonNull(expectedIdentity, "expectedIdentity")))
        { throw new IllegalArgumentException("Foreign resolution replay"); }
        new MasterSelectionResolutions(identity, currentRevision, receipts.values());
        MasterSelectionResolutionReceipt original = receipts.get(request.commandId());
        if (original == null || !original.equals(request))
        { throw new IllegalArgumentException("Unknown or changed resolution request"); }
        return original;
    }
}

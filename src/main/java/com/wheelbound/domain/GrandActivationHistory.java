package com.wheelbound.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Full original activation payloads for a future durable aggregate (WB-D089/104).
 * This structural history neither identifies the currently active attempt nor proves
 * intervening settlement, session admission or evidence. Empty legacy history must
 * remain explicitly absent at the persistence boundary, never inferred from progress.
 */
public final class GrandActivationHistory
{
    private final SealedGrandFate seal;
    private final Map<UUID, GrandActivationReceipt> receipts;

    /** Restore ordered payloads against an independently restored seal and head. */
    public GrandActivationHistory(SealedGrandFate seal, long currentRevision,
        Collection<GrandActivationReceipt> payloads)
    {
        this.seal = Objects.requireNonNull(seal, "seal");
        Objects.requireNonNull(payloads, "payloads");
        if (currentRevision < 0) { throw new IllegalArgumentException("Negative history head"); }
        Map<UUID, GrandActivationReceipt> retained = new LinkedHashMap<>();
        long previousAfter = -1;
        Set<GrandFateChecklist.Objective> previousCompleted = Collections.emptySet();
        for (GrandActivationReceipt receipt : payloads)
        {
            Objects.requireNonNull(receipt, "receipt");
            GrandFateChecklist checklist = receipt.attempt().checklist();
            if (!seal.equals(checklist.seal()) || receipt.afterRevision() > currentRevision
                || receipt.beforeRevision() <= previousAfter
                || !checklist.completed().containsAll(previousCompleted)
                || receipt.commandId().equals(seal.commandId())
                || retained.containsKey(receipt.commandId()))
            { throw new IllegalArgumentException("Foreign, future, overlapping or conflicting Grand activation"); }
            retained.put(receipt.commandId(), receipt);
            previousAfter = receipt.afterRevision();
            previousCompleted = checklist.completed();
        }
        receipts = Collections.unmodifiableMap(retained);
    }

    public SealedGrandFate seal() { return seal; }
    /** Iteration retains original activation order, including failed attempts. */
    public Map<UUID, GrandActivationReceipt> receipts() { return receipts; }

    /**
     * Check full replay payload at a supplied current identity/head. No activation is
     * proposed: new commands must pass the lifecycle gate and commit atomically.
     */
    public GrandActivationReceipt replay(GrandActivationReceipt request,
        RunIdentity expectedIdentity, long currentRevision)
    {
        Objects.requireNonNull(request, "request");
        if (!seal.identity().equals(Objects.requireNonNull(expectedIdentity, "expectedIdentity"))
            || !seal.equals(request.attempt().checklist().seal()))
        { throw new IllegalArgumentException("Foreign activation replay"); }
        new GrandActivationHistory(seal, currentRevision, receipts.values());
        GrandActivationReceipt original = receipts.get(request.commandId());
        if (original == null) { throw new IllegalArgumentException("Unknown activation command"); }
        if (!original.equals(request)) { throw new IllegalArgumentException("Activation payload collision"); }
        return original;
    }
}

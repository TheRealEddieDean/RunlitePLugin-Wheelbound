package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable structural journal for a future atomic Grand evidence command.
 * Public construction supplies no authentication, session admission or persistence.
 * The trusted boundary must commit this value with progress under the same lock.
 */
public final class GrandEvidenceJournal
{
    private final GrandFateProgress progress;
    private final Map<UUID, GrandEvidenceReceipt> receipts;

    /** Restore full payloads; every progress reference must resolve exactly once. */
    public GrandEvidenceJournal(GrandFateProgress progress, long currentRevision,
        Collection<GrandEvidenceReceipt> payloads)
    {
        this.progress = Objects.requireNonNull(progress, "progress");
        Objects.requireNonNull(payloads, "payloads");
        new GrandFateProgress(progress.checklist().seal(), currentRevision, progress.evidence().values());
        Map<UUID, GrandEvidenceReceipt> retained = new LinkedHashMap<>();
        long previousRevision = 0;
        for (GrandEvidenceReceipt receipt : payloads)
        {
            Objects.requireNonNull(receipt, "receipt");
            GrandFateProgress.Evidence reference = receipt.reference();
            if (!reference.equals(progress.evidence().get(reference.objective()))
                || reference.verifiedRevision() <= previousRevision
                || retained.containsKey(reference.receiptId()))
            { throw new IllegalArgumentException("Unresolved, unordered or duplicate Grand receipt"); }
            for (GrandEvidenceReceipt prior : retained.values())
            {
                if (prior.sameSourceEvent(receipt))
                { throw new IllegalArgumentException("Grand source event already used"); }
            }
            retained.put(reference.receiptId(), receipt);
            previousRevision = reference.verifiedRevision();
        }
        if (retained.size() != progress.evidence().size())
        { throw new IllegalArgumentException("Grand progress requires full receipt payloads"); }
        this.receipts = Collections.unmodifiableMap(retained);
    }

    public GrandFateProgress progress() { return progress; }
    /** Iteration order is strictly increasing accepted revision. */
    public Map<UUID, GrandEvidenceReceipt> receipts() { return receipts; }

    /**
     * Exact full replay returns this journal without inspecting a newer attempt.
     * New proposals require the current attempt and caller-supplied admitted session.
     * Neither matching UUIDs nor successful proposals authenticate a source or settle
     * off-task penalties. A future command must perform those checks before commit.
     */
    public GrandEvidenceJournal proposeAppend(GrandEvidenceReceipt receipt,
        GrandFateAttempt attempt, RunLifecycle current, UUID currentSessionId,
        RunIdentity expectedIdentity, long expectedRevision)
    {
        Objects.requireNonNull(receipt, "receipt");
        Objects.requireNonNull(current, "current");
        if (!current.identity().equals(expectedIdentity) || current.revision() != expectedRevision
            || !current.identity().equals(progress.checklist().seal().identity()))
        { throw new IllegalArgumentException("Foreign or stale Grand journal context"); }
        new GrandEvidenceJournal(progress, current.revision(), receipts.values());
        GrandEvidenceReceipt existing = receipts.get(receipt.reference().receiptId());
        if (existing != null)
        {
            if (existing.equals(receipt)) { return this; }
            throw new IllegalArgumentException("Grand command payload collision");
        }
        for (GrandEvidenceReceipt prior : receipts.values())
        {
            if (prior.sameSourceEvent(receipt))
            { throw new IllegalArgumentException("Grand source event already used"); }
        }
        GrandFateAttempt.Success success = receipt.proposeSuccess(attempt, current, progress,
            currentSessionId, expectedIdentity, expectedRevision);
        Collection<GrandEvidenceReceipt> extended = new ArrayList<>(receipts.values());
        extended.add(receipt);
        return new GrandEvidenceJournal(success.progress(), success.lifecycle().revision(), extended);
    }
}

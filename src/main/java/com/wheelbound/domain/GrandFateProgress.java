package com.wheelbound.domain;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Provenance-bearing restore value for WB-D104/117. References must come from a
 * trusted verification boundary, never directly from client observations. This
 * class validates structure only: it does not verify the referenced evidence,
 * persist progress, complete an attempt, or authorize an award. No live caller.
 */
public final class GrandFateProgress
{
    /** Reference to the original validated evidence and its durable run revision. */
    public static final class Evidence
    {
        private final SealedGrandFate seal;
        private final GrandFateChecklist.Objective objective;
        private final UUID receiptId;
        private final long verifiedRevision;

        public Evidence(SealedGrandFate seal, GrandFateChecklist.Objective objective,
            UUID receiptId, long verifiedRevision)
        {
            this.seal = Objects.requireNonNull(seal, "seal");
            this.objective = Objects.requireNonNull(objective, "objective");
            this.receiptId = Objects.requireNonNull(receiptId, "receiptId");
            if (!GrandFateChecklist.initial(seal).required().contains(objective)
                || verifiedRevision <= 0 || receiptId.equals(seal.commandId()))
            { throw new IllegalArgumentException("Invalid Grand evidence reference"); }
            this.verifiedRevision = verifiedRevision;
        }

        public SealedGrandFate seal() { return seal; }
        public GrandFateChecklist.Objective objective() { return objective; }
        public UUID receiptId() { return receiptId; }
        public long verifiedRevision() { return verifiedRevision; }

        @Override public boolean equals(Object value)
        {
            if (!(value instanceof Evidence)) { return false; }
            Evidence other = (Evidence) value;
            return seal.equals(other.seal) && objective == other.objective
                && receiptId.equals(other.receiptId) && verifiedRevision == other.verifiedRevision;
        }

        @Override public int hashCode()
        { return Objects.hash(seal, objective, receiptId, verifiedRevision); }
    }

    private final GrandFateChecklist checklist;
    private final Map<GrandFateChecklist.Objective, Evidence> evidence;

    /**
     * Restore explicit validated references at a known aggregate revision. Empty
     * input means no verified progress; legacy absence must not synthesize entries.
     * The trusted acceptance boundary must resolve full receipts; structural
     * references alone never establish authenticity or completion authority.
     */
    public GrandFateProgress(SealedGrandFate seal, long currentRevision,
        Collection<Evidence> references)
    {
        Objects.requireNonNull(seal, "seal");
        Objects.requireNonNull(references, "references");
        if (currentRevision < 0) { throw new IllegalArgumentException("Negative revision"); }
        EnumMap<GrandFateChecklist.Objective, Evidence> retained =
            new EnumMap<>(GrandFateChecklist.Objective.class);
        Set<UUID> receipts = new HashSet<>();
        for (Evidence reference : references)
        {
            Objects.requireNonNull(reference, "reference");
            if (!seal.equals(reference.seal()) || reference.verifiedRevision() > currentRevision
                || retained.putIfAbsent(reference.objective(), reference) != null
                || !receipts.add(reference.receiptId()))
            { throw new IllegalArgumentException("Foreign, future or duplicate Grand evidence"); }
        }
        this.checklist = GrandFateChecklist.restore(seal, retained.keySet());
        this.evidence = Collections.unmodifiableMap(retained);
    }

    public GrandFateChecklist checklist() { return checklist; }
    public Map<GrandFateChecklist.Objective, Evidence> evidence() { return evidence; }

    /**
     * Propose a monotonic extension after the caller has verified the full evidence
     * receipt. A new reference must belong to the next aggregate revision. Exact
     * reference replay retains this value without consuming another revision;
     * completed objectives and receipt IDs can never be reassigned.
     *
     * Structural validation only: currentRevision must be read under the future
     * durable command lock. This method neither authenticates receipts nor checks
     * lifecycle/session/attempt eligibility, persists a receipt, or awards completion.
     * The caller must preserve explicit legacy absence and must not publish this
     * proposal before atomically persisting the full receipt and resulting aggregate.
     */
    public GrandFateProgress appendReference(Evidence reference, long currentRevision)
    {
        Objects.requireNonNull(reference, "reference");
        // Validate the retained prefix even on replay; future progress cannot be
        // smuggled through by supplying an older aggregate revision.
        new GrandFateProgress(checklist.seal(), currentRevision, evidence.values());
        if (!checklist.seal().equals(reference.seal()))
        { throw new IllegalArgumentException("Foreign Grand evidence seal"); }
        Evidence existing = evidence.get(reference.objective());
        if (existing != null)
        {
            if (existing.equals(reference)) { return this; }
            throw new IllegalArgumentException("Completed Grand objective cannot be replaced");
        }
        long nextRevision = Math.addExact(currentRevision, 1);
        if (reference.verifiedRevision() != nextRevision)
        { throw new IllegalArgumentException("New Grand evidence requires next aggregate revision"); }
        Collection<Evidence> extended = new ArrayList<>(evidence.values());
        extended.add(reference);
        return new GrandFateProgress(checklist.seal(), nextRevision, extended);
    }
}

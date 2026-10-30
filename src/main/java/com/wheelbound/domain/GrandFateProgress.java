package com.wheelbound.domain;

import java.util.Collection;
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
     * The future codec/store must resolve receipts and enforce monotonic retention.
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
}

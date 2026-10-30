package com.wheelbound.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * WB-D138/139 precommit policy over a caller-verified, current eligibility snapshot.
 * Classifications are declarations, not evidence. This unwired model authorizes no
 * draw, debit or retry: lifecycle, spins, durable stream/history and reconciliation
 * must be checked by the future selection command. Never apply to committed offers.
 */
public final class MasterSelectionReadiness
{
    public enum Availability { ASSIGNABLE, VERIFIED_EMPTY, UNKNOWN, UNSUPPORTED }
    public enum Status { READY, WAITING_FOR_EVIDENCE, NO_VALID_FATE }
    public enum Landing { ORDINARY_ASSIGNMENT, EMPTY_ORDINARY_RETRY, SPECIAL_OBLIGATION }

    public static final class Classification
    {
        private final Availability availability;
        private final String reason;

        /** Reason identifies the complete-pool proof or the missing support/evidence. */
        public Classification(Availability availability, String reason)
        {
            this.availability = Objects.requireNonNull(availability, "availability");
            this.reason = Objects.requireNonNull(reason, "reason");
            if (reason.isBlank() || !reason.equals(reason.strip()))
            { throw new IllegalArgumentException("Missing or noncanonical readiness reason"); }
        }

        public Availability availability() { return availability; }
        public String reason() { return reason; }
    }

    private final ActiveWheel wheel;
    private final Map<UUID, Classification> classifications;
    private final Map<UUID, ActiveWheel.Slice> slices;
    private final Status status;

    /** Every positive-weight slice needs exactly one classification; no weight is removed. */
    public MasterSelectionReadiness(ActiveWheel wheel, Map<UUID, Classification> classifications)
    {
        this.wheel = Objects.requireNonNull(wheel, "wheel");
        this.classifications = Map.copyOf(classifications);
        Map<UUID, ActiveWheel.Slice> indexed = new HashMap<>();
        boolean blocked = false;
        boolean assignable = false;
        for (ActiveWheel.Slice slice : wheel.slices())
        {
            indexed.put(slice.id(), slice);
            Classification classification = this.classifications.get(slice.id());
            if (classification == null) { throw new IllegalArgumentException("Unclassified active slice"); }
            Availability availability = classification.availability();
            if (availability == Availability.VERIFIED_EMPTY && slice.kind() != ActiveWheel.Kind.ORDINARY)
            { throw new IllegalArgumentException("Special obligations cannot be empty retries"); }
            blocked |= availability == Availability.UNKNOWN || availability == Availability.UNSUPPORTED;
            assignable |= availability == Availability.ASSIGNABLE;
        }
        if (!indexed.keySet().equals(this.classifications.keySet()))
        { throw new IllegalArgumentException("Classification for a foreign slice"); }
        slices = Map.copyOf(indexed);
        status = blocked ? Status.WAITING_FOR_EVIDENCE
            : assignable ? Status.READY : Status.NO_VALID_FATE;
    }

    public ActiveWheel wheel() { return wheel; }
    public Map<UUID, Classification> classifications() { return classifications; }
    public Status status() { return status; }

    /** Classifies a frozen precommit landing only. Does not sample or advance a stream. */
    public Landing classifyLanding(UUID sliceId)
    {
        ActiveWheel.Slice slice = slices.get(Objects.requireNonNull(sliceId, "sliceId"));
        if (slice == null) { throw new IllegalArgumentException("Foreign landing"); }
        if (status != Status.READY) { throw new IllegalStateException("Selection is not ready"); }
        if (classifications.get(sliceId).availability() == Availability.VERIFIED_EMPTY)
        { return Landing.EMPTY_ORDINARY_RETRY; }
        return slice.kind() == ActiveWheel.Kind.ORDINARY
            ? Landing.ORDINARY_ASSIGNMENT : Landing.SPECIAL_OBLIGATION;
    }
}

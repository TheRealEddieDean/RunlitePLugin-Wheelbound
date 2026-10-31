package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Immutable required follow-up for a terminal special landing (WB-D058/138).
 * This structural payload does not select a punishment, donation candidate or
 * acquisition route. It grants no evidence authority, spin debit or settlement.
 * Future publication must retain it atomically with resolution and reservation.
 */
public final class MasterSpecialObligation
{
    public enum Requirement { MANDATORY_PUNISHMENT, MANDATORY_SACRIFICE }

    private final MasterSelectionResolutionReceipt resolution;
    private final UUID sliceId;
    private final Requirement requirement;

    public MasterSpecialObligation(MasterSelectionResolutionReceipt resolution)
    {
        this.resolution = Objects.requireNonNull(resolution, "resolution");
        var transcript = resolution.resolved();
        sliceId = UUID.fromString(transcript.landings().get(transcript.drawCount() - 1)
            .step().draw().selectedId());
        var kind = transcript.readiness().wheel().slices().stream()
            .filter(slice -> slice.id().equals(sliceId)).findFirst().orElseThrow().kind();
        switch (kind)
        {
            case TAINT: requirement = Requirement.MANDATORY_PUNISHMENT; break;
            case SACRIFICE: requirement = Requirement.MANDATORY_SACRIFICE; break;
            default: throw new IllegalArgumentException("Ordinary landing requires an assignment payload");
        }
    }

    /** Exact structural binding only; caller must also validate retained aggregate history. */
    public MasterSpecialObligation validateResolution(MasterSelectionResolutionReceipt expected)
    {
        if (!resolution.equals(Objects.requireNonNull(expected, "expected")))
        { throw new IllegalArgumentException("Changed special obligation resolution"); }
        return this;
    }

    public MasterSelectionResolutionReceipt resolution() { return resolution; }
    public UUID selectedSliceId() { return sliceId; }
    public Requirement requirement() { return requirement; }

    @Override public boolean equals(Object value)
    { return value instanceof MasterSpecialObligation
        && resolution.equals(((MasterSpecialObligation) value).resolution); }

    @Override public int hashCode() { return resolution.hashCode(); }
}

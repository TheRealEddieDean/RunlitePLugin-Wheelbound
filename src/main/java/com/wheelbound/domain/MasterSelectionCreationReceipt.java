package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Original, zero-draw selection creation payload (WB-D138/139).
 * Structural journal prerequisite only: no session/evidence admission, spin allocation,
 * sampling or persistence. A store must retain this original payload separately from
 * the advancing transcript and enforce global transaction uniqueness under its lock.
 */
public final class MasterSelectionCreationReceipt
{
    private final RunIdentity identity;
    private final long beforeRevision;
    private final MasterSelectionProvenance initial;
    private final List<Object> payload;

    public MasterSelectionCreationReceipt(RunIdentity identity, long beforeRevision,
        MasterSelectionProvenance initial)
    {
        this.identity = Objects.requireNonNull(identity, "identity");
        if (beforeRevision < 0) { throw new IllegalArgumentException("Negative revision"); }
        Math.addExact(beforeRevision, 1);
        this.beforeRevision = beforeRevision;
        this.initial = Objects.requireNonNull(initial, "initial");
        if (initial.drawCount() != 0)
        { throw new IllegalArgumentException("Creation must retain the original zero-draw transcript"); }
        List<Object> fields = new ArrayList<>(List.of(identity, beforeRevision,
            initial.transactionId(), initial.snapshotVersion(), initial.catalogVersion(),
            initial.streamDescriptor(), initial.initialStreamState()));
        for (ActiveWheel.Slice slice : initial.readiness().wheel().slices())
        {
            MasterSelectionReadiness.Classification classification =
                initial.readiness().classifications().get(slice.id());
            // Ordinary/special nullable fields are intentional; list order is draw order.
            fields.add(Arrays.asList(slice.id(), slice.kind(), slice.activity(),
                slice.enhancement(), slice.weightUnits(), classification.availability(),
                classification.reason()));
        }
        payload = List.copyOf(fields);
    }

    public RunIdentity identity() { return identity; }
    public UUID transactionId() { return initial.transactionId(); }
    public long beforeRevision() { return beforeRevision; }
    public long afterRevision() { return beforeRevision + 1; }
    public MasterSelectionProvenance initial() { return initial; }

    /** Full original request equality, never UUID-only or current-head comparison. */
    @Override public boolean equals(Object value)
    {
        return value instanceof MasterSelectionCreationReceipt
            && payload.equals(((MasterSelectionCreationReceipt) value).payload);
    }

    @Override public int hashCode() { return payload.hashCode(); }
}

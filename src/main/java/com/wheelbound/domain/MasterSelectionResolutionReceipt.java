package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Frozen terminal selection for future once-only allocation (WB-D120/138/139).
 * Structural preparation only: this neither commits an assignment nor spends a spin.
 * Ordinary completion/Reject timing and special settlement remain separate commands.
 */
public final class MasterSelectionResolutionReceipt
{
    private final UUID commandId;
    private final long beforeRevision;
    private final MasterSelectionCreationReceipt creation;
    private final MasterSelectionProvenance resolved;
    private final List<Object> payload;

    /**
     * Prepare one supplied terminal draw from the actual retained unresolved prefix.
     * The caller must load that prefix and retain the result under the same store lock;
     * this pure operation cannot establish storage freshness or authentic RNG state.
     */
    public static MasterSelectionResolutionReceipt prepare(UUID commandId, long beforeRevision,
        MasterSelectionCreationReceipt creation, MasterSelectionProvenance pending,
        MasterSelectionProvenance.Step terminal)
    {
        Objects.requireNonNull(pending, "pending");
        MasterSelectionProvenance resolved = pending.append(pending.transactionId(),
            pending.drawCount(), pending.currentStreamState(), terminal);
        return new MasterSelectionResolutionReceipt(commandId, beforeRevision, creation, resolved);
    }

    /** Restore a complete retained receipt; does not prove its previous durable prefix. */
    public MasterSelectionResolutionReceipt(UUID commandId, long beforeRevision,
        MasterSelectionCreationReceipt creation, MasterSelectionProvenance resolved)
    {
        this.commandId = Objects.requireNonNull(commandId, "commandId");
        this.creation = Objects.requireNonNull(creation, "creation");
        this.resolved = Objects.requireNonNull(resolved, "resolved");
        if (beforeRevision < creation.afterRevision() || commandId.equals(creation.transactionId())
            || !resolved.hasValidLanding())
        { throw new IllegalArgumentException("Resolution requires a later command and a valid landing"); }
        Math.addExact(beforeRevision, 1);
        this.beforeRevision = beforeRevision;
        MasterSelectionProvenance zero = new MasterSelectionProvenance(resolved.transactionId(),
            resolved.snapshotVersion(), resolved.catalogVersion(), resolved.streamDescriptor(),
            resolved.initialStreamState(), resolved.readiness(), List.of());
        if (!creation.equals(new MasterSelectionCreationReceipt(creation.identity(),
            creation.beforeRevision(), zero)))
        { throw new IllegalArgumentException("Resolution changed the original selection"); }
        List<Object> fields = new ArrayList<>(List.of(commandId, beforeRevision, creation));
        for (MasterSelectionProvenance.Landing landing : resolved.landings())
        {
            fields.add(landing.step().draw());
            fields.add(landing.step().streamStateAfter());
        }
        payload = List.copyOf(fields);
    }

    public UUID commandId() { return commandId; }
    public UUID transactionId() { return creation.transactionId(); }
    public RunIdentity identity() { return creation.identity(); }
    public long beforeRevision() { return beforeRevision; }
    public long afterRevision() { return beforeRevision + 1; }
    public MasterSelectionCreationReceipt creation() { return creation; }
    public MasterSelectionProvenance resolved() { return resolved; }

    @Override public boolean equals(Object value)
    { return value instanceof MasterSelectionResolutionReceipt
        && payload.equals(((MasterSelectionResolutionReceipt) value).payload); }

    @Override public int hashCode() { return payload.hashCode(); }
}

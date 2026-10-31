package com.wheelbound.persistence;

import com.wheelbound.domain.MasterSelectionResolutionReceipt;
import com.wheelbound.domain.MasterSpinAllocation;
import java.util.Objects;
import java.util.UUID;

/**
 * Typed original terminal response, including its exact one-spin reservation.
 * This is not an admitted RunReceipt: assignment/obligation publication, durable
 * storage and settlement must exist before a terminal command can be admitted.
 */
public final class MasterTerminalOperationProposal
{
    private final MasterSpinAllocation allocation;

    private MasterTerminalOperationProposal(MasterSpinAllocation allocation)
    {
        this.allocation = Objects.requireNonNull(allocation, "allocation");
    }

    /** Bind a new response (or retained replay) to the supplied aggregate. */
    public static MasterTerminalOperationProposal prepare(RunDocument head,
        MasterSelectionResolutionReceipt request)
    {
        return new MasterTerminalOperationProposal(
            MasterTerminalReservationPreparation.prepare(head, request).allocation());
    }

    /**
     * Restore original response fields without reconstructing balance from history.
     * Structural restoration alone grants no authority; validateRetained is required
     * before returning this response as a historical replay.
     */
    public static MasterTerminalOperationProposal restore(
        MasterSelectionResolutionReceipt resolution, long spinsBefore)
    {
        return new MasterTerminalOperationProposal(MasterSpinAllocation.restore(resolution, spinsBefore));
    }

    /** Require both original history and original accounting; never prepare a new append. */
    public MasterTerminalOperationProposal validateRetained(RunDocument head)
    {
        Objects.requireNonNull(head, "head");
        var history = head.masterResolutions().orElseThrow(() ->
            new IllegalArgumentException("Unavailable terminal history"));
        MasterResolutionBinding.validateHistory(head, history);
        history.replay(resolution(), head.lifecycle().identity(), head.lifecycle().revision());
        var retained = head.masterSpins().orElseThrow(() ->
            new IllegalArgumentException("Unavailable original spin reservation"))
            .replay(head.lifecycle().identity(), head.lifecycle().revision(), resolution());
        if (retained.spinsBefore() != allocation.spinsBefore())
        { throw new IllegalArgumentException("Changed original terminal accounting"); }
        return this;
    }

    public MasterSelectionResolutionReceipt resolution() { return allocation.resolution(); }
    public MasterSpinAllocation allocation() { return allocation; }
    public UUID commandId() { return resolution().commandId(); }
    public long beforeRevision() { return resolution().beforeRevision(); }
    public long afterRevision() { return resolution().afterRevision(); }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof MasterTerminalOperationProposal)) { return false; }
        var other = (MasterTerminalOperationProposal) value;
        return resolution().equals(other.resolution())
            && allocation.spinsBefore() == other.allocation.spinsBefore();
    }

    @Override public int hashCode()
    { return Objects.hash(resolution(), allocation.spinsBefore()); }
}

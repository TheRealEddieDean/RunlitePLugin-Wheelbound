package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Immutable one-spin accounting proposal for a terminal Master selection.
 * This is not a Fate/card/target assignment or a durable debit. A future aggregate
 * command must atomically retain this proposal, its resolution and the obligation
 * before publishing any result. Caller-supplied lifecycle/balance are not authority.
 * The proposed available balance reserves the ordinary Fate's eventual one-spin
 * charge; ordinary completion/Reject consumption remains a separate settlement.
 * This type does not adopt new charge timing or implement that settlement.
 */
public final class MasterSpinAllocation
{
    private final MasterSelectionResolutionReceipt resolution;
    private final long spinsBefore;

    private MasterSpinAllocation(MasterSelectionResolutionReceipt resolution, long spinsBefore)
    {
        this.resolution = Objects.requireNonNull(resolution, "resolution");
        if (spinsBefore <= 0) { throw new IllegalArgumentException("No allotted spin available"); }
        this.spinsBefore = spinsBefore;
    }

    /** Prepare against one current unresolved head; prefix binding remains required. */
    public static MasterSpinAllocation prepare(RunLifecycle head, long spins,
        MasterSelectionResolutionReceipt resolution)
    {
        Objects.requireNonNull(head, "head");
        Objects.requireNonNull(resolution, "resolution");
        if (!head.identity().equals(resolution.identity())
            || head.revision() != resolution.beforeRevision()
            || head.state() != RunLifecycle.State.ACTIVE
            || head.obligation() != RunLifecycle.Obligation.NONE)
        { throw new IllegalArgumentException("Allocation requires current unoccupied active head"); }
        return new MasterSpinAllocation(resolution, spins);
    }

    /** Restore original accounting only; aggregate retention and authority require separate validation. */
    public static MasterSpinAllocation restore(MasterSelectionResolutionReceipt resolution, long spinsBefore)
    {
        return new MasterSpinAllocation(resolution, spinsBefore);
    }

    /**
     * Validate a retained allocation for exact replay, including paused/later heads.
     * Returns original accounting, never subtracts from the current balance. The
     * caller must establish that this allocation was retained in that aggregate.
     */
    public MasterSpinAllocation replay(RunIdentity identity, long currentRevision,
        MasterSelectionResolutionReceipt requested)
    {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(requested, "requested");
        if (!resolution.identity().equals(identity) || currentRevision < resolution.afterRevision()
            || !resolution.equals(requested))
        { throw new IllegalArgumentException("Foreign, stale or changed allocation replay"); }
        return this;
    }

    public MasterSelectionResolutionReceipt resolution() { return resolution; }
    public UUID transactionId() { return resolution.transactionId(); }
    public UUID commandId() { return resolution.commandId(); }
    public long spinsBefore() { return spinsBefore; }
    public long spinsAfter() { return spinsBefore - 1; }
    public int spinDelta() { return -1; }
    public int fpDelta() { return 0; }
    public UUID selectedSliceId()
    {
        var transcript = resolution.resolved();
        return UUID.fromString(transcript.landings().get(transcript.drawCount() - 1)
            .step().draw().selectedId());
    }
    public ActiveWheel.Kind selectedKind()
    {
        return resolution.resolved().readiness().wheel().slices().stream()
            .filter(s -> s.id().equals(selectedSliceId())).findFirst().orElseThrow().kind();
    }
}

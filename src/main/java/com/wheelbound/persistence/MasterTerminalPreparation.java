package com.wheelbound.persistence;

import com.wheelbound.domain.MasterSelectionResolutionReceipt;
import com.wheelbound.domain.MasterSelectionResolutions;
import com.wheelbound.domain.MasterSpinAllocation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Prepares terminal history and accounting together against one unresolved aggregate.
 * No result is published: durable spin ownership, assignment/obligation, typed operation
 * and atomic persistence must still be supplied before gameplay can consume this proposal.
 */
public final class MasterTerminalPreparation
{
    private final MasterSpinAllocation allocation;
    private final MasterSelectionResolutions resolutions;

    private MasterTerminalPreparation(MasterSpinAllocation allocation,
        MasterSelectionResolutions resolutions)
    {
        this.allocation = allocation;
        this.resolutions = resolutions;
    }

    /**
     * Balance is an explicit proposal input, not evidence of a durable spin balance.
     * Legacy absent resolution history starts empty only for this new append; this
     * method never reconstructs historical allocations or authorizes retained replay.
     */
    public static MasterTerminalPreparation prepare(RunDocument head, long allottedSpins,
        MasterSelectionResolutionReceipt resolution)
    {
        Objects.requireNonNull(head, "head");
        Objects.requireNonNull(resolution, "resolution");
        var history = head.masterResolutions().orElseGet(() ->
            new MasterSelectionResolutions(head.lifecycle().identity(),
                head.lifecycle().revision(), List.of()));
        MasterResolutionBinding.validateAppend(head, history, resolution);
        var allocation = MasterSpinAllocation.prepare(head.lifecycle(), allottedSpins, resolution);
        var receipts = new ArrayList<>(history.receipts().values());
        receipts.add(resolution);
        var proposedHistory = new MasterSelectionResolutions(head.lifecycle().identity(),
            resolution.afterRevision(), receipts);
        return new MasterTerminalPreparation(allocation, proposedHistory);
    }

    public MasterSpinAllocation allocation() { return allocation; }
    public MasterSelectionResolutions resolutions() { return resolutions; }
}

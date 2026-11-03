package com.wheelbound.persistence;

import com.wheelbound.domain.MasterSelectionResolutionReceipt;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/** Pure atomic in-memory publication. No storage admission, punishment draw or settlement. */
public final class MasterTaintTerminalOperation
{
    private MasterTaintTerminalOperation() { }

    /** Publish an already sampled terminal batch without sampling again. The caller
     * must qualify current evidence and the source under its eventual storage lock.
     * Only the supported Taint obligation is admitted; this does not save or settle it.
     * Retained replay uses the original resolution request through prepare instead.
     */
    public static RunDocument prepareSampled(RunDocument head, java.util.UUID commandId,
        com.wheelbound.domain.MasterSelectionProvenance sampled)
    {
        Objects.requireNonNull(head, "head");
        MasterRetryRecovery.requireUnused(head, Objects.requireNonNull(commandId, "commandId"));
        var pending = head.pendingMasterSelection().orElseThrow(() ->
            new IllegalArgumentException("Missing pending Master selection"));
        var creation = head.masterCreations().orElseThrow(() ->
            new IllegalArgumentException("Missing Master creation history"))
            .matchPending(pending, head.lifecycle().identity(), head.lifecycle().revision());
        return prepare(head, new MasterSelectionResolutionReceipt(commandId,
            head.lifecycle().revision(), creation, sampled));
    }

    public static RunDocument prepare(RunDocument head, MasterSelectionResolutionReceipt request)
    {
        Objects.requireNonNull(head, "head");
        Objects.requireNonNull(request, "request");
        var prepared = MasterTaintLifecyclePreparation.prepare(head, request);
        var receipt = RunReceipt.masterTaintTerminal(prepared.terminal().response());
        for (var retained : head.receipts())
        {
            if (!retained.commandId().equals(request.commandId())) { continue; }
            if (!retained.equals(receipt))
            { throw new IllegalArgumentException("Command ID reused for different terminal request"); }
            return head;
        }
        if (head.masterResolutions().filter(h -> h.receipts().containsKey(request.commandId())).isPresent())
        { throw new IllegalArgumentException("Terminal replay requires original typed operation receipt"); }
        var receipts = new ArrayList<>(head.receipts());
        receipts.add(receipt);
        return new RunDocument(prepared.current().lifecycle(), head.catalogVersion(), receipts,
            head.fatePoints(), head.inventory(), head.grandFate(), head.grandProgress(), head.grandJournal(),
            head.grandActivations(), head.grandCurrentAttempt(), Optional.empty(), head.masterCreations(),
            Optional.of(prepared.terminal().resolutions()), Optional.of(prepared.terminal().spins()),
            Optional.of(prepared.current().obligation()));
    }
}

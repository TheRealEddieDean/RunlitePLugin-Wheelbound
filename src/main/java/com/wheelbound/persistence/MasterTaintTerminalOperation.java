package com.wheelbound.persistence;

import com.wheelbound.domain.MasterSelectionResolutionReceipt;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/** Pure atomic in-memory publication. No storage admission, punishment draw or settlement. */
public final class MasterTaintTerminalOperation
{
    private MasterTaintTerminalOperation() { }

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

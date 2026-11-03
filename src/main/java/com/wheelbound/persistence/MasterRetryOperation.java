package com.wheelbound.persistence;

import com.wheelbound.domain.RunLifecycle;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/** Pure atomic publication proposal. Current evidence must separately be qualified
 * under the storage lock; this operation does not authenticate evidence or RNG. */
public final class MasterRetryOperation
{
    private MasterRetryOperation() { }

    public static RunDocument prepare(RunDocument head, MasterRetryOperationProposal request)
    {
        Objects.requireNonNull(head, "head");
        Objects.requireNonNull(request, "request");
        if (!head.lifecycle().identity().equals(request.identity()))
        { throw new IllegalArgumentException("Foreign retry request"); }
        var receipt = RunReceipt.masterRetry(request);
        for (var retained : head.receipts())
        {
            if (!retained.commandId().equals(request.commandId())) { continue; }
            if (!retained.equals(receipt))
            { throw new IllegalArgumentException("Retry command reused for different request"); }
            return head;
        }
        request.validateAgainst(head);
        var next = new RunLifecycle(request.identity(), request.afterRevision(),
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        var receipts = new ArrayList<>(head.receipts());
        receipts.add(receipt);
        return new RunDocument(next, head.catalogVersion(), receipts, head.fatePoints(),
            head.inventory(), head.grandFate(), head.grandProgress(), head.grandJournal(),
            head.grandActivations(), head.grandCurrentAttempt(), Optional.of(request.after()),
            head.masterCreations(), head.masterResolutions(), head.masterSpins().map(spins ->
                new com.wheelbound.domain.MasterSpinState(spins.version(), spins.identity(),
                    next.revision(), spins.allotted(), spins.reservation())),
            head.currentMasterSpecial().map(value -> value.obligation()));
    }
}

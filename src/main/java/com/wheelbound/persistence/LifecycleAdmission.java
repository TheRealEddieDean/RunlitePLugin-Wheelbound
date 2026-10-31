package com.wheelbound.persistence;

import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.RunLifecycle;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/** Pure Pause/Resume preparation. The store must invoke this under its write lock.
 * Resuming retains frozen selection provenance; it never revalidates or advances it. */
public final class LifecycleAdmission
{
    private LifecycleAdmission() { }

    public static RunDocument prepare(RunDocument current, RunIdentity identity,
        UUID commandId, long expectedRevision, RunReceipt.Operation operation)
    {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(commandId, "commandId");
        Objects.requireNonNull(operation, "operation");
        if (operation != RunReceipt.Operation.PAUSE && operation != RunReceipt.Operation.RESUME)
        { throw new IllegalArgumentException("Use the operation-specific transaction service"); }
        if (!current.lifecycle().identity().equals(identity))
        { throw new IllegalArgumentException("Foreign run context"); }
        for (RunReceipt receipt : current.receipts())
        {
            if (!receipt.commandId().equals(commandId)) { continue; }
            if (receipt.operation() != operation || receipt.beforeRevision() != expectedRevision)
            { throw new IllegalArgumentException("Command ID reused for different request"); }
            return current;
        }
        RunLifecycle next = operation == RunReceipt.Operation.PAUSE
            ? current.lifecycle().pause(identity, expectedRevision)
            : current.lifecycle().resume(identity, expectedRevision);
        if (next == current.lifecycle())
        { throw new IllegalStateException("Already in requested lifecycle state"); }
        ArrayList<RunReceipt> receipts = new ArrayList<>(current.receipts());
        receipts.add(new RunReceipt(commandId, operation, expectedRevision, next.revision(),
            next.state(), next.obligation()));
        // Aggregate construction also checks IDs against selection/seal/evidence/history.
        return new RunDocument(next, current.catalogVersion(), receipts, current.fatePoints(),
            current.inventory(), current.grandFate(), current.grandProgress(), current.grandJournal(),
            current.grandActivations(), current.retainedGrandAttempt(next), current.pendingMasterSelection(),
            current.masterCreations());
    }
}

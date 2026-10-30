package com.wheelbound.persistence;

import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.RunLifecycle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Publishes a result only after the receipt and new snapshot have persisted together. */
public final class LifecycleCommands
{
    private final AtomicRunStore store;
    public LifecycleCommands(AtomicRunStore store) { this.store = Objects.requireNonNull(store, "store"); }

    public RunReceipt execute(RunIdentity identity, UUID commandId, long expectedRevision,
        RunReceipt.Operation operation) throws IOException
    {
        Objects.requireNonNull(commandId, "commandId");
        Objects.requireNonNull(operation, "operation");
        if (operation != RunReceipt.Operation.PAUSE && operation != RunReceipt.Operation.RESUME)
        { throw new IllegalArgumentException("Use the operation-specific transaction service"); }
        RunDocument current = store.load().orElseThrow(() -> new IOException("Run has not been initialized"));
        if (!current.lifecycle().identity().equals(identity)) { throw new IllegalArgumentException("Foreign run context"); }
        for (RunReceipt receipt : current.receipts())
        {
            if (!receipt.commandId().equals(commandId)) { continue; }
            if (receipt.operation() != operation || receipt.beforeRevision() != expectedRevision)
            { throw new IllegalArgumentException("Command ID reused for different request"); }
            return receipt; // Return the original response even after later commands have committed.
        }
        RunLifecycle next = operation == RunReceipt.Operation.PAUSE
            ? current.lifecycle().pause(identity, expectedRevision)
            : current.lifecycle().resume(identity, expectedRevision);
        if (next == current.lifecycle()) { throw new IllegalStateException("Already in requested lifecycle state"); }
        RunReceipt receipt = new RunReceipt(commandId, operation, expectedRevision, next.revision(), next.state(), next.obligation());
        List<RunReceipt> receipts = new ArrayList<>(current.receipts()); receipts.add(receipt);
        store.write(new RunDocument(next, current.catalogVersion(), receipts, current.fatePoints(), current.inventory(), current.grandFate(), current.grandProgress(), current.grandJournal()), expectedRevision);
        return receipt;
    }
}

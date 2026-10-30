package com.wheelbound.persistence;

import com.wheelbound.domain.RunIdentity;
import java.io.IOException;
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
        return store.changeLifecycle(identity, commandId, expectedRevision, operation);
    }
}

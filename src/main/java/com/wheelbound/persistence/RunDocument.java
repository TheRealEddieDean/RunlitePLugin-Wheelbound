package com.wheelbound.persistence;

import com.wheelbound.domain.RunLifecycle;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** Foundation snapshot and complete lifecycle receipt journal, not the playable-mode aggregate. */
public final class RunDocument
{
    private final RunLifecycle lifecycle;
    private final String catalogVersion;
    private final List<RunReceipt> receipts;

    public RunDocument(RunLifecycle lifecycle, String catalogVersion)
    { this(lifecycle, catalogVersion, List.of()); }

    public RunDocument(RunLifecycle lifecycle, String catalogVersion, List<RunReceipt> receipts)
    {
        this.lifecycle = Objects.requireNonNull(lifecycle, "lifecycle");
        this.catalogVersion = Objects.requireNonNull(catalogVersion, "catalogVersion");
        if (catalogVersion.trim().isEmpty()) { throw new IllegalArgumentException("Empty catalog version"); }
        this.receipts = List.copyOf(receipts);
        Set<UUID> commands = new HashSet<>();
        RunReceipt previous = null;
        for (RunReceipt receipt : this.receipts)
        {
            if (!commands.add(receipt.commandId())) { throw new IllegalArgumentException("Duplicate command ID"); }
            if (previous != null && (receipt.beforeRevision() != previous.afterRevision()
                || receipt.operation() == previous.operation() || receipt.obligation() != previous.obligation()))
            { throw new IllegalArgumentException("Broken lifecycle receipt chain"); }
            previous = receipt;
        }
        if (previous != null && (previous.afterRevision() != lifecycle.revision()
            || previous.state() != lifecycle.state() || previous.obligation() != lifecycle.obligation()))
        { throw new IllegalArgumentException("Receipt head does not match snapshot"); }
    }

    public RunLifecycle lifecycle() { return lifecycle; }
    public String catalogVersion() { return catalogVersion; }
    public List<RunReceipt> receipts() { return receipts; }
}

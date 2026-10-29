package com.wheelbound.persistence;

import com.wheelbound.domain.RunLifecycle;
import com.wheelbound.domain.FatePointLedger;
import com.wheelbound.domain.SliceInventory;
import java.util.Optional;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** Foundation snapshot and lifecycle/draw/debit journal, not the playable-mode aggregate. */
public final class RunDocument
{
    private final RunLifecycle lifecycle;
    private final String catalogVersion;
    private final List<RunReceipt> receipts;
    private final FatePointLedger fatePoints;
    private final SliceInventory inventory;

    public RunDocument(RunLifecycle lifecycle, String catalogVersion)
    { this(lifecycle, catalogVersion, List.of()); }

    public RunDocument(RunLifecycle lifecycle, String catalogVersion, List<RunReceipt> receipts)
    { this(lifecycle, catalogVersion, receipts, Optional.empty()); }

    /** Empty means unavailable legacy FP state, never an inferred zero balance. */
    public RunDocument(RunLifecycle lifecycle, String catalogVersion, List<RunReceipt> receipts,
        Optional<FatePointLedger> fatePoints)
    { this(lifecycle, catalogVersion, receipts, fatePoints, Optional.empty()); }

    /** Missing legacy inventory is unavailable; never infer lifetime history from ownership. */
    public RunDocument(RunLifecycle lifecycle, String catalogVersion, List<RunReceipt> receipts,
        Optional<FatePointLedger> fatePoints, Optional<SliceInventory> inventory)
    {
        this.inventory = Objects.requireNonNull(inventory, "inventory").orElse(null);
        this.fatePoints = Objects.requireNonNull(fatePoints, "fatePoints").orElse(null);
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
                || !follows(previous, receipt)))
            { throw new IllegalArgumentException("Broken lifecycle receipt chain"); }
            previous = receipt;
        }
        validateDebits();
        if (previous != null && (previous.afterRevision() != lifecycle.revision()
            || previous.state() != lifecycle.state() || previous.obligation() != lifecycle.obligation()))
        { throw new IllegalArgumentException("Receipt head does not match snapshot"); }
    }

    /** Debit entries follow a trusted legacy ledger prefix. Other current journal
     * operations preserve FP; future earning operations require their own validation. */
    private void validateDebits()
    {
        long count = receipts.stream().filter(r -> r.debit() != null).count();
        if (count == 0) { return; }
        if (fatePoints == null || count > fatePoints.entries().size())
        { throw new IllegalArgumentException("Debit journal lacks ledger entries"); }
        int index = fatePoints.entries().size() - Math.toIntExact(count);
        FatePointLedger reconstructed = new FatePointLedger(fatePoints.startingBalance(),
            fatePoints.entries().subList(0, index));
        for (RunReceipt receipt : receipts)
        {
            if (receipt.debit() == null) { continue; }
            FatePointLedger.Entry entry = fatePoints.entries().get(index++);
            if (entry.kind() != FatePointLedger.Kind.DISCRETIONARY_COST
                || entry.amount() != receipt.debit().amount())
            { throw new IllegalArgumentException("Debit journal disagrees with ledger"); }
            reconstructed = reconstructed.append(entry);
            if (reconstructed.balance() != receipt.debit().balance())
            { throw new IllegalArgumentException("Debit response disagrees with ledger"); }
        }
    }

    private boolean follows(RunReceipt previous, RunReceipt next)
    {
        switch (next.operation())
        {
            case PAUSE: return previous.state() == RunLifecycle.State.ACTIVE && next.obligation() == previous.obligation();
            case RESUME: return previous.state() == RunLifecycle.State.PAUSED && next.obligation() == previous.obligation();
            case FP_DEBIT: return previous.state() == RunLifecycle.State.ACTIVE && next.obligation() == previous.obligation();
            case DRAW: return previous.state() == RunLifecycle.State.ACTIVE && previous.obligation() == RunLifecycle.Obligation.NONE;
            default: return false;
        }
    }

    public RunLifecycle lifecycle() { return lifecycle; }
    public String catalogVersion() { return catalogVersion; }
    public List<RunReceipt> receipts() { return receipts; }
    public Optional<FatePointLedger> fatePoints() { return Optional.ofNullable(fatePoints); }
    public Optional<SliceInventory> inventory() { return Optional.ofNullable(inventory); }
}

package com.wheelbound.persistence;

import com.wheelbound.domain.RunLifecycle;
import com.wheelbound.domain.FatePointLedger;
import com.wheelbound.domain.SliceInventory;
import com.wheelbound.domain.ActiveWheel;
import com.wheelbound.domain.FatePointDebits;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** Foundation snapshot and lifecycle/draw/payment journal, not the playable-mode aggregate. */
public final class RunDocument
{
    private final com.wheelbound.domain.GrandEvidenceJournal grandJournal;
    private final com.wheelbound.domain.GrandFateProgress grandProgress;
    private final RunLifecycle lifecycle;
    private final String catalogVersion;
    private final List<RunReceipt> receipts;
    private final FatePointLedger fatePoints;
    private final SliceInventory inventory;
    private final com.wheelbound.domain.SealedGrandFate grandFate;

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
    { this(lifecycle, catalogVersion, receipts, fatePoints, inventory, Optional.empty()); }

    /** Legacy absence is unavailable; evidence alone does not authorize onboarding. */
    public RunDocument(RunLifecycle lifecycle, String catalogVersion, List<RunReceipt> receipts,
        Optional<FatePointLedger> fatePoints, Optional<SliceInventory> inventory,
        Optional<com.wheelbound.domain.SealedGrandFate> grandFate)
    { this(lifecycle, catalogVersion, receipts, fatePoints, inventory, grandFate, Optional.empty()); }

    /** Structural references only; no completion authorization or trusted evidence resolution. */
    public RunDocument(RunLifecycle lifecycle, String catalogVersion, List<RunReceipt> receipts,
        Optional<FatePointLedger> fatePoints, Optional<SliceInventory> inventory,
        Optional<com.wheelbound.domain.SealedGrandFate> grandFate,
        Optional<com.wheelbound.domain.GrandFateProgress> grandProgress)
    { this(lifecycle, catalogVersion, receipts, fatePoints, inventory, grandFate, grandProgress, Optional.empty()); }

    /** Full structural payloads; legacy absence never synthesizes evidence. */
    public RunDocument(RunLifecycle lifecycle, String catalogVersion, List<RunReceipt> receipts,
        Optional<FatePointLedger> fatePoints, Optional<SliceInventory> inventory,
        Optional<com.wheelbound.domain.SealedGrandFate> grandFate,
        Optional<com.wheelbound.domain.GrandFateProgress> grandProgress,
        Optional<com.wheelbound.domain.GrandEvidenceJournal> grandJournal)
    {
        Objects.requireNonNull(lifecycle, "lifecycle");
        this.grandFate = Objects.requireNonNull(grandFate, "grandFate").orElse(null);
        com.wheelbound.domain.GrandFateProgress supplied =
            Objects.requireNonNull(grandProgress, "grandProgress").orElse(null);
        if (supplied != null && (this.grandFate == null
            || !this.grandFate.equals(supplied.checklist().seal())))
        { throw new IllegalArgumentException("Progress requires its original Grand seal"); }
        this.grandProgress = supplied == null ? null : new com.wheelbound.domain.GrandFateProgress(
            this.grandFate, lifecycle.revision(), supplied.evidence().values());
        com.wheelbound.domain.GrandEvidenceJournal journal =
            Objects.requireNonNull(grandJournal, "grandJournal").orElse(null);
        if (journal != null && (this.grandProgress == null
            || !journal.progress().checklist().seal().equals(this.grandFate)
            || !journal.progress().evidence().equals(this.grandProgress.evidence())))
        { throw new IllegalArgumentException("Journal requires exact aggregate progress"); }
        this.grandJournal = journal == null ? null : new com.wheelbound.domain.GrandEvidenceJournal(
            this.grandProgress, lifecycle.revision(), journal.receipts().values());
        if (this.grandFate != null && !this.grandFate.identity().equals(lifecycle.identity()))
        { throw new IllegalArgumentException("Foreign Grand Fate identity"); }
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
            if ((this.grandFate != null && this.grandFate.commandId().equals(receipt.commandId()))
                || !commands.add(receipt.commandId())) { throw new IllegalArgumentException("Duplicate command ID"); }
            if (previous != null && (receipt.beforeRevision() != previous.afterRevision()
                || !follows(previous, receipt)))
            { throw new IllegalArgumentException("Broken lifecycle receipt chain"); }
            previous = receipt;
        }
        if (this.grandProgress != null && this.grandProgress.evidence().values().stream()
            .anyMatch(reference -> commands.contains(reference.receiptId())))
        { throw new IllegalArgumentException("Grand evidence collides with operation journal"); }
        validateDebits();
        validatePurchases();
        if (previous != null && (previous.afterRevision() != lifecycle.revision()
            || previous.state() != lifecycle.state() || previous.obligation() != lifecycle.obligation()))
        { throw new IllegalArgumentException("Receipt head does not match snapshot"); }
    }

    /** Debit entries follow a trusted legacy ledger prefix. Other current journal
     * operations preserve FP; future earning operations require their own validation. */
    private void validateDebits()
    {
        long count = receipts.stream().filter(r -> payment(r) != null).count();
        if (count == 0) { return; }
        if (fatePoints == null || count > fatePoints.entries().size())
        { throw new IllegalArgumentException("Debit journal lacks ledger entries"); }
        int index = fatePoints.entries().size() - Math.toIntExact(count);
        FatePointLedger reconstructed = new FatePointLedger(fatePoints.startingBalance(),
            fatePoints.entries().subList(0, index));
        for (RunReceipt receipt : receipts)
        {
            FatePointDebits.Receipt debit = payment(receipt);
            if (debit == null) { continue; }
            FatePointLedger.Entry entry = fatePoints.entries().get(index++);
            if (entry.kind() != FatePointLedger.Kind.DISCRETIONARY_COST
                || entry.amount() != debit.amount())
            { throw new IllegalArgumentException("Debit journal disagrees with ledger"); }
            reconstructed = reconstructed.append(entry);
            if (reconstructed.balance() != debit.balance())
            { throw new IllegalArgumentException("Debit response disagrees with ledger"); }
        }
    }

    private static FatePointDebits.Receipt payment(RunReceipt receipt)
    { return receipt.purchase() == null ? receipt.debit() : receipt.purchase().debit(); }

    /**
     * Purchase evidence is checked before schema-7 encoding and after decoding. Current
     * journal operations cannot move, enhance or destroy inventory, so each purchased
     * instance must still be active at base tier. Future typed edits must extend this
     * validation rather than treating missing ownership as evidence of destruction.
     * The initial ledger/history prefix remains trusted legacy state; it cannot prove
     * historical UUID freshness. A future command must check its pre-write snapshot.
     */
    private void validatePurchases()
    {
        Set<UUID> instances = new HashSet<>();
        Map<String, Long> tiers = new HashMap<>();
        for (RunReceipt receipt : receipts)
        {
            if (receipt.purchase() == null) { continue; }
            com.wheelbound.domain.SlicePurchaseReceipt purchase = receipt.purchase();
            if (!purchase.identity().equals(lifecycle.identity()) || inventory == null
                || !instances.add(purchase.instanceId()))
            { throw new IllegalArgumentException("Foreign, repeated or unavailable purchase inventory"); }
            String activity = purchase.quote().activity();
            long ordinal = purchase.quote().duplicateOrdinal();
            Long previous = tiers.put(activity, ordinal);
            if (previous != null && (previous == Long.MAX_VALUE || ordinal != previous + 1))
            { throw new IllegalArgumentException("Broken lifetime purchase sequence"); }
            boolean owned = inventory.activeWheel().slices().stream().anyMatch(slice ->
                slice.id().equals(purchase.instanceId()) && slice.kind() == ActiveWheel.Kind.ORDINARY
                    && activity.equals(slice.activity()) && slice.enhancement() == ActiveWheel.Enhancement.BASE);
            if (!owned)
            { throw new IllegalArgumentException("Purchase instance disagrees with inventory"); }
        }
        for (Map.Entry<String, Long> tier : tiers.entrySet())
        {
            SliceInventory.AcquisitionHistory history = inventory.acquisitionHistory().get(tier.getKey());
            if (history == null || history.paidDuplicates() != tier.getValue())
            { throw new IllegalArgumentException("Purchase journal disagrees with lifetime history"); }
        }
    }

    private boolean follows(RunReceipt previous, RunReceipt next)
    {
        switch (next.operation())
        {
            case PAUSE: return previous.state() == RunLifecycle.State.ACTIVE && next.obligation() == previous.obligation();
            case RESUME: return previous.state() == RunLifecycle.State.PAUSED && next.obligation() == previous.obligation();
            case FP_DEBIT: return previous.state() == RunLifecycle.State.ACTIVE && next.obligation() == previous.obligation();
            case SLICE_PURCHASE: return previous.state() == RunLifecycle.State.ACTIVE
                && previous.obligation() == RunLifecycle.Obligation.NONE;
            case DRAW: return previous.state() == RunLifecycle.State.ACTIVE && previous.obligation() == RunLifecycle.Obligation.NONE;
            default: return false;
        }
    }

    public RunLifecycle lifecycle() { return lifecycle; }
    public Optional<com.wheelbound.domain.GrandEvidenceJournal> grandJournal()
    { return Optional.ofNullable(grandJournal); }
    /** Absent legacy references never imply completed objectives. */
    public Optional<com.wheelbound.domain.GrandFateProgress> grandProgress()
    { return Optional.ofNullable(grandProgress); }
    public String catalogVersion() { return catalogVersion; }
    public List<RunReceipt> receipts() { return receipts; }
    public Optional<FatePointLedger> fatePoints() { return Optional.ofNullable(fatePoints); }
    public Optional<SliceInventory> inventory() { return Optional.ofNullable(inventory); }
    public Optional<com.wheelbound.domain.SealedGrandFate> grandFate() { return Optional.ofNullable(grandFate); }
}

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
    private final com.wheelbound.domain.GrandCurrentAttempt grandCurrentAttempt;
    private final com.wheelbound.domain.GrandActivationHistory grandActivations;
    private final com.wheelbound.domain.GrandEvidenceJournal grandJournal;
    private final com.wheelbound.domain.GrandFateProgress grandProgress;
    private final RunLifecycle lifecycle;
    private final com.wheelbound.domain.MasterSelectionResolutions masterResolutions;
    private final com.wheelbound.domain.MasterSelectionCreationJournal masterCreations;
    private final com.wheelbound.domain.MasterSelectionProvenance pendingMasterSelection;
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
        this(lifecycle, catalogVersion, receipts, fatePoints, inventory, grandFate, grandProgress,
            grandJournal, Optional.empty());
    }

    /** Original activation payloads; history never identifies the current attempt. */
    public RunDocument(RunLifecycle lifecycle, String catalogVersion, List<RunReceipt> receipts,
        Optional<FatePointLedger> fatePoints, Optional<SliceInventory> inventory,
        Optional<com.wheelbound.domain.SealedGrandFate> grandFate,
        Optional<com.wheelbound.domain.GrandFateProgress> grandProgress,
        Optional<com.wheelbound.domain.GrandEvidenceJournal> grandJournal,
        Optional<com.wheelbound.domain.GrandActivationHistory> grandActivations)
    {
        this(lifecycle, catalogVersion, receipts, fatePoints, inventory, grandFate, grandProgress,
            grandJournal, grandActivations, Optional.empty());
    }

    /** Explicit structural current state; never inferred from activation history. */
    public RunDocument(RunLifecycle lifecycle, String catalogVersion, List<RunReceipt> receipts,
        Optional<FatePointLedger> fatePoints, Optional<SliceInventory> inventory,
        Optional<com.wheelbound.domain.SealedGrandFate> grandFate,
        Optional<com.wheelbound.domain.GrandFateProgress> grandProgress,
        Optional<com.wheelbound.domain.GrandEvidenceJournal> grandJournal,
        Optional<com.wheelbound.domain.GrandActivationHistory> grandActivations,
        Optional<com.wheelbound.domain.GrandCurrentAttempt> grandCurrentAttempt)
    {
        this(lifecycle, catalogVersion, receipts, fatePoints, inventory, grandFate, grandProgress,
            grandJournal, grandActivations, grandCurrentAttempt, Optional.empty());
    }

    /** Schema-14 structural unresolved selection; absence never starts a selection.
     * Does not authenticate evidence/stream continuity or authorize selection. */
    public RunDocument(RunLifecycle lifecycle, String catalogVersion, List<RunReceipt> receipts,
        Optional<FatePointLedger> fatePoints, Optional<SliceInventory> inventory,
        Optional<com.wheelbound.domain.SealedGrandFate> grandFate,
        Optional<com.wheelbound.domain.GrandFateProgress> grandProgress,
        Optional<com.wheelbound.domain.GrandEvidenceJournal> grandJournal,
        Optional<com.wheelbound.domain.GrandActivationHistory> grandActivations,
        Optional<com.wheelbound.domain.GrandCurrentAttempt> grandCurrentAttempt,
        Optional<com.wheelbound.domain.MasterSelectionProvenance> pendingMasterSelection)
    {
        this(lifecycle, catalogVersion, receipts, fatePoints, inventory, grandFate, grandProgress,
            grandJournal, grandActivations, grandCurrentAttempt, pendingMasterSelection, Optional.empty());
    }

    /** Structural original creation history; absent legacy history is not empty history. */
    public RunDocument(RunLifecycle lifecycle, String catalogVersion, List<RunReceipt> receipts,
        Optional<FatePointLedger> fatePoints, Optional<SliceInventory> inventory,
        Optional<com.wheelbound.domain.SealedGrandFate> grandFate,
        Optional<com.wheelbound.domain.GrandFateProgress> grandProgress,
        Optional<com.wheelbound.domain.GrandEvidenceJournal> grandJournal,
        Optional<com.wheelbound.domain.GrandActivationHistory> grandActivations,
        Optional<com.wheelbound.domain.GrandCurrentAttempt> grandCurrentAttempt,
        Optional<com.wheelbound.domain.MasterSelectionProvenance> pendingMasterSelection,
        Optional<com.wheelbound.domain.MasterSelectionCreationJournal> masterCreations)
    {
        this(lifecycle, catalogVersion, receipts, fatePoints, inventory, grandFate, grandProgress,
            grandJournal, grandActivations, grandCurrentAttempt, pendingMasterSelection, masterCreations,
            Optional.empty());
    }

    /** Structural terminal history only; no assignment, spin accounting or persistence authority.
     * Absent legacy history is unavailable, never evidence of no previous resolutions. */
    public RunDocument(RunLifecycle lifecycle, String catalogVersion, List<RunReceipt> receipts,
        Optional<FatePointLedger> fatePoints, Optional<SliceInventory> inventory,
        Optional<com.wheelbound.domain.SealedGrandFate> grandFate,
        Optional<com.wheelbound.domain.GrandFateProgress> grandProgress,
        Optional<com.wheelbound.domain.GrandEvidenceJournal> grandJournal,
        Optional<com.wheelbound.domain.GrandActivationHistory> grandActivations,
        Optional<com.wheelbound.domain.GrandCurrentAttempt> grandCurrentAttempt,
        Optional<com.wheelbound.domain.MasterSelectionProvenance> pendingMasterSelection,
        Optional<com.wheelbound.domain.MasterSelectionCreationJournal> masterCreations,
        Optional<com.wheelbound.domain.MasterSelectionResolutions> masterResolutions)
    {
        Objects.requireNonNull(lifecycle, "lifecycle");
        this.masterResolutions = Objects.requireNonNull(masterResolutions, "masterResolutions")
            .map(history -> new com.wheelbound.domain.MasterSelectionResolutions(history.identity(),
                lifecycle.revision(), history.receipts().values())).orElse(null);
        com.wheelbound.domain.MasterSelectionCreationJournal creations =
            Objects.requireNonNull(masterCreations, "masterCreations").orElse(null);
        if (creations != null && !creations.identity().equals(lifecycle.identity()))
        { throw new IllegalArgumentException("Foreign Master creation history"); }
        this.masterCreations = creations == null ? null : new com.wheelbound.domain.MasterSelectionCreationJournal(
            lifecycle.identity(), lifecycle.revision(), creations.receipts().values());
        this.pendingMasterSelection = Objects.requireNonNull(pendingMasterSelection, "pendingMasterSelection").orElse(null);
        if (this.pendingMasterSelection != null
            && (this.pendingMasterSelection.hasValidLanding()
                || !this.pendingMasterSelection.catalogVersion().equals(catalogVersion)
                || (lifecycle.state() != RunLifecycle.State.ACTIVE && lifecycle.state() != RunLifecycle.State.PAUSED)
                || lifecycle.obligation() != RunLifecycle.Obligation.NONE
                || grandCurrentAttempt.isPresent()))
        { throw new IllegalArgumentException("Pending Master selection requires unresolved unobligated active/paused context"); }
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
        com.wheelbound.domain.GrandActivationHistory activations =
            Objects.requireNonNull(grandActivations, "grandActivations").orElse(null);
        if (activations != null && !activations.seal().equals(this.grandFate))
        { throw new IllegalArgumentException("Activation history requires exact original seal"); }
        this.grandActivations = activations == null ? null : new com.wheelbound.domain.GrandActivationHistory(
            this.grandFate, lifecycle.revision(), activations.receipts().values());
        com.wheelbound.domain.GrandCurrentAttempt attempt =
            Objects.requireNonNull(grandCurrentAttempt, "grandCurrentAttempt").orElse(null);
        if (attempt != null)
        {
            if (this.grandActivations == null
                || !attempt.lifecycle().identity().equals(lifecycle.identity())
                || attempt.lifecycle().revision() != lifecycle.revision()
                || attempt.lifecycle().state() != lifecycle.state()
                || attempt.lifecycle().obligation() != lifecycle.obligation()
                || !attempt.activation().equals(this.grandActivations.receipts().get(attempt.activation().commandId())))
            { throw new IllegalArgumentException("Current attempt requires exact aggregate context"); }
            this.grandCurrentAttempt = com.wheelbound.domain.GrandCurrentAttempt.restore(
                attempt.activation().commandId(), this.grandActivations, lifecycle,
                this.grandProgress == null ? com.wheelbound.domain.GrandFateChecklist.initial(this.grandFate)
                    : this.grandProgress.checklist());
        }
        else { this.grandCurrentAttempt = null; }
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
            if (receipt.masterCreation() != null
                && (this.masterCreations == null || !receipt.masterCreation().equals(
                    this.masterCreations.receipts().get(receipt.commandId()))))
            { throw new IllegalArgumentException("Creation operation requires exact original history"); }
            if (receipt.operation() == RunReceipt.Operation.GRAND_ACTIVATION
                && (this.grandActivations == null || !receipt.activation().equals(
                    this.grandActivations.receipts().get(receipt.commandId()))))
            { throw new IllegalArgumentException("Activation journal requires exact history payload"); }
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
        if (this.grandActivations != null)
        {
            for (com.wheelbound.domain.GrandActivationReceipt activation : this.grandActivations.receipts().values())
            {
                RunReceipt matching = this.receipts.stream()
                    .filter(r -> r.commandId().equals(activation.commandId())).findFirst().orElse(null);
                if ((matching != null && !activation.equals(matching.activation()))
                    || (matching == null && !this.receipts.isEmpty()
                        && activation.afterRevision() > this.receipts.get(0).beforeRevision())
                    || (this.grandProgress != null
                    && this.grandProgress.evidence().values().stream()
                        .anyMatch(reference -> reference.receiptId().equals(activation.commandId()))))
                { throw new IllegalArgumentException("Grand activation command collision"); }
                if (!activation.attempt().checklist().completed().isEmpty()
                    && (this.grandProgress == null || !this.grandProgress.checklist().completed()
                        .containsAll(activation.attempt().checklist().completed())))
                { throw new IllegalArgumentException("Activation prefix missing from aggregate progress"); }
                for (com.wheelbound.domain.GrandFateChecklist.Objective completed : activation.attempt().checklist().completed())
                {
                    if (this.grandProgress.evidence().get(completed).verifiedRevision() > activation.beforeRevision())
                    { throw new IllegalArgumentException("Activation prefix contains later evidence"); }
                }
            }
        }
        if (this.pendingMasterSelection != null)
        {
            UUID transaction = this.pendingMasterSelection.transactionId();
            if ((commands.contains(transaction) && !matchingCreation(transaction))
                || (this.grandFate != null && this.grandFate.commandId().equals(transaction))
                || (this.grandActivations != null && this.grandActivations.receipts().containsKey(transaction))
                || (this.grandProgress != null && this.grandProgress.evidence().values().stream()
                    .anyMatch(reference -> reference.receiptId().equals(transaction))))
            { throw new IllegalArgumentException("Pending selection transaction collides with retained history"); }
        }
        if (this.masterCreations != null)
        {
            for (UUID transaction : this.masterCreations.receipts().keySet())
            {
                if (!matchingCreation(transaction) && !this.receipts.isEmpty()
                    && this.masterCreations.receipts().get(transaction).afterRevision()
                        > this.receipts.get(0).beforeRevision())
                { throw new IllegalArgumentException("Creation missing from retained operation chain"); }
                if ((commands.contains(transaction) && !matchingCreation(transaction))
                    || (this.grandFate != null && this.grandFate.commandId().equals(transaction))
                    || (this.grandActivations != null && this.grandActivations.receipts().containsKey(transaction))
                    || (this.grandProgress != null && this.grandProgress.evidence().values().stream()
                        .anyMatch(reference -> reference.receiptId().equals(transaction))))
                { throw new IllegalArgumentException("Master creation collides with retained history"); }
            }
            if (this.pendingMasterSelection != null)
            { this.masterCreations.matchPending(this.pendingMasterSelection, lifecycle.identity(), lifecycle.revision()); }
        }
        if (this.masterResolutions != null)
        { MasterResolutionBinding.validateHistory(this, this.masterResolutions); }
        validateDebits();
        validatePurchases();
        if (previous != null && (previous.afterRevision() != lifecycle.revision()
            || previous.state() != lifecycle.state() || previous.obligation() != lifecycle.obligation()))
        { throw new IllegalArgumentException("Receipt head does not match snapshot"); }
    }

    private boolean matchingCreation(UUID transaction)
    {
        return masterCreations != null && receipts.stream().anyMatch(r ->
            r.commandId().equals(transaction) && r.masterCreation() != null
                && r.masterCreation().equals(masterCreations.receipts().get(transaction)));
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
            case MASTER_SELECTION_CREATION: return previous.state() == RunLifecycle.State.ACTIVE
                && previous.obligation() == RunLifecycle.Obligation.NONE;
            case GRAND_ACTIVATION: return previous.state() == RunLifecycle.State.ACTIVE
                && previous.obligation() == RunLifecycle.Obligation.NONE;
            default: return false;
        }
    }

    public Optional<com.wheelbound.domain.GrandActivationHistory> grandActivations()
    { return Optional.ofNullable(grandActivations); }
    /** Reconcile retained activity to a new head; never infer activity from history. */
    Optional<com.wheelbound.domain.GrandCurrentAttempt> retainedGrandAttempt(RunLifecycle next)
    {
        return grandCurrentAttempt().map(attempt -> com.wheelbound.domain.GrandCurrentAttempt.restore(
            attempt.activation().commandId(), grandActivations, next,
            grandProgress == null ? com.wheelbound.domain.GrandFateChecklist.initial(grandFate)
                : grandProgress.checklist()));
    }

    public Optional<com.wheelbound.domain.GrandCurrentAttempt> grandCurrentAttempt()
    { return Optional.ofNullable(grandCurrentAttempt); }
    public Optional<com.wheelbound.domain.MasterSelectionCreationJournal> masterCreations()
    { return Optional.ofNullable(masterCreations); }
    public Optional<com.wheelbound.domain.MasterSelectionResolutions> masterResolutions()
    { return Optional.ofNullable(masterResolutions); }
    public RunLifecycle lifecycle() { return lifecycle; }
    public Optional<com.wheelbound.domain.MasterSelectionProvenance> pendingMasterSelection()
    { return Optional.ofNullable(pendingMasterSelection); }
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

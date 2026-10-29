package com.wheelbound.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Immutable ownership snapshot and proposed edits. No authorized purchases, paid movement or grants.
 * Catalog admission and acquisition evidence are responsibilities of the restoring caller.
 */
public final class SliceInventory
{
    public enum PurchaseKind { FIRST_INSTANCE, PAID_DUPLICATE }

    /** Price-ladder input only, not a quote, purchase authorization or receipt. */
    public static final class PurchaseClassification
    {
        private final PurchaseKind kind;
        private final long duplicateOrdinal;

        private PurchaseClassification(PurchaseKind kind, long duplicateOrdinal)
        {
            this.kind = kind;
            this.duplicateOrdinal = duplicateOrdinal;
        }

        public PurchaseKind kind() { return kind; }

        /** One-based duplicate tier; first-instance pricing has no duplicate tier. */
        public long duplicateOrdinal()
        {
            if (kind != PurchaseKind.PAID_DUPLICATE)
            { throw new IllegalStateException("First instance has no duplicate ordinal"); }
            return duplicateOrdinal;
        }
    }

    public enum SatchelTier
    {
        BASE(3), FIRST(6), SECOND(10), THIRD(15);
        private final int capacity;
        SatchelTier(int capacity) { this.capacity = capacity; }
        public int capacity() { return capacity; }
    }

    /** Presence records the first-ever instance; starting grants have zero paid duplicates. */
    public static final class AcquisitionHistory
    {
        private final long paidDuplicates;

        public AcquisitionHistory(long paidDuplicates)
        {
            if (paidDuplicates < 0) { throw new IllegalArgumentException("Negative duplicate count"); }
            this.paidDuplicates = paidDuplicates;
        }

        public long paidDuplicates() { return paidDuplicates; }
    }

    private final ActiveWheel activeWheel;
    private final List<ActiveWheel.Slice> stored;
    private final SatchelTier satchelTier;
    private final Set<String> unlockedActivities;
    private final Map<String, AcquisitionHistory> acquisitionHistory;

    public SliceInventory(ActiveWheel activeWheel, List<ActiveWheel.Slice> stored,
        SatchelTier satchelTier, Set<String> unlockedActivities,
        Map<String, AcquisitionHistory> acquisitionHistory)
    {
        this.activeWheel = Objects.requireNonNull(activeWheel, "activeWheel");
        this.stored = List.copyOf(stored);
        this.satchelTier = Objects.requireNonNull(satchelTier, "satchelTier");
        this.unlockedActivities = Set.copyOf(unlockedActivities);
        this.acquisitionHistory = Map.copyOf(acquisitionHistory);
        if (this.stored.size() > satchelTier.capacity())
        { throw new IllegalArgumentException("Satchel exceeds capacity"); }
        for (String activity : this.unlockedActivities) { validateKey(activity); }
        for (String activity : this.acquisitionHistory.keySet())
        {
            validateKey(activity);
            if (!this.unlockedActivities.contains(activity))
            { throw new IllegalArgumentException("Acquisition requires retained activity unlock"); }
        }
        Set<UUID> ids = new HashSet<>();
        for (ActiveWheel.Slice slice : activeWheel.slices()) { validateOwned(slice, ids); }
        for (ActiveWheel.Slice slice : this.stored)
        {
            if (slice.kind() != ActiveWheel.Kind.ORDINARY)
            { throw new IllegalArgumentException("Special slices cannot enter Satchel"); }
            validateOwned(slice, ids);
        }
    }

    private static void validateKey(String activity)
    {
        if (activity.isEmpty() || !activity.equals(activity.trim()))
        { throw new IllegalArgumentException("Invalid activity key"); }
    }

    private void validateOwned(ActiveWheel.Slice slice, Set<UUID> ids)
    {
        if (!ids.add(slice.id())) { throw new IllegalArgumentException("Duplicate owned slice ID"); }
        if (slice.kind() == ActiveWheel.Kind.ORDINARY
            && !acquisitionHistory.containsKey(slice.activity()))
        { throw new IllegalArgumentException("Owned ordinary slice requires acquisition history"); }
    }

    public ActiveWheel activeWheel() { return activeWheel; }
    public List<ActiveWheel.Slice> stored() { return stored; }
    public SatchelTier satchelTier() { return satchelTier; }
    public Set<String> unlockedActivities() { return unlockedActivities; }
    public Map<String, AcquisitionHistory> acquisitionHistory() { return acquisitionHistory; }

    /**
     * Propose a new base ordinary instance in an available active slot (WB-D106/118).
     * The supplied ID must be fresh across both owned locations. Only the returned proposal
     * advances history; this snapshot remains unchanged. Repeating a proposal is not replay
     * protection. A caller must validate the frozen quote against classifyNewPurchase and
     * atomically persist debit, inventory and command receipt before publishing success.
     * No payment, grant, edit authorization or full-wheel replacement is performed here.
     */
    public SliceInventory proposeNewPurchase(UUID instanceId, String activity)
    {
        Objects.requireNonNull(instanceId, "instanceId");
        PurchaseClassification classification = classifyNewPurchase(activity);
        java.util.ArrayList<ActiveWheel.Slice> active = new java.util.ArrayList<>(activeWheel.slices());
        active.add(ActiveWheel.Slice.ordinary(instanceId, activity, ActiveWheel.Enhancement.BASE));
        java.util.Map<String, AcquisitionHistory> history = new java.util.HashMap<>(acquisitionHistory);
        history.put(activity, new AcquisitionHistory(classification.kind() == PurchaseKind.FIRST_INSTANCE
            ? 0 : classification.duplicateOrdinal()));
        return new SliceInventory(new ActiveWheel(active), stored, satchelTier,
            unlockedActivities, history);
    }

    /**
     * Propose storing an active ordinary instance (including Questing), preserving its identity.
     * This is not a free edit or payment authorization. The aggregate caller must enforce the
     * legal edit boundary, identity/revision and reviewed FP quote, then persist the debit,
     * complete inventory and replay receipt atomically before publishing success.
     */
    public SliceInventory proposeStorage(UUID activeId)
    {
        Objects.requireNonNull(activeId, "activeId");
        java.util.ArrayList<ActiveWheel.Slice> active = new java.util.ArrayList<>(activeWheel.slices());
        ActiveWheel.Slice selected = removeInstance(active, activeId);
        if (selected.kind() != ActiveWheel.Kind.ORDINARY)
        { throw new IllegalArgumentException("Special slices cannot enter Satchel"); }
        java.util.ArrayList<ActiveWheel.Slice> storage = new java.util.ArrayList<>(stored);
        storage.add(selected);
        return new SliceInventory(new ActiveWheel(active), storage, satchelTier,
            unlockedActivities, acquisitionHistory);
    }

    /**
     * Propose returning a stored instance to the end of the active wheel, without acquisition.
     * Uses the same paid-command requirements as proposeStorage; no price or debit is supplied here.
     */
    public SliceInventory proposeReactivation(UUID storedId)
    {
        Objects.requireNonNull(storedId, "storedId");
        java.util.ArrayList<ActiveWheel.Slice> storage = new java.util.ArrayList<>(stored);
        ActiveWheel.Slice selected = removeInstance(storage, storedId);
        java.util.ArrayList<ActiveWheel.Slice> active = new java.util.ArrayList<>(activeWheel.slices());
        active.add(selected);
        return new SliceInventory(new ActiveWheel(active), storage, satchelTier,
            unlockedActivities, acquisitionHistory);
    }

    private static ActiveWheel.Slice removeInstance(List<ActiveWheel.Slice> slices, UUID id)
    {
        for (int i = 0; i < slices.size(); i++)
        {
            if (slices.get(i).id().equals(id)) { return slices.remove(i); }
        }
        throw new IllegalArgumentException("Slice not in requested location");
    }

    /**
     * Propose permanent, no-refund destruction of explicitly selected owned ordinary instances.
     * WB-D105/106/118/121: retain unlocks/history, protect Questing, validate the final wheel.
     * This does not authorize an edit or publish success. The aggregate command caller must
     * enforce identity/revision/legal edit boundary and persist inventory and receipt atomically.
     * Catalog keys must be canonical (Questing is "questing").
     */
    public SliceInventory proposeDestruction(Set<UUID> selectedIds)
    {
        Set<UUID> remaining = new HashSet<>(Set.copyOf(selectedIds));
        if (remaining.isEmpty()) { throw new IllegalArgumentException("No destruction selected"); }
        java.util.ArrayList<ActiveWheel.Slice> active = new java.util.ArrayList<>();
        java.util.ArrayList<ActiveWheel.Slice> storage = new java.util.ArrayList<>();
        for (ActiveWheel.Slice slice : activeWheel.slices())
        {
            if (remaining.remove(slice.id())) { requireDestroyable(slice); }
            else { active.add(slice); }
        }
        for (ActiveWheel.Slice slice : stored)
        {
            if (remaining.remove(slice.id())) { requireDestroyable(slice); }
            else { storage.add(slice); }
        }
        if (!remaining.isEmpty()) { throw new IllegalArgumentException("Unknown owned slice ID"); }
        return new SliceInventory(new ActiveWheel(active), storage, satchelTier,
            unlockedActivities, acquisitionHistory);
    }

    private static void requireDestroyable(ActiveWheel.Slice slice)
    {
        if (slice.kind() != ActiveWheel.Kind.ORDINARY || "questing".equals(slice.activity()))
        { throw new IllegalArgumentException("Slice cannot be destroyed"); }
    }

    /**
     * Classify a proposed new ordinary instance using retained lifetime evidence (WB-D118).
     * Never use this for movement of an existing instance. Does not reserve capacity,
     * validate edit readiness, freeze prices or advance history. A future transaction must
     * revalidate this snapshot and atomically persist debit, instance, history and receipt.
     */
    public PurchaseClassification classifyNewPurchase(String activity)
    {
        Objects.requireNonNull(activity, "activity");
        validateKey(activity);
        if (!unlockedActivities.contains(activity))
        { throw new IllegalArgumentException("Purchase requires activity unlock"); }
        AcquisitionHistory history = acquisitionHistory.get(activity);
        if (history == null)
        { return new PurchaseClassification(PurchaseKind.FIRST_INSTANCE, 0); }
        return new PurchaseClassification(PurchaseKind.PAID_DUPLICATE,
            Math.addExact(history.paidDuplicates(), 1));
    }
}

package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.wheelbound.domain.ActiveWheel.*;
import static com.wheelbound.domain.SliceInventory.*;

public class SliceInventoryTest
{
    @Test public void questingStorageAndReturnPreserveInstanceAndLifetimeHistory()
    {
        Slice questing = slice("questing");
        Slice existingStored = slice("mining");
        List<Slice> active = new ArrayList<>(wheel().slices());
        active.add(questing);
        SliceInventory original = new SliceInventory(new ActiveWheel(active),
            List.of(existingStored), SatchelTier.BASE, unlocks(), history());
        SliceInventory stored = original.proposeStorage(questing.id());
        assertEquals(active.subList(0, 5), stored.activeWheel().slices());
        assertEquals(List.of(existingStored, questing), stored.stored());
        assertSame(questing, stored.stored().get(1));
        SliceInventory returned = stored.proposeReactivation(questing.id());
        assertEquals(active, returned.activeWheel().slices());
        assertSame(questing, returned.activeWheel().slices().get(5));
        assertEquals(Enhancement.FOURTH, questing.enhancement());
        assertEquals(12, questing.weightUnits());
        assertEquals(List.of(existingStored), returned.stored());
        assertEquals(original.acquisitionHistory(), returned.acquisitionHistory());
        assertEquals(original.unlockedActivities(), returned.unlockedActivities());
        assertEquals(original.satchelTier(), returned.satchelTier());
        assertEquals(8, returned.classifyNewPurchase("questing").duplicateOrdinal());
        assertEquals(active, original.activeWheel().slices());
        assertEquals(List.of(existingStored), original.stored());
        assertThrows(IllegalArgumentException.class, () -> stored.proposeStorage(questing.id()));
        assertThrows(IllegalArgumentException.class, () -> returned.proposeReactivation(questing.id()));
    }

    @Test public void movementRejectsCapacitySpecialWrongLocationAndInvalidFinalWheel()
    {
        List<Slice> active = new ArrayList<>(wheel().slices());
        Slice extra = slice("questing");
        Slice taint = Slice.special(UUID.randomUUID(), Kind.TAINT, 4);
        Slice sacrifice = Slice.special(UUID.randomUUID(), Kind.SACRIFICE, 4);
        active.addAll(List.of(extra, taint, sacrifice));
        List<Slice> storage = List.of(slice("mining"), slice("fishing"), slice("questing"));
        SliceInventory full = new SliceInventory(new ActiveWheel(active), storage,
            SatchelTier.BASE, unlocks(), history());
        for (UUID id : List.of(extra.id(), taint.id(), sacrifice.id(), storage.get(0).id(), UUID.randomUUID()))
        { assertThrows(IllegalArgumentException.class, () -> full.proposeStorage(id)); }
        assertThrows(IllegalArgumentException.class, () -> full.proposeReactivation(extra.id()));
        assertThrows(IllegalArgumentException.class, () -> full.proposeReactivation(UUID.randomUUID()));
        assertThrows(NullPointerException.class, () -> full.proposeStorage(null));
        assertThrows(NullPointerException.class, () -> full.proposeReactivation(null));
        assertEquals(active, full.activeWheel().slices());
        assertEquals(storage, full.stored());
        // Special storage remains forbidden even when there is room.
        SliceInventory room = new SliceInventory(new ActiveWheel(active), List.of(),
            SatchelTier.BASE, unlocks(), history());
        assertThrows(IllegalArgumentException.class, () -> room.proposeStorage(taint.id()));
        assertThrows(IllegalArgumentException.class, () -> room.proposeStorage(sacrifice.id()));
        SliceInventory minimum = new SliceInventory(wheel(), List.of(), SatchelTier.BASE, unlocks(), history());
        assertThrows(IllegalArgumentException.class,
            () -> minimum.proposeStorage(minimum.activeWheel().slices().get(0).id()));
        List<Slice> six = new ArrayList<>(wheel().slices());
        six.add(slice("mining"));
        SliceInventory diversity = new SliceInventory(new ActiveWheel(six), List.of(),
            SatchelTier.BASE, unlocks(), history());
        assertThrows(IllegalArgumentException.class, () -> diversity.proposeStorage(six.get(2).id()));
        while (active.size() < 24) { active.add(slice("mining")); }
        SliceInventory fullWheel = new SliceInventory(new ActiveWheel(active), storage,
            SatchelTier.BASE, unlocks(), history());
        assertThrows(IllegalArgumentException.class,
            () -> fullWheel.proposeReactivation(storage.get(0).id()));
        assertEquals(24, fullWheel.activeWheel().slices().size());
        assertEquals(storage, fullWheel.stored());
    }

    @Test public void destructionPreservesSurvivorsUnlocksAndLifetimePricing()
    {
        Slice extra = slice("mining");
        List<Slice> active = new ArrayList<>(wheel().slices());
        active.add(extra);
        Slice stored = slice("questing");
        Slice removable = slice("mining");
        SliceInventory original = new SliceInventory(new ActiveWheel(active),
            List.of(stored, removable), SatchelTier.BASE, unlocks(), history());
        SliceInventory result = original.proposeDestruction(Set.of(extra.id(), removable.id()));
        assertEquals(active.subList(0, 5), result.activeWheel().slices());
        assertEquals(List.of(stored), result.stored());
        assertSame(stored, result.stored().get(0));
        assertEquals(original.acquisitionHistory(), result.acquisitionHistory());
        assertEquals(original.unlockedActivities(), result.unlockedActivities());
        assertEquals(original.satchelTier(), result.satchelTier());
        assertEquals(2, result.classifyNewPurchase("mining").duplicateOrdinal());
        assertEquals(6, original.activeWheel().slices().size());
        assertEquals(2, original.stored().size());
        // Destroying the last copy still retains its lifetime purchase tier and unlock.
        SliceInventory last = new SliceInventory(wheel(), List.of(slice("woodcutting")),
            SatchelTier.BASE, unlocks(), Map.of("mining", new AcquisitionHistory(1),
                "fishing", new AcquisitionHistory(1), "combat", new AcquisitionHistory(0),
                "woodcutting", new AcquisitionHistory(4)));
        SliceInventory empty = last.proposeDestruction(Set.of(last.stored().get(0).id()));
        assertTrue(empty.stored().isEmpty());
        assertEquals(5, empty.classifyNewPurchase("woodcutting").duplicateOrdinal());
    }

    @Test public void destructionRejectsProtectedUnknownAndInvalidFinalWheelAtomically()
    {
        Slice questing = slice("questing");
        Slice special = Slice.special(UUID.randomUUID(), Kind.TAINT, 4);
        Slice sacrifice = Slice.special(UUID.randomUUID(), Kind.SACRIFICE, 4);
        List<Slice> active = new ArrayList<>(wheel().slices());
        active.addAll(List.of(questing, special, sacrifice));
        Slice storedQuesting = slice("questing");
        Slice removable = slice("mining");
        SliceInventory inventory = new SliceInventory(new ActiveWheel(active),
            List.of(storedQuesting, removable), SatchelTier.BASE, unlocks(), history());
        for (UUID forbidden : List.of(questing.id(), storedQuesting.id(), special.id(),
            sacrifice.id(), UUID.randomUUID()))
        {
            assertThrows(IllegalArgumentException.class,
                () -> inventory.proposeDestruction(Set.of(removable.id(), forbidden)));
        }
        assertThrows(IllegalArgumentException.class, () -> inventory.proposeDestruction(Set.of()));
        assertThrows(NullPointerException.class, () -> inventory.proposeDestruction(null));
        assertEquals(List.of(storedQuesting, removable), inventory.stored());
        SliceInventory minimum = new SliceInventory(wheel(), List.of(), SatchelTier.BASE,
            unlocks(), history());
        assertThrows(IllegalArgumentException.class,
            () -> minimum.proposeDestruction(Set.of(minimum.activeWheel().slices().get(0).id())));
        // Six ordinary instances but only one Combat: removing it leaves five across two activities.
        List<Slice> six = new ArrayList<>(wheel().slices());
        six.add(slice("mining"));
        SliceInventory diversity = new SliceInventory(new ActiveWheel(six), List.of(),
            SatchelTier.BASE, unlocks(), history());
        assertThrows(IllegalArgumentException.class,
            () -> diversity.proposeDestruction(Set.of(six.get(2).id())));
        assertEquals(6, diversity.activeWheel().slices().size());
    }

    @Test public void purchaseClassificationUsesLifetimeEvidenceAndNeverAdvancesIt()
    {
        SliceInventory inventory = new SliceInventory(wheel(), List.of(), SatchelTier.BASE,
            unlocks(), history());
        PurchaseClassification first = inventory.classifyNewPurchase("woodcutting");
        assertEquals(PurchaseKind.FIRST_INSTANCE, first.kind());
        assertThrows(IllegalStateException.class, first::duplicateOrdinal);
        // Starting grant baseline means the first paid new instance is duplicate tier one.
        assertEquals(PurchaseKind.PAID_DUPLICATE, inventory.classifyNewPurchase("combat").kind());
        assertEquals(1, inventory.classifyNewPurchase("combat").duplicateOrdinal());
        assertEquals(2, inventory.classifyNewPurchase("mining").duplicateOrdinal());
        // No currently owned Questing copies does not restore first-instance pricing.
        for (int i = 0; i < 3; i++)
        { assertEquals(8, inventory.classifyNewPurchase("questing").duplicateOrdinal()); }
        SliceInventory stored = new SliceInventory(inventory.activeWheel(), List.of(slice("questing")),
            SatchelTier.BASE, unlocks(), history());
        assertEquals(8, stored.classifyNewPurchase("questing").duplicateOrdinal());
        assertEquals(7, inventory.acquisitionHistory().get("questing").paidDuplicates());
        assertFalse(inventory.acquisitionHistory().containsKey("woodcutting"));
        assertTrue(inventory.stored().isEmpty());
    }

    @Test public void purchaseClassificationRejectsLockedMalformedAndOverflowInputs()
    {
        Map<String, AcquisitionHistory> history = new HashMap<>(history());
        history.put("questing", new AcquisitionHistory(Long.MAX_VALUE));
        history.put("combat", new AcquisitionHistory(Long.MAX_VALUE - 1));
        SliceInventory inventory = new SliceInventory(wheel(), List.of(), SatchelTier.BASE,
            unlocks(), history);
        assertEquals(Long.MAX_VALUE, inventory.classifyNewPurchase("combat").duplicateOrdinal());
        assertThrows(ArithmeticException.class, () -> inventory.classifyNewPurchase("questing"));
        assertEquals(Long.MAX_VALUE, inventory.acquisitionHistory().get("questing").paidDuplicates());
        for (String key : List.of("locked", "", " mining", "mining "))
        { assertThrows(IllegalArgumentException.class, () -> inventory.classifyNewPurchase(key)); }
        assertThrows(NullPointerException.class, () -> inventory.classifyNewPurchase(null));
    }

    private static Slice slice(String activity)
    { return Slice.ordinary(UUID.randomUUID(), activity, Enhancement.FOURTH); }

    private static ActiveWheel wheel()
    {
        return new ActiveWheel(List.of(slice("mining"), slice("fishing"),
            slice("combat"), slice("mining"), slice("fishing")));
    }

    private static Set<String> unlocks()
    { return Set.of("mining", "fishing", "combat", "questing", "woodcutting"); }

    private static Map<String, AcquisitionHistory> history()
    {
        return Map.of("mining", new AcquisitionHistory(1),
            "fishing", new AcquisitionHistory(1), "combat", new AcquisitionHistory(0),
            "questing", new AcquisitionHistory(7));
    }

    @Test public void ownershipPreservesInstancesAndHistoricalEvidenceWithoutCurrentCopies()
    {
        ActiveWheel active = wheel();
        Slice questing = slice("questing");
        List<Slice> stored = new ArrayList<>(List.of(questing));
        Set<String> unlocked = new HashSet<>(unlocks());
        Map<String, AcquisitionHistory> history = new HashMap<>(history());
        SliceInventory inventory = new SliceInventory(active, stored, SatchelTier.BASE, unlocked, history);
        stored.clear();
        unlocked.clear();
        history.clear();
        assertSame(active, inventory.activeWheel());
        assertSame(questing, inventory.stored().get(0));
        assertEquals(Enhancement.FOURTH, inventory.stored().get(0).enhancement());
        assertEquals(7, inventory.acquisitionHistory().get("questing").paidDuplicates());
        assertEquals(0, inventory.acquisitionHistory().get("combat").paidDuplicates());
        // Unlock alone is not evidence of any acquisition.
        assertTrue(inventory.unlockedActivities().contains("woodcutting"));
        assertFalse(inventory.acquisitionHistory().containsKey("woodcutting"));
        SliceInventory noCopies = new SliceInventory(active, List.of(), SatchelTier.BASE,
            inventory.unlockedActivities(), inventory.acquisitionHistory());
        assertEquals(7, noCopies.acquisitionHistory().get("questing").paidDuplicates());
        assertThrows(UnsupportedOperationException.class, () -> inventory.stored().clear());
        assertThrows(UnsupportedOperationException.class, () -> inventory.unlockedActivities().clear());
        assertThrows(UnsupportedOperationException.class, () -> inventory.acquisitionHistory().clear());
    }

    @Test public void approvedCapacitiesApplyOnlyToStoredOrdinaryInstances()
    {
        assertArrayEquals(new int[]{3, 6, 10, 15},
            java.util.Arrays.stream(SatchelTier.values()).mapToInt(SatchelTier::capacity).toArray());
        List<Slice> fullWheel = new ArrayList<>(wheel().slices());
        while (fullWheel.size() < 24)
        { fullWheel.add(Slice.special(UUID.randomUUID(), Kind.TAINT, 4)); }
        for (SatchelTier tier : SatchelTier.values())
        {
            List<Slice> stored = new ArrayList<>();
            for (int i = 0; i < tier.capacity(); i++) { stored.add(slice("questing")); }
            SliceInventory inventory = new SliceInventory(new ActiveWheel(fullWheel), stored,
                tier, unlocks(), history());
            assertEquals(tier.capacity(), inventory.stored().size());
            assertEquals(24, inventory.activeWheel().slices().size());
            stored.add(slice("mining"));
            assertThrows(IllegalArgumentException.class,
                () -> new SliceInventory(wheel(), stored, tier, unlocks(), history()));
        }
    }

    @Test public void malformedOwnershipAndSpecialStorageFailClosed()
    {
        ActiveWheel active = wheel();
        assertThrows(IllegalArgumentException.class, () -> new SliceInventory(active,
            List.of(active.slices().get(0)), SatchelTier.BASE, unlocks(), history()));
        Slice duplicate = slice("mining");
        assertThrows(IllegalArgumentException.class, () -> new SliceInventory(active,
            List.of(duplicate, duplicate), SatchelTier.BASE, unlocks(), history()));
        for (Kind kind : List.of(Kind.TAINT, Kind.SACRIFICE))
        {
            assertThrows(IllegalArgumentException.class, () -> new SliceInventory(active,
                List.of(Slice.special(UUID.randomUUID(), kind, 4)), SatchelTier.BASE, unlocks(), history()));
        }
        assertThrows(IllegalArgumentException.class, () -> new SliceInventory(active,
            List.of(slice("woodcutting")), SatchelTier.BASE, unlocks(), history()));
        Map<String, AcquisitionHistory> invalid = new HashMap<>(history());
        invalid.put("locked", new AcquisitionHistory(0));
        assertThrows(IllegalArgumentException.class, () -> new SliceInventory(active,
            List.of(), SatchelTier.BASE, unlocks(), invalid));
        assertThrows(IllegalArgumentException.class, () -> new AcquisitionHistory(-1));
        assertThrows(IllegalArgumentException.class, () -> new SliceInventory(active,
            List.of(), SatchelTier.BASE, Set.of(" "), Map.of()));
        assertThrows(NullPointerException.class, () -> new SliceInventory(active,
            java.util.Arrays.asList((Slice) null), SatchelTier.BASE, unlocks(), history()));
    }
}

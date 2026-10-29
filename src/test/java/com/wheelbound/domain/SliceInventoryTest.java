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

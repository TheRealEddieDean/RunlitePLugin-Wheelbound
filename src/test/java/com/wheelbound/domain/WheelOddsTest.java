package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.wheelbound.domain.ActiveWheel.*;

public class WheelOddsTest
{
    private static List<Slice> ordinary()
    {
        List<Slice> slices = new ArrayList<>();
        int i = 0;
        for (Enhancement tier : Enhancement.values())
        {
            String activity = i < 2 ? "mining" : (i < 4 ? "fishing" : "questing");
            slices.add(Slice.ordinary(UUID.randomUUID(), activity, tier));
            i++;
        }
        return slices;
    }

    @Test public void rawOddsIncludeSpecialsAndSumDuplicateActivitiesExactly()
    {
        List<Slice> slices = ordinary();
        slices.add(Slice.special(UUID.randomUUID(), Kind.TAINT, 5));
        slices.add(Slice.special(UUID.randomUUID(), Kind.SACRIFICE, 4));
        WheelOdds odds = new WheelOdds(new ActiveWheel(slices));
        assertEquals(7, odds.slices().size());
        assertEquals(3, odds.activities().size());
        assertEquals(44, odds.slices().values().stream().mapToInt(WheelOdds.Probability::weightUnits).sum());
        for (Slice slice : slices)
        {
            WheelOdds.Probability probability = odds.slices().get(slice.id());
            assertEquals(slice.weightUnits(), probability.weightUnits());
            assertEquals(44, probability.totalWeightUnits());
        }
        assertEquals(9, odds.activities().get("mining").weightUnits());
        assertEquals(14, odds.activities().get("fishing").weightUnits());
        // Questing remains a raw outcome even when a future eligibility snapshot says empty.
        assertEquals(12.0 / 44, odds.activities().get("questing").fraction(), 0.0);
        assertThrows(UnsupportedOperationException.class, () -> odds.slices().clear());
        assertThrows(UnsupportedOperationException.class, () -> odds.activities().clear());
        assertThrows(NullPointerException.class, () -> new WheelOdds(null));
    }

    @Test public void reorderIsInvariantAndEnhancementPreviewDoesNotChangeOriginal()
    {
        ActiveWheel original = new ActiveWheel(ordinary());
        WheelOdds before = new WheelOdds(original);
        List<UUID> reversed = new ArrayList<>(before.slices().keySet());
        Collections.reverse(reversed);
        WheelOdds reordered = new WheelOdds(original.reordered(reversed));
        assertEquals(reversed, new ArrayList<>(reordered.slices().keySet()));
        for (UUID id : before.slices().keySet())
        {
            assertEquals(before.slices().get(id).fraction(), reordered.slices().get(id).fraction(), 0.0);
        }
        List<Slice> proposed = new ArrayList<>(original.slices());
        Slice first = proposed.get(0);
        proposed.set(0, Slice.ordinary(first.id(), first.activity(), Enhancement.FIRST));
        WheelOdds after = new WheelOdds(new ActiveWheel(proposed));
        assertEquals(4, before.slices().get(first.id()).weightUnits());
        assertEquals(35, before.slices().get(first.id()).totalWeightUnits());
        assertEquals(5, after.slices().get(first.id()).weightUnits());
        assertEquals(36, after.slices().get(first.id()).totalWeightUnits());
        assertEquals(10, after.activities().get("mining").weightUnits());
        assertEquals(Enhancement.BASE, original.slices().get(0).enhancement());
    }
}

package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.wheelbound.domain.ActiveWheel.*;

public class ActiveWheelTest
{
    private static List<Slice> ordinary(int count, int activities)
    {
        List<Slice> slices = new ArrayList<>();
        for (int i = 0; i < count; i++)
        { slices.add(Slice.ordinary(UUID.randomUUID(), "activity." + (i % activities), Enhancement.BASE)); }
        return slices;
    }

    @Test public void sharedCapacityAndOrdinaryDiversityAreIndependent()
    {
        assertEquals(5, new ActiveWheel(ordinary(5, 3)).slices().size());
        assertThrows(IllegalArgumentException.class, () -> new ActiveWheel(ordinary(4, 3)));
        assertThrows(IllegalArgumentException.class, () -> new ActiveWheel(ordinary(24, 2)));
        List<Slice> full = ordinary(22, 3);
        full.add(Slice.special(UUID.randomUUID(), Kind.TAINT, 5));
        full.add(Slice.special(UUID.randomUUID(), Kind.SACRIFICE, 4));
        assertEquals(24, new ActiveWheel(full).slices().size());
        full.add(Slice.special(UUID.randomUUID(), Kind.TAINT, 6));
        assertThrows(IllegalArgumentException.class, () -> new ActiveWheel(full));
        List<Slice> tooFew = ordinary(4, 3);
        tooFew.add(Slice.special(UUID.randomUUID(), Kind.TAINT, 4));
        assertThrows(IllegalArgumentException.class, () -> new ActiveWheel(tooFew));
    }

    @Test public void exactWeightsAndReorderingPreserveIndividualInstances()
    {
        List<Slice> input = new ArrayList<>();
        int index = 0;
        for (Enhancement tier : Enhancement.values())
        { input.add(Slice.ordinary(UUID.randomUUID(), "activity." + (index++ % 3), tier)); }
        ActiveWheel wheel = new ActiveWheel(input);
        assertArrayEquals(new int[]{4, 5, 6, 8, 12},
            wheel.slices().stream().mapToInt(Slice::weightUnits).toArray());
        assertEquals(35, wheel.totalWeightUnits());
        input.clear();
        assertEquals(5, wheel.slices().size());
        assertThrows(UnsupportedOperationException.class, () -> wheel.slices().clear());
        List<UUID> order = new ArrayList<>();
        for (Slice slice : wheel.slices()) { order.add(slice.id()); }
        Collections.reverse(order);
        ActiveWheel reversed = wheel.reordered(order);
        order.clear();
        assertEquals(wheel.totalWeightUnits(), reversed.totalWeightUnits());
        for (int i = 0; i < 5; i++) { assertSame(wheel.slices().get(i), reversed.slices().get(4 - i)); }
        assertThrows(IllegalArgumentException.class, () -> wheel.reordered(List.of()));
        List<UUID> invalid = new ArrayList<>();
        for (Slice slice : wheel.slices()) { invalid.add(slice.id()); }
        invalid.set(0, invalid.get(1));
        assertThrows(IllegalArgumentException.class, () -> wheel.reordered(invalid));
        invalid.set(0, UUID.randomUUID());
        assertThrows(IllegalArgumentException.class, () -> wheel.reordered(invalid));
    }

    @Test public void malformedSnapshotsFailClosed()
    {
        List<Slice> duplicate = ordinary(5, 3);
        duplicate.add(duplicate.get(0));
        assertThrows(IllegalArgumentException.class, () -> new ActiveWheel(duplicate));
        assertThrows(IllegalArgumentException.class, () -> Slice.special(UUID.randomUUID(), Kind.ORDINARY, 4));
        assertThrows(IllegalArgumentException.class, () -> Slice.special(UUID.randomUUID(), Kind.TAINT, 0));
        assertThrows(IllegalArgumentException.class, () -> Slice.ordinary(UUID.randomUUID(), " ", Enhancement.BASE));
        assertThrows(NullPointerException.class, () -> Slice.ordinary(UUID.randomUUID(), "mining", null));
        List<Slice> overflow = ordinary(5, 3);
        overflow.add(Slice.special(UUID.randomUUID(), Kind.TAINT, Integer.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> new ActiveWheel(overflow));
    }
}

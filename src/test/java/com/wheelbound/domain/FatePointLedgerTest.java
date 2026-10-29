package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.wheelbound.domain.FatePointLedger.Kind.*;

public class FatePointLedgerTest
{
    private FatePointLedger.Entry entry(FatePointLedger.Kind kind, long amount)
    { return new FatePointLedger.Entry(kind, amount); }

    @Test public void penaltiesAllowDebtAndEarningsRecoverItWithoutMutatingHistory()
    {
        FatePointLedger initial = new FatePointLedger(10, List.of());
        FatePointLedger debt = initial.append(entry(PENALTY, 30));
        assertEquals(-20, debt.balance());
        assertEquals(10, initial.balance());
        assertTrue(initial.entries().isEmpty());
        FatePointLedger recovered = debt.append(entry(EARNING, 25));
        assertEquals(5, recovered.balance());
        FatePointLedger spent = recovered.append(entry(DISCRETIONARY_COST, 5));
        assertEquals(0, spent.balance());
        assertEquals(10, spent.startingBalance());
        assertEquals(3, spent.entries().size());
        assertEquals(-20, debt.balance());
        assertThrows(IllegalArgumentException.class, () -> debt.append(entry(DISCRETIONARY_COST, 1)));
        assertThrows(IllegalArgumentException.class, () -> recovered.append(entry(DISCRETIONARY_COST, 6)));
    }

    @Test public void restorationChecksIntermediateAffordabilityAndDefensivelyCopiesHistory()
    {
        List<FatePointLedger.Entry> history = new ArrayList<>(List.of(entry(EARNING, 20),
            entry(DISCRETIONARY_COST, 15), entry(PENALTY, 10)));
        FatePointLedger restored = new FatePointLedger(0, history);
        history.clear();
        assertEquals(-5, restored.balance());
        assertEquals(3, restored.entries().size());
        assertThrows(UnsupportedOperationException.class, () -> restored.entries().clear());
        assertThrows(IllegalArgumentException.class, () -> new FatePointLedger(0,
            List.of(entry(DISCRETIONARY_COST, 1), entry(EARNING, 20))));
        assertEquals(-1, new FatePointLedger(-1, List.of()).balance());
        assertThrows(NullPointerException.class, () -> new FatePointLedger(0, null));
        assertThrows(NullPointerException.class, () -> restored.append(null));
    }

    @Test public void invalidAmountsAndMachineOverflowFailWithoutClampingOrWrapping()
    {
        for (FatePointLedger.Kind kind : FatePointLedger.Kind.values())
        {
            assertThrows(IllegalArgumentException.class, () -> entry(kind, 0));
            assertThrows(IllegalArgumentException.class, () -> entry(kind, -1));
        }
        assertThrows(NullPointerException.class, () -> entry(null, 1));
        FatePointLedger maximum = new FatePointLedger(Long.MAX_VALUE, List.of());
        assertThrows(ArithmeticException.class, () -> maximum.append(entry(EARNING, 1)));
        FatePointLedger minimum = new FatePointLedger(Long.MIN_VALUE, List.of());
        assertThrows(ArithmeticException.class, () -> minimum.append(entry(PENALTY, 1)));
        assertEquals(Long.MAX_VALUE, maximum.balance());
        assertEquals(Long.MIN_VALUE, minimum.balance());
        assertEquals(-1, minimum.append(entry(EARNING, Long.MAX_VALUE)).balance());
        assertEquals(0, maximum.append(entry(DISCRETIONARY_COST, Long.MAX_VALUE)).balance());
        assertThrows(ArithmeticException.class, () -> new FatePointLedger(Long.MAX_VALUE,
            List.of(entry(EARNING, 1), entry(PENALTY, 1))));
    }
}

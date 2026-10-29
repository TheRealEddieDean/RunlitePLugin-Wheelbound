package com.wheelbound.domain;

import java.util.List;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class FatePointDebitsTest
{
    private final RunIdentity identity = new RunIdentity("local-profile", UUID.randomUUID());

    private FatePointDebits seed(long revision, RunLifecycle.State state, long balance)
    {
        return new FatePointDebits(new RunLifecycle(identity, revision, state,
            RunLifecycle.Obligation.ACTIVE_OBJECTIVE), new FatePointLedger(balance, List.of()));
    }

    @Test public void replayReturnsOriginalBalanceAfterLaterDebitWithoutAnotherCharge()
    {
        FatePointDebits initial = seed(7, RunLifecycle.State.ACTIVE, 20);
        UUID command = UUID.randomUUID();
        FatePointDebits.Result first = initial.debit(identity, command, 7, "fixture-product", "fixture-v1", 5);
        FatePointDebits head = first.proposed().debit(identity, UUID.randomUUID(), 8,
            "fixture-other", "fixture-v2", 15).proposed();
        FatePointDebits.Result replay = head.debit(identity, command, 7, "fixture-product", "fixture-v1", 5);
        assertSame(head, replay.proposed());
        assertSame(first.receipt(), replay.receipt());
        assertEquals(15, replay.receipt().balance());
        assertEquals(0, head.ledger().balance());
        assertEquals(9, head.lifecycle().revision());
        assertEquals(RunLifecycle.Obligation.ACTIVE_OBJECTIVE, head.lifecycle().obligation());
        assertEquals(20, initial.ledger().balance());
        assertTrue(initial.receipts().isEmpty());
        assertEquals(2, head.receipts().size());
        assertThrows(UnsupportedOperationException.class, () -> head.receipts().clear());
    }

    @Test public void collisionsAndForeignOrStaleRequestsCannotDebit()
    {
        UUID command = UUID.randomUUID();
        FatePointDebits head = seed(0, RunLifecycle.State.ACTIVE, 10)
            .debit(identity, command, 0, "product", "v1", 3).proposed();
        assertThrows(IllegalArgumentException.class, () -> head.debit(identity, command, 0, "product", "v1", 4));
        assertThrows(IllegalArgumentException.class, () -> head.debit(identity, command, 0, "other", "v1", 3));
        assertThrows(IllegalArgumentException.class, () -> head.debit(identity, command, 0, "product", "v2", 3));
        assertThrows(IllegalArgumentException.class, () -> head.debit(identity, command, 1, "product", "v1", 3));
        assertThrows(IllegalArgumentException.class, () -> head.debit(new RunIdentity("other", identity.runId()),
            command, 0, "product", "v1", 3));
        assertThrows(IllegalArgumentException.class, () -> head.debit(new RunIdentity(identity.accountKey(), UUID.randomUUID()),
            command, 0, "product", "v1", 3));
        assertThrows(IllegalArgumentException.class, () -> head.debit(identity, UUID.randomUUID(), 0, "product", "v1", 3));
        assertEquals(7, head.ledger().balance());
        assertEquals(1, head.receipts().size());
    }

    @Test public void inactiveInsufficientInvalidAndOverflowRequestsLeaveSeedUntouched()
    {
        for (RunLifecycle.State state : RunLifecycle.State.values())
        {
            if (state == RunLifecycle.State.ACTIVE) { continue; }
            FatePointDebits inactive = seed(0, state, 10);
            assertThrows(IllegalArgumentException.class, () -> inactive.debit(identity,
                UUID.randomUUID(), 0, "product", "v1", 1));
        }
        FatePointDebits initial = seed(0, RunLifecycle.State.ACTIVE, 2);
        for (long amount : new long[] {-1, 0, 3})
        {
            assertThrows(IllegalArgumentException.class, () -> initial.debit(identity,
                UUID.randomUUID(), 0, "product", "v1", amount));
        }
        assertThrows(IllegalArgumentException.class, () -> initial.debit(identity, UUID.randomUUID(), 0, " ", "v1", 1));
        assertThrows(NullPointerException.class, () -> initial.debit(identity, UUID.randomUUID(), 0, "product", null, 1));
        FatePointDebits maximum = seed(Long.MAX_VALUE, RunLifecycle.State.ACTIVE, 2);
        assertThrows(ArithmeticException.class, () -> maximum.debit(identity, UUID.randomUUID(),
            Long.MAX_VALUE, "product", "v1", 1));
        assertEquals(2, initial.ledger().balance());
        assertTrue(initial.receipts().isEmpty());
    }
}

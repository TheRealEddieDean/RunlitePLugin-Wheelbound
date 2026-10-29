package com.wheelbound.domain;

import java.util.List;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class FatePointDebitsTest
{
    private final RunIdentity identity = new RunIdentity("local-profile", UUID.randomUUID());

    @Test public void restoresOriginalResponsesAndCanContinueWithoutRepeatingDebits()
    {
        FatePointDebits initial = seed(7, RunLifecycle.State.ACTIVE, 20);
        UUID command = UUID.randomUUID();
        FatePointDebits head = initial.debit(identity, command, 7, "product", "v1", 5)
            .proposed().debit(identity, UUID.randomUUID(), 8, "other", "v2", 3).proposed();
        FatePointDebits restored = FatePointDebits.restore(initial.lifecycle(), initial.ledger(), head.receipts());
        assertEquals(12, restored.ledger().balance());
        assertEquals(9, restored.lifecycle().revision());
        assertEquals(RunLifecycle.Obligation.ACTIVE_OBJECTIVE, restored.lifecycle().obligation());
        FatePointDebits.Result replay = restored.debit(identity, command, 7, "product", "v1", 5);
        assertSame(restored, replay.proposed());
        assertEquals(15, replay.receipt().balance());
        assertEquals(2, restored.ledger().entries().size());
        assertEquals(10, restored.debit(identity, UUID.randomUUID(), 9, "third", "v1", 2)
            .proposed().ledger().balance());
        assertThrows(IllegalArgumentException.class,
            () -> restored.debit(identity, command, 7, "product", "v2", 5));
        assertTrue(FatePointDebits.restore(initial.lifecycle(), initial.ledger(), List.of()).receipts().isEmpty());
    }

    @Test public void restorationRejectsCorruptReorderedRepeatedAndUnaffordableHistory()
    {
        FatePointDebits initial = seed(7, RunLifecycle.State.ACTIVE, 20);
        FatePointDebits head = initial.debit(identity, UUID.randomUUID(), 7, "product", "v1", 5)
            .proposed().debit(identity, UUID.randomUUID(), 8, "other", "v1", 3).proposed();
        FatePointDebits.Receipt first = head.receipts().get(0);
        FatePointDebits.Receipt second = head.receipts().get(1);
        FatePointDebits.Receipt wrongBalance = new FatePointDebits.Receipt(first.commandId(), 7, 8,
            "product", "v1", 5, 14);
        FatePointDebits.Receipt reused = new FatePointDebits.Receipt(first.commandId(), 8, 9,
            "other", "v1", 3, 12);
        for (List<FatePointDebits.Receipt> history : List.of(List.of(wrongBalance),
            List.of(second, first), List.of(first, first), List.of(first, reused)))
        {
            assertThrows(IllegalArgumentException.class,
                () -> FatePointDebits.restore(initial.lifecycle(), initial.ledger(), history));
        }
        assertThrows(IllegalArgumentException.class, () -> FatePointDebits.restore(initial.lifecycle(),
            new FatePointLedger(4, List.of()), head.receipts()));
        assertThrows(IllegalArgumentException.class, () -> FatePointDebits.restore(
            seed(7, RunLifecycle.State.PAUSED, 20).lifecycle(), initial.ledger(), head.receipts()));
        assertThrows(IllegalArgumentException.class, () -> new FatePointDebits.Receipt(
            first.commandId(), 7, 9, "product", "v1", 5, 15));
        assertThrows(IllegalArgumentException.class, () -> new FatePointDebits.Receipt(
            first.commandId(), Long.MAX_VALUE, Long.MIN_VALUE, "product", "v1", 5, 15));
        assertEquals(20, initial.ledger().balance());
        assertTrue(initial.receipts().isEmpty());
    }

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

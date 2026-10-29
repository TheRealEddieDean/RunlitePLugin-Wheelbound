package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class DurableFatePointDebitsTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunIdentity id = new RunIdentity("account:profile", UUID.randomUUID());
    private final RunCodec codec = new RunCodec(new Gson());
    private AtomicRunStore store()
    { return new AtomicRunStore(folder.getRoot().toPath(), codec, id); }
    private DurableFatePointDebits service() { return new DurableFatePointDebits(store()); }
    private void initialize(Optional<FatePointLedger> ledger) throws Exception
    {
        store().write(new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), "v1", List.of(), ledger), -1);
    }
    private void initialize() throws Exception
    { initialize(Optional.of(new FatePointLedger(100, List.of()))); }

    @Test public void restartReplaysOriginalBalanceAfterMixedCommandsAndLaterDebit() throws Exception
    {
        initialize(); UUID command = UUID.randomUUID();
        assertEquals(90, service().debit(id, command, 0, "test-only", "q1", 10).balance());
        new LifecycleCommands(store()).execute(id, UUID.randomUUID(), 1, RunReceipt.Operation.PAUSE);
        new LifecycleCommands(store()).execute(id, UUID.randomUUID(), 2, RunReceipt.Operation.RESUME);
        new DurableWheelDraws(store(), bound -> 0).commit(id, UUID.randomUUID(), 3,
            new WeightedPool("v1", List.of(new WeightedPool.Entry("A", 1))));
        assertEquals(70, service().debit(id, UUID.randomUUID(), 4, "test-only", "q2", 20).balance());
        new LifecycleCommands(store()).execute(id, UUID.randomUUID(), 5, RunReceipt.Operation.PAUSE);
        assertEquals(90, service().debit(id, command, 0, "test-only", "q1", 10).balance());
        RunDocument current = store().load().get();
        assertEquals(6, current.lifecycle().revision());
        assertEquals(6, current.receipts().size());
        assertEquals(70, current.fatePoints().get().balance());
        assertEquals(2, current.fatePoints().get().entries().size());
        assertEquals(RunLifecycle.Obligation.OFFERS_COMMITTED, current.lifecycle().obligation());
    }

    @Test public void lostAcknowledgementReplaysOneDebitAfterReopen() throws Exception
    {
        initialize(); UUID command = UUID.randomUUID();
        AtomicRunStore failing = new AtomicRunStore(folder.getRoot().toPath(), codec, id, boundary -> {
            if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("Lost acknowledgement"); }
        });
        assertThrows(IOException.class, () -> new DurableFatePointDebits(failing)
            .debit(id, command, 0, "test-only", "q1", 10));
        assertEquals(90, service().debit(id, command, 0, "test-only", "q1", 10).balance());
        assertEquals(1, store().load().get().receipts().size());
        assertEquals(1, store().load().get().fatePoints().get().entries().size());
    }

    @Test public void failedWriteRetainsOldSnapshotAndBlocksUntilReconciliation() throws Exception
    {
        initialize(); byte[] before = Files.readAllBytes(store().snapshotPath());
        AtomicRunStore failing = new AtomicRunStore(folder.getRoot().toPath(), codec, id, boundary -> {
            if (boundary == AtomicRunStore.Boundary.TEMP_FORCED) { throw new IOException("Before replacement"); }
        });
        UUID command = UUID.randomUUID();
        assertThrows(IOException.class, () -> new DurableFatePointDebits(failing)
            .debit(id, command, 0, "test-only", "q1", 10));
        assertArrayEquals(before, Files.readAllBytes(store().snapshotPath()));
        assertTrue(Files.exists(store().pendingPath()));
        assertThrows(IOException.class, () -> service().debit(id, command, 0, "test-only", "q1", 10));
    }

    @Test public void requestCollisionsAndInvalidCommandsDoNotChangeSnapshot() throws Exception
    {
        initialize(); UUID command = UUID.randomUUID();
        service().debit(id, command, 0, "test-only", "q1", 10);
        byte[] before = Files.readAllBytes(store().snapshotPath());
        assertThrows(IllegalArgumentException.class, () -> service().debit(id, command, 0, "other", "q1", 10));
        assertThrows(IllegalArgumentException.class, () -> service().debit(id, command, 0, "test-only", "q2", 10));
        assertThrows(IllegalArgumentException.class, () -> service().debit(id, command, 0, "test-only", "q1", 11));
        assertThrows(IllegalArgumentException.class, () -> service().debit(id, command, 1, "test-only", "q1", 10));
        assertThrows(IllegalArgumentException.class, () -> service().debit(new RunIdentity("other", id.runId()),
            command, 0, "test-only", "q1", 10));
        assertThrows(IllegalArgumentException.class, () -> service().debit(new RunIdentity(id.accountKey(), UUID.randomUUID()),
            command, 0, "test-only", "q1", 10));
        assertThrows(IllegalArgumentException.class, () -> service().debit(id, UUID.randomUUID(), 0, "test-only", "q1", 10));
        assertThrows(IllegalArgumentException.class, () -> service().debit(id, UUID.randomUUID(), 1, "test-only", "q1", 0));
        assertThrows(IllegalArgumentException.class, () -> service().debit(id, UUID.randomUUID(), 1, " ", "q1", 10));
        assertThrows(IllegalArgumentException.class, () -> service().debit(id, UUID.randomUUID(), 1, "test-only", "q1", 91));
        assertThrows(IllegalArgumentException.class, () -> new LifecycleCommands(store())
            .execute(id, command, 0, RunReceipt.Operation.PAUSE));
        WeightedPool pool = new WeightedPool("v1", List.of(new WeightedPool.Entry("A", 1)));
        assertThrows(IllegalArgumentException.class, () -> new DurableWheelDraws(store(), bound -> {
            throw new AssertionError("Collision must not draw");
        }).commit(id, command, 0, pool));
        assertArrayEquals(before, Files.readAllBytes(store().snapshotPath()));
        UUID pause = UUID.randomUUID();
        new LifecycleCommands(store()).execute(id, pause, 1, RunReceipt.Operation.PAUSE);
        assertThrows(IllegalArgumentException.class, () -> service().debit(id, pause, 1, "test-only", "q1", 10));
        assertThrows(IllegalArgumentException.class, () -> service().debit(id, UUID.randomUUID(), 2, "test-only", "q1", 10));
        assertEquals(90, store().load().get().fatePoints().get().balance());
        new LifecycleCommands(store()).execute(id, UUID.randomUUID(), 2, RunReceipt.Operation.RESUME);
        UUID draw = UUID.randomUUID();
        new DurableWheelDraws(store(), bound -> 0).commit(id, draw, 3, pool);
        assertThrows(IllegalArgumentException.class, () -> service().debit(id, draw, 3, "test-only", "q1", 10));
    }

    @Test public void missingLegacyLedgerFailsClosed() throws Exception
    {
        initialize(Optional.empty());
        assertThrows(IllegalStateException.class, () -> service().debit(id, UUID.randomUUID(), 0, "test-only", "q1", 1));
        assertEquals(0, store().load().get().lifecycle().revision());
        assertFalse(store().load().get().fatePoints().isPresent());
    }
}

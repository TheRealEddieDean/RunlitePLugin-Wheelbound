package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class MasterTaintStoreTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunCodec codec = new RunCodec(new Gson());

    // No-op directory forcing isolates command semantics from platform durability.
    private AtomicRunStore store(Path root, RunDocument source, AtomicRunStore.CommitHook hook)
    { return new AtomicRunStore(root, codec, source.lifecycle().identity(), hook, ignored -> {}); }

    private AtomicRunStore seed(Path root, RunDocument source) throws Exception
    {
        AtomicRunStore target = store(root, source, ignored -> {});
        Files.createDirectories(target.snapshotPath().getParent());
        Files.write(target.snapshotPath(), codec.encode(source));
        return target;
    }

    private MasterSelectionResolutionReceipt request(RunDocument source, long ticket, String stream)
    {
        var pending = source.pendingMasterSelection().orElseThrow();
        var creation = source.masterCreations().orElseThrow().receipts().get(pending.transactionId());
        return MasterSelectionResolutionReceipt.prepare(new UUID(0, 200), 5, creation, pending,
            new MasterSelectionProvenance.Step(new DrawCommitment(pending.pool(), ticket), stream));
    }

    private MasterSelectionResolutionReceipt request(RunDocument source)
    { return request(source, source.pendingMasterSelection().orElseThrow().pool().totalWeight() - 1, "terminal"); }

    @Test public void commitAndRestartReplayRetainOriginalAccountingAndPausedHead() throws Exception
    {
        for (int prefix : new int[] {0, 1, 513})
        for (long balance : new long[] {1, 7, Long.MAX_VALUE})
        {
            var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, prefix, balance);
            var root = folder.newFolder().toPath();
            var target = seed(root, source);
            var request = request(source);
            var response = target.commitMasterTaint(request);
            assertEquals(balance, response.allocation().spinsBefore());
            var committed = target.load().orElseThrow();
            assertEquals(balance - 1, committed.masterSpins().orElseThrow().available());
            assertEquals(RunLifecycle.Obligation.PUNISHMENT_PENDING, committed.lifecycle().obligation());
            assertFalse(committed.pendingMasterSelection().isPresent());
            assertEquals(request, committed.currentMasterSpecial().orElseThrow().obligation().resolution());
            target.changeLifecycle(source.lifecycle().identity(), UUID.randomUUID(), 6, RunReceipt.Operation.PAUSE);
            byte[] paused = Files.readAllBytes(target.snapshotPath());
            var reopened = store(root, source, ignored -> { throw new AssertionError("Replay wrote snapshot"); });
            assertEquals(response, reopened.commitMasterTaint(request));
            assertArrayEquals(paused, Files.readAllBytes(target.snapshotPath()));
            assertEquals(7, reopened.load().orElseThrow().lifecycle().revision());
            assertThrows(IOException.class, () -> reopened.commitMasterTaint(request(source,
                source.pendingMasterSelection().orElseThrow().pool().totalWeight() - 1, "changed")));
            assertArrayEquals(paused, Files.readAllBytes(target.snapshotPath()));
        }
    }

    @Test public void unsupportedAndStaleRequestsPreserveSnapshot() throws Exception
    {
        for (var kind : new ActiveWheel.Kind[] {ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE})
        {
            var source = MasterSpecialTerminalPreparationChecks.fixture(kind, 1, 7);
            var target = seed(folder.newFolder().toPath(), source);
            byte[] original = Files.readAllBytes(target.snapshotPath());
            assertThrows(IOException.class, () -> target.commitMasterTaint(request(source, 16, "ordinary")));
            if (kind == ActiveWheel.Kind.SACRIFICE)
            { assertThrows(IOException.class, () -> target.commitMasterTaint(request(source))); }
            assertArrayEquals(original, Files.readAllBytes(target.snapshotPath()));
            target.changeLifecycle(source.lifecycle().identity(), UUID.randomUUID(), 5, RunReceipt.Operation.PAUSE);
            byte[] paused = Files.readAllBytes(target.snapshotPath());
            assertThrows(IOException.class, () -> target.commitMasterTaint(request(source)));
            assertArrayEquals(paused, Files.readAllBytes(target.snapshotPath()));
        }
    }

    @Test public void failedWritesRequireReconciliationAndLostAcknowledgementReplays() throws Exception
    {
        for (var boundary : AtomicRunStore.Boundary.values())
        {
            var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, 1, 7);
            var root = folder.newFolder().toPath();
            var target = seed(root, source);
            byte[] original = Files.readAllBytes(target.snapshotPath());
            var request = request(source);
            var failing = store(root, source, reached -> {
                if (reached == boundary) { throw new IOException("Injected failure"); }
            });
            assertThrows(IOException.class, () -> failing.commitMasterTaint(request));
            assertThrows(IOException.class, () -> failing.commitMasterTaint(request));
            var reopened = store(root, source, ignored -> { throw new AssertionError("Recovery wrote snapshot"); });
            if (boundary == AtomicRunStore.Boundary.TEMP_FORCED)
            {
                assertArrayEquals(original, Files.readAllBytes(target.snapshotPath()));
                assertTrue(Files.exists(target.pendingPath()));
                assertThrows(IOException.class, () -> reopened.commitMasterTaint(request));
            }
            else
            {
                assertEquals(7, reopened.commitMasterTaint(request).allocation().spinsBefore());
                assertEquals(6, reopened.load().orElseThrow().masterSpins().orElseThrow().available());
            }
        }
    }

    @Test public void foreignMissingAndLockedHeadsCannotCommit() throws Exception
    {
        var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, 1, 7);
        var root = folder.newFolder().toPath();
        var target = seed(root, source);
        byte[] original = Files.readAllBytes(target.snapshotPath());
        var foreign = new AtomicRunStore(root, codec, new RunIdentity("foreign", UUID.randomUUID()),
            ignored -> {}, ignored -> {});
        assertThrows(IllegalArgumentException.class, () -> foreign.commitMasterTaint(request(source)));
        var missing = store(folder.newFolder().toPath(), source, ignored -> {});
        assertThrows(IOException.class, () -> missing.commitMasterTaint(request(source)));
        try (var channel = java.nio.channels.FileChannel.open(target.lockPath(),
            java.nio.file.StandardOpenOption.WRITE);
            var lock = channel.lock())
        { assertThrows(IOException.class, () -> target.commitMasterTaint(request(source))); }
        assertArrayEquals(original, Files.readAllBytes(target.snapshotPath()));
    }

    @Test public void requestCannotSupplyMissingPendingPrefixOrSpinBalance() throws Exception
    {
        var full = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, 1, 7);
        var request = request(full);
        for (var source : new RunDocument[] {
            MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, 0, 7),
            MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, 1, 0)})
        {
            var target = seed(folder.newFolder().toPath(), source);
            byte[] original = Files.readAllBytes(target.snapshotPath());
            assertThrows(IOException.class, () -> target.commitMasterTaint(request));
            assertArrayEquals(original, Files.readAllBytes(target.snapshotPath()));
        }
    }
}

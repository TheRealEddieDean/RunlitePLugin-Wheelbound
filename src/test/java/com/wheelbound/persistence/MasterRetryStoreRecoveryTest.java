package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

/** Test-only candidate snapshots exercise recovery without enabling retry writes. */
public class MasterRetryStoreRecoveryTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunCodec codec = new RunCodec(new Gson());

    @Test public void recoversExactResponseAcrossLaterBatchesTerminalAndPause() throws Exception
    {
        for (int count : new int[] {0, 1, 128})
        for (boolean suspension : new boolean[] {false, true})
        {
            var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, count, 7);
            var request = request(source, new UUID(140, 1), suspension, "first");
            var first = MasterRetryOperation.prepare(source, request);
            var later = MasterRetryOperation.prepare(first, request(first, new UUID(140, 2), false, "later"));
            var pending = later.pendingMasterSelection().orElseThrow();
            var creation = later.masterCreations().orElseThrow().receipts().get(pending.transactionId());
            var resolved = MasterTaintTerminalOperation.prepare(later,
                MasterSelectionResolutionReceipt.prepare(new UUID(140, 3), later.lifecycle().revision(),
                    creation, pending, new MasterSelectionProvenance.Step(
                        new DrawCommitment(pending.pool(), pending.pool().totalWeight() - 1), "terminal")));
            var paused = LifecycleAdmission.prepare(resolved, request.identity(), new UUID(140, 4),
                resolved.lifecycle().revision(), RunReceipt.Operation.PAUSE);
            var resumed = LifecycleAdmission.prepare(paused, request.identity(), new UUID(140, 5),
                paused.lifecycle().revision(), RunReceipt.Operation.RESUME);
            var store = store(request.identity());
            for (var head : new RunDocument[] {first, later, resolved, paused, resumed})
            {
                install(store, head);
                byte[] original = Files.readAllBytes(store.snapshotPath());
                assertEquals(request, store.committedMasterRetry(request).orElseThrow());
                assertEquals(request, store(request.identity()).committedMasterRetry(request).orElseThrow());
                assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
                assertFalse(Files.exists(store.pendingPath()));
                assertFalse(Files.exists(store.previousPath()));
                assertEquals(head.lifecycle().revision(), store.load().orElseThrow().lifecycle().revision());
                var restoredSpins = store.load().orElseThrow().masterSpins().orElseThrow();
                assertEquals(head.masterSpins().orElseThrow().allotted(), restoredSpins.allotted());
                assertEquals(head.masterSpins().orElseThrow().available(), restoredSpins.available());
                assertEquals(head.masterSpins().orElseThrow().revision(), restoredSpins.revision());
                assertEquals(head.masterSpins().orElseThrow().reservation().isPresent(),
                    restoredSpins.reservation().isPresent());
            }
        }
    }

    @Test public void absenceAndConflictsNeverWriteOrCreateAnAssignment() throws Exception
    {
        var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, 1, 7);
        var request = request(source, new UUID(141, 1), false, "first");
        var store = store(request.identity());
        assertTrue(store.committedMasterRetry(request).isEmpty());
        assertFalse(Files.exists(store.snapshotPath()));
        install(store, source);
        assertTrue(store.committedMasterRetry(request).isEmpty());
        var head = MasterRetryOperation.prepare(source, request);
        install(store, head);
        byte[] original = Files.readAllBytes(store.snapshotPath());
        var changed = request(source, request.commandId(), false, "changed");
        assertThrows(IOException.class, () -> store.committedMasterRetry(changed));
        assertTrue(store.committedMasterRetry(request(source, new UUID(141, 2), false, "absent")).isEmpty());
        assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
        var pauseId = new UUID(141, 3);
        var collision = request(source, pauseId, false, "collision");
        var paused = LifecycleAdmission.prepare(head, request.identity(), pauseId,
            head.lifecycle().revision(), RunReceipt.Operation.PAUSE);
        install(store, paused);
        byte[] pausedBytes = Files.readAllBytes(store.snapshotPath());
        assertThrows(IOException.class, () -> store.committedMasterRetry(collision));
        assertArrayEquals(pausedBytes, Files.readAllBytes(store.snapshotPath()));
        var foreign = store(new RunIdentity("foreign", request.identity().runId()));
        assertThrows(IllegalArgumentException.class, () -> foreign.committedMasterRetry(request));
        assertFalse(Files.exists(foreign.lockPath()));
        assertThrows(NullPointerException.class, () -> store.committedMasterRetry(null));
    }

    @Test public void refusesLockedPendingAndCorruptSaves() throws Exception
    {
        var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, 0, 7);
        var request = request(source, new UUID(142, 1), true, "unused");
        var store = store(request.identity());
        install(store, MasterRetryOperation.prepare(source, request));
        byte[] original = Files.readAllBytes(store.snapshotPath());
        try (FileChannel channel = FileChannel.open(store.lockPath(), StandardOpenOption.CREATE,
                StandardOpenOption.WRITE); FileLock ignored = channel.lock())
        { assertThrows(IOException.class, () -> store.committedMasterRetry(request)); }
        Files.write(store.pendingPath(), original);
        assertThrows(IOException.class, () -> store.committedMasterRetry(request));
        assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
        Files.delete(store.pendingPath());
        Files.write(store.snapshotPath(), new byte[] {0});
        assertThrows(IOException.class, () -> store.committedMasterRetry(request));
        assertArrayEquals(new byte[] {0}, Files.readAllBytes(store.snapshotPath()));
    }

    @Test public void uncertainInstanceRequiresReopenEvenWithAReadableResponse() throws Exception
    {
        var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, 0, 7);
        var request = request(source, new UUID(143, 1), true, "unused");
        var failing = new AtomicRunStore(folder.getRoot().toPath(), codec, request.identity(), boundary -> {
            if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("Lost acknowledgement"); }
        });
        var initial = new RunDocument(new RunLifecycle(request.identity(), 0,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), "catalog");
        assertThrows(IOException.class, () -> failing.write(initial, -1));
        install(failing, MasterRetryOperation.prepare(source, request));
        assertThrows(IOException.class, () -> failing.committedMasterRetry(request));
        assertEquals(request, store(request.identity()).committedMasterRetry(request).orElseThrow());
    }

    private AtomicRunStore store(RunIdentity identity)
    { return new AtomicRunStore(folder.getRoot().toPath(), codec, identity); }

    private void install(AtomicRunStore store, RunDocument head) throws IOException
    {
        Files.createDirectories(store.snapshotPath().getParent());
        Files.write(store.snapshotPath(), codec.encodeRetryCandidate(head));
    }

    private static MasterRetryOperationProposal request(RunDocument head, UUID command,
        boolean suspension, String stream)
    {
        var before = head.pendingMasterSelection().orElseThrow();
        var creation = head.masterCreations().orElseThrow().receipts().get(before.transactionId());
        var after = suspension ? before : before.append(before.transactionId(), before.drawCount(),
            before.currentStreamState(), new MasterSelectionProvenance.Step(
                new DrawCommitment(before.pool(), 0), stream));
        return MasterRetryOperationProposal.restoreResponse(creation, head.lifecycle().identity(), command,
            head.lifecycle().revision(), before.drawCount(), after, suspension
                ? MasterRetryContinuation.Status.SUSPENDED : MasterRetryContinuation.Status.PROPOSED);
    }
}

package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionException;
import java.util.concurrent.RejectedExecutionException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

/** Candidate snapshots are test-only; session recovery never enables retry writes. */
public class MasterRetrySessionRecoveryTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunCodec codec = new RunCodec(new Gson());
    private final List<Runnable> tasks = new ArrayList<>();
    private final AsyncRunSession async = new AsyncRunSession(tasks::add);

    @Test public void recoversOriginalResponseAndRefreshesActualHead() throws Exception
    {
        for (int count : new int[] {0, 1, 128})
        for (boolean suspension : new boolean[] {false, true})
        {
            var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, count, 7);
            var request = request(source, new UUID(150, 1), suspension);
            var first = MasterRetryOperation.prepare(source, request);
            var later = MasterRetryOperation.prepare(first, request(first, new UUID(150, 2), false));
            var paused = LifecycleAdmission.prepare(later, request.identity(), new UUID(150, 3),
                later.lifecycle().revision(), RunReceipt.Operation.PAUSE);
            var store = install(first);
            var session = new RunSession();
            session.connect(store, request.identity());
            var stale = session.capture().orElseThrow();
            install(paused);
            byte[] bytes = Files.readAllBytes(store.snapshotPath());
            assertEquals(request, session.committedMasterRetry(request).orElseThrow());
            assertFalse(session.accepts(stale));
            assertTrue(session.capture().isEmpty());
            open(store, request.identity());
            var recovered = async.committedMasterRetry(request);
            assertTrue(async.capture().isEmpty());
            tasks.remove(0).run();
            assertEquals(request, recovered.join().orElseThrow().orElseThrow());
            assertTrue(async.capture().isEmpty());
            assertArrayEquals(bytes, Files.readAllBytes(store.snapshotPath()));
            assertFalse(Files.exists(store.pendingPath()));
        }
    }

    @Test public void absenceInvalidatesCallbacksAndConflictsDisconnect() throws Exception
    {
        var source = MasterRetryContinuationChecks.fixture(1);
        var request = request(source, new UUID(151, 1), true);
        var store = install(source);
        open(store, request.identity());
        var stale = async.capture().orElseThrow();
        byte[] bytes = Files.readAllBytes(store.snapshotPath());
        var absent = async.committedMasterRetry(request);
        assertFalse(async.accepts(stale));
        tasks.remove(0).run();
        assertTrue(absent.join().orElseThrow().isEmpty());
        var fresh = async.capture().orElseThrow();
        assertTrue(async.accepts(fresh));
        assertFalse(async.accepts(stale));
        assertArrayEquals(bytes, Files.readAllBytes(store.snapshotPath()));

        install(MasterRetryOperation.prepare(source, request));
        var changed = request(source, request.commandId(), false);
        var failed = async.committedMasterRetry(changed);
        tasks.remove(0).run();
        assertTrue(assertThrows(CompletionException.class, failed::join).getCause() instanceof IOException);
        assertTrue(async.capture().isEmpty());
        var disconnected = async.committedMasterRetry(request);
        assertTrue(assertThrows(CompletionException.class, disconnected::join).getCause()
            instanceof IllegalStateException);
        assertTrue(tasks.isEmpty());
    }

    @Test public void supersededRecoverySkipsStorageAndPreservesFreshConnection() throws Exception
    {
        var source = MasterRetryContinuationChecks.fixture(0);
        var request = request(source, new UUID(152, 1), true);
        var store = install(MasterRetryOperation.prepare(source, request));
        open(store, request.identity());
        var skipped = async.committedMasterRetry(request);
        var reopened = async.connect(store, request.identity());
        tasks.remove(1).run();
        assertTrue(reopened.join());
        var fresh = async.capture().orElseThrow();
        byte[] bytes = Files.readAllBytes(store.snapshotPath());
        // A skipped lookup must not even read a now-corrupt snapshot.
        Files.write(store.snapshotPath(), new byte[] {0});
        tasks.remove(0).run();
        assertTrue(skipped.join().isEmpty());
        assertTrue(async.accepts(fresh));
        Files.write(store.snapshotPath(), bytes);
        assertThrows(NullPointerException.class, () -> async.committedMasterRetry(null));
        assertTrue(async.accepts(fresh));
    }

    @Test public void submissionAndStorageFailuresCloseAdmission() throws Exception
    {
        var source = MasterRetryContinuationChecks.fixture(0);
        var request = request(source, new UUID(153, 1), true);
        var store = install(source);
        var rejecting = new AsyncRunSession(task -> {
            if (Files.exists(store.pendingPath())) { throw new RejectedExecutionException("offline fixture"); }
            tasks.add(task);
        });
        var opened = rejecting.connect(store, request.identity());
        tasks.remove(0).run();
        assertTrue(opened.join());
        var stale = rejecting.capture().orElseThrow();
        Files.write(store.pendingPath(), new byte[] {0});
        var rejected = rejecting.committedMasterRetry(request);
        assertTrue(assertThrows(CompletionException.class, rejected::join).getCause()
            instanceof RejectedExecutionException);
        assertFalse(rejecting.accepts(stale));
        assertTrue(rejecting.capture().isEmpty());
        Files.delete(store.pendingPath());
        open(store, request.identity());
        Files.delete(store.snapshotPath());
        var missing = async.committedMasterRetry(request);
        tasks.remove(0).run();
        assertTrue(assertThrows(CompletionException.class, missing::join).getCause() instanceof IOException);
        assertTrue(async.capture().isEmpty());
        assertFalse(Files.exists(store.snapshotPath()));
    }

    private void open(AtomicRunStore store, RunIdentity identity)
    {
        var opened = async.connect(store, identity);
        tasks.remove(0).run();
        assertTrue(opened.join());
    }

    private AtomicRunStore install(RunDocument head) throws IOException
    {
        var store = new AtomicRunStore(folder.getRoot().toPath(), codec, head.lifecycle().identity());
        Files.createDirectories(store.snapshotPath().getParent());
        Files.write(store.snapshotPath(), codec.encodeRetryCandidate(head));
        return store;
    }

    private static MasterRetryOperationProposal request(RunDocument head, UUID command, boolean suspension)
    {
        var before = head.pendingMasterSelection().orElseThrow();
        var creation = head.masterCreations().orElseThrow().receipts().get(before.transactionId());
        var after = suspension ? before : before.append(before.transactionId(), before.drawCount(),
            before.currentStreamState(), new MasterSelectionProvenance.Step(
                new DrawCommitment(before.pool(), 0), "next"));
        return MasterRetryOperationProposal.restoreResponse(creation, head.lifecycle().identity(), command,
            head.lifecycle().revision(), before.drawCount(), after, suspension
                ? MasterRetryContinuation.Status.SUSPENDED : MasterRetryContinuation.Status.PROPOSED);
    }
}

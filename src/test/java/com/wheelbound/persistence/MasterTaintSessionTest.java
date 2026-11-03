package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.*;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class MasterTaintSessionTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunCodec codec = new RunCodec(new Gson());
    private final RunDocument source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, 1, 7);
    private final List<Runnable> tasks = new ArrayList<>();
    private final AsyncRunSession async = new AsyncRunSession(tasks::add);

    private MasterSelectionResolutionReceipt request(String stream)
    {
        var pending = source.pendingMasterSelection().orElseThrow();
        var creation = source.masterCreations().orElseThrow().receipts().get(pending.transactionId());
        return MasterSelectionResolutionReceipt.prepare(new UUID(0, 200), 5, creation, pending,
            new MasterSelectionProvenance.Step(new DrawCommitment(pending.pool(),
                pending.pool().totalWeight() - 1), stream));
    }

    private AtomicRunStore store(AtomicRunStore.CommitHook hook)
    { return new AtomicRunStore(folder.getRoot().toPath(), codec, source.lifecycle().identity(), hook, ignored -> {}); }

    private AtomicRunStore seed() throws Exception
    {
        var target = store(ignored -> {});
        Files.createDirectories(target.snapshotPath().getParent());
        Files.write(target.snapshotPath(), codec.encode(source));
        return target;
    }

    private void connect(AtomicRunStore target)
    {
        var opened = async.connect(target, source.lifecycle().identity());
        tasks.remove(0).run();
        assertTrue(opened.join());
    }

    @Test public void synchronousCallbackQualificationReplayAndFailure() throws Exception
    {
        var target = seed();
        var session = new RunSession();
        session.connect(target, source.lifecycle().identity());
        var stale = session.capture().orElseThrow();
        session.connect(target, source.lifecycle().identity());
        var fresh = session.capture().orElseThrow();
        byte[] before = Files.readAllBytes(target.snapshotPath());
        assertThrows(IllegalArgumentException.class, () -> session.commitMasterTaint(stale, request("terminal")));
        assertTrue(session.accepts(fresh));
        assertArrayEquals(before, Files.readAllBytes(target.snapshotPath()));
        var response = session.commitMasterTaint(fresh, request("terminal"));
        assertFalse(session.accepts(fresh));
        target.changeLifecycle(source.lifecycle().identity(), UUID.randomUUID(), 6, RunReceipt.Operation.PAUSE);
        byte[] paused = Files.readAllBytes(target.snapshotPath());
        assertEquals(response, session.commitMasterTaint(request("terminal")));
        assertFalse(session.capture().isPresent());
        assertArrayEquals(paused, Files.readAllBytes(target.snapshotPath()));
        assertThrows(IOException.class, () -> session.commitMasterTaint(request("changed")));
        assertThrows(IllegalStateException.class, () -> session.commitMasterTaint(request("terminal")));
    }

    @Test public void asyncRejectsStaleBeforeQueueAndClosesAcceptedCallbacksImmediately() throws Exception
    {
        var target = seed();
        connect(target);
        var stale = async.capture().orElseThrow();
        connect(target);
        var fresh = async.capture().orElseThrow();
        assertTrue(async.commitMasterTaint(stale, request("terminal")).isCompletedExceptionally());
        var original = request("terminal");
        var wrongRevision = new MasterSelectionResolutionReceipt(original.commandId(), 6,
            original.creation(), original.resolved());
        assertTrue(async.commitMasterTaint(fresh, wrongRevision).isCompletedExceptionally());
        assertTrue(async.commitMasterTaint(fresh, null).isCompletedExceptionally());
        assertTrue(tasks.isEmpty()); assertTrue(async.accepts(fresh));
        var result = async.commitMasterTaint(fresh, request("terminal"));
        assertFalse(async.accepts(fresh)); assertFalse(async.capture().isPresent());
        assertEquals(5, target.load().orElseThrow().lifecycle().revision());
        tasks.remove(0).run();
        var response = result.join().orElseThrow();
        assertEquals(7, response.allocation().spinsBefore());
        assertEquals(6, target.load().orElseThrow().masterSpins().orElseThrow().available());
        target.changeLifecycle(source.lifecycle().identity(), UUID.randomUUID(), 6, RunReceipt.Operation.PAUSE);
        byte[] paused = Files.readAllBytes(target.snapshotPath());
        var replay = async.commitMasterTaint(request("terminal"));
        tasks.remove(0).run();
        assertEquals(response, replay.join().orElseThrow());
        assertFalse(async.capture().isPresent());
        assertArrayEquals(paused, Files.readAllBytes(target.snapshotPath()));
    }

    @Test public void supersededQueueSkipsStorageAndPreservesFreshConnection() throws Exception
    {
        var target = seed(); connect(target);
        byte[] before = Files.readAllBytes(target.snapshotPath());
        var skipped = async.commitMasterTaint(request("terminal"));
        var opened = async.connect(target, source.lifecycle().identity());
        tasks.remove(1).run(); assertTrue(opened.join());
        var fresh = async.capture().orElseThrow();
        tasks.remove(0).run();
        assertFalse(skipped.join().isPresent()); assertTrue(async.accepts(fresh));
        assertArrayEquals(before, Files.readAllBytes(target.snapshotPath()));
    }

    @Test public void startedCommitAfterDisconnectDoesNotRestoreAdmission() throws Exception
    {
        seed();
        var target = store(boundary -> { if (boundary == AtomicRunStore.Boundary.REPLACED) { async.disconnect(); } });
        connect(target);
        var committed = async.commitMasterTaint(request("terminal"));
        tasks.remove(0).run();
        assertTrue(committed.join().isPresent()); assertFalse(async.capture().isPresent());
        assertEquals(6, target.load().orElseThrow().masterSpins().orElseThrow().available());
    }

    @Test public void lostAcknowledgementDisconnectsAndReconnectReplaysWithoutDebit() throws Exception
    {
        var target = seed();
        var failing = store(boundary -> { if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("lost ack"); } });
        connect(failing);
        var result = async.commitMasterTaint(request("terminal"));
        tasks.remove(0).run();
        assertTrue(result.isCompletedExceptionally()); assertFalse(async.capture().isPresent());
        byte[] committed = Files.readAllBytes(target.snapshotPath());
        connect(target);
        var replay = async.commitMasterTaint(request("terminal"));
        tasks.remove(0).run();
        assertEquals(7, replay.join().orElseThrow().allocation().spinsBefore());
        assertArrayEquals(committed, Files.readAllBytes(target.snapshotPath()));
    }

    @Test public void submissionFailureClosesAdmission() throws Exception
    {
        var target = seed();
        var queued = new ArrayList<Runnable>();
        var rejected = new AsyncRunSession(task -> {
            if (queued.isEmpty()) { queued.add(task); }
            else { throw new RejectedExecutionException("fixture"); }
        });
        var opened = rejected.connect(target, source.lifecycle().identity());
        queued.get(0).run(); assertTrue(opened.join());
        var token = rejected.capture().orElseThrow();
        byte[] before = Files.readAllBytes(target.snapshotPath());
        assertTrue(rejected.commitMasterTaint(token, request("terminal")).isCompletedExceptionally());
        assertFalse(rejected.accepts(token)); assertFalse(rejected.capture().isPresent());
        assertArrayEquals(before, Files.readAllBytes(target.snapshotPath()));
    }
}

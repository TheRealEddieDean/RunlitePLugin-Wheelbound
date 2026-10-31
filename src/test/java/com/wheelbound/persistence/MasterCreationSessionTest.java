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

public class MasterCreationSessionTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunCodec codec = new RunCodec(new Gson());
    private final RunDocument source = MasterCreationOperationChecks.fixture(0);
    private final MasterSelectionCreationReceipt request = source.receipts().get(0).masterCreation();
    private final List<Runnable> tasks = new ArrayList<>();
    private final AsyncRunSession session = new AsyncRunSession(tasks::add);

    private AtomicRunStore store()
    { return new AtomicRunStore(folder.getRoot().toPath(), codec, request.identity()); }

    private AtomicRunStore initialize() throws Exception
    {
        AtomicRunStore target = store();
        RunDocument before = MasterCreationAdmissionChecks.base(source,
            new RunLifecycle(request.identity(), 3, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
            Optional.of(new MasterSelectionCreationJournal(request.identity(), 3, List.of())));
        Files.createDirectories(target.snapshotPath().getParent());
        Files.write(target.snapshotPath(), codec.encode(before));
        return target;
    }

    private void connect(AtomicRunStore target)
    {
        CompletableFuture<Boolean> opened = session.connect(target, request.identity());
        tasks.remove(0).run();
        assertTrue(opened.join());
    }

    @Test public void creationClosesCallbacksAndPausedAdvancedReplayPreservesHead() throws Exception
    {
        AtomicRunStore target = initialize();
        connect(target);
        RunSession.Token old = session.capture().get();
        CompletableFuture<Optional<MasterSelectionCreationReceipt>> created = session.createMasterSelection(request);
        assertFalse(session.accepts(old)); assertFalse(session.capture().isPresent());
        assertFalse(created.isDone()); assertEquals(3, target.load().get().lifecycle().revision());
        tasks.remove(0).run();
        assertEquals(request, created.join().get());
        assertFalse(session.accepts(old)); assertTrue(session.capture().isPresent());
        assertEquals(4, target.load().get().lifecycle().revision());
        RunDocument paused = LifecycleAdmission.prepare(MasterCreationOperationChecks.fixture(513),
            request.identity(), UUID.randomUUID(), 6, RunReceipt.Operation.PAUSE);
        Files.write(target.snapshotPath(), codec.encode(paused));
        byte[] saved = Files.readAllBytes(target.snapshotPath());
        CompletableFuture<Optional<MasterSelectionCreationReceipt>> replay = session.createMasterSelection(request);
        tasks.remove(0).run();
        assertEquals(request, replay.join().get()); assertFalse(session.capture().isPresent());
        assertEquals(513, target.load().get().pendingMasterSelection().get().drawCount());
        assertArrayEquals(saved, Files.readAllBytes(target.snapshotPath()));
    }

    @Test public void supersededQueuedCreationSkipsDiskAndCannotReplaceFreshSession() throws Exception
    {
        AtomicRunStore target = initialize();
        connect(target);
        byte[] saved = Files.readAllBytes(target.snapshotPath());
        CompletableFuture<Optional<MasterSelectionCreationReceipt>> skipped = session.createMasterSelection(request);
        CompletableFuture<Boolean> opened = session.connect(target, request.identity());
        tasks.remove(1).run(); assertTrue(opened.join());
        RunSession.Token fresh = session.capture().get();
        tasks.remove(0).run();
        assertFalse(skipped.join().isPresent()); assertTrue(session.accepts(fresh));
        assertArrayEquals(saved, Files.readAllBytes(target.snapshotPath()));
        assertFalse(Files.exists(target.pendingPath()));
    }

    @Test public void staleAndRejectedCommandsCloseAdmissionWithoutWriting() throws Exception
    {
        AtomicRunStore target = initialize();
        connect(target);
        byte[] saved = Files.readAllBytes(target.snapshotPath());
        CompletableFuture<Optional<MasterSelectionCreationReceipt>> failed = session.createMasterSelection(
            new MasterSelectionCreationReceipt(request.identity(), 2, request.initial()));
        tasks.remove(0).run();
        assertTrue(failed.isCompletedExceptionally()); assertFalse(session.capture().isPresent());
        assertArrayEquals(saved, Files.readAllBytes(target.snapshotPath()));
        List<Runnable> queued = new ArrayList<>();
        AsyncRunSession rejected = new AsyncRunSession(task -> {
            if (queued.isEmpty()) { queued.add(task); }
            else { throw new RejectedExecutionException("fixture"); }
        });
        CompletableFuture<Boolean> opened = rejected.connect(target, request.identity());
        queued.get(0).run(); assertTrue(opened.join());
        RunSession.Token old = rejected.capture().get();
        assertTrue(rejected.createMasterSelection(request).isCompletedExceptionally());
        assertFalse(rejected.accepts(old)); assertFalse(rejected.capture().isPresent());
        assertArrayEquals(saved, Files.readAllBytes(target.snapshotPath()));
    }

    @Test public void lostAcknowledgementRequiresReconnectAndExactReplay() throws Exception
    {
        initialize();
        AtomicRunStore failing = new AtomicRunStore(folder.getRoot().toPath(), codec, request.identity(),
            boundary -> { if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("Lost ack"); } });
        connect(failing);
        CompletableFuture<Optional<MasterSelectionCreationReceipt>> failed = session.createMasterSelection(request);
        tasks.remove(0).run();
        assertTrue(failed.isCompletedExceptionally()); assertFalse(session.capture().isPresent());
        AtomicRunStore reopened = store();
        byte[] saved = Files.readAllBytes(reopened.snapshotPath());
        connect(reopened);
        CompletableFuture<Optional<MasterSelectionCreationReceipt>> replay = session.createMasterSelection(request);
        tasks.remove(0).run();
        assertEquals(request, replay.join().get()); assertTrue(session.capture().isPresent());
        assertEquals(1, reopened.load().get().receipts().size());
        assertArrayEquals(saved, Files.readAllBytes(reopened.snapshotPath()));
    }

    @Test public void disconnectDuringCommitCannotRestoreSession() throws Exception
    {
        initialize();
        AtomicRunStore target = new AtomicRunStore(folder.getRoot().toPath(), codec, request.identity(),
            boundary -> { if (boundary == AtomicRunStore.Boundary.REPLACED) { session.disconnect(); } });
        connect(target);
        CompletableFuture<Optional<MasterSelectionCreationReceipt>> committed = session.createMasterSelection(request);
        tasks.remove(0).run();
        assertEquals(request, committed.join().get()); assertFalse(session.capture().isPresent());
        assertEquals(4, store().load().get().lifecycle().revision());
    }

    @Test public void recoveryDistinguishesAbsenceAndPreservesPausedAdvancedHead() throws Exception
    {
        AtomicRunStore target = initialize();
        connect(target);
        byte[] before = Files.readAllBytes(target.snapshotPath());
        RunSession.Token old = session.capture().get();
        CompletableFuture<Optional<Optional<MasterSelectionCreationReceipt>>> absent =
            session.committedMasterSelection(request);
        assertFalse(session.accepts(old)); assertFalse(absent.isDone());
        tasks.remove(0).run();
        assertTrue(absent.join().isPresent()); assertFalse(absent.join().get().isPresent());
        assertTrue(session.capture().isPresent()); assertFalse(session.accepts(old));
        assertArrayEquals(before, Files.readAllBytes(target.snapshotPath()));
        assertFalse(Files.exists(target.pendingPath()));

        RunDocument paused = LifecycleAdmission.prepare(MasterCreationOperationChecks.fixture(513),
            request.identity(), UUID.randomUUID(), 6, RunReceipt.Operation.PAUSE);
        Files.write(target.snapshotPath(), codec.encode(paused));
        byte[] saved = Files.readAllBytes(target.snapshotPath());
        CompletableFuture<Optional<Optional<MasterSelectionCreationReceipt>>> found =
            session.committedMasterSelection(request);
        tasks.remove(0).run();
        assertEquals(request, found.join().get().get());
        assertEquals(0, found.join().get().get().initial().drawCount());
        assertFalse(session.capture().isPresent());
        assertEquals(513, target.load().get().pendingMasterSelection().get().drawCount());
        assertArrayEquals(saved, Files.readAllBytes(target.snapshotPath()));
    }

    @Test public void supersededRecoverySkipsUnreadableDiskAndPreservesFreshSession() throws Exception
    {
        AtomicRunStore target = initialize();
        connect(target);
        CompletableFuture<Optional<Optional<MasterSelectionCreationReceipt>>> skipped =
            session.committedMasterSelection(request);
        CompletableFuture<Boolean> opened = session.connect(target, request.identity());
        tasks.remove(1).run(); assertTrue(opened.join());
        RunSession.Token fresh = session.capture().get();
        Files.write(target.pendingPath(), new byte[] { 1 });
        tasks.remove(0).run();
        assertFalse(skipped.join().isPresent()); assertTrue(session.accepts(fresh));
    }

    @Test public void recoveryConflictAndMissingSaveCloseAdmission() throws Exception
    {
        AtomicRunStore target = initialize();
        Files.write(target.snapshotPath(), codec.encode(source));
        connect(target);
        byte[] saved = Files.readAllBytes(target.snapshotPath());
        CompletableFuture<Optional<Optional<MasterSelectionCreationReceipt>>> conflict =
            session.committedMasterSelection(new MasterSelectionCreationReceipt(request.identity(), 2, request.initial()));
        tasks.remove(0).run();
        assertTrue(conflict.isCompletedExceptionally()); assertFalse(session.capture().isPresent());
        assertArrayEquals(saved, Files.readAllBytes(target.snapshotPath()));
        RunSession direct = new RunSession();
        direct.connect(target, request.identity());
        RunSession.Token old = direct.capture().get();
        assertEquals(request, direct.committedMasterSelection(request).get());
        assertFalse(direct.accepts(old));
        Files.delete(target.snapshotPath());
        assertThrows(IOException.class, () -> direct.committedMasterSelection(request));
        assertFalse(direct.capture().isPresent());
        assertFalse(Files.exists(target.snapshotPath()));
    }

    @Test public void synchronousBoundaryInvalidatesTokensAndFailsClosed() throws Exception
    {
        AtomicRunStore target = initialize();
        RunSession direct = new RunSession();
        direct.connect(target, request.identity());
        RunSession.Token old = direct.capture().get();
        assertEquals(request, direct.createMasterSelection(request));
        assertFalse(direct.accepts(old));
        RunSession.Token fresh = direct.capture().get();
        assertThrows(IOException.class, () -> direct.createMasterSelection(
            new MasterSelectionCreationReceipt(request.identity(), 2, request.initial())));
        assertFalse(direct.accepts(fresh)); assertFalse(direct.capture().isPresent());
    }
}

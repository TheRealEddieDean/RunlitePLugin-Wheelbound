package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class AsyncRunSessionTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final List<Runnable> tasks = new ArrayList<>();
    private final AsyncRunSession session = new AsyncRunSession(tasks::add);

    private AtomicRunStore store(RunIdentity id, boolean initialize) throws Exception
    {
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), new RunCodec(new Gson()), id);
        if (initialize)
        {
            store.write(new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE,
                RunLifecycle.Obligation.ACTIVE_OBJECTIVE), "fixture"), -1);
        }
        return store;
    }

    private GrandActivationReceipt initializeGrand(RunIdentity id) throws Exception
    {
        SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(0));
        GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
        store(id, false).write(new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), "fixture", java.util.List.of(), java.util.Optional.empty(),
            java.util.Optional.empty(), java.util.Optional.of(seal), java.util.Optional.empty(),
            java.util.Optional.empty(), java.util.Optional.of(new GrandActivationHistory(seal, 0,
                java.util.List.of()))), -1);
        return new GrandActivationReceipt(UUID.randomUUID(), id, 0, checklist,
            checklist.required().iterator().next());
    }

    @Test public void lifecycleCommandsCloseAdmissionAndReplayActualHead() throws Exception
    {
        RunIdentity id = new RunIdentity("pause:profile", UUID.randomUUID());
        AtomicRunStore target = store(id, true);
        session.connect(target, id); tasks.remove(0).run();
        RunSession.Token before = session.capture().get();
        UUID pauseId = UUID.randomUUID();
        CompletableFuture<java.util.Optional<RunReceipt>> pause =
            session.execute(pauseId, 0, RunReceipt.Operation.PAUSE);
        assertFalse(session.accepts(before)); assertFalse(pause.isDone());
        assertEquals(0, target.load().get().lifecycle().revision());
        tasks.remove(0).run();
        assertEquals(RunLifecycle.State.PAUSED, pause.join().get().state());
        assertFalse(session.capture().isPresent());
        session.execute(UUID.randomUUID(), 1, RunReceipt.Operation.RESUME);
        tasks.remove(0).run();
        RunSession.Token resumed = session.capture().get();
        CompletableFuture<java.util.Optional<RunReceipt>> replay =
            session.execute(pauseId, 0, RunReceipt.Operation.PAUSE);
        tasks.remove(0).run();
        assertEquals(pause.join(), replay.join());
        assertFalse(session.accepts(resumed)); assertTrue(session.capture().isPresent());
        assertEquals(2, target.load().get().receipts().size());
        assertEquals(RunLifecycle.Obligation.ACTIVE_OBJECTIVE, target.load().get().lifecycle().obligation());
        assertThrows(IllegalArgumentException.class,
            () -> session.execute(UUID.randomUUID(), 2, RunReceipt.Operation.DRAW));
    }

    @Test public void supersededPauseSkipsStorageAndStaleResumeFailsClosed() throws Exception
    {
        RunIdentity id = new RunIdentity("pause:profile", UUID.randomUUID());
        AtomicRunStore target = store(id, true);
        session.connect(target, id); tasks.remove(0).run();
        CompletableFuture<java.util.Optional<RunReceipt>> skipped =
            session.execute(UUID.randomUUID(), 0, RunReceipt.Operation.PAUSE);
        session.connect(target, id); tasks.remove(1).run();
        RunSession.Token fresh = session.capture().get();
        tasks.remove(0).run();
        assertFalse(skipped.join().isPresent()); assertTrue(session.accepts(fresh));
        assertEquals(0, target.load().get().lifecycle().revision());
        session.execute(UUID.randomUUID(), 0, RunReceipt.Operation.PAUSE); tasks.remove(0).run();
        CompletableFuture<java.util.Optional<RunReceipt>> failed =
            session.execute(UUID.randomUUID(), 0, RunReceipt.Operation.RESUME);
        tasks.remove(0).run();
        assertTrue(failed.isCompletedExceptionally()); assertFalse(session.capture().isPresent());
        assertEquals(RunLifecycle.State.PAUSED, target.load().get().lifecycle().state());
        assertEquals(1, target.load().get().receipts().size());
    }

    @Test public void blockedPauseCannotRestoreAdmissionOverNewConnection() throws Exception
    {
        RunIdentity oldId = new RunIdentity("old:profile", UUID.randomUUID());
        store(oldId, true);
        RunIdentity newId = new RunIdentity("new:profile", UUID.randomUUID());
        AtomicRunStore freshStore = store(newId, true);
        CountDownLatch writing = new CountDownLatch(1), release = new CountDownLatch(1);
        AtomicRunStore blocked = new AtomicRunStore(folder.getRoot().toPath(), new RunCodec(new Gson()), oldId,
            boundary -> {
                if (boundary == AtomicRunStore.Boundary.REPLACED)
                {
                    writing.countDown();
                    try
                    {
                        if (!release.await(5, TimeUnit.SECONDS)) { throw new IOException("Timed out waiting for release"); }
                    }
                    catch (InterruptedException ex) { Thread.currentThread().interrupt(); throw new IOException(ex); }
                }
            });
        ExecutorService workers = Executors.newFixedThreadPool(2);
        AsyncRunSession threaded = new AsyncRunSession(workers);
        try
        {
            assertTrue(threaded.connect(blocked, oldId).get(5, TimeUnit.SECONDS));
            RunSession.Token old = threaded.capture().get();
            CompletableFuture<java.util.Optional<RunReceipt>> pause =
                threaded.execute(UUID.randomUUID(), 0, RunReceipt.Operation.PAUSE);
            assertTrue(writing.await(5, TimeUnit.SECONDS));
            threaded.disconnect();
            assertTrue(threaded.connect(freshStore, newId).get(5, TimeUnit.SECONDS));
            RunSession.Token fresh = threaded.capture().get();
            release.countDown();
            assertTrue(pause.get(5, TimeUnit.SECONDS).isPresent());
            assertTrue(threaded.accepts(fresh)); assertFalse(threaded.accepts(old));
            assertEquals(RunLifecycle.State.PAUSED, store(oldId, false).load().get().lifecycle().state());
        }
        finally
        {
            release.countDown(); workers.shutdownNow();
            assertTrue(workers.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

    @Test public void activationLostAcknowledgementReconnectsAndReplaysOnce() throws Exception
    {
        RunIdentity id = new RunIdentity("grand:profile", UUID.randomUUID());
        GrandActivationReceipt request = initializeGrand(id);
        AtomicRunStore failing = new AtomicRunStore(folder.getRoot().toPath(), new RunCodec(new Gson()), id,
            boundary -> { if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("Lost ack"); } });
        session.connect(failing, id); tasks.remove(0).run();
        CompletableFuture<java.util.Optional<GrandActivationReceipt>> failed = session.activateGrandFate(request, 0);
        tasks.remove(0).run();
        assertTrue(failed.isCompletedExceptionally()); assertFalse(session.capture().isPresent());
        AtomicRunStore reopened = store(id, false);
        byte[] committed = Files.readAllBytes(reopened.snapshotPath());
        session.connect(reopened, id); tasks.remove(0).run();
        CompletableFuture<java.util.Optional<GrandActivationReceipt>> replay = session.activateGrandFate(request, 1);
        tasks.remove(0).run();
        assertEquals(request, replay.join().get()); assertTrue(session.capture().isPresent());
        assertEquals(1, reopened.load().get().receipts().size());
        assertArrayEquals(committed, Files.readAllBytes(reopened.snapshotPath()));
    }

    @Test public void activationClosesAdmissionWhileQueuedAndReplaysPausedHead() throws Exception
    {
        RunIdentity id = new RunIdentity("grand:profile", UUID.randomUUID());
        GrandActivationReceipt request = initializeGrand(id);
        AtomicRunStore target = store(id, false);
        session.connect(target, id); tasks.remove(0).run();
        RunSession.Token before = session.capture().get();
        CompletableFuture<java.util.Optional<GrandActivationReceipt>> command = session.activateGrandFate(request, 0);
        assertFalse(session.accepts(before)); assertFalse(session.capture().isPresent());
        assertFalse(command.isDone()); assertEquals(0, target.load().get().lifecycle().revision());
        tasks.remove(0).run();
        assertEquals(request, command.join().get());
        assertTrue(session.accepts(session.capture().get())); assertFalse(session.accepts(before));
        new LifecycleCommands(target).execute(id, UUID.randomUUID(), 1, RunReceipt.Operation.PAUSE);
        CompletableFuture<java.util.Optional<GrandActivationReceipt>> replay = session.activateGrandFate(request, 2);
        tasks.remove(0).run();
        assertEquals(request, replay.join().get()); assertFalse(session.capture().isPresent());
        assertEquals(2, target.load().get().receipts().size());
    }

    @Test public void supersededQueuedActivationDoesNotWriteOrReplaceNewSession() throws Exception
    {
        RunIdentity id = new RunIdentity("grand:profile", UUID.randomUUID());
        GrandActivationReceipt request = initializeGrand(id);
        AtomicRunStore target = store(id, false);
        session.connect(target, id); tasks.remove(0).run();
        CompletableFuture<java.util.Optional<GrandActivationReceipt>> command = session.activateGrandFate(request, 0);
        session.disconnect();
        session.connect(target, id); tasks.remove(1).run();
        RunSession.Token fresh = session.capture().get();
        tasks.remove(0).run();
        assertFalse(command.join().isPresent()); assertTrue(session.accepts(fresh));
        assertEquals(0, target.load().get().lifecycle().revision());
    }

    @Test public void disconnectDuringCommittedWriteCannotRestoreAdmission() throws Exception
    {
        RunIdentity id = new RunIdentity("grand:profile", UUID.randomUUID());
        GrandActivationReceipt request = initializeGrand(id);
        AtomicRunStore target = new AtomicRunStore(folder.getRoot().toPath(), new RunCodec(new Gson()), id,
            boundary -> { if (boundary == AtomicRunStore.Boundary.REPLACED) { session.disconnect(); } });
        session.connect(target, id); tasks.remove(0).run();
        CompletableFuture<java.util.Optional<GrandActivationReceipt>> command = session.activateGrandFate(request, 0);
        tasks.remove(0).run();
        assertEquals(request, command.join().get()); assertFalse(session.capture().isPresent());
        assertEquals(1, store(id, false).load().get().lifecycle().revision());
    }

    @Test public void failedActivationAndRejectedSubmissionCloseAdmission() throws Exception
    {
        RunIdentity id = new RunIdentity("grand:profile", UUID.randomUUID());
        GrandActivationReceipt request = initializeGrand(id);
        AtomicRunStore target = store(id, false);
        session.connect(target, id); tasks.remove(0).run();
        CompletableFuture<java.util.Optional<GrandActivationReceipt>> failed = session.activateGrandFate(request, 9);
        tasks.remove(0).run();
        assertTrue(failed.isCompletedExceptionally()); assertFalse(session.capture().isPresent());
        assertEquals(0, target.load().get().lifecycle().revision());
        AsyncRunSession rejected = new AsyncRunSession(task -> {
            if (tasks.isEmpty()) { tasks.add(task); }
            else { throw new RejectedExecutionException(); }
        });
        rejected.connect(target, id); tasks.get(0).run();
        RunSession.Token before = rejected.capture().get();
        assertTrue(rejected.activateGrandFate(request, 0).isCompletedExceptionally());
        assertFalse(rejected.accepts(before)); assertFalse(rejected.capture().isPresent());
        assertEquals(0, target.load().get().lifecycle().revision());
    }

    @Test public void disconnectWhileReadQueuedCannotReconnect() throws Exception
    {
        RunIdentity id = new RunIdentity("account:profile", UUID.randomUUID());
        CompletableFuture<Boolean> opened = session.connect(store(id, true), id);
        assertFalse(opened.isDone()); assertFalse(session.capture().isPresent());
        session.disconnect(); tasks.remove(0).run();
        assertFalse(opened.join()); assertFalse(session.capture().isPresent());
    }

    @Test public void reversedProfileReadsPublishOnlyNewestAndInvalidateTokens() throws Exception
    {
        RunIdentity first = new RunIdentity("first:profile", UUID.randomUUID());
        RunIdentity second = new RunIdentity("second:profile", UUID.randomUUID());
        AtomicRunStore a = store(first, true), b = store(second, true);
        session.connect(a, first); tasks.remove(0).run();
        RunSession.Token old = session.capture().get();
        CompletableFuture<Boolean> stale = session.connect(a, first);
        assertFalse(session.accepts(old));
        CompletableFuture<Boolean> latest = session.connect(b, second);
        tasks.remove(1).run(); RunSession.Token fresh = session.capture().get();
        tasks.remove(0).run();
        assertTrue(latest.join()); assertFalse(stale.join());
        assertTrue(session.accepts(fresh)); assertFalse(session.accepts(old));
        session.disconnect(); assertFalse(session.accepts(fresh));
    }

    @Test public void failedReadDoesNotCreateRunOrRestorePreviousAdmission() throws Exception
    {
        RunIdentity id = new RunIdentity("account:profile", UUID.randomUUID());
        session.connect(store(id, true), id); tasks.remove(0).run();
        RunSession.Token old = session.capture().get();
        RunIdentity missing = new RunIdentity("missing:profile", UUID.randomUUID());
        AtomicRunStore absent = store(missing, false);
        CompletableFuture<Boolean> failed = session.connect(absent, missing);
        tasks.remove(0).run();
        assertTrue(failed.isCompletedExceptionally());
        assertFalse(session.accepts(old)); assertFalse(session.capture().isPresent());
        assertFalse(absent.load().isPresent());
    }

    @Test public void rejectedWorkerFailsClosed()
    {
        AsyncRunSession rejected = new AsyncRunSession(task -> { throw new RejectedExecutionException(); });
        RunIdentity id = new RunIdentity("account:profile", UUID.randomUUID());
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), new RunCodec(new Gson()), id);
        assertTrue(rejected.connect(store, id).isCompletedExceptionally());
        assertFalse(rejected.capture().isPresent());
    }

    @Test public void disconnectedQueuedReadSkipsCorruptStorage() throws Exception
    {
        RunIdentity id = new RunIdentity("account:profile", UUID.randomUUID());
        AtomicRunStore corrupt = store(id, true);
        byte[] original = "corrupt snapshot".getBytes(StandardCharsets.UTF_8);
        Files.write(corrupt.snapshotPath(), original);
        CompletableFuture<Boolean> opened = session.connect(corrupt, id);
        session.disconnect();
        tasks.remove(0).run();
        // A storage read would fail exceptionally rather than complete normally.
        assertFalse(opened.join());
        assertFalse(session.capture().isPresent());
        assertArrayEquals(original, Files.readAllBytes(corrupt.snapshotPath()));
    }

    @Test public void supersededCorruptReadCannotAffectNewestSession() throws Exception
    {
        RunIdentity oldId = new RunIdentity("old:profile", UUID.randomUUID());
        AtomicRunStore corrupt = store(oldId, true);
        Files.write(corrupt.snapshotPath(), "corrupt snapshot".getBytes(StandardCharsets.UTF_8));
        CompletableFuture<Boolean> stale = session.connect(corrupt, oldId);
        RunIdentity freshId = new RunIdentity("fresh:profile", UUID.randomUUID());
        CompletableFuture<Boolean> latest = session.connect(store(freshId, true), freshId);
        tasks.remove(1).run();
        RunSession.Token fresh = session.capture().get();
        tasks.remove(0).run();
        assertFalse(stale.join());
        assertTrue(latest.join());
        assertTrue(session.accepts(fresh));
        // Current requests must still report corrupt storage, not silently skip it.
        CompletableFuture<Boolean> failed = session.connect(corrupt, oldId);
        tasks.remove(0).run();
        assertTrue(failed.isCompletedExceptionally());
        assertFalse(session.accepts(fresh));
        assertFalse(session.capture().isPresent());
    }
}

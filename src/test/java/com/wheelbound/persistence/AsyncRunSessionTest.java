package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.RunLifecycle;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
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

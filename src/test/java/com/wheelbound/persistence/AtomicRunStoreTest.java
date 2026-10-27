package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.UUID;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class AtomicRunStoreTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunCodec codec = new RunCodec(new Gson());
    private final RunIdentity id = new RunIdentity("account:profile", UUID.randomUUID());
    private AtomicRunStore store() { return new AtomicRunStore(folder.getRoot().toPath(), codec, id); }
    private RunDocument document(long revision)
    { return new RunDocument(new RunLifecycle(id, revision, RunLifecycle.State.PAUSED, RunLifecycle.Obligation.SETTLEMENT_PENDING), "v1"); }

    @Test public void newRunReopensAndRetainsLastGoodSnapshot() throws Exception
    {
        AtomicRunStore store = store(); assertFalse(store.load().isPresent());
        store.write(document(0), -1); store.write(document(1), 0);
        assertEquals(1, store().load().get().lifecycle().revision());
        assertEquals(0, codec.decode(Files.readAllBytes(store.previousPath()), id).lifecycle().revision());
        assertFalse(Files.exists(store.pendingPath()));
    }

    @Test public void staleAndDuplicateInitialWritesCannotOverwrite() throws Exception
    {
        AtomicRunStore store = store(); store.write(document(0), -1);
        byte[] original = Files.readAllBytes(store.snapshotPath());
        assertThrows(IOException.class, () -> store.write(document(0), -1));
        store.write(document(1), 0);
        assertThrows(IOException.class, () -> store.write(document(1), 0));
        assertEquals(1, store().load().get().lifecycle().revision());
        assertArrayEquals(original, Files.readAllBytes(store.previousPath()));
    }

    @Test public void corruptionIsPreservedAndBlocksFurtherWrites() throws Exception
    {
        AtomicRunStore store = store(); store.write(document(0), -1);
        byte[] corrupt = "original corrupt bytes".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        Files.write(store.snapshotPath(), corrupt);
        assertThrows(IOException.class, store::load);
        assertThrows(IOException.class, () -> store.write(document(1), 0));
        assertArrayEquals(corrupt, Files.readAllBytes(store.snapshotPath()));
    }

    @Test public void interruptedTempWriteRetainsOriginalAndRequiresReconciliation() throws Exception
    {
        AtomicRunStore normal = store(); normal.write(document(0), -1);
        byte[] original = Files.readAllBytes(normal.snapshotPath());
        AtomicRunStore failing = new AtomicRunStore(folder.getRoot().toPath(), codec, id, boundary -> {
            if (boundary == AtomicRunStore.Boundary.TEMP_FORCED) { throw new IOException("Simulated crash"); }
        });
        assertThrows(IOException.class, () -> failing.write(document(1), 0));
        assertArrayEquals(original, Files.readAllBytes(normal.snapshotPath()));
        assertTrue(Files.exists(normal.pendingPath()));
        assertThrows(IOException.class, () -> store().load());
        assertThrows(IOException.class, () -> failing.write(document(1), 0));
    }

    @Test public void lostAcknowledgementAfterReplaceReopensCommittedState() throws Exception
    {
        store().write(document(0), -1);
        AtomicRunStore failing = new AtomicRunStore(folder.getRoot().toPath(), codec, id, boundary -> {
            if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("Lost acknowledgement"); }
        });
        assertThrows(IOException.class, () -> failing.write(document(1), 0));
        assertEquals(1, store().load().get().lifecycle().revision());
        assertThrows(IOException.class, () -> failing.write(document(2), 1));
    }

    @Test public void identitiesUseSeparateOpaqueFilenamesAndForeignWritesFail() throws Exception
    {
        AtomicRunStore store = store(); store.write(document(0), -1);
        AtomicRunStore other = new AtomicRunStore(folder.getRoot().toPath(), codec, new RunIdentity("other", id.runId()));
        assertFalse(other.load().isPresent());
        assertNotEquals(store.snapshotPath(), other.snapshotPath());
        assertFalse(store.snapshotPath().getFileName().toString().contains("account"));
        assertThrows(IllegalArgumentException.class, () -> other.write(document(0), -1));
    }

    @Test public void overlappingWriterIsRejected() throws Exception
    {
        AtomicRunStore store = store(); store.write(document(0), -1);
        try (FileChannel channel = FileChannel.open(store.lockPath(), StandardOpenOption.WRITE);
            FileLock ignored = channel.lock())
        { assertThrows(IOException.class, () -> store.write(document(1), 0)); }
        assertEquals(0, store.load().get().lifecycle().revision());
    }

    @Test public void invalidRevisionJumpFailsBeforeCreatingSave() throws Exception
    {
        AtomicRunStore store = store();
        assertThrows(IllegalArgumentException.class, () -> store.write(document(2), -1));
        assertFalse(store.load().isPresent());
    }
}

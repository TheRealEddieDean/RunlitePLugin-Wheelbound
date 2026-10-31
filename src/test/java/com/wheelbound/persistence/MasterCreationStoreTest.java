package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class MasterCreationStoreTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunCodec codec = new RunCodec(new Gson());
    private final RunDocument source = MasterCreationOperationChecks.fixture(0);
    private final MasterSelectionCreationReceipt request = source.receipts().get(0).masterCreation();

    private RunDocument before()
    {
        return MasterCreationAdmissionChecks.base(source, new RunLifecycle(request.identity(), 3,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), Optional.of(
                new MasterSelectionCreationJournal(request.identity(), 3, List.of())));
    }

    private AtomicRunStore store() { return new AtomicRunStore(folder.getRoot().toPath(), codec, request.identity()); }

    private void restore(AtomicRunStore store, RunDocument document) throws Exception
    {
        Files.createDirectories(store.snapshotPath().getParent());
        Files.write(store.snapshotPath(), codec.encode(document));
    }

    @Test public void commitsEntireTransitionAndReplaysAfterRestartWithoutWriting() throws Exception
    {
        AtomicRunStore store = store();
        restore(store, before());
        byte[] old = Files.readAllBytes(store.snapshotPath());
        assertEquals(request, store.createMasterSelection(request));
        assertArrayEquals(old, Files.readAllBytes(store.previousPath()));
        RunDocument saved = store().load().get();
        assertEquals(4, saved.lifecycle().revision());
        assertEquals(request, saved.receipts().get(0).masterCreation());
        assertEquals(request, saved.masterCreations().get().receipts().get(request.transactionId()));
        assertEquals(0, saved.pendingMasterSelection().get().drawCount());
        assertEquals(-3, saved.fatePoints().get().balance());
        byte[] bytes = Files.readAllBytes(store.snapshotPath());
        assertEquals(request, store().createMasterSelection(request));
        assertArrayEquals(bytes, Files.readAllBytes(store.snapshotPath()));
        assertArrayEquals(old, Files.readAllBytes(store.previousPath()));
        assertFalse(Files.exists(store.pendingPath()));
    }

    @Test public void advancedPausedReplayAndRefusalsLeaveDiskUnchanged() throws Exception
    {
        AtomicRunStore store = store();
        RunDocument advanced = MasterCreationOperationChecks.fixture(513);
        restore(store, LifecycleAdmission.prepare(advanced, request.identity(), UUID.randomUUID(),
            6, RunReceipt.Operation.PAUSE));
        byte[] bytes = Files.readAllBytes(store.snapshotPath());
        assertEquals(request, store.createMasterSelection(request));
        assertEquals(513, store.load().get().pendingMasterSelection().get().drawCount());
        assertThrows(IOException.class, () -> store.createMasterSelection(
            new MasterSelectionCreationReceipt(request.identity(), 4, request.initial())));
        assertArrayEquals(bytes, Files.readAllBytes(store.snapshotPath()));
        assertFalse(Files.exists(store.previousPath()));
        assertFalse(Files.exists(store.pendingPath()));
        restore(store, before());
        bytes = Files.readAllBytes(store.snapshotPath());
        assertThrows(IOException.class, () -> store.createMasterSelection(
            new MasterSelectionCreationReceipt(request.identity(), 2, request.initial())));
        assertArrayEquals(bytes, Files.readAllBytes(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
        Files.write(store.pendingPath(), new byte[] {1});
        assertThrows(IOException.class, () -> store.createMasterSelection(request));
        assertArrayEquals(bytes, Files.readAllBytes(store.snapshotPath()));
    }

    @Test public void missingLegacyCorruptAndForeignRequestsFailClosed() throws Exception
    {
        AtomicRunStore store = store();
        assertThrows(IOException.class, () -> store.createMasterSelection(request));
        RunDocument legacy = MasterCreationAdmissionChecks.base(source, before().lifecycle(), Optional.empty());
        restore(store, legacy);
        byte[] bytes = Files.readAllBytes(store.snapshotPath());
        assertThrows(IOException.class, () -> store.createMasterSelection(request));
        assertThrows(IllegalArgumentException.class, () -> store.createMasterSelection(
            new MasterSelectionCreationReceipt(new RunIdentity("foreign", request.identity().runId()),
                request.beforeRevision(), request.initial())));
        assertArrayEquals(bytes, Files.readAllBytes(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
        Files.write(store.snapshotPath(), new byte[] {1});
        assertThrows(IOException.class, () -> store.createMasterSelection(request));
        assertArrayEquals(new byte[] {1}, Files.readAllBytes(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
    }

    @Test public void failedCommitRequiresReopenAndNeverSilentlyRetries() throws Exception
    {
        for (AtomicRunStore.Boundary failure : AtomicRunStore.Boundary.values())
        {
            Path root = folder.newFolder().toPath();
            AtomicRunStore store = new AtomicRunStore(root, codec, request.identity(), boundary -> {
                if (boundary == failure) { throw new IOException("injected failure"); }
            });
            restore(store, before());
            byte[] old = Files.readAllBytes(store.snapshotPath());
            assertThrows(IOException.class, () -> store.createMasterSelection(request));
            assertThrows(IOException.class, () -> store.createMasterSelection(request));
            assertThrows(IOException.class, () -> store.committedMasterSelection(request));
            AtomicRunStore reopened = new AtomicRunStore(root, codec, request.identity());
            if (failure == AtomicRunStore.Boundary.REPLACED)
            {
                byte[] bytes = Files.readAllBytes(store.snapshotPath());
                assertEquals(request, reopened.committedMasterSelection(request).get());
                assertEquals(request, reopened.createMasterSelection(request));
                assertArrayEquals(bytes, Files.readAllBytes(store.snapshotPath()));
                assertArrayEquals(old, Files.readAllBytes(store.previousPath()));
            }
            else
            {
                assertArrayEquals(old, Files.readAllBytes(store.snapshotPath()));
                assertTrue(Files.exists(store.pendingPath()));
                assertThrows(IOException.class, () -> reopened.createMasterSelection(request));
            }
        }
    }
}

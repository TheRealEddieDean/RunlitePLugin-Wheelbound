package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.MasterSelectionCreationReceipt;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class MasterCreationRecoveryTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunCodec codec = new RunCodec(new Gson());

    @Test public void structuralContracts() { MasterCreationRecoveryChecks.main(new String[0]); }

    @Test public void reopenRecoversOriginalWithoutRewritingAdvancedPausedHead() throws Exception
    {
        RunDocument source = MasterCreationAggregateChecks.fixture(513);
        MasterSelectionCreationReceipt request = source.masterCreations().get().receipts().values().iterator().next();
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, request.identity());
        assertFalse(store.committedMasterSelection(request).isPresent());
        Files.createDirectories(store.snapshotPath().getParent());
        // Restored head fixture, not a claim that creation admission is implemented.
        Files.write(store.snapshotPath(), codec.encode(source));
        store.changeLifecycle(request.identity(), UUID.randomUUID(), 5, RunReceipt.Operation.PAUSE);
        byte[] saved = Files.readAllBytes(store.snapshotPath());
        AtomicRunStore reopened = new AtomicRunStore(folder.getRoot().toPath(), codec, request.identity());
        assertEquals(request, reopened.committedMasterSelection(request).get());
        assertEquals(0, reopened.committedMasterSelection(request).get().initial().drawCount());
        assertEquals(513, reopened.load().get().pendingMasterSelection().get().drawCount());
        assertThrows(IOException.class, () -> reopened.committedMasterSelection(
            new MasterSelectionCreationReceipt(request.identity(), 4, request.initial())));
        assertFalse(reopened.committedMasterSelection(MasterCreationRecoveryChecks.request(request, UUID.randomUUID())).isPresent());
        assertArrayEquals(saved, Files.readAllBytes(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
        Files.write(store.pendingPath(), new byte[] {1});
        assertThrows(IOException.class, () -> reopened.committedMasterSelection(request));
        assertArrayEquals(saved, Files.readAllBytes(store.snapshotPath()));
    }

    @Test public void uncertainInstanceRequiresReopenEvenAfterReplacement() throws Exception
    {
        RunDocument source = MasterCreationAggregateChecks.fixture(1);
        MasterSelectionCreationReceipt request = source.masterCreations().get().receipts().values().iterator().next();
        Path root = folder.getRoot().toPath();
        AtomicRunStore store = new AtomicRunStore(root, codec, request.identity(), boundary -> {
            if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("lost acknowledgement"); }
        });
        Files.createDirectories(store.snapshotPath().getParent());
        Files.write(store.snapshotPath(), codec.encode(source));
        assertThrows(IOException.class, () -> store.changeLifecycle(request.identity(), UUID.randomUUID(),
            5, RunReceipt.Operation.PAUSE));
        byte[] saved = Files.readAllBytes(store.snapshotPath());
        assertThrows(IOException.class, () -> store.committedMasterSelection(request));
        AtomicRunStore reopened = new AtomicRunStore(root, codec, request.identity());
        assertEquals(request, reopened.committedMasterSelection(request).get());
        assertArrayEquals(saved, Files.readAllBytes(store.snapshotPath()));
        Files.write(store.snapshotPath(), new byte[] {1});
        assertThrows(IOException.class, () -> reopened.committedMasterSelection(request));
    }
}

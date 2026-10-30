package com.wheelbound.persistence;

import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class PendingLifecycleTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunCodec codec = new RunCodec(new GsonBuilder().serializeNulls().create());

    @Test public void admissionContracts() { PendingLifecycleChecks.main(new String[0]); }

    @Test public void persistsFrozenSelectionAndReplaysAfterRestart() throws Exception
    {
        Path root = folder.newFolder().toPath();
        AtomicRunStore store = new AtomicRunStore(root, codec, PendingLifecycleChecks.ID);
        // Seed a restored pending head; generic writes deliberately cannot introduce it.
        Files.createDirectories(store.snapshotPath().getParent());
        Files.write(store.snapshotPath(), codec.encode(PendingLifecycleChecks.fixture(513)));
        UUID pause = UUID.randomUUID(), resume = UUID.randomUUID();
        RunReceipt receipt = new LifecycleCommands(store).execute(PendingLifecycleChecks.ID,
            pause, 5, RunReceipt.Operation.PAUSE);
        assertEquals(6, receipt.afterRevision());
        RunDocument paused = store.load().get();
        assertEquals(MasterSelectionProvenanceJson.encode(PendingLifecycleChecks.fixture(513).pendingMasterSelection().get()),
            MasterSelectionProvenanceJson.encode(paused.pendingMasterSelection().get()));
        AtomicRunStore reopened = new AtomicRunStore(root, codec, PendingLifecycleChecks.ID);
        new LifecycleCommands(reopened).execute(PendingLifecycleChecks.ID, resume, 6, RunReceipt.Operation.RESUME);
        byte[] current = Files.readAllBytes(reopened.snapshotPath());
        assertEquals(receipt, new LifecycleCommands(reopened).execute(PendingLifecycleChecks.ID,
            pause, 5, RunReceipt.Operation.PAUSE));
        assertArrayEquals(current, Files.readAllBytes(reopened.snapshotPath()));
        assertThrows(IllegalArgumentException.class, () -> reopened.changeLifecycle(PendingLifecycleChecks.ID,
            PendingLifecycleChecks.TRANSACTION, 7, RunReceipt.Operation.PAUSE));
        assertArrayEquals(current, Files.readAllBytes(reopened.snapshotPath()));
        assertFalse(Files.exists(reopened.pendingPath()));
    }

    @Test public void lostAcknowledgementReplaysFrozenPausedHead() throws Exception
    {
        Path root = folder.newFolder().toPath();
        AtomicRunStore store = new AtomicRunStore(root, codec, PendingLifecycleChecks.ID, boundary -> {
            if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("lost acknowledgement"); }
        });
        Files.createDirectories(store.snapshotPath().getParent());
        Files.write(store.snapshotPath(), codec.encode(PendingLifecycleChecks.fixture(1)));
        UUID command = UUID.randomUUID();
        assertThrows(IOException.class, () -> store.changeLifecycle(PendingLifecycleChecks.ID,
            command, 5, RunReceipt.Operation.PAUSE));
        byte[] committed = Files.readAllBytes(store.snapshotPath());
        AtomicRunStore reopened = new AtomicRunStore(root, codec, PendingLifecycleChecks.ID);
        assertEquals(6, reopened.changeLifecycle(PendingLifecycleChecks.ID,
            command, 5, RunReceipt.Operation.PAUSE).afterRevision());
        assertArrayEquals(committed, Files.readAllBytes(reopened.snapshotPath()));
        assertEquals(1, reopened.load().get().pendingMasterSelection().get().drawCount());
    }

    @Test public void failedWriteRetainsOriginalAndRequiresReconciliation() throws Exception
    {
        Path root = folder.newFolder().toPath();
        AtomicRunStore store = new AtomicRunStore(root, codec, PendingLifecycleChecks.ID, boundary -> {
            if (boundary == AtomicRunStore.Boundary.TEMP_FORCED) { throw new IOException("interrupted"); }
        });
        Files.createDirectories(store.snapshotPath().getParent());
        byte[] original = codec.encode(PendingLifecycleChecks.fixture(1));
        Files.write(store.snapshotPath(), original);
        UUID command = UUID.randomUUID();
        assertThrows(IOException.class, () -> store.changeLifecycle(PendingLifecycleChecks.ID,
            command, 5, RunReceipt.Operation.PAUSE));
        assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
        assertThrows(IOException.class, () -> store.changeLifecycle(PendingLifecycleChecks.ID,
            command, 5, RunReceipt.Operation.PAUSE));
    }
}

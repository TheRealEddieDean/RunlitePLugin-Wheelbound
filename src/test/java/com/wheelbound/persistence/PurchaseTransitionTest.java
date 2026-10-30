package com.wheelbound.persistence;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class PurchaseTransitionTest
{
    @Rule public TemporaryFolder temporary = new TemporaryFolder();

    @Test public void validatesExactPurchaseEffects() { PurchaseTransitionChecks.main(new String[0]); }

    @Test public void rejectionPrecedesPendingWriteAndDoesNotPoisonStore() throws Exception
    {
        PurchaseTransitionChecks.main(new String[0]);
        RunDocument before = PurchaseTransitionChecks.before, after = PurchaseTransitionChecks.after;
        Path root = temporary.newFolder().toPath();
        AtomicRunStore store = new AtomicRunStore(root, new RunCodec(new Gson()), before.lifecycle().identity());
        assertThrows(IOException.class, () -> store.write(after, 0));
        assertFalse(Files.exists(store.pendingPath()));
        store.write(before, -1);
        byte[] saved = Files.readAllBytes(store.snapshotPath());
        RunDocument changedCatalog = new RunDocument(after.lifecycle(), "changed", after.receipts(),
            after.fatePoints(), after.inventory());
        assertThrows(IOException.class, () -> store.write(changedCatalog, 0));
        assertArrayEquals(saved, Files.readAllBytes(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
        store.write(after, 0);
        assertEquals(after.receipts(), store.load().get().receipts());
    }
}

package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class GrandActivationStoreTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunCodec codec = new RunCodec(new Gson());
    private final RunIdentity id = new RunIdentity("activation-store", UUID.randomUUID());
    private AtomicRunStore store() { return new AtomicRunStore(folder.getRoot().toPath(), codec, id); }
    private GrandActivationReceipt initialize(int ticket) throws Exception
    {
        SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
        GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
        RunDocument initial = new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), "frozen", List.of(), Optional.of(new FatePointLedger(-7, List.of())),
            Optional.empty(), Optional.of(seal), Optional.empty(), Optional.empty(),
            Optional.of(new GrandActivationHistory(seal, 0, List.of())));
        store().write(initial, -1);
        return new GrandActivationReceipt(UUID.randomUUID(), id, 0, checklist, checklist.required().iterator().next());
    }

    @Test public void commitsAndReplaysWithoutRewriting() throws Exception
    {
        GrandActivationReceipt request = initialize(0);
        AtomicRunStore store = store();
        assertEquals(request, store.activateGrandFate(request, 0));
        RunDocument saved = store().load().get();
        assertEquals(1, saved.lifecycle().revision());
        assertEquals(request, saved.grandCurrentAttempt().get().activation());
        assertEquals(request, saved.receipts().get(0).activation());
        assertEquals(-7, saved.fatePoints().get().balance());
        byte[] original = Files.readAllBytes(store.snapshotPath());
        byte[] previous = Files.readAllBytes(store.previousPath());
        assertEquals(request, store().activateGrandFate(request, 1));
        assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
        assertArrayEquals(previous, Files.readAllBytes(store.previousPath()));
        assertThrows(IOException.class, () -> store.activateGrandFate(request, 0));
        GrandActivationReceipt collision = new GrandActivationReceipt(UUID.randomUUID(), id, 0,
            request.attempt().checklist(), request.attempt().objective());
        assertThrows(IOException.class, () -> store.activateGrandFate(collision, 1));
        assertFalse(Files.exists(store.pendingPath()));
    }

    @Test public void tempFailurePreservesOriginalAndBlocksReentry() throws Exception
    {
        GrandActivationReceipt request = initialize(1);
        byte[] original = Files.readAllBytes(store().snapshotPath());
        AtomicRunStore failing = new AtomicRunStore(folder.getRoot().toPath(), codec, id, boundary -> {
            if (boundary == AtomicRunStore.Boundary.TEMP_FORCED) { throw new IOException("crash"); }
        });
        assertThrows(IOException.class, () -> failing.activateGrandFate(request, 0));
        assertArrayEquals(original, Files.readAllBytes(store().snapshotPath()));
        assertTrue(Files.exists(store().pendingPath()));
        assertThrows(IOException.class, () -> failing.activateGrandFate(request, 0));
        assertThrows(IOException.class, () -> store().activateGrandFate(request, 0));
    }

    @Test public void lostAcknowledgementRecoversExactReceiptAfterRestart() throws Exception
    {
        GrandActivationReceipt request = initialize(2);
        AtomicRunStore failing = new AtomicRunStore(folder.getRoot().toPath(), codec, id, boundary -> {
            if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("lost ack"); }
        });
        assertThrows(IOException.class, () -> failing.activateGrandFate(request, 0));
        assertThrows(IOException.class, () -> failing.activateGrandFate(request, 1));
        assertEquals(request, store().activateGrandFate(request, 1));
        assertEquals(1, store().load().get().receipts().size());
    }
}

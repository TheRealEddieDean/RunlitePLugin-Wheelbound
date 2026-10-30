package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

/** Synthetic quotes exercise persistence, never price/catalog authorization. */
public class PurchasePersistenceTest
{
    @Rule public TemporaryFolder temporary = new TemporaryFolder();
    private final RunCodec codec = new RunCodec(new Gson());
    private final RunIdentity identity = new RunIdentity("synthetic", UUID.randomUUID());

    private AtomicRunStore store()
    { return new AtomicRunStore(temporary.getRoot().toPath(), codec, identity); }

    private RunDocument initial()
    {
        return new RunDocument(new RunLifecycle(identity, 0, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), "synthetic", List.of(),
            Optional.of(new FatePointLedger(100, List.of())), Optional.of(PurchaseAggregateChecks.inventory()));
    }

    private RunDocument purchase(RunDocument before, long cost)
    {
        SliceInventory.PurchaseClassification tier = before.inventory().get().classifyNewPurchase("smithing");
        SlicePurchaseProposal proposal = SlicePurchaseProposal.propose(before.lifecycle(),
            before.fatePoints().get(), before.inventory().get(), identity, UUID.randomUUID(),
            before.lifecycle().revision(), UUID.randomUUID(), new SlicePurchaseProposal.Quote(
                "smithing", "synthetic", "v1", cost, tier.kind(),
                tier.kind() == SliceInventory.PurchaseKind.FIRST_INSTANCE ? 0 : tier.duplicateOrdinal()));
        List<RunReceipt> journal = new ArrayList<>(before.receipts());
        journal.add(new RunReceipt(proposal.debit().commandId(), RunReceipt.Operation.SLICE_PURCHASE,
            before.lifecycle().revision(), proposal.lifecycle().revision(), RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE, null, null, proposal.receipt()));
        return new RunDocument(proposal.lifecycle(), "synthetic", journal,
            Optional.of(proposal.ledger()), Optional.of(proposal.inventory()));
    }

    @Test public void restartRetainsOriginalResponseAfterLaterDuplicate() throws Exception
    {
        AtomicRunStore store = store();
        RunDocument before = initial(), first = purchase(before, 7), second = purchase(first, 9);
        SlicePurchaseReceipt original = first.receipts().get(0).purchase();
        assertFalse(store.committedPurchase(original.debit().commandId()).isPresent());
        store.write(before, -1);
        store.write(first, 0);
        store.write(second, 1);
        AtomicRunStore reopened = store();
        SlicePurchaseReceipt restored = reopened.committedPurchase(original.debit().commandId()).get();
        assertTrue(restored.matches(identity, original.debit().commandId(), 0,
            original.instanceId(), original.quote()));
        assertFalse(restored.matches(identity, original.debit().commandId(), 1,
            original.instanceId(), original.quote()));
        assertEquals(93, restored.debit().balance());
        assertEquals(1, restored.debit().afterRevision());
        RunDocument saved = reopened.load().get();
        assertEquals(84, saved.fatePoints().get().balance());
        assertEquals(7, saved.inventory().get().activeWheel().slices().size());
        assertEquals(1, saved.inventory().get().acquisitionHistory().get("smithing").paidDuplicates());
        byte[] bytes = Files.readAllBytes(store.snapshotPath());
        assertFalse(reopened.committedPurchase(UUID.randomUUID()).isPresent());
        assertThrows(IOException.class, () -> reopened.write(first, 0));
        assertArrayEquals(bytes, Files.readAllBytes(store.snapshotPath()));
    }

    @Test public void interruptedPurchaseCannotBeReportedCommittedOrRetried() throws Exception
    {
        RunDocument before = initial(), after = purchase(before, 7);
        AtomicRunStore normal = store();
        normal.write(before, -1);
        byte[] original = Files.readAllBytes(normal.snapshotPath());
        AtomicRunStore failing = new AtomicRunStore(temporary.getRoot().toPath(), codec, identity, boundary -> {
            if (boundary == AtomicRunStore.Boundary.TEMP_FORCED) { throw new IOException("Interrupted"); }
        });
        assertThrows(IOException.class, () -> failing.write(after, 0));
        assertArrayEquals(original, Files.readAllBytes(normal.snapshotPath()));
        assertTrue(Files.exists(normal.pendingPath()));
        assertThrows(IOException.class, () -> store().committedPurchase(after.receipts().get(0).commandId()));
        assertThrows(IOException.class, () -> failing.write(after, 0));
        assertThrows(IOException.class, () -> store().write(after, 0));
        assertArrayEquals(original, Files.readAllBytes(normal.snapshotPath()));
    }

    @Test public void lostAcknowledgementRestoresOneCompletePurchase() throws Exception
    {
        RunDocument before = initial(), after = purchase(before, 7);
        store().write(before, -1);
        AtomicRunStore failing = new AtomicRunStore(temporary.getRoot().toPath(), codec, identity, boundary -> {
            if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("Lost acknowledgement"); }
        });
        assertThrows(IOException.class, () -> failing.write(after, 0));
        assertThrows(IOException.class, () -> failing.write(after, 0));
        AtomicRunStore reopened = store();
        SlicePurchaseReceipt receipt = reopened.committedPurchase(after.receipts().get(0).commandId()).get();
        assertEquals(93, receipt.debit().balance());
        assertEquals(after.receipts(), reopened.load().get().receipts());
        assertEquals(6, reopened.load().get().inventory().get().activeWheel().slices().size());
        assertEquals(1, reopened.load().get().fatePoints().get().entries().size());
        assertEquals(0, codec.decode(Files.readAllBytes(reopened.previousPath()), identity).lifecycle().revision());
        assertFalse(Files.exists(reopened.pendingPath()));
        assertThrows(IOException.class, () -> reopened.write(after, 0));
    }

    @Test public void lookupRejectsOtherOperationsAndCorruptSnapshots() throws Exception
    {
        RunDocument before = initial();
        UUID command = UUID.randomUUID();
        RunReceipt pause = new RunReceipt(command, RunReceipt.Operation.PAUSE, 0, 1,
            RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
        RunDocument paused = new RunDocument(new RunLifecycle(identity, 1, RunLifecycle.State.PAUSED,
            RunLifecycle.Obligation.NONE), "synthetic", List.of(pause), before.fatePoints(), before.inventory());
        AtomicRunStore store = store();
        store.write(before, -1);
        store.write(paused, 0);
        assertThrows(IOException.class, () -> store.committedPurchase(command));
        assertThrows(NullPointerException.class, () -> store.committedPurchase(null));
        AtomicRunStore foreign = new AtomicRunStore(temporary.getRoot().toPath(), codec,
            new RunIdentity("foreign", identity.runId()));
        assertFalse(foreign.committedPurchase(command).isPresent());
        Path snapshot = store.snapshotPath();
        Files.write(snapshot, new byte[] { 0 });
        assertThrows(IOException.class, () -> store.committedPurchase(command));
        assertArrayEquals(new byte[] { 0 }, Files.readAllBytes(snapshot));
    }
}

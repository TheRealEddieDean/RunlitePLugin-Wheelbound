package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.*;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class MasterReadinessSessionTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunIdentity identity = new RunIdentity("readiness", UUID.randomUUID());
    private final RunCodec codec = new RunCodec(new Gson());
    private final List<Runnable> tasks = new ArrayList<>();
    private final AsyncRunSession session = new AsyncRunSession(tasks::add);

    private RunDocument document(long revision)
    {
        List<ActiveWheel.Slice> slices = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        { slices.add(ActiveWheel.Slice.ordinary(UUID.randomUUID(), "activity" + (i % 3), ActiveWheel.Enhancement.BASE)); }
        SliceInventory inventory = new SliceInventory(new ActiveWheel(slices), List.of(),
            SliceInventory.SatchelTier.BASE, Set.of("activity0", "activity1", "activity2"),
            Map.of("activity0", new SliceInventory.AcquisitionHistory(0),
                "activity1", new SliceInventory.AcquisitionHistory(0),
                "activity2", new SliceInventory.AcquisitionHistory(0)));
        return new RunDocument(new RunLifecycle(identity, revision, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), "catalog", List.of(), Optional.empty(), Optional.of(inventory));
    }

    private Map<String, MasterOrdinaryPoolEvidence> pools()
    {
        Map<String, MasterOrdinaryPoolEvidence> result = new HashMap<>();
        for (int i = 0; i < 3; i++)
        { result.put("activity" + i, new MasterOrdinaryPoolEvidence(identity, 6,
            "snapshot", "catalog", "activity" + i, Set.of("normal"), Map.of("normal",
                new MasterSelectionReadiness.Classification(MasterSelectionReadiness.Availability.ASSIGNABLE,
                    "Offline fixture")))); }
        return result;
    }

    private AtomicRunStore initialize() throws Exception
    {
        AtomicRunStore target = new AtomicRunStore(folder.getRoot().toPath(), codec, identity);
        Files.createDirectories(target.snapshotPath().getParent());
        Files.write(target.snapshotPath(), codec.encode(document(6)));
        connect(target);
        return target;
    }

    private void connect(AtomicRunStore target)
    {
        CompletableFuture<Boolean> opened = session.connect(target, identity);
        tasks.remove(tasks.size() - 1).run();
        assertTrue(opened.join());
    }

    @Test public void readsHeadWithoutWritingAndFreezesQueuedEvidence() throws Exception
    {
        AtomicRunStore target = initialize();
        byte[] before = Files.readAllBytes(target.snapshotPath());
        RunSession.Token old = session.capture().get();
        Map<String, MasterOrdinaryPoolEvidence> evidence = pools();
        CompletableFuture<Optional<MasterSelectionReadiness>> result = session.masterReadiness(old, "snapshot", evidence);
        evidence.clear();
        assertFalse(session.accepts(old)); assertFalse(result.isDone());
        tasks.remove(0).run();
        assertEquals(MasterSelectionReadiness.Status.READY, result.join().get().status());
        assertTrue(session.capture().isPresent()); assertFalse(session.accepts(old));
        assertArrayEquals(before, Files.readAllBytes(target.snapshotPath()));
        assertFalse(Files.exists(target.pendingPath()));
    }

    @Test public void staleDurableHeadFailsClosedWithoutWriting() throws Exception
    {
        AtomicRunStore target = initialize();
        CompletableFuture<Optional<MasterSelectionReadiness>> result = session.masterReadiness(
            session.capture().get(), "snapshot", pools());
        Files.write(target.snapshotPath(), codec.encode(document(7)));
        byte[] before = Files.readAllBytes(target.snapshotPath());
        tasks.remove(0).run();
        assertTrue(result.isCompletedExceptionally()); assertFalse(session.capture().isPresent());
        assertArrayEquals(before, Files.readAllBytes(target.snapshotPath()));
    }

    @Test public void supersededReadSkipsStorageAndStaleTokenPreservesFreshConnection() throws Exception
    {
        AtomicRunStore target = initialize();
        RunSession.Token old = session.capture().get();
        CompletableFuture<Optional<MasterSelectionReadiness>> result = session.masterReadiness(old, "snapshot", pools());
        connect(target);
        RunSession.Token fresh = session.capture().get();
        Files.write(target.snapshotPath(), new byte[] { 0 });
        tasks.remove(0).run();
        assertFalse(result.join().isPresent()); assertTrue(session.accepts(fresh));
        assertTrue(session.masterReadiness(old, "snapshot", pools()).isCompletedExceptionally());
        assertTrue(session.accepts(fresh)); assertTrue(tasks.isEmpty());
    }

    @Test public void unreadableHeadAndEvidenceMismatchCloseAdmission() throws Exception
    {
        AtomicRunStore target = initialize();
        CompletableFuture<Optional<MasterSelectionReadiness>> mismatch = session.masterReadiness(
            session.capture().get(), "changed", pools());
        tasks.remove(0).run();
        assertTrue(mismatch.isCompletedExceptionally()); assertFalse(session.capture().isPresent());
        connect(target);
        CompletableFuture<Optional<MasterSelectionReadiness>> unreadable = session.masterReadiness(
            session.capture().get(), "snapshot", pools());
        Files.write(target.snapshotPath(), new byte[] { 0 });
        tasks.remove(0).run();
        assertTrue(unreadable.isCompletedExceptionally()); assertFalse(session.capture().isPresent());
    }
}

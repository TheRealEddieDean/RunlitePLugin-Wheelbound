package com.wheelbound.persistence;

import com.google.gson.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class MasterCreationAggregateTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunCodec codec = new RunCodec(new Gson());

    @Test public void structuralContracts() { MasterCreationAggregateChecks.main(new String[0]); }

    @Test public void roundTripAndExplicitLegacyAbsence() throws Exception
    {
        for (int count : new int[] {0, 513})
        {
            RunDocument source = MasterCreationAggregateChecks.fixture(count);
            RunDocument restored = codec.decode(codec.encode(source), source.lifecycle().identity());
            assertEquals(source.masterCreations().get().receipts(), restored.masterCreations().get().receipts());
            assertEquals(count, restored.pendingMasterSelection().get().drawCount());
            assertArrayEquals(codec.encode(source), codec.encode(restored));
        }
        RunDocument source = MasterCreationAggregateChecks.fixture(0);
        JsonObject envelope = new JsonParser().parse(new String(codec.encode(source), StandardCharsets.UTF_8)).getAsJsonObject();
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        payload.remove("masterCreations"); payload.remove("masterResolutions");
        // Preserve required explicit nulls; only masterCreations is absent in schema 14.
        String text = new GsonBuilder().serializeNulls().create().toJson(payload);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        byte[] missing = new Gson().toJson(envelope).getBytes(StandardCharsets.UTF_8);
        assertThrows(IOException.class, () -> codec.decode(missing, source.lifecycle().identity()));
        envelope.addProperty("schemaVersion", 14);
        RunDocument legacy = codec.decode(new Gson().toJson(envelope).getBytes(StandardCharsets.UTF_8), source.lifecycle().identity());
        assertFalse(legacy.masterCreations().isPresent());
        assertTrue(legacy.pendingMasterSelection().isPresent());
        assertFalse(codec.decode(codec.encode(legacy), source.lifecycle().identity()).masterCreations().isPresent());
    }

    @Test public void storePreservesHistoryThroughLifecycleAndRejectsGenericWrites() throws Exception
    {
        RunDocument source = MasterCreationAggregateChecks.fixture(513);
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, source.lifecycle().identity());
        // Restored test head only: no creation admission exists yet.
        Files.createDirectories(store.snapshotPath().getParent());
        Files.write(store.snapshotPath(), codec.encode(source));
        UUID command = UUID.randomUUID();
        store.changeLifecycle(source.lifecycle().identity(), command, 5, RunReceipt.Operation.PAUSE);
        AtomicRunStore reopened = new AtomicRunStore(folder.getRoot().toPath(), codec, source.lifecycle().identity());
        RunDocument paused = reopened.load().get();
        assertEquals(source.masterCreations().get().receipts(), paused.masterCreations().get().receipts());
        byte[] bytes = Files.readAllBytes(reopened.snapshotPath());
        reopened.changeLifecycle(source.lifecycle().identity(), command, 5, RunReceipt.Operation.PAUSE);
        assertArrayEquals(bytes, Files.readAllBytes(reopened.snapshotPath()));
        RunDocument next = LifecycleAdmission.prepare(paused, source.lifecycle().identity(), UUID.randomUUID(), 6, RunReceipt.Operation.RESUME);
        assertThrows(IOException.class, () -> reopened.write(next, 6));
        RunDocument discarded = MasterCreationAggregateChecks.copy(next, Optional.empty(), Optional.empty());
        assertThrows(IOException.class, () -> reopened.write(discarded, 6));
        assertArrayEquals(bytes, Files.readAllBytes(reopened.snapshotPath()));
        assertFalse(Files.exists(reopened.pendingPath()));
    }

    @Test public void explicitEmptyHistoryIsDistinctAndCannotBeIntroducedGenerically() throws Exception
    {
        RunDocument initial = new RunDocument(new com.wheelbound.domain.RunLifecycle(PendingLifecycleChecks.ID, 0,
            com.wheelbound.domain.RunLifecycle.State.ACTIVE, com.wheelbound.domain.RunLifecycle.Obligation.NONE), "catalog");
        RunDocument explicit = MasterCreationAggregateChecks.copy(initial, Optional.empty(), Optional.of(
            new com.wheelbound.domain.MasterSelectionCreationJournal(PendingLifecycleChecks.ID, 0, List.of())));
        assertFalse(codec.decode(codec.encode(initial), PendingLifecycleChecks.ID).masterCreations().isPresent());
        assertTrue(codec.decode(codec.encode(explicit), PendingLifecycleChecks.ID).masterCreations().isPresent());
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, PendingLifecycleChecks.ID);
        assertThrows(IOException.class, () -> store.write(explicit, -1));
        assertFalse(Files.exists(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
    }
}

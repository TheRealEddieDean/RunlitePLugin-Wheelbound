package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class PendingMasterSelectionTest
{
    @Test public void structuralAggregateContracts() { PendingMasterSelectionChecks.main(new String[0]); }
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    private final RunCodec codec = new RunCodec(gson);
    private final RunIdentity id = new RunIdentity("pending-selection", UUID.randomUUID());

    private RunDocument document(RunLifecycle.State state, RunLifecycle.Obligation obligation,
        String catalog, MasterSelectionProvenance pending, long revision)
    {
        return new RunDocument(new RunLifecycle(id, revision, state, obligation), catalog, List.of(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.ofNullable(pending));
    }

    @Test public void roundTripsUnresolvedPrefixesInActiveAndPausedHeads() throws Exception
    {
        for (RunLifecycle.State state : List.of(RunLifecycle.State.ACTIVE, RunLifecycle.State.PAUSED))
        for (int count : new int[] {0, 1, 513})
        {
            MasterSelectionProvenance original = MasterSelectionProvenanceJsonTest.fixture(null, count, false);
            RunDocument source = document(state, RunLifecycle.Obligation.NONE, "catalog-2", original, 7);
            byte[] bytes = codec.encode(source);
            RunDocument restored = codec.decode(bytes, id);
            MasterSelectionProvenance pending = restored.pendingMasterSelection().get();
            assertEquals(count, pending.drawCount());
            assertEquals(original.currentStreamState(), pending.currentStreamState());
            assertEquals(MasterSelectionProvenanceJson.encode(original), MasterSelectionProvenanceJson.encode(pending));
            assertEquals(state, restored.lifecycle().state());
            assertArrayEquals(bytes, codec.encode(restored));
            assertFalse(restored.fatePoints().isPresent());
            assertTrue(restored.receipts().isEmpty());
        }
    }

    @Test public void explicitAbsenceAndSchemaThirteenAbsenceStayAbsent() throws Exception
    {
        byte[] bytes = codec.encode(document(RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, "catalog-2", null, 0));
        assertFalse(codec.decode(bytes, id).pendingMasterSelection().isPresent());
        JsonObject envelope = new JsonParser().parse(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        assertTrue(payload.get("pendingMasterSelection").isJsonNull());
        // Missing is invalid in schema 14, and unavailable in schema 13.
        payload.remove("pendingMasterSelection"); payload.remove("masterCreations");
        byte[] missing = edited(envelope, payload, 14);
        assertThrows(IOException.class, () -> codec.decode(missing, id));
        RunDocument legacy = codec.decode(edited(envelope, payload, 13), id);
        assertFalse(legacy.pendingMasterSelection().isPresent());
        assertFalse(codec.decode(codec.encode(legacy), id).pendingMasterSelection().isPresent());
    }

    private byte[] edited(JsonObject envelope, JsonObject payload, int schema)
    {
        String text = gson.toJson(payload);
        envelope.addProperty("schemaVersion", schema);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return gson.toJson(envelope).getBytes(StandardCharsets.UTF_8);
    }

    @Test public void rejectsResolvedObligatedTerminalAndChangedCatalogContexts()
    {
        MasterSelectionProvenance pending = MasterSelectionProvenanceJsonTest.fixture(null, 1, false);
        for (RunLifecycle.State state : List.of(RunLifecycle.State.NOT_STARTED,
            RunLifecycle.State.ABANDONED, RunLifecycle.State.COMPLETED))
        { assertThrows(IllegalArgumentException.class, () -> document(state, RunLifecycle.Obligation.NONE, "catalog-2", pending, 0)); }
        for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
        {
            if (obligation == RunLifecycle.Obligation.NONE) { continue; }
            assertThrows(IllegalArgumentException.class, () -> document(RunLifecycle.State.ACTIVE, obligation, "catalog-2", pending, 0));
        }
        assertThrows(IllegalArgumentException.class, () -> document(RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE, "catalog-3", pending, 0));
        for (ActiveWheel.Kind special : new ActiveWheel.Kind[] {null, ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE})
        { assertThrows(IllegalArgumentException.class, () -> document(RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE, "catalog-2", MasterSelectionProvenanceJsonTest.fixture(special, 1, true), 0)); }
    }

    @Test public void genericStoreCannotIntroduceOrDiscardPendingSelection() throws Exception
    {
        MasterSelectionProvenance pending = MasterSelectionProvenanceJsonTest.fixture(null, 3, false);
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, id);
        RunDocument retained = document(RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, "catalog-2", pending, 0);
        assertThrows(IOException.class, () -> store.write(retained, -1));
        assertFalse(Files.exists(store.snapshotPath()));
        assertFalse(Files.exists(store.pendingPath()));
        // Seed only as a codec fixture: no production admission command exists.
        byte[] original = codec.encode(retained);
        Files.write(store.snapshotPath(), original);
        assertEquals(3, store.load().get().pendingMasterSelection().get().drawCount());
        for (MasterSelectionProvenance next : new MasterSelectionProvenance[] {null, pending})
        {
            assertThrows(IOException.class, () -> store.write(document(RunLifecycle.State.PAUSED,
                RunLifecycle.Obligation.NONE, "catalog-2", next, 1), 0));
            assertArrayEquals(original, Files.readAllBytes(store.snapshotPath()));
            assertFalse(Files.exists(store.pendingPath()));
        }
    }
}

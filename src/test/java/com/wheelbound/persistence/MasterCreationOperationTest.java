package com.wheelbound.persistence;

import com.google.gson.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterCreationOperationTest
{
    @org.junit.Rule public org.junit.rules.TemporaryFolder folder = new org.junit.rules.TemporaryFolder();

    @Test public void chainContracts() { MasterCreationOperationChecks.main(new String[0]); }

    @Test public void restartAndLifecyclePreserveTypedCreation() throws Exception
    {
        RunCodec codec = new RunCodec(new Gson());
        RunDocument source = MasterCreationOperationChecks.fixture(513);
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), codec, source.lifecycle().identity());
        java.nio.file.Files.createDirectories(store.snapshotPath().getParent());
        java.nio.file.Files.write(store.snapshotPath(), codec.encode(source));
        store.changeLifecycle(source.lifecycle().identity(), new java.util.UUID(40, 1), 6, RunReceipt.Operation.PAUSE);
        AtomicRunStore reopened = new AtomicRunStore(folder.getRoot().toPath(), codec, source.lifecycle().identity());
        RunDocument paused = reopened.load().get();
        assertEquals(source.receipts().get(0), paused.receipts().get(0));
        assertEquals(513, paused.pendingMasterSelection().get().drawCount());
        assertEquals(source.fatePoints().get().balance(), paused.fatePoints().get().balance());
        assertEquals(source.receipts().get(0).masterCreation(), reopened.committedMasterSelection(
            source.receipts().get(0).masterCreation()).get());
        byte[] bytes = java.nio.file.Files.readAllBytes(reopened.snapshotPath());
        reopened.changeLifecycle(source.lifecycle().identity(), new java.util.UUID(40, 1), 6, RunReceipt.Operation.PAUSE);
        assertArrayEquals(bytes, java.nio.file.Files.readAllBytes(reopened.snapshotPath()));
    }

    @Test public void roundTripAndEnvelopeTampering() throws Exception
    {
        RunCodec codec = new RunCodec(new Gson());
        for (int count : new int[] {0, 513})
        {
            RunDocument source = MasterCreationOperationChecks.fixture(count);
            byte[] bytes = codec.encode(source);
            RunDocument restored = codec.decode(bytes, source.lifecycle().identity());
            assertEquals(source.receipts(), restored.receipts());
            assertEquals(count, restored.pendingMasterSelection().get().drawCount());
            assertArrayEquals(bytes, codec.encode(restored));
            for (String field : new String[] {"commandId", "beforeRevision", "afterRevision", "state", "obligation", "masterCreation"})
            {
                JsonObject envelope = new JsonParser().parse(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
                JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
                JsonObject receipt = payload.getAsJsonArray("receipts").get(0).getAsJsonObject();
                if (field.equals("masterCreation")) { receipt.remove(field); }
                else if (field.equals("commandId")) { receipt.addProperty(field, new java.util.UUID(99, 1).toString()); }
                else if (field.endsWith("Revision")) { receipt.addProperty(field, 1); }
                else { receipt.addProperty(field, field.equals("state") ? "PAUSED" : "ACTIVE_OBJECTIVE"); }
                byte[] changed = encodeEnvelope(envelope, payload);
                assertThrows(IOException.class, () -> codec.decode(changed, source.lifecycle().identity()));
            }
            JsonObject envelope = new JsonParser().parse(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
            envelope.addProperty("schemaVersion", 15);
            assertThrows(IOException.class, () -> codec.decode(new Gson().toJson(envelope).getBytes(StandardCharsets.UTF_8), source.lifecycle().identity()));
        }
        RunDocument legacy = MasterCreationAggregateChecks.fixture(0);
        JsonObject envelope = new JsonParser().parse(new String(codec.encode(legacy), StandardCharsets.UTF_8)).getAsJsonObject();
        envelope.addProperty("schemaVersion", 15);
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        payload.remove("masterResolutions"); payload.remove("masterSpins");
        RunDocument restored = codec.decode(encodeEnvelope(envelope, payload), legacy.lifecycle().identity());
        assertEquals(legacy.masterCreations().get().receipts(), restored.masterCreations().get().receipts());
    }

    private static byte[] encodeEnvelope(JsonObject envelope, JsonObject payload)
    {
        String text = new GsonBuilder().serializeNulls().create().toJson(payload);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return new Gson().toJson(envelope).getBytes(StandardCharsets.UTF_8);
    }
}

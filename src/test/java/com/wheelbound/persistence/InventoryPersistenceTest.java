package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class InventoryPersistenceTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final Gson gson = new Gson().newBuilder().serializeNulls().create();
    private final RunCodec codec = new RunCodec(gson);
    private final RunIdentity id = new RunIdentity("account:profile", UUID.randomUUID());

    private SliceInventory inventory()
    {
        List<ActiveWheel.Slice> active = new ArrayList<>();
        for (String key : List.of("mining", "mining", "fishing", "fishing", "combat"))
        { active.add(ActiveWheel.Slice.ordinary(UUID.randomUUID(), key, ActiveWheel.Enhancement.SECOND)); }
        active.add(ActiveWheel.Slice.special(UUID.randomUUID(), ActiveWheel.Kind.TAINT, 7));
        active.add(ActiveWheel.Slice.special(UUID.randomUUID(), ActiveWheel.Kind.SACRIFICE, 9));
        Map<String, SliceInventory.AcquisitionHistory> history = new HashMap<>();
        for (String key : List.of("mining", "fishing", "combat", "questing", "destroyed"))
        { history.put(key, new SliceInventory.AcquisitionHistory(key.equals("destroyed") ? Long.MAX_VALUE : 2)); }
        return new SliceInventory(new ActiveWheel(active), List.of(ActiveWheel.Slice.ordinary(
            UUID.randomUUID(), "questing", ActiveWheel.Enhancement.FOURTH)), SliceInventory.SatchelTier.THIRD,
            Set.of("mining", "fishing", "combat", "questing", "destroyed", "unlock-only"), history);
    }

    private RunDocument document()
    {
        return new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
            "v1", List.of(), Optional.of(new FatePointLedger(100, List.of())), Optional.of(inventory()));
    }

    private JsonObject payload(RunDocument source)
    {
        JsonObject envelope = new JsonParser().parse(new String(codec.encode(source), StandardCharsets.UTF_8)).getAsJsonObject();
        return new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
    }

    private byte[] bytes(JsonObject payload, int schema)
    {
        if (schema < 8) { payload.remove("grandFate"); }
        if (schema < 9) { payload.remove("grandProgress"); }
        if (schema < 10) { payload.remove("grandJournal"); }
        if (schema < 11) { payload.remove("grandActivations"); payload.remove("grandCurrentAttempt"); }
        String text = gson.toJson(payload);
        JsonObject envelope = new JsonObject();
        envelope.addProperty("schemaVersion", schema); envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return gson.toJson(envelope).getBytes(StandardCharsets.UTF_8);
    }

    @Test public void roundTripRetainsCompleteInventoryWithoutInferringHistory() throws Exception
    {
        RunDocument source = document();
        RunDocument restored = codec.decode(codec.encode(source), id);
        assertEquals(payload(source).get("inventory"), payload(restored).get("inventory"));
        SliceInventory result = restored.inventory().get();
        assertEquals(7, result.activeWheel().slices().size());
        assertEquals(7, result.activeWheel().slices().get(5).weightUnits());
        assertEquals(ActiveWheel.Enhancement.FOURTH, result.stored().get(0).enhancement());
        assertEquals(Long.MAX_VALUE, result.acquisitionHistory().get("destroyed").paidDuplicates());
        assertFalse(result.acquisitionHistory().containsKey("unlock-only"));
        assertEquals(3, result.classifyNewPurchase("questing").duplicateOrdinal());
    }

    @Test public void oldSchemasKeepInventoryUnavailableAndRejectNewField() throws Exception
    {
        JsonObject unavailable = payload(document()); unavailable.add("inventory", JsonNull.INSTANCE);
        assertFalse(codec.decode(bytes(unavailable, 6), id).inventory().isPresent());
        for (int schema = 1; schema <= 5; schema++)
        {
            JsonObject value = payload(document());
            value.remove("inventory");
            if (schema < 4) { value.remove("fatePoints"); }
            if (schema == 1) { value.remove("receipts"); }
            byte[] legacy = bytes(value, schema); byte[] original = legacy.clone();
            RunDocument restored = codec.decode(legacy, id);
            assertFalse(restored.inventory().isPresent());
            assertFalse(codec.decode(codec.encode(restored), id).inventory().isPresent());
            assertArrayEquals(original, legacy);
            value.add("inventory", payload(document()).get("inventory"));
            final int version = schema;
            assertThrows(IOException.class, () -> codec.decode(bytes(value, version), id));
        }
        JsonObject missing = payload(document()); missing.remove("inventory");
        assertThrows(IOException.class, () -> codec.decode(bytes(missing, 6), id));
    }

    @Test public void malformedInventoriesFailClosedThroughDomainValidation()
    {
        List<java.util.function.Consumer<JsonObject>> mutations = List.of(
            v -> v.addProperty("satchelTier", "FUTURE"),
            v -> v.getAsJsonArray("stored").add(v.getAsJsonArray("active").get(0)),
            v -> v.getAsJsonArray("stored").add(v.getAsJsonArray("active").get(5)),
            v -> v.getAsJsonArray("unlocks").add("mining"),
            v -> v.getAsJsonArray("history").add(v.getAsJsonArray("history").get(0)),
            v -> v.getAsJsonArray("history").get(0).getAsJsonObject().addProperty("paidDuplicates", -1),
            v -> v.getAsJsonArray("history").get(0).getAsJsonObject().addProperty("paidDuplicates", "1"),
            v -> v.add("history", new JsonArray()),
            v -> v.add("active", new JsonArray()),
            v -> v.getAsJsonArray("active").get(0).getAsJsonObject().addProperty("weightUnits", 999),
            v -> v.addProperty("unexpected", true));
        for (java.util.function.Consumer<JsonObject> mutate : mutations)
        {
            JsonObject value = payload(document()); mutate.accept(value.getAsJsonObject("inventory"));
            byte[] encoded = bytes(value, 6); byte[] original = encoded.clone();
            assertThrows(IOException.class, () -> codec.decode(encoded, id));
            assertArrayEquals(original, encoded);
        }
    }

    private AtomicRunStore store() { return new AtomicRunStore(folder.getRoot().toPath(), codec, id); }

    @Test public void failedReplacementPreservesInventoryAndBlocksFurtherWrites() throws Exception
    {
        RunDocument source = document(); store().write(source, -1);
        byte[] original = java.nio.file.Files.readAllBytes(store().snapshotPath());
        AtomicRunStore failing = new AtomicRunStore(folder.getRoot().toPath(), codec, id, boundary -> {
            if (boundary == AtomicRunStore.Boundary.TEMP_FORCED) { throw new IOException("Before replacement"); }
        });
        assertThrows(IOException.class, () -> new LifecycleCommands(failing)
            .execute(id, UUID.randomUUID(), 0, RunReceipt.Operation.PAUSE));
        assertArrayEquals(original, java.nio.file.Files.readAllBytes(store().snapshotPath()));
        assertEquals(payload(source).get("inventory"), payload(codec.decode(original, id)).get("inventory"));
        assertThrows(IOException.class, () -> new LifecycleCommands(store())
            .execute(id, UUID.randomUUID(), 0, RunReceipt.Operation.PAUSE));
    }

    @Test public void inventorySurvivesMixedCommandsAndLostAcknowledgementReplay() throws Exception
    {
        RunDocument source = document(); store().write(source, -1);
        new LifecycleCommands(store()).execute(id, UUID.randomUUID(), 0, RunReceipt.Operation.PAUSE);
        new LifecycleCommands(store()).execute(id, UUID.randomUUID(), 1, RunReceipt.Operation.RESUME);
        new DurableWheelDraws(store(), bound -> 0).commit(id, UUID.randomUUID(), 2,
            new WeightedPool("v1", List.of(new WeightedPool.Entry("A", 1))));
        UUID command = UUID.randomUUID();
        AtomicRunStore failing = new AtomicRunStore(folder.getRoot().toPath(), codec, id, boundary -> {
            if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("Lost acknowledgement"); }
        });
        assertThrows(IOException.class, () -> new DurableFatePointDebits(failing)
            .debit(id, command, 3, "test-only", "v1", 10));
        assertEquals(90, new DurableFatePointDebits(store()).debit(id, command, 3, "test-only", "v1", 10).balance());
        RunDocument result = store().load().get();
        assertEquals(4, result.lifecycle().revision());
        assertEquals(payload(source).get("inventory"), payload(result).get("inventory"));
    }
}

package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.MasterSelectionResolutions;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterResolutionAggregateTest
{
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    private final RunCodec codec = new RunCodec(gson);

    @Test public void retainsAndValidatesStructuralHistory()
    { MasterResolutionAggregateChecks.main(new String[0]); }

    @Test public void schema17RetainsExactTranscriptsAndExplicitAbsence() throws Exception
    {
        for (int count : new int[] {0, 1, 513})
        {
            RunDocument head = MasterResolutionAggregateChecks.resolved(count);
            byte[] bytes = codec.encode(head);
            RunDocument restored = codec.decode(bytes, head.lifecycle().identity());
            assertEquals(head.masterResolutions().get().receipts(), restored.masterResolutions().get().receipts());
            assertArrayEquals(bytes, codec.encode(restored));
        }
        RunDocument legacy = MasterCreationAggregateChecks.fixture(0);
        assertFalse(codec.decode(codec.encode(legacy), legacy.lifecycle().identity()).masterResolutions().isPresent());
        RunDocument empty = MasterResolutionAggregateChecks.copy(legacy, legacy.lifecycle(),
            legacy.pendingMasterSelection(), Optional.of(new MasterSelectionResolutions(
                legacy.lifecycle().identity(), 5, List.of())));
        assertTrue(codec.decode(codec.encode(empty), legacy.lifecycle().identity()).masterResolutions().get().receipts().isEmpty());
    }

    private byte[] mutate(RunDocument source, int schema, Consumer<JsonObject> change)
    {
        JsonObject envelope = new JsonParser().parse(new String(codec.encode(source), StandardCharsets.UTF_8)).getAsJsonObject();
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        change.accept(payload);
        String text = gson.toJson(payload);
        envelope.addProperty("schemaVersion", schema);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return gson.toJson(envelope).getBytes(StandardCharsets.UTF_8);
    }

    @Test public void schema15And16UpgradeWithoutInventingHistory() throws Exception
    {
        RunDocument source = MasterCreationAggregateChecks.fixture(1);
        for (int schema : new int[] {15, 16})
        {
            byte[] old = mutate(source, schema, p -> p.remove("masterResolutions"));
            byte[] unchanged = old.clone();
            RunDocument restored = codec.decode(old, source.lifecycle().identity());
            assertFalse(restored.masterResolutions().isPresent());
            assertEquals(source.masterCreations().get().receipts(), restored.masterCreations().get().receipts());
            assertFalse(codec.decode(codec.encode(restored), source.lifecycle().identity()).masterResolutions().isPresent());
            assertArrayEquals(unchanged, old);
        }
    }

    @Test public void malformedOrUnboundHistoryFailsWithoutChangingInput()
    {
        RunDocument source = MasterResolutionAggregateChecks.resolved(1);
        List<Consumer<JsonObject>> changes = List.of(
            p -> p.remove("masterResolutions"),
            p -> p.add("masterResolutions", new JsonArray()),
            p -> p.getAsJsonObject("masterResolutions").addProperty("version", 999),
            p -> p.getAsJsonObject("masterResolutions").addProperty("accountKey", "foreign"),
            p -> p.addProperty("revision", 5),
            p -> p.add("masterCreations", JsonNull.INSTANCE),
            p -> p.add("pendingMasterSelection", p.getAsJsonObject("masterResolutions")
                .getAsJsonArray("receipts").get(0).getAsJsonObject().getAsJsonObject("creation").get("initial")),
            p -> {
                JsonObject receipt = p.getAsJsonObject("masterResolutions").getAsJsonArray("receipts").get(0).getAsJsonObject();
                receipt.addProperty("commandId", receipt.getAsJsonObject("creation").getAsJsonObject("initial").get("transactionId").getAsString());
            });
        for (Consumer<JsonObject> change : changes)
        {
            byte[] bytes = mutate(source, 17, change);
            byte[] unchanged = bytes.clone();
            assertThrows(IOException.class, () -> codec.decode(bytes, source.lifecycle().identity()));
            assertArrayEquals(unchanged, bytes);
        }
        assertThrows(IOException.class, () -> codec.decode(mutate(source, 16, p -> {}), source.lifecycle().identity()));
    }
}

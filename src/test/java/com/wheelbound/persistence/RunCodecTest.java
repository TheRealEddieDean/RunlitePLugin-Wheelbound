package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class RunCodecTest
{
    private final Gson gson = new Gson(); // Test fixture; production uses RuneLite's injected instance.
    private final RunCodec codec = new RunCodec(gson);
    private final RunIdentity id = new RunIdentity("opaque-account:profile", UUID.randomUUID());

    private byte[] bytes(JsonObject value) { return gson.toJson(value).getBytes(StandardCharsets.UTF_8); }
    private JsonObject envelope()
    {
        return new JsonParser().parse(new String(codec.encode(new RunDocument(
            new RunLifecycle(id, 12, RunLifecycle.State.PAUSED, RunLifecycle.Obligation.SETTLEMENT_PENDING), "catalog-v1")),
            StandardCharsets.UTF_8)).getAsJsonObject();
    }

    private byte[] alteredPayload(String key, JsonElement value)
    {
        JsonObject envelope = envelope();
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        if (value == null) { payload.remove(key); } else { payload.add(key, value); }
        // Preserve unrelated required nulls so each corruption tests its intended field.
        String text = gson.newBuilder().serializeNulls().create().toJson(payload);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return bytes(envelope);
    }

    @Test public void everyLifecycleAndObligationRoundTrips() throws Exception
    {
        for (RunLifecycle.State state : RunLifecycle.State.values())
        for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
        {
            RunDocument result = codec.decode(codec.encode(new RunDocument(new RunLifecycle(id,
                Long.MAX_VALUE, state, obligation), "catalog-v1")), id);
            assertEquals(id, result.lifecycle().identity());
            assertEquals(Long.MAX_VALUE, result.lifecycle().revision());
            assertEquals(state, result.lifecycle().state());
            assertEquals(obligation, result.lifecycle().obligation());
            assertEquals("catalog-v1", result.catalogVersion());
        }
    }

    @Test public void checksumMismatchAndMalformedDataFailWithoutMutatingBytes()
    {
        JsonObject envelope = envelope(); envelope.addProperty("payload", "{}"); byte[] original = bytes(envelope);
        byte[] copy = original.clone();
        assertThrows(IOException.class, () -> codec.decode(original, id));
        assertArrayEquals(copy, original);
        for (String invalid : new String[]{"", "null", "[]", "{", "{}"})
        { assertThrows(IOException.class, () -> codec.decode(invalid.getBytes(StandardCharsets.UTF_8), id)); }
    }

    @Test public void futureSchemasAndUnknownFieldsAreNotSilentlyAccepted()
    {
        JsonObject envelope = envelope(); envelope.addProperty("schemaVersion", 999);
        assertThrows(IOException.class, () -> codec.decode(bytes(envelope), id));
        assertThrows(IOException.class, () -> codec.decode(alteredPayload("futureData", new JsonPrimitive("retained")), id));
        assertThrows(IOException.class, () -> codec.decode(alteredPayload("state", null), id));
    }

    @Test public void invalidDomainValuesAndNumericCoercionsFail()
    {
        for (JsonElement revision : new JsonElement[]{new JsonPrimitive(-1), new JsonPrimitive("12"),
            new JsonPrimitive(1.5), new JsonPrimitive("9223372036854775808"), JsonNull.INSTANCE})
        { assertThrows(IOException.class, () -> codec.decode(alteredPayload("revision", revision), id)); }
        assertThrows(IOException.class, () -> codec.decode(alteredPayload("state", new JsonPrimitive("FUTURE")), id));
        assertThrows(IOException.class, () -> codec.decode(alteredPayload("catalogVersion", new JsonPrimitive("")), id));
        assertThrows(IOException.class, () -> codec.decode(alteredPayload("runId", new JsonPrimitive("not-a-uuid")), id));
    }

    @Test public void foreignProfileOrRunCannotLoad()
    {
        assertThrows(IOException.class, () -> codec.decode(bytes(envelope()), new RunIdentity("different", id.runId())));
        assertThrows(IOException.class, () -> codec.decode(bytes(envelope()), new RunIdentity(id.accountKey(), UUID.randomUUID())));
    }

    @Test public void oversizedFilesAreRejectedBeforeParsing()
    { assertThrows(IOException.class, () -> codec.decode(new byte[RunCodec.MAX_BYTES + 1], id)); }

    @Test public void ledgerRoundTripsDebtAndOrderedHistory() throws Exception
    {
        FatePointLedger ledger = new FatePointLedger(-5, java.util.List.of(
            new FatePointLedger.Entry(FatePointLedger.Kind.EARNING, 20),
            new FatePointLedger.Entry(FatePointLedger.Kind.DISCRETIONARY_COST, 15),
            new FatePointLedger.Entry(FatePointLedger.Kind.PENALTY, 7)));
        RunDocument source = new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), "v1", java.util.List.of(), java.util.Optional.of(ledger));
        FatePointLedger restored = codec.decode(codec.encode(source), id).fatePoints().get();
        assertEquals(-5, restored.startingBalance());
        assertEquals(-7, restored.balance());
        assertEquals(3, restored.entries().size());
        assertEquals(FatePointLedger.Kind.DISCRETIONARY_COST, restored.entries().get(1).kind());
        assertEquals(15, restored.entries().get(1).amount());
    }

    @Test public void unavailableLedgerIsExplicitNullWithoutChangingInjectedGson() throws Exception
    {
        JsonObject envelope = envelope();
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        assertTrue(payload.has("fatePoints"));
        assertTrue(payload.get("fatePoints").isJsonNull());
        assertFalse(codec.decode(bytes(envelope), id).fatePoints().isPresent());
        JsonObject probe = new JsonObject();
        probe.add("absent", JsonNull.INSTANCE);
        assertEquals("{}", gson.toJson(probe));
    }

    @Test public void legacySchemasPreserveAbsentFpWithoutInventingZero() throws Exception
    {
        for (int schema = 1; schema <= 3; schema++)
        {
            JsonObject envelope = envelope();
            JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
            payload.remove("fatePoints"); payload.remove("inventory"); payload.remove("grandFate"); payload.remove("grandProgress"); payload.remove("grandJournal"); payload.remove("grandActivations"); payload.remove("grandCurrentAttempt"); payload.remove("pendingMasterSelection"); payload.remove("masterCreations");
            if (schema == 1) { payload.remove("receipts"); }
            String text = gson.toJson(payload);
            envelope.addProperty("schemaVersion", schema);
            envelope.addProperty("payload", text);
            envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
            RunDocument restored = codec.decode(bytes(envelope), id);
            assertFalse(restored.fatePoints().isPresent());
            assertFalse(codec.decode(codec.encode(restored), id).fatePoints().isPresent());
        }
    }

    @Test public void malformedLedgerAndInvalidIntermediateSpendingFailClosed()
    {
        for (String json : new String[]{
            "{\"startingBalance\":0,\"entries\":[{\"kind\":\"DISCRETIONARY_COST\",\"amount\":1},{\"kind\":\"EARNING\",\"amount\":2}]}",
            "{\"startingBalance\":9223372036854775807,\"entries\":[{\"kind\":\"EARNING\",\"amount\":1}]}",
            "{\"startingBalance\":0,\"entries\":[{\"kind\":\"EARNING\",\"amount\":\"1\"}]}",
            "{\"startingBalance\":0,\"entries\":[],\"balance\":99}"})
        { assertThrows(IOException.class, () -> codec.decode(alteredPayload("fatePoints", new JsonParser().parse(json)), id)); }
        assertThrows(IOException.class, () -> codec.decode(alteredPayload("fatePoints", null), id));
    }

    @Test public void schemaThreeDrawRemainsReadableAfterSchemaUpgrade() throws Exception
    {
        DrawCommitment draw = new DrawCommitment(new WeightedPool("pool-v1",
            java.util.List.of(new WeightedPool.Entry("chosen", 1))), 0);
        RunReceipt receipt = new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.DRAW, 0, 1,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.OFFERS_COMMITTED, draw);
        RunDocument document = new RunDocument(new RunLifecycle(id, 1, receipt.state(), receipt.obligation()),
            "v1", java.util.List.of(receipt));
        JsonObject envelope = new JsonParser().parse(new String(codec.encode(document), StandardCharsets.UTF_8)).getAsJsonObject();
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        payload.remove("fatePoints"); payload.remove("inventory"); payload.remove("grandFate"); payload.remove("grandProgress"); payload.remove("grandJournal"); payload.remove("grandActivations"); payload.remove("grandCurrentAttempt"); payload.remove("pendingMasterSelection"); payload.remove("masterCreations");
        String text = gson.toJson(payload);
        envelope.addProperty("schemaVersion", 3);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        RunDocument restored = codec.decode(bytes(envelope), id);
        assertEquals(0, restored.receipts().get(0).draw().ticket());
        assertEquals("pool-v1", restored.receipts().get(0).draw().pool().version());
        assertFalse(restored.fatePoints().isPresent());
    }
}

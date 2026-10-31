package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PurchaseJournalTest
{
    private final Gson gson = new Gson().newBuilder().serializeNulls().create();
    private final RunCodec codec = new RunCodec(gson);
    private final RunIdentity identity = new RunIdentity("test-account", UUID.randomUUID());

    @Test public void validatesEnvelopeAndHistoryEquality()
    { PurchaseJournalChecks.main(new String[0]); }

    @Test public void validatesPurchaseAggregateEvidence()
    { PurchaseAggregateChecks.main(new String[0]); }

    private RunDocument document()
    {
        SlicePurchaseProposal first = SlicePurchaseProposal.propose(
            new RunLifecycle(identity, 0, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
            new FatePointLedger(100, List.of()), PurchaseAggregateChecks.inventory(), identity,
            UUID.randomUUID(), 0, UUID.randomUUID(), new SlicePurchaseProposal.Quote(
                "smithing", "synthetic", "v1", 7, SliceInventory.PurchaseKind.FIRST_INSTANCE, 0));
        SlicePurchaseProposal second = SlicePurchaseProposal.propose(first.lifecycle(), first.ledger(),
            first.inventory(), identity, UUID.randomUUID(), 1, UUID.randomUUID(),
            new SlicePurchaseProposal.Quote("smithing", "synthetic", "v1", 9,
                SliceInventory.PurchaseKind.PAID_DUPLICATE, 1));
        return new RunDocument(second.lifecycle(), "synthetic", List.of(envelope(first), envelope(second)),
            Optional.of(second.ledger()), Optional.of(second.inventory()));
    }

    private RunReceipt envelope(SlicePurchaseProposal proposal)
    {
        FatePointDebits.Receipt debit = proposal.debit();
        return new RunReceipt(debit.commandId(), RunReceipt.Operation.SLICE_PURCHASE,
            debit.beforeRevision(), debit.afterRevision(), RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE, null, null, proposal.receipt());
    }

    private JsonObject json(byte[] bytes)
    { return new JsonParser().parse(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject(); }

    private byte[] bytes(JsonObject value)
    { return gson.toJson(value).getBytes(StandardCharsets.UTF_8); }

    private byte[] tamper(Consumer<JsonObject> change)
    {
        JsonObject envelope = json(codec.encode(document()));
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        change.accept(payload);
        String text = gson.toJson(payload);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return bytes(envelope);
    }

    private JsonObject purchase(JsonObject payload)
    { return payload.getAsJsonArray("receipts").get(0).getAsJsonObject().getAsJsonObject("purchase"); }

    @Test public void schemaSevenRestoresChainAndOriginalResponses() throws Exception
    {
        RunDocument source = document();
        byte[] encoded = codec.encode(source);
        assertEquals(RunCodec.SCHEMA, json(encoded).get("schemaVersion").getAsInt());
        RunDocument restored = codec.decode(encoded, identity);
        assertEquals(source.receipts(), restored.receipts());
        assertEquals(93, restored.receipts().get(0).purchase().debit().balance());
        assertEquals(84, restored.fatePoints().get().balance());
        assertArrayEquals(encoded, codec.encode(restored));
    }

    @Test public void schemaSixRemainsReadableButCannotContainPurchases() throws Exception
    {
        RunDocument legacy = new RunDocument(new RunLifecycle(identity, 0,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), "synthetic", List.of(),
            Optional.of(new FatePointLedger(100, List.of())), Optional.of(PurchaseAggregateChecks.inventory()));
        JsonObject old = json(codec.encode(legacy));
        old.addProperty("schemaVersion", 6);
        JsonObject legacyPayload = json(old.get("payload").getAsString().getBytes(StandardCharsets.UTF_8));
        legacyPayload.remove("grandFate"); legacyPayload.remove("grandProgress"); legacyPayload.remove("grandJournal"); legacyPayload.remove("grandActivations");
        legacyPayload.remove("grandCurrentAttempt"); legacyPayload.remove("pendingMasterSelection"); legacyPayload.remove("masterCreations"); legacyPayload.remove("masterResolutions"); legacyPayload.remove("masterSpins");
        String legacyText = gson.toJson(legacyPayload);
        old.addProperty("payload", legacyText);
        old.addProperty("sha256", RunCodec.sha256(legacyText.getBytes(StandardCharsets.UTF_8)));
        RunDocument restored = codec.decode(bytes(old), identity);
        assertTrue(restored.inventory().isPresent());
        assertEquals(100, restored.fatePoints().get().balance());
        assertEquals(RunCodec.SCHEMA, json(codec.encode(restored)).get("schemaVersion").getAsInt());
        JsonObject disguised = json(codec.encode(document()));
        disguised.addProperty("schemaVersion", 6);
        assertThrows(IOException.class, () -> codec.decode(bytes(disguised), identity));
    }

    @Test public void checksumValidTamperingFailsAggregateAndEnvelopeValidation()
    {
        for (String key : new String[]{"balance", "amount", "duplicateOrdinal", "beforeRevision", "afterRevision"})
        {
            byte[] invalid = tamper(payload -> purchase(payload).addProperty(key, 42));
            byte[] copy = invalid.clone();
            assertThrows(IOException.class, () -> codec.decode(invalid, identity));
            assertArrayEquals(copy, invalid);
        }
        assertThrows(IOException.class, () -> codec.decode(tamper(p -> purchase(p).addProperty("accountKey", "foreign")), identity));
        assertThrows(IOException.class, () -> codec.decode(tamper(p -> purchase(p).addProperty("instanceId", UUID.randomUUID().toString())), identity));
        assertThrows(IOException.class, () -> codec.decode(tamper(p -> purchase(p).addProperty("activity", "mining")), identity));
        assertThrows(IOException.class, () -> codec.decode(tamper(p -> purchase(p).addProperty("amount", "7")), identity));
        assertThrows(IOException.class, () -> codec.decode(tamper(p -> purchase(p).addProperty("commandId", UUID.randomUUID().toString())), identity));
        assertThrows(IOException.class, () -> codec.decode(tamper(p -> p.getAsJsonArray("receipts").get(1)
            .getAsJsonObject().getAsJsonObject("purchase").addProperty("instanceId", purchase(p).get("instanceId").getAsString())), identity));
        assertThrows(IOException.class, () -> codec.decode(tamper(p -> {
            for (JsonElement entry : p.getAsJsonObject("inventory").getAsJsonArray("history"))
            {
                JsonObject history = entry.getAsJsonObject();
                if ("smithing".equals(history.get("activity").getAsString()))
                { history.addProperty("paidDuplicates", 2); }
            }
        }), identity));
        assertThrows(IOException.class, () -> codec.decode(tamper(p -> p.add("inventory", JsonNull.INSTANCE)), identity));
        assertThrows(IOException.class, () -> codec.decode(tamper(p -> p.add("fatePoints", JsonNull.INSTANCE)), identity));
        assertThrows(IOException.class, () -> codec.decode(tamper(p -> p.getAsJsonObject("fatePoints").getAsJsonArray("entries").remove(1)), identity));
        for (String key : PurchaseReceiptJson.encode(document().receipts().get(0).purchase()).keySet())
        { assertThrows(IOException.class, () -> codec.decode(tamper(p -> purchase(p).remove(key)), identity)); }
        assertThrows(IOException.class, () -> codec.decode(tamper(p -> purchase(p).addProperty("unknown", true)), identity));
        assertThrows(IOException.class, () -> codec.decode(tamper(p -> p.getAsJsonArray("receipts").get(0).getAsJsonObject().addProperty("unknown", true)), identity));
    }
}

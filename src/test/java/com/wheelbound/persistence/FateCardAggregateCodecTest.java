package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Reader-first fixtures; fixture prices confer no quote authority. */
public class FateCardAggregateCodecTest
{
    // Schema fixtures must retain explicit nulls for unavailable legacy state.
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    private final RunCodec codec = new RunCodec(gson);
    private final RunIdentity id = new RunIdentity("card-migration", new UUID(201, 1));

    @Test public void restoresAllPaidTypesAndOriginalReplayAfterPause() throws Exception
    {
        for (var pool : FateCardOwnership.Pool.values())
        for (var type : List.of(FateCardOwnership.Type.LESSER, FateCardOwnership.Type.GREATER))
        {
            var seed = new RunLifecycle(id, 7, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
            var ledger = new FatePointLedger(Long.MAX_VALUE, List.of());
            var cards = FateCardOwnership.starting(id);
            var quote = new FateCardPurchaseProposal.Quote(pool, type, "fixture", "v1", 10);
            var command = new UUID(201, 2);
            var proposal = FateCardPurchaseProposal.propose(seed, ledger, cards, id, command, 7, quote);
            var pause = new RunReceipt(new UUID(201, 3), RunReceipt.Operation.PAUSE, 8, 9,
                RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
            var base = new RunDocument(new RunLifecycle(id, 9, RunLifecycle.State.PAUSED,
                RunLifecycle.Obligation.NONE), "fixture", List.of(pause), Optional.of(proposal.ledger()));
            var payload = payload(base);
            payload.add("fateCards", FateCardOwnershipCodec.encode(proposal.ownership()));
            payload.add("cardPurchases", FateCardPurchaseJournalCodec.encode(
                new FateCardPurchaseJournal(id, 9, List.of(proposal.receipt()))));
            var restored = codec.decode(envelope(payload, 21), id);
            assertEquals(proposal.ownership(), restored.fateCards().get());
            assertEquals(Long.MAX_VALUE - 10, restored.fatePoints().get().balance());
            var replay = FateCardPurchaseAdmission.prepare(restored, seed, ledger, cards, id, command, 7, quote);
            assertTrue(replay.replay());
            assertSame(restored, replay.proposed());
            assertEquals(8, replay.receipt().debit().afterRevision());
            assertEquals(9, restored.lifecycle().revision());
            assertFalse(restored.inventory().isPresent());
            assertThrows(IllegalArgumentException.class, () -> codec.encode(restored));
            assertThrows(IllegalArgumentException.class, () -> codec.encodeRetryCandidate(restored));
            // A checksummed but inconsistent aggregate must still fail domain admission.
            payload.getAsJsonObject("fatePoints").addProperty("startingBalance", Long.MAX_VALUE - 1);
            assertThrows(IOException.class, () -> codec.decode(envelope(payload, 21), id));
        }
    }

    @Test public void preservesLegacyAbsenceAndDistinguishesExplicitOwnership() throws Exception
    {
        var base = base();
        assertEquals(19, new JsonParser().parse(new String(codec.encode(base), StandardCharsets.UTF_8))
            .getAsJsonObject().get("schemaVersion").getAsInt());
        assertEquals(20, new JsonParser().parse(new String(codec.encodeRetryCandidate(base), StandardCharsets.UTF_8))
            .getAsJsonObject().get("schemaVersion").getAsInt());
        assertFalse(codec.decode(codec.encode(base), id).fateCards().isPresent());
        var payload = payload(base);
        var absent = codec.decode(envelope(payload, 21), id);
        assertFalse(absent.fateCards().isPresent());
        assertFalse(absent.cardPurchases().isPresent());
        payload.add("fateCards", FateCardOwnershipCodec.encode(FateCardOwnership.starting(id)));
        assertFalse(codec.decode(envelope(payload, 21), id).cardPurchases().isPresent());
        payload.add("cardPurchases", FateCardPurchaseJournalCodec.encode(new FateCardPurchaseJournal(id, 0, List.of())));
        assertTrue(codec.decode(envelope(payload, 21), id).cardPurchases().isPresent());
        assertThrows(IOException.class, () -> codec.decode(envelope(payload, 20), id));
        assertThrows(IOException.class, () -> codec.decode(envelope(payload, 22), id));
    }

    @Test public void refusesMissingForeignMalformedAndOrphanedDeclarations()
    {
        rejects(p -> p.remove("fateCards"));
        rejects(p -> p.remove("cardPurchases"));
        rejects(p -> p.addProperty("fateCards", true));
        rejects(p -> p.add("cardPurchases", new JsonArray()));
        rejects(p -> p.add("cardPurchases", FateCardPurchaseJournalCodec.encode(new FateCardPurchaseJournal(id, 0, List.of()))));
        rejects(p -> p.add("fateCards", FateCardOwnershipCodec.encode(FateCardOwnership.starting(
            new RunIdentity("foreign", id.runId())))));
        rejects(p -> p.addProperty("future", true));
    }

    private RunDocument base()
    { return new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), "fixture"); }

    private JsonObject payload(RunDocument base)
    {
        var envelope = new JsonParser().parse(new String(codec.encode(base), StandardCharsets.UTF_8)).getAsJsonObject();
        var payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        payload.add("fateCards", JsonNull.INSTANCE);
        payload.add("cardPurchases", JsonNull.INSTANCE);
        return payload;
    }

    private byte[] envelope(JsonObject payload, int schema)
    {
        String text = gson.toJson(payload);
        var envelope = new JsonObject();
        envelope.addProperty("schemaVersion", schema);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return gson.toJson(envelope).getBytes(StandardCharsets.UTF_8);
    }

    private void rejects(Consumer<JsonObject> mutation)
    {
        var payload = payload(base());
        mutation.accept(payload);
        assertThrows(IOException.class, () -> codec.decode(envelope(payload, 21), id));
    }
}

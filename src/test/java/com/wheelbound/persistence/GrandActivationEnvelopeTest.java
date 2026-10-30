package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class GrandActivationEnvelopeTest
{
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    private final RunCodec codec = new RunCodec(gson);

    @Test public void validatesOriginalPayloadHistoryAndOperationChain()
    { GrandActivationEnvelopeChecks.main(new String[0]); }

    private RunDocument fixture(int ticket, GrandFateChecklist.Objective objective, long before,
        boolean current, boolean paused)
    {
        RunIdentity id = new RunIdentity("codec-activation", UUID.randomUUID());
        SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
        GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
        GrandActivationReceipt activation = new GrandActivationReceipt(UUID.randomUUID(), id, before, checklist, objective);
        List<RunReceipt> receipts = new ArrayList<>();
        receipts.add(RunReceipt.grandActivation(activation));
        long head = activation.afterRevision();
        if (paused)
        {
            receipts.add(new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.PAUSE, head, head + 1,
                RunLifecycle.State.PAUSED, RunLifecycle.Obligation.ACTIVE_OBJECTIVE));
            head++;
        }
        RunLifecycle lifecycle = new RunLifecycle(id, head, paused ? RunLifecycle.State.PAUSED
            : RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        GrandActivationHistory history = new GrandActivationHistory(seal, head, List.of(activation));
        return new RunDocument(lifecycle, "test", receipts, Optional.empty(), Optional.empty(), Optional.of(seal),
            Optional.empty(), Optional.empty(), Optional.of(history), current
                ? Optional.of(GrandCurrentAttempt.restore(activation.commandId(), history, lifecycle, checklist))
                : Optional.empty());
    }

    private byte[] edited(byte[] bytes, int schema, Consumer<JsonObject> edit)
    {
        JsonObject envelope = new JsonParser().parse(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        edit.accept(payload);
        String text = gson.toJson(payload);
        envelope.addProperty("schemaVersion", schema);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return gson.toJson(envelope).getBytes(StandardCharsets.UTF_8);
    }

    private JsonObject operation(JsonObject payload)
    { return payload.getAsJsonArray("receipts").get(0).getAsJsonObject(); }

    @Test public void everyGoalObjectiveAndExplicitActivityRoundTrips() throws Exception
    {
        for (int ticket = 0; ticket < 5; ticket++)
        {
            SealedGrandFate seal = new SealedGrandFate(new RunIdentity("objectives", UUID.randomUUID()),
                UUID.randomUUID(), GrandFatePool.atTicket(ticket));
            for (GrandFateChecklist.Objective objective : GrandFateChecklist.initial(seal).required())
            for (boolean current : new boolean[] {false, true})
            for (boolean paused : new boolean[] {false, true})
            for (long before : new long[] {0, Long.MAX_VALUE - (paused ? 2 : 1)})
            {
                RunDocument source = fixture(ticket, objective, before, current, paused);
                byte[] bytes = codec.encode(source);
                RunDocument restored = codec.decode(bytes, source.lifecycle().identity());
                assertEquals(source.receipts(), restored.receipts());
                assertEquals(source.grandActivations().get().receipts(), restored.grandActivations().get().receipts());
                assertEquals(current, restored.grandCurrentAttempt().isPresent());
                assertArrayEquals(bytes, codec.encode(restored));
            }
        }
    }

    @Test public void checksumValidCorruptionAndLegacyActivationCannotBeAccepted()
    {
        GrandFateChecklist.Objective objective = GrandFateChecklist.initial(new SealedGrandFate(
            new RunIdentity("objective", UUID.randomUUID()), UUID.randomUUID(), GrandFatePool.atTicket(0)))
            .unfinished().iterator().next();
        RunDocument source = fixture(0, objective, 0, true, false);
        byte[] bytes = codec.encode(source);
        List<Consumer<JsonObject>> edits = List.of(
            p -> operation(p).remove("activation"),
            p -> operation(p).add("activation", JsonNull.INSTANCE),
            p -> operation(p).addProperty("commandId", UUID.randomUUID().toString()),
            p -> operation(p).addProperty("beforeRevision", 1),
            p -> operation(p).addProperty("afterRevision", 2),
            p -> operation(p).addProperty("state", "PAUSED"),
            p -> operation(p).addProperty("obligation", "NONE"),
            p -> operation(p).addProperty("operation", "RESUME"),
            p -> operation(p).add("draw", new JsonObject()),
            p -> operation(p).getAsJsonObject("activation").addProperty("version", 2),
            p -> operation(p).getAsJsonObject("activation").addProperty("commandId", UUID.randomUUID().toString()),
            p -> operation(p).getAsJsonObject("activation").addProperty("sealCommandId", UUID.randomUUID().toString()),
            p -> p.add("grandFate", JsonNull.INSTANCE),
            p -> p.add("grandActivations", JsonNull.INSTANCE));
        for (Consumer<JsonObject> edit : edits)
        {
            byte[] corrupt = edited(bytes, 13, edit);
            byte[] original = corrupt.clone();
            assertThrows(IOException.class, () -> codec.decode(corrupt, source.lifecycle().identity()));
            assertArrayEquals(original, corrupt);
        }
        assertThrows(IOException.class, () -> codec.decode(edited(bytes, 12, p -> { }), source.lifecycle().identity()));
    }

    @Test public void schema12HistoryRemainsExplicitlyInactiveAndUpgradesWithoutEnvelopeInference() throws Exception
    {
        RunIdentity id = new RunIdentity("legacy", UUID.randomUUID());
        SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(0));
        GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
        GrandActivationReceipt activation = new GrandActivationReceipt(UUID.randomUUID(), id, 0, checklist,
            checklist.unfinished().iterator().next());
        RunDocument source = new RunDocument(activation.attempt().lifecycle(), "test", List.of(),
            Optional.empty(), Optional.empty(), Optional.of(seal), Optional.empty(), Optional.empty(),
            Optional.of(new GrandActivationHistory(seal, 1, List.of(activation))));
        RunDocument legacy = codec.decode(edited(codec.encode(source), 12, p -> { }), id);
        RunDocument upgraded = codec.decode(codec.encode(legacy), id);
        assertTrue(upgraded.receipts().isEmpty());
        assertFalse(upgraded.grandCurrentAttempt().isPresent());
        assertEquals(activation, upgraded.grandActivations().get().receipts().get(activation.commandId()));
    }
}

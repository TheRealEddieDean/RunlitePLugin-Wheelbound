package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterSpinCodecTest
{
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    private final RunCodec codec = new RunCodec(gson);

    private RunDocument reserved(int empties, long balance)
    {
        RunDocument head = MasterResolutionAggregateChecks.resolved(empties);
        var receipt = head.masterResolutions().get().receipts().values().iterator().next();
        return MasterSpinAggregateChecks.attach(head, new MasterSpinState(1,
            head.lifecycle().identity(), receipt.afterRevision(), balance,
            Optional.of(MasterSpinAllocation.restore(receipt, balance))));
    }

    private byte[] mutate(RunDocument head, int schema, Consumer<JsonObject> change)
    {
        JsonObject envelope = new JsonParser().parse(new String(codec.encode(head), StandardCharsets.UTF_8)).getAsJsonObject();
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        change.accept(payload);
        String text = gson.toJson(payload);
        envelope.addProperty("schemaVersion", schema);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return gson.toJson(envelope).getBytes(StandardCharsets.UTF_8);
    }

    @Test public void reservationsRoundTripWithoutAnotherAllocation() throws Exception
    {
        for (int empties : new int[] {0, 1, 513})
        for (long balance : new long[] {1, 7, Long.MAX_VALUE})
        {
            RunDocument head = reserved(empties, balance);
            byte[] bytes = codec.encode(head);
            var restored = codec.decode(bytes, head.lifecycle().identity());
            var spins = restored.masterSpins().get();
            var receipt = head.masterSpins().get().reservation().get().resolution();
            assertEquals(balance, spins.allotted());
            assertEquals(balance - 1, spins.available());
            assertEquals(receipt, spins.replay(head.lifecycle().identity(), head.lifecycle().revision(), receipt).resolution());
            assertArrayEquals(bytes, codec.encode(restored));
            var paused = LifecycleAdmission.prepare(restored, head.lifecycle().identity(),
                java.util.UUID.randomUUID(), head.lifecycle().revision(), RunReceipt.Operation.PAUSE);
            assertEquals(balance - 1, codec.decode(codec.encode(paused), head.lifecycle().identity()).masterSpins().get().available());
        }
    }

    @Test public void absenceAndExplicitUnreservedBalancesStayDistinct() throws Exception
    {
        RunDocument head = MasterResolutionAggregateChecks.resolved(0);
        assertFalse(codec.decode(codec.encode(head), head.lifecycle().identity()).masterSpins().isPresent());
        for (long balance : new long[] {0, 1, Long.MAX_VALUE})
        {
            var explicit = MasterSpinAggregateChecks.attach(head, new MasterSpinState(1,
                head.lifecycle().identity(), head.lifecycle().revision(), balance, Optional.empty()));
            var restored = codec.decode(codec.encode(explicit), head.lifecycle().identity()).masterSpins().get();
            assertEquals(balance, restored.available());
            assertFalse(restored.reservation().isPresent());
        }
    }

    @Test public void schema17MigratesWithoutInferringSpinsAndRejectsNewField() throws Exception
    {
        RunDocument head = MasterResolutionAggregateChecks.resolved(1);
        byte[] old = mutate(head, 17, p -> p.remove("masterSpins"));
        byte[] original = old.clone();
        var restored = codec.decode(old, head.lifecycle().identity());
        assertFalse(restored.masterSpins().isPresent());
        assertEquals(head.masterResolutions().get().receipts(), restored.masterResolutions().get().receipts());
        assertFalse(codec.decode(codec.encode(restored), head.lifecycle().identity()).masterSpins().isPresent());
        assertArrayEquals(original, old);
        assertThrows(IOException.class, () -> codec.decode(mutate(head, 17, p -> {}), head.lifecycle().identity()));
    }

    @Test public void malformedOrUnboundReservationsFailWithoutChangingBytes()
    {
        RunDocument head = reserved(1, 7);
        List<Consumer<JsonObject>> changes = List.of(
            p -> p.remove("masterSpins"),
            p -> p.add("masterSpins", new JsonArray()),
            p -> p.getAsJsonObject("masterSpins").addProperty("version", 999),
            p -> p.getAsJsonObject("masterSpins").addProperty("accountKey", "foreign"),
            p -> p.getAsJsonObject("masterSpins").addProperty("revision", 7),
            p -> p.getAsJsonObject("masterSpins").addProperty("allotted", 8),
            p -> p.getAsJsonObject("masterSpins").addProperty("allotted", "7"),
            p -> p.add("masterResolutions", JsonNull.INSTANCE),
            p -> p.getAsJsonObject("masterSpins").getAsJsonObject("reservation").addProperty("spinsBefore", 6),
            p -> p.getAsJsonObject("masterSpins").getAsJsonObject("reservation")
                .getAsJsonObject("resolution").addProperty("commandId", java.util.UUID.randomUUID().toString()));
        for (Consumer<JsonObject> change : changes)
        {
            byte[] bytes = mutate(head, RunCodec.SCHEMA, change);
            byte[] original = bytes.clone();
            assertThrows(IOException.class, () -> codec.decode(bytes, head.lifecycle().identity()));
            assertArrayEquals(original, bytes);
        }
    }
}

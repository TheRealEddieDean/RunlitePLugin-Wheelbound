package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.util.*;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterCurrentSpecialObligationJsonTest
{
    private final RunIdentity identity = new RunIdentity("account/profile", new UUID(4, 5));

    private MasterCurrentSpecialObligation fixture(ActiveWheel.Kind kind, int empties, long balance)
    {
        var receipt = new MasterSelectionResolutionReceipt(new UUID(8, 9), 3,
            new MasterSelectionCreationReceipt(identity, 0,
                MasterSelectionProvenanceJsonTest.fixture(kind, 0, false)),
            MasterSelectionProvenanceJsonTest.fixture(kind, empties, true));
        var spins = new MasterSpinState(1, identity, 4, balance,
            Optional.of(MasterSpinAllocation.restore(receipt, balance)));
        var stage = kind == ActiveWheel.Kind.TAINT ? RunLifecycle.Obligation.PUNISHMENT_PENDING
            : RunLifecycle.Obligation.SACRIFICE_ACQUISITION;
        return MasterCurrentSpecialObligation.restore(new MasterSpecialObligation(receipt), history(receipt),
            spins, new RunLifecycle(identity, 4, RunLifecycle.State.ACTIVE, stage));
    }

    private MasterSelectionResolutions history(MasterSelectionResolutionReceipt receipt)
    { return new MasterSelectionResolutions(identity, 4, List.of(receipt)); }

    private JsonObject encode(MasterCurrentSpecialObligation special)
    {
        return MasterCurrentSpecialObligationJson.encode(special, history(special.obligation().resolution()),
            special.spins(), special.lifecycle());
    }

    private MasterCurrentSpecialObligation decode(JsonObject json, MasterCurrentSpecialObligation special)
    {
        return MasterCurrentSpecialObligationJson.decode(json, history(special.obligation().resolution()),
            special.spins(), special.lifecycle());
    }

    @Test public void explicitRoundTripPreservesReservationAcrossPauseResume()
    {
        for (var kind : new ActiveWheel.Kind[] {ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE})
        for (int empties : new int[] {0, 1, 513})
        for (long balance : new long[] {1, 7, Long.MAX_VALUE})
        {
            var active = fixture(kind, empties, balance);
            var paused = active.pause(identity, 4);
            var resumed = paused.resume(identity, 5);
            for (var special : List.of(active, paused, resumed))
            {
                String text = encode(special).toString();
                var input = new JsonParser().parse(text).getAsJsonObject();
                var restored = decode(input, special);
                assertEquals(text, input.toString());
                assertEquals(text, encode(restored).toString());
                assertEquals(special.obligation(), restored.obligation());
                assertSame(special.spins(), restored.spins());
                assertSame(special.lifecycle(), restored.lifecycle());
                assertEquals(balance - 1, restored.spins().available());
                assertEquals(balance, restored.spins().reservation().orElseThrow().spinsBefore());
            }
            assertThrows(IllegalArgumentException.class, () -> MasterCurrentSpecialObligationJson.encode(
                active, history(active.obligation().resolution()), active.spins(), paused.lifecycle()));
        }
    }

    private void rejects(Consumer<JsonObject> corrupt)
    {
        var special = fixture(ActiveWheel.Kind.TAINT, 1, 7);
        var input = encode(special);
        corrupt.accept(input);
        String text = input.toString();
        assertThrows(RuntimeException.class, () -> decode(input, special));
        assertEquals(text, input.toString());
    }

    @Test public void malformedAndUnboundContextFailsClosed()
    {
        rejects(j -> j.addProperty("version", 2));
        rejects(j -> j.addProperty("version", 4294967297L));
        rejects(j -> j.addProperty("version", "1"));
        rejects(j -> j.remove("version"));
        rejects(j -> j.addProperty("extra", true));
        rejects(j -> j.remove("resolution"));
        rejects(j -> j.add("resolution", JsonNull.INSTANCE));
        rejects(j -> j.add("resolution", new JsonArray()));
        rejects(j -> j.getAsJsonObject("resolution").addProperty("commandId", UUID.randomUUID().toString()));
        rejects(j -> j.getAsJsonObject("resolution").getAsJsonObject("creation").addProperty("accountKey", "foreign"));
        var special = fixture(ActiveWheel.Kind.TAINT, 1, 7);
        var input = encode(special);
        assertThrows(IllegalArgumentException.class, () -> MasterCurrentSpecialObligationJson.decode(input,
            new MasterSelectionResolutions(identity, 4, List.of()), special.spins(), special.lifecycle()));
        assertThrows(IllegalArgumentException.class, () -> MasterCurrentSpecialObligationJson.decode(input,
            history(special.obligation().resolution()), new MasterSpinState(1, identity, 4, 7, Optional.empty()),
            special.lifecycle()));
        assertThrows(IllegalArgumentException.class, () -> MasterCurrentSpecialObligationJson.decode(input,
            history(special.obligation().resolution()), special.spins(),
            new RunLifecycle(identity, 4, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE)));
        rejects(j -> {
            var ordinary = new MasterSelectionResolutionReceipt(new UUID(8, 9), 3,
                new MasterSelectionCreationReceipt(identity, 0,
                    MasterSelectionProvenanceJsonTest.fixture(null, 0, false)),
                MasterSelectionProvenanceJsonTest.fixture(null, 1, true));
            j.add("resolution", MasterSelectionResolutionReceiptJson.encode(ordinary));
        });
    }
}

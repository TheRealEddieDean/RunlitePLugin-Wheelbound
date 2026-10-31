package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wheelbound.domain.*;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterSpinStateJsonTest
{
    private final RunIdentity identity = new RunIdentity("account/profile", new UUID(4, 5));

    private MasterSpinState fixture(ActiveWheel.Kind kind, int empties, long balance)
    {
        MasterSelectionResolutionReceipt receipt = new MasterSelectionResolutionReceipt(new UUID(8, 9), 3,
            new MasterSelectionCreationReceipt(identity, 0,
                MasterSelectionProvenanceJsonTest.fixture(kind, 0, false)),
            MasterSelectionProvenanceJsonTest.fixture(kind, empties, true));
        return new MasterSpinState(1, identity, 4, balance,
            Optional.of(MasterSpinAllocation.restore(receipt, balance)));
    }

    @Test public void reservedTextRoundTripPreservesOriginalAccountingAndReplay()
    {
        for (ActiveWheel.Kind kind : new ActiveWheel.Kind[] {null, ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE})
        for (int empties : new int[] {0, 1, 513})
        for (long balance : new long[] {1, 7, Long.MAX_VALUE})
        {
            MasterSpinState original = fixture(kind, empties, balance);
            String text = MasterSpinStateJson.encode(original, 8).toString();
            JsonObject input = new JsonParser().parse(text).getAsJsonObject();
            MasterSpinState restored = MasterSpinStateJson.decode(input, identity, 8);
            assertEquals(text, MasterSpinStateJson.encode(restored, 8).toString());
            assertEquals(balance, restored.allotted());
            assertEquals(balance - 1, restored.available());
            MasterSpinAllocation allocation = restored.replay(identity, 8, original.reservation().get().resolution());
            assertEquals(original.reservation().get().resolution(), allocation.resolution());
            assertEquals(balance, allocation.spinsBefore());
            assertEquals(balance - 1, allocation.spinsAfter());
            assertEquals(0, allocation.fpDelta());
            input.addProperty("allotted", 0);
            assertEquals(text, MasterSpinStateJson.encode(restored, 8).toString());
        }
    }

    @Test public void explicitUnreservedZeroAndBoundaryValuesRemainExplicit()
    {
        for (long balance : new long[] {0, 1, Long.MAX_VALUE})
        {
            MasterSpinState original = new MasterSpinState(1, identity, Long.MAX_VALUE, balance, Optional.empty());
            JsonObject json = MasterSpinStateJson.encode(original, Long.MAX_VALUE);
            assertTrue(json.get("reservation").isJsonNull());
            MasterSpinState restored = MasterSpinStateJson.decode(json, identity, Long.MAX_VALUE);
            assertFalse(restored.reservation().isPresent());
            assertEquals(balance, restored.available());
            assertEquals(Long.MAX_VALUE, restored.revision());
        }
    }

    private void rejects(Consumer<JsonObject> corrupt)
    {
        JsonObject input = MasterSpinStateJson.encode(fixture(null, 1, 7), 8);
        corrupt.accept(input);
        String before = input.toString();
        assertThrows(RuntimeException.class, () -> MasterSpinStateJson.decode(input, identity, 8));
        assertEquals(before, input.toString());
    }

    @Test public void malformedForeignFutureAndInconsistentPayloadsFailClosed()
    {
        rejects(j -> j.addProperty("version", 2));
        rejects(j -> j.addProperty("version", 4294967297L));
        rejects(j -> j.addProperty("version", "1"));
        rejects(j -> j.addProperty("allotted", 1.5));
        rejects(j -> j.addProperty("allotted", -1));
        rejects(j -> j.addProperty("allotted", 6));
        rejects(j -> j.addProperty("revision", 9));
        rejects(j -> j.addProperty("revision", 3));
        rejects(j -> j.addProperty("accountKey", "foreign"));
        rejects(j -> j.addProperty("runId", new UUID(8, 8).toString()));
        rejects(j -> j.addProperty("extra", true));
        rejects(j -> j.remove("reservation"));
        rejects(j -> j.add("reservation", new JsonArray()));
        rejects(j -> j.getAsJsonObject("reservation").addProperty("version", 2));
        rejects(j -> j.getAsJsonObject("reservation").addProperty("spinsBefore", 0));
        rejects(j -> j.getAsJsonObject("reservation").addProperty("spinsBefore", "7"));
        rejects(j -> j.getAsJsonObject("reservation").add("resolution", JsonNull.INSTANCE));
        rejects(j -> j.getAsJsonObject("reservation").remove("resolution"));
        rejects(j -> j.getAsJsonObject("reservation").addProperty("extra", true));
        rejects(j -> j.getAsJsonObject("reservation").getAsJsonObject("resolution")
            .getAsJsonObject("creation").addProperty("accountKey", "foreign"));
        MasterSpinState state = fixture(null, 0, 1);
        assertThrows(IllegalArgumentException.class, () -> MasterSpinStateJson.encode(state, 3));
        assertThrows(IllegalArgumentException.class, () -> MasterSpinStateJson.decode(
            MasterSpinStateJson.encode(state, 4), identity, 3));
    }
}

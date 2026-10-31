package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wheelbound.domain.*;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterTaintTerminalResponseJsonTest
{
    private final RunIdentity identity = new RunIdentity("account/profile", new UUID(4, 5));

    private MasterTerminalOperationProposal fixture(ActiveWheel.Kind kind, int empties, long balance)
    {
        return MasterTerminalOperationProposal.restore(new MasterSelectionResolutionReceipt(new UUID(8, 9), 3,
            new MasterSelectionCreationReceipt(identity, 0,
                MasterSelectionProvenanceJsonTest.fixture(kind, 0, false)),
            MasterSelectionProvenanceJsonTest.fixture(kind, empties, true)), balance);
    }

    @Test public void textRoundTripRetainsExactTypedResponseAndOriginalAccounting()
    {
        for (int empties : new int[] {0, 1, 513})
        for (long balance : new long[] {1, 7, Long.MAX_VALUE})
        {
            var original = fixture(ActiveWheel.Kind.TAINT, empties, balance);
            String text = MasterTaintTerminalResponseJson.encode(original).toString();
            JsonObject input = new JsonParser().parse(text).getAsJsonObject();
            var restored = MasterTaintTerminalResponseJson.decode(input, identity);
            assertEquals(text, input.toString());
            assertEquals(text, MasterTaintTerminalResponseJson.encode(restored).toString());
            assertEquals(original, restored);
            assertEquals(original.hashCode(), restored.hashCode());
            assertEquals(RunReceipt.masterTaintTerminal(original), RunReceipt.masterTaintTerminal(restored));
            assertEquals(balance, restored.allocation().spinsBefore());
            assertEquals(balance - 1, restored.allocation().spinsAfter());
            assertEquals(0, restored.allocation().fpDelta());
            assertEquals(original.resolution(), restored.allocation().replay(identity, 8, original.resolution()).resolution());
            long changedBalance = balance == Long.MAX_VALUE ? balance - 1 : balance + 1;
            input.addProperty("spinsBefore", changedBalance);
            var changed = MasterTaintTerminalResponseJson.decode(input, identity);
            assertNotEquals(restored, changed);
            assertNotEquals(RunReceipt.masterTaintTerminal(restored), RunReceipt.masterTaintTerminal(changed));
            input.addProperty("spinsBefore", 42);
            assertEquals(balance, restored.allocation().spinsBefore());
        }
    }

    private void rejects(Consumer<JsonObject> corrupt)
    {
        JsonObject input = MasterTaintTerminalResponseJson.encode(fixture(ActiveWheel.Kind.TAINT, 1, 7));
        corrupt.accept(input);
        String before = input.toString();
        assertThrows(RuntimeException.class, () -> MasterTaintTerminalResponseJson.decode(input, identity));
        assertEquals(before, input.toString());
    }

    @Test public void malformedForeignFutureAndUnsupportedPayloadsFailClosed()
    {
        rejects(j -> j.addProperty("version", 2));
        rejects(j -> j.addProperty("version", 4294967297L));
        rejects(j -> j.addProperty("version", "1"));
        rejects(j -> j.remove("version"));
        rejects(j -> j.addProperty("extra", true));
        rejects(j -> j.remove("spinsBefore"));
        rejects(j -> j.addProperty("spinsBefore", 0));
        rejects(j -> j.addProperty("spinsBefore", -1));
        rejects(j -> j.addProperty("spinsBefore", 1.5));
        rejects(j -> j.addProperty("spinsBefore", "7"));
        rejects(j -> j.add("spinsBefore", JsonNull.INSTANCE));
        rejects(j -> j.remove("resolution"));
        rejects(j -> j.add("resolution", JsonNull.INSTANCE));
        rejects(j -> j.add("resolution", new JsonArray()));
        rejects(j -> j.getAsJsonObject("resolution").addProperty("version", 2));
        rejects(j -> j.getAsJsonObject("resolution").addProperty("afterRevision", 5));
        rejects(j -> j.getAsJsonObject("resolution").getAsJsonObject("creation").addProperty("accountKey", "foreign"));
        rejects(j -> j.getAsJsonObject("resolution").getAsJsonObject("creation")
            .addProperty("runId", new UUID(8, 8).toString()));
        for (ActiveWheel.Kind kind : new ActiveWheel.Kind[] {null, ActiveWheel.Kind.SACRIFICE})
        {
            var unsupported = fixture(kind, 1, 7);
            assertThrows(IllegalArgumentException.class, () -> MasterTaintTerminalResponseJson.encode(unsupported));
            rejects(j -> j.add("resolution", MasterSelectionResolutionReceiptJson.encode(unsupported.resolution())));
        }
    }
}

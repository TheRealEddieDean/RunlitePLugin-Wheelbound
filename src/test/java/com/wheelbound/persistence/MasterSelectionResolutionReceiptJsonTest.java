package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wheelbound.domain.ActiveWheel;
import com.wheelbound.domain.MasterSelectionCreationReceipt;
import com.wheelbound.domain.MasterSelectionProvenance;
import com.wheelbound.domain.MasterSelectionResolutionReceipt;
import com.wheelbound.domain.RunIdentity;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterSelectionResolutionReceiptJsonTest
{
    private final RunIdentity identity = new RunIdentity("account/profile", new UUID(4, 5));

    private MasterSelectionResolutionReceipt receipt(ActiveWheel.Kind special, int emptyCount, long revision)
    {
        MasterSelectionProvenance initial = MasterSelectionProvenanceJsonTest.fixture(special, 0, false);
        return new MasterSelectionResolutionReceipt(new UUID(8, 9), revision,
            new MasterSelectionCreationReceipt(identity, 0, initial),
            MasterSelectionProvenanceJsonTest.fixture(special, emptyCount, true));
    }

    @Test public void textRoundTripPreservesFullTranscriptAndRevisionBoundary()
    {
        for (ActiveWheel.Kind special : new ActiveWheel.Kind[] {null, ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE})
        for (int count : new int[] {0, 1, 513})
        for (long revision : new long[] {1, Long.MAX_VALUE - 1})
        {
            MasterSelectionResolutionReceipt original = receipt(special, count, revision);
            String text = MasterSelectionResolutionReceiptJson.encode(original).toString();
            JsonObject input = new JsonParser().parse(text).getAsJsonObject();
            MasterSelectionResolutionReceipt restored = MasterSelectionResolutionReceiptJson.decode(input, identity);
            assertEquals(original, restored);
            assertEquals(original.hashCode(), restored.hashCode());
            assertEquals(text, MasterSelectionResolutionReceiptJson.encode(restored).toString());
            input.getAsJsonObject("resolved").addProperty("initialStreamState", "changed input");
            assertEquals(text, MasterSelectionResolutionReceiptJson.encode(restored).toString());
        }
    }

    private void rejects(Consumer<JsonObject> corrupt)
    {
        JsonObject input = MasterSelectionResolutionReceiptJson.encode(receipt(null, 1, 3));
        corrupt.accept(input);
        String before = input.toString();
        assertThrows(RuntimeException.class, () -> MasterSelectionResolutionReceiptJson.decode(input, identity));
        assertEquals(before, input.toString());
    }

    @Test public void malformedForeignNonterminalAndChangedOriginalPayloadsFailClosed()
    {
        rejects(j -> j.addProperty("version", 2));
        rejects(j -> j.addProperty("version", 1.5));
        rejects(j -> j.addProperty("version", "1"));
        rejects(j -> j.addProperty("extra", true));
        rejects(j -> j.remove("creation"));
        rejects(j -> j.add("creation", JsonNull.INSTANCE));
        rejects(j -> j.add("resolved", new JsonArray()));
        rejects(j -> j.addProperty("commandId", "invalid"));
        rejects(j -> j.addProperty("commandId", new UUID(0, 99).toString()));
        rejects(j -> j.addProperty("beforeRevision", 0));
        rejects(j -> j.addProperty("beforeRevision", Long.MAX_VALUE));
        rejects(j -> j.addProperty("afterRevision", 9));
        rejects(j -> j.addProperty("afterRevision", "4"));
        rejects(j -> j.getAsJsonObject("creation").addProperty("accountKey", "foreign"));
        rejects(j -> j.getAsJsonObject("creation").addProperty("runId", new UUID(6, 7).toString()));
        rejects(j -> j.getAsJsonObject("creation").addProperty("afterRevision", 4));
        rejects(j -> j.getAsJsonObject("resolved").addProperty("snapshotVersion", "changed"));
        rejects(j -> j.getAsJsonObject("resolved").addProperty("initialStreamState", "changed"));
        rejects(j -> j.getAsJsonObject("resolved").getAsJsonArray("steps").remove(1));
        rejects(j -> j.add("resolved", MasterSelectionProvenanceJson.encode(
            MasterSelectionProvenanceJsonTest.fixture(null, 0, false))));
        JsonObject input = MasterSelectionResolutionReceiptJson.encode(receipt(null, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> MasterSelectionResolutionReceiptJson.decode(input,
            new RunIdentity("foreign", identity.runId())));
    }
}

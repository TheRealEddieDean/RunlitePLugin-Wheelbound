package com.wheelbound.persistence;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wheelbound.domain.ActiveWheel;
import com.wheelbound.domain.MasterSelectionCreationReceipt;
import com.wheelbound.domain.RunIdentity;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterSelectionCreationReceiptJsonTest
{
    private final RunIdentity identity = new RunIdentity("account/profile", new UUID(4, 5));

    private MasterSelectionCreationReceipt fixture(ActiveWheel.Kind special, long revision)
    {
        return new MasterSelectionCreationReceipt(identity, revision,
            MasterSelectionProvenanceJsonTest.fixture(special, 0, false));
    }

    @Test public void textRoundTripRetainsExactOriginalRequestAndRevisionBoundaries()
    {
        for (ActiveWheel.Kind special : new ActiveWheel.Kind[] {null, ActiveWheel.Kind.TAINT,
            ActiveWheel.Kind.SACRIFICE})
        for (long revision : new long[] {0, 17, Long.MAX_VALUE - 1})
        {
            MasterSelectionCreationReceipt original = fixture(special, revision);
            String text = MasterSelectionCreationReceiptJson.encode(original).toString();
            JsonObject input = new JsonParser().parse(text).getAsJsonObject();
            MasterSelectionCreationReceipt restored = MasterSelectionCreationReceiptJson.decode(input, identity);
            assertEquals(original, restored);
            assertEquals(original.hashCode(), restored.hashCode());
            assertEquals(text, MasterSelectionCreationReceiptJson.encode(restored).toString());
            input.getAsJsonObject("initial").addProperty("initialStreamState", "mutated");
            assertEquals(text, MasterSelectionCreationReceiptJson.encode(restored).toString());
            assertEquals(0, restored.initial().drawCount());
        }
    }

    private void rejects(Consumer<JsonObject> corrupt)
    {
        JsonObject input = MasterSelectionCreationReceiptJson.encode(fixture(null, 17));
        corrupt.accept(input);
        String before = input.toString();
        assertThrows(RuntimeException.class, () -> MasterSelectionCreationReceiptJson.decode(input, identity));
        assertEquals(before, input.toString());
    }

    @Test public void refusesForeignMalformedAndAdvancedOriginalRequests()
    {
        rejects(j -> j.addProperty("version", 2));
        rejects(j -> j.addProperty("version", 1.5));
        rejects(j -> j.addProperty("extra", true));
        rejects(j -> j.remove("initial"));
        rejects(j -> j.add("initial", JsonNull.INSTANCE));
        rejects(j -> j.addProperty("initial", "invalid"));
        rejects(j -> j.addProperty("accountKey", "foreign"));
        rejects(j -> j.addProperty("runId", new UUID(5, 6).toString()));
        rejects(j -> j.addProperty("runId", "invalid"));
        rejects(j -> j.addProperty("beforeRevision", -1));
        rejects(j -> j.addProperty("beforeRevision", Long.MAX_VALUE));
        rejects(j -> j.addProperty("beforeRevision", 17.5));
        rejects(j -> j.addProperty("afterRevision", 19));
        rejects(j -> j.addProperty("afterRevision", "18"));
        rejects(j -> j.add("initial", MasterSelectionProvenanceJson.encode(
            MasterSelectionProvenanceJsonTest.fixture(null, 1, false))));
        rejects(j -> j.add("initial", MasterSelectionProvenanceJson.encode(
            MasterSelectionProvenanceJsonTest.fixture(null, 0, true))));
        rejects(j -> j.getAsJsonObject("initial").addProperty("version", 2));
    }
}

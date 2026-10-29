package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import static org.junit.Assert.*;

public class DebitJournalTest
{
    @Test public void validatesMixedJournalAndRejectsCorruption()
    { DebitJournalChecks.main(new String[0]); }

    @Test public void mixedJournalRoundTripsAndOldSchemasCannotContainDebits() throws Exception
    {
        RunDocument source = DebitJournalChecks.mixed();
        RunCodec codec = new RunCodec(new Gson());
        byte[] encoded = codec.encode(source);
        RunDocument restored = codec.decode(encoded, source.lifecycle().identity());
        assertEquals(source.receipts(), restored.receipts());
        assertEquals(source.receipts().hashCode(), restored.receipts().hashCode());
        assertEquals(13, restored.fatePoints().get().balance());
        JsonObject envelope = new JsonParser().parse(new String(encoded, StandardCharsets.UTF_8)).getAsJsonObject();
        envelope.addProperty("schemaVersion", 4);
        assertThrows(IOException.class, () -> codec.decode(new Gson().toJson(envelope)
            .getBytes(StandardCharsets.UTF_8), source.lifecycle().identity()));
    }

    @Test public void schemaFourWithoutDebitsStillLoads() throws Exception
    {
        RunDocument mixed = DebitJournalChecks.mixed();
        RunDocument source = new RunDocument(mixed.lifecycle(), "legacy", java.util.List.of(), mixed.fatePoints());
        Gson gson = new Gson();
        RunCodec codec = new RunCodec(gson);
        JsonObject envelope = new JsonParser().parse(new String(codec.encode(source), StandardCharsets.UTF_8)).getAsJsonObject();
        envelope.addProperty("schemaVersion", 4);
        RunDocument restored = codec.decode(gson.toJson(envelope).getBytes(StandardCharsets.UTF_8), source.lifecycle().identity());
        assertEquals(13, restored.fatePoints().get().balance());
        assertTrue(restored.receipts().isEmpty());
    }
}

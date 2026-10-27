package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class RunCodecTest
{
    private final Gson gson = new Gson(); // Test fixture; production uses RuneLite's injected instance.
    private final RunCodec codec = new RunCodec(gson);
    private final RunIdentity id = new RunIdentity("opaque-account:profile", UUID.randomUUID());

    private byte[] bytes(JsonObject value) { return gson.toJson(value).getBytes(StandardCharsets.UTF_8); }
    private JsonObject envelope()
    {
        return JsonParser.parseString(new String(codec.encode(new RunDocument(
            new RunLifecycle(id, 12, RunLifecycle.State.PAUSED, RunLifecycle.Obligation.SETTLEMENT_PENDING), "catalog-v1")),
            StandardCharsets.UTF_8)).getAsJsonObject();
    }

    private byte[] alteredPayload(String key, JsonElement value)
    {
        JsonObject envelope = envelope();
        JsonObject payload = JsonParser.parseString(envelope.get("payload").getAsString()).getAsJsonObject();
        if (value == null) { payload.remove(key); } else { payload.add(key, value); }
        String text = gson.toJson(payload);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return bytes(envelope);
    }

    @Test public void everyLifecycleAndObligationRoundTrips() throws Exception
    {
        for (RunLifecycle.State state : RunLifecycle.State.values())
        for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
        {
            RunDocument result = codec.decode(codec.encode(new RunDocument(new RunLifecycle(id,
                Long.MAX_VALUE, state, obligation), "catalog-v1")), id);
            assertEquals(id, result.lifecycle().identity());
            assertEquals(Long.MAX_VALUE, result.lifecycle().revision());
            assertEquals(state, result.lifecycle().state());
            assertEquals(obligation, result.lifecycle().obligation());
            assertEquals("catalog-v1", result.catalogVersion());
        }
    }

    @Test public void checksumMismatchAndMalformedDataFailWithoutMutatingBytes()
    {
        JsonObject envelope = envelope(); envelope.addProperty("payload", "{}"); byte[] original = bytes(envelope);
        byte[] copy = original.clone();
        assertThrows(IOException.class, () -> codec.decode(original, id));
        assertArrayEquals(copy, original);
        for (String invalid : new String[]{"", "null", "[]", "{", "{}"})
        { assertThrows(IOException.class, () -> codec.decode(invalid.getBytes(StandardCharsets.UTF_8), id)); }
    }

    @Test public void futureSchemasAndUnknownFieldsAreNotSilentlyAccepted()
    {
        JsonObject envelope = envelope(); envelope.addProperty("schemaVersion", 2);
        assertThrows(IOException.class, () -> codec.decode(bytes(envelope), id));
        assertThrows(IOException.class, () -> codec.decode(alteredPayload("futureData", new JsonPrimitive("retained")), id));
        assertThrows(IOException.class, () -> codec.decode(alteredPayload("state", null), id));
    }

    @Test public void invalidDomainValuesAndNumericCoercionsFail()
    {
        for (JsonElement revision : new JsonElement[]{new JsonPrimitive(-1), new JsonPrimitive("12"),
            new JsonPrimitive(1.5), new JsonPrimitive("9223372036854775808"), JsonNull.INSTANCE})
        { assertThrows(IOException.class, () -> codec.decode(alteredPayload("revision", revision), id)); }
        assertThrows(IOException.class, () -> codec.decode(alteredPayload("state", new JsonPrimitive("FUTURE")), id));
        assertThrows(IOException.class, () -> codec.decode(alteredPayload("catalogVersion", new JsonPrimitive("")), id));
        assertThrows(IOException.class, () -> codec.decode(alteredPayload("runId", new JsonPrimitive("not-a-uuid")), id));
    }

    @Test public void foreignProfileOrRunCannotLoad()
    {
        assertThrows(IOException.class, () -> codec.decode(bytes(envelope()), new RunIdentity("different", id.runId())));
        assertThrows(IOException.class, () -> codec.decode(bytes(envelope()), new RunIdentity(id.accountKey(), UUID.randomUUID())));
    }

    @Test public void oversizedFilesAreRejectedBeforeParsing()
    { assertThrows(IOException.class, () -> codec.decode(new byte[RunCodec.MAX_BYTES + 1], id)); }
}

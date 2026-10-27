package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.RunLifecycle;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** Inject RuneLite's Gson. Never deserialize directly into domain objects bypassing their validation. */
public final class RunCodec
{
    public static final int SCHEMA = 2;
    public static final int MAX_BYTES = 1_048_576;
    private final Gson gson;

    public RunCodec(Gson gson) { this.gson = Objects.requireNonNull(gson, "gson"); }

    public byte[] encode(RunDocument document)
    {
        RunLifecycle run = document.lifecycle();
        JsonObject payload = new JsonObject();
        payload.addProperty("accountKey", run.identity().accountKey());
        payload.addProperty("runId", run.identity().runId().toString());
        payload.addProperty("revision", run.revision());
        payload.addProperty("state", run.state().name());
        payload.addProperty("obligation", run.obligation().name());
        payload.addProperty("catalogVersion", document.catalogVersion());
        JsonArray receipts = new JsonArray();
        for (RunReceipt receipt : document.receipts())
        {
            JsonObject item = new JsonObject();
            item.addProperty("commandId", receipt.commandId().toString());
            item.addProperty("operation", receipt.operation().name());
            item.addProperty("beforeRevision", receipt.beforeRevision());
            item.addProperty("afterRevision", receipt.afterRevision());
            item.addProperty("state", receipt.state().name());
            item.addProperty("obligation", receipt.obligation().name());
            receipts.add(item);
        }
        payload.add("receipts", receipts);
        String text = gson.toJson(payload);
        JsonObject envelope = new JsonObject();
        envelope.addProperty("schemaVersion", SCHEMA);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", sha256(text.getBytes(StandardCharsets.UTF_8)));
        byte[] bytes = gson.toJson(envelope).getBytes(StandardCharsets.UTF_8);
        if (bytes.length > MAX_BYTES) { throw new IllegalArgumentException("Snapshot too large"); }
        return bytes;
    }

    /** Invalid/future/foreign bytes are never changed or silently reset by decoding. */
    public RunDocument decode(byte[] bytes, RunIdentity expected) throws IOException
    {
        Objects.requireNonNull(expected, "expected");
        if (bytes.length == 0 || bytes.length > MAX_BYTES) { throw new IOException("Invalid snapshot length"); }
        try
        {
            JsonObject envelope = new JsonParser().parse(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
            keys(envelope, Set.of("schemaVersion", "payload", "sha256"));
            long schema = integer(envelope, "schemaVersion");
            if (schema != 1 && schema != SCHEMA) { throw new IOException("Unsupported snapshot schema; preserve original"); }
            String text = string(envelope, "payload");
            if (!sha256(text.getBytes(StandardCharsets.UTF_8)).equals(string(envelope, "sha256")))
            { throw new IOException("Snapshot checksum mismatch"); }
            JsonObject payload = new JsonParser().parse(text).getAsJsonObject();
            keys(payload, schema == 1
                ? Set.of("accountKey", "runId", "revision", "state", "obligation", "catalogVersion")
                : Set.of("accountKey", "runId", "revision", "state", "obligation", "catalogVersion", "receipts"));
            RunIdentity identity = new RunIdentity(string(payload, "accountKey"), UUID.fromString(string(payload, "runId")));
            if (!identity.equals(expected)) { throw new IOException("Foreign account/profile/run snapshot"); }
            RunLifecycle lifecycle = new RunLifecycle(identity, integer(payload, "revision"),
                RunLifecycle.State.valueOf(string(payload, "state")),
                RunLifecycle.Obligation.valueOf(string(payload, "obligation")));
            List<RunReceipt> receipts = new ArrayList<>();
            if (schema == SCHEMA)
            {
                for (JsonElement element : payload.getAsJsonArray("receipts"))
                {
                    JsonObject item = element.getAsJsonObject();
                    keys(item, Set.of("commandId", "operation", "beforeRevision", "afterRevision", "state", "obligation"));
                    receipts.add(new RunReceipt(UUID.fromString(string(item, "commandId")),
                        RunReceipt.Operation.valueOf(string(item, "operation")), integer(item, "beforeRevision"),
                        integer(item, "afterRevision"), RunLifecycle.State.valueOf(string(item, "state")),
                        RunLifecycle.Obligation.valueOf(string(item, "obligation"))));
                }
            }
            return new RunDocument(lifecycle, string(payload, "catalogVersion"), receipts);
        }
        catch (RuntimeException ex) { throw new IOException("Invalid snapshot; preserve original", ex); }
    }

    static void keys(JsonObject object, Set<String> expected)
    {
        if (!object.keySet().equals(expected)) { throw new IllegalArgumentException("Missing or unknown fields"); }
    }

    static String string(JsonObject object, String key)
    {
        JsonElement value = object.get(key);
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString())
        { throw new IllegalArgumentException("Expected string: " + key); }
        return value.getAsString();
    }

    static long integer(JsonObject object, String key)
    {
        JsonElement value = object.get(key);
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isNumber())
        { throw new IllegalArgumentException("Expected integer: " + key); }
        return value.getAsBigDecimal().longValueExact();
    }

    public static String sha256(byte[] bytes)
    {
        try
        {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(bytes);
            StringBuilder result = new StringBuilder();
            for (byte value : hash) { result.append(String.format(java.util.Locale.ROOT, "%02x", value & 255)); }
            return result.toString();
        }
        catch (NoSuchAlgorithmException ex) { throw new IllegalStateException(ex); }
    }
}

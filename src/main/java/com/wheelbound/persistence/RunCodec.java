package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.wheelbound.domain.DrawCommitment;
import com.wheelbound.domain.WeightedPool;
import com.wheelbound.domain.FatePointLedger;
import java.util.Optional;
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
    public static final int SCHEMA = 17;
    public static final int MAX_BYTES = 1_048_576;
    private final Gson gson;

    public RunCodec(Gson gson)
    {
        // Schema 4 requires an explicit null for unavailable legacy FP state.
        // Derive a writer that preserves it without changing the injected Gson.
        this.gson = Objects.requireNonNull(gson, "gson").newBuilder().serializeNulls().create();
    }

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
            if (receipt.draw() != null) { item.add("draw", encodeDraw(receipt.draw())); }
            if (receipt.debit() != null)
            {
                JsonObject debit = new JsonObject();
                debit.addProperty("quoteKey", receipt.debit().quoteKey());
                debit.addProperty("quoteVersion", receipt.debit().quoteVersion());
                debit.addProperty("amount", receipt.debit().amount());
                debit.addProperty("balance", receipt.debit().balance());
                item.add("debit", debit);
            }
            if (receipt.purchase() != null) { item.add("purchase", PurchaseReceiptJson.encode(receipt.purchase())); }
            if (receipt.activation() != null)
            { item.add("activation", GrandActivationReceiptJson.encode(receipt.activation(), run.revision())); }
            if (receipt.masterCreation() != null)
            { item.add("masterCreation", MasterSelectionCreationReceiptJson.encode(receipt.masterCreation())); }
            receipts.add(item);
        }
        payload.add("receipts", receipts);
        // An explicit null preserves legacy absence without inventing starting FP.
        JsonObject fp = null;
        if (document.fatePoints().isPresent())
        {
            FatePointLedger ledger = document.fatePoints().get();
            fp = new JsonObject();
            fp.addProperty("startingBalance", ledger.startingBalance());
            JsonArray entries = new JsonArray();
            for (FatePointLedger.Entry entry : ledger.entries())
            {
                JsonObject item = new JsonObject();
                item.addProperty("kind", entry.kind().name());
                item.addProperty("amount", entry.amount());
                entries.add(item);
            }
            fp.add("entries", entries);
        }
        payload.add("fatePoints", fp == null ? com.google.gson.JsonNull.INSTANCE : fp);
        payload.add("inventory", document.inventory().isPresent()
            ? InventoryJson.encode(document.inventory().get()) : com.google.gson.JsonNull.INSTANCE);
        JsonObject grand = null;
        if (document.grandFate().isPresent())
        {
            com.wheelbound.domain.SealedGrandFate sealed = document.grandFate().get();
            grand = new JsonObject();
            grand.addProperty("commandId", sealed.commandId().toString());
            grand.add("draw", encodeDraw(sealed.commitment()));
        }
        payload.add("grandFate", grand == null ? com.google.gson.JsonNull.INSTANCE : grand);
        // References preserve provenance structure only; decoding cannot authenticate receipts.
        JsonArray progress = null;
        if (document.grandProgress().isPresent())
        {
            progress = new JsonArray();
            for (com.wheelbound.domain.GrandFateProgress.Evidence reference
                : document.grandProgress().get().evidence().values())
            {
                JsonObject item = new JsonObject();
                item.addProperty("objective", reference.objective().name());
                item.addProperty("receiptId", reference.receiptId().toString());
                item.addProperty("verifiedRevision", reference.verifiedRevision());
                progress.add(item);
            }
        }
        payload.add("grandProgress", progress == null ? com.google.gson.JsonNull.INSTANCE : progress);
        payload.add("grandJournal", document.grandJournal().isPresent()
            ? GrandEvidenceJournalJson.encode(document.grandJournal().get(), document.lifecycle().revision())
            : com.google.gson.JsonNull.INSTANCE);
        payload.add("grandActivations", document.grandActivations().isPresent()
            ? GrandActivationHistoryJson.encode(document.grandActivations().get(), document.lifecycle().revision())
            : com.google.gson.JsonNull.INSTANCE);
        payload.add("grandCurrentAttempt", document.grandCurrentAttempt().isPresent()
            ? GrandCurrentAttemptJson.encode(document.grandCurrentAttempt().get(),
                document.grandActivations().get(), document.lifecycle(),
                document.grandProgress().isPresent() ? document.grandProgress().get().checklist()
                    : com.wheelbound.domain.GrandFateChecklist.initial(document.grandFate().get()))
            : com.google.gson.JsonNull.INSTANCE);
        payload.add("pendingMasterSelection", document.pendingMasterSelection().isPresent()
            ? MasterSelectionProvenanceJson.encode(document.pendingMasterSelection().get())
            : com.google.gson.JsonNull.INSTANCE);
        payload.add("masterCreations", document.masterCreations().isPresent()
            ? MasterSelectionCreationJournalJson.encode(document.masterCreations().get(), run.revision())
            : com.google.gson.JsonNull.INSTANCE);
        payload.add("masterResolutions", document.masterResolutions().isPresent()
            ? MasterSelectionResolutionsJson.encode(document.masterResolutions().get(), run.revision())
            : com.google.gson.JsonNull.INSTANCE);
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
            if (schema < 1 || schema > SCHEMA) { throw new IOException("Unsupported snapshot schema; preserve original"); }
            String text = string(envelope, "payload");
            if (!sha256(text.getBytes(StandardCharsets.UTF_8)).equals(string(envelope, "sha256")))
            { throw new IOException("Snapshot checksum mismatch"); }
            JsonObject payload = new JsonParser().parse(text).getAsJsonObject();
            keys(payload, schema == 1
                ? Set.of("accountKey", "runId", "revision", "state", "obligation", "catalogVersion")
                : schema < 4
                    ? Set.of("accountKey", "runId", "revision", "state", "obligation", "catalogVersion", "receipts")
                    : schema < 6
                    ? Set.of("accountKey", "runId", "revision", "state", "obligation", "catalogVersion", "receipts", "fatePoints")
                    : schema < 8
                    ? Set.of("accountKey", "runId", "revision", "state", "obligation", "catalogVersion", "receipts", "fatePoints", "inventory")
                    : schema == 8
                    ? Set.of("accountKey", "runId", "revision", "state", "obligation", "catalogVersion", "receipts", "fatePoints", "inventory", "grandFate")
                    : schema == 9
                    ? Set.of("accountKey", "runId", "revision", "state", "obligation", "catalogVersion", "receipts", "fatePoints", "inventory", "grandFate", "grandProgress")
                    : schema == 10
                    ? Set.of("accountKey", "runId", "revision", "state", "obligation", "catalogVersion", "receipts", "fatePoints", "inventory", "grandFate", "grandProgress", "grandJournal")
                    : schema == 11
                    ? Set.of("accountKey", "runId", "revision", "state", "obligation", "catalogVersion", "receipts", "fatePoints", "inventory", "grandFate", "grandProgress", "grandJournal", "grandActivations")
                    : schema < 14
                    ? Set.of("accountKey", "runId", "revision", "state", "obligation", "catalogVersion", "receipts", "fatePoints", "inventory", "grandFate", "grandProgress", "grandJournal", "grandActivations", "grandCurrentAttempt")
                    : schema == 14
                    ? Set.of("accountKey", "runId", "revision", "state", "obligation", "catalogVersion", "receipts", "fatePoints", "inventory", "grandFate", "grandProgress", "grandJournal", "grandActivations", "grandCurrentAttempt", "pendingMasterSelection")
                    : schema < 17
                    ? Set.of("accountKey", "runId", "revision", "state", "obligation", "catalogVersion", "receipts", "fatePoints", "inventory", "grandFate", "grandProgress", "grandJournal", "grandActivations", "grandCurrentAttempt", "pendingMasterSelection", "masterCreations")
                    : Set.of("accountKey", "runId", "revision", "state", "obligation", "catalogVersion", "receipts", "fatePoints", "inventory", "grandFate", "grandProgress", "grandJournal", "grandActivations", "grandCurrentAttempt", "pendingMasterSelection", "masterCreations", "masterResolutions"));
            RunIdentity identity = new RunIdentity(string(payload, "accountKey"), UUID.fromString(string(payload, "runId")));
            if (!identity.equals(expected)) { throw new IOException("Foreign account/profile/run snapshot"); }
            RunLifecycle lifecycle = new RunLifecycle(identity, integer(payload, "revision"),
                RunLifecycle.State.valueOf(string(payload, "state")),
                RunLifecycle.Obligation.valueOf(string(payload, "obligation")));
            Optional<FatePointLedger> fatePoints = Optional.empty();
            if (schema >= 4 && !payload.get("fatePoints").isJsonNull())
            {
                JsonObject fp = payload.getAsJsonObject("fatePoints");
                keys(fp, Set.of("startingBalance", "entries"));
                List<FatePointLedger.Entry> entries = new ArrayList<>();
                for (JsonElement element : fp.getAsJsonArray("entries"))
                {
                    JsonObject item = element.getAsJsonObject();
                    keys(item, Set.of("kind", "amount"));
                    entries.add(new FatePointLedger.Entry(FatePointLedger.Kind.valueOf(string(item, "kind")),
                        integer(item, "amount")));
                }
                fatePoints = Optional.of(new FatePointLedger(integer(fp, "startingBalance"), entries));
            }
            Optional<com.wheelbound.domain.SealedGrandFate> grandFate = Optional.empty();
            if (schema >= 8 && !payload.get("grandFate").isJsonNull())
            {
                JsonObject grand = payload.getAsJsonObject("grandFate");
                keys(grand, Set.of("commandId", "draw"));
                grandFate = Optional.of(new com.wheelbound.domain.SealedGrandFate(identity,
                    UUID.fromString(string(grand, "commandId")), decodeDraw(grand.get("draw"))));
            }
            List<RunReceipt> receipts = new ArrayList<>();
            if (schema >= 2)
            {
                for (JsonElement element : payload.getAsJsonArray("receipts"))
                {
                    JsonObject item = element.getAsJsonObject();
                    boolean hasDraw = schema >= 3 && "DRAW".equals(string(item, "operation"));
                    boolean hasDebit = schema >= 5 && "FP_DEBIT".equals(string(item, "operation"));
                    boolean hasPurchase = schema >= 7 && "SLICE_PURCHASE".equals(string(item, "operation"));
                    boolean hasActivation = schema >= 13 && "GRAND_ACTIVATION".equals(string(item, "operation"));
                    boolean hasCreation = schema >= 16 && "MASTER_SELECTION_CREATION".equals(string(item, "operation"));
                    keys(item, hasCreation
                        ? Set.of("commandId", "operation", "beforeRevision", "afterRevision", "state", "obligation", "masterCreation")
                        : hasActivation
                        ? Set.of("commandId", "operation", "beforeRevision", "afterRevision", "state", "obligation", "activation")
                        : hasPurchase
                        ? Set.of("commandId", "operation", "beforeRevision", "afterRevision", "state", "obligation", "purchase")
                        : hasDebit
                        ? Set.of("commandId", "operation", "beforeRevision", "afterRevision", "state", "obligation", "debit")
                        : hasDraw
                        ? Set.of("commandId", "operation", "beforeRevision", "afterRevision", "state", "obligation", "draw")
                        : Set.of("commandId", "operation", "beforeRevision", "afterRevision", "state", "obligation"));
                    if (hasCreation)
                    {
                        RunReceipt receipt = RunReceipt.masterSelectionCreation(MasterSelectionCreationReceiptJson.decode(
                            item.getAsJsonObject("masterCreation"), identity));
                        if (!receipt.commandId().equals(UUID.fromString(string(item, "commandId")))
                            || receipt.beforeRevision() != integer(item, "beforeRevision")
                            || receipt.afterRevision() != integer(item, "afterRevision")
                            || !receipt.state().name().equals(string(item, "state"))
                            || !receipt.obligation().name().equals(string(item, "obligation")))
                        { throw new IllegalArgumentException("Creation envelope disagrees with original response"); }
                        receipts.add(receipt);
                        continue;
                    }
                    if (hasActivation)
                    {
                        RunReceipt receipt = RunReceipt.grandActivation(GrandActivationReceiptJson.decode(
                            item.getAsJsonObject("activation"), grandFate.orElseThrow(
                                () -> new IllegalArgumentException("Activation envelope requires seal")), lifecycle.revision()));
                        if (!receipt.commandId().equals(UUID.fromString(string(item, "commandId")))
                            || receipt.beforeRevision() != integer(item, "beforeRevision")
                            || receipt.afterRevision() != integer(item, "afterRevision")
                            || !receipt.state().name().equals(string(item, "state"))
                            || !receipt.obligation().name().equals(string(item, "obligation")))
                        { throw new IllegalArgumentException("Activation envelope disagrees with original response"); }
                        receipts.add(receipt);
                        continue;
                    }
                    com.wheelbound.domain.FatePointDebits.Receipt debit = null;
                    if (hasDebit)
                    {
                        JsonObject data = item.getAsJsonObject("debit");
                        keys(data, Set.of("quoteKey", "quoteVersion", "amount", "balance"));
                        debit = new com.wheelbound.domain.FatePointDebits.Receipt(
                            UUID.fromString(string(item, "commandId")), integer(item, "beforeRevision"),
                            integer(item, "afterRevision"), string(data, "quoteKey"), string(data, "quoteVersion"),
                            integer(data, "amount"), integer(data, "balance"));
                    }
                    receipts.add(new RunReceipt(UUID.fromString(string(item, "commandId")),
                        RunReceipt.Operation.valueOf(string(item, "operation")), integer(item, "beforeRevision"),
                        integer(item, "afterRevision"), RunLifecycle.State.valueOf(string(item, "state")),
                        RunLifecycle.Obligation.valueOf(string(item, "obligation")),
                        hasDraw ? decodeDraw(item.get("draw")) : null, debit,
                        hasPurchase ? PurchaseReceiptJson.decode(item.getAsJsonObject("purchase"), identity) : null));
                }
            }
            Optional<com.wheelbound.domain.GrandFateProgress> progress = Optional.empty();
            if (schema >= 9 && !payload.get("grandProgress").isJsonNull())
            {
                com.wheelbound.domain.SealedGrandFate seal = grandFate.orElseThrow(
                    () -> new IllegalArgumentException("Grand progress requires a seal"));
                List<com.wheelbound.domain.GrandFateProgress.Evidence> references = new ArrayList<>();
                for (JsonElement element : payload.getAsJsonArray("grandProgress"))
                {
                    JsonObject item = element.getAsJsonObject();
                    keys(item, Set.of("objective", "receiptId", "verifiedRevision"));
                    references.add(new com.wheelbound.domain.GrandFateProgress.Evidence(seal,
                        com.wheelbound.domain.GrandFateChecklist.Objective.valueOf(string(item, "objective")),
                        UUID.fromString(string(item, "receiptId")), integer(item, "verifiedRevision")));
                }
                progress = Optional.of(new com.wheelbound.domain.GrandFateProgress(
                    seal, lifecycle.revision(), references));
            }
            Optional<com.wheelbound.domain.GrandEvidenceJournal> journal = Optional.empty();
            if (schema >= 10 && !payload.get("grandJournal").isJsonNull())
            {
                journal = Optional.of(GrandEvidenceJournalJson.decode(payload.getAsJsonObject("grandJournal"),
                    progress.orElseThrow(() -> new IllegalArgumentException("Journal requires progress")), lifecycle.revision()));
            }
            Optional<com.wheelbound.domain.GrandActivationHistory> activations = Optional.empty();
            if (schema >= 11 && !payload.get("grandActivations").isJsonNull())
            {
                activations = Optional.of(GrandActivationHistoryJson.decode(payload.getAsJsonObject("grandActivations"),
                    grandFate.orElseThrow(() -> new IllegalArgumentException("Activation history requires seal")), lifecycle.revision()));
            }
            Optional<com.wheelbound.domain.GrandCurrentAttempt> currentAttempt = Optional.empty();
            if (schema >= 12 && !payload.get("grandCurrentAttempt").isJsonNull())
            {
                currentAttempt = Optional.of(GrandCurrentAttemptJson.decode(payload.getAsJsonObject("grandCurrentAttempt"),
                    activations.orElseThrow(() -> new IllegalArgumentException("Current attempt requires history")),
                    lifecycle, progress.isPresent() ? progress.get().checklist()
                        : com.wheelbound.domain.GrandFateChecklist.initial(grandFate.get())));
            }
            return new RunDocument(lifecycle, string(payload, "catalogVersion"), receipts, fatePoints,
                schema >= 6 && !payload.get("inventory").isJsonNull()
                    ? Optional.of(InventoryJson.decode(payload.getAsJsonObject("inventory"))) : Optional.empty(), grandFate, progress, journal, activations, currentAttempt,
                schema >= 14 && !payload.get("pendingMasterSelection").isJsonNull()
                    ? Optional.of(MasterSelectionProvenanceJson.decode(payload.getAsJsonObject("pendingMasterSelection")))
                    : Optional.empty(),
                schema >= 15 && !payload.get("masterCreations").isJsonNull()
                    ? Optional.of(MasterSelectionCreationJournalJson.decode(payload.getAsJsonObject("masterCreations"),
                        identity, lifecycle.revision())) : Optional.empty(),
                schema >= 17 && !payload.get("masterResolutions").isJsonNull()
                    ? Optional.of(MasterSelectionResolutionsJson.decode(payload.getAsJsonObject("masterResolutions"),
                        identity, lifecycle.revision())) : Optional.empty());
        }
        catch (RuntimeException ex) { throw new IOException("Invalid snapshot; preserve original", ex); }
    }

    static JsonElement encodeDraw(DrawCommitment draw)
    {
        JsonObject result = new JsonObject();
        result.addProperty("version", draw.pool().version());
        result.addProperty("ticket", draw.ticket());
        JsonArray entries = new JsonArray();
        for (WeightedPool.Entry entry : draw.pool().entries())
        {
            JsonObject item = new JsonObject(); item.addProperty("id", entry.id()); item.addProperty("weight", entry.weight());
            entries.add(item);
        }
        result.add("entries", entries);
        return result;
    }

    static DrawCommitment decodeDraw(JsonElement element)
    {
        JsonObject value = element.getAsJsonObject();
        keys(value, Set.of("version", "ticket", "entries"));
        List<WeightedPool.Entry> entries = new ArrayList<>();
        for (JsonElement member : value.getAsJsonArray("entries"))
        {
            JsonObject item = member.getAsJsonObject(); keys(item, Set.of("id", "weight"));
            entries.add(new WeightedPool.Entry(string(item, "id"), Math.toIntExact(integer(item, "weight"))));
        }
        return new DrawCommitment(new WeightedPool(string(value, "version"), entries), Math.toIntExact(integer(value, "ticket")));
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

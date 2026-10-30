package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GrandProgressAggregateTest
{
    private final Gson gson = new Gson().newBuilder().serializeNulls().create();
    private final RunCodec codec = new RunCodec(gson);
    private final RunIdentity id = new RunIdentity("progress-codec", UUID.randomUUID());

    @Test public void validatesSealRevisionAndOperationNamespaces()
    { GrandProgressAggregateChecks.main(new String[0]); }

    private RunDocument document(int ticket, boolean present, boolean populated)
    {
        SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
        List<GrandFateProgress.Evidence> references = new ArrayList<>();
        if (populated)
        {
            for (GrandFateChecklist.Objective objective : GrandFateChecklist.initial(seal).required())
            { references.add(new GrandFateProgress.Evidence(seal, objective, UUID.randomUUID(), Long.MAX_VALUE)); }
        }
        return new RunDocument(new RunLifecycle(id, Long.MAX_VALUE, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), "test", List.of(), Optional.empty(), Optional.empty(),
            Optional.of(seal), present ? Optional.of(new GrandFateProgress(seal, Long.MAX_VALUE, references)) : Optional.empty());
    }

    private byte[] edited(RunDocument document, int schema, java.util.function.Consumer<JsonObject> edit)
    {
        JsonObject envelope = new JsonParser().parse(new String(codec.encode(document), StandardCharsets.UTF_8)).getAsJsonObject();
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        edit.accept(payload);
        String text = gson.toJson(payload);
        envelope.addProperty("schemaVersion", schema);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        return gson.toJson(envelope).getBytes(StandardCharsets.UTF_8);
    }

    @Test public void allGoalsAndExplicitAbsenceRoundTrip() throws Exception
    {
        for (int ticket = 0; ticket < 5; ticket++)
        for (boolean present : new boolean[]{false, true})
        for (boolean populated : new boolean[]{false, true})
        {
            RunDocument before = document(ticket, present, populated);
            RunDocument after = codec.decode(codec.encode(before), id);
            assertEquals(before.grandFate(), after.grandFate());
            assertEquals(present, after.grandProgress().isPresent());
            if (present) { assertEquals(before.grandProgress().get().evidence(), after.grandProgress().get().evidence()); }
            assertArrayEquals(codec.encode(before), codec.encode(after));
        }
    }

    @Test public void schemaEightRestoresAbsenceAndRequiresExplicitMigration() throws Exception
    {
        RunDocument document = document(0, true, true);
        assertFalse(codec.decode(edited(document, 8, p -> p.remove("grandProgress")), id).grandProgress().isPresent());
        assertThrows(IOException.class, () -> codec.decode(edited(document, 8, p -> {}), id));
    }

    @Test public void checksumValidInvalidReferencesFailClosed()
    {
        RunDocument document = document(0, true, true);
        List<java.util.function.Consumer<JsonObject>> corruptions = List.of(
            p -> p.remove("grandProgress"),
            p -> p.add("grandFate", JsonNull.INSTANCE),
            p -> p.addProperty("grandProgress", "bad"),
            p -> p.getAsJsonArray("grandProgress").add(p.getAsJsonArray("grandProgress").get(0).deepCopy()),
            p -> p.getAsJsonArray("grandProgress").get(0).getAsJsonObject().addProperty("objective", "UNKNOWN"),
            p -> p.getAsJsonArray("grandProgress").get(0).getAsJsonObject().addProperty("verifiedRevision", 0),
            p -> p.getAsJsonArray("grandProgress").get(0).getAsJsonObject().addProperty("receiptId", "invalid"),
            p -> p.getAsJsonArray("grandProgress").get(0).getAsJsonObject().addProperty("extra", true),
            p -> p.addProperty("revision", 1));
        for (java.util.function.Consumer<JsonObject> corruption : corruptions)
        {
            byte[] bytes = edited(document, 9, corruption);
            byte[] original = bytes.clone();
            assertThrows(IOException.class, () -> codec.decode(bytes, id));
            assertArrayEquals(original, bytes);
        }
    }

    @Test public void storePreservesReferencesAndRejectsRewritesBeforePendingFile() throws Exception
    {
        java.nio.file.Path root = java.nio.file.Files.createTempDirectory(
            java.nio.file.Path.of("build"), "grand-progress-store-");
        AtomicRunStore store = new AtomicRunStore(root, codec, id);
        RunDocument template = document(0, true, false);
        RunDocument initial = new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), "test", List.of(), Optional.empty(), Optional.empty(),
            template.grandFate(), template.grandProgress());
        store.write(initial, -1);
        byte[] original = java.nio.file.Files.readAllBytes(store.snapshotPath());
        RunLifecycle next = new RunLifecycle(id, 1, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        RunDocument dropped = new RunDocument(next, "test", List.of(), Optional.empty(), Optional.empty(), initial.grandFate());
        assertThrows(IOException.class, () -> store.write(dropped, 0));
        SealedGrandFate seal = initial.grandFate().get();
        GrandFateProgress.Evidence reference = new GrandFateProgress.Evidence(seal,
            GrandFateChecklist.initial(seal).required().iterator().next(), UUID.randomUUID(), 1);
        RunDocument injected = new RunDocument(next, "test", List.of(), Optional.empty(), Optional.empty(),
            initial.grandFate(), Optional.of(new GrandFateProgress(seal, 1, List.of(reference))));
        assertThrows(IOException.class, () -> store.write(injected, 0));
        assertArrayEquals(original, java.nio.file.Files.readAllBytes(store.snapshotPath()));
        assertFalse(java.nio.file.Files.exists(store.pendingPath()));
        AtomicRunStore reopened = new AtomicRunStore(root, codec, id);
        assertEquals(initial.grandProgress().get().evidence(), reopened.load().get().grandProgress().get().evidence());
    }
}

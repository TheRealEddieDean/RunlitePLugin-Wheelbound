package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class LifecycleCommandsTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunIdentity id = new RunIdentity("account:profile", UUID.randomUUID());
    private final Gson gson = new Gson();
    private final RunCodec codec = new RunCodec(gson);
    private AtomicRunStore store() { return new AtomicRunStore(folder.getRoot().toPath(), codec, id); }
    private void initialize() throws Exception
    { store().write(new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE), "v1"), -1); }

    @Test public void replayAfterRestartReturnsOriginalResponseEvenAfterResume() throws Exception
    {
        initialize(); UUID pause = UUID.randomUUID();
        RunReceipt first = new LifecycleCommands(store()).execute(id, pause, 0, RunReceipt.Operation.PAUSE);
        new LifecycleCommands(store()).execute(id, UUID.randomUUID(), 1, RunReceipt.Operation.RESUME);
        RunReceipt replay = new LifecycleCommands(store()).execute(id, pause, 0, RunReceipt.Operation.PAUSE);
        assertEquals(first.afterRevision(), replay.afterRevision());
        assertEquals(RunLifecycle.State.PAUSED, replay.state());
        RunDocument current = store().load().get();
        assertEquals(2, current.lifecycle().revision()); assertEquals(2, current.receipts().size());
        assertEquals(RunLifecycle.State.ACTIVE, current.lifecycle().state());
        assertEquals(RunLifecycle.Obligation.ACTIVE_OBJECTIVE, current.lifecycle().obligation());
    }

    @Test public void changedPayloadForeignIdentityAndStaleNewCommandCannotMutate() throws Exception
    {
        initialize(); UUID command = UUID.randomUUID(); LifecycleCommands engine = new LifecycleCommands(store());
        engine.execute(id, command, 0, RunReceipt.Operation.PAUSE);
        assertThrows(IllegalArgumentException.class, () -> engine.execute(id, command, 0, RunReceipt.Operation.RESUME));
        assertThrows(IllegalArgumentException.class, () -> engine.execute(id, command, 1, RunReceipt.Operation.PAUSE));
        assertThrows(IllegalArgumentException.class, () -> engine.execute(new RunIdentity("foreign", id.runId()), command, 0, RunReceipt.Operation.PAUSE));
        assertThrows(IllegalArgumentException.class, () -> engine.execute(id, UUID.randomUUID(), 0, RunReceipt.Operation.RESUME));
        assertEquals(1, store().load().get().lifecycle().revision());
    }

    @Test public void lostAcknowledgementReplaysWithoutSecondCommit() throws Exception
    {
        initialize(); UUID command = UUID.randomUUID();
        AtomicRunStore failing = new AtomicRunStore(folder.getRoot().toPath(), codec, id, boundary -> {
            if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("Lost acknowledgement"); }
        });
        assertThrows(IOException.class, () -> new LifecycleCommands(failing).execute(id, command, 0, RunReceipt.Operation.PAUSE));
        RunReceipt receipt = new LifecycleCommands(store()).execute(id, command, 0, RunReceipt.Operation.PAUSE);
        assertEquals(1, receipt.afterRevision()); assertEquals(1, store().load().get().receipts().size());
    }

    @Test public void noOpAndUninitializedCommandsDoNotCreateReceipts() throws Exception
    {
        assertThrows(IOException.class, () -> new LifecycleCommands(store()).execute(id, UUID.randomUUID(), 0, RunReceipt.Operation.PAUSE));
        initialize();
        assertThrows(IllegalStateException.class, () -> new LifecycleCommands(store()).execute(id, UUID.randomUUID(), 0, RunReceipt.Operation.RESUME));
        assertTrue(store().load().get().receipts().isEmpty());
    }

    @Test public void legacyFoundationPayloadUpgradesInMemoryWithoutChangingOriginal() throws Exception
    {
        initialize(); byte[] original = Files.readAllBytes(store().snapshotPath());
        JsonObject envelope = new JsonParser().parse(new String(original, StandardCharsets.UTF_8)).getAsJsonObject();
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        payload.remove("receipts"); String text = gson.toJson(payload);
        envelope.addProperty("schemaVersion", 1); envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        byte[] legacy = gson.toJson(envelope).getBytes(StandardCharsets.UTF_8); byte[] copy = legacy.clone();
        RunDocument restored = codec.decode(legacy, id);
        assertTrue(restored.receipts().isEmpty()); assertArrayEquals(copy, legacy);
        assertEquals(0, codec.decode(codec.encode(restored), id).lifecycle().revision());
    }

    @Test public void invalidJournalChainAndHeadCannotLoadOrConstruct() throws Exception
    {
        RunReceipt receipt = new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.PAUSE, 0, 1,
            RunLifecycle.State.PAUSED, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        RunLifecycle head = new RunLifecycle(id, 1, RunLifecycle.State.PAUSED, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        assertThrows(IllegalArgumentException.class, () -> new RunDocument(head, "v1", List.of(receipt, receipt)));
        assertThrows(IllegalArgumentException.class, () -> new RunDocument(new RunLifecycle(id, 2,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), "v1", List.of(receipt)));
        RunDocument valid = new RunDocument(head, "v1", List.of(receipt));
        RunDocument roundTrip = codec.decode(codec.encode(valid), id);
        assertEquals(receipt.commandId(), roundTrip.receipts().get(0).commandId());
        assertThrows(UnsupportedOperationException.class, () -> valid.receipts().clear());
    }
}

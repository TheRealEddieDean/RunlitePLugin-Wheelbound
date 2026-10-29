package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class DurableWheelDrawsTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunIdentity id = new RunIdentity("account:profile", UUID.randomUUID());
    private final Gson gson = new Gson();
    private final RunCodec codec = new RunCodec(gson);
    private final WeightedPool pool = new WeightedPool("v1", List.of(new WeightedPool.Entry("A", 1),
        new WeightedPool.Entry("B", 3), new WeightedPool.Entry("C", 2)));
    private AtomicRunStore store() { return new AtomicRunStore(folder.getRoot().toPath(), codec, id); }
    private void initialize(RunLifecycle.State state, RunLifecycle.Obligation obligation) throws Exception
    { store().write(new RunDocument(new RunLifecycle(id, 0, state, obligation), "v1"), -1); }

    @Test public void fpHistorySurvivesPauseResumeDrawAndRestartReplay() throws Exception
    {
        FatePointLedger ledger = new FatePointLedger(10, List.of(
            new FatePointLedger.Entry(FatePointLedger.Kind.PENALTY, 12)));
        store().write(new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), "v1", List.of(), Optional.of(ledger)), -1);
        new LifecycleCommands(store()).execute(id, UUID.randomUUID(), 0, RunReceipt.Operation.PAUSE);
        new LifecycleCommands(store()).execute(id, UUID.randomUUID(), 1, RunReceipt.Operation.RESUME);
        UUID draw = UUID.randomUUID();
        new DurableWheelDraws(store(), bound -> 0).commit(id, draw, 2, pool);
        new DurableWheelDraws(store(), bound -> { throw new AssertionError("Replay redrew"); })
            .commit(id, draw, 2, pool);
        FatePointLedger restored = store().load().get().fatePoints().get();
        assertEquals(10, restored.startingBalance());
        assertEquals(-2, restored.balance());
        assertEquals(1, restored.entries().size());
        assertEquals(FatePointLedger.Kind.PENALTY, restored.entries().get(0).kind());
        assertEquals(12, restored.entries().get(0).amount());
    }

    @Test public void everyTicketRespectsExactWeightsAndBoundaries()
    {
        Map<String, Integer> counts = new HashMap<>();
        for (int i = 0; i < pool.totalWeight(); i++) { counts.merge(pool.atTicket(i).id(), 1, Integer::sum); }
        assertEquals(Map.of("A", 1, "B", 3, "C", 2), counts);
        assertEquals("A", pool.atTicket(0).id()); assertEquals("B", pool.atTicket(1).id());
        assertEquals("B", pool.atTicket(3).id()); assertEquals("C", pool.atTicket(4).id());
        assertThrows(IllegalArgumentException.class, () -> pool.atTicket(-1));
        assertThrows(IllegalArgumentException.class, () -> pool.atTicket(6));
    }

    @Test public void restartReplaysFrozenDrawWithoutCallingRandom() throws Exception
    {
        initialize(RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE); UUID command = UUID.randomUUID();
        AtomicInteger calls = new AtomicInteger();
        DrawCommitment first = new DurableWheelDraws(store(), bound -> { calls.incrementAndGet(); assertEquals(6, bound); return 3; })
            .commit(id, command, 0, pool);
        DrawCommitment replay = new DurableWheelDraws(store(), ignored -> { throw new AssertionError("Replay must not draw"); })
            .commit(id, command, 0, pool);
        assertEquals(first, replay); assertEquals("B", replay.selectedId()); assertEquals(1, calls.get());
        assertEquals(pool, store().load().get().receipts().get(0).draw().pool());
        assertEquals(1, store().load().get().lifecycle().revision());
    }

    @Test public void lostAcknowledgementDoesNotRerollOrCreateSecondReceipt() throws Exception
    {
        initialize(RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE); UUID command = UUID.randomUUID();
        AtomicRunStore failing = new AtomicRunStore(folder.getRoot().toPath(), codec, id, boundary -> {
            if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("Lost acknowledgement"); }
        });
        assertThrows(IOException.class, () -> new DurableWheelDraws(failing, ignored -> 5).commit(id, command, 0, pool));
        DrawCommitment replay = new DurableWheelDraws(store(), ignored -> { throw new AssertionError("No reroll"); })
            .commit(id, command, 0, pool);
        assertEquals("C", replay.selectedId()); assertEquals(1, store().load().get().receipts().size());
    }

    @Test public void changedRequestAndSecondPendingDrawAreRejectedBeforeRandom() throws Exception
    {
        initialize(RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE); UUID command = UUID.randomUUID();
        new DurableWheelDraws(store(), ignored -> 0).commit(id, command, 0, pool);
        DurableWheelDraws engine = new DurableWheelDraws(store(), ignored -> { throw new AssertionError("Invalid command drew"); });
        WeightedPool changed = new WeightedPool("v1", List.of(new WeightedPool.Entry("A", 2), new WeightedPool.Entry("B", 3)));
        assertThrows(IllegalArgumentException.class, () -> engine.commit(id, command, 0, changed));
        assertThrows(IllegalArgumentException.class, () -> engine.commit(id, command, 1, pool));
        assertThrows(IllegalStateException.class, () -> engine.commit(id, UUID.randomUUID(), 1, pool));
        assertThrows(IllegalArgumentException.class, () -> engine.commit(new RunIdentity("other", id.runId()), command, 0, pool));
    }

    @Test public void pauseResumePreservesPendingDrawAndOriginalReplay() throws Exception
    {
        initialize(RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE); UUID command = UUID.randomUUID();
        new DurableWheelDraws(store(), ignored -> 2).commit(id, command, 0, pool);
        LifecycleCommands lifecycle = new LifecycleCommands(store());
        lifecycle.execute(id, UUID.randomUUID(), 1, RunReceipt.Operation.PAUSE);
        lifecycle.execute(id, UUID.randomUUID(), 2, RunReceipt.Operation.RESUME);
        DrawCommitment replay = new DurableWheelDraws(store(), ignored -> { throw new AssertionError("No reroll"); })
            .commit(id, command, 0, pool);
        assertEquals("B", replay.selectedId());
        assertEquals(3, store().load().get().receipts().size());
        assertEquals(RunLifecycle.Obligation.OFFERS_COMMITTED, store().load().get().lifecycle().obligation());
        assertThrows(IllegalArgumentException.class, () -> lifecycle.execute(id, UUID.randomUUID(), 3, RunReceipt.Operation.DRAW));
    }

    @Test public void pausedStaleAndWrongCatalogCannotDraw() throws Exception
    {
        initialize(RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
        DurableWheelDraws engine = new DurableWheelDraws(store(), ignored -> { throw new AssertionError("Invalid command drew"); });
        assertThrows(IllegalStateException.class, () -> engine.commit(id, UUID.randomUUID(), 0, pool));
        assertThrows(IllegalArgumentException.class, () -> engine.commit(id, UUID.randomUUID(), 7, pool));
        assertThrows(IllegalArgumentException.class, () -> engine.commit(id, UUID.randomUUID(), 0,
            new WeightedPool("v2", pool.entries())));
    }

    @Test public void invalidPoolsAndTicketsCannotBecomeCommitments() throws Exception
    {
        assertThrows(IllegalArgumentException.class, () -> new WeightedPool("v1", List.of()));
        assertThrows(IllegalArgumentException.class, () -> new WeightedPool("v1", List.of(new WeightedPool.Entry("A", 1), new WeightedPool.Entry("A", 2))));
        assertThrows(IllegalArgumentException.class, () -> new WeightedPool.Entry("A", 0));
        assertThrows(IllegalArgumentException.class, () -> new WeightedPool.Entry("", 1));
        assertThrows(ArithmeticException.class, () -> new WeightedPool("v1", List.of(new WeightedPool.Entry("A", Integer.MAX_VALUE), new WeightedPool.Entry("B", 1))));
        assertThrows(UnsupportedOperationException.class, () -> pool.entries().clear());
        initialize(RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        assertThrows(IllegalArgumentException.class, () -> new DurableWheelDraws(store(), ignored -> 6).commit(id, UUID.randomUUID(), 0, pool));
        assertEquals(0, store().load().get().lifecycle().revision());
    }

    @Test public void schemaTwoJournalLoadsWithoutLosingReceiptHistory() throws Exception
    {
        initialize(RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        UUID command = UUID.randomUUID(); new LifecycleCommands(store()).execute(id, command, 0, RunReceipt.Operation.PAUSE);
        byte[] bytes = codec.encode(store().load().get());
        JsonObject envelope = new JsonParser().parse(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
        // Schema 2 predates FP state; changing only the envelope version leaves an invalid payload.
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        payload.remove("fatePoints"); payload.remove("inventory");
        String text = gson.toJson(payload);
        envelope.addProperty("schemaVersion", 2);
        envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        RunDocument restored = codec.decode(gson.toJson(envelope).getBytes(StandardCharsets.UTF_8), id);
        assertEquals(1, restored.receipts().size());
        assertEquals(command, restored.receipts().get(0).commandId());
        assertEquals(RunReceipt.Operation.PAUSE, restored.receipts().get(0).operation());
        assertNull(restored.receipts().get(0).draw());
        assertFalse(restored.fatePoints().isPresent());
        RunDocument upgraded = codec.decode(codec.encode(restored), id);
        assertEquals(command, upgraded.receipts().get(0).commandId());
        assertFalse(upgraded.fatePoints().isPresent());
    }

    @Test public void invalidSerializedTicketIsRejectedEvenWithRecomputedChecksum() throws Exception
    {
        initialize(RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        new DurableWheelDraws(store(), ignored -> 0).commit(id, UUID.randomUUID(), 0, pool);
        JsonObject envelope = new JsonParser().parse(new String(codec.encode(store().load().get()), StandardCharsets.UTF_8)).getAsJsonObject();
        JsonObject payload = new JsonParser().parse(envelope.get("payload").getAsString()).getAsJsonObject();
        payload.getAsJsonArray("receipts").get(0).getAsJsonObject().getAsJsonObject("draw").addProperty("ticket", 6);
        String text = gson.toJson(payload); envelope.addProperty("payload", text);
        envelope.addProperty("sha256", RunCodec.sha256(text.getBytes(StandardCharsets.UTF_8)));
        assertThrows(IOException.class, () -> codec.decode(gson.toJson(envelope).getBytes(StandardCharsets.UTF_8), id));
    }
}

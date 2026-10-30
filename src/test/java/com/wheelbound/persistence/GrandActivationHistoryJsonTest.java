package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wheelbound.domain.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class GrandActivationHistoryJsonTest
{
    private GrandActivationHistory history(int ticket)
    {
        RunIdentity identity = new RunIdentity("history-json", UUID.randomUUID());
        SealedGrandFate seal = new SealedGrandFate(identity, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
        GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
        GrandFateChecklist.Objective objective = checklist.unfinished().iterator().next();
        return new GrandActivationHistory(seal, 3, List.of(
            new GrandActivationReceipt(UUID.randomUUID(), identity, 0, checklist, objective),
            new GrandActivationReceipt(UUID.randomUUID(), identity, 2, checklist, objective)));
    }

    private GrandActivationHistory restart(GrandActivationHistory history, long head)
    {
        return GrandActivationHistoryJson.decode(new JsonParser().parse(
            GrandActivationHistoryJson.encode(history, head).toString()).getAsJsonObject(), history.seal(), head);
    }

    @Test public void restartRetainsOrderedOriginalPayloadsAndReplayForEveryGoal()
    {
        for (int ticket = 0; ticket < 5; ticket++)
        {
            GrandActivationHistory original = history(ticket);
            for (long head : new long[] {3, 40, Long.MAX_VALUE})
            {
                GrandActivationHistory restored = restart(original, head);
                assertEquals(new ArrayList<>(original.receipts().values()), new ArrayList<>(restored.receipts().values()));
                for (GrandActivationReceipt receipt : original.receipts().values())
                { assertEquals(receipt, restored.replay(receipt, original.seal().identity(), head)); }
            }
            GrandActivationHistory empty = new GrandActivationHistory(original.seal(), 0, Collections.emptyList());
            assertTrue(restart(empty, 0).receipts().isEmpty());
            assertThrows(IllegalArgumentException.class, () -> restart(empty, -1));
        }
        GrandActivationHistory original = history(3);
        List<GrandActivationReceipt> receipts = new ArrayList<>(original.receipts().values());
        GrandFateChecklist checklist = receipts.get(0).attempt().checklist();
        GrandFateChecklist advanced = GrandFateChecklist.restore(original.seal(),
            Collections.singleton(receipts.get(0).attempt().objective()));
        receipts.add(new GrandActivationReceipt(UUID.randomUUID(), original.seal().identity(), 4,
            advanced, advanced.unfinished().iterator().next()));
        GrandActivationHistory progressed = new GrandActivationHistory(original.seal(), 5, receipts);
        assertEquals(receipts, new ArrayList<>(restart(progressed, 5).receipts().values()));
        GrandActivationReceipt boundary = new GrandActivationReceipt(UUID.randomUUID(), original.seal().identity(),
            Long.MAX_VALUE - 1, advanced, advanced.unfinished().iterator().next());
        receipts.add(boundary);
        assertEquals(boundary, restart(new GrandActivationHistory(original.seal(), Long.MAX_VALUE, receipts),
            Long.MAX_VALUE).replay(boundary, original.seal().identity(), Long.MAX_VALUE));
        JsonObject regression = GrandActivationHistoryJson.encode(progressed, 7);
        regression.getAsJsonArray("receipts").add(GrandActivationReceiptJson.encode(
            new GrandActivationReceipt(UUID.randomUUID(), original.seal().identity(), 6,
                checklist, advanced.unfinished().iterator().next()), 7));
        assertThrows(IllegalArgumentException.class, () -> GrandActivationHistoryJson.decode(regression, original.seal(), 7));
        GrandActivationReceipt changed = new GrandActivationReceipt(receipts.get(0).commandId(),
            original.seal().identity(), 0, checklist, advanced.unfinished().iterator().next());
        assertThrows(IllegalArgumentException.class, () -> restart(progressed, 5).replay(changed,
            original.seal().identity(), 5));
    }

    @Test public void malformedForeignFutureAndConflictingHistoriesFailClosed()
    {
        GrandActivationHistory history = history(3);
        List<Consumer<JsonObject>> changes = List.of(
            j -> j.remove("receipts"), j -> j.addProperty("receipts", false),
            j -> j.addProperty("version", 2), j -> j.addProperty("version", "1"),
            j -> j.addProperty("unknown", true), j -> j.addProperty("accountKey", "foreign"),
            j -> j.addProperty("sealCommandId", UUID.randomUUID().toString()),
            j -> j.getAsJsonArray("receipts").add(j.getAsJsonArray("receipts").get(0).deepCopy()),
            j -> {
                com.google.gson.JsonElement first = j.getAsJsonArray("receipts").remove(0);
                j.getAsJsonArray("receipts").add(first);
            },
            j -> j.getAsJsonArray("receipts").get(1).getAsJsonObject().addProperty("beforeRevision", 1),
            j -> j.getAsJsonArray("receipts").get(1).getAsJsonObject().addProperty("commandId",
                j.getAsJsonArray("receipts").get(0).getAsJsonObject().get("commandId").getAsString()),
            j -> j.getAsJsonArray("receipts").get(0).getAsJsonObject().addProperty("commandId", history.seal().commandId().toString()),
            j -> j.getAsJsonArray("receipts").get(0).getAsJsonObject().addProperty("accountKey", "foreign"),
            j -> j.getAsJsonArray("receipts").add(false));
        for (Consumer<JsonObject> change : changes)
        {
            JsonObject json = GrandActivationHistoryJson.encode(history, 3);
            change.accept(json);
            assertThrows(RuntimeException.class, () -> GrandActivationHistoryJson.decode(json, history.seal(), 3));
        }
        assertThrows(IllegalArgumentException.class, () -> GrandActivationHistoryJson.encode(history, 2));
        assertThrows(IllegalArgumentException.class, () -> GrandActivationHistoryJson.decode(
            GrandActivationHistoryJson.encode(history, 3), history.seal(), 2));
    }
}

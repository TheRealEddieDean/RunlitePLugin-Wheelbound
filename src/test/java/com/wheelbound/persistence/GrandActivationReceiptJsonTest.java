package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wheelbound.domain.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class GrandActivationReceiptJsonTest
{
    private final RunIdentity identity = new RunIdentity("activation-json", UUID.randomUUID());

    private GrandActivationReceipt receipt(int ticket)
    {
        return receipt(GrandFateChecklist.initial(new SealedGrandFate(identity, UUID.randomUUID(),
            GrandFatePool.atTicket(ticket))));
    }

    private GrandActivationReceipt receipt(GrandFateChecklist checklist)
    {
        return new GrandActivationReceipt(UUID.randomUUID(), identity, 12, checklist,
            checklist.unfinished().iterator().next());
    }

    private GrandActivationReceipt restart(GrandActivationReceipt original, long head)
    {
        JsonObject json = new JsonParser().parse(GrandActivationReceiptJson.encode(original, head).toString())
            .getAsJsonObject();
        return GrandActivationReceiptJson.decode(json, original.attempt().checklist().seal(), head);
    }

    @Test public void originalPayloadSurvivesRestartAndLaterHeadsForEveryGoal()
    {
        for (int ticket = 0; ticket < 5; ticket++)
        {
            GrandActivationReceipt original = receipt(ticket);
            for (long head : new long[] {13, 40, Long.MAX_VALUE})
            {
                GrandActivationReceipt restored = restart(original, head);
                assertEquals(original, restored);
                assertEquals(original.hashCode(), restored.hashCode());
                assertEquals(13, restored.attempt().lifecycle().revision());
                assertEquals(original.attempt().objective(), restored.attempt().objective());
            }
        }
        GrandActivationReceipt awakened = receipt(3);
        GrandFateChecklist checklist = GrandFateChecklist.restore(awakened.attempt().checklist().seal(),
            Collections.singleton(awakened.attempt().objective()));
        GrandActivationReceipt retained = receipt(checklist);
        assertEquals(retained, restart(retained, 50));
        GrandActivationReceipt boundary = new GrandActivationReceipt(UUID.randomUUID(), identity,
            Long.MAX_VALUE - 1, checklist, retained.attempt().objective());
        assertEquals(boundary, restart(boundary, Long.MAX_VALUE));
    }

    @Test public void sameCommandNeverHidesChangedSelectionOrCompletedPrefix()
    {
        GrandActivationReceipt original = receipt(3);
        GrandFateChecklist checklist = original.attempt().checklist();
        GrandFateChecklist.Objective other = checklist.unfinished().stream()
            .filter(o -> o != original.attempt().objective()).findFirst().get();
        GrandActivationReceipt selection = new GrandActivationReceipt(original.commandId(), identity, 12, checklist, other);
        GrandActivationReceipt prefix = new GrandActivationReceipt(original.commandId(), identity, 12,
            GrandFateChecklist.restore(checklist.seal(), Collections.singleton(other)), original.attempt().objective());
        assertNotEquals(restart(original, 50), restart(selection, 50));
        assertNotEquals(restart(original, 50), restart(prefix, 50));
    }

    @Test public void malformedForeignAndFuturePayloadsFailClosed()
    {
        GrandActivationReceipt original = receipt(3);
        List<Consumer<JsonObject>> changes = List.of(
            j -> j.remove("completed"), j -> j.addProperty("unknown", true),
            j -> j.addProperty("version", 2), j -> j.addProperty("version", "1"),
            j -> j.addProperty("beforeRevision", -1), j -> j.addProperty("beforeRevision", 12.5),
            j -> j.addProperty("beforeRevision", Long.MAX_VALUE), j -> j.addProperty("afterRevision", 14),
            j -> j.addProperty("objective", "UNKNOWN"), j -> j.addProperty("commandId", "invalid"),
            j -> j.addProperty("accountKey", "foreign"), j -> j.addProperty("runId", UUID.randomUUID().toString()),
            j -> j.addProperty("sealCommandId", UUID.randomUUID().toString()),
            j -> j.addProperty("completed", false),
            j -> j.getAsJsonArray("completed").add(original.attempt().objective().name()),
            j -> {
                j.getAsJsonArray("completed").add("AWAKENED_WHISPERER");
                j.getAsJsonArray("completed").add("AWAKENED_WHISPERER");
            },
            j -> j.getAsJsonArray("completed").add("GHOMMALS_HILT_5"));
        for (Consumer<JsonObject> change : changes)
        {
            JsonObject json = GrandActivationReceiptJson.encode(original, 13);
            change.accept(json);
            assertThrows(RuntimeException.class, () -> GrandActivationReceiptJson.decode(json,
                original.attempt().checklist().seal(), 50));
        }
        assertThrows(IllegalArgumentException.class, () -> GrandActivationReceiptJson.encode(original, 12));
        assertThrows(IllegalArgumentException.class, () -> GrandActivationReceiptJson.decode(
            GrandActivationReceiptJson.encode(original, 13), original.attempt().checklist().seal(), 12));
    }
}

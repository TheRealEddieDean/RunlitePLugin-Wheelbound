package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuestFateObservationWindowCodecTest
{
    private QuestFateSelection selection()
    {
        var offers = new QuestFateOffers(QuestFateOffers.Capacity.THREE, "catalog-1",
            List.of("quest-a", "quest-b", "quest-c"));
        var yes = QuestFateEligibility.Status.QUALIFIED;
        var eligibility = new QuestFateEligibility(offers, List.of(
            new QuestFateEligibility.Observation("quest-a", "route-a", "v1", yes, yes, yes),
            new QuestFateEligibility.Observation("quest-b", "route-b", "v1", yes, yes, yes),
            new QuestFateEligibility.Observation("quest-c", "route-c", "v1", yes, yes, yes)));
        return new QuestFateSelection(eligibility, offers.select("quest-a"));
    }

    private QuestFateObservationWindow window(long revision, long position,
        QuestFateCompletion.QuestState baseline)
    {
        var context = new RunLifecycle(new RunIdentity("account", UUID.randomUUID()), revision,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        return new QuestFateObservationWindow(context, UUID.randomUUID(), UUID.randomUUID(),
            UUID.randomUUID(), position, selection(), baseline);
    }

    @Test public void preservesOriginalContextAndCompletionSemanticsThroughGson()
    {
        for (long boundary : new long[] {0, Long.MAX_VALUE})
        {
            for (var baseline : QuestFateCompletion.QuestState.values())
            {
                var original = window(boundary, boundary, baseline);
                Gson gson = new Gson();
                String json = gson.toJson(QuestFateObservationWindowCodec.encode(original, selection()));
                var restored = QuestFateObservationWindowCodec.decode(
                    new JsonParser().parse(json).getAsJsonObject(), selection());
                assertEquals(original, restored);
                assertEquals(json, gson.toJson(QuestFateObservationWindowCodec.encode(restored, selection())));
            }
        }
        var original = window(7, 19, QuestFateCompletion.QuestState.IN_PROGRESS);
        var restored = QuestFateObservationWindowCodec.decode(
            QuestFateObservationWindowCodec.encode(original, selection()), selection());
        var active = new RunLifecycle(original.identity(), 7, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        restored.requireFreshCompletion(active, 7, original.assignmentId(), original.sessionId(),
            original.windowId(), selection(), 20, "quest-a", QuestFateCompletion.QuestState.COMPLETE);
        try
        {
            restored.requireFreshCompletion(active.pause(active.identity(), 7), 8,
                original.assignmentId(), original.sessionId(), original.windowId(), selection(),
                20, "quest-a", QuestFateCompletion.QuestState.COMPLETE);
            fail("Paused context accepted");
        }
        catch (IllegalStateException expected) { }
        assertEquals(original, restored);
    }

    @Test public void refusesMalformedFieldsTypesVersionsAndBounds()
    {
        JsonObject valid = QuestFateObservationWindowCodec.encode(
            window(7, 19, QuestFateCompletion.QuestState.NOT_STARTED), selection());
        for (String field : valid.keySet())
        {
            rejects(valid, v -> v.remove(field));
            rejects(valid, v -> v.add(field, JsonNull.INSTANCE));
            rejects(valid, v -> v.add(field, new JsonObject()));
        }
        rejects(valid, v -> v.addProperty("extra", true));
        rejects(valid, v -> v.addProperty("version", 2));
        for (String field : new String[] {"version", "baselineRevision", "baselinePosition"})
        {
            rejects(valid, v -> v.addProperty(field, "1"));
            rejects(valid, v -> v.addProperty(field, -1));
            rejects(valid, v -> v.addProperty(field, 1.5));
            rejects(valid, v -> v.add(field, new JsonParser().parse("9223372036854775808")));
        }
        for (String field : new String[] {"runId", "assignmentId", "sessionId", "windowId"})
        {
            rejects(valid, v -> v.addProperty(field, "invalid"));
            rejects(valid, v -> v.addProperty(field, "1-1-1-1-1"));
            rejects(valid, v -> v.addProperty(field, 1));
        }
        rejects(valid, v -> v.addProperty("baseline", "UNKNOWN"));
        rejects(valid, v -> v.addProperty("accountKey", " "));
    }

    @Test public void requiresSharedSelectionOnEncodeAndRetainsAlteredDeclarations()
    {
        var original = window(7, 19, QuestFateCompletion.QuestState.NOT_STARTED);
        var other = new QuestFateSelection(selection().eligibility(),
            selection().selection().offers().select("quest-b"));
        try { QuestFateObservationWindowCodec.encode(original, other); fail("Foreign selection accepted"); }
        catch (IllegalArgumentException expected) { }
        JsonObject value = QuestFateObservationWindowCodec.encode(original, selection());
        UUID changedSession = UUID.randomUUID();
        value.addProperty("sessionId", changedSession.toString());
        var changed = QuestFateObservationWindowCodec.decode(value, selection());
        assertEquals(changedSession, changed.sessionId());
        assertNotEquals(original, changed);
        // Selection is a shared enclosing reference, not independently authenticated here.
        var changedSelection = QuestFateObservationWindowCodec.decode(value, other);
        assertEquals(other, changedSelection.selection());
        assertNotEquals(changed, changedSelection);
    }

    private void rejects(JsonObject valid, Consumer<JsonObject> mutation)
    {
        JsonObject value = new JsonParser().parse(valid.toString()).getAsJsonObject();
        mutation.accept(value);
        try { QuestFateObservationWindowCodec.decode(value, selection()); fail("Malformed window accepted"); }
        catch (IllegalArgumentException | IllegalStateException expected) { }
    }
}

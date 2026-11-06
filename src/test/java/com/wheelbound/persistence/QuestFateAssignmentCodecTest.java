package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuestFateAssignmentCodecTest
{
    @Test public void roundTripsOriginalCommitmentWithoutRefreshingItsWindow()
    {
        var fixture = QuestFateAssignmentChecks.fixture();
        for (long spins : new long[] {1, 5, Long.MAX_VALUE})
        {
            var allocation = MasterSpinAllocation.restore(fixture.provenance().resolution(), spins);
            var original = new QuestFateAssignment(fixture.provenance(), allocation, fixture.originalWindow());
            var json = QuestFateAssignmentCodec.encode(original);
            var parsed = new JsonParser().parse(new Gson().toJson(json)).getAsJsonObject();
            var restored = QuestFateAssignmentCodec.decode(parsed, allocation);
            assertEquals(original, restored);
            assertEquals(original.hashCode(), restored.hashCode());
            assertEquals(original.originalWindow(), restored.originalWindow());
            assertSame(allocation, restored.allocation());
            assertEquals(json, QuestFateAssignmentCodec.encode(restored));
            assertEquals(spins - 1, allocation.spinsAfter());
        }
    }

    @Test public void rejectsMalformedAndIncompatibleCompositions()
    {
        var original = QuestFateAssignmentChecks.fixture();
        var valid = QuestFateAssignmentCodec.encode(original);
        Consumer<Consumer<JsonObject>> mutate = edit -> {
            var copy = valid.deepCopy();
            edit.accept(copy);
            rejects(() -> QuestFateAssignmentCodec.decode(copy, original.allocation()));
        };
        for (String field : valid.keySet())
        {
            mutate.accept(v -> v.remove(field));
            mutate.accept(v -> v.add(field, JsonNull.INSTANCE));
            mutate.accept(v -> v.add(field, new JsonArray()));
        }
        mutate.accept(v -> v.addProperty("extra", 1));
        mutate.accept(v -> v.addProperty("version", 2));
        mutate.accept(v -> v.addProperty("version", "1"));
        mutate.accept(v -> v.getAsJsonObject("originalWindow").addProperty("baseline", "COMPLETE"));
        mutate.accept(v -> v.getAsJsonObject("originalWindow").addProperty("accountKey", "foreign"));
        mutate.accept(v -> v.getAsJsonObject("originalWindow").addProperty("baselineRevision",
            original.provenance().resolution().afterRevision() - 1));
        mutate.accept(v -> v.getAsJsonObject("provenance").addProperty("spinsBefore", 6));
        rejects(() -> QuestFateAssignmentCodec.decode(valid, QuestFateAssignmentChecks.fixture().allocation()));
        rejects(() -> QuestFateAssignmentCodec.decode(valid, null));
        rejects(() -> QuestFateAssignmentCodec.encode(null));
    }

    @Test public void validAlteredDeclarationsAreNotAuthenticatedByRetainedAccounting()
    {
        var original = QuestFateAssignmentChecks.fixture();
        var json = QuestFateAssignmentCodec.encode(original);
        json.getAsJsonObject("provenance").addProperty("finalStreamState", "changed-state");
        var restored = QuestFateAssignmentCodec.decode(json, original.allocation());
        assertNotEquals(original, restored);
        assertEquals("changed-state", restored.provenance().finalStreamState());
        assertEquals(original.originalWindow(), restored.originalWindow());
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); fail("Invalid assignment accepted"); }
        catch (IllegalArgumentException | NullPointerException expected) { }
    }
}

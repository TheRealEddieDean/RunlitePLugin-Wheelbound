package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuestFateProvenanceCodecTest
{
    @Test public void roundTripsFullRetryTranscriptAndOriginalAccounting()
    {
        var assignment = QuestFateAssignmentChecks.fixture();
        var original = assignment.provenance();
        assertTrue(original.resolution().resolved().drawCount() > 1);
        for (long spins : new long[] {1, 5, Long.MAX_VALUE})
        {
            var retained = MasterSpinAllocation.restore(original.resolution(), spins);
            var json = QuestFateProvenanceCodec.encode(original, retained);
            var parsed = new JsonParser().parse(new Gson().toJson(json)).getAsJsonObject();
            var restored = QuestFateProvenanceCodec.decode(parsed, retained);
            assertEquals(original, restored);
            assertEquals(json, QuestFateProvenanceCodec.encode(restored, retained));
            assertEquals(spins, retained.spinsBefore());
            assertEquals(spins - 1, retained.spinsAfter());
        }
    }

    @Test public void rejectsChangedRetainedResolutionIncludingRetryPrefix()
    {
        var assignment = QuestFateAssignmentChecks.fixture();
        var original = assignment.provenance();
        var resolution = original.resolution();
        var transcript = resolution.resolved();
        var steps = new ArrayList<MasterSelectionProvenance.Step>();
        transcript.landings().forEach(landing -> steps.add(landing.step()));
        steps.set(0, new MasterSelectionProvenance.Step(steps.get(0).draw(), "changed-prefix"));
        var changed = new MasterSelectionProvenance(transcript.transactionId(), transcript.snapshotVersion(),
            transcript.catalogVersion(), transcript.streamDescriptor(), transcript.initialStreamState(),
            transcript.readiness(), steps);
        var other = new MasterSelectionResolutionReceipt(resolution.commandId(), resolution.beforeRevision(),
            resolution.creation(), changed);
        var retained = assignment.allocation();
        var json = QuestFateProvenanceCodec.encode(original, retained);
        rejects(() -> QuestFateProvenanceCodec.decode(json, MasterSpinAllocation.restore(other, 5)));
        rejects(() -> QuestFateProvenanceCodec.encode(original, MasterSpinAllocation.restore(other, 5)));
        rejects(() -> QuestFateProvenanceCodec.decode(json, MasterSpinAllocation.restore(resolution, 6)));
        rejects(() -> QuestFateProvenanceCodec.decode(json, QuestFateAssignmentChecks.fixture().allocation()));
    }

    @Test public void rejectsMalformedPayloadsAndPreservesValidChangedDeclarations()
    {
        var assignment = QuestFateAssignmentChecks.fixture();
        var retained = assignment.allocation();
        var valid = QuestFateProvenanceCodec.encode(assignment.provenance(), retained);
        Consumer<Consumer<JsonObject>> mutate = edit -> {
            JsonObject copy = valid.deepCopy();
            edit.accept(copy);
            rejects(() -> QuestFateProvenanceCodec.decode(copy, retained));
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
        mutate.accept(v -> v.addProperty("spinsBefore", "5"));
        mutate.accept(v -> v.addProperty("spinsBefore", 0));
        mutate.accept(v -> v.addProperty("spinsBefore", 1.5));
        mutate.accept(v -> v.add("spinsBefore", new JsonParser().parse("9223372036854775808")));
        mutate.accept(v -> v.addProperty("sliceId", "0-0-0-0-2"));
        mutate.accept(v -> v.addProperty("activityKey", "combat"));
        mutate.accept(v -> v.addProperty("streamDescriptor", " "));
        JsonObject changed = valid.deepCopy();
        changed.addProperty("finalStreamState", "other-state");
        var restored = QuestFateProvenanceCodec.decode(changed, retained);
        assertEquals("other-state", restored.finalStreamState());
        rejects(() -> assignment.provenance().requireSameProvenance(restored));
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); fail("Invalid provenance accepted"); }
        catch (IllegalArgumentException | NullPointerException expected) { }
    }
}

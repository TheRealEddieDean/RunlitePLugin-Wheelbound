package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuestFateHistoricalContextCodecTest
{
    @Test public void preservesHistoricalLifecycleAndCommitmentAcrossJson()
    {
        var assignment = QuestFateAssignmentChecks.fixture();
        long revision = assignment.creationRevision() + 3;
        var history = new MasterSelectionResolutions(assignment.identity(), revision,
            List.of(assignment.provenance().resolution()));
        var spins = new MasterSpinState(MasterSpinState.VERSION, assignment.identity(), revision,
            assignment.allocation().spinsBefore(), Optional.of(assignment.allocation()));
        for (var state : List.of(RunLifecycle.State.ACTIVE, RunLifecycle.State.PAUSED))
        {
            var current = QuestFateCurrentAssignment.restore(assignment, history, spins,
                new RunLifecycle(assignment.identity(), revision, state, RunLifecycle.Obligation.ACTIVE_OBJECTIVE));
            var encoded = QuestFateHistoricalContextCodec.encode(current);
            var parsed = new JsonParser().parse(new Gson().toJson(encoded)).getAsJsonObject();
            var restored = QuestFateHistoricalContextCodec.decode(parsed, history, spins);
            assertEquals(assignment, restored.assignment());
            assertEquals(revision, restored.lifecycle().revision());
            assertEquals(state, restored.lifecycle().state());
            assertEquals(encoded, QuestFateHistoricalContextCodec.encode(restored));

            Consumer<Consumer<JsonObject>> mutate = edit -> {
                var copy = parsed.deepCopy();
                edit.accept(copy);
                rejects(() -> QuestFateHistoricalContextCodec.decode(copy, history, spins));
            };
            for (String field : encoded.keySet())
            {
                mutate.accept(v -> v.remove(field));
                mutate.accept(v -> v.add(field, JsonNull.INSTANCE));
            }
            mutate.accept(v -> v.addProperty("version", 2));
            mutate.accept(v -> v.addProperty("version", "1"));
            mutate.accept(v -> v.addProperty("extra", true));
            mutate.accept(v -> v.addProperty("revision", revision - 1));
            mutate.accept(v -> v.addProperty("state", "COMPLETED"));
            mutate.accept(v -> v.addProperty("state", "unknown"));
            mutate.accept(v -> v.addProperty("obligation", "NONE"));
            mutate.accept(v -> v.add("assignment", new JsonArray()));
            rejects(() -> QuestFateHistoricalContextCodec.decode(parsed,
                new MasterSelectionResolutions(assignment.identity(), revision, List.of()), spins));
            rejects(() -> QuestFateHistoricalContextCodec.decode(parsed, history,
                new MasterSpinState(MasterSpinState.VERSION, assignment.identity(), revision,
                    spins.allotted(), Optional.empty())));
            assertEquals(encoded, parsed);
        }
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); fail("Invalid historical context accepted"); }
        catch (IllegalArgumentException | NullPointerException expected) { }
    }
}

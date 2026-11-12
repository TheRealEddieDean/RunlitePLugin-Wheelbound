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
    @Test public void optInHistoryRetainsOriginalDrawAndLegacyBoundaries()
    {
        for (var capacity : QuestFateOffers.Capacity.values())
        {
            var fixture = new QuestFateOfferDrawCodecTest.Fixture(capacity, 1);
            var resolution = fixture.provenance.resolution();
            var window = new QuestFateObservationWindow(new RunLifecycle(resolution.identity(),
                resolution.afterRevision(), RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE),
                java.util.UUID.randomUUID(), java.util.UUID.randomUUID(), java.util.UUID.randomUUID(), 0,
                fixture.provenance.reward().selection(), QuestFateCompletion.QuestState.IN_PROGRESS);
            var assignment = new QuestFateAssignment(fixture.provenance,
                MasterSpinAllocation.restore(resolution, 5), window, Optional.of(fixture.draw));
            long revision = assignment.creationRevision() + 3;
            var history = new MasterSelectionResolutions(assignment.identity(), revision, List.of(resolution));
            var spins = new MasterSpinState(MasterSpinState.VERSION, assignment.identity(), revision,
                assignment.allocation().spinsBefore(), Optional.of(assignment.allocation()));
            for (var state : List.of(RunLifecycle.State.ACTIVE, RunLifecycle.State.PAUSED))
            {
                var lifecycle = new RunLifecycle(assignment.identity(), revision, state,
                    RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
                var context = QuestFateCurrentAssignment.restore(assignment, history, spins, lifecycle);
                var encoded = QuestFateHistoricalContextCodec.encodeWithDrawEvidence(context);
                var parsed = new JsonParser().parse(new Gson().toJson(encoded)).getAsJsonObject();
                var restored = QuestFateHistoricalContextCodec.decodeWithDrawEvidence(parsed, history, spins);
                assertEquals(assignment, restored.assignment());
                assertEquals(fixture.draw.draws(), restored.assignment().offerDraw().get().draws());
                assertEquals(state, restored.lifecycle().state());
                assertEquals(revision, restored.lifecycle().revision());
                assertEquals(encoded, QuestFateHistoricalContextCodec.encodeWithDrawEvidence(restored));
                rejects(() -> QuestFateHistoricalContextCodec.encode(context));
                rejects(() -> QuestFateHistoricalContextCodec.decode(parsed, history, spins));
                var downgraded = parsed.deepCopy();
                downgraded.addProperty("version", 1);
                rejects(() -> QuestFateHistoricalContextCodec.decodeWithDrawEvidence(downgraded, history, spins));
                var missing = parsed.deepCopy();
                missing.getAsJsonObject("assignment").remove("offerDraw");
                rejects(() -> QuestFateHistoricalContextCodec.decodeWithDrawEvidence(missing, history, spins));
                rejects(() -> QuestFateHistoricalContextCodec.decodeWithDrawEvidence(parsed, history,
                    new MasterSpinState(MasterSpinState.VERSION, assignment.identity(), revision,
                        spins.allotted(), Optional.empty())));
                var legacy = QuestFateCurrentAssignment.restore(new QuestFateAssignment(
                    assignment.provenance(), assignment.allocation(), assignment.originalWindow()),
                    history, spins, lifecycle);
                var legacyPayload = QuestFateHistoricalContextCodec.encode(legacy);
                assertFalse(QuestFateHistoricalContextCodec.decodeWithDrawEvidence(legacyPayload,
                    history, spins).assignment().offerDraw().isPresent());
                var absent = QuestFateHistoricalContextCodec.encodeWithDrawEvidence(legacy);
                assertTrue(absent.getAsJsonObject("assignment").get("offerDraw").isJsonNull());
                assertEquals(legacy.assignment(), QuestFateHistoricalContextCodec.decodeWithDrawEvidence(
                    absent, history, spins).assignment());
                assertEquals(encoded, parsed);
            }
        }
    }

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

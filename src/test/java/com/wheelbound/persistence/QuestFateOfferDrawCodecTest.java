package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.util.*;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuestFateOfferDrawCodecTest
{
    static final class Fixture
    {
        final QuestFatePoolEvidence evidence;
        final QuestFateOfferDraw draw;
        final QuestFateProvenance provenance;

        Fixture(QuestFateOffers.Capacity capacity, int ticketOffset)
        {
            var identity = new RunIdentity("fixture:draw-codec", UUID.randomUUID());
            var slice = ActiveWheel.Slice.ordinary(UUID.randomUUID(), "questing", ActiveWheel.Enhancement.BASE);
            var observations = new ArrayList<QuestFateEligibility.Observation>();
            var entries = new ArrayList<WeightedPool.Entry>();
            var required = new HashSet<String>();
            for (int i = 0; i < 6; i++)
            {
                String id = "q" + i;
                required.add(id);
                observations.add(new QuestFateEligibility.Observation(id, "route", "v1",
                    QuestFateEligibility.Status.QUALIFIED, QuestFateEligibility.Status.QUALIFIED,
                    QuestFateEligibility.Status.QUALIFIED));
                entries.add(new WeightedPool.Entry(id, i + 2));
            }
            required.add("unavailable");
            observations.add(new QuestFateEligibility.Observation("unavailable", "locked-route", "v2",
                QuestFateEligibility.Status.INELIGIBLE, QuestFateEligibility.Status.QUALIFIED,
                QuestFateEligibility.Status.INELIGIBLE));
            evidence = new QuestFatePoolEvidence(identity, 7, "snapshot", "catalog", "questing",
                capacity, required, observations);
            var draws = new ArrayList<DrawCommitment>();
            for (int i = 0; i < capacity.count(); i++)
            {
                var step = new DrawCommitment(new WeightedPool("catalog", entries), ticketOffset);
                draws.add(step);
                entries.removeIf(entry -> entry.id().equals(step.selectedId()));
            }
            draw = new QuestFateOfferDraw(evidence, identity, 7, "snapshot", "catalog", slice, draws);
            var slices = new ArrayList<ActiveWheel.Slice>();
            slices.add(slice);
            var statuses = new HashMap<UUID, MasterSelectionReadiness.Classification>();
            statuses.put(slice.id(), evidence.masterEvidence().classifyFor(identity, 7, "snapshot", "catalog", slice));
            for (int i = 0; i < 4; i++)
            {
                var other = ActiveWheel.Slice.ordinary(UUID.randomUUID(), i % 2 == 0 ? "combat" : "mining",
                    ActiveWheel.Enhancement.BASE);
                slices.add(other);
                statuses.put(other.id(), new MasterSelectionReadiness.Classification(
                    MasterSelectionReadiness.Availability.ASSIGNABLE, "supplied"));
            }
            var initial = new MasterSelectionProvenance(UUID.randomUUID(), "snapshot", "catalog", "master", "0",
                new MasterSelectionReadiness(new ActiveWheel(slices), statuses), List.of());
            var resolved = initial.append(initial.transactionId(), 0, "0",
                new MasterSelectionProvenance.Step(new DrawCommitment(initial.pool(), 0), "1"));
            var resolution = new MasterSelectionResolutionReceipt(UUID.randomUUID(), 8,
                new MasterSelectionCreationReceipt(identity, 7, initial), resolved);
            var offers = draw.eligibility().offers();
            provenance = new QuestFateProvenance(resolution, slice.id(), "questing", "quest", "0", "end",
                new QuestFateReward(new QuestFateSelection(draw.eligibility(), offers.select(offers.questIds().get(0))),
                    "quote", "v1", 0));
        }
    }

    @Test public void completeEvidenceEnvelopeRetainsUnofferedAndIneligibleObservations()
    {
        for (var capacity : QuestFateOffers.Capacity.values())
        {
            var fixture = new Fixture(capacity, 1);
            var gson = new Gson();
            String json = gson.toJson(QuestFateDrawEvidenceCodec.encode(fixture.draw, fixture.provenance));
            var restored = QuestFateDrawEvidenceCodec.decode(
                new JsonParser().parse(json).getAsJsonObject(), fixture.provenance);
            assertEquals(fixture.evidence.observations(), restored.evidence().observations());
            assertEquals(7, restored.evidence().observations().size());
            assertEquals(fixture.draw.draws(), restored.draws());
            assertEquals(fixture.draw.eligibility(), restored.eligibility());
            assertEquals(json, gson.toJson(QuestFateDrawEvidenceCodec.encode(restored, fixture.provenance)));
        }
    }

    @Test public void completeEvidenceEnvelopeRejectsMalformedAndInconsistentPayloads()
    {
        rejectsEvidence(v -> v.remove("version"));
        rejectsEvidence(v -> v.addProperty("version", 2));
        rejectsEvidence(v -> v.addProperty("version", "1"));
        rejectsEvidence(v -> v.addProperty("extra", true));
        rejectsEvidence(v -> v.addProperty("capacity", "FOUR"));
        rejectsEvidence(v -> v.addProperty("capacity", "invalid"));
        rejectsEvidence(v -> v.remove("observations"));
        rejectsEvidence(v -> v.add("observations", JsonNull.INSTANCE));
        rejectsEvidence(v -> v.getAsJsonArray("observations").add(JsonNull.INSTANCE));
        rejectsEvidence(v -> v.getAsJsonArray("observations").add(v.getAsJsonArray("observations").get(0)));
        rejectsEvidence(v -> v.getAsJsonArray("observations").remove(0));
        rejectsEvidence(v -> v.getAsJsonArray("observations").get(0).getAsJsonObject()
            .addProperty("routeKey", "changed"));
        rejectsEvidence(v -> v.getAsJsonArray("observations").get(6).getAsJsonObject()
            .addProperty("accountRoute", "UNKNOWN"));
        rejectsEvidence(v -> v.remove("transcript"));
        rejectsEvidence(v -> v.add("transcript", JsonNull.INSTANCE));
        rejectsEvidence(v -> step(v.getAsJsonObject("transcript"), 0).addProperty("ticket", -1));
    }

    private static void rejectsEvidence(Consumer<JsonObject> change)
    {
        var fixture = new Fixture(QuestFateOffers.Capacity.THREE, 0);
        var value = QuestFateDrawEvidenceCodec.encode(fixture.draw, fixture.provenance);
        change.accept(value);
        try { QuestFateDrawEvidenceCodec.decode(value, fixture.provenance); fail("Accepted changed evidence"); }
        catch (IllegalArgumentException | IllegalStateException | ArithmeticException expected) { }
    }

    @Test public void legacyAssignmentCodecRefusesToDiscardRetainedDraw()
    {
        var fixture = new Fixture(QuestFateOffers.Capacity.THREE, 0);
        var resolution = fixture.provenance.resolution();
        var window = new QuestFateObservationWindow(new RunLifecycle(resolution.identity(),
            resolution.afterRevision(), RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE),
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 0,
            fixture.provenance.reward().selection(), QuestFateCompletion.QuestState.IN_PROGRESS);
        var allocation = MasterSpinAllocation.restore(resolution, 5);
        var legacy = new QuestFateAssignment(fixture.provenance, allocation, window);
        assertTrue(QuestFateAssignmentCodec.decode(QuestFateAssignmentCodec.encode(legacy), allocation)
            .offerDraw().isEmpty());
        var retained = new QuestFateAssignment(fixture.provenance, allocation, window,
            Optional.of(fixture.draw));
        try
        {
            QuestFateAssignmentCodec.encode(retained);
            fail("Expected refusal to discard original draw evidence");
        }
        catch (IllegalArgumentException expected) { }
    }

    @Test public void versionTwoAssignmentRetainsCompleteEvidenceAndExplicitAbsence()
    {
        for (var capacity : QuestFateOffers.Capacity.values())
        {
            var fixture = new Fixture(capacity, 1);
            var resolution = fixture.provenance.resolution();
            var window = new QuestFateObservationWindow(new RunLifecycle(resolution.identity(),
                resolution.afterRevision(), RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE),
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 0,
                fixture.provenance.reward().selection(), QuestFateCompletion.QuestState.IN_PROGRESS);
            var allocation = MasterSpinAllocation.restore(resolution, 5);
            var legacy = new QuestFateAssignment(fixture.provenance, allocation, window);
            var original = new QuestFateAssignment(fixture.provenance, allocation, window,
                Optional.of(fixture.draw));
            for (var assignment : List.of(legacy, original))
            {
                var value = QuestFateAssignmentCodec.encodeWithDrawEvidence(assignment);
                var parsed = new JsonParser().parse(new GsonBuilder().serializeNulls().create()
                    .toJson(value)).getAsJsonObject();
                var restored = QuestFateAssignmentCodec.decodeWithDrawEvidence(parsed, allocation);
                assertEquals(assignment, restored);
                assertEquals(assignment.hashCode(), restored.hashCode());
                assertEquals(value, QuestFateAssignmentCodec.encodeWithDrawEvidence(restored));
                assertEquals(assignment.offerDraw().isPresent(), restored.offerDraw().isPresent());
                assertEquals(assignment.originalWindow(), restored.originalWindow());
                assertSame(allocation, restored.allocation());
                try { QuestFateAssignmentCodec.decode(parsed, allocation); fail("Legacy reader accepted version 2"); }
                catch (IllegalArgumentException expected) { }
            }
            assertTrue(QuestFateAssignmentCodec.decodeWithDrawEvidence(
                QuestFateAssignmentCodec.encode(legacy), allocation).offerDraw().isEmpty());
            var invalidLegacy = QuestFateAssignmentCodec.encode(legacy);
            invalidLegacy.add("offerDraw", JsonNull.INSTANCE);
            try { QuestFateAssignmentCodec.decodeWithDrawEvidence(invalidLegacy, allocation);
                fail("Version 1 accepted an evidence field"); }
            catch (IllegalArgumentException expected) { }
            var valid = QuestFateAssignmentCodec.encodeWithDrawEvidence(original);
            Consumer<Consumer<JsonObject>> mutate = edit -> {
                var copy = valid.deepCopy();
                edit.accept(copy);
                try { QuestFateAssignmentCodec.decodeWithDrawEvidence(copy, allocation);
                    fail("Accepted malformed assignment evidence"); }
                catch (IllegalArgumentException | IllegalStateException expected) { }
            };
            mutate.accept(v -> v.remove("offerDraw"));
            mutate.accept(v -> v.add("offerDraw", new JsonArray()));
            mutate.accept(v -> v.addProperty("offerDraw", true));
            mutate.accept(v -> v.addProperty("version", 1));
            mutate.accept(v -> v.addProperty("version", 3));
            mutate.accept(v -> v.getAsJsonObject("offerDraw").getAsJsonArray("observations")
                .get(0).getAsJsonObject().addProperty("routeKey", "foreign-route"));
            mutate.accept(v -> step(v.getAsJsonObject("offerDraw").getAsJsonObject("transcript"), 0)
                .addProperty("ticket", -1));
        }
    }

    @Test public void retainsEveryPoolWeightOrderAndExactTicketThroughGson()
    {
        var gson = new Gson();
        for (var capacity : QuestFateOffers.Capacity.values())
        {
            for (int offset : new int[] {0, 1})
            {
                var fixture = new Fixture(capacity, offset);
                String json = gson.toJson(QuestFateOfferDrawCodec.encode(fixture.draw, fixture.provenance));
                var value = new JsonParser().parse(json).getAsJsonObject();
                var restored = QuestFateOfferDrawCodec.decode(value, fixture.evidence, fixture.provenance);
                assertEquals(fixture.draw.draws(), restored.draws());
                assertEquals(fixture.draw.eligibility(), restored.eligibility());
                assertEquals(json, gson.toJson(QuestFateOfferDrawCodec.encode(restored, fixture.provenance)));
                // Distinct tickets within the same selected interval must not be normalized.
                assertEquals(offset, restored.draws().get(0).ticket());
            }
        }
    }

    @Test public void rejectsMalformedTranscriptAndChangedReplay()
    {
        rejects(v -> v.remove("version"));
        rejects(v -> v.addProperty("version", 2));
        rejects(v -> v.addProperty("version", "1"));
        rejects(v -> v.addProperty("version", 1.5));
        rejects(v -> v.addProperty("extra", true));
        rejects(v -> v.remove("draws"));
        rejects(v -> v.add("draws", JsonNull.INSTANCE));
        rejects(v -> v.addProperty("draws", "invalid"));
        rejects(v -> v.getAsJsonArray("draws").remove(0));
        rejects(v -> v.getAsJsonArray("draws").add(JsonNull.INSTANCE));
        rejects(v -> v.getAsJsonArray("draws").set(0, JsonNull.INSTANCE));
        rejects(v -> step(v, 0).addProperty("ticket", -1));
        rejects(v -> step(v, 0).addProperty("ticket", 0.5));
        rejects(v -> step(v, 0).addProperty("ticket", 2147483648L));
        rejects(v -> step(v, 0).addProperty("version", "foreign"));
        rejects(v -> step(v, 0).addProperty("extra", true));
        rejects(v -> step(v, 0).getAsJsonArray("entries").get(0).getAsJsonObject().addProperty("weight", 0));
        rejects(v -> step(v, 1).addProperty("ticket", 4));
        rejects(v -> step(v, 1).getAsJsonArray("entries").get(0).getAsJsonObject().addProperty("weight", 99));
        rejects(v -> step(v, 0).getAsJsonArray("entries").remove(0));
    }

    @Test public void refusesForeignSuppliedContextOnEncodeAndDecode()
    {
        var a = new Fixture(QuestFateOffers.Capacity.THREE, 0);
        var b = new Fixture(QuestFateOffers.Capacity.THREE, 0);
        try { QuestFateOfferDrawCodec.encode(a.draw, b.provenance); fail(); }
        catch (IllegalArgumentException expected) { }
        try { QuestFateOfferDrawCodec.decode(QuestFateOfferDrawCodec.encode(a.draw, a.provenance),
            a.evidence, b.provenance); fail(); }
        catch (IllegalArgumentException expected) { }
    }

    private static JsonObject step(JsonObject value, int index)
    { return value.getAsJsonArray("draws").get(index).getAsJsonObject(); }

    private static void rejects(Consumer<JsonObject> change)
    {
        var fixture = new Fixture(QuestFateOffers.Capacity.THREE, 0);
        var value = QuestFateOfferDrawCodec.encode(fixture.draw, fixture.provenance);
        change.accept(value);
        try { QuestFateOfferDrawCodec.decode(value, fixture.evidence, fixture.provenance); fail("Accepted changed transcript"); }
        catch (IllegalArgumentException | IllegalStateException | ArithmeticException expected) { }
    }
}

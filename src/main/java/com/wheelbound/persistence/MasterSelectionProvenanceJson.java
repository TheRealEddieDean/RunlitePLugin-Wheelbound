package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wheelbound.domain.ActiveWheel;
import com.wheelbound.domain.MasterSelectionProvenance;
import com.wheelbound.domain.MasterSelectionReadiness;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Standalone version-1 frozen selection payload. Restores through domain validation;
 * it does not authenticate stream continuity/evidence or admit a durable selection.
 * Not yet integrated into RunCodec: legacy absence remains absence.
 */
final class MasterSelectionProvenanceJson
{
    private MasterSelectionProvenanceJson() { }

    static JsonObject encode(MasterSelectionProvenance provenance)
    {
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("transactionId", provenance.transactionId().toString());
        value.addProperty("snapshotVersion", provenance.snapshotVersion());
        value.addProperty("catalogVersion", provenance.catalogVersion());
        value.addProperty("streamDescriptor", provenance.streamDescriptor());
        value.addProperty("initialStreamState", provenance.initialStreamState());
        value.add("wheel", InventoryJson.encodeSlices(provenance.readiness().wheel().slices()));
        JsonArray classifications = new JsonArray();
        // Wheel order makes re-encoding deterministic without depending on map iteration.
        for (ActiveWheel.Slice slice : provenance.readiness().wheel().slices())
        {
            MasterSelectionReadiness.Classification classification =
                provenance.readiness().classifications().get(slice.id());
            JsonObject item = new JsonObject();
            item.addProperty("id", slice.id().toString());
            item.addProperty("availability", classification.availability().name());
            item.addProperty("reason", classification.reason());
            classifications.add(item);
        }
        value.add("classifications", classifications);
        JsonArray steps = new JsonArray();
        for (MasterSelectionProvenance.Landing landing : provenance.landings())
        {
            JsonObject item = new JsonObject();
            item.add("draw", RunCodec.encodeDraw(landing.step().draw()));
            item.addProperty("streamStateAfter", landing.step().streamStateAfter());
            steps.add(item);
        }
        value.add("steps", steps);
        return value;
    }

    static MasterSelectionProvenance decode(JsonObject value)
    {
        RunCodec.keys(value, Set.of("version", "transactionId", "snapshotVersion", "catalogVersion",
            "streamDescriptor", "initialStreamState", "wheel", "classifications", "steps"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported Master selection provenance version"); }
        ActiveWheel wheel = new ActiveWheel(InventoryJson.decodeSlices(array(value, "wheel")));
        Map<UUID, MasterSelectionReadiness.Classification> classifications = new HashMap<>();
        for (JsonElement entry : array(value, "classifications"))
        {
            JsonObject item = entry.getAsJsonObject();
            RunCodec.keys(item, Set.of("id", "availability", "reason"));
            if (classifications.put(UUID.fromString(RunCodec.string(item, "id")),
                new MasterSelectionReadiness.Classification(MasterSelectionReadiness.Availability.valueOf(
                    RunCodec.string(item, "availability")), RunCodec.string(item, "reason"))) != null)
            { throw new IllegalArgumentException("Duplicate selection classification"); }
        }
        List<MasterSelectionProvenance.Step> steps = new ArrayList<>();
        for (JsonElement entry : array(value, "steps"))
        {
            JsonObject item = entry.getAsJsonObject();
            RunCodec.keys(item, Set.of("draw", "streamStateAfter"));
            steps.add(new MasterSelectionProvenance.Step(RunCodec.decodeDraw(item.get("draw")),
                RunCodec.string(item, "streamStateAfter")));
        }
        return new MasterSelectionProvenance(UUID.fromString(RunCodec.string(value, "transactionId")),
            RunCodec.string(value, "snapshotVersion"), RunCodec.string(value, "catalogVersion"),
            RunCodec.string(value, "streamDescriptor"), RunCodec.string(value, "initialStreamState"),
            new MasterSelectionReadiness(wheel, classifications), steps);
    }

    private static JsonArray array(JsonObject value, String key)
    {
        JsonElement element = value.get(key);
        if (element == null || !element.isJsonArray())
        { throw new IllegalArgumentException("Expected selection array: " + key); }
        return element.getAsJsonArray();
    }
}

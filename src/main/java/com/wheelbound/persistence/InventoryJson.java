package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wheelbound.domain.ActiveWheel;
import com.wheelbound.domain.SliceInventory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Schema-6 inventory representation. Restores through domain validation, never Gson reflection. */
final class InventoryJson
{
    private InventoryJson() { }

    static JsonObject encode(SliceInventory inventory)
    {
        JsonObject result = new JsonObject();
        result.add("active", encodeSlices(inventory.activeWheel().slices()));
        result.add("stored", encodeSlices(inventory.stored()));
        result.addProperty("satchelTier", inventory.satchelTier().name());
        JsonArray unlocks = new JsonArray();
        inventory.unlockedActivities().stream().sorted().forEach(unlocks::add);
        result.add("unlocks", unlocks);
        JsonArray history = new JsonArray();
        inventory.acquisitionHistory().keySet().stream().sorted().forEach(activity -> {
            JsonObject item = new JsonObject();
            item.addProperty("activity", activity);
            item.addProperty("paidDuplicates", inventory.acquisitionHistory().get(activity).paidDuplicates());
            history.add(item);
        });
        result.add("history", history);
        return result;
    }

    static JsonArray encodeSlices(List<ActiveWheel.Slice> slices)
    {
        JsonArray result = new JsonArray();
        for (ActiveWheel.Slice slice : slices)
        {
            JsonObject item = new JsonObject();
            item.addProperty("id", slice.id().toString());
            item.addProperty("kind", slice.kind().name());
            if (slice.kind() == ActiveWheel.Kind.ORDINARY)
            {
                item.addProperty("activity", slice.activity());
                item.addProperty("enhancement", slice.enhancement().name());
            }
            else { item.addProperty("weightUnits", slice.weightUnits()); }
            result.add(item);
        }
        return result;
    }

    static SliceInventory decode(JsonObject value)
    {
        RunCodec.keys(value, Set.of("active", "stored", "satchelTier", "unlocks", "history"));
        Set<String> unlocks = new HashSet<>();
        for (JsonElement element : value.getAsJsonArray("unlocks"))
        {
            if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()
                || !unlocks.add(element.getAsString()))
            { throw new IllegalArgumentException("Invalid or duplicate unlock"); }
        }
        Map<String, SliceInventory.AcquisitionHistory> history = new HashMap<>();
        for (JsonElement element : value.getAsJsonArray("history"))
        {
            JsonObject item = element.getAsJsonObject();
            RunCodec.keys(item, Set.of("activity", "paidDuplicates"));
            if (history.put(RunCodec.string(item, "activity"), new SliceInventory.AcquisitionHistory(
                RunCodec.integer(item, "paidDuplicates"))) != null)
            { throw new IllegalArgumentException("Duplicate acquisition history"); }
        }
        return new SliceInventory(new ActiveWheel(decodeSlices(value.getAsJsonArray("active"))),
            decodeSlices(value.getAsJsonArray("stored")),
            SliceInventory.SatchelTier.valueOf(RunCodec.string(value, "satchelTier")), unlocks, history);
    }

    static List<ActiveWheel.Slice> decodeSlices(JsonArray values)
    {
        List<ActiveWheel.Slice> result = new ArrayList<>();
        for (JsonElement element : values)
        {
            JsonObject item = element.getAsJsonObject();
            ActiveWheel.Kind kind = ActiveWheel.Kind.valueOf(RunCodec.string(item, "kind"));
            RunCodec.keys(item, kind == ActiveWheel.Kind.ORDINARY
                ? Set.of("id", "kind", "activity", "enhancement") : Set.of("id", "kind", "weightUnits"));
            UUID id = UUID.fromString(RunCodec.string(item, "id"));
            result.add(kind == ActiveWheel.Kind.ORDINARY
                ? ActiveWheel.Slice.ordinary(id, RunCodec.string(item, "activity"),
                    ActiveWheel.Enhancement.valueOf(RunCodec.string(item, "enhancement")))
                : ActiveWheel.Slice.special(id, kind, Math.toIntExact(RunCodec.integer(item, "weightUnits"))));
        }
        return result;
    }
}

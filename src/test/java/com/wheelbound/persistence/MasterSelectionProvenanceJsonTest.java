package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wheelbound.domain.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterSelectionProvenanceJsonTest
{
    static MasterSelectionProvenance fixture(ActiveWheel.Kind special, int emptyCount, boolean resolved)
    {
        List<ActiveWheel.Slice> slices = new ArrayList<>();
        Map<UUID, MasterSelectionReadiness.Classification> classifications = new HashMap<>();
        for (int i = 0; i < 5; i++)
        {
            UUID id = new UUID(0, i + 1);
            slices.add(ActiveWheel.Slice.ordinary(id, "activity-" + i % 3, ActiveWheel.Enhancement.values()[i]));
            classifications.put(id, new MasterSelectionReadiness.Classification(i == 4
                ? MasterSelectionReadiness.Availability.ASSIGNABLE
                : MasterSelectionReadiness.Availability.VERIFIED_EMPTY, "reason-" + i));
        }
        if (special != null)
        {
            UUID id = new UUID(0, 6);
            slices.add(ActiveWheel.Slice.special(id, special, 7));
            classifications.put(id, new MasterSelectionReadiness.Classification(
                MasterSelectionReadiness.Availability.ASSIGNABLE, "supported special"));
        }
        MasterSelectionReadiness readiness = new MasterSelectionReadiness(new ActiveWheel(slices), classifications);
        MasterSelectionProvenance start = new MasterSelectionProvenance(new UUID(0, 99), "snapshot-1",
            "catalog-2", "opaque algorithm/seed", "opaque initial state", readiness, List.of());
        List<MasterSelectionProvenance.Step> steps = new ArrayList<>();
        for (int i = 0; i < emptyCount; i++)
        { steps.add(new MasterSelectionProvenance.Step(new DrawCommitment(start.pool(), 0), "state-" + i)); }
        if (resolved)
        { steps.add(new MasterSelectionProvenance.Step(new DrawCommitment(start.pool(),
            start.pool().totalWeight() - 1), "final state")); }
        return new MasterSelectionProvenance(start.transactionId(), start.snapshotVersion(), start.catalogVersion(),
            start.streamDescriptor(), start.initialStreamState(), readiness, steps);
    }

    @Test public void roundTripPreservesEmptyPrefixesOrdinaryAndBothSpecialLandings()
    {
        for (ActiveWheel.Kind special : new ActiveWheel.Kind[] {null, ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE})
        for (int count : new int[] {0, 1, 513})
        for (boolean resolved : new boolean[] {false, true})
        {
            MasterSelectionProvenance original = fixture(special, count, resolved);
            String bytes = MasterSelectionProvenanceJson.encode(original).toString();
            JsonObject json = new JsonParser().parse(bytes).getAsJsonObject();
            MasterSelectionProvenance restored = MasterSelectionProvenanceJson.decode(json);
            assertEquals(bytes, MasterSelectionProvenanceJson.encode(restored).toString());
            assertEquals(original.pool(), restored.pool());
            assertEquals(original.currentStreamState(), restored.currentStreamState());
            assertEquals(original.hasValidLanding(), restored.hasValidLanding());
            for (int i = 0; i < original.drawCount(); i++)
            {
                assertEquals(original.landings().get(i).step().draw(), restored.landings().get(i).step().draw());
                assertEquals(original.landings().get(i).kind(), restored.landings().get(i).kind());
                assertEquals(original.landings().get(i).reason(), restored.landings().get(i).reason());
            }
            json.addProperty("streamDescriptor", "changed input after decode");
            assertEquals(bytes, MasterSelectionProvenanceJson.encode(restored).toString());
        }
    }

    private void rejects(Consumer<JsonObject> corrupt)
    {
        JsonObject json = MasterSelectionProvenanceJson.encode(fixture(null, 1, true));
        corrupt.accept(json);
        String before = json.toString();
        assertThrows(RuntimeException.class, () -> MasterSelectionProvenanceJson.decode(json));
        assertEquals(before, json.toString());
    }

    @Test public void malformedOrInconsistentPayloadCannotRestore()
    {
        rejects(j -> j.addProperty("version", 2));
        rejects(j -> j.addProperty("version", 1.5));
        rejects(j -> j.remove("initialStreamState"));
        rejects(j -> j.addProperty("extra", true));
        rejects(j -> j.addProperty("transactionId", "invalid"));
        rejects(j -> j.addProperty("snapshotVersion", "changed"));
        rejects(j -> j.addProperty("streamDescriptor", " "));
        rejects(j -> j.addProperty("steps", "not an array"));
        rejects(j -> j.getAsJsonArray("classifications").remove(0));
        rejects(j -> j.getAsJsonArray("classifications").add(j.getAsJsonArray("classifications").get(0).deepCopy()));
        rejects(j -> j.getAsJsonArray("classifications").get(0).getAsJsonObject().addProperty("availability", "UNKNOWN"));
        rejects(j -> j.getAsJsonArray("wheel").get(0).getAsJsonObject().addProperty("enhancement", "FOURTH"));
        rejects(j -> j.getAsJsonArray("steps").add(j.getAsJsonArray("steps").get(0).deepCopy()));
        rejects(j -> j.getAsJsonArray("steps").get(0).getAsJsonObject().getAsJsonObject("draw").addProperty("ticket", -1));
    }
}

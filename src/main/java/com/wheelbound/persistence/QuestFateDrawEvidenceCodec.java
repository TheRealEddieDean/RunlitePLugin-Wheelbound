package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wheelbound.domain.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/** Lossless supplied complete-pool and transcript envelope. The explicit provenance
 * supplies the original context; restoration does not authenticate catalog or client
 * observations, enable assignment writers, or establish a generation policy. */
final class QuestFateDrawEvidenceCodec
{
    private QuestFateDrawEvidenceCodec() { }

    static JsonObject encode(QuestFateOfferDraw draw, QuestFateProvenance provenance)
    {
        draw.requireProvenance(provenance);
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("capacity", draw.evidence().capacity().name());
        JsonArray observations = new JsonArray();
        for (var observation : draw.evidence().observations())
        {
            JsonObject item = new JsonObject();
            item.addProperty("questId", observation.questId());
            item.addProperty("routeKey", observation.routeKey());
            item.addProperty("routeVersion", observation.routeVersion());
            item.addProperty("questState", observation.questState().name());
            item.addProperty("prerequisites", observation.prerequisites().name());
            item.addProperty("accountRoute", observation.accountRoute().name());
            observations.add(item);
        }
        value.add("observations", observations);
        value.add("transcript", QuestFateOfferDrawCodec.encode(draw, provenance));
        return value;
    }

    static QuestFateOfferDraw decode(JsonObject value, QuestFateProvenance provenance)
    {
        RunCodec.keys(value, Set.of("version", "capacity", "observations", "transcript"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported Questing draw evidence version"); }
        if (!value.get("observations").isJsonArray() || !value.get("transcript").isJsonObject())
        { throw new IllegalArgumentException("Expected complete observations and transcript"); }
        var observations = new ArrayList<QuestFateEligibility.Observation>();
        var required = new HashSet<String>();
        for (var element : value.getAsJsonArray("observations"))
        {
            if (!element.isJsonObject())
            { throw new IllegalArgumentException("Expected quest observation object"); }
            var item = element.getAsJsonObject();
            RunCodec.keys(item, Set.of("questId", "routeKey", "routeVersion", "questState",
                "prerequisites", "accountRoute"));
            String id = RunCodec.string(item, "questId");
            if (!required.add(id))
            { throw new IllegalArgumentException("Repeated complete-pool quest"); }
            observations.add(new QuestFateEligibility.Observation(id,
                RunCodec.string(item, "routeKey"), RunCodec.string(item, "routeVersion"),
                QuestFateEligibility.Status.valueOf(RunCodec.string(item, "questState")),
                QuestFateEligibility.Status.valueOf(RunCodec.string(item, "prerequisites")),
                QuestFateEligibility.Status.valueOf(RunCodec.string(item, "accountRoute"))));
        }
        var resolution = provenance.resolution();
        var master = resolution.resolved();
        // Membership is the retained supplied declaration, not a reviewed catalog.
        var evidence = new QuestFatePoolEvidence(resolution.identity(),
            resolution.creation().beforeRevision(), master.snapshotVersion(), master.catalogVersion(),
            provenance.activityKey(), QuestFateOffers.Capacity.valueOf(RunCodec.string(value, "capacity")),
            required, observations);
        return QuestFateOfferDrawCodec.decode(value.getAsJsonObject("transcript"), evidence, provenance);
    }
}

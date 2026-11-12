package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wheelbound.domain.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

/**
 * Versioned lossless supplied draw transcript. Restoration requires the original
 * complete pool evidence and provenance; neither is inferred from the offers.
 * This nested payload does not enable run writers or authenticate supplied policy.
 */
final class QuestFateOfferDrawCodec
{
    private QuestFateOfferDrawCodec() { }

    static JsonObject encode(QuestFateOfferDraw draw, QuestFateProvenance provenance)
    {
        Objects.requireNonNull(draw, "draw").requireProvenance(provenance);
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        JsonArray draws = new JsonArray();
        for (var step : draw.draws()) { draws.add(RunCodec.encodeDraw(step)); }
        value.add("draws", draws);
        return value;
    }

    static QuestFateOfferDraw decode(JsonObject value, QuestFatePoolEvidence evidence,
        QuestFateProvenance provenance)
    {
        Objects.requireNonNull(evidence, "evidence");
        Objects.requireNonNull(provenance, "provenance");
        RunCodec.keys(value, Set.of("version", "draws"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported Questing draw version"); }
        if (!value.get("draws").isJsonArray()
            || value.getAsJsonArray("draws").size() != evidence.capacity().count())
        { throw new IllegalArgumentException("Expected complete Questing draw array"); }
        var draws = new ArrayList<DrawCommitment>();
        for (var element : value.getAsJsonArray("draws"))
        {
            if (!element.isJsonObject())
            { throw new IllegalArgumentException("Expected Questing draw object"); }
            draws.add(RunCodec.decodeDraw(element));
        }
        var resolution = provenance.resolution();
        var master = resolution.resolved();
        var slice = master.readiness().wheel().slices().stream()
            .filter(candidate -> candidate.id().equals(provenance.sliceId())).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Missing Questing draw slice"));
        var restored = new QuestFateOfferDraw(evidence, resolution.identity(),
            resolution.creation().beforeRevision(), master.snapshotVersion(),
            master.catalogVersion(), slice, draws);
        restored.requireProvenance(provenance);
        return restored;
    }
}

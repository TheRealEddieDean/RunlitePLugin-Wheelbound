package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.wheelbound.domain.MasterSpinAllocation;
import com.wheelbound.domain.QuestFateProvenance;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Nested supplied provenance and original accounting, version 1. The enclosing
 * aggregate supplies its retained allocation, including the complete resolution.
 * Equality with supplied context is not authentication or durable admission.
 * No spin is debited and no run schema or public writer is enabled here.
 */
final class QuestFateProvenanceCodec
{
    private QuestFateProvenanceCodec() { }

    static JsonObject encode(QuestFateProvenance provenance, MasterSpinAllocation allocation)
    {
        Objects.requireNonNull(provenance, "provenance").requireAllocation(allocation);
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.add("resolution", MasterSelectionResolutionReceiptJson.encode(provenance.resolution()));
        value.addProperty("spinsBefore", allocation.spinsBefore());
        value.addProperty("sliceId", provenance.sliceId().toString());
        value.addProperty("activityKey", provenance.activityKey());
        value.addProperty("streamDescriptor", provenance.streamDescriptor());
        value.addProperty("initialStreamState", provenance.initialStreamState());
        value.addProperty("finalStreamState", provenance.finalStreamState());
        value.add("reward", QuestFateRewardCodec.encode(provenance.reward()));
        return value;
    }

    static QuestFateProvenance decode(JsonObject value, MasterSpinAllocation retained)
    {
        Objects.requireNonNull(retained, "retained");
        RunCodec.keys(value, Set.of("version", "resolution", "spinsBefore", "sliceId",
            "activityKey", "streamDescriptor", "initialStreamState", "finalStreamState", "reward"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported Questing provenance version"); }
        if (!value.get("resolution").isJsonObject() || !value.get("reward").isJsonObject())
        { throw new IllegalArgumentException("Expected Questing resolution and reward objects"); }
        var resolution = MasterSelectionResolutionReceiptJson.decode(
            value.getAsJsonObject("resolution"), retained.resolution().identity());
        var allocation = MasterSpinAllocation.restore(resolution, RunCodec.integer(value, "spinsBefore"));
        if (!resolution.equals(retained.resolution()) || allocation.spinsBefore() != retained.spinsBefore())
        { throw new IllegalArgumentException("Changed retained Questing allocation"); }
        String sliceText = RunCodec.string(value, "sliceId");
        UUID slice = UUID.fromString(sliceText);
        if (!slice.toString().equals(sliceText))
        { throw new IllegalArgumentException("Expected canonical Questing slice UUID"); }
        var provenance = new QuestFateProvenance(resolution, slice,
            RunCodec.string(value, "activityKey"), RunCodec.string(value, "streamDescriptor"),
            RunCodec.string(value, "initialStreamState"), RunCodec.string(value, "finalStreamState"),
            QuestFateRewardCodec.decode(value.getAsJsonObject("reward")));
        provenance.requireAllocation(retained);
        return provenance;
    }
}

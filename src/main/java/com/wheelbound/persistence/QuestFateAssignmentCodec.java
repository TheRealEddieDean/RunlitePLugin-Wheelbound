package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import com.wheelbound.domain.MasterSpinAllocation;
import com.wheelbound.domain.QuestFateAssignment;
import java.util.Objects;
import java.util.Set;
import java.util.Optional;

/**
 * Versioned nested original commitment, independent of the current run lifecycle.
 * Supplied retained accounting binds the complete Master transcript; it is not
 * authenticated catalog authority. No run schema, writer or admission is enabled.
 */
final class QuestFateAssignmentCodec
{
    private QuestFateAssignmentCodec() { }

    static JsonObject encode(QuestFateAssignment assignment)
    {
        Objects.requireNonNull(assignment, "assignment");
        if (assignment.offerDraw().isPresent())
        { throw new IllegalArgumentException("Assignment version 1 cannot retain original offer draw evidence"); }
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.add("provenance", QuestFateProvenanceCodec.encode(
            assignment.provenance(), assignment.allocation()));
        value.add("originalWindow", QuestFateObservationWindowCodec.encode(
            assignment.originalWindow(), assignment.provenance().reward().selection()));
        return value;
    }

    static QuestFateAssignment decode(JsonObject value, MasterSpinAllocation retained)
    {
        return decodeVersion(value, retained, false);
    }

    /** Explicit opt-in envelope; existing run schemas continue using version 1. */
    static JsonObject encodeWithDrawEvidence(QuestFateAssignment assignment)
    {
        Objects.requireNonNull(assignment, "assignment");
        JsonObject value = new JsonObject();
        value.addProperty("version", 2);
        value.add("provenance", QuestFateProvenanceCodec.encode(
            assignment.provenance(), assignment.allocation()));
        value.add("originalWindow", QuestFateObservationWindowCodec.encode(
            assignment.originalWindow(), assignment.provenance().reward().selection()));
        value.add("offerDraw", assignment.offerDraw()
            .map(draw -> (com.google.gson.JsonElement) QuestFateDrawEvidenceCodec.encode(
                draw, assignment.provenance())).orElse(JsonNull.INSTANCE));
        return value;
    }

    /** Version 1 means unavailable original evidence; version 2 requires explicit presence or null. */
    static QuestFateAssignment decodeWithDrawEvidence(JsonObject value, MasterSpinAllocation retained)
    {
        return decodeVersion(value, retained, true);
    }

    private static QuestFateAssignment decodeVersion(JsonObject value,
        MasterSpinAllocation retained, boolean allowDrawEvidence)
    {
        Objects.requireNonNull(retained, "retained");
        int version = RunCodec.integer(value, "version");
        if (version != 1 && !(allowDrawEvidence && version == 2))
        { throw new IllegalArgumentException("Unsupported Questing assignment version"); }
        RunCodec.keys(value, version == 1 ? Set.of("version", "provenance", "originalWindow")
            : Set.of("version", "provenance", "originalWindow", "offerDraw"));
        if (!value.get("provenance").isJsonObject() || !value.get("originalWindow").isJsonObject())
        { throw new IllegalArgumentException("Expected Questing provenance and original window objects"); }
        var provenance = QuestFateProvenanceCodec.decode(value.getAsJsonObject("provenance"), retained);
        var window = QuestFateObservationWindowCodec.decode(value.getAsJsonObject("originalWindow"),
            provenance.reward().selection());
        var draw = Optional.<com.wheelbound.domain.QuestFateOfferDraw>empty();
        if (version == 2 && !value.get("offerDraw").isJsonNull())
        {
            if (!value.get("offerDraw").isJsonObject())
            { throw new IllegalArgumentException("Expected original offer draw object or null"); }
            draw = Optional.of(QuestFateDrawEvidenceCodec.decode(value.getAsJsonObject("offerDraw"), provenance));
        }
        return new QuestFateAssignment(provenance, retained, window, draw);
    }
}

package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.wheelbound.domain.MasterSpinAllocation;
import com.wheelbound.domain.QuestFateAssignment;
import java.util.Objects;
import java.util.Set;

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
        Objects.requireNonNull(retained, "retained");
        RunCodec.keys(value, Set.of("version", "provenance", "originalWindow"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported Questing assignment version"); }
        if (!value.get("provenance").isJsonObject() || !value.get("originalWindow").isJsonObject())
        { throw new IllegalArgumentException("Expected Questing provenance and original window objects"); }
        var provenance = QuestFateProvenanceCodec.decode(value.getAsJsonObject("provenance"), retained);
        var window = QuestFateObservationWindowCodec.decode(value.getAsJsonObject("originalWindow"),
            provenance.reward().selection());
        return new QuestFateAssignment(provenance, retained, window);
    }
}

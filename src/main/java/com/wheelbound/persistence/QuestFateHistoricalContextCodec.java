package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.wheelbound.domain.MasterSelectionResolutions;
import com.wheelbound.domain.MasterSpinState;
import com.wheelbound.domain.QuestFateCurrentAssignment;
import com.wheelbound.domain.RunLifecycle;
import java.util.Objects;
import java.util.Set;

/** Nested supplied historical binding; no evidence authentication or run-schema admission. */
final class QuestFateHistoricalContextCodec
{
    private QuestFateHistoricalContextCodec() { }

    static JsonObject encode(QuestFateCurrentAssignment context)
    {
        Objects.requireNonNull(context, "context");
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.add("assignment", QuestFateAssignmentCodec.encode(context.assignment()));
        value.addProperty("revision", context.lifecycle().revision());
        value.addProperty("state", context.lifecycle().state().name());
        value.addProperty("obligation", context.lifecycle().obligation().name());
        return value;
    }

    /** Inputs must be retained historical accounting/history, never inferred from today's head. */
    static QuestFateCurrentAssignment decode(JsonObject value,
        MasterSelectionResolutions history, MasterSpinState spins)
    {
        Objects.requireNonNull(history, "history");
        Objects.requireNonNull(spins, "spins");
        RunCodec.keys(value, Set.of("version", "assignment", "revision", "state", "obligation"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported historical Questing context version"); }
        if (!value.get("assignment").isJsonObject())
        { throw new IllegalArgumentException("Expected historical Questing assignment object"); }
        var allocation = spins.reservation().orElseThrow(() ->
            new IllegalArgumentException("Missing historical Questing reservation"));
        var assignment = QuestFateAssignmentCodec.decode(value.getAsJsonObject("assignment"), allocation);
        var lifecycle = new RunLifecycle(assignment.identity(), RunCodec.integer(value, "revision"),
            RunLifecycle.State.valueOf(RunCodec.string(value, "state")),
            RunLifecycle.Obligation.valueOf(RunCodec.string(value, "obligation")));
        return QuestFateCurrentAssignment.restore(assignment, history, spins, lifecycle);
    }
}

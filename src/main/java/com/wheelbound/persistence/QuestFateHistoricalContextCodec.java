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
        return encodeVersion(context, false);
    }

    /** Opt-in only: existing history/schema callers retain their strict version-1 contract. */
    static JsonObject encodeWithDrawEvidence(QuestFateCurrentAssignment context)
    {
        return encodeVersion(context, true);
    }

    private static JsonObject encodeVersion(QuestFateCurrentAssignment context, boolean withDrawEvidence)
    {
        Objects.requireNonNull(context, "context");
        JsonObject value = new JsonObject();
        value.addProperty("version", withDrawEvidence ? 2 : 1);
        value.add("assignment", withDrawEvidence
            ? QuestFateAssignmentCodec.encodeWithDrawEvidence(context.assignment())
            : QuestFateAssignmentCodec.encode(context.assignment()));
        value.addProperty("revision", context.lifecycle().revision());
        value.addProperty("state", context.lifecycle().state().name());
        value.addProperty("obligation", context.lifecycle().obligation().name());
        return value;
    }

    /** Inputs must be retained historical accounting/history, never inferred from today's head. */
    static QuestFateCurrentAssignment decode(JsonObject value,
        MasterSelectionResolutions history, MasterSpinState spins)
    {
        return decodeVersion(value, history, spins, false);
    }

    /** Legacy version 1 retains unavailable evidence; version 2 composes the draw-aware assignment. */
    static QuestFateCurrentAssignment decodeWithDrawEvidence(JsonObject value,
        MasterSelectionResolutions history, MasterSpinState spins)
    {
        return decodeVersion(value, history, spins, true);
    }

    private static QuestFateCurrentAssignment decodeVersion(JsonObject value,
        MasterSelectionResolutions history, MasterSpinState spins, boolean allowDrawEvidence)
    {
        Objects.requireNonNull(history, "history");
        Objects.requireNonNull(spins, "spins");
        RunCodec.keys(value, Set.of("version", "assignment", "revision", "state", "obligation"));
        long version = RunCodec.integer(value, "version");
        if (version != 1 && !(allowDrawEvidence && version == 2))
        { throw new IllegalArgumentException("Unsupported historical Questing context version"); }
        if (!value.get("assignment").isJsonObject())
        { throw new IllegalArgumentException("Expected historical Questing assignment object"); }
        var allocation = spins.reservation().orElseThrow(() ->
            new IllegalArgumentException("Missing historical Questing reservation"));
        var assignment = version == 2
            ? QuestFateAssignmentCodec.decodeWithDrawEvidence(value.getAsJsonObject("assignment"), allocation)
            : QuestFateAssignmentCodec.decode(value.getAsJsonObject("assignment"), allocation);
        var lifecycle = new RunLifecycle(assignment.identity(), RunCodec.integer(value, "revision"),
            RunLifecycle.State.valueOf(RunCodec.string(value, "state")),
            RunLifecycle.Obligation.valueOf(RunCodec.string(value, "obligation")));
        return QuestFateCurrentAssignment.restore(assignment, history, spins, lifecycle);
    }
}

package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.wheelbound.domain.QuestFateCompletion;
import com.wheelbound.domain.QuestFateObservationWindow;
import com.wheelbound.domain.QuestFateSelection;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.RunLifecycle;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Nested version-1 observation context. The enclosing commitment stores selection
 * once and supplies that exact selection here. This codec neither authenticates
 * that context nor admits evidence. Restore the original ACTIVE baseline even
 * when the enclosing run is now paused; never manufacture a refreshed window.
 */
final class QuestFateObservationWindowCodec
{
    private QuestFateObservationWindowCodec() { }

    static JsonObject encode(QuestFateObservationWindow window, QuestFateSelection selection)
    {
        Objects.requireNonNull(window, "window").selection().requireSameSelection(selection);
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("accountKey", window.identity().accountKey());
        value.addProperty("runId", window.identity().runId().toString());
        value.addProperty("baselineRevision", window.baselineRevision());
        value.addProperty("assignmentId", window.assignmentId().toString());
        value.addProperty("sessionId", window.sessionId().toString());
        value.addProperty("windowId", window.windowId().toString());
        value.addProperty("baselinePosition", window.baselinePosition());
        value.addProperty("baseline", window.baseline().name());
        return value;
    }

    static QuestFateObservationWindow decode(JsonObject value, QuestFateSelection selection)
    {
        Objects.requireNonNull(selection, "selection");
        RunCodec.keys(value, Set.of("version", "accountKey", "runId", "baselineRevision",
            "assignmentId", "sessionId", "windowId", "baselinePosition", "baseline"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported Questing observation window version"); }
        RunIdentity identity = new RunIdentity(RunCodec.string(value, "accountKey"), uuid(value, "runId"));
        RunLifecycle baselineContext = new RunLifecycle(identity,
            RunCodec.integer(value, "baselineRevision"), RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        return new QuestFateObservationWindow(baselineContext, uuid(value, "assignmentId"),
            uuid(value, "sessionId"), uuid(value, "windowId"),
            RunCodec.integer(value, "baselinePosition"), selection,
            QuestFateCompletion.QuestState.valueOf(RunCodec.string(value, "baseline")));
    }

    private static UUID uuid(JsonObject value, String key)
    {
        String text = RunCodec.string(value, key);
        UUID result = UUID.fromString(text);
        if (!result.toString().equals(text))
        { throw new IllegalArgumentException("Expected canonical UUID: " + key); }
        return result;
    }
}

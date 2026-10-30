package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.wheelbound.domain.GrandActivationHistory;
import com.wheelbound.domain.GrandActivationReceipt;
import com.wheelbound.domain.GrandCurrentAttempt;
import com.wheelbound.domain.GrandFateChecklist;
import com.wheelbound.domain.RunLifecycle;
import java.util.Set;

/** Standalone structural payload; does not admit a session or commit an activation. */
final class GrandCurrentAttemptJson
{
    private GrandCurrentAttemptJson() { }

    static JsonObject encode(GrandCurrentAttempt attempt, GrandActivationHistory history,
        RunLifecycle current, GrandFateChecklist checklist)
    {
        GrandCurrentAttempt validated = GrandCurrentAttempt.restore(attempt.activation().commandId(),
            history, current, checklist);
        if (!validated.activation().equals(attempt.activation())
            || !attempt.lifecycle().identity().equals(current.identity())
            || attempt.lifecycle().revision() != current.revision()
            || attempt.lifecycle().state() != current.state()
            || attempt.lifecycle().obligation() != current.obligation())
        { throw new IllegalArgumentException("Current Grand attempt snapshot differs from context"); }
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.add("activation", GrandActivationReceiptJson.encode(attempt.activation(), current.revision()));
        return value;
    }

    static GrandCurrentAttempt decode(JsonObject value, GrandActivationHistory history,
        RunLifecycle current, GrandFateChecklist checklist)
    {
        RunCodec.keys(value, Set.of("version", "activation"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported current Grand attempt version"); }
        GrandActivationReceipt activation = GrandActivationReceiptJson.decode(
            value.getAsJsonObject("activation"), history.seal(), current.revision());
        GrandCurrentAttempt restored = GrandCurrentAttempt.restore(activation.commandId(), history, current, checklist);
        if (!activation.equals(restored.activation()))
        { throw new IllegalArgumentException("Current Grand activation differs from history"); }
        return restored;
    }
}

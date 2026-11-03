package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.wheelbound.domain.*;
import java.util.Set;

/** Explicit structural context only; no task selection, storage or session admission. */
final class MasterCurrentSpecialObligationJson
{
    private MasterCurrentSpecialObligationJson() { }

    static JsonObject encode(MasterCurrentSpecialObligation special,
        MasterSelectionResolutions history, MasterSpinState spins, RunLifecycle current)
    {
        MasterCurrentSpecialObligation.restore(special.obligation(), history, spins, current);
        var snapshot = special.lifecycle();
        if (!snapshot.identity().equals(current.identity()) || snapshot.revision() != current.revision()
            || snapshot.state() != current.state() || snapshot.obligation() != current.obligation()
            || special.spins().revision() != spins.revision()
            || special.spins().allotted() != spins.allotted())
        { throw new IllegalArgumentException("Current special snapshot differs from context"); }
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.add("resolution", MasterSelectionResolutionReceiptJson.encode(special.obligation().resolution()));
        return value;
    }

    static MasterCurrentSpecialObligation decode(JsonObject value,
        MasterSelectionResolutions history, MasterSpinState spins, RunLifecycle current)
    {
        RunCodec.keys(value, Set.of("version", "resolution"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported current special version"); }
        if (!value.get("resolution").isJsonObject())
        { throw new IllegalArgumentException("Expected current special resolution object"); }
        var obligation = new MasterSpecialObligation(MasterSelectionResolutionReceiptJson.decode(
            value.getAsJsonObject("resolution"), current.identity()));
        return MasterCurrentSpecialObligation.restore(obligation, history, spins, current);
    }
}

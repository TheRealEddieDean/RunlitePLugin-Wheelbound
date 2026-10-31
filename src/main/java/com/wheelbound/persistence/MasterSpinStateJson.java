package com.wheelbound.persistence;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.wheelbound.domain.MasterSpinAllocation;
import com.wheelbound.domain.MasterSpinState;
import com.wheelbound.domain.RunIdentity;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Versioned structural payload retained by RunCodec schema 18.
 * Does not establish retained terminal membership, storage authority or settlement.
 * Legacy absence belongs to the enclosing schema and must never become zero here.
 */
final class MasterSpinStateJson
{
    private MasterSpinStateJson() { }

    static JsonObject encode(MasterSpinState state, long currentRevision)
    {
        validateRevision(state, currentRevision);
        JsonObject value = new JsonObject();
        value.addProperty("version", state.version());
        value.addProperty("accountKey", state.identity().accountKey());
        value.addProperty("runId", state.identity().runId().toString());
        value.addProperty("revision", state.revision());
        value.addProperty("allotted", state.allotted());
        if (state.reservation().isPresent())
        {
            MasterSpinAllocation allocation = state.reservation().get();
            JsonObject reservation = new JsonObject();
            reservation.addProperty("version", 1);
            reservation.addProperty("spinsBefore", allocation.spinsBefore());
            reservation.add("resolution", MasterSelectionResolutionReceiptJson.encode(allocation.resolution()));
            value.add("reservation", reservation);
        }
        else { value.add("reservation", JsonNull.INSTANCE); }
        return value;
    }

    static MasterSpinState decode(JsonObject value, RunIdentity expected, long currentRevision)
    {
        RunCodec.keys(value, Set.of("version", "accountKey", "runId", "revision", "allotted", "reservation"));
        long version = RunCodec.integer(value, "version");
        if (version != MasterSpinState.VERSION)
        { throw new IllegalArgumentException("Unsupported spin state version"); }
        RunIdentity identity = new RunIdentity(RunCodec.string(value, "accountKey"),
            UUID.fromString(RunCodec.string(value, "runId")));
        if (!identity.equals(expected))
        { throw new IllegalArgumentException("Foreign spin state"); }
        Optional<MasterSpinAllocation> allocation = Optional.empty();
        if (!value.get("reservation").isJsonNull())
        {
            if (!value.get("reservation").isJsonObject())
            { throw new IllegalArgumentException("Expected spin reservation object"); }
            JsonObject reservation = value.getAsJsonObject("reservation");
            RunCodec.keys(reservation, Set.of("version", "spinsBefore", "resolution"));
            if (RunCodec.integer(reservation, "version") != 1)
            { throw new IllegalArgumentException("Unsupported spin allocation version"); }
            if (!reservation.get("resolution").isJsonObject())
            { throw new IllegalArgumentException("Expected terminal resolution object"); }
            allocation = Optional.of(MasterSpinAllocation.restore(
                MasterSelectionResolutionReceiptJson.decode(reservation.getAsJsonObject("resolution"), identity),
                RunCodec.integer(reservation, "spinsBefore")));
        }
        MasterSpinState state = new MasterSpinState(MasterSpinState.VERSION, identity,
            RunCodec.integer(value, "revision"), RunCodec.integer(value, "allotted"), allocation);
        validateRevision(state, currentRevision);
        return state;
    }

    private static void validateRevision(MasterSpinState state, long currentRevision)
    {
        if (currentRevision < state.revision())
        { throw new IllegalArgumentException("Spin state is newer than aggregate"); }
    }
}

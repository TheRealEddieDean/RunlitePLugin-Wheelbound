package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.wheelbound.domain.RunIdentity;
import java.util.Set;

/**
 * Versioned original Taint response payload. Structural decoding grants no replay
 * authority: retained history, reservation and current obligation must still bind.
 * Not yet wired into RunCodec; its loss-prevention refusal remains required.
 */
final class MasterTaintTerminalResponseJson
{
    private MasterTaintTerminalResponseJson() { }

    static JsonObject encode(MasterTerminalOperationProposal response)
    {
        // Reuse the typed envelope's supported-outcome gate, including ordinary refusal.
        RunReceipt.masterTaintTerminal(response);
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("spinsBefore", response.allocation().spinsBefore());
        value.add("resolution", MasterSelectionResolutionReceiptJson.encode(response.resolution()));
        return value;
    }

    static MasterTerminalOperationProposal decode(JsonObject value, RunIdentity expected)
    {
        RunCodec.keys(value, Set.of("version", "spinsBefore", "resolution"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported Taint terminal response version"); }
        if (!value.get("resolution").isJsonObject())
        { throw new IllegalArgumentException("Expected Taint terminal resolution object"); }
        var response = MasterTerminalOperationProposal.restore(
            MasterSelectionResolutionReceiptJson.decode(value.getAsJsonObject("resolution"), expected),
            RunCodec.integer(value, "spinsBefore"));
        RunReceipt.masterTaintTerminal(response);
        return response;
    }
}

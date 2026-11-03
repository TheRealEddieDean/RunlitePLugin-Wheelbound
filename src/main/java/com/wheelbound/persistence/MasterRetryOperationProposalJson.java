package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.wheelbound.domain.RunIdentity;
import java.util.Set;
import java.util.UUID;

/** Standalone versioned retry payload. Requires the original head to decode;
 * not integrated with RunCodec or historical replay admission.
 */
final class MasterRetryOperationProposalJson
{
    private MasterRetryOperationProposalJson() { }

    static JsonObject encode(MasterRetryOperationProposal proposal)
    {
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.addProperty("accountKey", proposal.identity().accountKey());
        value.addProperty("runId", proposal.identity().runId().toString());
        value.addProperty("commandId", proposal.commandId().toString());
        value.addProperty("beforeRevision", proposal.beforeRevision());
        value.addProperty("afterRevision", proposal.afterRevision());
        value.addProperty("beforeDrawCount", proposal.before().drawCount());
        value.addProperty("status", proposal.status().name());
        value.add("after", MasterSelectionProvenanceJson.encode(proposal.after()));
        return value;
    }

    static MasterRetryOperationProposal decode(JsonObject value, RunDocument original)
    {
        RunCodec.keys(value, Set.of("version", "accountKey", "runId", "commandId",
            "beforeRevision", "afterRevision", "beforeDrawCount", "status", "after"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported retry payload version"); }
        if (!value.get("after").isJsonObject())
        { throw new IllegalArgumentException("Expected retry transcript object"); }
        long count = RunCodec.integer(value, "beforeDrawCount");
        if (count < 0 || count > Integer.MAX_VALUE)
        { throw new IllegalArgumentException("Invalid original retry draw count"); }
        var proposal = MasterRetryOperationProposal.restore(original,
            new RunIdentity(RunCodec.string(value, "accountKey"),
                UUID.fromString(RunCodec.string(value, "runId"))),
            UUID.fromString(RunCodec.string(value, "commandId")),
            RunCodec.integer(value, "beforeRevision"), (int) count,
            MasterSelectionProvenanceJson.decode(value.getAsJsonObject("after")),
            MasterRetryContinuation.Status.valueOf(RunCodec.string(value, "status")));
        if (proposal.afterRevision() != RunCodec.integer(value, "afterRevision"))
        { throw new IllegalArgumentException("Inconsistent retry revision"); }
        return proposal;
    }
}

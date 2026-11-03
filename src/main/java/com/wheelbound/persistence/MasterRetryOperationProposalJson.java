package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.MasterSelectionCreationReceipt;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** Standalone versioned retry payload. Historical decoding requires its retained
 * creation; aggregate binding is still required before replay admission.
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
        return decode(value, Objects.requireNonNull(original, "original"), null);
    }

    /** Decode historical response fields without treating a later head as the original.
     * Call validateRetained on the assembled aggregate before admitting replay.
     */
    static MasterRetryOperationProposal decodeResponse(JsonObject value,
        MasterSelectionCreationReceipt creation)
    {
        return decode(value, null, Objects.requireNonNull(creation, "creation"));
    }

    private static MasterRetryOperationProposal decode(JsonObject value, RunDocument original,
        MasterSelectionCreationReceipt creation)
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
        var identity = new RunIdentity(RunCodec.string(value, "accountKey"),
            UUID.fromString(RunCodec.string(value, "runId")));
        var commandId = UUID.fromString(RunCodec.string(value, "commandId"));
        long revision = RunCodec.integer(value, "beforeRevision");
        var after = MasterSelectionProvenanceJson.decode(value.getAsJsonObject("after"));
        var status = MasterRetryContinuation.Status.valueOf(RunCodec.string(value, "status"));
        var proposal = original != null
            ? MasterRetryOperationProposal.restore(original, identity, commandId, revision, (int) count, after, status)
            : MasterRetryOperationProposal.restoreResponse(creation, identity, commandId, revision, (int) count, after, status);
        if (proposal.afterRevision() != RunCodec.integer(value, "afterRevision"))
        { throw new IllegalArgumentException("Inconsistent retry revision"); }
        return proposal;
    }
}

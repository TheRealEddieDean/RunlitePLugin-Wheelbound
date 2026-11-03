package com.wheelbound.persistence;

import com.google.gson.JsonParser;
import com.wheelbound.domain.MasterSelectionReadiness;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterRetryOperationProposalJsonTest
{
    @Test public void roundTripAndStrictOriginalHeadBinding()
    {
        for (int count : new int[] {0, 1, 513})
        {
            var head = MasterRetryContinuationChecks.fixture(count);
            for (var availability : MasterSelectionReadiness.Availability.values())
            {
                var proposal = MasterRetryOperationProposal.prepare(head, new UUID(95, count),
                    MasterRetryOperationProposalChecks.result(head, availability, 0, "repeated"));
                var json = MasterRetryOperationProposalJson.encode(proposal);
                String bytes = json.toString();
                var decoded = MasterRetryOperationProposalJson.decode(
                    new JsonParser().parse(bytes).getAsJsonObject(), head);
                assertEquals(proposal, decoded);
                assertEquals(proposal.hashCode(), decoded.hashCode());
                assertEquals(json, MasterRetryOperationProposalJson.encode(decoded));
                assertEquals(RunReceipt.masterRetry(proposal), RunReceipt.masterRetry(decoded));
                assertThrows(IllegalArgumentException.class, () ->
                    MasterRetryOperationProposalJson.decode(json, MasterRetryOperation.prepare(head, proposal)));
                for (String field : new String[] {"version", "beforeRevision", "afterRevision", "beforeDrawCount"})
                {
                    var changed = json.deepCopy();
                    changed.addProperty(field, -1);
                    assertThrows(IllegalArgumentException.class, () ->
                        MasterRetryOperationProposalJson.decode(changed, head));
                }
                var foreign = json.deepCopy();
                foreign.addProperty("accountKey", "foreign");
                assertThrows(IllegalArgumentException.class, () -> MasterRetryOperationProposalJson.decode(foreign, head));
                var unknown = json.deepCopy();
                unknown.addProperty("extra", true);
                assertThrows(IllegalArgumentException.class, () -> MasterRetryOperationProposalJson.decode(unknown, head));
                for (String field : new String[] {"status", "after", "commandId", "beforeDrawCount"})
                {
                    var missing = json.deepCopy();
                    missing.remove(field);
                    assertThrows(IllegalArgumentException.class, () ->
                        MasterRetryOperationProposalJson.decode(missing, head));
                }
                if (count > 0)
                {
                    var changedPrefix = json.deepCopy();
                    changedPrefix.getAsJsonObject("after").getAsJsonArray("steps")
                        .get(0).getAsJsonObject().addProperty("streamStateAfter", "changed-prefix");
                    assertThrows(IllegalArgumentException.class, () ->
                        MasterRetryOperationProposalJson.decode(changedPrefix, head));
                }
                var status = json.deepCopy();
                status.addProperty("status", proposal.status() == MasterRetryContinuation.Status.PROPOSED
                    ? "SUSPENDED" : "PROPOSED");
                assertThrows(IllegalArgumentException.class, () -> MasterRetryOperationProposalJson.decode(status, head));
                assertEquals(bytes, json.toString());
                assertEquals(1, head.lifecycle().revision());
            }
        }
    }
}

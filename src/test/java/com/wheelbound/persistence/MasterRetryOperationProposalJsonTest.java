package com.wheelbound.persistence;

import com.google.gson.JsonParser;
import com.wheelbound.domain.MasterSelectionReadiness;
import com.wheelbound.domain.ActiveWheel;
import com.wheelbound.domain.DrawCommitment;
import com.wheelbound.domain.MasterSelectionProvenance;
import com.wheelbound.domain.MasterSelectionResolutionReceipt;
import com.wheelbound.domain.RunLifecycle;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterRetryOperationProposalJsonTest
{
    @Test public void historicalResponsesSurviveTerminalPublicationAndPause()
    {
        for (int count : new int[] {0, 1, 513})
        for (boolean suspended : new boolean[] {false, true})
        {
            var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, count, 7);
            var before = source.pendingMasterSelection().orElseThrow();
            var creation = source.masterCreations().orElseThrow().receipts().get(before.transactionId());
            var after = suspended ? before : before.append(before.transactionId(), count,
                before.currentStreamState(), new MasterSelectionProvenance.Step(
                    new DrawCommitment(before.pool(), 0), "repeated"));
            var proposal = MasterRetryOperationProposal.restoreResponse(creation, creation.identity(),
                new UUID(97, 1), 5, count, after, suspended
                    ? MasterRetryContinuation.Status.SUSPENDED : MasterRetryContinuation.Status.PROPOSED);
            var next = MasterRetryOperation.prepare(source, proposal);
            var terminal = MasterSelectionResolutionReceipt.prepare(new UUID(97, 2), 6, creation, after,
                new MasterSelectionProvenance.Step(
                    new DrawCommitment(after.pool(), after.pool().totalWeight() - 1), "terminal"));
            var resolved = MasterTaintTerminalOperation.prepare(next, terminal);
            var paused = LifecycleAdmission.prepare(resolved, proposal.identity(), new UUID(97, 3), 7,
                RunReceipt.Operation.PAUSE);
            var resumed = LifecycleAdmission.prepare(paused, proposal.identity(), new UUID(97, 4), 8,
                RunReceipt.Operation.RESUME);
            var json = MasterRetryOperationProposalJson.encode(proposal);
            String bytes = json.toString();
            var decoded = MasterRetryOperationProposalJson.decodeResponse(
                new JsonParser().parse(bytes).getAsJsonObject(), creation);
            assertEquals(proposal, decoded);
            assertEquals(proposal.hashCode(), decoded.hashCode());
            assertEquals(json, MasterRetryOperationProposalJson.encode(decoded));
            for (var head : new RunDocument[] {next, resolved, paused, resumed})
            {
                assertSame(decoded, decoded.validateRetained(head));
                assertSame(head, MasterRetryOperation.prepare(head, decoded));
                assertThrows(IllegalArgumentException.class, () -> MasterRetryOperationProposalJson.decode(json, head));
            }
            assertThrows(IllegalArgumentException.class, () -> decoded.validateRetained(source));
            for (String field : json.keySet())
            {
                var missing = json.deepCopy();
                missing.remove(field);
                assertThrows(IllegalArgumentException.class, () ->
                    MasterRetryOperationProposalJson.decodeResponse(missing, creation));
            }
            for (String field : new String[] {"version", "beforeRevision", "afterRevision", "beforeDrawCount"})
            {
                var changed = json.deepCopy();
                changed.addProperty(field, -1);
                assertThrows(IllegalArgumentException.class, () ->
                    MasterRetryOperationProposalJson.decodeResponse(changed, creation));
            }
            var unknown = json.deepCopy();
            unknown.addProperty("extra", true);
            assertThrows(IllegalArgumentException.class, () -> MasterRetryOperationProposalJson.decodeResponse(unknown, creation));
            var foreign = json.deepCopy();
            foreign.addProperty("accountKey", "foreign");
            assertThrows(IllegalArgumentException.class, () -> MasterRetryOperationProposalJson.decodeResponse(foreign, creation));
            var collision = json.deepCopy();
            collision.addProperty("commandId", creation.transactionId().toString());
            assertThrows(IllegalArgumentException.class, () -> MasterRetryOperationProposalJson.decodeResponse(collision, creation));
            var changedCommand = json.deepCopy();
            changedCommand.addProperty("commandId", new UUID(97, 99).toString());
            var unretained = MasterRetryOperationProposalJson.decodeResponse(changedCommand, creation);
            assertThrows(IllegalArgumentException.class, () -> unretained.validateRetained(resumed));
            if (after.drawCount() > 0)
            {
                var changedState = json.deepCopy();
                changedState.getAsJsonObject("after").getAsJsonArray("steps")
                    .get(after.drawCount() - 1).getAsJsonObject().addProperty("streamStateAfter", "changed");
                var altered = MasterRetryOperationProposalJson.decodeResponse(changedState, creation);
                assertThrows(IllegalArgumentException.class, () -> altered.validateRetained(resumed));
            }
            assertEquals(bytes, json.toString());
            assertEquals(RunLifecycle.State.ACTIVE, source.lifecycle().state());
            assertEquals(7, source.masterSpins().orElseThrow().available());
        }
    }

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

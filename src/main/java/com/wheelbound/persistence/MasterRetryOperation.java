package com.wheelbound.persistence;

import com.wheelbound.domain.RunLifecycle;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.MasterOrdinaryPoolEvidence;
import com.wheelbound.domain.MasterSelectionProvenance;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/** Pure atomic publication proposal. Current evidence must separately be qualified
 * under the storage lock; this operation does not authenticate evidence or RNG. */
public final class MasterRetryOperation
{
    private MasterRetryOperation() { }

    /** Funded preparation boundary for a future locked gameplay caller. Legacy
     * snapshots without explicit spin accounting cannot authorize sampling. The
     * continuation still permits evidence-driven suspension at zero balance;
     * ready evidence must pass its unreserved-spin gate. No persistence or evidence
     * authentication is supplied here, and valid results require terminal admission.
     */
    public static MasterRetryContinuation.Result prepareFundedSelection(RunDocument head,
        RunIdentity identity, UUID commandId, long revision,
        UUID transactionId, int drawCount, String streamState, String snapshotVersion,
        Map<String, MasterOrdinaryPoolEvidence> pools,
        int budget, Function<MasterSelectionProvenance, MasterSelectionProvenance.Step> source)
    {
        Objects.requireNonNull(head, "head");
        if (!head.masterSpins().isPresent())
        { throw new IllegalArgumentException("Explicit Master spin accounting required"); }
        return prepareFreshSelection(head, identity, commandId, revision, transactionId,
            drawCount, streamState, snapshotVersion, pools, budget, source);
    }

    /** Prepare a fresh unresolved batch and its atomic update from the same head.
     * The caller must supply trusted current evidence and a side-effect-free source
     * under its storage lock. This does not save, authenticate RNG, or admit replay.
     * Valid landings require the separate terminal accounting path.
     */
    public static RunDocument prepareFresh(RunDocument head,
        RunIdentity identity, UUID commandId, long revision,
        UUID transactionId, int drawCount, String streamState, String snapshotVersion,
        Map<String, MasterOrdinaryPoolEvidence> pools,
        int budget, Function<MasterSelectionProvenance, MasterSelectionProvenance.Step> source)
    {
        var result = prepareFreshSelection(head, identity, commandId, revision, transactionId,
            drawCount, streamState, snapshotVersion, pools, budget, source);
        return prepare(head, MasterRetryOperationProposal.prepare(head, commandId, result));
    }

    /** Prepare either an unresolved batch or a valid landing without losing the
     * sampled transcript. A terminal caller must consume that exact result through
     * its supported accounting path; it must not draw the batch again. This pure
     * preparation neither publishes nor persists either outcome. Evidence and source
     * must be trusted by the eventual caller under its storage lock.
     */
    public static MasterRetryContinuation.Result prepareFreshSelection(RunDocument head,
        RunIdentity identity, UUID commandId, long revision,
        UUID transactionId, int drawCount, String streamState, String snapshotVersion,
        Map<String, MasterOrdinaryPoolEvidence> pools,
        int budget, Function<MasterSelectionProvenance, MasterSelectionProvenance.Step> source)
    {
        Objects.requireNonNull(head, "head");
        Objects.requireNonNull(commandId, "commandId");
        // Refuse reuse and overflow before invoking a draw source. Historical replay
        // must use the exact retained response, never freshly sampled work.
        MasterRetryRecovery.requireUnused(head, commandId);
        Math.addExact(revision, 1);
        return MasterRetryContinuation.prepare(head, identity, revision, transactionId,
            drawCount, streamState, snapshotVersion, pools, budget, source);
    }

    public static RunDocument prepare(RunDocument head, MasterRetryOperationProposal request)
    {
        Objects.requireNonNull(head, "head");
        Objects.requireNonNull(request, "request");
        if (!head.lifecycle().identity().equals(request.identity()))
        { throw new IllegalArgumentException("Foreign retry request"); }
        var receipt = RunReceipt.masterRetry(request);
        for (var retained : head.receipts())
        {
            if (!retained.commandId().equals(request.commandId())) { continue; }
            if (!retained.equals(receipt))
            { throw new IllegalArgumentException("Retry command reused for different request"); }
            return head;
        }
        request.validateAgainst(head);
        var next = new RunLifecycle(request.identity(), request.afterRevision(),
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        var receipts = new ArrayList<>(head.receipts());
        receipts.add(receipt);
        return new RunDocument(next, head.catalogVersion(), receipts, head.fatePoints(),
            head.inventory(), head.grandFate(), head.grandProgress(), head.grandJournal(),
            head.grandActivations(), head.grandCurrentAttempt(), Optional.of(request.after()),
            head.masterCreations(), head.masterResolutions(), head.masterSpins().map(spins ->
                new com.wheelbound.domain.MasterSpinState(spins.version(), spins.identity(),
                    next.revision(), spins.allotted(), spins.reservation())),
            head.currentMasterSpecial().map(value -> value.obligation()));
    }
}

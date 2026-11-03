package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

/** Pure, evidence-gated retry preparation. No persistence, sampling authority or
 * gameplay admission: a trusted caller must revalidate and save under its lock.
 * A suspension result retains the actual pending prefix; it is not a saved marker.
 */
public final class MasterRetryContinuation
{
    public enum Status { PROPOSED, SUSPENDED }

    public static final class Result
    {
        private final Status status;
        private final MasterSelectionProvenance prefix;
        private Result(Status status, MasterSelectionProvenance prefix)
        { this.status = status; this.prefix = prefix; }
        public Status status() { return status; }
        public MasterSelectionProvenance prefix() { return prefix; }
    }

    private MasterRetryContinuation() { }

    /** Evidence declarations must cover the current aggregate wheel and revision.
     * Changed snapshots or classifications require separate reconciliation. Even
     * READY evidence cannot silently replace the frozen reasons or eligibility.
     */
    public static Result prepare(RunDocument current, RunIdentity identity, long revision,
        UUID transactionId, int drawCount, String streamState, String snapshotVersion,
        Map<String, MasterOrdinaryPoolEvidence> pools, int budget,
        Function<MasterSelectionProvenance, MasterSelectionProvenance.Step> source)
    {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(source, "source");
        RunLifecycle head = current.lifecycle();
        if (!head.identity().equals(identity) || head.revision() != revision
            || head.state() != RunLifecycle.State.ACTIVE
            || head.obligation() != RunLifecycle.Obligation.NONE
            || current.grandCurrentAttempt().isPresent() || budget <= 0)
        { throw new IllegalArgumentException("Unavailable retry context or budget"); }
        MasterSelectionProvenance pending = current.pendingMasterSelection().orElseThrow(
            () -> new IllegalArgumentException("Missing pending selection"));
        if (!pending.transactionId().equals(transactionId) || pending.drawCount() != drawCount
            || !pending.currentStreamState().equals(streamState))
        { throw new IllegalArgumentException("Stale or foreign retry prefix"); }
        current.masterCreations().orElseThrow(
            () -> new IllegalArgumentException("Missing creation history"))
            .matchPending(pending, identity, revision);
        MasterSelectionReadiness fresh = MasterWheelEvidence.assemble(identity, revision,
            snapshotVersion, current.catalogVersion(), current.inventory().orElseThrow(
                () -> new IllegalArgumentException("Missing inventory")), pools);
        if (!pending.snapshotVersion().equals(snapshotVersion)
            || !pending.catalogVersion().equals(current.catalogVersion())
            || fresh.status() != MasterSelectionReadiness.Status.READY
            || !sameEvidence(pending.readiness(), fresh))
        { return new Result(Status.SUSPENDED, pending); }
        var proposed = MasterEmptyRetryBatch.propose(pending,
            transactionId, drawCount, streamState, budget, source);
        // Only unresolved proposals may enter the future pending-prefix command.
        // Valid landings remain exclusively on the terminal accounting path.
        if (!proposed.hasValidLanding()) { MasterRetryPrefixBinding.validate(current, proposed); }
        return new Result(Status.PROPOSED, proposed);
    }

    private static boolean sameEvidence(MasterSelectionReadiness frozen, MasterSelectionReadiness fresh)
    {
        var left = frozen.wheel().slices();
        var right = fresh.wheel().slices();
        if (left.size() != right.size()) { return false; }
        for (int i = 0; i < left.size(); i++)
        {
            var a = left.get(i);
            var b = right.get(i);
            if (!a.id().equals(b.id()) || a.kind() != b.kind()
                || !Objects.equals(a.activity(), b.activity()) || a.enhancement() != b.enhancement()
                || a.weightUnits() != b.weightUnits()) { return false; }
            var x = frozen.classifications().get(a.id());
            var y = fresh.classifications().get(b.id());
            if (x.availability() != y.availability() || !x.reason().equals(y.reason())) { return false; }
        }
        return true;
    }
}

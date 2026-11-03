package com.wheelbound.persistence;

import com.wheelbound.domain.MasterSelectionCreationReceipt;
import com.wheelbound.domain.MasterSelectionCreationJournal;
import com.wheelbound.domain.MasterSelectionProvenance;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.RunLifecycle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/** Immutable unresolved continuation/suspension payload. This is not a durably admitted
 * receipt or saved suspension marker. Storage admission must qualify current evidence
 * under its lock and retain the original response for durable replay.
 */
public final class MasterRetryOperationProposal
{
    private final RunIdentity identity;
    private final UUID commandId;
    private final long beforeRevision;
    private final MasterSelectionProvenance before;
    private final MasterSelectionProvenance after;
    private final MasterRetryContinuation.Status status;
    private final List<Object> payload;

    private MasterRetryOperationProposal(RunDocument head, UUID commandId,
        MasterRetryContinuation.Result result)
    {
        this(head, commandId, Objects.requireNonNull(result, "result").prefix(), result.status());
    }

    private MasterRetryOperationProposal(RunDocument head, UUID commandId,
        MasterSelectionProvenance after, MasterRetryContinuation.Status status)
    {
        this.identity = head.lifecycle().identity();
        this.commandId = Objects.requireNonNull(commandId, "commandId");
        this.beforeRevision = head.lifecycle().revision();
        Math.addExact(beforeRevision, 1);
        this.before = head.pendingMasterSelection().orElseThrow(
            () -> new IllegalArgumentException("Missing pending selection"));
        this.after = Objects.requireNonNull(after, "after");
        this.status = Objects.requireNonNull(status, "status");
        validateAgainst(head);
        if (status == MasterRetryContinuation.Status.SUSPENDED
            ? after.drawCount() != before.drawCount() : after.drawCount() <= before.drawCount())
        { throw new IllegalArgumentException("Suspension must retain prefix; continuation must advance it"); }
        var creation = head.masterCreations().get().matchPending(before, identity, beforeRevision);
        List<Object> fields = new ArrayList<>(List.of(identity, commandId, beforeRevision,
            creation, status, before.drawCount()));
        // Include all intermediate tickets/states, even when the final state repeats.
        for (var landing : after.landings())
        {
            fields.add(landing.step().draw());
            fields.add(landing.step().streamStateAfter());
        }
        payload = List.copyOf(fields);
    }

    /** Wrap an evidence-gated preparation, rejecting valid results which require
     * terminal accounting/obligation publication. Does not authenticate evidence.
     */
    public static MasterRetryOperationProposal prepare(RunDocument head, UUID commandId,
        MasterRetryContinuation.Result result)
    { return new MasterRetryOperationProposal(Objects.requireNonNull(head, "head"), commandId, result); }

    /** Restore only against the exact original head. This validates structure and
     * prefix continuity; it does not authenticate current evidence or admit replay.
     */
    public static MasterRetryOperationProposal restore(RunDocument original, RunIdentity identity,
        UUID commandId, long beforeRevision, int beforeDrawCount,
        MasterSelectionProvenance after, MasterRetryContinuation.Status status)
    {
        Objects.requireNonNull(original, "original");
        if (!original.lifecycle().identity().equals(identity)
            || original.lifecycle().revision() != beforeRevision
            || original.pendingMasterSelection().orElseThrow(
                () -> new IllegalArgumentException("Missing original retry prefix")).drawCount() != beforeDrawCount)
        { throw new IllegalArgumentException("Retry restoration requires exact original head"); }
        return new MasterRetryOperationProposal(original, commandId, after, status);
    }

    /** Reconstruct an original response from historical fields, without pretending the
     * saved current head is the original active head. This is structural decoding only:
     * the aggregate must bind the envelope and transcript before allowing replay.
     */
    public static MasterRetryOperationProposal restoreResponse(MasterSelectionCreationReceipt creation,
        RunIdentity identity, UUID commandId, long beforeRevision, int beforeDrawCount,
        MasterSelectionProvenance after, MasterRetryContinuation.Status status)
    {
        Objects.requireNonNull(creation, "creation");
        Objects.requireNonNull(after, "after");
        if (creation.transactionId().equals(commandId))
        { throw new IllegalArgumentException("Retry command collides with creation"); }
        if (beforeDrawCount < 0 || beforeDrawCount > after.drawCount())
        { throw new IllegalArgumentException("Invalid original retry boundary"); }
        List<MasterSelectionProvenance.Step> steps = new ArrayList<>();
        for (int i = 0; i < beforeDrawCount; i++) { steps.add(after.landings().get(i).step()); }
        var before = new MasterSelectionProvenance(after.transactionId(), after.snapshotVersion(),
            after.catalogVersion(), after.streamDescriptor(), after.initialStreamState(), after.readiness(), steps);
        // A historical validation context has only this selection's creation. Later
        // creations must not make an earlier retained response unrestorable.
        var original = new RunDocument(new RunLifecycle(identity, beforeRevision,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), after.catalogVersion(), List.of(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.of(before), Optional.of(
                new MasterSelectionCreationJournal(identity, beforeRevision, List.of(creation))));
        return restore(original, identity, commandId, beforeRevision, beforeDrawCount, after, status);
    }

    /** Require the exact retained envelope, even after terminal publication or Pause.
     * A structurally decoded response alone never authorizes historical replay.
     */
    public MasterRetryOperationProposal validateRetained(RunDocument head)
    {
        Objects.requireNonNull(head, "head");
        if (!identity.equals(head.lifecycle().identity()) || afterRevision() > head.lifecycle().revision()
            || head.receipts().stream().noneMatch(r -> RunReceipt.masterRetry(this).equals(r)))
        { throw new IllegalArgumentException("Missing or conflicting retained retry response"); }
        MasterRetryHistoryBinding.validate(head);
        return this;
    }

    /** Exact original head only; this method deliberately grants no historical replay. */
    public void validateAgainst(RunDocument head)
    {
        Objects.requireNonNull(head, "head");
        if (!identity.equals(head.lifecycle().identity()) || beforeRevision != head.lifecycle().revision())
        { throw new IllegalArgumentException("Stale or foreign retry operation"); }
        MasterRetryRecovery.requireUnused(head, commandId);
        MasterRetryPrefixBinding.validate(head, before);
        if (head.pendingMasterSelection().get().drawCount() != before.drawCount())
        { throw new IllegalArgumentException("Retry operation requires its exact original prefix"); }
        MasterRetryPrefixBinding.validate(head, after);
        // Bind both transcripts to the same retained original creation.
        MasterSelectionCreationReceipt original = head.masterCreations().get()
            .matchPending(before, identity, beforeRevision);
        if (!original.equals(head.masterCreations().get().matchPending(after, identity, beforeRevision)))
        { throw new IllegalArgumentException("Changed retry creation"); }
    }

    public RunIdentity identity() { return identity; }
    public UUID commandId() { return commandId; }
    public long beforeRevision() { return beforeRevision; }
    public long afterRevision() { return beforeRevision + 1; }
    public MasterSelectionProvenance before() { return before; }
    public MasterSelectionProvenance after() { return after; }
    public MasterRetryContinuation.Status status() { return status; }

    @Override public boolean equals(Object value)
    { return value instanceof MasterRetryOperationProposal
        && payload.equals(((MasterRetryOperationProposal) value).payload); }
    @Override public int hashCode() { return payload.hashCode(); }
}

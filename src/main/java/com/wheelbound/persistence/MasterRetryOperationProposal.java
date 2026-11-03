package com.wheelbound.persistence;

import com.wheelbound.domain.MasterSelectionCreationReceipt;
import com.wheelbound.domain.MasterSelectionProvenance;
import com.wheelbound.domain.RunIdentity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        this.identity = head.lifecycle().identity();
        this.commandId = Objects.requireNonNull(commandId, "commandId");
        this.beforeRevision = head.lifecycle().revision();
        Math.addExact(beforeRevision, 1);
        this.before = head.pendingMasterSelection().orElseThrow(
            () -> new IllegalArgumentException("Missing pending selection"));
        this.after = Objects.requireNonNull(result, "result").prefix();
        this.status = result.status();
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

    /** Exact original head only; this method deliberately grants no historical replay. */
    public void validateAgainst(RunDocument head)
    {
        Objects.requireNonNull(head, "head");
        if (!identity.equals(head.lifecycle().identity()) || beforeRevision != head.lifecycle().revision())
        { throw new IllegalArgumentException("Stale or foreign retry operation"); }
        if (head.receipts().stream().anyMatch(r -> r.commandId().equals(commandId)))
        { throw new IllegalArgumentException("Retry command ID already retained"); }
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

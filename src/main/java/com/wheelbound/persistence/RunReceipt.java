package com.wheelbound.persistence;

import com.wheelbound.domain.RunLifecycle;
import com.wheelbound.domain.SlicePurchaseReceipt;
import com.wheelbound.domain.DrawCommitment;
import com.wheelbound.domain.FatePointDebits;
import java.util.Objects;
import java.util.UUID;

/** Immutable command identity, request and original response, persisted with the resulting snapshot. */
public final class RunReceipt
{
    public enum Operation { PAUSE, RESUME, DRAW, FP_DEBIT, SLICE_PURCHASE, GRAND_ACTIVATION }
    private final com.wheelbound.domain.GrandActivationReceipt activation;
    private final SlicePurchaseReceipt purchase;
    private final FatePointDebits.Receipt debit;
    private final DrawCommitment draw;
    private final UUID commandId;
    private final Operation operation;
    private final long beforeRevision;
    private final long afterRevision;
    private final RunLifecycle.State state;
    private final RunLifecycle.Obligation obligation;

    public RunReceipt(UUID commandId, Operation operation, long beforeRevision, long afterRevision,
        RunLifecycle.State state, RunLifecycle.Obligation obligation)
    { this(commandId, operation, beforeRevision, afterRevision, state, obligation, null); }

    public RunReceipt(UUID commandId, Operation operation, long beforeRevision, long afterRevision,
        RunLifecycle.State state, RunLifecycle.Obligation obligation, DrawCommitment draw)
    { this(commandId, operation, beforeRevision, afterRevision, state, obligation, draw, null); }

    public RunReceipt(UUID commandId, Operation operation, long beforeRevision, long afterRevision,
        RunLifecycle.State state, RunLifecycle.Obligation obligation, DrawCommitment draw,
        FatePointDebits.Receipt debit)
    { this(commandId, operation, beforeRevision, afterRevision, state, obligation, draw, debit, null); }

    /** Typed envelope only; aggregate validation and schema support remain separate requirements. */
    public RunReceipt(UUID commandId, Operation operation, long beforeRevision, long afterRevision,
        RunLifecycle.State state, RunLifecycle.Obligation obligation, DrawCommitment draw,
        FatePointDebits.Receipt debit, SlicePurchaseReceipt purchase)
    { this(commandId, operation, beforeRevision, afterRevision, state, obligation, draw, debit, purchase, null); }

    /** Original response envelope; durable codec/store integration remains unavailable. */
    public static RunReceipt grandActivation(com.wheelbound.domain.GrandActivationReceipt activation)
    {
        Objects.requireNonNull(activation, "activation");
        return new RunReceipt(activation.commandId(), Operation.GRAND_ACTIVATION,
            activation.beforeRevision(), activation.afterRevision(), RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.ACTIVE_OBJECTIVE, null, null, null, activation);
    }

    private RunReceipt(UUID commandId, Operation operation, long beforeRevision, long afterRevision,
        RunLifecycle.State state, RunLifecycle.Obligation obligation, DrawCommitment draw,
        FatePointDebits.Receipt debit, SlicePurchaseReceipt purchase,
        com.wheelbound.domain.GrandActivationReceipt activation)
    {
        this.activation = activation;
        if ((operation == Operation.GRAND_ACTIVATION) != (activation != null)
            || (activation != null && (obligation != RunLifecycle.Obligation.ACTIVE_OBJECTIVE
                || !commandId.equals(activation.commandId())
                || beforeRevision != activation.beforeRevision() || afterRevision != activation.afterRevision())))
        { throw new IllegalArgumentException("Invalid Grand activation receipt identity or boundary"); }
        this.commandId = Objects.requireNonNull(commandId, "commandId");
        this.operation = Objects.requireNonNull(operation, "operation");
        this.state = Objects.requireNonNull(state, "state");
        this.obligation = Objects.requireNonNull(obligation, "obligation");
        if (beforeRevision < 0 || afterRevision != Math.addExact(beforeRevision, 1))
        { throw new IllegalArgumentException("Invalid receipt revisions"); }
        if (state != (operation == Operation.PAUSE ? RunLifecycle.State.PAUSED : RunLifecycle.State.ACTIVE))
        { throw new IllegalArgumentException("Invalid lifecycle response"); }
        if ((operation == Operation.DRAW) != (draw != null)
            || (operation == Operation.DRAW && obligation != RunLifecycle.Obligation.OFFERS_COMMITTED))
        { throw new IllegalArgumentException("Invalid draw receipt"); }
        if ((operation == Operation.FP_DEBIT) != (debit != null)
            || (debit != null && (!commandId.equals(debit.commandId())
                || beforeRevision != debit.beforeRevision() || afterRevision != debit.afterRevision())))
        { throw new IllegalArgumentException("Invalid debit receipt identity"); }
        if ((operation == Operation.SLICE_PURCHASE) != (purchase != null)
            || (purchase != null && (obligation != RunLifecycle.Obligation.NONE
                || !commandId.equals(purchase.debit().commandId())
                || beforeRevision != purchase.debit().beforeRevision()
                || afterRevision != purchase.debit().afterRevision())))
        { throw new IllegalArgumentException("Invalid purchase receipt identity or boundary"); }
        this.purchase = purchase;
        this.debit = debit;
        this.draw = draw;
        this.beforeRevision = beforeRevision;
        this.afterRevision = afterRevision;
    }

    @Override public boolean equals(Object value)
    {
        if (this == value) { return true; }
        if (!(value instanceof RunReceipt)) { return false; }
        RunReceipt other = (RunReceipt) value;
        return commandId.equals(other.commandId) && operation == other.operation
            && beforeRevision == other.beforeRevision && afterRevision == other.afterRevision
            && state == other.state && obligation == other.obligation && Objects.equals(draw, other.draw)
            && Objects.equals(debitKey(), other.debitKey())
            && Objects.equals(purchaseKey(), other.purchaseKey())
            && Objects.equals(activation, other.activation);
    }

    @Override public int hashCode()
    { return Objects.hash(commandId, operation, beforeRevision, afterRevision, state, obligation, draw, debitKey(), purchaseKey(), activation); }

    public com.wheelbound.domain.GrandActivationReceipt activation() { return activation; }

    private Object purchaseKey()
    {
        if (purchase == null) { return null; }
        FatePointDebits.Receipt response = purchase.debit();
        return java.util.List.of(purchase.identity(), purchase.instanceId(), purchase.quote().activity(),
            purchase.quote().key(), purchase.quote().version(), purchase.quote().amount(),
            purchase.quote().kind(), purchase.quote().duplicateOrdinal(), response.commandId(),
            response.beforeRevision(), response.afterRevision(), response.balance());
    }

    public SlicePurchaseReceipt purchase() { return purchase; }

    private Object debitKey()
    {
        return debit == null ? null : java.util.List.of(debit.commandId(), debit.beforeRevision(),
            debit.afterRevision(), debit.quoteKey(), debit.quoteVersion(), debit.amount(), debit.balance());
    }

    public FatePointDebits.Receipt debit() { return debit; }

    public DrawCommitment draw() { return draw; }
    public UUID commandId() { return commandId; }
    public Operation operation() { return operation; }
    public long beforeRevision() { return beforeRevision; }
    public long afterRevision() { return afterRevision; }
    public RunLifecycle.State state() { return state; }
    public RunLifecycle.Obligation obligation() { return obligation; }
}

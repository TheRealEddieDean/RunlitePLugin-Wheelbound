package com.wheelbound.persistence;

import com.wheelbound.domain.RunLifecycle;
import java.util.Objects;
import java.util.UUID;

/** Immutable command identity, request and original response, persisted with the resulting snapshot. */
public final class RunReceipt
{
    public enum Operation { PAUSE, RESUME }
    private final UUID commandId;
    private final Operation operation;
    private final long beforeRevision;
    private final long afterRevision;
    private final RunLifecycle.State state;
    private final RunLifecycle.Obligation obligation;

    public RunReceipt(UUID commandId, Operation operation, long beforeRevision, long afterRevision,
        RunLifecycle.State state, RunLifecycle.Obligation obligation)
    {
        this.commandId = Objects.requireNonNull(commandId, "commandId");
        this.operation = Objects.requireNonNull(operation, "operation");
        this.state = Objects.requireNonNull(state, "state");
        this.obligation = Objects.requireNonNull(obligation, "obligation");
        if (beforeRevision < 0 || afterRevision != Math.addExact(beforeRevision, 1))
        { throw new IllegalArgumentException("Invalid receipt revisions"); }
        if (state != (operation == Operation.PAUSE ? RunLifecycle.State.PAUSED : RunLifecycle.State.ACTIVE))
        { throw new IllegalArgumentException("Invalid lifecycle response"); }
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
            && state == other.state && obligation == other.obligation;
    }

    @Override public int hashCode()
    { return Objects.hash(commandId, operation, beforeRevision, afterRevision, state, obligation); }

    public UUID commandId() { return commandId; }
    public Operation operation() { return operation; }
    public long beforeRevision() { return beforeRevision; }
    public long afterRevision() { return afterRevision; }
    public RunLifecycle.State state() { return state; }
    public RunLifecycle.Obligation obligation() { return obligation; }
}

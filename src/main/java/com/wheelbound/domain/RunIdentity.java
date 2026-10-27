package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/** Local account/profile/run identity. Account keys must come from RuneLite, never display names. */
public final class RunIdentity
{
    private final String accountKey;
    private final UUID runId;

    public RunIdentity(String accountKey, UUID runId)
    {
        this.accountKey = Objects.requireNonNull(accountKey, "accountKey");
        if (accountKey.trim().isEmpty()) { throw new IllegalArgumentException("Empty account key"); }
        this.runId = Objects.requireNonNull(runId, "runId");
    }

    public String accountKey() { return accountKey; }
    public UUID runId() { return runId; }

    @Override public boolean equals(Object value)
    {
        if (this == value) { return true; }
        if (!(value instanceof RunIdentity)) { return false; }
        RunIdentity other = (RunIdentity) value;
        return accountKey.equals(other.accountKey) && runId.equals(other.runId);
    }

    @Override public int hashCode() { return Objects.hash(accountKey, runId); }
}

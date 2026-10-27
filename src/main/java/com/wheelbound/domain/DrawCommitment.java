package com.wheelbound.domain;

import java.util.Objects;

/** The frozen input and sampled ticket determine one result without consuming RNG on restore. */
public final class DrawCommitment
{
    private final WeightedPool pool;
    private final int ticket;

    public DrawCommitment(WeightedPool pool, int ticket)
    {
        this.pool = Objects.requireNonNull(pool, "pool");
        pool.atTicket(ticket); // Validate before a commitment can be saved or restored.
        this.ticket = ticket;
    }

    public WeightedPool pool() { return pool; }
    public int ticket() { return ticket; }
    public String selectedId() { return pool.atTicket(ticket).id(); }
    @Override public boolean equals(Object value)
    { return value instanceof DrawCommitment && ticket == ((DrawCommitment)value).ticket && pool.equals(((DrawCommitment)value).pool); }
    @Override public int hashCode() { return Objects.hash(pool, ticket); }
}

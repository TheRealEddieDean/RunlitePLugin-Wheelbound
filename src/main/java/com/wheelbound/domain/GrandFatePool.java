package com.wheelbound.domain;

import java.util.List;
import java.util.Objects;

/** WB-D003 fixed equal-weight pool. Does not seal a run or authorize an attempt/reward. */
public final class GrandFatePool
{
    /** Stable identities, independent of display names and enum ordinals. */
    public enum Goal
    {
        INFERNO("grand.inferno"),
        FORTIS_COLOSSEUM("grand.fortis_colosseum"),
        RADIANT_OATHPLATE("grand.radiant_oathplate"),
        AWAKENED_DT2("grand.awakened_dt2"),
        MASTER_HILT_5("grand.master_hilt_5");

        private final String id;
        Goal(String id) { this.id = id; }
        public String id() { return id; }
    }

    public static final String VERSION = "grand-fate-1";
    private static final WeightedPool POOL = new WeightedPool(VERSION, List.of(
        new WeightedPool.Entry(Goal.INFERNO.id(), 1),
        new WeightedPool.Entry(Goal.FORTIS_COLOSSEUM.id(), 1),
        new WeightedPool.Entry(Goal.RADIANT_OATHPLATE.id(), 1),
        new WeightedPool.Entry(Goal.AWAKENED_DT2.id(), 1),
        new WeightedPool.Entry(Goal.MASTER_HILT_5.id(), 1)));

    private GrandFatePool() {}

    /** All five outcomes remain present regardless of account progress or readiness. */
    public static WeightedPool snapshot() { return POOL; }

    /** Caller supplies an unbiased ticket only at an authorized durable sealing boundary. */
    public static DrawCommitment atTicket(int ticket) { return new DrawCommitment(POOL, ticket); }

    /** Validate the entire frozen pool, not merely a recognized selected identity. No RNG. */
    public static Goal goal(DrawCommitment commitment)
    {
        Objects.requireNonNull(commitment, "commitment");
        if (!POOL.equals(commitment.pool()))
        { throw new IllegalArgumentException("Unsupported Grand Fate pool"); }
        for (Goal goal : Goal.values())
        {
            if (goal.id().equals(commitment.selectedId())) { return goal; }
        }
        throw new IllegalStateException("Grand Fate identity missing");
    }
}

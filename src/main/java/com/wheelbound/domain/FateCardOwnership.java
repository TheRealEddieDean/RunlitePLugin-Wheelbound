package com.wheelbound.domain;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable L/S/G ownership only (WB-D046/049/050/113). Ownership is not current
 * offering eligibility. Challenge ranks, targets and Wilderness opt-in require
 * separate models; this snapshot cannot authorize their purchase or generation.
 * No prices, grants, persistence, draws or gameplay activation are supplied here.
 */
public final class FateCardOwnership
{
    public static final int VERSION = 1;
    public enum Pool { SKILLING, BOSSING }
    public enum Type { LESSER, STANDARD, GREATER }

    private final RunIdentity identity;
    private final Map<Pool, Set<Type>> owned;

    /** Restore explicit ownership; missing pools/defaults are invalid, not repaired.
     * The caller must authenticate this declaration against the durable aggregate.
     */
    public FateCardOwnership(int version, RunIdentity identity, Map<Pool, Set<Type>> owned)
    {
        if (version != VERSION) { throw new IllegalArgumentException("Unsupported card ownership version"); }
        this.identity = Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(owned, "owned");
        if (!owned.keySet().equals(Set.of(Pool.SKILLING, Pool.BOSSING)))
        { throw new IllegalArgumentException("Both separate card pools are required"); }
        var skilling = Set.copyOf(owned.get(Pool.SKILLING));
        var bossing = Set.copyOf(owned.get(Pool.BOSSING));
        if (!skilling.contains(Type.STANDARD) || !bossing.contains(Type.STANDARD))
        { throw new IllegalArgumentException("Standard must be owned in both pools"); }
        this.owned = Map.of(Pool.SKILLING, skilling, Pool.BOSSING, bossing);
    }

    /** Explicit new-run baseline only; never use to fill absent legacy ownership. */
    public static FateCardOwnership starting(RunIdentity identity)
    {
        return new FateCardOwnership(VERSION, identity, Map.of(
            Pool.SKILLING, Set.of(Type.STANDARD), Pool.BOSSING, Set.of(Type.STANDARD)));
    }

    public int version() { return VERSION; }
    public RunIdentity identity() { return identity; }
    public Map<Pool, Set<Type>> owned() { return owned; }
    public boolean owns(Pool pool, Type type)
    { return owned.get(Objects.requireNonNull(pool, "pool")).contains(Objects.requireNonNull(type, "type")); }

    /** WB-D124 ownership prerequisite only, not Challenge purchase admission.
     * Affordability, reviewed bundle/quote and atomic grant receipts remain required.
     */
    public boolean hasBossingChallengeCardPrerequisites()
    { return owns(Pool.BOSSING, Type.LESSER) && owns(Pool.BOSSING, Type.GREATER); }

    @Override public boolean equals(Object other)
    {
        if (!(other instanceof FateCardOwnership)) { return false; }
        var value = (FateCardOwnership) other;
        return identity.equals(value.identity) && owned.equals(value.owned);
    }

    @Override public int hashCode() { return Objects.hash(identity, owned); }
}

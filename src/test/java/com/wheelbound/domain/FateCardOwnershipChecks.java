package com.wheelbound.domain;

import java.util.*;
import static com.wheelbound.domain.FateCardOwnership.Pool.*;
import static com.wheelbound.domain.FateCardOwnership.Type.*;

/** Dependency-free checks also run by the JUnit wrapper. */
public final class FateCardOwnershipChecks
{
    public static void main(String[] args)
    {
        var identity = new RunIdentity("cards", new UUID(180, 1));
        var starting = FateCardOwnership.starting(identity);
        check(starting.version() == 1 && starting.identity().equals(identity));
        for (var pool : FateCardOwnership.Pool.values())
        {
            check(starting.owns(pool, STANDARD));
            check(!starting.owns(pool, LESSER) && !starting.owns(pool, GREATER));
        }
        // Exhaust all independent L/G ownership combinations across both pools.
        for (int mask = 0; mask < 16; mask++)
        {
            var skilling = EnumSet.of(STANDARD);
            var bossing = EnumSet.of(STANDARD);
            if ((mask & 1) != 0) { skilling.add(LESSER); }
            if ((mask & 2) != 0) { skilling.add(GREATER); }
            if ((mask & 4) != 0) { bossing.add(LESSER); }
            if ((mask & 8) != 0) { bossing.add(GREATER); }
            var input = new HashMap<FateCardOwnership.Pool, Set<FateCardOwnership.Type>>();
            input.put(SKILLING, skilling);
            input.put(BOSSING, bossing);
            var value = new FateCardOwnership(1, identity, input);
            var restored = new FateCardOwnership(value.version(), identity, value.owned());
            check(value.equals(restored) && value.hashCode() == restored.hashCode());
            check(!value.equals(FateCardOwnership.starting(new RunIdentity("other", identity.runId()))));
            check(value.owns(SKILLING, LESSER) == ((mask & 1) != 0));
            check(value.owns(SKILLING, GREATER) == ((mask & 2) != 0));
            check(value.owns(BOSSING, LESSER) == ((mask & 4) != 0));
            check(value.owns(BOSSING, GREATER) == ((mask & 8) != 0));
            check(value.hasBossingChallengeCardPrerequisites() == ((mask & 12) == 12));
            input.clear(); skilling.clear(); bossing.clear();
            check(value.equals(restored));
            rejects(() -> value.owned().clear());
            rejects(() -> value.owned().get(BOSSING).clear());
        }
        for (int version : new int[] {-1, 0, 2, Integer.MAX_VALUE})
        { rejects(() -> new FateCardOwnership(version, identity, starting.owned())); }
        rejects(() -> new FateCardOwnership(1, null, starting.owned()));
        rejects(() -> new FateCardOwnership(1, identity, null));
        rejects(() -> new FateCardOwnership(1, identity, Map.of()));
        rejects(() -> new FateCardOwnership(1, identity, Map.of(SKILLING, Set.of(STANDARD))));
        for (var pool : FateCardOwnership.Pool.values())
        {
            var invalid = new HashMap<>(starting.owned());
            invalid.put(pool, Set.of(LESSER, GREATER));
            rejects(() -> new FateCardOwnership(1, identity, invalid));
            invalid.put(pool, null);
            rejects(() -> new FateCardOwnership(1, identity, invalid));
            invalid.put(pool, new HashSet<>(Arrays.asList(STANDARD, null)));
            rejects(() -> new FateCardOwnership(1, identity, invalid));
        }
        rejects(() -> starting.owns(null, STANDARD));
        rejects(() -> starting.owns(BOSSING, null));
        check(!starting.hasBossingChallengeCardPrerequisites());
        System.out.println("FateCardOwnershipChecks PASSED");
    }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException | UnsupportedOperationException expected) { return; }
        throw new AssertionError("Expected refusal");
    }
}

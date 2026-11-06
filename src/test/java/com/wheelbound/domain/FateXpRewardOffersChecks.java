package com.wheelbound.domain;

/** Dependency-free contract checks; quoted amounts are fixtures, not balance. */
public final class FateXpRewardOffersChecks
{
    public static void main(String[] args)
    {
        FateXpObjective a = objective("MINING", 42, 1000, 500, "c", "g");
        FateXpObjective b = objective("MINING", 42, 1000, 700, "c", "g");
        FateXpStartingOffers targets = targets(a, b, Long.MIN_VALUE);
        FateXpRewardOffers.Reward first = reward(a, "first", "v1", 0);
        FateXpRewardOffers.Reward second = reward(b, "second", "v1", Long.MAX_VALUE);
        FateXpRewardOffers original = new FateXpRewardOffers(targets, first, second);
        FateXpRewardOffers restored = new FateXpRewardOffers(targets(a, b, Long.MIN_VALUE),
            reward(a, "first", "v1", 0), reward(b, "second", "v1", Long.MAX_VALUE));
        check(original.equals(restored) && restored.equals(original)
            && original.hashCode() == restored.hashCode());
        check(original.targets() == targets && original.rewards().get(0) == first
            && original.rewards().get(1) == second);
        check(first.objective() == a && first.quoteKey().equals("first")
            && first.quoteVersion().equals("v1") && first.fatePoints() == 0);
        check(second.fatePoints() == Long.MAX_VALUE);
        try { original.rewards().clear(); throw new AssertionError("Mutable rewards"); }
        catch (UnsupportedOperationException expected) { }
        invalid(() -> new FateXpRewardOffers(targets, second, first));
        invalid(() -> new FateXpRewardOffers(targets, first, first));
        // Matching target numbers alone cannot authenticate a quote's objective.
        for (FateXpObjective changed : new FateXpObjective[] {
            objective("FISHING", 42, 1000, 500, "c", "g"),
            objective("MINING", 43, 1000, 500, "c", "g"),
            objective("MINING", 42, 1001, 500, "c", "g"),
            objective("MINING", 42, 1000, 501, "c", "g"),
            objective("MINING", 42, 1000, 500, "c2", "g"),
            objective("MINING", 42, 1000, 500, "c", "g2") })
        { invalid(() -> new FateXpRewardOffers(targets, reward(changed, "first", "v1", 0), second)); }
        for (FateXpObjective.CombatSkill skill : FateXpObjective.CombatSkill.values())
        {
            FateXpObjective ca = FateXpObjective.combat(skill, 42, 1000, 500, "c", "g");
            FateXpObjective cb = FateXpObjective.combat(skill, 42, 1000, 700, "c", "g");
            FateXpStartingOffers combat = targets(ca, cb, 0);
            new FateXpRewardOffers(combat, reward(ca, "a", "v", 1), reward(cb, "b", "v", 2));
            invalid(() -> new FateXpRewardOffers(combat,
                reward(objective(skill.name(), 42, 1000, 500, "c", "g"), "a", "v", 1),
                reward(cb, "b", "v", 2)));
        }
        check(!original.equals(new FateXpRewardOffers(targets(a, b, 1), first, second)));
        for (FateXpRewardOffers.Reward changed : new FateXpRewardOffers.Reward[] {
            reward(a, "changed", "v1", 0), reward(a, "first", "v2", 0),
            reward(a, "first", "v1", 1) })
        {
            check(!first.equals(changed));
            check(!original.equals(new FateXpRewardOffers(targets, changed, second)));
        }
        // New quotes do not reprice previously retained offers.
        new FateXpRewardOffers(targets, reward(a, "first", "v2", 3), reward(b, "second", "v2", 4));
        check(original.equals(restored));
        check(!original.equals(null) && !original.equals("offers"));
        check(!first.equals(null) && !first.equals("reward"));
        invalid(() -> new FateXpRewardOffers(null, first, second));
        invalid(() -> new FateXpRewardOffers(targets, null, second));
        invalid(() -> new FateXpRewardOffers(targets, first, null));
        invalid(() -> reward(null, "q", "v", 1));
        invalid(() -> reward(a, "q", "v", -1));
        invalid(() -> reward(a, "q", "v", Long.MIN_VALUE));
        invalid(() -> reward(a, null, "v", 1));
        invalid(() -> reward(a, "q", null, 1));
        for (String malformed : new String[] { "", " ", " q", "q ", "\tq", "q\n" })
        {
            invalid(() -> reward(a, malformed, "v", 1));
            invalid(() -> reward(a, "q", malformed, 1));
        }
        System.out.println("FateXpRewardOffersChecks PASSED");
    }

    private static FateXpObjective objective(String skill, int level, long baseline, long target,
        String catalog, String generation)
    { return FateXpObjective.skilling(skill, level, baseline, target, catalog, generation); }
    private static FateXpStartingOffers targets(FateXpObjective a, FateXpObjective b, long seed)
    { return new FateXpStartingOffers(new FateXpStartingOffers.Offer(a, seed),
        new FateXpStartingOffers.Offer(b, Long.MAX_VALUE)); }
    private static FateXpRewardOffers.Reward reward(FateXpObjective objective, String key,
        String version, long fp)
    { return new FateXpRewardOffers.Reward(objective, key, version, fp); }
    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Reward offer mismatch"); } }
    private static void invalid(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Invalid reward offer accepted");
    }
}

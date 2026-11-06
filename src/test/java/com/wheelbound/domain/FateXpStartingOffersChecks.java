package com.wheelbound.domain;

/** Dependency-free structural checks; amounts/seeds are fixtures, not balance. */
public final class FateXpStartingOffersChecks
{
    public static void main(String[] args)
    {
        FateXpObjective a = skill("MINING", 42, 1000, 500, "c", "g");
        FateXpObjective b = skill("MINING", 42, 1000, 700, "c", "g");
        FateXpStartingOffers.Offer first = offer(a, Long.MIN_VALUE);
        FateXpStartingOffers.Offer second = offer(b, Long.MAX_VALUE);
        FateXpStartingOffers pair = new FateXpStartingOffers(first, second);
        check(pair.offers().size() == 2 && pair.offers().get(0) == first
            && pair.offers().get(1) == second);
        check(first.generationSeed() == Long.MIN_VALUE && second.generationSeed() == Long.MAX_VALUE);
        FateXpStartingOffers restored = new FateXpStartingOffers(offer(a, Long.MIN_VALUE),
            offer(b, Long.MAX_VALUE));
        check(pair.equals(restored) && restored.equals(pair) && pair.hashCode() == restored.hashCode());
        check(!pair.equals(new FateXpStartingOffers(second, first)));
        check(!pair.equals(new FateXpStartingOffers(offer(a, 0), second)));
        check(!pair.equals(null) && !pair.equals("pair"));
        check(!first.equals(null) && !first.equals("offer") && !first.equals(offer(b, Long.MIN_VALUE)));
        try { pair.offers().set(0, second); throw new AssertionError("Mutable offers"); }
        catch (UnsupportedOperationException expected) { }
        invalid(() -> new FateXpStartingOffers(first, offer(a, 1)));
        invalid(() -> new FateXpStartingOffers(null, second));
        invalid(() -> new FateXpStartingOffers(first, null));
        invalid(() -> offer(null, 0));
        for (FateXpObjective mismatch : new FateXpObjective[] {
            skill("FISHING", 42, 1000, 700, "c", "g"),
            skill("MINING", 43, 1000, 700, "c", "g"),
            skill("MINING", 42, 1001, 700, "c", "g"),
            skill("MINING", 42, 1000, 700, "changed", "g"),
            skill("MINING", 42, 1000, 700, "c", "changed") })
        { invalid(() -> new FateXpStartingOffers(first, offer(mismatch, 2))); }
        for (FateXpObjective.CombatSkill choice : FateXpObjective.CombatSkill.values())
        {
            FateXpObjective combatA = FateXpObjective.combat(choice, 1, 0, 1, "c", "g");
            FateXpObjective combatB = FateXpObjective.combat(choice, 1, 0, Long.MAX_VALUE, "c", "g");
            check(new FateXpStartingOffers(offer(combatA, 0), offer(combatB, 1))
                .offers().get(1).objective().requiredXp() == Long.MAX_VALUE);
            invalid(() -> new FateXpStartingOffers(offer(combatA, 0),
                offer(skill(choice.name(), 1, 0, 2, "c", "g"), 1)));
        }
        invalid(() -> new FateXpStartingOffers(
            offer(FateXpObjective.combat(FateXpObjective.CombatSkill.ATTACK, 1, 0, 1, "c", "g"), 0),
            offer(FateXpObjective.combat(FateXpObjective.CombatSkill.MAGIC, 1, 0, 2, "c", "g"), 1)));
        // A later independently constructed snapshot cannot alter retained offers.
        new FateXpStartingOffers(offer(skill("MINING", 43, 2000, 800, "v2", "v2"), 3),
            offer(skill("MINING", 43, 2000, 900, "v2", "v2"), 4));
        check(pair.equals(restored));
        System.out.println("FateXpStartingOffersChecks PASSED");
    }

    private static FateXpStartingOffers.Offer offer(FateXpObjective objective, long seed)
    { return new FateXpStartingOffers.Offer(objective, seed); }
    private static FateXpObjective skill(String key, int level, long baseline, long target,
        String catalog, String generation)
    { return FateXpObjective.skilling(key, level, baseline, target, catalog, generation); }
    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Starting offers mismatch"); } }
    private static void invalid(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Invalid starting offers accepted");
    }
}

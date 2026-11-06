package com.wheelbound.domain;

/** Dependency-free checks; all numeric targets are test fixtures, not balance rules. */
public final class FateXpObjectiveChecks
{
    public static void main(String[] args)
    {
        FateXpObjective mining = skill("MINING", 42, 1000, 500, "catalog-a", "generation-a");
        check(mining.skillKey().equals("MINING") && mining.combatSkill() == null
            && mining.realLevel() == 42 && mining.baselineXp() == 1000
            && mining.requiredXp() == 500 && mining.catalogVersion().equals("catalog-a")
            && mining.generationVersion().equals("generation-a"));
        FateXpObjective restored = skill("MINING", 42, 1000, 500, "catalog-a", "generation-a");
        check(mining.equals(restored) && restored.equals(mining)
            && mining.hashCode() == restored.hashCode());
        // Later observations/catalogs do not replace an existing frozen payload.
        FateXpObjective later = skill("MINING", 43, 1600, 700, "catalog-b", "generation-b");
        check(!mining.equals(later) && mining.requiredXp() == 500 && mining.baselineXp() == 1000);
        check(!mining.equals(skill("FISHING", 42, 1000, 500, "catalog-a", "generation-a")));
        check(!mining.equals(skill("MINING", 43, 1000, 500, "catalog-a", "generation-a")));
        check(!mining.equals(skill("MINING", 42, 1001, 500, "catalog-a", "generation-a")));
        check(!mining.equals(skill("MINING", 42, 1000, 501, "catalog-a", "generation-a")));
        check(!mining.equals(skill("MINING", 42, 1000, 500, "catalog-b", "generation-a")));
        check(!mining.equals(skill("MINING", 42, 1000, 500, "catalog-a", "generation-b")));
        check(!mining.equals(null) && !mining.equals("MINING"));
        check(FateXpObjective.CombatSkill.values().length == 5);
        for (FateXpObjective.CombatSkill choice : FateXpObjective.CombatSkill.values())
        {
            FateXpObjective combat = FateXpObjective.combat(choice, 1, 0, 1, "c", "g");
            check(combat.combatSkill() == choice && combat.skillKey().equals(choice.name()));
            check(combat.equals(FateXpObjective.combat(choice, 1, 0, 1, "c", "g")));
            check(!combat.equals(skill(choice.name(), 1, 0, 1, "c", "g")));
        }
        // Separate deltas, never add baseline and target to infer credited progress.
        check(skill("MINING", 1, Long.MAX_VALUE, Long.MAX_VALUE, "c", "g")
            .requiredXp() == Long.MAX_VALUE);
        invalid(() -> skill("MINING", 0, 0, 1, "c", "g"));
        invalid(() -> skill("MINING", -1, 0, 1, "c", "g"));
        invalid(() -> skill("MINING", 1, -1, 1, "c", "g"));
        invalid(() -> skill("MINING", 1, 0, 0, "c", "g"));
        invalid(() -> skill("MINING", 1, 0, -1, "c", "g"));
        for (String malformed : new String[] { null, "", " ", " MINING", "MINING ", "\t" })
        {
            invalid(() -> skill(malformed, 1, 0, 1, "c", "g"));
            invalid(() -> skill("MINING", 1, 0, 1, malformed, "g"));
            invalid(() -> skill("MINING", 1, 0, 1, "c", malformed));
        }
        invalid(() -> FateXpObjective.combat(null, 1, 0, 1, "c", "g"));
        System.out.println("FateXpObjectiveChecks PASSED");
    }

    private static FateXpObjective skill(String key, int level, long baseline, long target,
        String catalog, String generation)
    { return FateXpObjective.skilling(key, level, baseline, target, catalog, generation); }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Frozen objective mismatch"); } }

    private static void invalid(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Invalid objective accepted");
    }
}

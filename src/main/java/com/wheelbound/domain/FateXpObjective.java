package com.wheelbound.domain;

import java.util.Objects;

/**
 * Frozen XP objective payload prerequisite (WB-D023/091/098). This is neither
 * an assignment commitment nor authority to accept observations. The enclosing
 * assignment must retain offers, selection, randomness, reward, restrictions and
 * identity together. Catalog eligibility and reviewed target generation remain
 * caller obligations; no balance table or level-to-XP formula is inferred here.
 */
public final class FateXpObjective
{
    public enum CombatSkill { ATTACK, STRENGTH, DEFENCE, RANGED, MAGIC }

    private final String skillKey;
    private final CombatSkill combatSkill;
    private final int realLevel;
    private final long baselineXp;
    private final long requiredXp;
    private final String catalogVersion;
    private final String generationVersion;

    /** The skill key must be resolved from the supported skilling catalog. */
    public static FateXpObjective skilling(String skillKey, int realLevel, long baselineXp,
        long requiredXp, String catalogVersion, String generationVersion)
    {
        return new FateXpObjective(skillKey, null, realLevel, baselineXp, requiredXp,
            catalogVersion, generationVersion);
    }

    /** Combat choice precedes target generation; HP and Prayer are not choices. */
    public static FateXpObjective combat(CombatSkill selectedSkill, int realLevel, long baselineXp,
        long requiredXp, String catalogVersion, String generationVersion)
    {
        Objects.requireNonNull(selectedSkill, "selectedSkill");
        return new FateXpObjective(selectedSkill.name(), selectedSkill, realLevel, baselineXp,
            requiredXp, catalogVersion, generationVersion);
    }

    private FateXpObjective(String skillKey, CombatSkill combatSkill, int realLevel,
        long baselineXp, long requiredXp, String catalogVersion, String generationVersion)
    {
        this.skillKey = key(skillKey);
        this.combatSkill = combatSkill;
        if (realLevel <= 0 || baselineXp < 0 || requiredXp <= 0)
        { throw new IllegalArgumentException("Invalid frozen XP objective"); }
        this.realLevel = realLevel;
        this.baselineXp = baselineXp;
        this.requiredXp = requiredXp;
        this.catalogVersion = key(catalogVersion);
        this.generationVersion = key(generationVersion);
    }

    private static String key(String value)
    {
        Objects.requireNonNull(value, "catalog key/version");
        if (value.isBlank() || !value.equals(value.strip()))
        { throw new IllegalArgumentException("Noncanonical catalog key/version"); }
        return value;
    }

    public String skillKey() { return skillKey; }
    public CombatSkill combatSkill() { return combatSkill; }
    public int realLevel() { return realLevel; }
    public long baselineXp() { return baselineXp; }
    public long requiredXp() { return requiredXp; }
    public String catalogVersion() { return catalogVersion; }
    public String generationVersion() { return generationVersion; }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof FateXpObjective)) { return false; }
        FateXpObjective other = (FateXpObjective) value;
        return skillKey.equals(other.skillKey) && combatSkill == other.combatSkill
            && realLevel == other.realLevel && baselineXp == other.baselineXp
            && requiredXp == other.requiredXp && catalogVersion.equals(other.catalogVersion)
            && generationVersion.equals(other.generationVersion);
    }

    @Override public int hashCode()
    { return Objects.hash(skillKey, combatSkill, realLevel, baselineXp, requiredXp,
        catalogVersion, generationVersion); }
}

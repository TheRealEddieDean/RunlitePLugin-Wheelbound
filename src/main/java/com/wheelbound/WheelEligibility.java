package com.wheelbound;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import net.runelite.api.Skill;

final class WheelEligibility
{
    private WheelEligibility() {}

    static List<BossDefinition> bosses(List<BossDefinition> catalog, boolean raids, boolean onlyIncomplete,
        boolean limitLevels, boolean loggedIn, ToIntFunction<Skill> levels, Predicate<BossDefinition> incomplete)
    {
        if (!loggedIn && (limitLevels || onlyIncomplete)) { return List.of(); }
        return catalog.stream()
            .filter(b -> raids || !b.raid)
            .filter(b -> !limitLevels || b.profile != null && b.profile.matches(levels))
            .filter(b -> !onlyIncomplete || incomplete.test(b))
            .collect(Collectors.toUnmodifiableList());
    }

    static List<Skill> skills(boolean exclude99, boolean loggedIn, ToIntFunction<Skill> levels)
    { return skills(false, exclude99, loggedIn, levels); }

    static List<Skill> skills(boolean excludeCombat, boolean exclude99, boolean loggedIn, ToIntFunction<Skill> levels)
    {
        if (exclude99 && !loggedIn) { return List.of(); }
        return Arrays.stream(Skill.values())
            .filter(s -> !excludeCombat || !COMBAT_SKILLS.contains(s))
            .filter(s -> !exclude99 || levels.applyAsInt(s) < 99)
            .collect(Collectors.toUnmodifiableList());
    }

    static List<PetDefinition> pets(Set<WheelFilter> filters)
    {
        return PetDefinition.ALL.stream().filter(pet -> {
            switch (pet.kind)
            {
                case BOSS: return filters.contains(WheelFilter.PET_BOSSES);
                case RAID: return filters.contains(WheelFilter.PET_RAIDS);
                case SKILL: return filters.contains(WheelFilter.PET_SKILLS);
                case ACTIVITY: return filters.contains(WheelFilter.PET_ACTIVITIES);
                default: throw new IllegalStateException("Unknown pet source");
            }
        }).collect(Collectors.toUnmodifiableList());
    }

    private static final Set<Skill> COMBAT_SKILLS = Set.of(Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE,
        Skill.HITPOINTS, Skill.RANGED, Skill.MAGIC, Skill.PRAYER);

    static List<CaEncounter> achievements(List<CaEncounter> encounters, boolean excludeBosses, boolean excludeRaids,
        Predicate<CaEncounter> hasTasks, Predicate<CaEncounter> taskAvailable)
    {
        return encounters.stream().filter(e -> !excludeBosses || e.kind != CaEncounter.Kind.BOSS)
            .filter(e -> !excludeRaids || e.kind != CaEncounter.Kind.RAID)
            .filter(hasTasks).filter(taskAvailable).collect(Collectors.toUnmodifiableList());
    }
}

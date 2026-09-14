package com.wheelbound;

import java.util.Arrays;
import java.util.List;
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
    {
        if (exclude99 && !loggedIn) { return List.of(); }
        return Arrays.stream(Skill.values())
            .filter(s -> !exclude99 || levels.applyAsInt(s) < 99)
            .collect(Collectors.toUnmodifiableList());
    }
}

package com.wheelbound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.runelite.client.hiscore.HiscoreSkill;
import net.runelite.client.hiscore.HiscoreSkillType;

final class BossCatalog
{
    static final List<BossDefinition> ALL = create();
    private BossCatalog() {}

    private static List<BossDefinition> create()
    {
        Map<String, BossProfile> profiles = BossProfile.defaults();
        List<BossDefinition> bosses = new ArrayList<>();
        for (HiscoreSkill boss : HiscoreSkill.values())
        {
            if (boss.getType() != HiscoreSkillType.BOSS) { continue; }
            String name = boss.getName();
            boolean raid = boss.name().startsWith("CHAMBERS_OF_XERIC")
                || boss.name().startsWith("THEATRE_OF_BLOOD") || boss.name().startsWith("TOMBS_OF_AMASCUT");
            List<String> aliases;
            switch (boss)
            {
                case BARROWS_CHESTS: aliases = List.of("Barrows"); break;
                case LUNAR_CHESTS: aliases = List.of("Moons of Peril", "Lunar Chests"); break;
                case DAGANNOTH_PRIME: case DAGANNOTH_REX: case DAGANNOTH_SUPREME:
                    aliases = List.of(name, "Dagannoth Kings"); break;
                case TZTOK_JAD: aliases = List.of("TzTok-Jad", "The Fight Caves"); break;
                case TZKAL_ZUK: aliases = List.of("TzKal-Zuk", "The Inferno"); break;
                case SOL_HEREDIT: aliases = List.of("Sol Heredit", "Fortis Colosseum"); break;
                case NIGHTMARE: aliases = List.of("The Nightmare", "Nightmare"); break;
                case THE_ROYAL_TITANS: aliases = List.of("Royal Titans", name); break;
                // Only map modes to exact encounters. Do not count a normal-mode task as a hard-mode task.
                default: aliases = List.of(name);
            }
            BossProfile profile = profiles.get(name);
            if (profile == null)
            {
                for (String alias : aliases)
                {
                    profile = profiles.get(alias);
                    if (profile != null) { break; }
                }
            }
            bosses.add(new BossDefinition(boss, raid, profile, aliases));
        }
        return List.copyOf(bosses);
    }
}

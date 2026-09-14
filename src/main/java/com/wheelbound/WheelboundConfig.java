package com.wheelbound;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("wheelbound")
public interface WheelboundConfig extends Config
{
    @ConfigItem(keyName = "limitBossesToMyLevel", name = "Limit bosses to my level",
        description = "Use recommended real stats and Slayer requirements. Gear, quests and access are not checked.", position = 0)
    default boolean limitBossesToMyLevel() { return true; }

    @ConfigItem(keyName = "excludeLevel99Skills", name = "Exclude level 99 skills",
        description = "Exclude skills with a real level of 99.", position = 1)
    default boolean excludeLevel99Skills() { return true; }
}

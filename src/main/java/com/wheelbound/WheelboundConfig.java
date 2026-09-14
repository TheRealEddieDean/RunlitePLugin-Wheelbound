package com.wheelbound;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;

@ConfigGroup("wheelbound")
public interface WheelboundConfig extends Config
{
    // Settings live in each wheel's sidebar. Retain the group for ConfigManager persistence.
}

package com.wheelbound;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("wheelbound")
public interface WheelboundConfig extends Config
{
	@ConfigItem(
		keyName = "spinOnLogin",
		name = "Spin on login",
		description = "Automatically spin the wheel when you log in"
	)
	default boolean spinOnLogin()
	{
		return false;
	}

	@ConfigItem(
		keyName = "showResultInChat",
		name = "Show result in chat",
		description = "Announce the selected activity in game chat"
	)
	default boolean showResultInChat()
	{
		return true;
	}
}

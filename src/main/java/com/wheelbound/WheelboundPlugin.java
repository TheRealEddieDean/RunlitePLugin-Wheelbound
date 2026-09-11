package com.wheelbound;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

@Slf4j
@PluginDescriptor(
	name = "Wheelbound",
	description = "Let the wheel decide your next Old School RuneScape activity.",
	tags = {"wheel", "randomizer", "activities", "challenges", "bossing", "skilling"}
)
public class WheelboundPlugin extends Plugin
{
	private static final List<String> DEFAULT_ACTIVITIES = List.of(
		"Bossing",
		"Slayer",
		"Skilling",
		"Clue Scrolls",
		"Questing",
		"Money Making"
	);

	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private WheelboundPanel panel;

	@Inject
	private WheelboundConfig config;

	private NavigationButton navigationButton;
	private String lastResult;
	private boolean loginSpinPending;

	@Override
	protected void startUp()
	{
		BufferedImage icon = ImageUtil.loadImageResource(getClass(), "wheelbound_icon.png");

		navigationButton = NavigationButton.builder()
			.tooltip("Wheelbound")
			.icon(icon)
			.priority(5)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navigationButton);
		loginSpinPending = config.spinOnLogin();
	}

	@Override
	protected void shutDown()
	{
		if (navigationButton != null)
		{
			clientToolbar.removeNavigation(navigationButton);
		}

		navigationButton = null;
		lastResult = null;
		loginSpinPending = false;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN && loginSpinPending)
		{
			loginSpinPending = false;
			spin();
		}
		else if (event.getGameState() == GameState.LOGIN_SCREEN)
		{
			loginSpinPending = config.spinOnLogin();
		}
	}

	public void spin()
	{
		int index = ThreadLocalRandom.current().nextInt(DEFAULT_ACTIVITIES.size());
		lastResult = DEFAULT_ACTIVITIES.get(index);
		panel.setResult(lastResult);

		if (config.showResultInChat() && client.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(
				ChatMessageType.GAMEMESSAGE,
				"",
				"Wheelbound chose: " + lastResult,
				null
			);
		}

		log.info("Wheelbound selected activity: {}", lastResult);
	}

	public String getLastResult()
	{
		return lastResult;
	}

	@Provides
	WheelboundConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(WheelboundConfig.class);
	}
}

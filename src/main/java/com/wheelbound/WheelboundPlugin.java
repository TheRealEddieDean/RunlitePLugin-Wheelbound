package com.wheelbound;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Graphics2D;
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
		navigationButton = NavigationButton.builder()
			.tooltip("Wheelbound")
			.icon(createIcon())
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

	private BufferedImage createIcon()
	{
		BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillOval(3, 3, 26, 26);
		graphics.setColor(Color.DARK_GRAY);
		graphics.drawOval(3, 3, 26, 26);
		graphics.drawLine(16, 5, 16, 27);
		graphics.drawLine(5, 16, 27, 16);
		graphics.dispose();
		return image;
	}

	@Provides
	WheelboundConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(WheelboundConfig.class);
	}
}

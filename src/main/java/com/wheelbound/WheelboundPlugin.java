package com.wheelbound;

import com.google.inject.Provides;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.events.RuneScapeProfileChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

@Slf4j
@PluginDescriptor(name = "Wheelbound", description = "Choose a boss or skill with icon wheels and optional XP targets.",
    tags = {"wheel", "randomizer", "bossing", "skilling"})
public class WheelboundPlugin extends Plugin
{
    @Inject private Client client;
    @Inject private ClientThread clientThread;
    @Inject private ClientToolbar toolbar;
    @Inject private WheelboundConfig config;
    @Inject private ConfigManager settings;
    @Inject private WheelIconProvider icons;
    @Inject private WheelPopup popup;
    @Inject private net.runelite.client.ui.overlay.OverlayManager overlays;
    @Inject private net.runelite.client.input.MouseManager mouseManager;
    @Inject private net.runelite.client.input.KeyManager keyManager;
    private final BossData bossData = new BossData();
    private final CombatAchievementCache achievements = new CombatAchievementCache();
    private final AtomicBoolean caRefreshQueued = new AtomicBoolean();
    private final AtomicBoolean poolRefreshQueued = new AtomicBoolean();
    private WheelboundPanel panel;
    private NavigationButton navigation;
    private volatile boolean active;
    private volatile long session;
    private final java.util.Map<net.runelite.api.Skill, Integer> levels = new java.util.EnumMap<>(net.runelite.api.Skill.class);

    @Override protected void startUp() throws Exception
    {
        active = true; session++;
        // RuneLite invokes plugin lifecycle methods on the EDT already.
        panel = new WheelboundPanel(key -> settings.getConfiguration("wheelbound", key),
            (key, value) -> settings.setConfiguration("wheelbound", key, value));
        panel.setAction(this::request);
        panel.setPopup(popup);
        overlays.add(popup);
        mouseManager.registerMouseListener(popup.mouse);
        mouseManager.registerMouseWheelListener(popup.mouseWheel);
        keyManager.registerKeyListener(popup);
        navigation = NavigationButton.builder().tooltip("Wheelbound").icon(WheelStyle.createIcon())
            .priority(5).panel(panel).build();
        toolbar.addNavigation(navigation);
        clientThread.invokeLater(() -> { if (active) { resetAccount(); queueCaRefresh(); } });
    }

    @Override protected void shutDown() throws Exception
    {
        active = false; session++;
        achievements.reset(); bossData.clear(); levels.clear();
        caRefreshQueued.set(false); poolRefreshQueued.set(false);
        popup.hide();
        overlays.remove(popup);
        mouseManager.unregisterMouseListener(popup.mouse);
        mouseManager.unregisterMouseWheelListener(popup.mouseWheel);
        keyManager.unregisterKeyListener(popup);
        if (panel != null)
        {
            panel.setAction(ignored -> {}); panel.reset(); panel = null;
        }
        if (navigation != null)
        {
            toolbar.removeNavigation(navigation); navigation = null;
        }
    }

    @Subscribe public void onGameStateChanged(GameStateChanged event)
    {
        switch (event.getGameState())
        {
            case LOGGED_IN: queueCaRefresh(); break;
            case LOGIN_SCREEN: case HOPPING: case CONNECTION_LOST: case LOGGING_IN:
                resetAccount(); break;
            default: break;
        }
    }

    @Subscribe public void onRuneScapeProfileChanged(RuneScapeProfileChanged event)
    {
        // Invalidate outstanding UI responses immediately, before the queued local read.
        session++;
        clientThread.invokeLater(() -> { if (active) { resetAccount(); queueCaRefresh(); } });
    }

    @Subscribe public void onProfileChanged(ProfileChanged event)
    {
        session++;
        clientThread.invokeLater(() -> { if (active) { resetAccount(); queueCaRefresh(); } });
    }

    @Subscribe public void onVarbitChanged(VarbitChanged event)
    {
        if (CombatAchievementCache.isCompletionVarp(event.getVarpId())) { queueCaRefresh(); }
    }

    @Subscribe public void onStatChanged(StatChanged event)
    {
        Integer previous = levels.put(event.getSkill(), event.getLevel());
        if (previous == null || previous != event.getLevel()) { queuePoolRefresh(); }
    }

    @Subscribe public void onConfigChanged(ConfigChanged event)
    {
        if ("wheelbound".equals(event.getGroup()) &&
            ("limitBossesToMyLevel".equals(event.getKey()) || "excludeLevel99Skills".equals(event.getKey())))
        { queuePoolRefresh(); }
    }

    private boolean loggedIn() { return client.getGameState() == GameState.LOGGED_IN && client.getLocalPlayer() != null; }
    private String identity()
    {
        String profile = settings.getRSProfileKey();
        return loggedIn() && profile != null ? client.getAccountHash() + ":" + profile : null;
    }

    private void resetAccount()
    {
        session++; achievements.reset(); levels.clear();
        long epoch = session;
        SwingUtilities.invokeLater(() -> {
            if (active && session == epoch) { panel.reset(); request(false); }
        });
    }

    /** Coalesces a burst of completion-varp events; never reads CAs per tick or per spin. */
    private void queueCaRefresh()
    {
        if (!active || !caRefreshQueued.compareAndSet(false, true)) { return; }
        clientThread.invokeLater(() -> {
            caRefreshQueued.set(false);
            if (!active) { return; }
            achievements.reset();
            if (loggedIn())
            {
                try { achievements.refresh(identity(), BossCatalog.ALL, bossData.load(client), client::getVarpValue); }
                catch (RuntimeException ex) { log.debug("Local combat achievement data is unavailable", ex); }
            }
            queuePoolRefresh();
        });
    }

    private void queuePoolRefresh()
    {
        if (!active || !poolRefreshQueued.compareAndSet(false, true)) { return; }
        SwingUtilities.invokeLater(() -> {
            poolRefreshQueued.set(false);
            if (active) { request(false); }
        });
    }

    /** Preferences are captured on the EDT; all player reads and sprite loading use the client thread. */
    private void request(boolean spin)
    {
        if (!active) { return; }
        String mode = panel.mode();
        boolean raids = panel.includeRaids(), incomplete = panel.incompleteOnly();
        long revision = panel.generation(), epoch = session;
        clientThread.invokeLater(() -> {
            if (!active || session != epoch) { return; }
            List<WheelEntry> entries;
            String message;
            boolean loggedIn = loggedIn();
            if (mode.equals("Bossing"))
            {
                String account = identity();
                List<BossDefinition> eligible = WheelEligibility.bosses(BossCatalog.ALL, raids, incomplete,
                    config.limitBossesToMyLevel(), loggedIn, client::getRealSkillLevel,
                    boss -> achievements.hasIncomplete(account, boss));
                entries = eligible.stream().map(icons::boss).collect(Collectors.toList());
                message = eligible.size() + " eligible bosses. All appear in the centered wheel with equal odds.";
                if (config.limitBossesToMyLevel())
                {
                    long unknown = BossCatalog.ALL.stream().filter(b -> b.profile == null && (raids || !b.raid)).count();
                    if (unknown > 0) { message += " " + unknown + " lack reviewed level recommendations."; }
                }
                if (!loggedIn && (config.limitBossesToMyLevel() || incomplete))
                { message = "Log in to check your levels and Combat Achievements."; }
                else if (incomplete && !achievements.isReady(account))
                { message = "Local Combat Achievement data is unavailable. Try All bosses or log in again."; }
                else if (entries.isEmpty())
                { message = "No bosses match the current filters. Try All bosses, include raids, or adjust the master level setting."; }
            }
            else
            {
                entries = WheelEligibility.skills(config.excludeLevel99Skills(), loggedIn, client::getRealSkillLevel)
                    .stream().map(icons::skill).collect(Collectors.toList());
                message = entries.size() + " eligible skills. Open the centered wheel to spin.";
                if (!loggedIn && config.excludeLevel99Skills()) { message = "Log in to exclude level 99 skills."; }
                else if (entries.isEmpty()) { message = "All skills are level 99! Turn off Exclude level 99 skills to include them."; }
            }
            List<WheelEntry> snapshot = List.copyOf(entries);
            String info = message;
            SwingUtilities.invokeLater(() -> {
                if (!active || epoch != session || revision != panel.generation()) { return; }
                if (spin) { panel.spinResponse(snapshot, info, revision); }
                else { panel.updatePool(snapshot, info, revision); }
            });
        });
    }

    @Provides WheelboundConfig provideConfig(ConfigManager manager) { return manager.getConfig(WheelboundConfig.class); }
}

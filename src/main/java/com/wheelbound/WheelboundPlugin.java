package com.wheelbound;

import com.google.inject.Provides;
import java.util.List;
import java.util.Set;
import java.util.EnumSet;
import java.util.function.Predicate;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.events.RuneScapeProfileChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

@Slf4j
@PluginDescriptor(name = "Wheelbound", description = "Choose a boss, skill, quest, pet hunt or unfinished Combat Achievement encounter.",
    tags = {"wheel", "randomizer", "bossing", "skilling", "combat achievements", "pets", "quests"})
public class WheelboundPlugin extends Plugin
{
    @Inject private Client client;
    @Inject private ClientThread clientThread;
    @Inject private ClientToolbar toolbar;
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
    private boolean poolDirty;
    private volatile WheelboundPanel panel;
    private NavigationButton navigation;
    private volatile boolean active;
    private final AtomicLong session = new AtomicLong();
    private final java.util.Map<net.runelite.api.Skill, Integer> levels = new java.util.EnumMap<>(net.runelite.api.Skill.class);

    @Override protected void startUp()
    {
        long epoch = session.incrementAndGet();
        active = true;
        // RuneLite invokes plugin lifecycle methods on the EDT already.
        panel = new WheelboundPanel(key -> settings.getConfiguration("wheelbound", key),
            (key, value) -> settings.setConfiguration("wheelbound", key, value));
        panel.setAction(this::request);
        panel.setRefreshAction(this::queueCaRefresh);
        panel.setPopup(popup);
        overlays.add(popup);
        mouseManager.registerMouseListener(popup.mouse);
        mouseManager.registerMouseWheelListener(popup.mouseWheel);
        keyManager.registerKeyListener(popup);
        navigation = NavigationButton.builder().tooltip("Wheelbound").icon(WheelStyle.createIcon())
            .priority(5).panel(panel).build();
        toolbar.addNavigation(navigation);
        clientThread.invokeLater(() -> { if (active && session.get() == epoch) { resetAccount(); queueCaRefresh(); } });
    }

    @Override protected void shutDown()
    {
        active = false;
        long epoch = session.incrementAndGet();
        clientThread.invokeLater(() -> {
            if (!active && session.get() == epoch)
            { achievements.reset(); bossData.clear(); levels.clear(); poolDirty = false; }
        });
        caRefreshQueued.set(false); poolRefreshQueued.set(false);
        popup.hide();
        overlays.remove(popup);
        mouseManager.unregisterMouseListener(popup.mouse);
        mouseManager.unregisterMouseWheelListener(popup.mouseWheel);
        keyManager.unregisterKeyListener(popup);
        if (panel != null)
        {
            panel.setAction(ignored -> {}); panel.setRefreshAction(() -> {}); panel.reset(); panel = null;
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

    @Subscribe public void onRuneScapeProfileChanged(RuneScapeProfileChanged ignored)
    {
        // Invalidate outstanding UI responses immediately, before the queued local read.
        session.incrementAndGet();
        WheelboundPanel target = panel;
        clientThread.invokeLater(() -> { if (active && target != null && panel == target) { resetAccount(); queueCaRefresh(); } });
    }

    // Invoked by RuneLite's event bus.
    @SuppressWarnings("unused")
    @Subscribe public void onProfileChanged(ProfileChanged ignored)
    {
        session.incrementAndGet();
        WheelboundPanel target = panel;
        SwingUtilities.invokeLater(() -> {
            if (active && target != null && panel == target)
            { target.reloadPreferences(key -> settings.getConfiguration("wheelbound", key)); request(false); }
        });
        clientThread.invokeLater(() -> { if (active && target != null && panel == target) { resetAccount(); queueCaRefresh(); } });
    }

    @Subscribe public void onVarbitChanged(VarbitChanged event)
    {
        if (CombatAchievementCache.isCompletionVarp(event.getVarpId())) { queueCaRefresh(); }
        else { poolDirty = true; }
    }

    @Subscribe public void onGameTick(GameTick ignored)
    {
        if (poolDirty) { poolDirty = false; queuePoolRefresh(); }
    }

    // Invoked by RuneLite's event bus.
    @SuppressWarnings("unused")
    @Subscribe public void onStatChanged(StatChanged event)
    {
        Integer previous = levels.put(event.getSkill(), event.getLevel());
        if (previous == null || previous != event.getLevel()) { poolDirty = true; }
    }

    private boolean loggedIn() { return client.getGameState() == GameState.LOGGED_IN && client.getLocalPlayer() != null; }
    private String identity()
    {
        String profile = settings.getRSProfileKey();
        return loggedIn() && profile != null ? client.getAccountHash() + ":" + profile : null;
    }

    private void resetAccount()
    {
        long epoch = session.incrementAndGet();
        achievements.reset(); levels.clear(); poolDirty = false;
        SwingUtilities.invokeLater(() -> {
            if (active && session.get() == epoch) { panel.reset(); request(false); }
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
                try { achievements.refresh(identity(), bossData.load(client), client::getVarpValue); }
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
            if (active && panel != null) { request(false); }
        });
    }

    /** Preferences are captured on the EDT; all player reads and sprite loading use the client thread. */
    private void request(boolean spin)
    {
        if (!active || panel == null) { return; }
        if (panel.wheelType() == WheelType.CUSTOM) { panel.refreshCustomPool(spin); return; }
        WheelType type = panel.wheelType();
        Set<WheelFilter> filters = panel.selectedFilters();
        long revision = panel.generation(), epoch = session.get();
        clientThread.invokeLater(() -> {
            if (!active || session.get() != epoch) { return; }
            List<WheelEntry> entries;
            String message;
            WheelEntry completion = null;
            boolean loggedIn = loggedIn();
            boolean matchTask = filters.contains(type == WheelType.BOSSING ? WheelFilter.BOSS_TASK : WheelFilter.CA_TASK);
            AccountAccess.SlayerTask task = AccountAccess.SlayerTask.unavailable();
            if (loggedIn && type != WheelType.SKILLING && matchTask)
            {
                try { task = AccountAccess.slayerTask(client); }
                catch (RuntimeException ex) { log.debug("Slayer assignment is unavailable", ex); }
            }
            AccountAccess.SlayerTask assignment = task;
            if (type == WheelType.BOSSING)
            {
                boolean accountFilter = filters.contains(WheelFilter.ACCOUNT);
                Predicate<BossDefinition> access = AccountAccess.requirements(client);
                List<BossDefinition> eligible = WheelEligibility.bosses(BossCatalog.ALL,
                    filters.contains(WheelFilter.BOSS_RAIDS), false, accountFilter, loggedIn,
                    client::getRealSkillLevel, boss -> true).stream()
                    .filter(b -> BossDifficulty.included(b, filters))
                    .filter(b -> filters.contains(WheelFilter.MIMIC) || !b.name.equals("Mimic"))
                    .filter(b -> AccountAccess.taskAllows(b.name,
                        matchTask ? assignment : AccountAccess.SlayerTask.unavailable()))
                    .filter(b -> {
                        if (!accountFilter) { return true; }
                        try { return access.test(b); }
                        catch (RuntimeException ex) { log.debug("Access data is unavailable for {}", b.name, ex); return false; }
                    }).collect(Collectors.toUnmodifiableList());
                entries = eligible.stream().map(icons::boss).collect(Collectors.toList());
                message = eligible.size() + " eligible bosses. All appear in the centered wheel with equal odds.";
                if (!loggedIn && accountFilter)
                { message = "Log in to check your levels and quest access, or turn off Filter by Skill Level."; }
                else if (entries.isEmpty())
                { message = "No bosses match. Adjust the account or pool filters."; }
            }
            else if (type == WheelType.SKILLING)
            {
                boolean exclude99 = filters.contains(WheelFilter.MAXED_SKILLS);
                entries = WheelEligibility.skills(filters.contains(WheelFilter.COMBAT_SKILLS), exclude99, loggedIn, client::getRealSkillLevel)
                    .stream().map(icons::skill).collect(Collectors.toList());
                message = entries.size() + " eligible skills. Open the centered wheel to spin.";
                if (!loggedIn && exclude99) { message = "Log in to exclude level 99 skills."; }
                else if (entries.isEmpty()) { message = "No skills match. Adjust the combat or level-99 filters."; }
            }
            else if (type == WheelType.PET_HUNTING)
            {
                entries = List.of();
                boolean excludeOwned = filters.contains(WheelFilter.PET_OWNED);
                if (excludeOwned && !loggedIn) { message = "Log in to exclude pets already owned."; }
                else
                {
                    try
                    {
                        Set<Integer> owned = excludeOwned ? OwnedPets.read(client) : Set.of();
                        entries = WheelEligibility.pets(filters).stream()
                            .filter(pet -> !excludeOwned || !OwnedPets.contains(owned, pet))
                            .map(icons::pet).collect(Collectors.toList());
                        message = entries.isEmpty() ? "No pets match your ownership and source filters."
                            : entries.size() + " eligible pets. Each has equal wheel odds.";
                    }
                    catch (RuntimeException ex)
                    { log.debug("Pet ownership data is unavailable", ex); message = "Pet data unavailable. Change a checkbox to retry."; }
                }
            }
            else if (type == WheelType.QUESTING)
            {
                entries = List.of();
                if (!loggedIn) { message = "Log in to load your unfinished quests."; }
                else
                {
                    try
                    {
                        QuestPool quests = QuestPool.read(client, filters, icons);
                        entries = quests.entries;
                        message = quests.allComplete ? QuestPool.COMPLETE : entries.isEmpty()
                            ? "No unfinished quests match. Adjust difficulty or requirement filters."
                            : entries.size() + " unfinished quests. Each has equal wheel odds.";
                        if (quests.allComplete) { completion = icons.quest(-1, "All quests completed!"); }
                    }
                    catch (RuntimeException ex)
                    { log.debug("Quest data unavailable", ex); message = "Quest data unavailable. Change a checkbox to retry."; }
                }
            }
            else
            {
                String account = identity();
                Set<CaTier> excluded = EnumSet.noneOf(CaTier.class);
                for (WheelFilter filter : WheelFilter.values()) { if (filter.tier != null && !filters.contains(filter)) { excluded.add(filter.tier); } }
                entries = List.of();
                if (!loggedIn) { message = "Log in to load your unfinished Combat Achievements."; }
                else if (!achievements.isReady(account)) { message = "Local Combat Achievement data is unavailable. Change a checkbox to retry."; }
                else
                {
                    entries = WheelEligibility.achievements(bossData.encounters(client), !filters.contains(WheelFilter.CA_BOSSES),
                        !filters.contains(WheelFilter.CA_RAIDS), e -> achievements.hasIncomplete(account, e, excluded),
                        e -> !matchTask || AccountAccess.taskAllows(e.name, assignment))
                        .stream().map(e -> {
                            WheelEntry entry = icons.encounter(e);
                            return filters.contains(WheelFilter.CA_SPECIFIC)
                                ? entry.withTasks(achievements.incompleteTasks(account, e, excluded)) : entry;
                        }).collect(Collectors.toUnmodifiableList());
                    message = entries.isEmpty() ? "No unfinished tasks match. Include another tier or encounter category."
                        : entries.size() + " encounters with unfinished tasks in your included tiers.";
                }
            }
            List<WheelEntry> snapshot = WheelEntry.groupRaids(entries);
            String info = message;
            WheelEntry completedQuests = completion;
            SwingUtilities.invokeLater(() -> {
                if (!active || epoch != session.get() || revision != panel.generation()) { return; }
                if (spin) { panel.spinResponse(snapshot, info, revision); }
                else { panel.updatePool(snapshot, info, revision); }
                panel.setQuestCompletion(completedQuests);
            });
        });
    }

    // Invoked by Guice when resolving the configuration binding.
    @SuppressWarnings("unused")
    @Provides WheelboundConfig provideConfig(ConfigManager manager) { return manager.getConfig(WheelboundConfig.class); }
}

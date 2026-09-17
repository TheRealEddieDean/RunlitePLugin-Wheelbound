package com.wheelbound;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.events.RuneScapeProfileChanged;
import org.junit.Test;
import static org.junit.Assert.*;

public class WheelboundEventsTest
{
    @Test public void ordinaryVarbitBurstsWaitForOneGameTick() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            try
            {
                WheelboundPlugin plugin = new WheelboundPlugin();
                set(plugin, "active", true);
                VarbitChanged event = new VarbitChanged(); event.setVarpId(3138);
                for (int i = 0; i < 100; i++) { plugin.onVarbitChanged(event); }
                java.util.concurrent.atomic.AtomicBoolean queued =
                    (java.util.concurrent.atomic.AtomicBoolean)get(plugin, "poolRefreshQueued");
                assertFalse(queued.get()); assertEquals(true, get(plugin, "poolDirty"));
                plugin.onGameTick(new net.runelite.api.events.GameTick());
                assertTrue(queued.get()); assertEquals(false, get(plugin, "poolDirty"));
                plugin.onGameTick(new net.runelite.api.events.GameTick());
                assertEquals(false, get(plugin, "poolDirty"));
                set(plugin, "active", false);
            }
            catch (Exception ex) { throw new AssertionError(ex); }
        });
    }

    @Test public void enablingAndDisablingOnEdtAddsAndRemovesSidebar() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            try
            {
                WheelboundPlugin plugin = new WheelboundPlugin();
                net.runelite.client.ui.ClientToolbar toolbar = org.mockito.Mockito.mock(net.runelite.client.ui.ClientToolbar.class);
                set(plugin, "toolbar", toolbar);
                set(plugin, "settings", org.mockito.Mockito.mock(net.runelite.client.config.ConfigManager.class));
                QueuedThread thread = new QueuedThread();
                set(plugin, "clientThread", thread);
                set(plugin, "popup", new WheelPopup(null));
                set(plugin, "overlays", org.mockito.Mockito.mock(net.runelite.client.ui.overlay.OverlayManager.class));
                set(plugin, "mouseManager", org.mockito.Mockito.mock(net.runelite.client.input.MouseManager.class));
                set(plugin, "keyManager", org.mockito.Mockito.mock(net.runelite.client.input.KeyManager.class));
                for (int i = 0; i < 2; i++)
                {
                    plugin.startUp();
                    net.runelite.client.ui.NavigationButton button = (net.runelite.client.ui.NavigationButton)get(plugin, "navigation");
                    assertNotNull(button.getIcon());
                    assertSame(get(plugin, "panel"), button.getPanel());
                    org.mockito.Mockito.verify(toolbar).addNavigation(button);
                    CombatAchievementCache cache = (CombatAchievementCache)get(plugin, "achievements");
                    cache.refresh("A", java.util.Map.of("Obor", List.of(0)), id -> 0);
                    plugin.shutDown();
                    assertTrue("Cleanup waits for the client thread", cache.isReady("A"));
                    thread.work.forEach(Runnable::run);
                    thread.work.clear();
                    assertFalse("Client-thread cleanup clears account data", cache.isReady("A"));
                    org.mockito.Mockito.verify(toolbar).removeNavigation(button);
                    assertNull(get(plugin, "panel"));
                    assertNull(get(plugin, "navigation"));
                }
                // Shutdown must also tolerate an incomplete startup.
                plugin.shutDown();
            }
            catch (Exception e) { throw new AssertionError(e); }
        });
    }

    @Test public void completionEventBurstsQueueOneRefreshAndIgnoreOtherVarps() throws Exception
    {
        WheelboundPlugin plugin = new WheelboundPlugin();
        QueuedThread thread = new QueuedThread();
        set(plugin, "clientThread", thread); set(plugin, "active", true);
        VarbitChanged event = new VarbitChanged(); event.setVarpId(3138);
        plugin.onVarbitChanged(event);
        assertEquals(0, thread.work.size());
        for (int id : BossData.COMPLETION)
        {
            event.setVarpId(id); plugin.onVarbitChanged(event);
        }
        assertEquals(1, thread.work.size());
        set(plugin, "active", false);
        thread.work.remove(0).run();
    }

    @Test public void loginRefreshesAndProfileChangeInvalidatesPendingResponses() throws Exception
    {
        WheelboundPlugin plugin = new WheelboundPlugin();
        QueuedThread thread = new QueuedThread();
        set(plugin, "clientThread", thread); set(plugin, "active", true);
        GameStateChanged login = new GameStateChanged(); login.setGameState(GameState.LOGGED_IN);
        plugin.onGameStateChanged(login);
        assertEquals(1, thread.work.size());
        long before = ((java.util.concurrent.atomic.AtomicLong)get(plugin, "session")).get();
        plugin.onRuneScapeProfileChanged(new RuneScapeProfileChanged("A", "B"));
        assertTrue(((java.util.concurrent.atomic.AtomicLong)get(plugin, "session")).get() > before);
        assertEquals(2, thread.work.size());
        set(plugin, "active", false);
        thread.work.forEach(Runnable::run);
    }

    @Test public void logoutClearsCachedEligibilityAndCancelsThePanel() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            try
            {
                WheelboundPlugin plugin = new WheelboundPlugin();
                set(plugin, "clientThread", new QueuedThread()); set(plugin, "active", true);
                set(plugin, "panel", new WheelboundPanel(k -> null, (k, v) -> {}));
                CombatAchievementCache cache = (CombatAchievementCache)get(plugin, "achievements");
                cache.refresh("A", java.util.Map.of("Obor", List.of(0)), id -> 0);
                assertTrue(cache.isReady("A"));
                GameStateChanged logout = new GameStateChanged(); logout.setGameState(GameState.LOGIN_SCREEN);
                plugin.onGameStateChanged(logout);
                assertFalse(cache.isReady("A"));
                set(plugin, "active", false);
            }
            catch (Exception e) { throw new AssertionError(e); }
        });
    }

    private static final class QueuedThread extends ClientThread
    {
        final List<Runnable> work = new ArrayList<>();
        @Override public void invokeLater(Runnable action) { work.add(action); }
    }
    private static Object get(Object target, String name) throws Exception
    {
        Field field = target.getClass().getDeclaredField(name); field.setAccessible(true); return field.get(target);
    }
    private static void set(Object target, String name, Object value) throws Exception
    {
        Field field = target.getClass().getDeclaredField(name); field.setAccessible(true); field.set(target, value);
    }
}

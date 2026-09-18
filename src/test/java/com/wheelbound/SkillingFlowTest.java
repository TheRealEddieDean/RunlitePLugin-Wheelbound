package com.wheelbound;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import javax.swing.*;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;
import org.junit.Test;
import static org.junit.Assert.*;

public class SkillingFlowTest
{
    @Test public void checklistUpdatesOpenWheelInPlaceAndPersistsExclusions() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            Map<String, String> saved = new HashMap<>();
            WheelboundPanel panel = new WheelboundPanel(net.runelite.http.api.RuneLiteAPI.GSON, saved::get, (k, v) -> saved.put(k, v.toString()));
            WheelPopup popup = new WheelPopup(null); panel.setPopup(popup);
            List<WheelEntry> entries = List.of(new WheelEntry("OBOR", "Obor", null, 1),
                new WheelEntry("BRYOPHYTA", "Bryophyta", null, 1));
            panel.updatePool(entries, "test", panel.generation()); panel.openWheel();
            button(panel, "Obor").doClick();
            assertTrue(popup.isOpen()); assertEquals(1, popup.entryCount());
            assertEquals("BRYOPHYTA", panel.primaryWheel().entries().get(0).id);
            assertEquals("OBOR", saved.get("excludedBosses"));
            button(panel, "Bryophyta").doClick();
            assertTrue(popup.isOpen()); assertEquals(0, popup.entryCount());
            assertFalse(panel.primaryWheel().canSpin());
            button(panel, "Obor").doClick();
            assertEquals(1, popup.entryCount()); assertTrue(panel.primaryWheel().canSpin());
            panel.setAction(spin -> panel.updatePool(entries, "refreshed", panel.generation()));
            button(panel, "Include raids").doClick();
            assertTrue(popup.isOpen()); assertEquals(1, popup.entryCount());
            WheelboundPanel restored = new WheelboundPanel(net.runelite.http.api.RuneLiteAPI.GSON, saved::get, (k, v) -> {});
            restored.updatePool(entries, "test", restored.generation());
            assertEquals("OBOR", restored.primaryWheel().entries().get(0).id);
            panel.reset();
        });
    }

    @Test public void sidebarActivationOpensWheelAndHubCanSpinAgain() throws Exception
    {
        WheelboundPanel[] panel = new WheelboundPanel[1];
        WheelPopup popup = new WheelPopup(null);
        SwingUtilities.invokeAndWait(() -> {
            panel[0] = new WheelboundPanel(net.runelite.http.api.RuneLiteAPI.GSON, k -> k.equals("excludedBosses") ? "BRYOPHYTA" : null, (k, v) -> {});
            panel[0].setPopup(popup);
            List<WheelEntry> entries = List.of(new WheelEntry("OBOR", "Obor",
                new SkillIconManager().getSkillImage(Skill.ATTACK), 1), new WheelEntry("BRYOPHYTA", "Bryophyta", null, 1));
            panel[0].setAction(spin -> { if (spin) { panel[0].spinResponse(entries, "test", panel[0].generation()); } });
            panel[0].updatePool(entries, "test", panel[0].generation());
            assertNull(findButton(panel[0], "SPIN"));
            panel[0].onActivate();
            assertTrue(popup.isOpen()); assertFalse(panel[0].busy());
            popup.keyPressed(new java.awt.event.KeyEvent(new Canvas(), java.awt.event.KeyEvent.KEY_PRESSED,
                0, 0, java.awt.event.KeyEvent.VK_SPACE, ' '));
        });
        SwingUtilities.invokeAndWait(() -> {
            assertTrue(popup.isOpen()); assertTrue(panel[0].busy());
            assertFalse(button(panel[0], "Obor").isEnabled());
            assertEquals(1, panel[0].primaryWheel().entries().size());
        });
        awaitEdtCondition(() -> !panel[0].busy());
        SwingUtilities.invokeAndWait(() -> {
            assertFalse(panel[0].busy()); assertTrue(popup.hasResult());
            assertTrue(hasLabel(panel[0], "Obor"));
            popup.keyPressed(new java.awt.event.KeyEvent(new Canvas(), java.awt.event.KeyEvent.KEY_PRESSED,
                0, 0, java.awt.event.KeyEvent.VK_ENTER, '\n'));
        });
        SwingUtilities.invokeAndWait(() -> {
            assertTrue(panel[0].busy());
            panel[0].onDeactivate();
            assertFalse(popup.isOpen()); assertFalse(panel[0].busy());
            panel[0].onActivate();
            assertTrue(popup.isOpen());
            panel[0].selectWheel(WheelType.SKILLING);
            assertTrue(popup.isOpen()); assertEquals(0, popup.entryCount());
            panel[0].updatePool(List.of(), "empty", panel[0].generation());
            assertFalse(panel[0].primaryWheel().canSpin());
            panel[0].reset(); assertFalse(popup.hasResult());
        });
    }

    @Test public void hubClickStartsOnceAndCancellationDoesNotComplete() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            WheelComponent wheel = new WheelComponent();
            wheel.setSize(209, 224);
            List<WheelEntry> entries = List.of(new WheelEntry("a", "A", null, 1));
            wheel.setEntries(entries); wheel.setAvailable(true);
            AtomicInteger starts = new AtomicInteger();
            wheel.setSpinAction(() -> { starts.incrementAndGet(); wheel.animate(entries, 0, () -> fail("Cancelled spin completed")); });
            click(wheel, 5, 5);
            assertEquals(0, starts.get());
            click(wheel, 104, 112);
            assertTrue(wheel.busy());
            click(wheel, 104, 112);
            assertEquals(1, starts.get());
            wheel.cancel();
            assertFalse(wheel.busy());
            assertFalse(wheel.canSpin());
        });
    }

    @Test public void filtersPersistAndStaleResponsesAreIgnored() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            Map<String, String> preferences = new HashMap<>();
            WheelboundPanel panel = new WheelboundPanel(net.runelite.http.api.RuneLiteAPI.GSON, preferences::get, (k, v) -> preferences.put(k, v.toString()));
            long revision = panel.generation();
            button(panel, "Include raids").doClick();
            assertFalse(panel.selected(WheelFilter.BOSS_RAIDS));
            assertTrue(panel.selected(WheelFilter.BOSS_EASY));
            assertEquals("false", preferences.get("bossIncludeRaids"));
            panel.updatePool(List.of(new WheelEntry("a", "A", null, 1)), "stale", revision);
            assertFalse(panel.primaryWheel().canSpin());
            panel.updatePool(List.of(), "No matches", panel.generation());
            assertFalse(panel.primaryWheel().canSpin());
            button(panel, "Include Mimic").doClick();
            WheelboundPanel restored = new WheelboundPanel(net.runelite.http.api.RuneLiteAPI.GSON, preferences::get, (k, v) -> {});
            assertFalse(restored.selected(WheelFilter.BOSS_RAIDS)); assertFalse(restored.selected(WheelFilter.MIMIC));
            assertFalse(button(restored, "Include XP goal").isSelected());
        });
    }

    @Test public void manualXpWheelWaitsForUserAndPreservesResultOnPoolRefresh() throws Exception
    {
        WheelboundPanel[] holder = new WheelboundPanel[1];
        WheelPopup popup = new WheelPopup(null);
        SwingUtilities.invokeAndWait(() -> {
            WheelboundPanel panel = new WheelboundPanel(net.runelite.http.api.RuneLiteAPI.GSON, k -> k.equals("selectedWheel") ? "Skilling" : k.equals("includeXpGoal") ? "true" : null, (k, v) -> {});
            holder[0] = panel;
            panel.setPopup(popup);
            List<WheelEntry> entries = List.of(new WheelEntry("MINING", "Mining", null, 1));
            panel.setAction(spin -> { if (spin) { panel.spinResponse(entries, "test", panel.generation()); } });
            panel.updatePool(entries, "test", panel.generation());
            panel.openWheel();
            panel.primaryWheel().requestSpin();
            assertTrue(panel.busy());
            panel.updatePool(List.of(), "No eligible skills", panel.generation());
        });
        awaitEdtCondition(() -> holder[0].secondaryWheel().canSpin());
        Thread.sleep(250);
        SwingUtilities.invokeAndWait(() -> {
            assertTrue(holder[0].busy());
            assertFalse(holder[0].secondaryWheel().busy());
            assertTrue(holder[0].secondaryWheel().canSpin());
            popup.keyPressed(new java.awt.event.KeyEvent(new Canvas(), java.awt.event.KeyEvent.KEY_PRESSED,
                0, 0, java.awt.event.KeyEvent.VK_SPACE, ' '));
        });
        SwingUtilities.invokeAndWait(() -> {
            assertTrue(holder[0].secondaryWheel().busy());
            assertFalse(holder[0].secondaryWheel().canSpin());
        });
        awaitEdtCondition(() -> !holder[0].busy());
        SwingUtilities.invokeAndWait(() -> {
            assertFalse(holder[0].busy());
            assertTrue(hasLabel(holder[0], "Mining"));
            assertTrue(hasLabel(holder[0], "Gain "));
            assertFalse(holder[0].primaryWheel().canSpin());
            holder[0].reset();
            assertFalse(hasLabel(holder[0], "Mining"));
        });
    }

    @Test public void checklistGrowsWithViewportAndSmallWindowsCanScroll() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            WheelboundPanel panel = new WheelboundPanel(net.runelite.http.api.RuneLiteAPI.GSON, k -> null, (k, v) -> {});
            JViewport viewport = new JViewport(); viewport.setView(panel);
            viewport.setSize(242, 900); viewport.doLayout(); layout(panel);
            BossChecklist list = checklist(panel);
            assertNotNull(list);
            int tall = list.getHeight();
            assertTrue(tall > 200);
            viewport.setSize(242, 1100); viewport.doLayout(); layout(panel);
            assertEquals(tall + 200, list.getHeight());
            panel.selectWheel(WheelType.COMBAT_ACHIEVEMENTS);
            viewport.setSize(242, 350); viewport.doLayout(); layout(panel);
            assertFalse(panel.getScrollableTracksViewportHeight());
            assertTrue(panel.getHeight() > viewport.getHeight());
        });
    }

    private static BossChecklist checklist(Container parent)
    {
        for (Component child : parent.getComponents())
        {
            if (!child.isVisible()) { continue; }
            if (child instanceof BossChecklist) { return (BossChecklist)child; }
            if (child instanceof Container)
            {
                BossChecklist found = checklist((Container)child);
                if (found != null) { return found; }
            }
        }
        return null;
    }

    @Test public void renderSidebarForReview() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            try
            {
                net.runelite.client.ui.laf.RuneLiteLAF.setup();
                WheelboundPanel panel = new WheelboundPanel(net.runelite.http.api.RuneLiteAPI.GSON, k -> "selectedWheel".equals(k) ? "Skilling" : null, (k, v) -> {});
                SkillIconManager icons = new SkillIconManager();
                List<WheelEntry> entries = new ArrayList<>();
                for (Skill skill : Skill.values())
                {
                    entries.add(new WheelEntry(skill.name(), skill.getName(), icons.getSkillImage(skill), 1));
                }
                panel.updatePool(entries, "24 eligible skills. Click the center to spin.", panel.generation());
                assertTrue("The sidebar must never contain a wheel", wheels(panel).isEmpty());
                panel.setSize(242, 900); layout(panel);
                BufferedImage image = new BufferedImage(225, panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics(); panel.paint(g); g.dispose();
                File file = new File("build/reports/wheelbound-skilling.png");
                java.nio.file.Files.createDirectories(file.toPath().getParent()); ImageIO.write(image, "png", file);
                panel.selectWheel(WheelType.BOSSING);
                List<WheelEntry> bosses = new ArrayList<>();
                for (BossDefinition boss : BossCatalog.ALL.subList(0, 12))
                { bosses.add(new WheelEntry(boss.hiscore.name(), boss.name, null, 1)); }
                panel.updatePool(bosses, "12 sample entries. Boss sprites load from the game cache.", panel.generation());
                assertTrue("The sidebar must never contain a wheel", wheels(panel).isEmpty());
                panel.setSize(242, 900); layout(panel);
                image = new BufferedImage(225, panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                g = image.createGraphics(); panel.paint(g); g.dispose();
                ImageIO.write(image, "png", new File("build/reports/wheelbound-bossing-layout.png"));
                panel.selectWheel(WheelType.COMBAT_ACHIEVEMENTS);
                panel.updatePool(List.of(new WheelEntry("CA_1", "Bloodveld", null, 1),
                    new WheelEntry("CA_2", "Obor", null, 1), new WheelEntry("CA_3", "Vorkath", null, 1)),
                    "3 encounters with unfinished tasks.", panel.generation());
                panel.setSize(242, 900); layout(panel);
                image = new BufferedImage(225, panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                g = image.createGraphics(); panel.paint(g); g.dispose();
                ImageIO.write(image, "png", new File("build/reports/wheelbound-combat-achievements.png"));
                for (WheelFilter filter : WheelFilter.values())
                {
                    if (filter.wheel != WheelType.COMBAT_ACHIEVEMENTS) { continue; }
                    AbstractButton box = button(panel, filter.title);
                    assertNotNull(box);
                    assertTrue("Filter must fit its card: " + filter.title,
                        box.getY() + box.getHeight() <= box.getParent().getHeight());
                }
                panel.selectWheel(WheelType.QUESTING);
                panel.updatePool(List.of(new WheelEntry("QUEST_1", "Cook's Assistant", null, 1),
                    new WheelEntry("QUEST_2", "Dragon Slayer I", null, 1)), "2 unfinished quests.", panel.generation());
                panel.setSize(242, 900); layout(panel);
                image = new BufferedImage(242, panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                g = image.createGraphics(); panel.paint(g); g.dispose();
                ImageIO.write(image, "png", new File("build/reports/wheelbound-questing.png"));
                assertTrue("The sidebar must never contain a wheel", wheels(panel).isEmpty());
                for (WheelFilter filter : WheelFilter.values())
                {
                    if (filter.wheel != WheelType.QUESTING) { continue; }
                    AbstractButton box = visibleButton(panel, filter.title);
                    assertNotNull(box);
                    assertTrue(box.getY() + box.getHeight() <= box.getParent().getHeight());
                }
                panel.reset();
            }
            catch (Exception e) { throw new AssertionError(e); }
        });
    }

    /** Check Swing state on the EDT while the test thread waits with a bounded timeout. */
    private static void awaitEdtCondition(java.util.function.BooleanSupplier condition) throws Exception
    {
        java.util.concurrent.CountDownLatch ready = new java.util.concurrent.CountDownLatch(1);
        javax.swing.Timer timer = new javax.swing.Timer(25, event -> {
            if (condition.getAsBoolean()) { ready.countDown(); }
        });
        SwingUtilities.invokeAndWait(timer::start);
        try { assertTrue("Timed out waiting for the wheel", ready.await(8, java.util.concurrent.TimeUnit.SECONDS)); }
        finally { SwingUtilities.invokeAndWait(timer::stop); }
    }

    private static void click(WheelComponent wheel, int x, int y)
    {
        wheel.dispatchEvent(new MouseEvent(wheel, MouseEvent.MOUSE_PRESSED, 0, 0, x, y, 1, false, MouseEvent.BUTTON1));
        wheel.dispatchEvent(new MouseEvent(wheel, MouseEvent.MOUSE_RELEASED, 0, 0, x, y, 1, false, MouseEvent.BUTTON1));
    }
    private static List<WheelComponent> wheels(Container parent)
    {
        List<WheelComponent> found = new ArrayList<>();
        for (Component c : parent.getComponents())
        {
            if (c instanceof WheelComponent) { found.add((WheelComponent)c); }
            else if (c instanceof Container) { found.addAll(wheels((Container)c)); }
        }
        return found;
    }
    private static AbstractButton button(Container parent, String text)
    {
        AbstractButton found = findButton(parent, text);
        if (found == null) { throw new AssertionError("Missing button: " + text); }
        return found;
    }
    private static AbstractButton findButton(Container parent, String text)
    {
        for (Component c : parent.getComponents())
        {
            if (c instanceof AbstractButton && ((AbstractButton)c).getText().contains(text)) { return (AbstractButton)c; }
            if (c instanceof Container)
            {
                AbstractButton found = findButton((Container)c, text);
                if (found != null) { return found; }
            }
        }
        return null;
    }
    private static AbstractButton visibleButton(Container parent, String text)
    {
        for (Component child : parent.getComponents())
        {
            if (!child.isVisible()) { continue; }
            if (child instanceof AbstractButton && text.equals(((AbstractButton)child).getText()))
            { return (AbstractButton)child; }
            if (child instanceof Container)
            {
                AbstractButton found = visibleButton((Container)child, text);
                if (found != null) { return found; }
            }
        }
        return null;
    }

    private static boolean hasLabel(Container parent, String text)
    {
        for (Component c : parent.getComponents())
        {
            if (c instanceof JLabel && ((JLabel)c).getText() != null && ((JLabel)c).getText().contains(text)) { return true; }
            if (c instanceof Container && hasLabel((Container)c, text)) { return true; }
        }
        return false;
    }
    private static void layout(Container c)
    {
        c.doLayout();
        for (Component child : c.getComponents()) { if (child instanceof Container) { layout((Container)child); } }
    }
}

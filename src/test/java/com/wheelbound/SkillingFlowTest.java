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
            WheelboundPanel panel = new WheelboundPanel(saved::get, (k, v) -> saved.put(k, v.toString()));
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
            assertFalse(button(panel, "SPIN").isEnabled());
            button(panel, "Obor").doClick();
            assertEquals(1, popup.entryCount()); assertTrue(button(panel, "SPIN").isEnabled());
            panel.setAction(spin -> panel.updatePool(entries, "refreshed", panel.generation()));
            button(panel, "Exclude raids").doClick();
            assertTrue(popup.isOpen()); assertEquals(1, popup.entryCount());
            WheelboundPanel restored = new WheelboundPanel(saved::get, (k, v) -> {});
            restored.updatePool(entries, "test", restored.generation());
            assertEquals("OBOR", restored.primaryWheel().entries().get(0).id);
            panel.reset();
        });
    }

    @Test public void greenSidebarSpinStartsImmediatelyAndShowsOnlySelectedResult() throws Exception
    {
        WheelboundPanel[] panel = new WheelboundPanel[1];
        WheelPopup popup = new WheelPopup(null);
        SwingUtilities.invokeAndWait(() -> {
            panel[0] = new WheelboundPanel(k -> k.equals("excludedBosses") ? "BRYOPHYTA" : null, (k, v) -> {});
            panel[0].setPopup(popup);
            List<WheelEntry> entries = List.of(new WheelEntry("OBOR", "Obor",
                new SkillIconManager().getSkillImage(Skill.ATTACK), 1), new WheelEntry("BRYOPHYTA", "Bryophyta", null, 1));
            panel[0].setAction(spin -> { if (spin) { panel[0].spinResponse(entries, "test", panel[0].generation()); } });
            panel[0].updatePool(entries, "test", panel[0].generation());
            AbstractButton spin = button(panel[0], "SPIN");
            assertTrue(spin.getBackground().getGreen() > spin.getBackground().getRed());
            spin.doClick();
            assertTrue(popup.isOpen()); assertTrue(panel[0].busy());
            assertFalse(button(panel[0], "Obor").isEnabled());
            assertEquals(1, panel[0].primaryWheel().entries().size());
        });
        long deadline = System.nanoTime() + 8_000_000_000L;
        boolean[] busy = {true};
        while (busy[0] && System.nanoTime() < deadline)
        {
            Thread.sleep(100);
            SwingUtilities.invokeAndWait(() -> busy[0] = panel[0].busy());
        }
        SwingUtilities.invokeAndWait(() -> {
            assertFalse(panel[0].busy()); assertTrue(popup.hasResult());
            assertTrue(hasLabel(panel[0], "Obor"));
            panel[0].updatePool(List.of(), "empty", panel[0].generation());
            assertTrue(popup.hasResult()); // Eligibility refresh must preserve the displayed result.
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
            WheelboundPanel panel = new WheelboundPanel(preferences::get, (k, v) -> preferences.put(k, v.toString()));
            long revision = panel.generation();
            button(panel, "Exclude raids").doClick();
            assertTrue(panel.selected(WheelFilter.BOSS_RAIDS));
            assertFalse(button(panel, "All bosses").isSelected());
            assertEquals("true", preferences.get("bossExcludeRaids"));
            panel.updatePool(List.of(new WheelEntry("a", "A", null, 1)), "stale", revision);
            assertFalse(panel.primaryWheel().canSpin());
            panel.updatePool(List.of(), "No matches", panel.generation());
            assertFalse(panel.primaryWheel().canSpin());
            button(panel, "Exclude Mimic").doClick();
            WheelboundPanel restored = new WheelboundPanel(preferences::get, (k, v) -> {});
            assertTrue(restored.selected(WheelFilter.BOSS_RAIDS)); assertTrue(restored.selected(WheelFilter.MIMIC));
            assertFalse(button(restored, "Include XP goal").isSelected());
        });
    }

    @Test public void automaticXpWheelCompletesBothResultsAndPreservesResultOnPoolRefresh() throws Exception
    {
        WheelboundPanel[] holder = new WheelboundPanel[1];
        SwingUtilities.invokeAndWait(() -> {
            WheelboundPanel panel = new WheelboundPanel(k -> k.equals("selectedWheel") ? "Skilling" : k.equals("includeXpGoal") ? "true" : null, (k, v) -> {});
            holder[0] = panel;
            panel.setPopup(new WheelPopup(null));
            List<WheelEntry> entries = List.of(new WheelEntry("MINING", "Mining", null, 1));
            panel.setAction(spin -> { if (spin) { panel.spinResponse(entries, "test", panel.generation()); } });
            panel.updatePool(entries, "test", panel.generation());
            panel.openWheel();
            panel.primaryWheel().requestSpin();
            assertTrue(panel.busy());
            panel.updatePool(List.of(), "No eligible skills", panel.generation());
        });
        // Real timers, no pixel assertions. The two retained 3.5s animations must complete sequentially.
        long deadline = System.nanoTime() + 12_000_000_000L;
        boolean[] busy = {true};
        while (busy[0] && System.nanoTime() < deadline)
        {
            Thread.sleep(100);
            SwingUtilities.invokeAndWait(() -> busy[0] = holder[0].busy());
        }
        SwingUtilities.invokeAndWait(() -> {
            assertFalse(holder[0].busy());
            assertTrue(hasLabel(holder[0], "Mining"));
            assertTrue(hasLabel(holder[0], "Gain "));
            assertFalse(holder[0].primaryWheel().canSpin());
            holder[0].reset();
            assertFalse(hasLabel(holder[0], "Mining"));
        });
    }

    @Test public void renderSidebarForReview() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            try
            {
                net.runelite.client.ui.laf.RuneLiteLAF.setup();
                WheelboundPanel panel = new WheelboundPanel(k -> "selectedWheel".equals(k) ? "Skilling" : null, (k, v) -> {});
                SkillIconManager icons = new SkillIconManager();
                List<WheelEntry> entries = new ArrayList<>();
                for (Skill skill : Skill.values())
                {
                    entries.add(new WheelEntry(skill.name(), skill.getName(), icons.getSkillImage(skill), 1));
                }
                panel.updatePool(entries, "24 eligible skills. Click the center to spin.", panel.generation());
                assertTrue("The sidebar must never contain a wheel", wheels(panel).isEmpty());
                panel.setSize(225, panel.getPreferredSize().height); layout(panel);
                BufferedImage image = new BufferedImage(225, panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics(); panel.paint(g); g.dispose();
                File file = new File("build/reports/wheelbound-skilling.png");
                file.getParentFile().mkdirs(); ImageIO.write(image, "png", file);
                panel.selectWheel(WheelType.BOSSING);
                List<WheelEntry> bosses = new ArrayList<>();
                for (BossDefinition boss : BossCatalog.ALL.subList(0, 12))
                { bosses.add(new WheelEntry(boss.hiscore.name(), boss.name, null, 1)); }
                panel.updatePool(bosses, "12 sample entries. Boss sprites load from the game cache.", panel.generation());
                assertTrue("The sidebar must never contain a wheel", wheels(panel).isEmpty());
                panel.setSize(225, panel.getPreferredSize().height); layout(panel);
                image = new BufferedImage(225, panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                g = image.createGraphics(); panel.paint(g); g.dispose();
                ImageIO.write(image, "png", new File("build/reports/wheelbound-bossing-layout.png"));
                panel.selectWheel(WheelType.COMBAT_ACHIEVEMENTS);
                panel.updatePool(List.of(new WheelEntry("CA_1", "Bloodveld", null, 1),
                    new WheelEntry("CA_2", "Obor", null, 1), new WheelEntry("CA_3", "Vorkath", null, 1)),
                    "3 encounters with unfinished tasks.", panel.generation());
                panel.setSize(225, panel.getPreferredSize().height); layout(panel);
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
                panel.reset();
            }
            catch (Exception e) { throw new AssertionError(e); }
        });
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
    private static boolean hasLabel(Container parent, String text)
    {
        for (Component c : parent.getComponents())
        {
            if (c instanceof JLabel && ((JLabel)c).getText().contains(text)) { return true; }
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

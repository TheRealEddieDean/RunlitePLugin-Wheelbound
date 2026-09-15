package com.wheelbound;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CustomWheelsTest
{
    @Test public void storagePreservesUnicodeDuplicatesAndEnabledState()
    {
        CustomWheels data = new CustomWheels();
        CustomWheels.CustomWheel first = data.create("  Evening plans  ");
        data.add(first, "A, B & <C> \"quoted\" ☕"); data.add(first, "A, B & <C> \"quoted\" ☕");
        first.items.get(0).enabled = false;
        CustomWheels.CustomWheel second = data.create("Bossing"); data.add(second, "Theatre of Blood: Hard Mode");
        CustomWheels restored = CustomWheels.load(data.save());
        assertEquals("Evening plans", restored.find(first.id).name);
        assertEquals(2, restored.find(first.id).items.size());
        assertFalse(restored.find(first.id).items.get(0).enabled);
        assertTrue(restored.find(first.id).items.get(1).enabled);
        assertNotEquals(first.items.get(0).id, first.items.get(1).id);
        assertEquals(second.items.get(0).name, restored.find(second.id).entries().get(0).wheelLabel());
        restored.remove(restored.find(first.id));
        assertNull(CustomWheels.load(restored.save()).find(first.id));
        assertNotNull(CustomWheels.load(restored.save()).find(second.id));
    }

    @Test public void invalidInputAndUnreadableStorageDoNotOverwriteSavedLists() throws Exception
    {
        CustomWheels data = new CustomWheels(); data.create("Plans");
        for (String name : List.of("  ", "plans", "x".repeat(81), "one\ntwo"))
        {
            try { data.create(name); fail("Invalid name accepted"); } catch (IllegalArgumentException expected) { }
        }
        assertEquals(1, data.wheels().size());
        SwingUtilities.invokeAndWait(() -> {
            Map<String, String> saved = new HashMap<>(); saved.put(CustomWheels.KEY, "broken JSON");
            saved.put("selectedWheel", "Custom");
            WheelboundPanel panel = panel(saved);
            panel.createCustomWheel("Do not overwrite");
            assertEquals("broken JSON", saved.get(CustomWheels.KEY));
            assertFalse(button(panel, "Create wheel").isEnabled());
        });
    }

    @Test public void createToggleDeleteAndRestartUseIndependentPersistentEntries() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            Map<String, String> saved = new HashMap<>(); WheelboundPanel panel = panel(saved);
            WheelPopup popup = new WheelPopup(null); panel.setPopup(popup); panel.onActivate();
            panel.selectWheel(WheelType.CUSTOM);
            field(panel, "customWheelName").setText("Weekend"); button(panel, "Create wheel").doClick();
            String firstId = saved.get("selectedCustomWheel");
            JTextField entry = field(panel.getWrappedPanel(), "customEntryName");
            entry.setText("Theatre of Blood: Hard Mode"); entry.postActionEvent();
            entry.setText("Theatre of Blood: Hard Mode"); button(panel.getWrappedPanel(), "Add").doClick();
            panel.addCustomEntry("<html>Go for a walk & relax");
            assertEquals(3, popup.entryCount());
            List<JCheckBox> rows = checkboxes(panel);
            rows.get(0).doClick(); assertEquals(2, popup.entryCount());
            assertTrue(checkboxes(panel).get(2).getText().contains("&lt;html&gt;"));
            CustomWheels data = CustomWheels.load(saved.get(CustomWheels.KEY));
            assertFalse(data.find(firstId).items.get(0).enabled);
            String deleteTip = "Delete entry: <html>Go for a walk & relax";
            byTooltip(panel, deleteTip).doClick(); assertEquals(1, popup.entryCount());
            assertEquals(2, CustomWheels.load(saved.get(CustomWheels.KEY)).find(firstId).items.size());
            panel.selectWheel(WheelType.CUSTOM); panel.createCustomWheel("Chores"); panel.addCustomEntry("Dishes");
            assertEquals(1, popup.entryCount());
            panel.selectCustomWheel(firstId); assertEquals(1, popup.entryCount());
            WheelboundPanel restored = panel(saved);
            assertEquals(WheelType.CUSTOM, restored.wheelType());
            assertEquals(1, restored.primaryWheel().entries().size());
            assertFalse(checkboxes(restored).get(0).isSelected());
            assertTrue(checkboxes(restored).get(1).isSelected());
            assertEquals("Theatre of Blood: Hard Mode", restored.primaryWheel().entries().get(0).label);
            assertNull(saved.get("excludedBosses"));
            panel.onDeactivate();
        });
    }

    @Test public void wheelDeletionRequiresConfirmationAndHandlesLastWheelAndProfileChanges() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            Map<String, String> saved = new HashMap<>(); WheelboundPanel panel = panel(saved);
            panel.selectWheel(WheelType.CUSTOM); panel.createCustomWheel("First"); panel.addCustomEntry("One");
            String first = saved.get("selectedCustomWheel");
            panel.selectWheel(WheelType.CUSTOM); panel.createCustomWheel("Second"); panel.addCustomEntry("Two");
            String before = saved.get(CustomWheels.KEY);
            panel.setDeleteConfirmation(name -> false);
            button(panel.getWrappedPanel(), "Delete wheel").doClick(); assertEquals(before, saved.get(CustomWheels.KEY));
            panel.setDeleteConfirmation(name -> true); button(panel.getWrappedPanel(), "Delete wheel").doClick();
            assertEquals(first, saved.get("selectedCustomWheel")); assertEquals(1, panel.primaryWheel().entries().size());
            panel.deleteSelectedCustomWheel();
            assertEquals(WheelType.BOSSING, panel.wheelType()); assertEquals("", saved.get("selectedCustomWheel"));
            assertTrue(CustomWheels.load(saved.get(CustomWheels.KEY)).wheels().isEmpty());
            assertNull(findButton(panel.getWrappedPanel(), "Delete wheel"));
            panel.selectWheel(WheelType.CUSTOM); panel.createCustomWheel("Keep");
            before = saved.get(CustomWheels.KEY);
            panel.setDeleteConfirmation(name -> { panel.reloadPreferences(k -> null); return true; });
            panel.deleteSelectedCustomWheel();
            assertEquals(before, saved.get(CustomWheels.KEY));
        });
    }

    @Test public void customSpinsWorkOfflineLockEditingAndIgnoreStaleResponses() throws Exception
    {
        WheelboundPanel[] holder = new WheelboundPanel[1];
        WheelPopup popup = new WheelPopup(null);
        long[] revision = new long[1];
        SwingUtilities.invokeAndWait(() -> {
            Map<String, String> saved = new HashMap<>(); WheelboundPanel panel = panel(saved); holder[0] = panel;
            panel.setAction(spin -> { throw new AssertionError("Custom wheels must not request account data"); });
            panel.selectWheel(WheelType.CUSTOM); panel.createCustomWheel("Offline"); panel.addCustomEntry("Walk");
            panel.setPopup(popup); panel.onActivate();
            revision[0] = panel.generation();
            panel.primaryWheel().requestSpin(); assertTrue(panel.busy());
            String before = saved.get(CustomWheels.KEY);
            assertFalse(button(panel.getWrappedPanel(), "Add").isEnabled());
            assertFalse(button(panel.getWrappedPanel(), "Delete wheel").isEnabled());
            panel.addCustomEntry("Must not add"); panel.deleteSelectedCustomWheel();
            assertEquals(before, saved.get(CustomWheels.KEY));
        });
        Thread.sleep(3900);
        SwingUtilities.invokeAndWait(() -> {
            WheelboundPanel panel = holder[0];
            assertFalse(panel.busy()); assertTrue(popup.hasResult());
            panel.addCustomEntry("Read"); assertFalse(popup.hasResult()); assertEquals(2, popup.entryCount());
            panel.updatePool(List.of(new WheelEntry("OLD", "Old account result", null, 1)), "stale", revision[0]);
            assertEquals(2, popup.entryCount());
            panel.primaryWheel().requestSpin(); panel.reloadPreferences(k -> null);
            assertFalse(panel.busy()); assertFalse(popup.isOpen());
            assertEquals(WheelType.BOSSING, panel.wheelType());
            assertTrue(panel.primaryWheel().entries().isEmpty());
        });
    }

    @Test public void textEntryDoesNotTriggerWheelKeyboardShortcuts() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            WheelPopup popup = new WheelPopup(null); WheelComponent wheel = new WheelComponent();
            AtomicInteger spins = new AtomicInteger();
            popup.show("Custom", wheel, "", spins::incrementAndGet, () -> {});
            JTextField input = new JTextField();
            for (int key : new int[]{KeyEvent.VK_SPACE, KeyEvent.VK_ENTER, KeyEvent.VK_ESCAPE})
            {
                KeyEvent event = new KeyEvent(input, KeyEvent.KEY_PRESSED, 0, 0, key, KeyEvent.CHAR_UNDEFINED);
                popup.keyPressed(event); assertFalse(event.isConsumed());
            }
            assertEquals(0, spins.get()); assertTrue(popup.isOpen()); popup.hide();
        });
    }

    @Test public void renderCustomSidebarAndVerifyDeleteButtonStaysBelowScroll() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            try
            {
                net.runelite.client.ui.laf.RuneLiteLAF.setup();
                WheelboundPanel panel = panel(new HashMap<>());
                panel.selectWheel(WheelType.CUSTOM); panel.createCustomWheel("Weekend adventures");
                for (String name : List.of("Try a new recipe", "Take a walk", "Read a book", "Play a board game", "Visit a museum"))
                { panel.addCustomEntry(name); }
                checkboxes(panel).get(1).doClick();
                JPanel wrapped = panel.getWrappedPanel();
                for (int height : new int[]{450, 800})
                {
                    wrapped.setSize(242, height); layout(wrapped);
                    AbstractButton delete = button(wrapped, "Delete wheel");
                    Rectangle bounds = SwingUtilities.convertRectangle(delete.getParent(), delete.getBounds(), wrapped);
                    assertTrue(bounds.y > height / 2); assertTrue(bounds.y + bounds.height <= height);
                    BufferedImage image = new BufferedImage(242, height, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = image.createGraphics(); wrapped.paint(g); g.dispose();
                    new File("build/reports").mkdirs();
                    ImageIO.write(image, "png", new File("build/reports/wheelbound-custom-" + height + ".png"));
                }
            }
            catch (Exception e) { throw new AssertionError(e); }
        });
    }

    private static WheelboundPanel panel(Map<String, String> saved)
    { return new WheelboundPanel(saved::get, (k, v) -> saved.put(k, v.toString())); }
    private static JTextField field(Container parent, String name)
    {
        for (Component child : parent.getComponents())
        {
            if (child instanceof JTextField && name.equals(child.getName())) { return (JTextField)child; }
            if (child instanceof Container) { JTextField found = field((Container)child, name); if (found != null) { return found; } }
        }
        return null;
    }
    private static AbstractButton button(Container parent, String text)
    { AbstractButton result = findButton(parent, text); assertNotNull("Missing button " + text, result); return result; }
    private static AbstractButton findButton(Container parent, String text)
    {
        for (Component child : parent.getComponents())
        {
            if (!child.isVisible()) { continue; }
            if (child instanceof AbstractButton && text.equals(((AbstractButton)child).getText())) { return (AbstractButton)child; }
            if (child instanceof Container) { AbstractButton found = findButton((Container)child, text); if (found != null) { return found; } }
        }
        return null;
    }
    private static AbstractButton byTooltip(Container parent, String tip)
    {
        for (Component child : parent.getComponents())
        {
            if (!child.isVisible()) { continue; }
            if (child instanceof AbstractButton && tip.equals(((AbstractButton)child).getToolTipText())) { return (AbstractButton)child; }
            if (child instanceof Container) { AbstractButton found = byTooltip((Container)child, tip); if (found != null) { return found; } }
        }
        return null;
    }
    private static List<JCheckBox> checkboxes(Container parent)
    {
        List<JCheckBox> result = new java.util.ArrayList<>();
        for (Component child : parent.getComponents())
        {
            if (!child.isVisible()) { continue; }
            if (child instanceof JCheckBox) { result.add((JCheckBox)child); }
            else if (child instanceof Container) { result.addAll(checkboxes((Container)child)); }
        }
        return result;
    }
    private static void layout(Container parent)
    { parent.doLayout(); for (Component child : parent.getComponents()) { if (child instanceof Container) { layout((Container)child); } } }
}

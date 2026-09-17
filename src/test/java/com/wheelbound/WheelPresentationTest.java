package com.wheelbound;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class WheelPresentationTest
{
    @Test public void customGainLabelsAndLargeWheelsRenderSafely()
    {
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(765, 503,
            java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        try
        {
            java.util.ArrayList<WheelEntry> entries = new java.util.ArrayList<>();
            entries.add(new WheelEntry("CUSTOM_one", "Gain 10,000 XP", null, 1));
            WheelStyle.drawWheel(graphics, 400, 400, entries, 0, true, false, false, false);
            for (int i = 0; i < 1000; i++)
            { entries.add(new WheelEntry("CUSTOM_" + i, "Gain " + "x".repeat(195), null, 1)); }
            WheelStyle.drawWheel(graphics, 400, 400, entries, 0, true, false, false, false);
            assertEquals(1001, WheelSelection.totalWeight(entries));
            Rectangle box = WheelPopup.paintResultRow(graphics, new WheelPopup.Layout(765, 503), entries.get(1), null);
            assertTrue(new WheelPopup.Layout(765, 503).results.contains(box));
        }
        finally { graphics.dispose(); }
    }

    @Test public void missingArtworkHasAUsableFallback()
    {
        java.awt.image.BufferedImage image = WheelStyle.loadArtwork("missing-artwork.png");
        assertNotNull(image);
        assertEquals(64, image.getWidth());
    }

    @Test public void raidsGroupAfterFilteringWithEqualOddsAndFullNames()
    {
        List<WheelEntry> entries = WheelEntry.groupRaids(List.of(
            new WheelEntry("hard", "Theatre of Blood: Hard Mode", null, 1),
            new WheelEntry("normal", "Theatre of Blood", null, 1),
            new WheelEntry("expert", "Tombs of Amascut: Expert Mode", null, 1),
            new WheelEntry("challenge", "Chambers of Xeric: Challenge Mode", null, 1),
            new WheelEntry("VORKATH", "Vorkath", null, 1)));
        assertEquals(4, entries.size());
        assertEquals(4, WheelSelection.totalWeight(entries));
        assertEquals("THEATRE_OF_BLOOD", entries.get(0).id);
        assertEquals("Theatre of Blood", entries.get(0).label);
        assertEquals("ToB", entries.get(0).wheelLabel());
        assertEquals("ToA", entries.get(1).wheelLabel());
        assertEquals("CoX", entries.get(2).wheelLabel());
        assertEquals("Vorkath", entries.get(3).wheelLabel());
    }

    @Test public void includeFiltersMigrateAndPreserveExplicitNewValues() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            java.util.Map<String, String> saved = new java.util.HashMap<>();
            saved.put("caExcludeBosses", "true");
            saved.put("caExcludeEasy", "false");
            saved.put("caExcludeMaster", "true");
            WheelboundPanel panel = new WheelboundPanel(saved::get, (k, v) -> saved.put(k, v.toString()));
            assertFalse(panel.selected(WheelFilter.CA_BOSSES));
            assertTrue(panel.selected(WheelFilter.CA_RAIDS));
            assertTrue(panel.selected(WheelFilter.EASY));
            assertFalse(panel.selected(WheelFilter.MASTER));
            saved.put("caIncludeMaster", "true");
            panel.reloadPreferences(saved::get);
            assertTrue(panel.selected(WheelFilter.MASTER));
            assertTrue(panel.selectedFilters().contains(WheelFilter.MASTER));
        });
    }

    @Test public void xpTargetsHaveDistinctDifficultyColors()
    {
        assertEquals(7, java.util.Arrays.stream(XpGoal.values()).map(goal -> goal.color).distinct().count());
        assertTrue(XpGoal.TEN_THOUSAND.color.getGreen() > XpGoal.TEN_THOUSAND.color.getRed());
        assertTrue(XpGoal.MILLION.color.getRed() > XpGoal.MILLION.color.getGreen());
        assertEquals(100, WheelSelection.totalWeight(XpGoal.entries()));
    }

    @Test public void countStaysOutsideScrollingContentAtBottom() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            WheelboundPanel panel = new WheelboundPanel(k -> null, (k, v) -> {});
            panel.updatePool(List.of(new WheelEntry("VORKATH", "Vorkath", null, 1)), "test", panel.generation());
            JPanel wrapped = panel.getWrappedPanel();
            Component footer = ((BorderLayout)wrapped.getLayout()).getLayoutComponent(BorderLayout.SOUTH);
            assertNotNull(footer);
            for (int height : new int[]{350, 900})
            {
                wrapped.setSize(242, height); wrapped.doLayout();
                assertEquals(height, footer.getY() + footer.getHeight());
                assertTrue(footer.getHeight() > 20);
            }
        });
    }
}

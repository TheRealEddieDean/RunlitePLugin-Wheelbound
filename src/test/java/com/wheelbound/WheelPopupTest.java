package com.wheelbound;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WheelPopupTest
{
    @Test public void fullCatalogFitsCenteredPopupAtFixedAndResizableSizes() throws Exception
    {
        List<WheelEntry> entries = new ArrayList<>();
        SkillIconManager icons = new SkillIconManager();
        for (BossDefinition boss : BossCatalog.ALL)
        {
            // Live boss sprite cache is unavailable in unit tests; same-size local icons exercise layout.
            entries.add(new WheelEntry(boss.hiscore.name(), boss.name,
                icons.getSkillImage(Skill.values()[entries.size() % Skill.values().length]), 1));
        }
        assertEquals(BossCatalog.ALL.size(), entries.size());
        for (int[] size : new int[][]{{765, 503}, {1200, 900}})
        {
            BufferedImage image = new BufferedImage(size[0], size[1], BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setColor(new Color(78, 83, 69)); g.fillRect(0, 0, size[0], size[1]);
            WheelPopup.Layout layout = WheelPopup.paint(g, size[0], size[1],
                new WheelPopup.View("Bossing", entries, 0, "Click the center to spin", true, false), false, false);
            g.dispose();
            assertEquals(size[0] / 2.0, layout.card.getCenterX(), 1);
            assertEquals(size[1] / 2.0, layout.card.getCenterY(), 1);
            assertTrue(new Rectangle(0, 0, size[0], size[1]).contains(layout.card));
            assertTrue(layout.card.contains(layout.wheel));
            new File("build/reports").mkdirs();
            ImageIO.write(image, "png", new File("build/reports/wheelbound-centered-" + size[0] + ".png"));
            g = image.createGraphics();
            WheelPopup.paint(g, size[0], size[1],
                new WheelPopup.View("Bossing", entries.subList(0, 38), 0, "Click the center to spin", true, false), false, false);
            g.dispose();
            ImageIO.write(image, "png", new File("build/reports/wheelbound-labels-" + size[0] + ".png"));
            g = image.createGraphics();
            WheelPopup.paintResultRow(g, layout, entries.get(0), null);
            assertTrue(layout.card.contains(layout.results));
            assertTrue(layout.results.y > layout.wheel.y + layout.wheel.height);
            g.dispose();
            ImageIO.write(image, "png", new File("build/reports/wheelbound-result-" + size[0] + ".png"));
            g = image.createGraphics();
            WheelPopup.paintResultRow(g, layout, new WheelEntry("CUSTOM_preview", "Your next adventure", null, 1), null);
            g.dispose();
            ImageIO.write(image, "png", new File("build/reports/wheelbound-custom-result-" + size[0] + ".png"));
        }
    }

    @Test public void popupHubSpinsAndEscapeCancelsWithoutClickThrough() throws Exception
    {
        AtomicInteger starts = new AtomicInteger(), closes = new AtomicInteger();
        WheelPopup[] popup = new WheelPopup[1];
        WheelComponent[] wheel = new WheelComponent[1];
        Canvas canvas = new Canvas();
        SwingUtilities.invokeAndWait(() -> {
            Client client = mock(Client.class);
            when(client.getCanvasWidth()).thenReturn(765); when(client.getCanvasHeight()).thenReturn(503);
            popup[0] = new WheelPopup(client);
            wheel[0] = new WheelComponent();
            List<WheelEntry> entries = List.of(new WheelEntry("a", "A", null, 1));
            wheel[0].setEntries(entries); wheel[0].setAvailable(true);
            wheel[0].setSpinAction(() -> {
                starts.incrementAndGet();
                wheel[0].animate(entries, 0, () -> fail("Cancelled spin completed"));
            });
            popup[0].show("Bossing", wheel[0], "", wheel[0]::requestSpin, () -> {
                closes.incrementAndGet(); wheel[0].cancel();
            });
            BufferedImage image = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics(); popup[0].render(g); g.dispose();
            WheelPopup.Layout layout = new WheelPopup.Layout(765, 503);
            for (int i = 0; i < 2; i++)
            {
                MouseEvent press = new MouseEvent(canvas, MouseEvent.MOUSE_PRESSED, 0, 0,
                    (int)layout.wheel.getCenterX(), (int)layout.wheel.getCenterY(), 1, false, MouseEvent.BUTTON1);
                assertTrue(popup[0].mouse.mousePressed(press).isConsumed());
                MouseEvent release = new MouseEvent(canvas, MouseEvent.MOUSE_RELEASED, 0, 0,
                    press.getX(), press.getY(), 1, false, MouseEvent.BUTTON1);
                assertTrue(popup[0].mouse.mouseReleased(release).isConsumed());
            }
        });
        SwingUtilities.invokeAndWait(() -> {
            assertEquals(1, starts.get());
            KeyEvent escape = new KeyEvent(canvas, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_ESCAPE, (char)27);
            popup[0].keyPressed(escape);
            assertTrue(escape.isConsumed()); assertFalse(popup[0].isOpen());
            MouseEvent release = new MouseEvent(canvas, MouseEvent.MOUSE_RELEASED, 0, 0, 1, 1, 1, false, MouseEvent.BUTTON1);
            assertTrue(popup[0].mouse.mouseReleased(release).isConsumed());
        });
        SwingUtilities.invokeAndWait(() -> { assertEquals(1, closes.get()); assertFalse(wheel[0].busy()); });
    }
}

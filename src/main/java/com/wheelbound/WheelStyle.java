package com.wheelbound;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.util.List;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

/** Custom wheel artwork within RuneLite's normal panel palette. */
final class WheelStyle
{
    static final Color GOLD = new Color(219, 180, 94);
    static int hubSize(int size) { return Math.max(54, size / 5); }
    private static final Color[] SEGMENTS = {
        new Color(79, 43, 40), new Color(45, 73, 39), new Color(73, 58, 34),
        new Color(49, 39, 70), new Color(32, 56, 68), new Color(62, 63, 45)
    };

    static void drawWheel(Graphics2D graphics, int width, int height, List<WheelEntry> entries,
        double angle, boolean enabled, boolean hover, boolean pressed, boolean busy)
    {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        int size = Math.min(width, height) - 24;
        int x = (width - size) / 2, y = (height - size) / 2;
        double cx = width / 2.0, cy = height / 2.0;
        int total = WheelSelection.totalWeight(entries);
        double before = 0;
        g.setColor(ColorScheme.DARKER_GRAY_COLOR);
        g.fillOval(x, y, size, size);
        for (int i = 0; i < entries.size(); i++)
        {
            WheelEntry entry = entries.get(i);
            double extent = entry.weight * 360.0 / total;
            Color segment = SEGMENTS[i % SEGMENTS.length];
            if (entry.label.startsWith("Gain ")) { segment = XpGoal.valueOf(entry.id).color; }
            g.setPaint(new GradientPaint(x, y, segment.brighter(), x + size, y + size, segment));
            Arc2D arc = new Arc2D.Double(x, y, size, size, before - angle, extent, Arc2D.PIE);
            g.fill(arc);
            g.setColor(ColorScheme.BORDER_COLOR);
            g.setStroke(new BasicStroke(1));
            g.draw(arc);
            double a = Math.toRadians(before + extent / 2 - angle);
            // All icons share a single outer ring; dense pools scale down to avoid overlap.
            double radius = .45;
            int tx = (int) (cx + Math.cos(a) * size * radius);
            int ty = (int) (cy - Math.sin(a) * size * radius);
            if (entry.icon != null)
            {
                int box = Math.min(30, Math.max(6, (int)(2 * size * radius * Math.sin(Math.toRadians(Math.min(90, extent) / 2)) * .82)));
                double scale = Math.min((double) box / entry.icon.getWidth(), (double) box / entry.icon.getHeight());
                int iw = (int) Math.round(entry.icon.getWidth() * scale);
                int ih = (int) Math.round(entry.icon.getHeight() * scale);
                g.drawImage(entry.icon, tx - iw / 2, ty - ih / 2, iw, ih, null);
                drawRadialLabel(g, entry.wheelLabel(), cx, cy, a, extent, size, box);
            }
            else if (entry.label.startsWith("Gain ") && extent >= 14)
            {
                g.setFont(FontManager.getRunescapeSmallFont());
                g.setColor(ColorScheme.TEXT_COLOR);
                String label = entry.label.startsWith("Gain ") ? compactXp(entry.label) : entry.label.substring(0, 1);
                centered(g, label, tx, ty + 4);
            }
            else { drawRadialLabel(g, entry.label.startsWith("Gain ") ? compactXp(entry.label) : entry.wheelLabel(), cx, cy, a, extent, size, 0); }
            before += extent;
        }
        g.setStroke(new BasicStroke(7));
        g.setPaint(new GradientPaint(x, y, GOLD.brighter(), x + size, y + size, GOLD.darker()));
        g.drawOval(x, y, size, size);
        int hub = hubSize(size), hx = width / 2 - hub / 2, hy = height / 2 - hub / 2;
        g.setColor(pressed ? ColorScheme.MEDIUM_GRAY_COLOR
            : hover && enabled ? ColorScheme.DARKER_GRAY_HOVER_COLOR : ColorScheme.DARKER_GRAY_COLOR);
        g.fillOval(hx, hy, hub, hub);
        g.setColor(enabled ? GOLD : ColorScheme.LIGHT_GRAY_COLOR);
        g.setStroke(new BasicStroke(2));
        g.drawOval(hx, hy, hub, hub);
        g.setFont(FontManager.getRunescapeBoldFont());
        centered(g, busy ? "..." : "SPIN", width / 2, height / 2 + 5 + (pressed ? 1 : 0));
        if (hover && enabled)
        {
            g.setColor(GOLD);
            g.drawOval(hx - 3, hy - 3, hub + 6, hub + 6);
        }
        g.setColor(GOLD);
        g.fillPolygon(new int[]{width / 2 - 9, width / 2 + 9, width / 2},
            new int[]{y - 8, y - 8, y + 13}, 3);
        g.dispose();
    }

    private static String compactXp(String label)
    {
        return label.replace("Gain ", "").replace(" XP", "").replace(",000,000", "m").replace(",000", "k");
    }

    private static void drawRadialLabel(Graphics2D graphics, String label, double cx, double cy,
        double angle, double extent, int size, int iconSize)
    {
        double outer = size * .45 - iconSize / 2.0 - 8;
        double inner = Math.max(hubSize(size) / 2.0 + 9, size * .20);
        String initials = java.util.Arrays.stream(label.split("[\\s-]+"))
            .filter(word -> !word.isEmpty()).map(word -> word.substring(0, 1))
            .collect(java.util.stream.Collectors.joining());
        String compact = initials.length() > 1 ? initials : label.substring(0, Math.min(4, label.length()));
        for (String candidate : new String[]{label, compact})
        {
            for (int fontSize = 13; fontSize >= 7; fontSize--)
            {
                Font font = FontManager.getDefaultFont().deriveFont(Font.BOLD, (float)fontSize);
                FontMetrics metrics = graphics.getFontMetrics(font);
                int length = metrics.stringWidth(candidate);
                double start = outer - length;
                double thickness = 2 * start * Math.sin(Math.toRadians(Math.min(90, extent) / 2));
                if (start < inner || thickness < metrics.getHeight() + 2) { continue; }
                Graphics2D g = (Graphics2D)graphics.create();
                g.translate(cx, cy); g.rotate(-angle);
                g.setFont(font); g.setColor(ColorScheme.TEXT_COLOR);
                if (Math.cos(angle) < 0)
                {
                    g.rotate(Math.PI);
                    g.drawString(candidate, (float)-outer, (metrics.getAscent() - metrics.getDescent()) / 2f);
                }
                else { g.drawString(candidate, (float)start, (metrics.getAscent() - metrics.getDescent()) / 2f); }
                g.dispose();
                return;
            }
        }
    }

    static void centered(Graphics2D g, String text, int x, int y)
    {
        g.drawString(text, x - g.getFontMetrics().stringWidth(text) / 2, y);
    }

    private static final BufferedImage CLASSIC_ICON = loadArtwork("classic-wheel.png");
    private static final BufferedImage HEADER = loadArtwork("wheelbound-header.png");

    static BufferedImage createIcon() { return scaledArtwork(CLASSIC_ICON, 32); }
    static BufferedImage headerImage() { return scaledArtwork(HEADER, 210); }

    private static BufferedImage loadArtwork(String name)
    {
        try (java.io.InputStream stream = WheelStyle.class.getResourceAsStream(name))
        {
            if (stream == null) { throw new IllegalStateException("Missing artwork: " + name); }
            BufferedImage image = javax.imageio.ImageIO.read(stream);
            // Remove transparent export margins so each asset fits its UI slot.
            int left = image.getWidth(), top = image.getHeight(), right = -1, bottom = -1;
            for (int y = 0; y < image.getHeight(); y++)
            {
                for (int x = 0; x < image.getWidth(); x++)
                {
                    if ((image.getRGB(x, y) >>> 24) > 16)
                    { left = Math.min(left, x); top = Math.min(top, y); right = Math.max(right, x); bottom = Math.max(bottom, y); }
                }
            }
            return right < left ? image : image.getSubimage(left, top, right - left + 1, bottom - top + 1);
        }
        catch (java.io.IOException ex) { throw new IllegalStateException("Cannot load artwork: " + name, ex); }
    }

    private static BufferedImage scaledArtwork(BufferedImage source, int width)
    {
        int height = Math.max(1, (int)Math.round(source.getHeight() * (double)width / source.getWidth()));
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(source, 0, 0, width, height, null); g.dispose();
        return image;
    }
}

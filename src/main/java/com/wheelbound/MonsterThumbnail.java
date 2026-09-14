package com.wheelbound;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import net.runelite.api.Model;

/** Small local CA-model preview. Does not change the game's model or rasterizer state. */
final class MonsterThumbnail
{
    private MonsterThumbnail() {}

    static BufferedImage render(Model model, int yaw, int pitch)
    {
        if (model == null || model.getVerticesCount() == 0 || model.getFaceCount() == 0) { return null; }
        int count = model.getVerticesCount();
        double[] x = new double[count], y = new double[count], depth = new double[count];
        double angle = yaw * Math.PI / 1024, tilt = pitch * Math.PI / 1024;
        double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
        float[] vx = model.getVerticesX(), vy = model.getVerticesY(), vz = model.getVerticesZ();
        for (int i = 0; i < count; i++)
        {
            x[i] = vx[i] * Math.cos(angle) + vz[i] * Math.sin(angle);
            double z = vz[i] * Math.cos(angle) - vx[i] * Math.sin(angle);
            y[i] = vy[i] * Math.cos(tilt) - z * Math.sin(tilt);
            depth[i] = z * Math.cos(tilt) + vy[i] * Math.sin(tilt);
            minX = Math.min(minX, x[i]); maxX = Math.max(maxX, x[i]);
            minY = Math.min(minY, y[i]); maxY = Math.max(maxY, y[i]);
        }
        double scale = 58 / Math.max(1, Math.max(maxX - minX, maxY - minY));
        int[] px = new int[count], py = new int[count];
        for (int i = 0; i < count; i++)
        { px[i] = (int)Math.round(32 + (x[i] - (minX + maxX) / 2) * scale); py[i] = (int)Math.round(32 + (y[i] - (minY + maxY) / 2) * scale); }
        int[] a = model.getFaceIndices1(), b = model.getFaceIndices2(), c = model.getFaceIndices3();
        Integer[] order = new Integer[model.getFaceCount()];
        Arrays.setAll(order, i -> i);
        Arrays.sort(order, Comparator.comparingDouble((Integer i) -> depth[a[i]] + depth[b[i]] + depth[c[i]]).reversed());
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int[] colors = model.getFaceColors1(), third = model.getFaceColors3();
        byte[] alpha = model.getFaceTransparencies();
        short[] unlit = model.getUnlitFaceColors();
        for (int i : order)
        {
            if (third[i] == -2) { continue; }
            int hsl = unlit != null ? unlit[i] & 0xffff : colors[i] & 0xffff;
            g.setColor(color(hsl, alpha == null ? 255 : 255 - (alpha[i] & 255)));
            g.fillPolygon(new int[]{px[a[i]], px[b[i]], px[c[i]]}, new int[]{py[a[i]], py[b[i]], py[c[i]]}, 3);
        }
        g.dispose();
        return image;
    }

    private static Color color(int packed, int alpha)
    {
        double h = ((packed >> 10) & 63) / 64.0, s = ((packed >> 7) & 7) / 8.0, l = (packed & 127) / 128.0;
        double chroma = (1 - Math.abs(2 * l - 1)) * s, x = chroma * (1 - Math.abs(h * 6 % 2 - 1)), m = l - chroma / 2;
        double r = 0, g = 0, b = 0;
        switch ((int)(h * 6))
        {
            case 0: r = chroma; g = x; break;
            case 1: r = x; g = chroma; break;
            case 2: g = chroma; b = x; break;
            case 3: g = x; b = chroma; break;
            case 4: r = x; b = chroma; break;
            default: r = chroma; b = x; break;
        }
        return new Color((int)((r + m) * 255), (int)((g + m) * 255), (int)((b + m) * 255), alpha);
    }
}

package com.wheelbound;

import java.awt.*;
import java.util.Random;

/** Bounded, local celebration. Particle trajectories are analytic; no extra timer or thread. */
final class Confetti
{
    static final double DURATION_SECONDS = 3.0;
    private static final Color[] COLORS = {new Color(239, 190, 65), new Color(82, 201, 130),
        new Color(89, 173, 241), new Color(233, 101, 142), new Color(179, 130, 243)};
    private Confetti() {}

    static void paint(Graphics2D graphics, Rectangle bounds, double seconds)
    {
        if (seconds < 0 || seconds >= DURATION_SECONDS) { return; }
        Graphics2D g = (Graphics2D)graphics.create();
        g.clip(bounds);
        g.setComposite(AlphaComposite.SrcOver.derive((float)Math.min(1, (DURATION_SECONDS - seconds) / .8)));
        Random random = new Random(71263);
        for (int i = 0; i < 120; i++)
        {
            double direction = random.nextDouble() * Math.PI * 2;
            double speed = (90 + random.nextDouble() * 260) * Math.min(1, bounds.width / 650.0);
            double x = bounds.getCenterX() + Math.cos(direction) * speed * seconds;
            double y = bounds.getCenterY() - 50 + Math.sin(direction) * speed * seconds + 95 * seconds * seconds;
            Graphics2D particle = (Graphics2D)g.create();
            particle.translate(x, y); particle.rotate(direction + seconds * (i % 2 == 0 ? 5 : -5));
            particle.setColor(COLORS[i % COLORS.length]); particle.fillRect(-3, -5, 6, 10);
            particle.dispose();
        }
        g.dispose();
    }
}

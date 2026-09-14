package com.wheelbound;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.input.MouseWheelListener;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/** Centered game-canvas popup. Only immutable wheel snapshots cross from Swing to rendering. */
@Singleton
public class WheelPopup extends Overlay implements KeyListener
{
    private final Client client;
    private volatile View view;
    private volatile Result selectedResult;
    private volatile Layout bounds;
    private volatile Runnable spin = () -> {}, dismiss = () -> {};
    private final AtomicLong generation = new AtomicLong();
    private volatile boolean hover, pressed;
    private boolean consumeClick;

    @Inject WheelPopup(Client client)
    {
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        setPriority(PRIORITY_HIGHEST);
        setMovable(false);
    }

    static final class View
    {
        final String title, result;
        final List<WheelEntry> entries;
        final double angle;
        final boolean available, busy;
        View(String title, List<WheelEntry> entries, double angle, String result, boolean available, boolean busy)
        {
            this.title = title; this.entries = List.copyOf(entries); this.angle = angle;
            this.result = result; this.available = available; this.busy = busy;
        }
    }

    private static final class Result
    {
        final WheelEntry entry;
        final String detail;
        final long started = System.nanoTime();
        Result(WheelEntry entry, String detail) { this.entry = entry; this.detail = detail; }
    }

    void complete(WheelEntry entry, String detail)
    {
        if (view != null) { selectedResult = new Result(entry, detail); }
    }
    void clearResult() { selectedResult = null; }
    boolean hasResult() { return selectedResult != null; }
    int entryCount() { View current = view; return current == null ? 0 : current.entries.size(); }

    static final class Layout
    {
        final Rectangle card, wheel, close;
        Layout(int width, int height)
        {
            int w = Math.max(160, Math.min(740, width - 24));
            int h = Math.max(200, Math.min(820, height - 24));
            card = new Rectangle((width - w) / 2, (height - h) / 2, w, h);
            int size = Math.max(80, Math.min(w - 28, h - 160));
            wheel = new Rectangle((width - size) / 2, card.y + 78, size, size);
            close = new Rectangle(card.x + w - 36, card.y + 8, 28, 28);
        }
        boolean hub(Point point)
        {
            return point.distance(wheel.getCenterX(), wheel.getCenterY()) <= WheelStyle.hubSize(wheel.width - 24) / 2.0;
        }
    }

    void show(String title, WheelComponent wheel, String result, Runnable onSpin, Runnable onDismiss)
    {
        generation.incrementAndGet(); spin = onSpin; dismiss = onDismiss; hover = false; pressed = false; mousePoint = null;
        selectedResult = null;
        view = snapshot(title, wheel, result);
    }

    void update(String title, WheelComponent wheel, String result)
    {
        if (view != null) { view = snapshot(title, wheel, result); }
    }

    private static View snapshot(String title, WheelComponent wheel, String result)
    {
        return new View(title, wheel.entries(), wheel.angle(), result, wheel.canSpin(), wheel.busy());
    }

    void hide() { generation.incrementAndGet(); view = null; selectedResult = null; hover = false; pressed = false; }
    boolean isOpen() { return view != null; }

    private void close()
    {
        Runnable callback = dismiss;
        hide();
        long epoch = generation.get();
        SwingUtilities.invokeLater(() -> { if (generation.get() == epoch) { callback.run(); } });
    }

    private void requestSpin()
    {
        View current = view;
        if (current == null || selectedResult != null || !current.available || current.busy) { return; }
        long epoch = generation.get();
        SwingUtilities.invokeLater(() -> { if (view != null && generation.get() == epoch) { spin.run(); } });
    }

    @Override public Dimension render(Graphics2D graphics)
    {
        View current = view;
        if (current == null) { return null; }
        Result result = selectedResult;
        if (result != null)
        {
            bounds = paintResult(graphics, client.getCanvasWidth(), client.getCanvasHeight(),
                result.entry, result.detail, (System.nanoTime() - result.started) / 1_000_000_000.0);
            return null;
        }
        bounds = paint(graphics, client.getCanvasWidth(), client.getCanvasHeight(), current, hover, pressed);
        Point point = mousePoint;
        if (point != null && bounds.wheel.contains(point) && !bounds.hub(point) && !current.busy)
        {
            double dx = point.x - bounds.wheel.getCenterX(), dy = bounds.wheel.getCenterY() - point.y;
            double degrees = ((Math.toDegrees(Math.atan2(dy, dx)) + current.angle) % 360 + 360) % 360;
            int index = WheelSelection.indexAt(current.entries, degrees / 360 * WheelSelection.totalWeight(current.entries));
            if (index >= 0 && Math.hypot(dx, dy) <= (bounds.wheel.width - 24) / 2.0)
            {
                Graphics2D g = (Graphics2D)graphics.create();
                g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
                WheelEntry entry = current.entries.get(index);
                String tip = entry.label + String.format(java.util.Locale.US, " (%.1f%%)",
                    entry.weight * 100.0 / WheelSelection.totalWeight(current.entries));
                int w = g.getFontMetrics().stringWidth(tip) + 16;
                int x = Math.max(0, Math.min(point.x + 12, client.getCanvasWidth() - w));
                int y = Math.min(point.y + 20, client.getCanvasHeight() - 25);
                g.setColor(new Color(19, 22, 23, 240)); g.fillRoundRect(x, y, w, 24, 6, 6);
                g.setColor(WheelStyle.GOLD); g.drawString(tip, x + 8, y + 17);
                g.dispose();
            }
        }
        return null;
    }

    static Layout paint(Graphics2D graphics, int width, int height, View current, boolean hover, boolean pressed)
    {
        Layout layout = new Layout(width, height);
        Rectangle card = layout.card, wheel = layout.wheel;
        Graphics2D g = (Graphics2D)graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0, 0, 0, 155)); g.fillRect(0, 0, width, height);
        g.setPaint(new GradientPaint(card.x, card.y, new Color(28, 31, 32),
            card.x + card.width, card.y + card.height, new Color(19, 22, 23)));
        g.fillRoundRect(card.x, card.y, card.width, card.height, 18, 18);
        g.setColor(WheelStyle.GOLD.darker()); g.drawRoundRect(card.x, card.y, card.width, card.height, 18, 18);
        g.setColor(WheelStyle.GOLD); g.setFont(new Font(Font.SERIF, Font.BOLD, 27));
        WheelStyle.centered(g, "Wheelbound", width / 2, card.y + 34);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13)); g.setColor(new Color(225, 221, 207));
        WheelStyle.centered(g, current.title, width / 2, card.y + 60);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));
        g.drawString("\u00d7", layout.close.x + 6, layout.close.y + 22);
        Graphics2D wg = (Graphics2D)g.create(wheel.x, wheel.y, wheel.width, wheel.height);
        WheelStyle.drawWheel(wg, wheel.width, wheel.height, current.entries, current.angle,
            current.available, hover, pressed, current.busy);
        wg.dispose();
        g.setFont(new Font(Font.SERIF, Font.BOLD, 19)); g.setColor(WheelStyle.GOLD);
        String result = current.result == null ? "Let the wheel decide..." : current.result;
        while (g.getFontMetrics().stringWidth(result) > card.width - 28 && g.getFont().getSize() > 11)
        { g.setFont(g.getFont().deriveFont((float)g.getFont().getSize() - 1)); }
        WheelStyle.centered(g, result, width / 2, card.y + card.height - 48);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11)); g.setColor(new Color(158, 151, 130));
        String hint = current.title.endsWith("XP target")
            ? "10k 35% | 25k 27% | 50k 20% | 100k 10% | 250k 5% | 500k 2% | 1m 1%"
            : current.entries.size() + " entries - hover a slice for its name";
        WheelStyle.centered(g, hint, width / 2, card.y + card.height - 30);
        WheelStyle.centered(g, "Esc or \u00d7 to close - The game is still running", width / 2, card.y + card.height - 13);
        g.dispose();
        return layout;
    }

    static Layout paintResult(Graphics2D graphics, int width, int height, WheelEntry entry, String detail, double seconds)
    {
        Layout layout = new Layout(width, height);
        Rectangle card = layout.card;
        Graphics2D g = (Graphics2D)graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.setColor(new Color(0, 0, 0, 155)); g.fillRect(0, 0, width, height);
        g.setPaint(new GradientPaint(card.x, card.y, new Color(28, 31, 32),
            card.x + card.width, card.y + card.height, new Color(19, 22, 23)));
        g.fillRoundRect(card.x, card.y, card.width, card.height, 18, 18);
        g.setColor(WheelStyle.GOLD.darker()); g.drawRoundRect(card.x, card.y, card.width, card.height, 18, 18);
        Confetti.paint(g, card, seconds);
        int iconSize = Math.min(144, card.height / 4);
        int centerY = (int)card.getCenterY();
        if (entry.icon != null)
        {
            double scale = Math.min((double)iconSize / entry.icon.getWidth(), (double)iconSize / entry.icon.getHeight());
            int iw = (int)Math.round(entry.icon.getWidth() * scale), ih = (int)Math.round(entry.icon.getHeight() * scale);
            g.drawImage(entry.icon, (width - iw) / 2, centerY - ih - 16, iw, ih, null);
        }
        g.setColor(WheelStyle.GOLD);
        g.setFont(new Font(Font.SERIF, Font.BOLD, 36));
        while (g.getFontMetrics().stringWidth(entry.label) > card.width - 40 && g.getFont().getSize() > 14)
        { g.setFont(g.getFont().deriveFont((float)g.getFont().getSize() - 1)); }
        WheelStyle.centered(g, entry.label, width / 2, centerY + 36);
        if (detail != null)
        {
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            WheelStyle.centered(g, detail, width / 2, centerY + 70);
        }
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));
        g.drawString("\u00d7", layout.close.x + 6, layout.close.y + 22);
        g.dispose();
        return layout;
    }

    private volatile Point mousePoint;
    private void move(MouseEvent event)
    {
        mousePoint = event.getPoint();
        Layout layout = bounds;
        hover = layout != null && layout.hub(event.getPoint());
    }

    final MouseAdapter mouse = new MouseAdapter()
    {
        @Override public MouseEvent mousePressed(MouseEvent e)
        {
            consumeClick = isOpen();
            if (consumeClick)
            {
                e.consume(); move(e);
                Layout layout = bounds;
                if (e.getButton() == MouseEvent.BUTTON1 && layout != null)
                {
                    if (layout.close.contains(e.getPoint())) { close(); }
                    else { pressed = selectedResult == null && view != null && view.available && layout.hub(e.getPoint()); }
                }
            }
            return e;
        }
        @Override public MouseEvent mouseReleased(MouseEvent e)
        {
            if (isOpen() || consumeClick) { e.consume(); }
            if (pressed && e.getButton() == MouseEvent.BUTTON1 && bounds != null && bounds.hub(e.getPoint())) { requestSpin(); }
            pressed = false;
            return e;
        }
        @Override public MouseEvent mouseClicked(MouseEvent e)
        { if (isOpen() || consumeClick) { e.consume(); } consumeClick = false; return e; }
        @Override public MouseEvent mouseMoved(MouseEvent e)
        { if (isOpen()) { move(e); e.consume(); } return e; }
        @Override public MouseEvent mouseDragged(MouseEvent e)
        { if (isOpen() || consumeClick) { move(e); e.consume(); } return e; }
    };
    final MouseWheelListener mouseWheel = e -> { if (isOpen()) { e.consume(); } return e; };

    @Override public boolean isEnabledOnLoginScreen() { return true; }
    @Override public void keyPressed(KeyEvent event)
    {
        if (!isOpen()) { return; }
        event.consume();
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) { close(); }
        else if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_ENTER) { requestSpin(); }
    }
    @Override public void keyReleased(KeyEvent event) { if (isOpen()) { event.consume(); } }
    @Override public void keyTyped(KeyEvent event) { if (isOpen()) { event.consume(); } }
}

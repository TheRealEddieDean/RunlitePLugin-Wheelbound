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
        final Rectangle card, wheel, close, results;
        Layout(int width, int height)
        {
            int w = Math.max(160, Math.min(740, width - 24));
            int h = Math.max(200, Math.min(820, height - 24));
            card = new Rectangle((width - w) / 2, (height - h) / 2, w, h);
            int size = Math.max(80, Math.min(w - 28, h - 184));
            wheel = new Rectangle((width - size) / 2, card.y + 78, size, size);
            results = new Rectangle(card.x + 20, wheel.y + size + 18, w - 40, 64);
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
    Component creationAnchor(Component fallback) { return client == null ? fallback : client.getCanvas(); }

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
        if (current == null || !current.available || current.busy) { return; }
        long epoch = generation.get();
        SwingUtilities.invokeLater(() -> { if (view != null && generation.get() == epoch) { spin.run(); } });
    }

    @Override public Dimension render(Graphics2D graphics)
    {
        View current = view;
        if (current == null) { return null; }
        Result result = selectedResult;
        bounds = paint(graphics, client.getCanvasWidth(), client.getCanvasHeight(), current, hover, pressed);
        if (result != null)
        {
            paintResultRow(graphics, bounds, result.entry, result.detail);
            Graphics2D celebration = (Graphics2D)graphics.create();
            Confetti.paint(celebration, bounds.card, (System.nanoTime() - result.started) / 1_000_000_000.0);
            celebration.dispose();
        }
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
        WheelStyle.centered(g, fitText(g.getFontMetrics(), current.title, card.width - 40), width / 2, card.y + 60);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));
        g.drawString("\u00d7", layout.close.x + 6, layout.close.y + 22);
        Graphics2D wg = (Graphics2D)g.create(wheel.x, wheel.y, wheel.width, wheel.height);
        WheelStyle.drawWheel(wg, wheel.width, wheel.height, current.entries, current.angle,
            current.available, hover, pressed, current.busy);
        wg.dispose();
        g.dispose();
        return layout;
    }

    static Rectangle paintResultRow(Graphics2D graphics, Layout layout, WheelEntry entry, String detail)
    {
        if (entry == null) { return null; }
        Graphics2D g = (Graphics2D)graphics.create();
        Font nameFont = new Font(Font.SERIF, Font.BOLD, 20);
        while (g.getFontMetrics(nameFont).stringWidth(entry.label) > layout.results.width - 80 && nameFont.getSize() > 10)
        { nameFont = nameFont.deriveFont((float)nameFont.getSize() - 1); }
        String subtitle = entry.source != null ? entry.source : detail;
        int sourceIconSpace = entry.source != null ? 24 : 0;
        Font detailFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        while (subtitle != null && g.getFontMetrics(detailFont).stringWidth(subtitle) + sourceIconSpace > layout.results.width - 80
            && detailFont.getSize() > 8)
        { detailFont = detailFont.deriveFont((float)detailFont.getSize() - 1); }
        int textWidth = g.getFontMetrics(nameFont).stringWidth(entry.label);
        if (subtitle != null)
        { textWidth = Math.max(textWidth, g.getFontMetrics(detailFont).stringWidth(subtitle) + sourceIconSpace); }
        int boxWidth = Math.min(layout.results.width, textWidth + 80);
        Rectangle box = new Rectangle((int)layout.card.getCenterX() - boxWidth / 2,
            layout.results.y, boxWidth, layout.results.height);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(35, 38, 39));
        g.fillRoundRect(box.x, box.y, box.width, box.height, 10, 10);
        g.setColor(WheelStyle.GOLD.darker());
        g.drawRoundRect(box.x, box.y, box.width, box.height, 10, 10);
        int iconSize = 40;
        int textX = box.x + 66;
        g.setColor(WheelStyle.GOLD);
        if (entry.icon != null)
        {
            double scale = Math.min((double)iconSize / entry.icon.getWidth(), (double)iconSize / entry.icon.getHeight());
            int iw = (int)Math.round(entry.icon.getWidth() * scale), ih = (int)Math.round(entry.icon.getHeight() * scale);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g.drawImage(entry.icon, box.x + 14 + (iconSize - iw) / 2, box.y + (box.height - ih) / 2, iw, ih, null);
        }
        else
        {
            g.setFont(new Font(Font.SERIF, Font.BOLD, 26));
            WheelStyle.centered(g, entry.label.substring(0, 1), box.x + 34, box.y + 41);
        }
        g.setFont(nameFont);
        g.drawString(fitText(g.getFontMetrics(), entry.label, box.x + box.width - textX - 12), textX, box.y + (subtitle == null ? 39 : 27));
        if (subtitle != null)
        {
            if (entry.source != null)
            {
                if (entry.sourceIcon != null)
                {
                    double scale = Math.min(18.0 / entry.sourceIcon.getWidth(), 18.0 / entry.sourceIcon.getHeight());
                    int iw = (int)Math.round(entry.sourceIcon.getWidth() * scale);
                    int ih = (int)Math.round(entry.sourceIcon.getHeight() * scale);
                    g.drawImage(entry.sourceIcon, textX + (18 - iw) / 2, box.y + 34 + (18 - ih) / 2, iw, ih, null);
                }
                else
                {
                    g.setFont(detailFont);
                    g.drawRoundRect(textX, box.y + 34, 18, 18, 4, 4);
                    WheelStyle.centered(g, entry.source.substring(0, 1), textX + 9, box.y + 47);
                }
            }
            g.setFont(detailFont);
            g.setColor(new Color(225, 221, 207));
            g.drawString(subtitle, textX + sourceIconSpace, box.y + 47);
        }
        g.dispose();
        return box;
    }

    private static String fitText(FontMetrics metrics, String text, int width)
    {
        if (metrics.stringWidth(text) <= width) { return text; }
        int end = text.length();
        while (end > 0 && metrics.stringWidth(text.substring(0, end) + "?") > width)
        { end = text.offsetByCodePoints(end, -1); }
        return text.substring(0, end) + "?";
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
                    else { pressed = view != null && view.available && layout.hub(e.getPoint()); }
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
        if (!isOpen() || typing(event)) { return; }
        event.consume();
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) { close(); }
        else if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_ENTER) { requestSpin(); }
    }
    @Override public void keyReleased(KeyEvent event) { if (isOpen() && !typing(event)) { event.consume(); } }
    @Override public void keyTyped(KeyEvent event) { if (isOpen() && !typing(event)) { event.consume(); } }

    private static boolean typing(KeyEvent event)
    {
        return event.getComponent() instanceof javax.swing.text.JTextComponent
            || KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof javax.swing.text.JTextComponent;
    }
}

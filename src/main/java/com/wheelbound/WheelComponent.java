package com.wheelbound;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import net.runelite.client.ui.ColorScheme;

/** EDT-only reusable wheel. Selected index is predetermined by WheelSelection. */
final class WheelComponent extends JComponent
{
    private List<WheelEntry> entries = List.of();
    private Runnable spin = () -> {};
    private Timer timer;
    private double angle;
    private boolean hover;
    private boolean pressed;
    private boolean available;
    private Runnable frameListener = () -> {};
    void setFrameListener(Runnable listener) { frameListener = listener; }
    double angle() { return angle; }
    private void changed() { repaint(); frameListener.run(); }

    WheelComponent()
    {
        setPreferredSize(new Dimension(209, 224));
        setMinimumSize(new Dimension(150, 150));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 224));
        setOpaque(true);
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setFocusable(true);
        setToolTipText("Click the center to spin");
        getAccessibleContext().setAccessibleName("Spin wheel");
        MouseAdapter mouse = new MouseAdapter()
        {
            @Override public void mouseMoved(MouseEvent e)
            {
                hover = hubContains(e.getX(), e.getY());
                setCursor(Cursor.getPredefinedCursor(hover && canSpin() ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
                changed();
            }
            @Override public void mouseExited(MouseEvent e) { hover = false; pressed = false; changed(); }
            @Override public void mousePressed(MouseEvent e)
            {
                pressed = SwingUtilities.isLeftMouseButton(e) && canSpin() && hubContains(e.getX(), e.getY());
                if (pressed) { requestFocusInWindow(); }
                changed();
            }
            @Override public void mouseReleased(MouseEvent e)
            {
                boolean trigger = pressed && SwingUtilities.isLeftMouseButton(e) && hubContains(e.getX(), e.getY());
                pressed = false; changed();
                if (trigger) { requestSpin(); }
            }
        };
        addMouseListener(mouse); addMouseMotionListener(mouse);
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "spin");
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "spin");
        getActionMap().put("spin", new AbstractAction()
        {
            @Override public void actionPerformed(ActionEvent e) { requestSpin(); }
        });
    }

    private boolean hubContains(int x, int y)
    {
        return Math.hypot(x - getWidth() / 2.0, y - getHeight() / 2.0) <= 27;
    }
    void setSpinAction(Runnable action) { spin = action; }
    void setAvailable(boolean value) { available = value; changed(); }
    boolean canSpin() { return available && !busy() && !entries.isEmpty(); }
    boolean busy() { return timer != null; }
    void requestSpin() { if (canSpin()) { spin.run(); } }
    List<WheelEntry> entries() { return entries; }

    void setEntries(List<WheelEntry> values)
    {
        if (busy()) { throw new IllegalStateException("Cannot replace a spinning wheel"); }
        entries = List.copyOf(values); angle = 0; changed();
    }

    void animate(List<WheelEntry> values, int selected, Runnable completed)
    {
        if (busy() || selected < 0 || selected >= values.size()) { return; }
        entries = List.copyOf(values);
        double start = angle;
        double finish = WheelSelection.targetAngle(start, entries, selected);
        long started = System.nanoTime();
        timer = new Timer(16, e -> {
            double progress = Math.min(1, (System.nanoTime() - started) / 3_500_000_000.0);
            angle = start + (finish - start) * (1 - Math.pow(1 - progress, 4));
            changed();
            if (progress >= 1)
            {
                timer.stop(); timer = null; angle %= 360;
                completed.run();
            }
        });
        timer.start();
        changed();
    }

    void cancel()
    {
        if (timer != null) { timer.stop(); timer = null; }
        pressed = false; available = false; changed();
    }

    @Override public String getToolTipText(MouseEvent e)
    {
        if (hubContains(e.getX(), e.getY())) { return busy() ? "Spinning..." : canSpin() ? "Spin (Space or Enter)" : "No eligible entries"; }
        double dx = e.getX() - getWidth() / 2.0, dy = getHeight() / 2.0 - e.getY();
        if (Math.hypot(dx, dy) > (Math.min(getWidth(), getHeight()) - 24) / 2.0) { return null; }
        double degrees = ((Math.toDegrees(Math.atan2(dy, dx)) + angle) % 360 + 360) % 360;
        int index = WheelSelection.indexAt(entries, degrees / 360 * WheelSelection.totalWeight(entries));
        if (index < 0) { return null; }
        WheelEntry entry = entries.get(index);
        return entry.label + String.format(java.util.Locale.US, " (%.1f%% of this wheel)",
            entry.weight * 100.0 / WheelSelection.totalWeight(entries));
    }

    @Override protected void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
        graphics.setColor(getBackground()); graphics.fillRect(0, 0, getWidth(), getHeight());
        WheelStyle.drawWheel((Graphics2D) graphics, getWidth(), getHeight(), entries, angle, canSpin(), hover, pressed, busy());
    }

    @Override public javax.accessibility.AccessibleContext getAccessibleContext()
    {
        if (accessibleContext == null) { accessibleContext = new AccessibleJComponent() {}; }
        return accessibleContext;
    }
}

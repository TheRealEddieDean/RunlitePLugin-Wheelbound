package com.wheelbound;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import net.runelite.client.ui.ColorScheme;

/** Reusable manual selection for any wheel. Eligible unchecked entries remain listed. EDT only. */
final class BossChecklist extends JPanel
{
    private final Set<String> excluded = new TreeSet<>();
    private final JPanel rows = new JPanel();
    private final Consumer<String> changed;
    private final Consumer<WheelEntry> delete;
    private List<WheelEntry> entries = List.of();

    BossChecklist(String heading, String saved, Consumer<String> changed)
    { this(heading, saved, changed, null); }

    BossChecklist(String heading, String saved, Consumer<String> changed, Consumer<WheelEntry> delete)
    {
        this.changed = changed;
        this.delete = delete;
        if (saved != null && !saved.isBlank()) { excluded.addAll(Arrays.asList(saved.split(","))); }
        setLayout(new BorderLayout(0, 5)); setOpaque(false);
        JLabel title = new JLabel(heading);
        title.setForeground(ColorScheme.TEXT_COLOR);
        add(title, BorderLayout.NORTH);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setBackground(ColorScheme.DARK_GRAY_COLOR);
        rows.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        JScrollPane scroll = new JScrollPane(rows);
        scroll.getViewport().setBackground(ColorScheme.DARK_GRAY_COLOR);
        scroll.setBorder(BorderFactory.createLineBorder(ColorScheme.MEDIUM_GRAY_COLOR));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        scroll.setPreferredSize(new Dimension(200, 170));
        add(scroll, BorderLayout.CENTER);

    }

    void updateEntries(List<WheelEntry> values)
    {
        boolean same = entries.size() == values.size();
        for (int i = 0; same && i < values.size(); i++)
        { same = entries.get(i).id.equals(values.get(i).id); }
        entries = List.copyOf(values);
        if (same) { return; }
        rows.removeAll();
        for (WheelEntry entry : entries)
        {
            JCheckBox box = new JCheckBox("<html><div style='width:" + (delete == null ? 120 : 100)
                + "px'>" + escape(entry.label) + "</div></html>", !excluded.contains(entry.id));
            box.setToolTipText("<html>" + escape(entry.label) + "</html>");
            box.setOpaque(false); box.setForeground(ColorScheme.TEXT_COLOR);
            box.setFont(net.runelite.client.ui.FontManager.getDefaultFont().deriveFont(12f));
            box.setBorder(BorderFactory.createEmptyBorder(5, 3, 5, 3));
            box.setEnabled(isEnabled());
            box.addActionListener(e -> {
                if (box.isSelected()) { excluded.remove(entry.id); } else { excluded.add(entry.id); }
                changed.accept(String.join(",", excluded));
            });
            if (delete == null) { rows.add(box); }
            else
            {
                JPanel row = new JPanel(new BorderLayout()); row.setOpaque(false);
                row.setAlignmentX(Component.LEFT_ALIGNMENT);
                JButton remove = new JButton(new TrashIcon());
                remove.setContentAreaFilled(false); remove.setBorderPainted(false); remove.setOpaque(false);
                remove.setToolTipText("Delete entry: " + entry.label);
                remove.getAccessibleContext().setAccessibleName("Delete entry: " + entry.label);
                remove.setPreferredSize(new Dimension(26, 26));
                remove.setMargin(new Insets(3, 3, 3, 3)); remove.setEnabled(isEnabled());
                remove.addActionListener(e -> delete.accept(entry));
                row.add(box, BorderLayout.CENTER); row.add(remove, BorderLayout.EAST);
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height));
                rows.add(row);
            }
        }
        rows.revalidate(); rows.repaint();
    }

    List<WheelEntry> included(List<WheelEntry> values)
    {
        List<WheelEntry> result = new ArrayList<>();
        for (WheelEntry entry : values) { if (!excluded.contains(entry.id)) { result.add(entry); } }
        return List.copyOf(result);
    }

    void restore(String saved)
    {
        excluded.clear();
        if (saved != null && !saved.isBlank()) { excluded.addAll(Arrays.asList(saved.split(","))); }
        List<WheelEntry> current = entries; entries = List.of(); updateEntries(current);
    }
    @Override public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        if (rows != null) { enableChildren(rows, enabled); }
    }

    private static void enableChildren(Container parent, boolean enabled)
    {
        for (Component child : parent.getComponents())
        { child.setEnabled(enabled); if (child instanceof Container) { enableChildren((Container)child, enabled); } }
    }

    static String escape(String text)
    { return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;"); }

    private static final class TrashIcon implements Icon
    {
        public int getIconWidth() { return 12; }
        public int getIconHeight() { return 14; }
        public void paintIcon(Component component, Graphics graphics, int x, int y)
        {
            Graphics g = graphics.create(); g.translate(x, y);
            g.setColor(component.isEnabled() ? new Color(230, 105, 105) : Color.GRAY);
            g.drawRect(3, 1, 5, 2); g.drawLine(0, 3, 11, 3);
            g.drawRect(2, 4, 7, 9); g.drawLine(4, 6, 4, 11); g.drawLine(7, 6, 7, 11); g.dispose();
        }
    }
}

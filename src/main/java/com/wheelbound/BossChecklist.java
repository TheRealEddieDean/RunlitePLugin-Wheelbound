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
    private List<WheelEntry> entries = List.of();

    BossChecklist(String heading, String saved, Consumer<String> changed)
    {
        this.changed = changed;
        if (saved != null && !saved.isBlank()) { excluded.addAll(Arrays.asList(saved.split(","))); }
        setLayout(new BorderLayout(0, 5)); setOpaque(false);
        JLabel title = new JLabel(heading);
        title.setForeground(ColorScheme.TEXT_COLOR);
        add(title, BorderLayout.NORTH);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        JScrollPane scroll = new JScrollPane(rows);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        scroll.setPreferredSize(new Dimension(200, 170));
        add(scroll, BorderLayout.CENTER);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
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
            JCheckBox box = new JCheckBox("<html><div style='width:120px'>" + entry.label + "</div></html>", !excluded.contains(entry.id));
            box.setToolTipText(entry.label);
            box.setOpaque(false); box.setForeground(ColorScheme.TEXT_COLOR);
            box.setFont(net.runelite.client.ui.FontManager.getDefaultFont().deriveFont(12f));
            box.setBorder(BorderFactory.createEmptyBorder(5, 3, 5, 3));
            box.setEnabled(isEnabled());
            box.addActionListener(e -> {
                if (box.isSelected()) { excluded.remove(entry.id); } else { excluded.add(entry.id); }
                changed.accept(String.join(",", excluded));
            });
            rows.add(box);
        }
        rows.revalidate(); rows.repaint();
    }

    List<WheelEntry> included(List<WheelEntry> values)
    {
        List<WheelEntry> result = new ArrayList<>();
        for (WheelEntry entry : values) { if (!excluded.contains(entry.id)) { result.add(entry); } }
        return List.copyOf(result);
    }

    boolean allIncluded() { return excluded.isEmpty(); }
    void restore(String saved)
    {
        excluded.clear();
        if (saved != null && !saved.isBlank()) { excluded.addAll(Arrays.asList(saved.split(","))); }
        List<WheelEntry> current = entries; entries = List.of(); updateEntries(current);
    }
    void includeAll()
    {
        excluded.clear();
        List<WheelEntry> current = entries; entries = List.of(); updateEntries(current);
        changed.accept("");
    }
    void excludeAll()
    {
        entries.forEach(entry -> excluded.add(entry.id));
        List<WheelEntry> current = entries; entries = List.of(); updateEntries(current);
        changed.accept(String.join(",", excluded));
    }

    @Override public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        if (rows != null) { for (Component row : rows.getComponents()) { row.setEnabled(enabled); } }
    }
}

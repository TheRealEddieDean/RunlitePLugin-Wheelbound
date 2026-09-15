package com.wheelbound;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.*;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

/** Layout and two-stage spin coordination. Game reads and eligibility live outside Swing. */
public class WheelboundPanel extends PluginPanel implements Scrollable
{
    private final WheelComponent wheel = new WheelComponent();
    private final WheelComponent xpWheel = new WheelComponent();
    private final JLabel result = label("Spin the wheel", true);
    private final JLabel xpResult = label("", false);
    private final JLabel status = label("Loading eligible entries...", false);
    private final EnumMap<WheelType, BossChecklist> checklists = new EnumMap<>(WheelType.class);
    private final EnumMap<WheelFilter, JCheckBox> filters = new EnumMap<>(WheelFilter.class);
    private final JPanel checklistCards = new JPanel(new CardLayout());
    private final JComboBox<WheelType> selector = new JComboBox<>(WheelType.values());
    private List<WheelEntry> availablePool = List.of();
    private String poolMessage = "";
    private WheelPopup popup;
    private WheelComponent displayed = wheel;
    private String popupTitle, popupResult;
    private final JPanel options = new JPanel(new CardLayout());
    private final JCheckBox all = check("All bosses", "Select all bosses and clear pool exclusions, or deselect the current list. Account options still apply.");
    private final List<AbstractButton> controls = new ArrayList<>();
    private final BiConsumer<String, Object> save;
    private final Random random = new Random();
    private Consumer<Boolean> action = ignored -> {};
    private Runnable refreshAction = () -> action.accept(false);
    private WheelType type;
    private boolean busy;
    private boolean sidebarActive;
    private long generation;
    private List<WheelEntry> deferred;
    private String deferredMessage;

    WheelboundPanel(Function<String, String> load, BiConsumer<String, Object> save)
    {
        super();
        getScrollPane().setViewportView(this);
        this.save = save;
        type = WheelType.fromSaved(load.apply("selectedWheel"));
        for (WheelType wheelType : WheelType.values())
        {
            BossChecklist checklist = new BossChecklist(wheelType == WheelType.SKILLING ? "Included skills"
                : wheelType == WheelType.BOSSING ? "Included bosses"
                : wheelType == WheelType.PET_HUNTING ? "Included pets" : "Included encounters",
                load.apply(wheelType.exclusionKey), value -> {
                    save.accept(wheelType.exclusionKey, value);
                    generation++;
                    popupResult = "Click the center to spin";
                    if (popup != null) { popup.clearResult(); }
                    syncAll(); updatePool(availablePool, poolMessage, generation);
                    refreshAction.run();
                });
            checklists.put(wheelType, checklist);
            checklistCards.add(checklist, wheelType.name());
        }
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));
        JPanel content = column();
        JLabel title = label("Wheelbound", true);
        title.setFont(FontManager.getRunescapeBoldFont().deriveFont(22f));
        content.add(title);
        content.add(Box.createVerticalStrut(10));
        selector.setSelectedItem(type);
        selector.setFont(FontManager.getDefaultBoldFont().deriveFont(12f));
        selector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        selector.setToolTipText("Choose a wheel");
        selector.addActionListener(e -> selectWheel((WheelType)selector.getSelectedItem()));
        content.add(selector);
        content.add(Box.createVerticalStrut(8));
        content.add(result);
        content.add(xpResult);
        content.add(Box.createVerticalStrut(10));
        options.setOpaque(false);
        for (WheelType wheelType : WheelType.values())
        {
            JPanel wheelOptions = column();
            String section = "";
            for (WheelFilter filter : WheelFilter.values())
            {
                if (filter.wheel != wheelType) { continue; }
                if (!section.equals(filter.section))
                {
                    section = filter.section;
                    wheelOptions.add(Box.createVerticalStrut(8));
                    JLabel heading = new JLabel(section);
                    heading.setForeground(ColorScheme.BRAND_ORANGE);
                    heading.setFont(FontManager.getDefaultBoldFont());
                    heading.setAlignmentX(Component.LEFT_ALIGNMENT);
                    wheelOptions.add(heading);
                    if (wheelType == WheelType.BOSSING && section.equals("Boss pool")) { wheelOptions.add(all); }
                }
                JCheckBox box = check(filter.title, filter.tip);
                String saved = filter.savedValue(load);
                // Migrate the two removed master options once through the new sidebar defaults.
                if (saved == null && filter == WheelFilter.ACCOUNT) { saved = load.apply("limitBossesToMyLevel"); }
                if (saved == null && filter == WheelFilter.MAXED_SKILLS) { saved = load.apply("excludeLevel99Skills"); }
                if (saved == null && filter == WheelFilter.BOSS_RAIDS && load.apply("includeRaids") != null)
                { saved = Boolean.toString(!Boolean.parseBoolean(load.apply("includeRaids"))); }
                box.setSelected(saved == null ? filter.defaultValue : Boolean.parseBoolean(saved));
                box.addActionListener(e -> { save.accept(filter.key, box.isSelected()); syncAll(); filtersChanged(); });
                filters.put(filter, box); controls.add(box); wheelOptions.add(box);
            }
            options.add(wheelOptions, wheelType.name());
        }
        showCards();
        content.add(options);
        content.add(Box.createVerticalStrut(8));
        checklistCards.setOpaque(false);
        add(checklistCards, BorderLayout.CENTER);
        add(content, BorderLayout.NORTH);
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(12, 0, 8, 0));
        footer.add(status, BorderLayout.CENTER);
        getWrappedPanel().add(footer, BorderLayout.SOUTH);
        controls.add(all);
        all.addActionListener(e -> {
            if (!all.isSelected()) { checklist().excludeAll(); syncAll(); return; }
            for (WheelFilter filter : List.of(WheelFilter.BOSS_RAIDS, WheelFilter.MIMIC))
            { filters.get(filter).setSelected(false); save.accept(filter.key, false); }
            checklist().includeAll(); syncAll();
        });
        syncAll();
        wheel.setSpinAction(() -> {
            if (busy) { return; }
            busy = true; enableControls(false); wheel.setAvailable(false);
            action.accept(true);
        });
        xpWheel.setAvailable(false);
        wheel.setFrameListener(this::refreshPopup);
        xpWheel.setFrameListener(this::refreshPopup);
    }

    @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
    @Override public int getScrollableUnitIncrement(Rectangle visible, int orientation, int direction) { return 24; }
    @Override public int getScrollableBlockIncrement(Rectangle visible, int orientation, int direction)
    { return Math.max(24, visible.height - 24); }
    @Override public boolean getScrollableTracksViewportWidth() { return true; }
    @Override public boolean getScrollableTracksViewportHeight()
    { return getParent() instanceof JViewport && getParent().getHeight() >= getPreferredSize().height; }

    @Override public void onActivate()
    {
        sidebarActive = true;
        openWheel();
    }

    @Override public void onDeactivate()
    {
        sidebarActive = false;
        if (popup != null) { popup.hide(); }
        generation++; wheel.cancel(); xpWheel.cancel(); finish();
    }

    void setPopup(WheelPopup value) { popup = value; }
    void setRefreshAction(Runnable value) { refreshAction = value; }
    WheelComponent primaryWheel() { return wheel; }
    WheelComponent secondaryWheel() { return xpWheel; }

    void selectWheel(WheelType value)
    {
        if (value == null || type == value) { return; }
        type = value; selector.setSelectedItem(value); save.accept("selectedWheel", type.title);
        showCards(); reset(); action.accept(false);
        if (sidebarActive) { openWheel(); }
    }
    private void showCards()
    {
        ((CardLayout)options.getLayout()).show(options, type.name());
        ((CardLayout)checklistCards.getLayout()).show(checklistCards, type.name());
        for (Component card : options.getComponents())
        {
            if (card.isVisible())
            {
                card.invalidate();
                Dimension size = card.getPreferredSize();
                options.setPreferredSize(size);
                options.setMinimumSize(new Dimension(0, size.height));
                options.setMaximumSize(new Dimension(Integer.MAX_VALUE, size.height));
            }
        }
        if (options.getParent() != null) { options.getParent().invalidate(); }
        invalidate();
        revalidate();
    }
    private BossChecklist checklist() { return checklists.get(type); }
    private void syncAll()
    { all.setSelected(!selected(WheelFilter.BOSS_RAIDS) && !selected(WheelFilter.MIMIC) && checklists.get(WheelType.BOSSING).allIncluded()); }
    boolean selected(WheelFilter filter) { return filters.get(filter).isSelected(); }
    void reloadPreferences(Function<String, String> load)
    {
        filters.forEach((filter, box) -> {
            String value = filter.savedValue(load);
            if (value == null && filter == WheelFilter.ACCOUNT) { value = load.apply("limitBossesToMyLevel"); }
            if (value == null && filter == WheelFilter.MAXED_SKILLS) { value = load.apply("excludeLevel99Skills"); }
            if (value == null && filter == WheelFilter.BOSS_RAIDS && load.apply("includeRaids") != null)
            { value = Boolean.toString(!Boolean.parseBoolean(load.apply("includeRaids"))); }
            box.setSelected(value == null ? filter.defaultValue : Boolean.parseBoolean(value));
        });
        checklists.forEach((wheelType, checklist) -> checklist.restore(load.apply(wheelType.exclusionKey)));
        type = WheelType.fromSaved(load.apply("selectedWheel")); selector.setSelectedItem(type);
        syncAll(); showCards(); reset();
    }
    Set<WheelFilter> selectedFilters()
    {
        Set<WheelFilter> result = EnumSet.noneOf(WheelFilter.class);
        filters.forEach((filter, box) -> { if (box.isSelected()) { result.add(filter); } });
        return Set.copyOf(result);
    }

    void openWheel()
    {
        if (popup == null || busy || popup.isOpen()) { return; }
        displayed = wheel; popupTitle = type.title; popupResult = "Click the center to spin";
        popup.show(popupTitle, displayed, popupResult, () -> displayed.requestSpin(), () -> {
            generation++;
            wheel.cancel(); xpWheel.cancel();
            if (busy) { result.setText(html("Spin cancelled")); xpResult.setText(""); }
            finish();
        });
    }

    private void refreshPopup()
    {
        if (popup != null) { popup.update(popupTitle, displayed, popupResult); }
    }
    private void filtersChanged()
    {
        generation++; wheel.setAvailable(false);
        if (popup != null) { popup.clearResult(); }
        refreshAction.run();
    }
    void setAction(Consumer<Boolean> value) { action = value; }
    WheelType wheelType() { return type; }
    long generation() { return generation; }
    boolean busy() { return busy; }

    void updatePool(List<WheelEntry> entries, String message, long revision)
    {
        if (revision != generation) { return; }
        if (busy) { deferred = entries; deferredMessage = message; return; }
        availablePool = List.copyOf(entries); poolMessage = message;
        checklist().updateEntries(entries);
        List<WheelEntry> included = checklist().included(entries);
        displayed = wheel; popupTitle = type.title;
        wheel.setEntries(included); wheel.setAvailable(!included.isEmpty());
        status.setToolTipText(message);
        status.setText(html(!entries.isEmpty()
            ? included.size() + " of " + entries.size() + " eligible entries included."
                + (included.isEmpty() ? " Check an entry above to spin." : "")
                + (type == WheelType.COMBAT_ACHIEVEMENTS ? " Unfinished tasks only." : "")
            : message));
    }

    void spinResponse(List<WheelEntry> entries, String message, long revision)
    {
        if (revision != generation) { return; }
        List<WheelEntry> choices = checklist().included(entries);
        if (choices.isEmpty()) { finish(); updatePool(entries, message, revision); return; }
        int selected = WheelSelection.select(choices, random);
        displayed = wheel; popupTitle = type.title; popupResult = "Spinning...";
        if (popup != null) { popup.clearResult(); }
        result.setIcon(null); result.setText(html("Spinning...")); xpResult.setText("");
        boolean withXp = type == WheelType.SKILLING && selected(WheelFilter.XP);
        wheel.animate(choices, selected, () -> {
            popupResult = choices.get(selected).label;
            result.setText(html(popupResult));
            if (choices.get(selected).icon != null)
            {
                result.setIcon(new ImageIcon(net.runelite.client.util.ImageUtil.resizeImage(choices.get(selected).icon, 48, 48)));
                result.setHorizontalTextPosition(SwingConstants.CENTER);
                result.setVerticalTextPosition(SwingConstants.BOTTOM);
            }
            if (choices.get(selected).source != null) { xpResult.setText(html(choices.get(selected).source)); }
            if (withXp)
            {
                xpResult.setText(html("Click SPIN to choose your XP target")); revalidate();
                displayed = xpWheel; popupTitle = choices.get(selected).label + " - XP target";
                List<WheelEntry> goals = XpGoal.entries();
                xpWheel.setEntries(goals);
                xpWheel.setSpinAction(() -> {
                    xpWheel.setAvailable(false);
                    xpResult.setText(html("Choosing an XP target..."));
                    int goal = WheelSelection.select(goals, random);
                    xpWheel.animate(goals, goal, () -> {
                        popupResult = choices.get(selected).label + " - " + goals.get(goal).label;
                        xpResult.setText(html(goals.get(goal).label));
                        finish();
                        if (popup != null) { popup.complete(choices.get(selected), goals.get(goal).label); }
                    });
                });
                xpWheel.setAvailable(true);

            }
            else { finish(); if (popup != null) { popup.complete(choices.get(selected), null); } }
        });
        revalidate();
    }

    private void finish()
    {
        xpWheel.setAvailable(false);
        busy = false; enableControls(true); wheel.setAvailable(!wheel.entries().isEmpty());
        displayed = wheel; popupTitle = type.title; refreshPopup();
        if (deferred != null)
        {
            List<WheelEntry> entries = deferred; deferred = null;
            updatePool(entries, deferredMessage, generation);
        }
    }
    private void enableControls(boolean enabled)
    { controls.forEach(c -> c.setEnabled(enabled)); selector.setEnabled(enabled); checklists.values().forEach(c -> c.setEnabled(enabled)); }
    void reset()
    {
        if (popup != null) { popup.hide(); }
        generation++; wheel.cancel(); xpWheel.cancel(); busy = false; deferred = null;
        displayed = wheel; popupTitle = type.title; availablePool = List.of();
        checklist().updateEntries(List.of()); status.setText(html("Loading eligible entries..."));
        wheel.setEntries(List.of());
        result.setIcon(null); result.setText(html("Spin the wheel")); xpResult.setText("");
        enableControls(true); revalidate();
    }
    private static JPanel column()
    {
        JPanel panel = new JPanel(); panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false); panel.setAlignmentX(Component.CENTER_ALIGNMENT); return panel;
    }
    private static JCheckBox check(String text, String tip)
    {
        JCheckBox box = new JCheckBox(text); box.setOpaque(false);
        box.setFont(FontManager.getDefaultFont().deriveFont(12f)); box.setForeground(ColorScheme.TEXT_COLOR);
        box.setIconTextGap(7); box.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        box.setToolTipText(tip); box.setAlignmentX(Component.LEFT_ALIGNMENT); return box;
    }
    private static JLabel label(String text, boolean strong)
    {
        JLabel label = new JLabel(html(text), SwingConstants.CENTER);
        label.setForeground(strong ? ColorScheme.BRAND_ORANGE : ColorScheme.LIGHT_GRAY_COLOR);
        label.setFont(strong ? FontManager.getRunescapeBoldFont() : FontManager.getDefaultFont().deriveFont(12f));
        label.setAlignmentX(Component.CENTER_ALIGNMENT); return label;
    }
    private static String html(String text) { return "<html><div style='width:145px;text-align:center'>" + text + "</div></html>"; }
}

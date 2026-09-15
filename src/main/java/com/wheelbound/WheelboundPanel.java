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
    private final JComboBox<Object> selector = new JComboBox<>();
    private CustomWheels customWheels;
    private CustomWheels.CustomWheel custom;
    private String customLoadError;
    private boolean rebuildingSelector;
    private final JPanel customCreator = new JPanel(new BorderLayout(0, 5));
    private final JPanel customEntryEditor = new JPanel(new BorderLayout(0, 5));
    private final JTextField wheelName = new JTextField();
    private final JTextField entryName = new JTextField();
    private final JButton createWheel = new JButton("Create wheel");
    private final JButton addEntry = new JButton("Add");
    private final JButton deleteWheel = new JButton("Delete wheel");
    private java.util.function.Predicate<String> confirmDelete = name -> JOptionPane.showConfirmDialog(this,
        html("Delete the custom wheel ?" + name + "? and all its entries? This cannot be undone."),
        "Delete custom wheel", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    private List<WheelEntry> availablePool = List.of();
    private String poolMessage = "";
    private WheelPopup popup;
    private WheelComponent displayed = wheel;
    private String popupTitle, popupResult;
    private final JPanel options = new JPanel(new CardLayout());
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
    private WheelEntry questCompletion, deferredQuestCompletion;
    private boolean hasDeferredQuestCompletion;

    WheelboundPanel(Function<String, String> load, BiConsumer<String, Object> save)
    {
        super();
        getScrollPane().setViewportView(this);
        this.save = save;
        type = WheelType.fromSaved(load.apply("selectedWheel"));
        loadCustomWheels(load);
        for (WheelType wheelType : WheelType.values())
        {
            BossChecklist checklist = new BossChecklist(wheelType == WheelType.CUSTOM ? "Included entries"
                : wheelType == WheelType.SKILLING ? "Included skills"
                : wheelType == WheelType.BOSSING ? "Included bosses"
                : wheelType == WheelType.QUESTING ? "Included quests"
                : wheelType == WheelType.PET_HUNTING ? "Included pets" : "Included encounters",
                (wheelType == WheelType.CUSTOM ? null : load.apply(wheelType.exclusionKey)), value -> {
                    if (wheelType == WheelType.CUSTOM) { customSelectionChanged(value); return; }
                    save.accept(wheelType.exclusionKey, value);
                    generation++;
                    popupResult = "Click the center to spin";
                    if (popup != null) { popup.clearResult(); }
                    updatePool(availablePool, poolMessage, generation);
                    refreshAction.run();
                }, wheelType == WheelType.CUSTOM ? this::deleteCustomEntry : null);
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
        rebuildSelector();
        selector.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean selected, boolean focus)
            {
                super.getListCellRendererComponent(list, value == WheelType.CUSTOM ? "New custom wheel..." : value, index, selected, focus);
                putClientProperty("html.disable", Boolean.TRUE); return this;
            }
        });
        selector.setFont(FontManager.getDefaultBoldFont().deriveFont(12f));
        selector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        selector.setToolTipText("Choose a wheel");
        selector.setPrototypeDisplayValue("Combat Achievements");
        selector.setMinimumSize(new Dimension(0, 32));
        selector.addActionListener(e -> {
            if (rebuildingSelector) { return; }
            Object value = selector.getSelectedItem();
            if (value instanceof CustomWheels.CustomWheel) { selectCustomWheel(((CustomWheels.CustomWheel)value).id); }
            else if (value instanceof WheelType) { selectWheel((WheelType)value); }
        });
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
                }
                JCheckBox box = check(filter.title, filter.tip);
                String saved = filter.savedValue(load);
                // Migrate the two removed master options once through the new sidebar defaults.
                if (saved == null && filter == WheelFilter.ACCOUNT) { saved = load.apply("limitBossesToMyLevel"); }
                if (saved == null && filter == WheelFilter.MAXED_SKILLS) { saved = load.apply("excludeLevel99Skills"); }
                box.setSelected(saved == null ? filter.defaultValue : Boolean.parseBoolean(saved));
                box.addActionListener(e -> { save.accept(filter.key, box.isSelected()); filtersChanged(); });
                filters.put(filter, box); controls.add(box); wheelOptions.add(box);
            }
            if (wheelType == WheelType.CUSTOM)
            {
                customCreator.setOpaque(false);
                JLabel heading = new JLabel("Custom wheel name"); heading.setLabelFor(wheelName);
                customCreator.add(heading, BorderLayout.NORTH);
                customCreator.add(wheelName, BorderLayout.CENTER);
                customCreator.add(createWheel, BorderLayout.SOUTH);
                wheelOptions.add(customCreator);
            }
            options.add(wheelOptions, wheelType.name());
        }
        setupCustomControls();
        showCards();
        content.add(options);
        content.add(Box.createVerticalStrut(8));
        checklistCards.setOpaque(false);
        add(checklistCards, BorderLayout.CENTER);
        add(content, BorderLayout.NORTH);
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(12, 0, 8, 0));
        footer.add(customEntryEditor, BorderLayout.NORTH);
        footer.add(status, BorderLayout.CENTER);
        footer.add(deleteWheel, BorderLayout.SOUTH);
        getWrappedPanel().add(footer, BorderLayout.SOUTH);
        wheel.setSpinAction(() -> {
            if (busy) { return; }
            busy = true; enableControls(false); wheel.setAvailable(false);
            requestCurrent(true);
        });
        xpWheel.setAvailable(false);
        wheel.setFrameListener(this::refreshPopup);
        xpWheel.setFrameListener(this::refreshPopup);
        if (type == WheelType.CUSTOM) { bindCustomChecklist(); refreshCustomPool(false); }
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
        if (busy || value == null || type == value && custom == null) { return; }
        type = value; custom = null; selectionChanged();
    }

    void selectCustomWheel(String id)
    {
        CustomWheels.CustomWheel value = customWheels.find(id);
        if (busy || value == null || custom == value) { return; }
        custom = value; type = WheelType.CUSTOM; selectionChanged();
    }

    private void selectionChanged()
    {
        entryName.setText(""); wheelName.setText("");
        save.accept("selectedWheel", type.title);
        save.accept("selectedCustomWheel", custom == null ? "" : custom.id);
        rebuildSelector(); showCards(); reset(); bindCustomChecklist(); requestCurrent(false);
        if (sidebarActive) { openWheel(); }
    }

    private void requestCurrent(boolean spin)
    { if (type == WheelType.CUSTOM) { refreshCustomPool(spin); } else { action.accept(spin); } }

    private String wheelTitle() { return custom == null ? type.title : custom.name; }

    private void setupCustomControls()
    {
        wheelName.setName("customWheelName"); entryName.setName("customEntryName");
        wheelName.getAccessibleContext().setAccessibleName("Custom wheel name");
        entryName.getAccessibleContext().setAccessibleName("New entry");
        customEntryEditor.setOpaque(false);
        customEntryEditor.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        JLabel heading = new JLabel("New entry"); heading.setLabelFor(entryName);
        customEntryEditor.add(heading, BorderLayout.NORTH);
        JPanel input = new JPanel(new BorderLayout(5, 0)); input.setOpaque(false);
        input.add(entryName, BorderLayout.CENTER); input.add(addEntry, BorderLayout.EAST);
        customEntryEditor.add(input, BorderLayout.CENTER);
        deleteWheel.setBackground(new Color(155, 45, 45)); deleteWheel.setForeground(Color.WHITE);
        deleteWheel.setOpaque(true);
        deleteWheel.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
        deleteWheel.setToolTipText("Delete this custom wheel and all of its entries");
        controls.add(createWheel); controls.add(addEntry); controls.add(deleteWheel);
        createWheel.addActionListener(e -> createCustomWheel(wheelName.getText()));
        wheelName.addActionListener(e -> createCustomWheel(wheelName.getText()));
        addEntry.addActionListener(e -> addCustomEntry(entryName.getText()));
        entryName.addActionListener(e -> addCustomEntry(entryName.getText()));
        deleteWheel.addActionListener(e -> deleteSelectedCustomWheel());
        enableControls(true);
    }

    private void loadCustomWheels(Function<String, String> load)
    {
        customLoadError = null;
        try { customWheels = CustomWheels.load(load.apply(CustomWheels.KEY)); }
        catch (IllegalArgumentException ex) { customLoadError = ex.getMessage(); customWheels = new CustomWheels(); }
        custom = type == WheelType.CUSTOM ? customWheels.find(load.apply("selectedCustomWheel")) : null;
    }

    private void rebuildSelector()
    {
        rebuildingSelector = true;
        try
        {
            selector.removeAllItems();
            for (WheelType value : WheelType.values()) { if (value != WheelType.CUSTOM) { selector.addItem(value); } }
            for (CustomWheels.CustomWheel value : customWheels.wheels()) { selector.addItem(value); }
            selector.addItem(WheelType.CUSTOM);
            selector.setSelectedItem(custom == null ? type : custom);
        }
        finally { rebuildingSelector = false; }
    }

    void createCustomWheel(String name)
    {
        if (busy || customLoadError != null || type != WheelType.CUSTOM || custom != null) { return; }
        try
        {
            CustomWheels.CustomWheel created = customWheels.create(name);
            save.accept(CustomWheels.KEY, customWheels.save());
            wheelName.setText(""); selectCustomWheel(created.id); entryName.requestFocusInWindow();
        }
        catch (IllegalArgumentException ex) { status.setText(html(ex.getMessage())); }
    }

    void addCustomEntry(String name)
    {
        if (busy || custom == null) { return; }
        try
        {
            customWheels.add(custom, name); entryName.setText("");
            customEdited(); entryName.requestFocusInWindow();
        }
        catch (IllegalArgumentException ex) { status.setText(html(ex.getMessage())); }
    }

    private void customSelectionChanged(String excluded)
    {
        if (busy || custom == null) { return; }
        Set<String> ids = new java.util.HashSet<>(java.util.Arrays.asList(excluded.split(",")));
        custom.items.forEach(item -> item.enabled = !ids.contains("CUSTOM_" + item.id));
        customEdited();
    }

    private void deleteCustomEntry(WheelEntry entry)
    {
        if (busy || custom == null) { return; }
        custom.items.removeIf(item -> entry.id.equals("CUSTOM_" + item.id));
        customEdited();
    }

    private void customEdited()
    {
        save.accept(CustomWheels.KEY, customWheels.save());
        generation++; bindCustomChecklist();
        result.setText(html("Spin the wheel")); result.setIcon(null); xpResult.setText("");
        if (popup != null) { popup.clearResult(); }
        refreshCustomPool(false);
    }

    private void bindCustomChecklist()
    { checklists.get(WheelType.CUSTOM).restore(custom == null ? null : custom.exclusions()); }

    void refreshCustomPool(boolean spin)
    {
        if (type != WheelType.CUSTOM) { return; }
        List<WheelEntry> entries = custom == null ? List.of() : custom.entries();
        String message = customLoadError != null ? customLoadError : custom == null
            ? "Name your custom wheel and click Create wheel." : "Add entries below to build your wheel.";
        if (spin) { spinResponse(entries, message, generation); }
        else { updatePool(entries, message, generation); }
    }

    void setDeleteConfirmation(java.util.function.Predicate<String> confirmation) { confirmDelete = confirmation; }

    void deleteSelectedCustomWheel()
    {
        if (busy || custom == null) { return; }
        CustomWheels.CustomWheel target = custom;
        CustomWheels store = customWheels;
        if (!confirmDelete.test(target.name) || busy || custom != target || customWheels != store) { return; }
        customWheels.remove(target); save.accept(CustomWheels.KEY, customWheels.save());
        custom = null;
        if (customWheels.wheels().isEmpty()) { type = WheelType.BOSSING; }
        else { custom = customWheels.wheels().get(0); type = WheelType.CUSTOM; }
        selectionChanged();
    }

    private void showCards()
    {
        customCreator.setVisible(type == WheelType.CUSTOM && custom == null);
        customEntryEditor.setVisible(type == WheelType.CUSTOM && custom != null);
        deleteWheel.setVisible(type == WheelType.CUSTOM && custom != null);
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
    boolean selected(WheelFilter filter) { return filters.get(filter).isSelected(); }
    void reloadPreferences(Function<String, String> load)
    {
        entryName.setText(""); wheelName.setText("");
        filters.forEach((filter, box) -> {
            String value = filter.savedValue(load);
            if (value == null && filter == WheelFilter.ACCOUNT) { value = load.apply("limitBossesToMyLevel"); }
            if (value == null && filter == WheelFilter.MAXED_SKILLS) { value = load.apply("excludeLevel99Skills"); }
            box.setSelected(value == null ? filter.defaultValue : Boolean.parseBoolean(value));
        });
        checklists.forEach((wheelType, checklist) -> checklist.restore(wheelType == WheelType.CUSTOM ? null : load.apply(wheelType.exclusionKey)));
        type = WheelType.fromSaved(load.apply("selectedWheel")); loadCustomWheels(load);
        rebuildSelector(); showCards(); reset(); bindCustomChecklist();
        if (type == WheelType.CUSTOM) { refreshCustomPool(false); }
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
        displayed = wheel; popupTitle = wheelTitle(); popupResult = "Click the center to spin";
        popup.show(popupTitle, displayed, popupResult, () -> displayed.requestSpin(), () -> {
            generation++;
            wheel.cancel(); xpWheel.cancel();
            if (busy) { result.setText(html("Spin cancelled")); xpResult.setText(""); }
            finish();
        });
        if (questCompletion != null) { popup.complete(questCompletion, QuestPool.COMPLETE); }
    }

    void setQuestCompletion(WheelEntry entry)
    {
        if (type != WheelType.QUESTING) { return; }
        if (busy) { deferredQuestCompletion = entry; hasDeferredQuestCompletion = true; return; }
        boolean hadCompletion = questCompletion != null;
        questCompletion = entry;
        if (entry != null)
        {
            result.setText(html(QuestPool.COMPLETE));
            result.setIcon(entry.icon == null ? null : new ImageIcon(entry.icon));
            if (popup != null && !popup.hasResult()) { popup.complete(entry, QuestPool.COMPLETE); }
        }
        else if (hadCompletion)
        {
            result.setText(html("Spin the wheel")); result.setIcon(null);
            if (popup != null) { popup.clearResult(); }
        }
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
        displayed = wheel; popupTitle = wheelTitle();
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
        displayed = wheel; popupTitle = wheelTitle(); popupResult = "Spinning...";
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
            else
            {
                finish();
                if (popup != null)
                { popup.complete(questCompletion != null ? questCompletion : choices.get(selected),
                    questCompletion != null ? QuestPool.COMPLETE : null); }
            }
        });
        revalidate();
    }

    private void finish()
    {
        xpWheel.setAvailable(false);
        busy = false; enableControls(true); wheel.setAvailable(!wheel.entries().isEmpty());
        displayed = wheel; popupTitle = wheelTitle(); refreshPopup();
        if (deferred != null)
        {
            List<WheelEntry> entries = deferred; deferred = null;
            updatePool(entries, deferredMessage, generation);
        }
        if (hasDeferredQuestCompletion)
        {
            hasDeferredQuestCompletion = false;
            setQuestCompletion(deferredQuestCompletion); deferredQuestCompletion = null;
        }
    }
    private void enableControls(boolean enabled)
    {
        controls.forEach(c -> c.setEnabled(enabled)); selector.setEnabled(enabled);
        checklists.values().forEach(c -> c.setEnabled(enabled));
        wheelName.setEnabled(enabled && customLoadError == null); entryName.setEnabled(enabled && custom != null);
        createWheel.setEnabled(enabled && customLoadError == null);
        addEntry.setEnabled(enabled && custom != null); deleteWheel.setEnabled(enabled && custom != null);
    }
    void reset()
    {
        if (popup != null) { popup.hide(); }
        generation++; wheel.cancel(); xpWheel.cancel(); busy = false; deferred = null; questCompletion = null; deferredQuestCompletion = null; hasDeferredQuestCompletion = false;
        displayed = wheel; popupTitle = wheelTitle(); availablePool = List.of();
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
    private static String html(String text) { return "<html><div style='width:145px;text-align:center'>" + BossChecklist.escape(text) + "</div></html>"; }
}

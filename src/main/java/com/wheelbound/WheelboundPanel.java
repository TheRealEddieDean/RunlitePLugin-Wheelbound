package com.wheelbound;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.*;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

/** Layout and two-stage spin coordination. Game reads and eligibility live outside Swing. */
public class WheelboundPanel extends PluginPanel
{
    private final WheelComponent wheel = new WheelComponent();
    private final WheelComponent xpWheel = new WheelComponent();
    private final JLabel result = label("Spin the wheel", true);
    private final JLabel xpResult = label("", false);
    private final JLabel status = label("Loading eligible entries...", false);
    private final JButton open = new JButton("SPIN");
    private final BossChecklist bossChecklist;
    private List<WheelEntry> availablePool = List.of();
    private String poolMessage = "";
    private WheelPopup popup;
    private WheelComponent displayed = wheel;
    private String popupTitle, popupResult;
    private final JPanel options = new JPanel(new CardLayout());
    private final JCheckBox all = check("All bosses", "Use the broad pool; raid and master level settings still apply.");
    private final JCheckBox raids = check("Include raids", "Include raid modes from the boss catalog.");
    private final JCheckBox incomplete = check("<html><div style='width:125px'>Only bosses with incomplete Combat Achievements</div></html>", "Only mapped encounters with at least one incomplete local CA.");
    private final JCheckBox xp = check("Include XP goal", "Automatically spin a second, weighted XP wheel after selecting a skill.");
    private final List<AbstractButton> controls = new ArrayList<>();
    private final BiConsumer<String, Object> save;
    private final Random random = new Random();
    private Consumer<Boolean> action = ignored -> {};
    private String mode;
    private boolean busy;
    private long generation;
    private List<WheelEntry> deferred;
    private String deferredMessage;

    WheelboundPanel(Function<String, String> load, BiConsumer<String, Object> save)
    {
        this.save = save;
        bossChecklist = new BossChecklist(load.apply("excludedBosses"), value -> {
            save.accept("excludedBosses", value);
            generation++;
            popupResult = "Click the center to spin";
            if (popup != null) { popup.clearResult(); }
            updatePool(availablePool, poolMessage, generation);
        });
        mode = "Skilling".equals(load.apply("selectedWheel")) ? "Skilling" : "Bossing";
        incomplete.setSelected(Boolean.parseBoolean(load.apply("unfinishedOnly")));
        all.setSelected(!incomplete.isSelected());
        raids.setSelected(Boolean.parseBoolean(load.apply("includeRaids")));
        xp.setSelected(Boolean.parseBoolean(load.apply("includeXpGoal")));
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));
        JPanel content = column();
        JLabel title = label("Wheelbound", true);
        title.setFont(FontManager.getRunescapeBoldFont().deriveFont(22f));
        content.add(title);
        content.add(Box.createVerticalStrut(10));
        JPanel tabs = new JPanel(new GridLayout(1, 2, 4, 0));
        tabs.setOpaque(false);
        ButtonGroup group = new ButtonGroup();
        for (String name : List.of("Bossing", "Skilling"))
        {
            JToggleButton tab = new JToggleButton(name, mode.equals(name));
            tab.setFont(FontManager.getDefaultBoldFont().deriveFont(12f));
            tab.setFocusPainted(false);
            tab.setToolTipText(name.equals("Bossing") ? "What boss should I do?" : "What skill should I train?");
            tab.addActionListener(e -> {
                mode = name; save.accept("selectedWheel", mode);
                ((CardLayout) options.getLayout()).show(options, mode);
                reset(false); bossChecklist.setVisible(mode.equals("Bossing")); action.accept(false);
            });
            group.add(tab); tabs.add(tab); controls.add(tab);
        }
        tabs.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        content.add(tabs);
        content.add(Box.createVerticalStrut(8));
        open.setAlignmentX(Component.CENTER_ALIGNMENT);
        open.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        open.setEnabled(false);
        open.setBackground(new Color(35, 125, 63));
        open.setForeground(Color.WHITE);
        open.setFont(FontManager.getDefaultBoldFont().deriveFont(14f));
        open.setOpaque(true);
        open.setToolTipText("Open the centered wheel and start spinning");
        open.addActionListener(e -> { openWheel(); wheel.requestSpin(); });
        content.add(open);
        content.add(Box.createVerticalStrut(12));
        content.add(result);
        content.add(xpResult);
        content.add(Box.createVerticalStrut(10));
        JPanel bossOptions = column();
        bossOptions.add(all); bossOptions.add(raids); bossOptions.add(incomplete);
        JPanel skillOptions = column(); skillOptions.add(xp);
        options.setOpaque(false);
        options.add(bossOptions, "Bossing"); options.add(skillOptions, "Skilling");
        options.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        ((CardLayout) options.getLayout()).show(options, mode);
        content.add(options);
        content.add(Box.createVerticalStrut(8));
        content.add(status);
        content.add(Box.createVerticalStrut(12));
        bossChecklist.setVisible(mode.equals("Bossing"));
        content.add(bossChecklist);
        add(content, BorderLayout.NORTH);
        controls.addAll(List.of(all, raids, incomplete, xp, open));
        all.addActionListener(e -> { incomplete.setSelected(!all.isSelected()); filtersChanged(); });
        incomplete.addActionListener(e -> { all.setSelected(!incomplete.isSelected()); filtersChanged(); });
        raids.addActionListener(e -> filtersChanged());
        xp.addActionListener(e -> { save.accept("includeXpGoal", xp.isSelected());  revalidate(); });
        wheel.setSpinAction(() -> {
            if (busy) { return; }
            busy = true; enableControls(false); wheel.setAvailable(false);
            action.accept(true);
        });
        xpWheel.setAvailable(false);
        wheel.setFrameListener(this::refreshPopup);
        xpWheel.setFrameListener(this::refreshPopup);
    }

    void setPopup(WheelPopup value) { popup = value; }
    WheelComponent primaryWheel() { return wheel; }

    void openWheel()
    {
        if (popup == null || busy || wheel.entries().isEmpty()) { return; }
        displayed = wheel; popupTitle = mode; popupResult = "Click the center to spin";
        popup.show(popupTitle, displayed, popupResult, wheel::requestSpin, () -> {
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
        save.accept("unfinishedOnly", incomplete.isSelected()); save.accept("includeRaids", raids.isSelected());
        generation++; wheel.setAvailable(false); open.setEnabled(false);
        if (popup != null) { popup.clearResult(); }
        action.accept(false);
    }
    void setAction(Consumer<Boolean> value) { action = value; }
    String mode() { return mode; }
    boolean includeRaids() { return raids.isSelected(); }
    boolean incompleteOnly() { return incomplete.isSelected(); }
    long generation() { return generation; }
    boolean busy() { return busy; }

    void updatePool(List<WheelEntry> entries, String message, long revision)
    {
        if (revision != generation) { return; }
        if (busy) { deferred = entries; deferredMessage = message; return; }
        availablePool = List.copyOf(entries); poolMessage = message;
        if (mode.equals("Bossing")) { bossChecklist.updateEntries(entries); }
        List<WheelEntry> included = mode.equals("Bossing") ? bossChecklist.included(entries) : entries;
        displayed = wheel; popupTitle = mode;
        wheel.setEntries(included); wheel.setAvailable(!included.isEmpty());
        open.setEnabled(!included.isEmpty());
        status.setToolTipText(message);
        status.setText(html(mode.equals("Bossing") && !entries.isEmpty()
            ? included.size() + " of " + entries.size() + " eligible bosses included."
                + (included.isEmpty() ? " Check a boss below to spin." : "")
            : message));
    }

    void spinResponse(List<WheelEntry> entries, String message, long revision)
    {
        if (revision != generation) { return; }
        List<WheelEntry> choices = mode.equals("Bossing") ? bossChecklist.included(entries) : entries;
        status.setText(html(message));
        if (choices.isEmpty()) { finish(); updatePool(entries, message, revision); return; }
        int selected = WheelSelection.select(choices, random);
        displayed = wheel; popupTitle = mode; popupResult = "Spinning...";
        if (popup != null) { popup.clearResult(); }
        result.setIcon(null); result.setText(html("Spinning...")); xpResult.setText("");
        boolean withXp = mode.equals("Skilling") && xp.isSelected();
        wheel.animate(choices, selected, () -> {
            popupResult = choices.get(selected).label;
            result.setText(html(popupResult));
            if (choices.get(selected).icon != null)
            {
                result.setIcon(new ImageIcon(net.runelite.client.util.ImageUtil.resizeImage(choices.get(selected).icon, 48, 48)));
                result.setHorizontalTextPosition(SwingConstants.CENTER);
                result.setVerticalTextPosition(SwingConstants.BOTTOM);
            }
            if (withXp)
            {
                xpResult.setText(html("Choosing an XP target...")); revalidate();
                displayed = xpWheel; popupTitle = choices.get(selected).label + " - XP target";
                List<WheelEntry> goals = XpGoal.entries();
                int goal = WheelSelection.select(goals, random);
                xpWheel.animate(goals, goal, () -> { popupResult = choices.get(selected).label + " - " + goals.get(goal).label; xpResult.setText(html(goals.get(goal).label)); finish(); if (popup != null) { popup.complete(choices.get(selected), goals.get(goal).label); } });
            }
            else { finish(); if (popup != null) { popup.complete(choices.get(selected), null); } }
        });
        revalidate();
    }

    private void finish()
    {
        busy = false; enableControls(true); wheel.setAvailable(!wheel.entries().isEmpty());
        open.setEnabled(!wheel.entries().isEmpty()); refreshPopup();
        if (deferred != null)
        {
            List<WheelEntry> entries = deferred; deferred = null;
            updatePool(entries, deferredMessage, generation);
        }
    }
    private void enableControls(boolean enabled) { controls.forEach(c -> c.setEnabled(enabled)); bossChecklist.setEnabled(enabled); }
    void reset() { reset(true); }
    private void reset(boolean hide)
    {
        if (popup != null) { if (hide) { popup.hide(); } else { popup.clearResult(); } }
        generation++; wheel.cancel(); xpWheel.cancel(); busy = false; deferred = null;
        displayed = wheel; popupTitle = mode; availablePool = List.of();
        wheel.setEntries(List.of());
        result.setIcon(null); result.setText(html("Spin the wheel")); xpResult.setText("");
        enableControls(true); open.setEnabled(false); revalidate();
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

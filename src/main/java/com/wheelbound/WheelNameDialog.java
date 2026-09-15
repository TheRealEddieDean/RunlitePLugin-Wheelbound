package com.wheelbound;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Function;
import javax.swing.*;

/** EDT-only name form. Native Swing input retains caret, selection, paste and accessibility. */
final class WheelNameDialog
{
    private final JPanel content = new JPanel(new BorderLayout(0, 14)) {
        @Override protected void paintComponent(Graphics graphics)
        {
            Graphics2D g = (Graphics2D)graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setPaint(new GradientPaint(0, 0, new Color(28, 31, 32), getWidth(), getHeight(), new Color(19, 22, 23)));
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g.setColor(WheelStyle.GOLD.darker());
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18); g.dispose();
        }
    };
    private final JTextField name = new JTextField();
    private final JButton create = new JButton("Create");
    private final JLabel error = new JLabel(" ");
    private JDialog window;
    private boolean open;
    private Function<String, String> submit = ignored -> null;

    WheelNameDialog()
    {
        content.setBorder(BorderFactory.createEmptyBorder(18, 24, 22, 24));
        content.setPreferredSize(new Dimension(380, 245));
        JPanel header = new JPanel(new BorderLayout()); header.setOpaque(false);
        JLabel title = new JLabel("New custom wheel", SwingConstants.CENTER);
        title.setFont(new Font(Font.SERIF, Font.BOLD, 24)); title.setForeground(WheelStyle.GOLD);
        JButton close = new JButton("\u00d7"); close.setForeground(WheelStyle.GOLD);
        close.setContentAreaFilled(false); close.setBorderPainted(false);
        close.setToolTipText("Cancel"); close.getAccessibleContext().setAccessibleName("Cancel creation");
        close.addActionListener(e -> hide());
        header.add(title, BorderLayout.CENTER); header.add(close, BorderLayout.EAST);
        content.add(header, BorderLayout.NORTH);
        JPanel fields = new JPanel(new BorderLayout(0, 8)); fields.setOpaque(false);
        JLabel label = new JLabel("Wheel name"); label.setForeground(new Color(225, 221, 207)); label.setLabelFor(name);
        name.setName("customWheelName"); name.getAccessibleContext().setAccessibleName("Wheel name");
        name.setBackground(new Color(35, 38, 39)); name.setForeground(Color.WHITE); name.setCaretColor(WheelStyle.GOLD);
        name.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(WheelStyle.GOLD.darker()),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        error.setForeground(new Color(240, 130, 130));
        fields.add(label, BorderLayout.NORTH); fields.add(name, BorderLayout.CENTER); fields.add(error, BorderLayout.SOUTH);
        content.add(fields, BorderLayout.CENTER);
        create.setBackground(new Color(42, 133, 65)); create.setForeground(Color.WHITE); create.setOpaque(true);
        create.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        content.add(create, BorderLayout.SOUTH);
        create.addActionListener(e -> submit()); name.addActionListener(e -> submit());
        content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
        content.getActionMap().put("cancel", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent event) { hide(); }
        });
    }

    void show(Component anchor, Function<String, String> action)
    {
        if (open) { if (window != null) { window.toFront(); } return; }
        submit = action; name.setText(""); error.setText(" "); open = true;
        // Tests can exercise the exact form without creating native windows.
        if (anchor == null || !anchor.isShowing() || GraphicsEnvironment.isHeadless()) { return; }
        window = new JDialog(SwingUtilities.getWindowAncestor(anchor), "New custom wheel", Dialog.ModalityType.MODELESS);
        window.setUndecorated(true); window.setResizable(false); window.setContentPane(content);
        window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent event) { hide(); }
        });
        window.pack(); window.setLocationRelativeTo(anchor); window.setVisible(true);
        name.requestFocusInWindow();
    }

    private void submit()
    {
        if (!open) { return; }
        String message = submit.apply(name.getText());
        if (message != null)
        { error.setText("<html>" + BossChecklist.escape(message) + "</html>"); name.requestFocusInWindow(); }
    }

    void hide()
    { open = false; submit = ignored -> null; if (window != null) { window.dispose(); window = null; } }
    boolean isOpen() { return open; }
    JPanel content() { return content; }
}

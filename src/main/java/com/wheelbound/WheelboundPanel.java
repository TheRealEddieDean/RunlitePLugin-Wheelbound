package com.wheelbound;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.PluginPanel;

public class WheelboundPanel extends PluginPanel
{
	private static final String DEFAULT_RESULT = "Spin the wheel";

	private final JLabel resultLabel = new JLabel(DEFAULT_RESULT, SwingConstants.CENTER);

	@Inject
	private WheelboundPlugin plugin;

	@Inject
	public WheelboundPanel()
	{
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(12, 12, 12, 12));

		JPanel content = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(6, 6, 6, 6);

		JLabel title = new JLabel("WHEELBOUND", SwingConstants.CENTER);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
		constraints.gridy = 0;
		content.add(title, constraints);

		JLabel subtitle = new JLabel("Let the wheel decide.", SwingConstants.CENTER);
		constraints.gridy = 1;
		content.add(subtitle, constraints);

		resultLabel.setFont(resultLabel.getFont().deriveFont(Font.BOLD, 20f));
		resultLabel.setBorder(BorderFactory.createEmptyBorder(18, 4, 18, 4));
		constraints.gridy = 2;
		content.add(resultLabel, constraints);

		JButton spinButton = new JButton("SPIN");
		spinButton.setPreferredSize(new Dimension(0, 40));
		spinButton.addActionListener(event -> plugin.spin());
		constraints.gridy = 3;
		constraints.insets = new Insets(6, 6, 12, 6);
		content.add(spinButton, constraints);

		JLabel hint = new JLabel("Your next activity is chosen at random.", SwingConstants.CENTER);
		hint.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));
		constraints.gridy = 4;
		content.add(hint, constraints);

		add(content, BorderLayout.NORTH);
	}

	public void setResult(String result)
	{
		resultLabel.setText(result == null ? DEFAULT_RESULT : result);
	}
}

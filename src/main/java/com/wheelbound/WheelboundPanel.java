package com.wheelbound;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.util.List;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.PluginPanel;

public class WheelboundPanel extends PluginPanel
{
	private static final String DEFAULT_RESULT = "Spin the wheel";
	private static final int WHEEL_SIZE = 280;
	private static final int ANIMATION_DURATION_MS = 3500;
	private static final int FRAME_MS = 16;

	private final JLabel resultLabel = new JLabel(DEFAULT_RESULT, SwingConstants.CENTER);
	private final JButton spinButton = new JButton("SPIN");
	private final WheelCanvas wheelCanvas = new WheelCanvas();

	private Consumer<Void> spinAction;

	@Inject
	public WheelboundPanel()
	{
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel title = new JLabel("WHEELBOUND", SwingConstants.CENTER);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
		title.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		JLabel subtitle = new JLabel("Let the wheel decide.", SwingConstants.CENTER);

		resultLabel.setFont(resultLabel.getFont().deriveFont(Font.BOLD, 18f));
		resultLabel.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));

		spinButton.setPreferredSize(new Dimension(0, 40));
		spinButton.addActionListener(event ->
		{
			if (spinAction != null)
			{
				spinAction.accept(null);
			}
		});

		JPanel top = new JPanel(new BorderLayout());
		top.add(title, BorderLayout.NORTH);
		top.add(subtitle, BorderLayout.CENTER);

		JPanel bottom = new JPanel(new BorderLayout(0, 6));
		bottom.add(resultLabel, BorderLayout.NORTH);
		bottom.add(spinButton, BorderLayout.CENTER);

		add(top, BorderLayout.NORTH);
		add(wheelCanvas, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
	}

	public void setSpinAction(Consumer<Void> spinAction)
	{
		this.spinAction = spinAction;
	}

	public void animate(List<String> activities, int selectedIndex, Runnable completed)
	{
		wheelCanvas.animate(activities, selectedIndex, completed);
		spinButton.setEnabled(false);
		resultLabel.setText("Spinning...");
	}

	private void finishAnimation(String result)
	{
		resultLabel.setText(result == null ? DEFAULT_RESULT : result);
		spinButton.setEnabled(true);
	}

	private class WheelCanvas extends JPanel
	{
		private List<String> activities = List.of();
		private double angle;
		private double targetAngle;
		private long animationStart;
		private Timer timer;
		private Runnable completed;

		WheelCanvas()
		{
			setPreferredSize(new Dimension(WHEEL_SIZE, WHEEL_SIZE));
			setMinimumSize(new Dimension(WHEEL_SIZE, WHEEL_SIZE));
			setOpaque(false);
		}

		void animate(List<String> activities, int selectedIndex, Runnable completed)
		{
			this.activities = List.copyOf(activities);
			this.completed = completed;
			int count = this.activities.size();
			double segment = 360.0 / count;
			double desired = 90.0 - (selectedIndex + 0.5) * segment;
			double currentDegrees = Math.toDegrees(angle);
			double normalizedCurrent = ((currentDegrees % 360.0) + 360.0) % 360.0;
			double delta = desired - normalizedCurrent;
			while (delta <= 0)
			{
				delta += 360.0;
			}

			targetAngle = angle + Math.toRadians(delta + 360.0 * 5);
			animationStart = System.currentTimeMillis();

			if (timer != null)
			{
				timer.stop();
			}

			timer = new Timer(FRAME_MS, event -> updateAnimation());
			timer.start();
		}

		private void updateAnimation()
		{
			double elapsed = System.currentTimeMillis() - animationStart;
			double progress = Math.min(1.0, elapsed / ANIMATION_DURATION_MS);
			double eased = 1.0 - Math.pow(1.0 - progress, 4);
			angle = angle + (targetAngle - angle) * (eased - previousEase);
			previousEase = eased;
			repaint();

			if (progress >= 1.0)
			{
				timer.stop();
				previousEase = 0.0;
				finishAnimation(activities.get(selectedIndex()));
				if (completed != null)
				{
					completed.run();
				}
			}
		}

		private double previousEase;

		@Override
		protected void paintComponent(Graphics graphics)
		{
			super.paintComponent(graphics);
			if (activities.isEmpty())
			{
				return;
			}

			Graphics2D g = (Graphics2D) graphics.create();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			int size = Math.min(getWidth(), getHeight()) - 20;
			int x = (getWidth() - size) / 2;
			int y = (getHeight() - size) / 2;
			int centerX = x + size / 2;
			int centerY = y + size / 2;
			double segment = 360.0 / activities.size();

			g.rotate(angle, centerX, centerY);
			for (int i = 0; i < activities.size(); i++)
			{
				double start = i * segment;
				g.setColor(getSegmentColor(i));
				g.fill(new Arc2D.Double(x, y, size, size, start, segment, Arc2D.PIE));
				g.setColor(getForeground());
				g.setStroke(new BasicStroke(2f));
				g.draw(new Arc2D.Double(x, y, size, size, start, segment, Arc2D.PIE));

				double textAngle = Math.toRadians(start + segment / 2.0);
				int textRadius = size / 3;
				int textX = centerX + (int) (Math.cos(textAngle) * textRadius);
				int textY = centerY - (int) (Math.sin(textAngle) * textRadius);

				g.setFont(getFont().deriveFont(Font.BOLD, 12f));
				String text = activities.get(i);
				int textWidth = g.getFontMetrics().stringWidth(text);
				g.drawString(text, textX - textWidth / 2, textY);
			}
			g.rotate(-angle, centerX, centerY);

			g.setColor(getBackground());
			g.fillOval(centerX - 22, centerY - 22, 44, 44);
			g.setColor(getForeground());
			g.setStroke(new BasicStroke(2f));
			g.drawOval(centerX - 22, centerY - 22, 44, 44);

			// Fixed pointer at the top of the wheel.
			int[] xPoints = {centerX - 10, centerX + 10, centerX};
			int[] yPoints = {y - 4, y - 4, y + 16};
			g.fillPolygon(xPoints, yPoints, 3);

			g.dispose();
		}

		private java.awt.Color getSegmentColor(int index)
		{
			float hue = (float) index / Math.max(1, activities.size());
			return java.awt.Color.getHSBColor(hue, 0.55f, 0.9f);
		}
	}
}

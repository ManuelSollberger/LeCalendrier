package ch.telegraphstudios.LeCal.GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import ch.telegraphstudios.TSMenuBar.Design;
import ch.telegraphstudios.TSMenuBar.GraphicsUtils;

/**
 * This button is used in the welcome screen.
 */
public class WelcomeButton extends JPanel {

	private String title;
	
	public WelcomeButton(String title) {
		this.title = title;
		this.setBackground(new Color(200, 200, 200, 20));
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = GraphicsUtils.prepareGraphics(g);
		
		g2d.setFont(new Font(Design.DEFAULT_FONT_FAMILY, Font.PLAIN, 50));
		g2d.setColor(Color.WHITE);
		Rectangle2D titleBounds = g2d.getFontMetrics().getStringBounds(this.title, g2d);
		g2d.drawString(this.title, (int)(this.getWidth() / 2 - (titleBounds.getWidth() / 2)), (int)(this.getHeight() / 2 + (titleBounds.getHeight() / 4)));
	}
	
}

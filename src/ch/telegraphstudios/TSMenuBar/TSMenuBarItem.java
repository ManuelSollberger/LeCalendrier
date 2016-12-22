package ch.telegraphstudios.TSMenuBar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class TSMenuBarItem extends JPanel implements MouseListener {
	
	public static final int MENU_BAR_ITEM_PADDING = 8;
	public static final Font MENU_BAR_ITEM_FONT_BOLD = new Font(Design.DEFAULT_FONT_FAMILY, Font.BOLD, 14);
	public static final Font MENU_BAR_ITEM_FONT = new Font(Design.DEFAULT_FONT_FAMILY, Font.PLAIN, 14);
	
	/**
	 * Time in nanoseconds when the menu should close when the mouse is released.
	 */
	private static final int MENU_DRAG_TIMEOUT = 250000000;
	
	protected String name;
	
	/**
	 * Used to count the time between mouse press and release to provide a good behavior.
	 */
	private long mouseDownTime = 0;
	
	private TSMenu menu;
	
	public TSMenuBarItem(String name) {
		this.name = name;
		
		this.setFont(MENU_BAR_ITEM_FONT);
		this.setSize(GraphicsUtils.getStringSize(this.name, this.getFont()).width + (MENU_BAR_ITEM_PADDING * 2), TSMenuBar.MENU_BAR_HEIGHT);
		this.setBackground(new Color(0, 0, 0, 0));
		this.addMouseListener(this);
		this.setFocusable(false);
	}
	
	public void setMenu(TSMenu menu) {
		this.menu = menu;
	}
	
	public TSMenu getMenu() {
		return this.menu;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = GraphicsUtils.prepareGraphics(g);
		
		//Draw background
		Color colorA = GraphicsUtils.darkerColor(Design.DESIGN_COLOR, -10);
		Color colorB = Design.DESIGN_COLOR;
		
		if (TSMenuBar.isInitialized()) {
			if (TSMenuBar.getInstance().isMenuOpened() && TSMenuBar.getInstance().getSelectedItem().equals(this)) {
				colorA = Design.SELECTION_COLOR;
				colorB = GraphicsUtils.darkerColor(colorA, -20);
			}
		}
		
		g2d.setPaint(new GradientPaint(0, 0, colorA, 0, TSMenuBar.MENU_BAR_HEIGHT, colorB));
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		Dimension stringSize = GraphicsUtils.getStringSize(this.name, g2d);
		
		g2d.setColor(Color.WHITE);
		g2d.setFont(this.getFont());
		g2d.drawString(this.name, MENU_BAR_ITEM_PADDING, TSMenuBar.MENU_BAR_HEIGHT - (int)(stringSize.height / 2));
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void updateState() {
		if (TSMenuBar.getInstance().getSelectedItem() != null && TSMenuBar.getInstance().getSelectedItem().equals(this)) {
			this.menu.open(new Point(this.getX(), TSMenuBar.MENU_BAR_HEIGHT));
		}
		else {
			this.menu.close();
		}
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		this.mouseDownTime = System.nanoTime();
		
		if (!TSMenuBar.getInstance().isMenuOpened()) {
			TSMenuBar.getInstance().setSelectedItem(this);
		}
		else {
			TSMenuBar.getInstance().setSelectedItem(null);
		}
		this.updateState();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (System.nanoTime() - this.mouseDownTime > MENU_DRAG_TIMEOUT) {
			TSMenuBar.getInstance().setSelectedItem(null);
			this.updateState();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (TSMenuBar.getInstance().isMenuOpened()) {
			TSMenuBar.getInstance().setSelectedItem(this);
			this.repaint();
			this.updateState();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) { }
	
}

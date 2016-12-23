package ch.telegraphstudios.TSMenuBar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class TSMenuItem extends JPanel implements MouseListener, TSMenuItemCondition {
	
	public static final int MENU_ITEM_TOP_MARGIN = 2;
	private static final int MENU_ITEM_LEFT_MARGIN = 16;
	private static final int MENU_ITEM_RIGHT_MARGIN = 32;

	private boolean hovered;
	private String title;
	private TSMenu menu;
	private TSMenuItemCondition condition;
	
	private ArrayList<TSMenuItemClickListener> clickListeners = new ArrayList<TSMenuItemClickListener>();
	
	public TSMenuItem(String title) {
		this.title = title;
		this.setFont(TSMenuBarItem.MENU_BAR_ITEM_FONT);
		this.updateSize();
		this.addMouseListener(this);
		this.setBackground(new Color(0, 0, 0, 1));
		this.setCondition(this);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = GraphicsUtils.prepareGraphics(g);
		
		g2d.setFont(this.getFont());
		
		if (this.hovered && this.condition.isEnabled(this)) {
			g2d.setPaint(new GradientPaint(0, 0, GraphicsUtils.darkerColor(Design.SELECTION_COLOR, -20), 0, this.getHeight(), Design.SELECTION_COLOR));
		}
		else {
			g2d.setPaint(Design.DESIGN_COLOR);
		}
		
		g2d.fillRect(0, MENU_ITEM_TOP_MARGIN, this.getWidth(), this.getHeight() - (MENU_ITEM_TOP_MARGIN * 2));
		
		Dimension stringSize = GraphicsUtils.getStringSize(this.title, g2d);
		g2d.setColor(Color.WHITE);
		if (!this.condition.isEnabled(this)) {
			g2d.setColor(new Color(0xBBBBBB));
		}
		g2d.drawString(this.title, TSMenuBarItem.MENU_BAR_ITEM_PADDING + MENU_ITEM_LEFT_MARGIN, this.getHeight() - stringSize.height / 2);
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
		this.updateSize();
	}
	
	public void setCondition(TSMenuItemCondition condition) {
		this.condition = condition;
	}
	
	/**
	 * Recalculates the size of this item.
	 */
	private void updateSize() {
		this.setSize((TSMenuBarItem.MENU_BAR_ITEM_PADDING * 2) + GraphicsUtils.getStringSize(this.title, this.getFont()).width + MENU_ITEM_LEFT_MARGIN + MENU_ITEM_RIGHT_MARGIN, TSMenuBar.MENU_BAR_HEIGHT + (MENU_ITEM_TOP_MARGIN));
	}
	
	public void setMenu(TSMenu menu) {
		this.menu = menu;
	}
	
	public void addMenuItemClickListener(TSMenuItemClickListener listener) {
		this.clickListeners.add(listener);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		this.hovered = false;
		this.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (this.condition.isEnabled(this)) {
			TSMenuBar.getInstance().setSelectedItem(null);
			this.menu.close();
			
			for (TSMenuItemClickListener clickListener : clickListeners) {
				clickListener.onClick();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.hovered = true;
		this.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.hovered = false;
		this.repaint();
	}

	@Override
	public boolean isEnabled(TSMenuItem item) {
		return true;
	}

}

package ch.telegraphstudios.TSMenuBar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TSMenu extends JFrame implements WindowFocusListener, MouseListener {
	
	private ArrayList<TSMenuItem> items = new ArrayList<TSMenuItem>();
	
	public TSMenu() {
		this.setUndecorated(true);
		this.setBackground(new Color(0, 0, 0, 1));
		this.setType(Type.UTILITY);
		this.setSize(Utils.getScreenSize());
		this.addMouseListener(this);
		this.setLayout(null);
		this.setFocusable(false);
		
		//Create content view
		TSMenuContent content = new TSMenuContent(this);
		content.setSize(this.getSize());
		this.setContentPane(content);
	}
	
	public void addItem(TSMenuItem item) {
		item.setMenu(this);
		
		Dimension currentSize = this.updateSize();
		item.setLocation(0, currentSize.height + TSMenuItem.MENU_ITEM_TOP_MARGIN);
		
		this.items.add(item);
		this.add(item);
		this.updateSize();
	}
	
	public ArrayList<TSMenuItem> getItems() {
		return this.items;
	}
	
	public void open(Point origin) {
		if (!this.isVisible()) {
			this.setLocation(origin);
			this.setVisible(true);
		}
	}
	
	public void close() {
		this.setVisible(false);
	}
	
	/**
	 * Used to recalculate the needed size of this menu.
	 */
	public Dimension updateSize() {
		int widest = 0;
		int height = 0;
		
		for (TSMenuItem item : items) {
			double size = item.getWidth();
			if (size > widest) {
				widest = (int)size;
			}
			height += item.getHeight();
		}
		
		for (TSMenuItem item : items) {
			item.setSize(widest, item.getHeight());
		}
		
		if (widest > 0) {
			return new Dimension(widest, height);
		}
		else {
			return new Dimension(0, 0);
		}
	}

	@Override
	public void windowGainedFocus(WindowEvent e) { }

	@Override
	public void windowLostFocus(WindowEvent e) {
		//Hide this frame when the focus is lost.
		this.setVisible(false);
	}

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) {
		TSMenuBar.getInstance().setSelectedItem(null);
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

}

class TSMenuContent extends JPanel {
	
	private TSMenu menu;
	
	public TSMenuContent(TSMenu menu) {
		this.menu = menu;
		this.setLayout(null);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = GraphicsUtils.prepareGraphics(g);
		
		Dimension menuSize = menu.updateSize();
		
		g2d.setColor(Design.DESIGN_COLOR);
		g2d.fillRoundRect(0, 0, menuSize.width, menuSize.height + (2 * TSMenuItem.MENU_ITEM_TOP_MARGIN), TSMenuItem.MENU_ITEM_TOP_MARGIN, TSMenuItem.MENU_ITEM_TOP_MARGIN);
		//g2d.fillRect(0, 0, menuSize.width, menuSize.height);
	}
	
}

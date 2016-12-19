package ch.telegraphstudios.TSMenuBar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window.Type;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TSWindowButton extends JPanel implements MouseListener {

	public static final Color CLOSE_COLOR = new Color(0xFF7777);
	public static final Color MINIMIZE_COLOR = new Color(0xFFFF77);
	
	public static final int WINDOW_BUTTON_BORDER_RADIUS = 6;
	
	public static final int TYPE_CLOSE = 0;
	public static final int TYPE_MINIMIZE = 1;
	
	private int type;
	
	private boolean hovered = false;
	private boolean pressed = false;
	
	private TSWindow window;
	
	/**
	 * Instantiates a new window button that behaves like the given type.
	 * It aligns itself by the type. (Close is left, minimize is second-left).
	 * @param type is the type of this button; see constants.
	 * @param window is the window this button belongs to.
	 */
	public TSWindowButton(int type, TSWindow window) {
		this.type = type;
		this.window = window;
		this.setBackground(new Color(0, 0, 0, 0));
		this.addMouseListener(this);
		this.setSize(TSWindow.TITLE_BAR_HEIGHT - (TSWindow.TITLE_BAR_BUTTON_MARGIN * 2), TSWindow.TITLE_BAR_HEIGHT - (TSWindow.TITLE_BAR_BUTTON_MARGIN * 2));
		this.setLocation(TSWindow.TITLE_BAR_BUTTON_MARGIN + (this.type * (this.getWidth() + TSWindow.TITLE_BAR_BUTTON_MARGIN)), TSWindow.TITLE_BAR_BUTTON_MARGIN);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		//Draw button
		Graphics2D g2d = GraphicsUtils.prepareGraphics(g);
		
		if (this.type == TYPE_CLOSE) {
			g2d.setColor(CLOSE_COLOR);
		}
		else if (this.type == TYPE_MINIMIZE) {
			g2d.setColor(MINIMIZE_COLOR);
		}

		//Darker the color when hovered and pressed.
		if (this.hovered) {
			g2d.setColor(GraphicsUtils.darkerColor(g2d.getColor(), 25));
		}
		if (this.pressed) {
			g2d.setColor(GraphicsUtils.darkerColor(g2d.getColor(), 25));
		}
		
		g2d.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), WINDOW_BUTTON_BORDER_RADIUS, WINDOW_BUTTON_BORDER_RADIUS);
	}

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		this.pressed = true;
		this.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.pressed = false;
		this.repaint();
		
		//Do action
		if (this.type == TYPE_CLOSE) {
			if (this.window.getType() == Type.UTILITY) {
				this.window.setVisible(false);
			}
			else {
				this.window.dispatchEvent(new WindowEvent(this.window, WindowEvent.WINDOW_CLOSING));
			}
		}
		else if (this.type == TYPE_MINIMIZE) {
			this.window.setState(JFrame.ICONIFIED);
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
		this.pressed = false;
		this.repaint();
	}
	
}

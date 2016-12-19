package ch.telegraphstudios.TSMenuBar;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TSWindowTitleBar extends JPanel implements MouseListener, MouseMotionListener {

	private TSWindow parent;
	private static final Color TITLE_BAR_COLOR_TOP = GraphicsUtils.darkerColor(Design.DESIGN_COLOR, -20);
	private static final Color TITLE_BAR_COLOR = Design.DESIGN_COLOR;
	private static final Color TITLE_COLOR = new Color(0xF2F2F2);
	private static final int BORDER_RADIUS = 8;
	
	private Point dragOffset;
	
	private TSWindowButton closeButton;
	private TSWindowButton minimizeButton;
	private JLabel titleLabel;
	
	/**
	 * Instantiate a new window title bar.
	 * This generates the needed stuff like buttons and title label.
	 * It needs to have the parent instance passed.
	 * @param parent instance of the parent window.
	 */
	public TSWindowTitleBar(TSWindow parent) {
		this.parent = parent;
		
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		
		this.setBackground(new Color(0, 0, 0, 0));
		this.setSize(this.parent.getWidth(), TSWindow.TITLE_BAR_HEIGHT);
		this.setLocation(0, 0);
		this.setLayout(null);
		
		//Add window buttons
		this.closeButton = new TSWindowButton(TSWindowButton.TYPE_CLOSE, this.parent);
		this.minimizeButton = new TSWindowButton(TSWindowButton.TYPE_MINIMIZE, this.parent);

		this.add(this.closeButton);
		this.add(this.minimizeButton);
		
		//Add window title label
		this.titleLabel = new JLabel(this.parent.getTitle());
		this.titleLabel.setBounds(this.minimizeButton.getX() + this.minimizeButton.getWidth(), 0, this.getWidth() - this.minimizeButton.getX() - this.minimizeButton.getWidth(), this.getHeight());
		this.titleLabel.setForeground(TITLE_COLOR);
		this.titleLabel.setFont(new Font(Design.DEFAULT_FONT_FAMILY, Font.BOLD, 12));
		this.titleLabel.setHorizontalAlignment(JLabel.CENTER);
		
		this.add(this.titleLabel);
	}
	
	public void setTitle() {
		this.titleLabel.setText(this.parent.getTitle());
		this.repaint();
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = GraphicsUtils.prepareGraphics(g);
		
		GradientPaint gradient = new GradientPaint(0, 0, TITLE_BAR_COLOR_TOP, 0, TSWindow.TITLE_BAR_HEIGHT, TITLE_BAR_COLOR);
		g2d.setPaint(gradient);
		g2d.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), BORDER_RADIUS, BORDER_RADIUS);

		super.paint(g);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point newPosition = new Point((int)(e.getX() - this.dragOffset.getX() + this.parent.getX() + this.getX()), (int)(e.getY() - this.dragOffset.getY() + this.parent.getY() + this.getY()));
		
		if (newPosition.y < TSMenuBar.MENU_BAR_HEIGHT) {
			newPosition.y = TSMenuBar.MENU_BAR_HEIGHT;
		}
		
		this.parent.setLocation(newPosition);
	}

	@Override
	public void mouseMoved(MouseEvent e) { }

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		this.dragOffset = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }
	
}

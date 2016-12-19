package ch.telegraphstudios.TSMenuBar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;

/**
 * A JPWindow is a customized window to provide a unique and unmistakable design.
 * This automatically sets up a title bar with a title and two window buttons (close and minimize).
 * Resizing is not supported as it is not needed by this application.
 */
public class TSWindow extends JFrame implements MouseListener, MouseMotionListener, WindowFocusListener {

	public static final int DEFAULT_WIDTH = 960;
	public static final int DEFAULT_HEIGHT = 520;
	public static final int TITLE_BAR_HEIGHT = 24;
	public static final int TITLE_BAR_BOTTOM_MARGIN = 8;
	public static final int TITLE_BAR_BUTTON_MARGIN = 4;
	public static final int BORDER_SIZE = 0;
	public static final Color WINDOW_BACKGROUND = Design.DESIGN_COLOR;
	
	private TSWindowContent contentView;
	private boolean addedContentView = false;
	private boolean setLayout = false;
	
	private TSWindowTitleBar titleBar;
	
	private TSWindow parentWindow;
	
	private ArrayList<TSWindow> children = new ArrayList<TSWindow>();
	
	/**
	 * Instantiates a new window with the given size.
	 * @param width
	 * @param height
	 */
	public TSWindow(int width, int height) {
		this.setSize(width + (BORDER_SIZE * 2), height + TITLE_BAR_HEIGHT + TITLE_BAR_BOTTOM_MARGIN + BORDER_SIZE);
		this.setLocation(Utils.getScreenSize().width / 2 - (this.getWidth() / 2), Utils.getScreenSize().height / 2 - (this.getHeight() / 2));
		
		this.setUndecorated(true);
		this.setLayout(null);
		this.setBackground(new Color(0, 0, 0, 0));
		this.addWindowFocusListener(this);
		
		//Add title bar
		this.titleBar = new TSWindowTitleBar(this);
		this.addDirect(this.titleBar);
		
		this.contentView = new TSWindowContent();
		this.contentView.setSize(width, height);
		this.contentView.setLocation(BORDER_SIZE, TITLE_BAR_HEIGHT + TITLE_BAR_BOTTOM_MARGIN);
		this.contentView.setBackground(WINDOW_BACKGROUND);
		this.contentView.setLayout(null);
		this.addDirect(this.contentView);
	}
	
	/**
	 * Instantiates a new window with the default size.
	 */
	public TSWindow() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		super.paint(g);
	}
	
	public void addPaintListener(TSWindowPaintListener listener) {
		this.contentView.addPaintListener(listener);
	}
	
	private void addDirect(Component component) {
		this.addedContentView = false;
		this.add(component);
	}
	
	public void addChild(TSWindow child) {
		this.children.add(child);
	}
	
	public TSWindow getParentWindow() {
		return this.parentWindow;
	}
	
	/**
	 * Calls the setTitle method of the superclass and updates the string on the titleBar property.
	 */
	@Override
	public void setTitle(String title) {
		super.setTitle(title);
		this.titleBar.setTitle();
	}
	
	@Override
	public Component add(Component component) {
		if (!this.addedContentView) {
			this.addedContentView = true;
			return super.add(component);
		}
		else {
			return this.contentView.add(component);
		}
	}
	
	@Override
	public void add(Component component, Object constraints) {
		if (!this.addedContentView) {
			if (!(component instanceof JRootPane)) {
				this.addedContentView = true;
			}
			super.add(component, constraints);
		}
		else {
			this.contentView.add(component, constraints);
		}
	}
	
	@Override
	public void setLayout(LayoutManager manager) {
		if (this.contentView != null) {
			if (!setLayout) {
				setLayout = true;
				this.contentView.setLayout(manager);
			}
			else {
				super.setLayout(manager);
			}
		}
		else {
			super.setLayout(manager);
		}
	}
	
	@Override
	public Dimension getSize() {
		return new Dimension(super.getWidth() - (BORDER_SIZE * 2), super.getHeight() - TITLE_BAR_HEIGHT - BORDER_SIZE);
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		if (this.titleBar != null) {
			this.titleBar.setSize(width, this.titleBar.getHeight());
		}
	}
	
	@Override
	public void setSize(Dimension size) {
		this.setSize(size.width, size.height);
	}

	@Override
	public void mouseDragged(MouseEvent e) { }

	@Override
	public void mouseMoved(MouseEvent e) { }

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void windowGainedFocus(WindowEvent e) {
		if (!this.children.contains(e.getOppositeWindow()) && !TSMenuBar.getInstance().equals(e.getOppositeWindow()) && this.getType() != Type.UTILITY) {
			for (TSWindow child : this.children) {
				if (child.isVisible()) {
					child.setVisible(true);
				}
			}
			
			TSMenuBar.getInstance().setVisible(true);
		}
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		TSMenuBar.getInstance().setLastFocusedWindow(null);
	}
	
}

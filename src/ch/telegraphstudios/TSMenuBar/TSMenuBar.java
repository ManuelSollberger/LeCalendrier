package ch.telegraphstudios.TSMenuBar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

import javax.swing.FocusManager;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This is a menu bar that contains multiple customizable sub menus.
 * TSMenuBar is a singleton class to make sure that every application has its own menu bar.
 * 
 * This class has the 'TS' (Telegraph Studios) prefix to declare that it can appear in different applications.
 */
public class TSMenuBar extends JFrame implements MouseListener, WindowFocusListener {
	
	public static final int MENU_BAR_HEIGHT = 24;

	private static TSMenuBar INSTANCE;
	
	private TSMenu applicationMenu;
	private TSMenu windowMenu;
	
	private ArrayList<TSMenuBarItem> items = new ArrayList<TSMenuBarItem>();
	private String applicationName;
	
	private TSMenuBarItem selectedItem;
	private Window lastFocusedWindow;
	
	/**
	 * Instantiates a new menu bar. This should never be done externally because this is a singleton calss.
	 * @param applicationName is the name of the application.
	 */
	private TSMenuBar(String applicationName) {
		this.applicationName = applicationName;
		
		this.setUndecorated(true);
		this.setBounds(0, 0, Utils.getScreenSize().width, MENU_BAR_HEIGHT);
		this.setType(Type.UTILITY);
		this.setLayout(null);
		this.setVisible(true);
		this.setBackground(new Color(0, 0, 0, 1));
		this.setAlwaysOnTop(true);
		this.addMouseListener(this);
		this.addWindowFocusListener(this);
		this.addDefaultItems();
		
		this.repaint();
	}
	
	private void addDefaultItems() {
		//Add application menu item first.
		TSMenuBarApplicationItem applicationItem = new TSMenuBarApplicationItem(this.applicationName);
		applicationMenu = new TSMenu();
		
		//Add quit item.
		TSMenuItem quitItem = new TSMenuItem("Quit");
		quitItem.addMenuItemClickListener(new TSMenuItemClickListener() {
			@Override
			public void onClick() {
				System.exit(0);
			}
		});
		applicationMenu.addItem(quitItem);
		
		applicationItem.setMenu(applicationMenu);
		this.addMenuItem(applicationItem);

		//Add window item later.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				//Add window menu
				TSMenuBarItem windowItem = new TSMenuBarItem("Window");
				windowMenu = new TSMenu();
				
				//Add center window item
				TSMenuItem centerWindowItem = new TSMenuItem("Center window");
				centerWindowItem.addMenuItemClickListener(new TSMenuItemClickListener() {
					@Override
					public void onClick() {
						Dimension screenSize = Utils.getScreenSize();
						if (lastFocusedWindow != null) {
							lastFocusedWindow.setLocation(screenSize.width / 2 - (lastFocusedWindow.getWidth() / 2), screenSize.height / 2 - (lastFocusedWindow.getHeight() / 2));
						}
					}
				});
				windowMenu.addItem(centerWindowItem);
				
				windowItem.setMenu(windowMenu);
				addMenuItem(windowItem);
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = GraphicsUtils.prepareGraphics(g);
		
		int x = 0;
		for (TSMenuBarItem previousItem : items) {
			x += previousItem.getWidth();
		}
		
		g2d.setPaint(new GradientPaint(0, 0, GraphicsUtils.darkerColor(Design.DESIGN_COLOR, -10), 0, MENU_BAR_HEIGHT, Design.DESIGN_COLOR));
		g2d.fillRect(x, 0, this.getWidth() - x, MENU_BAR_HEIGHT);
	}
	
	public void addMenuItem(TSMenuBarItem item) {
		int x = 0;
		for (TSMenuBarItem previousItem : items) {
			x += previousItem.getWidth();
		}
		
		this.items.add(item);
		
		item.setLocation(x, 0);
		
		this.add(item);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				repaint();
			}
		});
	}
	
	public TSMenuBarItem getMenuBarItem(String name) {
		for (TSMenuBarItem item : items) {
			if (item.getName().equals(name)) {
				return item;
			}
		}
		
		return null;
	}
	
	/**
	 * @return the selected menu bar item (null if nothing is selected)
	 */
	public TSMenuBarItem getSelectedItem() {
		return this.selectedItem;
	}
	
	public void setSelectedItem(TSMenuBarItem item) {
		//Close current item
		if (this.selectedItem != null && this.selectedItem.getMenu() != null) {
			this.selectedItem.getMenu().close();
		}
		
		TSMenuBarItem oldItem = this.selectedItem;
		this.selectedItem = item;
		if (oldItem != null) {
			oldItem.updateState();
		}
		
		if (this.selectedItem != null) {
			//this.setSize(Utils.getScreenSize());
		}
		else {
			this.setSize(this.getWidth(), MENU_BAR_HEIGHT);
		}
	}
	
	public void setLastFocusedWindow(Window window) {
		this.lastFocusedWindow = window;
	}
	
	public Window getLastFocusedWindow() {
		return this.lastFocusedWindow;
	}
	
	/**
	 * @return true if one of the menu items is selected.
	 */
	public boolean isMenuOpened() {
		return (this.selectedItem != null);
	}

	private void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	public static TSMenuBar getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TSMenuBar("Application");
			//Throw an exception if the menu bar is not initialized.
			try {
				throw new Exception("TSMenuBar not initialized for this application! Call TSMenuBar.initialize() before using it!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return INSTANCE;
	}
	
	/**
	 * @return true if the menu bar is initialized; false if not.
	 */
	public static boolean isInitialized() {
		return (INSTANCE != null);
	}
	
	/**
	 * You must call this method before expecting any menu bar to appear!
	 */
	public static void initialize(String applicationName) {
		if (INSTANCE == null) {
			INSTANCE = new TSMenuBar(applicationName);
		}
		else {
			INSTANCE.setApplicationName(applicationName);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		this.setSelectedItem(null);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.mousePressed(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void windowGainedFocus(WindowEvent e) {
		this.lastFocusedWindow = e.getOppositeWindow();
	}

	@Override
	public void windowLostFocus(WindowEvent e) { }
	
}

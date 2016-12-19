package ch.telegraphstudios.LeCal;

import javax.swing.SwingUtilities;

import ch.telegraphstudios.LeCal.GUI.LCMainWindow;
import ch.telegraphstudios.TSMenuBar.TSMenuBar;
import ch.telegraphstudios.TSMenuBar.TSMenuItem;
import ch.telegraphstudios.TSMenuBar.TSMenuItemClickListener;

public class LeCalMain {
	
	/**
	 * This is the name of this application. Do not type it manually anywhere.
	 */
	public static final String APPLICATION_NAME = "LeCalendrier";
	
	private static LCMainWindow mainWindow;

	public static void main(String[] args) {
		
		//Initialize menu bar
		TSMenuBar.initialize(APPLICATION_NAME);
		
		//Create main window
		mainWindow = new LCMainWindow();
		mainWindow.setVisible(true);
		
		//Add open welcome window menu item
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				TSMenuItem openWelcomeWindowItem = new TSMenuItem("Open welcome screen");
				openWelcomeWindowItem.addMenuItemClickListener(new TSMenuItemClickListener() {
					@Override
					public void onClick() {
						mainWindow.setVisible(true);
					}
				});
				TSMenuBar.getInstance().getMenuBarItem("Window").getMenu().addItem(openWelcomeWindowItem);
			}
		});
		
	}
	
}

package ch.telegraphstudios.LeCal;

import javax.swing.SwingUtilities;

import ch.telegraphstudios.LeCal.Calendar.Calendar;
import ch.telegraphstudios.LeCal.Calendar.Templates;
import ch.telegraphstudios.LeCal.GUI.LCEditorWindow;
import ch.telegraphstudios.LeCal.GUI.LCMainWindow;
import ch.telegraphstudios.TSMenuBar.TSMenu;
import ch.telegraphstudios.TSMenuBar.TSMenuBar;
import ch.telegraphstudios.TSMenuBar.TSMenuBarItem;
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
		
		//Add menu bar items
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				//Open welcome screen item
				TSMenuItem openWelcomeWindowItem = new TSMenuItem("Open welcome screen");
				openWelcomeWindowItem.addMenuItemClickListener(new TSMenuItemClickListener() {
					@Override
					public void onClick() {
						mainWindow.setVisible(true);
					}
				});
				TSMenuBar.getInstance().getMenuBarItem("Window").getMenu().addItem(openWelcomeWindowItem);
				
				//File menu bar item
				TSMenuBarItem fileItem = new TSMenuBarItem("File");
				TSMenu fileMenu = new TSMenu();
				fileItem.setMenu(fileMenu);
				TSMenuBar.getInstance().addMenuItem(fileItem);
				
				//New document item
				TSMenuItem newDocumentItem = new TSMenuItem("New document...");
				newDocumentItem.addMenuItemClickListener(new TSMenuItemClickListener() {
					@Override
					public void onClick() {
						//Open editor window with a new document.
						LCEditorWindow editor = new LCEditorWindow(new Calendar());
						editor.setVisible(true);
					}
				});
				fileMenu.addItem(newDocumentItem);
				
			}
		});
		
		//Initialize templates
		Templates.initializeTemplates();
		
	}
	
}

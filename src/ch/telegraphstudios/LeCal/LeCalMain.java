package ch.telegraphstudios.LeCal;

import java.awt.Window;

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

		//Add custom menu bar items.
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
		
		//New PDF export item
		TSMenuItem pdfItem = new TSMenuItem("Export as PDF...");
		pdfItem.addMenuItemClickListener(new TSMenuItemClickListener() {
			@Override
			public void onClick() {
				//Save the currently focused document (if an editor window is in focus).
				Window focused = TSMenuBar.getInstance().getLastFocusedWindow();
				
				if (focused instanceof LCEditorWindow) {
					LCEditorWindow editor = (LCEditorWindow)focused;
					
					editor.getDocument().saveToPDF();
				}
			}
		});
		fileMenu.addItem(pdfItem);
		
		//-------------------------
		
		//Add default menu bar items.
		//This adds the items to those menu bar menus that will be initialized later.
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
				TSMenuBar instance = TSMenuBar.getInstance();
				TSMenuBarItem item = instance.getMenuBarItem("Window");
				item.getMenu().addItem(openWelcomeWindowItem);
				
			}
		});
		
		//Create main window
		mainWindow = new LCMainWindow();
		mainWindow.setVisible(true);
		
		//Initialize templates
		Templates.initializeTemplates();
		
	}
	
}

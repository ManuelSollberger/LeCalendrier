package ch.telegraphstudios.LeCal.Calendar;

import java.awt.Window;
import java.util.ArrayList;
import java.util.Random;

import ch.telegraphstudios.LeCal.LeCalMain;
import ch.telegraphstudios.LeCal.GUI.LCEditorWindow;
import ch.telegraphstudios.TSMenuBar.TSMenu;
import ch.telegraphstudios.TSMenuBar.TSMenuBar;
import ch.telegraphstudios.TSMenuBar.TSMenuBarItem;
import ch.telegraphstudios.TSMenuBar.TSMenuItem;
import ch.telegraphstudios.TSMenuBar.TSMenuItemClickListener;

/**
 * This class contains a list of default templates.
 */
public class Templates {
	
	private static ArrayList<PageTemplate> templates = new ArrayList<PageTemplate>();

	/**
	 * A template with just one large image that covers the entire page.
	 */
	public static PageTemplate TEMPLATE_SINGLE_IMAGE;

	/**
	 * A template with two smaller images.
	 */
	public static PageTemplate TEMPLATE_DOUBLE_IMAGE;

	/**
	 * A template with one big and two smaller images.
	 */
	public static PageTemplate TEMPLATE_TRIPLE_IMAGE;
	
	/**
	 * Call this very early.
	 */
	public static void initializeTemplates() {
		//Template menu bar item
		TSMenuBarItem templateItem = new TSMenuBarItem("Templates");
		
		TSMenu templateMenu = new TSMenu();
		templateItem.setMenu(templateMenu);
		
		TSMenuItem singleItem = new TSMenuItem("Single image");
		singleItem.setCondition(new LeCalMain());
		singleItem.addMenuItemClickListener(new TSMenuItemClickListener() {
			@Override
			public void onClick() {
				Window focused = TSMenuBar.getInstance().getLastFocusedWindow();
				if (focused instanceof LCEditorWindow) {
					LCEditorWindow editor = (LCEditorWindow)focused;
					editor.getDocument().getCurrentPage().setTemplate(getByName("SINGLE"));
					editor.repaint();
				}
			}
		});
		templateMenu.addItem(singleItem);
		
		TSMenuItem doubleItem = new TSMenuItem("Double image");
		doubleItem.setCondition(new LeCalMain());
		doubleItem.addMenuItemClickListener(new TSMenuItemClickListener() {
			@Override
			public void onClick() {
				Window focused = TSMenuBar.getInstance().getLastFocusedWindow();
				if (focused instanceof LCEditorWindow) {
					LCEditorWindow editor = (LCEditorWindow)focused;
					editor.getDocument().getCurrentPage().setTemplate(getByName("DOUBLE"));
					editor.repaint();
				}
			}
		});
		templateMenu.addItem(doubleItem);
		
		TSMenuItem tripleItem = new TSMenuItem("Triple image");
		tripleItem.setCondition(new LeCalMain());
		tripleItem.addMenuItemClickListener(new TSMenuItemClickListener() {
			@Override
			public void onClick() {
				Window focused = TSMenuBar.getInstance().getLastFocusedWindow();
				if (focused instanceof LCEditorWindow) {
					LCEditorWindow editor = (LCEditorWindow)focused;
					editor.getDocument().getCurrentPage().setTemplate(getByName("TRIPLE"));
					editor.repaint();
				}
			}
		});
		templateMenu.addItem(tripleItem);
		
		TSMenuBar.getInstance().addMenuItem(templateItem);
		
		//Single image
		TEMPLATE_SINGLE_IMAGE = new PageTemplate("SINGLE");
		DropZone mainDropZone = new DropZone();
		mainDropZone.setBounds(0, 0, Calendar.WIDTH, Calendar.HEIGHT);
		TEMPLATE_SINGLE_IMAGE.addDropZone(mainDropZone);
		templates.add(TEMPLATE_SINGLE_IMAGE);
		
		
		//Double image
		TEMPLATE_DOUBLE_IMAGE = new PageTemplate("DOUBLE");
		DropZone doubleLeftZone = new DropZone();
		doubleLeftZone.setBounds(0, 0, Calendar.WIDTH / 2, Calendar.HEIGHT);
		TEMPLATE_DOUBLE_IMAGE.addDropZone(doubleLeftZone);
		DropZone doubleRightZone = new DropZone();
		doubleRightZone.setBounds(Calendar.WIDTH / 2, 0, Calendar.WIDTH / 2 + 1, Calendar.HEIGHT);
		TEMPLATE_DOUBLE_IMAGE.addDropZone(doubleRightZone);
		templates.add(TEMPLATE_DOUBLE_IMAGE);
		
		
		//Triple image
		TEMPLATE_TRIPLE_IMAGE = new PageTemplate("TRIPLE");
		DropZone tripleLeftZone = new DropZone();
		tripleLeftZone.setBounds(0, 0, Calendar.WIDTH / 2, Calendar.HEIGHT);
		TEMPLATE_TRIPLE_IMAGE.addDropZone(tripleLeftZone);
		DropZone tripleRightZone = new DropZone();
		tripleRightZone.setBounds(Calendar.WIDTH / 2, 0, Calendar.WIDTH / 2 + 1, Calendar.HEIGHT);
		TEMPLATE_TRIPLE_IMAGE.addDropZone(tripleRightZone);
		DropZone tripleCenterZone = new DropZone();
		tripleCenterZone.setBounds(Calendar.WIDTH / 2 - (Calendar.WIDTH / 4), Calendar.HEIGHT / 2 - (Calendar.HEIGHT / 4), Calendar.WIDTH / 2, Calendar.HEIGHT / 2);
		TEMPLATE_TRIPLE_IMAGE.addDropZone(tripleCenterZone);
		templates.add(TEMPLATE_TRIPLE_IMAGE);
	}
	
	public static PageTemplate getRandomTemplate() {
		int random = new Random().nextInt(templates.size());
		return templates.get(random);
	}
	
	public static PageTemplate getByName(String name) {
		for (PageTemplate template : templates) {
			if (template.getName().equals(name)) {
				return template;
			}
		}
		
		return TEMPLATE_SINGLE_IMAGE;
	}
	
}

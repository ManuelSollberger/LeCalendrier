package ch.telegraphstudios.LeCal.Calendar;

import java.util.ArrayList;
import java.util.Random;

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
		//Single image
		TEMPLATE_SINGLE_IMAGE = new PageTemplate();
		DropZone mainDropZone = new DropZone();
		mainDropZone.setBounds(0, 0, Calendar.WIDTH, Calendar.HEIGHT);
		TEMPLATE_SINGLE_IMAGE.addDropZone(mainDropZone);
		templates.add(TEMPLATE_SINGLE_IMAGE);
		
		
		//Double image
		TEMPLATE_DOUBLE_IMAGE = new PageTemplate();
		DropZone doubleLeftZone = new DropZone();
		doubleLeftZone.setBounds(0, 0, Calendar.WIDTH / 2, Calendar.HEIGHT);
		TEMPLATE_DOUBLE_IMAGE.addDropZone(doubleLeftZone);
		DropZone doubleRightZone = new DropZone();
		doubleRightZone.setBounds(Calendar.WIDTH / 2, 0, Calendar.WIDTH / 2 + 1, Calendar.HEIGHT);
		TEMPLATE_DOUBLE_IMAGE.addDropZone(doubleRightZone);
		templates.add(TEMPLATE_DOUBLE_IMAGE);
		
		
		//Triple image
		TEMPLATE_TRIPLE_IMAGE = new PageTemplate();
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
	
}

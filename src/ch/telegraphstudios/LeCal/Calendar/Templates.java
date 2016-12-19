package ch.telegraphstudios.LeCal.Calendar;

/**
 * This class contains a list of default templates.
 */
public class Templates {

	/**
	 * A template with just one large image that covers the entire page.
	 */
	public static PageTemplate TEMPLATE_SINGLE_IMAGE;
	
	/**
	 * Call this very early.
	 */
	public static void initializeTemplates() {
		TEMPLATE_SINGLE_IMAGE = new PageTemplate();
		DropZone mainDropZone = new DropZone();
		mainDropZone.setBounds(0, 0, Calendar.WIDTH, Calendar.HEIGHT);
		TEMPLATE_SINGLE_IMAGE.addDropZone(mainDropZone);
	}
	
}

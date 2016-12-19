package ch.telegraphstudios.LeCal.Calendar;

import java.awt.Graphics2D;

/**
 * This is the bean that represents a calendar project.
 */
public class Calendar {
	
	/**
	 * The ratio of a calendar page is the square root of 2 (1.4) to fit a standard A-type page.
	 */
	public static final float CALENDAR_RATIO = (float)Math.sqrt(2);
	
	/**
	 * The height of the calendar view.
	 */
	public static final int HEIGHT = 520;
	
	/**
	 * The width of the calendar view. (sqrt(2) * width)
	 */
	public static final int WIDTH = (int)(HEIGHT * CALENDAR_RATIO);
	
	private String name;
	private CalendarPage[] pages = new CalendarPage[12];
	private int currentPage;
	
	public Calendar() {
		this.name = "Unnamed";
		
		//Init pages
		for (int i = 0; i < pages.length; i++) {
			pages[i] = new CalendarPage();
		}
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void selectPage(int page) {
		this.currentPage = page;
	}
	
	public int getCurrentPage() {
		return this.currentPage;
	}
	
	public void paint(Graphics2D g) {
		CalendarPage page = pages[this.currentPage];
		
		if (page != null) {
			page.paint(g);
		}
	}
	
}

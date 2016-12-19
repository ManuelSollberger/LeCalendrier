package ch.telegraphstudios.LeCal.Calendar;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import ch.telegraphstudios.TSMenuBar.GraphicsUtils;

public class CalendarPage {

	private PageTemplate template;
	private ArrayList<BufferedImage> dropZoneImages = new ArrayList<BufferedImage>();
	
	public CalendarPage() {
		this.template = Templates.TEMPLATE_SINGLE_IMAGE;
	}
	
	public PageTemplate getTemplate() {
		return this.template;
	}
	
	/**
	 * Paints this calendar page and its template.
	 * @param g
	 */
	public void paint(Graphics g) {
		this.template.paint(GraphicsUtils.prepareGraphics(g), this.dropZoneImages);
	}
	
}

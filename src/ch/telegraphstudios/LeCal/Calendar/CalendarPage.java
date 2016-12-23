package ch.telegraphstudios.LeCal.Calendar;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import ch.telegraphstudios.TSMenuBar.GraphicsUtils;

public class CalendarPage {

	private PageTemplate template;
	private ArrayList<BufferedImage> dropZoneImages = new ArrayList<BufferedImage>();
	private ArrayList<String> dropZoneImagePaths = new ArrayList<String>();
	
	public CalendarPage() {
		this.template = Templates.getRandomTemplate();
	}
	
	public void setTemplate(PageTemplate template) {
		this.template = template;
	}
	
	public PageTemplate getTemplate() {
		return this.template;
	}
	
	public void setDropZoneImage(int zoneIndex, BufferedImage image, String path) {
		while (zoneIndex >= this.dropZoneImages.size()) {
			this.dropZoneImages.add(null);
			this.dropZoneImagePaths.add(null);
		}
		
		this.dropZoneImages.set(zoneIndex, image);
		this.dropZoneImagePaths.set(zoneIndex, path);
	}
	
	public ArrayList<BufferedImage> getDropZoneImages() {
		return this.dropZoneImages;
	}
	
	/**
	 * Paints this calendar page and its template.
	 * @param g
	 */
	public void paint(Graphics g) {
		this.template.paint(GraphicsUtils.prepareGraphics(g), this.dropZoneImages);
	}
	
	public String toString() {
		String string = "";
		
		for (String path : dropZoneImagePaths) {
			if (!string.equals("")) {
				string += ",";
			}
			
			string += path;
		}
		
		return string;
	}
	
}

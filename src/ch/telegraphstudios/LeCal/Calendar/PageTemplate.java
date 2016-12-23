package ch.telegraphstudios.LeCal.Calendar;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A page template contains one or more drop zones.
 */
public class PageTemplate {

	private ArrayList<DropZone> dropZones = new ArrayList<DropZone>();
	private String name;
	
	public PageTemplate(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void addDropZone(DropZone dropZone) {
		this.dropZones.add(dropZone);
	}
	
	public ArrayList<DropZone> getDropZones() {
		return this.dropZones;
	}
	
	/**
	 * Paints this template and all its drop zones.
	 * @param g
	 * @param images
	 */
	public void paint(Graphics2D g, ArrayList<BufferedImage> images) {
		int index = 0;
		for (DropZone dropZone : dropZones) {
			BufferedImage image = null;
			if (index < images.size()) {
				image = images.get(index);
			}
			dropZone.paint(g, image);
			index++;
		}
	}
	
}

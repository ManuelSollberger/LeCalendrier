package ch.telegraphstudios.LeCal.Calendar;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * A drop zone is an area on a template where the user can drop an image.
 */
public class DropZone {
	
	private Rectangle bounds;

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public void setBounds(int x, int y, int width, int height) {
		this.setBounds(new Rectangle(x, y, width, height));
	}
	
	/**
	 * Renders this drop zone with the given image.
	 * @param g
	 * @param image
	 */
	public void paint(Graphics2D g, BufferedImage image) {
		if (image != null) {
			g.drawImage(image, 0, 0, this.bounds.width, this.bounds.height, null);
		}
		else {
			g.setColor(new Color(0x23DE23));
			g.fillRect(0, 0, this.bounds.width, this.bounds.height);
		}
	}
	
}

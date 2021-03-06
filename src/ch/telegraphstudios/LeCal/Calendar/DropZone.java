package ch.telegraphstudios.LeCal.Calendar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * A drop zone is an area on a template where the user can drop an image.
 */
public class DropZone {

	private static final int STROKE_INSET = 16;
	private static final int STROKE_THICKNESS = 8;
	private static final int STROKE_RADIUS = 32;
	private static final int STROKE_DASH = 40;
	private static final Color STROKE_COLOR = new Color(0xCCCCCC); 
	
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
	 * 
	 * @param g
	 * @param image
	 */
	public void paint(Graphics2D g, BufferedImage image) {
		float modifier = 1;
		if (image != null) {
			g.drawImage(image, (int)(this.bounds.x * modifier), (int)(this.bounds.y * modifier), (int)(this.bounds.width * modifier), (int)(this.bounds.height * modifier), null);
		} else {
			g.setColor(Color.WHITE);
			g.fillRect((int)(this.bounds.x * modifier), (int)(this.bounds.y * modifier), (int)(this.bounds.width * modifier), (int)(this.bounds.height * modifier));

			// Draw lined rectangle
			float dashes[] = {STROKE_DASH};
			BasicStroke dashed = new BasicStroke(STROKE_THICKNESS, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashes, 0.0f);
			g.setStroke(dashed);
			g.setColor(STROKE_COLOR);
			g.draw(new RoundRectangle2D.Double((STROKE_INSET + this.bounds.x) * modifier, (STROKE_INSET + this.bounds.y) * modifier, (this.bounds.width - (STROKE_INSET * 2)) * modifier, (this.bounds.height - (STROKE_INSET * 2)) * modifier, STROKE_RADIUS, STROKE_RADIUS));
		}
	}

}

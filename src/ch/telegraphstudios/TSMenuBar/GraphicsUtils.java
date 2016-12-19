package ch.telegraphstudios.TSMenuBar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class GraphicsUtils {

	private static float lineRepeat = 1f;
	private static float lineThickness = 4;

	public static float getLineRepeat() {
		return lineRepeat;
	}

	public static void setLineRepeat(float newLineRepeat) {
		lineRepeat = newLineRepeat;
	}

	public static float getLineThickness() {
		return lineThickness;
	}

	public static void setLineThickness(float thickness) {
		lineThickness = thickness;
	}

	public static Graphics2D prepareGraphics(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setBackground(new Color(0, 0, 0, 0));
		return g2d;
	}

	public static Graphics2D prepareGraphics(Graphics g) {
		return prepareGraphics((Graphics2D) g);
	}

	/**
	 * Converts the given hexadecimal color string to a true AWT color object.
	 * Returns null if the string is invalid.
	 * @param colorString
	 */
	public static Color hexadecimalToColor(String colorString) {
		if (colorString.startsWith("#") && colorString.length() == 7) {
			for (int i = 0; i < colorString.length(); i++) {
				String thisChar = colorString.substring(i, i + 1);
				if (!"1234567890ABCDEFabcdef#".contains(thisChar)) {
					return null;
				}
			}
		}
		else {
			return null;
		}
		
		return new Color(Integer.valueOf(colorString.substring(1, 3), 16), Integer.valueOf(colorString.substring(3, 5), 16), Integer.valueOf(colorString.substring(5, 7), 16));
	}
	
	public static String colorToHexadecimal(Color color) {
		return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
	}

	/**
	 * Creates a darker version of the given color.
	 * 
	 * @param input
	 *            is the initial color to make darker.
	 * @param amount
	 *            is the amount of "darkness" to be applied (from 0 to 255).
	 * @return the darker version of the input color.
	 */
	public static Color darkerColor(Color input, int amount) {
		int red = input.getRed() - amount;
		int green = input.getGreen() - amount;
		int blue = input.getBlue() - amount;

		if (red < 0) {
			red = 0;
		}
		if (red > 255) {
			red = 255;
		}

		if (green < 0) {
			green = 0;
		}
		if (green > 255) {
			green = 255;
		}

		if (blue < 0) {
			blue = 0;
		}
		if (blue > 255) {
			blue = 255;
		}

		return new Color(red, green, blue, input.getAlpha());
	}

	/**
	 * Calculates the size of the given string.
	 * 
	 * @param string
	 * @param g
	 * @return
	 */
	public static Dimension getStringSize(String string, Graphics g) {
		Rectangle2D rect = g.getFontMetrics().getStringBounds(string, g);

		return new Dimension((int) rect.getWidth(), (int) rect.getHeight());
	}

	/**
	 * Calculates the size of the given string by creating a temporary image
	 * that is needed by the font metrics.
	 * 
	 * @param string
	 * @param font
	 * @return
	 */
	public static Dimension getStringSize(String string, Font font) {
		BufferedImage measurementImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = (Graphics2D) measurementImage.getGraphics();
		graphics.setFont(font);
		return getStringSize(string, graphics);
	}

	/**
	 * Draws a line between the given start and end point.
	 * 
	 * @param g2d
	 * @param start
	 * @param end
	 */
	public static void drawLine(Graphics2D g2d, Point start, Point end) {
		new DrawThread(new LineDrawer(g2d, start, end, false));
	}

	/**
	 * Erases a line between the given start and end point.
	 * 
	 * @param g2d
	 * @param start
	 * @param end
	 */
	public static void eraseLine(Graphics2D g2d, Point start, Point end) {
		new DrawThread(new LineDrawer(g2d, start, end, true));
	}

	/**
	 * Inverts all the RGB values of the given color object.
	 * @param color
	 */
	public static Color invertColor(Color color) {
		return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
	}

}

/**
 * The draw thread starts a given runnable action which should draw something.
 */
class DrawThread extends Thread {

	private Runnable task;

	/**
	 * Instantiates and starts a new draw thread with the given draw task.
	 * 
	 * @param task
	 */
	public DrawThread(Runnable task) {
		this.task = task;
		this.start();
	}

	@Override
	public void run() {
		this.task.run();
	}

}

class Drawer {
	protected Graphics2D g2d;

	public Drawer(Graphics2D g2d) {
		this.g2d = g2d;
	}
}

class LineDrawer extends Drawer implements Runnable {

	private Point start;
	private Point end;

	private boolean erasing;

	public LineDrawer(Graphics2D g2d, Point start, Point end, boolean erasing) {
		super(g2d);

		this.start = start;
		this.end = end;
		this.erasing = erasing;
	}

	@Override
	public void run() {
		double length = Point.distance(start.getX(), start.getY(), end.getX(), end.getY());
		double differenceX = end.getX() - start.getX();
		double differenceY = end.getY() - start.getY();
		double step = GraphicsUtils.getLineRepeat();

		for (double position = 0; position <= length; position += step) {
			double t = position / length;

			Point thisPosition = new Point((int) (start.getX() + t * differenceX),
					(int) (start.getY() + t * differenceY));

			float thickness = GraphicsUtils.getLineThickness();
			if (!this.erasing) {
				this.g2d.fillOval(thisPosition.x - (int) (thickness / 2), thisPosition.y - (int) (thickness / 2),
						(int) thickness, (int) thickness);
			} else {
				this.g2d.clearRect(thisPosition.x - (int) (thickness / 2), thisPosition.y - (int) (thickness / 2),
						(int) thickness, (int) thickness);
			}
		}
	}

}
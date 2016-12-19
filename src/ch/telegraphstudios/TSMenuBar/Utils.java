package ch.telegraphstudios.TSMenuBar;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;

/**
 * This class provides some useful methods.
 */
public class Utils {

	/**
	 * @return the size of the default screen.
	 */
	public static Dimension getScreenSize() {
		DisplayMode mode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		return new Dimension(mode.getWidth(), mode.getHeight());
	}
	
}

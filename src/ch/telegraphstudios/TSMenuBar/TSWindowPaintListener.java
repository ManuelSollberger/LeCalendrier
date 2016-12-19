package ch.telegraphstudios.TSMenuBar;

import java.awt.Graphics2D;

/**
 * This listener fires an event if the window this belongs to is painted.
 */
public interface TSWindowPaintListener {

	public void paintWindow(Graphics2D g);
	
}

package ch.telegraphstudios.TSMenuBar;

import java.awt.Color;
import java.awt.Font;

/**
 * This class contains design colors and other things related to optical stuff.
 */
public class Design {

	/**
	 * The default font family to use for every standard label in this application.
	 */
	public static final String DEFAULT_FONT_FAMILY = "Arial";
	
	/**
	 * The default font to use for every standard label in this application.
	 */
	public static final Font DEFAULT_FONT = new Font(DEFAULT_FONT_FAMILY, Font.PLAIN, 12);
	
	/**
	 * This is the color to use for buttons, windows and other GUI elements.
	 */
	public static final Color DESIGN_COLOR = new Color(0x893434);
	
	/**
	 * This is the color to use for labels, titles and other foreground elements.
	 */
	public static final Color FOREGROUND_COLOR = new Color(0xFFFFFF);
	
	/**
	 * Use this color for highlighted GUI elements.
	 */
	public static final Color SELECTION_COLOR = new Color(0x5656DE);
	
	/**
	 * The distance to the edge of a component which sub-components should have.
	 */
	public static final int BORDER_DISTANCE = 8;
	
	/**
	 * The distance to the edge of a component which sub-components should have.
	 */
	public static final int GUI_HEIGHT = 24;
	
}

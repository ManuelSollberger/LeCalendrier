package ch.telegraphstudios.TSMenuBar;

import javax.swing.JLabel;

/**
 * This is a label that uses the design font by default.
 */
public class TSLabel extends JLabel {

	public TSLabel() {
		this("");
	}
	
	public TSLabel(String text) {
		super(text);
		this.setForeground(Design.FOREGROUND_COLOR);
		this.setFont(Design.DEFAULT_FONT);
	}
	
}

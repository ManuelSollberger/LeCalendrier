package ch.telegraphstudios.LeCal.GUI;

import java.awt.Font;

import ch.telegraphstudios.LeCal.LeCalMain;
import ch.telegraphstudios.TSMenuBar.Design;
import ch.telegraphstudios.TSMenuBar.TSLabel;
import ch.telegraphstudios.TSMenuBar.TSWindow;

public class LCMainWindow extends TSWindow {
	
	private TSLabel welcomeLabel;

	public LCMainWindow() {
		super();
		
		this.setTitle("Welcome to " + LeCalMain.APPLICATION_NAME);
		
		//Welcome label
		this.welcomeLabel = new TSLabel("Welcome to " + LeCalMain.APPLICATION_NAME);
		this.welcomeLabel.setFont(new Font(Design.DEFAULT_FONT_FAMILY, Font.PLAIN, 50));
		this.welcomeLabel.setBounds(Design.BORDER_DISTANCE, Design.BORDER_DISTANCE, this.getWidth() - (Design.BORDER_DISTANCE * 2), 50);
		this.add(this.welcomeLabel);
	}
	
}

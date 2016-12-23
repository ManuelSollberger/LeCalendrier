package ch.telegraphstudios.LeCal.GUI;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JFileChooser;

import ch.telegraphstudios.LeCal.LeCalMain;
import ch.telegraphstudios.LeCal.Calendar.Calendar;
import ch.telegraphstudios.TSMenuBar.Design;
import ch.telegraphstudios.TSMenuBar.TSLabel;
import ch.telegraphstudios.TSMenuBar.TSWindow;

/**
 * This is the welcome screen that displays the first options.
 */
public class LCMainWindow extends TSWindow {
	
	private TSLabel welcomeLabel;
	
	private WelcomeButton newButton;
	private WelcomeButton openButton;

	public LCMainWindow() {
		super();
		
		this.setTitle("Welcome to " + LeCalMain.APPLICATION_NAME);
		
		//Welcome label
		this.welcomeLabel = new TSLabel("Welcome to " + LeCalMain.APPLICATION_NAME);
		this.welcomeLabel.setFont(new Font(Design.DEFAULT_FONT_FAMILY, Font.PLAIN, 50));
		this.welcomeLabel.setBounds(Design.BORDER_DISTANCE, Design.BORDER_DISTANCE, this.getWidth() - (Design.BORDER_DISTANCE * 2), 50);
		this.add(this.welcomeLabel);
		
		//New document button
		this.newButton = new WelcomeButton("New calendar");
		this.newButton.setBounds(Design.BORDER_DISTANCE, this.welcomeLabel.getY() + this.welcomeLabel.getHeight() + Design.BORDER_DISTANCE, this.getWidth() / 2 - (Design.BORDER_DISTANCE * 2), this.getHeight() - this.welcomeLabel.getY() - this.welcomeLabel.getHeight() - Design.BORDER_DISTANCE * 6);
		this.newButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				new LCEditorWindow(new Calendar()).setVisible(true);
				newButton.getParent().getParent().setVisible(false);
			}
			
			@Override
			public void mousePressed(MouseEvent e) { }
			
			@Override
			public void mouseExited(MouseEvent e) { }
			
			@Override
			public void mouseEntered(MouseEvent e) { }
			
			@Override
			public void mouseClicked(MouseEvent e) { }
		});
		this.add(this.newButton);
		
		//Open button
		this.openButton = new WelcomeButton("Open...");
		this.openButton.setBounds(Design.BORDER_DISTANCE + (this.getWidth() / 2), this.welcomeLabel.getY() + this.welcomeLabel.getHeight() + Design.BORDER_DISTANCE, this.getWidth() / 2 - (Design.BORDER_DISTANCE * 2), this.getHeight() - this.welcomeLabel.getY() - this.welcomeLabel.getHeight() - Design.BORDER_DISTANCE * 6);
		this.openButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				openButton.getParent().getParent().setVisible(false);
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				File file = chooser.getSelectedFile();
				
				if (file != null) {
					new LCEditorWindow(new Calendar(file)).setVisible(true);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) { }
			
			@Override
			public void mouseExited(MouseEvent e) { }
			
			@Override
			public void mouseEntered(MouseEvent e) { }
			
			@Override
			public void mouseClicked(MouseEvent e) { }
		});
		this.add(this.openButton);
	}
	
}

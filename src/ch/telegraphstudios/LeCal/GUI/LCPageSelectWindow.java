package ch.telegraphstudios.LeCal.GUI;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import ch.telegraphstudios.LeCal.Calendar.Calendar;
import ch.telegraphstudios.LeCal.Calendar.CalendarPage;
import ch.telegraphstudios.LeCal.Calendar.PageButton;
import ch.telegraphstudios.TSMenuBar.GraphicsUtils;
import ch.telegraphstudios.TSMenuBar.TSWindow;
import ch.telegraphstudios.TSMenuBar.TSWindowPaintListener;

public class LCPageSelectWindow extends TSWindow {

	private Calendar document;
	private LCEditorWindow editor;
	private static final double PAGE_RATIO = Math.sqrt(2);
	private static final int WIDTH = 100;
	
	public LCPageSelectWindow(Calendar document, LCEditorWindow editor) {
		super(WIDTH, (int)(WIDTH / PAGE_RATIO * 12));
		
		this.document = document;
		this.editor = editor;
		
		this.setLocation(32, 32);
		this.setTitle("Pages");
		this.setType(Type.UTILITY);
		
		//Create page buttons
		int index = 0;
		for (CalendarPage page : this.document.getPages()) {
			PageButton button = new PageButton(page, this.document, index, this.editor);
			button.setSize(this.getWidth(), (int)(this.getWidth() / PAGE_RATIO));
			button.setLocation(0, index * button.getHeight());
			this.add(button);
			index++;
		}
		
		new RefreshThread(this);
	}
	
}

/**
 * This thread repaints the given component every second.
 */
class RefreshThread extends Thread {
	
	private Component component;
	
	public RefreshThread(Component component) {
		this.component = component;
		//Autostart
		this.start();
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			this.component.repaint();
		}
	}
	
}
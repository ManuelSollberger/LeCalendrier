package ch.telegraphstudios.LeCal.GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;

import ch.telegraphstudios.LeCal.Calendar.Calendar;
import ch.telegraphstudios.TSMenuBar.GraphicsUtils;
import ch.telegraphstudios.TSMenuBar.TSWindow;
import ch.telegraphstudios.TSMenuBar.TSWindowPaintListener;

public class LCEditorWindow extends TSWindow implements TSWindowPaintListener {
	
	private Calendar document;

	public LCEditorWindow(Calendar document) {
		super(Calendar.WIDTH, Calendar.HEIGHT);
		
		this.document = document;
		
		this.setTitle(this.document.getName() + " - Editor");
		this.addPaintListener(this);
	}

	@Override
	public void paintWindow(Graphics2D g) {
		this.document.paint(g);
	}
	
}

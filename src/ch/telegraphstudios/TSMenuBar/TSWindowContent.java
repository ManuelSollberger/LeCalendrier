package ch.telegraphstudios.TSMenuBar;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * This panel is used as a window content view.
 */
public class TSWindowContent extends JPanel {

	private ArrayList<TSWindowPaintListener> paintListeners = new ArrayList<TSWindowPaintListener>();
	
	public void addPaintListener(TSWindowPaintListener listener) {
		this.paintListeners.add(listener);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		//Call paint listeners.
		for (TSWindowPaintListener paintListener : paintListeners) {
			paintListener.paintWindow(GraphicsUtils.prepareGraphics(g));
		}
	}
	
}

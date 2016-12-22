package ch.telegraphstudios.TSMenuBar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ch.telegraphstudios.TSMenuBar.FileDrop.Listener;

/**
 * A TSDroplet is a component which lets the user drop an image on it.
 */
public class TSDroplet extends JPanel implements Listener {
	
	public static final int DRAG_OVERLAY_LINE_THICKNESS = 12;
	
	private boolean draggingOver;
	
	private ArrayList<TSFileDropListener> dropListeners = new ArrayList<TSFileDropListener>();
	private ArrayList<String> supportedFormats = new ArrayList<String>();
	
	private boolean dragSupported;
	
	private DragThread dragThread;
	int dragOffset;

	public TSDroplet() {
		super();
		new FileDrop(this, this);
	}
	
	/**
	 * Adds a drop listener to this element.
	 * @param listener is the listener that will be called on a file drop.
	 */
	public void addDropListener(TSFileDropListener listener) {
		this.dropListeners.add(listener);
	}
	
	public void addSupportedFormats(String[] formats) {
		for (String format : formats) {
			this.supportedFormats.add(format);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		if (this.draggingOver) {
			
			//Prepare drag over image
			BufferedImage dragImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D dragGraphics = GraphicsUtils.prepareGraphics(dragImage.getGraphics());
			
			dragGraphics.setColor(Color.YELLOW);
			if (!this.dragSupported) {
				dragGraphics.setColor(Color.RED);
			}
			dragGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			//Draw lines
			dragGraphics.setStroke(new BasicStroke(DRAG_OVERLAY_LINE_THICKNESS));
			dragGraphics.setColor(Color.BLACK);
			if (!this.dragSupported) {
				dragGraphics.setColor(Color.WHITE);
			}
			
			int longestSide = this.getWidth();
			if (this.getHeight() > longestSide) {
				longestSide = this.getHeight();
			}
			
			int linesToDraw = longestSide / DRAG_OVERLAY_LINE_THICKNESS + 3;
			linesToDraw *= 2;
			
			for (int i = -2; i < linesToDraw; i += 2) {
				int startX = 0;
				int startY = (int)(i * DRAG_OVERLAY_LINE_THICKNESS * 1.5);
				int endX = (int)(i * DRAG_OVERLAY_LINE_THICKNESS * 1.5);
				int endY = 0;

				startX -= this.dragOffset;
				startY -= this.dragOffset;
				endX -= this.dragOffset;
				endY -= this.dragOffset;
				
				dragGraphics.drawLine(startX, startY, endX, endY);
			}
			
			//Clear center
			int border = DRAG_OVERLAY_LINE_THICKNESS / 2;
			dragGraphics.clearRect(border, border, this.getWidth() - (border * 2), this.getHeight() - (border * 2));
			
			g.drawImage(dragImage, 0, 0, this.getWidth(), this.getHeight(), null);
			
		}
	}

	@Override
	public void filesDropped(File[] files, Component c) {
		this.fileDragExit(c);
		
		boolean allOK = true;
		
		for (File file : files) {
			if (!this.supportsFile(file)) {
				allOK = false;
			}
		}
		
		if (allOK) {
			for (TSFileDropListener listener : this.dropListeners) {
				listener.onFileDrop(this, files);
			}
		}
	}
	
	public boolean supportsFile(File file) {
		for (String format : this.supportedFormats) {
			if (file.getName().endsWith(format)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void fileDragExit(Component c) {
		this.draggingOver = false;
		this.dragThread.stopDrag();
		this.repaint();
	}

	@Override
	public void fileDragEnter(File[] files, Component c) {
		this.dragSupported = true;
		
		for (File file : files) {
			if (!this.supportsFile(file)) {
				this.dragSupported = false;
			}
		}
		
		this.draggingOver = true;
		this.dragThread = new DragThread(this);
		
		this.repaint();
	}
	
}

/**
 * This thread increments and resets the drag offset value of a droplet to improve the optical effect.
 */
class DragThread extends Thread {
	
	private TSDroplet droplet;
	private boolean running;
	
	public DragThread(TSDroplet droplet) {
		this.droplet = droplet;
		
		//Autostart
		this.running = true;
		this.start();
	}
	
	public void stopDrag() {
		this.running = false;
	}
	
	@Override
	public void run() {
		while (this.running) {
			this.droplet.dragOffset++;
			if (this.droplet.dragOffset > TSDroplet.DRAG_OVERLAY_LINE_THICKNESS * 1.5) {
				this.droplet.dragOffset = 1;
			}
			
			this.droplet.repaint();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Repaint component without drag effect
		this.droplet.getParent().repaint();
	}
	
}
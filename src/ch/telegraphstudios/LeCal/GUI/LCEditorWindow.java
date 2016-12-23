package ch.telegraphstudios.LeCal.GUI;

import java.awt.Graphics2D;
import java.awt.Window.Type;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;

import ch.telegraphstudios.LeCal.Calendar.Calendar;
import ch.telegraphstudios.LeCal.Calendar.DropZone;
import ch.telegraphstudios.TSMenuBar.TSDroplet;
import ch.telegraphstudios.TSMenuBar.TSFileDropListener;
import ch.telegraphstudios.TSMenuBar.TSMenuBar;
import ch.telegraphstudios.TSMenuBar.TSMenuItem;
import ch.telegraphstudios.TSMenuBar.TSWindow;
import ch.telegraphstudios.TSMenuBar.TSWindowPaintListener;


public class LCEditorWindow extends TSWindow implements TSWindowPaintListener, TSFileDropListener {
	
	private Calendar document;
	private LCPageSelectWindow pageWindow;
	
	private ArrayList<TSDroplet> droplets = new ArrayList<TSDroplet>();
	
	private String[] extensions = new String[] {"png", "jpg", "jpeg"};

	public LCEditorWindow(Calendar document) {
		super(Calendar.WIDTH, Calendar.HEIGHT);
		
		this.document = document;
		
		this.setTitle(this.document.getName() + " - Editor");
		this.addPaintListener(this);
		
		this.pageWindow = new LCPageSelectWindow(this.document, this);
		this.addChild(this.pageWindow);
		this.pageWindow.setVisible(true);
		
		this.updateDropZones();
	}
	
	/**
	 * This method recreates the virtual drop panels for every drop zone on the current page.
	 */
	public void updateDropZones() {
		//Delete old droplets
		for (TSDroplet droplet : droplets) {
			this.contentView.remove(droplet);
		}
		
		this.droplets = new ArrayList<TSDroplet>();
		
		ArrayList<TSDroplet> dropletsToAdd = new ArrayList<TSDroplet>(); 
		for (DropZone dropZone : this.document.getCurrentPage().getTemplate().getDropZones()) {
			TSDroplet droplet = new TSDroplet();
			droplet.setBounds(dropZone.getBounds());
			droplet.addSupportedFormats(this.extensions);
			droplet.addDropListener(this);
			dropletsToAdd.add(droplet);
			this.droplets.add(droplet);
		}
		
		for (int i = dropletsToAdd.size() - 1; i >= 0; i--) {
			TSDroplet droplet = dropletsToAdd.get(i);
			this.contentView.add(droplet);
		}
		
		this.setTitle("" + new DateFormatSymbols().getMonths()[this.document.getCurrentPageIndex()] + " (" + this.document.getName() + ") - Editor");
		
		this.repaint();
	}
	
	public Calendar getDocument() {
		return this.document;
	}

	@Override
	public void paintWindow(Graphics2D g) {
		this.document.paint(g);
	}

	@Override
	public void onFileDrop(TSDroplet source, File[] files) {
		if (files.length > 0) {
			//Take the first file.
			File file = files[0];
			
			for (String extension : this.extensions) {
				if (file.getName().endsWith("." + extension)) {
					
					//Read file to buffered image.
					try {
						BufferedImage image = ImageIO.read(file);
						
						this.document.getCurrentPage().setDropZoneImage(this.droplets.indexOf(source), image, file.getAbsolutePath());
						
						this.repaint();
					} catch (IOException e) {
						System.out.println("Error while loading image " + file.getAbsolutePath());
						e.printStackTrace();
					}
					
				}
			}
		}
	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		super.windowGainedFocus(e);
		
		//Enable certain items if this editor is selected.
		ArrayList<TSMenuItem> fileItems = TSMenuBar.getInstance().getMenuBarItem("File").getMenu().getItems();
		for (TSMenuItem item : fileItems) {
			if (item.getTitle().equals("Save...") || item.getTitle().equals("Export as PDF...")) {
				item.setEnabled(true);
			}
		}
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		super.windowLostFocus(e);
		
		//Disable certain items if no editor is selected.
		if (e.getOppositeWindow() != null && !e.getOppositeWindow().equals(TSMenuBar.getInstance())) {
			ArrayList<TSMenuItem> fileItems = TSMenuBar.getInstance().getMenuBarItem("File").getMenu().getItems();
			for (TSMenuItem item : fileItems) {
				if (item.getTitle().equals("Save...") || item.getTitle().equals("Export as PDF...")) {
					item.setEnabled(false);
				}
			}
		}
	}
	
}

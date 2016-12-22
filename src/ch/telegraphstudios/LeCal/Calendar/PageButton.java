package ch.telegraphstudios.LeCal.Calendar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import ch.telegraphstudios.LeCal.GUI.LCEditorWindow;
import ch.telegraphstudios.TSMenuBar.Design;
import ch.telegraphstudios.TSMenuBar.GraphicsUtils;

public class PageButton extends JPanel implements MouseListener {

	private CalendarPage page;
	private Calendar document;
	private int index;
	private LCEditorWindow editor;
	
	public PageButton(CalendarPage page, Calendar document, int index, LCEditorWindow editor) {
		this.page = page;
		this.document = document;
		this.index = index;
		this.editor = editor;
		
		this.addMouseListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = GraphicsUtils.prepareGraphics(g);
		
		BufferedImage pageImage = new BufferedImage(Calendar.WIDTH, Calendar.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D pageGraphics = GraphicsUtils.prepareGraphics(pageImage.getGraphics());
		
		//Paint the page onto the unscaled graphics context.
		this.page.paint(pageGraphics);
		
		//Paint the page scaled onto this button.
		g2d.drawImage(pageImage, 0, 0, this.getWidth(), this.getHeight(), null);
		
		//Draw the selected overlay if this page is selected.
		if (this.document.getCurrentPageIndex() == this.index) {
			g2d.setColor(Design.SELECTION_COLOR);
			g2d.setStroke(new BasicStroke(4));
			g2d.drawRect(0, 0, this.getWidth(), this.getHeight());
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) { }

	@Override
	public void mouseEntered(MouseEvent event) { }

	@Override
	public void mouseExited(MouseEvent event) { }

	@Override
	public void mousePressed(MouseEvent event) { }

	@Override
	public void mouseReleased(MouseEvent event) {
		this.document.selectPage(this.index);
		this.editor.updateDropZones();
		this.getParent().repaint();
	}
	
}

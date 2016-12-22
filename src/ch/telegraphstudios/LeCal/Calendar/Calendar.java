package ch.telegraphstudios.LeCal.Calendar;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.JFileChooser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import ch.telegraphstudios.TSMenuBar.GraphicsUtils;

/**
 * This is the bean that represents a calendar project.
 */
public class Calendar {
	
	/**
	 * The ratio of a calendar page is the square root of 2 (1.4) to fit a standard A-type page.
	 */
	public static final float CALENDAR_RATIO = (float)Math.sqrt(2);
	
	/**
	 * The height of the calendar view.
	 */
	public static final int HEIGHT = 520;
	
	/**
	 * The width of the calendar view. (sqrt(2) * width)
	 */
	public static final int WIDTH = (int)(HEIGHT * CALENDAR_RATIO);
	
	private String name;
	private CalendarPage[] pages = new CalendarPage[12];
	private int currentPage;
	
	public Calendar() {
		this.name = "Unnamed";
		
		//Init pages
		for (int i = 0; i < pages.length; i++) {
			pages[i] = new CalendarPage();
		}
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void selectPage(int page) {
		this.currentPage = page;
	}
	
	public CalendarPage getCurrentPage() {
		return this.pages[this.currentPage];
	}
	
	public int getCurrentPageIndex() {
		return this.currentPage;
	}
	
	public void paint(Graphics2D g) {
		CalendarPage page = pages[this.currentPage];
		
		if (page != null) {
			page.paint(g);
		}
	}

	public CalendarPage[] getPages() {
		return this.pages;
	}
	
	/**
	 * Save this calendar document as a PDF file.
	 */
	public void saveToPDF() {
		PDDocument document = new PDDocument();
		
		//Add all 12 pages
		for (int i = 0; i < 12; i++) {
			PDPage page = new PDPage(PDRectangle.A4);
			//page.setRotation(90);
			document.addPage(page);
			
			try {
				//PDPageContentStream contentStream = new PDPageContentStream(document, page);
				
				PDRectangle pageSize = page.getMediaBox();
				float pageWidth = pageSize.getWidth();
				float pageHeight = pageSize.getHeight();
				
				PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.OVERWRITE, false);
				
				//Add the rotation using the current transformation matrix
				//including a translation of pageWidth to use the lower left corner as 0,0 reference.
				//contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));

				//Prepare month image
				BufferedImage monthImage = Calendar.getCalendarPage(i + 1);
				PDImageXObject monthImageXO = LosslessFactory.createFromImage(document, monthImage);
				
				//Get drop zones and images.
				ArrayList<DropZone> dropZones = this.pages[i].getTemplate().getDropZones();
				int index = 0;
				for (DropZone dropZone : dropZones) {
					ArrayList<BufferedImage> images = this.pages[i].getDropZoneImages();
					if (index < images.size()) {
						BufferedImage zoneImage = images.get(index);
						PDImageXObject imageXO = LosslessFactory.createFromImage(document, zoneImage);
						
						//Scale down the coordinates and sizes
						int widthP = (int)((pageWidth / Calendar.WIDTH) * dropZone.getBounds().getWidth());
						int heightP = (int)(((pageHeight / 2) / Calendar.HEIGHT) * dropZone.getBounds().getHeight());
						int xP = (int)((pageWidth / Calendar.WIDTH) * dropZone.getBounds().getX());
						int yP = (int)(((pageHeight / 2) / Calendar.HEIGHT) * dropZone.getBounds().getY());
						
						//Draw this drop zone image.
						contentStream.drawImage(imageXO, xP, pageHeight - yP - heightP, widthP, heightP);
					}
					
					index++;
				}
				
				//Draw month page.
				int w = (int)((pageWidth / Calendar.WIDTH) * monthImage.getWidth());
				int h = (int)(((pageHeight / 2) / Calendar.HEIGHT) * monthImage.getHeight());
				contentStream.drawImage(monthImageXO, 0, 0, pageWidth, pageHeight / 2);
				
				contentStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Save document
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(null);
		File file = chooser.getSelectedFile();
		
		if (file != null) {
			try {
				document.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Close document
		try {
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param month
	 * @param width
	 * @param height
	 * @return an image with the calendar month page on it.
	 */
	public static BufferedImage getCalendarPage(int month) {
		BufferedImage page = new BufferedImage(Calendar.WIDTH, Calendar.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D pg = GraphicsUtils.prepareGraphics(page.getGraphics());
		
		final int cellSize = Calendar.WIDTH / 7;
		
		int year = Year.now().getValue() + 1;
		YearMonth yearMonth = YearMonth.of(year, month);
		int daysCount = yearMonth.lengthOfMonth();
		int weekdayIndex = yearMonth.atDay(1).getDayOfWeek().getValue() - 1;
		
		//Get all week numbers
		ArrayList<Integer> weeks = new ArrayList<Integer>();
		for (int date = 1; date <= daysCount; date++) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.set(year, month - 1, date);
			
			int weekNumber = cal.get(GregorianCalendar.WEEK_OF_YEAR);
			
			if (weeks.size() == 0 || weeks.get(weeks.size() - 1) != weekNumber) {
				weeks.add(weekNumber);
			}
		}
		
		//Store the dates with weekdays in here.
		ArrayList<ArrayList<Integer>> days = new ArrayList<ArrayList<Integer>>();
		
		//Iterate the weeks
		int date = 1;
		for (Integer weekNumber : weeks) {
			ArrayList<Integer> thisWeek = new ArrayList<Integer>();
			
			GregorianCalendar cal = new GregorianCalendar();
			cal.set(year, month - 1, date);
			int checkWeek = cal.get(GregorianCalendar.WEEK_OF_YEAR);
			
			//Check if this date is still in this week.
			while (weekNumber == checkWeek) {
				thisWeek.add(date);

				date++;
				
				cal = new GregorianCalendar();
				cal.set(year, month - 1, date);
				checkWeek = cal.get(GregorianCalendar.WEEK_OF_YEAR);
				
				if (date > daysCount) {
					break;
				}
			}
			
			days.add(thisWeek);
			
			if (date > daysCount) {
				break;
			}
			
			//Go to the next week.
		}
		
		//Generate the image (finally, I'm really tired of date calculation for now).
		int y = 0;
		int x = 0;
		
		for (ArrayList<Integer> week : days) {
			int dayIndex = 0;
			for (Integer day : week) {
				x = (7 - week.size()) + dayIndex;
				
				pg.setColor(Color.BLACK);
				pg.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
				pg.drawString("" + day, x * cellSize, y * cellSize + cellSize);
				
				dayIndex++;
			}
			
			y++;
		}
		
		return page;
	}
	
}

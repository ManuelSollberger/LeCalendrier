package ch.telegraphstudios.LeCal.Calendar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JFileChooser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import ch.telegraphstudios.TSMenuBar.Design;
import ch.telegraphstudios.TSMenuBar.GraphicsUtils;

/**
 * This is the bean that represents a calendar project.
 */
public class Calendar {
	
	private static final int PAGE_LEFT_RIGHT_PADDING = 64;
	private static final int PAGE_TOP_PADDING = 256;
	private static final int PAGE_BOTTOM_PADDING = 64;
	
	/**
	 * The delimiter for the saved file.
	 */
	private static final String DELIMITER = ";";
	
	public static final String DOCUMENT_EXTENSION = ".lecal";
	
	/**
	 * The ratio of a calendar page is the square root of 2 (1.4) to fit a standard A-type page.
	 */
	public static final float CALENDAR_RATIO = (float)Math.sqrt(2);
	
	/**
	 * The height of the calendar view.
	 */
	public static final int HEIGHT = 540;
	
	/**
	 * The width of the calendar view. (sqrt(2) * width)
	 */
	public static final int WIDTH = (int)(HEIGHT * CALENDAR_RATIO);
	
	/**
	 * This is the width of the image
	 */
	public static final int TRUE_WIDTH = WIDTH * 2;

	/**
	 * This is the height of the image
	 */
	public static final int TRUE_HEIGHT = HEIGHT * 2;
	
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
	
	/**
	 * Load the calendar at the given path.
	 * @param file
	 */
	public Calendar(File file) {
		this();
		
		if (file.getName().endsWith(DOCUMENT_EXTENSION)) {
			
			this.name = file.getName().replace(DOCUMENT_EXTENSION, "");
			
			String fileContent = "";
			
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				
				String line = reader.readLine();
				while (line != null && !line.equals("")) {
					if (!fileContent.equals("")) {
						fileContent += "\r\n";
					}
					fileContent += line;
					line = reader.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//Use file content.
			if (fileContent != null && !fileContent.equals("")) {
				String[] lines = fileContent.split("\r\n");
				
				int index = 0;
				for (CalendarPage page : pages) {
					String thisLine = lines[index];
					
					String[] parameter = thisLine.split(DELIMITER);
					
					//Get parameters.
					String templateName = parameter[0];
					
					if (parameter.length > 1) {
						String imageString = parameter[1];
						
						if (!imageString.equals("")) {
							String[] images = imageString.split(",");
							
							//Read images.
							int imageIndex = 0;
							for (String imagePath : images) {
								try {
									//Set the drop zone image.
									BufferedImage image = ImageIO.read(new File(imagePath));
									page.setDropZoneImage(imageIndex, image, imagePath);
								} catch (IOException e) {
									e.printStackTrace();
								}
								
								imageIndex++;
							}
						}
					}
					
					page.setTemplate(Templates.getByName(templateName));
					
					index++;
				}
			}
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
		if (!file.getName().endsWith(".pdf")) {
			file = new File(file.getAbsolutePath() + ".pdf");
		}
		
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
		BufferedImage page = new BufferedImage(Calendar.TRUE_WIDTH, Calendar.TRUE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D pg = GraphicsUtils.prepareGraphics(page.getGraphics());
		
		int year = Year.now().getValue() + 1;
		YearMonth yearMonth = YearMonth.of(year, month);
		int daysCount = yearMonth.lengthOfMonth();
		
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
		
		final int calendarWidth = Calendar.TRUE_WIDTH - (PAGE_LEFT_RIGHT_PADDING * 2);
		final int calendarHeight = Calendar.TRUE_HEIGHT - PAGE_TOP_PADDING - PAGE_BOTTOM_PADDING;
		
		final int cellWidth = calendarWidth / 7;
		final int cellHeight = calendarHeight / days.size();
		
		//Generate the image (finally, I'm really tired of date calculation for now).
		int y = 0;
		int x = 0;

		//Prepare the graphics context.
		pg.setColor(Color.BLACK);
		
		//Print month name
		String name = yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
		pg.setFont(new Font(Design.DEFAULT_FONT_FAMILY, Font.PLAIN, 80));
		pg.drawString(name, PAGE_LEFT_RIGHT_PADDING, 80 + PAGE_LEFT_RIGHT_PADDING);
		
		//Draw the week day names
		pg.setFont(new Font(Design.DEFAULT_FONT_FAMILY, Font.PLAIN, 30));
		for (int i = 1; i <= 7; i++) {
			String dayName = DayOfWeek.of(i).getDisplayName(TextStyle.FULL, Locale.getDefault());
			
			pg.drawString(dayName, PAGE_LEFT_RIGHT_PADDING + ((i - 1) * cellWidth), 80 + PAGE_LEFT_RIGHT_PADDING + 90);
		}

		pg.setFont(new Font(Design.DEFAULT_FONT_FAMILY, Font.PLAIN, 40));
		
		for (ArrayList<Integer> week : days) {
			int dayIndex = 0;
			
			for (Integer day : week) {
				if (days.indexOf(week) != days.size() - 1) {
					x = (7 - week.size()) + dayIndex;
				}
				else {
					x = dayIndex;
				}
				
				//Fill a light-gray background if this is a weekend day.
				int dayOfWeek = new GregorianCalendar(year, month - 1, day).get(GregorianCalendar.DAY_OF_WEEK);
				if (dayOfWeek == GregorianCalendar.SATURDAY || dayOfWeek == GregorianCalendar.SUNDAY) {
					pg.setColor(new Color(0xEEEEEE));
					pg.fillRect(x * cellWidth + PAGE_LEFT_RIGHT_PADDING, y * cellHeight + PAGE_TOP_PADDING, cellWidth, cellHeight);
				}
				
				pg.setColor(Color.BLACK);
				pg.drawRect(x * cellWidth + PAGE_LEFT_RIGHT_PADDING, y * cellHeight + PAGE_TOP_PADDING, cellWidth, cellHeight);
				pg.drawString("" + day, x * cellWidth + PAGE_LEFT_RIGHT_PADDING, y * cellHeight + cellHeight + PAGE_TOP_PADDING);
				
				dayIndex++;
			}
			
			y++;
		}
		
		return page;
	}
	
	public void saveToFile() {
		//Choose a file.
		JFileChooser chooser = new JFileChooser(new File(this.name));
		chooser.showSaveDialog(null);
		File file = chooser.getSelectedFile();
		if (!file.getName().endsWith(DOCUMENT_EXTENSION)) {
			file = new File(file.getAbsolutePath() + DOCUMENT_EXTENSION);
		}
		
		if (file != null) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				
				writer.write(this.toString());
				
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Converts this calendar document into a writable string.
	 */
	public String toString() {
		String string = "";
		
		for (int i = 0; i < 12; i++) {
			String thisLine = "";
			
			CalendarPage thisPage = this.pages[i];
			
			String templateName = thisPage.getTemplate().getName();
			
			thisLine += templateName;
			thisLine += DELIMITER + thisPage.toString();
			thisLine += "\r\n";
			
			string += thisLine;
		}
		
		return string;
	}
	
}

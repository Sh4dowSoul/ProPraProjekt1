package applicationLogic;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.HorizontalAlignment;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.VerticalAlignment;
import be.quodlibet.boxable.line.LineStyle;
import dataStorageAccess.ResultAccess;

/**
 * PDFExport class
 *
 * @author Sven Motschnig
 *
 */

public class PDFExport {
	// Loading the Fonts
	static File arial = new File("fonts/ARIAL.TTF");
	static File arialBold = new File("fonts/ARIALBD.TTF");
	static File arialBoldCursive = new File("fonts/ARIALBI.TTF");
	static File arialCursive = new File("fonts/ARIALI.TTF");

	static PDDocument document = null;
	static PDPageContentStream contentStream;
	static ResultComplete data;
	static int pageCounter = 0;
	static PDPage page1;

	/**
	 * Creates new PDFDocument, sets the Fonts, calls createPDF method for content
	 * and saves the File as PDF
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void export(int id) throws IOException, SQLException {
		try {
			// Creating PDF document object
			document = new PDDocument();
			data = ResultAccess.getCompleteResult(id);

			// Setting Fonts
			PDFont fontArial = PDType0Font.load(document, arial);
			PDFont fontArialBold = PDType0Font.load(document, arialBold);
			PDFont fontArialBoldCursive = PDType0Font.load(document, arialBoldCursive);
			PDFont fontArialCursive = PDType0Font.load(document, arialCursive);

			createPDF(fontArial, fontArialBold, fontArialBoldCursive, fontArialCursive);

			document.save("exportedFiles/BS" + "_" + data.getCompanyPlant().getCompany().getName() + "_" + id + ".pdf");
			System.out.println("Document saved");
		} catch (IOException e) {
			// sm. Fehlermeldung, wenn Dokument geöffnet ist => als Meldung in die GUI
			e.printStackTrace();
		} finally {
			// Closing the document
			if (document != null) {
				document.close();
			}
		}
	}

	/**
	 * generates dynamic texts with automatic line breaks
	 * 
	 * @param page
	 * @param pdfFont
	 * @param fontSize
	 * @param leading
	 * @param width
	 * @param startX
	 * @param startY
	 * @param content
	 * @return paddingOfString (for each line break)
	 * @throws IOException
	 */
	private static float setDynamicText(PDPage page, PDFont pdfFont, float fontSize, float leading, float width,
			float startX, float startY, String content) throws IOException {
		leading = leading * fontSize;
		int loopCounterDynamicText = 1;

		if (content == null || content.isEmpty()) {
			content = "";
		}

		List<String> lines = new ArrayList<String>();
		int lastSpace = -1;
		while (content.length() > 0) {
			int spaceIndex = content.indexOf(' ', lastSpace + 1);
			if (spaceIndex < 0)
				spaceIndex = content.length();
			String subString = content.substring(0, spaceIndex);
			float size = fontSize * pdfFont.getStringWidth(subString) / 1000;
			if (size > width) {
				if (lastSpace < 0)
					lastSpace = spaceIndex;
				subString = content.substring(0, lastSpace);
				lines.add(subString);
				content = content.substring(lastSpace).trim();
				lastSpace = -1;
				loopCounterDynamicText++;
			} else if (spaceIndex == content.length()) {
				lines.add(content);
				content = "";
			} else {
				lastSpace = spaceIndex;
			}
		}

		contentStream.beginText();
		contentStream.setFont(pdfFont, fontSize);
		contentStream.newLineAtOffset(startX, startY);
		for (String line : lines) {
			contentStream.showText(line);
			contentStream.newLineAtOffset(0, -leading);
		}
		contentStream.endText();

		boolean stringRuns = true;
		int counter = 1;
		float paddingOfString = 0;
		while (stringRuns) {
			if (loopCounterDynamicText != counter) {
				paddingOfString += 11f;
			} else if (loopCounterDynamicText == counter) {
				stringRuns = false;
			}
			counter++;
		}
		return paddingOfString;
	}

	/**
	 * creates single-row tables as frames
	 * 
	 * @param page
	 * @param yPosition
	 * @param cellHeight
	 * @param cellWidth
	 * @throws IOException
	 */
	private static void drawSingleCellTable(PDPage page, float yPosition, float cellHeight, float cellWidth)
			throws IOException {
		LineStyle BorderStyle = new LineStyle(Color.BLACK, 0.75f);
		float margin = 48f;
		int leftMargin = 55;
		boolean drawContent = true;
		float bottomMargin = 0;
		float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
		float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

		BaseTable table = new BaseTable(yPosition, yStartNewPage, bottomMargin, tableWidth, leftMargin, document, page,
				true, drawContent);

		Row<PDPage> headerRow = table.createRow(cellHeight);
		Cell<PDPage> cell = headerRow.createCell(cellWidth, "");
		cell.setBorderStyle(BorderStyle);

		table.draw();
	}

	/**
	 * creates new static texts (no user data)
	 * 
	 * @param font
	 * @param size
	 * @param positionX
	 * @param positionY
	 * @param content
	 * @throws IOException
	 */
	private static void setStaticText(PDFont font, int size, float positionX, float positionY, String content)
			throws IOException {
		contentStream.beginText();
		contentStream.setFont(font, size);
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.newLineAtOffset(positionX, positionY);
		contentStream.showText(content);
		contentStream.endText();
	}

	/**
	 * creates texts with contents from the database
	 * 
	 * @param font
	 * @param size
	 * @param positionX
	 * @param positionY
	 * @param content
	 * @throws IOException
	 */
	private static void setDatabaseText(PDFont font, int size, float positionX, float positionY, String content)
			throws IOException {
		contentStream.beginText();
		contentStream.setFont(font, size);
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.newLineAtOffset(positionX, positionY);
		if (content != null && !content.isEmpty()) {
			contentStream.showText(content);
		} else {
			contentStream.showText("");
		}
		contentStream.endText();
	}

	/**
	 * creates new static frames
	 * 
	 * @param positionX
	 * @param posotionY
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	private static void setStaticFrame(float positionX, float positionY, float width, float height) throws IOException {
		contentStream.setStrokingColor(Color.BLACK);
		contentStream.setNonStrokingColor(Color.WHITE);
		contentStream.setLineWidth(0.75f);
		contentStream.setLineDashPattern(new float[0], 0);
		contentStream.addRect(positionX, positionY, width, height);
		contentStream.closeAndFillAndStrokeEvenOdd();
	}

	/**
	 * sets new unchecked checkbox
	 * 
	 * @param posistionX
	 * @param positionY
	 * @throws IOException
	 */
	private static void setCheckboxUnchecked(float positionX, float positionY) throws IOException {
		contentStream.setStrokingColor(Color.BLACK);
		contentStream.setNonStrokingColor(Color.WHITE);
		contentStream.setLineWidth(0.75f);
		contentStream.setLineDashPattern(new float[0], 0);
		contentStream.addRect(positionX, positionY, 7, 7);
		contentStream.closeAndFillAndStrokeEvenOdd();
	}

	/**
	 * sets new checked checkbox
	 * 
	 * @param posistionX
	 * @param positionY
	 * @throws IOException
	 */
	private static void setCheckboxChecked(float posistionX, float positionY) throws IOException {
		contentStream.setStrokingColor(Color.BLACK);
		contentStream.setNonStrokingColor(Color.WHITE);
		contentStream.setLineWidth(0.75f);
		contentStream.setLineDashPattern(new float[0], 0);
		contentStream.addRect(posistionX, positionY, 7, 7);
		contentStream.closeAndFillAndStrokeEvenOdd();
		contentStream.setLineWidth(0.75f);
		contentStream.moveTo(posistionX, positionY);
		contentStream.lineTo(posistionX + 7, positionY + 7);
		contentStream.moveTo(posistionX, positionY + 7);
		contentStream.lineTo(posistionX + 7, positionY);
		contentStream.stroke();
	}

	/**
	 * places checkbox with one query
	 * 
	 * @param isTrue
	 * @param positionVertical
	 * @param positionHorizontal
	 * @throws IOException
	 */
	private static void setOneCheckbox(boolean isTrue, float positionVertical, float positionHorizontal)
			throws IOException {
		if (isTrue) {
			setCheckboxChecked(positionVertical, positionHorizontal);
		} else {
			setCheckboxUnchecked(positionVertical, positionHorizontal);
		}
	}

	/**
	 * places checkbox with two queries
	 * 
	 * @param isTrue
	 * @param positionLeft
	 * @param positionRight
	 * @param heightPosition
	 * @throws IOException
	 */
	private static void setTwoCheckboxes(boolean isTrue, float positionLeft, float positionRight, float heightPosition)
			throws IOException {
		if (isTrue) {
			setCheckboxChecked(positionLeft, heightPosition);
			setCheckboxUnchecked(positionRight, heightPosition);
		} else if (!isTrue) {
			setCheckboxChecked(positionRight, heightPosition);
			setCheckboxUnchecked(positionLeft, heightPosition);
		} else {
			setCheckboxUnchecked(positionLeft, heightPosition);
			setCheckboxUnchecked(positionRight, heightPosition);
		}
	}

	/**
	 * places checkbox with three queries
	 * 
	 * @param getStatus
	 * @param positionLeft
	 * @param positionMiddle
	 * @param positionRight
	 * @param heightPosition
	 * @throws IOException
	 */
	private static void setThreeCheckboxes(int getStatus, float positionLeft, float positionMiddle, float positionRight,
			float heightPosition) throws IOException {
		if (getStatus == 0) {
			setCheckboxChecked(positionLeft, heightPosition);
			setCheckboxUnchecked(positionMiddle, heightPosition);
			setCheckboxUnchecked(positionRight, heightPosition);
		} else if (getStatus == 1) {
			setCheckboxUnchecked(positionLeft, heightPosition);
			setCheckboxChecked(positionMiddle, heightPosition);
			setCheckboxUnchecked(positionRight, heightPosition);
		} else if (getStatus == 2) {
			setCheckboxUnchecked(positionLeft, heightPosition);
			setCheckboxUnchecked(positionMiddle, heightPosition);
			setCheckboxChecked(positionRight, heightPosition);
		} else {
			setCheckboxUnchecked(positionLeft, heightPosition);
			setCheckboxUnchecked(positionMiddle, heightPosition);
			setCheckboxUnchecked(positionRight, heightPosition);
		}
	}

	/**
	 * places checkbox with four queries
	 * 
	 * @param getStatus
	 * @param position1
	 * @param position2
	 * @param position3
	 * @param position4
	 * @param heightPosition
	 * @throws IOException
	 */
	private static void setFourCheckboxes(int getStatus, float position1, float position2, float position3,
			float position4, float heightPosition) throws IOException {
		if (getStatus == 0) {
			setCheckboxChecked(position1, heightPosition);
			setCheckboxUnchecked(position2, heightPosition);
			setCheckboxUnchecked(position3, heightPosition);
			setCheckboxUnchecked(position4, heightPosition);
		} else if (getStatus == 1) {
			setCheckboxUnchecked(position1, heightPosition);
			setCheckboxChecked(position2, heightPosition);
			setCheckboxUnchecked(position3, heightPosition);
			setCheckboxUnchecked(position4, heightPosition);
		} else if (getStatus == 2) {
			setCheckboxUnchecked(position1, heightPosition);
			setCheckboxUnchecked(position2, heightPosition);
			setCheckboxChecked(position3, heightPosition);
			setCheckboxUnchecked(position4, heightPosition);
		} else if (getStatus == 3) {
			setCheckboxUnchecked(position1, heightPosition);
			setCheckboxUnchecked(position2, heightPosition);
			setCheckboxUnchecked(position3, heightPosition);
			setCheckboxChecked(position4, heightPosition);
		} else {
			setCheckboxUnchecked(position1, heightPosition);
			setCheckboxUnchecked(position2, heightPosition);
			setCheckboxUnchecked(position3, heightPosition);
			setCheckboxUnchecked(position4, heightPosition);
		}
	}

	/**
	 * places checkbox with five queries
	 * 
	 * @param getStatus
	 * @param position1
	 * @param position2
	 * @param position3
	 * @param position4
	 * @param position5
	 * @param heightPosition
	 * @throws IOException
	 */
	private static void setFiveCheckboxes(int getStatus, float position1, float position2, float position3,
			float position4, float position5, float heightPosition) throws IOException {
		if (getStatus == 0) {
			setCheckboxChecked(position1, heightPosition);
			setCheckboxUnchecked(position2, heightPosition);
			setCheckboxUnchecked(position3, heightPosition);
			setCheckboxUnchecked(position4, heightPosition);
			setCheckboxUnchecked(position5, heightPosition);
		} else if (getStatus == 1) {
			setCheckboxUnchecked(position1, heightPosition);
			setCheckboxChecked(position2, heightPosition);
			setCheckboxUnchecked(position3, heightPosition);
			setCheckboxUnchecked(position4, heightPosition);
			setCheckboxUnchecked(position5, heightPosition);
		} else if (getStatus == 2) {
			setCheckboxUnchecked(position1, heightPosition);
			setCheckboxUnchecked(position2, heightPosition);
			setCheckboxChecked(position3, heightPosition);
			setCheckboxUnchecked(position4, heightPosition);
			setCheckboxUnchecked(position5, heightPosition);
		} else if (getStatus == 3) {
			setCheckboxUnchecked(position1, heightPosition);
			setCheckboxUnchecked(position2, heightPosition);
			setCheckboxUnchecked(position3, heightPosition);
			setCheckboxChecked(position4, heightPosition);
			setCheckboxUnchecked(position5, heightPosition);
		} else if (getStatus == 4) {
			setCheckboxUnchecked(position1, heightPosition);
			setCheckboxUnchecked(position2, heightPosition);
			setCheckboxUnchecked(position3, heightPosition);
			setCheckboxUnchecked(position4, heightPosition);
			setCheckboxChecked(position5, heightPosition);
		} else {
			setCheckboxUnchecked(position1, heightPosition);
			setCheckboxUnchecked(position2, heightPosition);
			setCheckboxUnchecked(position3, heightPosition);
			setCheckboxUnchecked(position4, heightPosition);
			setCheckboxUnchecked(position5, heightPosition);
		}
	}

	/**
	 * draws a separator line
	 * 
	 * @param lineWidth
	 * @param startPositionX
	 * @param startPositionY
	 * @param endPositionX
	 * @param endPositionY
	 * @param dotted
	 * @throws IOException
	 */
	private static void drawSeparatorLine(float lineWidth, float startPositionX, float startPositionY,
			float endPositionX, float endPositionY, boolean dotted) throws IOException {
		contentStream.setLineWidth(lineWidth);
		contentStream.moveTo(startPositionX, startPositionY);
		contentStream.lineTo(endPositionX, endPositionY);
		if (dotted) {
			contentStream.setLineDashPattern(new float[] { 2.5f }, 0);
		}
		contentStream.stroke();
	}

	/**
	 * creates static text of page number and total pages
	 * 
	 * @param font
	 * @param actPage
	 * @param totalPages
	 * @throws IOException
	 */
	private static void setStaticTextPageOf(PDFont font, int actPage, int totalPages) throws IOException {
		contentStream.beginText();
		contentStream.setFont(font, 9);
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.newLineAtOffset(474, 810);
		contentStream.showText("Blatt-Nr. " + actPage + " von " + totalPages);
		contentStream.endText();
	}

	/**
	 * creates static text of document version
	 * 
	 * @param font
	 * @throws IOException
	 */
	private static void setStaticTextVersion(PDFont font) throws IOException {
		contentStream.beginText();
		contentStream.setFont(font, 8);
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.newLineAtOffset(59, 25);
		contentStream.showText("VdS 2229 : 2017-12 (01)");
		contentStream.endText();
	}

	/**
	 * creates a template for dynamic pages (appendix a)
	 * 
	 * @param page
	 * @param table
	 * @param fontArial
	 * @param fontArialBold
	 * @param fontArialBoldCursive
	 * @param fontArialCursive
	 * @throws IOException
	 */
	private static void createTemplateLastPages(PDPage page, BaseTable table, PDFont fontArial, PDFont fontArialBold,
			PDFont fontArialBoldCursive, PDFont fontArialCursive) throws IOException {
		boolean lastPageOfDocument = false;
		if (table.getCurrentPage() != page) {
			contentStream.close();
			page = table.getCurrentPage();
			// lastStaticPage
			int i = 3;
			while (!lastPageOfDocument) {
				contentStream = new PDPageContentStream(document, document.getPage(i), AppendMode.APPEND, false);
				setStaticTextPageOf(fontArialCursive, (++pageCounter), document.getNumberOfPages());
				setStaticFrame(55.37f, 770, 21.8f, 22);
				setStaticText(fontArialBold, 8, 60.6f, 783, "lfd.");
				setStaticText(fontArialBold, 8, 60.6f, 773, "Nr.");
				setStaticFrame(77.2f, 770, 32.4f, 22);
				setStaticText(fontArialBold, 8, 78, 778, "Gefahr");
				setStaticText(fontArialBoldCursive, 6, 104, 781, "1");
				setStaticFrame(108.53f, 770, 346.1f, 22);
				setStaticText(fontArialBold, 8, 116, 783, "Gebäude / Anlage / Raum sowie");
				setStaticText(fontArialBold, 8, 116, 773, "Mängelbeschreibung und empfohlene Maßnahmen");
				setStaticFrame(453.87f, 770, 50, 22);
				setStaticText(fontArialBold, 8, 460, 783, "Mangel-");
				setStaticText(fontArialBold, 8, 460, 773, "Nummer");
				setStaticText(fontArialBoldCursive, 6, 494, 776, "2");
				setStaticFrame(503.87f, 770, 50, 22);
				setStaticText(fontArialBold, 8, 511, 783, "Betriebs-");
				setStaticText(fontArialBold, 8, 511, 773, "Bereich");
				setStaticText(fontArialBoldCursive, 6, 541, 776, "2");
				setStaticText(fontArialBoldCursive, 6, 59, 61, "1");
				setStaticText(fontArialCursive, 8, 64, 58,
						"Mängel, die eine Brandgefahr darstellen, werden mit „X“ und Mängel, die eine Personengefahr darstellen, mit „O“ gekennzeichnet");
				setStaticText(fontArialBoldCursive, 6, 59, 51, "2");
				setStaticText(fontArialCursive, 8, 64, 48,
						"Mangelnummer und die Nummern für die Betriebsbereiche sind der VdS-Mängelstatistik (VdS 2837) zu entnehmen");
				setStaticTextVersion(fontArial);
				contentStream.close();
				i++;
				if (i == document.getNumberOfPages()) {
					lastPageOfDocument = true;
				}
			}

		}
	}

	/**
	 * creates appendix table with data
	 * 
	 * @param page
	 * @param fontArial
	 * @return BaseTable
	 * @throws IOException
	 */
	private static BaseTable createAppendixTable(PDPage page, PDFont fontArial) throws IOException {
		float margin = 48f;
		int leftMargin = 55;
		float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
		float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
		boolean drawContent = true;
		float yStart = yStartNewPage + 23.75f;
		float bottomMargin = 55f;
		float yPosition = 677.6f;

		BaseTable table = new BaseTable(yPosition, yStart, bottomMargin, tableWidth, leftMargin, document, page, true,
				drawContent);

		LineStyle BorderStyle = new LineStyle(Color.BLACK, 0.75f);
		LineStyle MiddleStyle = new LineStyle(Color.BLACK, 0.375f);
		int id = 1;

		ArrayList<DefectResult> list = data.getDefects();
		for (DefectResult output : list) {
			Row<PDPage> row = table.createRow(20f);
			Cell<PDPage> cell = row.createCell(4.45f, String.valueOf(id++), HorizontalAlignment.CENTER,
					VerticalAlignment.MIDDLE);
			cell.setBorderStyle(MiddleStyle);
			cell.setLeftBorderStyle(BorderStyle);
			cell.setBottomBorderStyle(BorderStyle);
			cell.setFont(fontArial);
			cell.setFontSize(9);
			cell = row.createCell(6.27f, output.getDangerString(), HorizontalAlignment.CENTER,
					VerticalAlignment.MIDDLE);
			cell.setBorderStyle(MiddleStyle);
			cell.setBottomBorderStyle(BorderStyle);
			cell.setFont(fontArial);
			cell.setFontSize(9);
			cell = row.createCell(
					69.155f, (output.getBuilding()) + "<br />" + output.getRoom() + "<br />" + output.getMachine()
							+ "<br />" + output.getDefectCustomDescription(),
					HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
			cell.setBorderStyle(MiddleStyle);
			cell.setLineSpacing(1.5f);
			cell.setBottomBorderStyle(BorderStyle);
			cell.setFont(fontArial);
			cell.setFontSize(9);
			cell = row.createCell(10.02f, String.valueOf(output.getId()), HorizontalAlignment.CENTER,
					VerticalAlignment.MIDDLE);
			cell.setBorderStyle(MiddleStyle);
			cell.setBottomBorderStyle(BorderStyle);
			cell.setFont(fontArial);
			cell.setFontSize(9);
			cell = row.createCell(10.1f, String.valueOf(output.getBranchId()), HorizontalAlignment.CENTER,
					VerticalAlignment.MIDDLE);
			cell.setBorderStyle(MiddleStyle);
			cell.setRightBorderStyle(BorderStyle);
			cell.setBottomBorderStyle(BorderStyle);
			cell.setFont(fontArial);
			cell.setFontSize(9);
		}
		table.draw();

		return table;
	}

	/**
	 * appends existing pages with page number and total pages
	 * 
	 * @param page
	 * @param fontArialCursive
	 * @param fontArial
	 * @throws IOException
	 */
	private static void useAppendMode(PDPage page, PDFont fontArialCursive, PDFont fontArial) throws IOException {
		PDPageContentStream cos = new PDPageContentStream(document, page, AppendMode.APPEND, true, true);
		cos.beginText();
		cos.setFont(fontArialCursive, 9);
		cos.setNonStrokingColor(Color.BLACK);
		cos.newLineAtOffset(474, 810);
		cos.showText("Blatt-Nr. " + (++pageCounter) + " von " + String.valueOf(document.getNumberOfPages()));
		cos.endText();

		if (page == page1) {
			cos.beginText();
			cos.setFont(fontArial, 9);
			cos.setNonStrokingColor(Color.BLACK);
			cos.newLineAtOffset(340, 208);
			cos.showText(String.valueOf(document.getNumberOfPages()));
			cos.endText();
		}
		cos.close();
	}

	/**
	 * creates the PDFDocument
	 * 
	 * @param fontArial
	 * @param fontArialBold
	 * @param fontArialBoldCursive
	 * @param fontArialCursive
	 * @throws IOException
	 * @throws SQLException
	 */
	private static void createPDF(PDFont fontArial, PDFont fontArialBold, PDFont fontArialBoldCursive,
			PDFont fontArialCursive) throws IOException, SQLException {

		// retrieving the first page
		page1 = new PDPage(PDRectangle.A4);
		document.addPage(page1);
		float paddingP1;

		// creating PDImageXObject object
		PDImageXObject pdImage = PDImageXObject.createFromFile("img/pdf_komplett_72dpi.png", document);

		// creating the PDPageContentStream object
		contentStream = new PDPageContentStream(document, page1);

		// drawing the image in the PDF document
		contentStream.drawImage(pdImage, 0, 692);

		// drawing the small line on the left side of the document
		drawSeparatorLine(0.75f, 0, 542, 15, 542, false);

		setStaticText(fontArialCursive, 9, 513, 682, "Seite - 1 -");

		// textField: topLeft (Befundschein)
		setStaticText(fontArialBoldCursive, 12, 59, 680, "BEFUNDSCHEIN");
		setStaticText(fontArialCursive, 9, 164, 680, "über die Prüfung elektrischer Anlagen gemäß Vorgaben");
		setStaticText(fontArialCursive, 9, 59, 670,
				"der Sachversicherer nach den Prüfrichtlinien VdS 2871 durch VdS-anerkannte");
		setStaticText(fontArialCursive, 9, 59, 660, "Sachverständige");

		/// frame: topTopRight (Befundschein-Nr)
		setStaticFrame(404, 660, 150, 16);
		setStaticText(fontArialCursive, 9, 409, 665, "Befundschein-Nr.:");
		setDatabaseText(fontArial, 9, 495, 665, String.valueOf(data.getId()));

		// frame: topLeft (Versicherungsnehmer)
		setStaticFrame(55.37f, 535, 247, 118);
		setStaticText(fontArialBoldCursive, 9, 59, 642, "Versicherungsnehmer (VN)");
		setDatabaseText(fontArial, 9, 65, 620, data.getCompanyPlant().getCompany().getName());
		setDatabaseText(fontArial, 9, 65, 605, data.getCompanyPlant().getPlantStreet());
		setDatabaseText(fontArial, 9, 65, 575, String.valueOf(data.getCompanyPlant().getPlantZip()));
		setDatabaseText(fontArial, 9, 95, 575, data.getCompanyPlant().getPlantCity());

		// frame: topRight (Risikoanschrift)
		setStaticFrame(307, 535, 247, 118);
		setStaticText(fontArialBoldCursive, 9, 313, 642, "Risikoanschrift: ");
		setDatabaseText(fontArial, 9, 313, 630, String.valueOf(data.getCompanyPlant().getCompany().getHqZip()));
		setDatabaseText(fontArial, 9, 343, 630, data.getCompanyPlant().getCompany().getHqCity());
		setDatabaseText(fontArial, 9, 313, 618, data.getCompanyPlant().getCompany().getHqStreet());
		setStaticText(fontArialCursive, 9, 313, 603, "Begleiter vom VN: ");
		setDatabaseText(fontArial, 9, 400, 603, data.getCompanion());
		setStaticText(fontArialCursive, 9, 313, 588, "Sachverständiger: ");
		setDatabaseText(fontArial, 9, 400, 588, data.getSurveyor());
		setStaticText(fontArialCursive, 9, 313, 573, "VdS-Anerk.-Nr.: ");
		setDatabaseText(fontArial, 9, 400, 573, String.valueOf(data.getVdsApprovalNr()));
		setStaticText(fontArialCursive, 9, 313, 558, "Datum der Prüfung: ");
		setDatabaseText(fontArial, 9, 400, 558, data.getNiceDate());
		setStaticText(fontArialCursive, 9, 313, 543, "Prüfungsdauer: ");
		setDatabaseText(fontArial, 9, 400, 543, String.valueOf(data.getExaminationDuration()));
		setStaticText(fontArialCursive, 9, 463, 543, "Std.");
		setStaticText(fontArialCursive, 8, 483, 543, "(reine Prüfzeit)");

		// frame: topMiddle (Art des Betriebes oder der Anlage)
		setStaticText(fontArialBoldCursive, 12, 59, 517, "Art des Betriebes oder der Anlage");
		setDatabaseText(fontArial, 9, 59, 505, data.getDangerCategoryDescription());
		setStaticText(fontArialCursive, 9, 59, 492,
				"Sind frequenzgesteuerte Betriebsmittel (z. B. Motoren) in der elektrischen Anlage installiert?");
		setTwoCheckboxes(data.isFrequencyControlledUtilities(), 463, 503, 492);
		setStaticText(fontArialCursive, 9, 475, 492, "ja");
		setStaticText(fontArialCursive, 9, 515, 492, "nein");
		setStaticText(fontArialCursive, 9, 59, 479,
				"Sind Bereiche , die besondere Schutzmaßnahmen erfordern, durch den Betreiber ausgewiesen?");
		setStaticText(fontArialBoldCursive, 6, 115, 482, "1");
		setTwoCheckboxes(data.isPrecautionsDeclared(), 463, 503, 479);
		setStaticText(fontArialCursive, 9, 475, 479, "ja");
		setStaticText(fontArialCursive, 9, 515, 479, "nein");
		setStaticText(fontArialCursive, 9, 59, 466, "Wenn ja, welche:");
		float paddingPrecautions = setDynamicText(page1, fontArial, 9, 1.25f, 370f, 132f, 466f,
				data.getPrecautionsDeclaredLocation());
		paddingP1 = paddingPrecautions;
		setStaticText(fontArialCursive, 9, 59, 450 - paddingP1, "Wurden alle Bereiche des");
		setStaticText(fontArialCursive, 9, 59, 437 - paddingP1, "Risikostandorts geprüft?");
		setTwoCheckboxes(data.isExaminationComplete(), 177, 217, 437 - paddingP1);
		setStaticText(fontArialCursive, 9, 190, 437 - paddingP1, "ja");
		setStaticText(fontArialCursive, 9, 230, 437 - paddingP1, "nein - Nachbesichtigung (<6 Wo) vereinbart bis zum:");
		setDatabaseText(fontArial, 9, 450, 437 - paddingP1, data.getSubsequentExaminationDateNice());
		setStaticText(fontArialCursive, 6, 505, 437 - paddingP1, "(Datum)");
		setStaticText(fontArialCursive, 9, 59, 419 - paddingP1, "Begründung für nicht geprüfte Bereiche:");
		setDatabaseText(fontArial, 9, 225, 419 - paddingP1, data.getExaminationIncompleteReason());
		setStaticText(fontArialCursive, 9, 59, 402 - paddingP1,
				"Wurden nach Aussagen des Betreibers Teilbereiche der Anlage seit der letzten Revision erneuert, erweitert oder");
		setStaticText(fontArialCursive, 9, 59, 389 - paddingP1, "umgebaut (entfällt bei Erstprüfung)?");
		setThreeCheckboxes(data.getChangesSinceLastExamination(), 337, 417, 457, 389 - paddingP1);
		setStaticText(fontArialCursive, 9, 350, 389 - paddingP1, "Erstprüfung");
		setStaticText(fontArialCursive, 9, 430, 389 - paddingP1, "ja");
		setStaticText(fontArialCursive, 9, 470, 389 - paddingP1, "nein");
		setStaticText(fontArialCursive, 9, 59, 376 - paddingP1,
				"Wurden alle Mängel der vorhergehenden Revision beseitigt?");
		setThreeCheckboxes(data.getDefectsLastExaminationFixed(), 337, 417, 457, 376 - paddingP1);
		setStaticText(fontArialCursive, 9, 350, 376 - paddingP1, "Bericht fehlt");
		setStaticText(fontArialCursive, 9, 430, 376 - paddingP1, "ja");
		setStaticText(fontArialCursive, 9, 470, 376 - paddingP1, "nein");
		drawSingleCellTable(page1, 530.5f, 163f + paddingP1, 100f);

		// frame: middleMiddle (Gesamtbeurteilung der Anlage)
		setStaticText(fontArialBoldCursive, 12, 59, 349 - paddingP1, "Gesamtbeurteilung der Anlage");
		setStaticText(fontArialCursive, 9, 59, 337 - paddingP1, "Gefährdungskategorie gemäß Prüfrichtlinien VdS 2871");
		setFourCheckboxes(data.getDangerCategory(), 312, 367, 422, 477, 337 - paddingP1);
		setStaticText(fontArialBoldCursive, 10, 295, 337 - paddingP1, "(a)");
		setStaticText(fontArialBoldCursive, 10, 350, 337 - paddingP1, "(b)");
		setStaticText(fontArialBoldCursive, 10, 405, 337 - paddingP1, "(c)");
		setStaticText(fontArialBoldCursive, 10, 460, 337 - paddingP1, "(d)");
		setStaticText(fontArialCursive, 9, 59, 324 - paddingP1, "Ergänzende Erläuterungen:");
		float paddingDangerCategory = setDynamicText(page1, fontArial, 9, 1.25f, 370f, 175f, 324f - paddingP1,
				data.getDangerCategoryDescription());
		paddingP1 += paddingDangerCategory;
		drawSingleCellTable(page1, 363.5f - paddingPrecautions, 48f + paddingDangerCategory, 100f);

		// frame: bottomMiddle (Prüfungsergebnis)
		drawSingleCellTable(page1, 311.5f - paddingP1, 207f, 100f);
		setStaticText(fontArialBoldCursive, 12, 59, 296 - paddingP1, "Prüfungsergebnis");
		setStaticText(fontArialCursive, 11, 164, 296 - paddingP1, "(Einzelergebnisse)");
		setOneCheckbox(data.isExaminationResultNoDefect(), 59, 284 - paddingP1);
		setStaticText(fontArialCursive, 9, 77, 284 - paddingP1, "Keinen Mangel gestgestellt");
		setOneCheckbox(data.isExaminationResultDefect(), 59, 272 - paddingP1);
		setStaticText(fontArialCursive, 9, 77, 272 - paddingP1, "Die festgestellten Mängel sind im");
		setStaticText(fontArialBoldCursive, 9, 212, 272 - paddingP1, "Anhang A");
		setStaticText(fontArialCursive, 9, 258, 272 - paddingP1, "aufgeführt und spätestens zu beseitigen bis:");
		setDatabaseText(fontArial, 9, 450, 272 - paddingP1, data.getExaminationResultDefectDateNice());
		setOneCheckbox(data.isExaminationResultDanger(), 59, 248 - paddingP1);
		setStaticText(fontArialBoldCursive, 9, 77, 260 - paddingP1,
				"Es wurden Mängel festgestellt, die eine Brandgefahr (mit „X“ gekennzeichnet) bzw. eine Unfallgefahr (mit „O“");
		setStaticText(fontArialBoldCursive, 9, 77, 247 - paddingP1,
				"gekennzeichnet) hervorrufen können. Diese Mängel sind unverzüglich zu beseitigen");
		setStaticText(fontArialCursive, 9, 436, 247 - paddingP1, "! (Der Mangel ist dann mit");
		setStaticText(fontArialCursive, 9, 77, 235 - paddingP1,
				"einem X oder O zu kennzeichnen, wenn er im Extremfall, aber bei sonst normalen Betriebsbedingungen zu einem");
		setStaticText(fontArialCursive, 9, 77, 223 - paddingP1, "Brand bzw. zu einem Personenschaden führen kann.)");
		setStaticText(fontArialCursive, 9, 59, 208 - paddingP1,
				"Dieser Befundschein besteht einschließlich des Anhangs aus");
		setStaticText(fontArialCursive, 9, 380, 208 - paddingP1, "Seiten.");
		setStaticText(fontArialCursive, 9, 59, 193 - paddingP1,
				"Die elektrische(n) Anlage(n) wurde(n) gemäß den Prüfrichtlinien VdS 2871 nach bestem Wissen und Gewissen geprüft. Bei");
		setStaticText(fontArialCursive, 9, 59, 181 - paddingP1,
				"den nicht im Anhang dieses Befundscheins aufgeführten Anlagenteilen und Bereichen wurden keine Mängel festgestellt.");
		setStaticText(fontArialCursive, 9, 100, 108 - paddingP1, "Firmenstempel");
		setStaticText(fontArialCursive, 9, 250, 108 - paddingP1,
				"Datum und Unterschrift des VdS-anerkannten Sachverständigen");

		// footnotes
		setStaticText(fontArialBoldCursive, 6, 59, 97 - paddingP1, "1");
		setStaticText(fontArialCursive, 8, 64, 94 - paddingP1,
				"das sind z. B. Betriebsstätten nach VdS 2033 / Ex-Bereiche / stationäre Stromerzeugungsanlagen / Ladestationen für Fahrzeuge und");
		setStaticText(fontArialCursive, 8, 64, 84 - paddingP1, "Flurförderzeuge");

		// documentVersion
		setStaticTextVersion(fontArial);

		// closing the contentStream
		contentStream.close();

		// retrieving the second page
		PDPage page2 = new PDPage(PDRectangle.A4);
		document.addPage(page2);
		float paddingP2;

		// creating the PDPageContentStream object
		contentStream = new PDPageContentStream(document, page2);

		// frame: header
		setStaticFrame(55.37f, 779.63f, 498.5f, 26);
		setStaticText(fontArialCursive, 9, 513, 798, "Seite - 2 -");
		setStaticFrame(406, 779.63f, 147.9f, 16f);
		setStaticText(fontArialCursive, 9, 409, 785, "Befundschein-Nr.:");
		setDatabaseText(fontArial, 9, 495, 785, String.valueOf(data.getId()));

		// frame: top (Messungen)
		setStaticText(fontArialBoldCursive, 12, 59, 764, "Messungen");
		setStaticText(fontArialBoldCursive, 9, 59, 750, "• Isolationswiderstand:");
		setStaticText(fontArialCursive, 9, 160, 750, "Messung in mind. 50 % aller Stromkreise");
		setTwoCheckboxes(data.isIsolationChecked(), 353, 463, 750);
		setStaticText(fontArialCursive, 9, 365, 750, "ja");
		setStaticText(fontArialCursive, 9, 475, 750, "nein");
		setStaticText(fontArialCursive, 9, 64, 738,
				"Wenn Isolationswiderstandsmessungen nicht möglich sind: Lagen er-");
		setStaticText(fontArialCursive, 9, 64, 728,
				"satzweise Messprotokolle über Isolationswiderstandsmessungen vor?");
		setTwoCheckboxes(data.isIsolationMesasurementProtocols(), 353, 463, 738);
		setStaticText(fontArialCursive, 9, 365, 738, "ja");
		setStaticText(fontArialCursive, 9, 475, 738, "nein");
		setStaticText(fontArialBoldCursive, 6, 493, 741, "2");
		setStaticText(fontArialCursive, 9, 64, 716, "Wenn nein: Sind Ersatzmaßnahmen nach Aussage des Betreibers");
		setStaticText(fontArialCursive, 9, 64, 706, "vorhanden?");
		setTwoCheckboxes(data.isIsolationCompensationMeasures(), 353, 463, 716);
		setStaticText(fontArialCursive, 9, 365, 716, "ja");
		setStaticText(fontArialCursive, 9, 475, 716, "nein");
		setStaticText(fontArialCursive, 9, 64, 694, "Bemerkung hierzu:");
		float paddingIsolation = setDynamicText(page2, fontArial, 9, 1.25f, 400f, 142, 694,
				data.getIsolationCompensationMeasuresAnnotation());
		paddingP2 = paddingIsolation;
		drawSeparatorLine(0.75f, 55.5f, 688f - paddingP2, 554f, 688f - paddingP2, true);
		setStaticText(fontArialBoldCursive, 9, 59, 676 - paddingP2, "• Fehlerstrom-Schutzeinrichtungen (RCDs)");
		setTwoCheckboxes(data.getRcdAvailable(), 353, 463, 676 - paddingP2);
		setStaticText(fontArialCursive, 9, 365, 676 - paddingP2, "alle oder");
		setDatabaseText(fontArial, 9, 424, 676 - paddingP2, String.valueOf(data.getRcdAvailablePercent()));
		setStaticText(fontArialCursive, 9, 439, 676 - paddingP2, "%");
		setStaticText(fontArialCursive, 9, 475, 676 - paddingP2, "nein");
		setStaticText(fontArialBoldCursive, 6, 493, 679 - paddingP2, "3");
		setStaticText(fontArialCursive, 9, 64, 664 - paddingP2, "Bemerkung hierzu:");
		float paddingRcd = setDynamicText(page2, fontArial, 9, 1.25f, 400f, 142, 664f - paddingP2,
				data.getRcdAnnotation());
		paddingP2 += paddingRcd;
		drawSeparatorLine(0.75f, 55.5f, 658f - paddingP2, 554f, 658f - paddingP2, true);
		setStaticText(fontArialBoldCursive, 9, 59, 646 - paddingP2, "• Schleifenwiderstand");
		setTwoCheckboxes(data.isResistance(), 353, 463, 646 - paddingP2);
		setStaticText(fontArialCursive, 9, 365, 646 - paddingP2, "ja, Anzahl  :");
		setStaticText(fontArialBoldCursive, 6, 406, 649 - paddingP2, "4");
		setDatabaseText(fontArial, 9, 424, 646 - paddingP2, String.valueOf(data.getResistanceNumber()));
		setStaticText(fontArialCursive, 9, 439, 646 - paddingP2, "%");
		setStaticText(fontArialCursive, 9, 475, 646 - paddingP2, "nein");
		setStaticText(fontArialBoldCursive, 6, 493, 649 - paddingP2, "3");
		setStaticText(fontArialCursive, 9, 64, 634 - paddingP2, "Bemerkung hierzu:");
		float paddingResistance = setDynamicText(page2, fontArial, 9, 1.25f, 400f, 142, 634f - paddingP2,
				data.getResistanceAnnotation());
		paddingP2 += paddingResistance;
		drawSeparatorLine(0.75f, 55.5f, 628f - paddingP2, 554f, 628f - paddingP2, true);
		setStaticText(fontArialBoldCursive, 9, 59, 616 - paddingP2,
				"• Wurden thermische Auffälligkeiten messtechnisch vorgefunden?");
		setTwoCheckboxes(data.isThermalAbnormality(), 353, 463, 616 - paddingP2);
		setStaticText(fontArialCursive, 9, 365, 616 - paddingP2, "ja");
		setStaticText(fontArialCursive, 9, 475, 616 - paddingP2, "nein");
		setStaticText(fontArialCursive, 9, 64, 604 - paddingP2, "Bemerkung hierzu:");
		float paddingThermal = setDynamicText(page2, fontArial, 9, 1.25f, 400f, 142, 604f - paddingP2,
				data.getThermalAbnormalityAnnotation());
		paddingP2 += paddingThermal;
		drawSingleCellTable(page2, 780f, 182f + paddingP2, 100f);

		// frame: middle (Ortsveränderliche Betriebsmittel)
		setStaticText(fontArialBoldCursive, 12, 59, 586 - paddingP2, "Ortsveränderliche Betriebsmittel");
		setStaticText(fontArialCursive, 9, 59, 572 - paddingP2,
				"Werden nach Aussage des Betreibers die ortsveränderlichen");
		setStaticText(fontArialCursive, 9, 59, 562 - paddingP2,
				"Betriebsmittel regelmäßig geprüft (z. B. gemäß BetrSichV oder");
		setStaticText(fontArialCursive, 9, 59, 552 - paddingP2, "BGV A3)?");
		setTwoCheckboxes(data.isInternalPortableUtilities(), 353, 463, 562 - paddingP2);
		setStaticText(fontArialCursive, 9, 365, 562 - paddingP2, "ja");
		setStaticText(fontArialCursive, 9, 475, 562 - paddingP2, "nein");
		drawSingleCellTable(page2, 598.75f - paddingP2, 50f, 100f);
		setStaticText(fontArialCursive, 9, 59, 539 - paddingP2,
				"Beinhalten diese Prüfungen nach Aussage des Betreibers auch fremde");
		setStaticText(fontArialCursive, 9, 59, 529 - paddingP2,
				"ortsveränderliche Betriebsmittel, d. h. privat mitgebrachte, gemietete,");
		setStaticText(fontArialCursive, 9, 59, 519 - paddingP2,
				"geleaste oder durch Dritte bereitgestellte Betriebsmittel?");
		setThreeCheckboxes(data.getExternalPortableUtilities(), 353, 423, 493, 529 - paddingP2);
		setStaticText(fontArialCursive, 9, 365, 529 - paddingP2, "ja");
		setStaticText(fontArialCursive, 9, 435, 529 - paddingP2, "nein");
		setStaticText(fontArialCursive, 9, 505, 529 - paddingP2, "nr");
		setStaticText(fontArialBoldCursive, 6, 515, 532 - paddingP2, "5");
		drawSingleCellTable(page2, 549.5f - paddingP2, 35f, 100f);

		// frame: bottom (Allgemeine Informationen zur geprüften elektrischen Anlage)
		setStaticText(fontArialBoldCursive, 12, 59, 502 - paddingP2,
				"Allgemeine Informationen zur geprüften elektrischen Anlage");
		drawSingleCellTable(page2, 515.25f - paddingP2, 17.5f, 100f);
		setStaticText(fontArialBoldCursive, 9, 59, 488 - paddingP2, "Versorgungssystem");
		setFourCheckboxes(data.getSupplySystem(), 59, 124, 194, 379, 474 - paddingP2);
		setStaticText(fontArialCursive, 9, 71, 474 - paddingP2, "TN");
		setStaticText(fontArialCursive, 9, 136, 474 - paddingP2, "TT");
		setStaticText(fontArialCursive, 9, 206, 474 - paddingP2, "IT");
		setStaticText(fontArialCursive, 9, 391, 474 - paddingP2, "Ringeinspeisung");
		setStaticText(fontArialBoldCursive, 9, 59, 458 - paddingP2, "Leistungsbedarf der Gesamtanlage:");
		setDatabaseText(fontArial, 9, 240, 458 - paddingP2, String.valueOf(data.getEnergyDemand()));
		setStaticText(fontArial, 9, 268, 458 - paddingP2, "kVA");
		setStaticText(fontArialCursive, 9, 59, 442 - paddingP2,
				"Maximal möglicher Fremdbezug in %, bezogen auf o. g. Gesamtleistungsbedarf");
		setDatabaseText(fontArial, 9, 490, 442 - paddingP2, String.valueOf(data.getMaxEnergyDemandExternal()));
		setStaticText(fontArial, 9, 530, 442 - paddingP2, "%");
		setStaticText(fontArialCursive, 9, 59, 428 - paddingP2,
				"Maximal mögliche Eigenerzeugung (ohne Ersatzstrom) in %, bezogen auf o. g.");
		setDatabaseText(fontArial, 9, 490, 428 - paddingP2, String.valueOf(data.getMaxEnergyDemandInternal()));
		setStaticText(fontArial, 9, 530, 428 - paddingP2, "%");
		setStaticText(fontArialCursive, 9, 59, 418 - paddingP2, "Gesamtleistungsbedarf");
		setStaticText(fontArialBoldCursive, 9, 59, 398 - paddingP2, "Schutzeinrichtung (RCD / RCM):");
		setStaticText(fontArialCursive, 9, 59, 382 - paddingP2, "Fehlerstrom-Schutzeinrichtung (RCD)");
		setStaticText(fontArialCursive, 9, 59, 372 - paddingP2, "oder Differenzstrom-Überwachung (RCM)");
		setStaticText(fontArialCursive, 9, 250, 377 - paddingP2, "geschützte Stromkreise in %:");
		setDatabaseText(fontArial, 9, 380, 377 - paddingP2, String.valueOf(data.getProtectedCircuitsPercent()));
		drawSingleCellTable(page2, 498.5f - paddingP2, 140f, 100f);

		// frame: singleFrame (Für statische Zwecke)
		setStaticText(fontArialBoldCursive, 9, 59, 343 - paddingP2, "Für statistische Zwecke");
		setStaticText(fontArialCursive, 9, 59, 328 - paddingP2,
				"Geschätzte Anzahl der fest angeschlossenen Verbraucher in der elektrischen Anlage:");
		setFiveCheckboxes(data.getHardWiredLoads(), 59, 149, 239, 329, 419, 313 - paddingP2);
		setStaticText(fontArialCursive, 9, 71, 313 - paddingP2, "<= 250");
		setStaticText(fontArialCursive, 9, 161, 313 - paddingP2, "<= 500");
		setStaticText(fontArialCursive, 9, 251, 313 - paddingP2, "<= 1.000");
		setStaticText(fontArialCursive, 9, 341, 313 - paddingP2, "<= 5.000");
		setStaticText(fontArialCursive, 9, 431, 313 - paddingP2, "> 5.000");
		drawSingleCellTable(page2, 353f - paddingP2, 45f, 100f);

		// footnotes
		setStaticText(fontArialBoldCursive, 6, 59, 301 - paddingP2, "2");
		setStaticText(fontArialCursive, 8, 64, 298 - paddingP2,
				"Können keine Isolationswiderstandsmessungen durchgeführt werden und sind keine Messprotokolle vorhanden, ist dies");
		setStaticText(fontArialCursive, 8, 64, 288 - paddingP2,
				"als Mangel im Anhang (Mängelliste) zu vermerken. Hat der Betreiber nach eigenen Angaben Ersatzmaßnahmen");
		setStaticText(fontArialCursive, 8, 64, 278 - paddingP2, "vorgesehen, sind diese zu notieren.");
		setStaticText(fontArialBoldCursive, 6, 59, 271 - paddingP2, "3");
		setStaticText(fontArialCursive, 8, 64, 268 - paddingP2,
				"Bitte oben im Feld hinter den Worten „Bemerkung hierzu:“ eine Begründung angeben, z. B. wenn Messungen kaum oder");
		setStaticText(fontArialCursive, 8, 64, 258 - paddingP2,
				"überhaupt nicht durchgeführt werden konnten. Bitte ebenfalls dort notieren, wenn Messungen nicht notwendig waren, weil");
		setStaticText(fontArialCursive, 8, 64, 248 - paddingP2, "z. B. Messprotokolle aus anderen Prüfungen vorlagen.");
		setStaticText(fontArialBoldCursive, 6, 59, 241 - paddingP2, "4");
		setStaticText(fontArialCursive, 8, 64, 238 - paddingP2, "Angabe in der Regel in Prozenten.");
		setStaticText(fontArialBoldCursive, 6, 59, 231 - paddingP2, "5");
		setStaticText(fontArialCursive, 8, 64, 228 - paddingP2,
				"nr = nicht relevant, da nach Aussagen des Betreibers keine fremden ortsveränderlichen Betriebsmittel vorhanden sind.");

		// additionalAnnotations
		setStaticText(fontArialBoldCursive, 9, 59, 208 - paddingP2,
				"Weitere Erläuterungen wie z. B. verwendete Messgeräte (optional):");
		setDynamicText(page2, fontArial, 10, 1.25f, 490f, 59, 193 - paddingP2, data.getAdditionalAnnotations());

		// documentVersion
		setStaticTextVersion(fontArial);

		// closing the contentStream
		contentStream.close();

		// retrieving the appendix page
		PDPage page = new PDPage(PDRectangle.A4);
		document.addPage(page);

		// creating the PDPageContentStream object
		contentStream = new PDPageContentStream(document, page);

		// frame: header
		setStaticFrame(55.37f, 755, 498.5f, 35);
		setStaticText(fontArialBoldCursive, 12, 59, 768, "Anhang A zum Befundschein-Nr.:");
		setDatabaseText(fontArialCursive, 10, 280, 768, String.valueOf(data.getId()));
		setStaticFrame(55.37f, 700, 498.5f, 55);
		setStaticText(fontArialBoldCursive, 9, 59, 745, "Allgemeine Bemerkungen");
		setStaticText(fontArialCursive, 9, 59, 735,
				"Wenn in der elektrischen Anlage z. B. aus betrieblichen Gründen keine oder nicht im ausreichenden Umfang Isolations-");
		setStaticText(fontArialCursive, 9, 59, 725,
				"widerstandsmessungen durchgeführt werden können, wird dringend empfohlen, nach VdS 2349 für eine konstante Iso-");
		setStaticText(fontArialCursive, 9, 59, 715,
				"lationsüberwachung zu sorgen. In Einzelfällen kann der Sachversicherer auch ergänzende oder alternative Maßnahmen");
		setStaticText(fontArialCursive, 9, 59, 705,
				"fordern. Aus der Sicht des Sachversicherers kann dies auch eine thermografische Untersuchung sein.");

		// frame: headRow
		setStaticFrame(55.37f, 678, 21.8f, 22);
		setStaticText(fontArialBold, 8, 60.6f, 691, "lfd.");
		setStaticText(fontArialBold, 8, 60.6f, 681, "Nr.");
		setStaticFrame(77.2f, 678, 32.4f, 22);
		setStaticText(fontArialBold, 8, 78f, 686, "Gefahr");
		setStaticText(fontArialBoldCursive, 6, 104f, 689, "1");
		setStaticFrame(108.53f, 678, 346.1f, 22);
		setStaticText(fontArialBold, 8, 116, 691, "Gebäude / Anlage / Raum sowie");
		setStaticText(fontArialBold, 8, 116, 681, "Mängelbeschreibung und empfohlene Maßnahmen");
		setStaticFrame(453.87f, 678, 50, 22);
		setStaticText(fontArialBold, 8, 460, 691, "Mangel-");
		setStaticText(fontArialBold, 8, 460, 681, "Nummer");
		setStaticText(fontArialBoldCursive, 6, 494, 684, "2");
		setStaticFrame(503.87f, 678, 50, 22);
		setStaticText(fontArialBold, 8, 511, 691, "Betriebs-");
		setStaticText(fontArialBold, 8, 511, 681, "Bereich");
		setStaticText(fontArialBoldCursive, 6, 541, 684, "2");

		// appendixTable
		BaseTable table = createAppendixTable(page, fontArial);

		// footnotes
		setStaticText(fontArialBoldCursive, 6, 59, 61, "1");
		setStaticText(fontArialCursive, 8, 64, 58,
				"Mängel, die eine Brandgefahr darstellen, werden mit „X“ und Mängel, die eine Personengefahr darstellen, mit „O“ gekennzeichnet");
		setStaticText(fontArialBoldCursive, 6, 59, 51, "2");
		setStaticText(fontArialCursive, 8, 64, 48,
				"Mangelnummer und die Nummern für die Betriebsbereiche sind der VdS-Mängelstatistik (VdS 2837) zu entnehmen");
		setStaticTextVersion(fontArial);

		// closing the contentStream
		contentStream.close();

		// appending page number of total pages
		useAppendMode(page1, fontArialCursive, fontArial);
		useAppendMode(page2, fontArialCursive, fontArial);
		useAppendMode(page, fontArialCursive, fontArial);

		// dynamicAppendixPages
		createTemplateLastPages(page, table, fontArial, fontArialBold, fontArialBoldCursive, fontArialCursive);
	}
}

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
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.line.LineStyle;
import dataStorageAccess.controller.DiagnosisController;

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

	/**
	 * Creates new PDFDocument, sets the Fonts, calls createPDF method for
	 * content and saves the File as PDF
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void export() throws IOException, SQLException {
		try {
			// Creating PDF document object
			document = new PDDocument();

			// Setting Fonts
			PDFont fontArial = PDType0Font.load(document, arial);
			PDFont fontArialBold = PDType0Font.load(document, arialBold);
			PDFont fontArialBoldCursive = PDType0Font.load(document, arialBoldCursive);
			PDFont fontArialCursive = PDType0Font.load(document, arialCursive);

			// erstellt die eigentliche PDF-Datei, Methode ganz unten
			createPDF(fontArial, fontArialBold, fontArialBoldCursive, fontArialCursive);

			// Saving the document
			// sm. "Save as..."
			document.save("pdf/pfeifer.pdf");
			System.out.println("Document saved");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Closing the document
			if (document != null) {
				document.close();
			}
		}
	}

	/**
	 * bla
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
	public static float setDynamicText(PDPage page, PDFont pdfFont, float fontSize, float leading, float width,
			float startX, float startY, String content, boolean exceptSecondLine) throws IOException {

		leading = leading * fontSize;
		int loopCounterDynamicText = 1;

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

		// defines output
		contentStream.beginText();
		contentStream.setFont(pdfFont, fontSize);
		contentStream.newLineAtOffset(startX, startY);
		for (String line : lines) {
			contentStream.showText(line);
			contentStream.newLineAtOffset(0, -leading);
		}
		contentStream.endText();

		// defines padding 
		boolean stringRuns = true;
		int counter = 1;
		float paddingOfString = 0;
		if (!exceptSecondLine) {
			while (stringRuns) {
				if (loopCounterDynamicText != counter) {
					paddingOfString += 11f;
				} else if (loopCounterDynamicText == counter) {
					stringRuns = false;
				}
				counter++;
			}
		} else {
			if (loopCounterDynamicText > 2) {
				paddingOfString = 11f;
			} else {
				paddingOfString = 0;
			}
		}
		return paddingOfString;
	}

	/**
	 * bla
	 * 
	 * @param page
	 * @param yPosition
	 * @param cellHeight
	 * @param cellWidth
	 * @throws IOException
	 */
	public static void drawSingleCellTable(PDPage page, float yPosition, float cellHeight, float cellWidth)
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
	 * creates new static Texts (no user data)
	 * 
	 * @param font
	 * @param size
	 * @param positionX
	 * @param positionY
	 * @param content
	 * @throws IOException
	 */
	public static void setStaticText(PDFont font, int size, float positionX, float positionY, String content)
			throws IOException {
		contentStream.beginText();
		contentStream.setFont(font, size);
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.newLineAtOffset(positionX, positionY);
		contentStream.showText(content);
		contentStream.endText();
	}

	/**
	 * bla
	 * 
	 * @param font
	 * @param size
	 * @param positionX
	 * @param positionY
	 * @param content
	 * @throws IOException
	 */
	public static void setDatabaseText(PDFont font, int size, float positionX, float positionY, String content)
			throws IOException {
		contentStream.beginText();
		contentStream.setFont(font, size);
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.newLineAtOffset(positionX, positionY);
		if (content != null && !content.isEmpty()) {
			contentStream.showText(content);

		} else {
			contentStream.showText("#WERT");
		}
		contentStream.endText();
	}

	/**
	 * creates new Frame
	 * 
	 * @param positionX
	 * @param posotionY
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	public static void setStaticFrame(float positionX, float positionY, float width, float height) throws IOException {
		contentStream.setStrokingColor(Color.BLACK);
		contentStream.setNonStrokingColor(Color.WHITE);
		contentStream.setLineWidth(0.75f);
		contentStream.setLineDashPattern(new float[0], 0);
		contentStream.addRect(positionX, positionY, width, height);
		contentStream.closeAndFillAndStrokeEvenOdd();
	}

	/**
	 * sets new unchecked Checkbox
	 * 
	 * @param posistionX
	 * @param positionY
	 * @throws IOException
	 */
	public static void setCheckboxUnchecked(float positionX, float positionY) throws IOException {
		contentStream.setStrokingColor(Color.BLACK);
		contentStream.setNonStrokingColor(Color.WHITE);
		contentStream.setLineWidth(0.75f);
		contentStream.setLineDashPattern(new float[0], 0);
		contentStream.addRect(positionX, positionY, 7, 7);
		contentStream.closeAndFillAndStrokeEvenOdd();
	}

	/**
	 * sets new checked Checkbox
	 * 
	 * @param posistionX
	 * @param positionY
	 * @throws IOException
	 */
	public static void setCheckboxChecked(float posistionX, float positionY) throws IOException {
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
	 * bla
	 * 
	 * @param isTrue
	 * @param positionVertical
	 * @param positionHorizontal
	 * @throws IOException
	 */
	public static void setOneCheckbox(boolean isTrue, float positionVertical, float positionHorizontal)
			throws IOException {
		if (isTrue) {
			setCheckboxChecked(positionVertical, positionHorizontal);
		} else {
			setCheckboxUnchecked(positionVertical, positionHorizontal);
		}
	}

	/**
	 * bla
	 * 
	 * @param isTrue
	 * @param positionLeft
	 * @param positionRight
	 * @param heightPosition
	 * @throws IOException
	 */
	public static void setTwoCheckboxes(boolean isTrue, float positionLeft, float positionRight, float heightPosition)
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
	 * bla
	 * 
	 * @param getStatus
	 * @param positionLeft
	 * @param positionMiddle
	 * @param positionRight
	 * @param heightPosition
	 * @throws IOException
	 */
	public static void setThreeCheckboxes(int getStatus, float positionLeft, float positionMiddle, float positionRight,
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
	 * bla
	 * 
	 * @param getStatus
	 * @param position1
	 * @param position2
	 * @param position3
	 * @param position4
	 * @param heightPosition
	 * @throws IOException
	 */
	public static void setFourCheckboxes(int getStatus, float position1, float position2, float position3,
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
	public static void setFiveCheckboxes(int getStatus, float position1, float position2, float position3,
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
	 * bla
	 * 
	 * @param lineWidth
	 * @param startPositionX
	 * @param startPositionY
	 * @param endPositionX
	 * @param endPositionY
	 * @param dotted
	 * @throws IOException
	 */
	public static void drawPartingLine(float lineWidth, float startPositionX, float startPositionY, float endPositionX,
			float endPositionY, boolean dotted) throws IOException {
		contentStream.setLineWidth(lineWidth);
		contentStream.moveTo(startPositionX, startPositionY);
		contentStream.lineTo(endPositionX, endPositionY);
		if (dotted) {
			contentStream.setLineDashPattern(new float[] { 2.5f }, 0);
		}
		contentStream.stroke();
	}

	/**
	 * bla
	 * 
	 * @param font
	 * @param actPage
	 * @param totalPages
	 * @throws IOException
	 */
	public static void setConstTextPageOf(PDFont font, int actPage, int totalPages) throws IOException {
		contentStream.beginText();
		contentStream.setFont(font, 9);
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.newLineAtOffset(474, 810);
		contentStream.showText("Blatt-Nr. " + actPage + " von " + totalPages);
		contentStream.endText();
	}

	/**
	 * bla
	 * 
	 * @param font
	 * @throws IOException
	 */
	public static void setConstTextVersion(PDFont font) throws IOException {
		contentStream.beginText();
		contentStream.setFont(font, 8);
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.newLineAtOffset(59, 25);
		contentStream.showText("VdS 2229 : 2017-12 (01)");
		contentStream.endText();
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
	public static void createPDF(PDFont fontArial, PDFont fontArialBold, PDFont fontArialBoldCursive,
			PDFont fontArialCursive) throws IOException, SQLException {

		//// LoadData /////
		// sm. Diagnosis muss noch richtig geladen werden
		Diagnosis data = DiagnosisController.getDiagnosis(1);
		// System.out.println("Diagnois : " +
		// data.isFrequencyControlledUtilities());

		/////// PAGE 1 ///////
		// Retrieving the first page
		PDPage page1 = new PDPage(PDRectangle.A4);
		document.addPage(page1);
		float paddingP1;

		// Creating PDImageXObject object
		PDImageXObject pdImage = PDImageXObject.createFromFile("img/pdf_komplett_72dpi.png", document);

		// Creating the PDPageContentStream object
		contentStream = new PDPageContentStream(document, page1);

		// Drawing the image in the PDF document
		contentStream.drawImage(pdImage, 0, 692);

		// Drawing the small line on the left side of the document
		drawPartingLine(0.75f, 0, 542, 15, 542, false);

		// PageOfTotalPages (top) and DocumentVersion (bottom)
		setConstTextPageOf(fontArialCursive, 1, 3);
		setConstTextVersion(fontArial);

		// PageCounter, only page 1 and page 2
		setStaticText(fontArialCursive, 9, 513, 682, "Seite - 1 -");

		// TextField: topLeft (Befundschein)
		setStaticText(fontArialBoldCursive, 12, 59, 680, "BEFUNDSCHEIN");
		setStaticText(fontArialCursive, 9, 164, 680, "über die Prüfung elektrischer Anlagen gemäß Vorgaben");
		setStaticText(fontArialCursive, 9, 59, 670,
				"der Sachversicherer nach den Prüfrichtlinien VdS 2871 durch VdS-anerkannte");
		setStaticText(fontArialCursive, 9, 59, 660, "Sachverständige");

		/// Frame: topTopRight (Befundschein-Nr)
		setStaticFrame(404, 660, 150, 16);
		setStaticText(fontArialCursive, 9, 409, 665, "Befundschein-Nr.:");
		setDatabaseText(fontArial, 9, 495, 665, String.valueOf(data.getPlantId()));

		// Frame: topLeft (Versicherungsnehmer)
		setStaticFrame(55.37f, 535, 247, 118);
		setStaticText(fontArialBoldCursive, 9, 59, 642, "Versicherungsnehmer (VN)");
		// sm. setTextPlaceholder(fontArial, 9, 65, 620, "Deutsche Telekom AG");
		// sm. setTextPlaceholder(fontArial, 9, 65, 605, "12345 Berlin");
		// sm. setTextPlaceholder(fontArial, 9, 65, 590, "Telekomstraße 2");
		// sm. setTextPlaceholder(fontArial, 9, 65, 575, "Deutschland");

		// Frame: topRight (Risikoanschrift)
		setStaticFrame(307, 535, 247, 118);
		setStaticText(fontArialBoldCursive, 9, 313, 642, "Risikoanschrift: ");
		// sm. setTextPlaceholder(fontArial, 9, 313, 630, "Westerwald");
		// sm. setTextPlaceholder(fontArial, 9, 313, 618, "12587 Schwarzwald");
		setStaticText(fontArialCursive, 9, 313, 603, "Begleiter vom VN: ");
		setDatabaseText(fontArial, 9, 400, 603, data.getCompanion());
		setStaticText(fontArialCursive, 9, 313, 588, "Sachverständiger: ");
		setDatabaseText(fontArial, 9, 400, 588, data.getSurveyor());
		setStaticText(fontArialCursive, 9, 313, 573, "VdS-Anerk.-Nr.: ");
		setDatabaseText(fontArial, 9, 400, 573, String.valueOf(data.getVdsApprovalNr()));
		setStaticText(fontArialCursive, 9, 313, 558, "Datum der Prüfung: ");
		setDatabaseText(fontArial, 9, 400, 558, data.getExaminationDate());
		setStaticText(fontArialCursive, 9, 313, 543, "Prüfungsdauer: ");
		setDatabaseText(fontArial, 9, 400, 543, String.valueOf(data.getExaminationDuration()));
		setStaticText(fontArialCursive, 9, 463, 543, "Std.");
		setStaticText(fontArialCursive, 8, 483, 543, "(reine Prüfzeit)");

		// Frame: topMiddle (Art des Betriebes oder der Anlage)
		drawSingleCellTable(page1, 530.5f, 175f, 100f);
		setStaticText(fontArialBoldCursive, 12, 59, 517, "Art des Betriebes oder der Anlage");
		// sm. setDatabaseText(fontArial, 9, 59, 505, data.getDescription());
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
		setDatabaseText(fontArial, 9, 140, 466, data.getPrecautionsDeclaredLocation());
		setStaticText(fontArialCursive, 9, 59, 438, "Wurden alle Bereiche des");
		setStaticText(fontArialCursive, 9, 59, 425, "Risikostandorts geprüft?");
		setTwoCheckboxes(data.isExaminationComplete(), 177, 217, 425);
		setStaticText(fontArialCursive, 9, 190, 425, "ja");
		setStaticText(fontArialCursive, 9, 230, 425, "nein - Nachbesichtigung (<6 Wo) vereinbart bis zum:");
		setDatabaseText(fontArial, 9, 450, 425, data.getSubsequentExaminationDate());
		setStaticText(fontArialCursive, 6, 505, 425, "(Datum)");
		setStaticText(fontArialCursive, 9, 59, 407, "Begründung für nicht geprüfte Bereiche:");
		setDatabaseText(fontArial, 9, 225, 407, data.getExaminationIncompleteReason());
		setStaticText(fontArialCursive, 9, 59, 390,
				"Wurden nach Aussagen des Betreibers Teilbereiche der Anlage seit der letzten Revision erneuert, erweitert oder");
		setStaticText(fontArialCursive, 9, 59, 377, "umgebaut (entfällt bei Erstprüfung)?");
		setThreeCheckboxes(data.getChangesSinceLastExamination(), 337, 417, 457, 377);
		setStaticText(fontArialCursive, 9, 350, 377, "Erstprüfung");
		setStaticText(fontArialCursive, 9, 430, 377, "ja");
		setStaticText(fontArialCursive, 9, 470, 377, "nein");
		setStaticText(fontArialCursive, 9, 59, 364, "Wurden alle Mängel der vorhergehenden Revision beseitigt?");
		setThreeCheckboxes(data.getDefectsLastExaminationFixed(), 337, 417, 457, 364);
		setStaticText(fontArialCursive, 9, 350, 364, "Bericht fehlt");
		setStaticText(fontArialCursive, 9, 430, 364, "ja");
		setStaticText(fontArialCursive, 9, 470, 364, "nein");

		// Frame: middleMiddle (Gesamtbeurteilung der Anlage)
		setStaticText(fontArialBoldCursive, 12, 59, 337, "Gesamtbeurteilung der Anlage");
		setStaticText(fontArialCursive, 9, 59, 325, "Gefährdungskategorie gemäß Prüfrichtlinien VdS 2871");
		setFourCheckboxes(data.getDangerCategory(), 312, 367, 422, 477, 325);
		setStaticText(fontArialBoldCursive, 10, 295, 325, "(a)");
		setStaticText(fontArialBoldCursive, 10, 350, 325, "(b)");
		setStaticText(fontArialBoldCursive, 10, 405, 325, "(c)");
		setStaticText(fontArialBoldCursive, 10, 460, 325, "(d)");
		setStaticText(fontArialCursive, 9, 59, 312, "Ergänzende Erläuterungen:");
		float paddingDangerCategory = setDynamicText(page1, fontArial, 9, 1.25f, 370f, 175f, 312f,
				data.getDangerCategoryDescription(), true);
		paddingP1 = paddingDangerCategory;
		drawSingleCellTable(page1, 351.5f, 60f + paddingP1, 100f);

		// Frame: bottomMiddle (Prüfungsergebnis)
		drawSingleCellTable(page1, 287.5f - paddingP1, 207f, 100f);
		setStaticText(fontArialBoldCursive, 12, 59, 272 - paddingP1, "Prüfungsergebnis");
		setStaticText(fontArialCursive, 11, 164, 272 - paddingP1, "(Einzelergebnisse)");
		setOneCheckbox(data.isExaminationResultNoDefect(), 59, 260 - paddingP1);
		setStaticText(fontArialCursive, 9, 77, 260 - paddingP1, "Keinen Mangel gestgestellt");
		setOneCheckbox(data.isExaminationResultDefect(), 59, 248 - paddingP1);
		setStaticText(fontArialCursive, 9, 77, 248 - paddingP1, "Die festgestellten Mängel sind im");
		setStaticText(fontArialBoldCursive, 9, 212, 248 - paddingP1, "Anhang A");
		setStaticText(fontArialCursive, 9, 258, 248 - paddingP1, "aufgeführt und spätestens zu beseitigen bis:");
		// setTextPlaceholder(fontArial, 9, 450, 248, "01.09.2017");
		setOneCheckbox(data.isExaminationResultDanger(), 59, 236 - paddingP1);
		setStaticText(fontArialBoldCursive, 9, 77, 236 - paddingP1,
				"Es wurden Mängel festgestellt, die eine Brandgefahr (mit „X“ gekennzeichnet) bzw. eine Unfallgefahr (mit „O“");
		setStaticText(fontArialBoldCursive, 9, 77, 223 - paddingP1,
				"gekennzeichnet) hervorrufen können. Diese Mängel sind unverzüglich zu beseitigen");
		setStaticText(fontArialCursive, 9, 436, 223 - paddingP1, "! (Der Mangel ist dann mit");
		setStaticText(fontArialCursive, 9, 77, 211 - paddingP1,
				"einem X oder O zu kennzeichnen, wenn er im Extremfall, aber bei sonst normalen Betriebsbedingungen zu einem");
		setStaticText(fontArialCursive, 9, 77, 199 - paddingP1, "Brand bzw. zu einem Personenschaden führen kann.)");
		setStaticText(fontArialCursive, 9, 59, 184 - paddingP1,
				"Dieser Befundschein besteht einschließlich des Anhangs aus");
		// sm. setTextPlaceholderManual(fontArial, 9, 340, 184, "6");
		setStaticText(fontArialCursive, 9, 380, 184 - paddingP1, "Seiten.");
		setStaticText(fontArialCursive, 9, 59, 169 - paddingP1,
				"Die elektrische(n) Anlage(n) wurde(n) gemäß den Prüfrichtlinien VdS 2871 nach bestem Wissen und Gewissen geprüft. Bei");
		setStaticText(fontArialCursive, 9, 59, 157 - paddingP1,
				"den nicht im Anhang dieses Befundscheins aufgeführten Anlagenteilen und Bereichen wurden keine Mängel festgestellt.");
		setStaticText(fontArialCursive, 9, 100, 84 - paddingP1, "Firmenstempel");
		setStaticText(fontArialCursive, 9, 250, 84 - paddingP1,
				"Datum und Unterschrift des VdS-anerkannten Sachverständigen");

		// setTexField 4a)
		setStaticText(fontArialBoldCursive, 6, 59, 73 - paddingP1, "1");
		setStaticText(fontArialCursive, 8, 64, 70 - paddingP1,
				"das sind z. B. Betriebsstätten nach VdS 2033 / Ex-Bereiche / stationäre Stromerzeugungsanlagen / Ladestationen für Fahrzeuge und");
		setStaticText(fontArialCursive, 8, 64, 60 - paddingP1, "Flurförderzeuge");

		// Closing the ContentStream
		contentStream.close();

		/////// PAGE 2 ///////
		// Retrieving the second page
		PDPage page2 = new PDPage(PDRectangle.A4);
		document.addPage(page2);
		float paddingP2;
		// Creating the PDPageContentStream object
		contentStream = new PDPageContentStream(document, page2);

		/// setFrame & setTextField 1)
		setStaticFrame(55.37f, 779.63f, 498.5f, 26);

		// setTexField 2a)
		// sm. setTextPlaceholderManual(fontArialCursive, 9, 474, 810,
		// "Blatt-Nr. 2 von 3");
		setConstTextPageOf(fontArialCursive, 2, 3);

		// setTexField 3a)
		setStaticText(fontArialCursive, 9, 513, 798, "Seite - 2 -");

		/// setFrame & setTextField 2)
		setStaticFrame(406, 779.63f, 147.9f, 16f);
		setStaticText(fontArialCursive, 9, 409, 785, "Befundschein-Nr.:");
		setDatabaseText(fontArial, 9, 495, 785, String.valueOf(data.getPlantId()));

		////
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
				data.getIsolationCompensationMeasuresAnnotation(), false);
		paddingP2 = paddingIsolation;
		// sm. setDatabaseText(fontArial, 9, 142, 694,
		// data.getIsolationCompensationMeasuresAnnotation());
		drawPartingLine(0.75f, 55.5f, 688f - paddingP2, 554f, 688f - paddingP2, true);
		setStaticText(fontArialBoldCursive, 9, 59, 676 - paddingP2, "• Fehlerstrom-Schutzeinrichtungen (RCDs)");
		setTwoCheckboxes(data.getRcdAvailable(), 353, 463, 676 - paddingP2);
		setStaticText(fontArialCursive, 9, 365, 676 - paddingP2, "alle oder");
		setDatabaseText(fontArial, 9, 424, 676 - paddingP2, String.valueOf(data.getRcdAvailablePercent()));
		setStaticText(fontArialCursive, 9, 439, 676 - paddingP2, "%");
		setStaticText(fontArialCursive, 9, 475, 676 - paddingP2, "nein");
		setStaticText(fontArialBoldCursive, 6, 493, 679 - paddingP2, "3");
		setStaticText(fontArialCursive, 9, 64, 664 - paddingP2, "Bemerkung hierzu:");
		float paddingRcd = setDynamicText(page2, fontArial, 9, 1.25f, 400f, 142, 664f - paddingP2,
				data.getRcdAnnotation(), false);
		paddingP2 += paddingRcd;
		drawPartingLine(0.75f, 55.5f, 658f - paddingP2, 554f, 658f - paddingP2, true);
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
				data.getResistanceAnnotation(), false);
		paddingP2 += paddingResistance;
		drawPartingLine(0.75f, 55.5f, 628f - paddingP2, 554f, 628f - paddingP2, true);
		setStaticText(fontArialBoldCursive, 9, 59, 616 - paddingP2,
				"• Wurden thermische Auffälligkeiten messtechnisch vorgefunden?");
		setTwoCheckboxes(data.isThermalAbnormality(), 353, 463, 616 - paddingP2);
		setStaticText(fontArialCursive, 9, 365, 616 - paddingP2, "ja");
		setStaticText(fontArialCursive, 9, 475, 616 - paddingP2, "nein");
		setStaticText(fontArialCursive, 9, 64, 604 - paddingP2, "Bemerkung hierzu:");
		float paddingThermal = setDynamicText(page2, fontArial, 9, 1.25f, 400f, 142, 604f - paddingP2,
				data.getThermalAbnormalityAnnotation(), false);
		paddingP2 += paddingThermal;
		drawSingleCellTable(page2, 780f, 182f + paddingP2, 100f);

		////
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

		////
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

		////
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

		////
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

		////
		setStaticText(fontArialBoldCursive, 9, 59, 208 - paddingP2,
				"Weitere Erläuterungen wie z. B. verwendete Messgeräte (optional):");
		setDynamicText(page2, fontArial, 10, 1.25f, 490f, 59, 193 - paddingP2, data.getAdditionalAnnotations(), false);
		setConstTextVersion(fontArial);

		// Closing the ContentStream
		contentStream.close();

		/////// PAGE 3 ///////

		// Retrieving the third page
		PDPage page3 = new PDPage(PDRectangle.A4);
		document.addPage(page3);

		// Creating the PDPageContentStream object
		contentStream = new PDPageContentStream(document, page3);

		// setTexField 2a)
		setConstTextPageOf(fontArialCursive, 3, 3);

		// setFrame & setTextField 1)
		setStaticFrame(54, 755, 501, 35);
		setStaticText(fontArialBoldCursive, 12, 59, 768, "Anhang A zum Befundschein-Nr.:");
		// setTextPlaceholder(fontArialCursive, 10, 280, 768, "26/04/17");

		setStaticFrame(54, 700, 501, 55);
		setStaticText(fontArialBoldCursive, 9, 59, 745, "Allgemeine Bemerkungen");
		setStaticText(fontArialCursive, 9, 59, 735,
				"Wenn in der elektrischen Anlage z. B. aus betrieblichen Gründen keine oder nicht im ausreichenden Umfang Isolations-");
		setStaticText(fontArialCursive, 9, 59, 725,
				"widerstandsmessungen durchgeführt werden können, wird dringend empfohlen, nach VdS 2349 für eine konstante Iso-");
		setStaticText(fontArialCursive, 9, 59, 715,
				"lationsüberwachung zu sorgen. In Einzelfällen kann der Sachversicherer auch ergänzende oder alternative Maßnahmen");
		setStaticText(fontArialCursive, 9, 59, 705,
				"fordern. Aus der Sicht des Sachversicherers kann dies auch eine thermografische Untersuchung sein.");

		setStaticFrame(54, 678, 18, 22);
		setStaticText(fontArialBold, 8, 57, 691, "lfd.");
		setStaticText(fontArialBold, 8, 57, 681, "Nr.");
		setStaticFrame(72, 678, 32, 22);
		setStaticText(fontArialBold, 8, 74, 686, "Gefahr");
		setStaticText(fontArialBoldCursive, 6, 100, 689, "1");
		setStaticFrame(104, 678, 341, 22);
		setStaticText(fontArialBold, 8, 112, 691, "Gebäude / Anlage / Raum sowie");
		setStaticText(fontArialBold, 8, 112, 681, "Mängelbeschreibung und empfohlene Maßnahmen");
		setStaticFrame(445, 678, 55, 22);
		setStaticText(fontArialBold, 8, 448, 691, "Mangel-");
		setStaticText(fontArialBold, 8, 448, 681, "Nummer");
		setStaticText(fontArialBoldCursive, 6, 481, 684, "2");
		setStaticFrame(500, 678, 55, 22);
		setStaticText(fontArialBold, 8, 503, 691, "Betriebs-");
		setStaticText(fontArialBold, 8, 503, 681, "Bereich");
		setStaticText(fontArialBoldCursive, 6, 533, 683, "2");

		setStaticFrame(54, 630, 18, 48);
		setStaticFrame(72, 630, 32, 48);
		setStaticFrame(104, 630, 341, 48);
		setStaticFrame(445, 630, 55, 48);
		setStaticFrame(500, 630, 55, 48);

		setConstTextVersion(fontArial);

		// Closing the ContentStream
		contentStream.close();

		/////// PAGE X ///////

		// Retrieving the xth page
		PDPage page4 = new PDPage(PDRectangle.A4);
		// sm. document.addPage(page4);

		// Creating the PDPageContentStream object
		contentStream = new PDPageContentStream(document, page4);
		setStaticText(fontArialBold, 14, 150f, 520f, "Das ASD ASDASD ASDASD  ein Test");

		contentStream.close();

	}

}

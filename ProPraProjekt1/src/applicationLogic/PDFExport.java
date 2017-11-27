package applicationLogic;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
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
	
	static PDDocument document;
	static PDPageContentStream contentStream;

	
    // temp	
	static String placeholder = "26/04/17";
	
	
	
	/**
	 * draws new dotted Line
	 * 
	 * @param startPositionX
	 * @param startPositionY
	 * @param endPositionX
	 * @param endPositionY
	 * @throws IOException
	 */
	public static void drawDottedLine(float startPositionX, float startPositionY, float endPositionX, float endPositionY) throws IOException {
		contentStream.setLineWidth(0.75f);
		contentStream.moveTo(startPositionX, startPositionY);
		contentStream.lineTo(endPositionX, endPositionY);
		contentStream.setLineDashPattern(new float[]{2.5f}, 0);
		contentStream.stroke();
	}
	
	/**
	 * creates new Text
	 * 
	 * @param font
	 * @param size
	 * @param positionX
	 * @param positionY
	 * @param content
	 * @throws IOException
	 */
	public static void setText(PDFont font, int size, float positionX, float positionY, String content) throws IOException {
		contentStream.beginText();
		contentStream.setFont(font, size);
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.newLineAtOffset(positionX, positionY);
		contentStream.showText(content);
		contentStream.endText();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @param font
	 * @param size
	 * @param positionX
	 * @param positionY
	 * @param content
	 * @throws IOException
	 */
	public static void setTextPlacefholder(PDFont font, int size, float positionX, float positionY, String content)	throws IOException {
		contentStream.beginText();
		contentStream.setFont(font, size);
		contentStream.setNonStrokingColor(Color.YELLOW);
		contentStream.newLineAtOffset(positionX, positionY);
		contentStream.showText(content);
		contentStream.endText();
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * creates new Frame / Rectangle (solid & black)
	 * 
	 * @param positionX
	 * @param posotionY
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	public static void setFrame(float positionX, float positionY, float width, float height) throws IOException {
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
		contentStream.addRect(posistionX, positionY, 7, 7);
		contentStream.closeAndFillAndStrokeEvenOdd();

		contentStream.setLineWidth(0.75f);
		contentStream.moveTo(posistionX, positionY);
		contentStream.lineTo(posistionX + 7, positionY + 7);
		contentStream.moveTo(posistionX, positionY + 7);
		contentStream.lineTo(posistionX + 7, positionY);
		contentStream.stroke();
	}
	
	public static void export() throws IOException {
		try {
			// Creating PDF document object
			document = new PDDocument();

			// Setting Fonts
			PDFont fontArial = PDType0Font.load(document, arial);
			PDFont fontArialBold = PDType0Font.load(document, arialBold);
			PDFont fontArialBoldCursive = PDType0Font.load(document, arialBoldCursive);
			PDFont fontArialCursive = PDType0Font.load(document, arialCursive);
	
			//////////////////////////////////////////// PAGE 1 ////////////////////////////////////////////
			// Retrieving the first page
			PDPage page1 = new PDPage(PDRectangle.A4);
			document.addPage(page1);			
			
			// Creating PDImageXObject object
			PDImageXObject pdImage = PDImageXObject.createFromFile("img/pdf_komplett_72dpi.png", document);
	
			// Creating the PDPageContentStream object
			contentStream = new PDPageContentStream(document, page1);
	
			// Drawing the image in the PDF document
			contentStream.drawImage(pdImage, 0, 692);
			System.out.println("Images inserted");
			
			// partingLine (left-side)
			contentStream.setLineWidth(0.75f);
			contentStream.moveTo(0, 542);
			contentStream.lineTo(15, 542);
			contentStream.stroke();
	
			// setTextField 1a)
			setText(fontArialBoldCursive, 12, 59, 680, "BEFUNDSCHEIN");
			setText(fontArialCursive, 9, 164, 680, "über die Prüfung elektrischer Anlagen gemäß Vorgaben");
			setText(fontArialCursive, 9, 59, 670,
					"der Sachversicherer nach den Prüfrichtlinien VdS 2871 durch VdS-anerkannte");
			setText(fontArialCursive, 9, 59, 660, "Sachverständige");
	
			// setTexField 2a)
			setTextPlacefholder(fontArialCursive, 9, 474, 810, "Blatt-Nr. 1 von 3");
	
			// setTexField 3a)
			setTextPlacefholder(fontArialCursive, 9, 513, 682, "Seite - 1 -");
	
			/// setFrame & setTextField 1)
			setFrame(405, 660, 150, 16);
			setText(fontArialCursive, 9, 409, 665, "Befundschein-Nr.:");
			setTextPlacefholder(fontArialCursive, 9, 495, 665, placeholder);
	
			/// setFrame & setTextField 2)
			setFrame(54, 535, 248, 118);
			setText(fontArialBoldCursive, 9, 59, 642, "Versicherungsnehmer (VN)");
			setTextPlacefholder(fontArial, 9, 65, 620, "Deutsche Telekom AG");
			setTextPlacefholder(fontArial, 9, 65, 605, "12345 Berlin");
			setTextPlacefholder(fontArial, 9, 65, 590, "Telekomstraße 2");
			setTextPlacefholder(fontArial, 9, 65, 575, "Deutschland");
	
			/// setFrame & setTextField 3)
			setFrame(307, 535, 248, 118);
			setText(fontArialBoldCursive, 9, 313, 642, "Rikoanschrift: ");
			setTextPlacefholder(fontArial, 9, 313, 630, "Westerwald");
			setTextPlacefholder(fontArial, 9, 313, 618, "12587 Schwarzwald");
			setText(fontArialCursive, 9, 313, 603, "Begleiter vom VN: ");
			setTextPlacefholder(fontArial, 9, 400, 603, "Allianz AG");
			setText(fontArialCursive, 9, 313, 588, "Sachverständiger: ");
			setTextPlacefholder(fontArial, 9, 400, 588, "Helmut Schmidt");
			setText(fontArialCursive, 9, 313, 573, "VdS-Anerk.-Nr.: ");
			setTextPlacefholder(fontArial, 9, 400, 573, "215646-AA-53sd");
			setText(fontArialCursive, 9, 313, 558, "Datum der Prüfung: ");
			setTextPlacefholder(fontArial, 9, 400, 558, "12.12.2012");
			setText(fontArialCursive, 9, 313, 543, "Prüfungsdauer: ");
			setTextPlacefholder(fontArial, 9, 400, 543, "23");
			setText(fontArialCursive, 9, 463, 543, "Std.");
			setText(fontArialCursive, 8, 483, 543, "(reine Prüfzeit)");
	
			/// setFrame & setTextField 4)
			setFrame(54, 355, 501, 175);
			setText(fontArialBoldCursive, 12, 59, 517, "Art des Betriebes oder der Anlage");
			setTextPlacefholder(fontArial, 9, 59, 505, "Sägewerk");
			setText(fontArialCursive, 9, 59, 492,
					"Sind frequenzgesteuerte Betriebsmittel (z. B. Motore) in der elektrischen Anlage installiert?");
			setCheckboxUnchecked(463, 492);
			setText(fontArialCursive, 9, 475, 492, "ja");
			setCheckboxChecked(503, 492);
			setText(fontArialCursive, 9, 515, 492, "nein");
			setText(fontArialCursive, 9, 59, 479,
					"Sind Bereiche , die besondere Schutzmaßnahmen erfordern, durch den Betreiber ausgewiesen?");
			setCheckboxChecked(463, 479);
			setText(fontArialCursive, 9, 475, 479, "ja");
			setCheckboxUnchecked(503, 479);
			setText(fontArialCursive, 9, 515, 479, "nein");
			setText(fontArialBoldCursive, 6, 115, 482, "1");
			setText(fontArialCursive, 9, 59, 466, "Wenn ja, welche:");
			setTextPlacefholder(fontArial, 9, 140, 466, "Rauchverbotszone");
			setTextPlacefholder(fontArial, 9, 140, 453, "Laderampe");
			setText(fontArialCursive, 9, 59, 438, "Wurden alle Bereiche des");
			setText(fontArialCursive, 9, 59, 425, "Risikostandorts geprüft?");
			setCheckboxUnchecked(177, 425);
			setText(fontArialCursive, 9, 190, 425, "ja");
			setCheckboxChecked(217, 425);
			setText(fontArialCursive, 9, 230, 425, "nein - Nachbesichtigung (<6 Wo) vereinbart bis zum:");
			setTextPlacefholder(fontArial, 9, 450, 425, "24.12.2012");
			setText(fontArialCursive, 6, 505, 425, "(Datum)");
			setText(fontArialCursive, 9, 59, 407, "Begründung für nicht geprüfte Bereiche:");
			setTextPlacefholder(fontArial, 9, 225, 407, "Es wurden nur die nach Angabe versicherten Risiken geprüft");
			setText(fontArialCursive, 9, 59, 390,
					"Wurden nach Aussagen des Betreibers Teilbereiche der Anlage seit der letzten Revision erneuert, erweitert oder");
			setText(fontArialCursive, 9, 59, 377, "umgebaut (entfällt bei Erstprüfung)?");
			setCheckboxChecked(337, 377);
			setText(fontArialCursive, 9, 350, 377, "Erstprüfung");
			setCheckboxUnchecked(417, 377);
			setText(fontArialCursive, 9, 430, 377, "ja");
			setCheckboxUnchecked(457, 377);
			setText(fontArialCursive, 9, 470, 377, "nein");
			setText(fontArialCursive, 9, 59, 364, "Wurden alle Mängel der vorhergehenden Revision beseitigt?");
			setCheckboxUnchecked(337, 364);
			setText(fontArialCursive, 9, 350, 364, "Bericht fehlt");
			setCheckboxChecked(417, 364);
			setText(fontArialCursive, 9, 430, 364, "ja");
			setCheckboxUnchecked(457, 364);
			setText(fontArialCursive, 9, 470, 364, "nein");
	
			/// setFrame & setTextField 5)
			setFrame(54, 290, 501, 60);
			setText(fontArialBoldCursive, 12, 59, 337, "Gesamtbeurteilung der Anlage");
			setText(fontArialCursive, 9, 59, 325, "Gefährdungskategorie gemäß Prüfrichtlinien VdS 2871");
			setText(fontArialBoldCursive, 10, 295, 325, "(a)");
			setCheckboxChecked(312, 325);
			setText(fontArialBoldCursive, 10, 350, 325, "(b)");
			setCheckboxUnchecked(367, 325);
			setText(fontArialBoldCursive, 10, 405, 325, "(c)");
			setCheckboxUnchecked(422, 325);
			setText(fontArialBoldCursive, 10, 460, 325, "(d)");
			setCheckboxUnchecked(477, 325);
			setText(fontArialCursive, 9, 59, 313, "Ergänzende Erläuterungen:");
			setTextPlacefholder(fontArialCursive, 9, 175, 313, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr. At vero eos et accusam et justo duo.");
			setTextPlacefholder(fontArialCursive, 9, 59, 301, "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
	
			/// setFrame & setTextField 6)
			setFrame(54, 80, 501, 205);
			setText(fontArialBoldCursive, 12, 59, 272, "Prüfungsergebnis");
			setText(fontArialCursive, 11, 164, 272, "(Einzelergebnisse)");
			setCheckboxUnchecked(59, 260);
			setText(fontArialCursive, 9, 77, 260, "Keinen Mangel gestgestellt");
			setCheckboxChecked(59, 248);
			setText(fontArialCursive, 9, 77, 248, "Die festgestellten Mängel sind im");
			setText(fontArialBoldCursive, 9, 212, 248, "Anhang A");
			setText(fontArialCursive, 9, 258, 248, "aufgeführt und spätestens zu beseitigen bis:");
			setTextPlacefholder(fontArial, 9, 450, 248, "01.09.2017");
			setCheckboxChecked(59, 236);
			setText(fontArialBoldCursive, 9, 77, 236, "Es wurden Mängel festgestellt, die eine Brandgefahr (mit „X“ gekennzeichnet) bzw. eine Unfallgefahr (mit „O“");
			setText(fontArialBoldCursive, 9, 77, 223, "gekennzeichnet) hervorrufen können. Diese Mängel sind unverzüglich zu beseitigen");
			setText(fontArialCursive, 9, 436, 223, "! (Der Mangel ist dann mit");
			setText(fontArialCursive, 9, 77, 211, "einem X oder O zu kennzeichnen, wenn er im Extremfall, aber bei sonst normalen Betriebsbedingungen zu einem");
			setText(fontArialCursive, 9, 77, 199, "Brand bzw. zu einem Personenschaden führen kann.)");
			setText(fontArialCursive, 9, 59, 184, "Dieser Befundschein besteht einschließlich des Anhangs aus");
			setTextPlacefholder(fontArial, 9, 340, 184, "6");
			setText(fontArialCursive, 9, 380, 184, "Seiten.");
			setText(fontArialCursive, 9, 59, 169, "Die elektrische(n) Anlage(n) wurde(n) gemäß den Prüfrichtlinien VdS 2871 nach bestem Wissen und Gewissen geprüft. Bei");
			setText(fontArialCursive, 9, 59, 157, "den nicht im Anhang dieses Befundscheins aufgeführten Anlagenteilen und Bereichen wurden keine Mängel festgestellt.");
			setText(fontArialCursive, 9, 100, 84, "Firmenstempel");
			setText(fontArialCursive, 9, 250, 84, "Datum und Unterschrift des VdS-anerkannten Sachverständigen");
	
			// setTexField 4a)
			setText(fontArialBoldCursive, 6, 59, 73, "1");
			setText(fontArialCursive, 8, 64, 70,
					"das sind z. B. Betriebsstätten nach VdS 2033 / Ex-Bereiche / stationäre Stromerzeugungsanlagen / Ladestationen für Fahrzeuge und");
			setText(fontArialCursive, 8, 64, 60, "Flurförderzeuge");
	
			// setTexField 5a)
			setTextPlacefholder(fontArial, 8, 59, 25, "VdS 2229 : 2013-06 (08)");
						
			// Closing the ContentStream
			contentStream.close();
	
			//////////////////////////////////////////// PAGE 2 ////////////////////////////////////////////
	
			// Retrieving the second page
			PDPage page2 = new PDPage(PDRectangle.A4);
			document.addPage(page2);
			
			// Creating the PDPageContentStream object
			contentStream = new PDPageContentStream(document, page2);
	
			/// setFrame & setTextField 1)
			setFrame(54, 780, 501, 26);
			
			// setTexField 2a)
			setTextPlacefholder(fontArialCursive, 9, 474, 810, "Blatt-Nr. 2 von 3");
	
			// setTexField 3a)
			setTextPlacefholder(fontArialCursive, 9, 513, 798, "Seite - 2 -");
	
			/// setFrame & setTextField 2)
			setFrame(405, 780, 150, 16);
			setText(fontArialCursive, 9, 409, 785, "Befundschein-Nr.:");
			setTextPlacefholder(fontArialCursive, 9, 495, 785, placeholder);
			
			/// setFrame & setTextField 3)
			setFrame(54, 590, 501, 185);
			drawDottedLine(55, 680, 555, 680);
			drawDottedLine(55, 650, 555, 650);
			drawDottedLine(55, 620, 555, 620);
			setFrame(54, 540, 501, 50);
			setFrame(54, 505, 501, 35);
			setFrame(54, 490, 501, 15);
			setFrame(54, 335, 501, 155);
	
	
			// Closing the ContentStream
			contentStream.close();
			
			//////////////////////////////////////////// PAGE 3 ////////////////////////////////////////////
	
	//		// Retrieving the third page
	//		PDPage page3 = new PDPage(PDRectangle.A4);
	//		document.addPage(page3);
	//		
	//		// Creating the PDPageContentStream object
	//		contentStream = new PDPageContentStream(document, page3);
	//
	//		/// setFrame & setTextField 1)
	//		setFrame(54, 365, 501, 165);
	//		setText(fontArialCursive, 9, 59, 300, "Das ist ein Test");
	//
	//		// Closing the ContentStream
	//		contentStream.close();
			
			//////////////////////////////////////////// PAGE X ////////////////////////////////////////////
			
	
		
	
			////////////////////////////////////////////////////////////////////////////////////////////////
	
			// Saving the document
			document.save("pdf/pfeifer.pdf");
			System.out.println("Document saved");
	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Closing the document
			document.close();
		}
	}

}

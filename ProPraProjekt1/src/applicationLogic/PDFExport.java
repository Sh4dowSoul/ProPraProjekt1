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
import org.apache.pdfbox.pdmodel.font.PDType1Font;
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
	public static void setTextPlaceholder(PDFont font, int size, float positionX, float positionY, String content)	throws IOException {
		contentStream.beginText();
		contentStream.setFont(font, size);
		contentStream.setNonStrokingColor(Color.RED);
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
			setTextPlaceholder(fontArialCursive, 9, 474, 810, "Blatt-Nr. 1 von 3");
	
			// setTexField 3a)
			setTextPlaceholder(fontArialCursive, 9, 513, 682, "Seite - 1 -");
	
			/// setFrame & setTextField 1)
			setFrame(405, 660, 150, 16);
			setText(fontArialCursive, 9, 409, 665, "Befundschein-Nr.:");
			setTextPlaceholder(fontArialCursive, 9, 495, 665, placeholder);
	
			/// setFrame & setTextField 2)
			setFrame(54, 535, 248, 118);
			setText(fontArialBoldCursive, 9, 59, 642, "Versicherungsnehmer (VN)");
			setTextPlaceholder(fontArial, 9, 65, 620, "Deutsche Telekom AG");
			setTextPlaceholder(fontArial, 9, 65, 605, "12345 Berlin");
			setTextPlaceholder(fontArial, 9, 65, 590, "Telekomstraße 2");
			setTextPlaceholder(fontArial, 9, 65, 575, "Deutschland");
	
			/// setFrame & setTextField 3)
			setFrame(307, 535, 248, 118);
			setText(fontArialBoldCursive, 9, 313, 642, "Rikoanschrift: ");
			setTextPlaceholder(fontArial, 9, 313, 630, "Westerwald");
			setTextPlaceholder(fontArial, 9, 313, 618, "12587 Schwarzwald");
			setText(fontArialCursive, 9, 313, 603, "Begleiter vom VN: ");
			setTextPlaceholder(fontArial, 9, 400, 603, "Allianz AG");
			setText(fontArialCursive, 9, 313, 588, "Sachverständiger: ");
			setTextPlaceholder(fontArial, 9, 400, 588, "Helmut Schmidt");
			setText(fontArialCursive, 9, 313, 573, "VdS-Anerk.-Nr.: ");
			setTextPlaceholder(fontArial, 9, 400, 573, "215646-AA-53sd");
			setText(fontArialCursive, 9, 313, 558, "Datum der Prüfung: ");
			setTextPlaceholder(fontArial, 9, 400, 558, "12.12.2012");
			setText(fontArialCursive, 9, 313, 543, "Prüfungsdauer: ");
			setTextPlaceholder(fontArial, 9, 400, 543, "23");
			setText(fontArialCursive, 9, 463, 543, "Std.");
			setText(fontArialCursive, 8, 483, 543, "(reine Prüfzeit)");
	
			/// setFrame & setTextField 4)
			setFrame(54, 355, 501, 175);
			setText(fontArialBoldCursive, 12, 59, 517, "Art des Betriebes oder der Anlage");
			setTextPlaceholder(fontArial, 9, 59, 505, "Sägewerk");
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
			setTextPlaceholder(fontArial, 9, 140, 466, "Rauchverbotszone");
			setTextPlaceholder(fontArial, 9, 140, 453, "Laderampe");
			setText(fontArialCursive, 9, 59, 438, "Wurden alle Bereiche des");
			setText(fontArialCursive, 9, 59, 425, "Risikostandorts geprüft?");
			setCheckboxUnchecked(177, 425);
			setText(fontArialCursive, 9, 190, 425, "ja");
			setCheckboxChecked(217, 425);
			setText(fontArialCursive, 9, 230, 425, "nein - Nachbesichtigung (<6 Wo) vereinbart bis zum:");
			setTextPlaceholder(fontArial, 9, 450, 425, "24.12.2012");
			setText(fontArialCursive, 6, 505, 425, "(Datum)");
			setText(fontArialCursive, 9, 59, 407, "Begründung für nicht geprüfte Bereiche:");
			setTextPlaceholder(fontArial, 9, 225, 407, "Es wurden nur die nach Angabe versicherten Risiken geprüft");
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
			setTextPlaceholder(fontArialCursive, 9, 175, 313, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr. At vero eos et accusam et justo duo.");
			setTextPlaceholder(fontArialCursive, 9, 59, 301, "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
	
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
			setTextPlaceholder(fontArial, 9, 450, 248, "01.09.2017");
			setCheckboxChecked(59, 236);
			setText(fontArialBoldCursive, 9, 77, 236, "Es wurden Mängel festgestellt, die eine Brandgefahr (mit „X“ gekennzeichnet) bzw. eine Unfallgefahr (mit „O“");
			setText(fontArialBoldCursive, 9, 77, 223, "gekennzeichnet) hervorrufen können. Diese Mängel sind unverzüglich zu beseitigen");
			setText(fontArialCursive, 9, 436, 223, "! (Der Mangel ist dann mit");
			setText(fontArialCursive, 9, 77, 211, "einem X oder O zu kennzeichnen, wenn er im Extremfall, aber bei sonst normalen Betriebsbedingungen zu einem");
			setText(fontArialCursive, 9, 77, 199, "Brand bzw. zu einem Personenschaden führen kann.)");
			setText(fontArialCursive, 9, 59, 184, "Dieser Befundschein besteht einschließlich des Anhangs aus");
			setTextPlaceholder(fontArial, 9, 340, 184, "6");
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
			setTextPlaceholder(fontArial, 8, 59, 25, "VdS 2229 : 2013-06 (08)");
						
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
			setTextPlaceholder(fontArialCursive, 9, 474, 810, "Blatt-Nr. 2 von 3");
	
			// setTexField 3a)
			setTextPlaceholder(fontArialCursive, 9, 513, 798, "Seite - 2 -");
	
			/// setFrame & setTextField 2)
			setFrame(405, 780, 150, 16);
			setText(fontArialCursive, 9, 409, 785, "Befundschein-Nr.:");
			setTextPlaceholder(fontArialCursive, 9, 495, 785, placeholder);
			
			/// setFrame & setTextField 3)
			setFrame(54, 590, 501, 185);
			setText(fontArialBoldCursive, 12, 59, 764, "Messungen");
			setText(fontArialBoldCursive, 9, 59, 750, "• Isolationswiderstand:");
			setText(fontArialCursive, 9, 160, 750, "Messung in mind. 50 % aller Stromkreise");
			setCheckboxUnchecked(353, 750);
			setText(fontArialCursive, 9, 365, 750, "ja");
			setCheckboxChecked(463, 750);
			setText(fontArialCursive, 9, 475, 750, "nein");
			
			setText(fontArialCursive, 9, 64, 738, "Wenn Isolationswiderstandsmessungen nicht möglich sind: Lagen er-");
			setText(fontArialCursive, 9, 64, 728, "satzweise Messprotokolle über Isolationswiderstandsmessungen vor?");
			setCheckboxUnchecked(353, 738);
			setText(fontArialCursive, 9, 365, 738, "ja");
			setCheckboxChecked(463, 738);
			setText(fontArialCursive, 9, 475, 738, "nein");
			setText(fontArialBoldCursive, 6, 493, 741, "2");
			
			setText(fontArialCursive, 9, 64, 716, "Wenn nein: Sind Ersatzmaßnahmen nach Aussage des Betreibers");
			setText(fontArialCursive, 9, 64, 706, "vorhanden?");
			setCheckboxUnchecked(353, 716);
			setText(fontArialCursive, 9, 365, 716, "ja");
			setCheckboxChecked(463, 716);
			setText(fontArialCursive, 9, 475, 716, "nein");
			
			setText(fontArialCursive, 9, 64, 694, "Bemerkung hierzu:");
			setTextPlaceholder(fontArial, 9, 142, 694, "Trennstellen sind nicht ausreichend vorhanden, aus betrieblichen Gründen sind Abschaltungen nicht");
			setTextPlaceholder(fontArial, 9, 64, 684, "im geforderten Umfang möglich, Isolationsüberwachungen sind nur in neueren Anlagen installiert,");
			
			drawDottedLine(55, 680, 555, 680);
			
			setText(fontArialBoldCursive, 9, 59, 668, "• Fehlerstrom-Schutzeinrichtungen (RCDs)");
			setCheckboxChecked(353, 668);
			setText(fontArialCursive, 9, 365, 668, "alle oder");
			setTextPlaceholder(fontArialCursive, 9, 424, 668, "50 %");
			setCheckboxUnchecked(463, 668);
			setText(fontArialCursive, 9, 475, 668, "nein");
			setText(fontArialBoldCursive, 6, 493, 671, "3");
			setText(fontArialCursive, 9, 64, 656, "Bemerkung hierzu:");
			
			
			drawDottedLine(55, 650, 555, 650);
			
			setText(fontArialBoldCursive, 9, 59, 638, "• Schleifenwiderstand");
			setCheckboxUnchecked(353, 638);
			setText(fontArialCursive, 9, 365, 638, "ja, Anzahl  :");
			setText(fontArialBoldCursive, 6, 406, 641, "4");
			setTextPlaceholder(fontArialCursive, 9, 424, 638, "30 %");
			setCheckboxChecked(463, 638);
			setText(fontArialCursive, 9, 475, 638, "nein");
			setText(fontArialBoldCursive, 6, 493, 641, "3");
			setText(fontArialCursive, 9, 64, 626, "Bemerkung hierzu:");
			
			drawDottedLine(55, 620, 555, 620);
			
			setText(fontArialBoldCursive, 9, 59, 608, "• Wurden thermische Auffälligkeiten messtechnisch vorgefunden?");
			setCheckboxChecked(353, 608);
			setText(fontArialCursive, 9, 365, 608, "ja");
			setCheckboxUnchecked(463, 608);
			setText(fontArialCursive, 9, 475, 608, "nein");
			setText(fontArialCursive, 9, 64, 596, "Bemerkung hierzu:");
			setTextPlaceholder(fontArial, 9, 142, 596, "Es wurde keine Thermografieuntersuchung durchgeführt.");
			
			setFrame(54, 540, 501, 50);
			setText(fontArialBoldCursive, 12, 59, 578, "Ortsveränderliche Betriebsmittel");
			setText(fontArialCursive, 9, 59, 564, "Werden nach Aussage des Betreibers die ortsveränderlichen");
			setText(fontArialCursive, 9, 59, 554, "Betriebsmittel regelmäßig geprüft (z. B. gemäß BetrSichV oder");
			setText(fontArialCursive, 9, 59, 544, "BGV A3)?");
			setCheckboxChecked(353, 554);
			setText(fontArialCursive, 9, 365, 554, "ja");
			setCheckboxUnchecked(463, 554);
			setText(fontArialCursive, 9, 475, 554, "nein");
			
			setFrame(54, 505, 501, 35);
			setText(fontArialCursive, 9, 59, 530, "Beinhalten diese Prüfungen nach Aussage des Betreibers auch fremde");
			setText(fontArialCursive, 9, 59, 520, "ortsveränderliche Betriebsmittel, d. h. privat mitgebrachte, gemietete,");
			setText(fontArialCursive, 9, 59, 510, "geleaste oder durch Dritte bereitgestellte Betriebsmittel?");
			setCheckboxUnchecked(353, 520);
			setText(fontArialCursive, 9, 365, 520, "ja");
			setCheckboxChecked(423, 520);
			setText(fontArialCursive, 9, 435, 520, "nein");
			setCheckboxUnchecked(493, 520);
			setText(fontArialCursive, 9, 505, 520, "nr");
			setText(fontArialBoldCursive, 6, 515, 523, "5");
			
			setFrame(54, 490, 501, 15);
			setText(fontArialBoldCursive, 12, 59, 494, "Allgemeine Informationen zur geprüften elektrischen Anlage");
				
			setFrame(54, 335, 501, 155);
			setText(fontArialBoldCursive, 9, 59, 480, "Versorgungssystem");
			setCheckboxChecked(59, 466);
			setText(fontArialCursive, 9, 71, 466, "TN");
			setCheckboxChecked(124, 466);
			setText(fontArialCursive, 9, 136, 466, "TT");
			setCheckboxChecked(194, 466);
			setText(fontArialCursive, 9, 206, 466, "IT");
			setCheckboxChecked(379, 466);
			setText(fontArialCursive, 9, 391, 466, "Ringeinspeisung");
	
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

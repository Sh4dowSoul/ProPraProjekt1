package dataStorageAccess;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import applicationLogic.InspectionReportStatistic;
import applicationLogic.Statistic;
import dataStorageAccess.controller.DiagnosisController;
import dataStorageAccess.controller.FlawController;
import dataStorageAccess.controller.StatisticController;
import de.schnettler.AutocompleteSuggestion;

/**
 * @author Niklas Schnettler
 *
 */
public class StatisticAccess {
	
	/**
	 * Export the Statistic of a Company
	 * 
	 * @param companyId - Id of a Company
	 * @param fileName - The full path of the file (the statistic is supposed to be saved to)
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	public static void exportStatisticCompany(int companyId, String fileName) throws SQLException, FileNotFoundException {
		//Get list of Diagnoses
		ArrayList<InspectionReportStatistic> diagnosesList= DiagnosisController.getInspectionReportsForXml(companyId);
		diagnosesList = FlawController.getFlawsOfInspectionReportsForXml(diagnosesList);
		//Wrap result of Diagnosis + Defects in own Class
		Statistic diagnoses = new Statistic(diagnosesList);
			
		XStream xStream = new XStream(new DomDriver());
		xStream.autodetectAnnotations(true);
		xStream.omitField(AutocompleteSuggestion.class, "externalId");
		xStream.omitField(AutocompleteSuggestion.class, "internalId");
		xStream.omitField(AutocompleteSuggestion.class, "isCustomFlaw");
		FileOutputStream fs = new FileOutputStream(fileName);
		xStream.toXML(diagnoses, fs);
	}
}
package dataStorageAccess;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import applicationLogic.Statistic;
import applicationLogic.StatisticResult;
import dataStorageAccess.controller.DiagnosisController;
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
		ArrayList<StatisticResult> diagnosesList= DiagnosisController.getDiagnosesAndDefectsOfCompany(companyId);
		for (StatisticResult diagnosis : diagnosesList) {
			//Get defects from Diagnosis
			diagnosis.setDefects(StatisticController.getDefectsOfDiagnosis(diagnosis.getId()));
		}
		//Wrap result of Diagnosis + Defects in own Class
		Statistic diagnoses = new Statistic(diagnosesList);
			
		XStream xStream = new XStream(new DomDriver());
		xStream.autodetectAnnotations(true);
		xStream.omitField(AutocompleteSuggestion.class, "id");
		FileOutputStream fs = new FileOutputStream(fileName);
		xStream.toXML(diagnoses, fs);
	}
}
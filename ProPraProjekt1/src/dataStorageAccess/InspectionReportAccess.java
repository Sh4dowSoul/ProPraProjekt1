package dataStorageAccess;

import java.sql.SQLException;
import java.util.ArrayList;

import applicationLogic.FlawListElement;
import applicationLogic.InspectionReportFull;
import applicationLogic.InspectionReportStatistic;
import applicationLogic.InspectionResultPreview;
import dataStorageAccess.controller.DiagnosisController;
import dataStorageAccess.controller.FlawListController;

/**
 * @author Niklas Schnettler
 *
 */
public class InspectionReportAccess {
	
	/**
	 * Get a complete InspectionResult (inclusive defectsList) by its id
	 * 
	 * @param diagnosisId - Id of a Diagnosis
	 * @return A complete InspectionResult
	 * @throws SQLException
	 */
	public static InspectionReportFull getCompleteResult(int diagnosisId) throws SQLException {
		InspectionReportFull result = DiagnosisController.getDiagnosis(diagnosisId);
		result.setDefects(FlawListController.getFlawList(diagnosisId));
		return result;
	}
	
	/**
	 * Save a complete InspectionResult in the Database
	 * 
	 * @param result - The complete InspectionResult
	 * @throws SQLException
	 */
	public static void saveNewCompleteResult(InspectionReportFull result) throws SQLException {
		int diagnosisId = DiagnosisController.insertDiagnosis(result);
		FlawListController.insertFlawList(result.getDefects(), diagnosisId);
	}
	
	/**
	 * Update a complete InspectionResult which is already saved in the Database
	 * 
	 * @param result - The complete InspectionResult
	 * @throws SQLException
	 */
	public static void updateCompleteResult(InspectionReportFull result) throws SQLException {
		DiagnosisController.updateDiagnosis(result);
		for (FlawListElement defect : result.getDefects()) {
			//TODO: Fix inserting/updating FlawListElement
			//if (defect.getElementId() != -1) {
			//	DefectController.updateDefect(defect, result.getId());
			//} else {
			//	DefectController.insertDefect(defect, result.getId());
			//}
		}
	}
	
	/**
	 * Get a List of Previews of
	 * 	a) The 10 Last edited Results
	 *  b) All Results
	 * 
	 * @param lastEdited - wether you want to get the last edited Results 
	 * @return A List of ResultPreviews
	 * @throws SQLException
	 */
	public static ArrayList<InspectionResultPreview> getResultsPreview(boolean lastEdited) throws SQLException {
		if(lastEdited) {
			return DiagnosisController.getLastEditedDiagnosesPreview(10);
		} else {
			return DiagnosisController.getDiagnosesPreview();
		}
	}
}
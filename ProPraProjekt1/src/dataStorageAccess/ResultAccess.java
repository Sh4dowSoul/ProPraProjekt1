package dataStorageAccess;

import java.sql.SQLException;
import java.util.ArrayList;

import applicationLogic.DefectResult;
import applicationLogic.ResultComplete;
import applicationLogic.ResultPreview;
import applicationLogic.StatisticResult;
import dataStorageAccess.controller.DefectController;
import dataStorageAccess.controller.DiagnosisController;

/**
 * @author Niklas Schnettler
 *
 */
public class ResultAccess {
	
	/**
	 * Get a complete InspectionResult (inclusive defectsList) by its id
	 * 
	 * @param diagnosisId - Id of a Diagnosis
	 * @return A complete InspectionResult
	 * @throws SQLException
	 */
	public static ResultComplete getCompleteResult(int diagnosisId) throws SQLException {
		ResultComplete result = DiagnosisController.getDiagnosis(diagnosisId);
		result.setDefects(DefectController.getDefectsOffDiagnosis(diagnosisId));
		return result;
	}
	
	/**
	 * Save a complete InspectionResult in the Database
	 * 
	 * @param result - The complete InspectionResult
	 * @throws SQLException
	 */
	public static void saveNewCompleteResult(ResultComplete result) throws SQLException {
		int diagnosisId = DiagnosisController.insertDiagnosis(result);
		for (DefectResult defect : result.getDefects()) {
			DefectController.insertDefect(defect, diagnosisId);
		}
	}
	
	/**
	 * Update a complete InspectionResult which is already saved in the Database
	 * 
	 * @param result - The complete InspectionResult
	 * @throws SQLException
	 */
	public static void updateCompleteResult(ResultComplete result) throws SQLException {
		DiagnosisController.updateDiagnosis(result);
		for (DefectResult defect : result.getDefects()) {
			//TODO update defect in DB
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
	public static ArrayList<ResultPreview> getResultsPreview(boolean lastEdited) throws SQLException {
		if(lastEdited) {
			return DiagnosisController.getLastEditedDiagnosesPreview(10);
		} else {
			return DiagnosisController.getDiagnosesPreview();
		}
	}
	
	/**
	 * Get a List of the Statistic version of a whole inspectionResult (inspectionResult + defects)
	 * 
	 * @param companyId
	 * @return a List of StatisticResults
	 * @throws SQLException
	 */
	public static ArrayList<StatisticResult> getStatisticResultOfCompany(int companyId) throws SQLException {
		return DiagnosisController.getDiagnosesAndDefectsOfCompany(companyId);
	}
}
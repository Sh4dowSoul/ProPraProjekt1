package dataStorageAccess;

import java.sql.SQLException;

import applicationLogic.DefectResult;
import applicationLogic.ResultComplete;
import dataStorageAccess.controller.DefectController;
import dataStorageAccess.controller.DiagnosisController;

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
	
	public static void saveNewCompleteResult(ResultComplete result) throws SQLException {
		DiagnosisController.insertDiagnosis(result);
		for (DefectResult defect : result.getDefects()) {
			DefectController.insertDefect(defect, result.getId());
		}
	}
	
	public static void updateCompleteResult(ResultComplete result) throws SQLException {
		DiagnosisController.updateDiagnosis(result);
		for (DefectResult defect : result.getDefects()) {
			//TODO update defect in DB
		}
	}
}

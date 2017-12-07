package dataStorageAccess;

import java.sql.SQLException;

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
}

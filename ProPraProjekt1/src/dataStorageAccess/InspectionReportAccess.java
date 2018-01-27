package dataStorageAccess;

import java.sql.SQLException;
import java.util.ArrayList;

import org.controlsfx.control.Notifications;

import applicationLogic.ExceptionDialog;
import applicationLogic.FlawListElement;
import applicationLogic.InspectionReportFull;
import applicationLogic.InspectionReportStatistic;
import applicationLogic.InspectionResultPreview;
import dataStorageAccess.controller.DiagnosisController;
import dataStorageAccess.controller.FlawListController;
import javafx.util.Duration;

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
	public static int saveNewCompleteResult(InspectionReportFull result) {
		int diagnosisId = 0;
		try {
			diagnosisId = DiagnosisController.insertDiagnosis(result);
			if (result.getDefects() != null) {
				FlawListController.insertFlawList(result.getDefects(), diagnosisId);
			}
			Notifications.create()
            .title("Erfolgreich gespeichert")
            .text("Der Befundschein wurde erfolgreich gespeichert (Befundscheinname: " + diagnosisId + ")")
            .showInformation();
		} catch (SQLException e) {
			Notifications.create()
            .title("Es ist ein Problem aufgetreten")
            .text("Der Befundschein konnte leider nicht gespeichert werden.")
            .hideAfter(Duration.INDEFINITE)
            .onAction(event -> new ExceptionDialog("Fehler", "Fehler beim Speichern", "Beim Speichern des Befundscheins ist leider ein Fehler aufgetreten.", e))
            .showError();
		}
		return diagnosisId;
	}
	
	/**
	 * Update a complete InspectionResult which is already saved in the Database
	 * 
	 * @param result - The complete InspectionResult
	 * @throws SQLException
	 */
	public static void updateCompleteResult(InspectionReportFull result) {
		try {
			DiagnosisController.updateDiagnosis(result);
			//Delete old FlawList
			FlawListController.removeFlawList(result.getId());
			//Insert new FlawList
			if (result.getDefects() != null) {
				FlawListController.insertFlawList(result.getDefects(), result.getId());
			}
			Notifications.create()
            .title("Erfolgreich gespeichert")
            .text("Der Befundschein wurde erfolgreich gespeichert (überschrieben)")
            .showInformation();
		} catch (SQLException e) {
			Notifications.create()
            .title("Es ist ein Problem aufgetreten")
            .text("Der Befundschein konnte leider nicht gespeichert werden.")
            .hideAfter(Duration.INDEFINITE)
            .onAction(event -> new ExceptionDialog("Fehler", "Fehler beim Speichern", "Beim Speichern des Befundscheins ist leider ein Fehler aufgetreten.", e))
            .showError();
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
package dataStorageAccess;

import java.sql.SQLException;
import java.util.ArrayList;

import org.controlsfx.control.Notifications;

import applicationLogic.ErrorNotification;
import applicationLogic.ExceptionDialog;
import applicationLogic.InspectionReportFull;
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
	 * Get an InspectionReport
	 * 
	 * @param diagnosisId - The Id of the InspectionReport
	 * @return The InspectionReport
	 * @throws SQLException
	 */
	public static InspectionReportFull getCompleteResult(int diagnosisId) throws SQLException {
		InspectionReportFull result = DiagnosisController.getDiagnosis(diagnosisId);
		result.setDefects(FlawListController.getFlawList(diagnosisId));
		return result;
	}
	
	/**
	 * Save a new InspectionReport
	 * 
	 * @param result - The complete InspectionReport
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
			new ErrorNotification("Der Befundschein konnte leider nicht gespeichert werden.", "Fehler beim Speichern des Befundscheins in Datenbank", e);
		}
		return diagnosisId;
	}
	
	/**
	 * Update an InspectionReport which is already saved in the Database
	 * 
	 * @param result - The complete InspectionReport
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
			new ErrorNotification("Der Befundschein konnte leider nicht gespeichert werden.", "Fehler beim Speichern des Befundscheins in Datenbank", e);
		}
	}
	
	/**
	 * Get a List of Previews of
	 * 	a) The 10 Last edited Results
	 *  b) All Results
	 * 
	 * @param lastEdited - Whether you want to get the last edited Results 
	 * @return A List of InspectionReportPreviews
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
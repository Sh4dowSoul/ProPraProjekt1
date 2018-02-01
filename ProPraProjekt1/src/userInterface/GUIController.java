package userInterface;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import com.google.common.io.Files;

import applicationLogic.ErrorNotification;
import applicationLogic.ExceptionDialog;
import dataStorageAccess.DataSource;
import dataStorageAccess.FlawAccess;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 * @author Salih Arslan & Sven Meyer
 *
 */
public class GUIController implements Initializable{
	//*** GENERAL ***
	@FXML private TabPane mainTabPane;
	@FXML private Tab diagnosisTab;
	@FXML private Tab homeTab1;
	@FXML private Tab_Home homeTabController;
	@FXML private Tab_InspectionResult inspectionResultTabController;
	@FXML private Tab_Stats statsTabController;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		homeTabController.setParentController(this);
		inspectionResultTabController.setParentController(this);
		mainTabPane.getSelectionModel().selectedIndexProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
			switch (newValue.intValue()) {
			case 2:
				statsTabController.loadData();
				break;

			default:
				break;
			}
		});
	}
	
	/**
	 * Open InspectionReportTab
	 * 
	 * @param diagnosisId - The Id of the InspectionReport which should be opened
	 * @param editMode - If you want to edit an existing Inspection Report or create a new One
	 */
	public void openDiagnosisTab(int diagnosisId, boolean editMode) {
		//ChangeTab
		mainTabPane.getSelectionModel().select(1);
		diagnosisTab.setDisable(false);
		homeTab1.setDisable(true);
		if (editMode) {
			inspectionResultTabController.importInspectionReport(diagnosisId);
		} else {
			inspectionResultTabController.createNewInspectionReport();
		}
	}	
	
	/**
	 * Close InspectionReportTab
	 */
	public void closeDiagnosis() {
		mainTabPane.getSelectionModel().select(0);
		diagnosisTab.setDisable(true);
		homeTab1.setDisable(false);
		homeTabController.loadReports();
	}
	
	/**
	 * Prepare Export of Custom Flaws
	 * @param add
	 */
	public void exportCustomFlaws(ActionEvent add) {
		FileChooser fileChooser = new FileChooser();
 		  
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Mangel Dateien (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName("Mängel_" + LocalDate.now() +".csv");

        
        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        
        if(file != null){
        	try {
				FlawAccess.exportFlawToCsv(file);
				Notifications.create()
                .title("Erfolgreich gespeichert")
                .text("Die eigens erstellten Mängel wurden erfolgreich exportiert ")
                .onAction(event -> {
					try {
						Desktop.getDesktop().open(file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				})
                .showInformation();
			} catch (SQLException e) {
				new ErrorNotification("Die Mängel konnten leider nicht exportiert werden.", "Fehler beim Laden der Daten aus der Datenbank", e);
			} catch (IOException e) {
				new ErrorNotification("Die Mängel konnten leider nicht exportiert werden.", "Fehler beim Schreiben der Daten an gewünschten Speicherort. Bitte stellen Sie sicher, dass der ausgewählte Speicherort existiert.", e);
			}
        }
	}
	
	/**
	 * Prepare Export of Database (Backup)
	 * 
	 * @param add
	 */
	public void exportDataBase(ActionEvent add) {
		FileChooser fileChooser = new FileChooser();
		
		//Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Datenbank Dateien(*.db)","*.db");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName("Befundscheinverwaltung_" + LocalDate.now() +".db");

        
        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        
        if(file != null) {
        	try {
				Files.copy(new File(DataSource.getDatabasePath()), file);
				Notifications.create()
                .title("Erfolgreich gespeichert")
                .text("Das Backup der Daten wurde erfolgreich gespeichert")
                .showInformation();
			} catch (IOException e) {
				new ErrorNotification("Beim Speichern der Daten ist leider ein Fehler aufgetreten.", "Fehler beim Schreiben der Daten an gewünschten Speicherort. Bitte stellen Sie sicher, dass der ausgewählte Speicherort existiert.", e);
			}
        }
	}
}
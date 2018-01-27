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

import applicationLogic.ExceptionDialog;
import dataStorageAccess.DataSource;
import dataStorageAccess.FlawAccess;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
		mainTabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				switch (newValue.intValue()) {
				case 2:
					statsTabController.loadData();
					break;

				default:
					break;
				}
			}
		});
	}
	
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
	
	public void closeDiagnosis() {
		mainTabPane.getSelectionModel().select(0);
		diagnosisTab.setDisable(true);
		homeTab1.setDisable(false);
		homeTabController.loadReports();
	}
	
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
                .onAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							Desktop.getDesktop().open(file);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				})
                .showInformation();
			} catch (SQLException | IOException e) {
				Notifications.create()
                .title("Es ist ein Problem aufgetreten")
                .text("Die Mängel konnten leider nicht exportiert werden.")
                .hideAfter(Duration.INDEFINITE)
                .onAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						new ExceptionDialog("Export Fehler", "Fehler beim Exportieren", "Beim Exportieren der Mängel ist leider ein Fehler aufgetreten.", e);
					}
                })
                .showError();
			}
        }
	}
	
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
				Notifications.create()
                .title("Es ist ein Problem aufgetreten")
                .text("Die Daten konnten leider nicht gesichert werden")
                .hideAfter(Duration.INDEFINITE)
                .onAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						new ExceptionDialog("Fehler", "Fehler beim Speichern", "Beim Speichern der Daten ist leider ein Fehler aufgetreten.", e);
					}
                })
                .showError();
			}
        }
	}
}
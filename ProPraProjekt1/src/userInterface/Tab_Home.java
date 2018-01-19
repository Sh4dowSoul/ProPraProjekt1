package userInterface;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import applicationLogic.ExceptionDialog;
import applicationLogic.InspectionReportFull;
import applicationLogic.InspectionResultPreview;
import applicationLogic.PDFExport;
import applicationLogic.Util;
import dataStorageAccess.InspectionReportAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * @author Niklas Schnettler, Salih Arslan & Sven Meyer
 *
 */
public class Tab_Home implements Initializable{
	// *** HOME TAB ***
	@FXML private ListView<InspectionResultPreview> recentlyUsedList;
	@FXML private TableView<InspectionResultPreview> tableViewInspectionReports;
	@FXML private TableColumn<InspectionResultPreview,String> tableColumnInspectionReportId;
	@FXML private TableColumn<InspectionResultPreview,String> tableColumnCompany;
	@FXML private TableColumn<InspectionResultPreview,String> tableColumnDate;
	@FXML private TableColumn<InspectionResultPreview,String> tableColumnStatus;
	@FXML private ProgressIndicator diagnosisTableProgress;

	private GUIController mainController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupGUI();
		loadData();
    }
	
	private void setupGUI() {
		//InspectionReport TableView
		tableColumnInspectionReportId.setCellValueFactory(new PropertyValueFactory<InspectionResultPreview,String>("id"));
		tableColumnCompany.setCellValueFactory(new PropertyValueFactory<InspectionResultPreview,String>("companyName"));
		tableColumnDate.setCellValueFactory(new PropertyValueFactory<InspectionResultPreview,String>("niceDate"));
		tableViewInspectionReports.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			openInspectionResultOptionsDialog((InspectionResultPreview) tableViewInspectionReports.getSelectionModel().getSelectedItem());
		});
		
		//Last Edited List
		recentlyUsedList.setCellFactory(param -> new ListCell<InspectionResultPreview>() {
			@Override
			protected void updateItem(InspectionResultPreview item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				}
				else {
					String companyName = item.getCompanyName() != null ? item.getCompanyName() : "Unbekannt";
					setText("Befundschein " + item.getId() + " - Firma " + companyName + " - Änderung " + item.getLastEditedNice() + " - Status TODO");
					setOnMouseClicked(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {
							openInspectionResultOptionsDialog(item);
						}
			        });
				} 
			}
		});
	}

	private void loadData() {
		loadLastEdited();
		loadAllDiagnoses();
	}
	
	public void setParentController(GUIController parentController) {
	    this.mainController = parentController;
	}
	
	/**
	 * Adds diagnosis to the database
	 * @throws SQLException 
	 */
	public void createNewDiagnosisButton(ActionEvent add){
		mainController.openDiagnosisTab(0, false);
	}

	private void openInspectionResultOptionsDialog(InspectionResultPreview item) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Befundschein " + item.getId() + " - " + item.getCompanyName());
		alert.setHeaderText("Aktion für Befundschein " + item.getId() + " wählen");
		alert.setContentText("Befundschein Nr: " +item.getId() +"\nFirma: " + item.getCompanyName()+ "\nZuletzt bearbeitet: " + item.getLastEditedNice());
		alert.initStyle(StageStyle.UTILITY);

		ButtonType editButton = new ButtonType("Bearbeiten");
		ButtonType exportButton = new ButtonType("Exportieren");
		ButtonType cancelButton = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(editButton, exportButton, cancelButton);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == editButton){
			mainController.openDiagnosisTab(item.getId(), true);
		} else if (result.get() == exportButton) {
			try {
				InspectionReportFull inspectionReport= InspectionReportAccess.getCompleteResult(item.getId());
				Boolean isValid = inspectionReport.isValid();
				if (isValid == null) {
					isValid = Util.validateInspectionReport(inspectionReport, false);
				}
				if (isValid) {
					PDFExport.export(inspectionReport);
				} else {
					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setTitle("Befundschein nicht komplett");
					alert2.setHeaderText("Der Befundschein kann nicht exportiert werden.");
					alert2.setContentText("Es wurden nicht alle Pflichtfelder ausgefüllt. Wollen Sie den Befundschein jetzt öffnen um das Problem zu lösen?");

					ButtonType yesButton = new ButtonType("Ja");
					ButtonType noButton = new ButtonType("Nein", ButtonData.CANCEL_CLOSE);

					alert2.getButtonTypes().setAll(yesButton, noButton);

					Optional<ButtonType> result2 = alert2.showAndWait();
					if (result2.get() == yesButton){
						mainController.openDiagnosisTab(item.getId(), true);
					} 
				}
			} catch (SQLException e) {
				Notifications.create()
                .title("Es ist ein Problem aufgetreten")
                .text("Der ausgewählte Befundschein konnte leider nicht geladen werden.")
                .hideAfter(Duration.INDEFINITE)
                .onAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						new ExceptionDialog("Fehler", "Fehler beim laden des Befundscheins", "Beim Laden des Befundscheins ist leider ein Fehler aufgetreten.", e);
					}
                })
                .showError();
			}
		}
	}
	
	/**
	 * Load Last Edited List
	 */
	public void loadLastEdited() {
		final Task<ObservableList<InspectionResultPreview>> lastEditedListTask = new Task<ObservableList<InspectionResultPreview>>() {
            @Override
            protected ObservableList<InspectionResultPreview> call() throws Exception {
        		return FXCollections.observableArrayList(InspectionReportAccess.getResultsPreview(true));
            }
        };
        lastEditedListTask.setOnSucceeded(event ->
        	recentlyUsedList.setItems(lastEditedListTask.getValue())
	    );
        lastEditedListTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + lastEditedListTask.getException())
	    );
	    new Thread(lastEditedListTask).start();
	}
	
	/**
	 * Load all Diagnoses
	 */
	public void loadAllDiagnoses() {
		final Task<ObservableList<InspectionResultPreview>> allDiagnosesTask = new Task<ObservableList<InspectionResultPreview>>() {
            @Override
            protected ObservableList<InspectionResultPreview> call() throws Exception {
        		return FXCollections.observableArrayList(InspectionReportAccess.getResultsPreview(false));
            }
        };
        diagnosisTableProgress.visibleProperty().bind(allDiagnosesTask.runningProperty());
        allDiagnosesTask.setOnSucceeded(event ->
        tableViewInspectionReports.setItems(allDiagnosesTask.getValue())
	    );
        allDiagnosesTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + allDiagnosesTask.getException())
	    );
	    new Thread(allDiagnosesTask).start();
	}
}
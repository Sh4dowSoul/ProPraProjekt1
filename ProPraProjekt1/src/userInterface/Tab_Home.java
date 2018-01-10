package userInterface;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import applicationLogic.InspectionResultPreview;
import applicationLogic.PDFExport;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.StageStyle;

/**
 * @author Niklas Schnettler, Salih Arslan & Sven Meyer
 *
 */
public class Tab_Home implements Initializable{
	// *** HOME TAB ***
	@FXML private ListView<InspectionResultPreview> recentlyUsedList;
	@FXML private Button sortAlphaBtn;
	@FXML private TableView<InspectionResultPreview> companyTableView;
	@FXML private TableColumn<InspectionResultPreview,String> diagnosisCompany;
	@FXML private TableColumn<InspectionResultPreview,String> diagnosisId;
	@FXML private TableColumn<InspectionResultPreview,String> diagnosisDate;
	@FXML private ProgressIndicator diagnosisTableProgress;

	private GUIController mainController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupLastEditedList();
		prepareTable();
		loadLastEdited();
		loadAllDiagnoses();
    }
	
	public void setParentController(GUIController parentController) {
	    this.mainController = parentController;
	}
	
	private void prepareTable() {
		diagnosisId.setCellValueFactory(new PropertyValueFactory<InspectionResultPreview,String>("id"));
		diagnosisCompany.setCellValueFactory(new PropertyValueFactory<InspectionResultPreview,String>("companyName"));
		diagnosisDate.setCellValueFactory(new PropertyValueFactory<InspectionResultPreview,String>("niceDate"));
		
		companyTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			createDignosisOptionsDialog((InspectionResultPreview) companyTableView.getSelectionModel().getSelectedItem());
		});
	}
	
	
	
	private void setupLastEditedList() {
		recentlyUsedList.setCellFactory(param -> new ListCell<InspectionResultPreview>() {
			@Override
			protected void updateItem(InspectionResultPreview item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				}
				else {
					setText("Befundschein " + item.getId() + " - Firma " + item.getCompanyName() + " - Änderung " + item.getLastEditedNice());
					setOnMouseClicked(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {
							createDignosisOptionsDialog(item);
						}

						
			        });
				} 
			}
		});
	}
	
	/**
	 * Adds diagnosis to the database
	 * @throws SQLException 
	 */
	public void createNewDiagnosisButton(ActionEvent add){
		mainController.openDiagnosisTab(-1);
	}

	private void createDignosisOptionsDialog(InspectionResultPreview item) {
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
			mainController.setEditMode(true);
			mainController.openDiagnosisTab(item.getId());
		} else if (result.get() == exportButton) {
			PDFExport.export(item.getId());
		}  else {
		    // ... user chose CANCEL or closed the dialog
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
        	companyTableView.setItems(allDiagnosesTask.getValue())
	    );
        allDiagnosesTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + allDiagnosesTask.getException())
	    );
	    new Thread(allDiagnosesTask).start();
	}
}
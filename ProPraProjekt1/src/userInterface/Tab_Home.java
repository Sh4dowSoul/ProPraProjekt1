package userInterface;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import applicationLogic.PDFExport;
import applicationLogic.ResultPreview;
import dataStorageAccess.ResultAccess;
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
	@FXML private ListView<ResultPreview> recentlyUsedList;
	@FXML private Button sortAlphaBtn;
	@FXML private TableView<ResultPreview> companyTableView;
	@FXML private TableColumn<ResultPreview,String> diagnosisCompany;
	@FXML private TableColumn<ResultPreview,String> diagnosisId;
	@FXML private TableColumn<ResultPreview,String> diagnosisDate;
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
		diagnosisId.setCellValueFactory(new PropertyValueFactory<ResultPreview,String>("id"));
		diagnosisCompany.setCellValueFactory(new PropertyValueFactory<ResultPreview,String>("companyName"));
		diagnosisDate.setCellValueFactory(new PropertyValueFactory<ResultPreview,String>("niceDate"));
		
		companyTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			createDignosisOptionsDialog((ResultPreview) companyTableView.getSelectionModel().getSelectedItem());
		});
	}
	
	
	
	private void setupLastEditedList() {
		recentlyUsedList.setCellFactory(param -> new ListCell<ResultPreview>() {
			@Override
			protected void updateItem(ResultPreview item, boolean empty) {
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

	private void createDignosisOptionsDialog(ResultPreview item) {
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
	private void loadLastEdited() {
		final Task<ObservableList<ResultPreview>> lastEditedListTask = new Task<ObservableList<ResultPreview>>() {
            @Override
            protected ObservableList<ResultPreview> call() throws Exception {
        		return FXCollections.observableArrayList(ResultAccess.getResultsPreview(true));
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
	private void loadAllDiagnoses() {
		final Task<ObservableList<ResultPreview>> allDiagnosesTask = new Task<ObservableList<ResultPreview>>() {
            @Override
            protected ObservableList<ResultPreview> call() throws Exception {
        		return FXCollections.observableArrayList(ResultAccess.getResultsPreview(false));
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
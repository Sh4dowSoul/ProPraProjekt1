package userInterface;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import applicationLogic.ErrorNotification;
import applicationLogic.ExceptionDialog;
import applicationLogic.InspectionResultPreview;
import applicationLogic.PDFExport;
import dataStorageAccess.InspectionReportAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	
	//Tasks
	private LoadReportstask loadLastEditedReports;
	private LoadReportstask loadAllReports;
	
	//Parent Controller
	public void setParentController(GUIController parentController) {
	    this.mainController = parentController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupGUI();
		prepareTasks();
		loadReports();
    }
	
	/**
	 * Load Lists of InspectionReport Previews (Last EditedList, All Table)
	 */
	protected void loadReports() {
		loadLastEditedReports.restart();
		loadAllReports.restart();
	}

	/**
	 * Prepare Tasks to load InspectionReport Previews
	 */
	private void prepareTasks() {
		// Load Last Edited Task
		loadLastEditedReports = new LoadReportstask(true);
		loadLastEditedReports.setOnSucceeded(event ->{
			recentlyUsedList.setItems(loadLastEditedReports.getValue());
			recentlyUsedList.refresh();
		});
		
		// Load all Task
		loadAllReports = new LoadReportstask(false);
		diagnosisTableProgress.visibleProperty().bind(loadAllReports.runningProperty());
		loadAllReports.setOnSucceeded(event -> {
			tableViewInspectionReports.setItems(loadAllReports.getValue());
			tableViewInspectionReports.refresh();
		});
	}

	/**
	 * Setup the GUI
	 */
	private void setupGUI() {
		//InspectionReport TableView
		tableColumnInspectionReportId.setCellValueFactory(new PropertyValueFactory<InspectionResultPreview,String>("id"));
		tableColumnCompany.setCellValueFactory(new PropertyValueFactory<InspectionResultPreview,String>("companyName"));
		tableColumnDate.setCellValueFactory(new PropertyValueFactory<InspectionResultPreview,String>("niceDate"));
		tableColumnStatus.setCellValueFactory(new PropertyValueFactory<InspectionResultPreview,String>("validString"));
		tableViewInspectionReports.setRowFactory(tr -> {
            TableRow<InspectionResultPreview> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
            	openInspectionResultOptionsDialog(row.getItem());
            });
            return row;
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
					setText("Befundschein " + item.getId() + " - " + companyName + " - " + item.getLastEditedNice() + " - " + item.getValidString() );
					setOnMouseClicked(event -> openInspectionResultOptionsDialog(item));
				} 
			}
		});
	}

	
	
	/**
	 * Handle newInspectionReport Button Clicks
	 * @throws SQLException 
	 */
	public void createNewDiagnosisButton(ActionEvent add){
		mainController.openDiagnosisTab(0, false);
	}

	/**
	 * Open Dialog asking what to do with a selected InspectionReport
	 * 
	 * @param item
	 */
	private void openInspectionResultOptionsDialog(InspectionResultPreview item) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Befundschein " + item.getId() + " - " + item.getCompanyName());
		alert.setHeaderText("Aktion für Befundschein " + item.getId() + " wählen");
		alert.setContentText("Befundschein Nr: " +item.getId() +"\nFirma: " + item.getCompanyName()+ "\nZuletzt bearbeitet: " + item.getLastEditedNice());
		alert.initStyle(StageStyle.UTILITY);

		ButtonType editButton = new ButtonType("Bearbeiten");
		ButtonType exportButton = new ButtonType("Exportieren");
		ButtonType cancelButton = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(editButton, cancelButton);
		if (item.isValid()) {
			alert.getButtonTypes().add(exportButton);	
		}

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == editButton){
			mainController.openDiagnosisTab(item.getId(), true);
		} else if (result.get() == exportButton) {
			try {
				PDFExport.export(InspectionReportAccess.getCompleteResult(item.getId()));
			} catch (SQLException e) {
                new ErrorNotification("Beim Laden des Befundscheins ist leider ein Fehler aufgetreten.", "Fehler beim Laden der Daten aus der Datenbank" ,e);
			}
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////// TASKS
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Task to load InspectionReports
	 * 
	 * @author Niklas Schnettler
	 *
	 */
	private static class LoadReportstask extends Service<ObservableList<InspectionResultPreview>> {
		private boolean lastEdited;
		
		public LoadReportstask (boolean lastEdited) {
			this.lastEdited = lastEdited;
		}
		
		protected Task<ObservableList<InspectionResultPreview>> createTask() {
			return new Task<ObservableList<InspectionResultPreview>>() {
				protected ObservableList<InspectionResultPreview> call() throws SQLException {
					return FXCollections.observableArrayList(InspectionReportAccess.getResultsPreview(lastEdited));
				}
			};
		}
	}
}
package userInterface;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import applicationLogic.ResultPreview;
import dataStorageAccess.controller.DiagnosisController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.StageStyle;

public class Tab_Home implements Initializable{
	// *** HOME TAB ***
	@FXML private ListView<ResultPreview> recentlyUsedList;
	@FXML private Button sortAlphaBtn;
	@FXML private TableView companyTableView;
	@FXML private TableColumn compNameColumn;
	@FXML private TableColumn compDiagnosisColumn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupLastEditedList();
		loadLastEdited();
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
							System.out.println("TEST");
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
							    // ... user chose "One"
							} else if (result.get() == exportButton) {
							    // ... user chose "Two"
							}  else {
							    // ... user chose CANCEL or closed the dialog
							}
						}
			        });
				} 
			}
		});
	}


	/**
	 * Load Last Edited List
	 */
	private void loadLastEdited() {
		final Task<ObservableList<ResultPreview>> lastEditedListTask = new Task<ObservableList<ResultPreview>>() {
            @Override
            protected ObservableList<ResultPreview> call() throws Exception {
        		return FXCollections.observableArrayList(DiagnosisController.getLastEditedDiagnosesPreview(5));
            }
        };
        //statCompanyProgress.visibleProperty().bind(companyListTask.runningProperty());
        lastEditedListTask.setOnSucceeded(event ->
        	recentlyUsedList.setItems(lastEditedListTask.getValue())
	    );
        lastEditedListTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + lastEditedListTask.getException())
	    );
	    new Thread(lastEditedListTask).start();
	}
}

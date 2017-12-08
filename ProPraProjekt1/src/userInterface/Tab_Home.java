package userInterface;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import applicationLogic.Company;
import applicationLogic.ResultComplete;
import applicationLogic.ResultPreview;
import dataStorageAccess.controller.DiagnosisController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
					setOnMouseClicked(ev -> System.out.println("SELECTED ID " + item.getId()));
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

package userInterface;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

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
}
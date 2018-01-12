package userInterface;

import java.net.URL;
import java.util.ResourceBundle;


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
	
	private boolean editMode;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		homeTabController.setParentController(this);
		inspectionResultTabController.setParentController(this);
	}
	
	public void openDiagnosisTab(int diagnosisId) {
		//ChangeTab
		mainTabPane.getSelectionModel().select(1);
		diagnosisTab.setDisable(false);
		homeTab1.setDisable(true);
		if (editMode) {
			inspectionResultTabController.importInspectionReport(diagnosisId);
		} else {
			inspectionResultTabController.prepare();
		}
	}	
	
	public void closeDiagnosis() {
		mainTabPane.getSelectionModel().select(0);
		diagnosisTab.setDisable(true);
		homeTab1.setDisable(false);
		homeTabController.loadAllDiagnoses();
		homeTabController.loadLastEdited();
		setEditMode(false);
	}
	
	public void setEditMode(boolean editing) {
		editMode = editing;
	}
	
	public boolean getEditMode() {
		return editMode;
	}
}
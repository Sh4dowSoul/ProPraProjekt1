package userInterface;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;

public class GUIController implements Initializable{
	//*** GENERAL ***
	@FXML private TabPane mainTabPane;
	@FXML private Tab_Home homeTabController;
	@FXML private Tab_InspectionResult inspectionResultTabController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		homeTabController.setParentController(this);
	}
	
	public void changeTab(int to) {
		//ChangeTab
		mainTabPane.getSelectionModel().select(1);
		//Load diagnosis
		//...
	}
	
	public void editDiagnosis(int diagnosisId) {
		try {
			inspectionResultTabController.editDiagnosis(diagnosisId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package userInterface;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;

public class GUIController implements Initializable{
	//*** GENERAL ***
	@FXML private TabPane mainTabPane;
	@FXML private Tab_Home homeTabController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		homeTabController.setParentController(this);
	}
	
	public void changeTab() {
		//ChangeTab
		mainTabPane.getSelectionModel().select(1);
		//Load diagnosis
		//...
	}
}

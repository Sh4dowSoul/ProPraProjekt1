package userInterface;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Tab_Home implements Initializable{
	// *** HOME TAB ***
	@FXML private Button sortAlphaBtn;
	@FXML private TableView companyTableView;
	@FXML private TableColumn compNameColumn;
	@FXML private TableColumn compDiagnosisColumn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
    }
}

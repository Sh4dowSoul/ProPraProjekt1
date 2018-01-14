package userInterface;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Tab_Manager implements Initializable {

//	*** ADD NEW DEFECT ***
	@FXML private TextField newIdField;
	@FXML private TextField newDefectField;
	@FXML private Button clearNewDefectButton;
	@FXML private Button createNewDefectButton;
	
//	*** EDIT A DEFECT ***
	@FXML private TextField toEditDefectField;
	@FXML private TextField toEditDefectIdField;
	@FXML private TextField newEditDefectField;
	@FXML private TextField newEditDefectIdField;
	@FXML private Button clearEditButton;
	@FXML private Button editDefectField;
	@FXML private Button editDefectIdField;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		clearNew();
		clearEdit();
		// TODO Auto-generated method stub
		
	}
	
	public void clearNew() {
		newDefectField.clear();
		newIdField.clear();
		
	}
		
	public void clearEdit() {
		toEditDefectField.clear();
		toEditDefectIdField.clear();
		newEditDefectField.clear();
		newEditDefectIdField.clear();
		
	}
	
	
	
	
	
	
	
	
	
	

}

package userInterface;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Tab_PlantBtn {

	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void changeScreenDiagnosis (ActionEvent event) throws IOException{
		
	Parent tableViewParent = FXMLLoader.load(getClass().getResource("GUI_Diagnosis.fxml"));
	Scene tableViewScene = new Scene (tableViewParent);
	
	Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
	window.setScene(tableViewScene);
	window.show();
	
}
	
	
	
	
	
	
	
	
	
	
}

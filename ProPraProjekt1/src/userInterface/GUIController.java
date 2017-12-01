package userInterface;

import java.awt.List;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import applicationLogic.PDFExport;
import dataStorageAccess.controller.BranchController;
import dataStorageAccess.controller.DefectController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GUIController implements Initializable{
	@FXML
	private Label testLabel;
	@FXML private TextField defectSearchField;
	
	public void add(ActionEvent add) {
		testLabel.setText("jojo");
	}
	
	public void pdfExport(ActionEvent pdfExport) throws IOException {
		PDFExport.export();
	}
	
	public void xmlExport(ActionEvent xmlExport) throws IOException {
		
	}
	
	public void stats(ActionEvent stats) {
		
	}
	
	public void sortByCompany(ActionEvent sortByCompany) {
		
	}
	
	public void sortByName(ActionEvent sortByName) {
		
	}
	
	public void loadVN(ActionEvent loadVN) {
		
	}
	
	public void loadPlant(ActionEvent loadPlant) {
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
 
        try {
        	AutoCompletionBinding ab = TextFields.bindAutoCompletion(defectSearchField, DefectController.getAllDefects() );
        	ab.setMinWidth(600);
        	//ab.setOnAutoCompleted(e -> System.out.println(ab.getOnAutoCompleted())); --> Wenn man etwas ausgewählt hat
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}

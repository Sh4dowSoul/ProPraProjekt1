package userInterface;

import java.awt.List;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import applicationLogic.Company;
import applicationLogic.PDFExport;
import dataStorageAccess.controller.BranchController;
import dataStorageAccess.controller.DefectController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class GUIController implements Initializable{
	@FXML
	private Label testLabel;
	@FXML private TextField defectSearchField;
	@FXML private ListView<Company> companyList;
	
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
		//TODO: �ber Threads nachdenken.
        try {
        	//AutoComplete
        	AutoCompletionBinding ab = TextFields.bindAutoCompletion(defectSearchField, DefectController.getAllDefects() );
        	ab.setMinWidth(600);
        	//ab.setOnAutoCompleted(e -> System.out.println(ab.getOnAutoCompleted())); --> Wenn man etwas ausgew�hlt hat
        	
    		//CompanyList
    		ObservableList<Company> items = FXCollections.observableArrayList(dataStorageAccess.controller.CompanyController.getCompanies());
    		companyList.setItems(items);
    		companyList.setCellFactory(param -> new ListCell<Company>() {
    			@Override
    			protected void updateItem(Company item, boolean empty) {
    				super.updateItem(item, empty);
    				if (empty || item == null) {
    					setText(null);
    					setGraphic(null);
    				}
    				else {
    					setText(item.getName() + " - ID: " + item.getId());
    				} 
    			}
    		});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}

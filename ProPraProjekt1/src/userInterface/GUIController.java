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
import applicationLogic.Defect;
import applicationLogic.PDFExport;
import applicationLogic.StatisticElement;
import dataStorageAccess.controller.BranchController;
import dataStorageAccess.controller.DefectController;
import dataStorageAccess.controller.StatisticController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class GUIController implements Initializable{
//*** GENAERL ***
	@FXML private TabPane mainTabPane;
	
// *** HOME TAB ***
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
	
	
// *** BEFUNDSCHEIN TAB ***
	@FXML private TextField defectSearchField;
	@FXML private Button vnLoadBtn;
	@FXML private Button plantLoadBtn;
	@FXML private TextField compNameField;
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
//	@FXML private
	
	
//*** STATISTIC TAB ***
	@FXML private ListView<Company> companyList;
//	@FXML private ListView<Business> BusinessList;
	@FXML private TableView statisticTableView;
	@FXML private TableColumn statsDefectsColumn;
	@FXML private TableColumn statsDefectDescriptionColumn;
	@FXML private TableColumn statsQuantityColumn;
	@FXML private ProgressIndicator statCompanyProgress;
	@FXML private ProgressIndicator statBranchProgress;
	@FXML private ProgressIndicator statResultProgress;
//	@FXML private für xml export..
	
	
	private boolean statsFirstTme = true;
	private AutoCompletionBinding ab;
	
	public void add(ActionEvent add) throws IOException{
		
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
		mainTabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
	        switch(newTab.getId()) {
	        case "statTab":
	        	if (statsFirstTme) {
	        		loadCompanyList();
	        		statsFirstTme = false;
	        	}
	        	
	        }
	    });
		setupCompanyList();
		prepareAutocomplete();
		prepareStatsTable();	
    }
	
	
	private void prepareStatsTable() {
    	statsDefectsColumn.setCellValueFactory(new PropertyValueFactory<StatisticElement,String>("defectId"));
    	statsDefectDescriptionColumn.setCellValueFactory(new PropertyValueFactory<StatisticElement,String>("defectDescription"));
    	statsQuantityColumn.setCellValueFactory(new PropertyValueFactory<StatisticElement,String>("numberOccurrence"));
	}

	/**
	 * Prepares the Autocomplete TextField
	 */
	private void prepareAutocomplete() {
		final Task<ArrayList<Defect>> autocompleteTask = new Task<ArrayList<Defect>>() {
            @Override
            protected ArrayList<Defect> call() throws Exception {
        		return DefectController.getAllDefects();
            }
        };
        autocompleteTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
    		@Override
    		public void handle(final WorkerStateEvent event) {
    			ab = TextFields.bindAutoCompletion(defectSearchField, autocompleteTask.getValue());
    			ab.setMinWidth(600);
    		}
    	});
        autocompleteTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + autocompleteTask.getException())
	    );
	    new Thread(autocompleteTask).start();
	}
	
	
	/**
	 * Prepares the Company List
	 * 		CellFactory: Content of the rows
	 * 		Listener: Get selected row
	 */
	private void setupCompanyList() {
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
		companyList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Company>() {
			@Override
			public void changed(ObservableValue<? extends Company> observable, Company oldValue, Company newValue) {
				loadStatisticList(newValue.getId());
			}
		});
	}

	
	/**
	 * Loads the Companies in a Background Task
	 */
	private void loadCompanyList() {
		final Task<ObservableList<Company>> companyListTask = new Task<ObservableList<Company>>() {
            @Override
            protected ObservableList<Company> call() throws Exception {
        		return FXCollections.observableArrayList(dataStorageAccess.controller.CompanyController.getCompanies());
            }
        };
        statCompanyProgress.visibleProperty().bind(companyListTask.runningProperty());
	    companyListTask.setOnSucceeded(event ->
	        companyList.setItems(companyListTask.getValue())
	        );
	    companyListTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + companyListTask.getException())
	    );
	    new Thread(companyListTask).start();
	}
	
	
	/**
	 * Loads the Statistic of a company in a Background Task
	 * @param companyId - Id of the selected Company
	 */
	private void loadStatisticList(int companyId) {
		// TODO Auto-generated method stub
		final Task<ObservableList<StatisticElement>> statisticListTask = new Task<ObservableList<StatisticElement>>() {
            @Override
            protected ObservableList<StatisticElement> call() throws Exception {
        		return FXCollections.observableArrayList(StatisticController.getMostFrequentDefectCompany(companyId));
            }
        };
        statResultProgress.visibleProperty().bind(statisticListTask.runningProperty());
        statisticListTask.setOnSucceeded(event ->
	    	statisticTableView.setItems(statisticListTask.getValue())
	    );
        statisticListTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + statisticListTask.getException())
	    );
	    new Thread(statisticListTask).start();
	}
	
}

package userInterface;

import java.awt.List;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import applicationLogic.Branch;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class Tab_Stats implements Initializable{
	
	@FXML private ListView<Company> companyList;
	@FXML private Tab companyListTab;
	@FXML private ListView<Branch> branchList;
	@FXML private Tab branchListTab;
	@FXML private TableView statisticTableView;
	@FXML private TableColumn statsDefectsColumn;
	@FXML private TableColumn statsDefectDescriptionColumn;
	@FXML private TableColumn statsQuantityColumn;
	@FXML private Button xmlExpBtn;
	@FXML private ProgressIndicator statCompanyProgress;
	@FXML private ProgressIndicator statBranchProgress;
	@FXML private ProgressIndicator statResultProgress;
  
  
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//mainTabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
	    //    switch(newTab.getId()) {
	     //   case "statTab":
	      //  	if (statsFirstTme) {
	        		loadCompanyList();
	        		loadBranchesList();
	        //		statsFirstTme = false;
	        //	}
	        	
	       // }
	  //  });
		setupStatisticLists();
		prepareStatsTable();	
    }
	
	
	private void prepareStatsTable() {
    	statsDefectsColumn.setCellValueFactory(new PropertyValueFactory<StatisticElement,String>("defectId"));
    	statsDefectDescriptionColumn.setCellValueFactory(new PropertyValueFactory<StatisticElement,String>("defectDescription"));
    	statsQuantityColumn.setCellValueFactory(new PropertyValueFactory<StatisticElement,String>("numberOccurrence"));
	}

	
	/**
	 * Prepares Company and Branch Lists
	 * 		CellFactory: Content of the rows
	 * 		Listener: Get selected row
	 */
	private void setupStatisticLists() {
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
				loadCompanyStatistic(newValue.getId());
			}
		});
		
		
		branchList.setCellFactory(param -> new ListCell<Branch>() {
			@Override
			protected void updateItem(Branch item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				}
				else {
					setText(item.getId() + " - " + item.getDescription());
				} 
			}
		});
		branchList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Branch>() {
			@Override
			public void changed(ObservableValue<? extends Branch> observable, Branch oldValue, Branch newValue) {
				loadBranchStatistic(newValue.getId());
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
        		return FXCollections.observableArrayList(dataStorageAccess.controller.CompanyController.getCompaniesWithDefect());
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
	 * Loads the Branches in a Background Task
	 */
	private void loadBranchesList() {
		final Task<ObservableList<Branch>> branchListTask = new Task<ObservableList<Branch>>() {
            @Override
            protected ObservableList<Branch> call() throws Exception {
        		return FXCollections.observableArrayList(dataStorageAccess.controller.BranchController.getAllBranchesWithDefect());
            }
        };
        statBranchProgress.visibleProperty().bind(branchListTask.runningProperty());
        branchListTask.setOnSucceeded(event ->
        branchList.setItems(branchListTask.getValue())
	        );
        branchListTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + branchListTask.getException())
	    );
	    new Thread(branchListTask).start();
	}
	
	
	/**
	 * Loads the Statistic of a company in a Background Task
	 * @param companyId - Id of the selected Company
	 */
	private void loadCompanyStatistic(int companyId) {
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
	
	/**
	 * Loads the Statistic of a Branch in a Background Task
	 * @param companyId - Id of the selected Company
	 */
	private void loadBranchStatistic(int branchId) {
		// TODO Auto-generated method stub
		final Task<ObservableList<StatisticElement>> branchStatisticListTask = new Task<ObservableList<StatisticElement>>() {
            @Override
            protected ObservableList<StatisticElement> call() throws Exception {
        		return FXCollections.observableArrayList(StatisticController.getMostFrequentDefectBranch(branchId));
            }
        };
        statResultProgress.visibleProperty().bind(branchStatisticListTask.runningProperty());
        branchStatisticListTask.setOnSucceeded(event ->
	    	statisticTableView.setItems(branchStatisticListTask.getValue())
	    );
        branchStatisticListTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + branchStatisticListTask.getException())
	    );
	    new Thread(branchStatisticListTask).start();
	}
	
}

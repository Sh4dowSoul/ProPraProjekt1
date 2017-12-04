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

public class GUIController implements Initializable{
//*** GENERAL ***
	@FXML private TabPane mainTabPane;
	
// *** HOME TAB ***
	@FXML private Button sortAlphaBtn;
	@FXML private TableView companyTableView;
	@FXML private TableColumn compNameColumn;
	@FXML private TableColumn compDiagnosisColumn;
	
// *** BEFUNDSCHEIN TAB ***
// Versicherungsnehmer Adresse
	@FXML private Button vnLoadBtn;
	@FXML private TextField compNameField;
	@FXML private TextField streetCompField;
	@FXML private TextField compZipField;
	@FXML private TextField compCityField;
	
// Risikoanschrift
	@FXML private Button plantLoadBtn;
	@FXML private TextField plantStreetField;
	@FXML private TextField plantZipField;
	@FXML private TextField plantCityField;
	@FXML private TextField plantCompanionField;
	@FXML private TextField plantExpertField;
	@FXML private TextField plantAnerkNrField;
	@FXML private TextField plantInspectionField;
	@FXML private TextField plantInspectionTimeField;
	
//Art des Betriebes oder der Anlage
	@FXML private TextField branchName;
	@FXML private RadioButton freqYesBtn;
	@FXML private RadioButton freqNoBtn;
	@FXML private RadioButton precautionYesBtn;
	@FXML private RadioButton precautionNoBtn;
	@FXML private TextField precautionField;
	@FXML private RadioButton comleteYesBtn;
	@FXML private RadioButton comleteNoBtn;
	@FXML private TextField completeDateField;
	@FXML private TextField completeReasonField;
	@FXML private RadioButton changesSinceLastExaminationYesBtn;
	@FXML private RadioButton changesSinceLastExaminationNoBtn;
	@FXML private RadioButton changesSinceLastExaminationFirstExaminationBtn;
	@FXML private RadioButton defectsLastExaminationYesBtn;
	@FXML private RadioButton defectsLastExaminationNoBtn;
	@FXML private RadioButton defectsLastExaminationNoReportBtn;
	
//Gesamtbeurteilung der Anlage
	@FXML private RadioButton dangerCategorieGroupABtn;
	@FXML private RadioButton dangerCategorieGroupBBtn;
	@FXML private RadioButton dangerCategorieGroupCBtn;
	@FXML private RadioButton dangerCategorieGroupDBtn;
	@FXML private TextField dangerCategoryExtensionField;
	
//Prüfergebnis
	@FXML private CheckBox noDefectsBtn;
	@FXML private CheckBox defectsAttachedBtn;
	@FXML private TextField defectsAttachedDateField;
	@FXML private CheckBox removeDefectsImmediatelyBtn;
	@FXML private TextField pageCount;

// Messungen
	@FXML private RadioButton isoMinYesBtn;
	@FXML private RadioButton IsoMinNoBtn;
	@FXML private RadioButton isoProtocolYesBtn;
	@FXML private RadioButton isoProtocolNoBtn;
	@FXML private RadioButton isoCompensationYesBtn;
	@FXML private RadioButton isoCompensationNoBtn;
	@FXML private TextField isoCompensationCommentField;
	@FXML private RadioButton rcdAllBtn;
	@FXML private TextField rcdPercentageField;
	@FXML private RadioButton rcdNotBtn;
	@FXML private TextField rcdCommentField;
	@FXML private RadioButton resistanceYesBtn;
	@FXML private TextField resistancePercentageField;
	@FXML private RadioButton resistanceNoBtn;
	@FXML private TextField resistanceCommentField;
	@FXML private RadioButton thermicYesBtn;
	@FXML private RadioButton thermicNoBtn;
	@FXML private TextField thermicCommentField;
	
//Ortsveränderliche Betriebsmittel
	@FXML private RadioButton portableUtilitiesYesBtn;
	@FXML private RadioButton portableUtilitiesNoBtn;
	@FXML private RadioButton externalPortableUtilitiesYesBtn;
	@FXML private RadioButton externalPortableUtilitiesNoBtn;
	@FXML private RadioButton externalPortableUtilitiesNrBtn;
	
//Allgemeine Informationen zur geprüften elektrischen Anlage
	@FXML private RadioButton supplySystemTNBtn;
	@FXML private RadioButton supplySystemTTBtn;
	@FXML private RadioButton supplySystemITBtn;
	@FXML private RadioButton supplySystemCircleBtn;
	@FXML private TextField powerConsumptionField;
	@FXML private TextField externalPowerPercentageField;
	@FXML private TextField maxCapacityPercentageField;
	@FXML private TextField protectedCirclesPercentageField;
	@FXML private RadioButton hardWiredLoadsUnder250Btn;
	@FXML private RadioButton hardWiredLoadsUnder500Btn;
	@FXML private RadioButton hardWiredLoadsUnder1000Btn;
	@FXML private RadioButton hardWiredLoadsUnder5000Btn;
	@FXML private RadioButton hardWiredLoadsAbove5000Btn;
	@FXML private TextArea furtherExplanationsField;

// Anhang A
	@FXML private TextField defectSearchField;
	@FXML private TextField diagnosisDate;
	@FXML private TableView defectTableView;
	@FXML private TableColumn ifdnrColumn;
	@FXML private TableColumn dangerColumn;
	@FXML private TableColumn buildingColumn;
	@FXML private TableColumn RoomColumn;
	@FXML private TableColumn maschineColumn;
	@FXML private TableColumn branchColumn;
	@FXML private TableColumn recommodationClumn;
	@FXML private Button AddDiagnosisBtn;
	@FXML private Button pdfExpBtn;
	
//*** STATISTIC TAB ***
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
	        		loadBranchesList();
	        		statsFirstTme = false;
	        	}
	        	
	        }
	    });
		setupStatisticLists();
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

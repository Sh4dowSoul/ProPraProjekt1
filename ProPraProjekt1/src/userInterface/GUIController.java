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
import applicationLogic.StatisticElement;
import dataStorageAccess.controller.BranchController;
import dataStorageAccess.controller.DefectController;
import dataStorageAccess.controller.StatisticController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class GUIController implements Initializable{

// *** HOME TAB ***
//	@FXML private
	@FXML private Label sortAlphaBtn;
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
	@FXML private RadioButton noDefectsBtn;
	@FXML private RadioButton defectsAttachedBtn;
	@FXML private TextField defectsAttachedDateField;
	@FXML private RadioButton removeDefectsImmediatelyBtn;
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
	@FXML private TextField furtherExplanationsField;

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
//	@FXML private ListView<Branch> branchList;
	@FXML private Tab branchListTab;
	@FXML private TableView statisticTableView;
	@FXML private TableColumn statsDefectsColumn;
	@FXML private TableColumn statsDefectDescriptionColumn;
	@FXML private TableColumn statsQuantityColumn;
	@FXML private Button xmlExpBtn;
	
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
		//TODO: Über Threads nachdenken.
        try {
        	//AutoComplete
        	AutoCompletionBinding ab = TextFields.bindAutoCompletion(defectSearchField, DefectController.getAllDefects() );
        	ab.setMinWidth(600);
        	//ab.setOnAutoCompleted(e -> System.out.println(ab.getOnAutoCompleted())); --> Wenn man etwas ausgewählt hat
        	
        	//Tabelle
        	statsDefectsColumn.setCellValueFactory(new PropertyValueFactory<StatisticElement,String>("defectId"));
        	statsDefectDescriptionColumn.setCellValueFactory(new PropertyValueFactory<StatisticElement,String>("defectDescription"));
        	statsQuantityColumn.setCellValueFactory(new PropertyValueFactory<StatisticElement,String>("numberOccurrence"));
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
    		companyList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Company>() {
    			@Override
                public void changed(ObservableValue<? extends Company> observable, Company oldValue, Company newValue) {
            		System.out.println("Selected " + newValue.getId()); 
            		//Get statistic by company
					try {
	        			statisticTableView.setItems(FXCollections.observableArrayList(StatisticController.getMostFrequentDefectCompany(newValue.getId())));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
    		});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}

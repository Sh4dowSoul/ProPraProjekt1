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
import applicationLogic.Diagnosis;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class GUIController implements Initializable{
	@FXML private TextField defectSearchField;
	@FXML private ListView<Company> companyList;
	@FXML private TableView statisticTableView;
	@FXML private TableColumn statsDefectsColumn;
	@FXML private TableColumn statsDefectDescriptionColumn;
	@FXML private TableColumn statsQuantityColumn;
	
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
	
	public void addDiagnosis(add) {
		int id;
		String lastEdited;
		int plantId;
		String companion;
		String surveyor;
		int vdsApprovalNr;
		String examinationDate;
		double examinationDuration;
		boolean frequencyControlledUtilities;
		boolean precautionsDeclared;
		String precautionsDeclaredLocation;
		boolean examinationComplete;
		String subsequentExaminationDate;
		String examinationIncompleteReason;
		int changesSinceLastExamination;
		int defectsLastExaminationFixed;
		int dangerCategory;
		String dangerCategoryDescription;
		boolean examinationResultNoDefect;
		boolean examinationResultDefect;
		boolean examinationResultDanger;
		int pages;
		boolean isolationChecked;
		boolean isolationMesasurementProtocols;
		boolean isolationCompensationMeasures;
		String isolationCompensationMeasuresAnnotation;
		Boolean rcdAvailable;
		int rcdAvailablePercent;
		String rcdAnnotation;
		boolean resistance;
		int resistanceNumber;
		String resistanceAnnotation;
		boolean thermalAbnormality;
		String thermalAbnormalityAnnotation;
		boolean internalPortableUtilities;
		boolean externalPortableUtilities;
		int supplySystem;
		int energyDemand;
		int maxEnergyDemandExternal;
		int maxEnergyDemandInternal;
		int protectedCircuitsPercent;
		int hardWiredLoads;
		String additionalAnnotations;
//		Diagnosis diagnosis = new Diagnosis(id,
//				 lastEdited,
//				 plantId,
//				 companion,
//				 surveyor,
//				 vdsApprovalNr,
//				 examinationDate,
//				 examinationDuration,
//				 frequencyControlledUtilities,
//				 precautionsDeclared,
//				 precautionsDeclaredLocation,
//				 examinationComplete,
//				 subsequentExaminationDate,
//				 examinationIncompleteReason,
//				 changesSinceLastExamination,
//				 defectsLastExaminationFixed,
//				 dangerCategory,
//				 dangerCategoryDescription,
//				 examinationResultNoDefect,
//				 examinationResultDefect,
//				 examinationResultDanger,
//				 pages,
//				 isolationChecked,
//				 isolationMesasurementProtocols,
//				 isolationCompensationMeasures,
//				 isolationCompensationMeasuresAnnotation,
//				 rcdAvailable,
//				 rcdAvailablePercent,
//				 rcdAnnotation,
//				 resistance,
//				 resistanceNumber,
//				 resistanceAnnotation,
//				 thermalAbnormality,
//				 thermalAbnormalityAnnotation,
//				 internalPortableUtilities,
//				 externalPortableUtilities,
//				 supplySystem,
//				 energyDemand,
//				 maxEnergyDemandExternal,
//				 maxEnergyDemandInternal,
//				 protectedCircuitsPercent,
//				 hardWiredLoads,
//				 additionalAnnotations
//				);		
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

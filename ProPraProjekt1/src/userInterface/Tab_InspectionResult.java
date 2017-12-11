package userInterface;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;


import applicationLogic.AutoCompletionEvent;
import applicationLogic.AutocompleteSuggestion;
import applicationLogic.AutocompleteTextField;
import applicationLogic.Company;
import applicationLogic.CompanyPlant;
import applicationLogic.DefectAtomic;
import applicationLogic.ResultComplete;
import dataStorageAccess.controller.DefectController;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Tab_InspectionResult implements Initializable{
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
	@FXML private RadioButton completeYesBtn;
	@FXML private RadioButton completeNoBtn;
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
	@FXML private RadioButton isoMinNoBtn;
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
	@FXML private AutocompleteTextField defectSearchField;
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
	
	@FXML private TextField resultDefectId;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepareAutocomplete();
		defectSearchField.setAutoCompletionEvent(new AutoCompletionEvent() {
			@Override
			public void onAutoCompleteResult(AutocompleteSuggestion suggestion) {
				resultDefectId.setText(Integer.toString(suggestion.getId()));
			}
		});
    }
	
	/**
	 * Adds diagnosis to the database
	 * @throws SQLException 
	 */
	public void addDiagnosis(ActionEvent add) throws SQLException {
		boolean dangerGroupA = dangerCategorieGroupABtn.isArmed();
		boolean dangerGroupB = dangerCategorieGroupBBtn.isArmed();
		boolean dangerGroupC = dangerCategorieGroupCBtn.isArmed();
		boolean dangerGroupD = dangerCategorieGroupDBtn.isArmed();
		int dangerGroup = -1;
		
		boolean supplySystemTN = supplySystemTNBtn.isArmed();
		boolean supplySystemTT = supplySystemTTBtn.isArmed();
		boolean supplySystemIT = supplySystemITBtn.isArmed();
		boolean supplySystemCircle = supplySystemCircleBtn.isArmed();
		int supplySys = -1;

		boolean hardWiredLoadsUnder250 = hardWiredLoadsUnder250Btn.isArmed();
		boolean hardWiredLoadsUnder500 = hardWiredLoadsUnder500Btn.isArmed();
		boolean hardWiredLoadsUnder1000 = hardWiredLoadsUnder1000Btn.isArmed();
		boolean hardWiredLoadsUnder5000 = hardWiredLoadsUnder5000Btn.isArmed();
		boolean hardWiredLoadsAbove5000 = hardWiredLoadsAbove5000Btn.isArmed();
		int hwl = -1;
		
		boolean dlenr = defectsLastExaminationNoReportBtn.isArmed();
		boolean dflf = defectsLastExaminationYesBtn.isArmed();
		int defectsLastEx = -1;
		
		boolean csle = changesSinceLastExaminationYesBtn.isArmed();
		boolean cslefe = changesSinceLastExaminationFirstExaminationBtn.isArmed();
		int changesSinceLastEx = -1;
		
		if(dangerGroupA) {
			dangerGroup = 0;
		}else if(dangerGroupB){
			dangerGroup = 1;
		}else if(dangerGroupC){
			dangerGroup = 2;
		}else if(dangerGroupD){
			dangerGroup = 3;
		}
		
		if(supplySystemTN) {
			supplySys = 0;
		}else if (supplySystemTT) {
			supplySys = 1;
		}else if (supplySystemIT) {
			supplySys = 2;
		}else if (supplySystemCircle) {
			supplySys = 3;
		}
		
		if(hardWiredLoadsUnder250) {
			hwl = 0;
		}else if(hardWiredLoadsUnder500) {
			hwl = 1;
		}else if(hardWiredLoadsUnder1000) {
			hwl = 2;
		}else if(hardWiredLoadsUnder5000) {
			hwl = 3;
		}else if(hardWiredLoadsAbove5000) {
			hwl = 4;
		}
		
		if(dlenr) {
			defectsLastEx = 0;
		}else if (dflf) {
			defectsLastEx = 1;
		}
		
		if(csle) {
			changesSinceLastEx = 0;
		}else if (cslefe) {
			changesSinceLastEx = 1;
		}
		
		int id = 0;
		LocalDate date = LocalDate.parse(plantInspectionField.getText());
		LocalDate lastEdited = null;
		String companion = plantCompanionField.getText();
		String surveyor = plantExpertField.getText();
		int vdsApprovalNr = Integer.parseInt(plantAnerkNrField.getText());
		double examinationDuration = Integer.parseInt(plantInspectionTimeField.getText());
		boolean frequencyControlledUtilities = freqYesBtn.isArmed();
		boolean precautionsDeclared = precautionYesBtn.isArmed();
		String precautionsDeclaredLocation = precautionField.getText();
		boolean examinationComplete = completeYesBtn.isArmed();
		LocalDate subsequentExaminationDate  = LocalDate.parse(completeDateField.getText()); 
		String examinationIncompleteReason = completeReasonField.getText();
		int changesSinceLastExamination = changesSinceLastEx;
		int defectsLastExaminationFixed = defectsLastEx;
		int dangerCategory = dangerGroup;										
		String dangerCategoryDescription = dangerCategoryExtensionField.getText();
		boolean examinationResultNoDefect = noDefectsBtn.isArmed();	
		boolean examinationResultDefect = defectsAttachedBtn.isArmed();
		LocalDate examinationResultDefectDate = LocalDate.parse(defectsAttachedDateField.getText()); 
		boolean examinationResultDanger = removeDefectsImmediatelyBtn.isArmed();
		int pages = 0;
		boolean isolationChecked = isoMinYesBtn.isArmed();
		boolean isolationMesasurementProtocols = isoProtocolYesBtn.isArmed();
		boolean isolationCompensationMeasures = isoCompensationYesBtn.isArmed();
		String isolationCompensationMeasuresAnnotation = isoCompensationCommentField.getText();
		boolean rcdAvailable = rcdAllBtn.isArmed();
		int rcdAvailablePercent = Integer.parseInt(rcdPercentageField.getText());
		String rcdAnnotation = rcdCommentField.getText();
		boolean resistance = resistanceYesBtn.isArmed();
		int resistanceNumber = Integer.parseInt(resistancePercentageField.getText());
		String resistanceAnnotation = resistanceCommentField.getText();
		boolean thermalAbnormality = thermicYesBtn.isArmed();
		String thermalAbnormalityAnnotation = thermicCommentField.getText();
		boolean internalPortableUtilities = portableUtilitiesYesBtn.isArmed();
		int externalPortableUtilities = 0 ; 									//boolean? externalPortableUtilitiesNrBtn.isArmed();
		int supplySystem = supplySys;
		int energyDemand = Integer.parseInt(powerConsumptionField.getText());
		int maxEnergyDemandExternal = Integer.parseInt(externalPowerPercentageField.getText());
		int maxEnergyDemandInternal = Integer.parseInt(maxCapacityPercentageField.getText());
		int protectedCircuitsPercent = Integer.parseInt(protectedCirclesPercentageField.getText());
		int hardWiredLoads = hwl;
		String additionalAnnotations = furtherExplanationsField.getText();
		
		int plantId = 0;
		String plantStreet = plantStreetField.getText();
		int plantZip = Integer.parseInt(plantZipField.getText());
		String plantCity = plantCityField.getText();
		
		int compId = 0;
		String companyName = compNameField.getText();
		String hqStreet = streetCompField.getText();
		int hqZip = Integer.parseInt(compZipField.getText());
		String hqCity = compCityField.getText();
		
		//Checking completeness
		if(plantInspectionField.getText().isEmpty()
			||plantCompanionField.getText().isEmpty()
			||plantExpertField.getText().isEmpty()
			||plantAnerkNrField.getText().isEmpty()
			||plantInspectionTimeField.getText().isEmpty()
			||(!freqYesBtn.isArmed() && !freqNoBtn.isArmed())
 			||(!precautionYesBtn.isArmed() && !precautionNoBtn.isArmed())
			||precautionField.getText().isEmpty()
			||(!completeYesBtn.isArmed() && !completeNoBtn.isArmed())
			||completeDateField.getText().isEmpty()
			//||completeReasonField.getText().isEmpty()			//not always used
			||changesSinceLastEx == -1
			||defectsLastEx == -1
			||dangerGroup == -1
			//||dangerCategoryExtensionField.getText().isEmpty()		//not always used
			||(!noDefectsBtn.isArmed() && !defectsAttachedBtn.isArmed() && !removeDefectsImmediatelyBtn.isArmed())
			||pages == 0 		//eingabe im befundschein tab noch vorhanden ?  bzw wichtig f�r den Konstruktor?
			||(!isoMinYesBtn.isArmed() && !isoMinNoBtn.isArmed())
			||(!isoProtocolYesBtn.isArmed() && !isoProtocolNoBtn.isArmed())
			||(!isoCompensationYesBtn.isArmed() && !isoCompensationNoBtn.isArmed())
			//||isoCompensationCommentField.getText().isEmpty()			//not always used
			||(!rcdAllBtn.isArmed() && !rcdNotBtn.isArmed())
			||rcdPercentageField.getText().isEmpty()
			//||rcdCommentField.getText().isEmpty()			//not always used
			||(!resistanceYesBtn.isArmed() && !resistanceNoBtn.isArmed())
			||resistanceYesBtn.isArmed() && resistancePercentageField.getText().isEmpty()
			//||resistanceCommentField.getText()			//not always used
			||(!thermicYesBtn.isArmed() && !thermicNoBtn.isArmed())
			//||thermicCommentField.getText().isEmpty()			//not always used
			||(!portableUtilitiesYesBtn.isArmed() && !portableUtilitiesNoBtn.isArmed())
			||(!externalPortableUtilitiesYesBtn.isArmed() && !externalPortableUtilitiesNoBtn.isArmed())			//nrBtn ? -> group 
			||supplySys == -1
			||powerConsumptionField.getText().isEmpty()
			||externalPowerPercentageField.getText().isEmpty()
			||maxCapacityPercentageField.getText().isEmpty()
			||protectedCirclesPercentageField.getText().isEmpty()
			||hwl == -1
			//||furtherExplanationsField.getText().isEmpty()			//not always used
			) {
				System.out.println("Fehler: Nicht alle Felder ausgef�llt");
				//addDiagnosis();
		}
		
		Company company = new Company(compId, companyName, hqStreet, hqZip, hqCity);
		CompanyPlant companyPlant = new CompanyPlant(plantId, plantStreet, plantZip, plantCity, company);
		ResultComplete resultComplete = new ResultComplete(id,
				 date,
				 lastEdited,
				 companion,
				 surveyor,
				 vdsApprovalNr,
				 examinationDuration,
				 frequencyControlledUtilities,
				 precautionsDeclared,
				 precautionsDeclaredLocation,
				 examinationComplete,
				 subsequentExaminationDate,
				 examinationIncompleteReason,
				 changesSinceLastExamination,
				 defectsLastExaminationFixed,
				 dangerCategory,
				 dangerCategoryDescription,
				 examinationResultNoDefect,
				 examinationResultDefect,
				 examinationResultDefectDate,
				 examinationResultDanger,
				 isolationChecked,
				 isolationMesasurementProtocols,
				 isolationCompensationMeasures,
				 isolationCompensationMeasuresAnnotation,
				 rcdAvailable,
				 rcdAvailablePercent,
				 rcdAnnotation,
				 resistance,
				 resistanceNumber,
				 resistanceAnnotation,
				 thermalAbnormality,
				 thermalAbnormalityAnnotation,
				 internalPortableUtilities,
				 externalPortableUtilities,
				 supplySystem,
				 energyDemand,
				 maxEnergyDemandExternal,
				 maxEnergyDemandInternal,
				 protectedCircuitsPercent,
				 hardWiredLoads,
				 additionalAnnotations,
				 companyPlant
				);
		dataStorageAccess.controller.DiagnosisController.insertDiagnosis(resultComplete);
	}
	public void changeScreenVNBtn (ActionEvent event) throws IOException{
		
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("GUI_VNBtn.fxml"));
		Scene tableViewScene = new Scene (tableViewParent);
		
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(tableViewScene);
		window.show();
		
	}
	
public void changeScreenplantBtn (ActionEvent event) throws IOException{
		
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("GUI_PlantBtn.fxml"));
		Scene tableViewScene = new Scene (tableViewParent);
		
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(tableViewScene);
		window.show();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Prepares the Autocomplete TextField
	 */
	private void prepareAutocomplete() {
		final Task<ArrayList<DefectAtomic>> autocompleteTask = new Task<ArrayList<DefectAtomic>>() {
            @Override
            protected ArrayList<DefectAtomic> call() throws Exception {
        		return DefectController.getAllDefects();
            }
        };
        autocompleteTask.setOnSucceeded(event ->
        	defectSearchField.getEntries().addAll(autocompleteTask.getValue())
    	);
        autocompleteTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + autocompleteTask.getException())
	    );
	    new Thread(autocompleteTask).start();
	}
}

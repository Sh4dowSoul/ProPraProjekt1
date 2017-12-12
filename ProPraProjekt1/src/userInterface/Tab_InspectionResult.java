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
import applicationLogic.Branch;
import applicationLogic.Company;
import applicationLogic.CompanyPlant;
import applicationLogic.DefectAtomic;
import applicationLogic.DefectResult;
import applicationLogic.ResultComplete;
import applicationLogic.ResultPreview;
import dataStorageAccess.ResultAccess;
import dataStorageAccess.controller.DefectController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
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
import javafx.scene.control.cell.PropertyValueFactory;
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
	@FXML private TextField diagnosisDate;
	@FXML private TableView<DefectResult> defectTableView;
	@FXML private TableColumn<DefectResult,String> defectIdColumn;
	@FXML private TableColumn<DefectResult,String> dangerColumn;
	@FXML private TableColumn<DefectResult,String> buildingColumn;
	@FXML private TableColumn<DefectResult,String> roomColumn;
	@FXML private TableColumn<DefectResult,String> maschineColumn;
	@FXML private TableColumn<DefectResult,String> branchColumn;
	@FXML private TableColumn<DefectResult,String> descriptionColumn;
	@FXML private Button AddDiagnosisBtn;
	@FXML private Button pdfExpBtn;
	//Hinzufügen
	@FXML private AutocompleteTextField defectSearchField;
	@FXML private TextField resultDefectId;
	@FXML private TextField branchText;
	@FXML private TextField buildingText;
	@FXML private TextField roomText;
	@FXML private TextField machineText;
	@FXML private TextField customDescriptionText;
	@FXML private CheckBox dangerFireSwitchBox;
	@FXML private CheckBox dangerPersonSwitchBox;
	
	
	private int currentDefectId;
	int currentDangerSituation;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepareAutocomplete();
		prepareTable();
		
		defectSearchField.setAutoCompletionEvent(new AutoCompletionEvent() {
			@Override
			public void onAutoCompleteResult(AutocompleteSuggestion suggestion) {
				resultDefectId.setText(Integer.toString(suggestion.getId()));
				currentDefectId = suggestion.getId();
			}
		});
    }
	private void prepareTable() {
		defectIdColumn.setCellValueFactory(new PropertyValueFactory<DefectResult,String>("id"));
		dangerColumn.setCellValueFactory(new PropertyValueFactory<DefectResult,String>("dangerString"));
		buildingColumn.setCellValueFactory(new PropertyValueFactory<DefectResult,String>("building"));
		roomColumn.setCellValueFactory(new PropertyValueFactory<DefectResult,String>("room"));
		maschineColumn.setCellValueFactory(new PropertyValueFactory<DefectResult,String>("machine"));
		branchColumn.setCellValueFactory(new PropertyValueFactory<DefectResult,String>("branchId"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<DefectResult,String>("defectCustomDescription"));
	} 
	
	public void addToTable(ActionEvent add){
		if (verifyNewTableInput()) {
			currentDangerSituation = 0;
			if (dangerFireSwitchBox.isSelected() && dangerPersonSwitchBox.isSelected()) {
				currentDangerSituation = 3;
			} else {
				if(dangerFireSwitchBox.isSelected()) {
					currentDangerSituation = 1;
				} else {
					if(dangerPersonSwitchBox.isSelected()) {
					currentDangerSituation = 2;
					}
				}	
			}
			defectTableView.getItems().add(new DefectResult(currentDefectId,Integer.valueOf(branchText.getText()),currentDangerSituation,buildingText.getText(), roomText.getText(), machineText.getText(), customDescriptionText.getText()));
			resetAddToTable();
		}
	}
	
	private void resetAddToTable() {
		defectSearchField.clear();
		resultDefectId.clear();
		branchText.clear();
		dangerFireSwitchBox.setSelected(false);
		dangerPersonSwitchBox.setSelected(false);
		buildingText.clear();
		roomText.clear();
		machineText.clear();
		machineText.clear();
		customDescriptionText.clear();
		
	}
	public boolean verifyNewTableInput() {
		return (validate(resultDefectId) & validate(branchText));
	}

	private boolean validate(TextField tf) {
	    if (tf.getText().isEmpty()) {
	    	tf.getStyleClass().add("error");
	    	return false;
	    }
	    else{
	    	tf.getStyleClass().remove("error");
	    }
	    return true;
	}
	
	
	/**
	 * Adds diagnosis to the database
	 * @throws SQLException 
	 */
	public void addDiagnosis(ActionEvent add) throws SQLException {
		boolean dangerGroupA = dangerCategorieGroupABtn.isSelected();
		boolean dangerGroupB = dangerCategorieGroupBBtn.isSelected();
		boolean dangerGroupC = dangerCategorieGroupCBtn.isSelected();
		boolean dangerGroupD = dangerCategorieGroupDBtn.isSelected();
		int dangerGroup = -1;
		
		boolean externalPortableUtilitiesYes = externalPortableUtilitiesYesBtn.isSelected();
		boolean externalPortableUtilitiesNo = externalPortableUtilitiesNoBtn.isSelected();
		boolean externalPortableUtilitiesNr = externalPortableUtilitiesNrBtn.isSelected();
		int epu = -1;
		
		boolean supplySystemTN = supplySystemTNBtn.isSelected();
		boolean supplySystemTT = supplySystemTTBtn.isSelected();
		boolean supplySystemIT = supplySystemITBtn.isSelected();
		boolean supplySystemCircle = supplySystemCircleBtn.isSelected();
		int supplySys = -1;

		boolean hardWiredLoadsUnder250 = hardWiredLoadsUnder250Btn.isSelected();
		boolean hardWiredLoadsUnder500 = hardWiredLoadsUnder500Btn.isSelected();
		boolean hardWiredLoadsUnder1000 = hardWiredLoadsUnder1000Btn.isSelected();
		boolean hardWiredLoadsUnder5000 = hardWiredLoadsUnder5000Btn.isSelected();
		boolean hardWiredLoadsAbove5000 = hardWiredLoadsAbove5000Btn.isSelected();
		int hwl = -1;
		
		boolean dlenr = defectsLastExaminationNoReportBtn.isSelected();
		boolean dflf = defectsLastExaminationYesBtn.isSelected();
		int defectsLastEx = -1;
		
		boolean csle = changesSinceLastExaminationYesBtn.isSelected();
		boolean cslefe = changesSinceLastExaminationFirstExaminationBtn.isSelected();
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
		
		if(externalPortableUtilitiesYes) {
			epu = 0;	
		}else if(externalPortableUtilitiesNo) {
			epu = 1;
		}else if (externalPortableUtilitiesNr) {
			epu = 2;
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
		LocalDate lastEdited = LocalDate.now();
		String companion = plantCompanionField.getText();
		String surveyor = plantExpertField.getText();
		int vdsApprovalNr = Integer.parseInt(plantAnerkNrField.getText());
		double examinationDuration = Double.parseDouble(plantInspectionTimeField.getText());
		boolean frequencyControlledUtilities = freqYesBtn.isSelected();
		boolean precautionsDeclared = precautionYesBtn.isSelected();
		String precautionsDeclaredLocation = precautionField.getText();
		boolean examinationComplete = completeYesBtn.isSelected();
		LocalDate subsequentExaminationDate  = LocalDate.parse(completeDateField.getText()); 
		String examinationIncompleteReason = completeReasonField.getText();
		int changesSinceLastExamination = changesSinceLastEx;
		int defectsLastExaminationFixed = defectsLastEx;
		int dangerCategory = dangerGroup;										
		String dangerCategoryDescription = dangerCategoryExtensionField.getText();
		boolean examinationResultNoDefect = noDefectsBtn.isSelected();	
		boolean examinationResultDefect = defectsAttachedBtn.isSelected();
		LocalDate examinationResultDefectDate = LocalDate.parse(defectsAttachedDateField.getText()); 
		boolean examinationResultDanger = removeDefectsImmediatelyBtn.isSelected();
		boolean isolationChecked = isoMinYesBtn.isSelected();
		boolean isolationMesasurementProtocols = isoProtocolYesBtn.isSelected();
		boolean isolationCompensationMeasures = isoCompensationYesBtn.isSelected();
		String isolationCompensationMeasuresAnnotation = isoCompensationCommentField.getText();
		boolean rcdAvailable = rcdAllBtn.isSelected();
		int rcdAvailablePercent = Integer.parseInt(rcdPercentageField.getText());
		String rcdAnnotation = rcdCommentField.getText();
		boolean resistance = resistanceYesBtn.isSelected();
		int resistanceNumber = Integer.parseInt(resistancePercentageField.getText());
		String resistanceAnnotation = resistanceCommentField.getText();
		boolean thermalAbnormality = thermicYesBtn.isSelected();
		String thermalAbnormalityAnnotation = thermicCommentField.getText();
		boolean internalPortableUtilities = portableUtilitiesYesBtn.isSelected();
		int externalPortableUtilities = epu; 									//boolean? externalPortableUtilitiesNrBtn.isSelected();
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
			||(precautionYesBtn.isArmed() && precautionField.getText().isEmpty())
			||(!completeYesBtn.isArmed() && !completeNoBtn.isArmed())
			||(completeNoBtn.isArmed() && completeDateField.getText().isEmpty())
			//||completeReasonField.getText().isEmpty()			//not always used
			||changesSinceLastEx == -1
			||defectsLastEx == -1
			||dangerGroup == -1
			//||dangerCategoryExtensionField.getText().isEmpty()		//not always used
			||(!noDefectsBtn.isArmed() && !defectsAttachedBtn.isArmed() && !removeDefectsImmediatelyBtn.isArmed())
			||(defectsAttachedBtn.isArmed() && defectsAttachedDateField.toString().isEmpty())
			||(!isoMinYesBtn.isArmed() && !isoMinNoBtn.isArmed())
			||(!isoProtocolYesBtn.isArmed() && !isoProtocolNoBtn.isArmed())
			||(!isoCompensationYesBtn.isArmed() && !isoCompensationNoBtn.isArmed())
			//||isoCompensationCommentField.getText().isEmpty()			//not always used
			||(!rcdAllBtn.isArmed() && !rcdNotBtn.isArmed())
			||(rcdAllBtn.isArmed() && rcdPercentageField.getText().isEmpty())
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
				System.out.println("Fehler: Nicht alle Felder ausgefuellt");
				//label.setText("nicht alles ausgef�llt");
				//addDiagnosis();
		}
		
		Branch branch = new Branch(-1,"test");
		Company company = new Company(compId, companyName, hqStreet, hqZip, hqCity);
		CompanyPlant companyPlant = new CompanyPlant(plantId, plantStreet, plantZip, plantCity, company);
		ResultComplete resultComplete = new ResultComplete(id,
				 date,
				 lastEdited,
				 companion,
				 surveyor,
				 vdsApprovalNr,
				 examinationDuration,
				 branch,
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
		System.out.println("Befundschein erfolgreich hinzugefuegt");
	}
	
	/**
	 * Takes diagnosis from database to edit it in GUI
	 * @throws SQLException 
	 */
	public void editDiagnosis(int id) throws SQLException {
		ResultComplete result = ResultAccess.getCompleteResult(id);
		
		defectTableView.setItems(FXCollections.observableArrayList(result.getDefects()));
		
		//set maybe unused fields empty
		completeReasonField.setText("");
		dangerCategoryExtensionField.setText("");
		isoCompensationCommentField.setText("");
		rcdCommentField.setText("");
		resistanceCommentField.setText("");
		thermicCommentField.setText("");
		furtherExplanationsField.setText("");
		
		//fill fields and buttons
		plantInspectionField.setText(LocalDate.parse(result.getDate().toString()).toString());
		plantCompanionField.setText(result.getCompanion());
		plantExpertField.setText(result.getSurveyor());
		plantAnerkNrField.setText(String.valueOf(result.getVdsApprovalNr()));
		plantInspectionTimeField.setText(String.valueOf(result.getExaminationDuration()));
		
		if(result.isFrequencyControlledUtilities()) {
			freqYesBtn.fire();
		}else {
			freqNoBtn.fire();
		}
		
		if(result.isPrecautionsDeclared()) {
			precautionYesBtn.fire();
		}else {
			precautionNoBtn.fire();
		}
		
		precautionField.setText(result.getPrecautionsDeclaredLocation());
		
		if(result.isExaminationComplete()) {
			completeYesBtn.fire();
		}else {
			completeNoBtn.fire();
		}
		
		completeDateField.setText(LocalDate.parse(result.getSubsequentExaminationDate().toString()).toString());
		completeReasonField.setText(result.getExaminationIncompleteReason());
		
		int changesSinceLastEx = result.getChangesSinceLastExamination();
		if(changesSinceLastEx == 0) {
			changesSinceLastExaminationYesBtn.fire();
		}else if (changesSinceLastEx == 1) {
			changesSinceLastExaminationFirstExaminationBtn.fire();
		}
		
		int defectsLastEx = result.getDefectsLastExaminationFixed();
		if(defectsLastEx == 0) {
			defectsLastExaminationNoReportBtn.fire();
		}else if (defectsLastEx == 1) {
			defectsLastExaminationYesBtn.fire();
		}
		
		int dangerGroup = result.getDangerCategory();
		if(dangerGroup == 0) {
			dangerCategorieGroupABtn.fire();
		}else if(dangerGroup == 1) {
			dangerCategorieGroupBBtn.fire();
		}else if (dangerGroup == 2) {
			dangerCategorieGroupCBtn.fire();
		}else if (dangerGroup == 3) {
			dangerCategorieGroupDBtn.fire();
		}
		
		dangerCategoryExtensionField.setText(result.getDangerCategoryDescription());
		
		if(result.isExaminationResultNoDefect()){
			noDefectsBtn.fire();
		}	
		if(result.isExaminationResultDefect()) {
			defectsAttachedBtn.fire();
		}
		if(result.isExaminationResultDanger()) {
			removeDefectsImmediatelyBtn.fire();
		}
		
		defectsAttachedDateField.setText(LocalDate.parse(result.getExaminationResultDefectDate().toString()).toString());
		
		if(result.isIsolationChecked()) {
			isoMinYesBtn.fire();
		}else {
			isoMinNoBtn.fire();
		}
		
		if(result.isIsolationMesasurementProtocols()) {
			isoProtocolYesBtn.fire();
		}else {
			isoProtocolNoBtn.fire();
		}
		
		if(result.isIsolationCompensationMeasures()) {
			isoCompensationYesBtn.fire();
		}else {
			isoCompensationNoBtn.fire();
		}
		
		isoCompensationCommentField.setText(result.getIsolationCompensationMeasuresAnnotation());
		
		if(result.getRcdAvailable()) {
			rcdAllBtn.fire();
		}else {
			rcdNotBtn.fire();
		}
		
		rcdPercentageField.setText(String.valueOf(result.getRcdAvailablePercent()));
		rcdCommentField.setText(result.getRcdAnnotation());
		
		if(result.isResistance()) {
			resistanceYesBtn.fire();
		}else {
			resistanceNoBtn.fire();
		}
		
		resistancePercentageField.setText(String.valueOf(result.getResistanceNumber()));
		resistanceCommentField.setText(result.getResistanceAnnotation());
		
		if(result.isThermalAbnormality()) {
			thermicYesBtn.fire();
		}else {
			thermicNoBtn.fire();
		}
		
		thermicCommentField.setText(result.getThermalAbnormalityAnnotation());
		
		if(result.isInternalPortableUtilities()) {
			portableUtilitiesYesBtn.fire();
		}else {
			portableUtilitiesNoBtn.fire();
		}
		
		int epu = result.getExternalPortableUtilities();
		if(epu == 0) {
			externalPortableUtilitiesYesBtn.fire();
		}else if(epu == 1) {
			externalPortableUtilitiesNoBtn.fire();
		}else if(epu == 2) {
			externalPortableUtilitiesNrBtn.fire();
		}
		
		int supplySystem = result.getSupplySystem();
		if(supplySystem == 0) {
			supplySystemTNBtn.fire();
		}else if(supplySystem == 1) {
			supplySystemTTBtn.fire();
		}else if(supplySystem == 2) {
			supplySystemITBtn.fire();
		}else if(supplySystem == 3) {
			supplySystemCircleBtn.fire();
		}
		
		powerConsumptionField.setText(String.valueOf(result.getEnergyDemand()));
		externalPowerPercentageField.setText(String.valueOf(result.getMaxEnergyDemandExternal()));
		maxCapacityPercentageField.setText(String.valueOf(result.getMaxEnergyDemandInternal()));
		protectedCirclesPercentageField.setText(String.valueOf(result.getProtectedCircuitsPercent()));
		
		int hardWiredLoads = result.getHardWiredLoads();
		if(hardWiredLoads == 0) {
			hardWiredLoadsUnder250Btn.fire();
		}else if(hardWiredLoads == 1) {
			hardWiredLoadsUnder500Btn.fire();
		}else if(hardWiredLoads == 2) {
			hardWiredLoadsUnder1000Btn.fire();
		}else if(hardWiredLoads == 3) {
			hardWiredLoadsUnder5000Btn.fire();
		}else if(hardWiredLoads == 4) {
			hardWiredLoadsAbove5000Btn.fire();
		}
		
		furtherExplanationsField.setText(result.getAdditionalAnnotations());
		
		CompanyPlant cp = result.getCompanyPlant();
		plantStreetField.setText(cp.getPlantStreet());
		plantZipField.setText(String.valueOf(cp.getPlantZip()));
		plantCityField.setText(cp.getPlantCity());
		
		Company c = cp.getCompany();
		compNameField.setText(c.getName());
		streetCompField.setText(c.getHqStreet());
		compZipField.setText(String.valueOf(c.getHqZip()));
		compCityField.setText(c.getHqCity());
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

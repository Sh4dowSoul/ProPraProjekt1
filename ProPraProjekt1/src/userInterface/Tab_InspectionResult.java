package userInterface;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;


import applicationLogic.Branch;
import applicationLogic.Company;
import applicationLogic.CompanyPlant;
import applicationLogic.ExceptionDialog;
import applicationLogic.Flaw;
import applicationLogic.FlawListElement;
import applicationLogic.InspectionReportFull;
import applicationLogic.PDFExport;
import applicationLogic.Util;
import dataStorageAccess.BranchAccess;
import dataStorageAccess.CompanyAccess;
import dataStorageAccess.DefectAccess;
import dataStorageAccess.FlawAccess;
import dataStorageAccess.InspectionReportAccess;
import de.schnettler.AutoCompletionEvent;
import de.schnettler.AutocompleteSuggestion;
import de.schnettler.AutocompleteTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 * @author Niklas Schnettler, Salih Arslan, Sven Meyer, Sven Motschnig, Daniel Novakovic
 *
 */
public class Tab_InspectionResult implements Initializable{
// *** BEFUNDSCHEIN TAB ***
// Versicherungsnehmer Adresse
	@FXML private ComboBox<Company> comboBoxCompanyName;
	@FXML private TextField textFieldCompanyStreet;
	@FXML private TextField textFieldCompanyZipCode;
	@FXML private TextField textFieldCompanyCity;
	@FXML private Button buttonSaveCompany;
	
	@FXML private ComboBox<CompanyPlant> comboBoxCompanyPlantStreet;
	@FXML private TextField textFieldCompanyPlantZipCode;
	@FXML private TextField textFieldCompanyPlantCity;
	@FXML private Button buttonSaveCompanyPlant;
	

// Risikoanschrift
	@FXML private TextField plantCompanionField;
	@FXML private TextField plantExpertField;
	@FXML private TextField plantAnerkNrField;
	@FXML private TextField plantInspectionField;
	@FXML private TextField plantInspectionTimeField;
	@FXML private DatePicker datePickerExaminationDate;
	
//Art des Betriebes oder der Anlage
	@FXML private TextField branchName;
	@FXML private RadioButton freqYesBtn;
	@FXML private RadioButton freqNoBtn;
	@FXML private RadioButton precautionYesBtn;
	@FXML private RadioButton precautionNoBtn;
	@FXML private TextField precautionField;
	@FXML private RadioButton completeYesBtn;
	@FXML private RadioButton completeNoBtn;
	@FXML private DatePicker datePickerSubsequentExaminationDate;
	@FXML private TextField completeReasonField;
	@FXML private RadioButton changesSinceLastExaminationYesBtn;
	@FXML private RadioButton changesSinceLastExaminationNoBtn;
	@FXML private RadioButton changesSinceLastExaminationFirstExaminationBtn;
	@FXML private RadioButton defectsLastExaminationYesBtn;
	@FXML private RadioButton defectsLastExaminationNoBtn;
	@FXML private RadioButton defectsLastExaminationNoReportBtn;
	@FXML private ToggleGroup freqGroup;
	@FXML private ToggleGroup precautionGroup;
	@FXML private ToggleGroup completeGroup;
	@FXML private ToggleGroup changesSinceLastExaminationGroup;
	@FXML private ToggleGroup defectsLastExaminationFixedGroup;
	
//Gesamtbeurteilung der Anlage
	@FXML private RadioButton dangerCategorieGroupABtn;
	@FXML private RadioButton dangerCategorieGroupBBtn;
	@FXML private RadioButton dangerCategorieGroupCBtn;
	@FXML private RadioButton dangerCategorieGroupDBtn;
	@FXML private TextField dangerCategoryExtensionField;
	@FXML private ToggleGroup dangerCategorieGroup;
	
//Prüfergebnis
	@FXML private CheckBox noDefectsBtn;
	@FXML private CheckBox defectsAttachedBtn;
	@FXML private DatePicker datePickerResolvedUntil;
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
	@FXML private ToggleGroup IsoMinGroup;
	@FXML private ToggleGroup IsoProtocollGroup;
	@FXML private ToggleGroup IsoCompensationGroup;
	@FXML private ToggleGroup RcdGroup;
	@FXML private ToggleGroup ResistanceGroup;
	@FXML private ToggleGroup ThermicGroup;
	
//Ortsveränderliche Betriebsmittel
	@FXML private RadioButton portableUtilitiesYesBtn;
	@FXML private RadioButton portableUtilitiesNoBtn;
	@FXML private RadioButton externalPortableUtilitiesYesBtn;
	@FXML private RadioButton externalPortableUtilitiesNoBtn;
	@FXML private RadioButton externalPortableUtilitiesNrBtn;
	@FXML private ToggleGroup PortableUtilitiesGroup;
	@FXML private ToggleGroup ExternalPortableUtilitiesGroup;
	
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
	@FXML private ToggleGroup SupplySystemGroup;
	@FXML private ToggleGroup HardWiredLoadsGroup;

// Anhang A
	@FXML private TableView<FlawListElement> defectTableView;
	@FXML private TableColumn<FlawListElement,String> defectIdColumn;
	@FXML private TableColumn<FlawListElement,String> dangerColumn;
	@FXML private TableColumn<FlawListElement,String> buildingColumn;
	@FXML private TableColumn<FlawListElement,String> roomColumn;
	@FXML private TableColumn<FlawListElement,String> maschineColumn;
	@FXML private TableColumn<FlawListElement,String> branchColumn;
	@FXML private TableColumn<FlawListElement,String> descriptionColumn;
	@FXML private Button AddDiagnosisBtn;
	@FXML private Button pdfExpBtn;
	//Hinzufügen
	@FXML private AutocompleteTextField defectSearchField;
	@FXML private TextField resultDefectId;
	@FXML private AutocompleteTextField branchText;
	@FXML private TextField buildingText;
	@FXML private TextField roomText;
	@FXML private TextField machineText;
	@FXML private TextField customDescriptionText;
	@FXML private CheckBox dangerFireSwitchBox;
	@FXML private CheckBox dangerPersonSwitchBox;
	@FXML private Button addDefectButton;
	
	@FXML private AnchorPane anchorPaneForm;
	@FXML private BorderPane borderPane;
	
	private GUIController mainController;
	//New Defect
	int newDefectDanger;
	private int newDefectId;
	boolean tableUpdate = false;
	private int tableSelctedId;
	private int showTableDialog = 1;
	private int defectElementId = -1;
	//InspectionResult Save
	private boolean inspectionResultSaved;
	private int inspectionResultId = 0;
	private Company inspectionResultCompany;
	private CompanyPlant inspectionResultCompanyPlant;
	private InspectionReportFull resultComplete;
	
	private ArrayList<String> errors;
	
	
	//Current Working Data
	//Company
	private ArrayList<Company> companyList;
	private Company currentCompany;
	//CompanyPlant
	private ArrayList<CompanyPlant> companyPlantsList;
	private CompanyPlant currentCompanyPlant;
	//InpsectionReport
	private InspectionReportFull currentInspectionReport;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepareGUI();
		prepareData();
		prepareBranchesAutocomplete();
		prepareDefectsAutocomplete();
		pdfExpBtn.setDisable(true);
    }
	
	private void prepareData() {
		loadCompanies();
		loadCompanyPlants();
	}

	private void prepareGUI() {
		//CompanyComboBox
		comboBoxCompanyName.setCellFactory(new Callback<ListView<Company>, ListCell<Company>>() {
			 @Override
			 public ListCell<Company> call(ListView<Company> param) {
				 return new ListCell<Company>(){
					 @Override
					 public void updateItem(Company item, boolean empty){
						 super.updateItem(item, empty);
						 if(!empty) {
							 setText(item.getDescription());
							 setGraphic(null);
						 }
					 }
				 };
			}
		});
		comboBoxCompanyName.setConverter(new StringConverter<Company>() {
	        @Override
	        public String toString(Company company) {
	            return company == null ? "" : company.getDescription();
	        }

	        @Override
	        public Company fromString(String string) {
                for(Company company : companyList){
                	//Company Selected
                    if(company.getDescription().equalsIgnoreCase(string)){
                        return company;
                    }
                }
                //New Company Name Entered
                return new Company(-1,string);
	        }
	    });
		comboBoxCompanyName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Company>() {
	        public void changed(ObservableValue<? extends Company> observable, Company oldValue, Company newValue) {
	        	if (newValue != null) {
	        		currentCompany = newValue;
		        	removeCompanyPlant();
		        	switch(newValue.getInternalId()) {
		        		case -1:
		        			//New Company -> Enable and clear TextFields
			        		textFieldCompanyStreet.setDisable(false);
			        		textFieldCompanyZipCode.setDisable(false);
			        		textFieldCompanyCity.setDisable(false);
			        		textFieldCompanyStreet.clear();
			        		textFieldCompanyZipCode.clear();
			        		textFieldCompanyCity.clear();
			        		buttonSaveCompany.setDisable(false);
			        		break;
		        		case 0:
		        			//Company not yet in database
		        			break;
		        		default: 
		        			//Existing Company -> Disable TextFields and load Company Data
			        		disableCompanyPrepareCompanyPlant();
			        		textFieldCompanyStreet.setText(newValue.getStreet());
			        		textFieldCompanyZipCode.setText(String.valueOf(newValue.getZipCode()));
			        		textFieldCompanyCity.setText(newValue.getCity());
			        		buttonSaveCompany.setDisable(true);
			        		break;
		        	}
	        	} else {
	        		System.out.println("ERROR: New Company ComboBox Value is Null");
	        	}
	        	
	        }

			private void removeCompanyPlant() {
				currentCompanyPlant = null;
				textFieldCompanyPlantZipCode.setDisable(true);
        		textFieldCompanyPlantCity.setDisable(true);
        		textFieldCompanyPlantZipCode.clear();
        		textFieldCompanyPlantCity.clear();
        		buttonSaveCompanyPlant.setDisable(false);
        		comboBoxCompanyPlantStreet.setValue(null);
			}
		});
		
		
		//CompanyPlantComboBox
		comboBoxCompanyPlantStreet.setCellFactory(new Callback<ListView<CompanyPlant>, ListCell<CompanyPlant>>() {
			 @Override
			 public ListCell<CompanyPlant> call(ListView<CompanyPlant> param) {
				 return new ListCell<CompanyPlant>(){
					 @Override
					 public void updateItem(CompanyPlant item, boolean empty){
						 super.updateItem(item, empty);
						 if(!empty) {
							 setText(item.getDescription());
							 setGraphic(null);
						 }
					 }
				 };
			}
		});
		comboBoxCompanyPlantStreet.setConverter(new StringConverter<CompanyPlant>() {
	        @Override
	        public String toString(CompanyPlant companyPlant) {
	            return companyPlant == null ? "" : companyPlant.getDescription();
	        }

	        @Override
	        public CompanyPlant fromString(String string) {
                for(CompanyPlant companyPlant : companyPlantsList){
                	//Company Selected
                    if(companyPlant.getDescription().equalsIgnoreCase(string)){
                        return companyPlant;
                    }
                }
                //New Company Name Entered
                return new CompanyPlant(-1, string, currentCompany);
	        }
	    });
		comboBoxCompanyPlantStreet.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CompanyPlant>() {
	        public void changed(ObservableValue<? extends CompanyPlant> observable, CompanyPlant oldValue, CompanyPlant newValue) {
	        	if (newValue != null) {
	        		currentCompanyPlant = newValue;
		        	switch(newValue.getInternalId()) {
		        		case -1:
		        			//New CompanyPlant -> Enable and clear TextFields
			        		textFieldCompanyPlantZipCode.setDisable(false);
			        		textFieldCompanyPlantCity.setDisable(false);
			        		textFieldCompanyPlantZipCode.clear();
			        		textFieldCompanyPlantCity.clear();
			        		buttonSaveCompanyPlant.setDisable(false);
			        		break;
		        		case 0:
		        			//CompanyPlant not yet in database
		        			break;
		        		default: 
		        			//Existing CompanyPlant -> Disable TextFields and load Company Data
			        		disableCompanyPlantTextFields();
			        		textFieldCompanyPlantZipCode.setText(String.valueOf(newValue.getZipCode()));
			        		textFieldCompanyPlantCity.setText(newValue.getCity());
			        		buttonSaveCompanyPlant.setDisable(true);
			        		break;
		        	}
	        	} else {
	        		System.out.println("ERROR: New CompanyPlant ComboBox Value is Null");
	        	}
	        }
		});
		
		//FlawList TableView
		defectIdColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("externalFlawId"));
		dangerColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("dangerString"));
		buildingColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("building"));
		roomColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("room"));
		maschineColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("machine"));
		branchColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("branchId"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("flawDescription"));
	}

	public void saveCompany(ActionEvent event) throws IOException {
		if(validate(textFieldCompanyZipCode, true)) {
			currentCompany.setCity(textFieldCompanyCity.getText());
			currentCompany.setStreet(textFieldCompanyStreet.getText());
			currentCompany.setZipCode(Integer.valueOf(textFieldCompanyZipCode.getText()));
			currentCompany.setInternalId(0);
			//Add new Plant to Lists
			disableCompanyPrepareCompanyPlant();
			comboBoxCompanyName.getItems().add(currentCompany);//Should be visible in ComboBox
			companyList.add(currentCompany);
			buttonSaveCompany.setDisable(true);
		}
	}
	
	public void saveCompanyPlant(ActionEvent event) throws IOException {
		if(validate(textFieldCompanyPlantZipCode, true)) {
			currentCompanyPlant.setCity(textFieldCompanyPlantCity.getText());
			currentCompanyPlant.setZipCode(Integer.valueOf(textFieldCompanyPlantZipCode.getText()));
			currentCompanyPlant.setInternalId(0);
			disableCompanyPlantTextFields();
			//Add new Plant to Lists
			comboBoxCompanyPlantStreet.getItems().add(currentCompanyPlant);//Should be visible in ComboBox
			companyPlantsList.add(currentCompanyPlant);//CompanyPlant List
			buttonSaveCompanyPlant.setDisable(true);
		}
	}
	
	private void disableCompanyPrepareCompanyPlant() {
		textFieldCompanyStreet.setDisable(true);
		textFieldCompanyZipCode.setDisable(true);
		textFieldCompanyCity.setDisable(true);
		//-> Enable CompanyPlant ComboBox
		comboBoxCompanyPlantStreet.setDisable(false);
		//-> Filter CompaniePlants for selected Company
		ArrayList<CompanyPlant> plants = new ArrayList<>();
		for(CompanyPlant companyPlant : companyPlantsList) {
			if (companyPlant.getCompany().getInternalId() == currentCompany.getInternalId()) {
				plants.add(companyPlant);
			}
		}
		comboBoxCompanyPlantStreet.setItems(FXCollections.observableArrayList(plants));
	}
	private void disableCompanyPlantTextFields() {
		textFieldCompanyPlantZipCode.setDisable(true);
		textFieldCompanyPlantCity.setDisable(true);
	}

	/**
	 * Get together with parent Controller
	 * 
	 * @param parentController - The parent Controller
	 */
	public void setParentController(GUIController parentController) {
	    this.mainController = parentController;
	}
	
	
	
	/**
	 * Defect edit Dialog (Do you want to edit this defect? )
	 * 
	 * @param selectedItem - selected Defect
	 */
	private void createDignosisOptionsDialog(FlawListElement selectedItem) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Bearbeiten");
		alert.setHeaderText("Wollen sie den ausgewählten Mangel bearbeiten?" );
		alert.initStyle(StageStyle.UTILITY);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			addDefectButton.setText("Update " + tableSelctedId);
			resetAddToTable();
			openInAddToTableMenu(selectedItem);
		}
	}

	
	public FlawListElement getNewFlawData() {
		//Get Danger Category
		int defectDanger = 0;
		if (dangerFireSwitchBox.isSelected() && dangerPersonSwitchBox.isSelected()) {
			defectDanger = 3;
		} else {
			if(dangerFireSwitchBox.isSelected()) {
				defectDanger = 1;
			} else {
				if(dangerPersonSwitchBox.isSelected()) {
					defectDanger = 2;
				}
			}
		}
		
		//Get FlawId and -Description
		try {
			String flawDescriptionEntered = defectSearchField.getText();
			//Check if already present or new flaw
			if(!FlawAccess.getFlawDescriptions(newDefectId).contains(flawDescriptionEntered)) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Neuer Mangel");
				alert.setHeaderText("Textbaustein als neuen Mangel abspeichern?");
				alert.setContentText("Unter der Mangelnummer " + newDefectId +" ist bisher keine Mangel mit der Beschreibung \n\n'" + 
				flawDescriptionEntered +
				"'\n\nbekannt. Soll der eingegebene Mangel in der Datenbank gespeichert werden?" );


				ButtonType noButton = new ButtonType("No", ButtonData.NO);
				ButtonType yesButton = new ButtonType("Yes", ButtonData.YES);
				alert.getButtonTypes().setAll(noButton, yesButton);

				Optional<ButtonType> Dialogresult = alert.showAndWait();
				if (Dialogresult.get() == yesButton) {
					//TODO Neuen customMangel in der Datenbank speichern
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		//return new FlawListElement(-1, newDefectId,Integer.valueOf(branchText.getText()),defectDanger, buildingText.getText(), roomText.getText(), machineText.getText(), customDescriptionText.getText());
	}
	
	public void addFlawToTable(ActionEvent add) {
		if (verifyNewFlaw()) {
			//Add new Flaw to TableView
			defectTableView.getItems().add(getNewFlawData());
			//Reset addFlawForm
			resetAddToTable();
		}
	}
	
	public boolean verifyNewFlaw() {
		return (validate(resultDefectId, true) & validate(branchText, false));
	}
	
	/**
	 * Reset add to defect table textfields & CheckBoxes
	 */
	private void resetAddToTable() {
		defectSearchField.clear();
		resultDefectId.clear();
		dangerFireSwitchBox.setSelected(false);
		dangerPersonSwitchBox.setSelected(false);
		buildingText.clear();
		roomText.clear();
		machineText.clear();
		machineText.clear();
		customDescriptionText.clear();
	}
	
	/**
	 * Load a defect into "add defect to table" views
	 * 
	 * @param defect
	 */
	private void openInAddToTableMenu(FlawListElement defect) {
		defectSearchField.setText(defect.getFlaw().getDescription());
		newDefectId = defect.getFlaw().getExternalId();
		resultDefectId.setText(String.valueOf(defect.getFlaw().getExternalId()));
		branchText.setText(String.valueOf(defect.getBranchId()));
		int danger = defect.getDanger();
		defectElementId = defect.getElementId();
		switch (danger) {
		case 1: 
			dangerFireSwitchBox.setSelected(true);
			break;
		case 2: 
			dangerPersonSwitchBox.setSelected(true);
			break;
		case 3: 
			dangerFireSwitchBox.setSelected(true);
			dangerPersonSwitchBox.setSelected(true);
			break;
		}
		buildingText.setText(defect.getBuilding());
		roomText.setText(defect.getRoom());
		machineText.setText(defect.getMachine());
		tableUpdate = true;
	}
	
	public boolean verifyNewTableInput() {
		return (validate(resultDefectId, true) & validate(branchText, false));
	}

	
	
	
	/**
	 * Prepare Result for export to Database
	 * @throws SQLException 
	 */
	public void addDiagnosis(ActionEvent add){
		fetchInspectionReportData();
		try {
			InspectionReportAccess.saveNewCompleteResult(currentInspectionReport);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//Wrapps user input in ResulteComplete object
		/*
		if (fetchInspectionReportData()) {
			
			boolean newDiagnosis = true;
			boolean cancelled = false;
			
			//Check if "Edit Mode"
			if (mainController.getEditMode()) {
				//Edit Mode - Ask if save as new Result or Override old Result
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Speichere Befundschein");
				alert.setHeaderText("Überschreiben oder neu speichern?");
				alert.setContentText("Wollen sie den alten Befundschein überschreiben oder einen neuen Befundschein speichern?");

				ButtonType overrideButton = new ButtonType("Überschreiben");
				ButtonType newButton = new ButtonType("Als neuen Befundschein speichern");
				ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
				alert.getButtonTypes().setAll(overrideButton, newButton, cancelButton);

				Optional<ButtonType> Dialogresult = alert.showAndWait();
				if (Dialogresult.get() == overrideButton){
					newDiagnosis = false;
				}if (Dialogresult.get() == cancelButton) {
					cancelled = true;
				}
			} 
			if (!cancelled) {
				if (newDiagnosis) {
					try {
						InspectionReportAccess.saveNewCompleteResult(resultComplete);
						Notifications.create()
	                    .title("Erfolgreich gespeichert")
	                    .text("Der Befundschein wurde erfolgreich gespeichert ")
	                    .showInformation();
						inspectionResultSaved = true;
						pdfExpBtn.setDisable(false);
					} catch (SQLException e) {
						Notifications.create()
		                 .title("Es ist ein Problem aufgetreten")
		                 .text("Der Befundschein konnte leider nicht gespeichert werden.")
		                 .hideAfter(Duration.INDEFINITE)
		                 .onAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								new ExceptionDialog("Export Fehler", "Fehler beim Exportieren", "Beim Exportieren des Befundscheins ist leider ein Fehler aufgetreten.", e);
							}
		                 })
		                 .showError();
					}
				} else {
					try {
						InspectionReportAccess.updateCompleteResult(resultComplete);
						Notifications.create()
	                    .title("Erfolgreich gespeichert")
	                    .text("Der Befundschein wurde erfolgreich bearbeitet und gespeichert. ")
	                    .showInformation();
						inspectionResultSaved = true;
						pdfExpBtn.setDisable(false);
					} catch (SQLException e) {
						Notifications.create()
		                 .title("Es ist ein Problem aufgetreten")
		                 .text("Der Befundschein konnte leider nicht gespeichert werden.")
		                 .hideAfter(Duration.INDEFINITE)
		                 .onAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								new ExceptionDialog("Export Fehler", "Fehler beim Exportieren", "Beim Exportieren des Befundscheins ist leider ein Fehler aufgetreten.", e);
							}
		                 })
		                 .showError();
					}
				}
			} 
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Befundschein nicht komplett");
			String content ="Bitte überprüfen Sie Ihre Eingaben in den Kategorien \n";
			for(String entry : errors) {
				content += "- " +entry + "\n";
			}
			alert.setContentText(content);
			alert.showAndWait();
		}*/
		
	}
	
	
	
	/**
	 * Import Result for edit
	 * @throws SQLException 
	 */
	public void importInspectionReport(int id) {
		//TODO Prepare for a new Session
		
		Boolean currentBoolean = null;
		Integer currentInteger = null;
		try {
			currentInspectionReport = InspectionReportAccess.getCompleteResult(id);
			
			//Set FlawList to Tableview
			defectTableView.setItems(FXCollections.observableArrayList(currentInspectionReport.getDefects()));
			
			//Set current Company(Plant)
			if(currentInspectionReport.getCompanyPlant()!= null) {
				currentCompanyPlant = currentInspectionReport.getCompanyPlant();
				currentCompany = currentCompanyPlant.getCompany();
				//Load Company Data into form
				comboBoxCompanyName.setValue(currentCompany);
				currentCompanyPlant = currentInspectionReport.getCompanyPlant();//Warnung: Muss ein weiteres Mal geladen werden, da nachdem die Firma in die ComboBox geladen wurde, wurde currentCompanyPlant null gesetzt. TODO Fix this unwanted behavior
				comboBoxCompanyPlantStreet.setValue(currentCompanyPlant);
			}
			
		
			//Load ExaminationInfo Data
			plantCompanionField.setText(currentInspectionReport.getCompanion());
			plantExpertField.setText(currentInspectionReport.getSurveyor());
			Util.setNullableIntToTextField(plantAnerkNrField, currentInspectionReport.getVdsApprovalNr());
			datePickerExaminationDate.setValue(currentInspectionReport.getDate());
			Util.setNullableDoubleToTextField(plantInspectionTimeField, currentInspectionReport.getExaminationDuration());
			
			//Load "Art des Betriebs" Data
			Util.setNullableIntToTextField(branchName, currentInspectionReport.getBranch().getExternalId());
			currentBoolean = currentInspectionReport.isFrequencyControlledUtilities();
			if(currentBoolean != null) {
				if(currentBoolean) {
					freqYesBtn.setSelected(true);
				}else {
					freqNoBtn.setSelected(true);
				}
			}
			
			currentBoolean = currentInspectionReport.isPrecautionsDeclared();
			if(currentBoolean != null) {
				if(currentBoolean) {
					precautionYesBtn.setSelected(true);
				}else {
					precautionNoBtn.setSelected(true);
				}
			}
			precautionField.setText(currentInspectionReport.getPrecautionsDeclaredLocation());
			currentBoolean = currentInspectionReport.isExaminationComplete();
			if(currentBoolean != null) {
				if(currentBoolean) {
					completeYesBtn.setSelected(true);
				}else {
					completeNoBtn.setSelected(true);
				}
			}
			
			datePickerSubsequentExaminationDate.setValue(currentInspectionReport.getSubsequentExaminationDate());
			completeReasonField.setText(currentInspectionReport.getExaminationIncompleteReason());
			currentInteger = currentInspectionReport.getChangesSinceLastExamination();
			if(currentInteger != null) {
				switch (currentInteger) {
				case 0:
					changesSinceLastExaminationNoBtn.setSelected(true);
					break;
				case 1:
					changesSinceLastExaminationYesBtn.setSelected(true);
					break;
				case 2:
					changesSinceLastExaminationFirstExaminationBtn.setSelected(true);
					break;
				}
			}
			currentInteger = currentInspectionReport.getDefectsLastExaminationFixed();
			if(currentInteger != null) {
				switch (currentInteger) {
				case 0:
					defectsLastExaminationNoBtn.setSelected(true);
					break;
				case 1:
					defectsLastExaminationYesBtn.setSelected(true);
					break;
				case 2:
					defectsLastExaminationNoReportBtn.setSelected(true);
					break;
				}
			}
			
			
			
			//Load "Gesamtbeurteilung der Anlage"
			currentInteger = currentInspectionReport.getDangerCategory();
			if(currentInteger != null) {
				switch (currentInteger) {
				case 0:
					dangerCategorieGroupABtn.setSelected(true);
					break;
				case 1:
					dangerCategorieGroupBBtn.setSelected(true);
					break;
				case 2:
					dangerCategorieGroupCBtn.setSelected(true);
					break;
				case 3:
					dangerCategorieGroupDBtn.setSelected(true);
					break;
				}
			}
			dangerCategoryExtensionField.setText(currentInspectionReport.getDangerCategoryDescription());
			
			
			//Load "Prüfergebnis"
			currentBoolean = currentInspectionReport.isExaminationResultNoDefect();
			if (currentBoolean != null) {
				noDefectsBtn.setSelected(currentBoolean);
			}
			currentBoolean = currentInspectionReport.isExaminationResultDefect();
			if (currentBoolean != null) {
				defectsAttachedBtn.setSelected(currentBoolean);
			}
			currentBoolean = currentInspectionReport.isExaminationResultDanger();
			if (currentBoolean != null) {
				removeDefectsImmediatelyBtn.setSelected(currentBoolean);
			}
			datePickerResolvedUntil.setValue(currentInspectionReport.getExaminationResultDefectDate());
			
			
			//Load "Messungen"
			currentBoolean = currentInspectionReport.isIsolationChecked();
			if (currentBoolean != null) {
				if(currentBoolean) {
					isoMinYesBtn.setSelected(true);
				}else {
					isoMinNoBtn.setSelected(true);
				}
			}
			currentBoolean = currentInspectionReport.isIsolationMesasurementProtocols();
			if (currentBoolean != null) {
				if(currentBoolean) {
					isoProtocolYesBtn.setSelected(true);
				}else {
					isoProtocolNoBtn.setSelected(true);
				}
			}
			currentBoolean = currentInspectionReport.isIsolationCompensationMeasures();
			if (currentBoolean != null) {
				if(currentBoolean) {
					isoCompensationYesBtn.setSelected(true);
				}else {
					isoCompensationNoBtn.setSelected(true);
				}
			}
			isoCompensationCommentField.setText(currentInspectionReport.getIsolationCompensationMeasuresAnnotation());
			currentBoolean = currentInspectionReport.getRcdAvailable();
			if (currentBoolean != null) {
				if(currentBoolean) {
					rcdAllBtn.setSelected(true);
				}else {
					rcdNotBtn.setSelected(true);
				}
			}
			Util.setNullableDoubleToTextField(rcdPercentageField, currentInspectionReport.getRcdAvailablePercent());
			rcdCommentField.setText(currentInspectionReport.getRcdAnnotation());
			currentBoolean = currentInspectionReport.isResistance();
			if (currentBoolean != null) {
				if(currentBoolean) {
					resistanceYesBtn.setSelected(true);
				}else {
					resistanceNoBtn.setSelected(true);
				}
			}
			Util.setNullableIntToTextField(resistancePercentageField, currentInspectionReport.getResistanceNumber());
			resistanceCommentField.setText(currentInspectionReport.getResistanceAnnotation());
			currentBoolean = currentInspectionReport.isThermalAbnormality();
			if (currentBoolean != null) {
				if(currentBoolean) {
					thermicYesBtn.setSelected(true);
				}else {
					thermicNoBtn.setSelected(true);
				}
			}
			thermicCommentField.setText(currentInspectionReport.getThermalAbnormalityAnnotation());
			
			
			//Load "Ortsveränderliche Betriebsmittel"
			currentBoolean = currentInspectionReport.isInternalPortableUtilities();
			if (currentBoolean != null) {
				if(currentBoolean) {
					portableUtilitiesYesBtn.setSelected(true);
				}else {
					portableUtilitiesNoBtn.setSelected(true);
				}
			}
			
			currentInteger = currentInspectionReport.getExternalPortableUtilities();
			if (currentInteger != null) {
				switch(currentInteger) {
				case 0:
					externalPortableUtilitiesYesBtn.setSelected(true);
					break;
				case 1:
					externalPortableUtilitiesNoBtn.setSelected(true);
					break;
				case 2:
					externalPortableUtilitiesNrBtn.setSelected(true);
					break;
				}
			}
			
			
			//Load "Allgemeine Informationen..."
			currentInteger = currentInspectionReport.getSupplySystem();
			if (currentInteger != null) {
				switch(currentInteger) {
				case 0:
					supplySystemTNBtn.setSelected(true);
					break;
				case 1:
					supplySystemTTBtn.setSelected(true);
					break;
				case 2:
					supplySystemITBtn.setSelected(true);
					break;
				case 3:
					supplySystemCircleBtn.setSelected(true);
					break;
				}
			}
			
			Util.setNullableIntToTextField(powerConsumptionField, currentInspectionReport.getEnergyDemand());
			Util.setNullableIntToTextField(externalPowerPercentageField, currentInspectionReport.getMaxEnergyDemandExternal());
			Util.setNullableIntToTextField(maxCapacityPercentageField, currentInspectionReport.getMaxEnergyDemandInternal());
			Util.setNullableIntToTextField(protectedCirclesPercentageField, currentInspectionReport.getProtectedCircuitsPercent());
			
			currentInteger = currentInspectionReport.getHardWiredLoads();
			if (currentInteger != null) {
				switch(currentInteger) {
					case 0:
						hardWiredLoadsUnder250Btn.setSelected(true);
						break;
					case 1:
						hardWiredLoadsUnder500Btn.setSelected(true);
						break;
					case 2:
						hardWiredLoadsUnder1000Btn.setSelected(true);
						break;
					case 3:
						hardWiredLoadsUnder5000Btn.setSelected(true);
						break;
					case 4:
						hardWiredLoadsAbove5000Btn.setSelected(true);
						break;
				}
			}
			
			furtherExplanationsField.setText(currentInspectionReport.getAdditionalAnnotations());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Get user Input
	 * 
	 * @return if loading was sucessful
	 */
	private void fetchInspectionReportData() {
		currentInspectionReport = new InspectionReportFull();
		
		//Company
		currentInspectionReport.setCompanyPlant(currentCompanyPlant);
		
		//ExaminationInfo
		currentInspectionReport.setCompanion(plantCompanionField.getText());
		currentInspectionReport.setSurveyor(plantExpertField.getText());
		if (Util.validateInt(plantAnerkNrField)){
			currentInspectionReport.setVdsApprovalNr(Integer.valueOf(plantAnerkNrField.getText()));
		}
		currentInspectionReport.setDate(datePickerExaminationDate.getValue());
		if(Util.validateDouble(plantInspectionTimeField)) {
			currentInspectionReport.setExaminationDuration(Double.valueOf(plantInspectionTimeField.getText()));
		}
		
		//Art des Betriebs/Anlage
		if(Util.validateInt(branchName)) {
			currentInspectionReport.setBranch(new Branch(Integer.valueOf(branchName.getText())));
		}
		currentInspectionReport.setFrequencyControlledUtilities(freqGroup.getSelectedToggle() != null ? freqYesBtn.isSelected() : null);
		currentInspectionReport.setPrecautionsDeclared(precautionGroup.getSelectedToggle() != null ? precautionYesBtn.isSelected() : null);
		currentInspectionReport.setPrecautionsDeclaredLocation(precautionField.getText());
		currentInspectionReport.setExaminationComplete(completeGroup.getSelectedToggle() != null ? completeYesBtn.isSelected() : null);
		currentInspectionReport.setSubsequentExaminationDate(datePickerSubsequentExaminationDate.getValue());
		currentInspectionReport.setExaminationIncompleteReason(completeReasonField.getText());
		currentInspectionReport.setChangesSinceLastExamination(changesSinceLastExaminationGroup.getSelectedToggle() != null ? getSelectedToggle(changesSinceLastExaminationGroup) : null);
		currentInspectionReport.setDefectsLastExaminationFixed(defectsLastExaminationFixedGroup.getSelectedToggle() != null ? getSelectedToggle(defectsLastExaminationFixedGroup) : null);
		
		//Gesamtbeurteilung der Anlage
		currentInspectionReport.setDangerCategory(dangerCategorieGroup.getSelectedToggle() != null ? getSelectedToggle(dangerCategorieGroup) : null);
		currentInspectionReport.setDangerCategoryDescription(dangerCategoryExtensionField.getText());
		
		//Prüfergebnis
		currentInspectionReport.setExaminationResultNoDefect(noDefectsBtn.isSelected());
		currentInspectionReport.setExaminationResultDefect(defectsAttachedBtn.isSelected());
		currentInspectionReport.setExaminationResultDefectDate(datePickerResolvedUntil.getValue());
		currentInspectionReport.setExaminationResultDanger(removeDefectsImmediatelyBtn.isSelected());
		
		
		//Messungen
		currentInspectionReport.setIsolationChecked(IsoMinGroup.getSelectedToggle() != null ? isoMinYesBtn.isSelected() : null);
		currentInspectionReport.setIsolationMesasurementProtocols(IsoProtocollGroup.getSelectedToggle() != null ? isoProtocolYesBtn.isSelected() : null);
		currentInspectionReport.setIsolationCompensationMeasures(IsoCompensationGroup.getSelectedToggle() != null ? isoCompensationYesBtn.isSelected() : null);
		currentInspectionReport.setIsolationCompensationMeasuresAnnotation(isoCompensationCommentField.getText());
		currentInspectionReport.setRcdAvailable(RcdGroup.getSelectedToggle() != null ? rcdAllBtn.isSelected() : null);
		if (Util.validateDouble(rcdPercentageField)) {
			currentInspectionReport.setRcdAvailablePercent(Double.valueOf(rcdPercentageField.getText()));
		}
		currentInspectionReport.setRcdAnnotation(rcdCommentField.getText());
		currentInspectionReport.setResistance(ResistanceGroup.getSelectedToggle() != null ? resistanceYesBtn.isSelected() : null);
		if (Util.validateInt(resistancePercentageField)) {
			currentInspectionReport.setResistanceNumber(Integer.valueOf(resistancePercentageField.getText()));
		}
		currentInspectionReport.setThermalAbnormality(ThermicGroup.getSelectedToggle() != null ? thermicYesBtn.isSelected() : null);
		currentInspectionReport.setResistanceAnnotation(resistanceCommentField.getText());
		
		
		//Ortsveränderliche Betriebsmittel
		currentInspectionReport.setInternalPortableUtilities(PortableUtilitiesGroup.getSelectedToggle() != null ? portableUtilitiesYesBtn.isSelected() : null);
		currentInspectionReport.setExternalPortableUtilities(ExternalPortableUtilitiesGroup.getSelectedToggle() != null ? getSelectedToggle(ExternalPortableUtilitiesGroup) : null);
		
		
		//Allgemeine Informationen
		currentInspectionReport.setSupplySystem(SupplySystemGroup.getSelectedToggle() != null ? getSelectedToggle(SupplySystemGroup) : null);
		if (Util.validateInt(powerConsumptionField)) {
			currentInspectionReport.setEnergyDemand(Integer.valueOf(powerConsumptionField.getText()));
		}
		if (Util.validateInt(externalPowerPercentageField)) {
			currentInspectionReport.setMaxEnergyDemandExternal(Integer.valueOf(externalPowerPercentageField.getText()));
		}
		if (Util.validateInt(maxCapacityPercentageField)) {
			currentInspectionReport.setMaxEnergyDemandInternal(Integer.valueOf(maxCapacityPercentageField.getText()));
		}
		if (Util.validateDouble(protectedCirclesPercentageField)) {
			currentInspectionReport.setProtectedCircuitsPercent(Integer.valueOf(protectedCirclesPercentageField.getText()));
		}
		currentInspectionReport.setHardWiredLoads(HardWiredLoadsGroup.getSelectedToggle() != null ? getSelectedToggle(HardWiredLoadsGroup) : null);
		currentInspectionReport.setAdditionalAnnotations(furtherExplanationsField.getText());
	}
	
	
	private int getSelectedToggle(ToggleGroup toggleGroup) {
		ObservableList<Toggle> toggles = toggleGroup.getToggles();
		for (int i = 0; i < toggles.size(); i++) {
			if (toggles.get(i).isSelected()) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Resets all buttons and textfields
	 */
	public void cleanUpSession() {
		currentCompany = null;
		currentCompanyPlant = null;
		currentInspectionReport = null;
		clearNode(borderPane);
		
		//TODO: Cleanup Table
	}
	
	private <T extends Pane> void clearNode(T parentPane) {
		for (Node newnode : parentPane.getChildren()) {
			if (newnode instanceof TextField) {
				((TextField) newnode).clear();
            } else {
            	if (newnode instanceof RadioButton) {
            		((RadioButton) newnode).setSelected(false);
            	} else {
            		if (newnode instanceof CheckBox) {
            			((CheckBox) newnode).setSelected(false);
            		} else {
            			if (newnode instanceof DatePicker) {
            				((DatePicker) newnode).setValue(null);
            			} else {
            				if (newnode instanceof ComboBox) {
            					((ComboBox) newnode).setValue(null);
            				}
            			}
            		}
            	}
            }
            if (newnode instanceof Pane) {
                clearNode((Pane) newnode);
            }
		} 
	}
	
	
	/**
	 * Export current result as PDF (after save)
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void exportDiagnosis (ActionEvent event) throws IOException{
		PDFExport.export(resultComplete.getId());
		}
	
	public void closeDiagnosis (ActionEvent event) throws IOException{
		//Check if saved
		//Cleanup every entry
		if(inspectionResultSaved) {
			mainController.closeDiagnosis();
			cleanUpSession();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warnung - Befundschein nicht gesichert");
			alert.setHeaderText("Achtung, der aktuelle Befundschein wurde noch nicht gespeichert.");
			alert.setContentText("Wollen sie den Befundschein verwerfen?");

			ButtonType discardButton = new ButtonType("Verwerfen");
			ButtonType saveButton = new ButtonType("Speichern");
			ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(discardButton, saveButton, cancelButton);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == discardButton){
				mainController.closeDiagnosis();
				cleanUpSession();
			} else if (result.get() == saveButton) {
				addDiagnosis(null);
			}
		}
	}
	
	/**
	 * Prepares the Defects Autocomplete TextField
	 */
	private void prepareDefectsAutocomplete() {
	//Defects
		final Task<ArrayList<Flaw>> autocompleteTask = new Task<ArrayList<Flaw>>() {
            @Override
            protected ArrayList<Flaw> call() throws Exception {
            	//Load Defects
        		return FlawAccess.getAllFlaws();
            }
        };
        autocompleteTask.setOnSucceeded(event ->
        	//Autocomplete: Initialize Suggestions
        	defectSearchField.getEntries().addAll(autocompleteTask.getValue())
    	);
        autocompleteTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + autocompleteTask.getException())
	    );
	    new Thread(autocompleteTask).start();
	    
	    //Add EventHandler - gets invoked when Suggestion is selected
	    defectSearchField.setAutoCompletionEvent(new AutoCompletionEvent() {
			@Override
			public void onAutoCompleteResult(AutocompleteSuggestion suggestion) {
				resultDefectId.setText(Integer.toString(suggestion.getExternalId()));
				newDefectId = suggestion.getExternalId();
			}
		});
	
	}
	
	/**
	 * Prepares the Branches Autocomplete TextField
	 */
	private void prepareBranchesAutocomplete() {
		//Branches
	    final Task<ArrayList<Branch>> branchAutocompleteTask = new Task<ArrayList<Branch>>() {
            @Override
            protected ArrayList<Branch> call() throws Exception {
            	//Load Branches
        		return BranchAccess.getBranches(false);
            }
        };
        branchAutocompleteTask.setOnSucceeded(event ->
        	//Autocomplete: Initialize Suggestions
        	branchText.getEntries().addAll(branchAutocompleteTask.getValue())
    	);
        branchAutocompleteTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + branchAutocompleteTask.getException())
	    );
	    new Thread(branchAutocompleteTask).start();
	    //Change AutoCompletionMode to Complete by ID
	    branchText.setAutoCompleteMode(1);
	    branchText.setAutoCompletionEvent(new AutoCompletionEvent() {
			@Override
			public void onAutoCompleteResult(AutocompleteSuggestion suggestion) {					
				//Nothing
			}
	    });
	}
	
	
	/**
	 * Prepares the Companies Autocomplete TextField
	 */
	private void loadCompanies() {
		final Task<ArrayList<Company>> loadCompaniesTask = new Task<ArrayList<Company>>() {
            @Override
            protected ArrayList<Company> call() throws Exception {
        		return CompanyAccess.getCompanies(false);
            }
        };
        loadCompaniesTask.setOnSucceeded(event -> {
        	companyList = loadCompaniesTask.getValue();
        	comboBoxCompanyName.setItems(FXCollections.observableArrayList(companyList));
        });
        loadCompaniesTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + loadCompaniesTask.getException())
	    );
	    new Thread(loadCompaniesTask).start();
	}
	
	/**
	 * Prepares the Company Plants Autocomplete TextField
	 */
	private void loadCompanyPlants() {
		final Task<ArrayList<CompanyPlant>> loadCompaniePlantTask = new Task<ArrayList<CompanyPlant>>() {
            @Override
            protected ArrayList<CompanyPlant> call() throws Exception {
        		return CompanyAccess.getCompanyPlants();
            }
        };
        
        loadCompaniePlantTask.setOnSucceeded(event -> {
        	companyPlantsList = loadCompaniePlantTask.getValue();
        	comboBoxCompanyPlantStreet.setItems(FXCollections.observableArrayList(companyPlantsList));
        });
        loadCompaniePlantTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + loadCompaniePlantTask.getException())
	    );
	    new Thread(loadCompaniePlantTask).start();
	}
	
	
	
	
	
	
	
	/**
	 * Validate if TextField is not empty (and check for NumberFormatExceptions)
	 * 
	 * @param tf - TextField
	 * @param checkInt - Check for NumberFormatExceptions?
	 * @return if textfield is not empty
	 */
	private boolean validate(TextField tf, boolean checkInt) {
	    if (tf.getText().isEmpty()) {
	    	tf.getStyleClass().add("error");
	    	return false;
	    }
	    else{
	    	if (checkInt) {
	    		try {
	    			Integer.valueOf(tf.getText());
	    		} catch (NumberFormatException e) {
	    			tf.getStyleClass().add("error");
	    			return false;
	    		}
	    	}
	    	tf.getStyleClass().remove("error");
	    }
	    return true;
	}
	

	public void prepare() {
		// TODO Auto-generated method stub
		cleanUpSession();
		loadCompanies();
		pdfExpBtn.setDisable(true);
	}
	
	
	/**
	 * checks result for mandatory field completeness
	 * 
	 * @return if complete
	 */
	public boolean checkMandatoryFields() {
		boolean valid = true;
		
		if (inspectionResultCompany == null) {
			valid = false;
			System.out.println("Company not valid");
		}
		
		if (plantExpertField.getText().isEmpty() || plantAnerkNrField.getText().isEmpty() || plantInspectionField.getText().isEmpty() || plantInspectionTimeField.getText().isEmpty()) {
			valid = false;
			System.out.println("Company plant not complete");
		}
		
		if (!freqYesBtn.isSelected() && !freqNoBtn.isSelected()) {
			valid = false;
			System.out.println("Frequency not selected");
		}
		
		if (branchName.getText().isEmpty()) {
			valid = false;
			System.out.println("Branch empty");
		}
		
		if (!precautionYesBtn.isSelected() && !precautionNoBtn.isSelected()) {
			valid = false;
			System.out.println("Precaution not selected");
		}
		
		if (precautionYesBtn.isSelected() && precautionField.getText().isEmpty()) {
			valid = false;
			System.out.println("Precaution field empty");
		}
		
		if (!completeYesBtn.isSelected() && !completeNoBtn.isSelected()) {
			valid = false;
			System.out.println("Completion not selected");
		}
		
		if (!changesSinceLastExaminationYesBtn.isSelected() && !changesSinceLastExaminationNoBtn.isSelected() && !changesSinceLastExaminationFirstExaminationBtn.isSelected()) {
			valid = false;
			System.out.println("Changes since last examination not selected");
		}
		
		if (!defectsLastExaminationYesBtn.isSelected() && !defectsLastExaminationNoBtn.isSelected() && !defectsLastExaminationNoReportBtn.isSelected()) {
			valid = false;
			System.out.println("Defects since last examination not selected");
		}
		
		if (!dangerCategorieGroupABtn.isSelected() && !dangerCategorieGroupBBtn.isSelected() && !dangerCategorieGroupCBtn.isSelected() && !dangerCategorieGroupDBtn.isSelected()) {
			valid = false;
			System.out.println("Danger Group not selected");
		}
		
		if (!noDefectsBtn.isSelected() && !defectsAttachedBtn.isSelected() && !removeDefectsImmediatelyBtn.isSelected()){
			valid = false;
			System.out.println("Result not selected");
		}
		
		if (defectsAttachedBtn.isSelected() && defectsAttachedDateField.getText().isEmpty()) {
			valid = false;
			System.out.println("Defects removel date empty");
		}
		
		if (!isoMinYesBtn.isSelected() && !isoMinNoBtn.isSelected()) {
			valid = false;
			System.out.println("Isolation not selected");
		}
		
		if (!isoProtocolYesBtn.isSelected() && !isoProtocolNoBtn.isSelected()) {
			valid = false;
			System.out.println("Isolation protocol not selected");
		}
		
		if (!isoCompensationYesBtn.isSelected() && !isoCompensationNoBtn.isSelected()) {
			valid = false;
			System.out.println("Isolation compensation not selected");
		}
		
		if (!rcdAllBtn.isSelected() && rcdNotBtn.isSelected()) {
			valid = false;
			System.out.println("RCD not selected");
		}
		
		if (rcdAllBtn.isSelected() && rcdPercentageField.getText().isEmpty()) {
			valid = false;
			System.out.println("RCD percentage empty");
		}
		
		if (!resistanceYesBtn.isSelected() && !resistanceNoBtn.isSelected()) {
			valid = false;
			System.out.println("Resistance not selected");
		}
		
		if (resistanceYesBtn.isSelected() && resistancePercentageField.getText().isEmpty()) {
			valid = false;
			System.out.println("Resistance percentage empty");
		}
		
		if (!thermicYesBtn.isSelected() && !thermicNoBtn.isSelected()) {
			valid = false;
			System.out.println("Thermic not selected");
		}
		
		if (portableUtilitiesYesBtn.isSelected() && !portableUtilitiesNoBtn.isSelected()) {
			valid = false;
			System.out.println("Portable utilities not selected");
		}
		
		if (!externalPortableUtilitiesYesBtn.isSelected() && !externalPortableUtilitiesNoBtn.isSelected() && !externalPortableUtilitiesNrBtn.isSelected()) {
			valid = false;
			System.out.println("External portable utilities not selected");
		}
		
		if (!supplySystemTNBtn.isSelected() && !supplySystemTTBtn.isSelected() && !supplySystemITBtn.isSelected() && !supplySystemCircleBtn.isSelected()) {
			valid = false;
			System.out.println("Supply system not selected");
		}
		
		if (!hardWiredLoadsUnder250Btn.isSelected() && !hardWiredLoadsUnder500Btn.isSelected() && !hardWiredLoadsUnder1000Btn.isSelected() && !hardWiredLoadsUnder5000Btn.isSelected() && !hardWiredLoadsAbove5000Btn.isSelected()) {
			valid = false;
			System.out.println("Hardwired loads not selected");
		}
		return valid;
	}
}
	
	
	
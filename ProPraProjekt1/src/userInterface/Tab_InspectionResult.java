package userInterface;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
	
	@FXML private Button vnLoadBtn;

	
// Risikoanschrift
	@FXML private Button plantLoadBtn;
	@FXML private AutocompleteTextField plantStreetField;
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
	
	
	//NEW
	private ArrayList<Company> companyList;
	private Company currentCompany;
	private ArrayList<CompanyPlant> companyPlantsList;
	private CompanyPlant currentCompanyPlant;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepareGUI();
		prepareData();
		prepareBranchesAutocomplete();
		prepareDefectsAutocomplete();
		loadCompanies();
		prepareTable();
		pdfExpBtn.setDisable(true);
    }
	
	private void prepareData() {
		loadCompanies();
		loadCompanyPlants();
	}

	private void prepareGUI() {
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
		
		
		comboBoxCompanyName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Company>() {
	        public void changed(ObservableValue<? extends Company> observable, Company oldValue, Company newValue) {
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
		
		comboBoxCompanyPlantStreet.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CompanyPlant>() {
	        public void changed(ObservableValue<? extends CompanyPlant> observable, CompanyPlant oldValue, CompanyPlant newValue) {
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
	        }
		});
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
	
	private void prepareTable() {
		defectIdColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("id"));
		dangerColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("dangerString"));
		buildingColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("building"));
		roomColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("room"));
		maschineColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("machine"));
		branchColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("branchId"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("defectCustomDescription"));
		
		defectTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (showTableDialog ==1) {
				tableSelctedId = defectTableView.getSelectionModel().getSelectedIndex();
				createDignosisOptionsDialog((FlawListElement) defectTableView.getSelectionModel().getSelectedItem());
				showTableDialog = 0;
			}
		});
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
		//Wrapps user input in ResulteComplete object
		if (loadResultData()) {
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
		}
		
	}
	
	
	/**
	 * Import Result for edit
	 * @throws SQLException 
	 */
	public void editDiagnosis(int id) {
		loadCompanies();
		InspectionReportFull result;
		try {
			result = InspectionReportAccess.getCompleteResult(id);
			
			defectTableView.setItems(FXCollections.observableArrayList(result.getDefects()));
			
			inspectionResultId = result.getId();
			completeReasonField.clear();
			dangerCategoryExtensionField.clear();
			isoCompensationCommentField.clear();
			rcdCommentField.clear();
			resistanceCommentField.clear();
			thermicCommentField.clear();
			furtherExplanationsField.clear();
			
			//fill fields and buttons
			plantInspectionField.setText(LocalDate.parse(result.getDate().toString()).toString());
			plantCompanionField.setText(result.getCompanion());
			plantExpertField.setText(result.getSurveyor());
			plantAnerkNrField.setText(String.valueOf(result.getVdsApprovalNr()));
			plantInspectionTimeField.setText(String.valueOf(result.getExaminationDuration()));
			
			branchName.setText(String.valueOf(result.getBranch().getExternalId()));
			
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
			
			inspectionResultCompanyPlant = result.getCompanyPlant();
			inspectionResultCompany = inspectionResultCompanyPlant.getCompany();
			plantStreetField.setText(inspectionResultCompanyPlant.getStreet());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	/**
	 * Resets all buttons and textfields
	 */
	public void reset() {
		resultComplete = null;
		pdfExpBtn.setDisable(true);
		newDefectDanger = 0;
		newDefectId = 0;
		tableUpdate = false;
		tableSelctedId = 0;
		showTableDialog = 1;
		defectElementId = -1;
		//InspectionResult Save
		inspectionResultSaved = false;
		inspectionResultId = 0;
		InspectionReportFull resultComplete = null;
		ArrayList<String> errors = null;
	//Tabelle 
		defectTableView.getItems().clear();
	// Versicherungsnehmer Adresse
		  inspectionResultCompany = null;
		  inspectionResultCompanyPlant = null;
		
	// Risikoanschrift
		  plantCompanionField.clear();
		  plantExpertField.clear();
		  plantAnerkNrField.clear();
		  plantInspectionField.clear();
		  plantInspectionTimeField.clear();
		
	//Art des Betriebes oder der Anlage
		  branchName.clear();
		  freqYesBtn.setSelected(false);
		  freqNoBtn.setSelected(false);
		  precautionYesBtn.setSelected(false);
		  precautionNoBtn.setSelected(false);
		  precautionField.clear();
		  completeYesBtn.setSelected(false);
		  completeNoBtn.setSelected(false);
		  completeDateField.clear();
		  completeReasonField.clear();
		  changesSinceLastExaminationYesBtn.setSelected(false);
		  changesSinceLastExaminationNoBtn.setSelected(false);
		  changesSinceLastExaminationFirstExaminationBtn.setSelected(false);
		  defectsLastExaminationYesBtn.setSelected(false);
		  defectsLastExaminationNoBtn.setSelected(false);
		  defectsLastExaminationNoReportBtn.setSelected(false);
		
	//Gesamtbeurteilung der Anlage
		  dangerCategorieGroupABtn.setSelected(false);
		  dangerCategorieGroupBBtn.setSelected(false);
		  dangerCategorieGroupCBtn.setSelected(false);
		  dangerCategorieGroupDBtn.setSelected(false);
		  dangerCategoryExtensionField.clear();
		
	//Prüfergebnis
		  noDefectsBtn.setSelected(false);
		  defectsAttachedBtn.setSelected(false);
		  defectsAttachedDateField.clear();
		  removeDefectsImmediatelyBtn.setSelected(false);

	// Messungen
		  isoMinYesBtn.setSelected(false);
		  isoMinNoBtn.setSelected(false);
		  isoProtocolYesBtn.setSelected(false);
		  isoProtocolNoBtn.setSelected(false);
		  isoCompensationYesBtn.setSelected(false);
		  isoCompensationNoBtn.setSelected(false);
		  isoCompensationCommentField.clear();
		  rcdAllBtn.setSelected(false);
		  rcdPercentageField.clear();
		  rcdNotBtn.setSelected(false);
		  rcdCommentField.clear();
		  resistanceYesBtn.setSelected(false);
		  resistancePercentageField.clear();
		  resistanceNoBtn.setSelected(false);
		  resistanceCommentField.clear();
		  thermicYesBtn.setSelected(false);
		  thermicNoBtn.setSelected(false);
		  thermicCommentField.clear();
		
	//Ortsveränderliche Betriebsmittel
		  portableUtilitiesYesBtn.setSelected(false);
		  portableUtilitiesNoBtn.setSelected(false);
		  externalPortableUtilitiesYesBtn.setSelected(false);
		  externalPortableUtilitiesNoBtn.setSelected(false);
		  externalPortableUtilitiesNrBtn.setSelected(false);
		
	//Allgemeine Informationen zur geprüften elektrischen Anlage
		  supplySystemTNBtn.setSelected(false);
		  supplySystemTTBtn.setSelected(false);
		  supplySystemITBtn.setSelected(false);
		  supplySystemCircleBtn.setSelected(false);
		  powerConsumptionField.clear();
		  externalPowerPercentageField.clear();
		  maxCapacityPercentageField.clear();
		  protectedCirclesPercentageField.clear();
		  hardWiredLoadsUnder250Btn.setSelected(false);
		  hardWiredLoadsUnder500Btn.setSelected(false);
		  hardWiredLoadsUnder1000Btn.setSelected(false);
		  hardWiredLoadsUnder5000Btn.setSelected(false);
		  hardWiredLoadsAbove5000Btn.setSelected(false);
		  furtherExplanationsField.clear();

	
    //Hinzufuegen
		  defectSearchField.clear();
		  resultDefectId.clear();
		  branchText.clear();
		  buildingText.clear();
		  roomText.clear();
		  machineText.clear();
		  customDescriptionText.clear();
		  dangerFireSwitchBox.disarm();
		  dangerPersonSwitchBox.disarm();
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
			reset();
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
				reset();
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
	 * Get user Input
	 * 
	 * @return if loading was sucessful
	 */
	private boolean loadResultData() {
		errors = new ArrayList<>();
		
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
		
		
		boolean valid = true;
		
		if (inspectionResultCompanyPlant == null) {
			valid = false;
			errors.add("Werk");
			System.out.println("Plant not valid");
		}
		
		if(plantCompanionField.getText().isEmpty() ||plantExpertField.getText().isEmpty() ||plantAnerkNrField.getText().isEmpty() ||plantInspectionField.getText().isEmpty() ||plantInspectionTimeField.getText().isEmpty()) {
			valid = false;
			errors.add("Risiko");
			System.out.println("Risiko not valid");
		}
		
		//Prüfergebnis
		if((!noDefectsBtn.isSelected() && !defectsAttachedBtn.isSelected() && !removeDefectsImmediatelyBtn.isSelected()) ||(defectsAttachedBtn.isSelected() && defectsAttachedDateField.getText().isEmpty())) {
			valid = false;
			errors.add("Prüfergebnis");
			System.out.println("Fehler: 'Prüfergebnis'");
		}
		
		//Art des Betriebs
		if((branchName.getText().isEmpty() || precautionYesBtn.isSelected() && precautionField.getText().isEmpty()) || (completeNoBtn.isSelected() && completeDateField.getText().isEmpty())) {
			valid = false;
			errors.add("Art des Betriebes oder der Anlage");
			System.out.println("Fehler: 'Art des Betriebes oder der Anlage'");
		}
		
		//Messungen
		if((rcdAllBtn.isSelected() && rcdPercentageField.getText().isEmpty()) || resistanceYesBtn.isSelected() && resistancePercentageField.getText().isEmpty()) {
			valid = false;
			errors.add("Messungen");
			System.out.println("Fehler: 'Messungen'");
		}
			
		
		if(powerConsumptionField.getText().isEmpty() || externalPowerPercentageField.getText().isEmpty() || maxCapacityPercentageField.getText().isEmpty() || protectedCirclesPercentageField.getText().isEmpty()) {
			valid = false;	
			errors.add("Allgemeine Informationen zur elektrischen Anlage");
			System.out.println("Fehler: 'Allgemeine Informationen zur elektrischen Anlage'");
		}
		
		
		//invalid combinations check and correction
		if(precautionNoBtn.isSelected() && !precautionField.getText().isEmpty()) {
				System.out.println("Eine Felderkombination ist nicht möglich");
				precautionField.clear();
		}
		
		if(completeYesBtn.isSelected() && !completeDateField.getText().isEmpty()) {
				System.out.println("Eine Felderkombination ist nicht möglich");
				completeDateField.setText("0001-01-01");
		}
		
		if(!defectsAttachedBtn.isSelected() && !defectsAttachedDateField.getText().isEmpty()) {
				System.out.println("Eine Felderkombination ist nicht möglich");
				defectsAttachedDateField.setText("0001-01-01");
		}
		
		
		
		if(rcdNotBtn.isSelected() && !rcdPercentageField.getText().isEmpty()) {
				System.out.println("Eine Felderkombination ist nicht möglich");
				rcdPercentageField.clear();
		}
		
		if(resistanceNoBtn.isSelected() && !resistancePercentageField.getText().isEmpty()) {
				System.out.println("Eine Felderkombination ist nicht möglich");
				resistancePercentageField.clear();
		}
		
		if(valid) {
			LocalDate date = LocalDate.parse(plantInspectionField.getText());
			LocalDate lastEdited = LocalDate.now();
			String companion = plantCompanionField.getText();
			String surveyor = plantExpertField.getText();
			int vdsApprovalNr = Integer.parseInt(plantAnerkNrField.getText());
			int branchId = Integer.valueOf(branchName.getText());
			double examinationDuration = Double.parseDouble(plantInspectionTimeField.getText());
			boolean frequencyControlledUtilities = freqYesBtn.isSelected();
			boolean precautionsDeclared = precautionYesBtn.isSelected();
			String precautionsDeclaredLocation = precautionField.getText();
			boolean examinationComplete = completeYesBtn.isSelected();
			LocalDate subsequentExaminationDate = null;
			if (!completeDateField.getText().isEmpty()) {
				subsequentExaminationDate  = LocalDate.parse(completeDateField.getText()); 
			}
			String examinationIncompleteReason = completeReasonField.getText();
			int changesSinceLastExamination = changesSinceLastEx;
			int defectsLastExaminationFixed = defectsLastEx;
			int dangerCategory = dangerGroup;										
			String dangerCategoryDescription = dangerCategoryExtensionField.getText();
			boolean examinationResultNoDefect = noDefectsBtn.isSelected();	
			boolean examinationResultDefect = defectsAttachedBtn.isSelected();
			LocalDate examinationResultDefectDate = null;
			if (!defectsAttachedDateField.getText().isEmpty()) {
				examinationResultDefectDate = LocalDate.parse(defectsAttachedDateField.getText()); 
			}
			boolean examinationResultDanger = removeDefectsImmediatelyBtn.isSelected();
			boolean isolationChecked = isoMinYesBtn.isSelected();
			boolean isolationMesasurementProtocols = isoProtocolYesBtn.isSelected();
			boolean isolationCompensationMeasures = isoCompensationYesBtn.isSelected();
			String isolationCompensationMeasuresAnnotation = isoCompensationCommentField.getText();
			boolean rcdAvailable = rcdAllBtn.isSelected();
			int rcdAvailablePercent = 0;
			if(!rcdPercentageField.getText().isEmpty()) {
				rcdAvailablePercent = Integer.parseInt(rcdPercentageField.getText());
			}
			String rcdAnnotation = rcdCommentField.getText();
			boolean resistance = resistanceYesBtn.isSelected();
			int resistanceNumber = 0;
			if(!resistancePercentageField.getText().isEmpty()) {
				resistanceNumber = Integer.parseInt(resistancePercentageField.getText());
			}
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
			Branch branch = new Branch(branchId,"");
			
			resultComplete = new InspectionReportFull(inspectionResultId,
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
					 inspectionResultCompanyPlant
					);
			resultComplete.setDefects(new ArrayList<>(defectTableView.getItems()));
			return true;
		}
		return false;
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
		reset();
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
	
	
	
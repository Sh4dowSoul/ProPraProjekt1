package userInterface;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import applicationLogic.Branch;
import applicationLogic.Company;
import applicationLogic.CompanyPlant;
import applicationLogic.ErrorNotification;
import applicationLogic.ExceptionDialog;
import applicationLogic.Flaw;
import applicationLogic.FlawListElement;
import applicationLogic.InspectionReportFull;
import applicationLogic.PDFExport;
import applicationLogic.Util;
import dataStorageAccess.BranchAccess;
import dataStorageAccess.CompanyAccess;
import dataStorageAccess.FlawAccess;
import dataStorageAccess.InspectionReportAccess;
import de.schnettler.AutocompleteTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
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
	@FXML private CheckBox dangerFireSwitchBox;
	@FXML private CheckBox dangerPersonSwitchBox;
	@FXML private Button addDefectButton;
	@FXML private Button cancelAddFlawButton;
	
	@FXML private BorderPane borderPane;
	@FXML private GridPane gridPaneFlawForm;
	
	//Information
	@FXML private Text informationReportId;
	@FXML private Text informationMode;
	@FXML private Text informationSaved;
	@FXML private Text informationValidated;
	
	private GUIController mainController;
	//New Defect
	int newDefectDanger;
	boolean tableUpdate = false;
	
	
	//Current Working Data
	//Mode
	boolean isEditMode = false;
	boolean flawFormIsEditMode = false;
	//Company
	private ArrayList<Company> companyList;
	private Company currentCompany;
	//CompanyPlant
	private ArrayList<CompanyPlant> companyPlantsList;
	private CompanyPlant currentCompanyPlant;
	//InpsectionReport
	private InspectionReportFull newInspectionReport;
	private InspectionReportFull importedInspectionReport;
	//Flaw
	private ArrayList<FlawListElement> importedFlawList;
	private Flaw currentFlaw;
	private FlawListElement currentFlawListElementEdit;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepareGUI();
		prepareData();
		prepareBranchesAutocomplete();
		prepareDefectsAutocomplete();
    }
	
	/**
	 * Prepare Company Data (For Company selection)
	 */
	private void prepareData() {
		loadCompanies();
		loadCompanyPlants();
	}

	/**
	 * Prepare the GUI
	 */
	private void prepareGUI() {
	//CompanyComboBox
		comboBoxCompanyName.setCellFactory(param -> new ListCell<Company>(){
			 @Override
			 public void updateItem(Company item, boolean empty){
				 super.updateItem(item, empty);
				 if(!empty) {
					 setText(item.getDescription());
					 setGraphic(null);
				 }
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
		comboBoxCompanyPlantStreet.setCellFactory(param -> new ListCell<CompanyPlant>(){
			 @Override
			 public void updateItem(CompanyPlant item, boolean empty){
				 super.updateItem(item, empty);
				 if(!empty) {
					 setText(item.getDescription());
					 setGraphic(null);
				 }
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
		comboBoxCompanyPlantStreet.getSelectionModel().selectedItemProperty().addListener((ChangeListener<CompanyPlant>) (observable, oldValue, newValue) -> {
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
		});
		
		//FlawList TableView
		defectIdColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("externalFlawId"));
		dangerColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("dangerString"));
		buildingColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("building"));
		roomColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("room"));
		maschineColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("machine"));
		branchColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("branchId"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<FlawListElement,String>("flawDescription"));
		defectTableView.setRowFactory( tv -> {
		    TableRow<FlawListElement> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		    	//DoubleClick to Edit
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		        	prepareEditOfFlawListElement(row.getItem());
		        }
		        //Right Click to open Menu (Remove, ... )
		        if (!row.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
		        	final ContextMenu contextMenu = new ContextMenu();
		        	MenuItem remove = new MenuItem("Entfernen");
		        	remove.setOnAction(event1 -> {
						defectTableView.getItems().remove(row.getIndex());
						defectTableView.refresh();
					});
		        	contextMenu.getItems().addAll(remove);
		        	contextMenu.show(row,event.getScreenX(), event.getScreenY());
                }
		    });
		    
		    //Drag Started
		    row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString("Not Needed");
                    db.setContent(cc);
                    event.consume();
                }
            });
		    
		    //Drag dropped
		    row.setOnDragDropped(event -> {
		    	//Get Source
		    	int draggedFrom = Integer.valueOf(((TableRow) event.getGestureSource()).getIndex());
		    	FlawListElement dragged = defectTableView.getItems().get(draggedFrom);
		    	//Remove item from Source
		    	defectTableView.getItems().remove(draggedFrom);
		    	//Add item to target
		    	defectTableView.getItems().add(row.getIndex(), dragged);
                event.setDropCompleted(true);
                event.consume();
            });
		    
		    //Dragged above another row
		    row.setOnDragOver(event -> {
		    	//Drag is not allowed if: - Dragged on source, dragged on empty cell
		    	if (event.getGestureSource() != row && !row.isEmpty()) {
		    		event.acceptTransferModes(TransferMode.MOVE);
		    	}
		    	event.consume();
		    });
		    return row ;
		});
		cancelAddFlawButton.setVisible(false);
	}

	/**
	 * Handle SaveCompanyButton Clicks - Save a new Company
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void saveCompany(ActionEvent event) throws IOException {
		if(Util.validateInt(textFieldCompanyZipCode, false) & Util.validateNotEmpty(textFieldCompanyCity) & Util.validateNotEmpty(textFieldCompanyStreet)) {
			currentCompany.setCity(textFieldCompanyCity.getText());
			currentCompany.setStreet(textFieldCompanyStreet.getText());
			currentCompany.setZipCode(Integer.valueOf(textFieldCompanyZipCode.getText()));
			try {
				//Insert Company into Database, get and set new Id
				currentCompany.setInternalId(CompanyAccess.insertCompany(currentCompany));
				disableCompanyPrepareCompanyPlant();
				comboBoxCompanyName.getItems().add(currentCompany);//Should be visible in ComboBox
				companyList.add(currentCompany);
				buttonSaveCompany.setDisable(true);
			} catch (SQLException e) {
                new ErrorNotification("Beim Speichern der Firma ist leider ein Fehler aufgetreten", "Fehler beim Speichern der Daten (Firma) in der Datenbank" ,e);
			}
		}
	}
	
	/**
	 * Handle SaveCompanyPlantButton Clicks - Save a new CompanyPlant
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void saveCompanyPlant(ActionEvent event) throws IOException {
		if(Util.validateInt(textFieldCompanyPlantZipCode, false) & Util.validateNotEmpty(textFieldCompanyPlantCity)) {
			currentCompanyPlant.setCity(textFieldCompanyPlantCity.getText());
			currentCompanyPlant.setZipCode(Integer.valueOf(textFieldCompanyPlantZipCode.getText()));
			
			try {
				//Insert CompanyPlant into Database, get and set new Id
				currentCompanyPlant.setInternalId(CompanyAccess.insertCompanyPlant(currentCompanyPlant));
				disableCompanyPlantTextFields();
				//Add new Plant to Lists
				comboBoxCompanyPlantStreet.getItems().add(currentCompanyPlant);//Should be visible in ComboBox
				companyPlantsList.add(currentCompanyPlant);//CompanyPlant List
				buttonSaveCompanyPlant.setDisable(true);
			} catch (SQLException e) {
				new ErrorNotification("Beim Speichern des Werks ist leider ein Fehler aufgetreten", "Fehler beim Speichern der Daten (Werk) in der Datenbank" ,e);
			}
			
		}
	}
	
	/**
	 * Disables the TextFields (which are used to add data for a new Company) and Prepare for Plant slection
	 */
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
	
	/**
	 * Disables the TextFields (which are used to add data for a new CompanyPlant)
	 */
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
	 * 
	 * Fetch Data for the creation of a new FlawListElement
	 * @return The new FlawListElement
	 */
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
			if(!FlawAccess.getFlawDescriptions(currentFlaw.getExternalId()).contains(flawDescriptionEntered)) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Neuen Mangel anlegen");
				alert.setHeaderText("Textbaustein als neuen Mangel abspeichern?");
				alert.setContentText("Unter der Mangelnummer " + currentFlaw.getExternalId() +" ist bisher kein wiederverwendbarer Textbaustein mit der Beschreibung \n\n'" + 
				flawDescriptionEntered +
				"'\n\nbekannt. Soll der eingegebene Textbaustein für eine spätere Verwendung gespeichert werden?" );
					

				ButtonType noButton = new ButtonType("Nein");
				ButtonType yesButton = new ButtonType("Ja");
				alert.getButtonTypes().setAll(noButton, yesButton);

				Optional<ButtonType> Dialogresult = alert.showAndWait();
				currentFlaw = new Flaw(currentFlaw.getExternalId(), true, flawDescriptionEntered);
				
				if(Dialogresult.get() == noButton) {
					currentFlaw.setDontShowAsSuggestion(true);
				}
				currentFlaw.setInternalId(FlawAccess.insertCustomFlaw(currentFlaw));
				if (Dialogresult.get() == yesButton) {
					defectSearchField.getEntries().add(currentFlaw);	
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new FlawListElement(currentFlaw, Integer.valueOf(branchText.getText()), defectDanger, buildingText.getText(), roomText.getText(), machineText.getText(), 0);
	}
	
	/**
	 * Handle "AddFlawToTable" Button press and add FlawListElement to table
	 * 
	 * @param add
	 */
	public void addFlawToTable(ActionEvent add) {
		if (Util.validateInt(resultDefectId, false) & Util.validateInt(branchText, false)) {
			
			if (!flawFormIsEditMode){
				//Add new Flaw to TableView
				defectTableView.getItems().add(getNewFlawData());
			} else {
				//Update Flaw in Tableview
				FlawListElement newData = getNewFlawData();
				currentFlawListElementEdit.setBranchId(newData.getBranchId());
				currentFlawListElementEdit.setBuilding(newData.getBuilding());
				currentFlawListElementEdit.setDanger(newData.getDanger());
				currentFlawListElementEdit.setFlaw(newData.getFlaw());
				currentFlawListElementEdit.setMachine(newData.getMachine());
				currentFlawListElementEdit.setRoom(newData.getRoom());
				defectTableView.refresh();
				flawFormIsEditMode = false;
			}
			
			//Reset addFlawForm
			resetAddToTable();
		}
	}
	
	
	/**
	 * Reset add to flaw table textfields & CheckBoxes
	 */
	public void resetAddToTable() {
		defectSearchField.clear();
		resultDefectId.clear();
		dangerFireSwitchBox.setSelected(false);
		dangerPersonSwitchBox.setSelected(false);
		buildingText.clear();
		roomText.clear();
		machineText.clear();
		machineText.clear();
		cancelAddFlawButton.setVisible(false);//Hide Cancel Button
		addDefectButton.setText("Hinzufügen");
	}
	
	/**
	 * Load a defect into "add defect to table" views
	 * 
	 * @param defect
	 */
	private void prepareEditOfFlawListElement(FlawListElement defect) {
		cancelAddFlawButton.setVisible(true);//Show Cancel Button
		currentFlawListElementEdit = defect;
		currentFlaw = defect.getFlaw();
		flawFormIsEditMode = true;
		defectSearchField.setText(defect.getFlaw().getDescription());
		defect.getFlaw().getExternalId();
		resultDefectId.setText(String.valueOf(defect.getFlaw().getExternalId()));
		branchText.setText(String.valueOf(defect.getBranchId()));
		int danger = defect.getDanger();
		defect.getElementId();
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
		addDefectButton.setText("Ändern");
	}
	
	
	/**
	 * Prepare Report for export to Database
	 * @throws SQLException 
	 */
	public void saveInspectionReportPrepare(ActionEvent add){
		//Fetch Data
		fetchInspectionReportData();
		//Validate
		validateIspectionReport(false);
		//Check if edited
		if (!compareInspectionReports()) {									//InspectionReport has been edited
			if (!isEditMode) {
				//Save as new InspectionReport
				saveInspectionReport(true);
			} else {
				showSaveModeDialog();
			}
		} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Information: Keine Änderung erkannt");
				alert.setContentText("Es wurde keine Änderung des Befundscheins erkannt. Der Speichervorgang wurde deshalb abgebrochen");
				alert.show();
		}
	}
	
	private boolean compareInspectionReports() {
		//Set imported Defects for Comparison (Unlinked from ObservableList)
		if (importedFlawList != null) {
			importedInspectionReport.setDefects(FXCollections.observableArrayList(importedFlawList));
		}
		return importedInspectionReport.equals(newInspectionReport);
	}
	
	private boolean validateIspectionReport(boolean showInGui) {
		//Validate
		boolean valid = Util.validateInspectionReport(newInspectionReport, showInGui);
		newInspectionReport.setValid(valid);
		return valid;
	}
	
	private boolean showSaveModeDialog() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Speichere Befundschein");
		alert.setHeaderText("Überschreiben oder neu speichern?");
		alert.setContentText("Wollen sie den ursprünglichen Befundschein überschreiben oder den aktuellen als neuen Befundschein abspeichern?");

		ButtonType overrideButton = new ButtonType("Überschreiben");
		ButtonType newButton = new ButtonType("Als neuen Befundschein speichern");
		ButtonType cancelButton = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(overrideButton, newButton, cancelButton);

		Optional<ButtonType> Dialogresult = alert.showAndWait();
		if (Dialogresult.get() == overrideButton){
			saveInspectionReport(false);
			return true;
		}
		if (Dialogresult.get() == newButton){
			saveInspectionReport(true);
			return true;
		}
		return false;
	}
	
	
	/**
	 *Save InspectionReport in DataBase
	 * 
	 * @param newReport
	 */
	private void saveInspectionReport(boolean newReport) {
		//save as new Report
		if (newReport) {
			newInspectionReport.setId(InspectionReportAccess.saveNewCompleteResult(newInspectionReport));
			importedInspectionReport = newInspectionReport;
			if(newInspectionReport.getDefects() != null) {
				setImportedFlawList(newInspectionReport.getDefects());
			}
			isEditMode = true;
			updateInformation();
			pdfExpBtn.setDisable(false);
		} else {
			//Override Report
			InspectionReportAccess.updateCompleteResult(newInspectionReport);
			importedInspectionReport = newInspectionReport;
			setImportedFlawList(newInspectionReport.getDefects());
			updateInformation();
		}
	}
	
	/**
	 * Update curent Information
	 */
	private void updateInformation() {
		informationReportId.setText(String.valueOf(importedInspectionReport.getId()));
		String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
		informationSaved.setText("Gespeichert (" + time + ")");
		String valid = newInspectionReport.isValid() ? "Vollständig (" : "Unvollständig (";
		informationValidated.setText(valid + time + ")");
		informationMode.setText(isEditMode ? "Bearbeitung" : "Neuerstellung");
	}
	
	
	 /**
	 * Prepare for creating an new InspectionReport
	 */
	public void createNewInspectionReport() {
		 isEditMode = false;
		 informationMode.setText("Neuerstellung");
		 importedInspectionReport = new InspectionReportFull();
		 pdfExpBtn.setDisable(true);
	 }
	    

	/**
	 * Import InspectionReport for edit
	 * @throws SQLException 
	 */
	public void importInspectionReport(int id) {
		//TODO Prepare for a new Session
		isEditMode = true;
		informationMode.setText("Bearbeitung");
		
		Boolean currentBoolean = null;
		Integer currentInteger = null;
		try {
			importedInspectionReport = InspectionReportAccess.getCompleteResult(id);
			informationReportId.setText(String.valueOf(importedInspectionReport.getId()));
			
			//Set FlawList to Tableview
			defectTableView.setItems(FXCollections.observableArrayList(importedInspectionReport.getDefects()));
			setImportedFlawList(importedInspectionReport.getDefects());
			if (importedFlawList == null) {
				importedInspectionReport.setDefects(null);
			}
			
			//Set current Company(Plant)
			if(importedInspectionReport.getCompanyPlant()!= null) {
				currentCompanyPlant = importedInspectionReport.getCompanyPlant();
				currentCompany = currentCompanyPlant.getCompany();
				//Load Company Data into form
				comboBoxCompanyName.setValue(currentCompany);
				currentCompanyPlant = importedInspectionReport.getCompanyPlant();//Warnung: Muss ein weiteres Mal geladen werden, da nachdem die Firma in die ComboBox geladen wurde, wurde currentCompanyPlant null gesetzt. TODO Fix this unwanted behavior
				currentCompany = currentCompanyPlant.getCompany();
				comboBoxCompanyPlantStreet.setValue(currentCompanyPlant);
			}
			
		
			//Load ExaminationInfo Data
			plantCompanionField.setText(importedInspectionReport.getCompanion());
			plantExpertField.setText(importedInspectionReport.getSurveyor());
			Util.setNullableIntToTextField(plantAnerkNrField, importedInspectionReport.getVdsApprovalNr());
			datePickerExaminationDate.setValue(importedInspectionReport.getDate());
			Util.setNullableDoubleToTextField(plantInspectionTimeField, importedInspectionReport.getExaminationDuration());
			
			//Load "Art des Betriebs" Data
			Branch branch = importedInspectionReport.getBranch();
			if (branch != null) {
				branchName.setText(String.valueOf(branch.getExternalId()));
			}
			
			currentBoolean = importedInspectionReport.isFrequencyControlledUtilities();
			if(currentBoolean != null) {
				if(currentBoolean) {
					freqYesBtn.setSelected(true);
				}else {
					freqNoBtn.setSelected(true);
				}
			}
			
			currentBoolean = importedInspectionReport.isPrecautionsDeclared();
			if(currentBoolean != null) {
				if(currentBoolean) {
					precautionYesBtn.setSelected(true);
				}else {
					precautionNoBtn.setSelected(true);
				}
			}
			precautionField.setText(importedInspectionReport.getPrecautionsDeclaredLocation());
			currentBoolean = importedInspectionReport.isExaminationComplete();
			if(currentBoolean != null) {
				if(currentBoolean) {
					completeYesBtn.setSelected(true);
				}else {
					completeNoBtn.setSelected(true);
				}
			}
			
			datePickerSubsequentExaminationDate.setValue(importedInspectionReport.getSubsequentExaminationDate());
			completeReasonField.setText(importedInspectionReport.getExaminationIncompleteReason());
			currentInteger = importedInspectionReport.getChangesSinceLastExamination();
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
			currentInteger = importedInspectionReport.getDefectsLastExaminationFixed();
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
			currentInteger = importedInspectionReport.getDangerCategory();
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
			dangerCategoryExtensionField.setText(importedInspectionReport.getDangerCategoryDescription());
			
			
			//Load "Prüfergebnis"
			currentBoolean = importedInspectionReport.isExaminationResultNoDefect();
			if (currentBoolean != null) {
				noDefectsBtn.setSelected(currentBoolean);
			}
			currentBoolean = importedInspectionReport.isExaminationResultDefect();
			if (currentBoolean != null) {
				defectsAttachedBtn.setSelected(currentBoolean);
			}
			currentBoolean = importedInspectionReport.isExaminationResultDanger();
			if (currentBoolean != null) {
				removeDefectsImmediatelyBtn.setSelected(currentBoolean);
			}
			datePickerResolvedUntil.setValue(importedInspectionReport.getExaminationResultDefectDate());
			
			
			//Load "Messungen"
			currentBoolean = importedInspectionReport.isIsolationChecked();
			if (currentBoolean != null) {
				if(currentBoolean) {
					isoMinYesBtn.setSelected(true);
				}else {
					isoMinNoBtn.setSelected(true);
				}
			}
			currentBoolean = importedInspectionReport.isIsolationMesasurementProtocols();
			if (currentBoolean != null) {
				if(currentBoolean) {
					isoProtocolYesBtn.setSelected(true);
				}else {
					isoProtocolNoBtn.setSelected(true);
				}
			}
			currentBoolean = importedInspectionReport.isIsolationCompensationMeasures();
			if (currentBoolean != null) {
				if(currentBoolean) {
					isoCompensationYesBtn.setSelected(true);
				}else {
					isoCompensationNoBtn.setSelected(true);
				}
			}
			isoCompensationCommentField.setText(importedInspectionReport.getIsolationCompensationMeasuresAnnotation());
			currentBoolean = importedInspectionReport.getRcdAvailable();
			if (currentBoolean != null) {
				if(currentBoolean) {
					rcdAllBtn.setSelected(true);
				}else {
					rcdNotBtn.setSelected(true);
				}
			}
			Util.setNullableDoubleToTextField(rcdPercentageField, importedInspectionReport.getRcdAvailablePercent());
			rcdCommentField.setText(importedInspectionReport.getRcdAnnotation());
			currentBoolean = importedInspectionReport.isResistance();
			if (currentBoolean != null) {
				if(currentBoolean) {
					resistanceYesBtn.setSelected(true);
				}else {
					resistanceNoBtn.setSelected(true);
				}
			}
			Util.setNullableIntToTextField(resistancePercentageField, importedInspectionReport.getResistanceNumber());
			resistanceCommentField.setText(importedInspectionReport.getResistanceAnnotation());
			currentBoolean = importedInspectionReport.isThermalAbnormality();
			if (currentBoolean != null) {
				if(currentBoolean) {
					thermicYesBtn.setSelected(true);
				}else {
					thermicNoBtn.setSelected(true);
				}
			}
			thermicCommentField.setText(importedInspectionReport.getThermalAbnormalityAnnotation());
			
			
			//Load "Ortsveränderliche Betriebsmittel"
			currentBoolean = importedInspectionReport.isInternalPortableUtilities();
			if (currentBoolean != null) {
				if(currentBoolean) {
					portableUtilitiesYesBtn.setSelected(true);
				}else {
					portableUtilitiesNoBtn.setSelected(true);
				}
			}
			
			currentInteger = importedInspectionReport.getExternalPortableUtilities();
			if (currentInteger != null) {
				switch(currentInteger) {
				case 0:
					externalPortableUtilitiesNoBtn.setSelected(true);
					break;
				case 1:
					externalPortableUtilitiesYesBtn.setSelected(true);
					break;
				case 2:
					externalPortableUtilitiesNrBtn.setSelected(true);
					break;
				}
			}
			
			
			//Load "Allgemeine Informationen..."
			currentInteger = importedInspectionReport.getSupplySystem();
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
			
			Util.setNullableIntToTextField(powerConsumptionField, importedInspectionReport.getEnergyDemand());
			Util.setNullableIntToTextField(externalPowerPercentageField, importedInspectionReport.getMaxEnergyDemandExternal());
			Util.setNullableIntToTextField(maxCapacityPercentageField, importedInspectionReport.getMaxEnergyDemandInternal());
			Util.setNullableIntToTextField(protectedCirclesPercentageField, importedInspectionReport.getProtectedCircuitsPercent());
			
			currentInteger = importedInspectionReport.getHardWiredLoads();
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
			
			furtherExplanationsField.setText(importedInspectionReport.getAdditionalAnnotations());
		} catch (SQLException e) {
			new ErrorNotification("Der ausgewählte Befundschein konnte leider nicht geladen werden", "Fehler beim Laden des Befundscheins aus der Datenbank", e);
		}
	}
	
	
	/**
	 * 
	 * Handle imported FlawList problems
	 * 
	 * @param flaws
	 */
	private void setImportedFlawList(ObservableList<FlawListElement> flaws) {
		importedFlawList = null;
		if (flaws != null && !flaws.isEmpty()) {
			importedFlawList = new ArrayList<>();
			for (FlawListElement flaw : importedInspectionReport.getDefects()) {
				importedFlawList.add(new FlawListElement(flaw.getElementId(), flaw.getFlaw(), flaw.getBranchId(), flaw.getDanger(), flaw.getBuilding(), flaw.getRoom(), flaw.getMachine(), flaw.getPosition()));
			}
		}
	}

	/**
	 * Fetch user Input (of InspectionReport)
	 * 
	 */
	private void fetchInspectionReportData() {
		int currentId = 0;
		if (importedInspectionReport != null) {
			currentId = importedInspectionReport.getId();
		}
		newInspectionReport = new InspectionReportFull();
		newInspectionReport.setId(currentId);
		//Get current position of Flaws
		for (FlawListElement element : defectTableView.getItems()) {
			element.setPosition(defectTableView.getItems().indexOf(element) + 1);
		}
		
		newInspectionReport.setDefects(defectTableView.getItems().isEmpty() ? null : defectTableView.getItems());
		
		//Company
		if (currentCompanyPlant != null) {
			newInspectionReport.setCompanyPlant(currentCompanyPlant);
			newInspectionReport.getCompanyPlant().setCompany(currentCompany);
		}
		
		//ExaminationInfo
		String plantCompanion = plantCompanionField.getText();
		newInspectionReport.setCompanion(plantCompanion != null && !plantCompanion.isEmpty()? plantCompanion : null);
		String plantExpert = plantExpertField.getText();
		newInspectionReport.setSurveyor(plantExpert != null && !plantExpert.isEmpty()? plantExpert : null);
		if (Util.validateInt(plantAnerkNrField, true)){
			newInspectionReport.setVdsApprovalNr(Integer.valueOf(plantAnerkNrField.getText()));
		}
		newInspectionReport.setDate(datePickerExaminationDate.getValue());
		if(Util.validateDouble(plantInspectionTimeField)) {
			newInspectionReport.setExaminationDuration(Double.valueOf(plantInspectionTimeField.getText()));
		}
		
		//Art des Betriebs/Anlage
		if(Util.validateInt(branchName, true)) {
			newInspectionReport.setBranch(new Branch(Integer.valueOf(branchName.getText())));
		}
		newInspectionReport.setFrequencyControlledUtilities(freqGroup.getSelectedToggle() != null ? freqYesBtn.isSelected() : null);
		newInspectionReport.setPrecautionsDeclared(precautionGroup.getSelectedToggle() != null ? precautionYesBtn.isSelected() : null);
		String precautionsDeclaredLocation = precautionField.getText();
		newInspectionReport.setPrecautionsDeclaredLocation(precautionsDeclaredLocation != null && !precautionsDeclaredLocation.isEmpty()? precautionsDeclaredLocation : null);
		newInspectionReport.setExaminationComplete(completeGroup.getSelectedToggle() != null ? completeYesBtn.isSelected() : null);
		newInspectionReport.setSubsequentExaminationDate(datePickerSubsequentExaminationDate.getValue());
		String examinationIncompleteReason = completeReasonField.getText();
		newInspectionReport.setExaminationIncompleteReason(examinationIncompleteReason != null && !examinationIncompleteReason.isEmpty()? examinationIncompleteReason : null);
		newInspectionReport.setChangesSinceLastExamination(changesSinceLastExaminationGroup.getSelectedToggle() != null ? Util.getSelectedToggle(changesSinceLastExaminationGroup) : null);
		newInspectionReport.setDefectsLastExaminationFixed(defectsLastExaminationFixedGroup.getSelectedToggle() != null ? Util.getSelectedToggle(defectsLastExaminationFixedGroup) : null);
		
		//Gesamtbeurteilung der Anlage
		newInspectionReport.setDangerCategory(dangerCategorieGroup.getSelectedToggle() != null ? Util.getSelectedToggle(dangerCategorieGroup) : null);
		String dangerCategoryDescription = dangerCategoryExtensionField.getText();
		newInspectionReport.setDangerCategoryDescription(dangerCategoryDescription != null && !dangerCategoryDescription.isEmpty()? dangerCategoryDescription : null);
		
		//Prüfergebnis
		newInspectionReport.setExaminationResultNoDefect(noDefectsBtn.isSelected());
		newInspectionReport.setExaminationResultDefect(defectsAttachedBtn.isSelected());
		newInspectionReport.setExaminationResultDefectDate(datePickerResolvedUntil.getValue());
		newInspectionReport.setExaminationResultDanger(removeDefectsImmediatelyBtn.isSelected());
		
		
		//Messungen
		newInspectionReport.setIsolationChecked(IsoMinGroup.getSelectedToggle() != null ? isoMinYesBtn.isSelected() : null);
		newInspectionReport.setIsolationMesasurementProtocols(IsoProtocollGroup.getSelectedToggle() != null ? isoProtocolYesBtn.isSelected() : null);
		newInspectionReport.setIsolationCompensationMeasures(IsoCompensationGroup.getSelectedToggle() != null ? isoCompensationYesBtn.isSelected() : null);
		String isolationCompensationMeasuresAnnotation = isoCompensationCommentField.getText();
		newInspectionReport.setIsolationCompensationMeasuresAnnotation(isolationCompensationMeasuresAnnotation != null && !isolationCompensationMeasuresAnnotation.isEmpty()? isolationCompensationMeasuresAnnotation : null);
		newInspectionReport.setRcdAvailable(RcdGroup.getSelectedToggle() != null ? rcdAllBtn.isSelected() : null);
		if (Util.validateDouble(rcdPercentageField)) {
			newInspectionReport.setRcdAvailablePercent(Double.valueOf(rcdPercentageField.getText()));
		}
		String rcdAnnotation = rcdCommentField.getText();
		newInspectionReport.setRcdAnnotation(rcdAnnotation != null && !rcdAnnotation.isEmpty()? rcdAnnotation : null);
		newInspectionReport.setResistance(ResistanceGroup.getSelectedToggle() != null ? resistanceYesBtn.isSelected() : null);
		if (Util.validateInt(resistancePercentageField, true)) {
			newInspectionReport.setResistanceNumber(Integer.valueOf(resistancePercentageField.getText()));
		}
		String resistanceAnnotation = resistanceCommentField.getText();
		newInspectionReport.setResistanceAnnotation(resistanceAnnotation != null && !resistanceAnnotation.isEmpty()? resistanceAnnotation : null);
		newInspectionReport.setThermalAbnormality(ThermicGroup.getSelectedToggle() != null ? thermicYesBtn.isSelected() : null);
		String thermalAbnormalityAnnotation = thermicCommentField.getText();
		newInspectionReport.setThermalAbnormalityAnnotation(thermalAbnormalityAnnotation != null && !thermalAbnormalityAnnotation.isEmpty()? thermalAbnormalityAnnotation : null);
		
		
		
		//Ortsveränderliche Betriebsmittel
		newInspectionReport.setInternalPortableUtilities(PortableUtilitiesGroup.getSelectedToggle() != null ? portableUtilitiesYesBtn.isSelected() : null);
		newInspectionReport.setExternalPortableUtilities(ExternalPortableUtilitiesGroup.getSelectedToggle() != null ? Util.getSelectedToggle(ExternalPortableUtilitiesGroup) : null);
		
		
		//Allgemeine Informationen
		newInspectionReport.setSupplySystem(SupplySystemGroup.getSelectedToggle() != null ? Util.getSelectedToggle(SupplySystemGroup) : null);
		if (Util.validateInt(powerConsumptionField, true)) {
			newInspectionReport.setEnergyDemand(Integer.valueOf(powerConsumptionField.getText()));
		}
		if (Util.validateInt(externalPowerPercentageField, true)) {
			newInspectionReport.setMaxEnergyDemandExternal(Integer.valueOf(externalPowerPercentageField.getText()));
		}
		if (Util.validateInt(maxCapacityPercentageField, true)) {
			newInspectionReport.setMaxEnergyDemandInternal(Integer.valueOf(maxCapacityPercentageField.getText()));
		}
		if (Util.validateDouble(protectedCirclesPercentageField)) {
			newInspectionReport.setProtectedCircuitsPercent(Integer.valueOf(protectedCirclesPercentageField.getText()));
		}
		newInspectionReport.setHardWiredLoads(HardWiredLoadsGroup.getSelectedToggle() != null ? Util.getSelectedToggle(HardWiredLoadsGroup) : null);
		newInspectionReport.setAdditionalAnnotations(furtherExplanationsField.getText() != null && !furtherExplanationsField.getText().isEmpty()? furtherExplanationsField.getText() : null);
	}
	
	
	

	/**
	 * CleanUp session (data, workspace variables)
	 */
	public void cleanUpSession() {
		currentCompany = null;
		currentCompanyPlant = null;
		newInspectionReport = null;
		importedInspectionReport = null;
		importedFlawList = null;
		informationMode.setText("-/-");
		informationReportId.setText("-/-");
		informationSaved.setText("-/-");
		informationValidated.setText("-/-");
		currentFlaw = null;
		currentFlawListElementEdit = null;
		isEditMode = false;
		flawFormIsEditMode = false;
		//FlawList Table
		defectTableView.getItems().clear();
		Util.clearNode(borderPane);
		Util.clearNode(gridPaneFlawForm);
		cancelAddFlawButton.setVisible(false);
	}
	
	
	
	
	/**
	 * Export current result as PDF (after save)
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void exportDiagnosis (ActionEvent event) throws IOException{
		fetchInspectionReportData();
		if(validateIspectionReport(true)) {
			//Check for Changes 
			if (!compareInspectionReports()) {
				//Save before Export
				if (showSaveModeDialog()) {
					//Changes saved, export
					PDFExport.export(newInspectionReport);
				}
			} else {
				//No Changes, export
				PDFExport.export(newInspectionReport);
			}
			
		}
	}
	
	/**
	 * Handle "CloseDiagnosisButton" clicks - Check for changes and close Tab
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void closeDiagnosis (ActionEvent event) throws IOException{
		fetchInspectionReportData();
		//Check for Changes 
		if (!importedInspectionReport.equals(newInspectionReport)) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Achtung, der aktuelle Befundschein wurde noch nicht gespeichert.");
			alert.setContentText("Wollen sie den Befundschein verwerfen oder abspeichern?");
			ButtonType discardButton = new ButtonType("Verwerfen");
			ButtonType saveButton = new ButtonType("Speichern");
			ButtonType cancelButton = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(discardButton, saveButton, cancelButton);					
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == discardButton){
				//Discard
				mainController.closeDiagnosis();
				cleanUpSession();
			} else if (result.get() == saveButton) {
				//Prepare Save
				saveInspectionReportPrepare(null);
			}
		} else{
			//No Changes
			mainController.closeDiagnosis();
			cleanUpSession();
		}
	}
	
	/**
	 * Prepares the Flaw Autocomplete TextField
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
	    	autocompleteTask.getException().printStackTrace()
	    );
	    new Thread(autocompleteTask).start();
	    
	    //Add EventHandler - gets invoked when Suggestion is selected
	    defectSearchField.setAutoCompletionEvent(suggestion -> {
			resultDefectId.setText(Integer.toString(suggestion.getExternalId()));
			currentFlaw = (Flaw) suggestion;
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
	    	branchAutocompleteTask.getException().printStackTrace()
	    );
	    new Thread(branchAutocompleteTask).start();
	    //Change AutoCompletionMode to Complete by ID
	    branchText.setAutoCompleteMode(1);
	    branchText.setAutoCompletionEvent(suggestion -> {					
			//Nothing
		});
	}
	
	
	/**
	 * Load all Companies
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
	    	loadCompaniesTask.getException().printStackTrace()
	    );
	    new Thread(loadCompaniesTask).start();
	}
	
	/**
	 * Load all CompanyPlants
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
	    	loadCompaniePlantTask.getException().printStackTrace()
	    );
	    new Thread(loadCompaniePlantTask).start();
	}
}
	
	
	
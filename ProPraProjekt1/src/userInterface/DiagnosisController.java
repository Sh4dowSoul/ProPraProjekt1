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

public class DiagnosisController implements Initializable{
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
	
	private AutoCompletionBinding ab;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepareAutocomplete();
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
	
}

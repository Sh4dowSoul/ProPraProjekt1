package userInterface;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import applicationLogic.ExceptionDialog;
import applicationLogic.Flaw;
import dataStorageAccess.FlawAccess;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class Tab_Manager implements Initializable {

//	*** ADD NEW DEFECT ***
	@FXML private TextField newIdField;
	@FXML private TextField newDefectField;
	@FXML private Button clearNewDefectButton;
	@FXML private Button createNewDefectButton;
	
//	*** EDIT A DEFECT ***
	@FXML private TextField toEditDefectField;
	@FXML private TextField toEditDefectIdField;
	@FXML private TextField newEditDefectField;
	@FXML private TextField newEditDefectIdField;
	@FXML private Button clearEditButton;
	@FXML private Button editDefectField;
	@FXML private Button editDefectIdField;
	
	@FXML private Button buttonExportCustomFlaws;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		clearNew();
		clearEdit();
		// TODO Auto-generated method stub
		
	}
	
	public void clearNew() {
		newDefectField.clear();
		newIdField.clear();
		
	}
		
	public void clearEdit() {
		toEditDefectField.clear();
		toEditDefectIdField.clear();
		newEditDefectField.clear();
		newEditDefectIdField.clear();
		
	}
}

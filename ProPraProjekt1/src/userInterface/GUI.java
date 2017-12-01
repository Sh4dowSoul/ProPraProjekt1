package userInterface;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import applicationLogic.Company;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GUI extends Application {
	private Stage primaryStage;
	private AnchorPane mainLayout;
	private TabPane mainTabPane;
	private Tab statsTab;
	private AnchorPane statsPane;
	private TabPane compAndBranchTabPane;
	private Tab companiesTab;
	private AnchorPane companiesAnchorPane;
	private ListView companyList1;
	@FXML
	ListView<String> companyList;
	ListView<String> recentlyUsedList;
	TableColumn companyColumn;
	TableColumn diagnosisColumn; 
	
	
	@Override
	public void start(Stage primaryStage) throws IOException, SQLException {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("DefectManager");
		primaryStage.setWidth(1300);	//width of anchorpane/mainLayout
		primaryStage.setHeight(950);
		startUp();
		showMainView();
		//show();
	}
	//alle panes etc adden bzw aufeinander adden
	private void showMainView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GUI.class.getResource("GUI.fxml"));
		mainLayout = loader.load();
		
		TabPane mainTabPane = new TabPane();
		Tab statsTab = new Tab();
		AnchorPane statsPane = new AnchorPane();
		TabPane compAndBranchTabPane = new TabPane();
		Tab companiesTab = new Tab();
		AnchorPane companiesAnchorPane = new AnchorPane();
		ListView companyList1 = new ListView();
		
		mainLayout.getChildren().add(mainTabPane);
		
		mainTabPane.getTabs().add(statsTab);
		//statsPane auf statsTab
		mainLayout.getChildren().add(statsPane);
		
		compAndBranchTabPane.getTabs().add(companiesTab);
		//compAndBranchTabPane auf statsPane
		mainLayout.getChildren().add(compAndBranchTabPane);
		
		//companiesAnchorPane auf companiesTab
		mainLayout.getChildren().add(companiesAnchorPane);
		
		//companyList1 auf companiesAnchorPane
		mainLayout.getChildren().add(companyList1);
		
		
		
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	//arraylist from companycontroller.getcompanies to observablelist<string> for companylist
	//über alle elemnte iterieren und .tostring
	public void startUp() throws SQLException {
		ArrayList <String> companies = new ArrayList<String>() ;
		
		for (int t=0; t<dataStorageAccess.controller.CompanyController.getCompanies().size(); t++) {
			companies.add(dataStorageAccess.controller.CompanyController.getCompanies().get(t).toString());
		}
		
		ObservableList<String> items = FXCollections.observableArrayList(companies);
		ListView<String> companyList = new ListView<String>(items);
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
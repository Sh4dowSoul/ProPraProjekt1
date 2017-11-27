package userInterface;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GUI extends Application {
	private Stage primaryStage;
	private AnchorPane mainLayout;
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
		//startUp();
		showMainView();
		//show();
	}
	
	private void showMainView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GUI.class.getResource("GUI.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void startUp() throws SQLException {
//		ObservableList<Company> items = FXCollections.observableList(dataStorageAccess.controller.CompanyController.getCompanies());
//		companyList.setItems(items);	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
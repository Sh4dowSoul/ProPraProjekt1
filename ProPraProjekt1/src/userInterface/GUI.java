package userInterface;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import com.google.common.io.Files;

import dataStorageAccess.DataSource;
import dataStorageAccess.controller.DataBaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author Salih Arslan & Sven Meyer
 *
 */
public class GUI extends Application {
	private Stage primaryStage;
	private AnchorPane mainLayout;
	private Stage stage;
	
	
	@Override
	public void start(Stage primaryStage) throws IOException, SQLException {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("DefectManager");
		primaryStage.setWidth(1300);	//width of anchorpane/mainLayout
		primaryStage.setHeight(950);
		
		//Prepare Database
		if (!new File(DataSource.getDatabasePath()).exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Datenbank nicht gefunden");
			alert.setHeaderText("Die Datenbank konnte nicht gefunden werden");
			alert.setContentText("Falls sie ein Backup der Datenbank besitzen, können sie es im folgenden auswählen. Ansonsten muss leider eine neue, leere Datenbank erstellt werden.");

			ButtonType searchButton = new ButtonType("Suche Daten");
			ButtonType createButton = new ButtonType("Erstelle neue Datenbank");
			
			alert.getButtonTypes().setAll(searchButton, createButton);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == searchButton){
				//Open FilePicker to select DatabaseFile
				FileChooser fileChooser = new FileChooser();

				// Set extension filter
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Datenbank (*.db)","*.db");
				fileChooser.getExtensionFilters().add(extFilter);
				
				// Show save file dialog
				File file = fileChooser.showOpenDialog(primaryStage);

				if (file != null) {
					Files.copy(file, new File(DataSource.getDatabasePath()));
				}
			} else {
				//Create New Database
				DataSource.configDataSource();
				DataBaseController.createNewDataBase();
				//showMainView();
			}
			
		} else {
			showMainView();
		}
		
	}
	/**
     * Initializes the main layout.
     */
	private void showMainView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GUI.class.getResource("GUI_Main.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		scene.getStylesheets().add(getClass().getResource("text-field-red-border.css").toExternalForm());
		primaryStage.setMaximized(true);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	public static void main(String[] args) throws IOException, SQLException {
		launch(args);
	}
	
}
package userInterface;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import com.google.common.io.Files;

import dataStorageAccess.DataSource;
import dataStorageAccess.controller.DataBaseController;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
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
	public void start(Stage primaryStage) throws IOException, SQLException, InterruptedException {
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

				//Copy and rename selected DatabaseFile
				if (file != null) {
					Files.copy(file, new File(DataSource.getDatabasePath()));
				}
				
				//TODO Validate Imported DB
				//Initialize Connection Pool
				DataSource.configDataSource();
				showMainView();
			} else {
				//Create New Database
				Alert creatingDbDialog = new Alert(AlertType.INFORMATION);
				creatingDbDialog.setTitle("Erstelle Datenbank");
				creatingDbDialog.setHeaderText("Erstelle neue Datenbank und füge Standardwerte ein");
				ProgressIndicator progressIndicator = new ProgressIndicator();
				creatingDbDialog.setGraphic(progressIndicator);
				creatingDbDialog.show();
	            
				//Initialize Connection Pool
				DataSource.configDataSource();
				
				createNewDbTask.setOnSucceeded(event -> {
					creatingDbDialog.close();
					try {
						showMainView();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				
				//Create DB
				Thread thread = new Thread(createNewDbTask);
				thread.start();
			}
		} else {
			//Prepare ConnectionPool
			DataSource.configDataSource();
			//TODO ValidateDB Structure
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
	
	Task<Void> createNewDbTask = new Task<Void>() {
		@Override 
		protected Void call() throws Exception {
			DataBaseController.createNewDataBase();
			return null;
		}
	};
}
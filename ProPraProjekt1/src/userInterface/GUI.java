package userInterface;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import com.google.common.io.Files;

import dataStorageAccess.BranchAccess;
import dataStorageAccess.DataSource;
import dataStorageAccess.controller.DataBaseController;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
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
	
	public static void main(String[] args) throws IOException, SQLException {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Mangel Manager");
		primaryStage.setWidth(1300);	//width of anchorpane/mainLayout
		primaryStage.setHeight(950);
		primaryStage.getIcons().add(new Image(GUI.class.getClassLoader().getResourceAsStream("resources/images/icon.png")));
		prepareDataBase();
	}
	
	/**
	 * Prepare DataBase (Check if it exists, handle creation of new DataBase)
	 */
	private void prepareDataBase() {
		//Check if Database File exists
		if (!new File(DataSource.getDatabasePath()).exists()) {
			troubleshootMissingDataBase("Datenbank nicht gefunden", "Die Datenbank konnte nicht gefunden werden" , "Falls sie ein Backup der Datenbank besitzen, können sie es nun wiederherstellen. Ansonsten muss eine neue leere Datenbank erstellt werden." );
		} else {
			//Prepare ConnectionPool
			DataSource.configDataSource();
			//Validate Database
			try {
				if (DataBaseController.getTableDeclarations()) {
					showMainView();
				} else {
					deleteInvalidDatabaseFile();
					troubleshootMissingDataBase("Datenbank nicht kompatibel", "Die ausgewählte Datenbank ist nicht kompatibel", "Falls sie ein Backup der Datenbank besitzen, können sie es nun wiederherstellen. Ansonsten muss eine neue leere Datenbank erstellt werden.");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * TroubleShoot missing DataBase. Creates a Dialog asking the User what to do (create New One? , Search for existing Backup?). 
	 * 
	 * @param title
	 * @param header
	 * @param content
	 */
	private void troubleshootMissingDataBase(String title, String header, String content) {
		//Database does not exist
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		ButtonType searchButton = new ButtonType("Suche Daten");
		ButtonType createButton = new ButtonType("Erstelle neue Datenbank");	
		alert.getButtonTypes().setAll(searchButton, createButton);
		
		//Search Button
		alert.getDialogPane().lookupButton(searchButton).addEventFilter(ActionEvent.ACTION, event -> {
			//Open FilePicker to select DatabaseFile
			FileChooser fileChooser = new FileChooser();

			// Set extension filter
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Datenbank (*.db)","*.db");
			fileChooser.getExtensionFilters().add(extFilter);
					
			// Show save file dialog
			File file = fileChooser.showOpenDialog(primaryStage);

			//Copy and rename selected DatabaseFile
			if (file != null) {
				//Database File has been selected
				//Check if Folder exists
				File databaseFolder = new File(DataSource.getDataBaseFolder());
				if (!databaseFolder.isDirectory()) {
					databaseFolder.mkdirs();
				}
				try {
					Files.copy(file, new File(DataSource.getDatabasePath()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Initialize Connection Pool
				DataSource.configDataSource();
				
				//Validate TableDefinitions
				try {
					if (DataBaseController.getTableDeclarations()) {
						showMainView();
					} else {
						//Database is invalid
						alert.setTitle("Datenbank nicht kompatibel");
						alert.setHeaderText("Die ausgewählte Datenbank ist nicht kompatibel");
						alert.setContentText("Die ausgewählte Datenbank ist nicht kompatibel/funktionsfähig. Sie können eine andere Datenbank auswählen oder eine neue erstellen lassen.");
						deleteInvalidDatabaseFile();
						event.consume();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				//No File Selected, keep Dialog
				event.consume();
			}
		 });
		
		//Create new Button
		alert.showAndWait().filter(response -> response == createButton).ifPresent(response -> createNewDatabase());
	}
	
	/**
	 * Delete an invalid Database
	 */
	private void deleteInvalidDatabaseFile() {
		//Delete invalid Database
		File dbFile = new File(DataSource.getDatabasePath());
		if (dbFile.exists()) {
			//Close DataSource if opened
			DataSource.closeDataSource();
			//Delete old DB File
			try {
				java.nio.file.Files.delete(dbFile.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Create a new Database
	 */
	private void createNewDatabase() {
		//Create New Database
		Alert creatingDbDialog = new Alert(AlertType.INFORMATION);
		creatingDbDialog.setTitle("Erstelle Datenbank");
		creatingDbDialog.setHeaderText("Erstelle neue Datenbank und füge Standardwerte ein");
		ProgressIndicator progressIndicator = new ProgressIndicator();
		creatingDbDialog.setGraphic(progressIndicator);
		creatingDbDialog.show();
	            
		//Check if Folder exists
		File databaseFolder = new File(DataSource.getDataBaseFolder());
		if (!databaseFolder.isDirectory()) {
			databaseFolder.mkdirs();
		}
		
		//Initialize Connection Pool
		DataSource.configDataSource();
				
		createNewDbTask.setOnSucceeded(event -> {
			creatingDbDialog.close();
			showMainView();	
		});
				
		//Create DB
		Thread thread = new Thread(createNewDbTask);
		thread.start();
	}
	
	
	/**
     * Initializes the main layout.
     */
	private void showMainView() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GUI.class.getResource("GUI_Main.fxml"));
		try {
			mainLayout = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scene scene = new Scene(mainLayout);
		scene.getStylesheets().add(getClass().getResource("text-field-red-border.css").toExternalForm());
		primaryStage.setMaximized(true);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	
	
	/**
	 * Task to create new DataBase
	 */
	Task<Void> createNewDbTask = new Task<Void>() {
		@Override 
		protected Void call() throws Exception {
			DataBaseController.createNewDataBase();
			return null;
		}
	};
}
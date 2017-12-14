package userInterface;

import java.io.IOException;
import java.sql.SQLException;

import applicationLogic.PDFExport;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
		showMainView();
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
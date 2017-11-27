package userInterface;

import java.io.IOException;

import applicationLogic.PDFExport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GUIController {
	
	@FXML
	private Label testLabel;
	@FXML
	private Button pdfExpBtn;
	
	public void add(ActionEvent add) {
		testLabel.setText("jojo");
	}
	
	public void del(ActionEvent del) {
		
	}
	
	public void filter(ActionEvent filter) {
		
	}
	
	public void export(ActionEvent export) throws IOException {
		PDFExport.export();
	}
	
	public void stats(ActionEvent stats) {
		
	}
	
}

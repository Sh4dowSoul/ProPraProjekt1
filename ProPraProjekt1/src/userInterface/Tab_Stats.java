package userInterface;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import applicationLogic.Branch;
import applicationLogic.Company;
import applicationLogic.ExceptionDialog;
import applicationLogic.FlawStatistic;
import dataStorageAccess.BranchAccess;
import dataStorageAccess.CompanyAccess;
import dataStorageAccess.DefectAccess;
import dataStorageAccess.StatisticAccess;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.scene.chart.XYChart;

/**
 * @author Niklas Schnettler
 *
 */
public class Tab_Stats implements Initializable{
	
	@FXML private ListView<Company> companyList;
	@FXML private Tab companyListTab;
	@FXML private ListView<Branch> branchList;
	@FXML private Tab branchListTab;
	@FXML private TabPane statTabs;
	@FXML private TableView<FlawStatistic> statisticTableView;
	@FXML private TableColumn<FlawStatistic,String> statsDefectsColumn;
	@FXML private TableColumn<FlawStatistic,String> statsDefectDescriptionColumn;
	@FXML private TableColumn<FlawStatistic,String> statsQuantityColumn;
	@FXML private Button xmlExpBtn;
	@FXML private ProgressIndicator statCompanyProgress;
	@FXML private ProgressIndicator statBranchProgress;
	@FXML private ProgressIndicator statResultProgress;
	@FXML private Button exportButton;
	
    @FXML private BarChart<?, ?> BarChart;
	@FXML private CategoryAxis x;
    @FXML private NumberAxis y;

	private Company currentCompany;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		XYChart.Series set1 = new XYChart.Series<>();
		set1.getData().add(new XYChart.Data("1801", 21));
		set1.getData().add(new XYChart.Data("1806", 12));
		set1.getData().add(new XYChart.Data("1803", 9));
		set1.getData().add(new XYChart.Data("1805", 9));
		set1.getData().add(new XYChart.Data("1101", 6));
		set1.getData().add(new XYChart.Data("1106", 6));
		set1.getData().add(new XYChart.Data("1301", 6));
		set1.getData().add(new XYChart.Data("1105", 3));
		set1.getData().add(new XYChart.Data("1107", 1));
		BarChart.getData().addAll(set1);
		
		setupTabListener();
		setupStatisticLists();
		prepareStatsTable();
		loadCompanyList();
	    loadBranchesList();
	    companyList.getSelectionModel().selectFirst();
    }
	
	
	/**
	 * Setup Listener to switch between companies and branches
	 */
	private void setupTabListener() {
		statTabs.getSelectionModel().selectedItemProperty().addListener(
			    new ChangeListener<Tab>() {
			        @Override
			        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
			            switch (t1.getId()) {
			            	case "companyListTab":
			            		companyList.getSelectionModel().select(0);
			            		break;
			            	case "branchListTab":
			            		branchList.getSelectionModel().select(0);
			            		currentCompany = null;
			            		exportButton.setVisible(false);
			            		break;
			            }
			        }
			    }
			);
	}


	/**
	 * Prepare Stats Table
	 */
	private void prepareStatsTable() {
    	statsDefectsColumn.setCellValueFactory(new PropertyValueFactory<FlawStatistic,String>("externalId"));
    	statsDefectDescriptionColumn.setCellValueFactory(new PropertyValueFactory<FlawStatistic,String>("Description"));
    	statsQuantityColumn.setCellValueFactory(new PropertyValueFactory<FlawStatistic,String>("numberOccurrence"));
	}
	
	

	
	/**
	 * Prepares Company and Branch Lists
	 * 		CellFactory: Content of the rows
	 * 		Listener: Get selected row
	 */
	private void setupStatisticLists() {
		companyList.setCellFactory(param -> new ListCell<Company>() {
			@Override
			protected void updateItem(Company item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				}
				else {
					setText(item.getDescription());
				} 
			}
		});
		companyList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Company>() {
			@Override
			public void changed(ObservableValue<? extends Company> observable, Company oldValue, Company newValue) {
				
				if (newValue.getInternalId() == -1) {
					loadCompanyStatistic(newValue.getInternalId(), true);
					exportButton.setVisible(false);
				}else {
					loadCompanyStatistic(newValue.getInternalId(), false);
					currentCompany = newValue;
					exportButton.setVisible(true);
				}
			}
		});
		
		
		branchList.setCellFactory(param -> new ListCell<Branch>() {
			@Override
			protected void updateItem(Branch item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				}
				else {
					if (item.getExternalId() != -1) {
						setText(item.getExternalId() + " - " + item.getDescription());
					} else {
						setText(item.getDescription());
					}
					
				} 
			}
		});
		branchList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Branch>() {
			@Override
			public void changed(ObservableValue<? extends Branch> observable, Branch oldValue, Branch newValue) {
				if (newValue.getExternalId() == -1) {
					loadBranchStatistic(newValue.getExternalId(), true);
				}else {
					loadBranchStatistic(newValue.getExternalId(), false);
				}
			}
		});
	}

	
	/**
	 * Loads the Companies in a Background Task
	 */
	private void loadCompanyList() {
		final Task<ObservableList<Company>> companyListTask = new Task<ObservableList<Company>>() {
            @Override
            protected ObservableList<Company> call() throws Exception {
        		return FXCollections.observableArrayList(CompanyAccess.getCompanies(true));
            }
        };
        statCompanyProgress.visibleProperty().bind(companyListTask.runningProperty());
	    companyListTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				companyList.setItems(companyListTask.getValue());
				companyList.getItems().add(0, new Company(-1, "Alle Firmen"));
				companyList.getSelectionModel().select(0);
			}
		});
	    companyListTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + companyListTask.getException())
	    );
	    new Thread(companyListTask).start();
	}
	
	
	/**
	 * Loads the Branches in a Background Task
	 */
	private void loadBranchesList() {
		final Task<ObservableList<Branch>> branchListTask = new Task<ObservableList<Branch>>() {
            @Override
            protected ObservableList<Branch> call() throws Exception {
        		return FXCollections.observableArrayList(BranchAccess.getBranches(true));
            }
        };
        statBranchProgress.visibleProperty().bind(branchListTask.runningProperty());
        branchListTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				branchList.setItems(branchListTask.getValue());
				branchList.getItems().add(0, new Branch(-1, "Alle Branchen"));
				//branchList.getSelectionModel().select(0);
			}
		});
        branchListTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + branchListTask.getException())
	    );
	    new Thread(branchListTask).start();
	}
	
	
	/**
	 * Loads the Statistic of a company in a Background Task
	 * @param companyId - Id of the selected Company
	 */
	private void loadCompanyStatistic(int companyId, boolean loadAll) {
		// TODO Auto-generated method stub
		final Task<ObservableList<FlawStatistic>> statisticListTask = new Task<ObservableList<FlawStatistic>>() {
            @Override
            protected ObservableList<FlawStatistic> call() throws Exception {
            	if (!loadAll) {
            		return FXCollections.observableArrayList(DefectAccess.getFrequentDefectsCompany(true, companyId));
            	} else {
            		return FXCollections.observableArrayList(DefectAccess.getFrequentDefectsCompany(false, companyId));
            	}
        		
            }
        };
        statResultProgress.visibleProperty().bind(statisticListTask.runningProperty());
        statisticListTask.setOnSucceeded(event ->
	    	statisticTableView.setItems(statisticListTask.getValue())
	    );
        statisticListTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + statisticListTask.getException())
	    );
	    new Thread(statisticListTask).start();
	}
	
	/**
	 * Loads the Statistic of a Branch in a Background Task
	 * @param companyId - Id of the selected Company
	 */
	private void loadBranchStatistic(int branchId, boolean loadAll) {
		// TODO Auto-generated method stub
		final Task<ObservableList<FlawStatistic>> branchStatisticListTask = new Task<ObservableList<FlawStatistic>>() {
            @Override
            protected ObservableList<FlawStatistic> call() throws Exception {
            	if (!loadAll) {
            		return FXCollections.observableArrayList(DefectAccess.getFrequentDefectsBranch(true, branchId));
            	} else {
            		return FXCollections.observableArrayList(DefectAccess.getFrequentDefectsBranch(false, branchId));
            	}
        		
            }
        };
        statResultProgress.visibleProperty().bind(branchStatisticListTask.runningProperty());
        branchStatisticListTask.setOnSucceeded(event ->
	    	statisticTableView.setItems(branchStatisticListTask.getValue())
	    );
        branchStatisticListTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + branchStatisticListTask.getException())
	    );
	    new Thread(branchStatisticListTask).start();
	}
	
	 /**
	  * Handle XML Export Button
	  * 
	 * @param event
	 */
	@FXML
	 private void handleExportButton(ActionEvent event) {
		 if(currentCompany != null) {
			 FileChooser fileChooser = new FileChooser();
   		  
             //Set extension filter
             FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Statistik Dateien (*.xml)", "*.xml");
             fileChooser.getExtensionFilters().add(extFilter);
             fileChooser.setInitialFileName("VdS-Statistik_" + currentCompany.getDescription() + "_" + new SimpleDateFormat("dd_MM_yyyy").format(Calendar.getInstance().getTime()) +".xml");

             
             //Show save file dialog
             File file = fileChooser.showSaveDialog(null);
             
             if(file != null){
            	 try {
					StatisticAccess.exportStatisticCompany(currentCompany.getInternalId(), file.getAbsolutePath());
            		Notifications.create()
                    .title("Erfolgreich gespeichert")
                    .text("Die Statistik "+ file.getName() +" wurde erfolgreich gespeichert ")
                    .onAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							try {
								Desktop.getDesktop().open(file);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					})
                    .showInformation();
				} catch (FileNotFoundException | SQLException e) {
					Notifications.create()
	                 .title("Es ist ein Problem aufgetreten")
	                 .text("Die Statistik "+ file.getName() +" konnte leider nicht gespeichert werden.")
	                 .hideAfter(Duration.INDEFINITE)
	                 .onAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							new ExceptionDialog("Export Fehler", "Fehler beim Exportieren", "Beim Exportieren der Statistik ist leider ein Fehler aufgetreten.", e);
						}
	                 })
	                 .showError();
				}
            	 
            	 
             }
	     }
	 }
	
}
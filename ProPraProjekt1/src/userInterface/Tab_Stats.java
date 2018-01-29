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

import applicationLogic.AutocompleteSuggestion;
import applicationLogic.Branch;
import applicationLogic.Company;
import applicationLogic.ExceptionDialog;
import applicationLogic.FlawStatistic;
import dataStorageAccess.BranchAccess;
import dataStorageAccess.CompanyAccess;
import dataStorageAccess.DefectAccess;
import dataStorageAccess.StatisticAccess;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
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

/**
 * @author Niklas Schnettler
 *
 */
public class Tab_Stats implements Initializable {

	@FXML
	private ListView<Company> companyList;
	@FXML
	private Tab companyListTab;
	@FXML
	private ListView<Branch> branchList;
	@FXML
	private Tab branchListTab;
	@FXML
	private TabPane statTabs;
	@FXML
	private TableView<FlawStatistic> statisticTableView;
	@FXML
	private TableColumn<FlawStatistic, String> statsDefectsColumn;
	@FXML
	private TableColumn<FlawStatistic, String> statsDefectDescriptionColumn;
	@FXML
	private TableColumn<FlawStatistic, String> statsQuantityColumn;
	@FXML
	private Button xmlExpBtn;
	@FXML
	private ProgressIndicator statCompanyProgress;
	@FXML
	private ProgressIndicator statBranchProgress;
	@FXML
	private ProgressIndicator statResultProgress;
	@FXML
	private Button exportButton;

	@FXML
	private PieChart chart;

	// Tasks
	private LoadCompaniesTask companiesTask;
	private LoadBranchesTask branchesTask;
	private LoadStatisticTask statisticTask;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		prepareGUI();
		prepareTasks();
		setupSelectionListeners();

		companyList.getSelectionModel().selectFirst();

	}

	/**
	 * Load Data (List of Companies and Branches with Flaws)
	 */
	protected void loadData() {
		companiesTask.restart();
		branchesTask.restart();
	}

	/**
	 * Prepare GUI
	 */
	private void prepareGUI() {
		// Chart Labels
		chart.setAnimated(false);

		// TableView
		statsDefectsColumn.setCellValueFactory(new PropertyValueFactory<FlawStatistic, String>("externalId"));
		statsDefectDescriptionColumn
				.setCellValueFactory(new PropertyValueFactory<FlawStatistic, String>("Description"));
		statsQuantityColumn.setCellValueFactory(new PropertyValueFactory<FlawStatistic, String>("numberOccurrence"));

		// Stats Tab (Company - Branch)
		companyList.getItems().add(0, new Company("Alle Firmen"));

		// Company Cell Factory
		companyList.setCellFactory(param -> new ListCell<Company>() {
			@Override
			protected void updateItem(Company item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getDescription());
				}
			}
		});

		// Branch Cell Factory
		branchList.setCellFactory(param -> new ListCell<Branch>() {
			@Override
			protected void updateItem(Branch item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getExternalId() + " - " + item.getDescription());
				}
			}
		});

	}

	/**
	 * Prepare Tasks to load Data
	 */
	private void prepareTasks() {
		// Company Task
		companiesTask = new LoadCompaniesTask();
		statCompanyProgress.visibleProperty().bind(companiesTask.runningProperty());
		companiesTask.setOnSucceeded(event -> {
			companyList.getItems().remove(1, companyList.getItems().size());
			companyList.getItems().addAll(companiesTask.getValue());
			companyList.getSelectionModel().select(0);
		});

		// Branch Task
		branchesTask = new LoadBranchesTask();
		statBranchProgress.visibleProperty().bind(branchesTask.runningProperty());
		branchesTask.setOnSucceeded(event -> {
			branchList.setItems((ObservableList<Branch>) branchesTask.getValue());
		});

		// Statistic Task
		statisticTask = new LoadStatisticTask();
		statResultProgress.visibleProperty().bind(statisticTask.runningProperty());
		statisticTask.setOnSucceeded(event -> {
			statisticTableView.setItems(statisticTask.getValue());
			generateChart(statisticTask.getValue());
		});
	}

	/**
	 * Setup Selection Listeners
	 */
	private void setupSelectionListeners() {
		// Company List Selection Listener
		companyList.getSelectionModel().selectedItemProperty()
				.addListener((ChangeListener<Company>) (observable, oldValue, newValue) -> {
					if (newValue != null) {
						statisticTask.setItem(newValue);
						statisticTask.restart();
						exportButton.setVisible(!newValue.isDummy());
					}
				});
		// Branch List Seletion Listener
		branchList.getSelectionModel().selectedItemProperty()
				.addListener((ChangeListener<Branch>) (observable, oldValue, newValue) -> {
					if (newValue != null) {
						statisticTask.setItem(newValue);
						statisticTask.restart();
					}
				});
		// TabBar
		statTabs.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Tab>) (ov, t, t1) -> {
			statisticTableView.getItems().clear();
			switch (t1.getId()) {
			case "companyListTab":
				companyList.getSelectionModel().select(0);
				branchList.getSelectionModel().clearSelection();
				break;
			case "branchListTab":
				branchList.getSelectionModel().select(0);
				companyList.getSelectionModel().clearSelection();
				exportButton.setVisible(false);
				break;
			}
		});
	}

	/**
	 * Handle XML Export Button
	 * 
	 * @param event
	 */
	@FXML
	private void handleExportButton(ActionEvent event) {
		Company selectedCompany = companyList.getSelectionModel().getSelectedItem();
		if (!selectedCompany.isDummy()) {
			FileChooser fileChooser = new FileChooser();

			// Set extension filter
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Statistik Dateien (*.xml)",
					"*.xml");
			fileChooser.getExtensionFilters().add(extFilter);
			fileChooser.setInitialFileName("VdS-Statistik_" + selectedCompany.getDescription() + "_"
					+ new SimpleDateFormat("dd_MM_yyyy").format(Calendar.getInstance().getTime()) + ".xml");

			// Show save file dialog
			File file = fileChooser.showSaveDialog(null);

			if (file != null) {
				try {
					StatisticAccess.exportStatisticCompany(selectedCompany.getInternalId(), file.getAbsolutePath());
					Notifications.create().title("Erfolgreich gespeichert")
							.text("Die Statistik " + file.getName() + " wurde erfolgreich gespeichert ")
							.onAction(event1 -> {
								try {
									Desktop.getDesktop().open(file);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}).showInformation();
				} catch (FileNotFoundException | SQLException e) {
					Notifications.create().title("Es ist ein Problem aufgetreten")
							.text("Die Statistik " + file.getName() + " konnte leider nicht gespeichert werden.")
							.hideAfter(Duration.INDEFINITE)
							.onAction(event1 -> new ExceptionDialog("Export Fehler", "Fehler beim Exportieren",
									"Beim Exportieren der Statistik ist leider ein Fehler aufgetreten.", e))
							.showError();
				}
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////// TASKS
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * Task to Load Companies
	 * 
	 * @author Niklas Schnettler
	 *
	 */
	private static class LoadCompaniesTask extends Service<ObservableList<Company>> {
		protected Task<ObservableList<Company>> createTask() {
			return new Task<ObservableList<Company>>() {
				protected ObservableList<Company> call() throws SQLException {
					return FXCollections.observableArrayList(CompanyAccess.getCompanies(true));
				}
			};
		}
	}

	/**
	 * 
	 * Task to load Branches
	 * 
	 * @author Niklas Schnettler
	 *
	 */
	private static class LoadBranchesTask extends Service<ObservableList<Branch>> {
		protected Task<ObservableList<Branch>> createTask() {
			return new Task<ObservableList<Branch>>() {
				protected ObservableList<Branch> call() throws SQLException {
					return FXCollections.observableArrayList(BranchAccess.getBranches(true));
				}
			};
		}
	}

	/**
	 * Task to load Statistics of Company/Branch
	 * 
	 * @author Niklas Schnettler
	 *
	 */
	private static class LoadStatisticTask extends Service<ObservableList<FlawStatistic>> {
		private AutocompleteSuggestion item;

		protected Task<ObservableList<FlawStatistic>> createTask() {
			return new Task<ObservableList<FlawStatistic>>() {
				protected ObservableList<FlawStatistic> call() throws SQLException {
					if (item instanceof Company) {
						// Statistic of Company
						return FXCollections.observableArrayList(DefectAccess
								.getFrequentDefectsCompany(!((Company) item).isDummy(), item.getInternalId()));
					} else {
						// Statistic of Branch
						return FXCollections
								.observableArrayList(DefectAccess.getFrequentDefectsBranch(item.getExternalId()));
					}
				}
			};
		}

		public void setItem(AutocompleteSuggestion item) {
			this.item = item;
		}
	}

	/**
	 * Generate PieChart
	 * 
	 * @param data
	 */
	private void generateChart(ObservableList<FlawStatistic> data) {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for (FlawStatistic flaw : data) {
			pieChartData.add(new PieChart.Data(String.valueOf(flaw.getExternalId()), flaw.getNumberOccurrence()));
		}

		chart.setData(FXCollections.observableArrayList(pieChartData));
	}
}
package dataStorageAccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * The Database connection Class
 * 
 * @author Niklas Schnettler
 *
 */
public class DataSource {

	private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;
    private static String dataBaseFolder = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() +"\\MangelManager\\";
    private static String databasePath = dataBaseFolder +"BefundscheineVerwaltung.db";
    public static boolean good;
    
    private static Map<String, String> tableDefinitions;
    
	
	public static void configDataSource() {
		config.setJdbcUrl("jdbc:sqlite:" + databasePath);
		config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }
	
	public static void closeDataSource() {
		if (dataSource != null && !dataSource.isClosed()) {
			dataSource.close();
		}
	}
    
    private DataSource() {}
    
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public static boolean isGood() {
		return good;
	}

	public static void setGood(boolean good) {
		DataSource.good = good;
	}

	public static String getDatabasePath() {
		return databasePath;
	}

	public static String getDataBaseFolder() {
		return dataBaseFolder;
	}

	public static Map<String, String> getTableSql() {
		return tableDefinitions;
	}
	
	static {
		//Table Definitions
    	tableDefinitions = new HashMap<String, String>();
        tableDefinitions.put("Branch", 
        		"CREATE TABLE Branch "
				+ "(branchId INTEGER UNIQUE, "
				+ "branchName TEXT NOT NULL UNIQUE, "
				+ "PRIMARY KEY(branchId))");
        tableDefinitions.put("Company", 
        		"CREATE TABLE Company "
				+ "(companyId INTEGER NOT NULL, "
				+ "companyName TEXT NOT NULL, "
				+ "companyStreet TEXT NOT NULL, "
				+ "companyZip INTEGER NOT NULL, "
				+ "companyCity TEXT NOT NULL, "
				+ "PRIMARY KEY(companyId))");
        tableDefinitions.put("CompanyPlant", 
        		"CREATE TABLE CompanyPlant "
				+ "(plantId INTEGER NOT NULL, "
				+ "companyId INTEGER NOT NULL, "
				+ "plantStreet TEXT NOT NULL, "
				+ "plantZip INTEGER NOT NULL, "
				+ "plantCity TEXT NOT NULL, "
				+ "PRIMARY KEY(plantId))");
        tableDefinitions.put("Flaw", 
        		"CREATE TABLE Flaw "
				+ "(internalFlawId INTEGER, "
				+ "externalFlawId INTEGER NOT NULL, "
				+ "isCustomFlaw INTEGER, "
				+ "flawDescription TEXT NOT NULL, "
				+ "PRIMARY KEY(internalFlawId))");
        tableDefinitions.put("FlawListElement", 
        		"CREATE TABLE FlawListElement "
				+ "(elementId INTEGER NOT NULL, "
				+ "inspectionReportId INTEGER NOT NULL, "
				+ "internalFlawId INTEGER NOT NULL, "
				+ "branchId	INTEGER NOT NULL, "
				+ "danger INTEGER, "
				+ "building TEXT, "
				+ "room	TEXT, "
				+ "machine TEXT, "
				+ "position INTEGER, "
				+ "FOREIGN KEY(internalFlawId) REFERENCES Flaw(internalFlawId), "
				+ "PRIMARY KEY(elementId), "
				+ "FOREIGN KEY(inspectionReportId) REFERENCES InspectionReport(inspectionReportId))");
        tableDefinitions.put("InspectionReport", 
        		"CREATE TABLE InspectionReport "
				+ "(inspectionReportId INTEGER NOT NULL, "
				+ "inspectionReportLastEdited TEXT, "
				+ "inspectionReportValidated INTEGER, "
				+ "plantId INTEGER, "
				+ "companion TEXT, "
				+ "surveyor TEXT, "
				+ "vdsApprovalNr INTEGER, "
				+ "examinationDate TEXT, "
				+ "examinationDuration REAL, "
				+ "branchId INTEGER, "
				+ "frequencyControlledUtilities INTEGER, "
				+ "precautionsDeclared INTEGER, "
				+ "precautionsDeclaredWhere TEXT, "
				+ "examinationCompleted INTEGER, "
				+ "subsequentExaminationDate TEXT, "
				+ "subsequentExaminationReason TEXT, "
				+ "changesSinceLastExamination INTEGER, "
				+ "defectsLastExaminationFixed INTEGER, "
				+ "dangerCategoryVds INTEGER, "
				+ "dangerCategoryVdsDescription TEXT, "
				+ "examinationResultNoDefect INTEGER, "
				+ "examinationResultDefect INTEGER, "
				+ "examinationResultDefectDate INTEGER, "
				+ "examinationResultDanger INTEGER, "
				+ "isolationCheckedEnough INTEGER, "
				+ "isolationMeasurementProtocols INTEGER, "
				+ "isolationCompensationMeasures INTEGER, "
				+ "isolationCompensationMeasuresAnnotation TEXT, "
				+ "rcdAvailable INTEGER, "
				+ "rcdAvailablePercent REAL, "
				+ "rcdAnnotation TEXT, "
				+ "resistance INTEGER, "
				+ "resistanceNumber INTEGER, "
				+ "resistanceAnnotation TEXT, "
				+ "thermalAbnormality INTEGER, "
				+ "thermalAbnormalityAnnotation TEXT, "
				+ "internalPortableUtilities INTEGER, "
				+ "externalPortableUtilities INTEGER, "
				+ "supplySystem INTEGER, "
				+ "energyDemand INTEGER, "
				+ "maxEnergyDemandExternal INTEGER, "
				+ "maxEnergyDemandInternal INTEGER, "
				+ "protectedCircuitsPercent INTEGER, "
				+ "hardWiredLoads INTEGER, "
				+ "additionalAnnotations TEXT, "
				+ "PRIMARY KEY(inspectionReportId), "
				+ "FOREIGN KEY(plantId) REFERENCES CompanyPlant(plantId))");
    }
}

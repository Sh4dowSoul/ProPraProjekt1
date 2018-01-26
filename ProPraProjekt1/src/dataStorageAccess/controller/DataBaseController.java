package dataStorageAccess.controller;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import applicationLogic.Branch;
import applicationLogic.Flaw;
import applicationLogic.FlawListElement;
import applicationLogic.Util;
import dataStorageAccess.BranchAccess;
import dataStorageAccess.DataSource;
import dataStorageAccess.FlawAccess;

public class DataBaseController {

	public static void createNewDataBase() throws SQLException, FileNotFoundException {
		Statement statement = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DataSource.getConnection();
			statement = connection.createStatement();
			//Branches
			statement.executeUpdate("CREATE TABLE Branch "
					+ "(branchId INTEGER UNIQUE, "
					+ "branchName TEXT NOT NULL UNIQUE, "
					+ "PRIMARY KEY(branchId))");
			//Companies
			statement.executeUpdate("CREATE TABLE Company "
					+ "(companyId INTEGER NOT NULL, "
					+ "companyName TEXT NOT NULL, "
					+ "companyStreet TEXT NOT NULL, "
					+ "companyZip INTEGER NOT NULL, "
					+ "companyCity TEXT NOT NULL, "
					+ "PRIMARY KEY(companyId))");
			//CompanyPlants
			statement.executeUpdate("CREATE TABLE CompanyPlant "
					+ "(plantId INTEGER NOT NULL, "
					+ "companyId INTEGER NOT NULL, "
					+ "plantStreet TEXT NOT NULL, "
					+ "plantZip INTEGER NOT NULL, "
					+ "plantCity TEXT NOT NULL, "
					+ "PRIMARY KEY(plantId))");
			//Flaws
			statement.executeUpdate("CREATE TABLE Flaw "
					+ "(internalFlawId INTEGER, "
					+ "externalFlawId INTEGER NOT NULL, "
					+ "isCustomFlaw INTEGER, "
					+ "flawDescription TEXT NOT NULL, "
					+ "PRIMARY KEY(internalFlawId))");
			//FlawListElement
			statement.executeUpdate("CREATE TABLE FlawListElement "
					+ "(elementId INTEGER NOT NULL, "
					+ "inspectionReportId INTEGER NOT NULL, "
					+ "internalFlawId INTEGER NOT NULL, "
					+ "branchId	INTEGER NOT NULL, "
					+ "danger INTEGER, "
					+ "building TEXT, "
					+ "room	TEXT, "
					+ "machine TEXT, "
					+ "position INTEGER, "
					+ "FOREIGN KEY(inspectionReportId) REFERENCES InspectionReport(inspectionReportId), "
					+ "FOREIGN KEY(internalFlawId) REFERENCES Flaw(internalFlawId), "
					+ "PRIMARY KEY(elementId))");
			//InspectionReport
			statement.executeUpdate("CREATE TABLE InspectionReport "
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
					+ "FOREIGN KEY(plantId) REFERENCES CompanyPlant(plantId), "
					+ "PRIMARY KEY(inspectionReportId))");
			
			//Insert Default branches
			String stmt = "INSERT INTO Branch "
					+ "(branchId, branchName) "
					+ "VALUES(?,?)";
			preparedStatement = connection.prepareStatement(stmt);
			for(Branch branch : BranchAccess.importBranchesFromXml()) {
				Util.setValues(
						preparedStatement, 
						branch.getInternalId(),
						branch.getDescription()
						);
				preparedStatement.executeUpdate();
			}
			
			//Insert Default Flaws
			stmt = "INSERT INTO Flaw "
					+ "(internalFlawId, externalFlawId, isCustomFlaw, flawDescription) "
					+ "VALUES(?,?,?,?)";
			preparedStatement = connection.prepareStatement(stmt);
			for (Flaw flaw : FlawAccess.importFlawsFromXml()) {
				Util.setValues(preparedStatement,
						flaw.getInternalId(),
						flaw.getExternalId(),
						flaw.isCustomFlaw(),
						flaw.getDescription()
						);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
}

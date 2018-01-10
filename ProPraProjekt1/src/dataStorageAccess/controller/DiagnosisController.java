package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import applicationLogic.Branch;
import applicationLogic.Company;
import applicationLogic.CompanyPlant;
import applicationLogic.InspectionReportFull;
import applicationLogic.InspectionReportStatistic;
import applicationLogic.InspectionResultPreview;
import applicationLogic.Util;
import dataStorageAccess.DataSource;

/**
 * @author Niklas Schnettler
 *
 */
public class DiagnosisController {
	
	/**
	 * @param number of Diagnoses you want to get
	 * @return a list of the "n" last edited Diagnoses 
	 * @throws SQLException
	 */
	public static ArrayList<InspectionResultPreview> getLastEditedDiagnosesPreview(int number) throws SQLException {
		ArrayList<InspectionResultPreview> result = new ArrayList<InspectionResultPreview>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT inspectionReportId, examinationDate, companyId, companyName, inspectionReportLastEdited "+ 
					"FROM InspectionReport NATURAL JOIN Company NATURAL JOIN CompanyPlant "+ 
					"ORDER BY inspectionReportLastEdited desc "+
					"LIMIT " + number);
		) {
			while (resultSet.next()) {
				result.add(new InspectionResultPreview(
						resultSet.getInt("inspectionReportId"), 
						LocalDate.parse(resultSet.getString("examinationDate")), 
						resultSet.getInt("companyId"), 
						resultSet.getString("companyName"), 
						LocalDate.parse(resultSet.getString("inspectionReportLastEdited"))));
			}
		}
		return result;
	}
	
	/**
	 * @return A List of Previews of Diagnoses (id, date, companyId, companyName)
	 * @throws SQLException
	 */
	public static ArrayList<InspectionResultPreview> getDiagnosesPreview() throws SQLException {
		ArrayList<InspectionResultPreview> result = new ArrayList<InspectionResultPreview>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT inspectionReportId, examinationDate, companyId, companyName, inspectionReportLastEdited "+ 
					"FROM InspectionReport NATURAL JOIN Company NATURAL JOIN CompanyPlant "+ 
					"ORDER BY companyName");
		) {
			while (resultSet.next()) {
				result.add(new InspectionResultPreview(
						resultSet.getInt("inspectionReportId"), 
						LocalDate.parse(resultSet.getString("examinationDate")), 
						resultSet.getInt("companyId"), 
						resultSet.getString("companyName"), 
						LocalDate.parse(resultSet.getString("inspectionReportLastEdited"))));
			}
		}
		return result;
	}
	
	/**
	 * @param id - diagnosis_id from Diagnosis Table
	 * @return the whole Diagnosis, loaded from the database
	 * @throws SQLException
	 */
	public static InspectionReportFull getDiagnosis(int id) throws SQLException {
		InspectionReportFull result = null;
		try (
				Connection connection = DataSource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(
						"SELECT * "+ 
						"FROM InspectionReport NATURAL JOIN CompanyPlant Natural JOIN Company NATURAL JOIN Branch "+ 
						"WHERE inspectionReportId = " + id);
			) {
				while (resultSet.next()) {
					result = new InspectionReportFull(resultSet.getInt("InspectionReportId"),
											LocalDate.parse(resultSet.getString("examinationDate")),
											LocalDate.parse(resultSet.getString("inspectionReportLastEdited")),
											resultSet.getString("companion"), 
											resultSet.getString("surveyor"),
											resultSet.getInt("vdsApprovalNr"), 
											resultSet.getDouble("examinationDuration"), 
											new Branch(
													resultSet.getInt("branchId"),
													resultSet.getString("branchName")
														),
											resultSet.getBoolean("frequencyControlledUtilities"), 
											resultSet.getBoolean("precautionsDeclared"),
											resultSet.getString("precautionsDeclaredWhere"),
											resultSet.getBoolean("examinationCompleted"),
											LocalDate.parse(resultSet.getString("subsequentExaminationDate")),
											resultSet.getString("subsequentExaminationReason"),
											resultSet.getInt("changesSinceLastExamination"),
											resultSet.getInt("defectsLastExaminationFixed"),
											resultSet.getInt("dangerCategoryVds"),
											resultSet.getString("dangerCategoryVdsDescription"),
											resultSet.getBoolean("examinationResultNoDefect"),
											resultSet.getBoolean("examinationResultDefect"),
											LocalDate.parse(resultSet.getString("examinationResultDefectDate")),
											resultSet.getBoolean("examinationResultDanger"),
											resultSet.getBoolean("isolationCheckedEnough"),
											resultSet.getBoolean("isolationMeasurementProtocols"),
											resultSet.getBoolean("isolationCompensationMeasures"),
											resultSet.getString("isolationCompensationMeasuresAnnotation"),
											resultSet.getBoolean("rcdAvailable"),
											resultSet.getInt("rcdAvailablePercent"),
											resultSet.getString("rcdAnnotation"),
											resultSet.getBoolean("resistance"),
											resultSet.getInt("resistanceNumber"),
											resultSet.getString("resistanceAnnotation"),
											resultSet.getBoolean("thermalAbnormality"),
											resultSet.getString("thermalAbnormalityAnnotation"),
											resultSet.getBoolean("internalPortableUtilities"),
											resultSet.getInt("externalPortableUtilities"),
											resultSet.getInt("supplySystem"),
											resultSet.getInt("energyDemand"),
											resultSet.getInt("maxEnergyDemandExternal"),
											resultSet.getInt("maxEnergyDemandInternal"),
											resultSet.getInt("protectedCircuitsPercent"),
											resultSet.getInt("hardWiredLoads"),
											resultSet.getString("additionalAnnotations"),
											new CompanyPlant(
													resultSet.getInt("plantId"),
													resultSet.getString("plantStreet"),
													resultSet.getInt("plantZip"),
													resultSet.getString("plantCity"),
													new Company(
															resultSet.getInt("companyId"),
															resultSet.getString("companyName"),
															resultSet.getString("companyStreet"),
															resultSet.getInt("companyZip"),
															resultSet.getString("companyCity")
															)
													)
											);
				}
			}
			return result;
	}
	
	
	/**
	 * Get a List of Defects and Diagnoses of a company (for statistic export)
	 * 
	 * @param companyId - Id of a Company
	 * @return A List of StatisticResults
	 * @throws SQLException
	 */
	public static ArrayList<InspectionReportStatistic> getInspectionReportsForXml(int companyId) throws SQLException{
		ArrayList<InspectionReportStatistic> result = new ArrayList<InspectionReportStatistic>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT inspectionReportId, examinationDate, companyName, branchId, hardWiredLoads, dangerCategoryVds "+
					"FROM InspectionReport Natural JOIN Company Natural JOIN CompanyPlant " +
					"WHERE companyId = " + companyId);
		) {
			while (resultSet.next()) {
				result.add(new InspectionReportStatistic(
						resultSet.getInt("inspectionReportId"), 
						LocalDate.parse(resultSet.getString("examinationDate")),
						resultSet.getInt("dangerCategoryVds"),
						resultSet.getInt("branchId"),
						resultSet.getString("companyName"),
						resultSet.getInt("hardWiredLoads")));
			}
		}
		return result;
	}
	
	/**
	 * Update a Disgnosis in the Database
	 * 
	 * @param diagnosis - A Diagnosis
	 * @throws SQLException
	 */
	public static void updateDiagnosis(InspectionReportFull diagnosis) throws SQLException {
		System.out.println("TEST " + diagnosis.getId() + " " + "Test2 " + diagnosis.getExaminationDuration());
		String statement = "UPDATE Diagnosis "
				+ "SET inspectionReportLastEdited = ?, plantId = ?, companion = ?, "
				+ "surveyor = ?, vdsApprovalNr = ?, examinationDate = ?, "
				+ "examinationDuration = ?, branchId = ?,frequencyControlledUtilities = ?, precautionsDeclared = ?, "
				+ "precautionsDeclaredWhere = ?, examinationCompleted = ?, subsequentExaminationDate = ?, "
				+ "subsequentExaminationReason = ?, changesSinceLastExamination = ?, defectsLastExaminationFixed = ?, "
				+ "dangerCategorieVds = ?, dangerCategorieVdsDescription = ?, examinationResultNoDefect = ?, "
				+ "examinationResultDefect = ?, examinationResultDefectDate = ?, examinationResultDanger = ?, isolationCheckedEnough = ?, "
				+ "isolationMeasurementProtocols = ?, isolationCompensationMeasures = ?, isolationCompensationMeasuresAnnotation = ?, "
				+ "rcdAvailable = ?, rcdAvailablePercent = ?, rcdAnnotation = ?, "
				+ "resistance = ?, resistanceNumber = ?, resistanceAnnotation = ?, "
				+ "thermalAbnormality = ?, thermalAbnormalityAnnotation = ?, internalPortableUtilities = ?, "
				+ "externalPortableUtilities = ?, supplySystem = ?, energyDemand = ?, "
				+ "maxEnergyDemandExternal = ?, maxEnergyDemandInternal = ?, protectedCircuitsPercent = ?, "
				+ "hardWiredLoads = ?, additionalAnnotations = ? " 
				+ "WHERE diagnosis_id = " + diagnosis.getId();

		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DataSource.getConnection();
			preparedStatement = connection.prepareStatement(statement);

			Util.setValues(preparedStatement,
					diagnosis.getLastEdited(), diagnosis.getCompanyPlant().getInternalId(), diagnosis.getCompanion(), 
					diagnosis.getSurveyor(), diagnosis.getVdsApprovalNr(), diagnosis.getDate(),
					diagnosis.getExaminationDuration(),diagnosis.getBranch().getInternalId(), diagnosis.isFrequencyControlledUtilities(),diagnosis.isPrecautionsDeclared(),
					diagnosis.getPrecautionsDeclaredLocation(), diagnosis.isExaminationComplete(), diagnosis.getSubsequentExaminationDate(),
					diagnosis.getExaminationIncompleteReason(), diagnosis.getChangesSinceLastExamination(), diagnosis.getDefectsLastExaminationFixed(),
					diagnosis.getDangerCategory(), diagnosis.getDangerCategoryDescription(), diagnosis.isExaminationResultNoDefect(),
					diagnosis.isExaminationResultDefect(),diagnosis.getExaminationResultDefectDate(), diagnosis.isExaminationResultDanger(), diagnosis.isIsolationChecked(),
					diagnosis.isIsolationMesasurementProtocols(), diagnosis.isIsolationCompensationMeasures(), diagnosis.getIsolationCompensationMeasuresAnnotation(),
					diagnosis.getRcdAvailable(), diagnosis.getRcdAvailablePercent(), diagnosis.getRcdAnnotation(),
					diagnosis.isResistance(), diagnosis.getResistanceNumber(), diagnosis.getResistanceAnnotation(),
					diagnosis.isThermalAbnormality(), diagnosis.getThermalAbnormalityAnnotation(), diagnosis.isInternalPortableUtilities(),
					diagnosis.getExternalPortableUtilities(), diagnosis.getSupplySystem(), diagnosis.getEnergyDemand(),
					diagnosis.getMaxEnergyDemandExternal(), diagnosis.getMaxEnergyDemandInternal(), diagnosis.getProtectedCircuitsPercent(),
					diagnosis.getHardWiredLoads(), diagnosis.getAdditionalAnnotations());
		
			// execute insert SQL statement
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	
	/**
	 * Inserts Diagnosis into Database
	 * @param diagnosis - Diagnosis, which should be inserted into the Database
	 * @throws SQLException
	 */
	public static int insertDiagnosis(InspectionReportFull diagnosis) throws SQLException {
		int diagnosisId = 0;
		String statement = "INSERT INTO Diagnosis "
				+ "(diagnosis_lastEdited, plant_id, companion, "
				+ "surveyor, vds_approval_nr, examination_date, "
				+ "examination_duration, branch_id,frequency_controlled_utilities, precautions_declared, "
				+ "precautions_declared_where, examination_completed, subsequent_examination_date, "
				+ "subsequent_examination_reason, changes_sincel_last_examination, defects_last_examination_fixed, "
				+ "danger_categorie_vds, danger_categorie_vds_description, examination_resultNoDefect, "
				+ "examination_resultDefect, examinationResultDefectDate, examination_resultDanger, isolation_checkedEnough, "
				+ "isolation_measurementProtocols, isolation_compensationMeasures, isolation_compensationMeasures_annotation, "
				+ "rcd_available, rcd_available_percent, rcd_annotation, "
				+ "resistance, resistance_number, resistance_annotation, "
				+ "thermalAbnormality, thermalAbnormality_annotation, internalPortableUtilities, "
				+ "externalPortableUtilities, supplySystem, energyDemand, "
				+ "maxEnergyDemandExternal, maxEnergyDemandInternal, protectedCircuitsPercent, "
				+ "hardWiredLoads, additionalAnnotations) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DataSource.getConnection();
			preparedStatement = connection.prepareStatement(statement);

			Util.setValues(preparedStatement,
					diagnosis.getLastEdited(), diagnosis.getCompanyPlant().getInternalId(), diagnosis.getCompanion(), 
					diagnosis.getSurveyor(), diagnosis.getVdsApprovalNr(), diagnosis.getDate(),
					diagnosis.getExaminationDuration(),diagnosis.getBranch().getInternalId(), diagnosis.isFrequencyControlledUtilities(),diagnosis.isPrecautionsDeclared(),
					diagnosis.getPrecautionsDeclaredLocation(), diagnosis.isExaminationComplete(), diagnosis.getSubsequentExaminationDate(),
					diagnosis.getExaminationIncompleteReason(), diagnosis.getChangesSinceLastExamination(), diagnosis.getDefectsLastExaminationFixed(),
					diagnosis.getDangerCategory(), diagnosis.getDangerCategoryDescription(), diagnosis.isExaminationResultNoDefect(),
					diagnosis.isExaminationResultDefect(),diagnosis.getExaminationResultDefectDate(), diagnosis.isExaminationResultDanger(), diagnosis.isIsolationChecked(),
					diagnosis.isIsolationMesasurementProtocols(), diagnosis.isIsolationCompensationMeasures(), diagnosis.getIsolationCompensationMeasuresAnnotation(),
					diagnosis.getRcdAvailable(), diagnosis.getRcdAvailablePercent(), diagnosis.getRcdAnnotation(),
					diagnosis.isResistance(), diagnosis.getResistanceNumber(), diagnosis.getResistanceAnnotation(),
					diagnosis.isThermalAbnormality(), diagnosis.getThermalAbnormalityAnnotation(), diagnosis.isInternalPortableUtilities(),
					diagnosis.getExternalPortableUtilities(), diagnosis.getSupplySystem(), diagnosis.getEnergyDemand(),
					diagnosis.getMaxEnergyDemandExternal(), diagnosis.getMaxEnergyDemandInternal(), diagnosis.getProtectedCircuitsPercent(),
					diagnosis.getHardWiredLoads(), diagnosis.getAdditionalAnnotations());
			
			// execute insert SQL statement
			preparedStatement.executeUpdate();
			
			//Get Id of inserted Diagnosis
			ResultSet resultSet = connection.createStatement().executeQuery("SELECT last_insert_rowid() ");
			diagnosisId = resultSet.getInt("last_insert_rowid()");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return diagnosisId;
	}
}

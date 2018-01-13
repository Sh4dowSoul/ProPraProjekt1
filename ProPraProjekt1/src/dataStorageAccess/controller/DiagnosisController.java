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
				String examinationDateString = resultSet.getString("examinationDate");
				LocalDate examinationDate = null;
				if(!resultSet.wasNull()) {
					examinationDate = LocalDate.parse(examinationDateString);
				}
				result.add(new InspectionResultPreview(
						resultSet.getInt("inspectionReportId"), 
						examinationDate, 
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
				String examinationDateString = resultSet.getString("examinationDate");
				LocalDate examinationDate = null;
				if(!resultSet.wasNull()) {
					examinationDate = LocalDate.parse(examinationDateString);
				}
				result.add(new InspectionResultPreview(
						resultSet.getInt("inspectionReportId"), 
						examinationDate,
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
						"FROM InspectionReport NATURAL JOIN CompanyPlant Natural JOIN Company LEFT JOIN Branch ON  InspectionReport.branchId = Branch.branchId "+ 
						"WHERE inspectionReportId = " + id);
			) {
				while (resultSet.next()) {
					result = new InspectionReportFull();
					//Id
					result.setId(resultSet.getInt("InspectionReportId"));
					
					//Company
					int companyPlantId = resultSet.getInt("plantId");
					if(!resultSet.wasNull()) {
						result.setCompanyPlant(new CompanyPlant(
								companyPlantId,
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
					
					
					//Date
					String examinationdate = resultSet.getString("examinationDate");
					if(!resultSet.wasNull()) {
						result.setDate(LocalDate.parse(examinationdate));
					}
					
					//Last Edited
					String lastEdited = resultSet.getString("inspectionReportLastEdited");
					if(!resultSet.wasNull()) {
						result.setLastEdited(LocalDate.parse(lastEdited));
					}
					
					//Companion
					result.setCompanion(resultSet.getString("companion"));
					
					//Surveyor
					result.setSurveyor(resultSet.getString("surveyor"));
					
					//VDS Approval
					result.setVdsApprovalNr((Integer) resultSet.getObject("vdsApprovalNr"));
					
					//ExaminationDuration
					result.setExaminationDuration((Double) resultSet.getObject("examinationDuration"));
					
					//Branch					
					result.setBranch(new Branch((Integer) resultSet.getObject("branchId")));//TODO: Convert BranchID to INTEGER
					
					//FrequencyControlledUtilities
					result.setFrequencyControlledUtilities(resultSet.getObject("frequencyControlledUtilities") != null ? resultSet.getBoolean("frequencyControlledUtilities") : null);
					
					//PrecautionsDeclared
					result.setPrecautionsDeclared(resultSet.getObject("precautionsDeclared") != null ? resultSet.getBoolean("precautionsDeclared") : null);
					
					//PrecautionsDeclaredWhere
					result.setPrecautionsDeclaredLocation(resultSet.getString("precautionsDeclaredWhere"));
					
					//ExaminationCompleted
					result.setExaminationComplete(resultSet.getObject("examinationCompleted") != null ? resultSet.getBoolean("examinationCompleted") : null);
					
					//SubsequentExaminationDate
					String subsequentExaminationDate = resultSet.getString("subsequentExaminationDate");
					if(!resultSet.wasNull()) {
						result.setSubsequentExaminationDate(LocalDate.parse(subsequentExaminationDate));
					}
					
					//subsequentExaminationReason
					result.setExaminationIncompleteReason(resultSet.getString("subsequentExaminationReason"));
					
					//changesSinceLastExamination
					result.setChangesSinceLastExamination((Integer) resultSet.getObject("changesSinceLastExamination"));
					
					//defectsLastExaminationFixed
					result.setDefectsLastExaminationFixed((Integer) resultSet.getObject("defectsLastExaminationFixed"));
					
					//dangerCategoryVds
					result.setDangerCategory((Integer) resultSet.getObject("dangerCategoryVds"));
					
					//dangerCategoryVdsDescription
					resultSet.getString("dangerCategoryVdsDescription");
					
					//examinationResultNoDefect
					result.setExaminationResultNoDefect(resultSet.getObject("examinationResultNoDefect") != null ? resultSet.getBoolean("examinationResultNoDefect") : null);
					
					//examinationResultDefect
					result.setExaminationResultDefect(resultSet.getObject("examinationResultDefect") != null ? resultSet.getBoolean("examinationResultDefect") : null);
					
					//examinationResultDefectDate
					String examinationResultDefectDate = resultSet.getString("examinationResultDefectDate");
					if(!resultSet.wasNull()) {
						result.setExaminationResultDefectDate(LocalDate.parse(examinationResultDefectDate));
					}
					
					//examinationResultDanger
					result.setExaminationResultDanger(resultSet.getObject("examinationResultDanger") != null ? resultSet.getBoolean("examinationResultDanger") : null);
					
					//isolationCheckedEnough
					result.setIsolationChecked(resultSet.getObject("isolationCheckedEnough") != null ? resultSet.getBoolean("isolationCheckedEnough") : null);
					
					//isolationMeasurementProtocols
					result.setIsolationMesasurementProtocols(resultSet.getObject("isolationMeasurementProtocols") != null ? resultSet.getBoolean("isolationMeasurementProtocols") : null);
					
					//isolationCompensationMeasures
					result.setIsolationCompensationMeasures(resultSet.getObject("isolationCompensationMeasures") != null ? resultSet.getBoolean("isolationCompensationMeasures") : null);
					
					//isolationCompensationMeasuresAnnotation
					resultSet.getString("isolationCompensationMeasuresAnnotation");
					
					//rcdAvailable
					result.setRcdAvailable(resultSet.getObject("rcdAvailable") != null ? resultSet.getBoolean("rcdAvailable") : null);
					
					//rcdAvailablePercent
					result.setRcdAvailablePercent((Double) resultSet.getObject("rcdAvailablePercent"));
					
					//rcdAnnotation
					resultSet.getString("rcdAnnotation");
					
					//resistance
					result.setResistance(resultSet.getObject("resistance") != null ? resultSet.getBoolean("resistance") : null);
					
					//resistanceNumber
					result.setResistanceNumber((Integer) resultSet.getObject("resistanceNumber"));
					
					//resistanceAnnotation
					resultSet.getString("resistanceAnnotation");
					
					//thermalAbnormality
					result.setThermalAbnormality(resultSet.getObject("thermalAbnormality") != null ? resultSet.getBoolean("thermalAbnormality") : null);
					
					//thermalAbnormalityAnnotation
					result.setThermalAbnormalityAnnotation(resultSet.getString("thermalAbnormalityAnnotation"));
					
					//internalPortableUtilities
					result.setInternalPortableUtilities(resultSet.getObject("internalPortableUtilities") != null ? resultSet.getBoolean("internalPortableUtilities") : null);
					
					//externalPortableUtilities
					result.setExternalPortableUtilities((Integer) resultSet.getObject("externalPortableUtilities"));
					
					//supplySystem
					result.setSupplySystem((Integer) resultSet.getObject("supplySystem"));
					
					//energyDemand
					result.setEnergyDemand((Integer) resultSet.getObject("energyDemand"));
					
					//maxEnergyDemandExternal
					result.setMaxEnergyDemandExternal((Integer) resultSet.getObject("maxEnergyDemandExternal"));
					
					//maxEnergyDemandInternal
					result.setMaxEnergyDemandInternal((Integer) resultSet.getObject("maxEnergyDemandInternal"));
					
					//protectedCircuitsPercent
					result.setProtectedCircuitsPercent((Integer) resultSet.getObject("protectedCircuitsPercent"));
					
					//hardWiredLoads
					result.setHardWiredLoads((Integer) resultSet.getObject("hardWiredLoads"));
					
					//additionalAnnotations
					result.setAdditionalAnnotations(resultSet.getString("additionalAnnotations"));
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

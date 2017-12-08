package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import applicationLogic.Company;
import applicationLogic.CompanyPlant;
import applicationLogic.ResultComplete;
import applicationLogic.ResultPreview;
import applicationLogic.StatisticResult;
import dataStorageAccess.DataSource;

public class DiagnosisController {
	
	/**
	 * @param number of Diagnoses you want to get
	 * @return a list of the "n" last edited Diagnoses 
	 * @throws SQLException
	 */
	public static ArrayList<ResultPreview> getLastEditedDiagnosesPreview(int number) throws SQLException {
		ArrayList<ResultPreview> result = new ArrayList<ResultPreview>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT diagnosis_id, examination_date, company_id, company_name, diagnosis_lastedited "+ 
					"FROM Diagnosis join (Company Natural join CompanyPlant as CompanyWhosPlant) on Diagnosis.plant_id = companyWhosPlant.plant_id "+ 
					"ORDER BY diagnosis_lastedited desc "+
					"LIMIT " + number);
		) {
			while (resultSet.next()) {
				result.add(new ResultPreview(resultSet.getInt("diagnosis_id"), LocalDate.parse(resultSet.getString("examination_date")), resultSet.getInt("company_id"), resultSet.getString("company_name"), LocalDate.parse(resultSet.getString("diagnosis_lastedited"))));
			}
		}
		return result;
	}
	
	/**
	 * @return A List of Previews of Diagnoses (id, date, companyId, companyName)
	 * @throws SQLException
	 */
	public static ArrayList<ResultPreview> getDiagnosesPreview() throws SQLException {
		ArrayList<ResultPreview> result = new ArrayList<ResultPreview>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT diagnosis_id, examination_date, company_id, company_name "+ 
					"FROM Diagnosis join (Company Natural join CompanyPlant as CompanyWhosPlant) on Diagnosis.plant_id = companyWhosPlant.plant_id "+ 
					"ORDER BY company_name asc ");
		) {
			while (resultSet.next()) {
				result.add(new ResultPreview(resultSet.getInt("diagnosis_id"), LocalDate.parse(resultSet.getString("examination_date")), resultSet.getInt("company_id"), resultSet.getString("company_name"), LocalDate.parse(resultSet.getString("diagnosis_lastedited"))));
			}
		}
		return result;
	}
	
	/**
	 * @param id - diagnosis_id from Diagnosis Table
	 * @return the whole Diagnosis, loaded from the database
	 * @throws SQLException
	 */
	public static ResultComplete getDiagnosis(int id) throws SQLException {
		ResultComplete result = null;
		try (
				Connection connection = DataSource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(
						"SELECT * "+ 
						"FROM Diagnosis NATURAL JOIN CompanyPlant Natural JOIN Company "+ 
						"WHERE diagnosis_id = " + id);
			) {
				while (resultSet.next()) {
					result = new ResultComplete(resultSet.getInt("diagnosis_id"),
											LocalDate.parse(resultSet.getString("examination_date")),
											LocalDate.parse(resultSet.getString("diagnosis_lastEdited")),
											resultSet.getString("companion"), 
											resultSet.getString("surveyor"),
											resultSet.getInt("vds_approval_nr"), 
											resultSet.getDouble("examination_duration"), 
											resultSet.getBoolean("frequency_controlled_utilities"), 
											resultSet.getBoolean("precautions_declared"),
											resultSet.getString("precautions_declared_where"),
											resultSet.getBoolean("examination_completed"),
											resultSet.getString("subsequent_examination_date"),
											resultSet.getString("subsequent_examination_reason"),
											resultSet.getInt("changes_sincel_last_examination"),
											resultSet.getInt("defects_last_examination_fixed"),
											resultSet.getInt("danger_categorie_vds"),
											resultSet.getString("danger_categorie_vds_description"),
											resultSet.getBoolean("examination_resultNoDefect"),
											resultSet.getBoolean("examination_resultDefect"),
											LocalDate.parse(resultSet.getString("examinationResultDefectDate")),
											resultSet.getBoolean("examination_resultDanger"),
											resultSet.getInt("examination_pages"),
											resultSet.getBoolean("isolation_checkedEnough"),
											resultSet.getBoolean("isolation_measurementProtocols"),
											resultSet.getBoolean("isolation_compensationMeasures"),
											resultSet.getString("isolation_compensationMeasures_annotation"),
											resultSet.getBoolean("rcd_available"),
											resultSet.getInt("rcd_available_percent"),
											resultSet.getString("rcd_annotation"),
											resultSet.getBoolean("resistance"),
											resultSet.getInt("resistance_number"),
											resultSet.getString("resistance_annotation"),
											resultSet.getBoolean("thermalAbnormality"),
											resultSet.getString("thermalAbnormality_annotation"),
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
													resultSet.getInt("plant_id"),
													resultSet.getString("plant_street"),
													resultSet.getInt("plant_zip"),
													resultSet.getString("plant_city"),
													new Company(
															resultSet.getInt("company_id"),
															resultSet.getString("company_name"),
															resultSet.getString("hq_street"),
															resultSet.getInt("hq_zip"),
															resultSet.getString("hq_city")
															)
													)
											);
				}
			}
			return result;
	}
	
	
	public static ArrayList<StatisticResult> getDiagnosesAndDefectsOfCompany(int companyId) throws SQLException{
		ArrayList<StatisticResult> result = new ArrayList<StatisticResult>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT diagnosis_id, examination_date, company_id, company_name "+
					"FROM Diagnosis join (Company Natural join CompanyPlant as CompanyWhosPlant) on Diagnosis.plant_id = companyWhosPlant.plant_id " +
					"WHERE company_id = " + companyId);
		) {
			while (resultSet.next()) {
				result.add(new StatisticResult(resultSet.getInt("diagnosis_id"), LocalDate.parse(resultSet.getString("examination_date")),"TODO","TODO","TODO"));
			}
		}
		return result;
	}
	
	/**
	 * Inserts Diagnosis into Database
	 * @param diagnosis - Diagnosis, which should be inserted into the Database
	 * @throws SQLException
	 */
	public static void insertDiagnosis(ResultComplete diagnosis) throws SQLException {
		String statement = "INSERT INTO Diagnosis "
				+ "(diagnosis_lastEdited, plant_id, companion, "
				+ "surveyor, vds_approval_nr, examination_date, "
				+ "examination_duration, frequency_controlled_utilities, precautions_declared, "
				+ "precautions_declared_where, examination_completed, subsequent_examination_date, "
				+ "subsequent_examination_reason, changes_sincel_last_examination, defects_last_examination_fixed, "
				+ "danger_categorie_vds, danger_categorie_vds_description, examination_resultNoDefect, "
				+ "examination_resultDefect, examination_resultDanger, isolation_checkedEnough, "
				+ "isolation_measurementProtocols, isolation_compensationMeasures, isolation_compensationMeasures_annotation, "
				+ "rcd_available, rcd_available_percent, rcd_annotation, "
				+ "resistance, resistance_number, resistance_annotation, "
				+ "thermalAbnormality, thermalAbnormality_annotation, internalPortableUtilities, "
				+ "externalPortableUtilities, supplySystem, energyDemand, "
				+ "maxEnergyDemandExternal, maxEnergyDemandInternal, protectedCircuitsPercent, "
				+ "hardWiredLoads, additionalAnnotations) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DataSource.getConnection();
			preparedStatement = connection.prepareStatement(statement);

			setValues(preparedStatement,
					diagnosis.getLastEdited(), diagnosis.getCompanyPlant().getId(), diagnosis.getCompanion(), 
					diagnosis.getSurveyor(), diagnosis.getVdsApprovalNr(), diagnosis.getDate(),
					diagnosis.getExaminationDuration(), diagnosis.isFrequencyControlledUtilities(),diagnosis.isPrecautionsDeclared(),
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
	 * Helper Method to simplify inserting multiple Objects into Database
	 * @param preparedStatement
	 * @param values
	 * @throws SQLException
	 */
	public static void setValues(PreparedStatement preparedStatement, Object... values) throws SQLException {
	    for (int i = 0; i < values.length; i++) {
	        preparedStatement.setObject(i + 1, values[i]);
	    }
	}
}

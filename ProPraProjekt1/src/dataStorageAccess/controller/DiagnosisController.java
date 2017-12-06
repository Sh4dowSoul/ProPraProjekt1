package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import applicationLogic.Branch;
import applicationLogic.Diagnosis;
import applicationLogic.DiagnosisPreview;
import applicationLogic.StatisticElement;
import dataStorageAccess.DBConnection;

public class DiagnosisController {
	
	/**
	 * @param number of Diagnoses you want to get
	 * @return a list of the "n" last edited Diagnoses 
	 * @throws SQLException
	 */
	public static ArrayList<DiagnosisPreview> getLastEditedDiagnosesPreview(int number) throws SQLException {
		ArrayList<DiagnosisPreview> result = new ArrayList<DiagnosisPreview>();
		try (
			Connection connection = DBConnection.getInstance().initConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT diagnosis_id, examination_date, company_id, company_name, diagnosis_lastedited "+ 
					"FROM Diagnosis join (Company Natural join CompanyPlant as CompanyWhosPlant) on Diagnosis.plant_id = companyWhosPlant.plant_id "+ 
					"ORDER BY diagnosis_lastedited desc "+
					"LIMIT " + number);
		) {
			while (resultSet.next()) {
				result.add(new DiagnosisPreview(resultSet.getInt("diagnosis_id"), new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getString("examination_date")), resultSet.getInt("company_id"), resultSet.getString("company_name")));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @return A List of Previews of Diagnoses (id, date, companyId, companyName)
	 * @throws SQLException
	 */
	public static ArrayList<DiagnosisPreview> getDiagnosesPreview() throws SQLException {
		ArrayList<DiagnosisPreview> result = new ArrayList<DiagnosisPreview>();
		try (
			Connection connection = DBConnection.getInstance().initConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT diagnosis_id, examination_date, company_id, company_name "+ 
					"FROM Diagnosis join (Company Natural join CompanyPlant as CompanyWhosPlant) on Diagnosis.plant_id = companyWhosPlant.plant_id "+ 
					"ORDER BY company_name asc ");
		) {
			while (resultSet.next()) {
				result.add(new DiagnosisPreview(resultSet.getInt("diagnosis_id"), new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getString("examination_date")), resultSet.getInt("company_id"), resultSet.getString("company_name")));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @param id - diagnosis_id from Diagnosis Table
	 * @return the whole Diagnosis, loaded from the database
	 * @throws SQLException
	 */
	public static Diagnosis getDiagnosis(int id) throws SQLException {
		Diagnosis result = null;
		try (
				Connection connection = DBConnection.getInstance().initConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(
						"SELECT * "+ 
						"FROM Diagnosis  "+ 
						"WHERE diagnosis_id = " + id);
			) {
				while (resultSet.next()) {
					result = new Diagnosis(resultSet.getInt("diagnosis_id"), 
											resultSet.getString("diagnosis_lastEdited"),
											resultSet.getInt("plant_id"), 
											resultSet.getString("companion"), 
											resultSet.getString("surveyor"),
											resultSet.getInt("vds_approval_nr"), 
											resultSet.getString("examination_date"),
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
											resultSet.getString("additionalAnnotations"));
				}
			}
			return result;
	}
	
	/**
	 * Inserts Diagnosis into Database
	 * @param diagnosis - Diagnosis, which should be inserted into the Database
	 * @throws SQLException
	 */
	public static void insertDiagnosis(Diagnosis diagnosis) throws SQLException {
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
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DBConnection.getInstance().initConnection();
			preparedStatement = connection.prepareStatement(statement);

			setValues(preparedStatement,
					diagnosis.getLastEdited(), diagnosis.getPlantId(), diagnosis.getCompanion(), 
					diagnosis.getSurveyor(), diagnosis.getVdsApprovalNr(), diagnosis.getExaminationDate(),
					diagnosis.getExaminationDuration(), diagnosis.isFrequencyControlledUtilities(),diagnosis.isPrecautionsDeclared(),
					diagnosis.getPrecautionsDeclaredLocation(), diagnosis.isExaminationComplete(), diagnosis.getSubsequentExaminationDate(),
					diagnosis.getExaminationIncompleteReason(), diagnosis.getChangesSinceLastExamination(), diagnosis.getDefectsLastExaminationFixed(),
					diagnosis.getDangerCategory(), diagnosis.getDangerCategoryDescription(), diagnosis.isExaminationResultNoDefect(),
					diagnosis.isExaminationResultDefect(), diagnosis.isExaminationResultDanger(), diagnosis.isIsolationChecked(),
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

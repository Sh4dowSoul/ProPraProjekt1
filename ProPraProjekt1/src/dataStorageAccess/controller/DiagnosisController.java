package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import applicationLogic.Branch;
import applicationLogic.Diagnosis;
import applicationLogic.DiagnosisPreview;
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
			PreparedStatement statement = connection.prepareStatement(
					"SELECT diagnosis_id, examination_date, company_id, company_name, diagnosis_lastedited "+ 
					"FROM Diagnosis join (Company Natural join CompanyPlant as CompanyWhosPlant) on Diagnosis.plant_id = companyWhosPlant.plant_id "+ 
					"ORDER BY diagnosis_lastedited desc "+
					"LIMIT " + number);
			ResultSet resultSet = statement.executeQuery();
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
			PreparedStatement statement = connection.prepareStatement(
					"SELECT diagnosis_id, examination_date, company_id, company_name "+ 
					"FROM Diagnosis join (Company Natural join CompanyPlant as CompanyWhosPlant) on Diagnosis.plant_id = companyWhosPlant.plant_id "+ 
					"ORDER BY company_name asc ");
			ResultSet resultSet = statement.executeQuery();
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
				PreparedStatement statement = connection.prepareStatement(
						"SELECT * "+ 
						"FROM Diagnosis  "+ 
						"WHERE diagnosis_id = " + id);
				ResultSet resultSet = statement.executeQuery();
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
											resultSet.getBoolean("externalPortableUtilities"),
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
}

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
	//Get n Last Edited Diagnoses
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
}

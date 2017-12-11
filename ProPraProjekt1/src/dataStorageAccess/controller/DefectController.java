package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import applicationLogic.DefectAtomic;
import applicationLogic.DefectResult;
import dataStorageAccess.DataSource;

public class DefectController {
	/**
	 * @return A List off all Defects
	 * @throws SQLException
	 */
	public static ArrayList<DefectAtomic> getAllDefects() throws SQLException {
		ArrayList<DefectAtomic> result = new ArrayList<DefectAtomic>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Defects");
		) {
			while (resultSet.next()) {
				result.add(new DefectAtomic(resultSet.getInt("defect_id"), resultSet.getString("defect_description")));
			}
		}
		return result;
	}
	
	/**
	 * Get a List of Defects from an InspectionResult
	 * 
	 * @param diagnosisId
	 * @return a list of Defects (long/InspectionResult version)
	 * @throws SQLException
	 */
	public static ArrayList<DefectResult> getDefectsOffDiagnosis(int diagnosisId) throws SQLException {
		ArrayList<DefectResult> result = new ArrayList<DefectResult>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT * " + 
					"FROM DefectElement " + 
					"WHERE Diagnosis_id = " + diagnosisId
			);
		) {
			while (resultSet.next()) {
				result.add(new DefectResult(resultSet.getInt("defect_id"), resultSet.getInt("branch_id"), resultSet.getInt("danger"), resultSet.getString("building"), resultSet.getString("room"), resultSet.getString("machine"), resultSet.getString("defect_customDescription")));
			}
		}
		return result;
	}
}
package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import applicationLogic.DefectStatistic;
import dataStorageAccess.DataSource;

public class StatisticController {
	
	/**
	 * @param id - Id of a Company
	 * @return A List of the most Frequent Defects in Company
	 * @throws SQLException
	 */
	public static ArrayList<DefectStatistic> getMostFrequentDefectCompany(int id) throws SQLException{
		ArrayList<DefectStatistic> result = new ArrayList<DefectStatistic>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT defect_id, defect_description, count(defect_id) " + 
					"FROM DefectElement JOIN Diagnosis ON DefectElement.diagnosis_id = Diagnosis.diagnosis_id NATURAL JOIN CompanyPlant NATURAL JOIN Company NATURAL JOIN defects " + 
					"GROUP BY defect_id " + 
					"Having company_id = " + id + " " +
					"ORDER BY count(defect_id) desc " 
					);
			) {
			while (resultSet.next()) {
				result.add(new DefectStatistic(resultSet.getInt("defect_id"), resultSet.getString("defect_description"), resultSet.getInt("count(defect_id)")));
			}
		}
	return result;
	}
	
	
	/**
	 * @return A List of the most Frequent Defects across all Companies
	 * @throws SQLException
	 */
	public static ArrayList<DefectStatistic> getMostFrequentDefectAllCompanies() throws SQLException{
		ArrayList<DefectStatistic> result = new ArrayList<DefectStatistic>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT defect_id, defect_description, count(defect_id) " + 
					"FROM DefectElement NATURAL JOIN defects " + 
					"GROUP BY defect_id " + 
					"ORDER BY count(defect_id) desc " 
					);
			) {
			while (resultSet.next()) {
				result.add(new DefectStatistic(resultSet.getInt("defect_id"), resultSet.getString("defect_description"), resultSet.getInt("count(defect_id)")));
			}
		}
	return result;
	}
	
	
	/**
	 * @param id - Id of a Branch
	 * @return A List of the most Frequent Defects of a Branch
	 * @throws SQLException
	 */
	public static ArrayList<DefectStatistic> getMostFrequentDefectBranch(int id) throws SQLException{
		ArrayList<DefectStatistic> result = new ArrayList<DefectStatistic>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT defect_id, defect_description, count(defect_id) " + 
					"FROM DefectElement NATURAL JOIN defects " + 
					"WHERE branch_id = " + id + " " +
					"GROUP BY defect_id " + 
					"ORDER BY count(defect_id) desc "
				);
			) {
			while (resultSet.next()) {
				result.add(new DefectStatistic(resultSet.getInt("defect_id"), resultSet.getString("defect_description"), resultSet.getInt("count(defect_id)")));
			}
		}
		return result;
	}
	
	
	/**
	 * @param id - Id of a Branch
	 * @return A List of the most Frequent Defects of a Branch
	 * @throws SQLException
	 */
	public static ArrayList<DefectStatistic> getMostFrequentDefectAllBranches() throws SQLException{
		ArrayList<DefectStatistic> result = new ArrayList<DefectStatistic>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT defect_id, defect_description, count(defect_id) " + 
					"FROM DefectElement NATURAL JOIN defects " + 
					"GROUP BY defect_id " + 
					"ORDER BY count(defect_id) desc "
				);
			) {
			while (resultSet.next()) {
				result.add(new DefectStatistic(resultSet.getInt("defect_id"), resultSet.getString("defect_description"), resultSet.getInt("count(defect_id)")));
			}
		}
		return result;
	}
	
	/**
	 * Get the Defects of an specific InspectionResult
	 * 
	 * @param diagnosisId - The id of an InspectionResult
	 * @return a List of all Defects of an InspectionResult
	 * @throws SQLException
	 */
	public static ArrayList<DefectStatistic> getDefectsOfDiagnosis(int diagnosisId) throws SQLException{
		ArrayList<DefectStatistic> result = new ArrayList<DefectStatistic>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT defect_id, branch_id " + 
					"FROM Diagnosis NATURAL JOIN DefectElement " + 
					"WHERE diagnosis_id = " + diagnosisId);
			) {
			while (resultSet.next()) {
				result.add(new DefectStatistic(resultSet.getInt("branch_id"), resultSet.getInt("defect_id")));
			}
		}
		return result;
	}	
}
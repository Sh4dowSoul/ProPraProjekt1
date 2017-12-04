package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import applicationLogic.DiagnosisPreview;
import applicationLogic.StatisticElement;
import dataStorageAccess.DBConnection;

public class StatisticController {
	
	/**
	 * @param id - Id of a Company
	 * @return A List of the most Frequent Defects in Company
	 * @throws SQLException
	 */
	public static ArrayList<StatisticElement> getMostFrequentDefectCompany(int id) throws SQLException{
		ArrayList<StatisticElement> result = new ArrayList<StatisticElement>();
		try (
			Connection connection = DBConnection.getInstance().initConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT defect_id, defect_description, count(defect_id), branch_id " + 
					"FROM DefectElement NATURAL JOIN Diagnosis NATURAL JOIN CompanyPlant NATURAL JOIN Company NATURAL JOIN defects " + 
					"WHERE company_id = " + id + " " +
					"GROUP BY defect_id " + 
					"ORDER BY count(defect_id) desc " + 
					"Limit 10");
			) {
			while (resultSet.next()) {
				result.add(new StatisticElement(resultSet.getInt("defect_id"), resultSet.getString("defect_description"), resultSet.getInt("count(defect_id)")));
			}
		}
	return result;
	}
	
	
	/**
	 * @return A List of the most Frequent Defects across all Companies
	 * @throws SQLException
	 */
	public static ArrayList<StatisticElement> getMostFrequentDefectAllCompanies() throws SQLException{
		ArrayList<StatisticElement> result = new ArrayList<StatisticElement>();
		try (
			Connection connection = DBConnection.getInstance().initConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT defect_id, defect_description, count(defect_id), branch_id " + 
					"FROM DefectElement NATURAL JOIN defects " + 
					"GROUP BY defect_id " + 
					"ORDER BY count(defect_id) desc " + 
					"Limit 3");
			) {
			while (resultSet.next()) {
				result.add(new StatisticElement(resultSet.getInt("defect_id"), resultSet.getString("defect_description"), resultSet.getInt("count(defect_id)")));
			}
		}
	return result;
	}
	
	
	/**
	 * @param id - Id of a Branch
	 * @return A List of the most Frequent Defects of a Branch
	 * @throws SQLException
	 */
	public static ArrayList<StatisticElement> getMostFrequentDefectBranch(int id) throws SQLException{
		ArrayList<StatisticElement> result = new ArrayList<StatisticElement>();
		try (
			Connection connection = DBConnection.getInstance().initConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT defect_id, defect_description, count(defect_id), branch_id " + 
					"FROM DefectElement NATURAL JOIN defects" + 
					"WHERE branch_id = " + id + " " +
					"GROUP BY branch_id " + 
					"ORDER BY count(defect_id) desc " + 
					"Limit 3");
			) {
			while (resultSet.next()) {
				result.add(new StatisticElement(resultSet.getInt("defect_id"), resultSet.getString("defect_description"), resultSet.getInt("count(defect_id)")));
			}
		}
	return result;
	}
		
}

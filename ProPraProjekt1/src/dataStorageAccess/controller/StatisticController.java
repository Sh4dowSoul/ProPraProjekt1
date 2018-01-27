package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import applicationLogic.FlawStatistic;
import dataStorageAccess.DataSource;

/**
 * @author Niklas Schnettler
 *
 */
public class StatisticController {
	
	/**
	 * @param id - Id of a Company
	 * @return A List of the most Frequent Defects in Company
	 * @throws SQLException
	 */
	public static ArrayList<FlawStatistic> getMostFrequentDefectCompany(int id) throws SQLException{
		ArrayList<FlawStatistic> result = new ArrayList<FlawStatistic>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT externalFlawId, flawDescription, count(*) " + 
					"FROM FlawListElement JOIN InspectionReport ON FlawListElement.inspectionReportId = InspectionReport.inspectionReportId NATURAL JOIN FLAW NATURAL JOIN CompanyPlant NATURAL JOIN Company " + 
					"WHERE InspectionReportvalidated " +
					"GROUP BY externalFlawId, CompanyId " + 
					"Having companyId = " + id + " " +
					"ORDER BY count(externalFlawId) desc " 
					);
			) {
			while (resultSet.next()) {
				result.add(new FlawStatistic(resultSet.getInt("externalFlawId"), resultSet.getString("flawDescription"), resultSet.getInt("count(*)")));
			}
		}
	return result;
	}
	
	
	/**
	 * @return A List of the most Frequent Defects across all Companies
	 * @throws SQLException
	 */
	public static ArrayList<FlawStatistic> getMostFrequentDefects() throws SQLException{
		ArrayList<FlawStatistic> result = new ArrayList<FlawStatistic>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT externalFlawId, flawDescription, count(*) " + 
					"FROM FlawListElement JOIN InspectionReport ON FlawListElement.inspectionReportId = InspectionReport.inspectionReportId NATURAL JOIN Flaw " + 
					"WHERE InspectionReportvalidated " +
					"GROUP BY externalFlawId " + 
					"ORDER BY count(*) desc " 
					);
			) {
			while (resultSet.next()) {
				result.add(new FlawStatistic(resultSet.getInt("externalFlawId"), resultSet.getString("flawDescription"), resultSet.getInt("count(*)")));
			}
		}
	return result;
	}
	
	
	/**
	 * @param id - Id of a Branch
	 * @return A List of the most Frequent Defects of a Branch
	 * @throws SQLException
	 */
	public static ArrayList<FlawStatistic> getMostFrequentDefectBranch(int id) throws SQLException{
		ArrayList<FlawStatistic> result = new ArrayList<FlawStatistic>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT externalFlawId, flawDescription, count(*) " + 
					"FROM FlawListElement JOIN InspectionReport ON FlawListElement.inspectionReportId = InspectionReport.inspectionReportId NATURAL JOIN Flaw " + 
					"WHERE InspectionReportvalidated " +
					"GROUP BY externalFlawId, FlawListElement.BranchId " + 
					"HAVING FlawListElement.branchId = " + id +" " +
					"ORDER BY count(*) desc "
				);
			) {
			while (resultSet.next()) {
				result.add(new FlawStatistic(resultSet.getInt("externalFlawId"), resultSet.getString("flawDescription"), resultSet.getInt("count(*)")));
			}
		}
		return result;
	}	
}
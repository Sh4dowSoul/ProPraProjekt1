package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import applicationLogic.Flaw;
import applicationLogic.FlawStatistic;
import applicationLogic.InspectionReportStatistic;
import dataStorageAccess.DataSource;

public class FlawController {

	/**
	 * Get a List of all Flaws
	 * 
	 * @return A List off all Flaws
	 * @throws SQLException
	 */
	public static ArrayList<Flaw> getAllFlaws() throws SQLException {
		ArrayList<Flaw> result = new ArrayList<Flaw>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Flaw");
		) {
			while (resultSet.next()) {
				result.add(new Flaw(
						resultSet.getInt("externalFlawId"),
						resultSet.getInt("internalFlawId"),
						resultSet.getBoolean("isCustomFlaw"),
						resultSet.getString("flawDescription")
						));
			}
		}
		return result;
	}
	
	public static ArrayList<String> getFlawDescription(int externalFlawId) throws SQLException {
		ArrayList<String> result = new ArrayList<>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
				"SELECT * " + 
				"FROM Flaw " + 
				"WHERE externalFlawId = " + externalFlawId
				);
			) {
				while (resultSet.next()) {
					result.add(resultSet.getString("flawDescription"));
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
	public static ArrayList<InspectionReportStatistic> getFlawsOfInspectionReportsForXml(ArrayList<InspectionReportStatistic> inspectionReports) throws SQLException{
		String statement = "SELECT externalFlawId, FlawListElement.branchId  "
				+ "FROM InspectionReport JOIN (FlawListElement Natural Join Flaw) ON InspectionReport.inspectionReportId = FlawListElement.inspectionReportId  "
				+ "WHERE InspectionReport.InspectionReportId = ?";
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DataSource.getConnection();
			preparedStatement = connection.prepareStatement(statement);
			
			for(InspectionReportStatistic statistic : inspectionReports) {
				ArrayList<FlawStatistic> result = new ArrayList<>();
				preparedStatement.setInt(1, statistic.getId());
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					result.add(new FlawStatistic(resultSet.getInt("externalFlawId"), resultSet.getInt("branchId")));
				}
				statistic.setDefects(result);
			}
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
		return inspectionReports;
	}	
}

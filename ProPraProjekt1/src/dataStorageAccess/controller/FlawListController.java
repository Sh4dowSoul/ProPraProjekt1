package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import applicationLogic.Flaw;
import applicationLogic.FlawListElement;
import applicationLogic.Util;
import dataStorageAccess.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FlawListController {
	/**
	 * Get FlawList of an InspectionReport
	 * 
	 * @param reportId - Id of an InspectionReport
	 * @return the flawList of the InspectionReport
	 * @throws SQLException
	 */
	public static ObservableList<FlawListElement> getFlawList(int reportId) throws SQLException {
		ArrayList<FlawListElement> result = new ArrayList<FlawListElement>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT * " + 
					"FROM FlawListElement NATURAL JOIN Flaw " + 
					"WHERE inspectionReportId = " + reportId
			);
		) {
			while (resultSet.next()) {
				result.add(new FlawListElement(
						resultSet.getInt("elementId"), 
						new Flaw(resultSet.getInt("externalFlawId"), resultSet.getInt("internalFlawId"), resultSet.getBoolean("isCustomFlaw"), resultSet.getString("flawDescription")), 
						resultSet.getInt("branchId"), 
						resultSet.getInt("danger"), 
						resultSet.getString("building"), 
						resultSet.getString("room"), 
						resultSet.getString("machine")));
			}
		}
		return FXCollections.observableArrayList(result);
	}
	
	
	/**
	 * Insert a Defect into the Database
	 * 
	 * @param defect - A Defect
	 * @param diagnosisId - the Id of an Diagnosis
	 * @throws SQLException
	 */
	public static void insertFlawList(ObservableList<FlawListElement> flawListElements, int inspectionReportId) throws SQLException {
		String statement = "INSERT INTO FlawListElement "
				+ "(inspectionReportId, internalFlawId, branchId, danger, building, room, machine) "
				+ "VALUES(?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DataSource.getConnection();
			preparedStatement = connection.prepareStatement(statement);

			for(FlawListElement flawListElement : flawListElements) {
				Util.setValues(
						preparedStatement, 
						inspectionReportId, 
						flawListElement.getFlaw().getInternalId(), 
						flawListElement.getBranchId(), 
						flawListElement.getDanger(), 
						flawListElement.getBuilding(), 
						flawListElement.getRoom(),
						flawListElement.getMachine()
						);
				preparedStatement.executeUpdate();
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
	}
	
	public static void updateDefect(FlawListElement defect, int diagnosisId) throws SQLException {
		String statement = "UPDATE FlawListElement "
				+ "SET inspectionReportId = ?, internalFlawId = ?, branchId = ? ,danger = ?, building = ?, "
				+ "room = ?, machine = ? " 
				+ "WHERE elementId = " + defect.getElementId();

		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DataSource.getConnection();
			preparedStatement = connection.prepareStatement(statement);

			Util.setValues(preparedStatement,
					diagnosisId, defect.getFlaw().getInternalId(), defect.getBranchId(), defect.getDanger(), defect.getBuilding(), defect.getRoom(), defect.getMachine());
		
			// execute insert SQL statement
			preparedStatement.executeUpdate();
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
}

package dataStorageAccess.controller;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import applicationLogic.Branch;
import applicationLogic.Flaw;
import applicationLogic.Util;
import dataStorageAccess.BranchAccess;
import dataStorageAccess.DataSource;
import dataStorageAccess.FlawAccess;

public class DataBaseController {

	/**
	 * Creates a new Database (Tables, Default Data)
	 * 
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	public static void createNewDataBase() throws SQLException, FileNotFoundException {
		Statement statement = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Map<String, String> tableDefinitions = DataSource.getTableSql();
		try {
			connection = DataSource.getConnection();
			statement = connection.createStatement();
			//Branches
			statement.executeUpdate(tableDefinitions.get("Branch"));
			//Companies
			statement.executeUpdate(tableDefinitions.get("Company"));
			//CompanyPlants
			statement.executeUpdate(tableDefinitions.get("CompanyPlant"));
			//Flaws
			statement.executeUpdate(tableDefinitions.get("Flaw"));
			//FlawListElement
			statement.executeUpdate(tableDefinitions.get("FlawListElement"));
			//InspectionReport
			statement.executeUpdate(tableDefinitions.get("InspectionReport"));
			
			//Insert Default branches
			String stmt = "INSERT INTO Branch "
					+ "(branchId, branchName) "
					+ "VALUES(?,?)";
			preparedStatement = connection.prepareStatement(stmt);
			for(Branch branch : BranchAccess.importBranchesFromXml()) {
				Util.setValues(
						preparedStatement, 
						branch.getInternalId(),
						branch.getDescription()
						);
				preparedStatement.executeUpdate();
			}
			
			//Insert Default Flaws
			stmt = "INSERT INTO Flaw "
					+ "(internalFlawId, externalFlawId, isCustomFlaw, flawDescription) "
					+ "VALUES(?,?,?,?)";
			preparedStatement = connection.prepareStatement(stmt);
			for (Flaw flaw : FlawAccess.importFlawsFromXml()) {
				Util.setValues(preparedStatement,
						flaw.getInternalId(),
						flaw.getExternalId(),
						flaw.isCustomFlaw(),
						flaw.getDescription()
						);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	/**
	 * Validate TableDeclarations of Connected DataBase
	 * 
	 * @return A Map of 
	 * @throws SQLException
	 */
	public static boolean getTableDeclarations() throws SQLException {
		Map<String, String> tableSql = DataSource.getTableSql();
		//Insert Default branches
		String stmt = "SELECT sql "
				+ "FROM sqlite_master "
				+ "WHERE type = 'table' AND name = ?";
		Statement statement = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DataSource.getConnection();
			preparedStatement = connection.prepareStatement(stmt);
			for (String tableName : tableSql.keySet()) {
				Util.setValues(preparedStatement, tableName);
				ResultSet resultSet = preparedStatement.executeQuery();
				if (resultSet.isBeforeFirst() ) {    
					String table_schema_there = resultSet.getString(1).replaceAll("\\s+","").replaceAll("[\\[\\]'`\"]", "");
					if (!(table_schema_there).equals(tableSql.get(tableName).replaceAll("\\s+","").replaceAll("[\\[\\]'`\"]", ""))) {
						return false;
					}
				} else {
					return false;
				}
				
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return true;
	}
}

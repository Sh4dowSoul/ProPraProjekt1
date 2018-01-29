package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import applicationLogic.Company;
import applicationLogic.CompanyPlant;
import applicationLogic.Util;
import dataStorageAccess.DataSource;

/**
 * @author Niklas Schnettler
 *
 */
public class CompanyController {

	/**
	 * Get all Companies
	 * 
	 * @return A List of all Companies
	 * @throws SQLException
	 */
	public static ArrayList<Company> getCompanies() throws SQLException {
		ArrayList<Company> result = new ArrayList<Company>();
		try (
				Connection connection = DataSource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(
					"SELECT * "+ 
					"FROM Company "+ 
					"ORDER BY companyName");
		) {
			while (resultSet.next()) {
				result.add(new Company(
						resultSet.getInt("companyId"),
						resultSet.getString("companyName"),
						resultSet.getString("companyStreet"),
						resultSet.getInt("companyZip"),
						resultSet.getString("companyCity")
						));
			}
		}
		return result;
	}
	
	
	/**
	 * Get all Companies with Flaws
	 * 
	 * @return A List of all Companies which had a Defect
	 * @throws SQLException
	 */
	public static ArrayList<Company> getCompaniesWithDefect() throws SQLException {
		ArrayList<Company> result = new ArrayList<Company>();
		try (
			Connection connection = DataSource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(
					"SELECT DISTINCT CompanyId, CompanyName, CompanyStreet, CompanyZip, CompanyCity "+ 
					"FROM FlawListElement LEFT JOIN InspectionReport on FlawListElement.inspectionReportId = InspectionReport.inspectionReportId NATURAL JOIN CompanyPlant NATURAL JOIN Company "+
					"WHERE InspectionReportvalidated = 1 " +
					"Order BY CompanyName ");
		) {
			while (resultSet.next()) {
				result.add(new Company(
						resultSet.getInt("CompanyId"),
						resultSet.getString("CompanyName"),
						resultSet.getString("CompanyStreet"),
						resultSet.getInt("CompanyZip"),
						resultSet.getString("CompanyCity")
						));
			}
		}
		return result;
	}
	
	
	/**
	 * Get Plants of a Company
	 * 
	 * @param company - A Company
	 * @return A List of Plants of a specific Company
	 * @throws SQLException
	 */
	
	public static ArrayList<CompanyPlant> getPlantsOfcompany(Company company) throws SQLException{
		ArrayList<CompanyPlant> result = new ArrayList<CompanyPlant>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT * "+ 
					"FROM CompanyPlant "+ 
					"WHERE companyId = " + company.getInternalId()+ " "+
					"ORDER BY plantStreet");
		) {
			while (resultSet.next()) {
				result.add(new CompanyPlant(
						resultSet.getInt("plantId"),
						resultSet.getString("plantStreet"),
						resultSet.getInt("plantZip"),
						resultSet.getString("plantCity"),
						company
						));
			}
		}
		return result;
	}
	
	/**
	 * Get all CompanyPlants
	 * 
	 * @return A List of Plants of all Companies
	 * @throws SQLException
	 */
	
	public static ArrayList<CompanyPlant> getCompanyPlants() throws SQLException{
		ArrayList<CompanyPlant> result = new ArrayList<CompanyPlant>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT * "+ 
					"FROM CompanyPlant "+ 
					"ORDER BY plantStreet");
		) {
			while (resultSet.next()) {
				result.add(new CompanyPlant(
						resultSet.getInt("plantId"),
						resultSet.getString("plantStreet"),
						resultSet.getInt("plantZip"),
						resultSet.getString("plantCity"),
						new Company(resultSet.getInt("companyId"), null)
						));
			}
		}
		return result;
	}
	
	/**
	 * Insert new Company into DataBase
	 * 
	 * @param company - A Company
	 * @return The Id of the new Company (in Database)
	 * @throws SQLException
	 */
	public static int insertCompany(Company company) throws SQLException {
		String statement = "INSERT INTO Company "
				+ "(companyName, companyStreet, companyZip, companyCity) "
				+ "VALUES(?,?,?,?)";
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		int companyId;
		try {
			connection = DataSource.getConnection();
			preparedStatement = connection.prepareStatement(statement);

			Util.setValues(preparedStatement, company.getDescription(), company.getStreet(), company.getZipCode(), company.getCity());
			
			// execute insert SQL statement
			preparedStatement.executeUpdate();
			
			//Get Id of inserted Company
			ResultSet resultSet = connection.createStatement().executeQuery("SELECT last_insert_rowid() ");
			companyId = resultSet.getInt("last_insert_rowid()");
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return companyId;
	}
	
	/**
	 * Insert a Company Plant
	 * 
	 * @param companyPlant - A new Company Plant
	 * @return The Id of the new CompanyPlant
	 * @throws SQLException
	 */
	public static int insertCompanyPlant(CompanyPlant companyPlant) throws SQLException {
		String statement = "INSERT INTO CompanyPlant "
				+ "(companyId, plantStreet, plantZip, plantCity) "
				+ "VALUES(?,?,?,?)";
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		int companyPlantId;
		try {
			connection = DataSource.getConnection();
			preparedStatement = connection.prepareStatement(statement);

			Util.setValues(preparedStatement, companyPlant.getCompany().getInternalId(), companyPlant.getStreet(), companyPlant.getZipCode(), companyPlant.getCity());
			
			// execute insert SQL statement
			preparedStatement.executeUpdate();
			
			//Get Id of inserted Plant
			ResultSet resultSet = connection.createStatement().executeQuery("SELECT last_insert_rowid() ");
			companyPlantId = resultSet.getInt("last_insert_rowid()");
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return companyPlantId;
	}
}
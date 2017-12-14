package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import applicationLogic.Company;
import applicationLogic.CompanyPlant;
import applicationLogic.DefectResult;
import applicationLogic.Util;
import dataStorageAccess.DataSource;

public class CompanyController {

	/**
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
					"ORDER BY company_name desc ");
		) {
			while (resultSet.next()) {
				result.add(new Company(resultSet.getInt("company_id"), resultSet.getString("company_name"), resultSet.getString("hq_street"), resultSet.getInt("hq_zip"), resultSet.getString("hq_city")));
			}
		}
		return result;
	}
	
	
	/**
	 * @return A List of all Companies which had a Defect
	 * @throws SQLException
	 */
	public static ArrayList<Company> getCompaniesWithDefect() throws SQLException {
		ArrayList<Company> result = new ArrayList<Company>();
		try (
			Connection connection = DataSource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(
					"SELECT * "+ 
					"FROM Company "+
					"WHERE company_id IN ( " +		
					"SELECT company_id " +
					"FROM DefectElement NATURAL JOIN diagnosis NATURAL JOIN CompanyPlant NATURAL JOIN company) " +
					"ORDER BY company_name asc ");
		) {
			while (resultSet.next()) {
				result.add(new Company(resultSet.getInt("company_id"), resultSet.getString("company_name"), resultSet.getString("hq_street"), resultSet.getInt("hq_zip"), resultSet.getString("hq_city")));
			}
		}
		return result;
	}
	
	
	/**
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
					"WHERE company_id = " + company.getId()+ " "+
					"ORDER BY plant_street desc ");
		) {
			while (resultSet.next()) {
				result.add(new CompanyPlant(resultSet.getInt("plant_id"), resultSet.getString("plant_street"), resultSet.getInt("plant_zip"), resultSet.getString("plant_city"), company ));
			}
		}
		return result;
	}
	
	public static void insertCompany(Company company) throws SQLException {
		String statement = "INSERT INTO Company "
				+ "(company_name, hq_street, hq_zip, hq_city) "
				+ "VALUES(?,?,?,?)";
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DataSource.getConnection();
			preparedStatement = connection.prepareStatement(statement);

			Util.setValues(preparedStatement, company.getName(), company.getHqStreet(), company.getHqZip(), company.getHqCity());
			
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
	
	public static void insertCompanyPlant(CompanyPlant companyPlant) throws SQLException {
		String statement = "INSERT INTO CompanyPlant "
				+ "(plant_id, company_id, plant_street, plant_zip, plant_city) "
				+ "VALUES(?,?,?,?,?)";
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DataSource.getConnection();
			preparedStatement = connection.prepareStatement(statement);

			Util.setValues(preparedStatement,
					companyPlant.getId(), companyPlant.getCompany().getId(), companyPlant.getPlantStreet(), companyPlant.getPlantZip(), companyPlant.getPlantCity());
			
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

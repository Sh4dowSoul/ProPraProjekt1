package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import applicationLogic.Company;
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
				result.add(new Company(resultSet.getInt("company_id"), resultSet.getString("company_name"), resultSet.getString("hq_street"), resultSet.getString("hq_zip")));
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
				result.add(new Company(resultSet.getInt("company_id"), resultSet.getString("company_name"), resultSet.getString("hq_street"), resultSet.getString("hq_zip")));
			}
		}
		return result;
	}
	
	//TODO Fix this
	/**
	 * @param company_id - The Id of the Company
	 * @return A List of Plants of a specific Company
	 * @throws SQLException
	 */
	/*
	public static ArrayList<CompanyPlant> getPlantsOfcompany(int company_id) throws SQLException{
		ArrayList<CompanyPlant> result = new ArrayList<CompanyPlant>();
		try (
			Connection connection = DBConnection.getInstance().initConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT * "+ 
					"FROM CompanyPlant "+ 
					"WHERE company_id = " + company_id+ " "+
					"ORDER BY plant_street desc ");
		) {
			while (resultSet.next()) {
				result.add(new CompanyPlant(resultSet.getInt("plant_id"), resultSet.getInt("company_id"), resultSet.getString("plant_street"), resultSet.getString("plant_zip"), ));
			}
		}
		return result;
	} */
}

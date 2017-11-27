package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import applicationLogic.Company;
import applicationLogic.CompanyPlant;
import dataStorageAccess.DBConnection;

public class CompanyController {

	public static ArrayList<Company> getCompanies() throws SQLException {
		ArrayList<Company> result = new ArrayList<Company>();
		try (
			Connection connection = DBConnection.getInstance().initConnection();
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * "+ 
					"FROM Company "+ 
					"ORDER BY company_name desc ");
			ResultSet resultSet = statement.executeQuery();
		) {
			while (resultSet.next()) {
				result.add(new Company(resultSet.getInt("company_id"), resultSet.getString("company_name"), resultSet.getString("hq_street"), resultSet.getString("hq_zip")));
			}
		}
		return result;
	}
	
	public static ArrayList<CompanyPlant> getPlantsOfcompany(int company_id) throws SQLException{
		ArrayList<CompanyPlant> result = new ArrayList<CompanyPlant>();
		try (
			Connection connection = DBConnection.getInstance().initConnection();
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * "+ 
					"FROM CompanyPlant "+ 
					"WHERE company_id = " + company_id+ " "+
					"ORDER BY plant_street desc ");
			ResultSet resultSet = statement.executeQuery();
		) {
			while (resultSet.next()) {
				result.add(new CompanyPlant(resultSet.getInt("plant_id"), resultSet.getInt("company_id"), resultSet.getString("plant_street"), resultSet.getString("plant_zip")));
			}
		}
		return result;
	}
}

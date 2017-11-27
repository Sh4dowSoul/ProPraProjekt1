package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import applicationLogic.Company;
import applicationLogic.DiagnosisPreview;
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
}

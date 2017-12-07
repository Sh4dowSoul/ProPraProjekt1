package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import applicationLogic.Branch;
import applicationLogic.DefectAtomic;
import dataStorageAccess.DBConnection;
import dataStorageAccess.DataSource;

public class DefectController {
	/**
	 * @return A List off all Defects
	 * @throws SQLException
	 */
	public static ArrayList<DefectAtomic> getAllDefects() throws SQLException {
		ArrayList<DefectAtomic> result = new ArrayList<DefectAtomic>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Defects");
		) {
			while (resultSet.next()) {
				result.add(new DefectAtomic(resultSet.getInt("defect_id"), resultSet.getString("defect_description")));
			}
		}
		return result;
	}

}

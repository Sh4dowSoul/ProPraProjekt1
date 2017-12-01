package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import applicationLogic.Branch;
import applicationLogic.Defect;
import dataStorageAccess.DBConnection;

public class DefectController {
	/**
	 * @return A List off all Defects
	 * @throws SQLException
	 */
	public static ArrayList<Defect> getAllDefects() throws SQLException {
		ArrayList<Defect> result = new ArrayList<Defect>();
		try (
			Connection connection = DBConnection.getInstance().initConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM Defects");
			ResultSet resultSet = statement.executeQuery();
		) {
			while (resultSet.next()) {
				result.add(new Defect(resultSet.getInt("defect_id"), resultSet.getString("defect_description")));
			}
		}
		return result;
	}

}
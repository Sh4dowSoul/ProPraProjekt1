package dataStoragAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBAccess {

	protected static ArrayList<String> getDefects(String querry) throws SQLException {
		String statementString = null;
		try (
			Connection connection = DBConnection.getInstance().initConnection();
			PreparedStatement statement = connection.prepareStatement(querry);
			ResultSet resultSet = statement.executeQuery();
		) {
			while (resultSet.next()) {
				int defectId = resultSet.getInt("defect_id");
				String defectDescription = resultSet.getString("defect_description");
				System.out.println(defectDescription);
				System.out.println(defectId);
			}
		}
		return null;
	}
}
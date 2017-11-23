package dataStorageAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import applicationLogic.Branch;

public class BranchController {
	//Get Branches starting with name --> Used for Autocomplete
	public static ArrayList<Branch> filterBranch(String name) throws SQLException {
		ArrayList<Branch> result = new ArrayList<Branch>();
		try (
			Connection connection = DBConnection.getInstance().initConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM Branches WHERE branch_name LIKE '" + name + "%'");
			ResultSet resultSet = statement.executeQuery();
		) {
			while (resultSet.next()) {
				result.add(new Branch(resultSet.getInt("branch_id"), resultSet.getString("branch_name")));
			}
		}
		return result;
	}

}

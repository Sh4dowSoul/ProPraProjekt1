package dataStoragAccess;

import java.sql.SQLException;

public class TestClass {
	public static void main(String[] args) {
		try {
			DBAccess.getDefects("SELECT * FROM Defects");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

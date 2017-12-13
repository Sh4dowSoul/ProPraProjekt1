package applicationLogic;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Util {
	public static String getCheckedNull(String string) {
		if (string == null) {
			return "";
		}
		return string;
	}
	
	/**
	 * Helper Method to simplify inserting multiple Objects into Database
	 * @param preparedStatement
	 * @param values
	 * @throws SQLException
	 */
	public static void setValues(PreparedStatement preparedStatement, Object... values) throws SQLException {
	    for (int i = 0; i < values.length; i++) {
	        preparedStatement.setObject(i + 1, values[i]);
	    }
	}
}

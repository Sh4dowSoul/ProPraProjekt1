package applicationLogic;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.scene.control.TextField;

/**
 * Helper methods
 * 
 * @author Niklas Schnettler
 *
 */
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
	
	public static void setNullableIntToTextField(TextField tf, Integer value) {
		if(value != null) {
			tf.setText(Integer.toString(value));
		}
	}
	
	public static void setNullableDoubleToTextField(TextField tf, Double value) {
		if(value != null) {
			tf.setText(Double.toString(value));
		}
	}
	
	public static Boolean getNullableBoolean(Object obj, Boolean bln) {
		return obj != null ? bln : null;
	}
}

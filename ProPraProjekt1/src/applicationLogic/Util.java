package applicationLogic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;

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
	
	public static boolean validateInt(TextField tf) {
		if (tf.getText().isEmpty()) {
			return false;
		}
		try {
			Integer.valueOf(tf.getText());
			tf.getStyleClass().removeAll(Collections.singleton("error")); 
		} catch (NumberFormatException e) {
			tf.getStyleClass().add("error");
			return false;
		}
		return true;
	}
	
	public static boolean validateDouble(TextField tf) {
		if (tf.getText().isEmpty()) {
			return false;
		}
		try {
			Double.valueOf(tf.getText());
			tf.getStyleClass().removeAll(Collections.singleton("error")); 
		} catch (NumberFormatException e) {
			tf.getStyleClass().add("error");
			return false;
		}
		return true;
	}
}

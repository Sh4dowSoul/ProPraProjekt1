package dataStorageAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBConnection {
	private static DBConnection dbcontroller = null;
    private static Connection connection;
    private static final String DB_PATH = "jdbc:sqlite:BefundscheineVerwaltung.db";
    
    /* Singleton */
	private DBConnection(){ 
	} 
	public static DBConnection getInstance(){ 
		if (dbcontroller == null) {
			dbcontroller = new DBConnection();
		}
		return dbcontroller;
    }
	
	
	public Connection initConnection() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			//connection still open
			System.out.println("Connection already opened");
			return connection;
		} else {
			//new Connection
			System.out.println("Connecting to Database...");
			connection = DriverManager.getConnection(DB_PATH);
			if (!connection.isClosed()) {
				System.out.println("Connection established");
			}
		}
		return connection;
	}
}

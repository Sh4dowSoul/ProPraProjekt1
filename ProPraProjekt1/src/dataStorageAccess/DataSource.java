package dataStorageAccess;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JFileChooser;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * The Database connection Class
 * 
 * @author Niklas Schnettler
 *
 */
public class DataSource {

	private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;
    private static String databasePath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() +"\\MangelManager\\BefundscheineVerwaltung.db";
    public static boolean good;
	
	public static void configDataSource() {
		config.setJdbcUrl("jdbc:sqlite:" + databasePath);
		config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
	}
    
    private DataSource() {}
    
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public static boolean isGood() {
		return good;
	}

	public static void setGood(boolean good) {
		DataSource.good = good;
	}

	public static String getDatabasePath() {
		return databasePath;
	}
}

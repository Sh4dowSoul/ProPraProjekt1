package dataStorageAccess;

import java.sql.Connection;
import java.sql.SQLException;

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
    
    static {
        config.setJdbcUrl("jdbc:sqlite:BefundscheineVerwaltung.db");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }
    
    private DataSource() {}
    
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

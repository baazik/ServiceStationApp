package mb.company.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager {

    private static final ResourceBundle properties = ResourceBundle.getBundle("application");
    private static final String DB_URL = properties.getString("db.url");
    private static final String DB_CLASS = properties.getString("db.class");

    private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName(DB_CLASS);
                connection = DriverManager.getConnection(DB_URL);
            } catch (ClassNotFoundException e) {
                log.error("JDBC driver not found!");
                e.printStackTrace();
                throw new SQLException("JDBC driver not found!");
            }
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}

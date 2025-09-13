package tech.zeta.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

    // Private constructor prevents instantiation from other classes
    private DBUtil() {}

    // Holder class for lazy-loaded singleton instance
    private static Connection connection;

    // Public method to provide access to the connection
    public static Connection getConnection() {
        if( connection == null ){
            connection = createConnection();
        }
        return connection;
    }

    // Initialize the connection
    private static Connection createConnection() {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(inputStream);
        } catch (IOException ioException) {
            System.err.println("Error loading properties file: " + ioException.getMessage());
            return null;
        }

        String dbClassName = properties.getProperty("db_class_name");
        String dbDatabaseUrl = properties.getProperty("db_database_url");
        String dbUsername = properties.getProperty("db_username");
        String dbPassword = properties.getProperty("db_password");
        String dbDataBaseName = properties.getProperty("db_dataBaseName");

        try {
            Class.forName(dbClassName);
            return DriverManager.getConnection(dbDatabaseUrl + "/" + dbDataBaseName, dbUsername, dbPassword);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

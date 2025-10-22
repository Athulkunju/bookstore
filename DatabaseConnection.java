package com.bookstore.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Database connection utility class
 * Handles database connections and configuration
 */
public class DatabaseConnection {
    private static final String CONFIG_FILE = "database.properties";
    private static DatabaseConnection instance;
    private Connection connection;
    
    // Default database configuration
    private String url = "jdbc:mysql://localhost:3306/bookstore_db";
    private String username = "root";
    private String password = "password";
    private String driver = "com.mysql.cj.jdbc.Driver";
    
    private DatabaseConnection() {
        loadConfiguration();
        initializeConnection();
    }
    
    /**
     * Get singleton instance of DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Load database configuration from properties file
     */
    private void loadConfiguration() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
            url = props.getProperty("db.url", url);
            username = props.getProperty("db.username", username);
            password = props.getProperty("db.password", password);
            driver = props.getProperty("db.driver", driver);
        } catch (IOException e) {
            System.out.println("Could not load database configuration. Using defaults.");
            System.out.println("Create a database.properties file with the following properties:");
            System.out.println("db.url=jdbc:mysql://localhost:3306/bookstore_db");
            System.out.println("db.username=your_username");
            System.out.println("db.password=your_password");
            System.out.println("db.driver=com.mysql.cj.jdbc.Driver");
        }
    }
    
    /**
     * Initialize database connection
     */
    private void initializeConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            System.err.println("Please add MySQL JDBC driver to your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to database!");
            System.err.println("Please check your database configuration and ensure MySQL is running.");
            e.printStackTrace();
        }
    }
    
    /**
     * Get database connection
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                initializeConnection();
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
            initializeConnection();
        }
        return connection;
    }
    
    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    /**
     * Test database connection
     */
    public boolean testConnection() {
        try {
            Connection testConn = getConnection();
            return testConn != null && !testConn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get database URL
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * Get database username
     */
    public String getUsername() {
        return username;
    }
}

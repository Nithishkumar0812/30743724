package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database URL, username, and password. Update these with your actual database credentials.
    private static final String URL = "jdbc:mysql://localhost:3306/retail_order_management"; // Ensure the timezone is set
    private static final String USER = "root"; // Update database username
    private static final String PASSWORD = "123456"; // Update database password

    // Method to get a connection to the database
    public static Connection getConnection() throws SQLException {
        // Load the MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
        // Return a new connection to the database
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

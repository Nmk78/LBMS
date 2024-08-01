package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_connection {
    private static final String URL = "jdbc:mysql://localhost:3306/LBMS";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Load the JDBC driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL JDBC Driver class
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Get a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

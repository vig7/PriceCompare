

import java.sql.*;
import java.util.Date;

public class DBOperations {
    static Connection crunchifyConn = null;
    static PreparedStatement crunchifyPrepareStat = null;

    protected static Connection makeJDBCConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Congrats - Seems your MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            System.out.println("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
            e.printStackTrace();
            return null;
        }

        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.
            crunchifyConn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/HHs0I2HNbt?useSSL=false", "HHs0I2HNbt", "Z4x5imlcY7");
            if (crunchifyConn != null) {
                System.out.println("Connection Successful! Enjoy. Now it's time to push data");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.out.println("MySQL Connection Failed!");
            e.printStackTrace();
            return null;
        }
        return crunchifyConn;
    }



}
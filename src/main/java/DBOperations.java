

import java.sql.*;
import java.util.Date;

/**
 * @author Crunchify.com
 * Simple Hello World MySQL Tutorial on how to make JDBC connection, Add and Retrieve Data by App Shah
 *
 */

public class DBOperations {


    static Connection crunchifyConn = null;
    static PreparedStatement crunchifyPrepareStat = null;


    protected static Connection makeJDBCConnection() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            log("Congrats - Seems your MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            log("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
            e.printStackTrace();
            return null;
        }

        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.

            crunchifyConn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/HHs0I2HNbt?useSSL=false", "HHs0I2HNbt", "Z4x5imlcY7");
            if (crunchifyConn != null) {
                log("Connection Successful! Enjoy. Now it's time to push data");
            } else {
                log("Failed to make connection!");
            }
        } catch (SQLException e) {
            log("MySQL Connection Failed!");
            e.printStackTrace();
            return null;
        }
        return crunchifyConn;

    }

    private static void addDataToDB(String Price, String Link, String Name, Date Time ) {

        try {

            String insertQueryStatement ="update pricespec set flipkartPrice="+Price+",FlipkartLink="+Link+",FlipkartTimeStamp="+Time+"where name="+Name;
            crunchifyPrepareStat = crunchifyConn.prepareStatement(insertQueryStatement);

            // execute insert SQL statement
            crunchifyPrepareStat.executeUpdate();
            log("ds" + " added successfully");
        } catch (

                SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getDataFromDB() {

        try {
            // MySQL Select Query Tutorial
            String getQueryStatement = "SELECT * FROM pricespec";

            crunchifyPrepareStat = crunchifyConn.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = crunchifyPrepareStat.executeQuery();

            // Let's iterate through the java ResultSet
            while (rs.next()) {
                String name = rs.getString("name");

                // Simply Print the results
                System.out.format("%s \n", name);
            }

        } catch (

                SQLException e) {
            e.printStackTrace();
        }

    }

    // Simple log utility
    private static void log(String string) {
        System.out.println(string);

    }
}
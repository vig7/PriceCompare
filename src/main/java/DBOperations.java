

import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author Crunchify.com
 * Simple Hello World MySQL Tutorial on how to make JDBC connection, Add and Retrieve Data by App Shah
 *
 */

public class DBOperations {


    static Connection crunchifyConn = null;
    static PreparedStatement crunchifyPrepareStat = null;

    public static void main(String[] argv) {

        try {
            log("-------- Simple Crunchify Tutorial on how to make JDBC connection to MySQL DB locally on macOS ------------");
            makeJDBCConnection();

//            log("\n---------- Adding company 'Crunchify LLC' to DB ----------");
//            addDataToDB("Google Inc.", "Mountain View, CA, US", 50000, "https://google.com");
//            addDataToDB("Apple Inc.", "Cupertino, CA, US", 30000, "http://apple.com");

            log("\n---------- Let's get Data from DB ----------");

            crunchifyPrepareStat.close();
            crunchifyConn.close(); // connection close

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private static void makeJDBCConnection() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            log("Congrats - Seems your MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            log("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
            e.printStackTrace();
            return;
        }

        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.
            crunchifyConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pricemobiile", "root", "");
            if (crunchifyConn != null) {
                log("Connection Successful! Enjoy. Now it's time to push data");
            } else {
                log("Failed to make connection!");
            }
        } catch (SQLException e) {
            log("MySQL Connection Failed!");
            e.printStackTrace();
            return;
        }

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
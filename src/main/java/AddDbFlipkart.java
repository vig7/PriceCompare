import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


public class AddDbFlipkart {

     Connection crunchifyConn = null;
     PreparedStatement crunchifyPrepareStat = null;

    void AaddDbFlipkart(){

        try {
            makeJDBCConnection();

//

            crunchifyPrepareStat.close();
            crunchifyConn.close(); // connection close

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

      void makeJDBCConnection() {

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

    void addDataToDB(String Name,String Price, String Link,  Date Time ) {

        try {
            if(Price!=null)
            Price=Price.substring(1);
            String insertQueryStatement ="update pricespec set flipkartPrice='"+Price+"',FlipkartLink='"+Link+"',FlipkartTimeStamp='"+Time+"' where Name='"+Name+"' ";
            crunchifyPrepareStat = crunchifyConn.prepareStatement(insertQueryStatement);
            System.out.println(Name);
            Thread.sleep(1000);

            // execute insert SQL statement
            crunchifyPrepareStat.executeUpdate();
            log("ds" + " added successfully");
            crunchifyPrepareStat.close();
            crunchifyConn.close(); // connection close

        } catch (

                Exception e) {
            e.printStackTrace();
        }
    }

     void getDataFromDB() {

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

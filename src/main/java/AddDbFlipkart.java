import javafx.scene.paint.Stop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    void addDataToDB(String Name,String Price, String Link,  Date Time ,Boolean stock) {

        try {

            //String insertQueryStatement ="update finaltab set flipkartPrice='"+Price+"',FlipkartLink='"+Link+"',FlipkartTimeStamp='"+Time+"' where Name='"+Name+"' ";
            String insertQueryStatement ="update finaltab set flipkartStock="+stock+" where FlipkartLink='"+Link+"'";
            System.out.println(insertQueryStatement);
            crunchifyPrepareStat = crunchifyConn.prepareStatement(insertQueryStatement);

            Thread.sleep(1000);

            // execute insert SQL statement
            crunchifyPrepareStat.executeUpdate();
            log("ds" + " added successfully");
             // connection close

        } catch (

                Exception e) {
            e.printStackTrace();
        }
    }
     ArrayList<String> getDataFromDB() {
         ArrayList<String> links=new ArrayList<String>();
        try {
            // MySQL Select Query Tutorial
            String getQueryStatement = "SELECT * from finaltab";

            crunchifyPrepareStat = crunchifyConn.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = crunchifyPrepareStat.executeQuery();
                        // Let's iterate through the java ResultSet
            while (rs.next()) {
                String link = rs.getString("flipkartlink");
                String stock = rs.getString("flipkartStock");

                // Simply Print the results
                links.add(link);
            }

        } catch (

                SQLException e) {
            e.printStackTrace();
        }
        return links;

    }
    ArrayList<String> getStock(){
        ArrayList<String> stock=new ArrayList<String>();
        try {
            // MySQL Select Query Tutorial
            String getQueryStatement = "SELECT * from finaltab";

            crunchifyPrepareStat = crunchifyConn.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = crunchifyPrepareStat.executeQuery();
            // Let's iterate through the java ResultSet
            while (rs.next()) {
                String s = rs.getString("flipkartStock");

                // Simply Print the results
                stock.add(s);
            }

        } catch (

                SQLException e) {
            e.printStackTrace();
        }
        return stock;
    }

    // Simple log utility
    private static void log(String string) {
        System.out.println(string);

    }
    void close(){
        try{
            crunchifyPrepareStat.close();
            crunchifyConn.close();
        }
        catch (Exception e){

        }
    }
}

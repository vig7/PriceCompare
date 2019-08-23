import java.sql.*;
import java.util.Date;

public class AddDbPaytm {

    Connection crunchifyConn = null;
    PreparedStatement crunchifyPrepareStat = null;

    void AaddDbFlipkart(){

        try {
            log("-------- Simple Crunchify Tutorial on how to make JDBC connection to MySQL DB locally on macOS ------------");
            makeJDBCConnection();

//            log("\n---------- Adding company 'Crunchify LLC' to DB ----------");
//            addDataToDB("Google Inc.", "Mountain View, CA, US", 50000, "https://google.com");
//            addDataToDB("Apple Inc.", "Cupertino, CA, US", 30000, "http://apple.com");


            crunchifyPrepareStat.close();
            crunchifyConn.close(); // connection close

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    String getTimestamp(String name) {
        String data="";

        String getQueryStatement = "SELECT * from phonedatabase where name='"+name+"'";
        System.out.println(getQueryStatement);
        try{
            crunchifyPrepareStat = crunchifyConn.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = crunchifyPrepareStat.executeQuery();
            if(rs.next()){
                data = rs.getString("TimeStamp");

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return data;

    }
    void addStockandPrice(String Name,String Price,  Timestamp Time) {

        try {
            String insertQueryStatement ="update phonedatabase set paytmPrice='"+Price+"',TimeStamp='"+Time+"' where Name='"+Name+"' ";
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

    String getLink(String name) {
        String data="";

        String getQueryStatement = "SELECT * from phonedatabase where name='"+name+"'";
        System.out.println(getQueryStatement+name);
        try{
            crunchifyPrepareStat = crunchifyConn.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = crunchifyPrepareStat.executeQuery();
            if(rs.next()) {
                data = rs.getString("paytmLink");
            }
            System.out.println(data);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return data;

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
            System.out.println( Name+Price);
            String insertQueryStatement ="update phonedatabase set PaytmPrice='"+Price+"',PaytmLink='"+Link+"',TimeStamp='"+Time+"' where Name='"+Name+"' ";
            crunchifyPrepareStat = crunchifyConn.prepareStatement(insertQueryStatement);

            Thread.sleep(1000);

            // execute insert SQL statement
            crunchifyPrepareStat.executeUpdate();
            log("ds" + " added successfully");
            crunchifyPrepareStat.close();
            crunchifyConn.close(); // connection close

        } catch (Exception e) {
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
    void close(){
        try{
            crunchifyPrepareStat.close();
            crunchifyConn.close(); // connection close

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}

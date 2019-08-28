import java.sql.*;
import java.util.Date;

public class AddDbPaytm {

    DBOperations db=new DBOperations();

    void AaddDbFlipkart(){

        try {
            log("-------- Simple Crunchify Tutorial on how to make JDBC connection to MySQL DB locally on macOS ------------");
            db.makeJDBCConnection();

//            log("\n---------- Adding company 'Crunchify LLC' to DB ----------");
//            addDataToDB("Google Inc.", "Mountain View, CA, US", 50000, "https://google.com");
//            addDataToDB("Apple Inc.", "Cupertino, CA, US", 30000, "http://apple.com");


            db.crunchifyPrepareStat.close();
            db.crunchifyConn.close(); // connection close

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    String getTimestamp(String name) {
        String data="";
        db.makeJDBCConnection();
        String getQueryStatement = "SELECT * from phonedatabase where name='"+name+"'";
        System.out.println(getQueryStatement);
        try{
            db.crunchifyPrepareStat = db.crunchifyConn.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = db.crunchifyPrepareStat.executeQuery();
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
            db.makeJDBCConnection();
            String insertQueryStatement ="update phonedatabase set paytmPrice='"+Price+"',TimeStamp='"+Time+"' where Name='"+Name+"' ";
            System.out.println(insertQueryStatement);
            db.crunchifyPrepareStat = db.crunchifyConn.prepareStatement(insertQueryStatement);

            Thread.sleep(1000);

            // execute insert SQL statement
            db.crunchifyPrepareStat.executeUpdate();
            log("ds" + " added successfully");
            // connection close

        } catch (

                Exception e) {
            e.printStackTrace();
        }
    }

    String getLink(String name) {
        String data="";
        db.makeJDBCConnection();
        String getQueryStatement = "SELECT * from phonedatabase where name='"+name+"'";
        System.out.println(getQueryStatement+name);
        try{
            db.crunchifyPrepareStat = db.crunchifyConn.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = db.crunchifyPrepareStat.executeQuery();
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
    void addDataToDB(String Name,String Price, String Link,  Date Time ) {

        try {
            System.out.println( Name+Price);
            String insertQueryStatement ="update phonedatabase set PaytmPrice='"+Price+"',PaytmLink='"+Link+"',TimeStamp='"+Time+"' where Name='"+Name+"' ";
            db.crunchifyPrepareStat = db.crunchifyConn.prepareStatement(insertQueryStatement);

            Thread.sleep(1000);

            // execute insert SQL statement
            db.crunchifyPrepareStat.executeUpdate();
            log("ds" + " added successfully");
            db.crunchifyPrepareStat.close();
            db.crunchifyConn.close(); // connection close

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getDataFromDB() {

        try {
            // MySQL Select Query Tutorial
            String getQueryStatement = "SELECT * FROM pricespec";

            db.crunchifyPrepareStat = db.crunchifyConn.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = db.crunchifyPrepareStat.executeQuery();

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
            db.crunchifyPrepareStat.close();
            db.crunchifyConn.close(); // connection close

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}

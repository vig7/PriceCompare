import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class AddDbAmazon  extends  DBOperations{
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
            String insertQueryStatement ="update phonedatabase set Price='"+Price+"',TimeStamp='"+Time+"' where Name='"+Name+"' ";
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
    void addDataToDB(String Name,String Price, String Link,  Date Time ) {

        try {
            System.out.println( Name+Price+Link);
            String insertQueryStatement ="update phonedatabase set AmazonPrice='"+Price+"',AmazonLink='"+Link+"',TimeStamp='"+Time+"' where Name='"+Name+"' ";
            System.out.println(insertQueryStatement);
            crunchifyPrepareStat = crunchifyConn.prepareStatement(insertQueryStatement);


            // execute insert SQL statement
            crunchifyPrepareStat.executeUpdate();
            log("ds" + " added successfully");

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

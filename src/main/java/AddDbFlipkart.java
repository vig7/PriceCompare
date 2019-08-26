import java.sql.*;
import java.util.ArrayList;
import java.util.Date;


public class AddDbFlipkart extends  DBOperations{


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


    void addDataToDB(String Name,String Price, String Link,  Date Time ,Boolean stock) {

        try {

            String insertQueryStatement ="update phonedatabase set flipkartPrice='"+Price+"',FlipkartLink='"+Link+"',TimeStamp='"+Time+"' where Name='"+Name+"' ";
            //String insertQueryStatement ="update phonedatabase set flipkartStock="+stock+" where FlipkartLink='"+Link+"'";
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
    boolean checkIfExist(String name){
        try {
            String getQueryStatement ="Select count(*) from phonedatabase where Name='"+name+"' ";
            //String insertQueryStatement ="update phonedatabase set flipkartStock="+stock+" where FlipkartLink='"+Link+"'";
            System.out.println(getQueryStatement);
            crunchifyPrepareStat = crunchifyConn.prepareStatement(getQueryStatement);

            // execute insert SQL statement
            ResultSet rs = crunchifyPrepareStat.executeQuery();
            if(rs.next()){
                if(rs.getString("Count(*)").equals("0")){
                    return false;
                }
                else return true;
            }
            // connection close

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    void addStockandPrice(String Name,String Price,  Timestamp Time,int stock) {

        try {
            Price=Price.substring(1);
            String insertQueryStatement ="update phonedatabase set flipkartPrice='"+Price+"',TimeStamp='"+Time+"',FlipkartStock='"+stock+"' where Name='"+Name+"' ";
            //String insertQueryStatement ="update phonedatabase set flipkartStock="+stock+" where FlipkartLink='"+Link+"'";
            System.out.println(insertQueryStatement);
            crunchifyPrepareStat = crunchifyConn.prepareStatement(insertQueryStatement);

            Thread.sleep(1000);

            // execute insert SQL statement
            crunchifyPrepareStat.executeUpdate();
            log("ds" + " added successfully");
            // connection close

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//checktimestamp
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

    String getLink(String name) {
        String data="";

        String getQueryStatement = "SELECT * from phonedatabase where name='"+name+"'";
        System.out.println(getQueryStatement+name);
        try{
            crunchifyPrepareStat = crunchifyConn.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = crunchifyPrepareStat.executeQuery();
            if(rs.next()) {
                data = rs.getString("flipkartLink");
            }
            System.out.println(data);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return data;

    }

        ArrayList<String> getDataFromDB() {
         ArrayList<String> links=new ArrayList<String>();
        try {
            // MySQL Select Query Tutorial
            String getQueryStatement = "SELECT * from phonedatabase";

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
            String getQueryStatement = "SELECT * from phonedatabase";

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
    boolean checkTitle(String data){
        try {
            // MySQL Select Query Tutorial
            String getQueryStatement = "SELECT name from phonedatabase where name LIKE '% "+data+" %'";

            crunchifyPrepareStat = crunchifyConn.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = crunchifyPrepareStat.executeQuery();
            // Let's iterate through the java ResultSet

            while (rs.next()) {
                String s = rs.getString("name");

                // Simply Print the results
                if(s.length()!=0)
                    return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    boolean checkTitleCom(String data){
        try {
            // MySQL Select Query Tutorial
            String getQueryStatement = "SELECT name from phonedatabase where name LIKE '%"+data+" %'";

            crunchifyPrepareStat = crunchifyConn.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = crunchifyPrepareStat.executeQuery();
            // Let's iterate through the java ResultSet

            while (rs.next()) {
                String s = rs.getString("name");

                // Simply Print the results
                if(s.length()!=0)
                    return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    String  checkExten(String data,String ram,String rom){
        try {
            System.out.println(data +" "+ram+" "+rom);
            // MySQL Select Query Tutorial
            if(ram.equals("-1"))
                ram="";
            if(rom.equals("-1"))
                rom="";
            String getQueryStatement = "SELECT name from phonedatabase where name LIKE '%"+data+"%%"+rom+"%'&& RAM LIKE '%"+ram+"%' order by flipkartPrice ASC";

            crunchifyPrepareStat = crunchifyConn.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = crunchifyPrepareStat.executeQuery();

            // Let's iterate through the java ResultSet

            while (rs.next()) {
                String s = rs.getString("name");

                // Simply Print the results

               return s;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    return null;
    }

}

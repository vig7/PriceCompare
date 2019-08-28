import spark.Spark;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static spark.Spark.port;

public class AutoUpdateFlipkart {
    static  void check(String name){

        AddDbFlipkart flipk=new AddDbFlipkart();
        flipk.makeJDBCConnection();
        String data=flipk.getTimestamp(name);
        java.util.Date date=new java.util.Date();
        Timestamp databasetimestamp =new Timestamp(date.getTime());
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(data);
             databasetimestamp = new java.sql.Timestamp(parsedDate.getTime());
        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
        }
        Timestamp currenttime =new Timestamp(date.getTime());
        if(databasetimestamp.before(currenttime)){
               flipkartinsert insert=new flipkartinsert();
               insert.hit(name);
        }
    }
    public  static  void  main(String args[]){

        port(5678);
        GetPhoneSpecs.apply();
        Spark.get("/UpdateFlip", (request, response) -> {
            String name=request.queryParams("Name");
            check(name);
            return "1";
        });
    }
}
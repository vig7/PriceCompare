import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AutoUpdatePaytm {
    static  void check(String name){
        AddDbPaytm paytm=new AddDbPaytm();
        paytm.makeJDBCConnection();
        String data=paytm.getTimestamp(name);
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
            PaytmInsert update=new PaytmInsert();
            update.hit(name);
        }
    }
}

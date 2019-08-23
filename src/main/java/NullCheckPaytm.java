import java.sql.Timestamp;

public class NullCheckPaytm {
        void check (String name){
            CrawlPaytm paytm=new CrawlPaytm();
            String[] data = paytm.test(name);
            if (!data[1].equals("-1")) {
                AddDbPaytm db=new AddDbPaytm();
                db.makeJDBCConnection();
                String link = db.getLink(name);
                java.util.Date date = new java.util.Date();
                Timestamp databasetimestamp = new Timestamp(date.getTime());
                db.addDataToDB(name, data[1], data[2], databasetimestamp);
                db.close();
            }
        }
}

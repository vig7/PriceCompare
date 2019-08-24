import java.sql.Timestamp;

public class NullCheckFlip {
    void check(String name){
        CrawlFlipk flip=new CrawlFlipk();
        String[] data=flip.getPrice(name);
        if(!data[0].equals("-1")){
            AddStockFlipk stock=new AddStockFlipk();
            AddDbFlipkart db=new AddDbFlipkart();
            db.makeJDBCConnection();
            String link=db.getLink(name);

            java.util.Date date=new java.util.Date();
            Timestamp databasetimestamp =new Timestamp(date.getTime());
            db.addDataToDB(name,data[1],data[2],databasetimestamp,stock.test(link));
            db.close();
        }
    }
}

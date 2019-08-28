import com.opencsv.CSVReader;
import net.media.crawler.AsyncCrawler;
import net.media.crawler.CrawlerConfig;
import net.media.crawler.DefaultCrawlerConfig;
import net.media.crawler.util.RandomUserAgentManager;
import net.media.crawler.util.UserAgentManager;

import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ManualUpdateAmazon {
    void update(){
        ArrayList<String> data=new ArrayList<String>();
        CrawlAmazon amazon=new CrawlAmazon();
        Jdbc jdbc=new Jdbc();
        AddDbAmazon db=new AddDbAmazon();
        db.makeJDBCConnection();
        try {
            //File file = new File("PriceSpec.csv");

            UserAgentManager userAgentManager = new RandomUserAgentManager();
            CrawlerConfig crawlerConfig = new DefaultCrawlerConfig("TestCrawlApp", userAgentManager, false);
            AsyncCrawler asyncCrawler = new AsyncCrawler();
            CSVReader reader = new CSVReader(new FileReader("FinalSpec.csv"));
            String[] d=new String[3];
            int j=0;
            int k=0;
            while (j<4321){
                String[] name=reader.readNext();
                if(k==0) {
                    if (name[0].equals("Exmart X3")) {
                        System.out.println("dsadasdsa");
                        k = 1;
                    }
                    else {
                        j++;
                        continue;

                    }
                }
                String[] value=amazon.add(name[0],asyncCrawler,crawlerConfig);
                if(!value[0].equals("-1")){
                    d[0]=name[0];
                    d[1]=value[1];
                    d[2]=value[2];
                }
                else {
                    d[0]=name[0];
                    d[1]=null;
                    d[2]=null;
                }
                if(d[0]!=null) {
                    java.util.Date date = new java.util.Date();
                    Timestamp Time = new Timestamp(date.getTime());
                    db.addDataToDB(d[0], d[1], d[2], Time);
                }
            }
            db.close();
            reader.close();
        } catch (Exception e) {

            System.out.println(e);
        }
        db.close();
    }
}

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.Timestamp;
import java.util.Date;

public class flipkartinsert {
    void hit(String name){
        AddDbFlipkart flipk=new AddDbFlipkart();
        flipk.makeJDBCConnection();
        String link=flipk.getLink(name);
        if(link.equals("null")){
            NullCheckFlip check=new NullCheckFlip();
            check.check(name);
        }
        else {
            try {

                Document document = Jsoup.connect( link).get();
                String price = document.select("div._3qQ9m1").text();
                String look = document.select("div._9-sL7L").text();
                Date date = new Date();

                Timestamp timestamp = new Timestamp(date.getTime());
                System.out.println(price + look + link);
                if (price.length() == 0)
                    price = "null";
                if (look.length() == 0) {

                    flipk.addStockandPrice(name, price, timestamp, 1);
                } else {
                    flipk.addStockandPrice(name, price, timestamp, 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

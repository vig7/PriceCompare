import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.Timestamp;
import java.util.Date;

public class PaytmInsert {
    void  hit(String name){
        AddDbPaytm paytm=new AddDbPaytm();

        String link=paytm.getLink(name);
        if(link.equals("null")||link.length()==0){
            NullCheckPaytm check=new NullCheckPaytm();
            check.check(name);
        }
        else {
            {
                try {
                    System.out.println(link);
                    Document document = Jsoup.connect(link).get();
                    String price = document.select("span._1V3w").text();
                    System.out.println(price);
                    Date date = new Date();
                    Timestamp timestamp = new Timestamp(date.getTime());
                    System.out.println(price + name);
                    if (price.length() == 0)
                        price = "null";
                    paytm.addStockandPrice(name, price, timestamp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

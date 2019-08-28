import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.sql.SQLException;

public class CrawlSnapUrl {
    void crawl(String snapurl,String name) throws SQLException {
        System.out.println(snapurl);
        try {
//                String url = "https://www.snapdeal.com/product/nokia-red-1-8gb/5764608151593451586";
            String url=snapurl;
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                    .get();
            Elements price = document.select("div.pdp-e-i-PAY-r > span.pdp-final-price > span.payBlkBig");
            Elements stock = document.select("div.pdp-elec-topcenter-inner > span.sold-out-err");
            System.out.println(price.text());
            CrawlSnapUrl csu=new CrawlSnapUrl();
            if(price.isEmpty())
                SnapDealDB.updatePrice("","1",name);
            else {
                String prices=price.text().substring(4);
                SnapDealDB.updatePrice(prices, "0", name);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}

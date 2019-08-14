import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AmazonProductDetails {
    Map<String,String> mobileCollection=new HashMap<String, String>();
    void getAmazonPrice() throws IOException {
        String brandName="iphone+6";
        Connection.Response response =
                Jsoup.connect("https://www.amazon.in/s?k="+brandName+"&ref=nb_sb_noss_2")
                        .userAgent("Mozilla/5.0")
                        .timeout(10 * 1000)
                        .method(Connection.Method.GET)
                        .followRedirects(true)
                        .execute();

        //parse the document from response
        Document document = response.parse();
        Elements modelName = document.select("h2.a-size-mini > a.a-text-normal");
        int no_of_pages=0;
        for(Element link:modelName){
            if(no_of_pages<=4) {
                Connection.Response response1 = Jsoup.connect("https://www.amazon.in/" + link.attr("href"))
                        .userAgent("Mozilla/5.0")
                        .timeout(10 * 1000)
                        .method(Connection.Method.GET)
                        .followRedirects(true)
                        .execute();

                //parse the document from response
                Document doc = response1.parse();
                Elements prodName = doc.select("#titleSection > h1 > span");
                Elements prodPrice = doc.select("#priceblock_ourprice");
                Elements prodStock = doc.select("#availability");
            if(prodPrice.text()==null)
                mobileCollection.put(prodName.text(),"0");
            else
                mobileCollection.put(prodName.text(), prodPrice.text());
                no_of_pages++;
            }

        }
        System.out.println(mobileCollection);
        }
    }


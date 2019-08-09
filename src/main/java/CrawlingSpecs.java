import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CrawlingSpecs {
    Map<String,String> mobileCollection=new HashMap<String, String>();
    void getAmazonPrice() throws IOException {
        String brandName="oneplus_7";
        Connection.Response response =
                Jsoup.connect("https://www.amazon.in/s?k="+brandName+"&ref=nb_sb_noss_2")
                        .userAgent("Mozilla/5.0")
                        .timeout(10 * 1000)
                        .method(Connection.Method.GET)
                        .followRedirects(true)
                        .execute();

        //parse the document from response
        Document document = response.parse();
        Elements sponsored_checks=document.select("div.sg-col-inner > div.a-section > div.a-row > span.a-size-base");
        Elements modelName = document.select("h2 a.a-text-normal");
        Elements price = document.select("div.a-row > a.a-text-normal > span.a-price > span.a-offscreen");
        for(int i=0;i<10;i++){
            if(sponsored_checks.size()>i) {
                if (!sponsored_checks.get(i).text().equals("Sponsored"))
                    mobileCollection.put(modelName.get(i).text(),price.get(i).text());
            }
            else
                mobileCollection.put(modelName.get(i).text(),price.get(i).text());
        }
          System.out.println(mobileCollection);

        }
    }


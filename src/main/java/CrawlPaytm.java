import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class CrawlPaytm {
    void test(){
        String product="nokia";
        try {
            String url="https://paytmmall.com/shop/search?q="+product+"&from=organic&child_site_id=6&site_id=2";
            Document document = Jsoup.connect(url).get();
            Elements links=document.select("div._1kMS");
            Elements name=document.select("div._2apC");

            System.out.println(links.text()+"\n"+name.text());
        }
        catch (Exception e){
            System.out.println(e);
        }

    }
}

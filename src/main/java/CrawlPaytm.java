import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlPaytm {
    String[] test(String dataBaseName){
        ValidateNamePaytm validate=new ValidateNamePaytm();
        String[] value=new String[3];
        try {
            String url="https://paytmmall.com/shop/search?q="+dataBaseName+"&from=organic&child_site_id=6&site_id=2&category=66781";
            Document document = Jsoup.connect(url).get();
            Elements look=document.select("div._3WhJ");
            int j=0;
            for (Element l:look){
                Elements price=l.select("div._1kMS");
                String name=l.select("div._2apC").text();
                String links=l.select("a").attr("href");
                value[0]=name;
                value[1]=""+price.text();
                value[2]="https://www.paytmmall.com"+links;
                j++;
                if(validate.check(name,dataBaseName)){
                    return value;
                }
                if(j>3) {
                    value[1]="-1";
                    return value;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        value[1]=""+-1;
        return value;
    }

}

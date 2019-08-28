import net.media.crawler.AsyncCrawler;
import net.media.crawler.CrawlerConfig;
import net.media.crawler.data.AsyncCrawlResponse;
import org.asynchttpclient.ListenableFuture;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlAmazon {
    String[] add(String name,AsyncCrawler asyncCrawler,CrawlerConfig crawlerConfig){
        String alterprice;
        //data 1 is name,2 is price,3 is link
        String[] data=new String[3];
        ValidateName validate=new ValidateName();
        try {
            ListenableFuture<AsyncCrawlResponse> future = asyncCrawler.crawl("https://www.amazon.in/s?k="+name+"&ref=nb_sb_noss_2", crawlerConfig);

            String doc=""+future.get().getContent();
            Document document= Jsoup.parse(doc);
            Elements block=document.select("div.s-include-content-margin.s-border-bottom");
            for(Element temp:block){
                data[0]=temp.select("span.a-size-medium.a-color-base.a-text-normal").text();
                data[1]=temp.select("span.a-price-whole").text();
                if(data[1].length()==0)
                    continue;
                Elements atab=temp.select("a.a-link-normal");
                data[2]=atab.attr("href");
                data[2]="https://www.amazon.in"+data[2];
                if(validate.check(data[0],name)) {
                    System.out.println(data[0] + data[1] + data[2]);
                    return  data;
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        data[0]="-1";
        return data;
    }
}

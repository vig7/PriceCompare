import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlSnap {
    void test(){
            String product="redmi note 7";
            try {
                String url="https://www.snapdeal.com/search?keyword="+product+"&santizedKeyword=&catId=&categoryId=0&suggested=false&vertical=&noOfResults=20&searchState=&clickSrc=go_header&lastKeyword=&prodCatId=&changeBackToAll=false&foundInAll=false&categoryIdSearched=&cityPageUrl=&categoryUrl=&url=&utmContent=&dealDetail=&sort=rlvncy";
                Document document = Jsoup.connect(url).get();
                Elements links=document.select("span.product-price");

                Elements name=document.select("p.product-title");

                System.out.println(links.text()+"\n"+name.text());
            }
            catch (Exception e){
                System.out.println(e);
            }

        }
}

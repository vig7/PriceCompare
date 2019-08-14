import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class CrawlTata {
    void test() {
        String product = "redmi note 7";
        try {
            String url = "https://www.tatacliq.com/search/?searchCategory=all&text=" + product;
            Document document = Jsoup.connect(url).get();
            System.out.println(document);
            Elements links = document.select("div.dKCupF_5rtgdFHZiQ3xpQ");
            Elements name = document.select("h3.Bt9jWqBhJEHlqtj4x2xNa");
            System.out.println(links.text() + "\n" + name);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

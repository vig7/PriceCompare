import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PaytmInsert {
    void  hit(String name){
        AddDbPaytm paytm=new AddDbPaytm();

        String link=paytm.getLink(name);
        System.out.println(link);
        if(link==null){
            NullCheckPaytm check = new NullCheckPaytm();
            check.check(name);
        }
        else if (!link.contains("https://")) {
                NullCheckPaytm check = new NullCheckPaytm();
                check.check(name);
        }

        else {
            {
                try {
                    System.out.println(link);
                    System.setProperty("webdriver.gecko.driver","geckodriver.exe");
                    FirefoxDriver driver = new FirefoxDriver();
                    driver.navigate().to(link);
                    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                    String stringdoc=driver.getPageSource();
                    driver.close();
                    Document document = Jsoup.parse(stringdoc);
                  //  Document document=Jsoup.connect(link)
                    String price = document.select("span._1V3w").text();
                    System.out.println(price +"paytm");
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

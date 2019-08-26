import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Iterator;
import java.util.List;

public class CrawlAmazon {
    String[] add(String name){
        System.setProperty("webdriver.chrome.driver",

                "C:\\Users\\vignesh.ra\\Desktop\\chromedriver.exe");


        ChromeOptions o = new ChromeOptions();
        o.addArguments("disable-extensions");
        o.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(o);
        String[] data=new String[3];
        ValidateName validate=new ValidateName();
        driver.get("https://www.amazon.in/s?k=" + name + "&ref=nb_sb_noss_2");

        List<WebElement> element = driver.findElements(By.cssSelector("div.s-include-content-margin.s-border-bottom"));
        Iterator<WebElement> iter = element.iterator();
        while (iter.hasNext()){
            WebElement product=iter.next();
            String price=product.findElement(By.cssSelector("span.a-price-whole")).getText();
            String WebsiteName=product.findElement(By.cssSelector("span.a-size-medium.a-color-base.a-text-normal")).getText();
            String link=product.findElement(By.cssSelector("a.a-link-normal.a-text-normal")).getText();
            if(price.length()==0)
                continue;
            if(validate.check(WebsiteName,name)){
                data[0]=WebsiteName;
                data[1]=price;
                data[2]=link;
                driver.quit();
                return data;
            }
        }
        data[1]="-1";
        driver.quit();
        return data;
    }
}

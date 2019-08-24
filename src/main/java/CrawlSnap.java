//import com.sun.org.apache.xpath.internal.operations.Mod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

class Model{
    private String price;private String stock,url;
    Model(String price,String stock,String url){
        this.price=price;
        this.stock=stock;
        this.url=url;
    }
    String getPrices(){
        return this.price;
    }
    String getStock(){
        return this.stock;
    }
    String getUrl(){
        return this.url;
    }
}

public class CrawlSnap {
    boolean flag=false;
    DBOperations db = new DBOperations();
    java.sql.Connection Conn =db.makeJDBCConnection();
    PreparedStatement PrepareStat = null;
    HashMap<String, Model> mobileCollection=new HashMap<String, Model>();
    Date date= new Date();
    Timestamp ts = new Timestamp(date.getTime());

    void test() throws SQLException {
        String getQueryStatement = "SELECT * FROM phonedatabase where phone_id>1150 ";
        String brandName ;
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        while (rs.next()) {
            brandName = rs.getString("Name");
            try {
                String url = "https://www.snapdeal.com/search?keyword=" + brandName + "&santizedKeyword=&catId=&categoryId=175&suggested=false&vertical=&noOfResults=20&searchState=&clickSrc=go_header&lastKeyword=&prodCatId=&changeBackToAll=false&foundInAll=false&categoryIdSearched=&cityPageUrl=&categoryUrl=&url=&utmContent=&dealDetail=&sort=rlvncy#bcrumbSearch" + brandName;

                Document document = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                        .get();
                Elements maintag = document.select("div.product-tuple-listing");
                int no_of_links=0;
                for (Element link : maintag) {
                    if (no_of_links <= 2) {
                        Elements name = link.select("div.product-tuple-description > div.product-desc-rating > a > p");
                        System.out.println(name.text());
                        ValidateName vm=new ValidateName();
                        if (vm.check(name.text(), brandName)) {
                            Elements ttl = link.select("div.product-tuple-description > div.product-tuple-image > a > span");
                            Elements stock = link.select("div.product-tuple-image >  a > span.badge-soldout");
                            Elements mainLink=link.select("div.product-tuple-description > div.product-desc-rating >  a.noUdLine");
                            String linksbrand = mainLink.attr("href");
                            if (!ttl.isEmpty())
                                updatePrice(brandName,"0","0",linksbrand);
                            else {
                                Elements price = link.select("div.product-tuple-description > div.product-desc-rating > a > div.product-price-row > div.lfloat > span.product-price");
                                System.out.println(price.text());
                                if (!price.isEmpty()) {
                                    if (!stock.isEmpty())
                                        updatePrice(brandName,price.text(),stock.text(),linksbrand);

                                    else
                                        updatePrice(brandName,price.text(),"1",linksbrand);
                                }
                            }
                            break;
                        }
                        else flag=true;
                    }
                }
                Thread.sleep(1000);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        System.out.println(mobileCollection);
    }


    void test(String brandName)  {
            try {
                brandName=brandName.replaceAll(" ","%20");
                String url = "https://www.snapdeal.com/search?keyword=" + brandName + "&santizedKeyword=&catId=&categoryId=175&suggested=false&vertical=&noOfResults=20&searchState=&clickSrc=go_header&lastKeyword=&prodCatId=&changeBackToAll=false&foundInAll=false&categoryIdSearched=&cityPageUrl=&categoryUrl=&url=&utmContent=&dealDetail=&sort=rlvncy#bcrumbSearch" + brandName;
                System.out.println(url);
                Document document = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                        .get();

                Elements maintag = document.select("div.product-tuple-listing");
                int no_of_links=0;

                for (Element link : maintag) {

                    if (no_of_links <= 2) {
                        Elements name = link.select("div.product-tuple-description > div.product-desc-rating > a > p");
                        System.out.println(name.text());

                        if (ValidateName.check(name.text(), brandName)) {
                            System.out.println("inside");
                            Elements ttl = link.select("div.product-tuple-description > div.product-tuple-image > a > span");
                            Elements stock = link.select("div.product-tuple-image >  a > span.badge-soldout");
                            Elements mainLink=link.select("div.product-tuple-description > div.product-desc-rating >  a.noUdLine");
                            String linksbrand = mainLink.attr("href");
                            Elements price = link.select("div.product-tuple-description > div.product-desc-rating > a > div.product-price-row > div.lfloat > span.product-price");
                            if(stock.isEmpty()) {
                                if (!price.isEmpty()) {
                                    String prices = price.text().substring(4);
                                    updatePrice(brandName, prices, "1", linksbrand);
                                }
                            }
                            else {
                                    updatePrice(brandName, "", "0", linksbrand);
                                }
                            break;
                        }
                        else flag=true;
                        no_of_links++;
                    }
                    else break;
                    Thread.sleep(1000);
                }
                if(flag==true)
                    updatePrice(brandName);


            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    private void updatePrice(String bName) throws SQLException {
        try {
            bName=bName.replaceAll("%20"," ");
            String getQueryStatement = "Update phonedatabase set Timestamp='" + ts +"' where Name = '" + bName + "'";
            PrepareStat = Conn.prepareStatement(getQueryStatement);
            PrepareStat.executeUpdate();
            System.out.println("Updated ts");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    private void updatePrice(String bName,String bprice,String bstock,String burl) throws SQLException {
            try {
                bName=bName.replaceAll("%20"," ");
                String getQueryStatement = "Update phonedatabase set SnapPrice='" + bprice
                        +"', Timestamp='" + ts
                        + "' ,SnapStock='"+bstock
                        +"',SnapLink='"
                        +burl+ "' where Name = '" + bName + "'";
                PrepareStat = Conn.prepareStatement(getQueryStatement);
                PrepareStat.executeUpdate();
                System.out.println("Updated");
            }
            catch(Exception e){
                System.out.println(e);
            }
    }
}

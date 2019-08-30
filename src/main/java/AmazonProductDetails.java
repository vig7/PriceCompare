import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class AmazonProductDetails {
    DBOperations db = new DBOperations();
    PreparedStatement PrepareStat = null;
    Date date= new Date();
    Timestamp ts = new Timestamp(date.getTime());
    boolean flag=false;

    void getAmazonPrice(String brandName) throws SQLException {
        try {
            Connection.Response response =
                    Jsoup.connect("/www.amazon.in/s?k=" + brandName + "&ref=nb_sb_noss_2")
                            .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                            .timeout(10 * 1000)
                            .method(Connection.Method.GET)
                            .execute();
            //parse the document from response
            Document document = response.parse();
            Elements modelName = document.select("h2.a-size-mini > a.a-text-normal");
            int no_of_pages = 0;
            for (Element link : modelName) {
                if (no_of_pages <= 4) {
                    String url = "https://www.amazon.in/" + link.attr("href");
                    Connection.Response response1 = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                            .timeout(10 * 1000)
                            .method(Connection.Method.GET)
                            .execute();
                    //parse the document from response
                    Document doc = response1.parse();
                    Elements prodName = doc.select("#titleSection > h1 > span");
                    Elements prodPrice = doc.select("#priceblock_ourprice");
                    Elements prodStock = doc.select("#availability");
                    Elements otherprice=doc.select("#olp_feature_div > #olp-new > span.olp-padding-right >span > span");
                    if (ValidateName.check(prodName.text(), brandName)) {
                        System.out.println();
                        if (prodPrice.isEmpty() && !otherprice.isEmpty()) {
                            String price=prodPrice.text().substring(2,otherprice.text().length()-1);
                            System.out.println(price);
                            updatePrice(brandName, price, "0", url);
                        }
                        else if(otherprice.isEmpty() && prodPrice.isEmpty()){
                            updatePrice(brandName, "0", "0", url);
                        }
                        else {
                            String price=prodPrice.text().substring(2,prodPrice.text().length()-1);
                            updatePrice(brandName, price, "1", url);
                        }
                        break;
                    } else
                        flag = true;
                    no_of_pages++;
                } else break;
                Thread.sleep(1000);
            }
            if (flag == true)
                updatePrice(brandName);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    void setAmazonPrice(String brandName,String url) {
        try {
            Connection.Response response1 = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                    .timeout(10 * 1000)
                    .method(Connection.Method.GET)
                    .execute();
            Document doc = response1.parse();
            Elements prodName = doc.select("#titleSection > h1 > span");
            Elements prodPrice = doc.select("#priceblock_ourprice");
            Elements prodStock = doc.select("#availability");
            Elements otherprice = doc.select("#olp_feature_div > #olp-new > span.olp-padding-right >span > span");
            System.out.println(prodPrice);
            if (prodPrice.isEmpty() && !otherprice.isEmpty()) {
                String price = prodPrice.text().substring(2, otherprice.text().length() - 1);
                System.out.println(price);
                updatePrice(price, "0", brandName);
            } else if (prodPrice.isEmpty() && otherprice.isEmpty())
                updatePrice("", "0", brandName);
            else {
                String price = prodPrice.text().substring(2, prodPrice.text().length() - 1);
                updatePrice(price, "1", brandName);
            }
        }catch(Exception e){
            System.out.println("Something went wrong.."+e);
        }
    }

    private void updatePrice(String bName) throws SQLException {
        java.sql.Connection Conn =new DBOperations().makeJDBCConnection();
        try {
            String getQueryStatement = "Update phonedatabase set Timestamp='" + ts +"' where Name = '" + bName + "'";
            PrepareStat = Conn.prepareStatement(getQueryStatement);
            int count=PrepareStat.executeUpdate();
            if(count>0)
                System.out.println("Updated ts");
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally {
            Conn.close();
        }
    }

    private void updatePrice(String bName,String bprice,String bstock,String burl) throws SQLException {
        java.sql.Connection Conn =new DBOperations().makeJDBCConnection();
        try {
            String getQueryStatement = "Update phonedatabase set AmazonPrice='" + bprice
                    +"', Timestamp='" + ts
                    + "' ,AmazonStock='"+bstock
                    +"',AmazonLink='"
                    +burl+ "' where Name = '" + bName + "'";
            PrepareStat = Conn.prepareStatement(getQueryStatement);
            int count=PrepareStat.executeUpdate();
            if(count>0)
                System.out.println("Updated");
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally {
            Conn.close();
        }
    }

    private void updatePrice(String bprice,String bstock,String bName) throws SQLException {
        java.sql.Connection Conn =new DBOperations().makeJDBCConnection();
        try {
            Date date= new Date();
            Timestamp ts = new Timestamp(date.getTime());
            String getQueryStatement = "Update phonedatabase set AmazonPrice='" + bprice
                    +"', Timestamp='" + ts
                    + "' ,AmazonStock='"+bstock +"' where Name = '" + bName + "'";
            PrepareStat = Conn.prepareStatement(getQueryStatement);
            int count=PrepareStat.executeUpdate();
            if(count>0)
                System.out.println("Updated 2");
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally {
            Conn.close();
        }

    }
}

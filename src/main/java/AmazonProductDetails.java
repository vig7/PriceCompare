import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AmazonProductDetails {
    DBOperations db = new DBOperations();
    java.sql.Connection Conn =db.makeJDBCConnection();
    PreparedStatement PrepareStat = null;
    Date date= new Date();
    Timestamp ts = new Timestamp(date.getTime());
    boolean flag=false;

    void getAmazonPrice(String brandName)  {
       // String brandName="iphone+6";
        try {
            Connection.Response response =
                    Jsoup.connect("https://www.amazon.in/s?k=" + brandName + "&ref=nb_sb_noss_2")
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
                    Connection.Response response1 = Jsoup.connect("https://www.amazon.in/" + link.attr("href"))
                            .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                            .timeout(10 * 1000)
                            .method(Connection.Method.GET)
                            .execute();

                    //parse the document from response
                    Document doc = response1.parse();
                    Elements prodName = doc.select("#titleSection > h1 > span");
                    Elements prodPrice = doc.select("#priceblock_ourprice");
                    Elements prodStock = doc.select("#availability");
                    if (ValidateName.check(prodName.text(), brandName)) {
                        if (prodPrice.text() == null)
                            updatePrice(brandName, prodPrice.text(), "1", url);
                        else
                            updatePrice(brandName, prodPrice.text(), "0", url);
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

    void setAmazonPrice(String brandName,String url) throws IOException, SQLException {
        // String brandName="iphone+6";
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
                if (prodPrice.text() == null)
                    updatePrice(brandName, prodPrice.text(), "1");
                else
                    updatePrice(brandName, prodPrice.text(), "0");


        }

    private void updatePrice(String bName) throws SQLException {
        try {
            String getQueryStatement = "Update phonedatabase set Timestamp='" + ts +"' where Name = '" + bName + "'";
            PrepareStat = Conn.prepareStatement(getQueryStatement);
            PrepareStat.executeUpdate();
            System.out.println("Updated");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    private void updatePrice(String bName,String bprice,String bstock,String burl) throws SQLException {
        try {
            String getQueryStatement = "Update phonedatabase set SnapPrice='" + bprice
                    +"', Timestamp='" + ts
                    + "' ,AmazonStock='"+bstock
                    +"',AmazonLink='"
                    +burl+ "' where Name = '" + bName + "'";
            PrepareStat = Conn.prepareStatement(getQueryStatement);
            PrepareStat.executeUpdate();
            System.out.println("Updated");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    private void updatePrice(String bprice,String bstock,String bName) throws SQLException {
        try {
            Date date= new Date();
            Timestamp ts = new Timestamp(date.getTime());
            String getQueryStatement = "Update phonedatabase set AmazonPrice='" + bprice
                    +"', Timestamp='" + ts
                    + "' ,AmazonStock='"+bstock +"' where Name = '" + bName + "'";
            PrepareStat = Conn.prepareStatement(getQueryStatement);
            PrepareStat.executeUpdate();
            System.out.println("Updated");
        }
        catch(Exception e){
            System.out.println(e);
        }
        // }
    }
    }


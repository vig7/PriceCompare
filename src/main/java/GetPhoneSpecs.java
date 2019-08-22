import com.google.gson.Gson;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import static spark.Spark.*;

class PhoneDetails{
    private int id;
    private  String Name,flipkartPrice,flipkartStock,SnapPrice,SnapStock, operatingSystem,Camera,Display,RAM,specialFeat,Battery,FlipkartLink,SnapLink;
    private String AmazonPrice, AmazonStock, AmazonLink,PaytmLink,PaytmPrice;
    PhoneDetails(int id,String Name,String flipkartPrice,String flipkartStock
            ,String SnapPrice,String SnapStock,String AmazonStock,String AmazonPrice,String PaytmPrice){
        this.id=id;
        this.Name=Name;
        this.flipkartPrice=flipkartPrice;
        this.flipkartStock=flipkartStock;
        this.SnapPrice=SnapPrice;
        this.SnapStock=SnapStock;
        this.AmazonStock=AmazonStock;
        this.AmazonPrice=AmazonPrice;
        this.PaytmPrice=PaytmPrice;
    }
    PhoneDetails(int id,String Name,String operatingSystem,String Display,String Camera
            , String Battery,String specialFeat,String RAM,String flipkartPrice,String flipkartStock
            ,String FlipkartLink,String SnapPrice,String SnapStock,String SnapLink,String AmazonPrice
            ,String AmazonStock,String AmazonLink,String PaytmPrice,String PaytmLink){
        this.id=id;
        this.Name=Name;
        this.flipkartPrice=flipkartPrice;
        this.flipkartStock=flipkartStock;
        this.SnapPrice=SnapPrice;
        this.SnapStock=SnapStock;
        this.operatingSystem=operatingSystem;
        this.Camera=Camera;
        this.Display=Display;
        this.RAM=RAM;
        this.Battery=Battery;
        this.specialFeat=specialFeat;
        this.FlipkartLink=FlipkartLink;
        this.SnapLink=SnapLink;
        this.AmazonLink=AmazonLink;
        this.AmazonPrice=AmazonPrice;
        this.AmazonStock=AmazonStock;
        this.PaytmPrice=PaytmPrice;
        this.PaytmLink=PaytmLink;
    }
    PhoneDetails(int id,String Name){
        this.id=id;
        this.Name=Name;
    }
}

public class GetPhoneSpecs {
    private static Gson gson;
    private static  DBOperations db = new DBOperations();
    private static java.sql.Connection Conn =db.makeJDBCConnection();
    private  static PreparedStatement PrepareStat = null;
    ArrayList<PhoneDetails> list=new ArrayList();
    private static final HashMap<String, String> corsHeaders = new HashMap<String, String>();

    static {
        corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
        corsHeaders.put("Access-Control-Allow-Origin", "*");
        corsHeaders.put("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
        corsHeaders.put("Access-Control-Allow-Credentials", "true");
    }

    public final static void apply() {
        Filter filter = new Filter() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                corsHeaders.forEach((key, value) -> {
                    response.header(key, value);
                });
            }
        };
        Spark.after(filter);
    }

    private static ArrayList getPhoneDetails() throws SQLException {
        ArrayList<PhoneDetails> list=new ArrayList();
        String getQueryStatement = "SELECT phone_id,Name,flipkartPrice,flipkartStock,SnapPrice,SnapStock FROM phonedatabase limit 8 ";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        while (rs.next()) {
            list.add(new PhoneDetails(rs.getInt("phone_id")
                    ,rs.getString("Name")
                    ,rs.getString("flipkartPrice")
                    ,rs.getString("flipkartStock")
                    ,rs.getString("SnapPrice")
                    ,rs.getString("SnapStock")
                    ,rs.getString("AmazonStock")
                    ,rs.getString("AmazonPrice")
                    ,rs.getString("PaytmPrice")));
        }
        return  list;
    }
    private static boolean checkSnapTimestamp(Timestamp last_updated_ts,String url,String name,String stock) throws SQLException {
        Timestamp current_ts=new Timestamp(new Date().getTime());
        if(last_updated_ts.getDate()<current_ts.getDate()){
            if(!url.isEmpty()) {
                System.out.println("1");
                new CrawlSnapUrl().crawl( url,name);
            }
            else {
                System.out.println("2");
                new CrawlSnap().test(name);
            }
            return true;
        }
        else if(last_updated_ts.getDate()==current_ts.getDate()){
            int time_diff=last_updated_ts.getHours()-current_ts.getHours();
            int min_diff=last_updated_ts.getMinutes()-current_ts.getMinutes();
            int sec_diff=last_updated_ts.getSeconds()-current_ts.getSeconds();
            if(time_diff>0 && min_diff>0 && sec_diff>0) {
                if ( !url.isEmpty()) {
                    new CrawlSnapUrl().crawl(url, name);
                }
                else{
                    CrawlSnap cs=new CrawlSnap();
                    cs.test(name);
                }
            }
            return true;
        }
        return  false;
    }

    private static boolean checkAmazonTimestamp(Timestamp last_updated_ts,String url,String name,String stock) throws SQLException, IOException {
        Timestamp current_ts=new Timestamp(new Date().getTime());
        if(last_updated_ts.getDate()<current_ts.getDate()){
            if(url.isEmpty()) {
                new AmazonProductDetails().getAmazonPrice(name);
            }
            else if(!url.isEmpty()){
                new AmazonProductDetails().setAmazonPrice(name,url);
            }
            return true;
        }
        else if(last_updated_ts.getDate()==current_ts.getDate()){
            int time_diff=last_updated_ts.getHours()-current_ts.getHours();
            int min_diff=last_updated_ts.getMinutes()-current_ts.getMinutes();
            int sec_diff=last_updated_ts.getSeconds()-current_ts.getSeconds();
            if(time_diff>0 && min_diff>0 && sec_diff>0) {
                if(url.isEmpty()) {
                    new AmazonProductDetails().getAmazonPrice(name);
                }
                else if(!url.isEmpty()){
                    new AmazonProductDetails().setAmazonPrice(name,url);
                }
            }
            return true;
        }
        return  false;
    }

    private static ArrayList getPhoneSpecs(int id) throws SQLException, IOException {
        ArrayList<PhoneDetails> list=new ArrayList();
        String getQueryStatement = "SELECT * FROM phonedatabase where phone_id="+id;
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        boolean flag=false,aflag=false;
        while(rs.next()) {
            Timestamp last_updated_ts = rs.getTimestamp("Timestamp");
            if (checkSnapTimestamp(last_updated_ts, rs.getString("SnapLink"), rs.getString("Name"), rs.getString("SnapStock")))
                flag = true;
            if (checkAmazonTimestamp(last_updated_ts, rs.getString("AmazonLink"), rs.getString("Name"), rs.getString("AmazonStock")))
                aflag = true;
            if (flag != true && aflag!=true) {
                list.add(new PhoneDetails(rs.getInt("phone_id")
                        , rs.getString("Name")
                        , rs.getString("Operating_System")
                        , rs.getString("Display")
                        , rs.getString("Camera")
                        , rs.getString("Battery")
                        , rs.getString("Special_Features_Mobile_Phones")
                        , rs.getString("RAM")
                        , rs.getString("flipkartPrice")
                        , rs.getString("flipkartStock")
                        , rs.getString("FlipkartLink")
                        , rs.getString("SnapPrice")
                        , rs.getString("SnapStock")
                        , rs.getString("SnapLink")
                        ,rs.getString("AmazonPrice")
                        ,rs.getString("AmazonStock")
                        ,rs.getString("AmazonLink")
                        ,rs.getString("PaytmPrice")
                        ,rs.getString("PaytmLink")));
            } else {
                rs = PrepareStat.executeQuery();
                while (rs.next()) {
                    list.add(new PhoneDetails(rs.getInt("phone_id")
                            , rs.getString("Name")
                            , rs.getString("Operating_System")
                            , rs.getString("Display")
                            , rs.getString("Camera")
                            , rs.getString("Battery")
                            , rs.getString("Special_Features_Mobile_Phones")
                            , rs.getString("RAM")
                            , rs.getString("flipkartPrice")
                            , rs.getString("flipkartStock")
                            , rs.getString("FlipkartLink")
                            , rs.getString("SnapPrice")
                            , rs.getString("SnapStock")
                            , rs.getString("SnapLink")
                            ,rs.getString("AmazonPrice")
                            ,rs.getString("AmazonStock")
                            ,rs.getString("AmazonLink")
                            ,rs.getString("PaytmPrice")
                            ,rs.getString("PaytmLink")));
                }
            }
        }
        return  list;
    }

    private static ArrayList getSearchResults(String name) throws SQLException {
        System.out.println(name);
        ArrayList<PhoneDetails> list=new ArrayList();
        String getQueryStatement = "SELECT phone_id,Name FROM phonedatabase where Name like '"+name+"%'";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        while (rs.next()) {
            list.add(new PhoneDetails(rs.getInt("phone_id"),rs.getString("Name")));
        }
        return  list;
    }

    private static ArrayList getSearchSpecificResults(String name) throws SQLException, IOException {
        ArrayList<PhoneDetails> list=new ArrayList();
        String getQueryStatement = "SELECT * FROM phonedatabase where Name = '"+name+"'";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        boolean flag=false,aflag=false;
        while(rs.next()) {
            Timestamp last_updated_ts = rs.getTimestamp("Timestamp");
            if (checkSnapTimestamp(last_updated_ts, rs.getString("SnapLink"), rs.getString("Name"), rs.getString("SnapStock")))
                flag = true;
            if (checkAmazonTimestamp(last_updated_ts, rs.getString("AmazonLink"), rs.getString("Name"), rs.getString("AmazonStock")))
                aflag = true;
            if (flag != true && aflag!=true) {
                list.add(new PhoneDetails(rs.getInt("phone_id")
                        , rs.getString("Name")
                        , rs.getString("Operating_System")
                        , rs.getString("Display")
                        , rs.getString("Camera")
                        , rs.getString("Battery")
                        , rs.getString("Special_Features_Mobile_Phones")
                        , rs.getString("RAM")
                        , rs.getString("flipkartPrice")
                        , rs.getString("flipkartStock")
                        , rs.getString("FlipkartLink")
                        , rs.getString("SnapPrice")
                        , rs.getString("SnapStock")
                        , rs.getString("SnapLink")
                        ,rs.getString("AmazonPrice")
                        ,rs.getString("AmazonStock")
                        ,rs.getString("AmazonLink")
                        ,rs.getString("PaytmPrice")
                        ,rs.getString("PaytmLink")));
            } else {
                rs = PrepareStat.executeQuery();
                while (rs.next()) {
                    list.add(new PhoneDetails(rs.getInt("phone_id")
                            , rs.getString("Name")
                            , rs.getString("Operating_System")
                            , rs.getString("Display")
                            , rs.getString("Camera")
                            , rs.getString("Battery")
                            , rs.getString("Special_Features_Mobile_Phones")
                            , rs.getString("RAM")
                            , rs.getString("flipkartPrice")
                            , rs.getString("flipkartStock")
                            , rs.getString("FlipkartLink")
                            , rs.getString("SnapPrice")
                            , rs.getString("SnapStock")
                            , rs.getString("SnapLink")
                            ,rs.getString("AmazonPrice")
                            ,rs.getString("AmazonStock")
                            ,rs.getString("AmazonLink")
                            ,rs.getString("PaytmPrice")
                            ,rs.getString("PaytmLink")));
                }
            }
        }
        return  list;
    }

    public static void main(String[] arg) {
        port(5678);
        GetPhoneSpecs.apply();
        Spark.get("/FeaturedPhones", (request, response) -> {
            ArrayList list=getPhoneDetails();
            gson=new Gson();
            return gson.toJson(list);
         });

        Spark.get("/MobileSpecs", (request, response) -> {
            int id=Integer.parseInt(request.queryParams("id"));
            ArrayList list=getPhoneSpecs(id);
            gson=new Gson();
            return gson.toJson(list);
        });

        Spark.get("/SearchResults", (request, response) -> {
            String serachKey=request.queryParams("searchKey");
            ArrayList list=getSearchResults(serachKey);
            gson=new Gson();
            return gson.toJson(list);
        });

        Spark.get("/SearchSpecificResults", (request, response) -> {
            String serachKey=request.queryParams("searchKey");
            ArrayList list=getSearchSpecificResults(serachKey);
            gson=new Gson();
            return gson.toJson(list);
        });

//        post("/usersignup/username", (request, response) -> {
//            String uname = request.queryParams("username");
//            String password = request.queryParams("password");
//            Authentication.User user1 = gson.fromJson(request.body(), Authentication.User.class);
//
//            // userid=userid+1;
//            //User user=new User(user1.getusername(),uname,password);
//            response.body("Successfully Signed up");
//            response.status(201);
//             return userid;
//        });
    }
}

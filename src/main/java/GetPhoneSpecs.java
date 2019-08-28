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

import static spark.Spark.port;

class PhoneDetails{
    private int id;
    private  String Name,flipkartPrice,flipkartStock,SnapPrice,SnapStock, operatingSystem,Camera,Display,RAM,specialFeat,Battery,FlipkartLink,SnapLink;
    private String AmazonPrice, AmazonStock, AmazonLink,PaytmLink,PaytmPrice;
    private int setCon=0;
    PhoneDetails(int id,String Name,String flipkartPrice,String flipkartStock
            ,String SnapPrice,String SnapStock,String AmazonStock,String AmazonPrice,String PaytmPrice,int setCon){
        this.id=id;
        this.Name=Name;
        this.flipkartPrice=flipkartPrice;
        this.flipkartStock=flipkartStock;
        this.SnapPrice=SnapPrice;
        this.SnapStock=SnapStock;
        this.AmazonStock=AmazonStock;
        this.AmazonPrice=AmazonPrice;
        this.PaytmPrice=PaytmPrice;
        this.setCon=setCon;
    }
    PhoneDetails(int id,String Name,String operatingSystem,String Display,String Camera
            , String Battery,String specialFeat,String RAM,String flipkartPrice,String flipkartStock
            ,String FlipkartLink,String SnapPrice,String SnapStock,String SnapLink,String AmazonPrice
            ,String AmazonStock,String AmazonLink,String PaytmPrice,String PaytmLink,int setCon){
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
        this.setCon=setCon;
    }
    PhoneDetails(int id,String Name,int setCon){
        this.id=id;
        this.Name=Name;
        this.setCon=setCon;
    }
}

public class GetPhoneSpecs {
    private static Gson gson;
    private static java.sql.Connection Conn;
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

        Conn =DBOperations.makeJDBCConnection();
        String getQueryStatement = "SELECT phone_id,Name,flipkartPrice,flipkartStock,SnapPrice,SnapStock,AmazonStock,AmazonPrice,PaytmPrice FROM phonedatabase limit 8 ";
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
                    ,rs.getString("PaytmPrice"),0));
        }
        Conn.close();
        return  list;
    }

    private static void crawlSnap(String url,String name){
        Thread t=null;
        try {
            if (!url.isEmpty()) {
                //  Runnable runnable = () -> {

                try {
                    new CrawlSnapUrl().crawl(url, name);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

//                t = new Thread(runnable);
//                System.out.println(t.getId()+" started");
//                t.start();
            } else {
//                Runnable runnable = () -> {
                try {
                    new CrawlSnap().test(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                };
//                 t = new Thread(runnable);
//                t.start();
//                System.out.println(t.getId()+" started");
            }
        }catch (Exception e){
            System.out.println(e);
        }
        finally{
//            System.out.println(t.getId()+" stopped");
//            t.stop();
        }
    }
    private static boolean checkSnapTimestamp(Timestamp last_updated_ts,String url,String name,String stock) throws SQLException {
        Timestamp current_ts=new Timestamp(new Date().getTime());
        if(last_updated_ts.getDate()<current_ts.getDate()){
            crawlSnap(url,name);
            System.out.println("hh");
            return true;
        }
        else if(last_updated_ts.getDate()==current_ts.getDate()){
            int time_diff=last_updated_ts.getHours()-current_ts.getHours();
            int min_diff=last_updated_ts.getMinutes()-current_ts.getMinutes();
            int sec_diff=last_updated_ts.getSeconds()-current_ts.getSeconds();
            if(time_diff>0 && min_diff>0 && sec_diff>0) {
                crawlSnap(url,name);
                System.out.println("hh1");
            }
            return true;
        }
        return  false;
    }

    private static boolean checkAmazonTimestamp(Timestamp last_updated_ts,String url,String name,String stock) throws SQLException, IOException {
        Timestamp current_ts=new Timestamp(new Date().getTime());
        if(last_updated_ts.getDate()<current_ts.getDate()){
            if(url.isEmpty()) {
                //new AmazonProductDetails().getAmazonPrice(name);
            }
            else if(!url.isEmpty()){
                // new AmazonProductDetails().setAmazonPrice(name,url);
            }
            return true;
        }
        else if(last_updated_ts.getDate()==current_ts.getDate()){
            int time_diff=last_updated_ts.getHours()-current_ts.getHours();
            int min_diff=last_updated_ts.getMinutes()-current_ts.getMinutes();
            int sec_diff=last_updated_ts.getSeconds()-current_ts.getSeconds();
            if(time_diff>0 && min_diff>0 && sec_diff>0) {
                if(url.isEmpty()) {
                    //new AmazonProductDetails().getAmazonPrice(name);
                }
                else if(!url.isEmpty()){
                    //new AmazonProductDetails().setAmazonPrice(name,url);
                }
            }
            return true;
        }
        return  false;
    }

    private static ArrayList getPhoneSpecs(int id) throws SQLException, IOException {
        Conn=DBOperations.makeJDBCConnection();
        ArrayList<PhoneDetails> list=new ArrayList();
        String getQueryStatement = "SELECT * FROM phonedatabase where phone_id="+id;
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        boolean flag=false,aflag=false;
            if (flag != true && aflag!=true) {
                rs.next();

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
                        ,rs.getString("PaytmLink"),0));
            } else {
                rs = PrepareStat.executeQuery();
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
                            ,rs.getString("PaytmLink"),1));
                }

        Conn.close();
        return  list;
    }
    private static ArrayList getupdatedPhoneSpecs(int id) throws SQLException, IOException {
        Conn=DBOperations.makeJDBCConnection();
        ArrayList<PhoneDetails> list=new ArrayList();
        AutoUpdateFlipkart flipkart=new AutoUpdateFlipkart();
        AutoUpdatePaytm paytm=new AutoUpdatePaytm();
        String getQueryStatement = "SELECT * FROM phonedatabase where phone_id="+id;
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();

        boolean flag=false,aflag=false;
            rs.next();
            System.out.println(rs.getTimestamp("TimeStamp"));
            Timestamp last_updated_ts = rs.getTimestamp("Timestamp");
            if (checkSnapTimestamp(last_updated_ts, rs.getString("SnapLink"), rs.getString("Name"), rs.getString("SnapStock")))
                flag = true;
            if (checkAmazonTimestamp(last_updated_ts, rs.getString("AmazonLink"), rs.getString("Name"), rs.getString("AmazonStock")))
                aflag = true;
        flipkart.check(rs.getString("Name"));
        paytm.check(rs.getString("Name"));
        Conn=DBOperations.makeJDBCConnection();
                rs = PrepareStat.executeQuery();
                rs.next();
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
                            ,rs.getString("PaytmLink"),1));


        Conn.close();
        return  list;
    }


    private static ArrayList getFullDetails(String name) throws SQLException {
        Conn=DBOperations.makeJDBCConnection();
        ArrayList<PhoneDetails> list=new ArrayList();
        String getQueryStatement = "SELECT * FROM phonedatabase where Name like '"+name+"%'";
//        String getQueryStatement="SELECT * FROM phonedatabase WHERE MATCH (Name) AGAINST ('"+name+"' IN NATURAL LANGUAGE MODE)";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        while (rs.next()) {
            list.add(new PhoneDetails(rs.getInt("phone_id")
                    , rs.getString("Name")
                    , rs.getString("flipkartPrice")
                    ,rs.getString("flipkartStock")
                    , rs.getString("SnapPrice")
                    , rs.getString("SnapStock")
                    ,rs.getString("AmazonStock")
                    ,rs.getString("AmazonPrice")
                    ,rs.getString("PaytmPrice"),0));
        }
        Conn.close();
        return list;
    }

    private static ArrayList getSearchResults(String name) throws SQLException {
        Conn=DBOperations.makeJDBCConnection();
        ArrayList<PhoneDetails> list=new ArrayList();
        String getQueryStatement = "SELECT phone_id,Name FROM phonedatabase where Name like '"+name+"%'";
//        String getQueryStatement="SELECT phone_id,Name FROM phonedatabase WHERE MATCH (Name) AGAINST ('"+name+"' IN NATURAL LANGUAGE MODE)";

        PrepareStat = Conn.prepareStatement(getQueryStatement);
        PreparedStatement PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        while (rs.next()) {
            list.add(new PhoneDetails(rs.getInt("phone_id"),rs.getString("Name"),0));
        }
        Conn.close();
        return  list;
    }

    private static ArrayList getprice(String name) throws SQLException {
        Conn=DBOperations.makeJDBCConnection();
        ArrayList<String> list = new ArrayList();
        String getQueryStatement = "SELECT * FROM phonedatabase where name ='" + name + "'";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        while (rs.next()) {
            list.add(rs.getString("Name"));
            list.add(rs.getString("flipkartPrice"));
            list.add(rs.getString("FlipkartLink"));
            list.add(rs.getString("PaytmPrice"));
            list.add(rs.getString("PaytmLink"));
            list.add(rs.getString("SnapPrice"));
            list.add(rs.getString("SnapLink"));
            list.add(rs.getString("AmazonPrice"));
            list.add(rs.getString("AmazonLink"));


        }
        Conn.close();
        return list;
    }

    private static ArrayList getSearchSpecificResults(String name) throws SQLException, IOException {
        Conn=DBOperations.makeJDBCConnection();
        ArrayList<PhoneDetails> list=new ArrayList();
        String getQueryStatement = "SELECT * FROM phonedatabase where Name = '"+name+"'";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        boolean flag=false,aflag=false,fflag=false,pflag=false;
        while(rs.next()) {
            Timestamp last_updated_ts = rs.getTimestamp("Timestamp");
            if (checkSnapTimestamp(last_updated_ts, rs.getString("SnapLink"), rs.getString("Name"), rs.getString("SnapStock")))
                flag = true;
            if (checkAmazonTimestamp(last_updated_ts, rs.getString("AmazonLink"), rs.getString("Name"), rs.getString("AmazonStock")))
                aflag = true;
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
                        ,rs.getString("PaytmLink"),1));
            }
        }
        Conn.close();
        return  list;
    }
    private static  int  addFeedback(String email,String comment,int rating,int id) throws SQLException {
        Conn=DBOperations.makeJDBCConnection();
        String getQueryStatement="INSERT INTO User_Feedback (User_Email,product_id,feedback,rating) VALUES (?,?,?,?)";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        PrepareStat.setString(1,email);
        PrepareStat.setString(3,comment);
        PrepareStat.setInt(4,rating);
        PrepareStat.setInt(2,id);
        int count=PrepareStat.executeUpdate();
        Conn.close();
        if(count>0)
            return 1;
        return 0;
    }

    public static void main(String[] arg) throws SQLException {
        try {
            Port port=new Port();
            port(port.getHerokuAssignedPort());
            GetPhoneSpecs.apply();
            Spark.get("/FeaturedPhones", (request, response) -> {
                ArrayList list = getPhoneDetails();
                gson = new Gson();
                return gson.toJson(list);
            });

            Spark.get("/SimilarPhones", (request, response) -> {
                ArrayList list = getFullDetails(request.queryParams("searchKey"));
                gson = new Gson();
                return gson.toJson(list);

            });

            Spark.get("/MobileSpecs", (request, response) -> {
                int id = Integer.parseInt(request.queryParams("id"));
                ArrayList list = getPhoneSpecs(id);
                gson = new Gson();
                return gson.toJson(list);
            });
            Spark.get("/updatedSpecs", (request, response) -> {
                int id = Integer.parseInt(request.queryParams("id"));
                ArrayList list = getupdatedPhoneSpecs(id);
                gson = new Gson();
                return gson.toJson(list);
            });

            Spark.get("/SearchResults", (request, response) -> {
                String serachKey = request.queryParams("searchKey");
                ArrayList list = getSearchResults(serachKey);
                gson = new Gson();
                return gson.toJson(list);
            });

            Spark.get("/SearchSpecificResults", (request, response) -> {
                String serachKey = request.queryParams("searchKey");
                ArrayList list = getSearchSpecificResults(serachKey);
                gson = new Gson();
                return gson.toJson(list);
            });
            Spark.get("/Extension", (request, response) -> {
                String serachKey = request.queryParams("Title");
                Extension ext = new Extension();
                System.out.println(serachKey);
                String res = ext.hit(serachKey);
                ArrayList list = getprice(res);
                System.out.println(list);
                gson = new Gson();

                return gson.toJson(list);
            });
            Spark.post("/feedback", (request, response) -> {
                String email = request.queryParams("email");
                int id = Integer.parseInt(request.queryParams("id"));
                String comment = request.queryParams("comment");
                int rating = Integer.parseInt(request.queryParams("rating"));
                System.out.println(email);
                int res = addFeedback(email, comment, rating, id);

                response.body("Successfully added");
                response.status((200));

                if (res == 1)
                    return 1;
                return 0;
            });
        }catch(Exception e){
            Conn.close();
            System.out.println(e);
        }


        Spark.get("/UpdateFlip", (request, response) -> {
            String Flipname=request.queryParams("Name");
            AutoUpdateFlipkart updateflip=new AutoUpdateFlipkart();
            updateflip.check(Flipname);
            return "1";
        });
        Spark.get("/UpdatePaytm", (request, response) -> {
            String Paytmname=request.queryParams("Name");
            AutoUpdatePaytm updatepaytm=new AutoUpdatePaytm();
            updatepaytm.check(Paytmname);
            return "1";
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
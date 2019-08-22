import com.google.gson.Gson;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static spark.Spark.*;

class PhoneDetails{
    private int id;
    private  String Name,flipkartPrice,flipkartStock,SnapPrice,SnapStock, operatingSystem,Camera,Display,RAM,specialFeat,Battery,FlipkartLink,SnapLink;
    PhoneDetails(int id,String Name,String flipkartPrice,String flipkartStock
            ,String SnapPrice,String SnapStock){
        this.id=id;
        this.Name=Name;
        this.flipkartPrice=flipkartPrice;
        this.flipkartStock=flipkartStock;
        this.SnapPrice=SnapPrice;
        this.SnapStock=SnapStock;
    }
    PhoneDetails(int id,String Name,String operatingSystem,String Display,String Camera
            , String Battery,String specialFeat,String RAM,String flipkartPrice,String flipkartStock
            ,String FlipkartLink,String SnapPrice,String SnapStock,String SnapLink){
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
        String getQueryStatement = "SELECT phone_id,Name,flipkartPrice,flipkartStock,SnapPrice,SnapStock FROM finaltab limit 8 ";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        while (rs.next()) {
            list.add(new PhoneDetails(rs.getInt("phone_id")
                    ,rs.getString("Name")
                    ,rs.getString("flipkartPrice")
                    ,rs.getString("flipkartStock")
                    ,rs.getString("SnapPrice")
                    ,rs.getString("SnapStock")));
        }
        return  list;
    }

    private static ArrayList getPhoneSpecs(String id) throws SQLException {
        ArrayList<PhoneDetails> list=new ArrayList();
        String getQueryStatement = "SELECT * FROM finaltab where name='"+id+"'";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        while (rs.next()) {
            list.add(new PhoneDetails(rs.getInt("Name")
                    ,rs.getString("Name")
                    ,rs.getString("Operating_System")
                    ,rs.getString("Display")
                    ,rs.getString("Camera")
                    ,rs.getString("Battery")
                    ,rs.getString("Special_Features_Mobile_Phones")
                    ,rs.getString("RAM")
                    ,rs.getString("flipkartPrice")
                    ,rs.getString("flipkartStock")
                    ,rs.getString("FlipkartLink")
                    ,rs.getString("SnapPrice")
                    ,rs.getString("SnapStock")
                    ,rs.getString("SnapLink")));
        }
        return  list;
    }

    private static ArrayList getSearchResults(String name) throws SQLException {
        ArrayList<String> list=new ArrayList();
        String getQueryStatement = "SELECT * FROM finaltab where name ='"+name+"'";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        while (rs.next()) {
            list.add(rs.getString("Name"));
            list.add(rs.getString("flipkartPrice"));
            list.add(rs.getString("FlipkartLink"));
            list.add(rs.getString("PaytmPrice"));
            list.add(rs.getString("PaytmLink"));
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
            String Name=(request.queryParams("Name"));
            ArrayList list=getPhoneSpecs(Name);
            gson=new Gson();
            return gson.toJson(list);

        });

        Spark.get("/SearchResults", (request, response) -> {
            String serachKey=request.queryParams("searckKey");
            ArrayList list=getSearchResults(serachKey);
            gson=new Gson();
            return gson.toJson(list);

        });
        Spark.get("/Extension", (request, response) -> {
            String serachKey=request.queryParams("Title");
            Extension ext=new Extension();
            System.out.println(serachKey);
            String res=ext.hit(serachKey);
            ArrayList list=getSearchResults(res);
            System.out.println(list);
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
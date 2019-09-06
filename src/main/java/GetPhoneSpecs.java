import com.google.gson.Gson;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

import static spark.Spark.port;

class PhoneDetails {
    private int id;
    private String Name, flipkartPrice, flipkartStock, SnapPrice, SnapStock, operatingSystem, Camera, Display, RAM, specialFeat, Battery, FlipkartLink, SnapLink;
    private String AmazonPrice, AmazonStock, AmazonLink, PaytmLink, PaytmPrice;
    private int setCon = 0;

    PhoneDetails(int id, String Name, String flipkartPrice, String flipkartStock
            , String SnapPrice, String SnapStock, String AmazonStock, String AmazonPrice, String PaytmPrice, int setCon) {
        this.id = id;
        this.Name = Name;
        this.flipkartPrice = flipkartPrice;
        this.flipkartStock = flipkartStock;
        this.SnapPrice = SnapPrice;
        this.SnapStock = SnapStock;
        this.AmazonStock = AmazonStock;
        this.AmazonPrice = AmazonPrice;
        this.PaytmPrice = PaytmPrice;
        this.setCon = setCon;
    }

    PhoneDetails(int id, String Name, String operatingSystem, String Display, String Camera
            , String Battery, String specialFeat, String RAM, String flipkartPrice, String flipkartStock
            , String FlipkartLink, String SnapPrice, String SnapStock, String SnapLink, String AmazonPrice
            , String AmazonStock, String AmazonLink, String PaytmPrice, String PaytmLink, int setCon) {
        this.id = id;
        this.Name = Name;
        this.flipkartPrice = flipkartPrice;
        this.flipkartStock = flipkartStock;
        this.SnapPrice = SnapPrice;
        this.SnapStock = SnapStock;
        this.operatingSystem = operatingSystem;
        this.Camera = Camera;
        this.Display = Display;
        this.RAM = RAM;
        this.Battery = Battery;
        this.specialFeat = specialFeat;
        this.FlipkartLink = FlipkartLink;
        this.SnapLink = SnapLink;
        this.AmazonLink = AmazonLink;
        this.AmazonPrice = AmazonPrice;
        this.AmazonStock = AmazonStock;
        this.PaytmPrice = PaytmPrice;
        this.PaytmLink = PaytmLink;
        this.setCon = setCon;
    }

    PhoneDetails(int id, String Name, int setCon) {
        this.id = id;
        this.Name = Name;
        this.setCon = setCon;
    }
}

public class GetPhoneSpecs {
    private static Gson gson;
    private static java.sql.Connection Conn;
    private  static PreparedStatement PrepareStat = null;
    private static final HashMap<String, String> corsHeaders = new HashMap<String, String>();
    static Redis redis=new Redis();

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
        ArrayList<PhoneDetails> list = new ArrayList();
        list = Redis.getPhoneDetails();
        if (list != null && list.size() > 0) {
            return list;
        } else {
            list = getPhoneDetailsFromDb();
            Redis.setPhoneDetails(list);
        }
        return list;
    }

    private static ArrayList<PhoneDetails> getPhoneDetailsFromDb() throws SQLException {
        ArrayList<PhoneDetails> list = new ArrayList<>();
        Conn = new DBOperations().makeJDBCConnection();
        String getQueryStatement = "SELECT phone_id,Name,flipkartPrice,flipkartStock,SnapPrice,SnapStock,AmazonStock,AmazonPrice,PaytmPrice FROM phonedatabase limit 8 ";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        while (rs.next()) {
            list.add(new PhoneDetails(rs.getInt("phone_id")
                    , rs.getString("Name")
                    , rs.getString("flipkartPrice")
                    , rs.getString("flipkartStock")
                    , rs.getString("SnapPrice")
                    , rs.getString("SnapStock")
                    , rs.getString("AmazonStock")
                    , rs.getString("AmazonPrice")
                    , rs.getString("PaytmPrice"), 0));
        }
        Conn.close();
        return list;
    }

    private static void crawlSnap(String url, String name) {
        try {
            if (!url.isEmpty()) {
                try {
                    new CrawlSnapUrl().crawl(url, name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    new CrawlSnap().test(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static boolean checkSnapTimestamp(Timestamp last_updated_ts, String url, String name, String stock) throws SQLException {
        Timestamp current_ts = new Timestamp(new Date().getDate());
        Date d1=new Date(last_updated_ts.getTime());
        Date d2=new Date();
        if (d1.compareTo(d2)<0) {
            crawlSnap(url, name);
           // new AmazonProductDetails().getAmazonPrice(name);
                return true;
            }
        else if (d1.getDay()==d2.getDay()) {

            int time_diff = d1.getHours() - d2.getHours();
            int min_diff = d1.getMinutes() - d2.getMinutes();
            int sec_diff = d1.getSeconds() - d2.getSeconds();
            if (time_diff > 0 && min_diff > 0 && sec_diff > 0) {
                 crawlSnap(url, name);
             //   new AmazonProductDetails().setAmazonPrice(name,url);
            }
                return true;
            }
        return false;
    }

    private static ArrayList getPhoneSpecs(int id) throws SQLException, IOException {
        Connection Conn=new DBOperations().makeJDBCConnection();
        ArrayList<PhoneDetails> list = new ArrayList();
        try {
            String getQueryStatement = "SELECT * FROM phonedatabase where phone_id=" + id;
            PrepareStat = Conn.prepareStatement(getQueryStatement);
            ResultSet rs = PrepareStat.executeQuery();
            rs.next();
            String name = rs.getString("Name");
            String ram = rs.getString("RAM");
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
                    , rs.getString("AmazonPrice")
                    , rs.getString("AmazonStock")
                    , rs.getString("AmazonLink")
                    , rs.getString("PaytmPrice")
                    , rs.getString("PaytmLink"), 1));
            String getQueryStatementRec = "select * from phonedatabase where name like '"+name.split(" ")[0]+"%' and name <> '"+name+"' and  RAM >=(select RAM from phonedatabase  where name = '"+name+"' ) limit 4";
            PrepareStat = Conn.prepareStatement(getQueryStatementRec);
            ResultSet rs1 = PrepareStat.executeQuery();
            while(rs1.next()){
                list.add(new PhoneDetails(rs1.getInt("phone_id")
                    , rs1.getString("Name")
                    , rs1.getString("flipkartPrice")
                    , rs1.getString("flipkartStock")
                    , rs1.getString("SnapPrice")
                    , rs1.getString("SnapStock")
                    , rs1.getString("AmazonStock")
                    , rs1.getString("AmazonPrice")
                    , rs1.getString("PaytmPrice"), 0));
            }
            rs.close();
            rs1.close();
        }catch(Exception e){
            System.out.println(e);
        }
        finally {
            Conn.close();
        }
        return  list;
    }

    private static ArrayList getupdatedPhoneSpecs(int id) throws SQLException, IOException {
        Conn=new DBOperations().makeJDBCConnection();
        ArrayList<String> list=new ArrayList();
        AutoUpdateFlipkart flipkart=new AutoUpdateFlipkart();
        AutoUpdatePaytm paytm=new AutoUpdatePaytm();
        String getQueryStatement = "SELECT * FROM phonedatabase where phone_id="+id;
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        boolean flag=false,aflag=false;

        if(!rs.next())
            return list;
            Timestamp last_updated_ts = rs.getTimestamp("Timestamp");
            String name=rs.getString("Name");
            String snapurl=rs.getString("SnapLink");
            String snapstock=rs.getString("SnapStock");
            String amazonlink=rs.getString("AmazonLink");
            String amazonstock=rs.getString("AmazonStock");
            if (checkSnapTimestamp(last_updated_ts, snapurl, name, snapstock))
                flag = true;
        if(flag==true ) {
            flipkart.check(name);
            paytm.check(name);
            list=runSelect(id);
        }
        rs.close();
        Conn.close();
        return list;
    }

    private static  ArrayList runSelect(int id) throws SQLException {
        Connection Conn=new DBOperations().makeJDBCConnection();
        ArrayList<String> list=new ArrayList<>();
        String getQueryStatement = "SELECT * FROM phonedatabase where phone_id="+id;
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs1 = Conn.prepareStatement(getQueryStatement).executeQuery();
        rs1.next();
        list.add(rs1.getString("flipkartPrice"));
        list.add(rs1.getString("SnapPrice"));
        list.add(rs1.getString("AmazonPrice"));
        list.add(rs1.getString("PaytmPrice"));
        rs1.close();
        Conn.close();
        return list;
    }

    public static HashMap<PhoneDetails, Double> sortByValue(HashMap<PhoneDetails, Double> hm)
    {
    List<Map.Entry<PhoneDetails, Double> > list =
            new LinkedList<Map.Entry<PhoneDetails, Double> >(hm.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<PhoneDetails, Double> >() {
        public int compare(Map.Entry<PhoneDetails, Double> o1,
                           Map.Entry<PhoneDetails, Double> o2)
        {
            return (o2.getValue()).compareTo(o1.getValue());
        }
    });
    HashMap<PhoneDetails, Double> temp = new LinkedHashMap<PhoneDetails, Double>();
    for (Map.Entry<PhoneDetails, Double> aa : list) {
        temp.put(aa.getKey(), aa.getValue());
    }
    return temp;
}


    private static ArrayList getlevenResults(String name) throws SQLException, IOException {
        Conn =new DBOperations().makeJDBCConnection();
        HashMap <PhoneDetails ,Double> hm=new HashMap<>();
        ArrayList<PhoneDetails> listphones=new ArrayList<>();
        char firstName=name.charAt(0);
        String queryStatement="(select * from phonedatabase where Name like '"+firstName+"%') " +
                "union (select * from phonedatabase where Name like '%"+firstName+"%')";
        PrepareStat=Conn.prepareStatement(queryStatement);
        ResultSet rs=PrepareStat.executeQuery();
        boolean flag=false;
        jaroDistance js=new jaroDistance();
            while (rs.next()){
                flag=true;
                String n=rs.getString("Name");
                String fp=rs.getString("flipkartPrice") ;
                String fs= rs.getString("flipkartStock");
                String snapprice=rs.getString("SnapPrice") ;
                String snapstock=rs.getString("SnapStock");
                String amazonstock=rs.getString("AmazonStock");
                String amazonprice= rs.getString("AmazonPrice");
                String paytmprice=rs.getString("PaytmPrice");
                Double count=js.apply(name,n);
                if(count>=70) {
                    hm.put(new PhoneDetails(rs.getInt("phone_id"),
                           n,fp,fs,snapprice,snapstock,amazonstock,amazonprice,paytmprice,0 ), count);
                }
        }
        Map<PhoneDetails, Double> hm1 = sortByValue(hm);
        if(flag==false)
            return listphones;
        for (Map.Entry<PhoneDetails, Double> en : hm1.entrySet()) {
            listphones.add(en.getKey());
        }
        rs.close();
        Conn.close();
        return listphones;
    }

    private static ArrayList getFullDetails(String name) throws SQLException {
        Conn = new DBOperations().makeJDBCConnection();
        ArrayList<PhoneDetails> list = new ArrayList();
        String getQueryStatement="(SELECT * FROM phonedatabase where Name like '"+name+"%' )";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        while (rs.next()) {
            list.add(new PhoneDetails(rs.getInt("phone_id")
                    , rs.getString("Name")
                    , rs.getString("flipkartPrice")
                    , rs.getString("flipkartStock")
                    , rs.getString("SnapPrice")
                    , rs.getString("SnapStock")
                    , rs.getString("AmazonStock")
                    , rs.getString("AmazonPrice")
                    , rs.getString("PaytmPrice"), 0));
        }
        Conn.close();
        return list;
    }

    private static ArrayList getSearchResults(String name) throws SQLException {
        Conn=new DBOperations().makeJDBCConnection();
        ArrayList<PhoneDetails> list=new ArrayList();
        String getQueryStatement="(SELECT * FROM phonedatabase where Name ='"+name+"') " +
                "union (select * from phonedatabase where  Name like '"+name+"%') " +
                "union (select * from phonedatabase where  Name like '%"+name+"%')";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
        while (rs.next()) {
            String n=rs.getString("Name");
            list.add(new PhoneDetails(rs.getInt("phone_id"), n, 0));
        }
        Conn.close();
        return list;
    }

    private static ArrayList getprice(String name) throws SQLException {
        Conn = new DBOperations().makeJDBCConnection();
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
        Conn = new DBOperations().makeJDBCConnection();
        ArrayList<PhoneDetails> list = new ArrayList();
        String getQueryStatement = "SELECT * FROM phonedatabase where Operating_System like '" + name + "%' or " +
                "Operating_System like '%"+name+"%' limit 10";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        ResultSet rs = PrepareStat.executeQuery();
       while(rs.next()){
           list.add(new PhoneDetails(rs.getInt("phone_id"),rs.getString("Name")
                   , rs.getString("flipkartPrice")
                   , rs.getString("flipkartStock")
                   , rs.getString("SnapPrice")
                   , rs.getString("SnapStock")
                   , rs.getString("AmazonStock")
                   , rs.getString("AmazonPrice")
                   , rs.getString("PaytmPrice"),0));
       }
        Conn.close();
        return list;
    }

    private static int addFeedback(String email, String comment, int rating, int id) throws SQLException {
        Conn = new DBOperations().makeJDBCConnection();
        String getQueryStatement = "INSERT INTO feedback (User_Email,product_id,feedback,rating) VALUES (?,?,?,?)";
        PrepareStat = Conn.prepareStatement(getQueryStatement);
        PrepareStat.setString(1, email);
        PrepareStat.setInt(2, id);
        PrepareStat.setString(3, comment);
        PrepareStat.setInt(4, rating);
        int count = PrepareStat.executeUpdate();
        Conn.close();
        if (count > 0)
            return 1;
        return 0;
    }

    public static void main(String[] arg) throws SQLException {
        try {
            Port port = new Port();
            port(port.getHerokuAssignedPort());
            GetPhoneSpecs.apply();
            Spark.get("/FeaturedPhones", (request, response) -> {
                ArrayList list = getPhoneDetails();
                gson = new Gson();
                return gson.toJson(list);
            });

            Spark.get("/SimilarPhones", (request, response) -> {
                ArrayList list = getlevenResults(request.queryParams("searchKey"));
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

            Spark.get("/getAllPhones", (request, response) -> {
                String serachKey = request.queryParams("searchKey");
                ArrayList list = getFullDetails(serachKey);
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
                String requestfeedback=request.body();
                String parts[]=requestfeedback.split("&");
                String subparts[]=new String[4];
                int k=0;
                for(int i=0;i<parts.length;i++)
                    subparts[i]=parts[i].split("=")[1];

                int id = Integer.parseInt(subparts[0]);
                String email = subparts[1];
                String comment = subparts[2];
                email=email.replaceAll("%40","@");
//                email=email.split("%40")[0]+"@"+email.split("%40")[1];
                if(comment.length()>1) {
                   comment= comment.replaceAll("%20"," ");
                }
                int rating = Integer.parseInt(subparts[3]);
                int res = addFeedback(email, comment, rating, id);
                if (res == 1) {
                    response.body("Successfully added");
                    response.status((200));
                    return 1;
                }
                return 0;
            });
        } catch (Exception e) {
            Conn.close();
            System.out.println(e);
        }


        Spark.get("/UpdateFlip", (request, response) -> {
            String Flipname = request.queryParams("Name");
            AutoUpdateFlipkart updateflip = new AutoUpdateFlipkart();
            updateflip.check(Flipname);
            return "1";
        });
        Spark.get("/UpdatePaytm", (request, response) -> {
            String Paytmname = request.queryParams("Name");
            AutoUpdatePaytm updatepaytm = new AutoUpdatePaytm();
            updatepaytm.check(Paytmname);
            return "1";
        });

    }
}
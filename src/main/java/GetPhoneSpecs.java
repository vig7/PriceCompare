import com.google.gson.Gson;
import org.eclipse.jetty.server.Authentication;

import static spark.Spark.port;
import static spark.Spark.post;

public class GetPhoneSpecs {
    private static Gson gson;

    public static void main(String[] arg) {
        int userid=0;
        port(5678);
        // get("/hello", (request, response) -> "Hello World!");;
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

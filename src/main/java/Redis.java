import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Redis {

    public static ArrayList<PhoneDetails> getPhoneDetails() {
        Jedis jedis = getRedisConnection();
        Type listType = new TypeToken<ArrayList<PhoneDetails>>(){}.getType();
        ArrayList<PhoneDetails> phoneDetails = new Gson().fromJson(jedis.get("topTrendingPhones"), listType);
        jedis.expire("topTrendingPhones",432000);
        jedis.close();
        return phoneDetails;
    }

    private static Jedis getRedisConnection(){
        return  new Jedis("localhost", 6379);
    }


    public static void setPhoneDetails(ArrayList<PhoneDetails> list) {
        Jedis jedis = getRedisConnection();
        jedis.set("topTrendingPhones",new Gson().toJson(list));
        jedis.close();
    }
}

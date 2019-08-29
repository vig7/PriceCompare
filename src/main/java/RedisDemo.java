import redis.clients.jedis.Jedis;

import java.util.Set;

public class RedisDemo {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println("Connection to server sucessfully");
        Set<String> keys = jedis.keys("*");
        System.out.printf(keys.toString());
    }
}

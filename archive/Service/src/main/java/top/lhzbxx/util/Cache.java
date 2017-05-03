package top.lhzbxx.util;

import redis.clients.jedis.Jedis;
import top.lhzbxx.config.FrameworkConfig;

import static top.lhzbxx.config.FrameworkConfig.REDIS_POOL;

/**
 * Author: LuHao
 * Date: 16/5/29
 * Mail: lhzbxx@gmail.com
 */

public class Cache {

// Jedis.set() parameter:
//
// nxxx NX|XX,
//      NX -- Only set the key if it does not already exist.
//      XX -- Only set the key if it already exist.
// expx EX|PX, expire time units.
//      EX -- seconds.
//      PX -- milliseconds
// time expire time in the units of {@param #expx}

    public static void setAccessToken(String accessToken, String userId) {
        try (Jedis jedis = REDIS_POOL.getResource()) {
            jedis.set(FrameworkConfig.APPLICATION_NAME + ":" + FrameworkConfig.ACCESS_TOKEN_NAME
                    + ":" + accessToken, userId, "NX", "EX", FrameworkConfig.REFRESH_TOKEN_EXPIRE);
        }
    }

    public static void setRefreshToken(String refreshToken, String userId) {
        try (Jedis jedis = REDIS_POOL.getResource()) {
            jedis.set(FrameworkConfig.APPLICATION_NAME + ":" + FrameworkConfig.REFRESH_TOKEN_NAME
                    + ":" + refreshToken, userId, "NX", "EX", FrameworkConfig.REFRESH_TOKEN_EXPIRE);
        }
    }

    public static String checkAccessToken(String accessToken) {
        try (Jedis jedis = REDIS_POOL.getResource()) {
            return jedis.get(FrameworkConfig.APPLICATION_NAME + ":" +
                    FrameworkConfig.ACCESS_TOKEN_NAME + ":" + accessToken);
        }
    }

    public static String checkRefreshToken(String refreshToken) {
        try (Jedis jedis = REDIS_POOL.getResource()) {
            String userId = jedis.get(FrameworkConfig.APPLICATION_NAME + ":" +
                    FrameworkConfig.REFRESH_TOKEN_NAME + ":" + refreshToken);
            jedis.del(FrameworkConfig.APPLICATION_NAME + ":" +
                    FrameworkConfig.REFRESH_TOKEN_NAME + ":" + refreshToken);
            return userId;
        }
    }

}

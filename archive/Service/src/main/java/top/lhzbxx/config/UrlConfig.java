package top.lhzbxx.config;

/**
 * Author: LuHao
 * Date: 16/5/23
 * Mail: lhzbxx@gmail.com
 */

public class UrlConfig {

    public static final String USER = "/users";
    public static final String USER_AUTH = "/users/auth";
    public static final String USER_INFO = "/users/info";
    public static final String USER_INFO_AVATAR = "/users/info/avatar";
    public static final String USER_ID = "/users/:id";
    public static final String UTIL = "/utils";
    public static final String UTIL_NOW = "/utils/now";
    public static final String UTIL_REDIS = "/utils/redis";
    public static final String UTIL_ENCRYPT_SHA1 = "/utils/encrypt/sha1";
    public static final String UTIL_SALT = "/utils/salt";
    public static final String UTIL_UUID = "/utils/uuid";
    public static final String UTIL_SERIAL = "/utils/serial";
    public static final String UTIL_OVERVIEW = "/utils/overview";
    public static final String RECORD = "/records";
    public static final String RECORD_ID = "/records/:id";
    public static final String BOOK = "/books";
    public static final String BOOK_ID = "/books/:id";
    public static final String BOOK_ID1_RECORD_ID2 = "/books/:id1/records/:id2";
    public static String ALLOW_ONLY_AUTH(String path) {
        return "/api" + path;
    }

}

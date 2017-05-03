package top.lhzbxx.config;

import org.sql2o.Sql2o;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Author: LuHao
 * Date: 16/5/23
 * Mail: lhzbxx@gmail.com
 */

public class FrameworkConfig {

    public static final String APPLICATION_NAME = "YoTv";
    public static final int ACCESS_TOKEN_EXPIRE = 60 * 60 * 24 * 7;
    public static final int REFRESH_TOKEN_EXPIRE = 60 * 60 * 24 * 30;
    public static final int PASSWORD_LENGTH = 32;
    public static final int SALT_LENGTH = 32;
    public static final int USER_ACCOUNT_MIN_LENGTH = 5;
    public static final int USER_ACCOUNT_MAX_LENGTH = 20;
    public static final int USER_NICKNAME_MIN_LENGTH = 3;
    public static final int USER_NICKNAME_MAX_LENGTH = 10;
    public static final int USER_SERIAL_LENGTH = 4;
    public static final JedisPool REDIS_POOL = new JedisPool(
            new JedisPoolConfig(), DeployConfig.REDIS_HOST, DeployConfig.REDIS_PORT,
            DeployConfig.REDIS_TIMEOUT, DeployConfig.REDIS_PASSWORD, DeployConfig.REDIS_DB_INDEX);
    public static final Sql2o SQL2O = new Sql2o("jdbc:" + DeployConfig.DATABASE_NAME + "://" +
            DeployConfig.DATABASE_HOST + ":" + DeployConfig.DATABASE_PORT + "/" + DeployConfig.DATABASE_DB,
            DeployConfig.DATABASE_USER, DeployConfig.DATABASE_PASSWORD);
    public static final String ACCESS_TOKEN_NAME = "access_token";
    public static final String REFRESH_TOKEN_NAME = "refresh_token";
    public static final String RECORD_SOURCE_USER_MANUAL = "manual";
    public static final String RECORD_SOURCE_USER_SOUND = "sound";
    public static final String RECORD_SOURCE_USER_PARAGRAPH = "paragraph";
    public static final int BOOK_OWNER_AUTH_ROOT = ((int) 'x');
    public static final int BOOK_OWNER_AUTH_READ_ONLY = ((int) 'r');
    public static final int BOOK_OWNER_AUTH_WRITE = ((int) 'w');
    public static final int BOOK_OWNER_TYPE_ORGANIZATION = ((int) 'o');
    public static final int BOOK_OWNER_TYPE_USER = ((int) 'u');
    public static final int ORGANIZATION_AUTH_ROOT = ((int) 'x');

}

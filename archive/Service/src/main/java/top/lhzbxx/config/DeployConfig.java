package top.lhzbxx.config;

/**
 * Author: LuHao
 * Date: 16/5/23
 * Mail: lhzbxx@gmail.com
 */

public class DeployConfig {

    static final boolean DEPLOY_OR_TEST = false;
    static final String REDIS_HOST = DEPLOY_OR_TEST ? "115.28.71.169" : "192.168.99.100";
    static final String REDIS_PASSWORD = DEPLOY_OR_TEST ? "6bKB5rWp5biF5ZOl" : null;
    static final int REDIS_PORT = 6379;
    static final int REDIS_TIMEOUT = 3000;
    static final int REDIS_DB_INDEX = 1;
    static final String DATABASE_NAME = "postgresql";
    static final String DATABASE_HOST = DEPLOY_OR_TEST ? "115.28.71.169" : "192.168.99.100";
    static final String DATABASE_PORT = "5432";
    static final String DATABASE_DB = "save";
    static final String DATABASE_USER = "save";
    static final String DATABASE_PASSWORD = "8325ea9654acb17f55504c1cac481257";
    public static final String QINIU_ACCESS_KEY = "qIcvAcZzlPLaE_Cg0BKCL4zZXPYbKu11Og4Ah2LT";
    public static final String QINIU_SECRET_KEY = "AhDPtNaMtNdPaG9W-6Dqu8oEqR_bqj5x6OVRF_S6";
    public static final String QINIU_BUCKETNAME = "save";
    public static final String KEYSTORE_LOCATION = "deploy/keystore.jks";
    public static final String KEYSTORE_PASSWORD = "9b2010716a8a5849716e582067aecd26";
    public static final String UPLOAD_LOCATION = "assets";
    public static final int MIN_COMPATIBLE_VERSION = 1;

}

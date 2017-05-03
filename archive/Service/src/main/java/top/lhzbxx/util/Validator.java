package top.lhzbxx.util;

import org.sql2o.Connection;
import top.lhzbxx.config.FrameworkConfig;
import top.lhzbxx.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Author: LuHao
 * Date: 16/5/27
 * Mail: lhzbxx@gmail.com
 */

public class Validator {

    public static boolean isValidEmailAddress(String email) {
        String pattern = "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))";
        return email.matches(pattern);
    }

    public static boolean isValidStringLength(String str, int min, int max) {
        return str.length() >= min && str.length() <= max;
    }

    public static boolean isValidPassword(String str) {
        return str.length() == 32;
    }

    public static UUID validAccessToken(String str) {
        str = FrameworkConfig.REDIS_POOL.getResource().get(str);
        if (str.isEmpty()) {
            return null;
        }
        return UUID.fromString(str);
    }

    public static boolean isValidUUID(String str) {
        String pattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
        return str.matches(pattern);
    }

    public static boolean isUserExist(String str) {
        try (Connection conn = FrameworkConfig.SQL2O.beginTransaction()) {
            return conn.createQuery("SELECT EXISTS(SELECT TRUE FROM users WHERE account=:account)")
                    .addParameter("account", str)
                    .executeAndFetchFirst(Boolean.class);
        }
    }

    public static boolean isRecordExist(String recordId) {
        try (Connection conn = FrameworkConfig.SQL2O.beginTransaction()) {
            return conn.createQuery("SELECT EXISTS(SELECT TRUE FROM record WHERE id=:id)")
                    .addParameter("id", UUID.fromString(recordId))
                    .executeAndFetchFirst(Boolean.class);
        }
    }

    public static boolean isRecordExistAndAuthed(String recordId, String userId) {
        try (Connection conn = FrameworkConfig.SQL2O.beginTransaction()) {
            return conn.createQuery("SELECT EXISTS(SELECT TRUE FROM record WHERE id=:id AND userId=:userId)")
                    .addParameter("id", UUID.fromString(recordId))
                    .addParameter("userId", UUID.fromString(userId))
                    .executeAndFetchFirst(Boolean.class);
        }
    }

    public static boolean isRecordExistInRecordSheet(String bookId, String recordId) {
        try (Connection conn = FrameworkConfig.SQL2O.beginTransaction()) {
            return conn.createQuery("SELECT EXISTS(SELECT TRUE FROM recordSheet WHERE bookId=:bookId AND recordId=:recordId)")
                    .addParameter("bookId", UUID.fromString(bookId))
                    .addParameter("recordId", UUID.fromString(recordId))
                    .executeAndFetchFirst(Boolean.class);
        }
    }

    public static boolean isBookExist(String bookId) {
        try (Connection conn = FrameworkConfig.SQL2O.beginTransaction()) {
            return conn.createQuery("SELECT EXISTS(SELECT TRUE FROM book WHERE id=:id)")
                    .addParameter("id", UUID.fromString(bookId))
                    .executeAndFetchFirst(Boolean.class);
        }
    }

    public static boolean isBookExistAndAuthed(String bookId, String userId) {
        try (Connection conn = FrameworkConfig.SQL2O.beginTransaction()) {
            return conn.createQuery("SELECT EXISTS(SELECT TRUE FROM bookAuth WHERE bookId=:bookId AND ownerId=:userId)")
                    .addParameter("bookId", UUID.fromString(bookId))
                    .addParameter("userId", UUID.fromString(userId))
                    .executeAndFetchFirst(Boolean.class);
        }
    }

    public static boolean isBookExistAndCanWrite(String bookId, String userId) {
        try (Connection conn = FrameworkConfig.SQL2O.beginTransaction()) {
            return conn.createQuery("SELECT EXISTS(SELECT TRUE FROM bookAuth WHERE " +
                    "bookId=:bookId AND ownerId=:userId AND ownerAuth>:auth)")
                    .addParameter("bookId", UUID.fromString(bookId))
                    .addParameter("userId", UUID.fromString(userId))
                    .addParameter("auth", FrameworkConfig.BOOK_OWNER_AUTH_READ_ONLY)
                    .executeAndFetchFirst(Boolean.class);
        }
    }

    public static boolean isBookExistAndRooted(String bookId, String userId) {
        try (Connection conn = FrameworkConfig.SQL2O.beginTransaction()) {
            return conn.createQuery("SELECT EXISTS(SELECT TRUE FROM bookAuth WHERE" +
                    " bookId=:bookId AND ownerId=:userId AND ownerAuth=:ownerAuth)")
                    .addParameter("bookId", UUID.fromString(bookId))
                    .addParameter("userId", UUID.fromString(userId))
                    .addParameter("ownerAuth", FrameworkConfig.BOOK_OWNER_AUTH_ROOT)
                    .executeAndFetchFirst(Boolean.class);
        }
    }

    public static boolean isValidRecordSource(String source) {
        return source.equals(FrameworkConfig.RECORD_SOURCE_USER_MANUAL) ||
                source.equals(FrameworkConfig.RECORD_SOURCE_USER_PARAGRAPH) ||
                source.equals(FrameworkConfig.RECORD_SOURCE_USER_SOUND);
    }

    public static User userWithAccount(String account) {
        try (Connection conn = FrameworkConfig.SQL2O.beginTransaction()) {
            return conn.createQuery("SELECT id, password, salt FROM users WHERE account=:account LIMIT 1")
                    .addParameter("account", account)
                    .executeAndFetchFirst(User.class);
        }
    }

    public static String userIdWithAccessToken(String accessToken) {
        return Cache.checkAccessToken(accessToken);
    }

    public static String userIdWithRefreshToken(String refreshToken) {
        return Cache.checkRefreshToken(refreshToken);
    }

    public static boolean isValidNickname(String str) {
        return isValidStringLength(str, FrameworkConfig.USER_NICKNAME_MIN_LENGTH, FrameworkConfig.USER_NICKNAME_MAX_LENGTH);
    }

    public static boolean isValidAccount(String str) {
        return isValidStringLength(str, FrameworkConfig.USER_ACCOUNT_MIN_LENGTH, FrameworkConfig.USER_ACCOUNT_MAX_LENGTH);
    }

    public static Date dateValidDateTime(String str) {
        if (str == null) {
            return null;
        }
        try {
            return SimpleDateFormat.getDateTimeInstance().parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean isNotNull(Object... args) {
        for (Object i : args) {
            if (i == null) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidDateTime(Date date) {
        return date.compareTo(new Date()) == -1;
    }

    public static boolean isAllValidDateTime(Date... args) {
        for (Date i : args) {
            if (!isValidDateTime(i)) {
                return false;
            }
        }
        return true;
    }

}

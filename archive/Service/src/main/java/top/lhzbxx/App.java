package top.lhzbxx;

/**
 * Author: LuHao
 * Date: 16/5/11
 * Mail: lhzbxx@gmail.com
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.eclipse.jetty.server.Response;
import redis.clients.jedis.Jedis;
import top.lhzbxx.base.Model;
import top.lhzbxx.base.Sql2oModel;
import top.lhzbxx.config.DeployConfig;
import top.lhzbxx.config.FrameworkConfig;
import top.lhzbxx.config.MessageConfig;
import top.lhzbxx.config.UrlConfig;
import top.lhzbxx.model.Book;
import top.lhzbxx.model.Record;
import top.lhzbxx.model.RecordSheet;
import top.lhzbxx.payload.*;
import top.lhzbxx.util.Common;
import top.lhzbxx.util.Validator;

import javax.servlet.MultipartConfigElement;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;

import static spark.Spark.*;
import static spark.route.RouteOverview.enableRouteOverview;
import static top.lhzbxx.config.FrameworkConfig.REDIS_POOL;

public class App {
    public static void main(String[] args) {

//        secure(DeployConfig.KEYSTORE_LOCATION, DeployConfig.KEYSTORE_PASSWORD, null, null);

        enableRouteOverview(UrlConfig.UTIL_OVERVIEW);

        Model model = new Sql2oModel(FrameworkConfig.SQL2O);

        Gson gson = new Gson();

        // 针对最小兼容的版本号检查
        before("*", (request, response) -> {
            String version = request.headers("Version");
            if (version == null || DeployConfig.MIN_COMPATIBLE_VERSION > Integer.parseInt(version)) {
                halt(Response.SC_FORBIDDEN, MessageConfig.INVALID_VERSION);
            }
        });

        // 权限验证
        before("/auth/*", (request, response) -> {
            String accessToken = request.headers(FrameworkConfig.ACCESS_TOKEN_NAME);
            if (accessToken == null) {
                halt(Response.SC_PRECONDITION_FAILED, MessageConfig.LACK_OF_ACCESS_TOKEN);
            }
            String userId = Validator.userIdWithAccessToken(accessToken);
            if (userId == null) {
                halt(Response.SC_UNAUTHORIZED, MessageConfig.INVALID_ACCESS_TOKEN);
            }
            request.attribute("userId", userId);
        });

        get("/", (request, response) -> "YoTv API");

        // 注册
        post(UrlConfig.USER, (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            UserPayload payload = mapper.readValue(request.body(), UserPayload.class);
            if (!payload.isValid()) {
                halt(payload.getStatusCode(), payload.getMessage());
            }
            String userId = model.createUser(payload.getAccount(), payload.getPassword(), payload.getNickname());
            return model.authorizeUser(userId);
        });

        // 登录
        post(UrlConfig.USER_AUTH, (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            AuthPayload payload = mapper.readValue(request.body(), AuthPayload.class);
            if (!payload.isValid()) {
                halt(payload.getStatusCode(), payload.getMessage());
            }
            return model.authorizeUser(payload.getUserId());
        });

        // 刷新
        patch(UrlConfig.USER_AUTH, (request, response) -> {
            String refreshToken = request.headers(FrameworkConfig.REFRESH_TOKEN_NAME);
            if (refreshToken == null) {
                halt(Response.SC_BAD_REQUEST, MessageConfig.LACK_OF_REFRESH_TOKEN);
            }
            String userId = Validator.userIdWithRefreshToken(refreshToken);
            if (userId == null) {
                halt(Response.SC_BAD_REQUEST, MessageConfig.INVALID_REFRESH_TOKEN);
            }
            return model.authorizeUser(userId);
        });

        // 修改头像
        put(UrlConfig.ALLOW_ONLY_AUTH(UrlConfig.USER_INFO_AVATAR), (request, response) -> {
            String userId = request.attribute("userId");
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement(""));
            String fileName = request.raw().getPart("file").getName();
            try (InputStream is = request.raw().getPart("file").getInputStream()) {
                OutputStream os = new FileOutputStream(DeployConfig.UPLOAD_LOCATION + "/" + fileName);
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                is.close();
            }
            return "Success!";
        });

        // 添加新收支
        post(UrlConfig.ALLOW_ONLY_AUTH(UrlConfig.RECORD), (request, response) -> {
            String userId = request.attribute("userId");
            ObjectMapper mapper = new ObjectMapper();
            RecordPayload payload = mapper.readValue(request.body(), RecordPayload.class);
            payload.setIsAdd(true);
            if (!payload.isValid()) {
                halt(payload.getStatusCode(), payload.getMessage());
            }
            Record record = payload.generateNewRecord();
            return model.createRecord(record, userId);
        }, gson::toJson);

        // 查看某收支
        get(UrlConfig.ALLOW_ONLY_AUTH(UrlConfig.RECORD_ID), (request, response) -> {
            String userId = request.attribute("userId");
            String recordId = request.params(":id");
            if (!Validator.isRecordExist(recordId)) {
                halt(Response.SC_NOT_FOUND, MessageConfig.INVALID_RECORD_ID);
            }
            return model.readRecord(recordId);
        }, gson::toJson);

        // 修改旧收支
        put(UrlConfig.ALLOW_ONLY_AUTH(UrlConfig.RECORD_ID), (request, response) -> {
            String userId = request.attribute("userId");
            String recordId = request.params(":id");
            ObjectMapper mapper = new ObjectMapper();
            RecordPayload payload = mapper.readValue(request.body(), RecordPayload.class);
            payload.setIsAdd(false);
            payload.setId(recordId);
            payload.setUserId(userId);
            if (!payload.isValid()) {
                halt(payload.getStatusCode(), payload.getMessage());
            }
            Record record = payload.generateNewRecord();
            return model.updateRecord(record, userId);
        }, gson::toJson);

        // 删除旧收支
        delete(UrlConfig.ALLOW_ONLY_AUTH(UrlConfig.RECORD_ID), (request, response) -> {
            String userId = request.attribute("userId");
            String recordId = request.params(":id");
            if (!Validator.isRecordExistAndAuthed(recordId, userId)) {
                halt(Response.SC_FORBIDDEN, MessageConfig.INVALID_RECORD_ID);
            }
            model.deleteRecord(recordId);
            return response;
        });

        // 添加新账本
        post(UrlConfig.ALLOW_ONLY_AUTH(UrlConfig.BOOK), (request, response) -> {
            String userId = request.attribute("userId");
            ObjectMapper mapper = new ObjectMapper();
            BookPayload payload = mapper.readValue(request.body(), BookPayload.class);
            payload.setIsAdd(true);
            if (!payload.isValid()) {
                halt(payload.getStatusCode(), payload.getMessage());
            }
            Book book = payload.generateNewBook();
            return model.createBook(book, userId);
        }, gson::toJson);

        // 查看所有授权账本
        get(UrlConfig.ALLOW_ONLY_AUTH(UrlConfig.BOOK), (request, response) -> {
            String userId = request.attribute("userId");
            return model.readBooks(userId);
        }, gson::toJson);

        // 查看某账本
        get(UrlConfig.ALLOW_ONLY_AUTH(UrlConfig.BOOK_ID), (request, response) -> {
            String userId = request.attribute("userId");
            String bookId = request.params(":id");
            if (!Validator.isBookExist(bookId)) {
                halt(Response.SC_NOT_FOUND, MessageConfig.INVALID_BOOK_ID);
            }
            return model.readBook(bookId);
        }, gson::toJson);

        // 向账本添加收支
        post(UrlConfig.ALLOW_ONLY_AUTH(UrlConfig.BOOK_ID1_RECORD_ID2), (request, response) -> {
            String userId = request.attribute("userId");
            String bookId = request.params(":id1");
            String recordId = request.params(":id2");
            ObjectMapper mapper = new ObjectMapper();
            RecordSheetPayload payload = mapper.readValue(request.body(), RecordSheetPayload.class);
            payload.setUserId(userId);
            payload.setBookId(bookId);
            payload.setRecordId(recordId);
            if (!payload.isValid()) {
                halt(payload.getStatusCode(), payload.getMessage());
            }
            return model.createRecordSheetEntry(payload.generateNewRecordSheetEntry());
        }, gson::toJson);

        // 向账本删除收支
        delete(UrlConfig.ALLOW_ONLY_AUTH(UrlConfig.BOOK_ID1_RECORD_ID2), (request, response) -> {
            String userId = request.attribute("userId");
            String bookId = request.params(":id1");
            String recordId = request.params(":id2");
            if (!Validator.isValidUUID(bookId) || !Validator.isValidUUID(recordId)) {
                halt(Response.SC_FORBIDDEN, MessageConfig.LACK_OF_PARAMETER);
            }
            if (!Validator.isBookExistAndCanWrite(bookId, userId)) {
                halt(Response.SC_FORBIDDEN, MessageConfig.INVALID_BOOK_ID);
            }
            if (!Validator.isRecordExistInRecordSheet(bookId, recordId)) {
                halt(Response.SC_FORBIDDEN, MessageConfig.INVALID_RECORD_ID);
            }
            model.deleteRecordSheetEntry(bookId, recordId);
            return "";
        });

        // 修改旧账本
        put(UrlConfig.ALLOW_ONLY_AUTH(UrlConfig.BOOK_ID), (request, response) -> {
            String userId = request.attribute("userId");
            String bookId = request.params(":id");
            ObjectMapper mapper = new ObjectMapper();
            BookPayload payload = mapper.readValue(request.body(), BookPayload.class);
            payload.setIsAdd(false);
            payload.setId(bookId);
            payload.setUserId(userId);
            if (!payload.isValid()) {
                halt(payload.getStatusCode(), payload.getMessage());
            }
            Book book = payload.generateNewBook();
            return model.updateBook(book, userId);
        }, gson::toJson);

        // 删除旧账本
        delete(UrlConfig.ALLOW_ONLY_AUTH(UrlConfig.BOOK_ID), (request, response) -> {
            String userId = request.attribute("userId");
            String bookId = request.params(":id");
            if (!Validator.isBookExistAndRooted(bookId, userId)) {
                halt(Response.SC_FORBIDDEN, MessageConfig.INVALID_BOOK_ID);
            }
            model.deleteBook(bookId);
            return response;
        });

        // Util可用列表
        get(UrlConfig.UTIL, (request, response) -> {
            List<String> paths = new ArrayList<>();
            for (Field util : UrlConfig.class.getDeclaredFields()) {
                String path = util.getName();
                if (path.startsWith("UTIL_")) {
                    paths.add(path);
                }
            }
            return paths;
        });

        // 当前时间
        get(UrlConfig.UTIL_NOW, (request, response) -> {
            Map<String, String> time = new HashMap<>();
            Date date = new Date();
            time.put("DateTime", date.toString());
            time.put("Timestamp", Long.toString(date.getTime()));
            return time;
        }, gson::toJson);

        // 随机UUID
        get(UrlConfig.UTIL_UUID, (request, response) -> UUID.randomUUID());

        // 随机字符串
        get(UrlConfig.UTIL_SALT, (request, response) -> {
            int length = Integer.parseInt(request.queryParams("length"));
            return Common.generateSalt(length);
        });

        // SHA1加密
        get(UrlConfig.UTIL_ENCRYPT_SHA1, (request, response) ->
                Common.encryptSHA1(request.queryParams("cipher")));

        // 读取Value
        get(UrlConfig.UTIL_REDIS, (request, response) -> {
            try (Jedis jedis = REDIS_POOL.getResource()) {
                String value = jedis.get(request.queryParams("key"));
                if (value == null) {
                    halt(Response.SC_BAD_REQUEST, MessageConfig.VALUE_NOT_EXIST);
                }
                return value;
            }
        });

        // 设置Key
        post(UrlConfig.UTIL_REDIS, (request, response) -> {
            try (Jedis jedis = REDIS_POOL.getResource()) {
                jedis.set(request.queryParams("key"), request.queryParams("value"));
                response.status(201);
                return "";
            }
        });

        // 清空缓存
        delete(UrlConfig.UTIL_REDIS, (request, response) -> {
            try (Jedis jedis = REDIS_POOL.getResource()) {
                Set<String> keys = jedis.keys("YoTv:*");
                for (String key : keys) {
                    jedis.del(key);
                }
                response.status(204);
                return "";
            }
        });

        // 随机编号
        get(UrlConfig.UTIL_SERIAL, (request, response) -> {
            int length = Integer.parseInt(request.queryParams("length"));
            return Common.generateSerial(length);
        });

//        exception(PSQLException.class, (exception, request, response) -> {
//            halt(Response.SC_EXPECTATION_FAILED, exception.toString());
//        });

//        after((request, response) -> {
//            System.out.print(response.raw().getStatus());
//            if (response.raw().getStatus() == 404) {
//                System.out.print(response.raw().getStatus());
//            }
//        });

    }
}
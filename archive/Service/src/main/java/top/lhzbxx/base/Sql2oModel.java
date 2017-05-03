package top.lhzbxx.base;

import com.google.gson.Gson;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import top.lhzbxx.config.FrameworkConfig;
import top.lhzbxx.model.Book;
import top.lhzbxx.model.Record;
import top.lhzbxx.model.RecordSheet;
import top.lhzbxx.util.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Author: LuHao
 * Date: 16/5/27
 * Mail: lhzbxx@gmail.com
 */

public class Sql2oModel implements Model {

    private Sql2o sql2o;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public String createUser(String account, String password, String nickname) {
        try (Connection conn = this.sql2o.beginTransaction()) {
            UUID id = UUID.randomUUID();
            String salt = Common.generateSalt(FrameworkConfig.SALT_LENGTH);
            password = Common.encryptSHA1(password + salt);
            conn.createQuery("INSERT INTO users(id, account, password, salt) VALUES (:id, :account, :password, :salt)")
                    .addParameter("id", id)
                    .addParameter("account", account)
                    .addParameter("password", password)
                    .addParameter("salt", salt)
                    .executeUpdate();
            conn.createQuery("INSERT INTO userInfo(userId, nickname, serial) VALUES (:id, :nickname, :serial)")
                    .addParameter("id", id)
                    .addParameter("nickname", nickname)
                    .addParameter("serial", Common.generateSerial(FrameworkConfig.USER_SERIAL_LENGTH))
                    .executeUpdate();
            conn.commit();
            AuthToken token = new AuthToken(id.toString());
            return new Gson().toJson(token);
        }
    }

    @Override
    public String authorizeUser(String userId) {
        AuthToken token = new AuthToken(userId);
        return new Gson().toJson(token);
    }

    @Override
    public Record createRecord(Record record, String userId) {
        try (Connection conn = this.sql2o.beginTransaction()) {
            conn.createQuery("INSERT INTO record(id, userId, place, type, amount, attachment, isRegular," +
                    " regularRule, source, comment, isReimbursed, happenAt, createAt, updateAt)" +
                    " VALUES (:id, :userId, :place, :type, :amount, :attachment, :isRegular," +
                    " :regularRule, :source, :comment, :isReimbursed, :happenAt, :createAt, :updateAt)")
                    .addParameter("id", record.getId())
                    .addParameter("userId", UUID.fromString(userId))
                    .addParameter("place", record.getPlace())
                    .addParameter("type", record.getType())
                    .addParameter("amount", record.getAmount())
                    .addParameter("attachment", record.getAttachment())
                    .addParameter("isRegular", record.isRegular())
                    .addParameter("regularRule", record.getComment())
                    .addParameter("source", record.getSource())
                    .addParameter("comment", record.getComment())
                    .addParameter("isReimbursed", record.isReimbursed())
                    .addParameter("happenAt", record.getHappenAt())
                    .addParameter("createAt", record.getCreateAt())
                    .addParameter("updateAt", record.getUpdateAt())
                    .executeUpdate();
            conn.commit();
            return record;
        }
    }

    @Override
    public Record updateRecord(Record record, String userId) {
        try (Connection conn = this.sql2o.beginTransaction()) {
            conn.createQuery("UPDATE record SET(userId, place, type, amount, attachment, isRegular," +
                    " regularRule, source, comment, isReimbursed, happenAt, createAt, updateAt)" +
                    " = (:userId, :place, :type, :amount, :attachment, :isRegular," +
                    " :regularRule, :source, :comment, :isReimbursed, :happenAt, :createAt, :updateAt)" +
                    " WHERE id=:id")
                    .addParameter("userId", UUID.fromString(userId))
                    .addParameter("place", record.getPlace())
                    .addParameter("type", record.getType())
                    .addParameter("amount", record.getAmount())
                    .addParameter("attachment", record.getAttachment())
                    .addParameter("isRegular", record.isRegular())
                    .addParameter("regularRule", record.getComment())
                    .addParameter("source", record.getSource())
                    .addParameter("comment", record.getComment())
                    .addParameter("isReimbursed", record.isReimbursed())
                    .addParameter("happenAt", record.getHappenAt())
                    .addParameter("createAt", record.getCreateAt())
                    .addParameter("updateAt", record.getUpdateAt())
                    .addParameter("id", record.getId())
                    .executeUpdate();
            conn.commit();
            return record;
        }
    }

    @Override
    public Record readRecord(String recordId) {
        try (Connection conn = this.sql2o.beginTransaction()) {
            return conn.createQuery("SELECT * FROM record WHERE id=:id")
                    .addParameter("id", UUID.fromString(recordId))
                    .executeAndFetchFirst(Record.class);
        }
    }

    @Override
    public void deleteRecord(String recordId) {
        try (Connection conn = this.sql2o.beginTransaction()) {
            conn.createQuery("DELETE FROM record WHERE id=:id")
                    .addParameter("id", UUID.fromString(recordId))
                    .executeUpdate();
            conn.commit();
        }
    }

    @Override
    public Book createBook(Book book, String userId) {
        try (Connection conn = this.sql2o.beginTransaction()) {
            conn.createQuery("INSERT INTO book(id, userId, name, cover, isPublic, saveTarget, createAt, updateAt)" +
                    " VALUES (:id, :userId, :name, :cover, :isPublic, :saveTarget, :createAt, :updateAt)")
                    .addParameter("id", book.getId())
                    .addParameter("userId", UUID.fromString(userId))
                    .addParameter("name", book.getName())
                    .addParameter("cover", book.getCover())
                    .addParameter("isPublic", book.isPublic())
                    .addParameter("saveTarget", book.getSaveTarget())
                    .addParameter("createAt", book.getCreateAt())
                    .addParameter("updateAt", book.getUpdateAt())
                    .executeUpdate();
            conn.createQuery("INSERT INTO bookAuth(bookId, ownerId, ownerType, ownerAuth)" +
                    " VALUES (:bookId, :ownerId, :ownerType, :ownerAuth)")
                    .addParameter("bookId", book.getId())
                    .addParameter("ownerId", UUID.fromString(userId))
                    .addParameter("ownerType", FrameworkConfig.BOOK_OWNER_TYPE_USER)
                    .addParameter("ownerAuth", FrameworkConfig.BOOK_OWNER_AUTH_ROOT)
                    .executeUpdate();
            conn.commit();
            return book;
        }
    }

    @Override
    public Book updateBook(Book book, String userId) {
        try (Connection conn = this.sql2o.beginTransaction()) {
            conn.createQuery("UPDATE book SET(userId, name, cover, isPublic, saveTarget, createAt, updateAt)" +
                    " = (:userId, :name, :cover, :isPublic, :saveTarget, :createAt, :updateAt)" +
                    " WHERE id=:id")
                    .addParameter("userId", UUID.fromString(userId))
                    .addParameter("name", book.getName())
                    .addParameter("cover", book.getCover())
                    .addParameter("isPublic", book.isPublic())
                    .addParameter("saveTarget", book.getSaveTarget())
                    .addParameter("createAt", book.getCreateAt())
                    .addParameter("updateAt", book.getUpdateAt())
                    .addParameter("id", book.getId())
                    .executeUpdate();
            conn.commit();
            return book;
        }
    }

    @Override
    public Book readBook(String bookId) {
        try (Connection conn = this.sql2o.beginTransaction()) {
            return conn.createQuery("SELECT * FROM book WHERE id=:id")
                    .addParameter("id", UUID.fromString(bookId))
                    .executeAndFetchFirst(Book.class);
        }
    }

    @Override
    public List<Book> readBooks(String userId) {
        try (Connection conn = this.sql2o.beginTransaction()) {
            // todo: 加入群组后的Join。
            return conn.createQuery("SELECT book.id, book.userId, book.name, book.cover, book.isPublic," +
                    " book.saveTarget, book.createAt, book.updateAt FROM book JOIN bookAuth ON book.id=bookAuth.bookId" +
                    " WHERE bookAuth.ownerId=:id")
                    .addParameter("id", UUID.fromString(userId))
                    .executeAndFetch(Book.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteBook(String bookId) {
        try (Connection conn = this.sql2o.beginTransaction()) {
            conn.createQuery("DELETE FROM book WHERE id=:id")
                    .addParameter("id", UUID.fromString(bookId))
                    .executeUpdate();
            conn.createQuery("DELETE FROM bookAuth WHERE bookId=:id")
                    .addParameter("id", UUID.fromString(bookId))
                    .executeUpdate();
            conn.commit();
        }
    }

    @Override
    public RecordSheet createRecordSheetEntry(RecordSheet recordSheet) {
        try (Connection conn = this.sql2o.beginTransaction()) {
            conn.createQuery("INSERT INTO recordSheet(userId, recordId, bookId, createAt)" +
                    " VALUES (:userId, :recordId, :bookId, :createAt)")
                    .addParameter("userId", recordSheet.getUserId())
                    .addParameter("recordId", recordSheet.getRecordId())
                    .addParameter("bookId", recordSheet.getBookId())
                    .addParameter("createAt", recordSheet.getCreateAt())
                    .executeUpdate();
            conn.commit();
            return recordSheet;
        }
    }

    @Override
    public void deleteRecordSheetEntry(String bookId, String recordId) {
        try (Connection conn = this.sql2o.beginTransaction()) {
            conn.createQuery("DELETE FROM recordSheet WHERE recordId=:recordId AND bookId=:bookId")
                    .addParameter("recordId", UUID.fromString(recordId))
                    .addParameter("bookId", UUID.fromString(bookId))
                    .executeUpdate();
            conn.commit();
        }
    }

}

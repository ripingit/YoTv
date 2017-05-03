package top.lhzbxx.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.eclipse.jetty.server.Response;
import top.lhzbxx.config.MessageConfig;
import top.lhzbxx.model.Book;
import top.lhzbxx.util.Validator;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: LuHao
 * Date: 16/5/30
 * Mail: lhzbxx@gmail.com
 */

@Data
public class BookPayload implements Payload {

    @JsonIgnore
    private String id;
    @JsonIgnore
    private Boolean isAdd;
    @JsonIgnore
    private String userId;
    private String name;
    private String cover;
    private String isPublic;
    private String saveTarget;
    private String createAt;
    private String updateAt;
    @JsonIgnore
    private String message;
    @JsonIgnore
    private int statusCode;

    public boolean isValid() {
        if (!Validator.isNotNull(isAdd, name, isPublic, saveTarget, createAt, updateAt)) {
            message = MessageConfig.LACK_OF_PARAMETER;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (!isAdd) {
            if (!Validator.isNotNull(id) || !Validator.isValidUUID(id)) {
                message = MessageConfig.LACK_OF_PARAMETER;
                statusCode = Response.SC_FORBIDDEN;
                return false;
            }
        }
        if (!Validator.isNotNull(Validator.dateValidDateTime(createAt),
                Validator.dateValidDateTime(updateAt))) {
            message = MessageConfig.INVALID_DATE_FORMAT;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (!Validator.isAllValidDateTime(Validator.dateValidDateTime(createAt),
                Validator.dateValidDateTime(updateAt))) {
            message = MessageConfig.INVALID_DATE_TIME;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (!isAdd) {
            if (!Validator.isBookExistAndAuthed(id, userId)) {
                message = MessageConfig.INVALID_RECORD_ID;
                statusCode = Response.SC_FORBIDDEN;
                return false;
            }
        }
        return true;
    }

    public Book generateNewBook() {
        Book book = new Book();
        if (isAdd) {
            book.setId(UUID.randomUUID());
        } else {
            book.setId(UUID.fromString(id));
        }
        book.setName(name);
        book.setCover(cover);
        book.setPublic(Boolean.parseBoolean(isPublic));
        book.setSaveTarget(new BigDecimal(saveTarget));
        book.setCreateAt(Validator.dateValidDateTime(createAt));
        book.setUpdateAt(Validator.dateValidDateTime(updateAt));
        return book;
    }

}

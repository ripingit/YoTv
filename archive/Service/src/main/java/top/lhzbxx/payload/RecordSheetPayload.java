package top.lhzbxx.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.eclipse.jetty.server.Response;
import top.lhzbxx.config.MessageConfig;
import top.lhzbxx.model.RecordSheet;
import top.lhzbxx.util.Validator;

import java.util.Date;
import java.util.UUID;

/**
 * Author: LuHao
 * Date: 16/6/7
 * Mail: lhzbxx@gmail.com
 */

@Data
public class RecordSheetPayload implements Payload {

    @JsonIgnore
    private String userId;
    @JsonIgnore
    private String recordId;
    @JsonIgnore
    private String bookId;
    private String createAt;
    @JsonIgnore
    private String message;
    @JsonIgnore
    private int statusCode;

    public boolean isValid() {
        if (!Validator.isNotNull(userId, recordId, bookId, createAt)) {
            message = MessageConfig.LACK_OF_PARAMETER;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (!Validator.isValidUUID(bookId) || !Validator.isValidUUID(recordId)) {
            message = MessageConfig.LACK_OF_PARAMETER;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        Date date = Validator.dateValidDateTime(createAt);
        if (!Validator.isNotNull(date)) {
            message = MessageConfig.INVALID_DATE_FORMAT;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (!Validator.isAllValidDateTime(date)) {
            message = MessageConfig.INVALID_DATE_TIME;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (!Validator.isBookExistAndCanWrite(bookId, userId)) {
            message = MessageConfig.INVALID_BOOK_ID;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (!Validator.isRecordExist(recordId)) {
            message = MessageConfig.INVALID_RECORD_ID;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (Validator.isRecordExistInRecordSheet(bookId, recordId)) {
            message = MessageConfig.DUPLICATE_RECORD_IN_BOOK;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        return true;
    }

    public RecordSheet generateNewRecordSheetEntry() {
        RecordSheet recordEntry = new RecordSheet();
        recordEntry.setBookId(UUID.fromString(bookId));
        recordEntry.setUserId(UUID.fromString(userId));
        recordEntry.setRecordId(UUID.fromString(recordId));
        recordEntry.setCreateAt(Validator.dateValidDateTime(createAt));
        return recordEntry;
    }

}

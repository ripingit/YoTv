package top.lhzbxx.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.eclipse.jetty.server.Response;
import top.lhzbxx.config.MessageConfig;
import top.lhzbxx.model.Record;
import top.lhzbxx.util.Validator;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: LuHao
 * Date: 16/5/30
 * Mail: lhzbxx@gmail.com
 */

@Data
public class RecordPayload implements Payload {

    @JsonIgnore
    private String id;
    @JsonIgnore
    private Boolean isAdd;
    @JsonIgnore
    private String userId;
    private String amount;
    private String type;
    private String happenAt;
    private String place;
    private String attachment;
    private String isRegular;
    private String regularRule;
    private String source;
    private String comment;
    private String isReimbursed;
    private String createAt;
    private String updateAt;
    @JsonIgnore
    private String message;
    @JsonIgnore
    private int statusCode;

    public boolean isValid() {
        if (!Validator.isNotNull(isAdd, amount, type, source, happenAt, createAt, updateAt)) {
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
        if (!Validator.isValidRecordSource(source)) {
            message = MessageConfig.INVALID_RECORD_SOURCE;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (!Validator.isNotNull(Validator.dateValidDateTime(happenAt),
                Validator.dateValidDateTime(createAt),
                Validator.dateValidDateTime(updateAt))) {
            message = MessageConfig.INVALID_DATE_FORMAT;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (!Validator.isAllValidDateTime(Validator.dateValidDateTime(happenAt),
                Validator.dateValidDateTime(createAt),
                Validator.dateValidDateTime(updateAt))) {
            message = MessageConfig.INVALID_DATE_TIME;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (!isAdd) {
            if (!Validator.isRecordExistAndAuthed(id, userId)) {
                message = MessageConfig.INVALID_RECORD_ID;
                statusCode = Response.SC_FORBIDDEN;
                return false;
            }
        }
        return true;
    }

    public Record generateNewRecord() {
        Record record = new Record();
        if (isAdd) {
            record.setId(UUID.randomUUID());
        } else {
            record.setId(UUID.fromString(id));
        }
        record.setAmount(new BigDecimal(amount));
        record.setAttachment(attachment);
        record.setRegular(Boolean.parseBoolean(isRegular));
        record.setReimbursed(Boolean.parseBoolean(isReimbursed));
        record.setPlace(place);
        record.setRegularRule(regularRule);
        record.setType(type);
        record.setComment(comment);
        record.setHappenAt(Validator.dateValidDateTime(happenAt));
        record.setCreateAt(Validator.dateValidDateTime(createAt));
        record.setUpdateAt(Validator.dateValidDateTime(updateAt));
        return record;
    }

}

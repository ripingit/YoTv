package top.lhzbxx.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.eclipse.jetty.server.Response;
import top.lhzbxx.config.MessageConfig;
import top.lhzbxx.util.Validator;

/**
 * Author: LuHao
 * Date: 16/5/27
 * Mail: lhzbxx@gmail.com
 */

@Data
public class UserPayload implements Payload {

    private String account;
    private String password;
    private String nickname;
    @JsonIgnore
    private int statusCode;
    @JsonIgnore
    private String message;

    public boolean isValid() {
        if (!Validator.isNotNull(account, password, nickname)) {
            message = MessageConfig.LACK_OF_PARAMETER;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (!Validator.isValidAccount(account)) {
            message = MessageConfig.INVALID_USER_ACCOUNT_LENGTH;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (!Validator.isValidNickname(nickname)) {
            message = MessageConfig.INVALID_USER_NICKNAME;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (!Validator.isValidPassword(password)) {
            message = MessageConfig.INVALID_USER_PASSWORD;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        if (Validator.isUserExist(account)) {
            message = MessageConfig.DUPLICATE_USER_ACCOUNT;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        return true;
    }

}

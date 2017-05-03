package top.lhzbxx.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.eclipse.jetty.server.Response;
import top.lhzbxx.config.MessageConfig;
import top.lhzbxx.model.User;
import top.lhzbxx.util.Common;
import top.lhzbxx.util.Validator;

/**
 * Author: LuHao
 * Date: 16/5/27
 * Mail: lhzbxx@gmail.com
 */

@Data
public class AuthPayload implements Payload {

    private String account;
    private String password;
    private String userId;
    @JsonIgnore
    private int statusCode;
    @JsonIgnore
    private String message;

    public boolean isValid() {
        if (!Validator.isNotNull(account, password)) {
            message = MessageConfig.LACK_OF_PARAMETER;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        User user = Validator.userWithAccount(account);
        if (user == null || user.getPassword().equals(Common.encryptSHA1(password + user.getSalt()))) {
            message = MessageConfig.PASSWORD_NOT_CORRECT;
            statusCode = Response.SC_FORBIDDEN;
            return false;
        }
        userId  = user.getId().toString();
        return true;
    }

}

package top.lhzbxx.payload;

/**
 * Author: LuHao
 * Date: 16/5/27
 * Mail: lhzbxx@gmail.com
 */

public interface Payload {

    boolean isValid();

    String getMessage();

    int getStatusCode();

}

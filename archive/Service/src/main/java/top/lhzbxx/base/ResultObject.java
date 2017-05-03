package top.lhzbxx.base;

/**
 * Author: LuHao
 * Date: 16/5/24
 * Mail: lhzbxx@gmail.com
 */

public class ResultObject {

    private int status;
    private String message;
    private Object data;

    public ResultObject(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}

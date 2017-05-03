package top.lhzbxx.model;

import lombok.Data;
import spark.utils.StringUtils;

import java.util.UUID;

/**
 * Author: LuHao
 * Date: 16/5/12
 * Mail: lhzbxx@gmail.com
 */

@Data
public class User {
    UUID id;
    String account;
    String password;
    String salt;
}
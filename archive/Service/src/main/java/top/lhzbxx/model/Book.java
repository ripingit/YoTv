package top.lhzbxx.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Author: LuHao
 * Date: 16/5/12
 * Mail: lhzbxx@gmail.com
 */

@Data
public class Book {
    UUID id;
    UUID userId;
    String name;
    String cover;
    boolean isPublic;
    BigDecimal saveTarget;
    Date updateAt;
    Date createAt;
}

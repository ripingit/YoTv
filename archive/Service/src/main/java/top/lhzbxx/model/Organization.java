package top.lhzbxx.model;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * Author: LuHao
 * Date: 16/5/27
 * Mail: lhzbxx@gmail.com
 */

@Data
public class Organization {
    UUID id;
    String avatar;
    String name;
    UUID userId;
    Date createAt;
    Date updateAt;
}

package top.lhzbxx.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Author: LuHao
 * Date: 16/6/7
 * Mail: lhzbxx@gmail.com
 */

@Data
public class RecordSheet {

    UUID userId;
    UUID recordId;
    UUID bookId;
    Date createAt;

}

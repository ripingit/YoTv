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
public class Record {

    UUID id;
    UUID userId;
    BigDecimal amount;
    String place;
    String type;
    String attachment;
    boolean isRegular;
    String regularRule;
    String source;
    String comment;
    boolean isReimbursed;
    Date happenAt;
    Date createAt;
    Date updateAt;

    public int incomeOrExpense() {
        return amount.compareTo(new BigDecimal(0));
    }

}

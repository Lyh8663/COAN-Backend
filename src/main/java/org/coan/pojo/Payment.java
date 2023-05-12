package org.coan.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("tb_payment")
public class Payment {

    private long offerId;

    /**
     * 交易方式，若为nft，game property则对应名称
     */
    private String currency;

    private double amount;

    private int type;


}

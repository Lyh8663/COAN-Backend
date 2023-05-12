package org.coan.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_deal")
public class CoanTrade {

    private Long id;
    private Long seller;
    private Long buyer;
    // 使用0表示交易中，1表示交易完成
    private int state;
    private int tgtType;
    private int srcType;
    private Double srcAmount;
    private Double tgtAmount;
    /**
     * 卖家商品
     */
    private String source;

    /**
     * 买家
     */
    private String target;
    private Timestamp createTime;
    private Timestamp updateTime;
}


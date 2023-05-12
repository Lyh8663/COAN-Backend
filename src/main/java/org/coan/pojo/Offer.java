package org.coan.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@TableName("tb_offer")
public class Offer {

    @TableId("id")
    private Long id;

    private long publisher;

    private int offerType;

    private String title;

    private int itemType;

    /**
     * 表示交易物
     */
    private String item;

    private double amount;


    private int cashSupport;

    private double value;

    private String description;

    private Timestamp createTime;

    private Timestamp updateTime;


}

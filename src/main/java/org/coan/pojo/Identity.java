package org.coan.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@TableName("tb_identity")
public class Identity {
    @TableId("id")
    private Long id;

    private String username;

    private String idNumber;

    private double credit;

    private String token;

    private double cash;

    /**
     * 0:禁用 1:正常
     */
    private int status;

    private String briefIntroduction;


    private Timestamp createTime;

    private Timestamp updateTime;
    private int historyQuota;
    private int historyTrade;
    private int currentQuota;
    private int currentTrade;


}

package org.coan.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_coin_record")
public class CoinRecord {
    @TableId("record_id")
    private String recordId;
    private String project;
    private String changePercentage;
    private String classification;
    private String classificationId;
    private int currencyId;
    private double dayHigh;
    private double dayLow;
    private double flowTotal;
    private String fullName;
    private String fullNameSeo;
    private String icon;
    private double last;
    private long marketCap;
    private String newFlag;
    private double open;
    private double openUtc0;
    private double openUtc8;
    private String quoteCurrencySymbol;
    private String symbol;
    private Timestamp time;

}

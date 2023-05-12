package org.coan.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("tb_coin")
public class Coin {
    /**
     * 币种
     */
    @TableId("project")
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
    private long volume;


}

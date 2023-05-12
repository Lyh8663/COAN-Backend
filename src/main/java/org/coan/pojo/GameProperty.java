package org.coan.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("tb_game_property")
public class GameProperty {

    private String name;

    private long owner;

    private double amount;
}

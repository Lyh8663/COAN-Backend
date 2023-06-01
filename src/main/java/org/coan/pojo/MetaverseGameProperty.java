package org.coan.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author Nebula
 * @Description 元宇宙游戏资产
 * @date 2023/5/21 19:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_metaverse_game_property_user")
@ToString
public class MetaverseGameProperty {
    Integer id;
    Integer propertyId;
    Long ownerId;
    BigDecimal amount;
    String name;
}

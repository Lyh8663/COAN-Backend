package org.coan.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

/**
 * @author Nebula
 * @Description 普通游戏资产
 * @date 2023/5/21 19:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_normal_game_property_user")
@ToString
public class NormalGameProperty {
    Integer id;
    Integer propertyId;
    Long ownerId;
    BigDecimal amount;
}

package org.coan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Nebula
 * @Description TODO
 * @date 2023/6/1 20:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_exchange_request")
@ToString
public class ExchangeRequest {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;
    Integer propertyIdSend;
    Integer propertyIdReceive;
    Long senderId;
    Long receiverId;
    Integer type;
    Integer isSuccess;
    String comment;

    public ExchangeRequest(Integer propertyIdSend, Integer propertyIdReceive, Long senderId, Long receiverId, Integer type, Integer isSuccess, String comment) {
        this.propertyIdSend = propertyIdSend;
        this.propertyIdReceive = propertyIdReceive;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.type = type;
        this.isSuccess = isSuccess;
        this.comment = comment;
    }
}

package org.coan.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
public class CoanUser {

    private Long id;
    private String username;
    private String phone;
    private String email;
    private String realName;
    private String idCode;
    private int idType;
    private String registTime;

}

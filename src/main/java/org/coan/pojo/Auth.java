package org.coan.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Auth {

    private Long userId;
    private int idType;
    private String idCode;
    private String realName;
}

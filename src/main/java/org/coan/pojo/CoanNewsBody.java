package org.coan.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoanNewsBody {

    private Long id;
    private String provider;
    private Timestamp date;
    private String description;
    private String articleUrl;
    private String title;
    private String content;

}


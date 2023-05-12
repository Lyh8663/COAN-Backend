package org.coan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.coan.pojo.CoanNewsContent;
import org.coan.pojo.CoanTrade;
import org.coan.pojo.CoanUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NewsTest {
    @Autowired
    private NewsContentService newsService;

    @Test
    public void testGetAll() {
        CoanNewsContent NewsCon= newsService.getContentByUrl("/news/cryptocurrency-news/article-2134505");
        System.out.println(NewsCon);
    }

}

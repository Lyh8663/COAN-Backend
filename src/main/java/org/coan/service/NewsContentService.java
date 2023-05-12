package org.coan.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.coan.mapper.CoanNewsContentMapper;
import org.coan.pojo.CoanNewsContent;
import org.coan.pojo.CoanTrade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsContentService {

    @Autowired
    private CoanNewsContentMapper newsContentMapper;

    public CoanNewsContent getContentByUrl(String url) {
        LambdaQueryWrapper<CoanNewsContent> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CoanNewsContent::getUrl, url);
        return newsContentMapper.selectOne(lqw);
    }

    public void addNewsContent(CoanNewsContent coanNews) {
        newsContentMapper.insert(coanNews);
    }

    public CoanNewsContent deleteNewsContent(String url) {
        CoanNewsContent coanNews = new CoanNewsContent();
        coanNews.setUrl(url);
        System.out.println(url);
        newsContentMapper.deleteById(coanNews);
        return coanNews;
    }


}

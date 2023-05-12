package org.coan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.coan.mapper.CoanNewsMapper;
import org.coan.pojo.CoanNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    @Autowired
    private CoanNewsMapper newsMapper;

    public Page<CoanNews> getAllNewsPage(int pageNum, int pageSize) {
        QueryWrapper<CoanNews> qw = new QueryWrapper<>();
        qw.lambda().orderByDesc(CoanNews::getDate);
        return newsMapper.selectPage(new Page<>(pageNum, pageSize), qw);
    }

    public CoanNews addNews(CoanNews coanNews) {
        newsMapper.insert(coanNews);
        return coanNews;
    }

    public CoanNews deleteNews(CoanNews coanNews) {
        newsMapper.deleteById(coanNews);
        return coanNews;
    }


}

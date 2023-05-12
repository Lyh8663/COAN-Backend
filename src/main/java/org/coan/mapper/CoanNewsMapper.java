package org.coan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.coan.pojo.CoanNews;

@Mapper
public interface CoanNewsMapper extends BaseMapper<CoanNews> {
    @Select("SELECT tb_news.* FROM tb_news " +
            "WHERE tb_news.article_url = #{url} ")
    Page<CoanNews> selectByUrl(String url, IPage<CoanNews> page);

}

package org.coan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.coan.pojo.CoanNewsContent;

@Mapper
public interface CoanNewsContentMapper extends BaseMapper<CoanNewsContent> {
    @Select("SELECT tb_new_content.* FROM tb_new_content " +
            "WHERE tb_new_content.url = #{url} ")
    Page<CoanNewsContent> selectByUrl(String url, IPage<CoanNewsContent> page);

    @Select("SELECT tb_new_content.* FROM tb_new_content " +
            "WHERE tb_new_content.url = #{url} ")
    CoanNewsContent selectOneByUrl(String url);

}

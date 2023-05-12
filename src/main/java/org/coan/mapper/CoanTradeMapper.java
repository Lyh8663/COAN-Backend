package org.coan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.coan.pojo.CoanTrade;

@Mapper
public interface CoanTradeMapper extends BaseMapper<CoanTrade> {

    @Select("SELECT tb_deal.* FROM tb_deal " +
            "WHERE (tb_deal.seller = #{userId} " +
            "or tb_deal.buyer = #{userId} ) " +
            "and tb_deal.state = #{state}  " +
            "order by tb_deal.create_time desc")
    Page<CoanTrade> selectByIdAndState(Long userId, short state, IPage<CoanTrade> page);

    @Select("SELECT tb_deal.* FROM tb_deal " +
            "WHERE tb_deal.seller = #{userId} " +
            "or tb_deal.buyer = #{userId}  " +
            "order by tb_deal.create_time desc")
    Page<CoanTrade> selectAllPageById(Long userId, IPage<CoanTrade> page);
}

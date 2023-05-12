package org.coan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.coan.pojo.Coin;

@Mapper
public interface CoinMapper extends BaseMapper<Coin> {
    @Select("SELECT tb_coin.* FROM tb_coin, tb_sector " +
            "WHERE tb_sector.sector_name = #{sectorName} " +
            "order by tb_coin.market_cap desc")
    Page<Coin> findCoinsBySector(String sectorName, IPage<Coin> page);

    @Select("SELECT last FROM rb_coin where project = #{projectName}")
    double findProjectValue(String projectName);
}

package org.coan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.coan.pojo.GameProperty;

import java.util.List;

@Mapper
public interface GamePropertyMapper extends BaseMapper<GameProperty> {
    @Select("select * from tb_game_property where owner=#{id}")
    List<GameProperty> findGamePropertyByUserId(long id);

    @Select("select * from tb_game_property where owner=#{id} and property = #{name}")
    GameProperty findGamePropertyByKey(long id, String name);

    @Delete("delete from tb_game_property where property=#{name} and owner= #{id}")
    int deleteGameProperty(String name, long id);

    @Update("update tb_game_property set amount = #{amount} where property=#{name} and owner= #{id}")
    int updateGameProperty(String name, long id, double amount);
}

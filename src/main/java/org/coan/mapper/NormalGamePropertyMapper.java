package org.coan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.coan.pojo.NormalGameProperty;

/**
 * @author Nebula
 * @Description TODO
 * @date 2023/5/21 19:18
 */
@Mapper
public interface NormalGamePropertyMapper extends BaseMapper<NormalGameProperty> {
    @Select("SELECT * FROM `tb_normal_game_property_user` WHERE property_id = #{propertyId}")
    Page<NormalGameProperty> selectPropertyByType(Integer propertyId, IPage<NormalGameProperty> page);

    @Update("UPDATE tb_normal_game_property_user SET owner_id = #{ownerId} WHERE id = #{id}")
    int updateOwner(Long ownerId, Integer id);

    @Select("SELECT * FROM tb_normal_game_property_user WHERE id = #{id}")
    NormalGameProperty selectPropertyById(Integer id);
}

package org.coan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.coan.pojo.MetaverseGameProperty;
import org.coan.pojo.NormalGameProperty;

/**
 * @author Nebula
 * @Description TODO
 * @date 2023/5/21 19:18
 */
@Mapper
public interface MetaverseGamePropertyMapper extends BaseMapper<MetaverseGameProperty> {
    @Select("SELECT * FROM `tb_metaverse_game_property_user` WHERE property_id = #{propertyId}")
    Page<MetaverseGameProperty> selectPropertyByType(Integer propertyId, IPage<NormalGameProperty> page);

    @Select("SELECT * FROM tb_metaverse_game_property_user as pu INNER JOIN tb_metaverse_game_property as p on pu.property_id = p.id WHERE pu.id = #{id} ")
    NormalGameProperty selectPropertyWithName(Integer id);

    @Update("UPDATE tb_metaverse_game_property_user SET owner_id = #{ownerId} WHERE id = #{id}")
    int updateOwner(Long ownerId, Integer id);

    @Select("SELECT * FROM tb_metaverse_game_property_user WHERE id = #{id}")
    MetaverseGameProperty selectPropertyById(Integer id);

    @Select("SELECT name from tb_metaverse_game_property where id=(SELECT property_id FROM tb_metaverse_game_property_user WHERE id=#{id} )")
    String queryPropertyName(Integer id);
}

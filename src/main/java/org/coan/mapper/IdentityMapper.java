package org.coan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.coan.pojo.Identity;
import org.coan.pojo.vo.IdentityVo;

@Mapper
public interface IdentityMapper extends BaseMapper<Identity> {

    @Select("select * from tb_identity where id = #{id}")
    @Results({@Result(id = true, property = "id", column = "id"),
            @Result(property = "cryptoCurrencies", column = "id",
                    many = @Many(select = "org.coan.mapper.GamePropertyMapper.findGamePropertyByUserId")),
            @Result(property = "nftProperties", column = "id",
                    many = @Many(select = "org.coan.mapper.NFTMapper.findNFTByUserId")),
            @Result(property = "gameProperties", column = "id",
                    many = @Many(select = "org.coan.mapper.GamePropertyMapper.findGamePropertyByUserId"))})
    IdentityVo getIdentityVoById(long id);
}

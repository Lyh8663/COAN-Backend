package org.coan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.coan.pojo.NFT;

import java.util.List;

@Mapper
public interface NFTMapper extends BaseMapper<NFT> {
    @Select("select * from tb_nft where owner = #{id}")
    List<NFT> findNFTByUserId(long id);

    @Select("select * from tb_nft where owner = #{id} and nft = #{name}")
    NFT findNFTByKey(long id, String name);

    @Delete("delete from tb_nft where nft=#{name} and owner= #{id}")
    int deleteNFT(String name, long id);

    @Update("update tb_nft set amount = #{amount} where nft=#{name} and owner= #{id}")
    int updateNFT(String name, long id, double amount);
}

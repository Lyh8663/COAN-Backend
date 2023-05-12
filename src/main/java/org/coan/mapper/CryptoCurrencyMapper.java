package org.coan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.coan.pojo.CryptoCurrency;

import java.util.List;

@Mapper
public interface CryptoCurrencyMapper extends BaseMapper<CryptoCurrency> {
    @Select("select * from tb_crypto_currency where userId = #{id}")
    List<CryptoCurrency> findCryptoByUserId(long id);

    @Select("select * from tb_crypto_currency where userId = #{id} and currency = #{name}")
    CryptoCurrency findCryptoByKey(long id, String name);

    @Delete("delete from tb_crypto_currency where currency=#{name} and userId= #{id}")
    int deleteCryptoCurrency(String name, long id);

    @Update("update tb_crypto_currency set amount = #{amount} where currency=#{name} and userId= #{id}")
    int updateCryptoCurrency(String name, long id, double amount);

}

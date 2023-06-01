package org.coan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.coan.pojo.ExchangeRequest;
import org.coan.pojo.NormalGameProperty;

/**
 * @author Nebula
 * @Description TODO
 * @date 2023/6/1 20:28
 */
@Mapper
public interface ExchangeRequestMapper extends BaseMapper<ExchangeRequest> {
    @Select("SELECT * FROM `tb_exchange_request` WHERE sender_id = #{senderId}")
    Page<ExchangeRequest> selectExchangeRequestByType(Long senderId, IPage<NormalGameProperty> page);

    @Select("SELECT * FROM tb_exchange_request WHERE id = #{id}")
    ExchangeRequest queryExchangeRequestById(Integer id);
}

package org.coan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.coan.pojo.Payment;

import java.util.List;

@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {
    @Select("select * from tb_payment where offer_id = #{id}")
    List<Payment> findPaymentsByOfferId(long id);

    @Delete("DELETE FROM tb_payment where offer_id = #{id}")
    void deletePaymentsByOfferId(long id);
}

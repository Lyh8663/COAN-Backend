package org.coan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.coan.pojo.Offer;
import org.coan.pojo.vo.OfferVo;

@Mapper
public interface OfferMapper extends BaseMapper<Offer> {

    @Select("SELECT * FROM tb_offer where id = #{id}")
    @Results({@Result(id = true, property = "id", column = "id"),
            @Result(property = "payments", column = "id",
                    many = @Many(
                            select = "org.coan.mapper.PaymentMapper.findPaymentsByOfferId"
                    ))})
    OfferVo findOfferById(long id);
}

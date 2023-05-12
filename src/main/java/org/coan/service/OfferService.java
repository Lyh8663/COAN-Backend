package org.coan.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.coan.mapper.OfferMapper;
import org.coan.mapper.PaymentMapper;
import org.coan.pojo.Offer;
import org.coan.pojo.Payment;
import org.coan.pojo.vo.OfferVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

import static org.coan.enumeration.ItemTypeEnum.*;

@Service
public class OfferService {

    @Autowired
    private OfferMapper mapper;

    @Autowired
    private PaymentMapper paymentMapper;


    public boolean saveOffer(OfferVo offerVo) {
        if (Objects.isNull(offerVo)) {
            throw new RuntimeException("信息不存在");
        }
        offerVo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        offerVo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (mapper.insert(offerVo) < 1) {
            throw new RuntimeException("保存信息失败");
        }
        for (Payment payment : offerVo.getPayments()) {
            payment.setOfferId(offerVo.getId());
            if (paymentMapper.insert(payment) < 1) {
                throw new RuntimeException("保存支付方式失败");
            }
        }
        return true;
    }

    public OfferVo getOfferById(long id) {
        return mapper.findOfferById(id);
    }

    public Offer updateOfferVo(long id, OfferVo offerVo) {
        paymentMapper.deletePaymentsByOfferId(id);
        for(Payment payment: offerVo.getPayments()) {
            payment.setOfferId(id);
            paymentMapper.insert(payment);
        }
        Offer oldOffer = mapper.findOfferById(id);
        if (Objects.isNull(oldOffer)) {
            throw new RuntimeException("修改未知信息");
        }
        Timestamp timestamp = oldOffer.getCreateTime();
        BeanUtils.copyProperties(offerVo, oldOffer);
        oldOffer.setId(id);
        oldOffer.setCreateTime(timestamp);
        oldOffer.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (mapper.updateById(oldOffer) < 1) {
            throw new RuntimeException("修改失败");
        }
        return oldOffer;
    }

    public boolean delOffer(long id) {
        paymentMapper.deletePaymentsByOfferId(id);
        return mapper.deleteById(id) > 0;
    }

    public Page<Offer> getNftOffers(int page, int size) {
        LambdaQueryWrapper<Offer> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Offer::getOfferType, NFT.getType())
                .orderByDesc(Offer::getCreateTime);
        return mapper.selectPage(new Page<>(page, size), lqw);
    }

    public Page<Offer> getGameOffers(int page, int size) {
        LambdaQueryWrapper<Offer> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Offer::getOfferType, GAME.getType())
                .orderByDesc(Offer::getCreateTime);
        return mapper.selectPage(new Page<>(page, size), lqw);
    }

    public Page<Offer> getCryptoOffers(int page, int size) {
        LambdaQueryWrapper<Offer> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Offer::getOfferType, CRYPTO.getType())
                .orderByDesc(Offer::getCreateTime);
        return mapper.selectPage(new Page<>(page, size), lqw);
    }


    public Page<Offer> getOfferPage(int pageNum, int pageSize, Map<String, Object> conditions) {
        LambdaQueryWrapper<Offer> lqw = new LambdaQueryWrapper<>();
        //TODO:add select conditions
        return mapper.selectPage(new Page<>(pageNum, pageSize), lqw);

    }
}

package org.coan.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.coan.mapper.CoanTradeMapper;
import org.coan.pojo.CoanTrade;
import org.coan.pojo.Offer;
import org.coan.pojo.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import static org.coan.enumeration.OfferTypeEnum.BUY_IN;
import static org.coan.enumeration.OfferTypeEnum.SWAP_IN;
import static org.coan.enumeration.TradeStatusEnum.ONGOING;


@Service
public class TradeService {

    @Autowired
    private CoanTradeMapper coanTradeMapper;

    public boolean updateTrade(CoanTrade coanTrade) {
        return coanTradeMapper.updateById(coanTrade) > 0;

    }

    public boolean deleteTrade(long id) {
        CoanTrade res = coanTradeMapper.selectById(id);
        if (res == null || res.getState() != ONGOING.getType()) {
            return false;
        }
        return coanTradeMapper.deleteById(id) > 0;
    }

    public Page<CoanTrade> getTradePageByUserAndState(long id, int state, int pageNum, int pageSize) {
        LambdaQueryWrapper<CoanTrade> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CoanTrade::getState, state).and(
                qw -> qw.eq(CoanTrade::getSeller, id).or().eq(CoanTrade::getBuyer, id)
        ).orderByDesc(CoanTrade::getUpdateTime);
        return coanTradeMapper.selectPage(new Page<>(pageNum, pageSize), lqw);
    }

    public Page<CoanTrade> getAllTradePageByUser(long id, int pageNum, int pageSize) {
        LambdaQueryWrapper<CoanTrade> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CoanTrade::getSeller, id).or().eq(CoanTrade::getBuyer, id)
                .orderByDesc(CoanTrade::getUpdateTime);
        return coanTradeMapper.selectPage(new Page<>(pageNum, pageSize), lqw);
    }

    public Page<CoanTrade> getAllTradePage(int pageNum, int pageSize) {
        QueryWrapper<CoanTrade> qw = new QueryWrapper<>();
        qw.lambda().orderByDesc(CoanTrade::getUpdateTime);
        return coanTradeMapper.selectPage(new Page<>(pageNum, pageSize), qw);
    }

    public CoanTrade addTrade(CoanTrade trade) {
        coanTradeMapper.insert(trade);
        return trade;
    }

    public CoanTrade addOngoingTrade(CoanTrade coanTrade) {
        coanTrade.setState(0);
        coanTrade.setCreateTime(new Timestamp(System.currentTimeMillis()));
        coanTradeMapper.insert(coanTrade);
        return coanTrade;
    }

    public CoanTrade tradeSuccess(CoanTrade coanTrade) {
        coanTrade.setState(1);
        coanTrade.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        coanTradeMapper.updateById(coanTrade);
        return coanTrade;
    }

    public Page<CoanTrade> getTradePageByProperties(String properties, int pageNum, int pageSize) {
        LambdaQueryWrapper<CoanTrade> lqw = new LambdaQueryWrapper<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        cal.add(Calendar.MINUTE, -30);
        lqw.eq(CoanTrade::getState, 1).and(
                qw -> qw.ge(CoanTrade::getUpdateTime, new Timestamp(cal.getTimeInMillis()))).and(
                qw -> qw.eq(CoanTrade::getSource, properties).or().eq(CoanTrade::getTarget, properties)
        ).orderByDesc(CoanTrade::getUpdateTime);
        return coanTradeMapper.selectPage(new Page<>(pageNum, pageSize), lqw);
    }

    public List<CoanTrade> getTradePageByUserAndTime(long id, int time) {
        LambdaQueryWrapper<CoanTrade> lqw = new LambdaQueryWrapper<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        cal.add(Calendar.MINUTE, -1 * time);
        lqw.eq(CoanTrade::getState, 1).and(
                qw -> qw.ge(CoanTrade::getUpdateTime, new Timestamp(cal.getTimeInMillis()))).and(
                qw -> qw.eq(CoanTrade::getSeller, id).or().eq(CoanTrade::getBuyer, id)
        ).orderByDesc(CoanTrade::getUpdateTime);
        return coanTradeMapper.selectList(lqw);
    }
}
package org.coan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.coan.mapper.CoinMapper;
import org.coan.pojo.Coin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoinService {

    @Autowired
    private CoinMapper coinMapper;

    public Page<Coin> getCoinPageBySectorName(String name, int pageNum, int pageSize) {
        return coinMapper.findCoinsBySector(name, new Page<>(pageNum, pageSize));
    }

    public Page<Coin> getAllCoinPage(int pageNum, int pageSize) {
        QueryWrapper<Coin> qw = new QueryWrapper<>();
        qw.lambda().orderByDesc(Coin::getMarketCap);
        return coinMapper.selectPage(new Page<>(pageNum, pageSize), qw);
    }

    public List<String> getAllCoins() {
        List<Coin> coinList = coinMapper.selectList(new QueryWrapper<>());
        List<String> res = new ArrayList<>();
        for (Coin coin : coinList) {
            res.add(coin.getProject());
        }
        return res;
    }

    public Double getCoinRadio(String source, String target) {
        QueryWrapper<Coin> qw2 = new QueryWrapper<>();
        qw2.lambda().eq(Coin::getProject, target);
        QueryWrapper<Coin> qw = new QueryWrapper<>();
        qw.lambda().eq(Coin::getProject, source);
        List<Coin> LSource = coinMapper.selectList(qw);
        List<Coin> LTarget = coinMapper.selectList(qw2);
        if (LSource.isEmpty() || LTarget.isEmpty()) {
            return -1.0;
        }
        double vSource = LSource.get(0).getLast();
        double vTarget = LTarget.get(0).getLast();
        return vSource / vTarget;
    }

}

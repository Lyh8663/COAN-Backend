package org.coan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.coan.pojo.CoanTrade;
import org.coan.pojo.CoanUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.coan.enumeration.ItemTypeEnum.CASH;
import static org.coan.enumeration.ItemTypeEnum.NFT;
import static org.coan.enumeration.TradeStatusEnum.ONGOING;

@SpringBootTest
public class TradeTest {
    @Autowired
    private TradeService tradeService;

    @Autowired
    private WalletService walletService;

    @Test
    public void testGetAll() {
        CoanUser coanUser = new CoanUser();
        coanUser.setId(55L);
        short b = 0;
        Page<CoanTrade> a = tradeService.getTradePageByUserAndState(coanUser.getId(), b, 0, 1);
        System.out.println(a.getRecords());
    }

    @Test
    public void testCompleteTrade() {
        tradeService.deleteTrade(21321L);
        CoanTrade trade = new CoanTrade();
        trade.setId(21321L);
        trade.setState(ONGOING.getType());
        trade.setTgtType(CASH.getType());
        trade.setSrcType(NFT.getType());
        trade.setSource("first");
        trade.setSrcAmount(1.0);
        trade.setTgtAmount(300.0);
        trade.setCreateTime(new Timestamp(System.currentTimeMillis()));
        trade.setUpdateTime(trade.getCreateTime());
        trade.setSeller(2L);
        trade.setBuyer(11112222L);
        tradeService.addTrade(trade);
        walletService.completeTrade(trade);
    }

}

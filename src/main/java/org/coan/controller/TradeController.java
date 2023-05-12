package org.coan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.coan.pojo.CoanTrade;
import org.coan.pojo.Identity;
import org.coan.pojo.Offer;
import org.coan.pojo.Payment;
import org.coan.pojo.vo.OfferVo;
import org.coan.service.*;
import org.coan.util.CreditCalculation;
import org.coan.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static org.coan.enumeration.TradeStatusEnum.FINISHED;

@RestController
@Api("交易信息")
@RequestMapping("/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/{id}")
    @ApiOperation(value = "获得用户的历史交易", httpMethod = "GET")
    public R getAllTradeByUser(@PathVariable("id") Long id,
                               @RequestParam(defaultValue = "0") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize) {
        if (Objects.isNull(id)) {
            return R.fail().message("参数为空");
        }
        return R.ok().data("data", tradeService.getAllTradePageByUser(id, pageNum, pageSize));
    }

    @GetMapping("/state")
    @ApiOperation(value = "根据交易状态查找历史交易", httpMethod = "GET")
    public R getTradeByUserAndState(@RequestParam(value = "id") Long id,
                                    @RequestParam(value = "state") Integer state,
                                    @RequestParam(defaultValue = "0") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        if (Objects.isNull(id) || Objects.isNull(state)) {
            return R.fail().message("查询参数异常");
        }

        return R.ok().data("data", tradeService.getTradePageByUserAndState(id, state, pageNum, pageSize));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除未完成的交易", httpMethod = "DELETE")
    public R deleteTrade(@PathVariable("id") long id) {
        if (tradeService.deleteTrade(id)) {
            return R.ok();
        } else {
            return R.fail().message("删除失败");
        }
    }

    @PostMapping("/add")
    @ApiOperation(value = "进行新交易", httpMethod = "POST")
    public R addTrade(@RequestBody CoanTrade trade) {
        if (identityService.isIdentityStateNormal(trade.getBuyer())) {
            return R.fail().message("你的账户已被封禁");
        }
        Identity identity = identityService.getIdentityById(trade.getBuyer());
        // check trade
        if(identity.getCredit()>95){
            identity.setCurrentTrade(identity.getCurrentTrade() + 1);
            identity.setCurrentQuota(identity.getCurrentQuota()+
                    (int)(paymentService.getQuota(trade.getTarget(), trade.getTgtAmount(), trade.getTgtType())));
            identityService.updateIdentity(identity.getId(), identity);
        }else {
            if (identity.getCurrentTrade() + 1 > identity.getHistoryTrade()) {
                return R.fail().message("达到交易次数上限");
            } else {
                identity.setCurrentTrade(identity.getCurrentTrade() + 1);
                identityService.updateIdentity(identity.getId(), identity);
            }
            boolean isAllow = identityService.checkQuota(identity,
                    paymentService.getQuota(trade.getTarget(), trade.getTgtAmount(), trade.getTgtType()));
            if (!isAllow) {
                return R.fail().message("交易限额");
            }
        }
        return R.ok().data("data", tradeService.addTrade(trade));
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改交易信息", httpMethod = "POST")
    public R updateTrade(@RequestBody CoanTrade coanTrade) {
        if (Objects.isNull(coanTrade)) {
            return R.fail().message("参数错误");
        }
        return R.ok().data("data", tradeService.updateTrade(coanTrade));
    }


    @GetMapping("/ongoing")
    @Deprecated
    public R addOngoingTrade(@RequestBody CoanTrade coanTrade) {
        Boolean pass = CreditCalculation.checkCreditChangeBeforeTrade(identityService.getIdentityById(coanTrade.getSeller()).getCredit());
        pass &= CreditCalculation.checkCreditChangeBeforeTrade(identityService.getIdentityById(coanTrade.getBuyer()).getCredit());
        if (!pass) {
            R.fail().message("用户信用分过低，交易失败");
        }
        return R.ok().data("data", tradeService.addOngoingTrade(coanTrade));
    }


    @PostMapping("/success")
    @ApiOperation(value = "完成交易", httpMethod = "POST")
    public R tradeSuccess(@RequestBody CoanTrade coanTrade) {

//        信用分的计算和各个账户的更改
        Double changeSeller = calculateCreditChangeByTrade(coanTrade);
        Double changeBuyer = changeSeller;
        int buyerNum30 = tradeService.getTradePageByUserAndTime(coanTrade.getBuyer(), 30).size();
        int sellerNum30 = tradeService.getTradePageByUserAndTime(coanTrade.getSeller(), 30).size();
        int buyerNumDay = tradeService.getTradePageByUserAndTime(coanTrade.getBuyer(), 24 * 60).size();
        int sellerNumDay = tradeService.getTradePageByUserAndTime(coanTrade.getSeller(), 24 * 60).size();
        if (buyerNum30 > 30 || buyerNumDay > 200) {
            changeBuyer -= 10.0;
        }
        if (sellerNum30 > 30 || sellerNumDay > 200) {
            changeSeller -= 10.0;
        }
        Double marketRadio = coinService.getCoinRadio(coanTrade.getSource(), coanTrade.getTarget());
        Double radio = coanTrade.getSrcAmount() / coanTrade.getTgtAmount();
        if (radio / marketRadio > 2 || radio / marketRadio < 0.5) {
            changeBuyer -= 10.0;
            changeSeller -= 10.0;
        }
        identityService.changeCreditById(coanTrade.getSeller(), changeSeller);
        identityService.changeCreditById(coanTrade.getBuyer(), changeBuyer);
        walletService.completeTrade(coanTrade);
        coanTrade.setState(FINISHED.getType());
        tradeService.updateTrade(coanTrade);
//          更改钱包
        return R.ok().data("data", tradeService.tradeSuccess(coanTrade));
    }

    @GetMapping("/all")
    @ApiOperation(value = "获取平台的交易", httpMethod = "GET")
    public R getAllTradePage(@RequestParam(defaultValue = "0") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok().data("data", tradeService.getAllTradePage(pageNum, pageSize));
    }

    Double calculateCreditChangeByTrade(CoanTrade coanTrade) {
        Page<CoanTrade> res1 = tradeService.getTradePageByProperties(coanTrade.getSource(), 0, 1);
        Page<CoanTrade> res2 = tradeService.getTradePageByProperties(coanTrade.getTarget(), 0, 1);
        if (res1.getRecords().isEmpty() && res2.getRecords().isEmpty()) {
            return 0.5;
        }
        return -10.0;
    }

}

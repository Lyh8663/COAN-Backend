package org.coan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.coan.pojo.Offer;
import org.coan.pojo.vo.OfferVo;
import org.coan.service.IdentityService;
import org.coan.service.OfferService;
import org.coan.service.WalletService;
import org.coan.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api("买卖/交换信息相关")
@RequestMapping("offer")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private WalletService walletService;

    @GetMapping("/nft")
    @ApiOperation(value = "获取买卖/交换nft的信息", httpMethod = "GET")
    public R getNftOffers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return R.ok().data("data", offerService.getNftOffers(page, size));
    }

    @GetMapping("/game")
    @ApiOperation(value = "获取买卖/交换游戏资产的信息", httpMethod = "GET")
    public R getGameOffers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> params = new HashMap<>();
        //TODO:handle conditions
        return R.ok().data("data", offerService.getGameOffers(page, size));
    }

    @GetMapping("/crypto")
    @ApiOperation(value = "获取买卖/交换加密货币的信息", httpMethod = "GET")
    public R getCryptoOffers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> params = new HashMap<>();
        //TODO:handle conditions
        return R.ok().data("data", offerService.getCryptoOffers(page, size));
    }

    @GetMapping("")
    @ApiOperation(value = "根据关键词查找信息", httpMethod = "GET")
    public R getOfferByConditions(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        //TODO:handle conditions
        Map<String, Object> params = new HashMap<>();
        Page<Offer> res;
        try {
            res = offerService.getOfferPage(page, size, params);
        } catch (Exception e) {
            return R.fail().message(e.getMessage());
        }
        return R.ok().data("data", res);
    }

    @PostMapping("")
    @ApiOperation(value = "用户发布买卖、交换信息", httpMethod = "POST")
    public R saveOffer(@RequestBody OfferVo vo) {
        boolean flag1 = identityService.isIdentityStateNormal(vo.getPublisher());
        //boolean flag2 = walletService.isPropertyExist()
        if(!flag1) {
            return R.fail().message("用户id不存在");
        }
        //TODO:check property exist
        try {
            offerService.saveOffer(vo);
        } catch (Exception e) {
            return R.fail().message(e.getMessage());
        }
        return R.ok();
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "根据id修改信息", httpMethod = "PUT")
    public R updateOffer(@PathVariable("id") long id,
                         @RequestBody OfferVo vo) {
        try {
            offerService.updateOfferVo(id, vo);
        } catch (Exception e) {
            R.fail().message(e.getMessage());
        }
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除已有信息", httpMethod = "DELETE")
    public R deleteOffer(@PathVariable("id") long id) {
        boolean flag = false;
        try {
            flag = offerService.delOffer(id);
        } catch (Exception e) {
            R.fail().message(e.getMessage());
        }
        if(flag) {
            return R.ok();
        }
        else {
            return R.fail();
        }
    }

}

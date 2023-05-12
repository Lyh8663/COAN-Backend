package org.coan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.coan.pojo.CryptoCurrency;
import org.coan.pojo.GameProperty;
import org.coan.pojo.NFT;
import org.coan.service.WalletService;
import org.coan.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("钱包信息")
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;


    @GetMapping("/total/{id}")
    @ApiOperation(value = "获取钱包中当前的价值", httpMethod = "GET")
    public R getWalletValue(@PathVariable("id") long id) {
        double res = walletService.getWalletValueById(id);
        return R.ok().data("data", res);
    }

    @GetMapping("/crypto/{id}")
    @ApiOperation(value = "获取所有加密货币的详情", httpMethod = "GET")
    public R getAllCrypto(@PathVariable("id") long id,
                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<CryptoCurrency> cryptoPage = walletService.getCryptoPage(id, page, size);
        return R.ok().data("data", cryptoPage);
    }

    @GetMapping("/game/{id}")
    @ApiOperation(value = "获取钱包中所有游戏资产的详情", httpMethod = "GET")
    public R getAllGameProperties(@PathVariable("id") long id,
                                  @RequestParam(value = "page", defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<GameProperty> propertyPage = walletService.getGamePropertyPage(id, page, size);
        return R.ok().data("data", propertyPage);
    }

    @GetMapping("/nft/{id}")
    @ApiOperation(value = "获取钱包中所有nft的详情", httpMethod = "GET")
    public R getAllNFTs(@PathVariable("id") long id,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<NFT> nftPage = walletService.getNFTPage(id, page, size);
        return R.ok().data("data", nftPage);
    }


}

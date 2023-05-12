package org.coan.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.coan.crawler.Crawler;
import org.coan.service.CoinService;
import org.coan.service.SectorService;
import org.coan.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("行情相关")
@RequestMapping("market")
public class CoinController {
    @Autowired
    private SectorService sectorService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private Crawler crawler;

    @GetMapping("/sectors")
    @ApiOperation(value = "获取所有经济模块", httpMethod = "GET")
    public R getSectorList() {
        return R.ok().data("data", sectorService.getSectorList());
    }

    @GetMapping("")
    @ApiOperation(value = "获取所有币种信息页面", httpMethod = "GET")
    public R getAllCoins(@RequestParam(defaultValue = "0") Integer pageNum,
                         @RequestParam(defaultValue = "10") Integer pageSize) {

        return R.ok().data("data", coinService.getAllCoinPage(pageNum, pageSize));
    }

    @GetMapping("/{name}")
    @ApiOperation(value = "获取名称相关的币种相关信息", httpMethod = "GET")
    public R getBySectors(@PathVariable("name") String name,
                          @RequestParam(defaultValue = "0") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok().data("data", coinService.getCoinPageBySectorName(name, pageNum, pageSize));
    }

    @Scheduled(cron = "${crawler.cron.coin}")
    public void crawlCoin() {
        crawler.crawlCoin();
    }

    @Scheduled(cron = "${crawler.cron.sector}")
    public void crawlSectors() {
        crawler.crawlSectorList();
    }


}

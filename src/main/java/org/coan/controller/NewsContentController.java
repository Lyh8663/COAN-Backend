package org.coan.controller;

import io.swagger.annotations.Api;
import org.coan.crawler.Crawler;
import org.coan.service.CoinService;
import org.coan.service.NewsContentService;
import org.coan.service.NewsService;
import org.coan.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("新闻相关")
@RequestMapping("newsContent")
public class NewsContentController {
    @Autowired
    private SectorService sectorService;

    @Autowired
    private CoinService coinService;
    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsContentService newsContentService;

    @Autowired
    private Crawler crawler;


//    @GetMapping("")
//    public R getAll(@RequestParam(defaultValue = "0")Integer pageNum,
//                                 @RequestParam(defaultValue = "10") Integer pageSize) {
//        return R.ok().data("data",newsService.getAllNewsPage(pageNum, pageSize));
//    }
//    @GetMapping("add")
//    public R addNews(CoanNews coanNews) {
//        return R.ok().data("data",newsService.addNews(coanNews));
//    }
//    @GetMapping("delete")
//    public R deleteNews(CoanNews coanNews) {
//        return R.ok().data("data",newsService.deleteNews(coanNews));
//    }


}

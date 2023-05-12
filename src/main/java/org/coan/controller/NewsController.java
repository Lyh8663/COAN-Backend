package org.coan.controller;

import io.swagger.annotations.Api;
import org.coan.crawler.Crawler;
import org.coan.pojo.CoanNews;
import org.coan.pojo.CoanNewsBody;
import org.coan.pojo.CoanNewsContent;
import org.coan.service.CoinService;
import org.coan.service.NewsContentService;
import org.coan.service.NewsService;
import org.coan.service.SectorService;
import org.coan.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@Api("新闻相关")
@RequestMapping("news")
public class NewsController {
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


    @GetMapping("")
    public R getAll(@RequestParam(defaultValue = "0") Integer pageNum,
                    @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok().data("data", newsService.getAllNewsPage(pageNum, pageSize));
    }

    @PostMapping("add")
    public R addNews(@RequestBody CoanNewsBody newsBody) {
        CoanNewsContent content=new CoanNewsContent(newsBody.getArticleUrl(),newsBody.getContent());
        CoanNews coanNews=new CoanNews(newsBody.getId(),newsBody.getProvider(),newsBody.getDate()
        ,newsBody.getDescription(),newsBody.getArticleUrl(), newsBody.getTitle());
        Timestamp cur=new Timestamp(System.currentTimeMillis());
        String url = cur.toString() + coanNews.getProvider();
        coanNews.setArticleUrl(url);
        content.setUrl(url);
        coanNews.setDescription(content.getContent().substring(0, Math.min(content.getContent().length(), 100)));
        newsContentService.addNewsContent(content);
        return R.ok().data("data", newsService.addNews(coanNews));
    }

    @PostMapping("delete")
    public R deleteNews(@RequestBody CoanNews coanNews) {
        newsContentService.deleteNewsContent(coanNews.getArticleUrl());
        return R.ok().data("data", newsService.deleteNews(coanNews));
    }

    @PostMapping("content")
    public R getContentByUrl(@RequestBody String url) {
        CoanNewsContent content=newsContentService.getContentByUrl(url);
        return R.ok().data("data", content);
    }

    @Scheduled(cron = "${crawler.cron.news}")
    public void crawlCoin() {
        crawler.crawlNews();
    }

    @Scheduled(cron = "${crawler.cron.content}")
    public void crawlSectors() {
        crawler.crawlNewsContent();
    }


}

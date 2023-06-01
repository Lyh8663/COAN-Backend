package org.coan.controller;

import io.swagger.annotations.Api;
import org.coan.service.MetaverseGamePropertyService;
import org.coan.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Nebula
 * @Description TODO
 * @date 2023/5/21 18:06
 */
@RestController
@Api("元宇宙游戏资产相关")
@RequestMapping("/metaverseGameProperty")
public class MetaverseGamePropertyController {

    @Autowired
    MetaverseGamePropertyService metaverseGamePropertyService;

    /**
     * 按类型获取游戏资产
     *
     * @param propertyId 游戏资产种类
     * @param pageNum    分页页号
     * @param pageSize   分页大小
     * @return Http状态码
     */
    @GetMapping("/propertyType/{propertyId}")
    public R getNormalGamePropertiesByType(@PathVariable("propertyId") String propertyId,
                                           @RequestParam(defaultValue = "0") Integer pageNum,
                                           @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok().data("data", metaverseGamePropertyService.getNormalGamePropertiesByType(Integer.parseInt(propertyId), pageNum, pageSize));
    }

    @GetMapping("/property/{id}")
    public R getNormalGamePropertyWithName(@PathVariable("id")String id){
        return R.ok().data("data",metaverseGamePropertyService.selectPropertyWithName(Integer.valueOf(id)));
    }

    /**
     * 交换游戏资产
     *
     * @param id 游戏资产交换请求id
     * @return
     */
    @RequestMapping("/exchange/{id}")
    public ModelAndView exchangeMetaverseGameProper(@PathVariable("id") String id) {
        boolean flag = metaverseGamePropertyService.exchangeMetaverseGameProperty(Integer.valueOf(id));
        if (flag) {
            return new ModelAndView(new RedirectView("https://www.baidu.com"));//成功页面
            //return R.ok().data("data", flag);
        } else {
            return new ModelAndView(new RedirectView("https://www.google.com"));//失败
            //return R.fail().message("普通游戏资产交换失败");
        }

    }

    @RequestMapping("/refuse/{id}")
    public ModelAndView refuseExchangeMetaverseGameProper(@PathVariable("id") String id) {
        boolean flag = metaverseGamePropertyService.refuseExchange(Integer.valueOf(id));
        if (flag) {
            return new ModelAndView(new RedirectView("https://www.baidu.com"));//成功页面
            //return R.ok().data("data", flag);
        } else {
            return new ModelAndView(new RedirectView("https://www.google.com"));//失败
            //return R.fail().message("普通游戏资产交换失败");
        }

    }

    /**
     * 向被交换者发送邮件
     *
     * @param id1 邮件接收方元宇宙游戏资产id
     * @param id2 邮件发送方游戏资产id
     * @return
     */
    @PostMapping("/requestExchange/{id1}/{id2}")
    public R requestExchanger(@PathVariable("id1") String id1, @PathVariable("id2") String id2,@RequestParam("comment")String comment) {
        boolean flag = metaverseGamePropertyService.requestExchange(Integer.valueOf(id1),Integer.valueOf(id2),comment);
        if (flag) {
            return R.ok().data("data", flag);
        } else {
            return R.fail().message(" 请求交换普通游戏资产邮件发送失败");
        }
    }
}

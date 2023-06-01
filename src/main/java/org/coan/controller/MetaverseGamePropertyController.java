package org.coan.controller;

import io.swagger.annotations.Api;
import org.coan.service.MetaverseGamePropertyService;
import org.coan.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @param id1 要交换的游戏资产id
     * @param id2 对方的游戏资产id
     * @return
     */
    @RequestMapping("/exchange/{id1}/{id2}")
    public R exchangeNormalGameProper(@PathVariable("id1") String id1, @PathVariable("id2") String id2) {
        boolean flag = metaverseGamePropertyService.exchangeNormalGameProperty(Integer.valueOf(id1), Integer.valueOf(id2));
        if (flag) {
            return R.ok().data("data", flag);
        } else {
            return R.fail().message("元宇宙游戏资产交换失败");
        }

    }

    /**
     * 向被交换者发送邮件
     *
     * @param id1 邮件接收方元宇宙游戏资产id
     * @param id2 邮件发送方游戏资产id
     * @return
     */
    @PostMapping("/sendEmail/{id1}/{id2}")
    public R sendEmailToExchanger(@PathVariable("id1") String id1, @PathVariable("id2") String id2) {
        boolean flag = metaverseGamePropertyService.sendEmailToExchanger(Integer.valueOf(id1),Integer.valueOf(id2));
        if (flag) {
            return R.ok().data("data", flag);
        } else {
            return R.fail().message(" 请求交换元宇宙游戏资产邮件发送失败");
        }
    }
}

package org.coan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.coan.service.NormalGamePropertyService;
import org.coan.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Nebula
 * @Description TODO
 * @date 2023/5/21 18:06
 */
@RestController
@Api("游戏资产相关")
@RequestMapping("/normalGameProperty")
public class NormalGamePropertyController {

    @Autowired
    NormalGamePropertyService normalGamePropertyService;

    @GetMapping("/propertyType/{propertyId}")
    public R getNormalGamePropertiesByType(@PathVariable("propertyId") String propertyId,
                                           @RequestParam(defaultValue = "0") Integer pageNum,
                                           @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok().data("data", normalGamePropertyService.getNormalGamePropertiesByType(Integer.parseInt(propertyId), pageNum, pageSize));
    }

}

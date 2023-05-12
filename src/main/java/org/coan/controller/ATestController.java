package org.coan.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("初期测试接口")
public class ATestController {

    @GetMapping("/test")
    @ApiOperation(value = "测试是否上传成功", httpMethod = "GET")
    public String isDeploySuccess() {
        return "yes!";
    }

}

package org.coan.controller;

import io.swagger.annotations.ApiOperation;
import org.coan.pojo.Identity;
import org.coan.service.IdentityService;
import org.coan.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("identity")
public class IdentityController {
    @Autowired
    private IdentityService service;

    @GetMapping("/{id}")
    @ApiOperation(value = "根据userId获取用户数字身份不包含虚拟资产", httpMethod = "GET")
    public R getIdentity(@PathVariable("id") long id) {
        Identity identity = service.getIdentityById(id);
        if (identity == null) {
            return R.fail().message("不存在该身份");
        }
        return R.ok().data("data", identity);
    }

    @GetMapping("/properties/{id}")
    @ApiOperation(value = "根据userId获取用户数字身份包含所有资产", httpMethod = "GET")
    public R getIdentityVo(@PathVariable("id") long id) {
        Identity identity = service.getIdentityVoById(id);
        if (identity == null) {
            return R.fail().message("不存在该身份");
        }
        return R.ok().data("data", identity);
    }

    @GetMapping("/exist/{id}")
    @ApiOperation(value = "根据userId查看用户是否存在数字身份", httpMethod = "GET")
    public R isIdentityExist(@PathVariable("id") long id) {
        Boolean flag = service.isIdentityExist(id);
        return R.ok().data("data", flag);
    }

    @PostMapping("")
    @ApiOperation(value = "新建用户数字身份", httpMethod = "POST")
    public R createIdentity(@RequestBody Identity identity) {
        boolean flag = service.createIdentity(identity);
        if(flag){
            return R.ok().data("data", "创建数字身份成功");
        }
        else {
            return R.fail().message("创建数字身份失败");
        }

    }

    @PutMapping("/{id}")
    @ApiOperation(value = "更新用户数字身份", httpMethod = "PUT")
    public R updateIdentity(@PathVariable long id, @RequestBody Identity identity) {
        boolean flag = service.updateIdentity(id, identity);
        if(flag){
            return R.ok().data("data", flag);
        }
        else{
            return R.fail().message("更新失败");
        }

    }

    @PostMapping("/{id}")
    @ApiOperation(value = "充值法币", httpMethod = "POST")
    public R rechargeCash(@PathVariable long id, double cash) {
        if(service.rechargeCash(id,cash)){
            return R.ok().data("data", "充值成功");
        }
        else {
            return R.fail().message("充值失败");
        }
    }

    @GetMapping("/de/{id}")
    @ApiOperation(value = "注销账户", httpMethod = "GET")
    public R deIdentityById(@PathVariable long id) {
        return R.ok().data("data", service.deIdentityById(id));
    }


    @GetMapping("/change")
    @Deprecated
    public R changeIdentityCredit(@RequestParam(value = "id") Long id,
                                  @RequestParam(value = "num") Double num) {
        if(service.changeCreditById(id, num)) {
            return R.ok().data("data", true);
        }
        else {
            return R.fail().message("修改失败");
        }

    }

    public void updateQuotaAndTrade() {
        List<Identity> allIdentity = service.getAllIdentity();
        for (Identity identity : allIdentity) {
            double credit = identity.getCredit();
            if(credit>95){
                identity.setHistoryQuota((int) (identity.getCurrentQuota()));
                identity.setHistoryTrade((int) (identity.getCurrentTrade()));
            }else if(credit >= 80){
                identity.setHistoryQuota((int) (identity.getCurrentQuota() * 0.7));
                identity.setHistoryTrade((int) (identity.getCurrentTrade() * 0.7));
            }else if (credit < 80 && credit >= 60) {
                identity.setHistoryQuota((int) (identity.getCurrentQuota() * 0.3));
                identity.setHistoryTrade((int) (identity.getCurrentTrade() * 0.3));
            } else if (credit < 60) {
                service.banIdentity(identity.getId());
            }
            identity.setCurrentQuota(0);
            identity.setCurrentTrade(0);
            service.updateIdentity(identity.getId(), identity);
        }
    }

//    public void updateQuotaAndTrade(Identity identity) {
////        identity.setHistoryQuota();
//        double credit = identity.getCredit();
//
//    }
}

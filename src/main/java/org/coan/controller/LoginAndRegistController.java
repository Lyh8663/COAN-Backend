package org.coan.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.coan.pojo.Auth;
import org.coan.pojo.CoanUser;
import org.coan.service.LoginAndRegistService;
import org.coan.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("登录注册相关")
public class LoginAndRegistController {

    @Autowired
    private LoginAndRegistService loginAndRegistService;

    @CrossOrigin
    @PostMapping("/emailLogin")
    @ApiOperation(value = "邮箱+验证码登录", httpMethod = "POST")
    public R emailLogin(@Param("email") String email, @Param("code") String code) {
        //查看redis缓存中邮箱的key是否存在。若不存在，返回不存在或已过期
        boolean isEmailExist = loginAndRegistService.isEmailExist(email);
        if (!isEmailExist) {
            return R.error().message("验证码已过期");
        }
        //验证码存在时执行比对，相同继续执行，不同返回验证码错误
        boolean isEmailCodeSame = loginAndRegistService.isEmailCodeSame(email, code);
        if(!isEmailCodeSame){
            return R.error().message("验证码错误");
        }
        //确认验证码相同，判断用户是否为第一次登录。第一次登录则创建用户信息。
        boolean isEmaillRegisted = loginAndRegistService.isEmailRegisted(email);
        if (!isEmaillRegisted) {//创建用户表信息
            loginAndRegistService.createUserInfo(email);
        }
        //根据邮箱查找用户信息
        CoanUser coanUser;
        coanUser = loginAndRegistService.selectUserByEmail(email);
        //设置返回值
        return R.ok().data("userInfo", coanUser).data("isFirstLogin", !isEmaillRegisted).message("登陆成功");
    }

    @CrossOrigin
    @GetMapping("/sendEmailCode")
    @ApiOperation(value = "发送邮件验证码", httpMethod = "GET")
    public R sendEmailCode(@Param("email") String email) {//测试通过
        //检测邮箱格式是否正确
        boolean isEmailFormLegal = loginAndRegistService.isEmailFormLegal(email);
        if (!isEmailFormLegal) {
            return R.error().message("邮箱格式错误");
        }
//        boolean isEmailRegisted = loginAndRegistService.isEmailRegisted(email);
        //生成邮箱验证码
        String code = loginAndRegistService.createCode();
        //发送邮箱验证码

        loginAndRegistService.sendEmailCode(email,code);
        //将邮箱验证码存入redis，定时5分钟
        loginAndRegistService.saveCode2Redis(email,code);

        //默认返回值
        return R.ok().message("邮件已发送，请注意查收");
    }

    @CrossOrigin
    @PostMapping("/authentication")
    @ApiOperation(value = "实名认证", httpMethod = "POST")
    public R authentication(@RequestBody Auth auth) {
        if(auth == null || auth.getUserId() == null) {
            return R.fail().message("无用户信息");
        }
        Auth res = loginAndRegistService.authentice(auth);
        if(res != null) {
            return R.ok().data("data", auth);
        }
        else {
            return R.fail().message("实名认证失败");
        }
    }

    @PostMapping("/phoneLogin")
    @ApiOperation(value = "手机+验证码登录", httpMethod = "POST")
    public R phoneLogin() {
        return R.ok();
    }

    @GetMapping("/sendPhoneCode")
    @ApiOperation(value = "发送手机验证码", httpMethod = "GET")
    public R sendPhoneCode() {
        return R.ok();
    }

}

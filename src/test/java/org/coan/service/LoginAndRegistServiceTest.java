package org.coan.service;

import org.coan.util.RedisCacheUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class LoginAndRegistServiceTest {

    @Autowired
    private LoginAndRegistService loginAndRegistService;

    @Test
    public void isEmailLegalTest() {
        System.out.println(loginAndRegistService.isEmailLegal("1005772685@qq.com"));
    }

    @Test
    public void createCodeTest() {
        System.out.println(loginAndRegistService.createCode());
    }

//    @Test
//    public void sendEmailCodeTest() {
//        loginAndRegistService.sendEmailCode("1005772685@qq.com", "123456");
//    }

    @Test
    public void saveCode2RedisTest() {
        loginAndRegistService.saveCode2Redis("1005772685@qq.com", "12aaa6");
    }

    @Test
    public void isEmailExistTest() {
//        RedisTemplate redisTemplate = RedisCacheUtil.redis;
//        System.out.println(redisTemplate.hasKey("1005772685@qq.com"));
        System.out.println(loginAndRegistService.isEmailExist("1005772685@qq.com"));
    }

    @Test
    public void isEmailCodeSame() {
        RedisTemplate redisTemplate = RedisCacheUtil.redis;
        redisTemplate.opsForValue().set("1005772685@qq.com", "123456",
                1, TimeUnit.MINUTES);
        System.out.println(loginAndRegistService.isEmailCodeSame(
                "1005772685@qq.com", "123"));
    }

    @Test
    public void createUserInfoTest() {
        loginAndRegistService.createUserInfo("123@163.com");
    }

    @Test
    public void selectUserByEmailTest() {
        System.out.println(loginAndRegistService.selectUserByEmail("1005772685@qq.com"));

    }
}

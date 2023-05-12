package org.coan.redis;

import org.coan.util.RedisCacheUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {

//    @Resource
//    private RedisTemplate redisTemplate;

    @Test
    void testString() {

        RedisTemplate redisTemplate = RedisCacheUtil.redis;
        //插入一条string类型数据
//        System.out.println(redisTemplate);
//        redisTemplate.opsForValue().set("name1","李四");
//        System.out.println(redisTemplate.opsForValue().get("name"));
        redisTemplate.opsForValue().set("1005772685@qq.com", "code", 5, TimeUnit.MINUTES);
//        redisTemplate.opsForValue().set("email","code");
        String code = (String) redisTemplate.opsForValue().get("1005772685@qq.com");
        System.out.println(code);
        //不存在该key的时候查出来结果为空
        System.out.println(redisTemplate.hasKey("1005772685@qq.com"));
    }

}

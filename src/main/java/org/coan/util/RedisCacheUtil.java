package org.coan.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class RedisCacheUtil {

    @Resource
    private RedisTemplate redisTemplate;

    public static RedisTemplate redis;

    @PostConstruct
    public void getRedisTemplate() {
        redis = this.redisTemplate;
    }

}

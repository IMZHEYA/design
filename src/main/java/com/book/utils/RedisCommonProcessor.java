package com.book.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisCommonProcessor {
    @Autowired
    private RedisTemplate redisTemplate;

    //通过key获取value
    public Object get(String key){
        if(key == null){
            throw new UnsupportedOperationException("Redis key could not be null");
        }
        return redisTemplate.opsForValue().get(key);
    }
    //向redis中存入k v 键值对
    public void set(String key,Object value){
        redisTemplate.opsForValue().set(key,value);
    }
    //向redis中存入k v 数据对，并支持过期时间
    public void set(String key,Object value,long time){
        if(time > 0){
            redisTemplate.opsForValue().set(key,value,time, TimeUnit.SECONDS);
        }else {
            set(key, value);
        }
    }

    //根据key删除Redis缓存数据
    public void remove(String key){
        redisTemplate.delete(key);
    }
}

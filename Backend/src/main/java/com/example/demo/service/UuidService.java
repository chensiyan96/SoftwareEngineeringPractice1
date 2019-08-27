package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UuidService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public int createUuidForEncryptionKey(int key) {
        Random ra = new Random();
        int uuid = ra.nextInt(10);
        //redisTemplate.opsForValue().set(uuid, key, 1, TimeUnit.MINUTES);
        return uuid;
    }

}

package com.example.demo.service;

import org.apache.commons.lang.RandomStringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UuidService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String createUuidForPrivateKey(String priKey) {
        String uuid;
        do {
            uuid = RandomStringUtils.randomAlphanumeric(16);
        } while (stringRedisTemplate.hasKey(genRedisKey(uuid)));
        stringRedisTemplate.opsForValue().set(genRedisKey(uuid), priKey, 6, TimeUnit.HOURS);
        return uuid;
    }

    public String getPrivateKeyByUuid(String uuid) {
        String priKey = stringRedisTemplate.opsForValue().get(genRedisKey(uuid));
        stringRedisTemplate.delete(uuid);
        return priKey;
    }

    @NotNull
    @Contract(pure = true)
    private static String genRedisKey(@NotNull String uuid){
        return "uuid" + uuid;
    }

}

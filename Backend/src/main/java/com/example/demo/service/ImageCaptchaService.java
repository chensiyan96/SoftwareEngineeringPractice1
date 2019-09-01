package com.example.demo.service;

import com.google.code.kaptcha.Producer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

@Service
public class ImageCaptchaService {

    @Autowired
    private Producer producer;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public BufferedImage createCaptcha(@NotNull String uuid) {
        String captcha_str = producer.createText();
        stringRedisTemplate.opsForValue().set(genRedisKey(uuid), captcha_str, 5, TimeUnit.MINUTES);
        return producer.createImage(captcha_str);
    }

    public boolean checkCaptcha(@NotNull String uuid, @NotNull String captcha_str) {
        boolean check_pass = captcha_str.equals(stringRedisTemplate.opsForValue().get(genRedisKey(uuid)));
        if (check_pass) {
            stringRedisTemplate.delete(genRedisKey(uuid));
        }
        return check_pass;
    }

    @NotNull
    @Contract(pure = true)
    private static String genRedisKey(@NotNull String uuid){
        return "img" + uuid;
    }

}

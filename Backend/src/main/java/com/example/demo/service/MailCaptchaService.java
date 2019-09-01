package com.example.demo.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class MailCaptchaService {

    private final String emailFrom = "register@tongshu.online";

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void sendCaptcha(@NotNull String to) {
        Random ra = new Random();
        int captcha = ra.nextInt(899999) + 100000;
        String captcha_str = Integer.toString(captcha);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("验证码");
        message.setText("您好！欢迎注册成为筒书用户，您本次注册的验证码是：" + captcha_str
                + "，十分钟内有效。如果不是您本人的操作，请忽略此邮件；请勿将验证码告诉他人。");
        message.setFrom(emailFrom);
        mailSender.send(message);
        stringRedisTemplate.opsForValue().set(to, captcha_str, 10, TimeUnit.MINUTES);
    }

    public boolean checkCaptcha(@NotNull String to, @NotNull String captcha_str) {
        boolean check_pass = captcha_str.equals(stringRedisTemplate.opsForValue().get(to));
        if (check_pass) {
            stringRedisTemplate.delete(to);
        }
        return check_pass;
    }

}



package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.util.RedisUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TokenService {

    //12小时后过期
    private final static int EXPIRE = 3600 * 12;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String createToken(int userId) {
        deleteToken(userId);

        String token = RandomStringUtils.randomAlphanumeric(32);

        String tokenKey= "token" + token;
        String userIdKey= "userId" + userId;

        redisUtils.set(tokenKey,userId,EXPIRE);
        redisUtils.set(userIdKey,token,EXPIRE);

        return token;
    }

    public Integer queryByToken(String token) {
        String userId = redisUtils.get("token" + token);
        if(StringUtils.isEmpty(userId)){
            return null;
        }
        return Integer.parseInt(userId);
    }

    public void deleteToken(Integer userId) {
        String userIdKey = "userId" + userId;
        String token = redisUtils.get(userIdKey);
        String tokenKey= "token" + token;
        redisUtils.delete(userIdKey);
        redisUtils.delete(tokenKey);
    }

    public void refreshToken(Integer userId, String token) {
        String tokenKey= "token" + token;
        String userIdKey= "userId" + userId;
        redisUtils.updateExpire(tokenKey);
        redisUtils.updateExpire(userIdKey);
    }

    public User getCurrentUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    public int getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

}

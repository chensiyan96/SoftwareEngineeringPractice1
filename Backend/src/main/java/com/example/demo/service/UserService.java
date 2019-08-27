package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.UserMapper;
import com.example.demo.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public User getUserByEmail(@NotNull String email) {
        return baseMapper.selectOne(new QueryWrapper<User>()
                .lambda()
                .eq(User::getEmail, email));
    }

    public boolean isEmailExist(@NotNull String email) {
        return baseMapper.selectOne(new QueryWrapper<User>()
                .lambda()
                .eq(User::getEmail, email)) != null;
    }

    public boolean isUsernameExist(@NotNull String username) {
        return baseMapper.selectOne(new QueryWrapper<User>()
                .lambda()
                .eq(User::getUsername, username)) != null;
    }

}

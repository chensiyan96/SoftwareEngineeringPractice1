package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogService {

    @Autowired
    private UserMapper userMapper;

    public int createUser(User src) {
        return userMapper.insert(src);
    }

    public User getUserByEmail(String email) {
//        User user = userMapper.selectOne(new QueryWrapper<User>()
//                .lambda()
//                .eq(User::getEmail, email));
        return null;
    }
}

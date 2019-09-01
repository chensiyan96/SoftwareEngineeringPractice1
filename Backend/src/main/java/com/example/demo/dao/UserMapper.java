package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    boolean isEmailExist(String email);
    boolean isUsernameExist(String username);
    List<User> recommendUsers();
    int getLikeNum(int userId);
    int getArticleNum(int userId);

}

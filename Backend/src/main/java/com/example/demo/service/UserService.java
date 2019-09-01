package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.FollowMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.model.Follow;
import com.example.demo.model.User;
import com.example.demo.model.UserVO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private FollowMapper followMapper;

    public User getUserByEmail(@NotNull String email) {
        return baseMapper.selectOne(new QueryWrapper<User>()
                .lambda()
                .eq(User::getEmail, email));
    }

    public boolean isEmailExist(@NotNull String email) {
        return baseMapper.isEmailExist(email);
    }

    public boolean isUsernameExist(@NotNull String username) {
        return baseMapper.isUsernameExist(username);
    }

    public boolean followUser(int hostId, int fansId) {
        if (followMapper.isRecordExist(hostId, fansId)) {
            return false;
        }
        followMapper.insert(new Follow(hostId, fansId));
        return true;
    }

    public boolean cancleFollow(int hostId, int fansId) {
        if (!followMapper.isRecordExist(hostId, fansId)) {
            return false;
        }
        followMapper.deleteFollowRecord(hostId, fansId);
        return true;
    }

    public List<UserVO> recommendUsers() {
        return addVO(baseMapper.recommendUsers());
    }

    public List<UserVO> getFollowUserByHostId(int hostId) {
        return addVO(followMapper.getFollowUserByHostId(hostId));
    }

    public List<UserVO> getFollowUserByFansId(int fansId) {
        return addVO(followMapper.getFollowUserByFansId(fansId));
    }

    @NotNull
    private List<UserVO> addVO(@NotNull List<User> users) {
        List<UserVO> userVOS = new ArrayList<>();
        for (User user : users) {
            int userId = user.getUserId();
            userVOS.add(new UserVO(user, baseMapper.getLikeNum(userId), baseMapper.getArticleNum(userId)));
        }
        return userVOS;
    }

}

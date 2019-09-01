package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.model.Follow;
import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

    List<User> getFollowUserByHostId(int hostId);
    List<User> getFollowUserByFansId(int fansId);
    int getFollowCountByHostId(int hostId);
    int getFollowCountByFansId(int fansId);
    boolean isRecordExist(int hostId, int fansId);
    void deleteFollowRecord(int hostId, int fansId);

}

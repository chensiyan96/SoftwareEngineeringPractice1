package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.model.Love;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoveMapper extends BaseMapper<Love> {
    int getFCountByUserId(int userId);
    int getLikeCountByArticleId(int articleId);
    boolean isRecordExist(int userId, int articleId);
}

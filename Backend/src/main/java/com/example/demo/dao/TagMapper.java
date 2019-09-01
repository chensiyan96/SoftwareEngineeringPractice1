package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.model.Tag;
import com.example.demo.model.TagVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签 Mapper 接口
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据linkId获取Tag列表
     */
    List<Tag> listByLinkId(@Param("linkId") Integer linkId);

    /**
     * 根据linkId删除多对多关联
     */
    void deleteTagLink(@Param("linkId") Integer linkId, @Param("type") Integer type);

    /**
     * 获取tagVoList
     */
    List<TagVO> listTagVo();

    List<TagVO> listByUserId(int userId);
}

package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.model.TagLink;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章标签多对多维护表 Mapper 接口
 */
@Mapper
public interface TagLinkMapper extends BaseMapper<TagLink> {

}

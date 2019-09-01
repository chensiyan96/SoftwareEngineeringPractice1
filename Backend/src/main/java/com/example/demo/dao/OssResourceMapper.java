package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.model.OssResource;
import org.apache.ibatis.annotations.Mapper;

/**
 * 云存储资源表 Mapper 接口
 */
@Mapper
public interface OssResourceMapper extends BaseMapper<OssResource> {

}
